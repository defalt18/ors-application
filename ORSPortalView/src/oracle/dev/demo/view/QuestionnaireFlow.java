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

public class QuestionnaireFlow {
    public QuestionnaireFlow() {
    }
    
    public UserData fillUserFromSessionScope() {
        UserData user = new UserData();
        Map sessionScope = ADFContext.getCurrent().getSessionScope();
        if(sessionScope.get("UserType") == null) return null;
        
        user.setUsername(sessionScope.get("Username").toString());
        user.setFirstName(sessionScope.get("Firstname").toString());
        user.setLastName(sessionScope.get("Lastname").toString());
        user.setPhoneNumber(sessionScope.get("Contact").toString());
        user.setUserType(sessionScope.get("UserType").toString());
        switch(user.getUserType())
        {
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
    
    public ORSPortalAppModuleImpl getAppModule() {
        String amDef = "oracle.dev.demo.model.services.ORSPortalAppModule";
        String config = "ORSPortalAppModuleLocal";
        ApplicationModule ami = Configuration.createRootApplicationModule(amDef, config);
        return (ORSPortalAppModuleImpl) ami;
    }
    
    public void raiseError(String errorMessage) {
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();

        message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "");
        context.addMessage("", message);
    }

    public String onSubmit() {
        
        Integer score = 0, numRows = 0;
        Row currentItem = null;
        
        Map sessionScope = ADFContext.getCurrent().getSessionScope();

        DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();;
        DCIteratorBinding itorBinding = bindings.findIteratorBinding("QuestionsVOIterator");

        if (itorBinding != null) {
            Row[] allRows = itorBinding.getAllRowsInRange();
            numRows = allRows.length;
            
            for(int i = 0; i < allRows.length; i++) {
                currentItem = allRows[i];
                String marked = currentItem.getAttribute("Choices").toString();
                String answer = currentItem.getAttribute("Correct_Response").toString();
                answer = convertType(answer);
                if (marked.equals(answer))
                    score++;
            }
        }
        
        String status = "Completed";
        if(numRows == 0)
            status = "Completed";
        else
            status = ((Float)(Float.parseFloat(score.toString())/numRows) * 100 >= 60 ? "Completed" : "Rejected");

        UserData user = fillUserFromSessionScope();
        
        if (user != null) {
            ORSPortalAppModuleImpl am = this.getAppModule();
            ImplResponse response = am.addInterviewRoundStatus(status, user, sessionScope.get("Jobid").toString(), score);
            if (response.getError() != null) {
                raiseError(response.getError().toString());
                return response.getRedirect();
            }
        }
        return "goToDashboard";
    }

    public String convertType(String tochange) {
        switch (tochange) {
        case "1":
            return "A";
        case "2":
            return "B";
        case "3":
            return "C";
        case "4":
            return "D";

        }
        return "";
    }
}
