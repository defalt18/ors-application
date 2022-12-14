package oracle.dev.demo.model.services;

import java.sql.Timestamp;

import oracle.dev.demo.classes.UserData;
import oracle.dev.demo.classes.ImplResponse;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import oracle.dev.demo.model.views.readonly.ChoicesVOImpl;

import oracle.jbo.Row;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaItem;
import oracle.jbo.ViewCriteriaManager;
import oracle.jbo.ViewCriteriaRow;
import oracle.jbo.ViewObject;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.ViewLinkImpl;
import oracle.jbo.server.ViewObjectImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Fri Aug 19 16:24:59 IST 2022
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class ORSPortalAppModuleImpl extends ApplicationModuleImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public ORSPortalAppModuleImpl() {
    }

    /**
     * Container's getter for ApplicationsVO.
     * @return ApplicationsVO
     */
    public ViewObjectImpl getApplicationsVO() {
        return (ViewObjectImpl) findViewObject("ApplicationsVO");
    }

    /**
     * Container's getter for CandidateHistoriesVO.
     * @return CandidateHistoriesVO
     */
    public ViewObjectImpl getCandidateHistoriesVO() {
        return (ViewObjectImpl) findViewObject("CandidateHistoriesVO");
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
     * Container's getter for InterviewProcessesVO.
     * @return InterviewProcessesVO
     */
    public ViewObjectImpl getInterviewProcessesVO() {
        return (ViewObjectImpl) findViewObject("InterviewProcessesVO");
    }

    /**
     * Container's getter for JobsVO.
     * @return JobsVO
     */
    public ViewObjectImpl getJobsVO() {
        return (ViewObjectImpl) findViewObject("JobsVO");
    }

    /**
     * Container's getter for QuestionsVO.
     * @return QuestionsVO
     */
    public ViewObjectImpl getQuestionsVO() {
        return (ViewObjectImpl) findViewObject("QuestionsVO");
    }

    public ImplResponse getAndChangeStatus(String empId, String status) {

        ImplResponse response = new ImplResponse();

        if (empId == null || status == null) {
            response.setError(new Exception("Arguments are empty!"));
            return response;
        }

        ViewObject vo = getEmployeesVO();
        ViewObject interviewsVO = getInterviewProcessesVO();

        ViewCriteria vc = vo.createViewCriteria();
        ViewCriteriaRow vcr = vc.createViewCriteriaRow();

        ViewCriteria interviewsVC = interviewsVO.createViewCriteria();
        ViewCriteriaRow interviewsVCR = interviewsVC.createViewCriteriaRow();

        try {
            vcr.setAttribute("Employeeid", empId);
            vc.add(vcr);
            vo.applyViewCriteria(vc);
            vo.executeQuery();
        } catch (Exception e) {
            response.setError(e);
            return response;
        }

        try {
            if (vo.hasNext()) {
                Row row = vo.next();
                row.setAttribute("Status", status);

                if (status == "Not Available") {

                    interviewsVCR.setAttribute("Employeeid", empId);
                    interviewsVCR.setAttribute("Status", "In progress");

                    interviewsVC.add(interviewsVCR);
                    interviewsVO.applyViewCriteria(interviewsVC);
                    interviewsVO.executeQuery();

                    if (interviewsVO.hasNext()) {
                        response.setError(new Exception("Finish in progress interviews before setting availability"));
                        return response;
                    }
                }
            } else {
                response.setError(new Exception("No employee found!"));
                return response;
            }
        } catch (Exception e) {
            response.setError(e);
            return response;
        }

        response.setResponse("success");

        vo.executeQuery();
        this.getDBTransaction().commit();

        return response;
    }

    public ImplResponse validateLoginCredentials(String username, String password) {

        ImplResponse response = new ImplResponse();
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
            user.setUserType(row.getAttribute("UserType").toString());
        }

        response.setResponse(user);
        return response;
    }

    public boolean checkIfUserAlreadyExist(String username) {
        UserData user = (UserData) validateLoginCredentials(username, "").getResponse();
        if (user == null)
            return false;
        return true;
    }

    public ImplResponse getUserWithCredentials(String username, String password) {

        ImplResponse response = new ImplResponse();
        UserData user = null;
        user = (UserData) validateLoginCredentials(username, password).getResponse();

        if (user == null)
            return response;

        ViewObject vo = null;

        if (user.getUserType().equals("0"))
            vo = getCandidatesVO();
        else
            vo = getEmployeesVO();

        ViewCriteria vc = vo.createViewCriteria();
        ViewCriteriaRow vcr = vc.createViewCriteriaRow();
        vcr.setAttribute("Username", username);
        vc.add(vcr);
        vo.applyViewCriteria(vc);

        vo.executeQuery();

        if (vo.hasNext()) {
            Row row = vo.next();
            Map details = new Hashtable();
            String[] keys = row.getAttributeNames();

            for (int i = 0; i < keys.length; i++) {

                try {
                    details.put(keys[i], row.getAttribute(keys[i]).toString());
                } catch (Exception e) {
                    System.out.println("[Warning: Adding" + keys[i] + " ] : " + e);
                }

            }

            details.put("userType", user.getUserType());

            user = new UserData(details);
        } else {
            response.setError(new Exception("Some error occured"));
            return response;
        }

        response.setResponse(user);

        return response;
    }


    public ImplResponse registerUser(String firstName, String lastName, String phoneNumber, String username,
                                     String password, String skillSet) {

        ImplResponse response = new ImplResponse();
        ViewObject credsVO = getCredsVO();
        ViewObject candidateVO = getCandidatesVO();

        if (checkIfUserAlreadyExist(username)) {
            response.setError(new Exception("User already exists"));
            return response;
        }

        Row newCreds = credsVO.createRow();
        Row newUserDetail = candidateVO.createRow();
        try {

            newCreds.setAttribute("Username", username);
            newCreds.setAttribute("Password", password);
            newCreds.setAttribute("UserType", 0);
            credsVO.insertRow(newCreds);

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

        } catch (Exception e) {
            response.setError(e);
            return response;
        }

        Map details = new Hashtable<>();
        String[] keys = newUserDetail.getAttributeNames();

        for (int i = 0; i < keys.length; i++) {
            try {
                details.put(keys[i], newUserDetail.getAttribute(keys[i]).toString());
            } catch (Exception e) {
                System.out.println("[Warning: Adding" + keys[i] + " ] : " + e);
            }
        }

        details.put("userType", "0");

        UserData user = new UserData(details);

        response.setResponse(user);
        return response;
    }

    public ImplResponse checkApplicationStatus(String jobId, String candidateId) {
        ImplResponse response = new ImplResponse();
        ViewObject AppVO = getApplicationsVO();

        try {
            AppVO.setWhereClause("candidateId = '" + candidateId + "' AND jobid = " + jobId);
            AppVO.executeQuery();

            response.setResponse(AppVO.hasNext());

        } catch (Exception e) {
            System.out.print("[Error: checkApplicationStatus] : " + e);
            response.setError(e);
            return response;
        }

        return response;
    }

    public ImplResponse applyForJobWithId(String jobId, String candidateId, String candidateName) {

        ImplResponse response = new ImplResponse();
        ViewObject applicationsVO = getApplicationsVO();
        ViewObject interviewProcessVO = getInterviewProcessesVO();
        ViewObject candidateHistoryVO = getCandidateHistoriesVO();

        String jobName = null;

        ViewObject jobsVO = getJobsVO();
        ViewCriteria vc = jobsVO.createViewCriteria();
        ViewCriteriaRow vcr = vc.createViewCriteriaRow();

        vcr.setAttribute("Jobid", jobId);
        vc.add(vcr);
        jobsVO.applyViewCriteria(vc);
        jobsVO.executeQuery();

        try {

            if (jobsVO.hasNext()) {
                Row row = jobsVO.next();
                jobName = row.getAttribute("Jobname").toString();
                Integer candidatesAlreadyApplied =
                    Integer.parseInt(row.getAttribute("Noofcandidatesapplied").toString());
                Integer vacancies = Integer.parseInt(row.getAttribute("Noofvacancies").toString());
                if (vacancies == 0)
                    throw new Exception("Sorry this job has no vacancies");
                row.setAttribute("Noofcandidatesapplied", candidatesAlreadyApplied + 1);
            } else {
                response.setError(new Exception("No jobs with Job id " + jobId + " found"));
                return response;
            }
        } catch (Exception e) {
            response.setError(e);
            return response;
        }

        try {
            Timestamp sqlTimestamp = new Timestamp(System.currentTimeMillis());
            Row newApplication = applicationsVO.createRow();
            newApplication.setAttribute("Applicationstatus", "in progress");
            newApplication.setAttribute("Appliedon", sqlTimestamp);
            newApplication.setAttribute("Candidateid", candidateId);
            newApplication.setAttribute("Jobid", jobId);

            applicationsVO.insertRow(newApplication);

            Row newInterview = interviewProcessVO.createRow();
            newInterview.setAttribute("Candidateid", candidateId);
            newInterview.setAttribute("Candidatename", candidateName);
            newInterview.setAttribute("Employeeid", "101102");
            newInterview.setAttribute("Jobid", jobId);
            newInterview.setAttribute("Round", 1);
            newInterview.setAttribute("Jobname", jobName);
            newInterview.setAttribute("Scheduledon", sqlTimestamp);
            newInterview.setAttribute("Status", "Pending");

            interviewProcessVO.insertRow(newInterview);

            Row newHistoryItem = candidateHistoryVO.createRow();
            newHistoryItem.setAttribute("Candidateid", candidateId);
            newHistoryItem.setAttribute("Feedback", "");
            newHistoryItem.setAttribute("Interviewerid", "101102");
            newHistoryItem.setAttribute("Jobid", jobId);
            newHistoryItem.setAttribute("Roundno", 1);
            newHistoryItem.setAttribute("Status", "Ready");

            candidateHistoryVO.insertRow(newHistoryItem);

            applicationsVO.executeQuery();
            interviewProcessVO.executeQuery();
            candidateHistoryVO.executeQuery();
            jobsVO.executeQuery();

            this.getDBTransaction().commit();
        } catch (Exception e) {
            System.out.print("[Error: applyForJobWithId] : " + e);
            response.setError(e);
            return response;
        }

        response.setResponse("goToQuestions");

        return response;
    }

    /**
     * Container's getter for EmployeeDetailsVO1.
     * @return EmployeeDetailsVO1
     */
    public ViewObjectImpl getEmployeeDetailsVO() {
        return (ViewObjectImpl) findViewObject("EmployeeDetailsVO");
    }

    /**
     * Container's getter for EmployeeCredDetailsVO1.
     * @return EmployeeCredDetailsVO1
     */
    public ViewObjectImpl getEmployeeCredDetailsVO1() {
        return (ViewObjectImpl) findViewObject("EmployeeCredDetailsVO1");
    }

    /**
     * Container's getter for EmployeeCredentialsVO1.
     * @return EmployeeCredentialsVO1
     */
    public ViewObjectImpl getEmployeeCredentialsVO() {
        return (ViewObjectImpl) findViewObject("EmployeeCredentialsVO");
    }

    /**
     * Container's getter for ChoicesVO1.
     * @return ChoicesVO1
     */
    public ChoicesVOImpl getChoicesVO1() {
        return (ChoicesVOImpl) findViewObject("ChoicesVO1");
    }

    public ImplResponse changeRoundStatus(String candidateId, String empId, String jobId, Integer round,
                                          String feedback, String status) {

        ImplResponse response = new ImplResponse();
        ViewObject interviewsVO = getInterviewProcessesVO();
        ViewObject historyVO = getCandidateHistoriesVO();
        ViewObject applicationsVO = getApplicationsVO();
        ViewObject jobsVO = getJobsVO();
        ViewObject employeesVO = getEmployeesVO();
        Integer jobLevel = 0;
        String availableEmpId = "";

        try {
            employeesVO.setWhereClause("employeeid = " + empId);
            jobsVO.setWhereClause("Jobid = " + jobId);

            interviewsVO.setWhereClause("employeeid = " + empId + " AND " + "jobid = " + jobId + " AND round = '" +
                                        round.toString() + "'");
            interviewsVO.executeQuery();
            employeesVO.executeQuery();
            jobsVO.executeQuery();

            if (jobsVO.hasNext()) {
                jobLevel = Integer.parseInt(jobsVO.next()
                                                  .getAttribute("Experiencelevel")
                                                  .toString());
            }


            if (interviewsVO.hasNext()) {
                Row currentInterview = interviewsVO.next();

                currentInterview.setAttribute("Status", status);

                historyVO.setWhereClause("candidateid = '" + candidateId + "' AND " + "jobid = " + jobId + " AND " +
                                         "Roundno = " + round.toString());
                historyVO.executeQuery();

                if (historyVO.hasNext()) {
                    Row history = historyVO.next();
                    history.setAttribute("Status", status);
                    history.setAttribute("Feedback", feedback);

                    historyVO.executeQuery();

                    if (status.equals("In progress")) {
                        if (employeesVO.hasNext()) {
                            employeesVO.next().setAttribute("Status", "Not Available");
                        }
                    }

                    if (status.equals("Completed")) {
                        if (round < 3) {
                            availableEmpId = findAvailableEmployeeWithMinExperience(jobLevel, Integer.parseInt(empId));
                            
                            Integer empIdToInsert =
                                availableEmpId != null ? Integer.parseInt(availableEmpId) : 101102; // HR
                            String statusToInsert = availableEmpId != null ? "Ready" : "Pending";

                            Timestamp sqlTimestamp = new Timestamp(System.currentTimeMillis());
                            Row newInterview = interviewsVO.createRow();
                            newInterview.setAttribute("Candidateid", candidateId);
                            newInterview.setAttribute("Employeeid", empIdToInsert);
                            newInterview.setAttribute("Candidatename",
                                                      currentInterview.getAttribute("Candidatename").toString());
                            newInterview.setAttribute("Jobname", currentInterview.getAttribute("Jobname").toString());
                            newInterview.setAttribute("Jobid", Integer.parseInt(jobId));
                            newInterview.setAttribute("Round", round + 1);
                            newInterview.setAttribute("Status", statusToInsert);
                            newInterview.setAttribute("Scheduledon", sqlTimestamp);
                            interviewsVO.insertRow(newInterview);

                            Row historyItem = historyVO.createRow();
                            historyItem.setAttribute("Candidateid", candidateId);
                            historyItem.setAttribute("Interviewerid", empIdToInsert);
                            historyItem.setAttribute("Feedback", "");
                            historyItem.setAttribute("Jobid", Integer.parseInt(jobId));
                            historyItem.setAttribute("Roundno", round + 1);
                            historyItem.setAttribute("Status", statusToInsert);
                            historyVO.insertRow(historyItem);

                            historyVO.executeQuery();
                            interviewsVO.executeQuery();
                        } else {
                            applicationsVO.setWhereClause("candidateid = '" + candidateId + "' and " + "jobid = " +
                                                          jobId);
                            applicationsVO.executeQuery();

                            if (applicationsVO.hasNext()) {
                                Row application = applicationsVO.next();
                                application.setAttribute("Applicationstatus", "Processed");
                            }
                            applicationsVO.executeQuery();

                            jobsVO.setWhereClause("jobid = " + jobId);
                            jobsVO.executeQuery();

                            if (jobsVO.hasNext()) {
                                Row job = jobsVO.next();
                                Integer vacancies = Integer.parseInt(job.getAttribute("Noofvacancies").toString());
                                job.setAttribute("Noofvacancies", vacancies - 1);
                            }
                            jobsVO.executeQuery();
                        }
                    }
                }

            }
            this.getDBTransaction().commit();
            
            if (availableEmpId == null) {
                response.setRedirect("goToInterviews");
                throw new Exception("Interviewers not avialable. Try again later!");
            }
            
        } catch (Exception e) {
            response.setError(e);
            return response;
        }
        return response;
    }

    public String findAvailableEmployeeWithMinExperience(Integer experienceLevel, Integer criteria) {
        String empId = null;
        ViewObject employeesVO = getEmployeesVO();

        if (criteria != -1)
            employeesVO.setWhereClause("status = 'Available'" + " AND " + " TO_NUMBER(experiencelevel) >= " +
                                       experienceLevel + " AND employeeid != " + criteria + " AND " +
                                       "jobname != 'HR'");
        else
            employeesVO.setWhereClause("status = 'Available'" + " AND " + " TO_NUMBER(experiencelevel) >= " +
                                       experienceLevel + " AND " + "jobname != 'HR'");

        employeesVO.executeQuery();

        if (employeesVO.hasNext()) {
            Row employee = employeesVO.next();
            empId = employee.getAttribute("Employeeid").toString();
        }
        return empId;
    }

    public ImplResponse addInterviewRoundStatus(String status, UserData user, String jobId, Integer score) {

        ImplResponse response = new ImplResponse();
        ViewObject interviewsVO = getInterviewProcessesVO();
        ViewObject jobsVO = getJobsVO();
        ViewObject historyVO = getCandidateHistoriesVO();
        Integer jobLevel = null;
        String empId = null, jobName = null;

        ViewCriteria jobVC = jobsVO.createViewCriteria();
        ViewCriteriaRow jobVCrow = jobVC.createViewCriteriaRow();


        try {

            jobVCrow.setAttribute("Jobid", jobId);
            jobVC.add(jobVCrow);
            jobsVO.appendViewCriteria(jobVC);
            jobsVO.executeQuery();

            if (jobsVO.hasNext()) {
                Row job = jobsVO.next();
                jobLevel = Integer.parseInt(job.getAttribute("Experiencelevel").toString());
                jobName = job.getAttribute("Jobname").toString();
                empId = findAvailableEmployeeWithMinExperience(jobLevel, -1);
            } else
                throw new Exception("No jobs with Job id : " + jobId + "found");
        } catch (Exception e) {
            response.setError(e);
            return response;
        }

        try {

            ViewCriteria historyVC = historyVO.createViewCriteria();
            ViewCriteriaRow historyVCR = historyVC.createViewCriteriaRow();
            historyVCR.setAttribute("Candidateid", user.getCandidateId());
            historyVCR.setAttribute("Jobid", Integer.parseInt(jobId));
            historyVCR.setAttribute("Roundno", 1);

            historyVC.add(historyVCR);
            historyVO.appendViewCriteria(historyVC);
            historyVO.executeQuery();

            if (historyVO.hasNext()) {
                Row history = historyVO.next();
                history.setAttribute("Status", status);
                history.setAttribute("Feedback", "Score : " + score.toString());
            }

            historyVO.executeQuery();
            if (status.equals("Completed")) {
                Row historyItem = historyVO.createRow();
                historyItem.setAttribute("Candidateid", user.getCandidateId());
                historyItem.setAttribute("Interviewerid", empId != null ? Integer.parseInt(empId) : 101102);
                historyItem.setAttribute("Feedback", "");
                historyItem.setAttribute("Jobid", Integer.parseInt(jobId));
                historyItem.setAttribute("Roundno", 2);
                historyItem.setAttribute("Status", empId != null ? "Ready" : "Pending");
                historyVO.insertRow(historyItem);
                historyVO.executeQuery();
            }

        } catch (Exception e) {
            System.out.print("[Error] Adding history item : " + e);
            response.setError(e);
            return response;
        }

        try {
            Timestamp sqlTimestamp = new Timestamp(System.currentTimeMillis());

            ViewCriteria interviewsVC = interviewsVO.createViewCriteria();
            ViewCriteriaRow interviewsVCR = interviewsVC.createViewCriteriaRow();
            interviewsVCR.setAttribute("Candidateid", user.getCandidateId());
            interviewsVCR.setAttribute("Jobid", jobId);
            interviewsVCR.setAttribute("Round", "1");

            interviewsVC.add(interviewsVCR);
            interviewsVO.appendViewCriteria(interviewsVC);
            interviewsVO.executeQuery();

            if (interviewsVO.hasNext()) {
                Row interviewItemRow = interviewsVO.next();
                if (status.equals("Completed")) {
                    interviewsVO.removeCurrentRow();
                } else
                    interviewItemRow.setAttribute("Status", status);
                interviewsVO.executeQuery();
            }

            if (status.equals("Completed")) {
                Row newInterview = interviewsVO.createRow();
                newInterview.setAttribute("Candidateid", user.getCandidateId());
                newInterview.setAttribute("Employeeid", empId != null ? Integer.parseInt(empId) : 101102);
                newInterview.setAttribute("Candidatename", user.getFirstName() + " " + user.getLastName());
                newInterview.setAttribute("Jobid", jobId);
                newInterview.setAttribute("Jobname", jobName);
                newInterview.setAttribute("Round", 2);
                newInterview.setAttribute("Status", empId != null ? "Ready" : "Pending");
                newInterview.setAttribute("Scheduledon", sqlTimestamp);
                interviewsVO.insertRow(newInterview);
                interviewsVO.executeQuery();
            }
            this.getDBTransaction().commit();

            if (empId == null) {
                response.setRedirect("goToDashboard");
                throw new Exception("No Employee available to take interview. Please request interview later.");
            }
        } catch (Exception e) {
            System.out.print("[Error: addInterviewRoundStatus] : " + e);
            response.setError(e);
            return response;
        }
        return response;
    }

    public ImplResponse requestInterviewForCid(String candidateId, String jobId) {
        ImplResponse response = new ImplResponse();
        String round = null, empId = null;
        ViewObject jobsVO = getJobsVO();
        ViewObject interviewsVO = getInterviewProcessesVO();
        ViewObject historyVO = getCandidateHistoriesVO();

        try {
            interviewsVO.setWhereClause("candidateid = '" + candidateId + "' AND jobid = " + jobId +
                                        " AND status = 'Pending'");
            interviewsVO.executeQuery();

            jobsVO.setWhereClause("jobid = " + jobId);
            jobsVO.executeQuery();

            if (interviewsVO.hasNext()) {

                Integer jobLevel = Integer.parseInt(jobsVO.next()
                                                          .getAttribute("Experiencelevel")
                                                          .toString());
                empId = findAvailableEmployeeWithMinExperience(jobLevel, -1);
                if (empId == null) {
                    response.setRedirect("goToInterviews");
                    response.setError(new Exception("No employees available. Please try again later"));
                    return response;
                }
                Row row = interviewsVO.next();
                round = row.getAttribute("Round").toString();
                row.setAttribute("Employeeid", Integer.parseInt(empId));
                row.setAttribute("Status", "Ready");
            }
            interviewsVO.executeQuery();

            historyVO.setWhereClause("candidateid = '" + candidateId + "' AND jobid = " + jobId + " AND roundno = " +
                                     round);
            historyVO.executeQuery();

            if (historyVO.hasNext()) {
                Row row = historyVO.next();
                row.setAttribute("Status", "Ready");
                row.setAttribute("Interviewerid", Integer.parseInt(empId));
            }

            this.getDBTransaction().commit();
        } catch (Exception e) {
            response.setError(e);
            return response;
        }
        return response;
    }

    /**
     * Container's getter for CandidateHistoriesVO1.
     * @return CandidateHistoriesVO1
     */
    public ViewObjectImpl getHistoriesByCId() {
        return (ViewObjectImpl) findViewObject("HistoriesByCId");
    }

    /**
     * Container's getter for SysC004695733Link1.
     * @return SysC004695733Link1
     */
    public ViewLinkImpl getSysC004695733Link1() {
        return (ViewLinkImpl) findViewLink("SysC004695733Link1");
    }

    /**
     * Container's getter for EmployeesVO1.
     * @return EmployeesVO1
     */
    public ViewObjectImpl getCredentialsForEmployee() {
        return (ViewObjectImpl) findViewObject("CredentialsForEmployee");
    }

    /**
     * Container's getter for SysC004695806Link1.
     * @return SysC004695806Link1
     */
    public ViewLinkImpl getSysC004695806Link1() {
        return (ViewLinkImpl) findViewLink("SysC004695806Link1");
    }

    /**
     * Container's getter for AddJobsVO1.
     * @return AddJobsVO1
     */
    public ViewObjectImpl getAddJobsVO1() {
        return (ViewObjectImpl) findViewObject("AddJobsVO1");
    }

    /**
     * Container's getter for AdminInterviews1.
     * @return AdminInterviews1
     */
    public ViewObjectImpl getAdminInterviews1() {
        return (ViewObjectImpl) findViewObject("AdminInterviews1");
    }
}

