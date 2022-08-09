package oracle.demo.project.model.services;

import java.text.SimpleDateFormat;
import java.util.UUID;

import oracle.demo.project.model.entities.UserBean;
import oracle.demo.project.model.entities.UserData;

import oracle.jbo.Row;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaRow;
import oracle.jbo.ViewObject;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.ViewObjectImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Fri Aug 05 12:12:32 IST 2022
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class ORSAppModuleImpl extends ApplicationModuleImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public ORSAppModuleImpl() {
    }

    /**
     * Container's getter for CandidatesVO.
     * @return CandidatesVO
     */
    public ViewObjectImpl getCandidatesVO() {
        return (ViewObjectImpl) findViewObject("CandidatesVO");
    }

    /**
     * Container's getter for CredsVO.
     * @return CredsVO
     */
    public ViewObjectImpl getCredsVO() {
        return (ViewObjectImpl) findViewObject("CredsVO");
    }

    /**
     * Container's getter for EmployeesVO.
     * @return EmployeesVO
     */
    public ViewObjectImpl getEmployeesVO() {
        return (ViewObjectImpl) findViewObject("EmployeesVO");
    }

    /**
     * Container's getter for JobsVO1.
     * @return JobsVO1
     */
    public ViewObjectImpl getJobsVO() {
        return (ViewObjectImpl) findViewObject("JobsVO");
    }

    public UserData validateLoginCredentials(String username, String password) {
        UserData user = null;
        ViewObject vo = getCredsVO();
        ViewCriteria vc = vo.createViewCriteria();
        ViewCriteriaRow vcr = vc.createViewCriteriaRow();
        vcr.setAttribute("Username", username);
        vcr.setAttribute("Password", password);
        vc.add(vcr);
        vo.applyViewCriteria(vc);
        vo.executeQuery();
        if (vo.hasNext()) {
            user = new UserData();
            Row row = vo.next();
            user.setUsername(row.getAttribute("Username").toString());
        }
        return user;
    }

    public boolean checkIfUserAlreadyExist(String username) {
        UserData user = validateLoginCredentials(username, "");
        if (user == null)
            return false;
        return true;
    }

    public String registerUser(String firstName, String lastName, String phoneNumber, String username, String password,
                               String skillSet) {
        ViewObject credsVO = getCredsVO();
        ViewObject candidateVO = getCandidatesVO();

        if (checkIfUserAlreadyExist(username))
            return "user already exists";

        Row newCreds = credsVO.createRow();
        newCreds.setAttribute("Username", username);
        newCreds.setAttribute("Password", password);
        newCreds.setAttribute("UserType", 0);
        credsVO.insertRow(newCreds);

        Row newUserDetail = candidateVO.createRow();

        String candidateId = UUID.randomUUID().toString();
        newUserDetail.setAttribute("Username", username);
        newUserDetail.setAttribute("Firstname", firstName);
        newUserDetail.setAttribute("Lastname", lastName);
        newUserDetail.setAttribute("Contact", phoneNumber);
        newUserDetail.setAttribute("Skillset", skillSet);
        newUserDetail.setAttribute("Candidateid", candidateId);
        candidateVO.insertRow(newUserDetail);

        credsVO.executeQuery();
        candidateVO.executeQuery();

        this.getDBTransaction().commit();

        return "success";
    }

    public String applyForJobWithId(String jobId, UserBean user) {
        
        ViewObject applicationsVO = getApplicationsVO();
        
        try {
            Row newApplication = applicationsVO.createRow();
            newApplication.setAttribute("Applicationstatus", "in progress");
            newApplication.setAttribute("Appliedon", new SimpleDateFormat("yyyy-MM-dd"));
            newApplication.setAttribute("Candidateid", user.getCandidateId());
            newApplication.setAttribute("Jobid", jobId);

            applicationsVO.insertRow(newApplication);
            applicationsVO.executeQuery();

            this.getDBTransaction().commit();
        } catch (Exception e) {
            System.out.print(e);
        }
        return "goToAck";
    }

    /**
     * Container's getter for QuestionsVO1.
     * @return QuestionsVO1
     */
    public ViewObjectImpl getQuestionsVO() {
        return (ViewObjectImpl) findViewObject("QuestionsVO");
    }

    /**
     * Container's getter for ApplicationsVO1.
     * @return ApplicationsVO1
     */
    public ViewObjectImpl getApplicationsVO() {
        return (ViewObjectImpl) findViewObject("ApplicationsVO");
    }
}

