package net.sourceforge.fenixedu.dataTransferObject.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.constants.publication.PublicationConstants;
import net.sourceforge.fenixedu.dataTransferObject.InfoAdvisory;
import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsSet;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoCampus;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinator;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularSemester;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEmployee;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamStudentRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValues;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupProperties;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesis;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPaymentPhase;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPrice;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoResponsibleFor;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoScientificArea;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteIST;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroupAttend;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentKind;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoUniversity;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoWorkLocation;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudyChoice;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoManagementPositionCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoOtherTypeCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoServiceExemptionCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.dataTransferObject.gaugingTests.physics.InfoGaugingTestResult;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseHistoric;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuide;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuideSituation;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAttribute;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthorPerson;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationFormat;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationSubtype;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationType;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoDelegate;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoStudentCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCategory;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOldPublication;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOrientation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoProfessionalCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoPublicationsNumber;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoServiceProviderRegime;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoTeachingCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoWeeklyOcupation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InfoTeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.domain.*;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudy;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudyChoice;
import net.sourceforge.fenixedu.domain.Seminaries.ICandidacy;
import net.sourceforge.fenixedu.domain.Seminaries.ICaseStudyChoice;
import net.sourceforge.fenixedu.domain.Seminaries.Modality;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import net.sourceforge.fenixedu.domain.credits.IManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.credits.IOtherTypeCreditLine;
import net.sourceforge.fenixedu.domain.credits.IServiceExemptionCreditLine;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;
import net.sourceforge.fenixedu.domain.credits.ServiceExemptionCreditLine;
import net.sourceforge.fenixedu.domain.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.gaugingTests.physics.IGaugingTestResult;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.domain.gesdis.ICourseHistoric;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;
import net.sourceforge.fenixedu.domain.gesdis.IStudentCourseReport;
import net.sourceforge.fenixedu.domain.gesdis.StudentCourseReport;
import net.sourceforge.fenixedu.domain.publication.Author;
import net.sourceforge.fenixedu.domain.publication.IAttribute;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.domain.publication.IPublicationFormat;
import net.sourceforge.fenixedu.domain.publication.IPublicationSubtype;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideSituation;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.student.IDelegate;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.domain.teacher.ICareer;
import net.sourceforge.fenixedu.domain.teacher.ICategory;
import net.sourceforge.fenixedu.domain.teacher.IExternalActivity;
import net.sourceforge.fenixedu.domain.teacher.IOldPublication;
import net.sourceforge.fenixedu.domain.teacher.IOrientation;
import net.sourceforge.fenixedu.domain.teacher.IProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.IPublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.IServiceProviderRegime;
import net.sourceforge.fenixedu.domain.teacher.ITeachingCareer;
import net.sourceforge.fenixedu.domain.teacher.IWeeklyOcupation;
import net.sourceforge.fenixedu.domain.teacher.OldPublication;
import net.sourceforge.fenixedu.domain.teacher.Orientation;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
import net.sourceforge.fenixedu.domain.teacher.WeeklyOcupation;
import net.sourceforge.fenixedu.domain.teacher.workTime.ITeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 * 
 */
public abstract class Cloner {

    public static InfoObject get(IDomainObject domainObject) {
        if (domainObject == null) {
            return null;
        }

        InfoObject infoObject = null;
        Class[] parameters = getParameters(domainObject.getClass());
        Object[] args = { domainObject };
        try {
            Method method = Cloner.class.getDeclaredMethod("copy", parameters);
            if (method != null) {
                infoObject = (InfoObject) method.invoke(Cloner.class, args);
            }
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return infoObject;
    }

    private static Class[] getParameters(Class domainClass) {
        Class[] interfaces = domainClass.getInterfaces();
        // Start with the most frequent case
        if (interfaces.length == 1) {
            return interfaces;
        } else if (interfaces.length == 0) {
            return getParameters(domainClass.getSuperclass());
        } else {

            Class[] parameters = null;
            for (int i = 0; i < interfaces.length; i++) {

                if (Arrays.asList(interfaces[i].getInterfaces()).contains(IDomainObject.class)
                        || ((interfaces[i].getSuperclass() != null) && (interfaces[i].getSuperclass()
                                .getName().equals(IDomainObject.class.getName())))) {
                    parameters = new Class[1];
                    parameters[0] = interfaces[i];
                }
            }
            return parameters;
        }
    }

    public static IShift copyInfoShift2Shift(InfoShift infoShift) {
        IShift shift = new Shift();
        IExecutionCourse executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoShift
                .getInfoDisciplinaExecucao());
        copyObjectProperties(shift, infoShift);

        shift.setDisciplinaExecucao(executionCourse);
        shift.setIdInternal(infoShift.getIdInternal());
        return shift;
    }

    /**
     * Method copyInfoExecutionCourse2ExecutionCourse.
     * 
     * @param infoExecutionCourse
     * @return IDisciplinaExecucao
     */
    public static IExecutionCourse copyInfoExecutionCourse2ExecutionCourse(
            InfoExecutionCourse infoExecutionCourse) {
        IExecutionCourse executionCourse = new ExecutionCourse();
        IExecutionPeriod executionPeriod = Cloner
                .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionCourse.getInfoExecutionPeriod());

        copyObjectProperties(executionCourse, infoExecutionCourse);

        executionCourse.setExecutionPeriod(executionPeriod);
        return executionCourse;
    }

    // DO NOT DELETE - this is used locally through introspection!!!
    private static InfoExecutionCourse copy(IExecutionCourse executionCourse) {
        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionCourse
                .getExecutionPeriod());

        if (infoExecutionPeriod != null) {
            infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
        }

        copyObjectProperties(infoExecutionCourse, executionCourse);

        infoExecutionCourse.setNumberOfAttendingStudents(new Integer(executionCourse
                .getAttendingStudents() == null ? 0 : executionCourse.getAttendingStudents().size()));
        return infoExecutionCourse;
    }

    /**
     * Method copyInfoLesson2Lesson.
     * 
     * @param lessonExample
     * @return ILesson
     */
    public static ILesson copyInfoLesson2Lesson(InfoLesson infoLesson) {
        ILesson lesson = new Lesson();
        IRoom sala = null;

        IRoomOccupation roomOccupation = null;
        // IShift shift = null;
        try {
            BeanUtils.copyProperties(lesson, infoLesson);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        sala = Cloner.copyInfoRoom2Room(infoLesson.getInfoSala());
        roomOccupation = Cloner
                .copyInfoRoomOccupation2RoomOccupation(infoLesson.getInfoRoomOccupation());

        lesson.setSala(sala);
        lesson.setRoomOccupation(roomOccupation);

        return lesson;
    }

    /**
     * Method copyInfoRoom2Room.
     * 
     * @param infoRoom
     */
    public static IRoom copyInfoRoom2Room(InfoRoom infoRoom) {
        IRoom room = new Room();
        copyObjectProperties(room, infoRoom);
        return room;
    }

    /**
     * @param room
     * @return ILesson
     */
    public static InfoRoom copyRoom2InfoRoom(IRoom room) {

        if (room != null) {
            InfoRoom infoRoom = new InfoRoom();

            copyObjectProperties(infoRoom, room);
            return infoRoom;
        }
        return null;
    }

    /**
     * Method copyInfoRoomOccupation2RoomOccupation.
     * 
     * @param infoRoomOccupation
     */
    public static IRoomOccupation copyInfoRoomOccupation2RoomOccupation(
            InfoRoomOccupation infoRoomOccupation) {
        IRoomOccupation roomOccupation = new RoomOccupation();
        copyObjectProperties(roomOccupation, infoRoomOccupation);
        return roomOccupation;
    }

    /**
     * Method copyRoomOccupation2InfoRoomOccupation.
     * 
     * @param roomOccupation
     */
    public static InfoRoomOccupation copyRoomOccupation2InfoRoomOccupation(IRoomOccupation roomOccupation) {
        if (roomOccupation != null) {
            InfoRoomOccupation infoRoomOccupation = new InfoRoomOccupation();
            copyObjectProperties(infoRoomOccupation, roomOccupation);
            return infoRoomOccupation;
        }
        return null;
    }

    /**
     * Method copyInfoLesson2Lesson.
     * 
     * @param lessonExample
     * @return ILesson
     */
    public static InfoLesson copyILesson2InfoLesson(ILesson lesson) {
        if (lesson == null) {
            return null;
        }

        try {
            // Temporary bug fix.
            // Try to materialize the proxy... if it fails return null.
            // Should look into this further... but I don't have the time
            // at the moment. This situation occores constantly when
            // viewing Class 05102 of the execution period 80.
            // "\n\n");
            lesson.getIdInternal();
        } catch (Throwable nex) {

            return null;
        }

        InfoLesson infoLesson = new InfoLesson();
        /*
         * InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse)
         * Cloner.get(lesson .getDisciplinaExecucao());
         */
        InfoRoom infoRoom = Cloner.copyRoom2InfoRoom(lesson.getSala());
        InfoRoomOccupation infoRoomOccupation = Cloner.copyIRoomOccupation2InfoRoomOccupation(lesson
                .getRoomOccupation());

        copyObjectProperties(infoLesson, lesson);

        infoLesson.setInfoSala(infoRoom);
        infoLesson.setInfoRoomOccupation(infoRoomOccupation);

        return infoLesson;
    }

    /**
     * Method copyInfoShift2Shift.
     * 
     * @param infoShift
     * @return IShift
     */
    public static IShift copyInfoShift2IShift(InfoShift infoShift) {
        if (infoShift == null)
            return null;
        IShift shift = new Shift();
        IExecutionCourse executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoShift
                .getInfoDisciplinaExecucao());

        copyObjectProperties(shift, infoShift);
        List lessons = new ArrayList();
        if (shift.getAssociatedLessons() != null) {
            for (int i = 0; i < shift.getAssociatedLessons().size(); i++) {
                InfoLesson infoLesson = (InfoLesson) shift.getAssociatedLessons().get(i);
                ILesson lesson = Cloner.copyInfoLesson2Lesson(infoLesson);
                lessons.add(lesson);
            }
        }
        shift.setDisciplinaExecucao(executionCourse);
        shift.setAssociatedLessons(lessons);
        return shift;
    }

    /**
     * Method copyInfoShift2Shift.
     * 
     * @param infoShift
     * @return IShift
     */
    public static InfoShift copyShift2InfoShift(IShift shift) {
        InfoShift infoShift = new InfoShift();

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner.get(shift
                .getDisciplinaExecucao());

        List infoLessonList = (List) CollectionUtils.collect(shift.getAssociatedLessons(),
                new Transformer() {
                    public Object transform(Object arg0) {
                        return copyILesson2InfoLesson((ILesson) arg0);
                    }
                });

        List infoClassesList = (List) CollectionUtils.collect(shift.getAssociatedClasses(),
                new Transformer() {
                    public Object transform(Object arg0) {
                        return copyClass2InfoClass((ISchoolClass) arg0);
                    }
                });

        copyObjectProperties(infoShift, shift);
        infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
        infoShift.setInfoLessons(infoLessonList);
        infoShift.setInfoClasses(infoClassesList);

        return infoShift;
    }

    /**
     * Method copyInfoShift2Shift.
     * 
     * @param infoShift
     * @return IShift
     */
    public static InfoClass copyClass2InfoClass(ISchoolClass classD) {
        InfoClass infoClass = new InfoClass();
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner.get(classD
                .getExecutionDegree());
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(classD
                .getExecutionPeriod());

        copyObjectProperties(infoClass, classD);

        infoClass.setInfoExecutionDegree(infoExecutionDegree);
        infoClass.setInfoExecutionPeriod(infoExecutionPeriod);
        return infoClass;
    }

    /**
     * Method copyIExecutionPeriod2InfoExecutionPeriod.
     * 
     * @param iExecutionPeriod
     * @return InfoExecutionPeriod
     */

    // DO NOT DELETE - this is used locally through introspection!!!
    private static InfoExecutionPeriod copy(IExecutionPeriod executionPeriod) {
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) Cloner.get(executionPeriod
                .getExecutionYear());

        copyObjectProperties(infoExecutionPeriod, executionPeriod);

        infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
        return infoExecutionPeriod;
    }

    private static void copyObjectProperties(Object destination, Object source) {
        if (source != null)
            try {

                CopyUtils.copyProperties(destination, source);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
    }

    /**
     * @param infoExecutionDegree
     * @return IExecutionDegree
     */
    public static IExecutionDegree copyInfoExecutionDegree2ExecutionDegree(
            InfoExecutionDegree infoExecutionDegree) {

        IExecutionDegree executionDegree = new ExecutionDegree();
        IDegreeCurricularPlan degreeCurricularPlan = Cloner
                .copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoExecutionDegree
                        .getInfoDegreeCurricularPlan());

        IExecutionYear executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionDegree
                .getInfoExecutionYear());

        copyObjectProperties(executionDegree, infoExecutionDegree);

        executionDegree.setExecutionYear(executionYear);
        executionDegree.setDegreeCurricularPlan(degreeCurricularPlan);

        ICampus campus = Cloner.copyInfoCampus2ICampus(infoExecutionDegree.getInfoCampus());
        executionDegree.setCampus(campus);

        return executionDegree;

    }

    /**
     * @param executionDegree
     * @return InfoExecutionDegree
     */

    // DO NOT DELETE - this is used locally through introspection!!!
    protected static InfoExecutionDegree copy(IExecutionDegree executionDegree) {

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = Cloner
                .copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(executionDegree.getDegreeCurricularPlan());
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) Cloner.get(executionDegree
                .getExecutionYear());
        try {
            BeanUtils.copyProperties(infoExecutionDegree, executionDegree);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        infoExecutionDegree.setTemporaryExamMap(executionDegree.getTemporaryExamMap());

        // added by T�nia Pous�o
        InfoCampus infoCampus = Cloner.copyICampus2InfoCampus(executionDegree.getCampus());
        infoExecutionDegree.setInfoCampus(infoCampus);

        return infoExecutionDegree;

    }

    /**
     * Method copyInfoExecutionYear2IExecutionYear.
     * 
     * @param infoExecutionYear
     * @return IExecutionYear
     */
    public static IExecutionYear copyInfoExecutionYear2IExecutionYear(InfoExecutionYear infoExecutionYear) {
        IExecutionYear executionYear = new ExecutionYear();
        try {
            BeanUtils.copyProperties(executionYear, infoExecutionYear);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
        return executionYear;
    }

    /**
     * Method copyInfoExecutionYear2IExecutionYear.
     * 
     * @param infoExecutionYear
     * @return IExecutionYear
     */

    // DO NOT DELETE - this is used locally through introspection!!!
    protected static InfoExecutionYear copy(IExecutionYear executionYear) {
        InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        copyObjectProperties(infoExecutionYear, executionYear);
        return infoExecutionYear;
    }

    /**
     * Method copyIDegree2InfoDegree.
     * 
     * @param iCurso
     * @return InfoDegree
     */
    public static InfoDegree copyIDegree2InfoDegree(IDegree degree) {
        InfoDegree infoDegree = new InfoDegree();
        copyObjectProperties(infoDegree, degree);
        return infoDegree;
    }

    /**
     * Method copyInfoDegree2IDegree.
     * 
     * @param infoDegree
     * @return IDegree
     */
    public static IDegree copyInfoDegree2IDegree(InfoDegree infoDegree) {
        IDegree degree = new Degree();
        copyObjectProperties(degree, infoDegree);
        return degree;

    }

    /**
     * Method copyInfoExecutionPeriod2IExecutionPeriod.
     * 
     * @param infoExecutionPeriod
     * @return IExecutionPeriod
     */
    public static IExecutionPeriod copyInfoExecutionPeriod2IExecutionPeriod(
            InfoExecutionPeriod infoExecutionPeriod) {

        IExecutionPeriod executionPeriod = new ExecutionPeriod();
        InfoExecutionYear infoExecutionYear = infoExecutionPeriod.getInfoExecutionYear();
        IExecutionYear executionYear = null;
        if (infoExecutionYear != null) {
            executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionYear);
        } else {
            copyObjectProperties(executionYear, infoExecutionYear);
        }

        copyObjectProperties(executionPeriod, infoExecutionPeriod);

        executionPeriod.setExecutionYear(executionYear);

        return executionPeriod;
    }

    /**
     * Method copyInfoClass2Class.
     * 
     * @param infoTurma
     * @return ISchoolClass
     */
    public static ISchoolClass copyInfoClass2Class(InfoClass infoClass) {
        ISchoolClass domainClass = new SchoolClass();

        IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoClass
                .getInfoExecutionPeriod());
        IExecutionDegree executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(infoClass
                .getInfoExecutionDegree());

        copyObjectProperties(domainClass, infoClass);

        domainClass.setExecutionDegree(executionDegree);
        domainClass.setExecutionPeriod(executionPeriod);
        return domainClass;
    }

    /**
     * Method copyIShift2InfoShift.
     * 
     * @param elem
     * @return Object
     */
    // public static InfoShift copyIShift2InfoShift(IShift shift)
    // DO NOT DELETE - this is used locally through introspection!!!
    private static InfoShift copy(IShift shift) {

        if (shift == null)
            return null;
        InfoShift infoShift = new InfoShift();
        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner.get(shift
                .getDisciplinaExecucao());
        List infoLessonList = (List) CollectionUtils.collect(shift.getAssociatedLessons(),
                new Transformer() {
                    public Object transform(Object arg0) {
                        return copyILesson2InfoLesson((ILesson) arg0);
                    }
                });

        List infoClassesList = (List) CollectionUtils.collect(shift.getAssociatedClasses(),
                new Transformer() {
                    public Object transform(Object arg0) {
                        return copyClass2InfoClass((ISchoolClass) arg0);
                    }
                });

        copyObjectProperties(infoShift, shift);
        infoShift.setAvailabilityFinal(shift.getAvailabilityFinal());
        infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
        infoShift.setIdInternal(shift.getIdInternal());
        infoShift.setInfoLessons(infoLessonList);
        infoShift.setInfoClasses(infoClassesList);

        return infoShift;
    }

    /**
     * Method copyInfoStudent2IStudent.
     * 
     * @param infoStudent
     * @return IStudent
     */
    public static IStudent copyInfoStudent2IStudent(InfoStudent infoStudent) {
        IStudent student = new Student();
        IPerson person = Cloner.copyInfoPerson2IPerson(infoStudent.getInfoPerson());
        IStudentKind studentGroupInfo = Cloner.copyInfoStudentKind2IStudentKind(infoStudent
                .getInfoStudentKind());
        copyObjectProperties(student, infoStudent);
        student.setPerson(person);
        student.setStudentKind(studentGroupInfo);

        // by gedl at august the 5th, 2003
        student.setIdInternal(infoStudent.getIdInternal());
        return student;
    }

    /**
     * Method copyIStudent2InfoStudent.
     * 
     * @param elem
     * @return Object
     */
    public static InfoStudent copyIStudent2InfoStudent(IStudent student) {
        InfoStudent infoStudent = new InfoStudent();
        copyObjectProperties(infoStudent, student);

        infoStudent.setInfoPerson(Cloner.copyIPerson2InfoPerson(student.getPerson()));
        infoStudent
                .setInfoStudentKind(Cloner.copyIStudentKind2InfoStudentKind(student.getStudentKind()));

        // by gedl at august the 5th, 2003
        infoStudent.setIdInternal(student.getIdInternal());
        return infoStudent;
    }

    /**
     * Method copyInfoPerson2IPerson.
     * 
     * @param infoPerson
     * @return IPerson
     */
    public static IPerson copyInfoPerson2IPerson(InfoPerson infoPerson) {
        IPerson person = null;
        if (infoPerson != null) {
            person = new Person();
            ICountry country = Cloner.copyInfoCountry2ICountry(infoPerson.getInfoPais());
            copyObjectProperties(person, infoPerson);
            person.setPais(country);
        }
        return person;
    }

    /**
     * Method copyIPerson2InfoPerson.
     * 
     * @param Person
     * @return InfoPerson
     */
    public static InfoPerson copyIPerson2InfoPerson(IPerson person) {

        try {
            // Temporary bug fix.
            // Try to materialize the proxy... if it fails return null.
            // Should look into this further... but I don't have the time
            // at the moment. This situation occores constantly when
            // viewing Class 05102 of the execution period 80.
            person.getIdInternal();
        } catch (Throwable nex) {
            return null;
        }

        InfoPerson infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPerson();
            InfoCountry infoCountry = null;
            if (person.getPais() != null) {
                infoCountry = Cloner.copyICountry2InfoCountry(person.getPais());
            }

            copyObjectProperties(infoPerson, person);
            infoPerson.setInfoPais(infoCountry);

            if (person.getAdvisories() != null) {
                infoPerson.setInfoAdvisories((List) CollectionUtils.collect(person.getAdvisories(),
                        new Transformer() {
                            public Object transform(Object arg0) {
                                return copyIAdvisory2InfoAdvisory((IAdvisory) arg0);
                            }
                        }));
            } else {
                infoPerson.setInfoAdvisories(new ArrayList());
            }
        }
        return infoPerson;
    }

    /**
     * @param advisory
     * @return
     */
    public static InfoAdvisory copyIAdvisory2InfoAdvisory(IAdvisory advisory) {
        InfoAdvisory infoAdvisory = new InfoAdvisory();
        copyObjectProperties(infoAdvisory, advisory);
        return infoAdvisory;
    }

    /**
     * @param advisory
     * @return
     */
    public static IAdvisory copyInfoAdvisory2IAdvisory(InfoAdvisory infoAdvisory) {
        IAdvisory advisory = new Advisory();
        copyObjectProperties(advisory, infoAdvisory);
        return advisory;
    }

    public static InfoCandidateSituation copyICandidateSituation2InfoCandidateSituation(
            ICandidateSituation candidateSituation) {
        InfoCandidateSituation infoCandidateSituation = new InfoCandidateSituation();
        copyObjectProperties(infoCandidateSituation, candidateSituation);
        return infoCandidateSituation;
    }

    /**
     * Method copyIMasterDegreeCandidate2InfoMasterDegreCandidate
     * 
     * @param masterDegreeCandidate
     * @return InfoMasterDegreeCandidate
     */
    public static InfoMasterDegreeCandidate copyIMasterDegreeCandidate2InfoMasterDegreCandidate(
            IMasterDegreeCandidate masterDegreeCandidate) {
        if (masterDegreeCandidate == null)
            throw new IllegalArgumentException("ERROR -----------------------------");

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate();

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner.get(masterDegreeCandidate
                .getExecutionDegree());
        infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);

        InfoPerson infoPerson = Cloner.copyIPerson2InfoPerson(masterDegreeCandidate.getPerson());
        infoMasterDegreeCandidate.setInfoPerson(infoPerson);
        if (masterDegreeCandidate.getActiveCandidateSituation() != null) {
            InfoCandidateSituation infoCandidateSituation = Cloner
                    .copyICandidateSituation2InfoCandidateSituation(masterDegreeCandidate
                            .getActiveCandidateSituation());
            infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
        }

        copyObjectProperties(infoMasterDegreeCandidate, masterDegreeCandidate);
        return infoMasterDegreeCandidate;
    }

    /**
     * Method copyInfoCountry2ICountry
     * 
     * @param infoCountry
     * @return
     */
    public static ICountry copyInfoCountry2ICountry(InfoCountry infoCountry) {
        ICountry country = new Country();
        copyObjectProperties(country, infoCountry);
        return country;
    }

    /**
     * Method copyICountry2InfoCountry
     * 
     * @param country
     * @return
     */
    public static InfoCountry copyICountry2InfoCountry(ICountry country) {
        InfoCountry infoCountry = new InfoCountry();
        copyObjectProperties(infoCountry, country);
        return infoCountry;
    }

    /**
     * @param role
     * @return InfoRole
     */
    public static InfoRole copyIRole2InfoRole(IRole role) {
        InfoRole infoRole = new InfoRole();
        copyObjectProperties(infoRole, role);
        return infoRole;
    }

    public static InfoBibliographicReference copyIBibliographicReference2InfoBibliographicReference(
            IBibliographicReference bibliographicReference) {
        InfoBibliographicReference infoBibliographicReference = new InfoBibliographicReference();
        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner
                .get(bibliographicReference.getExecutionCourse());
        copyObjectProperties(infoBibliographicReference, bibliographicReference);
        infoBibliographicReference.setInfoExecutionCourse(infoExecutionCourse);
        return infoBibliographicReference;
    }

    /**
     * Method copyInfoSite2ISite.
     * 
     * @param infoSite
     * @return ISite
     */

    public static ISite copyInfoSite2ISite(InfoSite infoSite) {
        ISite site = new Site();
        IExecutionCourse executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoSite
                .getInfoExecutionCourse());

        copyObjectProperties(site, infoSite);
        site.setExecutionCourse(executionCourse);

        return site;
    }

    /**
     * Method copyISite2InfoSite.
     * 
     * @param site
     * @return InfoSite
     */

    public static InfoSite copyISite2InfoSite(ISite site) {
        InfoSite infoSite = new InfoSite();

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner.get(site
                .getExecutionCourse());

        copyObjectProperties(infoSite, site);
        infoSite.setInfoExecutionCourse(infoExecutionCourse);

        return infoSite;
    }

    /**
     * Method copyInfoSection2ISection.
     * 
     * @param infoSection
     * @return ISection
     */

    public static ISection copyInfoSection2ISection(InfoSection infoSection) {

        ISection section = new Section();

        ISection fatherSection = null;

        ISite site = Cloner.copyInfoSite2ISite(infoSection.getInfoSite());

        InfoSection infoSuperiorSection = infoSection.getSuperiorInfoSection();

        if (infoSuperiorSection != null) {
            fatherSection = Cloner.copyInfoSection2ISection(infoSuperiorSection);
        }

        copyObjectProperties(section, infoSection);

        section.setSuperiorSection(fatherSection);
        section.setSite(site);
        section.setIdInternal(infoSection.getIdInternal());

        return section;

    }

    /**
     * Method copyISection2InfoSection.
     * 
     * @param section
     * @return InfoSection
     */

    public static InfoSection copyISection2InfoSection(ISection section) {

        InfoSection infoSection = new InfoSection();

        InfoSection fatherInfoSection = null;

        InfoSite infoSite = Cloner.copyISite2InfoSite(section.getSite());

        ISection superiorSection = section.getSuperiorSection();

        if (superiorSection != null) {
            fatherInfoSection = Cloner.copyISection2InfoSection(superiorSection);
        }

        copyObjectProperties(infoSection, section);

        infoSection.setSuperiorInfoSection(fatherInfoSection);
        infoSection.setInfoSite(infoSite);
        infoSection.setIdInternal(section.getIdInternal());

        return infoSection;

    }

    /**
     * Method copyInfoItem2IItem.
     * 
     * @param infoItem
     * @return IItem
     */

    public static IItem copyInfoItem2IItem(InfoItem infoItem) {

        IItem item = new Item();

        ISection section = Cloner.copyInfoSection2ISection(infoItem.getInfoSection());

        copyObjectProperties(item, infoItem);

        item.setSection(section);

        return item;

    }

    /**
     * Method copyIItem2InfoItem.
     * 
     * @param item
     * @return InfoItem
     */

    public static InfoItem copyIItem2InfoItem(IItem item) {

        InfoItem infoItem = new InfoItem();
        InfoSection infoSection = Cloner.copyISection2InfoSection(item.getSection());

        copyObjectProperties(infoItem, item);

        infoItem.setInfoSection(infoSection);

        return infoItem;

    }

    /**
     * Method copyIAnnouncement2InfoAnnouncement.
     * 
     * @param announcement
     * @return InfoAnnouncement
     */
    public static InfoAnnouncement copyIAnnouncement2InfoAnnouncement(IAnnouncement announcement) {
        InfoAnnouncement infoAnnouncement = new InfoAnnouncement();

        InfoSite infoSite = Cloner.copyISite2InfoSite(announcement.getSite());

        copyObjectProperties(infoAnnouncement, announcement);
        infoAnnouncement.setInfoSite(infoSite);

        return infoAnnouncement;
    }

    /**
     * @param curriculum
     * @return InfoCurriculum
     */
    public static InfoCurriculum copyICurriculum2InfoCurriculum(ICurriculum curriculum) {
        InfoCurriculum infoCurriculum = new InfoCurriculum();

        InfoCurricularCourse infoCurricularCourse = Cloner
                .copyCurricularCourse2InfoCurricularCourse(curriculum.getCurricularCourse());
        InfoPerson infoPerson = Cloner.copyIPerson2InfoPerson(curriculum.getPersonWhoAltered());

        copyObjectProperties(infoCurriculum, curriculum);
        infoCurriculum.setInfoCurricularCourse(infoCurricularCourse);
        infoCurriculum.setInfoPersonWhoAltered(infoPerson);

        return infoCurriculum;
    }

    public static InfoUniversity copyIUniversity2InfoUniversity(IUniversity university) {
        InfoUniversity infoUniversity = new InfoUniversity();
        copyObjectProperties(infoUniversity, university);
        return infoUniversity;
    }

    public static IUniversity copyInfoUniversity2IUniversity(InfoUniversity infoUniversity) {
        IUniversity university = new University();
        copyObjectProperties(university, infoUniversity);
        return university;
    }

    /**
     * @param exam
     * @return InfoExam
     */
    public static InfoExam copyIExam2InfoExam(IExam exam) {
        InfoExam infoExam = new InfoExam();

        copyObjectProperties(infoExam, exam);
        List infoRooms = new ArrayList();
        List infoCurricularCourseScope = new ArrayList();
        List infoRoomOccupation = new ArrayList();
        List infoExecutionCourse = new ArrayList();

        List associatedRooms = exam.getAssociatedRooms();
        if (exam != null && associatedRooms != null && associatedRooms.size() > 0) {
            for (int i = 0; i < associatedRooms.size(); i++) {
                infoRooms.add(copyRoom2InfoRoom((IRoom) associatedRooms.get(i)));
            }
        }

        // associate curricularCourseScopes
        if (exam != null && exam.getAssociatedCurricularCourseScope() != null
                && exam.getAssociatedCurricularCourseScope().size() > 0) {
            for (int i = 0; i < exam.getAssociatedCurricularCourseScope().size(); i++) {
                ICurricularCourseScope scope = (ICurricularCourseScope) exam
                        .getAssociatedCurricularCourseScope().get(i);
                infoCurricularCourseScope
                        .add(copyICurricularCourseScope2InfoCurricularCourseScope(scope));
            }
        }
        infoExam.setAssociatedCurricularCourseScope(infoCurricularCourseScope);

        // associate room occupation (new version)
        if (exam != null && exam.getAssociatedRoomOccupation() != null
                && exam.getAssociatedRoomOccupation().size() > 0) {

            for (int i = 0; i < exam.getAssociatedRoomOccupation().size(); i++) {
                infoRoomOccupation.add(copyIRoomOccupation2InfoRoomOccupation((IRoomOccupation) exam
                        .getAssociatedRoomOccupation().get(i)));
            }
        }
        infoExam.setAssociatedRoomOccupation(infoRoomOccupation);

        // associate execution course (new version)
        if (exam != null && exam.getAssociatedExecutionCourses() != null
                && exam.getAssociatedExecutionCourses().size() > 0) {

            for (int i = 0; i < exam.getAssociatedExecutionCourses().size(); i++) {
                infoExecutionCourse.add(get((IExecutionCourse) exam.getAssociatedExecutionCourses().get(
                        i)));
            }
        }
        infoExam.setAssociatedExecutionCourse(infoExecutionCourse);

        return infoExam;
    }

    /**
     * @param infoExam
     * @return IExam
     */
    public static IExam copyInfoExam2IExam(InfoExam infoExam) {
        IExam exam = new Exam();

        copyObjectProperties(exam, infoExam);

        if (infoExam != null && infoExam.getAssociatedCurricularCourseScope() != null
                && infoExam.getAssociatedCurricularCourseScope().size() > 0) {
            List curricularCourseScopes = new ArrayList();
            for (int i = 0; i < infoExam.getAssociatedCurricularCourseScope().size(); i++) {
                curricularCourseScopes
                        .add(copyInfoCurricularCourseScope2ICurricularCourseScope((InfoCurricularCourseScope) infoExam
                                .getAssociatedCurricularCourseScope().get(i)));
            }
            exam.setAssociatedCurricularCourseScope(curricularCourseScopes);
        }

        if (infoExam != null && infoExam.getAssociatedRoomOccupation() != null
                && infoExam.getAssociatedRoomOccupation().size() > 0) {
            List roomOccupation = new ArrayList();
            for (int i = 0; i < infoExam.getAssociatedRoomOccupation().size(); i++) {
                roomOccupation.add(copyInfoRoomOccupation2IRoomOccupation((InfoRoomOccupation) infoExam
                        .getAssociatedRoomOccupation().get(i)));
            }
            exam.setAssociatedRoomOccupation(roomOccupation);
        }

        return exam;
    }

    // Ana e Ricardo
    // -----------------------------------------

    /**
     * @param InfoRoomOccupation
     * @return IRoomOcupation
     */
    public static IRoomOccupation copyInfoRoomOccupation2IRoomOccupation(
            InfoRoomOccupation infoRoomOccupation) {
        IRoomOccupation roomOccupation = new RoomOccupation();

        IPeriod period = Cloner.copyInfoPeriod2IPeriod(infoRoomOccupation.getInfoPeriod());
        IRoom room = Cloner.copyInfoRoom2Room(infoRoomOccupation.getInfoRoom());

        copyObjectProperties(roomOccupation, infoRoomOccupation);

        roomOccupation.setPeriod(period);
        roomOccupation.setRoom(room);

        return roomOccupation;
    }

    /**
     * @param IRoomOccupation
     * @return InfoRoomOccupation
     */
    public static InfoRoomOccupation copyIRoomOccupation2InfoRoomOccupation(
            IRoomOccupation roomOccupation) {

        InfoRoomOccupation infoRoomOccupation = new InfoRoomOccupation();

        InfoPeriod infoPeriod = Cloner.copyIPeriod2InfoPeriod(roomOccupation.getPeriod());
        InfoRoom infoRoom = Cloner.copyRoom2InfoRoom(roomOccupation.getRoom());

        copyObjectProperties(infoRoomOccupation, roomOccupation);

        infoRoomOccupation.setInfoPeriod(infoPeriod);
        infoRoomOccupation.setInfoRoom(infoRoom);

        return infoRoomOccupation;
    }

    /**
     * @param InfoPeriod
     * @return IPeriod
     */
    public static IPeriod copyInfoPeriod2IPeriod(InfoPeriod infoPeriod) {
        IPeriod period = new Period();

        copyObjectProperties(period, infoPeriod);

        return period;
    }

    /**
     * @param IPeriod
     * @return InfoPeriod
     */
    public static InfoPeriod copyIPeriod2InfoPeriod(IPeriod period) {
        InfoPeriod infoPeriod = new InfoPeriod();

        copyObjectProperties(infoPeriod, period);

        return infoPeriod;
    }

    /**
     * @param teacher
     * @return
     */
    public static InfoTeacher copyITeacher2InfoTeacher(ITeacher teacher) {
        InfoTeacher infoTeacher = null;
        InfoPerson infoPerson = null;
        InfoCategory infoCategory = null;

        if (teacher != null) {
            infoTeacher = new InfoTeacher();
            infoPerson = new InfoPerson();
            infoCategory = new InfoCategory();
            infoPerson = copyIPerson2InfoPerson(teacher.getPerson());
            infoCategory = copyICategory2InfoCategory(teacher.getCategory());
            copyObjectProperties(infoTeacher, teacher);
            infoTeacher.setInfoPerson(infoPerson);
            infoTeacher.setInfoCategory(infoCategory);
        }
        return infoTeacher;
    }

    /**
     * @param infoTeacher
     * @return
     */
    public static ITeacher copyInfoTeacher2Teacher(InfoTeacher infoTeacher) {
        ITeacher teacher = new Teacher();
        IPerson person = new Person();
        ICategory category = new Category();
        person = copyInfoPerson2IPerson(infoTeacher.getInfoPerson());
        category = copyInfoCategory2ICategory(infoTeacher.getInfoCategory());
        copyObjectProperties(teacher, infoTeacher);
        teacher.setPerson(person);
        teacher.setCategory(category);

        return teacher;
    }

    /**
     * @param category
     * @return
     */
    public static InfoCategory copyICategory2InfoCategory(ICategory category) {
        InfoCategory infoCategory = new InfoCategory();
        copyObjectProperties(infoCategory, category);

        return infoCategory;
    }

    /**
     * @param infoCategory
     * @return
     */
    public static ICategory copyInfoCategory2ICategory(InfoCategory infoCategory) {
        ICategory category = new Category();
        copyObjectProperties(category, infoCategory);

        return category;
    }

    /**
     * @param infoCourseReport
     * @return
     */
    public static ICourseReport copyInfoCourseReport2ICourseReport(InfoCourseReport infoCourseReport) {
        ICourseReport courseReport = new CourseReport();
        IExecutionCourse executionCourse = copyInfoExecutionCourse2ExecutionCourse(infoCourseReport
                .getInfoExecutionCourse());
        copyObjectProperties(courseReport, infoCourseReport);

        courseReport.setExecutionCourse(executionCourse);

        return courseReport;
    }

    /**
     * @author joana-nuno
     * @param IContributor
     * @return InfoContributor
     */

    public static InfoContributor copyIContributor2InfoContributor(IContributor contributor) {

        InfoContributor infoContributor = new InfoContributor();
        copyObjectProperties(infoContributor, contributor);
        return infoContributor;
    }

    /**
     * @param contributor
     * @return IContributor
     */
    public static IContributor copyInfoContributor2IContributor(InfoContributor infoContributor) {

        IContributor contributor = new Contributor();
        copyObjectProperties(contributor, infoContributor);
        return contributor;
    }

    /**
     * @param infoGuide
     * @return IGuide
     */
    public static IGuide copyInfoGuide2IGuide(InfoGuide infoGuide) {
        IGuide guide = new Guide();
        copyObjectProperties(guide, infoGuide);

        guide.setContributor(Cloner.copyInfoContributor2IContributor(infoGuide.getInfoContributor()));
        guide.setPerson(Cloner.copyInfoPerson2IPerson(infoGuide.getInfoPerson()));
        guide.setExecutionDegree(Cloner.copyInfoExecutionDegree2ExecutionDegree(infoGuide
                .getInfoExecutionDegree()));

        if (infoGuide.getInfoGuideEntries() != null) {
            Iterator iterator = infoGuide.getInfoGuideEntries().iterator();
            List guideEntries = new ArrayList();
            while (iterator.hasNext()) {
                guideEntries
                        .add(Cloner.copyInfoGuideEntry2IGuideEntry((InfoGuideEntry) iterator.next()));
            }

            guide.setGuideEntries(guideEntries);
        }

        return guide;
    }

    /**
     * @param guide
     * @return InfoGuide
     */
    public static InfoGuide copyIGuide2InfoGuide(IGuide guide) {

        InfoGuide infoGuide = new InfoGuide();
        copyObjectProperties(infoGuide, guide);
        infoGuide.setInfoContributor(Cloner.copyIContributor2InfoContributor(guide.getContributor()));
        infoGuide.setInfoPerson(Cloner.copyIPerson2InfoPerson(guide.getPerson()));
        infoGuide.setInfoExecutionDegree((InfoExecutionDegree) Cloner.get(guide.getExecutionDegree()));

        List infoGuideEntries = new ArrayList();
        if (guide.getGuideEntries() != null) {
            Iterator iterator = guide.getGuideEntries().iterator();
            while (iterator.hasNext()) {
                infoGuideEntries.add(Cloner
                        .copyIGuideEntry2InfoGuideEntry((IGuideEntry) iterator.next()));
            }
        }
        infoGuide.setInfoGuideEntries(infoGuideEntries);

        List infoGuideSituations = new ArrayList();
        if (guide.getGuideSituations() != null) {
            Iterator iterator = guide.getGuideSituations().iterator();
            while (iterator.hasNext()) {
                InfoGuideSituation infoGuideSituation = Cloner
                        .copyIGuideSituation2InfoGuideSituation((IGuideSituation) iterator.next());
                infoGuideSituations.add(infoGuideSituation);

                // Check to see if this is the active Situation

                if (infoGuideSituation.getState().equals(new State(State.ACTIVE))) {
                    infoGuide.setInfoGuideSituation(infoGuideSituation);
                }

            }
        }
        infoGuide.setInfoGuideSituations(infoGuideSituations);

        return infoGuide;
    }

    /**
     * @param guideEntry
     * @return InfoGuideEntry
     */
    public static InfoGuideEntry copyIGuideEntry2InfoGuideEntry(IGuideEntry guideEntry) {
        InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
        copyObjectProperties(infoGuideEntry, guideEntry);
        return infoGuideEntry;
    }

    /**
     * @param infoGuideEntry
     * @return IGuideEntry
     */
    public static IGuideEntry copyInfoGuideEntry2IGuideEntry(InfoGuideEntry infoGuideEntry) {
        IGuideEntry guideEntry = new GuideEntry();
        copyObjectProperties(guideEntry, infoGuideEntry);
        return guideEntry;
    }

    /**
     * @param guideSituation
     * @return InfoGuideSituation
     */
    public static InfoGuideSituation copyIGuideSituation2InfoGuideSituation(
            IGuideSituation guideSituation) {
        InfoGuideSituation infoGuideSituation = new InfoGuideSituation();
        copyObjectProperties(infoGuideSituation, guideSituation);
        return infoGuideSituation;
    }

    /**
     * @param infoGuideSituation
     * @return IGuideSituation
     */
    public static IGuideSituation copyInfoGuideSituation2IGuideSituation(
            InfoGuideSituation infoGuideSituation) {
        IGuideSituation guideSituation = new GuideSituation();
        copyObjectProperties(guideSituation, infoGuideSituation);
        guideSituation.setGuide(Cloner.copyInfoGuide2IGuide(infoGuideSituation.getInfoGuide()));
        return guideSituation;
    }

    // ---------------------------------------------- DCS-RJAO
    // -----------------------------------------------

    /**
     * @author dcs-rjao
     * @param InfoDegreeCurricularPlan
     * @return IDegreeCurricularPlan
     */
    public static IDegreeCurricularPlan copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(
            InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
        IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();

        IDegree degree = Cloner.copyInfoDegree2IDegree(infoDegreeCurricularPlan.getInfoDegree());

        copyObjectProperties(degreeCurricularPlan, infoDegreeCurricularPlan);

        degreeCurricularPlan.setDegree(degree);

        return degreeCurricularPlan;
    }

    /**
     * @author dcs-rjao
     * @param IDegreeCurricularPlan
     * @return InfoDegreeCurricularPlan
     */
    public static InfoDegreeCurricularPlan copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(
            IDegreeCurricularPlan degreeCurricularPlan) {

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();

        InfoDegree infoDegree = Cloner.copyIDegree2InfoDegree(degreeCurricularPlan.getDegree());

        copyObjectProperties(infoDegreeCurricularPlan, degreeCurricularPlan);

        infoDegreeCurricularPlan.setInfoDegree(infoDegree);

        return infoDegreeCurricularPlan;
    }

    /**
     * @author dcs-rjao
     * @param InfoBranch
     * @return IBranch
     */
    public static IBranch copyInfoBranch2IBranch(InfoBranch infoBranch) {

        IBranch branch = new Branch();
        if (infoBranch != null) {
            IDegreeCurricularPlan degreeCurricularPlan = Cloner
                    .copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoBranch
                            .getInfoDegreeCurricularPlan());
            copyObjectProperties(branch, infoBranch);
            branch.setDegreeCurricularPlan(degreeCurricularPlan);
        }
        return branch;
    }

    public static InfoCurricularCourse copyCurricularCourse2InfoCurricularCourseWithCurricularCourseScopes(
            ICurricularCourse curricularCourse) {

        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(curricularCourse
                .getDegreeCurricularPlan());

        copyObjectProperties(infoCurricularCourse, curricularCourse);

        infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

        List infoCurricularCourseScopes = new ArrayList();
        Iterator iter = curricularCourse.getScopes().iterator();
        while (iter.hasNext()) {
            infoCurricularCourseScopes
                    .add(copyICurricularCourseScope2InfoCurricularCourseScope((ICurricularCourseScope) iter
                            .next()));

        }
        infoCurricularCourse.setInfoScopes(infoCurricularCourseScopes);

        return infoCurricularCourse;
    }

    /**
     * @author dcs-rjao
     * @param IBranch
     * @return InfoBranch
     */
    public static InfoBranch copyIBranch2InfoBranch(IBranch branch) {

        InfoBranch infoBranch = new InfoBranch();
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        // modified by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on
        // 25/Set/2003
        if (branch != null) {
            infoDegreeCurricularPlan = Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(branch
                    .getDegreeCurricularPlan());
            copyObjectProperties(infoBranch, branch);
        }
        infoBranch.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        return infoBranch;
    }

    /**
     * @author dcs-rjao
     * @param InfoCurricularCourse
     * @return ICurricularCourse
     */
    public static ICurricularCourse copyInfoCurricularCourse2CurricularCourse(
            InfoCurricularCourse infoCurricularCourse) {

        ICurricularCourse curricularCourse = new CurricularCourse();
        IUniversity university = Cloner.copyInfoUniversity2IUniversity(infoCurricularCourse
                .getInfoUniversity());
        IDegreeCurricularPlan planoCurricularCurso = copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoCurricularCourse
                .getInfoDegreeCurricularPlan());

        copyObjectProperties(curricularCourse, infoCurricularCourse);

        if (infoCurricularCourse.getInfoScientificArea() != null) {
            IScientificArea scientificArea = copyInfoScientificArea2IScientificArea(infoCurricularCourse
                    .getInfoScientificArea());
            curricularCourse.setScientificArea(scientificArea);
        }

        curricularCourse.setDegreeCurricularPlan(planoCurricularCurso);
        curricularCourse.setUniversity(university);
        return curricularCourse;
    }

    /**
     * @author dcs-rjao
     * @param ICurricularCourse
     * @return InfoCurricularCourse
     */

    public static InfoCurricularCourse copyCurricularCourse2InfoCurricularCourse(
            ICurricularCourse curricularCourse) {

        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
        InfoUniversity infoUniversity = Cloner.copyIUniversity2InfoUniversity(curricularCourse
                .getUniversity());
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(curricularCourse
                .getDegreeCurricularPlan());

        copyObjectProperties(infoCurricularCourse, curricularCourse);

        if (curricularCourse.getScientificArea() != null) {
            InfoScientificArea infoScientificArea = copyIScientificArea2InfoScientificArea(curricularCourse
                    .getScientificArea());
            infoCurricularCourse.setInfoScientificArea(infoScientificArea);
        }

        infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        infoCurricularCourse.setInfoUniversity(infoUniversity);
        return infoCurricularCourse;
    }

    /**
     * @author dcs-rjao
     * @param ICurricularCourseScope
     * @return InfoCurricularCourseScope
     */

    public static InfoCurricularCourseScope copyICurricularCourseScope2InfoCurricularCourseScope(
            ICurricularCourseScope curricularCourseScope) {

        InfoCurricularCourseScope infoCurricularCourseScope = new InfoCurricularCourseScope();

        InfoCurricularCourse infoCurricularCourse = copyCurricularCourse2InfoCurricularCourse(curricularCourseScope
                .getCurricularCourse());
        InfoCurricularSemester infoCurricularSemester = copyCurricularSemester2InfoCurricularSemester(curricularCourseScope
                .getCurricularSemester());
        InfoBranch infoBranch = copyIBranch2InfoBranch(curricularCourseScope.getBranch());

        copyObjectProperties(infoCurricularCourseScope, curricularCourseScope);

        infoCurricularCourseScope.setInfoCurricularCourse(infoCurricularCourse);
        infoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);
        infoCurricularCourseScope.setInfoBranch(infoBranch);

        return infoCurricularCourseScope;
    }

    /**
     * @author dcs-rjao
     * @param InfoCurricularCourseScope
     * @return ICurricularCourseScope
     */
    public static ICurricularCourseScope copyInfoCurricularCourseScope2ICurricularCourseScope(
            InfoCurricularCourseScope infoCurricularCourseScope) {

        ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();

        ICurricularCourse curricularCourse = copyInfoCurricularCourse2CurricularCourse(infoCurricularCourseScope
                .getInfoCurricularCourse());
        ICurricularSemester curricularSemester = copyInfoCurricularSemester2CurricularSemester(infoCurricularCourseScope
                .getInfoCurricularSemester());
        IBranch branch = copyInfoBranch2IBranch(infoCurricularCourseScope.getInfoBranch());

        copyObjectProperties(curricularCourseScope, infoCurricularCourseScope);

        curricularCourseScope.setCurricularCourse(curricularCourse);
        curricularCourseScope.setCurricularSemester(curricularSemester);
        curricularCourseScope.setBranch(branch);

        return curricularCourseScope;
    }

    /**
     * @author dcs-rjao
     * @param InfoCurricularSemester
     * @return ICurricularSemester
     */
    public static ICurricularSemester copyInfoCurricularSemester2CurricularSemester(
            InfoCurricularSemester infoCurricularSemester) {

        ICurricularSemester curricularSemester = new CurricularSemester();

        ICurricularYear curricularYear = copyInfoCurricularYear2CurricularYear(infoCurricularSemester
                .getInfoCurricularYear());

        copyObjectProperties(curricularSemester, infoCurricularSemester);
        curricularSemester.setCurricularYear(curricularYear);

        return curricularSemester;
    }

    /**
     * @author dcs-rjao
     * @param ICurricularSemester
     * @return InfoCurricularSemester
     */
    public static InfoCurricularSemester copyCurricularSemester2InfoCurricularSemester(
            ICurricularSemester curricularSemester) {

        InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester();

        InfoCurricularYear infoCurricularYear = copyCurricularYear2InfoCurricularYear(curricularSemester
                .getCurricularYear());

        copyObjectProperties(infoCurricularSemester, curricularSemester);

        infoCurricularSemester.setInfoCurricularYear(infoCurricularYear);

        return infoCurricularSemester;
    }

    /**
     * @author dcs-rjao
     * @param InfoCurricularYear
     * @return ICurricularYear
     */
    public static ICurricularYear copyInfoCurricularYear2CurricularYear(
            InfoCurricularYear infoCurricularYear) {
        ICurricularYear curricularYear = new CurricularYear();
        copyObjectProperties(curricularYear, infoCurricularYear);
        return curricularYear;
    }

    /**
     * @author dcs-rjao
     * @param ICurricularYear
     * @return InfoCurricularYear
     */
    public static InfoCurricularYear copyCurricularYear2InfoCurricularYear(ICurricularYear curricularYear) {
        InfoCurricularYear infoCurricularYear = new InfoCurricularYear();
        copyObjectProperties(infoCurricularYear, curricularYear);
        return infoCurricularYear;
    }

    /**
     * @author dcs-rjao
     * @param copyInfoStudentCurricularPlan2IStudentCurricularPlan
     * @return IStudentCurricularPlan
     */
    public static IStudentCurricularPlan copyInfoStudentCurricularPlan2IStudentCurricularPlan(
            InfoStudentCurricularPlan infoStudentCurricularPlan) {

        IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();

        IStudent student = Cloner.copyInfoStudent2IStudent(infoStudentCurricularPlan.getInfoStudent());
        IBranch branch = Cloner.copyInfoBranch2IBranch(infoStudentCurricularPlan.getInfoBranch());

        IBranch secundaryBranch = null;
        if (infoStudentCurricularPlan.getInfoSecundaryBranch() != null) {
            secundaryBranch = Cloner.copyInfoBranch2IBranch(infoStudentCurricularPlan
                    .getInfoSecundaryBranch());
        }
        IDegreeCurricularPlan degreeCurricularPlan = Cloner
                .copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoStudentCurricularPlan
                        .getInfoDegreeCurricularPlan());

        try {
            BeanUtils.copyProperties(studentCurricularPlan, infoStudentCurricularPlan);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        studentCurricularPlan.setStudent(student);
        studentCurricularPlan.setBranch(branch);
        studentCurricularPlan.setSecundaryBranch(secundaryBranch);
        studentCurricularPlan.setDegreeCurricularPlan(degreeCurricularPlan);

        return studentCurricularPlan;
    }

    /**
     * @author dcs-rjao
     * @param copyIStudentCurricularPlan2InfoStudentCurricularPlan
     * @return InfoStudentCurricularPlan
     */
    public static InfoStudentCurricularPlan copyIStudentCurricularPlan2InfoStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan) {

        InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();

        InfoStudent infoStudent = null;
        if (studentCurricularPlan.getStudent() != null) {
            infoStudent = Cloner.copyIStudent2InfoStudent(studentCurricularPlan.getStudent());
        }
        InfoBranch infoBranch = null;
        if (studentCurricularPlan.getBranch() != null) {
            infoBranch = Cloner.copyIBranch2InfoBranch(studentCurricularPlan.getBranch());
        }
        InfoBranch infoSecundaryBranch = null;
        if (studentCurricularPlan.getSecundaryBranch() != null) {
            infoSecundaryBranch = Cloner.copyIBranch2InfoBranch(studentCurricularPlan
                    .getSecundaryBranch());
        }
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        if (studentCurricularPlan.getDegreeCurricularPlan() != null) {
            infoDegreeCurricularPlan = Cloner
                    .copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(studentCurricularPlan
                            .getDegreeCurricularPlan());
        }
        try {
            BeanUtils.copyProperties(infoStudentCurricularPlan, studentCurricularPlan);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        infoStudentCurricularPlan.setInfoStudent(infoStudent);
        infoStudentCurricularPlan.setInfoBranch(infoBranch);
        infoStudentCurricularPlan.setInfoSecundaryBranch(infoSecundaryBranch);
        infoStudentCurricularPlan.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

        return infoStudentCurricularPlan;
    }

    /**
     * @author dcs-rjao
     * @param IStudentKind
     * @return InfoStudentKind
     */
    public static InfoStudentKind copyIStudentKind2InfoStudentKind(IStudentKind studentGroupInfo) {
        InfoStudentKind infoStudentKind = new InfoStudentKind();
        copyObjectProperties(infoStudentKind, studentGroupInfo);
        return infoStudentKind;
    }

    /**
     * @author dcs-rjao
     * @param IStudentKind
     * @return InfoStudentKind
     */
    public static IStudentKind copyInfoStudentKind2IStudentKind(InfoStudentKind infoStudentGroupInfo) {
        IStudentKind studentKind = new StudentKind();
        copyObjectProperties(studentKind, infoStudentGroupInfo);
        return studentKind;
    }

    /**
     * @author dcs-rjao
     * @param IEnrolmentEvaluation
     * @return InfoEnrolmentEvaluation
     */
    public static InfoEnrolmentEvaluation copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(
            IEnrolmentEvaluation enrolmentEvaluation) {
        // properties of infoEnrolment are not copied for not to get into loop
        InfoEnrolmentEvaluation infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
        InfoPerson infoPerson = copyIPerson2InfoPerson(enrolmentEvaluation
                .getPersonResponsibleForGrade());
        copyObjectProperties(infoEnrolmentEvaluation, enrolmentEvaluation);
        infoEnrolmentEvaluation.setInfoPersonResponsibleForGrade(infoPerson);
        return infoEnrolmentEvaluation;
    }

    /**
     * @author dcs-rjao
     * @param IEnrolmentEvaluation
     * @return InfoEnrolmentEvaluation
     */
    public static IEnrolmentEvaluation copyInfoEnrolmentEvaluation2IEnrolmentEvaluation(
            InfoEnrolmentEvaluation infoEnrolmentEvaluation) {
        // properties of infoEnrolment are not copied for not to get into loop
        IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
        IPerson pessoa = copyInfoPerson2IPerson(infoEnrolmentEvaluation
                .getInfoPersonResponsibleForGrade());
        copyObjectProperties(enrolmentEvaluation, infoEnrolmentEvaluation);
        enrolmentEvaluation.setPersonResponsibleForGrade(pessoa);
        return enrolmentEvaluation;
    }

    public static InfoShiftProfessorship copyIShiftProfessorship2InfoShiftProfessorship(
            IShiftProfessorship teacherShiftPercentage) {
        InfoShiftProfessorship infoTeacherShiftPercentage = new InfoShiftProfessorship();

        InfoProfessorship infoProfessorShip = Cloner
                .copyIProfessorship2InfoProfessorship(teacherShiftPercentage.getProfessorship());
        InfoShift infoShift = (InfoShift) Cloner.get(teacherShiftPercentage.getShift());

        copyObjectProperties(infoTeacherShiftPercentage, teacherShiftPercentage);

        infoTeacherShiftPercentage.setInfoProfessorship(infoProfessorShip);
        infoTeacherShiftPercentage.setInfoShift(infoShift);

        return infoTeacherShiftPercentage;
    }

    public static InfoProfessorship copyIProfessorship2InfoProfessorship(IProfessorship professorship) {
        InfoProfessorship infoProfessorShip = new InfoProfessorship();

        InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(professorship.getTeacher());

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner.get(professorship
                .getExecutionCourse());

        copyObjectProperties(infoProfessorShip, professorship);

        infoProfessorShip.setInfoTeacher(infoTeacher);
        infoProfessorShip.setInfoExecutionCourse(infoExecutionCourse);

        return infoProfessorShip;
    }

    public static IShiftProfessorship copyInfoShiftProfessorship2IShiftProfessorship(
            InfoShiftProfessorship infoTeacherShiftPercentage) {
        InfoShift infoShift = infoTeacherShiftPercentage.getInfoShift();
        InfoProfessorship infoProfessorShip = infoTeacherShiftPercentage.getInfoProfessorship();

        IShiftProfessorship teacherShiftPercentage = new ShiftProfessorship();
        IProfessorship professorship = Cloner.copyInfoProfessorship2IProfessorship(infoProfessorShip);
        IShift shift = Cloner.copyInfoShift2IShift(infoShift);

        copyObjectProperties(teacherShiftPercentage, infoTeacherShiftPercentage);

        teacherShiftPercentage.setPercentage(infoTeacherShiftPercentage.getPercentage());
        teacherShiftPercentage.setShift(shift);
        teacherShiftPercentage.setProfessorship(professorship);

        return teacherShiftPercentage;
    }

    public static IProfessorship copyInfoProfessorship2IProfessorship(InfoProfessorship infoProfessorShip) {
        InfoExecutionCourse infoExecutionCourse = infoProfessorShip.getInfoExecutionCourse();
        InfoTeacher infoTeacher = infoProfessorShip.getInfoTeacher();

        IProfessorship professorship = new Professorship();

        copyObjectProperties(professorship, infoProfessorShip);
        IExecutionCourse executionCourse = null;
        if (infoExecutionCourse != null) {
            executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
        } else {
            copyObjectProperties(executionCourse, infoExecutionCourse);
        }
        professorship.setExecutionCourse(executionCourse);

        ITeacher teacher = null;

        if (infoTeacher != null) {
            teacher = Cloner.copyInfoTeacher2Teacher(infoTeacher);
        } else {
            copyObjectProperties(teacher, infoTeacher);
        }
        professorship.setTeacher(teacher);

        return professorship;
    }

    /**
     * @param IAttends
     * @return InfoFrequenta
     */
    public static InfoFrequenta copyIFrequenta2InfoFrequenta(IAttends frequenta) {
        if (frequenta == null) {
            return null;
        }

        InfoFrequenta infoFrequenta = new InfoFrequenta();

        InfoStudent infoStudent = new InfoStudent();
        infoStudent = Cloner.copyIStudent2InfoStudent(frequenta.getAluno());

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
        infoExecutionCourse = (InfoExecutionCourse) Cloner.get(frequenta.getDisciplinaExecucao());

        InfoEnrolment infoEnrolment = null;
        if (frequenta.getEnrolment() != null) {
            infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                    .newInfoFromDomain(frequenta.getEnrolment());
        }

        infoFrequenta.setIdInternal(frequenta.getIdInternal());
        infoFrequenta.setAluno(infoStudent);
        infoFrequenta.setDisciplinaExecucao(infoExecutionCourse);
        infoFrequenta.setInfoEnrolment(infoEnrolment);

        return infoFrequenta;

    }

    /**
     * @param price
     * @return InfoPrice
     */
    public static InfoPrice copyIPrice2InfoPrice(IPrice price) {

        InfoPrice infoPrice = new InfoPrice();
        copyObjectProperties(infoPrice, price);
        return infoPrice;
    }

    /**
     * @param examStudentRoom
     * @return
     */
    public static InfoExamStudentRoom copyIExamStudentRoom2InfoExamStudentRoom(
            IExamStudentRoom examStudentRoom) {

        InfoExam infoExam = Cloner.copyIExam2InfoExam(examStudentRoom.getExam());
        InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(examStudentRoom.getStudent());
        InfoRoom infoRoom = Cloner.copyRoom2InfoRoom(examStudentRoom.getRoom());

        InfoExamStudentRoom infoExamStudentRoom = new InfoExamStudentRoom();

        copyObjectProperties(infoExamStudentRoom, examStudentRoom);
        infoExamStudentRoom.setInfoExam(infoExam);
        infoExamStudentRoom.setInfoRoom(infoRoom);
        infoExamStudentRoom.setInfoStudent(infoStudent);
        return infoExamStudentRoom;
    }

    public static InfoDepartment copyIDepartment2InfoDepartment(IDepartment department) {
        InfoDepartment infoDeparment = new InfoDepartment();
        copyObjectProperties(infoDeparment, department);
        return infoDeparment;
    }

    public static InfoSummary copyISummary2InfoSummary(ISummary summary) {
        if (summary == null)
            return null;

        InfoSummary infoSummary = new InfoSummary();
        copyObjectProperties(infoSummary, summary);

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) get(summary.getExecutionCourse());
        infoSummary.setInfoExecutionCourse(infoExecutionCourse);

        if (summary.getShift() != null) {
            InfoShift infoShift = copy(summary.getShift());
            infoSummary.setInfoShift(infoShift);
        }
        if (summary.getProfessorship() != null) {
            InfoProfessorship infoProfessorship = copyIProfessorship2InfoProfessorship(summary
                    .getProfessorship());
            infoSummary.setInfoProfessorship(infoProfessorship);
        }
        if (summary.getTeacher() != null) {
            InfoTeacher infoTeacher = copyITeacher2InfoTeacher(summary.getTeacher());
            infoSummary.setInfoTeacher(infoTeacher);
        }

        if (summary.getRoom() != null) {
            InfoRoom infoRoom = copyRoom2InfoRoom(summary.getRoom());
            infoSummary.setInfoRoom(infoRoom);
        }
        return infoSummary;
    }

    public static ISummary copyInfoSummary2ISummary(InfoSummary infoSummary) {
        if (infoSummary != null)
            return null;

        ISummary summary = new Summary();
        copyObjectProperties(summary, infoSummary);
        IExecutionCourse executionCourse = copyInfoExecutionCourse2ExecutionCourse(infoSummary
                .getInfoExecutionCourse());
        summary.setExecutionCourse(executionCourse);

        if (infoSummary.getInfoShift() != null) {
            IShift shift = copyInfoShift2IShift(infoSummary.getInfoShift());
            summary.setShift(shift);
        }

        if (infoSummary.getInfoProfessorship() != null) {
            IProfessorship professorship = copyInfoProfessorship2IProfessorship(infoSummary
                    .getInfoProfessorship());
            summary.setProfessorship(professorship);
        }

        if (infoSummary.getInfoTeacher() != null) {
            ITeacher teacher = copyInfoTeacher2Teacher(infoSummary.getInfoTeacher());
            summary.setTeacher(teacher);
        }

        if (infoSummary.getInfoRoom() != null) {
            IRoom room = copyInfoRoom2Room(infoSummary.getInfoRoom());
            summary.setRoom(room);
        }
        return summary;
    }

    /**
     * @param candidateEnrolment
     * @return InfoCandidateEnrolment
     */
    public static InfoCandidateEnrolment copyICandidateEnrolment2InfoCandidateEnrolment(
            ICandidateEnrolment candidateEnrolment) {

        InfoCandidateEnrolment infoCandidateEnrolment = new InfoCandidateEnrolment();

        infoCandidateEnrolment.setInfoCurricularCourse(Cloner
                .copyCurricularCourse2InfoCurricularCourse(candidateEnrolment.getCurricularCourse()));
        infoCandidateEnrolment.setInfoMasterDegreeCandidate(Cloner
                .copyIMasterDegreeCandidate2InfoMasterDegreCandidate(candidateEnrolment
                        .getMasterDegreeCandidate()));

        return infoCandidateEnrolment;
    }

    /**
     * @param groupProperties
     * @return infoGroupProperties
     */

    public static InfoGroupProperties copyIGroupProperties2InfoGroupProperties(
            IGroupProperties groupProperties) {
        InfoGroupProperties infoGroupProperties = new InfoGroupProperties();

        infoGroupProperties.setEnrolmentBeginDay(groupProperties.getEnrolmentBeginDay());
        infoGroupProperties.setEnrolmentEndDay(groupProperties.getEnrolmentEndDay());
        infoGroupProperties.setMaximumCapacity(groupProperties.getMaximumCapacity());
        infoGroupProperties.setMinimumCapacity(groupProperties.getMinimumCapacity());
        infoGroupProperties.setIdealCapacity(groupProperties.getIdealCapacity());
        infoGroupProperties.setGroupMaximumNumber(groupProperties.getGroupMaximumNumber());
        infoGroupProperties.setEnrolmentPolicy(groupProperties.getEnrolmentPolicy());
        infoGroupProperties.setIdInternal(groupProperties.getIdInternal());
        infoGroupProperties.setName(groupProperties.getName());
        infoGroupProperties.setShiftType(groupProperties.getShiftType());
        infoGroupProperties.setProjectDescription(groupProperties.getProjectDescription());

        return infoGroupProperties;
    }

    /**
     * @param infoGroupProperties
     * @return IGroupProperties
     */

    public static IGroupProperties copyInfoGroupProperties2IGroupProperties(
            InfoGroupProperties infoGroupProperties) {
        IGroupProperties groupProperties = new GroupProperties();

        groupProperties.setEnrolmentBeginDay(infoGroupProperties.getEnrolmentBeginDay());
        groupProperties.setEnrolmentEndDay(infoGroupProperties.getEnrolmentEndDay());
        groupProperties.setMaximumCapacity(infoGroupProperties.getMaximumCapacity());
        groupProperties.setMinimumCapacity(infoGroupProperties.getMinimumCapacity());
        groupProperties.setIdealCapacity(infoGroupProperties.getIdealCapacity());
        groupProperties.setGroupMaximumNumber(infoGroupProperties.getGroupMaximumNumber());
        groupProperties.setEnrolmentPolicy(infoGroupProperties.getEnrolmentPolicy());
        groupProperties.setIdInternal(infoGroupProperties.getIdInternal());
        groupProperties.setName(infoGroupProperties.getName());
        groupProperties.setShiftType(infoGroupProperties.getShiftType());
        groupProperties.setProjectDescription(infoGroupProperties.getProjectDescription());

        return groupProperties;
    }

    /**
     * @param infoGroupPropertiesExecutionCourse
     * @return IGroupPropertiesExecutionCourse
     */

    public static IGroupPropertiesExecutionCourse copyInfoGroupPropertiesExecutionCourse2IGroupPropertiesExecutionCourse(
            InfoGroupPropertiesExecutionCourse infoGroupPropertiesExecutionCourse) {
        IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = new GroupPropertiesExecutionCourse();
        IGroupProperties groupProperties = copyInfoGroupProperties2IGroupProperties(infoGroupPropertiesExecutionCourse
                .getInfoGroupProperties());
        IExecutionCourse executionCourse = copyInfoExecutionCourse2ExecutionCourse(infoGroupPropertiesExecutionCourse
                .getInfoExecutionCourse());
        groupPropertiesExecutionCourse.setExecutionCourse(executionCourse);
        groupPropertiesExecutionCourse.setGroupProperties(groupProperties);
        return groupPropertiesExecutionCourse;
    }

    /**
     * @param IGroupPropertiesExecutionCourse
     * @return infoGroupPropertiesExecutionCourse
     */

    public static InfoGroupPropertiesExecutionCourse copyIGroupPropertiesExecutionCourse2InfoGroupPropertiesExecutionCourse(
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) {
        InfoGroupPropertiesExecutionCourse infoGroupPropertiesExecutionCourse = new InfoGroupPropertiesExecutionCourse();
        infoGroupPropertiesExecutionCourse.setIdInternal(groupPropertiesExecutionCourse.getIdInternal());
        InfoGroupProperties infoGroupProperties = copyIGroupProperties2InfoGroupProperties(groupPropertiesExecutionCourse
                .getGroupProperties());
        InfoExecutionCourse infoExecutionCourse = copy(groupPropertiesExecutionCourse
                .getExecutionCourse());
        infoGroupPropertiesExecutionCourse.setInfoExecutionCourse(infoExecutionCourse);
        infoGroupPropertiesExecutionCourse.setInfoGroupProperties(infoGroupProperties);
        return infoGroupPropertiesExecutionCourse;
    }

    /**
     * @param attendsSet
     * @return infoAttendsSet
     */

    public static InfoAttendsSet copyIAttendsSet2InfoAttendsSet(IAttendsSet attendsSet) {

        InfoAttendsSet infoAttendsSet = new InfoAttendsSet();
        infoAttendsSet.setIdInternal(attendsSet.getIdInternal());
        infoAttendsSet.setName(attendsSet.getName());

        return infoAttendsSet;
    }

    /**
     * @param studentGroup
     * @return infoStudentGroup
     */

    public static InfoStudentGroup copyIStudentGroup2InfoStudentGroup(IStudentGroup studentGroup) {
        InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
        InfoShift infoShift = new InfoShift();

        if (studentGroup.getAttendsSet().getGroupProperties().getShiftType() != null) {
            infoShift = copyShift2InfoShift(studentGroup.getShift());
            infoStudentGroup.setInfoShift(infoShift);
        }
        infoStudentGroup.setGroupNumber(studentGroup.getGroupNumber());
        infoStudentGroup.setIdInternal(studentGroup.getIdInternal());

        return infoStudentGroup;
    }

    /**
     * @param studentGroupAttend
     * @return infoStudentGroupAttend
     */

    public static InfoStudentGroupAttend copyIStudentGroupAttend2InfoStudentGroupAttend(
            IStudentGroupAttend studentGroupAttend) {
        InfoStudentGroupAttend infoStudentGroupAttend = new InfoStudentGroupAttend();
        InfoFrequenta infoAttend = new InfoFrequenta();
        InfoStudentGroup infoStudentGroup = new InfoStudentGroup();

        infoAttend = copyIFrequenta2InfoFrequenta(studentGroupAttend.getAttend());
        infoStudentGroup = copyIStudentGroup2InfoStudentGroup(studentGroupAttend.getStudentGroup());

        studentGroupAttend.setIdInternal(studentGroupAttend.getIdInternal());
        infoStudentGroupAttend.setInfoAttend(infoAttend);
        infoStudentGroupAttend.setInfoStudentGroup(infoStudentGroup);
        return infoStudentGroupAttend;
    }

    public static InfoCurricularYear copyICurricularYear2InfoCurricularYear(
            ICurricularYear curricularYear) {
        InfoCurricularYear infoCurricularYear = new InfoCurricularYear();
        copyObjectProperties(infoCurricularYear, curricularYear);
        return infoCurricularYear;
    }

    // by gedl AT rnl DOT ist DOT utl DOT pt (August the 5th, 2003)
    public static ICandidacy copyInfoCandicacy2ICandidacy(InfoCandidacy infoCandidacy) {
        ICandidacy candidacy = null;
        if (infoCandidacy != null) {
            candidacy = new Candidacy();
            List caseStudyChoices = new LinkedList();
            candidacy.setMotivation(infoCandidacy.getMotivation());
            for (Iterator iter = infoCandidacy.getCaseStudyChoices().iterator(); iter.hasNext();) {
                InfoCaseStudyChoice element = (InfoCaseStudyChoice) iter.next();
                ICaseStudyChoice caseStudy = new CaseStudyChoice();
                caseStudy.setOrder(element.getOrder());
                caseStudy.setCaseStudy(new CaseStudy());
                caseStudy.getCaseStudy().setIdInternal(element.getCaseStudy().getIdInternal());
                caseStudyChoices.add(caseStudy);
            }

            candidacy.setCaseStudyChoices(caseStudyChoices);
            
            candidacy.setCurricularCourse(new CurricularCourse());
            candidacy.getCurricularCourse().setIdInternal(infoCandidacy.getCurricularCourse()
                    .getIdInternal());
            
            candidacy.setStudent(new Student(infoCandidacy.getInfoStudent().getIdInternal()));
            
            candidacy.setTheme(new Theme());
            candidacy.getTheme().setIdInternal(infoCandidacy.getTheme().getIdInternal());
            
            candidacy.setModality(new Modality());
            candidacy.getModality().setIdInternal(infoCandidacy.getInfoModality().getIdInternal());
            
            candidacy.setSeminary(new Seminary());
            candidacy.getSeminary().setIdInternal(infoCandidacy.getInfoSeminary().getIdInternal());
            
            candidacy.setIdInternal(infoCandidacy.getIdInternal());
        }
        return candidacy;
    }

    public static InfoWebSite copyIWebSite2InfoWebSite(IWebSite webSite) {

        InfoWebSite infoWebSite = null;

        if (webSite instanceof ISiteIST) {
            infoWebSite = new InfoSiteIST();
        }

        copyObjectProperties(infoWebSite, webSite);

        return infoWebSite;
    }

    public static InfoWebSiteSection copyIWebSiteSection2InfoWebSiteSection(IWebSiteSection section) {

        InfoWebSiteSection infoWebSiteSection = new InfoWebSiteSection();

        copyObjectProperties(infoWebSiteSection, section);

        InfoWebSite infoWebSite = Cloner.copyIWebSite2InfoWebSite(section.getWebSite());
        infoWebSiteSection.setInfoWebSite(infoWebSite);

        return infoWebSiteSection;

    }

    public static InfoWebSiteItem copyIWebSiteItem2InfoWebSiteItem(IWebSiteItem item) {
        InfoWebSiteItem infoWebSiteItem = new InfoWebSiteItem();

        copyObjectProperties(infoWebSiteItem, item);

        InfoWebSiteSection infoWebSiteSection = Cloner.copyIWebSiteSection2InfoWebSiteSection(item
                .getWebSiteSection());
        infoWebSiteItem.setInfoWebSiteSection(infoWebSiteSection);

        InfoPerson person = Cloner.copyIPerson2InfoPerson(item.getEditor());
        infoWebSiteItem.setInfoEditor(person);

        Calendar calendar = Calendar.getInstance();
        if (item.getItemBeginDay() != null) {
            calendar.clear();
            calendar.setTimeInMillis(item.getItemBeginDay().getTime());
            infoWebSiteItem.setItemBeginDayCalendar(calendar);
        }
        if (item.getItemEndDay() != null) {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(item.getItemEndDay().getTime());
            infoWebSiteItem.setItemEndDayCalendar(calendar);
        }

        return infoWebSiteItem;
    }

    public static InfoMasterDegreeThesisDataVersion copyIMasterDegreeThesisDataVersion2InfoMasterDegreeThesisDataVersion(
            IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {

        InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = new InfoMasterDegreeThesisDataVersion();

        copyObjectProperties(infoMasterDegreeThesisDataVersion, masterDegreeThesisDataVersion);

        InfoMasterDegreeThesis infoMasterDegreeThesis = Cloner
                .copyIMasterDegreeThesis2InfoMasterDegreeThesis(masterDegreeThesisDataVersion
                        .getMasterDegreeThesis());
        InfoEmployee infoEmployee = Cloner.copyIEmployee2InfoEmployee(masterDegreeThesisDataVersion
                .getResponsibleEmployee());

        List infoExternalAssistentGuiders = Cloner
                .copyListIExternalPerson2ListInfoExternalPerson(masterDegreeThesisDataVersion
                        .getExternalAssistentGuiders());
        List infoAssistentGuiders = Cloner
                .copyListITeacher2ListInfoTeacher(masterDegreeThesisDataVersion.getAssistentGuiders());
        List infoGuiders = Cloner.copyListITeacher2ListInfoTeacher(masterDegreeThesisDataVersion
                .getGuiders());
        List infoExternalPersonGuiders = Cloner
                .copyListIExternalPerson2ListInfoExternalPerson(masterDegreeThesisDataVersion
                        .getExternalGuiders());

        infoMasterDegreeThesisDataVersion.setInfoMasterDegreeThesis(infoMasterDegreeThesis);
        infoMasterDegreeThesisDataVersion.setInfoResponsibleEmployee(infoEmployee);
        infoMasterDegreeThesisDataVersion.setInfoExternalAssistentGuiders(infoExternalAssistentGuiders);
        infoMasterDegreeThesisDataVersion.setInfoAssistentGuiders(infoAssistentGuiders);
        infoMasterDegreeThesisDataVersion.setInfoGuiders(infoGuiders);
        infoMasterDegreeThesisDataVersion.setInfoExternalGuiders(infoExternalPersonGuiders);

        return infoMasterDegreeThesisDataVersion;

    }

    public static List copyListIMasterDegreeThesisDataVersion2ListInfoMasterDegreeThesisDataVersion(
            List listIMasterDegreeThesisDataVersion) {
        List listInfoMasterDegreeThesisDataVersion = new ArrayList();

        Iterator iterListIMasterDegreeThesisDataVersion = listIMasterDegreeThesisDataVersion.iterator();

        while (iterListIMasterDegreeThesisDataVersion.hasNext()) {
            IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion = (IMasterDegreeThesisDataVersion) iterListIMasterDegreeThesisDataVersion
                    .next();
            InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = Cloner
                    .copyIMasterDegreeThesisDataVersion2InfoMasterDegreeThesisDataVersion(masterDegreeThesisDataVersion);
            listInfoMasterDegreeThesisDataVersion.add(infoMasterDegreeThesisDataVersion);
        }

        return listInfoMasterDegreeThesisDataVersion;
    }

    public static InfoMasterDegreeProofVersion copyIMasterDegreeProofVersion2InfoMasterDegreeProofVersion(
            IMasterDegreeProofVersion masterDegreeProofVersion) {
        InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = new InfoMasterDegreeProofVersion();

        copyObjectProperties(infoMasterDegreeProofVersion, masterDegreeProofVersion);

        InfoMasterDegreeThesis infoMasterDegreeThesis = Cloner
                .copyIMasterDegreeThesis2InfoMasterDegreeThesis(masterDegreeProofVersion
                        .getMasterDegreeThesis());
        InfoEmployee infoEmployee = Cloner.copyIEmployee2InfoEmployee(masterDegreeProofVersion
                .getResponsibleEmployee());
        List infoJuries = Cloner.copyListITeacher2ListInfoTeacher(masterDegreeProofVersion.getJuries());
        List infoExternalPersonJuries = Cloner
                .copyListIExternalPerson2ListInfoExternalPerson(masterDegreeProofVersion
                        .getExternalJuries());

        infoMasterDegreeProofVersion.setInfoMasterDegreeThesis(infoMasterDegreeThesis);
        infoMasterDegreeProofVersion.setInfoResponsibleEmployee(infoEmployee);
        infoMasterDegreeProofVersion.setInfoJuries(infoJuries);
        infoMasterDegreeProofVersion.setInfoExternalJuries(infoExternalPersonJuries);

        return infoMasterDegreeProofVersion;

    }

    public static List copyListIMasterDegreeProofVersion2ListInfoMasterDegreeProofVersion(
            List listIMasterDegreeProofVersion) {
        List listInfoMasterDegreeProofVersion = new ArrayList();

        Iterator iterListIMasterDegreeProofVersion = listIMasterDegreeProofVersion.iterator();

        while (iterListIMasterDegreeProofVersion.hasNext()) {
            IMasterDegreeProofVersion masterDegreeProofVersion = (IMasterDegreeProofVersion) iterListIMasterDegreeProofVersion
                    .next();
            InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = Cloner
                    .copyIMasterDegreeProofVersion2InfoMasterDegreeProofVersion(masterDegreeProofVersion);
            listInfoMasterDegreeProofVersion.add(infoMasterDegreeProofVersion);
        }

        return listInfoMasterDegreeProofVersion;
    }

    public static InfoMasterDegreeThesis copyIMasterDegreeThesis2InfoMasterDegreeThesis(
            IMasterDegreeThesis masterDegreeThesis) {
        InfoMasterDegreeThesis infoMasterDegreeThesis = new InfoMasterDegreeThesis();
        InfoStudentCurricularPlan infoStudentCurricularPlan = Cloner
                .copyIStudentCurricularPlan2InfoStudentCurricularPlan(masterDegreeThesis
                        .getStudentCurricularPlan());
        copyObjectProperties(infoMasterDegreeThesis, masterDegreeThesis);
        infoMasterDegreeThesis.setInfoStudentCurricularPlan(infoStudentCurricularPlan);

        return infoMasterDegreeThesis;
    }

    public static InfoEmployee copyIEmployee2InfoEmployee(IEmployee employee) {
        InfoEmployee infoEmployee = new InfoEmployee();
        InfoPerson infoPerson = Cloner.copyIPerson2InfoPerson(employee.getPerson());
        infoEmployee.setIdInternal(employee.getIdInternal());
        infoEmployee.setPerson(infoPerson);

        return infoEmployee;
    }

    public static IExternalPerson copyInfoExternalPerson2IExternalPerson(
            InfoExternalPerson infoExternalPerson) {
        IExternalPerson externalPerson = new ExternalPerson();
        copyObjectProperties(externalPerson, infoExternalPerson);
        IPerson person = Cloner.copyInfoPerson2IPerson(infoExternalPerson.getInfoPerson());
        externalPerson.setPerson(person);
        IWorkLocation workLocation = Cloner.copyInfoWorkLocation2IWorkLocation(infoExternalPerson
                .getInfoWorkLocation());
        externalPerson.setWorkLocation(workLocation);

        return externalPerson;
    }

    public static InfoExternalPerson copyIExternalPerson2InfoExternalPerson(
            IExternalPerson externalPerson) {
        InfoExternalPerson infoExternalPerson = new InfoExternalPerson();
        copyObjectProperties(infoExternalPerson, externalPerson);
        InfoPerson infoPerson = Cloner.copyIPerson2InfoPerson(externalPerson.getPerson());
        infoExternalPerson.setInfoPerson(infoPerson);
        InfoWorkLocation infoWorkLocation = Cloner.copyIWorkLocation2InfoWorkLocation(externalPerson
                .getWorkLocation());
        infoExternalPerson.setInfoWorkLocation(infoWorkLocation);

        return infoExternalPerson;
    }

    public static List copyListIExternalPerson2ListInfoExternalPerson(List listIExternalPerson) {
        List listInfoExternalPersons = new ArrayList();

        Iterator iterListIExternalPerson = listIExternalPerson.iterator();

        while (iterListIExternalPerson.hasNext()) {
            IExternalPerson externalPerson = (IExternalPerson) iterListIExternalPerson.next();
            InfoExternalPerson infoExternalPerson = Cloner
                    .copyIExternalPerson2InfoExternalPerson(externalPerson);
            listInfoExternalPersons.add(infoExternalPerson);
        }

        return listInfoExternalPersons;
    }

    public static List copyListInfoExternalPerson2ListIExternalPerson(List listInfoExternalPerson) {
        List listExternalPersons = new ArrayList();

        Iterator iterListInfoExternalPerson = listInfoExternalPerson.iterator();

        while (iterListInfoExternalPerson.hasNext()) {
            InfoExternalPerson infoExternalPerson = (InfoExternalPerson) iterListInfoExternalPerson
                    .next();
            IExternalPerson externalPerson = Cloner
                    .copyInfoExternalPerson2IExternalPerson(infoExternalPerson);
            listExternalPersons.add(externalPerson);
        }

        return listExternalPersons;
    }

    public static List copyListITeacher2ListInfoTeacher(List listITeacher) {
        List listInfoTeacher = new ArrayList();

        Iterator iterListITeachers = listITeacher.iterator();

        while (iterListITeachers.hasNext()) {
            ITeacher teacher = (ITeacher) iterListITeachers.next();
            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            listInfoTeacher.add(infoTeacher);
        }

        return listInfoTeacher;
    }

    public static List copyListInfoTeacher2ListITeacher(List listInfoTeacher) {
        List listITeacher = new ArrayList();

        Iterator iterListInfoTeacher = listInfoTeacher.iterator();

        while (iterListInfoTeacher.hasNext()) {
            InfoTeacher infoTeacher = (InfoTeacher) iterListInfoTeacher.next();
            ITeacher teacher = Cloner.copyInfoTeacher2Teacher(infoTeacher);
            listITeacher.add(teacher);
        }

        return listITeacher;
    }

    public static InfoCoordinator copyICoordinator2InfoCoordenator(ICoordinator coordinator) {
        InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(coordinator.getTeacher());
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner.get(coordinator
                .getExecutionDegree());
        InfoCoordinator infoCoordinator = new InfoCoordinator();
        copyObjectProperties(infoCoordinator, coordinator);
        infoCoordinator.setInfoExecutionDegree(infoExecutionDegree);
        infoCoordinator.setInfoTeacher(infoTeacher);
        return infoCoordinator;
    }

    /**
     * @author T�nia Pous�o Created on 13/Nov/2003
     */
    public static InfoCampus copyICampus2InfoCampus(ICampus campus) {
        InfoCampus infoCampus = new InfoCampus();
        copyObjectProperties(infoCampus, campus);

        return infoCampus;
    }

    /**
     * @author T�nia Pous�o Created on 13/Nov/2003
     */
    public static ICampus copyInfoCampus2ICampus(InfoCampus infoCampus) {
        ICampus campus = new Campus();
        copyObjectProperties(campus, infoCampus);

        return campus;
    }

    public static InfoCareer copyICareer2InfoCareer(ICareer career) {
        InfoCareer infoCareer = null;

        if (career instanceof IProfessionalCareer) {
            IProfessionalCareer professionalCareer = (IProfessionalCareer) career;
            infoCareer = copyIProfessionalCareer2InfoProfessionalCareer(professionalCareer);
        } else {
            ITeachingCareer teachingCareer = (ITeachingCareer) career;
            infoCareer = copyITeachingCareer2InfoTeachingCareer(teachingCareer);
        }

        return infoCareer;
    }

    public static ICareer copyInfoCareer2ICareer(InfoCareer infoCareer) {
        ICareer career = null;

        if (infoCareer instanceof InfoProfessionalCareer) {
            InfoProfessionalCareer infoProfessionalCareer = (InfoProfessionalCareer) infoCareer;
            career = copyInfoProfessionalCareer2IProfessionalCareer(infoProfessionalCareer);
        } else {
            InfoTeachingCareer infoTeachingCareer = (InfoTeachingCareer) infoCareer;
            career = copyInfoTeachingCareer2ITeachingCareer(infoTeachingCareer);
        }

        return career;
    }

    private static InfoProfessionalCareer copyIProfessionalCareer2InfoProfessionalCareer(
            IProfessionalCareer professionalCareer) {
        InfoProfessionalCareer infoProfessionalCareer = new InfoProfessionalCareer();
        InfoTeacher infoTeacher = copyITeacher2InfoTeacher(professionalCareer.getTeacher());
        copyObjectProperties(infoProfessionalCareer, professionalCareer);

        infoProfessionalCareer.setInfoTeacher(infoTeacher);

        return infoProfessionalCareer;
    }

    private static IProfessionalCareer copyInfoProfessionalCareer2IProfessionalCareer(
            InfoProfessionalCareer infoProfessionalCareer) {
        IProfessionalCareer professionalCareer = new ProfessionalCareer();
        ITeacher teacher = copyInfoTeacher2Teacher(infoProfessionalCareer.getInfoTeacher());
        copyObjectProperties(professionalCareer, infoProfessionalCareer);

        professionalCareer.setTeacher(teacher);
        return professionalCareer;
    }

    private static InfoTeachingCareer copyITeachingCareer2InfoTeachingCareer(
            ITeachingCareer teachingCareer) {
        InfoTeachingCareer infoTeachingCareer = new InfoTeachingCareer();
        InfoTeacher infoTeacher = copyITeacher2InfoTeacher(teachingCareer.getTeacher());
        InfoCategory infoCategory = copyICategory2InfoCategory(teachingCareer.getCategory());
        copyObjectProperties(infoTeachingCareer, teachingCareer);

        infoTeachingCareer.setInfoTeacher(infoTeacher);
        infoTeachingCareer.setInfoCategory(infoCategory);

        return infoTeachingCareer;
    }

    private static ITeachingCareer copyInfoTeachingCareer2ITeachingCareer(
            InfoTeachingCareer infoTeachingCareer) {
        ITeachingCareer teachingCareer = new TeachingCareer();
        ITeacher teacher = copyInfoTeacher2Teacher(infoTeachingCareer.getInfoTeacher());
        ICategory category = copyInfoCategory2ICategory(infoTeachingCareer.getInfoCategory());
        copyObjectProperties(teachingCareer, infoTeachingCareer);

        teachingCareer.setTeacher(teacher);
        teachingCareer.setCategory(category);

        return teachingCareer;
    }

    /**
     * @param infoWeeklyOcupation
     * @return
     */
    public static IWeeklyOcupation copyInfoWeeklyOcupation2IWeeklyOcupation(
            InfoWeeklyOcupation infoWeeklyOcupation) {
        IWeeklyOcupation weeklyOcupation = new WeeklyOcupation();
        ITeacher teacher = copyInfoTeacher2Teacher(infoWeeklyOcupation.getInfoTeacher());
        copyObjectProperties(weeklyOcupation, infoWeeklyOcupation);

        weeklyOcupation.setTeacher(teacher);

        return weeklyOcupation;
    }

    /**
     * @param externalActivity
     * @return
     */
    public static InfoExternalActivity copyIExternalActivity2InfoExternalActivity(
            IExternalActivity externalActivity) {
        InfoExternalActivity infoExternalActivity = new InfoExternalActivity();
        InfoTeacher infoTeacher = copyITeacher2InfoTeacher(externalActivity.getTeacher());
        copyObjectProperties(infoExternalActivity, externalActivity);

        infoExternalActivity.setInfoTeacher(infoTeacher);

        return infoExternalActivity;
    }

    /**
     * @param infoExternalActivity
     * @return
     */
    public static IExternalActivity copyInfoExternalActivity2IExternalActivity(
            InfoExternalActivity infoExternalActivity) {
        IExternalActivity externalActivity = new ExternalActivity();
        ITeacher teacher = copyInfoTeacher2Teacher(infoExternalActivity.getInfoTeacher());
        copyObjectProperties(externalActivity, infoExternalActivity);

        externalActivity.setTeacher(teacher);

        return externalActivity;
    }

    /**
     * @param infoServiceProviderRegime
     * @return
     */
    public static IServiceProviderRegime copyInfoServiceProviderRegime2IServiceProviderRegime(
            InfoServiceProviderRegime infoServiceProviderRegime) {
        IServiceProviderRegime serviceProviderRegime = new ServiceProviderRegime();
        ITeacher teacher = copyInfoTeacher2Teacher(infoServiceProviderRegime.getInfoTeacher());
        copyObjectProperties(serviceProviderRegime, infoServiceProviderRegime);

        serviceProviderRegime.setTeacher(teacher);

        return serviceProviderRegime;
    }

    public static InfoReimbursementGuide copyIReimbursementGuide2InfoReimbursementGuide(
            IReimbursementGuide reimbursementGuide) {
        InfoReimbursementGuide infoReimbursementGuide = new InfoReimbursementGuide();
        InfoGuide infoGuide = copyIGuide2InfoGuide(reimbursementGuide.getGuide());

        List infoReimbursementGuideSituations = new ArrayList();
        Iterator it = reimbursementGuide.getReimbursementGuideSituations().iterator();

        while (it.hasNext()) {

            infoReimbursementGuideSituations
                    .add(copyIReimbursementGuideSituation2InfoReimbursementGuideSituation((IReimbursementGuideSituation) it
                            .next()));
        }

        infoReimbursementGuide.setInfoReimbursementGuideSituations(infoReimbursementGuideSituations);
        copyObjectProperties(infoReimbursementGuide, reimbursementGuide);
        infoReimbursementGuide.setInfoGuide(infoGuide);

        return infoReimbursementGuide;
    }

    public static InfoReimbursementGuideSituation copyIReimbursementGuideSituation2InfoReimbursementGuideSituation(
            IReimbursementGuideSituation reimbursementGuideSituation) {
        InfoReimbursementGuideSituation infoReimbursementGuideSituation = new InfoReimbursementGuideSituation();
        InfoEmployee infoEmployee = copyIEmployee2InfoEmployee(reimbursementGuideSituation.getEmployee());
        copyObjectProperties(infoReimbursementGuideSituation, reimbursementGuideSituation);
        infoReimbursementGuideSituation.setInfoEmployee(infoEmployee);

        return infoReimbursementGuideSituation;
    }

    public static IOrientation copyInfoOrientation2IOrientation(InfoOrientation infoOrientation) {
        IOrientation orientation = new Orientation();
        ITeacher teacher = Cloner.copyInfoTeacher2Teacher(infoOrientation.getInfoTeacher());
        copyObjectProperties(orientation, infoOrientation);

        orientation.setTeacher(teacher);
        return orientation;
    }

    public static IPublicationsNumber copyInfoPublicationsNumber2IPublicationsNumber(
            InfoPublicationsNumber infoPublicationsNumber) {
        IPublicationsNumber publicationsNumber = new PublicationsNumber();
        ITeacher teacher = Cloner.copyInfoTeacher2Teacher(infoPublicationsNumber.getInfoTeacher());
        copyObjectProperties(publicationsNumber, infoPublicationsNumber);

        publicationsNumber.setTeacher(teacher);
        return publicationsNumber;
    }

    public static InfoOldPublication copyIOldPublication2InfoOldPublication(
            IOldPublication oldPublication) {
        InfoOldPublication infoOldPublication = new InfoOldPublication();
        InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(oldPublication.getTeacher());
        copyObjectProperties(infoOldPublication, oldPublication);

        infoOldPublication.setInfoTeacher(infoTeacher);
        return infoOldPublication;
    }

    public static IOldPublication copyInfoOldPublication2IOldPublication(
            InfoOldPublication infoOldPublication) {
        IOldPublication oldPublication = new OldPublication();
        ITeacher teacher = Cloner.copyInfoTeacher2Teacher(infoOldPublication.getInfoTeacher());
        copyObjectProperties(oldPublication, infoOldPublication);

        oldPublication.setTeacher(teacher);
        return oldPublication;
    }

    public static InfoGaugingTestResult copyIGaugingTestResult2IngoGaugingTestResult(
            IGaugingTestResult gaugingTestResult) {
        InfoStudent infoStudent = copyIStudent2InfoStudent(gaugingTestResult.getStudent());
        InfoGaugingTestResult infoGaugingTestResult = new InfoGaugingTestResult();
        copyObjectProperties(infoGaugingTestResult, gaugingTestResult);
        infoGaugingTestResult.setInfoStudent(infoStudent);
        return infoGaugingTestResult;
    }

    /**
     * @param supportLesson
     * @return
     */
    public static InfoSupportLesson copyISupportLesson2InfoSupportLesson(ISupportLesson supportLesson) {
        InfoSupportLesson infoSupportLesson = new InfoSupportLesson();
        InfoProfessorship infoProfessorship = Cloner.copyIProfessorship2InfoProfessorship(supportLesson
                .getProfessorship());

        copyObjectProperties(infoSupportLesson, supportLesson);

        infoSupportLesson.setInfoProfessorship(infoProfessorship);

        return infoSupportLesson;
    }

    /**
     * @param supportLesson
     * @return
     */
    public static ISupportLesson copyInfoSupportLesson2ISupportLesson(InfoSupportLesson infoSupportLesson) {
        ISupportLesson supportLesson = new SupportLesson();
        IProfessorship professorship = Cloner.copyInfoProfessorship2IProfessorship(infoSupportLesson
                .getInfoProfessorship());

        copyObjectProperties(supportLesson, infoSupportLesson);

        supportLesson.setProfessorship(professorship);

        return supportLesson;
    }

    /**
     * @param teacherDegreeFinalProjectStudent
     * @return
     */
    public static InfoTeacherDegreeFinalProjectStudent copyITeacherDegreeFinalProjectStudent2InfoTeacherDegreeFinalProjectStudent(
            ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent) {
        InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacherDegreeFinalProjectStudent
                .getTeacher());
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner
                .get(teacherDegreeFinalProjectStudent.getExecutionPeriod());
        InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(teacherDegreeFinalProjectStudent
                .getStudent());

        InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = new InfoTeacherDegreeFinalProjectStudent();

        copyObjectProperties(infoTeacherDegreeFinalProjectStudent, teacherDegreeFinalProjectStudent);

        infoTeacherDegreeFinalProjectStudent.setInfoExecutionPeriod(infoExecutionPeriod);
        infoTeacherDegreeFinalProjectStudent.setInfoStudent(infoStudent);
        infoTeacherDegreeFinalProjectStudent.setInfoTeacher(infoTeacher);

        return infoTeacherDegreeFinalProjectStudent;
    }

    /**
     * @param teacherDegreeFinalProjectStudent
     * @return
     */
    public static ITeacherDegreeFinalProjectStudent copyInfoTeacherDegreeFinalProjectStudent2ITeacherDegreeFinalProjectStudent(
            InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent) {
        ITeacher teacher = Cloner.copyInfoTeacher2Teacher(infoTeacherDegreeFinalProjectStudent
                .getInfoTeacher());
        IExecutionPeriod executionPeriod = Cloner
                .copyInfoExecutionPeriod2IExecutionPeriod(infoTeacherDegreeFinalProjectStudent
                        .getInfoExecutionPeriod());
        IStudent student = Cloner.copyInfoStudent2IStudent(infoTeacherDegreeFinalProjectStudent
                .getInfoStudent());

        TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = new TeacherDegreeFinalProjectStudent();

        copyObjectProperties(teacherDegreeFinalProjectStudent, infoTeacherDegreeFinalProjectStudent);

        teacherDegreeFinalProjectStudent.setExecutionPeriod(executionPeriod);
        teacherDegreeFinalProjectStudent.setStudent(student);
        teacherDegreeFinalProjectStudent.setTeacher(teacher);

        return teacherDegreeFinalProjectStudent;
    }

    public static ITeacherInstitutionWorkTime copyInfoTeacherInstitutionWorkingTime2ITeacherInstitutionWorkTime(
            InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime) {
        ITeacher teacher = Cloner.copyInfoTeacher2Teacher(infoTeacherInstitutionWorkTime
                .getInfoTeacher());
        IExecutionPeriod executionPeriod = Cloner
                .copyInfoExecutionPeriod2IExecutionPeriod(infoTeacherInstitutionWorkTime
                        .getInfoExecutionPeriod());

        ITeacherInstitutionWorkTime teacherInstitutionWorkTime = new TeacherInstitutionWorkTime();

        copyObjectProperties(teacherInstitutionWorkTime, infoTeacherInstitutionWorkTime);

        teacherInstitutionWorkTime.setTeacher(teacher);
        teacherInstitutionWorkTime.setExecutionPeriod(executionPeriod);

        return teacherInstitutionWorkTime;
    }

    public static InfoTeacherInstitutionWorkTime copyITeacherInstitutionWorkingTime2InfoTeacherInstitutionWorkTime(
            ITeacherInstitutionWorkTime teacherInstitutionWorkTime) {
        InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacherInstitutionWorkTime
                .getTeacher());
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner
                .get(teacherInstitutionWorkTime.getExecutionPeriod());

        InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime = new InfoTeacherInstitutionWorkTime();

        copyObjectProperties(infoTeacherInstitutionWorkTime, teacherInstitutionWorkTime);

        infoTeacherInstitutionWorkTime.setInfoTeacher(infoTeacher);
        infoTeacherInstitutionWorkTime.setInfoExecutionPeriod(infoExecutionPeriod);

        return infoTeacherInstitutionWorkTime;
    }

    public static IWorkLocation copyInfoWorkLocation2IWorkLocation(InfoWorkLocation infoWorkLocation) {
        IWorkLocation workLocation = new WorkLocation();
        copyObjectProperties(workLocation, infoWorkLocation);

        return workLocation;
    }

    public static InfoWorkLocation copyIWorkLocation2InfoWorkLocation(IWorkLocation workLocation) {
        InfoWorkLocation infoWorkLocation = new InfoWorkLocation();
        copyObjectProperties(infoWorkLocation, workLocation);

        return infoWorkLocation;
    }

    public static InfoResponsibleFor copyIResponsibleFor2InfoResponsibleFor(
            IResponsibleFor responsibleFor) {
        IExecutionCourse executionCourse = responsibleFor.getExecutionCourse();
        ITeacher teacher = responsibleFor.getTeacher();

        InfoResponsibleFor infoResponsibleFor = new InfoResponsibleFor();
        copyObjectProperties(infoResponsibleFor, responsibleFor);

        InfoExecutionCourse infoExecutionCourse = null;
        if (executionCourse != null) {
            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
        } else {
            copyObjectProperties(infoExecutionCourse, executionCourse);
        }
        infoResponsibleFor.setInfoExecutionCourse(infoExecutionCourse);

        InfoTeacher infoTeacher = null;
        if (teacher != null) {
            infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
        } else {
            copyObjectProperties(infoTeacher, teacher);
        }
        infoResponsibleFor.setInfoTeacher(infoTeacher);

        return infoResponsibleFor;
    }

    public static List copyListIWorkLocation2ListInfoWorkLocation(List workLocations) {
        List listInfoWorkLocations = new ArrayList();
        Iterator iter = workLocations.iterator();

        while (iter.hasNext()) {
            IWorkLocation workLocation = (IWorkLocation) iter.next();
            InfoWorkLocation infoWorkLocation = Cloner.copyIWorkLocation2InfoWorkLocation(workLocation);
            listInfoWorkLocations.add(infoWorkLocation);
        }

        return listInfoWorkLocations;
    }

    public static IScientificArea copyInfoScientificArea2IScientificArea(
            InfoScientificArea infoScientificArea) {
        IScientificArea scientificArea = new ScientificArea();
        copyObjectProperties(scientificArea, infoScientificArea);

        return scientificArea;
    }

    public static InfoScientificArea copyIScientificArea2InfoScientificArea(
            IScientificArea scientificArea) {
        InfoScientificArea infoScientificArea = new InfoScientificArea();
        copyObjectProperties(infoScientificArea, scientificArea);

        return infoScientificArea;
    }

    public static InfoGratuityValues copyIGratuityValues2InfoGratuityValues(
            IGratuityValues gratuityValues) {
        InfoGratuityValues infoGratuityValues = new InfoGratuityValues();
        copyObjectProperties(infoGratuityValues, gratuityValues);

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner.get(gratuityValues
                .getExecutionDegree());
        InfoEmployee infoEmployee = Cloner.copyIEmployee2InfoEmployee(gratuityValues.getEmployee());

        infoGratuityValues.setInfoExecutionDegree(infoExecutionDegree);
        infoGratuityValues.setInfoEmployee(infoEmployee);

        return infoGratuityValues;
    }

    public static InfoPaymentPhase copyIPaymentPhase2InfoPaymentPhase(IPaymentPhase paymentPhase) {
        InfoPaymentPhase infoPaymentPhase = new InfoPaymentPhase();
        copyObjectProperties(infoPaymentPhase, paymentPhase);

        InfoGratuityValues infoGratuityValues = Cloner
                .copyIGratuityValues2InfoGratuityValues(paymentPhase.getGratuityValues());
        infoPaymentPhase.setInfoGratuityValues(infoGratuityValues);
        return infoPaymentPhase;

    }

    public static InfoGratuitySituation copyIGratuitySituation2InfoGratuitySituation(
            IGratuitySituation gratuitySituation) {
        InfoGratuitySituation infoGratuitySituation = new InfoGratuitySituation();
        copyObjectProperties(infoGratuitySituation, gratuitySituation);

        if (gratuitySituation.getGratuityValues() != null) {
            InfoGratuityValues infoGratuityValues = Cloner
                    .copyIGratuityValues2InfoGratuityValues(gratuitySituation.getGratuityValues());
            infoGratuitySituation.setInfoGratuityValues(infoGratuityValues);
        }

        InfoStudentCurricularPlan infoStudentCurricularPlan = Cloner
                .copyIStudentCurricularPlan2InfoStudentCurricularPlan(gratuitySituation
                        .getStudentCurricularPlan());
        infoGratuitySituation.setInfoStudentCurricularPlan(infoStudentCurricularPlan);

        return infoGratuitySituation;
    }

    public static InfoCourseHistoric copyICourseHistoric2InfoCourseHistoric(
            ICourseHistoric courseHistoric) {
        InfoCourseHistoric infoCourseHistoric = new InfoCourseHistoric();
        copyObjectProperties(infoCourseHistoric, courseHistoric);
        InfoCurricularCourse infoCurricularCourse = Cloner
                .copyCurricularCourse2InfoCurricularCourse(courseHistoric.getCurricularCourse());
        infoCourseHistoric.setInfoCurricularCourse(infoCurricularCourse);

        return infoCourseHistoric;
    }

    public static InfoDelegate copyIDelegate2InfoDelegate(IDelegate delegate) {
        InfoDelegate infoDelegate = new InfoDelegate();
        copyObjectProperties(infoDelegate, delegate);
        InfoDegree infoDegree = Cloner.copyIDegree2InfoDegree(delegate.getDegree());
        InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(delegate.getStudent());
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) Cloner
                .get(delegate.getExecutionYear());
        infoDelegate.setInfoDegree(infoDegree);
        infoDelegate.setInfoStudent(infoStudent);
        infoDelegate.setInfoExecutionYear(infoExecutionYear);

        return infoDelegate;
    }

    public static InfoStudentCourseReport copyIStudentCourseReport2InfoStudentCourseReport(
            IStudentCourseReport studentCourseReport) {
        InfoStudentCourseReport infoStudentCourseReport = new InfoStudentCourseReport();
        copyObjectProperties(infoStudentCourseReport, studentCourseReport);
        InfoCurricularCourse infoCurricularCourse = Cloner
                .copyCurricularCourse2InfoCurricularCourse(studentCourseReport.getCurricularCourse());
        infoStudentCourseReport.setInfoCurricularCourse(infoCurricularCourse);

        return infoStudentCourseReport;
    }

    public static IStudentCourseReport copyInfoStudentCourseReport2IStudentCourseReport(
            InfoStudentCourseReport infoStudentCourseReport) {
        IStudentCourseReport studentCourseReport = new StudentCourseReport();
        copyObjectProperties(studentCourseReport, infoStudentCourseReport);
        ICurricularCourse curricularCourse = Cloner
                .copyInfoCurricularCourse2CurricularCourse(infoStudentCourseReport
                        .getInfoCurricularCourse());
        studentCourseReport.setCurricularCourse(curricularCourse);

        return studentCourseReport;
    }

    public static InfoOtherTypeCreditLine copyIOtherTypeCreditLine2InfoOtherCreditLine(
            IOtherTypeCreditLine creditLine) {
        InfoTeacher infoTeacher = copyITeacher2InfoTeacher(creditLine.getTeacher());
        InfoExecutionPeriod infoExecutionPeriod = copy(creditLine.getExecutionPeriod());

        InfoOtherTypeCreditLine infoOtherTypeCreditLine = new InfoOtherTypeCreditLine();

        copyObjectProperties(infoOtherTypeCreditLine, creditLine);

        infoOtherTypeCreditLine.setInfoTeacher(infoTeacher);
        infoOtherTypeCreditLine.setInfoExecutionPeriod(infoExecutionPeriod);
        return infoOtherTypeCreditLine;
    }

    public static IOtherTypeCreditLine copyInfoOtherTypeCreditLine2IOtherCreditLine(
            InfoOtherTypeCreditLine creditLine) {
        ITeacher teacher = null;
        if (creditLine.getInfoTeacher() != null) {
            teacher = copyInfoTeacher2Teacher(creditLine.getInfoTeacher());
        }
        IExecutionPeriod executionPeriod = null;
        if (creditLine.getInfoExecutionPeriod() != null) {
            executionPeriod = copyInfoExecutionPeriod2IExecutionPeriod(creditLine
                    .getInfoExecutionPeriod());
        }

        IOtherTypeCreditLine otherTypeCreditLine = new OtherTypeCreditLine();

        copyObjectProperties(otherTypeCreditLine, creditLine);

        otherTypeCreditLine.setTeacher(teacher);
        otherTypeCreditLine.setExecutionPeriod(executionPeriod);
        return otherTypeCreditLine;
    }

    public static IServiceExemptionCreditLine copyInfoServiceExemptionCreditLine2IServiceExemptionCreditLine(
            InfoServiceExemptionCreditLine creditLine) {
        ITeacher teacher = null;
        if (creditLine.getInfoTeacher() != null) {
            teacher = copyInfoTeacher2Teacher(creditLine.getInfoTeacher());
        }

        IServiceExemptionCreditLine serviceExemptionCreditLine = new ServiceExemptionCreditLine();

        copyObjectProperties(serviceExemptionCreditLine, creditLine);

        serviceExemptionCreditLine.setTeacher(teacher);
        return serviceExemptionCreditLine;
    }

    public static IManagementPositionCreditLine copyInfoManagementPositionCreditLine2IManagementPositionCreditLine(
            InfoManagementPositionCreditLine creditLine) {
        ITeacher teacher = null;
        if (creditLine.getInfoTeacher() != null) {
            teacher = copyInfoTeacher2Teacher(creditLine.getInfoTeacher());
        }

        IManagementPositionCreditLine managementPositionCreditLine = new ManagementPositionCreditLine();

        copyObjectProperties(managementPositionCreditLine, creditLine);

        managementPositionCreditLine.setTeacher(teacher);
        return managementPositionCreditLine;
    }

    public static InfoManagementPositionCreditLine copyIManagementPositionCreditLine2InfoManagementPositionCreditLine(
            IManagementPositionCreditLine creditLine) {
        InfoTeacher infoTeacher = copyITeacher2InfoTeacher(creditLine.getTeacher());

        InfoManagementPositionCreditLine infoCreditLine = new InfoManagementPositionCreditLine();

        copyObjectProperties(infoCreditLine, creditLine);

        infoCreditLine.setInfoTeacher(infoTeacher);
        return infoCreditLine;
    }

    public static InfoServiceExemptionCreditLine copyIServiceExemptionCreditLine2InfoServiceExemptionCreditLine(
            IServiceExemptionCreditLine creditLine) {
        InfoTeacher infoTeacher = copyITeacher2InfoTeacher(creditLine.getTeacher());

        InfoServiceExemptionCreditLine infoCreditLine = new InfoServiceExemptionCreditLine();

        copyObjectProperties(infoCreditLine, creditLine);

        infoCreditLine.setInfoTeacher(infoTeacher);
        return infoCreditLine;
    }

    public static IReimbursementGuideEntry copyInfoReimbursementGuideEntry2IReimbursementGuideEntry(
            InfoReimbursementGuideEntry infoReimbursementGuideEntry) {

        IReimbursementGuideEntry reimbursementGuideEntry = new ReimbursementGuideEntry();

        IGuideEntry guideEntry = null;

        if (infoReimbursementGuideEntry.getInfoGuideEntry() != null) {
            guideEntry = copyInfoGuideEntry2IGuideEntry(infoReimbursementGuideEntry.getInfoGuideEntry());
            reimbursementGuideEntry.setGuideEntry(guideEntry);
        }
        copyObjectProperties(reimbursementGuideEntry, infoReimbursementGuideEntry);

        return reimbursementGuideEntry;

    }

    public static List copyListInfoReimbursementGuideEntries2ListIReimbursementGuideEntries(
            List listInfoReimbursementGuideEntries) {
        List listReimbursementGuideEntries = new ArrayList(listInfoReimbursementGuideEntries.size());

        Iterator iterListInfoReimbursementGuideEntries = listInfoReimbursementGuideEntries.iterator();

        while (iterListInfoReimbursementGuideEntries.hasNext()) {
            InfoReimbursementGuideEntry infoReimbursementGuideEntry = (InfoReimbursementGuideEntry) iterListInfoReimbursementGuideEntries
                    .next();
            IReimbursementGuideEntry reimbursementGuideEntry = Cloner
                    .copyInfoReimbursementGuideEntry2IReimbursementGuideEntry(infoReimbursementGuideEntry);
            listReimbursementGuideEntries.add(reimbursementGuideEntry);
        }

        return listReimbursementGuideEntries;
    }

    public static InfoReimbursementGuideEntry copyIReimbursementGuideEntry2InfoReimbursementGuideEntry(
            IReimbursementGuideEntry reimbursementGuideEntry) {
        InfoReimbursementGuideEntry infoReimbursementGuideEntry = new InfoReimbursementGuideEntry();

        InfoGuideEntry infoGuideEntry = copyIGuideEntry2InfoGuideEntry(reimbursementGuideEntry
                .getGuideEntry());

        copyObjectProperties(infoReimbursementGuideEntry, reimbursementGuideEntry);
        infoReimbursementGuideEntry.setInfoGuideEntry(infoGuideEntry);

        return infoReimbursementGuideEntry;
    }

    public static InfoPublicationType copyIPublicationType2InfoPublicationType(
            IPublicationType publicationType) {
        InfoPublicationType infoPublicationType = new InfoPublicationType();
        copyObjectProperties(infoPublicationType, publicationType);
        return infoPublicationType;
    }

    public static IPublicationType copyInfoPublicationType2IPublicationType(
            InfoPublicationType infoPublicationType) {
        IPublicationType publicationType = new PublicationType();
        copyObjectProperties(publicationType, infoPublicationType);
        return publicationType;
    }

    public static InfoAttribute copyIAttribute2InfoAttribute(IAttribute attribute) {
        InfoAttribute infoAttribute = new InfoAttribute();
        copyObjectProperties(infoAttribute, attribute);
        return infoAttribute;
    }

    public static InfoPublicationSubtype copyIPublicationSubtype2InfoPublicationSubtype(
            IPublicationSubtype PublicationSubtype) {
        InfoPublicationSubtype infoPublicationSubtype = new InfoPublicationSubtype();
        copyObjectProperties(infoPublicationSubtype, PublicationSubtype);
        return infoPublicationSubtype;
    }

    public static InfoPublicationFormat copyIPublicationFormat2InfoPublicationFormat(
            IPublicationFormat PublicationFormat) {
        InfoPublicationFormat infoPublicationFormat = new InfoPublicationFormat();
        copyObjectProperties(infoPublicationFormat, PublicationFormat);
        return infoPublicationFormat;
    }

    public static IAuthor copyInfoAuthor2IAuthor(InfoAuthor infoAuthor) {
        IAuthor author = new Author();
        IPerson person = new Person();
        if (infoAuthor.getKeyPerson() != null) {
            person = copyInfoPerson2IPerson(infoAuthor.getInfoPessoa());
        }
        copyObjectProperties(author, infoAuthor);
        if (infoAuthor.getKeyPerson() != null) {
            author.setPerson(person);
        }
        return author;
    }

    public static InfoAuthor copyIAuthor2InfoAuthor(IAuthor author) {
        InfoAuthor infoAuthor = new InfoAuthor();
        InfoPerson infoPerson = new InfoPerson();
        if (author.getKeyPerson() != null) {
            infoPerson = copyIPerson2InfoPerson(author.getPerson());
        }
        copyObjectProperties(infoAuthor, author);
        if (author.getKeyPerson() != null) {
            infoAuthor.setInfoPessoa(infoPerson);
        }
        if (author.getOrganization() == null
                || author.getOrganization().length() == PublicationConstants.ZERO_VALUE) {
            infoAuthor.setOrganization(PublicationConstants.DEFAULT_ORGANIZATION);
        }
        return infoAuthor;
    }

    public static InfoAuthorPerson copyIAuthor2InfoAuthorperson(IAuthor author) {

        InfoAuthorPerson infoAuthorPerson = new InfoAuthorPerson();
        String keyIdString = author.getIdInternal().toString() + PublicationConstants.INIT_AUTHOR;

        infoAuthorPerson.setIdInternal(author.getIdInternal());
        infoAuthorPerson.setKeyFinal(keyIdString);
        infoAuthorPerson.setName(author.getAuthor());
        infoAuthorPerson.setOrganisation(author.getOrganization());

        return infoAuthorPerson;
    }

    public static InfoAuthorPerson copyIPerson2InfoAuthorPerson(IPerson person) {

        InfoAuthorPerson infoAuthorPerson = new InfoAuthorPerson();

        String keyIdString = person.getIdInternal().toString() + PublicationConstants.INIT_PERSON;

        infoAuthorPerson.setIdInternal(person.getIdInternal());
        infoAuthorPerson.setKeyFinal(keyIdString);
        infoAuthorPerson.setName(person.getNome());
        infoAuthorPerson.setOrganisation(PublicationConstants.DEFAULT_ORGANIZATION);
        return infoAuthorPerson;
    }
    // TJBF& PFON

}