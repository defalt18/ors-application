package oracle.demo.project.model.entities;

public class UserBean {
    private String username;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String skillSet;
    private String candidateId;

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
