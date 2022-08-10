package oracle.demo.project.model.entities;

import java.util.ArrayList;

public class UserBean {
    private String username;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String skillSet;
    private String candidateId;
    private String userType;
    
    public UserBean() {}
    
    public UserBean(ArrayList<Object> details) {
        this.candidateId = details.get(0).toString();
        this.phoneNumber = details.get(1).toString();
        this.username = details.get(8).toString();
        this.firstName = details.get(2).toString();
        this.lastName = details.get(3).toString();
        this.skillSet = details.get(4).toString();
        this.userType = details.get(9).toString();
    }
    
    public UserBean(String username, String phoneNumber, String firstName, String lastName, String skillSet) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.skillSet = skillSet;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

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

    public void setSkillSet(String skillSet) {
        this.skillSet = skillSet;
    }

    public String getSkillSet() {
        return skillSet;
    }
}
