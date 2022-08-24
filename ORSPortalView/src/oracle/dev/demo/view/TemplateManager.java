package oracle.dev.demo.view;

import java.util.ArrayList;

import oracle.adf.share.ADFContext;

import oracle.dev.demo.classes.UserData;

public class TemplateManager {
    public TemplateManager() {
    }

    public String onLogout() {
        String userType = ADFContext.getCurrent().getSessionScope().get("UserType").toString();
        UserData user = new UserData();
        user.setUserType(userType);
        
        ArrayList<String> keys = user.getUserMapKeys();
        for(int i = 0; i < keys.size(); i++) {
            ADFContext.getCurrent().getSessionScope().remove(keys.get(i));
        }
        
        ADFContext.getCurrent().getSessionScope().put("CandidateId", "");
        ADFContext.getCurrent().getSessionScope().put("Employeeid", "");
        
        return "goToLogin";
    }
}
