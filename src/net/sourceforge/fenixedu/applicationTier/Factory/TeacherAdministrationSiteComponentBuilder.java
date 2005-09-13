package net.sourceforge.fenixedu.applicationTier.Factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadShiftsAndGroups;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourseAndInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamWithRoomOccupations;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupingWithAttends;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupingWithExportGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSectionWithAll;
import net.sourceforge.fenixedu.dataTransferObject.InfoSectionWithInfoSiteAndInfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoLessons;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteBibliography;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluationExecutionCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluationMethods;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteExamExecutionCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteGroupsByShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteInstructions;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteItems;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteNewProjectProposals;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteObjectives;
import net.sourceforge.fenixedu.dataTransferObject.InfoSitePrograms;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRegularSections;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRootSections;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSections;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSentedProjectProposalsWaiting;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShifts;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroupAndStudents;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsAndShiftByStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTeachers;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroupWithAttendsAndGroupingAndShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.dataTransferObject.util.CMSUtils;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExportGrouping;
import net.sourceforge.fenixedu.domain.IFinalEvaluation;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.onlineTests.IOnlineTest;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluationMethod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamStudentRoom;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.ojb.broker.core.proxy.ProxyHelper;
import org.apache.slide.common.SlideException;

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

    public ISiteComponent getComponent(ISiteComponent component, ISite site,
            ISiteComponent commonComponent, Object obj1, Object obj2) throws FenixServiceException,
            ExcepcaoPersistencia {

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
            return getInfoCurriculum((InfoCurriculum) component, site, (Integer) obj1);
        } else if (component instanceof InfoSiteEvaluationMethods) {
            return getInfoEvaluationMethods((InfoSiteEvaluationMethods) component, site);
        } else if (component instanceof InfoEvaluationMethod) {
            return getInfoEvaluationMethod((InfoEvaluationMethod) component, site);
        } else if (component instanceof InfoSiteBibliography) {
            return getInfoSiteBibliography((InfoSiteBibliography) component, site);
        } else if (component instanceof InfoBibliographicReference) {
            return getInfoBibliographicReference((InfoBibliographicReference) component, site,
                    (Integer) obj1);
        } else if (component instanceof InfoSiteTeachers) {
            return getInfoSiteTeachers((InfoSiteTeachers) component, site, (String) obj2);
        } else if (component instanceof InfoSiteEvaluation) {
            return getInfoSiteEvaluation((InfoSiteEvaluation) component, site);
        } else if (component instanceof InfoSiteExam) {
            return getInfoSiteExam((InfoSiteExam) component, site);
        } else if (component instanceof InfoSiteEvaluationExecutionCourses) {
            return getInfoSiteEvaluationExecutionCourses((InfoSiteEvaluationExecutionCourses) component,
                    site, (Integer) obj1);
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
        } else if (component instanceof InfoSiteNewProjectProposals) {
            return getInfoSiteNewProjectProposals((InfoSiteNewProjectProposals) component, site);
        } else if (component instanceof InfoSiteSentedProjectProposalsWaiting) {
            return getInfoSiteSentedProjectProposalsWaiting(
                    (InfoSiteSentedProjectProposalsWaiting) component, site);
        } else if (component instanceof InfoSiteShiftsAndGroups) {
            return getInfoSiteShiftsAndGroups((InfoSiteShiftsAndGroups) component, (Integer) obj1);
        } else if (component instanceof InfoSiteStudentGroup) {
            return getInfoSiteStudentGroup((InfoSiteStudentGroup) component, (Integer) obj1);
        } else if (component instanceof InfoSiteGrouping) {
            return getInfoSiteGroupProperties((InfoSiteGrouping) component, (Integer) obj1);
        } else if (component instanceof InfoSiteShifts) {
            return getInfoSiteShifts((InfoSiteShifts) component, (Integer) obj1, (Integer) obj2);
        } else if (component instanceof InfoSiteStudentGroupAndStudents) {
            return getInfoSiteStudentGroupAndStudents((InfoSiteStudentGroupAndStudents) component,
                    (Integer) obj1, (Integer) obj2);
        }
        return null;
    }

    /**
     * @param common
     * @param site
     * @return
     * @throws ExcepcaoPersistencia
     */
    private ISiteComponent getInfoSiteCommon(InfoSiteCommon component, ISite site)
            throws FenixServiceException, ExcepcaoPersistencia {

        final List<ISection> allSections = site.getAssociatedSections();
        final List infoSectionsList = new ArrayList(allSections.size());
        for (final ISection section : allSections) {
            infoSectionsList.add(InfoSectionWithAll.newInfoFromDomain(section));
        }
        Collections.sort(infoSectionsList);

        component.setTitle(site.getExecutionCourse().getNome());
        component.setMail(site.getMail());
        component.setSections(infoSectionsList);

        final IExecutionCourse executionCourse = site.getExecutionCourse();
        final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                .newInfoFromDomain(executionCourse);
        component.setExecutionCourse(infoExecutionCourse);

        final List<ICurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
        final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>(curricularCourses.size());
        for (final ICurricularCourse curricularCourse : curricularCourses) {
            infoCurricularCourses.add(InfoCurricularCourseWithInfoDegree.newInfoFromDomain(curricularCourse));
        }
        component.setAssociatedDegrees(infoCurricularCourses);

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

        component.setInfoExecutionCourse(InfoExecutionCourse
                .newInfoFromDomain(site.getExecutionCourse()));
        component.setStyle(site.getStyle());

        return component;
    }

    private InfoSiteAnnouncement getInfoSiteAnnouncement(InfoSiteAnnouncement component, ISite site)
            throws FenixServiceException {
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            List announcementsList = sp.getIPersistentAnnouncement().readAnnouncementsBySite(
                    site.getIdInternal());
            List infoAnnouncementsList = new ArrayList();

            if (announcementsList != null && announcementsList.isEmpty() == false) {
                Iterator iterAnnouncements = announcementsList.iterator();
                while (iterAnnouncements.hasNext()) {
                    IAnnouncement announcement = (IAnnouncement) iterAnnouncements.next();

                    infoAnnouncementsList.add(InfoAnnouncement.newInfoFromDomain(announcement));
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
    private ISiteComponent getInfoAnnouncement(InfoAnnouncement component, ISite site,
            Integer announcementCode) throws FenixServiceException {
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IAnnouncement iAnnouncement = (IAnnouncement) sp.getIPersistentAnnouncement().readByOID(
                    Announcement.class, announcementCode);
            InfoAnnouncement infoAnnouncement = InfoAnnouncement.newInfoFromDomain(iAnnouncement);

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
    private ISiteComponent getInfoSiteObjectives(InfoSiteObjectives component, ISite site)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();

            IExecutionCourse executionCourse = site.getExecutionCourse();
            List curricularCourses = executionCourse.getAssociatedCurricularCourses();
            Iterator iter = curricularCourses.iterator();
            List infoCurriculums = new ArrayList();

            while (iter.hasNext()) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
                ICurriculum curriculum = persistentCurriculum
                        .readCurriculumByCurricularCourse(curricularCourse.getIdInternal());

                if (curriculum != null) {

                    infoCurriculums.add(InfoCurriculumWithInfoCurricularCourse
                            .newInfoFromDomain(curriculum));
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
    private ISiteComponent getInfoSitePrograms(InfoSitePrograms component, ISite site)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();

            IExecutionCourse executionCourse = site.getExecutionCourse();
            List curricularCourses = executionCourse.getAssociatedCurricularCourses();
            Iterator iter = curricularCourses.iterator();
            List infoCurriculums = new ArrayList();

            while (iter.hasNext()) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
                ICurriculum curriculum = persistentCurriculum
                        .readCurriculumByCurricularCourse(curricularCourse.getIdInternal());

                if (curriculum != null) {
                    InfoCurriculum infoCurriculum = InfoCurriculumWithInfoCurricularCourse
                            .newInfoFromDomain(curriculum);
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
    private ISiteComponent getInfoEvaluationMethods(InfoSiteEvaluationMethods component, ISite site)
            throws FenixServiceException {
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();

            IExecutionCourse executionCourse = site.getExecutionCourse();
            List curricularCourses = executionCourse.getAssociatedCurricularCourses();
            Iterator iter = curricularCourses.iterator();
            List infoEvaluationMethods = new ArrayList();

            while (iter.hasNext()) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
                ICurriculum curriculum = persistentCurriculum
                        .readCurriculumByCurricularCourse(curricularCourse.getIdInternal());

                if (curriculum != null) {
                    infoEvaluationMethods.add(InfoCurriculum.newInfoFromDomain(curriculum));
                }
            }
            component.setInfoEvaluations(infoEvaluationMethods);
            component.setInfoCurricularCourses(readInfoCurricularCourses(site));

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return component;
    }

    private ISiteComponent getInfoEvaluationMethod(InfoEvaluationMethod component, ISite site)
            throws FenixServiceException {

        try {
            IExecutionCourse executionCourse = site.getExecutionCourse();

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentEvaluationMethod persistentEvaluationMethod = sp.getIPersistentEvaluationMethod();
            IEvaluationMethod evaluationMethod = persistentEvaluationMethod
                    .readByIdExecutionCourse(executionCourse.getIdInternal());

            if (evaluationMethod != null) {
                component = InfoEvaluationMethod.newInfoFromDomain(evaluationMethod);
            }
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
    private ISiteComponent getInfoCurriculum(InfoCurriculum component, ISite site,
            Integer curricularCourseCode) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseCode);
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();

            ICurriculum curriculum = persistentCurriculum
                    .readCurriculumByCurricularCourse(curricularCourse.getIdInternal());
            InfoCurriculum infoCurriculum = null;

            if (curriculum != null) {
                infoCurriculum = InfoCurriculumWithInfoCurricularCourseAndInfoDegree
                        .newInfoFromDomain(curriculum);
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
    private ISiteComponent getInfoSiteBibliography(InfoSiteBibliography component, ISite site)
            throws FenixServiceException {
        List references = null;
        List infoBibRefs = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentBibliographicReference persistentBibliographicReference = persistentBibliographicReference = sp
                    .getIPersistentBibliographicReference();

            IExecutionCourse executionCourse = site.getExecutionCourse();

            references = persistentBibliographicReference.readBibliographicReference(executionCourse
                    .getIdInternal());

            if (references != null) {
                Iterator iterator = references.iterator();
                infoBibRefs = new ArrayList();
                while (iterator.hasNext()) {
                    IBibliographicReference bibRef = (IBibliographicReference) iterator.next();

                    InfoBibliographicReference infoBibRef = InfoBibliographicReference
                            .newInfoFromDomain(bibRef);
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
    private ISiteComponent getInfoBibliographicReference(InfoBibliographicReference component,
            ISite site, Integer bibliographicReferenceCode) throws FenixServiceException {
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IBibliographicReference iBibliographicReference = (IBibliographicReference) sp
                    .getIPersistentBibliographicReference().readByOID(BibliographicReference.class,
                            bibliographicReferenceCode);

            InfoBibliographicReference infoBibliographicReference = InfoBibliographicReference
                    .newInfoFromDomain(iBibliographicReference);

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
    private ISiteComponent getInfoSiteTeachers(InfoSiteTeachers component, ISite site, String username)
            throws FenixServiceException {
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();

            IExecutionCourse executionCourse = site.getExecutionCourse();
            List teachers = persistentProfessorship.readByExecutionCourse(executionCourse
                    .getIdInternal());
            List infoTeachers = new ArrayList();
            if (teachers != null) {

                Iterator iter = teachers.iterator();
                while (iter.hasNext()) {
                    IProfessorship professorship = (IProfessorship) iter.next();
                    ITeacher teacher = professorship.getTeacher();

                    InfoTeacher infoTeacher = InfoTeacherWithPerson.newInfoFromDomain(teacher);
                    infoTeachers.add(infoTeacher);
                }

                // see if teacher is responsible for that execution course
                IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
                List responsibleTeachers = executionCourse.responsibleFors();

                List infoResponsibleTeachers = new ArrayList();
                boolean isResponsible = false;
                if (responsibleTeachers != null) {
                    Iterator iter2 = responsibleTeachers.iterator();
                    while (iter2.hasNext()) {
                        IProfessorship responsibleFor = (IProfessorship) iter2.next();
                        ITeacher teacher = responsibleFor.getTeacher();

                        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
                        infoResponsibleTeachers.add(infoTeacher);
                    }

                    ITeacher teacher = persistentTeacher.readTeacherByUsername(username);
                    IProfessorship responsibleFor = persistentProfessorship
                            .readByTeacherAndExecutionCourse(teacher.getIdInternal(), executionCourse
                                    .getIdInternal());
                    if (teacher != null) {
                        if (responsibleTeachers != null && !responsibleTeachers.isEmpty()
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
     * @param component
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteEvaluation(InfoSiteEvaluation component, ISite site) {
        IExecutionCourse executionCourse = site.getExecutionCourse();

        List evaluations = executionCourse.getAssociatedEvaluations();
        Iterator iter = evaluations.iterator();

        // boolean hasFinalEvaluation = false;
        List infoEvaluations = new ArrayList();
        List infoFinalEvaluations = new ArrayList();
        List infoOnlineTests = new ArrayList();

        while (iter.hasNext()) {
            IEvaluation evaluation = (IEvaluation) iter.next();

            if (evaluation instanceof Proxy) {
                evaluation = (IEvaluation) ProxyHelper.getRealObject(evaluation);
            }

            if (evaluation instanceof IExam) {
                infoEvaluations.add(InfoEvaluation.newInfoFromDomain(evaluation));
            } else if (evaluation instanceof IFinalEvaluation) {
                infoFinalEvaluations.add(InfoEvaluation.newInfoFromDomain(evaluation));
            } else if (evaluation instanceof IOnlineTest) {
                infoOnlineTests.add(InfoEvaluation.newInfoFromDomain(evaluation));
            }
        }

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("day.time"));
        comparatorChain.addComparator(new BeanComparator("beginning.time"));

        Collections.sort(infoEvaluations, comparatorChain);
        // merge lists
        infoEvaluations.addAll(infoOnlineTests);
        infoEvaluations.addAll(infoFinalEvaluations);

        component.setInfoEvaluations(infoEvaluations);
        return component;
    }

    /**
     * @param component
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteExam(InfoSiteExam component, ISite site) {
        System.out.println("\n\n\n\n\nEntrei na funao getInfoSite Exam!!!\n\n\n\n\n");
        IExecutionCourse executionCourse = site.getExecutionCourse();
        List<IEvaluation> associatedEvaluations = executionCourse.getAssociatedEvaluations();
        List<IExam> exams = new ArrayList();
        for (IEvaluation evaluation : associatedEvaluations) {
            if (evaluation instanceof IExam) {
                exams.add((IExam) evaluation);
            }
        }
        List infoExams = new ArrayList();
        Iterator iter = exams.iterator();
        while (iter.hasNext()) {
            IExam exam = (IExam) iter.next();

            InfoExam infoExam = InfoExam.newInfoFromDomain(exam);
            infoExams.add(infoExam);
        }
        component.setInfoExams(infoExams);
        return component;
    }

    /**
     * @param component
     * @param site
     * @param evaluation
     * @return
     */
    private ISiteComponent getInfoSiteEvaluationExecutionCourses(
            InfoSiteEvaluationExecutionCourses component, ISite site, Integer evaluationCode)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();
            IEvaluation evaluation = (IEvaluation) persistentEvaluation.readByOID(Evaluation.class,
                    evaluationCode);

            if (evaluation instanceof Proxy) {
                evaluation = (IEvaluation) ProxyHelper.getRealObject(evaluation);
            }

            if (evaluation instanceof IExam) {
                InfoSiteExamExecutionCourses componentExam = (InfoSiteExamExecutionCourses) component;
                IExam exam = (IExam) evaluation;
                IPersistentExamStudentRoom persistentExamStudentRoom = sp
                        .getIPersistentExamStudentRoom();
                List enrolledStudents = persistentExamStudentRoom.readByExamOID(exam.getIdInternal());

                InfoExam infoExam = InfoExamWithRoomOccupations.newInfoFromDomain(exam);
                infoExam.setEnrolledStudents(new Integer(enrolledStudents.size()));
                List executionCourses = exam.getAssociatedExecutionCourses();
                List infoExecutionCourses = new ArrayList();
                Iterator iter = executionCourses.iterator();
                while (iter.hasNext()) {
                    IExecutionCourse element = (IExecutionCourse) iter.next();

                    InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                            .newInfoFromDomain(element);
                    infoExecutionCourse.setNumberOfAttendingStudents(element.getAttendsCount());
                    infoExecutionCourses.add(infoExecutionCourse);
                }

                componentExam.setInfoExam(infoExam);
                componentExam.setInfoExecutionCourses(infoExecutionCourses);
                component = componentExam;
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

    private ISiteComponent getInfoEvaluation(InfoEvaluation component, ISite site, Integer evaluationCode)
            throws FenixServiceException {
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();

            IEvaluation evaluation = (IEvaluation) persistentEvaluation.readByOID(Evaluation.class,
                    evaluationCode);

            InfoEvaluation infoEvaluation = InfoEvaluation.newInfoFromDomain(evaluation);

            if (infoEvaluation instanceof InfoExam) {
                InfoExam infoExam = (InfoExam) infoEvaluation;
                InfoExam examComponent = new InfoExam();

                // examComponent.setAssociatedRooms(infoExam.getAssociatedRooms());
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
    private ISiteComponent getInfoSiteRootSections(InfoSiteRootSections component, ISite site)
            throws FenixServiceException {
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            List allSections = sp.getIPersistentSection().readBySite(
                    site.getExecutionCourse().getSigla(),
                    site.getExecutionCourse().getExecutionPeriod().getName(),
                    site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());

            // build the result of this service
            Iterator iterator = allSections.iterator();
            List infoSectionsList = new ArrayList(allSections.size());

            while (iterator.hasNext()) {
                ISection section = (ISection) iterator.next();
                if (section.getSuperiorSection() == null) {
                    infoSectionsList.add(InfoSection.newInfoFromDomain(section));
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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentSection persistentSection = sp.getIPersistentSection();
            IPersistentItem persistentItem = sp.getIPersistentItem();

            iSection = (ISection) persistentSection.readByOID(Section.class, sectionCode);

            itemsList = persistentItem.readAllItemsBySection(iSection.getIdInternal(), iSection
                    .getSite().getExecutionCourse().getSigla(), iSection.getSite().getExecutionCourse()
                    .getExecutionPeriod().getExecutionYear().getYear(), iSection.getSite()
                    .getExecutionCourse().getExecutionPeriod().getName());
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        List infoItemsList = new ArrayList(itemsList.size());
        Iterator iter = itemsList.iterator();
        IFileSuport fileSuport = FileSuport.getInstance();
        try {
            fileSuport.beginTransaction();
            while (iter.hasNext()) {
                IItem item = (IItem) iter.next();

                InfoItem infoItem = InfoItem.newInfoFromDomain(item);
                try {
                    infoItem.setLinks(CMSUtils.getItemLinks(fileSuport, item.getSlideName()));
                } catch (SlideException e1) {
                    // the item does not have a folder associated
                }
                infoItemsList.add(infoItem);
            }
            fileSuport.commitTransaction();
        } catch (Exception e1) {
            try {
                fileSuport.abortTransaction();
            } catch (Exception e2) {
                throw new FenixServiceException(e2);
            }
        }

        component.setSection(InfoSectionWithAll.newInfoFromDomain(iSection));
        Collections.sort(infoItemsList);
        component.setItems(infoItemsList);

        return component;
    }

    /**
     * @param sections
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteRegularSections(InfoSiteRegularSections component, ISite site,
            Integer sectionCode) throws FenixServiceException {
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentSection persistentSection = sp.getIPersistentSection();

            ISection iSuperiorSection = (ISection) persistentSection.readByOID(Section.class,
                    sectionCode);
            List allSections = persistentSection.readBySite(site.getExecutionCourse().getSigla(), site
                    .getExecutionCourse().getExecutionPeriod().getName(), site.getExecutionCourse()
                    .getExecutionPeriod().getExecutionYear().getYear());

            // build the result of this service
            Iterator iterator = allSections.iterator();
            List infoSectionsList = new ArrayList(allSections.size());
            while (iterator.hasNext()) {
                ISection section = (ISection) iterator.next();

                if (section.getSuperiorSection() != null
                        && section.getSuperiorSection().equals(iSuperiorSection)) {

                    infoSectionsList.add(InfoSection.newInfoFromDomain(section));
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
    private ISiteComponent getInfoSiteSections(InfoSiteSections component, ISite site,
            Integer sectionCode) throws FenixServiceException {
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentSection persistentSection = sp.getIPersistentSection();

            ISection iSection = (ISection) persistentSection.readByOID(Section.class, sectionCode);

            InfoSection infoSection = InfoSection.newInfoFromDomain(iSection);
            List allSections = persistentSection.readBySite(site.getExecutionCourse().getSigla(), site
                    .getExecutionCourse().getExecutionPeriod().getName(), site.getExecutionCourse()
                    .getExecutionPeriod().getExecutionYear().getYear());

            // build the result of this service
            Iterator iterator = allSections.iterator();
            List infoSectionsList = new ArrayList(allSections.size());

            if (iSection.getSuperiorSection() == null) {
                while (iterator.hasNext()) {
                    ISection section = (ISection) iterator.next();
                    if ((section.getSuperiorSection() == null)
                            && !section.getName().equals(iSection.getName())) {
                        infoSectionsList.add(InfoSection.newInfoFromDomain(section));
                    }
                }
            } else {
                while (iterator.hasNext()) {
                    ISection section = (ISection) iterator.next();
                    if ((section.getSuperiorSection() != null && section.getSuperiorSection()
                            .getIdInternal().equals(iSection.getSuperiorSection().getIdInternal()))
                            && !section.getName().equals(iSection.getName())) {
                        infoSectionsList.add(InfoSection.newInfoFromDomain(section));
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
    private ISiteComponent getInfoSiteItems(InfoSiteItems component, ISite site, Integer itemCode)
            throws FenixServiceException {
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentItem persistentItem = sp.getIPersistentItem();
            IPersistentSection persistentSection = sp.getIPersistentSection();

            IItem iItem = (IItem) persistentItem.readByOID(Item.class, itemCode);

            InfoItem infoItem = InfoItem.newInfoFromDomain(iItem);

            ISection iSection = (ISection) persistentSection.readByOID(Section.class, iItem.getSection()
                    .getIdInternal());
            infoItem.setInfoSection(InfoSectionWithInfoSiteAndInfoExecutionCourse
                    .newInfoFromDomain(iSection));
            List allItems = persistentItem.readAllItemsBySection(iSection.getIdInternal(), iSection
                    .getSite().getExecutionCourse().getSigla(), iSection.getSite().getExecutionCourse()
                    .getExecutionPeriod().getExecutionYear().getYear(), iSection.getSite()
                    .getExecutionCourse().getExecutionPeriod().getName());

            // build the result of this service
            Iterator iterator = allItems.iterator();
            List infoItemsList = new ArrayList(allItems.size());

            while (iterator.hasNext()) {
                IItem item = (IItem) iterator.next();
                if (!item.getIdInternal().equals(iItem.getIdInternal())) {
                    infoItemsList.add(InfoItem.newInfoFromDomain(item));
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

    private List readInfoCurricularCourses(ISite site) {

        IExecutionCourse executionCourse = site.getExecutionCourse();
        List curricularCourses = executionCourse.getAssociatedCurricularCourses();
        Iterator iter = curricularCourses.iterator();
        List infoCurricularCourses = new ArrayList();
        while (iter.hasNext()) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                    .newInfoFromDomain(curricularCourse);
            infoCurricularCourses.add(infoCurricularCourse);
        }
        return infoCurricularCourses;
    }

    /**
     * @param component
     * @param site
     * @return
     */

    private ISiteComponent getInfoSiteProjects(InfoSiteProjects component, ISite site)
            throws FenixServiceException {

        List infoGroupPropertiesList = readExecutionCourseProjects(site.getExecutionCourse()
                .getIdInternal());
        component.setInfoGroupPropertiesList(infoGroupPropertiesList);
        return component;
    }

    private List readExecutionCourseProjects(Integer executionCourseCode) throws ExcepcaoInexistente,
            FenixServiceException {

        List projects = null;
        IGrouping groupProperties;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                    .readByOID(ExecutionCourse.class, executionCourseCode);

            List executionCourseProjects = new ArrayList();
            List groupPropertiesExecutionCourseList = executionCourse.getExportGroupings();
            Iterator iterGroupPropertiesExecutionCourse = groupPropertiesExecutionCourseList.iterator();
            while (iterGroupPropertiesExecutionCourse.hasNext()) {
                IExportGrouping groupPropertiesExecutionCourse = (IExportGrouping) iterGroupPropertiesExecutionCourse
                        .next();
                if ((groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 1)
                        || (groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 2)) {
                    executionCourseProjects.add(groupPropertiesExecutionCourse.getGrouping());
                }
            }

            projects = new ArrayList();
            Iterator iterator = executionCourseProjects.iterator();

            while (iterator.hasNext()) {
                // projects
                groupProperties = (IGrouping) iterator.next();

                InfoGrouping infoGrouping = InfoGroupingWithExportGrouping
                        .newInfoFromDomain(groupProperties);

                projects.add(infoGrouping);
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
     * @return
     */

    private ISiteComponent getInfoSiteNewProjectProposals(InfoSiteNewProjectProposals component,
            ISite site) throws FenixServiceException {

        List infoGroupPropertiesList = readExecutionCourseNewProjectProposals(site.getExecutionCourse()
                .getIdInternal());
        component.setInfoGroupPropertiesList(infoGroupPropertiesList);
        return component;
    }

    private List readExecutionCourseNewProjectProposals(Integer executionCourseCode)
            throws ExcepcaoInexistente, FenixServiceException {

        List projects = null;
        IGrouping groupProperties;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                    .readByOID(ExecutionCourse.class, executionCourseCode);

            List executionCourseProjects = new ArrayList();
            List groupPropertiesExecutionCourseList = executionCourse.getExportGroupings();
            Iterator iterGroupPropertiesExecutionCourse = groupPropertiesExecutionCourseList.iterator();
            while (iterGroupPropertiesExecutionCourse.hasNext()) {
                IExportGrouping groupPropertiesExecutionCourse = (IExportGrouping) iterGroupPropertiesExecutionCourse
                        .next();
                if (groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 3) {
                    executionCourseProjects.add(groupPropertiesExecutionCourse.getGrouping());
                }
            }

            projects = new ArrayList();
            Iterator iterator = executionCourseProjects.iterator();

            while (iterator.hasNext()) {

                groupProperties = (IGrouping) iterator.next();

                InfoGrouping infoGroupProperties = InfoGrouping.newInfoFromDomain(groupProperties);

                projects.add(infoGroupProperties);
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
     * @return
     */

    private ISiteComponent getInfoSiteSentedProjectProposalsWaiting(
            InfoSiteSentedProjectProposalsWaiting component, ISite site) throws FenixServiceException {

        List infoGroupPropertiesList = readExecutionCourseSentedProjectProposalsWaiting(site
                .getExecutionCourse().getIdInternal());
        component.setInfoGroupPropertiesList(infoGroupPropertiesList);
        return component;
    }

    private List readExecutionCourseSentedProjectProposalsWaiting(Integer executionCourseCode)
            throws ExcepcaoInexistente, FenixServiceException {

        List projects = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                    .readByOID(ExecutionCourse.class, executionCourseCode);

            List executionCourseSentedProjects = new ArrayList();
            List groupPropertiesList = executionCourse.getGroupings();
            Iterator iterGroupPropertiesList = groupPropertiesList.iterator();
            while (iterGroupPropertiesList.hasNext()) {
                boolean found = false;
                IGrouping groupProperties = (IGrouping) iterGroupPropertiesList.next();
                List groupPropertiesExecutionCourseList = groupProperties.getExportGroupings();
                Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList
                        .iterator();
                while (iterGroupPropertiesExecutionCourseList.hasNext() && !found) {
                    IExportGrouping groupPropertiesExecutionCourse = (IExportGrouping) iterGroupPropertiesExecutionCourseList
                            .next();
                    if (groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 3) {
                        executionCourseSentedProjects.add(groupPropertiesExecutionCourse.getGrouping());
                        found = true;
                    }
                }
            }

            projects = new ArrayList();
            Iterator iterator = executionCourseSentedProjects.iterator();

            while (iterator.hasNext()) {

                IGrouping groupProperties = (IGrouping) iterator.next();

                InfoGrouping infoGroupProperties = InfoGroupingWithExportGrouping
                        .newInfoFromDomain(groupProperties);

                projects.add(infoGroupProperties);
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

    private ISiteComponent getInfoSiteShiftsAndGroups(InfoSiteShiftsAndGroups component,
            Integer groupPropertiesCode) throws FenixServiceException {

        IGrouping groupProperties = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            groupProperties = (IGrouping) sp.getIPersistentGrouping().readByOID(Grouping.class,
                    groupPropertiesCode);

            List infoSiteShiftsAndGroups = ReadShiftsAndGroups.run(groupProperties).getInfoSiteGroupsByShiftList();
            component.setInfoSiteGroupsByShiftList(infoSiteShiftsAndGroups);

            Integer numberOfStudentsOutsideGrouping = readNumberOfStudentsOutsideGrouping(groupProperties);
            component.setNumberOfStudentsOutsideAttendsSet(numberOfStudentsOutsideGrouping);

            Integer numberOfStudentsInsideAttendsSet = readNumberOfStudentsInsideGrouping(groupProperties);
            component.setNumberOfStudentsInsideAttendsSet(numberOfStudentsInsideAttendsSet);

            InfoGrouping infoGrouping = InfoGrouping.newInfoFromDomain(groupProperties);
            component.setInfoGrouping(infoGrouping);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return component;
    }

    private Integer readNumberOfStudentsOutsideGrouping(IGrouping groupProperties) {

        if (groupProperties == null)
            return null;

        return groupProperties.getNumberOfStudentsNotInGrouping();

    }

    private Integer readNumberOfStudentsInsideGrouping(IGrouping groupProperties) {

        if (groupProperties == null)
            return null;

        return groupProperties.getNumberOfStudentsInGrouping();

    }

    private ISiteComponent getInfoSiteStudentGroup(InfoSiteStudentGroup component, Integer studentGroupID)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IStudentGroup studentGroup = (IStudentGroup) persistentSupport
                .getIPersistentStudentGroup().readByOID(StudentGroup.class, studentGroupID);

        if (studentGroup == null) {
            return null;
        }

        final List<InfoSiteStudentInformation> infoSiteStudentInformations = new ArrayList<InfoSiteStudentInformation>();
        for (final IAttends attend : studentGroup.getAttends()) {
            infoSiteStudentInformations.add(new InfoSiteStudentInformation(attend.getAluno().getPerson()
                    .getNome(), attend.getAluno().getPerson().getEmail(), attend.getAluno().getPerson()
                    .getUsername(), attend.getAluno().getNumber()));
        }
        Collections.sort(infoSiteStudentInformations, new BeanComparator("number"));
        component.setInfoSiteStudentInformationList(infoSiteStudentInformations);
        component.setInfoStudentGroup(InfoStudentGroupWithAttendsAndGroupingAndShift
                .newInfoFromDomain(studentGroup));

        if (studentGroup.getGrouping().getMaximumCapacity() != null) {
            int freeGroups = studentGroup.getGrouping().getMaximumCapacity()
                    - studentGroup.getAttendsCount();
            component.setNrOfElements(Integer.valueOf(freeGroups));
        } else
            component.setNrOfElements("Sem limite");
        return component;
    }

    private ISiteComponent getInfoSiteStudentGroupAndStudents(InfoSiteStudentGroupAndStudents component,
            Integer groupPropertiesCode, Integer shiftCode) throws FenixServiceException {
        List infoSiteStudentsAndShiftByStudentGroupList = readStudentGroupAndStudents(
                groupPropertiesCode, shiftCode);
        component
                .setInfoSiteStudentsAndShiftByStudentGroupList(infoSiteStudentsAndShiftByStudentGroupList);

        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = readShiftAndGroups(groupPropertiesCode,
                shiftCode);
        component.setInfoSiteShiftsAndGroups(infoSiteShiftsAndGroups);
        return component;
    }

    private InfoSiteShiftsAndGroups readShiftAndGroups(Integer groupPropertiesCode, Integer shiftCode)
            throws ExcepcaoInexistente, FenixServiceException {

        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = new InfoSiteShiftsAndGroups();
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IGrouping grouping = (IGrouping) sp.getIPersistentGrouping().readByOID(Grouping.class,
                    groupPropertiesCode);

            if (grouping == null)
                return null;

            IShift shift = (IShift) sp.getITurnoPersistente().readByOID(Shift.class, shiftCode);

            List infoSiteGroupsByShiftList = new ArrayList();
            InfoSiteShift infoSiteShift = new InfoSiteShift();
            infoSiteShift.setInfoShift(InfoShiftWithInfoLessons.newInfoFromDomain(shift));

            List allStudentGroups = grouping.readAllStudentGroupsBy(shift);

            if (grouping.getGroupMaximumNumber() != null) {
                int vagas = grouping.getGroupMaximumNumber().intValue() - allStudentGroups.size();
                infoSiteShift.setNrOfGroups(new Integer(vagas));
            } else
                infoSiteShift.setNrOfGroups("Sem limite");
            InfoSiteGroupsByShift infoSiteGroupsByShift = new InfoSiteGroupsByShift();
            infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);

            List infoSiteStudentGroupsList = null;
            if (allStudentGroups.size() != 0) {
                infoSiteStudentGroupsList = new ArrayList();
                Iterator iterGroups = allStudentGroups.iterator();
                while (iterGroups.hasNext()) {
                    InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
                    InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
                    infoStudentGroup = InfoStudentGroup.newInfoFromDomain((IStudentGroup) iterGroups
                            .next());
                    infoSiteStudentGroup.setInfoStudentGroup(infoStudentGroup);
                    infoSiteStudentGroupsList.add(infoSiteStudentGroup);
                }
                Collections.sort(infoSiteStudentGroupsList, new BeanComparator(
                        "infoStudentGroup.groupNumber"));
            }
            infoSiteGroupsByShift.setInfoSiteStudentGroupsList(infoSiteStudentGroupsList);

            infoSiteGroupsByShiftList.add(infoSiteGroupsByShift);
            infoSiteShiftsAndGroups.setInfoSiteGroupsByShiftList(infoSiteGroupsByShiftList);

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException();
        }
        return infoSiteShiftsAndGroups;
    }

    private List readStudentGroupAndStudents(Integer groupPropertiesCode, Integer shiftCode)
            throws ExcepcaoInexistente, FenixServiceException {

        List infoSiteStudentsAndShiftByStudentGroupList = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IGrouping groupProperties = (IGrouping) sp.getIPersistentGrouping().readByOID(
                    Grouping.class, groupPropertiesCode);
            if (groupProperties == null)
                return null;

            IShift shift = (IShift) sp.getITurnoPersistente().readByOID(Shift.class, shiftCode);

            List aux = new ArrayList();
            List studentGroupsWithShift = groupProperties.getStudentGroupsWithShift();
            Iterator iterStudentGroupsWithShift = studentGroupsWithShift.iterator();
            while (iterStudentGroupsWithShift.hasNext()) {
                IStudentGroup studentGroup = (IStudentGroup) iterStudentGroupsWithShift.next();
                if (studentGroup.getShift().equals(shift)) {
                    aux.add(studentGroup);
                }
            }
            List allStudentGroups = new ArrayList();
            allStudentGroups.addAll(groupProperties.getStudentGroups());

            Iterator iterAux = aux.iterator();
            while (iterAux.hasNext()) {
                IStudentGroup studentGroup = (IStudentGroup) iterAux.next();
                allStudentGroups.remove(studentGroup);
            }

            Iterator iterAllStudentGroups = allStudentGroups.iterator();
            InfoSiteStudentsAndShiftByStudentGroup infoSiteStudentsAndShiftByStudentGroup = null;
            while (iterAllStudentGroups.hasNext()) {
                infoSiteStudentsAndShiftByStudentGroup = new InfoSiteStudentsAndShiftByStudentGroup();

                IStudentGroup studentGroup = (IStudentGroup) iterAllStudentGroups.next();
                IShift turno = studentGroup.getShift();
                infoSiteStudentsAndShiftByStudentGroup.setInfoStudentGroup(InfoStudentGroup
                        .newInfoFromDomain(studentGroup));
                infoSiteStudentsAndShiftByStudentGroup.setInfoShift(InfoShift.newInfoFromDomain(turno));

                List attendsList = studentGroup.getAttends();

                List studentGroupAttendInformationList = new ArrayList();
                Iterator iterAttendsList = attendsList.iterator();
                InfoSiteStudentInformation infoSiteStudentInformation = null;
                IAttends attend = null;

                while (iterAttendsList.hasNext()) {
                    infoSiteStudentInformation = new InfoSiteStudentInformation();

                    attend = (IAttends) iterAttendsList.next();

                    infoSiteStudentInformation.setNumber(attend.getAluno().getNumber());

                    studentGroupAttendInformationList.add(infoSiteStudentInformation);

                }

                Collections.sort(studentGroupAttendInformationList, new BeanComparator("number"));

                infoSiteStudentsAndShiftByStudentGroup
                        .setInfoSiteStudentInformationList(studentGroupAttendInformationList);
                infoSiteStudentsAndShiftByStudentGroupList.add(infoSiteStudentsAndShiftByStudentGroup);

                Collections.sort(infoSiteStudentsAndShiftByStudentGroupList, new BeanComparator(
                        "infoStudentGroup.groupNumber"));

            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException();
        }
        return infoSiteStudentsAndShiftByStudentGroupList;
    }

    /**
     * @param component
     * @param site
     * @return
     */

    private ISiteComponent getInfoSiteGroupProperties(InfoSiteGrouping component,
            Integer groupPropertiesCode) throws FenixServiceException {

        InfoGrouping infoGrouping = readGroupProperties(groupPropertiesCode);
        component.setInfoGrouping(infoGrouping);
        return component;
    }

    private InfoGrouping readGroupProperties(Integer groupPropertiesCode) throws ExcepcaoInexistente,
            FenixServiceException {

        IGrouping groupProperties = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            groupProperties = (IGrouping) sp.getIPersistentGrouping().readByOID(Grouping.class,
                    groupPropertiesCode);

        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException("error.impossibleReadGroupProperties");
        }

        return InfoGroupingWithAttends.newInfoFromDomain(groupProperties);
    }

    /**
     * @param shifts
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteShifts(InfoSiteShifts component, Integer groupPropertiesCode,
            Integer studentGroupCode) throws FenixServiceException {
        List infoShifts = new ArrayList();
        IGrouping groupProperties = null;
        IExecutionCourse executionCourse = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IStudentGroup studentGroup = null;
            groupProperties = (IGrouping) sp.getIPersistentGrouping().readByOID(Grouping.class,
                    groupPropertiesCode);
            if (groupProperties == null) {
                return null;
            }
            if (studentGroupCode != null) {

                studentGroup = (IStudentGroup) sp.getIPersistentStudentGroup().readByOID(
                        StudentGroup.class, studentGroupCode);

                if (studentGroup == null) {
                    component.setShifts(null);
                    return component;
                }

                component.setOldShift(InfoShift.newInfoFromDomain(studentGroup.getShift()));
            }

            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                    .getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                    .getGroupEnrolmentStrategyInstance(groupProperties);

            if (strategy.checkHasShift(groupProperties)) {

                ITurnoPersistente persistentShift = sp.getITurnoPersistente();
                List executionCourses = new ArrayList();
                executionCourses = groupProperties.getExecutionCourses();

                Iterator iterExecutionCourses = executionCourses.iterator();
                List shifts = new ArrayList();
                while (iterExecutionCourses.hasNext()) {
                    IExecutionCourse executionCourse2 = (IExecutionCourse) iterExecutionCourses.next();
                    List someShifts = persistentShift.readByExecutionCourse(executionCourse2
                            .getIdInternal());

                    shifts.addAll(someShifts);
                }

                if (shifts == null || shifts.isEmpty()) {

                } else {
                    for (int i = 0; i < shifts.size(); i++) {
                        IShift shift = (IShift) shifts.get(i);
                        if (strategy.checkShiftType(groupProperties, shift)) {
                            executionCourse = shift.getDisciplinaExecucao();
                            InfoShift infoShift = new InfoShift(shift.getNome(), shift.getTipo(), shift
                                    .getLotacao(), InfoExecutionCourse
                                    .newInfoFromDomain(executionCourse));

                            List lessons = shift.getAssociatedLessons();
                            List infoLessons = new ArrayList();
                            List<ISchoolClass> classesShifts = shift.getAssociatedClasses();
                            List infoClasses = new ArrayList();

                            for (int j = 0; j < lessons.size(); j++)
                                infoLessons.add(InfoLesson.newInfoFromDomain((ILesson) lessons.get(j)));

                            infoShift.setInfoLessons(infoLessons);

                            for (int j = 0; j < classesShifts.size(); j++)
                                infoClasses.add(InfoClass.newInfoFromDomain(classesShifts.get(j)));
                            infoShift.setInfoClasses(infoClasses);
                            infoShift.setIdInternal(shift.getIdInternal());

                            infoShifts.add(infoShift);
                        }
                    }
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        component.setShifts(infoShifts);

        return component;
    }
}