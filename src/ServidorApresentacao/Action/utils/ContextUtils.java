/*
 * Created on 2003/07/28
 *  
 */
package ServidorApresentacao.Action.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.util.LabelValueBean;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoClass;
import DataBeans.InfoCurricularYear;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.TipoSala;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ContextUtils
{

    public static final void setExecutionPeriodContext(HttpServletRequest request)
    {
        String executionPeriodOIDString =
            (String) request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID);
        if (executionPeriodOIDString == null)
        {
            executionPeriodOIDString = request.getParameter(SessionConstants.EXECUTION_PERIOD_OID);
        }

        Integer executionPeriodOID = null;
        if (executionPeriodOIDString != null
            && !executionPeriodOIDString.equals("")
            && !executionPeriodOIDString.equals("null"))
        {
            executionPeriodOID = new Integer(executionPeriodOIDString);
        }
        InfoExecutionPeriod infoExecutionPeriod = null;

        if (executionPeriodOID != null)
        {
            // Read from database
            try
            {
                Object[] args = { executionPeriodOID };
                infoExecutionPeriod =
                    (InfoExecutionPeriod) ServiceUtils.executeService(
                        null,
                        "ReadExecutionPeriodByOID",
                        args);
            } catch (FenixServiceException e)
            {
                e.printStackTrace();
            }
        } else
        {

            // Read current execution period from database
            try
            {
                infoExecutionPeriod =
                    (InfoExecutionPeriod) ServiceUtils.executeService(
                        null,
                        "ReadCurrentExecutionPeriod",
                        new Object[0]);
            } catch (FenixServiceException e)
            {
                e.printStackTrace();
            }
        }
        if (infoExecutionPeriod != null)
        {
            // Place it in request
            request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
            request.setAttribute(
                SessionConstants.EXECUTION_PERIOD_OID,
                infoExecutionPeriod.getIdInternal().toString());
            if (infoExecutionPeriod.getInfoExecutionYear() != null)
            {
                request.setAttribute("schoolYear", infoExecutionPeriod.getInfoExecutionYear().getYear());
            }

        } else
        {
            System.out.println(
                "#### ERROR: Unexisting or invalid executionPeriod - throw proper exception: Someone was playing with the links");
        }
    }

    /**
	 * @param request
	 */
    public static void setExecutionDegreeContext(HttpServletRequest request)
    {
        String executionDegreeOIDString =
            (String) request.getAttribute(SessionConstants.EXECUTION_DEGREE_OID);

        if ((executionDegreeOIDString == null) || (executionDegreeOIDString.length() == 0))
        {
            executionDegreeOIDString = request.getParameter(SessionConstants.EXECUTION_DEGREE_OID);

            // No degree was chosen
            if ((executionDegreeOIDString == null) || (executionDegreeOIDString.length() == 0))
            {
                request.setAttribute(SessionConstants.EXECUTION_DEGREE, null);
            }
        }

        Integer executionDegreeOID = null;
        if (executionDegreeOIDString != null)
        {
            try
            {
                executionDegreeOID = new Integer(executionDegreeOIDString);
            } catch (NumberFormatException ex)
            {
                return;
            }
        }

        InfoExecutionDegree infoExecutionDegree = null;

        if (executionDegreeOID != null)
        {
            // Read from database
            try
            {
                Object[] args = { executionDegreeOID };
                infoExecutionDegree =
                    (InfoExecutionDegree) ServiceUtils.executeService(
                        null,
                        "ReadExecutionDegreeByOID",
                        args);
            } catch (FenixServiceException e)
            {
                e.printStackTrace();
            }

            if (infoExecutionDegree != null)
            {
                // Place it in request
                request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
                request.setAttribute(
                    SessionConstants.EXECUTION_DEGREE_OID,
                    infoExecutionDegree.getIdInternal().toString());
            } else
            {
                System.out.println(
                    "#### ERROR: Unexisting or invalid executionDegree - throw proper exception: Someone was playing with the links");
            }
        }
    }

    /**
	 * @param request
	 */
    public static void setCurricularYearContext(HttpServletRequest request)
    {
        String curricularYearOIDString =
            (String) request.getAttribute(SessionConstants.CURRICULAR_YEAR_OID);
        if (curricularYearOIDString == null)
        {
            curricularYearOIDString = request.getParameter(SessionConstants.CURRICULAR_YEAR_OID);
        }

        Integer curricularYearOID = null;
        if (curricularYearOIDString != null)
        {
            curricularYearOID = new Integer(curricularYearOIDString);
        }

        InfoCurricularYear infoCurricularYear = null;

        if (curricularYearOID != null)
        {
            // Read from database
            try
            {
                Object[] args = { curricularYearOID };
                infoCurricularYear =
                    (InfoCurricularYear) ServiceUtils.executeService(
                        null,
                        "ReadCurricularYearByOID",
                        args);
            } catch (FenixServiceException e)
            {
                e.printStackTrace();
            }

            if (infoCurricularYear != null)
            {
                // Place it in request
                request.setAttribute(SessionConstants.CURRICULAR_YEAR, infoCurricularYear);
                request.setAttribute(
                    SessionConstants.CURRICULAR_YEAR_OID,
                    infoCurricularYear.getIdInternal().toString());
            } else
            {
                System.out.println(
                    "#### ERROR: Unexisting or invalid curricularYear - throw proper exception: Someone was playing with the links");
            }

        }
    }

    /**
	 * @param request
	 */
    public static void setCurricularYearsContext(HttpServletRequest request)
    {
//        List curricularYearsB = (List) request.getAttribute(SessionConstants.CURRICULAR_YEARS_LIST);

        String curricularYears_1 = (String) request.getAttribute(SessionConstants.CURRICULAR_YEARS_1);
        String curricularYears_2 = (String) request.getAttribute(SessionConstants.CURRICULAR_YEARS_2);
        String curricularYears_3 = (String) request.getAttribute(SessionConstants.CURRICULAR_YEARS_3);
        String curricularYears_4 = (String) request.getAttribute(SessionConstants.CURRICULAR_YEARS_4);
        String curricularYears_5 = (String) request.getAttribute(SessionConstants.CURRICULAR_YEARS_5);
        if (curricularYears_1 == null)
        {
            curricularYears_1 = request.getParameter(SessionConstants.CURRICULAR_YEARS_1);
        }
        if (curricularYears_2 == null)
        {
            curricularYears_2 = request.getParameter(SessionConstants.CURRICULAR_YEARS_2);
        }
        if (curricularYears_3 == null)
        {
            curricularYears_3 = request.getParameter(SessionConstants.CURRICULAR_YEARS_3);
        }
        if (curricularYears_4 == null)
        {
            curricularYears_4 = request.getParameter(SessionConstants.CURRICULAR_YEARS_4);
        }
        if (curricularYears_5 == null)
        {
            curricularYears_5 = request.getParameter(SessionConstants.CURRICULAR_YEARS_5);
        }

        List curricularYears = new ArrayList();

        if (curricularYears_1 != null)
        {
            try
            {
                curricularYears.add(new Integer(curricularYears_1));
            } catch (NumberFormatException ex)
            {
            }
        }
        if (curricularYears_2 != null)
        {
            try
            {
                curricularYears.add(new Integer(curricularYears_2));
            } catch (NumberFormatException ex)
            {
            }
        }
        if (curricularYears_3 != null)
        {
            try
            {
                curricularYears.add(new Integer(curricularYears_3));
            } catch (NumberFormatException ex)
            {
            }
        }
        if (curricularYears_4 != null)
        {
            try
            {
                curricularYears.add(new Integer(curricularYears_4));
            } catch (NumberFormatException ex)
            {
            }
        }
        if (curricularYears_5 != null)
        {
            try
            {
                curricularYears.add(new Integer(curricularYears_5));
            } catch (NumberFormatException ex)
            {
            }
        }

        request.setAttribute(SessionConstants.CURRICULAR_YEARS_LIST, curricularYears);
    }

    /**
	 * @param request
	 */
    public static void setExecutionCourseContext(HttpServletRequest request)
    {
        String executionCourseOIDString =
            (String) request.getAttribute(SessionConstants.EXECUTION_COURSE_OID);
        if (executionCourseOIDString == null)
        {
            executionCourseOIDString = request.getParameter(SessionConstants.EXECUTION_COURSE_OID);
        }

        Integer executionCourseOID = null;
        if (executionCourseOIDString != null
            && !executionCourseOIDString.equals("")
            && !executionCourseOIDString.equals("null"))
        {
            executionCourseOID = new Integer(executionCourseOIDString);
        }

        InfoExecutionCourse infoExecutionCourse = null;

        if (executionCourseOID != null)
        {
            // Read from database
            try
            {
                Object[] args = { executionCourseOID };
                infoExecutionCourse =
                    (InfoExecutionCourse) ServiceUtils.executeService(
                        null,
                        "ReadExecutionCourseByOID",
                        args);
            } catch (FenixServiceException e)
            {
                e.printStackTrace();
            }

            if (infoExecutionCourse != null)
            {
                // Place it in request
                request.setAttribute(SessionConstants.EXECUTION_COURSE, infoExecutionCourse);
            } else
            {
                System.out.println(
                    "#### ERROR: Unexisting or invalid executionCourse - throw proper exception: Someone was playing with the links");
            }
        }
    }

    /**
	 * @param request
	 */
    public static void setShiftContext(HttpServletRequest request)
    {
        String shiftOIDString = (String) request.getAttribute(SessionConstants.SHIFT_OID);
        if (shiftOIDString == null)
        {
            shiftOIDString = request.getParameter(SessionConstants.SHIFT_OID);
        }

        Integer shiftOID = null;
        if (shiftOIDString != null)
        {
            shiftOID = new Integer(shiftOIDString);
        }

        InfoShift infoShift = null;

        if (shiftOID != null)
        {
            // Read from database
            try
            {
                Object[] args = { shiftOID };
                infoShift = (InfoShift) ServiceUtils.executeService(null, "ReadShiftByOID", args);
            } catch (FenixServiceException e)
            {
                e.printStackTrace();
            }

            if (infoShift != null)
            {
                /* Sort the list of lesson */
                ComparatorChain chainComparator = new ComparatorChain();
                chainComparator.addComparator(new BeanComparator("diaSemana.diaSemana"));
                chainComparator.addComparator(new Comparator()
                {

                    public int compare(Object o1, Object o2)
                    {
                        Calendar ctime1 = ((InfoLesson) o1).getInicio();
                        Calendar ctime2 = ((InfoLesson) o2).getInicio();

                        Integer time1 =
                            new Integer(
                                ctime1.get(Calendar.HOUR_OF_DAY) * 60 + ctime1.get(Calendar.MINUTE));

                        Integer time2 =
                            new Integer(
                                ctime2.get(Calendar.HOUR_OF_DAY) * 60 + ctime2.get(Calendar.MINUTE));

                        return time1.compareTo(time2);
                    }
                });
                chainComparator.addComparator(new Comparator()
                {

                    public int compare(Object o1, Object o2)
                    {
                        Calendar ctime1 = ((InfoLesson) o1).getFim();
                        Calendar ctime2 = ((InfoLesson) o2).getFim();

                        Integer time1 =
                            new Integer(
                                ctime1.get(Calendar.HOUR_OF_DAY) * 60 + ctime1.get(Calendar.MINUTE));

                        Integer time2 =
                            new Integer(
                                ctime2.get(Calendar.HOUR_OF_DAY) * 60 + ctime2.get(Calendar.MINUTE));

                        return time1.compareTo(time2);
                    }
                });
                if (infoShift.getInfoLessons() == null)
                {
					infoShift.setInfoLessons(new ArrayList());
                }
				if (infoShift.getInfoClasses() == null)
				{
					infoShift.setInfoClasses(new ArrayList());
				}
                
               	chainComparator.addComparator(new BeanComparator("infoSala.nome"));
				Collections.sort(infoShift.getInfoLessons(), chainComparator);

				if (infoShift.getInfoLessons().isEmpty())
                {
                    infoShift.setInfoLessons(null);
                }

                if (infoShift.getInfoClasses().isEmpty())
                {
                    infoShift.setInfoClasses(null);
                }

                // Place it in request
                request.setAttribute(SessionConstants.SHIFT, infoShift);
            } else
            {
                System.out.println(
                    "#### ERROR: Unexisting or invalid shift - throw proper exception: Someone was playing with the links");
            }
        }
    }

    /**
	 * @param request
	 */
    public static void setClassContext(HttpServletRequest request)
    {
        String classOIDString = (String) request.getAttribute(SessionConstants.CLASS_VIEW_OID);
        if (classOIDString == null)
        {
            classOIDString = request.getParameter(SessionConstants.CLASS_VIEW_OID);
        }

        Integer classOID = null;
        if (classOIDString != null)
        {
            classOID = new Integer(classOIDString);
        }

        InfoClass infoClass = null;

        if (classOID != null)
        {
            // Read from database
            try
            {
                Object[] args = { classOID };
                infoClass = (InfoClass) ServiceUtils.executeService(null, "ReadClassByOID", args);
            } catch (FenixServiceException e)
            {
                e.printStackTrace();
            }

            // Place it in request
            request.setAttribute(SessionConstants.CLASS_VIEW, infoClass);
        }
    }

    /**
	 * @param request
	 */
    public static void setLessonContext(HttpServletRequest request)
    {
        String lessonOIDString = (String) request.getAttribute(SessionConstants.LESSON_OID);
        if (lessonOIDString == null)
        {
            lessonOIDString = request.getParameter(SessionConstants.LESSON_OID);
        }

        Integer lessonOID = null;
        if (lessonOIDString != null)
        {
            lessonOID = new Integer(lessonOIDString);
        }

        InfoLesson infoLesson = null;

        if (lessonOID != null)
        {
            // Read from database
            try
            {
                Object[] args = { lessonOID };
                infoLesson = (InfoLesson) ServiceUtils.executeService(null, "ReadLessonByOID", args);
            } catch (FenixServiceException e)
            {
                e.printStackTrace();
            }

            // Place it in request
            request.setAttribute(SessionConstants.LESSON, infoLesson);
        }
    }

    public static void setSelectedRoomsContext(HttpServletRequest request) throws FenixActionException
    {

        Object argsSelectRooms[] =
            {
                 new InfoRoom(
                    readRequestValue(request, "selectRoomCriteria_Name"),
                    readRequestValue(request, "selectRoomCriteria_Building"),
                    readIntegerRequestValue(request, "selectRoomCriteria_Floor"),
                    readTypeRoomRequestValue(request, "selectRoomCriteria_Type"),
                    readIntegerRequestValue(request, "selectRoomCriteria_CapacityNormal"),
                    readIntegerRequestValue(request, "selectRoomCriteria_CapacityExame"))};

        List selectedRooms = null;
        try
        {
            selectedRooms = (List) ServiceManagerServiceFactory.executeService(null, "SelectRooms", argsSelectRooms);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        if (selectedRooms != null && !selectedRooms.isEmpty())
        {
            Collections.sort(selectedRooms);
        }
        request.setAttribute(SessionConstants.SELECTED_ROOMS, selectedRooms);

        setRoomSearchCriteriaContext(request);

    }

    /**
	 * @param request
	 */
    private static void setRoomSearchCriteriaContext(HttpServletRequest request)
    {

        request.setAttribute(
            "selectRoomCriteria_Name",
            readRequestValue(request, "selectRoomCriteria_Name"));
        request.setAttribute(
            "selectRoomCriteria_Building",
            readRequestValue(request, "selectRoomCriteria_Building"));
        request.setAttribute(
            "selectRoomCriteria_Floor",
            readRequestValue(request, "selectRoomCriteria_Floor"));
        request.setAttribute(
            "selectRoomCriteria_Type",
            readRequestValue(request, "selectRoomCriteria_Type"));
        request.setAttribute(
            "selectRoomCriteria_CapacityNormal",
            readRequestValue(request, "selectRoomCriteria_CapacityNormal"));
        request.setAttribute(
            "selectRoomCriteria_CapacityExame",
            readRequestValue(request, "selectRoomCriteria_CapacityExame"));

    }

    /**
	 * @param request
	 */
    public static void setSelectedRoomIndexContext(HttpServletRequest request)
    {
        String selectedRoomIndexString =
            (String) request.getAttribute(SessionConstants.SELECTED_ROOM_INDEX);

        if (selectedRoomIndexString == null)
        {
            selectedRoomIndexString = request.getParameter(SessionConstants.SELECTED_ROOM_INDEX);
        }

        Integer selectedRoomIndex = null;
        if (selectedRoomIndexString != null)
        {
            selectedRoomIndex = new Integer(selectedRoomIndexString);
            // Place it in request
            request.setAttribute(SessionConstants.SELECTED_ROOM_INDEX, selectedRoomIndex);
        } else
        {
            System.out.println("ERROR: Missing selectedRoomIndex in request");
        }
    }

    public static void prepareChangeExecutionDegreeAndCurricularYear(HttpServletRequest request)
    {
        IUserView userView = SessionUtils.getUserView(request);

        InfoExecutionPeriod infoExecutionPeriod =
            (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);

        /* Obtain a list of curricular years */
        List labelListOfCurricularYears = getLabelListOfCurricularYears();
        request.setAttribute(SessionConstants.LABELLIST_CURRICULAR_YEARS, labelListOfCurricularYears);

        /* Obtain a list of degrees for the specified execution year */
        Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear()};
        List executionDegreeList = null;
        try
        {
            executionDegreeList =
                (List) ServiceUtils.executeService(
                    userView,
                    "ReadExecutionDegreesByExecutionYear",
                    argsLerLicenciaturas);

            /* Sort the list of degrees */
            Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
        } catch (FenixServiceException e)
        {
        
            e.printStackTrace();
        }

        /* Generate a label list for the above list of degrees */
        List labelListOfExecutionDegrees = getLabelListOfExecutionDegrees(executionDegreeList);
        request.setAttribute("licenciaturas", labelListOfExecutionDegrees);

        ///////////////////////////////////////////////////////////////////////
        // TODO : place the following code in a seperate function and call it
        //        here and anywhere else the execution period may be selected.

        //HttpSession session = request.getSession(false);

        //IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");

        InfoExecutionPeriod selectedExecutionPeriod =
            (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);

        Object argsReadExecutionPeriods[] = {
        };
        ArrayList executionPeriods;
        try
        {
            executionPeriods =
                (ArrayList) ServiceManagerServiceFactory.executeService(userView, "ReadNotClosedExecutionPeriods", argsReadExecutionPeriods);

            selectedExecutionPeriod.getInfoExecutionYear().getYear();
            selectedExecutionPeriod.getSemester();
            ComparatorChain chainComparator = new ComparatorChain();
            chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"));
            chainComparator.addComparator(new BeanComparator("semester"));
            Collections.sort(executionPeriods, chainComparator);

            ArrayList executionPeriodsLabelValueList = new ArrayList();
            for (int i = 0; i < executionPeriods.size(); i++)
            {
                InfoExecutionPeriod infoExecutionPeriod2 = (InfoExecutionPeriod) executionPeriods.get(i);
                executionPeriodsLabelValueList.add(
                    new LabelValueBean(
                        infoExecutionPeriod2.getName()
                            + " - "
                            + infoExecutionPeriod2.getInfoExecutionYear().getYear(),
                        "" + infoExecutionPeriod2.getIdInternal()));
            }

            request.setAttribute(SessionConstants.LIST_INFOEXECUTIONPERIOD, executionPeriods);

            request.setAttribute(
                SessionConstants.LABELLIST_EXECUTIONPERIOD,
                executionPeriodsLabelValueList);
        } catch (FenixServiceException e1)
        {
        }

    }

    // -------------------------------------------------------------------------------
    // Read from request utils
    // -------------------------------------------------------------------------------
    private static String readRequestValue(HttpServletRequest request, String name)
    {
        String obj = null;
        if (((String) request.getAttribute(name)) != null
            && !((String) request.getAttribute(name)).equals(""))
            obj = (String) request.getAttribute(name);
        else if (
            request.getParameter(name) != null
                && !request.getParameter(name).equals("")
                && !request.getParameter(name).equals("null"))
            obj = request.getParameter(name);

        return obj;
    }

    private static Integer readIntegerRequestValue(HttpServletRequest request, String name)
    {
        String obj = readRequestValue(request, name);
        if (obj != null)
            return new Integer(obj);
        else
            return null;
    }

    private static TipoSala readTypeRoomRequestValue(HttpServletRequest request, String name)
    {
        Integer obj = readIntegerRequestValue(request, name);
        if (obj != null)
            return new TipoSala(obj);
        else
            return null;
    }

    public static List getLabelListOfCurricularYears()
    {
        ArrayList labelListOfCurricularYears = new ArrayList();
        labelListOfCurricularYears.add(new LabelValueBean("escolher", ""));
        labelListOfCurricularYears.add(new LabelValueBean("1 �", "1"));
        labelListOfCurricularYears.add(new LabelValueBean("2 �", "2"));
        labelListOfCurricularYears.add(new LabelValueBean("3 �", "3"));
        labelListOfCurricularYears.add(new LabelValueBean("4 �", "4"));
        labelListOfCurricularYears.add(new LabelValueBean("5 �", "5"));
        return labelListOfCurricularYears;
    }

    public static List getLabelListOfOptionalCurricularYears()
    {
        ArrayList labelListOfCurricularYears = new ArrayList();
        labelListOfCurricularYears.add(new LabelValueBean("todos", ""));
        labelListOfCurricularYears.add(new LabelValueBean("1 �", "1"));
        labelListOfCurricularYears.add(new LabelValueBean("2 �", "2"));
        labelListOfCurricularYears.add(new LabelValueBean("3 �", "3"));
        labelListOfCurricularYears.add(new LabelValueBean("4 �", "4"));
        labelListOfCurricularYears.add(new LabelValueBean("5 �", "5"));
        return labelListOfCurricularYears;
    }

    public static List getLabelListOfExecutionDegrees(List executionDegreeList)
    {
        List labelListOfExecutionDegrees =
            (List) CollectionUtils.collect(
                executionDegreeList,
                new EXECUTION_DEGREE_2_EXECUTION_DEGREE_LABEL());
		labelListOfExecutionDegrees.add(0, new LabelValueBean("escolher", ""));
        return labelListOfExecutionDegrees;
    }

    private static class EXECUTION_DEGREE_2_EXECUTION_DEGREE_LABEL implements Transformer
    {

        /*
		 * (non-Javadoc)
		 * 
		 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
		 */
        public Object transform(Object arg0)
        {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arg0;

            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name =
                infoExecutionDegree
                    .getInfoDegreeCurricularPlan()
                    .getInfoDegree()
                    .getTipoCurso()
                    .toString()
                    + " de "
                    + name;

            return new LabelValueBean(name, infoExecutionDegree.getIdInternal().toString());
        }
    }

	/* ************************************************************
		Novos metodos para os exames 
	************************************************************* */
	
	public static ArrayList createCurricularYearList()
	{				
		ArrayList anosCurriculares = new ArrayList();
		
		anosCurriculares.add(new LabelValueBean("1 �", "1"));
		anosCurriculares.add(new LabelValueBean("2 �", "2"));
		anosCurriculares.add(new LabelValueBean("3 �", "3"));
		anosCurriculares.add(new LabelValueBean("4 �", "4"));
		anosCurriculares.add(new LabelValueBean("5 �", "5"));
	
		return anosCurriculares;
}	
	public static ArrayList createExecutionDegreeList(HttpServletRequest request) throws FenixServiceException
	{
		IUserView userView = SessionUtils.getUserView(request);
		
		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);

		/* Cria o form bean com as licenciaturas em execucao.*/
		Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear()};		
			
		List executionDegreeList = (List) ServiceUtils.executeService(userView, "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);

		ArrayList licenciaturas = new ArrayList();

		Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

		Iterator iterator = executionDegreeList.iterator();

		int index = 0;
		while (iterator.hasNext()) {
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
			String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

			name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso().toString() + " em " + name;

			name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree)
				? "-" + infoExecutionDegree.getInfoDegreeCurricularPlan().getName()
				: "";

			licenciaturas.add(new LabelValueBean(name, infoExecutionDegree.getIdInternal().toString()));
		}		

		return licenciaturas;
	}
	
	
	/**
	 * Method existencesOfInfoDegree.
	 * @param executionDegreeList
	 * @param infoExecutionDegree
	 * @return int
	 */
	private static boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {
		InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
		Iterator iterator = executionDegreeList.iterator();
	
		while (iterator.hasNext()) {
			InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
			if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
				&& !(infoExecutionDegree.equals(infoExecutionDegree2)))
				return true;
	
		}
		return false;
	}
	
}