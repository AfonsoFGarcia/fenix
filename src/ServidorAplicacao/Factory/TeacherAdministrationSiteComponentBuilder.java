package ServidorAplicacao.Factory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.slide.common.SlideException;

import DataBeans.ISiteComponent;
import DataBeans.InfoAnnouncement;
import DataBeans.InfoBibliographicReference;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurriculum;
import DataBeans.InfoEvaluation;
import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoGroupProperties;
import DataBeans.InfoItem;
import DataBeans.InfoLink;
import DataBeans.InfoSection;
import DataBeans.InfoShift;
import DataBeans.InfoSite;
import DataBeans.InfoSiteAllGroups;
import DataBeans.InfoSiteAnnouncement;
import DataBeans.InfoSiteBibliography;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteEvaluation;
import DataBeans.InfoSiteEvaluationMethods;
import DataBeans.InfoSiteExam;
import DataBeans.InfoSiteGroupProperties;
import DataBeans.InfoSiteGroupsByShift;
import DataBeans.InfoSiteInstructions;
import DataBeans.InfoSiteItems;
import DataBeans.InfoSiteObjectives;
import DataBeans.InfoSitePrograms;
import DataBeans.InfoSiteProjects;
import DataBeans.InfoSiteRegularSections;
import DataBeans.InfoSiteRootSections;
import DataBeans.InfoSiteSection;
import DataBeans.InfoSiteSections;
import DataBeans.InfoSiteShifts;
import DataBeans.InfoSiteStudentGroup;
import DataBeans.InfoSiteStudentInformation;
import DataBeans.InfoSiteTeachers;
import DataBeans.InfoStudentGroupAttend;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.Announcement;
import Dominio.BibliographicReference;
import Dominio.CurricularCourse;
import Dominio.DisciplinaExecucao;
import Dominio.Evaluation;
import Dominio.EvaluationExecutionCourse;
import Dominio.FinalEvaluation;
import Dominio.GroupProperties;
import Dominio.IAnnouncement;
import Dominio.IAula;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluation;
import Dominio.IEvalutionExecutionCourse;
import Dominio.IExam;
import Dominio.IFinalEvaluation;
import Dominio.IGroupProperties;
import Dominio.IItem;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITeacher;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.Item;
import Dominio.Section;
import Dominio.StudentGroup;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentEvaluationExecutionCourse;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import fileSuport.FileSuport;
import fileSuport.FileSuportObject;
import fileSuport.IFileSuport;

/**
 * @author Fernanda Quit�rio
 *
 */
public class TeacherAdministrationSiteComponentBuilder {

	private static TeacherAdministrationSiteComponentBuilder instance = null;

	public TeacherAdministrationSiteComponentBuilder() {
	}

	public static TeacherAdministrationSiteComponentBuilder getInstance() {
		if (instance == null) {
			instance = new TeacherAdministrationSiteComponentBuilder();
		}
		return instance;
	}

	public ISiteComponent getComponent(ISiteComponent component, ISite site, ISiteComponent commonComponent, Object obj1, Object obj2) throws FenixServiceException {
		
		if (component instanceof InfoSiteCommon) {
			return getInfoSiteCommon((InfoSiteCommon) component, site);
		} else if (component instanceof InfoSiteInstructions) {
			return getInfoSiteInstructions((InfoSiteInstructions) component, site);
		} else if (component instanceof InfoSite) {
			return getInfoSiteCustomizationOptions((InfoSite) component, site);
		} else if (component instanceof InfoSiteAnnouncement) {
			return getInfoSiteAnnouncement((InfoSiteAnnouncement) component, site);
		} else if (component instanceof InfoAnnouncement) {
			return getInfoAnnouncement((InfoAnnouncement) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteObjectives) {
			return getInfoSiteObjectives((InfoSiteObjectives) component, site);
		} else if (component instanceof InfoSitePrograms) {
			return getInfoSitePrograms((InfoSitePrograms) component, site);
		} else if (component instanceof InfoCurriculum) {
			return getInfoCurriculum((InfoCurriculum) component, site,(Integer) obj1);
		} else if (component instanceof InfoSiteEvaluationMethods) {
			return getInfoEvaluationMethods((InfoSiteEvaluationMethods) component, site);
		//} else if (component instanceof InfoCurriculum) {
		//	return getInfoEvaluationMethod((InfoCurriculum) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteBibliography) {
			return getInfoSiteBibliography((InfoSiteBibliography) component, site);
		} else if (component instanceof InfoBibliographicReference) {
			return getInfoBibliographicReference((InfoBibliographicReference) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteTeachers) {
			return getInfoSiteTeachers((InfoSiteTeachers) component, site, (String) obj2);
		} else if (component instanceof InfoSiteEvaluation) {
			return getInfoSiteEvaluation((InfoSiteEvaluation) component, site);
		} else if (component instanceof InfoSiteExam) {
			return getInfoSiteExam((InfoSiteExam) component, site);
		} else if (component instanceof InfoSiteRootSections) {
			return getInfoSiteRootSections((InfoSiteRootSections) component, site);
		} else if (component instanceof InfoEvaluation) {
			return getInfoEvaluation((InfoEvaluation) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteSection) {
			return getInfoSiteSection((InfoSiteSection) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteRegularSections) {
			return getInfoSiteRegularSections((InfoSiteRegularSections) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteSections) {
			return getInfoSiteSections((InfoSiteSections) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteItems) {
			return getInfoSiteItems((InfoSiteItems) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteProjects) {
			return getInfoSiteProjects((InfoSiteProjects) component, site);
		} else if (component instanceof InfoSiteAllGroups) {
			return getInfoSiteAllGroups((InfoSiteAllGroups) component, (Integer) obj1);
		} else if (component instanceof InfoSiteStudentGroup) {
			return getInfoSiteStudentGroup((InfoSiteStudentGroup) component, (Integer) obj1);
		} else if (component instanceof InfoSiteGroupProperties) {
			return getInfoSiteGroupProperties((InfoSiteGroupProperties) component, (Integer) obj1);
		} else if (component instanceof InfoSiteShifts) {
			return getInfoSiteShifts((InfoSiteShifts) component, (Integer) obj1,(Integer) obj2);
		}
		return null;
	}

	/**
	 * @param common
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteCommon(InfoSiteCommon component, ISite site) throws FenixServiceException {

		ISuportePersistente sp;
		List allSections = null;
		List infoSectionsList = null;
		try {
			// read sections	

			sp = SuportePersistenteOJB.getInstance();
			allSections = sp.getIPersistentSection().readBySite(site);

			// build the result of this service
			Iterator iterator = allSections.iterator();
			infoSectionsList = new ArrayList(allSections.size());

			while (iterator.hasNext())
				infoSectionsList.add(Cloner.copyISection2InfoSection((ISection) iterator.next()));

			Collections.sort(infoSectionsList);

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
		component.setTitle(site.getExecutionCourse().getNome());
		component.setMail(site.getMail());
		component.setSections(infoSectionsList);
		InfoExecutionCourse executionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(site.getExecutionCourse());
		component.setExecutionCourse(executionCourse);
		return component;
	}

	/**
	 * @param component
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteInstructions(InfoSiteInstructions component, ISite site) {

		return component;
	}

	/**
	 * @param component
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteCustomizationOptions(InfoSite component, ISite site) {
		component.setAlternativeSite(site.getAlternativeSite());
		component.setMail(site.getMail());
		component.setInitialStatement(site.getInitialStatement());
		component.setIntroduction(site.getIntroduction());
		component.setIdInternal(site.getIdInternal());
		component.setInfoExecutionCourse(Cloner.copyIExecutionCourse2InfoExecutionCourse(site.getExecutionCourse()));
		component.setStyle(site.getStyle());

		return component;
	}

	private InfoSiteAnnouncement getInfoSiteAnnouncement(InfoSiteAnnouncement component, ISite site) throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			List announcementsList = sp.getIPersistentAnnouncement().readAnnouncementsBySite(site);
			List infoAnnouncementsList = new ArrayList();

			if (announcementsList != null && announcementsList.isEmpty() == false) {
				Iterator iterAnnouncements = announcementsList.iterator();
				while (iterAnnouncements.hasNext()) {
					IAnnouncement announcement = (IAnnouncement) iterAnnouncements.next();
					infoAnnouncementsList.add(Cloner.copyIAnnouncement2InfoAnnouncement(announcement));
				}
			}

			component.setAnnouncements(infoAnnouncementsList);
			return component;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	/**
	 * @param announcement
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoAnnouncement(InfoAnnouncement component, ISite site, Integer announcementCode) throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			Announcement announcement = new Announcement(announcementCode);
			IAnnouncement iAnnouncement = (IAnnouncement) sp.getIPersistentAnnouncement().readByOId(announcement, false);
			InfoAnnouncement infoAnnouncement = Cloner.copyIAnnouncement2InfoAnnouncement(iAnnouncement);

			component.setCreationDate(infoAnnouncement.getCreationDate());
			component.setIdInternal(infoAnnouncement.getIdInternal());
			component.setInformation(infoAnnouncement.getInformation());
			component.setInfoSite(infoAnnouncement.getInfoSite());
			component.setLastModifiedDate(infoAnnouncement.getLastModifiedDate());
			component.setTitle(infoAnnouncement.getTitle());
			return component;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	/**
	 * @param objectives
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteObjectives(InfoSiteObjectives component, ISite site) throws FenixServiceException {
		try {	
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
			
				IDisciplinaExecucao executionCourse = site.getExecutionCourse();				
				List curricularCourses = executionCourse.getAssociatedCurricularCourses();
				Iterator iter = curricularCourses.iterator();
				List infoCurriculums = new ArrayList();
			
				while (iter.hasNext()) {
					ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
					ICurriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse);
				
					if (curriculum != null) {
						infoCurriculums.add(Cloner.copyICurriculum2InfoCurriculum(curriculum));
					}			
				}
				component.setInfoCurriculums(infoCurriculums);
				component.setInfoCurricularCourses(readInfoCurricularCourses(site));
			
			} catch (ExcepcaoPersistencia e) {
				throw new FenixServiceException(e);
			}

			return component;
	}
	
	
	/**
	 * @param program
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSitePrograms(InfoSitePrograms component, ISite site) throws FenixServiceException {
			try {	
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
			
				IDisciplinaExecucao executionCourse = site.getExecutionCourse();				
				List curricularCourses = executionCourse.getAssociatedCurricularCourses();
				Iterator iter = curricularCourses.iterator();
				List infoCurriculums = new ArrayList();
			
					while (iter.hasNext()) {
						ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
						ICurriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse);
																	
						if (curriculum != null) {
							InfoCurriculum infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
							infoCurriculum.setInfoCurricularCourse(Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse));
							infoCurriculums.add(infoCurriculum);
						}			
					}
					component.setInfoCurriculums(infoCurriculums);
					component.setInfoCurricularCourses(readInfoCurricularCourses(site));
					
					
				} catch (ExcepcaoPersistencia e) {
					throw new FenixServiceException(e);
				}

				return component;
			}
	
	/**
	 * @param evaluation
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoEvaluationMethods(InfoSiteEvaluationMethods component, ISite site) throws FenixServiceException {
		try {
						
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
			
			IDisciplinaExecucao executionCourse = site.getExecutionCourse();				
			List curricularCourses = executionCourse.getAssociatedCurricularCourses();
			Iterator iter = curricularCourses.iterator();
			List infoEvaluationMethods = new ArrayList();
			
			while (iter.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
				ICurriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse);
				
				if (curriculum != null) {
					infoEvaluationMethods.add(Cloner.copyICurriculum2InfoCurriculum(curriculum));
				}			
			}
			component.setInfoEvaluations(infoEvaluationMethods);
			component.setInfoCurricularCourses(readInfoCurricularCourses(site));
			
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return component;
	}
	
	/**
	* @param methods
	* @param site
	* @param integer
	* @return
	*/
	private ISiteComponent getInfoEvaluationMethod(InfoCurriculum component, ISite site, Integer curricularCourseCode) throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			ICurricularCourse curricularCourse = new CurricularCourse(curricularCourseCode);
			curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, false);
			IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
			//if(curricularCourse.getBasic().booleanValue()){
			//		throw new IllegalEditException();
			//}	
			ICurriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse);
			InfoCurriculum infoCurriculum = null;
		
			if (curriculum != null) {
				infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
			}
			
			return infoCurriculum;
			
			} catch (ExcepcaoPersistencia e) {
				throw new FenixServiceException(e);
			}
		}

	/**
		* @param methods
		* @param site
		* @param integer
		* @return
		*/
		private ISiteComponent getInfoCurriculum(InfoCurriculum component, ISite site, Integer curricularCourseCode) throws FenixServiceException {
			try {
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
				ICurricularCourse curricularCourse = new CurricularCourse(curricularCourseCode);
				curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, false);
				IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
				
				ICurriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse);
				InfoCurriculum infoCurriculum = null;
				
				if (curriculum != null) {
					infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
					infoCurriculum.setInfoCurricularCourse(Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse));
				}
				
				return infoCurriculum;
			
				} catch (ExcepcaoPersistencia e) {
					throw new FenixServiceException(e);
				}
			}



	/**
	 * @param bibliography
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteBibliography(InfoSiteBibliography component, ISite site) throws FenixServiceException {
		List references = null;
		List infoBibRefs = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IPersistentBibliographicReference persistentBibliographicReference = persistentBibliographicReference = sp.getIPersistentBibliographicReference();

			IDisciplinaExecucao executionCourse = site.getExecutionCourse();

			references = persistentBibliographicReference.readBibliographicReference(executionCourse);

			if (references != null) {
				Iterator iterator = references.iterator();
				infoBibRefs = new ArrayList();
				while (iterator.hasNext()) {
					IBibliographicReference bibRef = (IBibliographicReference) iterator.next();

					InfoBibliographicReference infoBibRef = Cloner.copyIBibliographicReference2InfoBibliographicReference(bibRef);
					infoBibRefs.add(infoBibRef);

				}
				component.setBibliographicReferences(infoBibRefs);
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return component;
	}

	/**
	 * @param reference
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoBibliographicReference(InfoBibliographicReference component, ISite site, Integer bibliographicReferenceCode) throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			BibliographicReference bibliographicReference = new BibliographicReference(bibliographicReferenceCode);
			IBibliographicReference iBibliographicReference = (IBibliographicReference) sp.getIPersistentBibliographicReference().readByOId(bibliographicReference, false);
			InfoBibliographicReference infoBibliographicReference = Cloner.copyIBibliographicReference2InfoBibliographicReference(iBibliographicReference);

			component.setTitle(infoBibliographicReference.getTitle());
			component.setAuthors(infoBibliographicReference.getAuthors());
			component.setReference(infoBibliographicReference.getReference());
			component.setYear(infoBibliographicReference.getYear());
			component.setOptional(infoBibliographicReference.getOptional());
			component.setIdInternal(infoBibliographicReference.getIdInternal());
			return component;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	/**
	 * @param teachers
	 * @param site
	 * @param username
	 * @return
	 */
	private ISiteComponent getInfoSiteTeachers(InfoSiteTeachers component, ISite site, String username) throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();

			IDisciplinaExecucao executionCourse = site.getExecutionCourse();
			List teachers = persistentProfessorship.readByExecutionCourse(executionCourse);
			List infoTeachers = new ArrayList();
			if (teachers != null) {

				Iterator iter = teachers.iterator();
				while (iter.hasNext()) {
					IProfessorship professorship = (IProfessorship) iter.next();
					ITeacher teacher = professorship.getTeacher();
					InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
					infoTeachers.add(infoTeacher);
				}

				// see if teacher is responsible for that execution course
				IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
				IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
				List responsibleTeachers = persistentResponsibleFor.readByExecutionCourse(executionCourse);

				List infoResponsibleTeachers = new ArrayList();
				boolean isResponsible = false;
				if (responsibleTeachers != null) {
					Iterator iter2 = responsibleTeachers.iterator();
					while (iter2.hasNext()) {
						IResponsibleFor responsibleFor = (IResponsibleFor) iter2.next();
						ITeacher teacher = responsibleFor.getTeacher();
						InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
						infoResponsibleTeachers.add(infoTeacher);
					}

					ITeacher teacher = persistentTeacher.readTeacherByUsername(username);
					IResponsibleFor responsibleFor = persistentResponsibleFor.readByTeacherAndExecutionCourse(teacher, executionCourse);
					if (teacher != null) {
						if (responsibleTeachers != null && !responsibleTeachers.isEmpty() && responsibleTeachers.contains(responsibleFor)) {
							isResponsible = true;
						}
					}
				}

				component.setInfoTeachers(infoTeachers);
				component.setIsResponsible(new Boolean(isResponsible));
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return component;
	}

	/**
	 * 
	 * @param component
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteEvaluation(InfoSiteEvaluation component, ISite site) throws FenixServiceException {
		IDisciplinaExecucao executionCourse = site.getExecutionCourse();

		List evaluations = executionCourse.getAssociatedEvaluations();
		Iterator iter = evaluations.iterator();

		boolean hasFinalEvaluation = false;
		List infoEvaluations = new ArrayList();
		List infoFinalEvaluations = new ArrayList();
		while (iter.hasNext()) {
			IEvaluation evaluation = (IEvaluation) iter.next();

			if (evaluation instanceof IExam) {
				infoEvaluations.add(Cloner.copyIEvaluation2InfoEvaluation(evaluation));
			} else if (evaluation instanceof IFinalEvaluation) {
				hasFinalEvaluation = true;
				infoFinalEvaluations.add(Cloner.copyIEvaluation2InfoEvaluation(evaluation));
			}
		}

		if (!hasFinalEvaluation) {
			ISuportePersistente sp;
			IFinalEvaluation finalEvaluation = null;
			try {
				sp = SuportePersistenteOJB.getInstance();
				IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();

				finalEvaluation = new FinalEvaluation();
				persistentEvaluation.lockWrite(finalEvaluation);

				//associate final evaluation to execution course				
				IPersistentEvaluationExecutionCourse persistentEvaluationExecutionCourse = sp.getIPersistentEvaluationExecutionCourse();
				IEvalutionExecutionCourse evalutionExecutionCourse = new EvaluationExecutionCourse();
				evalutionExecutionCourse.setEvaluation(finalEvaluation);
				evalutionExecutionCourse.setExecutionCourse(executionCourse);

				persistentEvaluationExecutionCourse.lockWrite(evalutionExecutionCourse);
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace(System.out);
				throw new FenixServiceException(e);
			}

			//add final evaluation to evaluation list
			infoFinalEvaluations.add(Cloner.copyIEvaluation2InfoEvaluation(finalEvaluation));
		}

		ComparatorChain comparatorChain = new ComparatorChain();
		comparatorChain.addComparator(new BeanComparator("day.time"));
		comparatorChain.addComparator(new BeanComparator("beginning.time"));

		Collections.sort(infoEvaluations, comparatorChain);

		//merge two list
		infoEvaluations.addAll(infoFinalEvaluations);

		component.setInfoEvaluations(infoEvaluations);
		return component;
	}

	/**
	 * 
	 * @param component
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteExam(InfoSiteExam component, ISite site) {
		IDisciplinaExecucao executionCourse = site.getExecutionCourse();
		List exams = executionCourse.getAssociatedExams();
		List infoExams = new ArrayList();
		Iterator iter = exams.iterator();
		while (iter.hasNext()) {
			IExam exam = (IExam) iter.next();
			infoExams.add(Cloner.copyIExam2InfoExam(exam));
		}
		component.setInfoExams(infoExams);
		return component;
	}

	/**
	 * @param evaluation
	 * @param site
	 * @return
	 */

	private ISiteComponent getInfoEvaluation(InfoEvaluation component, ISite site, Integer evaluationCode) throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();

			IEvaluation evaluation = new Evaluation();
			evaluation.setIdInternal(evaluationCode);
			evaluation = (IEvaluation) persistentEvaluation.readByOId(evaluation, false);
			InfoEvaluation infoEvaluation = Cloner.copyIEvaluation2InfoEvaluation(evaluation);

			if (infoEvaluation instanceof InfoExam) {
				InfoExam infoExam = (InfoExam) infoEvaluation;
				InfoExam examComponent = new InfoExam();

				examComponent.setAssociatedRooms(infoExam.getAssociatedRooms());
				examComponent.setEvaluationType(infoExam.getEvaluationType());
				examComponent.setInfoExecutionCourse(infoExam.getInfoExecutionCourse());
				examComponent.setSeason(infoExam.getSeason());
				try {
					BeanUtils.copyProperties(examComponent, infoExam);
				} catch (IllegalAccessException e1) {
					throw new FenixServiceException(e1);
				} catch (InvocationTargetException e1) {
					throw new FenixServiceException(e1);
				}
				component = examComponent;
			} else if (evaluation instanceof IFinalEvaluation) {
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return component;
	}

	/**
	 * @param sections
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteRootSections(InfoSiteRootSections component, ISite site) throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			List allSections = sp.getIPersistentSection().readBySite(site);

			// build the result of this service
			Iterator iterator = allSections.iterator();
			List infoSectionsList = new ArrayList(allSections.size());

			while (iterator.hasNext()) {
				ISection section = (ISection) iterator.next();
				if (section.getSuperiorSection() == null) {
					infoSectionsList.add(Cloner.copyISection2InfoSection(section));
				}
			}
			Collections.sort(infoSectionsList);

			component.setRootSections(infoSectionsList);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return component;
	}

	/**
	 * @param section
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteSection(InfoSiteSection component, ISite site, Integer sectionCode) throws FenixServiceException {

		ISection iSection = null;
		List itemsList = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentSection persistentSection = sp.getIPersistentSection();
			IPersistentItem persistentItem = sp.getIPersistentItem();

			iSection = (ISection) persistentSection.readByOId(new Section(sectionCode), false);

			itemsList = persistentItem.readAllItemsBySection(iSection);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		List infoItemsList = new ArrayList(itemsList.size());
		Iterator iter = itemsList.iterator();
		IFileSuport fileSuport = FileSuport.getInstance();
		while (iter.hasNext()) {
			IItem item =(IItem) iter.next();
			InfoItem infoItem = Cloner.copyIItem2InfoItem(item);
			try {
				List files = fileSuport.getDirectoryFiles(item.getSlideName());
				if (files!=null && !files.isEmpty()) {
					List links = new ArrayList();
					Iterator iterFiles = files.iterator();
					while(iterFiles.hasNext()) {
						FileSuportObject file = (FileSuportObject) iterFiles.next();
						InfoLink infoLink = new InfoLink();
						infoLink.setLink(file.getFileName());
						infoLink.setLinkName(file.getLinkName());
						links.add(infoLink);
					}
					infoItem.setLinks(links);
				}
			} catch (SlideException e1) {
				//the item does not have a folder associated
			}
			infoItemsList.add(infoItem);
		}
		component.setSection(Cloner.copyISection2InfoSection(iSection));
		Collections.sort(infoItemsList);
		component.setItems(infoItemsList);

		return component;
	}

	/**
	 * @param sections
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteRegularSections(InfoSiteRegularSections component, ISite site, Integer sectionCode) throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentSection persistentSection = sp.getIPersistentSection();

			ISection iSuperiorSection = (ISection) persistentSection.readByOId(new Section(sectionCode), false);
			List allSections = persistentSection.readBySite(site);

			// build the result of this service
			Iterator iterator = allSections.iterator();
			List infoSectionsList = new ArrayList(allSections.size());
			while (iterator.hasNext()) {
				ISection section = (ISection) iterator.next();

				if (section.getSuperiorSection() != null && section.getSuperiorSection().equals(iSuperiorSection)) {
					infoSectionsList.add(Cloner.copyISection2InfoSection(section));
				}
			}
			Collections.sort(infoSectionsList);

			component.setRegularSections(infoSectionsList);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return component;
	}

	/**
	 * @param sections
	 * @param site
	 * @param integer
	 * @return
	 */
	private ISiteComponent getInfoSiteSections(InfoSiteSections component, ISite site, Integer sectionCode) throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentSection persistentSection = sp.getIPersistentSection();

			ISection iSection = (ISection) persistentSection.readByOId(new Section(sectionCode), false);
			InfoSection infoSection = Cloner.copyISection2InfoSection(iSection);
			List allSections = persistentSection.readBySite(site);

			// build the result of this service
			Iterator iterator = allSections.iterator();
			List infoSectionsList = new ArrayList(allSections.size());

			if (iSection.getSuperiorSection() == null) {
				while (iterator.hasNext()) {
					ISection section = (ISection) iterator.next();
					if ((section.getSuperiorSection() == null) && !section.getName().equals(iSection.getName())) {
						infoSectionsList.add(Cloner.copyISection2InfoSection(section));
					}
				}
			} else {
				while (iterator.hasNext()) {
					ISection section = (ISection) iterator.next();
					if ((section.getSuperiorSection() != null && section.getSuperiorSection().getIdInternal().equals(iSection.getSuperiorSection().getIdInternal()))
						&& !section.getName().equals(iSection.getName())) {
						infoSectionsList.add(Cloner.copyISection2InfoSection(section));
					}
				}
			}

			Collections.sort(infoSectionsList);
			component.setSection(infoSection);
			component.setSections(infoSectionsList);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return component;
	}

	/**
	 * @param items
	 * @param site
	 * @param integer
	 * @return
	 */
	private ISiteComponent getInfoSiteItems(InfoSiteItems component, ISite site, Integer itemCode) throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentItem persistentItem = sp.getIPersistentItem();
			IPersistentSection persistentSection = sp.getIPersistentSection();

			IItem iItem = (IItem) persistentItem.readByOId(new Item(itemCode), false);
			InfoItem infoItem = Cloner.copyIItem2InfoItem(iItem);

			ISection iSection = (ISection) persistentSection.readByOId(new Section(infoItem.getInfoSection().getIdInternal()), false);
			List allItems = persistentItem.readAllItemsBySection(iSection);

			// build the result of this service
			Iterator iterator = allItems.iterator();
			List infoItemsList = new ArrayList(allItems.size());

			while (iterator.hasNext()) {
				IItem item = (IItem) iterator.next();
				if (!item.getIdInternal().equals(iItem.getIdInternal())) {
					infoItemsList.add(Cloner.copyIItem2InfoItem(item));
				}
			}

			Collections.sort(infoItemsList);
			component.setItem(infoItem);
			component.setItems(infoItemsList);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return component;
	}

	private List readCurriculum(ISite site) throws ExcepcaoPersistencia {

		IDisciplinaExecucao executionCourse = site.getExecutionCourse();
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		List curricularCourses = executionCourse.getAssociatedCurricularCourses();
		List curriculums = new ArrayList();
		Iterator iter = curricularCourses.iterator();
		while (iter.hasNext()) {
			ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();

			ICurriculum curriculum = null;
			curriculum = sp.getIPersistentCurriculum().readCurriculumByCurricularCourse(curricularCourse);
			if (curriculum != null) {
				curriculums.add(curriculum);
			}

		}

		return curriculums;
	}

	private List readInfoCurricularCourses(ISite site) throws ExcepcaoPersistencia {

		IDisciplinaExecucao executionCourse = site.getExecutionCourse();
		List curricularCourses = executionCourse.getAssociatedCurricularCourses();
		Iterator iter = curricularCourses.iterator();
		List infoCurricularCourses = new ArrayList();
		while (iter.hasNext()) {
			ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
			InfoCurricularCourse infoCurricularCourse = Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
			infoCurricularCourses.add(infoCurricularCourse);
		}
		return infoCurricularCourses;
	}
	/**
		* @param component
		* @param site
		* @return
		*/

	private ISiteComponent getInfoSiteProjects(InfoSiteProjects component, ISite site) throws FenixServiceException {

		List infoGroupPropertiesList = readExecutionCourseProjects(site.getExecutionCourse().getIdInternal());
		component.setInfoGroupPropertiesList(infoGroupPropertiesList);
		return component;
	}

	public List readExecutionCourseProjects(Integer executionCourseCode) throws ExcepcaoInexistente, FenixServiceException {

		List projects = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) sp.getIDisciplinaExecucaoPersistente().readByOId(new DisciplinaExecucao(executionCourseCode), false);

			List executionCourseProjects = sp.getIPersistentGroupProperties().readAllGroupPropertiesByExecutionCourse(executionCourse);

			projects = new ArrayList();
			Iterator iterator = executionCourseProjects.iterator();

			while (iterator.hasNext()) {
				projects.add(Cloner.copyIGroupProperties2InfoGroupProperties((IGroupProperties) iterator.next()));

			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException("error.impossibleReadExecutionCourseProjects");
		}

		return projects;
	}

	/**
	* @param component
	* @param site
	* @param groupPropertiesCode
	* @return
	*/

	private ISiteComponent getInfoSiteAllGroups(InfoSiteAllGroups component, Integer groupPropertiesCode) throws FenixServiceException {

		List infoSiteGroupsByShiftList = readAllShiftsAndGroupsByProject(groupPropertiesCode);
		
		component.setInfoSiteGroupsByShiftList(infoSiteGroupsByShiftList);
		return component;
	}

	public List readAllShiftsAndGroupsByProject(Integer groupPropertiesCode) throws ExcepcaoInexistente, FenixServiceException {

		List infoSiteGroupsByShiftList = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IGroupProperties groupProperties = (IGroupProperties) sp.getIPersistentGroupProperties().readByOId(new GroupProperties(groupPropertiesCode), false);
			List allStudentsGroup = sp.getIPersistentStudentGroup().readAllStudentGroupByGroupProperties(groupProperties);

			ITurno shift = null;
			List shiftsInternalList = new ArrayList();
			Iterator iterator = allStudentsGroup.iterator();

			while (iterator.hasNext()) {
				shift = ((IStudentGroup) iterator.next()).getShift();
				if (!shiftsInternalList.contains(shift))
					shiftsInternalList.add(shift);
			}
			Iterator iterator2 = shiftsInternalList.iterator();
			List studentGroupsList = null;
			InfoSiteGroupsByShift infoSiteGroupsByShift = null;
			shift = null;
			infoSiteGroupsByShiftList = new ArrayList(shiftsInternalList.size());
			
			while (iterator2.hasNext()) {
				shift = (ITurno) iterator2.next();

				studentGroupsList = sp.getIPersistentStudentGroup().readAllStudentGroupByGroupPropertiesAndShift(groupProperties, shift);
				
				List infoStudentGroupList = new ArrayList(studentGroupsList.size());
				Iterator iter = studentGroupsList.iterator();
				while (iter.hasNext())
					infoStudentGroupList.add(Cloner.copyIStudentGroup2InfoStudentGroup((IStudentGroup) iter.next()));

				Collections.sort(infoStudentGroupList, new BeanComparator("groupNumber"));

				infoSiteGroupsByShift = new InfoSiteGroupsByShift();
				infoSiteGroupsByShift.setInfoStudentGroupsList(infoStudentGroupList);
				infoSiteGroupsByShift.setInfoShift(Cloner.copyIShift2InfoShift(shift));
				infoSiteGroupsByShiftList.add(infoSiteGroupsByShift);

			}

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException("error.impossibleReadAllShiftsByProject");
		}
		return infoSiteGroupsByShiftList;
	}

	/**
		* @param component
		* @param site
		* @param groupPropertiesCode
		* @return
		*/

	private ISiteComponent getInfoSiteStudentGroup(InfoSiteStudentGroup component, Integer studentGroupCode) throws FenixServiceException {

		List infoSiteStudents = readStudentGroupInformation(studentGroupCode);
		component.setInfoSiteStudentInformationList(infoSiteStudents);
		return component;
	}

	public List readStudentGroupInformation(Integer studentGroupCode) throws FenixServiceException {

		List studentGroupAttendInformationList = null;
		List studentGroupAttendList =null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IStudentGroup studentGroup = (IStudentGroup) sp.getIPersistentStudentGroup().readByOId(new StudentGroup(studentGroupCode), false);
			
			if(studentGroup==null)
				return null;
			
			studentGroupAttendList = sp.getIPersistentStudentGroupAttend().readAllByStudentGroup(studentGroup);
			
			} catch (ExcepcaoPersistencia ex) {
				ex.printStackTrace();
			
			throw new FenixServiceException("error.impossibleReadStudentGroupInformation");
			}
		
			studentGroupAttendInformationList = new ArrayList(studentGroupAttendList.size());
			Iterator iter = studentGroupAttendList.iterator();
			InfoSiteStudentInformation infoSiteStudentInformation = null;
			InfoStudentGroupAttend infoStudentGroupAttend = null;

			while (iter.hasNext()) {
				infoSiteStudentInformation = new InfoSiteStudentInformation();

				infoStudentGroupAttend = Cloner.copyIStudentGroupAttend2InfoStudentGroupAttend((IStudentGroupAttend) iter.next());

				infoSiteStudentInformation.setNumber(infoStudentGroupAttend.getInfoAttend().getAluno().getNumber());

				infoSiteStudentInformation.setName(infoStudentGroupAttend.getInfoAttend().getAluno().getInfoPerson().getNome());

				infoSiteStudentInformation.setEmail(infoStudentGroupAttend.getInfoAttend().getAluno().getInfoPerson().getEmail());
				
				infoSiteStudentInformation.setUsername(infoStudentGroupAttend.getInfoAttend().getAluno().getInfoPerson().getUsername());
				
				
				studentGroupAttendInformationList.add(infoSiteStudentInformation);

			}

			Collections.sort(studentGroupAttendInformationList, new BeanComparator("number"));

			
		
		return studentGroupAttendInformationList;
	}

	

	/**
			* @param component
			* @param site
			* @return
			*/

	private ISiteComponent getInfoSiteGroupProperties(InfoSiteGroupProperties component, Integer groupPropertiesCode) throws FenixServiceException {

		InfoGroupProperties infoGroupProperties = readGroupProperties(groupPropertiesCode);
		component.setInfoGroupProperties(infoGroupProperties);
		return component;
	}
	
	public InfoGroupProperties readGroupProperties(Integer groupPropertiesCode) throws ExcepcaoInexistente, FenixServiceException {

		List projects = null;
		IGroupProperties groupProperties = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			groupProperties = (IGroupProperties) sp.getIPersistentGroupProperties().readByOId(new GroupProperties(groupPropertiesCode), false);

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException("error.impossibleReadGroupProperties");
		}

		return Cloner.copyIGroupProperties2InfoGroupProperties(groupProperties);
	}
	
	
	
	
	/**
	 * @param shifts
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteShifts(InfoSiteShifts component, Integer objectCode, Integer studentGroupCode) throws FenixServiceException {
		List infoShifts = new ArrayList();
		IDisciplinaExecucao executionCourse=null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
			if(studentGroupCode!=null)
			{
			
				IStudentGroup studentGroup = (IStudentGroup) sp.getIPersistentStudentGroup().readByOId(new StudentGroup(studentGroupCode), false);
			
				if(studentGroup==null)
				{
					component.setShifts(null);
					return component;
				}
			}	
			
			executionCourse = (IDisciplinaExecucao) sp.getIDisciplinaExecucaoPersistente().readByOId(new DisciplinaExecucao(objectCode), false);

			List shifts = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);

			if (shifts == null || shifts.isEmpty()) {

			} else {

				for (int i = 0; i < shifts.size(); i++) {
					ITurno shift = (ITurno) shifts.get(i);
					InfoShift infoShift =
						new InfoShift(shift.getNome(),shift.getTipo(),shift.getLotacao(),Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse));

					List lessons = sp.getITurnoAulaPersistente().readByShift(shift);
					List infoLessons = new ArrayList();
					List classesShifts = sp.getITurmaTurnoPersistente().readClassesWithShift(shift);
					List infoClasses = new ArrayList();

					for (int j = 0; j < lessons.size(); j++)
						infoLessons.add(Cloner.copyILesson2InfoLesson((IAula) lessons.get(j)));

					infoShift.setInfoLessons(infoLessons);

					for (int j = 0; j < classesShifts.size(); j++)
						infoClasses.add(Cloner.copyClass2InfoClass(((ITurmaTurno) classesShifts.get(j)).getTurma()));

					infoShift.setInfoClasses(infoClasses);
					infoShift.setIdInternal(shift.getIdInternal());

					infoShifts.add(infoShift);
				}
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		component.setShifts(infoShifts);
		component.setInfoExecutionPeriodName(executionCourse.getExecutionPeriod().getName());
		component.setInfoExecutionYearName(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
		return component;
	}
	
	
//	private ISiteComponent getInfoSiteShifts(InfoSiteShifts component, Integer objectCode, Integer studentGroupCode) throws FenixServiceException {
//			List shiftsWithAssociatedClassesAndLessons = new ArrayList();
//			IDisciplinaExecucao executionCourse=null;
//			try {
//				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
//			
//				if(studentGroupCode!=null)
//				{
//			
//					IStudentGroup studentGroup = (IStudentGroup) sp.getIPersistentStudentGroup().readByOId(new StudentGroup(studentGroupCode), false);
//			
//					if(studentGroup==null)
//					{
//						component.setShifts(null);
//						return component;
//					}
//				}	
//			
//				executionCourse = (IDisciplinaExecucao) sp.getIDisciplinaExecucaoPersistente().readByOId(new DisciplinaExecucao(objectCode), false);
//
//				List shifts = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);
//
//				if (shifts == null || shifts.isEmpty()) {
//
//				} else {
//
//					for (int i = 0; i < shifts.size(); i++) {
//						ITurno shift = (ITurno) shifts.get(i);
//						InfoShiftWithAssociatedInfoClassesAndInfoLessons shiftWithAssociatedClassesAndLessons =
//							new InfoShiftWithAssociatedInfoClassesAndInfoLessons(Cloner.copyShift2InfoShift(shift), null, null);
//
//						List lessons = sp.getITurnoAulaPersistente().readByShift(shift);
//						List infoLessons = new ArrayList();
//						List classesShifts = sp.getITurmaTurnoPersistente().readClassesWithShift(shift);
//						List infoClasses = new ArrayList();
//
//						for (int j = 0; j < lessons.size(); j++)
//							infoLessons.add(Cloner.copyILesson2InfoLesson((IAula) lessons.get(j)));
//
//						shiftWithAssociatedClassesAndLessons.setInfoLessons(infoLessons);
//
//						for (int j = 0; j < classesShifts.size(); j++)
//							infoClasses.add(Cloner.copyClass2InfoClass(((ITurmaTurno) classesShifts.get(j)).getTurma()));
//
//						shiftWithAssociatedClassesAndLessons.setInfoClasses(infoClasses);
//
//						shiftsWithAssociatedClassesAndLessons.add(shiftWithAssociatedClassesAndLessons);
//					}
//				}
//
//			} catch (ExcepcaoPersistencia e) {
//				throw new FenixServiceException(e);
//			}
//			component.setShifts(shiftsWithAssociatedClassesAndLessons);
//			component.setInfoExecutionPeriodName(executionCourse.getExecutionPeriod().getName());
//			component.setInfoExecutionYearName(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
//			return component;
//		}
	
}