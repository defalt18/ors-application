package oracle.dev.demo.view;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;

import oracle.dev.demo.classes.ImplResponse;
import oracle.dev.demo.classes.UserData;
import oracle.dev.demo.model.services.ORSPortalAppModuleImpl;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.client.Configuration;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class JobPortalFlow {
    private Integer jobId;
    private Boolean appliedFlag;

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setAppliedFlag(Boolean appliedFlag) {
        this.appliedFlag = appliedFlag;
    }

    public Boolean getAppliedFlag() {
        return appliedFlag;
    }

    public JobPortalFlow() {
    }

    public ORSPortalAppModuleImpl getAppModule() {
        String amDef = "oracle.dev.demo.model.services.ORSPortalAppModule";
        String config = "ORSPortalAppModuleLocal";
        ApplicationModule ami = Configuration.createRootApplicationModule(amDef, config);
        return (ORSPortalAppModuleImpl) ami;
    }

    public UserData fillUserFromSessionScope() {
        UserData user = new UserData();
        Map sessionScope = ADFContext.getCurrent().getSessionScope();
        if (sessionScope.get("UserType") == null)
            return null;

        user.setUsername(sessionScope.get("Username").toString());
        user.setFirstName(sessionScope.get("Firstname").toString());
        user.setLastName(sessionScope.get("Lastname").toString());
        user.setPhoneNumber(sessionScope.get("Contact").toString());
        user.setUserType(sessionScope.get("UserType").toString());
        switch (user.getUserType()) {
        case "0":
            user.setCandidateId(sessionScope.get("CandidateId").toString());
            user.setSkillSet(sessionScope.get("Skillset").toString());
            break;
        case "2":
            user.setEmployeeId(sessionScope.get("Employeeid").toString());
            user.setJobName(sessionScope.get("Jobname").toString());
            user.setExperienceLevel(sessionScope.get("Experiencelevel").toString());
            break;
        default:
            user.setRounds(sessionScope.get("Rounds").toString());
            user.setStatus(sessionScope.get("Status").toString());
            user.setEmployeeId(sessionScope.get("Employeeid").toString());
            user.setJobName(sessionScope.get("Jobname").toString());
            user.setExperienceLevel(sessionScope.get("Experiencelevel").toString());
            break;
        }

        return user;
    }

    public void raiseError(String errorMessage) {
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();

        message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "");
        context.addMessage("", message);
    }


    public String onApply(Integer id) {
        setJobId(id);

        Boolean applied = false;

        UserData user = fillUserFromSessionScope();

        if (user != null) {
            ImplResponse response = new ImplResponse();
            ORSPortalAppModuleImpl am = this.getAppModule();
            if(user.getUserType().equals("0"))
                response = am.checkApplicationStatus(jobId.toString(), user.getCandidateId());
            applied = user.getUserType().equals("0") ? (Boolean)response.getResponse(): false;
            System.out.println("Applied : " + applied);
            
            ADFContext.getCurrent().getPageFlowScope().put("appliedJobId", id.toString());
        } else return "goToLogin";

        setAppliedFlag(applied);
        System.out.println(getAppliedFlag());

        Row currentItem = null;

        DCBindingContainer bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding itorBinding = bindings.findIteratorBinding("JobsVOIterator"); //ur iterator name

        if (itorBinding != null) {
            currentItem = itorBinding.getCurrentRow();
            while (currentItem != null) {
                if (Integer.parseInt(currentItem.getAttribute("Jobid").toString()) == jobId) {
                    break;
                }
                currentItem = itorBinding.getNavigatableRowIterator().next();
            }
        }
        return null;
    }

    public String onSubmit(Integer id) {
        String PAGE = "goToLogin";
        setJobId(id);
        System.out.print("Applied for : " + jobId);
        UserData user = fillUserFromSessionScope();

        ADFContext.getCurrent()
                  .getPageFlowScope()
                  .put("routedFrom", PAGE);
        ADFContext.getCurrent()
                  .getSessionScope()
                  .put("Jobid", id.toString());
        if (user != null) {
            ORSPortalAppModuleImpl am = this.getAppModule();
            ImplResponse response1 = am.checkApplicationStatus(jobId.toString(), user.getCandidateId());
            if (response1.getError() != null || (Boolean)response1.getResponse() == true) {
                if(response1.getError() != null)
                    raiseError(response1.getError().toString());
                else
                    raiseError("You have already applied for this job!");
                return null;
            }
            ImplResponse response =
                am.applyForJobWithId(id.toString(), user.getCandidateId(),
                                     user.getFirstName() + " " + user.getLastName());
            if (response.getError() != null) {
                raiseError(response.getError().toString());
                return null;
            }
            PAGE = response.getResponse().toString();
            ADFContext.getCurrent()
                      .getSessionScope()
                      .put("routedFrom", "goToPortal");
        }
        return PAGE;
    }
    
    public static void executeClientJavascript(String script) {
     FacesContext facesContext = FacesContext.getCurrentInstance();
     ExtendedRenderKitService service = Service.getRenderKitService(facesContext, ExtendedRenderKitService.class);
     service.addScript(facesContext, script);
    }
    
    public String onClickPrint() {
     this.executeClientJavascript("printDiv();");
     return null;
    }
}
