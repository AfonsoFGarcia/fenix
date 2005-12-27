/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.ICourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class CourseGroupManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle messages = getResourceBundle("ServidorApresentacao/BolonhaManagerResources");
   
    private String name = null;
    
    public Integer getDegreeCurricularPlanID() {
        return getAndHoldIntegerParameter("degreeCurricularPlanID");
    }

    public Integer getParentCourseGroupID() {
        return getAndHoldIntegerParameter("parentCourseGroupID");
    }
    
    public Integer getCourseGroupID() {
        return getAndHoldIntegerParameter("courseGroupID");
    }

    public String getName() throws FenixFilterException, FenixServiceException {
        return (name == null && getCourseGroupID() != null) ? getCourseGroup(getCourseGroupID()).getName() : name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public IDegreeCurricularPlan getDegreeCurricularPlan() throws FenixFilterException, FenixServiceException {
        return (IDegreeCurricularPlan) readDomainObject(DegreeCurricularPlan.class, getDegreeCurricularPlanID());
    }
    
    public ICourseGroup getCourseGroup(Integer courseGroupID) throws FenixFilterException, FenixServiceException {
        return (ICourseGroup) readDomainObject(CourseGroup.class, getCourseGroupID());
    }
    
    public String createCourseGroup() throws FenixFilterException {        
        try {
            final Object args[] = {getParentCourseGroupID(), getName()};
            ServiceUtils.executeService(getUserView(), "CreateCourseGroup", args);
            addInfoMessage(messages.getString("courseGroupCreated"));
            return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            setErrorMessage(e.getMessage());            
        } catch (final DomainException e) {
            setErrorMessage(e.getMessage());            
        }
        return "";
    }
    
    public String editCourseGroup() throws FenixFilterException {        
        try {
            final Object args[] = {getCourseGroupID(), getName()};
            ServiceUtils.executeService(getUserView(), "EditCourseGroup", args);
            addInfoMessage(messages.getString("courseGroupEdited"));
            return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            setErrorMessage(e.getMessage());            
        } catch (final DomainException e) {
            setErrorMessage(e.getMessage());            
        }
        return "";
    }
    
    public String deleteCourseGroup() throws FenixFilterException {        
        try {
            final Object args[] = {getCourseGroupID()};
            ServiceUtils.executeService(getUserView(), "DeleteCourseGroup", args);
            addInfoMessage(messages.getString("courseGroupDeleted"));
            return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            setErrorMessage(e.getMessage());            
        } catch (final DomainException e) {
            setErrorMessage(e.getMessage());
        }
        return "";
    }
}
