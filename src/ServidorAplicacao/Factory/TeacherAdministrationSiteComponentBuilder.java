package ServidorAplicacao.Factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoAnnouncement;
import DataBeans.InfoBibliographicReference;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurriculum;
import DataBeans.InfoEvaluation;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoItem;
import DataBeans.InfoSection;
import DataBeans.InfoSite;
import DataBeans.InfoSiteAnnouncement;
import DataBeans.InfoSiteBibliography;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteExam;
import DataBeans.InfoSiteFirstPage;
import DataBeans.InfoSiteInstructions;
import DataBeans.InfoSiteItems;
import DataBeans.InfoSiteObjectives;
import DataBeans.InfoSiteProgram;
import DataBeans.InfoSiteRegularSections;
import DataBeans.InfoSiteRootSections;
import DataBeans.InfoSiteSection;
import DataBeans.InfoSiteSections;
import DataBeans.InfoSiteTeachers;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.Announcement;
import Dominio.BibliographicReference;
import Dominio.IAnnouncement;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluation;
import Dominio.IExam;
import Dominio.IItem;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.ITeacher;
import Dominio.Item;
import Dominio.Section;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

	public ISiteComponent getComponent(ISiteComponent component, ISite site, ISiteComponent commonComponent, Object obj1, Object obj2)
		throws FenixServiceException {

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
		} else if (component instanceof InfoSiteProgram) {
			return getInfoSiteProgram((InfoSiteProgram) component, site);
		} else if (component instanceof InfoEvaluation) {
			return getInfoEvaluation((InfoEvaluation) component, site);
		} else if (component instanceof InfoSiteBibliography) {
			return getInfoSiteBibliography((InfoSiteBibliography) component, site);
		} else if (component instanceof InfoBibliographicReference) {
			return getInfoBibliographicReference((InfoBibliographicReference) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteTeachers) {
			return getInfoSiteTeachers((InfoSiteTeachers) component, site, (String) obj2);
		}else if (component instanceof InfoSiteExam) {
			return getInfoSiteExam((InfoSiteExam) component, site);
		} else if (component instanceof InfoSiteRootSections) {
			return getInfoSiteRootSections((InfoSiteRootSections) component, site);
		} else if (component instanceof InfoSiteSection) {
			return getInfoSiteSection((InfoSiteSection) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteRegularSections) {
			return getInfoSiteRegularSections((InfoSiteRegularSections) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteSections) {
			return getInfoSiteSections((InfoSiteSections) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteItems) {
			return getInfoSiteItems((InfoSiteItems) component, site, (Integer) obj1);
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
		List infoCurricularCourseScopeList = null;
		List infoCurricularCourseList = null;
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
	private ISiteComponent getInfoAnnouncement(InfoAnnouncement component, ISite site, Integer announcementCode)
		throws FenixServiceException {
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

			ICurriculum curriculum = readCurriculum(site);

			InfoCurriculum infoCurriculum = null;
			if (curriculum != null) {
				infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
				component.setGeneralObjectives(infoCurriculum.getGeneralObjectives());
				component.setOperacionalObjectives(infoCurriculum.getOperacionalObjectives());
				component.setGeneralObjectivesEn(infoCurriculum.getGeneralObjectivesEn());
				component.setOperacionalObjectivesEn(infoCurriculum.getOperacionalObjectivesEn());
			}

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
	private ISiteComponent getInfoSiteProgram(InfoSiteProgram component, ISite site) throws FenixServiceException {
		try {

			ICurriculum curriculum = readCurriculum(site);

			InfoCurriculum infoCurriculum = null;
			if (curriculum != null) {
				infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
				component.setProgram(infoCurriculum.getProgram());
				component.setProgramEn(infoCurriculum.getProgramEn());
			}

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
	private ISiteComponent getInfoEvaluation(InfoEvaluation component, ISite site) throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucao executionCourse = site.getExecutionCourse();
			IEvaluation evaluation = sp.getIPersistentEvaluation().readByExecutionCourse(executionCourse);
			if (evaluation != null) {
				InfoEvaluation infoEvaluation = Cloner.copyIEvaluation2InfoEvaluation(evaluation);
				component.setEvaluationElements(infoEvaluation.getEvaluationElements());
				component.setEvaluationElementsEn(infoEvaluation.getEvaluationElementsEn());
				component.setInfoExecutionCourse(Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse));
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return component;
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

			IPersistentBibliographicReference persistentBibliographicReference =
				persistentBibliographicReference = sp.getIPersistentBibliographicReference();

			IDisciplinaExecucaoPersistente persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();

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
	private ISiteComponent getInfoBibliographicReference(
		InfoBibliographicReference component,
		ISite site,
		Integer bibliographicReferenceCode)
		throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			BibliographicReference bibliographicReference = new BibliographicReference(bibliographicReferenceCode);
			IBibliographicReference iBibliographicReference =
				(IBibliographicReference) sp.getIPersistentBibliographicReference().readByOId(bibliographicReference, false);
			InfoBibliographicReference infoBibliographicReference =
				Cloner.copyIBibliographicReference2InfoBibliographicReference(iBibliographicReference);

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
					IResponsibleFor responsibleFor =
						persistentResponsibleFor.readByTeacherAndExecutionCourse(teacher, executionCourse);
					if (teacher != null) {
						if (responsibleTeachers != null
							&& !responsibleTeachers.isEmpty()
							&& responsibleTeachers.contains(responsibleFor)) {
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
	private ISiteComponent getInfoSiteExam(InfoSiteExam component, ISite site) {
		IDisciplinaExecucao executionCourse = site.getExecutionCourse();
		List exams = executionCourse.getAssociatedExams();
		List infoExams = new ArrayList();
		Iterator iter = exams.iterator();
		while (iter.hasNext()){
			IExam exam = (IExam) iter.next();
			infoExams.add(Cloner.copyIExam2InfoExam(exam));
		}
		component.setInfoExams(infoExams);
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
	private ISiteComponent getInfoSiteSection(InfoSiteSection component, ISite site, Integer sectionCode)
		throws FenixServiceException {

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
		while (iter.hasNext())
			infoItemsList.add(Cloner.copyIItem2InfoItem((IItem) iter.next()));

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
	private ISiteComponent getInfoSiteRegularSections(InfoSiteRegularSections component, ISite site, Integer sectionCode)
		throws FenixServiceException {
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
	private ISiteComponent getInfoSiteSections(InfoSiteSections component, ISite site, Integer sectionCode)
		throws FenixServiceException {
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
					if ((section.getSuperiorSection() != null
						&& section.getSuperiorSection().getIdInternal().equals(iSection.getSuperiorSection().getIdInternal()))
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

	private ICurriculum readCurriculum(ISite site) throws ExcepcaoPersistencia {
		ICurriculum curriculum = null;
		IDisciplinaExecucao executionCourse = site.getExecutionCourse();
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		curriculum = sp.getIPersistentCurriculum().readCurriculumByExecutionCourse(executionCourse);
		return curriculum;
	}
	private List readCurricularCourses(IDisciplinaExecucao executionCourse) {
		List infoCurricularCourseScopeList;
		List infoCurricularCourseList = new ArrayList();
		if (executionCourse.getAssociatedCurricularCourses() != null)
			for (int i = 0; i < executionCourse.getAssociatedCurricularCourses().size(); i++) {
				ICurricularCourse curricularCourse = (ICurricularCourse) executionCourse.getAssociatedCurricularCourses().get(i);
				InfoCurricularCourse infoCurricularCourse = Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
				infoCurricularCourseScopeList = new ArrayList();
				for (int j = 0; j < curricularCourse.getScopes().size(); j++) {
					ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) curricularCourse.getScopes().get(j);
					InfoCurricularCourseScope infoCurricularCourseScope =
						Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
					infoCurricularCourseScopeList.add(infoCurricularCourseScope);
				}

				infoCurricularCourse.setInfoScopes(infoCurricularCourseScopeList);
				infoCurricularCourseList.add(infoCurricularCourse);
			}
		return infoCurricularCourseList;
	} /**
											 * @param page
											 * @param site
											 * @return
											 */
	private ISiteComponent getInfoSiteFirstPage(InfoSiteFirstPage component, ISite site) throws FenixServiceException {
		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucao executionCourse = site.getExecutionCourse();
			InfoAnnouncement infoAnnouncement = readLastAnnouncement(persistentSupport, executionCourse);
			List responsibleInfoTeachersList = readResponsibleTeachers(persistentSupport, executionCourse);
			List lecturingInfoTeachersList = readLecturingTeachers(persistentSupport, executionCourse);
			//set all the required information to the component	
			component.setLastAnnouncement(infoAnnouncement);
			component.setAlternativeSite(site.getAlternativeSite());
			component.setInitialStatement(site.getInitialStatement());
			component.setIntroduction(site.getIntroduction());
			component.setSiteIdInternal(site.getIdInternal());
			if (!responsibleInfoTeachersList.isEmpty()) {
				component.setResponsibleTeachers(responsibleInfoTeachersList);
			}
			lecturingInfoTeachersList.removeAll(responsibleInfoTeachersList);
			if (!lecturingInfoTeachersList.isEmpty()) {
				component.setLecturingTeachers(lecturingInfoTeachersList);
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return component;
	}

	private List readLecturingTeachers(ISuportePersistente persistentSupport, IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
		List domainLecturingTeachersList = null;
		IPersistentProfessorship persistentProfessorship = persistentSupport.getIPersistentProfessorship();
		domainLecturingTeachersList = persistentProfessorship.readByExecutionCourse(executionCourse);
		List lecturingInfoTeachersList = new ArrayList();
		if (domainLecturingTeachersList != null) {

			Iterator iter = domainLecturingTeachersList.iterator();
			while (iter.hasNext()) {
				IProfessorship professorship = (IProfessorship) iter.next();
				ITeacher teacher = professorship.getTeacher();
				InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
				lecturingInfoTeachersList.add(infoTeacher);
			}
		}
		return lecturingInfoTeachersList;
	}

	private List readResponsibleTeachers(ISuportePersistente persistentSupport, IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
		List responsibleDomainTeachersList = null;
		IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
		IPersistentResponsibleFor persistentResponsibleFor = persistentSupport.getIPersistentResponsibleFor();
		responsibleDomainTeachersList = persistentResponsibleFor.readByExecutionCourse(executionCourse);
		List responsibleInfoTeachersList = new ArrayList();
		if (responsibleDomainTeachersList != null) {
			Iterator iter = responsibleDomainTeachersList.iterator();
			while (iter.hasNext()) {
				IResponsibleFor responsibleFor = (IResponsibleFor) iter.next();
				ITeacher teacher = responsibleFor.getTeacher();
				InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
				responsibleInfoTeachersList.add(infoTeacher);
			}

		}
		return responsibleInfoTeachersList;
	}

	private InfoAnnouncement readLastAnnouncement(ISuportePersistente persistentSupport, IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
		ISite site = persistentSupport.getIPersistentSite().readByExecutionCourse(executionCourse);
		IAnnouncement announcement = persistentSupport.getIPersistentAnnouncement().readLastAnnouncementForSite(site);
		InfoAnnouncement infoAnnouncement = null;
		if (announcement != null)
			infoAnnouncement = Cloner.copyIAnnouncement2InfoAnnouncement(announcement);
		return infoAnnouncement;
	}
}
