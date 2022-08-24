package oracle.dev.demo.classes;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class UserData {
    private String username = "";
    private String userType = "";
    private String phoneNumber = "";
    private String experienceLevel = "";
    private String rounds = "";
    private String status = "";
    private String jobName = "";
    private String skillSet = "";
    private String firstName = "";
    private String lastName = "";
    private String candidateId = "";
    private String employeeId = "";
    
    public UserData() {}
    
    public UserData(Map details) {
        String userType = details.get("userType").toString();
        this.username = details.get("Username").toString();
        this.phoneNumber = details.get("Contact").toString();
        this.lastName = details.get("Lastname").toString();
        this.userType = userType;
        this.firstName = details.get("Firstname").toString();
        
        switch(userType) {
        case "0":
            this.candidateId = details.get("Candidateid").toString();
            this.skillSet = details.get("Skillset").toString();
            break;
        case "2":
            this.experienceLevel = details.get("Experiencelevel").toString();
            this.employeeId = details.get("Employeeid").toString();
            this.jobName = details.get("Jobname").toString();
            break;
        case "1":
        default: 
            this.rounds = details.get("Rounds").toString();
            this.status = details.get("Status").toString();
            this.experienceLevel = details.get("Experiencelevel").toString();
            this.employeeId = details.get("Employeeid").toString();
            this.jobName = details.get("Jobname").toString();
            break;
        }
    }
    
    public UserData(String username, String firstName, String lastName, String userType, String phoneNumber, String candidateId, String skillSet) {
        this.username = username;
        this.userType = userType;   
        this.phoneNumber = phoneNumber;   
        this.skillSet = skillSet;   
        this.candidateId = candidateId;   
        this.firstName = firstName;   
        this.lastName = lastName;   
    }

    @SuppressWarnings("unchecked")
    public Map getUserMap() {
        Map details = new Hashtable<>();
        details.put("Username", this.username);
        details.put("Contact", this.phoneNumber);  
        details.put("Firstname", this.firstName);  
        details.put("Lastname", this.lastName);  
        details.put("UserType", this.userType); 
        switch(this.userType) {
        case "0":
                details.put("CandidateId", this.candidateId);  
                details.put("Skillset", this.skillSet);
                break;
        case "2":
            details.put("Employeeid", this.employeeId);
            details.put("Experiencelevel", this.experienceLevel);
            details.put("Jobname", this.jobName);
            break;
        case "1":
        default:
            details.put("Employeeid", this.employeeId);
            details.put("Experiencelevel", this.experienceLevel);
            details.put("Jobname", this.jobName);
            details.put("Rounds", this.rounds);
            details.put("Status", this.status);
            break;
        }
        return details;                                                                                                  
    }
    public ArrayList<String> getUserMapKeys() {
        ArrayList<String> keys = new ArrayList<String>();
        keys.add("Username");
        keys.add("Firstname");
        keys.add("Lastname");
        keys.add("Contact");
        keys.add("UserType");
        switch(this.userType)
        {
        case "0":
            keys.add("CandidateId");
            keys.add("Skillset");
            break;
        case "2":
            keys.add("Employeeid");
            keys.add("Experiencelevel");
            keys.add("Jobname");
            break;
        default: 
            keys.add("Employeeid");
            keys.add("Experiencelevel");
            keys.add("Jobname");
            keys.add("Rounds");
            keys.add("Status");
            break;
        }
        return keys;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setRounds(String rounds) {
        this.rounds = rounds;
    }

    public String getRounds() {
        return rounds;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setSkillSet(String skillSet) {
        this.skillSet = skillSet;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserType() {
        return userType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSkillSet() {
        return skillSet;
    }

    public String getCandidateId() {
        return candidateId;
    }

}
