package oracle.dev.demo.view;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import oracle.adf.share.ADFContext;

import oracle.dev.demo.classes.ImplResponse;
import oracle.dev.demo.classes.UserData;
import oracle.dev.demo.model.services.ORSPortalAppModuleImpl;

import oracle.jbo.ApplicationModule;
import oracle.jbo.client.Configuration;

public class LoginFlow {
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

    public LoginFlow() {
    }

    public void usernameValidator(FacesContext facesContext, UIComponent uIComponent, Object object) {
        // Add event code here...


    }

    public void passwordValidator(FacesContext facesContext, UIComponent uIComponent, Object object) {
        // Add event code here...


    }

    public void raiseError(String errorMessage) {
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();

        message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "");
        context.addMessage("", message);
    }

    public ORSPortalAppModuleImpl getAppModule() {
        String amDef = "oracle.dev.demo.model.services.ORSPortalAppModule";
        String config = "ORSPortalAppModuleLocal";
        ApplicationModule ami = Configuration.createRootApplicationModule(amDef, config);
        return (ORSPortalAppModuleImpl) ami;
    }

    public String onLoginClick() {

        if (username == null || password == null) {
            raiseError("Username or password is missing");
            return null;
        }

        ORSPortalAppModuleImpl am = this.getAppModule();
        ImplResponse response = am.getUserWithCredentials(username, password);

        if (response.getError() != null) {
            raiseError(response.getError().toString());
            return null;
        }

        UserData user = (UserData) response.getResponse();
        if (user == null) {
            raiseError("User Not Found");
        } else {
            Map userMap = user.getUserMap();
            ADFContext.getCurrent()
                      .getSessionScope()
                      .putAll(userMap);
            return "goToDashboard";
        }
        return null;
    }
}
