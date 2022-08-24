package oracle.dev.demo.view;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.share.ADFContext;

import oracle.dev.demo.classes.ImplResponse;
import oracle.dev.demo.model.services.ORSPortalAppModuleImpl;

import oracle.jbo.ApplicationModule;
import oracle.jbo.client.Configuration;

public class InterviewsFlow {
    private String oldCandidateStatus;
    private String candidateStatus;
    private String feedback;
    
    private String candidateId;
    private String roundNo;
    private Integer jobId;

    public void setOldCandidateStatus(String oldCandidateStatus) {
        this.oldCandidateStatus = oldCandidateStatus;
    }

    public String getOldCandidateStatus() {
        return oldCandidateStatus;
    }

    public void setCandidateStatus(String candidateStatus) {
        this.candidateStatus = candidateStatus;
    }

    public String getCandidateStatus() {
        return candidateStatus;
    }

    public InterviewsFlow() {
    }

    public void smc1_validator(FacesContext facesContext, UIComponent uIComponent, Object object) {
        // Add event code here...
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setRoundno(String roundNo) {
        this.roundNo = roundNo;
    }

    public String getRoundno() {
        return roundNo;
    }

    public ORSPortalAppModuleImpl getAppModule() {
        String amDef = "oracle.dev.demo.model.services.ORSPortalAppModule";
        String config = "ORSPortalAppModuleLocal";
        ApplicationModule ami = Configuration.createRootApplicationModule(amDef, config);
        return (ORSPortalAppModuleImpl) ami;
    }

    public void onChangeAvailability(ValueChangeEvent valueChangeEvent) {

        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();

        String oldStatus = ADFContext.getCurrent()
                                     .getSessionScope()
                                     .get("Status")
                                     .toString();
        String status = valueChangeEvent.getNewValue().toString();
        String empId = ADFContext.getCurrent()
                                 .getSessionScope()
                                 .get("Employeeid")
                                 .toString();
        ORSPortalAppModuleImpl am = this.getAppModule();
        ImplResponse response = am.getAndChangeStatus(empId, status);

        if (response.getError() == null)
            ADFContext.getCurrent()
                      .getSessionScope()
                      .replace("Status", oldStatus, status);
        else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getError().toString(), "");
            context.addMessage("", message);
        }
    }

    public String onClickChangeStatus(String cid, Integer jobid, String round) {

        Map sessionScope = ADFContext.getCurrent().getSessionScope();
        sessionScope.put("backing_Cid", cid);
        sessionScope.put("backing_jobid", jobid);
        sessionScope.put("backing_round", round);
        
        return null;
    }

    public void raiseError(String errorMessage) {
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();

        message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "");
        context.addMessage("", message);
    }
    
    public void onChangeCandidateStatus(ValueChangeEvent valueChangeEvent) {
        String status = valueChangeEvent.getNewValue().toString();
        this.candidateStatus = status;
    }

    public String onUpdateStatus() {
        System.out.print("Entered");
        ORSPortalAppModuleImpl am = this.getAppModule();
        String empId = ADFContext.getCurrent().getSessionScope().get("Employeeid").toString();
        String Cid = ADFContext.getCurrent().getSessionScope().get("backing_Cid").toString();
        String jobid = ADFContext.getCurrent().getSessionScope().get("backing_jobid").toString();
        String round = ADFContext.getCurrent().getSessionScope().get("backing_round").toString();

        ImplResponse response = am.changeRoundStatus(Cid, empId, jobid, Integer.parseInt(round), feedback, candidateStatus);
        if(response.getError() != null) raiseError(response.getError().toString());
        
        ADFContext.getCurrent().getSessionScope().remove("backing_Cid");
        ADFContext.getCurrent().getSessionScope().remove("backing_jobid");
        ADFContext.getCurrent().getSessionScope().remove("backing_round");
        
        return response.getRedirect() != null ? response.getRedirect() : "goToInterviews";
    }
    
    public String onRequestInterview(Integer jobId) {
        System.out.print("Entered");
        ORSPortalAppModuleImpl am = this.getAppModule();
        
        ImplResponse response = am.requestInterviewForCid(ADFContext.getCurrent().getSessionScope().get("CandidateId").toString(), jobId.toString());
        if (response.getError() != null) {
            raiseError(response.getError().toString());
            return response.getRedirect();
        }
        return "goToInterviews";
    }
}
