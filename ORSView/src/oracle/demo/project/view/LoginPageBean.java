package oracle.demo.project.view;

import java.util.Map;

import javax.faces.application.FacesMessage;
//import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import oracle.adf.share.ADFContext;

import oracle.demo.project.model.entities.UserBean;
//import oracle.demo.project.model.entities.UserData;
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
        UserBean user = am.validateLoginCredentials(username, password);
        
        if (user == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User Not Found", "");
            context.addMessage("", message);
        } else {
            ADFContext.getCurrent().getSessionScope().put("user", user);
            String previousPage = ADFContext.getCurrent().getPageFlowScope().get("routedFrom").toString();
            return ((previousPage == null) ? previousPage : "goToPortal");
        }
        return null;
    }
}