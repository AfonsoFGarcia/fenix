package net.sourceforge.fenixedu.presentationTier.backBeans.publico;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.struts.util.MessageResources;

public class PublicEvaluationsBackingBean extends FenixBackingBean {

    private static final MessageResources messages = MessageResources.getMessageResources("ServidorApresentacao/PublicDegreeInformation");

    private static final DateFormat yearFormat = new SimpleDateFormat("yyyy");
    private static final DateFormat hourFormat = new SimpleDateFormat("HH:mm");

    private Integer degreeID;

    private Integer degreeCurricularPlanID;

    private Integer executionPeriodID;

    private Integer curricularYearID;

    private Degree degree;

	public Integer getDegreeID() {
		return (degreeID == null) ? degreeID = getAndHoldIntegerParameter("degreeID") : degreeID;
    }

	public Integer getDegreeCurricularPlanID() throws FenixFilterException, FenixServiceException {
		if (degreeCurricularPlanID == null) {
			degreeCurricularPlanID = getAndHoldIntegerParameter("degreeCurricularPlanID");
			if (degreeCurricularPlanID == null) {
				degreeCurricularPlanID = getMostRecentDegreeCurricularPlan().getIdInternal();
			}
		}
		return degreeCurricularPlanID;
    }

	public Integer getExecutionPeriodID() throws FenixFilterException, FenixServiceException {
		if (executionPeriodID == null || !contains(getExecutionPeriodSelectItems(), executionPeriodID)) {
			executionPeriodID = getAndHoldIntegerParameter("executionPeriodID");
			if (executionPeriodID == null) {
				executionPeriodID = getMostRecentExecutionPeriod().getIdInternal();
			}
		}
		return executionPeriodID;
	}

    public Integer getCurricularYearID() {
        return (curricularYearID == null) ? curricularYearID = getAndHoldIntegerParameter("curricularYearID") : curricularYearID;
    }

	public Degree getDegree() throws FenixFilterException, FenixServiceException {
        if (degree == null) {
            final Object[] args = { Degree.class, getDegreeID() };
            degree = (Degree) ServiceUtils.executeService(getUserView(), "ReadDomainObject", args);
        }
        return degree;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() throws FenixFilterException, FenixServiceException {
        final Degree degree = getDegree();
        final Integer degreeCurricularPlanID = getDegreeCurricularPlanID();
        if (degree != null && degreeCurricularPlanID != null) {
        	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
        		if (degreeCurricularPlanID.equals(degreeCurricularPlan.getIdInternal()) && degreeCurricularPlan.getCurricularStage().equals(CurricularStage.OLD)) {
        			return degreeCurricularPlan;
        		}
        	}
        }
        return null;
    }

    public DegreeCurricularPlan getMostRecentDegreeCurricularPlan() throws FenixFilterException, FenixServiceException {
        DegreeCurricularPlan mostRecentDegreeCurricularPlan = null;
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegree().getDegreeCurricularPlans()) {
            if (degreeCurricularPlan.getState() == DegreeCurricularPlanState.ACTIVE && degreeCurricularPlan.getCurricularStage().equals(CurricularStage.OLD)) {
                final Date initialDate = degreeCurricularPlan.getInitialDate();
                final Date mostRecentInitialDate = degreeCurricularPlan.getInitialDate();
                if (mostRecentDegreeCurricularPlan == null || initialDate.after(mostRecentInitialDate)) {
                    mostRecentDegreeCurricularPlan = degreeCurricularPlan;
                }
            }
        }
        return mostRecentDegreeCurricularPlan;
    }

    public ExecutionPeriod getExecutionPeriod() throws FenixFilterException, FenixServiceException {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final Integer executionPeriodID = getExecutionPeriodID();
        if (degreeCurricularPlan != null && executionPeriodID != null) {
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
                final ExecutionYear executionYear = executionDegree.getExecutionYear();
                for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
                    if (executionPeriod.getIdInternal().equals(executionPeriodID)) {
                        return executionPeriod;
                    }
                }
            }
        }
        return null;        
    }

    private boolean contains(final List<SelectItem> executionPeriodSelectItems, final Integer integer) {
        for (final SelectItem selectItem : executionPeriodSelectItems) {
            if (selectItem.getValue().equals(integer)) {
                return true;
            }
        }
        return false;
    }

    public CurricularYear getCurricularYear() throws FenixFilterException, FenixServiceException {
        final Integer curricularYearID = getCurricularYearID();
        if (curricularYearID != null) {
            final Object[] args = { CurricularYear.class, curricularYearID };
            return (CurricularYear) ServiceUtils.executeService(getUserView(), "ReadDomainObject", args);
        } else {
            return null;
        }
    }

    private ExecutionPeriod getMostRecentExecutionPeriod() throws FenixFilterException, FenixServiceException {
    	ExecutionPeriod mostRecentExecutionPeriod = null;

    	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
    	if (degreeCurricularPlan != null) {
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
            	final ExecutionYear executionYear = executionDegree.getExecutionYear();
            	for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
            		if (executionPeriod.getState() != PeriodState.NOT_OPEN) {
            			if (mostRecentExecutionPeriod == null) {
            				mostRecentExecutionPeriod = executionPeriod;
            			} else {
            				final ExecutionYear mostRecentExecutionYear = mostRecentExecutionPeriod.getExecutionYear();
            				if (executionYear.getYear().compareTo(mostRecentExecutionYear.getYear()) > 0
            						|| (executionYear == mostRecentExecutionYear && executionPeriod.getSemester().compareTo(mostRecentExecutionPeriod.getSemester()) > 0)) {
            					mostRecentExecutionPeriod = executionPeriod;
            				}
            			}
            		}
            	}
            }
    	}
    	return mostRecentExecutionPeriod;
	}

    public String getDegreeName() throws FenixFilterException, FenixServiceException {
        final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return locale.getLanguage().equalsIgnoreCase("en") ? getDegree().getNameEn() : getDegree().getNome();
    }

    public List<SelectItem> getDegreeCurricularPlanSelectItems() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> degreeCurricularPlanSelectItems = new ArrayList<SelectItem>();

        final Degree degree = getDegree();
        if (degree != null) {
            for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
                if (degreeCurricularPlan.getState() == DegreeCurricularPlanState.ACTIVE && degreeCurricularPlan.getCurricularStage().equals(CurricularStage.OLD)) {
                    final Date initialDate = degreeCurricularPlan.getInitialDate();
                    degreeCurricularPlanSelectItems.add(new SelectItem(degreeCurricularPlan.getIdInternal(), yearFormat.format(initialDate)));
                }
            }
        }

        return degreeCurricularPlanSelectItems;
    }

    public List<SelectItem> getExecutionPeriodSelectItems() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> executionPeriodSelectItems = new ArrayList<SelectItem>();

        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
        	final ExecutionYear executionYear = executionDegree.getExecutionYear();
        	for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
        		if (executionPeriod.getState() != PeriodState.NOT_OPEN) {
        			executionPeriodSelectItems.add(new SelectItem(executionPeriod.getIdInternal(), executionPeriod.getName() + " " + executionYear.getYear()));
        		}
        	}
        }

        return executionPeriodSelectItems;
    }

    public List<SelectItem> getCurricularYearSelectItems() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> curricularYearSelectItems = new ArrayList<SelectItem>();
        curricularYearSelectItems.add(new SelectItem(1, "1"));
        curricularYearSelectItems.add(new SelectItem(2, "2"));
        curricularYearSelectItems.add(new SelectItem(3, "3"));
        curricularYearSelectItems.add(new SelectItem(4, "4"));
        curricularYearSelectItems.add(new SelectItem(5, "5"));
        return curricularYearSelectItems;
    }

    public List<CalendarLink> getCalendarLinks() throws FenixFilterException, FenixServiceException {
        List<CalendarLink> calendarLinks = new ArrayList<CalendarLink>();

        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final CurricularYear curricularYear = getCurricularYear();
        final ExecutionPeriod executionPeriod = getExecutionPeriod();
        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            if (curricularYear == null || isActiveForCurricularYear(curricularCourse, curricularYear)) {
                for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
                    if (executionCourse.getExecutionPeriod() == executionPeriod) {
                        for (final Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {
                            if (evaluation instanceof WrittenEvaluation) {
                                final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
                                CalendarLink calendarLink = new CalendarLink();
                                calendarLinks.add(calendarLink);

                                calendarLink.setObjectOccurrence(writtenEvaluation.getDay());
                                calendarLink.setObjectLinkLabel(constructCalendarPresentation(executionCourse, writtenEvaluation));
                                calendarLink.setLinkParameters(constructLinkParameters(executionCourse));
                            } else if (evaluation instanceof Project) {
                                final Project project = (Project) evaluation;
                                   CalendarLink calendarLinkBegin = new CalendarLink();
                                   calendarLinks.add(calendarLinkBegin);
                                   calendarLinkBegin.setObjectOccurrence(project.getBegin());
                                   calendarLinkBegin.setObjectLinkLabel(constructCalendarPresentation(executionCourse, project, project.getBegin(), messages.getMessage("label.evaluation.project.begin")));
                                   calendarLinkBegin.setLinkParameters(constructLinkParameters(executionCourse));

                                   CalendarLink calendarLinkEnd = new CalendarLink();
                                   calendarLinks.add(calendarLinkEnd);
                                   calendarLinkEnd.setObjectOccurrence(project.getEnd());
                                   calendarLinkEnd.setObjectLinkLabel(constructCalendarPresentation(executionCourse, project, project.getEnd(), messages.getMessage("label.evaluation.project.end")));
                                   calendarLinkEnd.setLinkParameters(constructLinkParameters(executionCourse));
                            }                            
                        }
                    }
                }
            }
        }

        return calendarLinks;
    }

    private Map<String, String> constructLinkParameters(final ExecutionCourse executionCourse) {
        final Site site = executionCourse.getSite();

        final Map<String, String> linkParameters = new HashMap<String, String>();
        linkParameters.put("method", "evaluations");
        linkParameters.put("objectCode", (site != null) ? site.getIdInternal().toString() : null);
        linkParameters.put("executionPeriodOID", executionCourse.getExecutionPeriod().getIdInternal().toString());
        return linkParameters;
    }

    private String constructCalendarPresentation(final ExecutionCourse executionCourse, final Project project, final Date time, final String tail) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(messages.getMessage("label.evaluation.shortname.project"));        
        stringBuilder.append(" ");
        stringBuilder.append(executionCourse.getSigla());
        stringBuilder.append(" (");
        stringBuilder.append(hourFormat.format(time));
        stringBuilder.append(") ");
        stringBuilder.append(tail);
        return stringBuilder.toString();
    }

    private String constructCalendarPresentation(final ExecutionCourse executionCourse, final WrittenEvaluation writtenEvaluation) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (writtenEvaluation instanceof WrittenTest) {
            stringBuilder.append(messages.getMessage("label.evaluation.shortname.test"));
        } else if (writtenEvaluation instanceof Exam) {
            stringBuilder.append(messages.getMessage("label.evaluation.shortname.exam"));
        }
        stringBuilder.append(executionCourse.getSigla());
        stringBuilder.append(" (");
        stringBuilder.append(hourFormat.format(writtenEvaluation.getBeginningDate()));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private boolean isActiveForCurricularYear(final CurricularCourse curricularCourse, final CurricularYear curricularYear) {
        for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
            if (curricularCourseScope.getCurricularSemester().getCurricularYear() == curricularYear) {
                return true;
            }
        }
        return false;
    }

    public String getApplicationContext() {
        final String appContext = PropertiesManager.getProperty("app.context");
        return (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
    }

    public void setCurricularYearID(Integer curricularYearID) {
        this.curricularYearID = curricularYearID;
    }

    public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public void setDegreeID(Integer degreeID) {
        this.degreeID = degreeID;
    }

    public void setExecutionPeriodID(Integer executionPeriodID) {
        this.executionPeriodID = executionPeriodID;
    }

    public Date getBeginDate() throws FenixFilterException, FenixServiceException {
    	final ExecutionPeriod executionPeriod = getExecutionPeriod();
    	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
    	final ExecutionYear executionYear = executionPeriod.getExecutionYear();
    	for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
    		if (executionDegree.getDegreeCurricularPlan() == degreeCurricularPlan) {
    			if (executionPeriod.getSemester().intValue() == 1 && executionDegree.getPeriodLessonsFirstSemester() != null) {
    				return executionDegree.getPeriodLessonsFirstSemester().getStart();
    			} else if (executionPeriod.getSemester().intValue() == 2 && executionDegree.getPeriodLessonsSecondSemester() != null) {
    				return executionDegree.getPeriodLessonsSecondSemester().getStart();
    			} else {
    				return executionPeriod.getBeginDate();
    			}
    		}
    	}
    	return null;
    }

    public Date getEndDate() throws FenixFilterException, FenixServiceException {
    	final ExecutionPeriod executionPeriod = getExecutionPeriod();
    	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
    	final ExecutionYear executionYear = executionPeriod.getExecutionYear();
    	for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
    		if (executionDegree.getDegreeCurricularPlan() == degreeCurricularPlan) {
    			if (executionPeriod.getSemester().intValue() == 1 && executionDegree.getPeriodExamsFirstSemester() != null) {
    				return executionDegree.getPeriodExamsFirstSemester().getEnd();
    			} else if (executionPeriod.getSemester().intValue() == 2 && executionDegree.getPeriodExamsSecondSemester() != null) {
    				return executionDegree.getPeriodExamsSecondSemester().getEnd();
    			} else {
    				return executionPeriod.getEndDate();
    			}
    		}
    	}
    	return null;
    }

}