package oracle.demo.project.view;

import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;

import oracle.demo.project.model.entities.UserBean;
import oracle.demo.project.model.services.ORSAppModuleImpl;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;

import oracle.jbo.ViewObject;
import oracle.jbo.client.Configuration;

public class JobDescPopupBean {
    
    private Integer jobId;
    //private RichPopup jobApplicationPopup;

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getJobId() {
        return jobId;
    }
    
//    public void setJobApplicationPopup(RichPopup jobApplicationPopup) {
//        this.jobApplicationPopup = jobApplicationPopup;
//    }
//
//    public RichPopup getJobApplicationPopup() {
//        return jobApplicationPopup;
//    }
//    
    public void onApply(int id) {
        
        setJobId(id);
        
        Row currentItem = null;
                
        DCBindingContainer bindings = this.getDCBindingContainer();
        DCIteratorBinding itorBinding = bindings.findIteratorBinding("JobsVOIterator");//ur iterator name
       
        if(itorBinding!= null) {
            currentItem = itorBinding.getCurrentRow();
            while(currentItem != null) {
                if(Integer.parseInt(currentItem.getAttribute("Jobid").toString()) == jobId) {
                    break;
                }
                currentItem = itorBinding.getNavigatableRowIterator().next();
            }
        }
        
    }
    
    public DCBindingContainer getDCBindingContainer() {
            DCBindingContainer bindingsContainer =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            return bindingsContainer;
    }

    public String onSubmit(int id) {
        String PAGE = "goToLogin";
        setJobId(id);
        System.out.print("Applied for : " + jobId);
        String amDef = "oracle.demo.project.model.services.ORSAppModule";
        String config = "ORSAppModuleLocal";
        UserBean user = (UserBean)(ADFContext.getCurrent().getSessionScope().get("user"));
        ADFContext.getCurrent().getPageFlowScope().put("routedFrom", PAGE);
        if(user != null) {
            ApplicationModule ami = Configuration.createRootApplicationModule(amDef, config);
            ORSAppModuleImpl am = (ORSAppModuleImpl) ami;
            PAGE = am.applyForJobWithId(jobId.toString(), user);
            ADFContext.getCurrent().getPageFlowScope().put("routedFrom", "goToPortal");
        }
        return PAGE;
    }
    

}
