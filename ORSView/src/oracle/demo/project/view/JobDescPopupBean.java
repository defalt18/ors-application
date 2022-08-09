package oracle.demo.project.view;

import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.demo.project.model.entities.UserBean;
import oracle.demo.project.model.services.ORSAppModuleImpl;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
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
        System.out.println("Entered OnApply Method!");
        setJobId(id);
        System.out.println("Success JobId: "+jobId);
        
        Row currentItem = null;
        ViewObject jobVO;
                
        DCBindingContainer bindings = this.getDCBindingContainer();
        DCIteratorBinding itorBinding = bindings.findIteratorBinding("JobsVOIterator");//ur iterator name
       
       int it = 0;
        if(itorBinding!= null) {
            currentItem = itorBinding.getCurrentRow();
            while(currentItem != null) {
                if(Integer.parseInt(currentItem.getAttribute("Jobid").toString()) == jobId) {
                    break;
                }
                currentItem = itorBinding.getNavigatableRowIterator().next();
            }
        }
        
        System.out.println(currentItem);
        System.out.println("Exiting OnApply Method!");
//        System.out.println(rsi);
        System.out.println(currentItem.getAttribute("Jobid"));
    }
    
    public DCBindingContainer getDCBindingContainer() {
            DCBindingContainer bindingsContainer =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            return bindingsContainer;
    }

    public String onSubmit() {
        String PAGE = "goToLogin";
        System.out.print("Applied for : " + jobId);
        String amDef = "oracle.demo.project.model.services.ORSAppModule";
        String config = "ORSAppModuleLocal";
        UserBean user = (UserBean)(ADFContext.getCurrent().getSessionScope().get("user"));
        if(user != null) {
            ApplicationModule ami = Configuration.createRootApplicationModule(amDef, config);
            ORSAppModuleImpl am = (ORSAppModuleImpl) ami;
            PAGE = am.applyForJobWithId(jobId.toString(), user);
        }
        return PAGE;
    }

}
