package oracle.dev.demo.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.share.ADFContext;

import oracle.dev.demo.classes.ImplResponse;
import oracle.dev.demo.classes.UserData;
import oracle.dev.demo.model.services.ORSPortalAppModuleImpl;

import oracle.jbo.ApplicationModule;
import oracle.jbo.client.Configuration;

public class SignUpFlow {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String username;
    private String password;
    private String skillSet = "NA";

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

    public SignUpFlow() {
    }

    public void usernameValidator(FacesContext facesContext, UIComponent uIComponent, Object object) {
        // Add event code here...


    }

    public void contactValidator(FacesContext facesContext, UIComponent uIComponent, Object object) {
        // Add event code here...



    }

    public void firstNameValidator(FacesContext facesContext, UIComponent uIComponent, Object object) {
        // Add event code here...



    }

    public void lastNameValidator(FacesContext facesContext, UIComponent uIComponent, Object object) {
        // Add event code here...



    }

    public void onChangeSkills(ValueChangeEvent valueChangeEvent) {
        this.skillSet = valueChangeEvent.getNewValue().toString();
    }

    public void signUpValidator(FacesContext facesContext, UIComponent uIComponent, Object object) {
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

    public Boolean isEmpty(String item) {
        return item == null || item.trim().equals("");
    }

    public Boolean validationError(String item) {
        return !item.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");
    }

    public Boolean validatePhoneNumber(String number) {
        Pattern ptrn = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        Matcher match = ptrn.matcher(number);

        return !(match.find() && match.group().equals(number));
    }

    public Boolean processErrors() {

        Boolean hasErrors = false;

        if (isEmpty(username) || isEmpty(password) || isEmpty(firstName) || isEmpty(lastName) || isEmpty(phoneNumber)) {
            hasErrors = true;
            if (isEmpty(username)) {
                raiseError("Please enter a username");
                return hasErrors;
            }
            if (isEmpty(password)) {
                raiseError("Please enter a password");
                return hasErrors;
            }
            if (isEmpty(firstName)) {
                raiseError("Please enter your first name");
                return hasErrors;
            }
            if (isEmpty(lastName)) {
                raiseError("Please enter your lastname");
                return hasErrors;
            }

            raiseError("Please enter your phoneNumber");
            return hasErrors;
        }

        if (password.length() < 6) {
            hasErrors = true;
            raiseError("Please enter a password with atleast 6 characters");
            return hasErrors;
        }

        if (username.length() < 6) {
            hasErrors = true;
            raiseError("Username should have more than 6 characters.");
            return hasErrors;
        }

        if (validationError(firstName)) {
            hasErrors = true;
            raiseError("Please enter your first name in a valid format");
            return hasErrors;
        }

        if (validationError(lastName)) {
            hasErrors = true;
            raiseError("Please enter last name in a valid format");
            return hasErrors;
        }

        if (validatePhoneNumber(phoneNumber)) {
            hasErrors = true;
            raiseError("Please enter a valid contact number");
        }
        return hasErrors;
    }

    public String onRegister() {

        if (processErrors())
            return null;

        ORSPortalAppModuleImpl am = this.getAppModule();
        ImplResponse response =
            am.registerUser(firstName, lastName, phoneNumber, username, password,
                            isEmpty(skillSet) ? "Not Available" : skillSet.substring(1, skillSet.length() - 1));
        
        if (response.getError() != null) {
            raiseError(response.getError().toString());
            return null;
        }
        
        UserData user = (UserData) response.getResponse();
        
        System.out.print(user);

        if (user == null) {
            raiseError("User with the entered details already exist");
            return null;
        }

        ADFContext.getCurrent()
                  .getSessionScope()
                  .putAll(user.getUserMap());
        return "goToDashboard";
    }
}
