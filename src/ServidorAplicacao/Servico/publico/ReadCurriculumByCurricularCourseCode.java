
package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurriculum;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurriculum;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PeriodState;

/**
 * @author T�nia Pous�o 13/Nov/2003
 */
public class ReadCurriculumByCurricularCourseCode implements IService
{

	public ReadCurriculumByCurricularCourseCode()
	{

	}

	public InfoCurriculum run(Integer curricularCourseCode) throws FenixServiceException
	{

		InfoCurriculum infoCurriculum = null;
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
			IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
							.getIPersistentCurricularCourseScope();
			IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
			if (curricularCourseCode == null)
			{
				throw new FenixServiceException("nullCurricularCourse");
			}

			ICurricularCourse curricularCourse = new CurricularCourse();
			curricularCourse.setIdInternal(curricularCourseCode);

			curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(
							curricularCourse, false);
			if (curricularCourse == null)
			{
				throw new NonExistingServiceException();
			}

			ICurriculum curriculum = persistentCurriculum
							.readCurriculumByCurricularCourse(curricularCourse);
			if (curriculum != null)
			{
				infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
			} else
			{
				//Although doesn't exist CURRICULUM, an object is returned with
				// the correspond curricular course
				infoCurriculum = new InfoCurriculum();
				infoCurriculum.setInfoCurricularCourse(Cloner
								.copyCurricularCourse2InfoCurricularCourse(curricularCourse));
			}
			
			List infoExecutionCourses = buildExecutionCourses(persistentExecutionCourse, curricularCourse);
			infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);
						
			List activeInfoScopes = buildActiveScopes(persistentCurricularCourseScope,
							curricularCourse);
			infoCurriculum.getInfoCurricularCourse().setInfoScopes(activeInfoScopes);
		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
		return infoCurriculum;
	}
	
	private List buildExecutionCourses(IPersistentExecutionCourse persistentExecutionCourse,
					ICurricularCourse curricularCourse) throws ExcepcaoPersistencia
	{
		List infoExecutionCourses = new ArrayList();
		List executionCourses = curricularCourse.getAssociatedExecutionCourses();
		Iterator iterExecutionCourses = executionCourses.iterator();
		while (iterExecutionCourses.hasNext())
		{
			IExecutionCourse executionCourse = (IExecutionCourse) iterExecutionCourses.next();
			if (executionCourse.getExecutionPeriod().getState().equals(PeriodState.OPEN)
							|| executionCourse.getExecutionPeriod().getState().equals(
											PeriodState.CURRENT))
			{
				InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner
								.get(executionCourse);
				infoExecutionCourse.setHasSite(persistentExecutionCourse.readSite(executionCourse
								.getIdInternal()));
				infoExecutionCourses.add(infoExecutionCourse);
			}
		}
		return infoExecutionCourses;
	}

	private List buildActiveScopes(IPersistentCurricularCourseScope persistentCurricularCourseScope,
					ICurricularCourse curricularCourse) throws ExcepcaoPersistencia
	{
		//selects active curricular course scopes
		List activeCurricularCourseScopes = persistentCurricularCourseScope
						.readActiveCurricularCourseScopesByCurricularCourse(curricularCourse);

		activeCurricularCourseScopes = (List) CollectionUtils.select(activeCurricularCourseScopes,
						new Predicate()
						{
							public boolean evaluate(Object arg0)
							{
								ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
								if (curricularCourseScope.isActive().booleanValue())
								{
									return true;
								}
								return false;
							}
						});

		List activeInfoScopes = (List) CollectionUtils.collect(activeCurricularCourseScopes,
						new Transformer()
						{

							public Object transform(Object arg0)
							{

								return Cloner
												.copyICurricularCourseScope2InfoCurricularCourseScope((ICurricularCourseScope) arg0);
							}
						});
		return activeInfoScopes;
	}

	/*
	 * private InfoCurriculum fillScopesAndExecutionCourses(InfoCurriculum
	 * infoCurriculum, ICurriculum curriculum) throws ExcepcaoPersistencia {
	 * List scopes = new ArrayList();
	 * infoCurriculum.getInfoCurricularCourse().setInfoScopes(scopes);
	 * 
	 * List infoExecutionCourses = new ArrayList(); List executionCourses =
	 * curriculum.getCurricularCourse().getAssociatedExecutionCourses();
	 * Iterator iterExecutionCourses = executionCourses.iterator(); while
	 * (iterExecutionCourses.hasNext()) { IExecutionCourse executionCourse =
	 * (IExecutionCourse) iterExecutionCourses.next(); if
	 * (executionCourse.getExecutionPeriod().getState().equals(PeriodState.OPEN) ||
	 * executionCourse.getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
	 * InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse)
	 * Cloner.get(executionCourse); infoExecutionCourse.setHasSite(
	 * persistentExecutionCourse.readSite(executionCourse.getIdInternal()));
	 * infoExecutionCourses.add(infoExecutionCourse); } }
	 * infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);
	 * 
	 * return infoCurriculum; }
	 */
}