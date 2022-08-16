package oracle.demo.project.view;

import com.oracle.wls.shaded.org.apache.bcel.generic.RETURN;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import oracle.adf.controller.ControllerContext;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class PrintPageBean {
    
    PrintableBehaviorBean printableBehavior = null;
    
    String printablePageReturnURL = null;
    
    public String onPrint(){
        printableBehavior.setOutputMode("printable");
        return "Print";
    }
    
    public void beforePhaseMethod(PhaseEvent phaseEvent) {
     //only perform action if RENDER_RESPONSE phase is reached
     if (phaseEvent.getPhaseId() == PhaseId.RENDER_RESPONSE){
        FacesContext fctx = FacesContext.getCurrentInstance();
     
         //check internal request parameter
     
        Map requestMap = fctx.getExternalContext().getRequestMap();
        Object showPrintableBehavior =
        requestMap.get("oracle.adfinternal.view.faces.el.PrintablePage");
     
        if(showPrintableBehavior != null){
     
            if (Boolean.TRUE == showPrintableBehavior){
                ExtendedRenderKitService erks = null;
                erks = Service.getRenderKitService(
                fctx,ExtendedRenderKitService.class);
            
                //invoke JavaScript from the server
                erks.addScript(fctx, "window.print();");
            }
        }
    }
    }

     
     
    public void setPrintableBehavior(PrintableBehaviorBean printableBehavior) {
        this.printableBehavior = printableBehavior;
    }

    public PrintableBehaviorBean getPrintableBehavior() {
        return printableBehavior;
    }

    public void setPrintablePageReturnURL(String printablePageReturnURL) {
        this.printablePageReturnURL = printablePageReturnURL;
    }

    public String getPrintablePageReturnURL() {
        return printablePageReturnURL;
    }
}
