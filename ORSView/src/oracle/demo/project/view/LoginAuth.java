package oracle.demo.project.view;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import oracle.demo.project.model.beans.UserData;
import oracle.demo.project.model.entities.ORSAppModuleImpl;

import oracle.jbo.ApplicationModule;
import oracle.jbo.client.Configuration;

public class LoginAuth {
    
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
        String amDef = "oracle.demo.project.model.entities.ORSAppModule";
        String config = "ORSAppModuleLocal";
        ApplicationModule ami = Configuration.createRootApplicationModule(amDef, config);
        ORSAppModuleImpl am = (ORSAppModuleImpl) ami;
        UserData user = am.validateLoginCredentials(username, password);
        if (user == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User Not Found", "");
            context.addMessage("", message);

        } else {
            return "goToDashboard";
        }
        return null;
    }
}
