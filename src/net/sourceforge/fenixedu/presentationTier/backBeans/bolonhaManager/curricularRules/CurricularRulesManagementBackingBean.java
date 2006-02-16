/*
 * Created on Feb 3, 2006
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.faces.component.UISelectItems;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CurricularRuleParametersDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curricularRules.PrecedenceRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionBetweenDegreeModules;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

import org.apache.commons.beanutils.BeanComparator;

public class CurricularRulesManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle bolonhaResources = getResourceBundle("resources/BolonhaManagerResources");
    private final ResourceBundle enumerationResources = getResourceBundle("resources/EnumerationResources");
    private final ResourceBundle domainResources = getResourceBundle("resources/DomainExceptionResources");
    private final String NO_SELECTION = "no_selection";
    
    private Integer degreeModuleID = null;
    private DegreeModule degreeModule = null;

    private DegreeCurricularPlan degreeCurricularPlan = null;
    private CurricularRule curricularRule = null;

    private UISelectItems curricularRuleTypeItems;
    private UISelectItems degreeModuleItems;
    private UISelectItems courseGroupItems;

    public Integer getDegreeCurricularPlanID() {
        return getAndHoldIntegerParameter("degreeCurricularPlanID");
    }

    public Integer getDegreeModuleID() throws FenixFilterException, FenixServiceException {
        if (degreeModuleID == null) {
            degreeModuleID = getAndHoldIntegerParameter("degreeModuleID"); 
            if (degreeModuleID == null && getCurricularRule() != null) {
                degreeModuleID = getCurricularRule().getDegreeModuleToApplyRule().getIdInternal();
            }
        }
        return degreeModuleID;
    }

    public DegreeModule getDegreeModule() throws FenixFilterException, FenixServiceException {
        return (degreeModule == null) ? (degreeModule = (DegreeModule) readDomainObject(
                DegreeModule.class, getDegreeModuleID())) : degreeModule;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() throws FenixFilterException,
            FenixServiceException {
        return (degreeCurricularPlan == null) ? (degreeCurricularPlan = (DegreeCurricularPlan) readDomainObject(
                DegreeCurricularPlan.class, getDegreeCurricularPlanID()))
                : degreeCurricularPlan;
    }

    public String getSelectedCurricularRuleType() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("selectedCurricularRuleType") == null && getCurricularRule() != null) {
            setSelectedCurricularRuleType(getCurricularRule().getCurricularRuleType().getName());            
        }
        return (String) getViewState().getAttribute("selectedCurricularRuleType");
    }

    public void setSelectedCurricularRuleType(String ruleType) {
        getViewState().setAttribute("selectedCurricularRuleType", ruleType);
    }

    private List<SelectItem> getRuleTypes() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final CurricularRuleType curricularRuleType : CurricularRuleType.values()) {
            switch (curricularRuleType) {
            case CREDITS_LIMIT:
                if (getDegreeModule() instanceof CurricularCourse 
                        && ((CurricularCourse) getDegreeModule()).getType().equals(CurricularCourseType.OPTIONAL_COURSE)) {
                    result.add(new SelectItem(curricularRuleType.getName(), enumerationResources.getString(curricularRuleType.getName())));
                    break;
                }
            case DEGREE_MODULES_SELECTION_LIMIT:
            case PRECEDENCY_BETWEEN_DEGREE_MODULES:
                if (getDegreeModule() instanceof CourseGroup) {
                    result.add(new SelectItem(curricularRuleType.getName(), enumerationResources.getString(curricularRuleType.getName())));
                }
                break;
            case PRECEDENCY_APPROVED_DEGREE_MODULE:
            case PRECEDENCY_ENROLED_DEGREE_MODULE:
            case ENROLMENT_TO_BE_APPROVED_BY_COORDINATOR:
                if (getDegreeModule() instanceof CurricularCourse) {
                    result.add(new SelectItem(curricularRuleType.getName(), enumerationResources.getString(curricularRuleType.getName())));
                }
            default:
                break;
            }
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(NO_SELECTION, enumerationResources.getString("dropDown.Default")));
        return result;
    }

    public List<String> getRulesLabels() throws FenixFilterException, FenixServiceException {
        final List<String> resultLabels = new ArrayList<String>();
        for (CurricularRule curricularRule : getDegreeModule().getCurricularRules()) {
            resultLabels.add(CurricularRuleLabelFormatter.getLabel(curricularRule));
        }
        return resultLabels;
    }

    public String getRuleLabel() throws FenixFilterException, FenixServiceException {
        return CurricularRuleLabelFormatter.getLabel(getCurricularRule());
    }

    public UISelectItems getCurricularRuleTypeItems() throws FenixFilterException, FenixServiceException {
        if (curricularRuleTypeItems == null) {
            curricularRuleTypeItems = new UISelectItems();
            curricularRuleTypeItems.setValue(getRuleTypes());
        }
        return curricularRuleTypeItems;
    }

    public void setCurricularRuleTypeItems(UISelectItems ruleTypeItems) {
        this.curricularRuleTypeItems = ruleTypeItems;
    }

    public void onChangeCurricularRuleTypeDropDown(ValueChangeEvent event) throws FenixFilterException,
            FenixServiceException {
        getDegreeModuleItems().setValue(readDegreeModules((String) event.getNewValue()));
        getCourseGroupItems().setValue(readParentCourseGroups((String) event.getNewValue()));
    }

    public Integer getSelectedDegreeModuleID() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("selectedDegreeModuleID") == null && getCurricularRule() != null) {
            if (getCurricularRule() instanceof PrecedenceRule) {
                setSelectedDegreeModuleID(((PrecedenceRule) getCurricularRule()).getPrecedenceDegreeModule().getIdInternal());                
            } 
        }
        return (Integer) getViewState().getAttribute("selectedDegreeModuleID");
    }

    public void setSelectedDegreeModuleID(Integer selectedDegreeModuleID) {
        getViewState().setAttribute("selectedDegreeModuleID", selectedDegreeModuleID);
    }
   
    public Integer getSelectedContextCourseGroupID() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("selectedContextCourseGroupID") == null && getCurricularRule() != null) {
            if (getCurricularRule().getContextCourseGroup() != null) {
                setSelectedContextCourseGroupID(getCurricularRule().getContextCourseGroup().getIdInternal());
            }
        }
        return (Integer) getViewState().getAttribute("selectedContextCourseGroupID");
    }

    public void setSelectedContextCourseGroupID(Integer selectedContextCourseGroupID) {
        getViewState().setAttribute("selectedContextCourseGroupID", selectedContextCourseGroupID);
    }
    
    public Integer getMinimumLimit() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("minimumLimit") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof DegreeModulesSelectionLimit) {
                setMinimumLimit(((DegreeModulesSelectionLimit) getCurricularRule()).getMinimum());               
            } else {
                setMinimumLimit(Integer.valueOf(0));
            }
        }
        return (Integer) getViewState().getAttribute("minimumLimit");
    }

    public void setMinimumLimit(Integer minimumLimit) {
        getViewState().setAttribute("minimumLimit", minimumLimit);
    }
    
    public Integer getMaximumLimit() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("maximumLimit") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof DegreeModulesSelectionLimit) {
                setMaximumLimit(((DegreeModulesSelectionLimit) getCurricularRule()).getMaximum());               
            } else {
                setMaximumLimit(Integer.valueOf(0));
            }
        }
        return (Integer) getViewState().getAttribute("maximumLimit");
    }

    public void setMaximumLimit(Integer maximumLimit) {
        getViewState().setAttribute("maximumLimit", maximumLimit);
    }
   
    public Double getMinimumCredits() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("minimumCredits") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof CreditsLimit) {
                setMinimumCredits(((CreditsLimit) getCurricularRule()).getMinimum());               
            } else if (getCurricularRule() != null && getCurricularRule() instanceof RestrictionBetweenDegreeModules) {
                setMinimumCredits(((RestrictionBetweenDegreeModules) getCurricularRule()).getMinimum());
            } else {
                setMinimumCredits(Double.valueOf(0));
            }
        }
        return (Double) getViewState().getAttribute("minimumCredits");
    }

    public void setMinimumCredits(Double minimumCredits) {
        getViewState().setAttribute("minimumCredits", minimumCredits);
    }
    
    public Double getMaximumCredits() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("maximumCredits") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof CreditsLimit) {
                setMaximumCredits(((CreditsLimit) getCurricularRule()).getMaximum());               
            } else {
                setMaximumCredits(Double.valueOf(0));
            }
        }
        return (Double) getViewState().getAttribute("maximumCredits");
    }

    public void setMaximumCredits(Double maximumCredits) {
        getViewState().setAttribute("maximumCredits", maximumCredits);
    }
    
    public String getSelectedSemester() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("selectedSemester") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof PrecedenceRule) {
                final PrecedenceRule precedenceRule = (PrecedenceRule) getCurricularRule();
                setSelectedSemester(precedenceRule.getCurricularPeriodOrder() != null ? precedenceRule
                        .getCurricularPeriodOrder().toString() : "0");
            } else {
                setSelectedSemester("0");
            }
        }
        return (String) getViewState().getAttribute("selectedSemester");
    }

    public void setSelectedSemester(String selectedSemester) {
        getViewState().setAttribute("selectedSemester", selectedSemester);
    }
    
    public Integer getCurricularRuleID() {
        return getAndHoldIntegerParameter("curricularRuleID");
    }
    
    public CurricularRule getCurricularRule() throws FenixFilterException, FenixServiceException {
        return (curricularRule == null) ? (curricularRule = (CurricularRule) readDomainObject(
                CurricularRule.class, getCurricularRuleID())) : curricularRule;
    }
    
    public UISelectItems getDegreeModuleItems() throws FenixFilterException, FenixServiceException {
        if (degreeModuleItems == null) {
            degreeModuleItems = new UISelectItems();
            degreeModuleItems.setValue(readDegreeModules(getSelectedCurricularRuleType()));
        }
        return degreeModuleItems;
    }

    public void setDegreeModuleItems(UISelectItems degreeModuleItems) {
        this.degreeModuleItems = degreeModuleItems;
    }

    public UISelectItems getCourseGroupItems() throws FenixFilterException, FenixServiceException {
        if (courseGroupItems == null) {
            courseGroupItems = new UISelectItems();
            courseGroupItems.setValue(readParentCourseGroups(getSelectedCurricularRuleType()));
        }
        return courseGroupItems;
    }

    public void setCourseGroupItems(UISelectItems courseGroupItems) {
        this.courseGroupItems = courseGroupItems;
    }

    private Object readParentCourseGroups(String selectedCurricularRuleType) throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        if (selectedCurricularRuleType != null && !selectedCurricularRuleType.equals(NO_SELECTION)) {
            for (final Context context : getDegreeModule().getDegreeModuleContexts()) {
                final CourseGroup courseGroup = context.getCourseGroup();
                if (!courseGroup.isRoot()) {
                    result.add(new SelectItem(courseGroup.getIdInternal(), courseGroup.getName()));
                }
            }
            Collections.sort(result, new BeanComparator("label"));
        }
        result.add(0, new SelectItem(Integer.valueOf(0), bolonhaResources.getString("all")));
        return result;
    }

    // TODO: check this method
    private List<SelectItem> readDegreeModules(String selectedCurricularRuleType)
            throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        if (selectedCurricularRuleType != null && !selectedCurricularRuleType.equals(NO_SELECTION)) {
            if (selectedCurricularRuleType.equals(CurricularRuleType.PRECEDENCY_APPROVED_DEGREE_MODULE.name())
                    || selectedCurricularRuleType.equals(CurricularRuleType.PRECEDENCY_ENROLED_DEGREE_MODULE.name())) {
                
                getCurricularCourses(result);
                
            } else if (selectedCurricularRuleType.equals(CurricularRuleType.CREDITS_LIMIT.name())
                    || selectedCurricularRuleType.equals(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT.name())
                    || selectedCurricularRuleType.equals(CurricularRuleType.PRECEDENCY_BETWEEN_DEGREE_MODULES.name())) {
                
                getCourseGroups(result);
            }
        }
        result.add(0, new SelectItem(Integer.valueOf(0), bolonhaResources.getString("choose")));
        return result;
    }

    private void getCourseGroups(final List<SelectItem> result) throws FenixFilterException, FenixServiceException {
        final List<CourseGroup> courseGroups = (List<CourseGroup>) getDegreeCurricularPlan().getDcpDegreeModules(CourseGroup.class);
        courseGroups.remove(getDegreeModule());
        for (final CourseGroup courseGroup : courseGroups) {
            result.add(new SelectItem(courseGroup.getIdInternal(), courseGroup.getName()));
        }
        Collections.sort(result, new BeanComparator("label"));
    }

    private void getCurricularCourses(final List<SelectItem> result) throws FenixFilterException, FenixServiceException {
        final List<CurricularCourse> curricularCourses = (List<CurricularCourse>) getDegreeCurricularPlan()
                .getDcpDegreeModules(CurricularCourse.class);
        curricularCourses.remove(getDegreeModule());
        for (final CurricularCourse curricularCourse : curricularCourses) {
            result.add(new SelectItem(curricularCourse.getIdInternal(), curricularCourse.getName()));
        }
        Collections.sort(result, new BeanComparator("label"));
    }

    public String createCurricularRule() {
        try {
            checkSelectedAttributes();
            final Object[] args = { getDegreeModuleID(),
                    CurricularRuleType.valueOf(getSelectedCurricularRuleType()),
                    buildCurricularRuleParametersDTO() };
            ServiceUtils.executeService(getUserView(), "CreateCurricularRule", args);
            return "setCurricularRules";
        } catch (FenixActionException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.notAuthorized"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (NumberFormatException e) {
            addErrorMessage(bolonhaResources.getString("invalid.minimum.maximum.values"));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }
    
    public String editCurricularRule() {
        try {
            final Object[] args = { getCurricularRuleID(), buildCurricularRuleParametersDTO()};
            ServiceUtils.executeService(getUserView(), "EditCurricularRule", args);
            return "setCurricularRules";
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.notAuthorized"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }
    
    public String deleteCurricularRule() {
        try {
            final Object[] args = { getCurricularRuleID() };
            ServiceUtils.executeService(getUserView(), "DeleteCurricularRule", args);
            addInfoMessage(bolonhaResources.getString("curricularRule.deleted"));
            return "setCurricularRules";
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.notAuthorized"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }
    
    private void checkSelectedAttributes() throws FenixActionException, FenixFilterException, FenixServiceException {
        if (getSelectedCurricularRuleType() == null || getSelectedCurricularRuleType().equals(NO_SELECTION)) {
            throw new FenixActionException("must.select.curricular.rule.type");
        }        
    }
    
    private CurricularRuleParametersDTO buildCurricularRuleParametersDTO() throws FenixFilterException, FenixServiceException, NumberFormatException {
        final CurricularRuleParametersDTO parametersDTO = new CurricularRuleParametersDTO();
        parametersDTO.setSelectedDegreeModuleID(getSelectedDegreeModuleID());
        parametersDTO.setContextCourseGroupID((getSelectedContextCourseGroupID() != null && getSelectedContextCourseGroupID().equals(0)) ? null : getSelectedContextCourseGroupID() );
        parametersDTO.setCurricularPeriodInfoDTO(new CurricularPeriodInfoDTO(Integer.valueOf(getSelectedSemester()), CurricularPeriodType.SEMESTER));            
        parametersDTO.setMinimumCredits(getMinimumCredits());
        parametersDTO.setMaximumCredits(getMaximumCredits());
        parametersDTO.setMinimumLimit(getMinimumLimit());
        parametersDTO.setMaximumLimit(getMaximumLimit());
        return parametersDTO;
    }
}
