/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Constants
 * 
 * Created on 3/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop.utils;

/**
 * @author jpvl
 *
 * 
 */
public abstract class SessionConstants {
	public static final String CONTEXT_PREFIX = "context.";

	public static final String CONTEXT_KEY =
		CONTEXT_PREFIX + "anoCurricularAndSemestreAndInfoLicenciaturaExecucao";
	public static final String INFO_LIC_EXEC_KEY =
		CONTEXT_PREFIX + "infoLicenciaturaExecucao";
	public static final String U_VIEW = "UserView";
	public static final String CLASS_VIEW = "classView";
	public static final String EXECUTION_COURSE_KEY = "infoDisciplinaExecucao";
	public static final String LESSON_LIST_ATT = "lessonList";
	public static final String EXECUTION_COURSE_LIST_KEY =
		CONTEXT_PREFIX + "disciplinasExecucao";
	public static final String INFO_STUDENT_KEY = "infoStudent";
	
	public static final String SEMESTER_LIST_KEY = "semester.list";
	public static final String INFO_DEGREE_LIST_KEY = "infoDegree.list";
	public static final String CURRICULAR_YEAR_LIST_KEY = "curricularYear.list";
	public static final String SHIFT_VIEW = "shiftview";
	public static final String INFO_SHIFTS_EXECUTION_COURSE_KEY = "infoTurnosDeDisciplinaExecucao";
	public static final String INFO_EXECUTION_PERIOD_KEY = "info_execution_period_key"; 
}
