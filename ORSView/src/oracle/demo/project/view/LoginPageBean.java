package oracle.demo.project.view;

import java.util.Map;

import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;

import oracle.adf.share.ADFContext;

import oracle.demo.project.model.entities.UserBean;
import oracle.demo.project.model.services.ORSAppModuleImpl;
import oracle.jbo.ApplicationModule;
import oracle.jbo.client.Configuration;
public class LoginPageBean {
    private String username;
    private String password;
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    
    public String onLogin() {
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        if (username == null || password == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "username or password is missing", "");
            context.addMessage("", message);
            return null;
        }
        String amDef = "oracle.demo.project.model.services.ORSAppModule";
        String config = "ORSAppModuleLocal";
        ApplicationModule ami = Configuration.createRootApplicationModule(amDef, config);
        ORSAppModuleImpl am = (ORSAppModuleImpl) ami;
        UserBean user = am.getUserWithCredentials(username, password);

        if (user == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User Not Found", "");
            context.addMessage("", message);
        } else {
            String action;
            Map userMap = user.getUserMap();
            ADFContext.getCurrent().getSessionScope().putAll(userMap);
            switch(user.getUserType())
            {
                case "1":
                    action = "goToEmployeeDashboard";
                    break;
                case "0":
                    action = "goToCandidateDashboard";
                    break;
                case "2":
                    action = "goToHRDashboard";
                    break;
                default:
                    action = "goToCandidateDashboard";
                    break;
            }
            return action;
        }
        return null;
    }
}
