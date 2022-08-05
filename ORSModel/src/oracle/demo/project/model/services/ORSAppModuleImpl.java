package oracle.demo.project.model.services;

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
}

