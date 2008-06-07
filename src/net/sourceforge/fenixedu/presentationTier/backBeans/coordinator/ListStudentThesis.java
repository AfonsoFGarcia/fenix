package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ListStudentThesis {

    private Integer degreeCurricularPlanID;

    private IUserView userView;

    public ListStudentThesis() {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                .getSession(false);
        userView = (IUserView) session.getAttribute(SetUserViewFilter.USER_SESSION_ATTRIBUTE);
    }

    public Integer getDegreeCurricularPlanID() {
        return degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;

        ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

    }

    public List getMasterDegreeThesisDataVersions() {

        Object[] args = { degreeCurricularPlanID };
        try {
            return (List) ServiceUtils.executeService(
                    "ReadActiveMasterDegreeThesisDataVersionsByDegreeCurricularPlan", args);
        } catch (FenixFilterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

}
