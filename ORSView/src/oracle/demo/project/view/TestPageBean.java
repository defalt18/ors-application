package oracle.demo.project.view;

import javax.faces.event.ActionEvent;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;

public class TestPageBean {
    private String jobId;
    private RichPopup questionnaireTF;

    public void setQuestionnaireTF(RichPopup questionnaireTF) {
        this.questionnaireTF = questionnaireTF;
    }

    public RichPopup getQuestionnaireTF() {
        return questionnaireTF;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobId() {
        return jobId;
    }

    public String onClickApply() {
        ADFContext.getCurrent().getPageFlowScope().put("jobId", jobId.substring(1));
        
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        questionnaireTF.show(hints);
        
        return null;
    }

    public void onApplyListener(ActionEvent actionEvent) {
        String jobId = actionEvent.getComponent().getId();
        setJobId(jobId);
    }
}
