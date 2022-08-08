package oracle.demo.project.view;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.faces.event.ValueChangeEvent;

import oracle.demo.project.model.services.ORSAppModuleImpl;

import oracle.jbo.ApplicationModule;
import oracle.jbo.client.Configuration;

public class SignUpBean {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String username;
    private String password;
    private String skillSet;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

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

    public void setSkillSet(String skillSet) {
        this.skillSet = skillSet;
    }

    public String getSkillSet() {
        return skillSet;
    }

    public String onRegister() {
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
        String status = am.registerUser(firstName, lastName, phoneNumber, username, password, skillSet.substring(1, skillSet.length() - 1));

        if (status == "user already exists") {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "username already exists", "");
            context.addMessage("", message);
            return null;
        }

        return "goToDashboard";
    }

    public void onChangeSelect(ValueChangeEvent valueChangeEvent) {
        this.skillSet = valueChangeEvent.getNewValue().toString();
    }

}
