/*
 * Created on 16/Set/2003, 15:30:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorApresentacao.Action.teacher;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import DataBeans.InfoFrequenta;
import DataBeans.InfoGroupProjectStudents;
import DataBeans.InfoGroupProperties;
import DataBeans.InfoSiteProjects;
import DataBeans.InfoSiteStudents;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 16/Set/2003, 15:30:39
 * 
 */
public class DownloadStudentList extends FenixAction
{
	static final String COLUMNS_HEADERS= "N�\tInscrito\tCurso\tNome\tE-mail";
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		HttpSession session= request.getSession(false);
		Integer objectCode= null;
		Integer shiftID= null;
		try   
		{
			shiftID= new Integer((String) request.getParameter("shiftCode"));
		}
		catch (NumberFormatException ex)
		{
			//ok, we don't want to view a shift's student list
		}
		String objectCodeString= request.getParameter("objectCode");
		if (objectCodeString == null)
		{
			objectCodeString= (String) request.getAttribute("objectCode");
		}
		objectCode= new Integer(objectCodeString);
		Integer scopeCode= null;
		String scopeCodeString= request.getParameter("scopeCode");
		if (scopeCodeString == null)
		{
			scopeCodeString= (String) request.getAttribute("scopeCode");
		}
		if (scopeCodeString != null)
		{
			scopeCode= new Integer(scopeCodeString);
		}
		UserView userView= (UserView) session.getAttribute("UserView");
		Object args[]= { objectCode, scopeCode };
		Object argsProjects[]= { objectCode };
		Object argsInfos[]= { objectCode };
		GestorServicos gestor= GestorServicos.manager();
		TeacherAdministrationSiteView siteView= null;
		InfoSiteProjects projects= null;
		List infosGroups= null;
		List attendacies= null;
		List shiftStudents= null;
		try
		{
			siteView=
				(TeacherAdministrationSiteView) gestor.executar(userView, "ReadStudentsByCurricularCourse", args);

			projects= (InfoSiteProjects) gestor.executar(userView,"teacher.ReadExecutionCourseProjects", argsProjects);
			
			infosGroups=
				(List) gestor.executar(userView, "teacher.GetProjectsGroupsByExecutionCourseID", argsInfos);
			InfoSiteStudents infoSiteStudents= (InfoSiteStudents) siteView.getComponent();
			Collections.sort(infoSiteStudents.getStudents(), new BeanComparator("number"));
			if (shiftID != null)
			{
				//the objectCode is needed by the filter...doing this is awfull !!!
				//please read http://www.dcc.unicamp.br/~oliva/fun/prog/resign-patterns
				Object[] argsReadShiftStudents= { objectCode, shiftID };
				shiftStudents=
					(List) gestor.executar(userView, "teacher.ReadStudentsByShiftID", argsReadShiftStudents);
				infoSiteStudents.setStudents(shiftStudents);
			}
			Object[] argsAttendacies= { objectCode, infoSiteStudents.getStudents()};
			attendacies= (List) gestor.executar(userView, "teacher.GetAttendaciesByStudentList", argsAttendacies);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		String result= new String(COLUMNS_HEADERS);
		if (projects != null && projects.getInfoGroupPropertiesList() != null)
		{
			for (Iterator iter= projects.getInfoGroupPropertiesList().iterator(); iter.hasNext();)
			{
				InfoGroupProperties infoGroupProperties= (InfoGroupProperties) iter.next();
				result += "\t" + "\"" + "Grupo do Projecto " + infoGroupProperties.getName() + "\"";
			}
		}
		result += "\n";
		
		for (Iterator attendaciesIterator= attendacies.iterator(); attendaciesIterator.hasNext();)
		{
			InfoFrequenta infoFrequenta= (InfoFrequenta) attendaciesIterator.next();
			result += infoFrequenta.getAluno().getNumber();
			result += "\t";
			if (infoFrequenta.getInfoEnrolment() == null)
				result += "N�o";
			else
				result += "Sim";
			result += "\t";
			if (infoFrequenta.getInfoEnrolment() == null)
				result += "N/A";
			else
				result += "\""
					+ infoFrequenta
						.getInfoEnrolment()
						.getInfoStudentCurricularPlan()
						.getInfoDegreeCurricularPlan()
						.getName()
					+ "\"";
			result += "\t" + "\"" + infoFrequenta.getAluno().getInfoPerson().getNome() + "\"";
			result += "\t" + "\"" + infoFrequenta.getAluno().getInfoPerson().getEmail() + "\"";
			if (projects != null && projects.getInfoGroupPropertiesList() != null)
			{
				for (Iterator projectsIterator= projects.getInfoGroupPropertiesList().iterator();
					projectsIterator.hasNext();
					)
				{
					InfoGroupProperties infoGroupProperties= (InfoGroupProperties) projectsIterator.next();
					int projectIdInternal= infoGroupProperties.getIdInternal().intValue();
					int groupNumber= -1;
					int shiftCode= -1;
					int executionCourseCode= -1;
					int groupPropertiesCode= -1;
					int studentGroupCode= -1;
					Integer studentNumber= infoFrequenta.getAluno().getNumber();
					for (Iterator groupsIterator= infosGroups.iterator(); groupsIterator.hasNext();)
					{
						InfoGroupProjectStudents groupInfo= (InfoGroupProjectStudents) groupsIterator.next();
						if (projectIdInternal
							== groupInfo.getStudentGroup().getGroupProperties().getIdInternal().intValue()
							&& groupInfo.isStudentMemberOfThisGroup(studentNumber))
						{
							groupNumber= groupInfo.getStudentGroup().getGroupNumber().intValue();
							studentGroupCode= groupInfo.getStudentGroup().getIdInternal().intValue();
							shiftCode= groupInfo.getStudentGroup().getShift().getIdInternal().intValue();
							executionCourseCode=
								groupInfo
									.getStudentGroup()
									.getGroupProperties()
									.getExecutionCourse()
									.getIdInternal()
									.intValue();
							break;
						}
					}
					if (groupNumber != -1)
						result += "\t" + groupNumber;
					else
						result += "\t" + "N/A";
				}
			}
			result += "\n";
		}
		try
		{
			ServletOutputStream writer= response.getOutputStream();
			response.setContentType("application/vnd.ms-excel");
			writer.print(result);
			writer.flush();
			response.flushBuffer();
		}
		catch (IOException e1)
		{
			throw new FenixActionException();
		}
		return null;
	}
}
