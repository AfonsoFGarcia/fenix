package DataBeans.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoAdvisory;
import DataBeans.InfoAnnouncement;
import DataBeans.InfoBibliographicReference;
import DataBeans.InfoBranch;
import DataBeans.InfoCandidateEnrolment;
import DataBeans.InfoCandidateSituation;
import DataBeans.InfoClass;
import DataBeans.InfoContributor;
import DataBeans.InfoCountry;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularSemester;
import DataBeans.InfoCurricularYear;
import DataBeans.InfoCurriculum;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoDepartment;
import DataBeans.InfoDepartmentCourse;
import DataBeans.InfoDistributedTest;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoEnrolmentInExtraCurricularCourse;
import DataBeans.InfoEnrolmentInOptionalCurricularCourse;
import DataBeans.InfoEvaluation;
import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoExam;
import DataBeans.InfoExamStudentRoom;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoFinalEvaluation;
import DataBeans.InfoFrequenta;
import DataBeans.InfoGratuity;
import DataBeans.InfoGroupProperties;
import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.InfoGuideSituation;
import DataBeans.InfoItem;
import DataBeans.InfoLesson;
import DataBeans.InfoMark;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoMetadata;
import DataBeans.InfoPerson;
import DataBeans.InfoPrice;
import DataBeans.InfoProfessorShip;
import DataBeans.InfoQuestion;
import DataBeans.InfoRole;
import DataBeans.InfoRoom;
import DataBeans.InfoSection;
import DataBeans.InfoShift;
import DataBeans.InfoSite;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoStudentGroup;
import DataBeans.InfoStudentGroupAttend;
import DataBeans.InfoStudentKind;
import DataBeans.InfoSummary;
import DataBeans.InfoTeacher;
import DataBeans.InfoTest;
import DataBeans.InfoTestQuestion;
import DataBeans.Seminaries.InfoCandidacy;
import DataBeans.Seminaries.InfoCaseStudy;
import DataBeans.Seminaries.InfoCaseStudyChoice;
import DataBeans.Seminaries.InfoEquivalency;
import DataBeans.Seminaries.InfoModality;
import DataBeans.Seminaries.InfoSeminary;
import DataBeans.Seminaries.InfoTheme;
import DataBeans.teacher.credits.InfoCredits;
import DataBeans.teacher.credits.InfoTeacherShiftPercentage;
import Dominio.*;
import Dominio.Seminaries.Candidacy;
import Dominio.Seminaries.CaseStudyChoice;
import Dominio.Seminaries.ICandidacy;
import Dominio.Seminaries.ICaseStudy;
import Dominio.Seminaries.ICaseStudyChoice;
import Dominio.Seminaries.ICourseEquivalency;
import Dominio.Seminaries.IModality;
import Dominio.Seminaries.ISeminary;
import Dominio.Seminaries.ITheme;
import Util.EvaluationType;
import Util.State;

/**
 * @author jpvl
 *
 */
public abstract class Cloner {

	public static ITurno copyInfoShift2Shift(InfoShift infoShift) {
		ITurno shift = new Turno();
		IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoShift.getInfoDisciplinaExecucao());

		System.out.println("executionCourse= " + executionCourse);

		copyObjectProperties(shift, infoShift);

		shift.setDisciplinaExecucao(executionCourse);
		shift.setIdInternal(infoShift.getIdInternal());
		return shift;
	}
	/**
	 * Method copyInfoExecutionCourse2ExecutionCourse.
	 * @param infoExecutionCourse
	 * @return IDisciplinaExecucao
	 */
	public static IDisciplinaExecucao copyInfoExecutionCourse2ExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
		IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
		IExecutionPeriod executionPeriod =
			Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionCourse.getInfoExecutionPeriod());

		copyObjectProperties(executionCourse, infoExecutionCourse);

		executionCourse.setExecutionPeriod(executionPeriod);
		return executionCourse;
	}

	public static InfoExecutionCourse copyIExecutionCourse2InfoExecutionCourse(IDisciplinaExecucao executionCourse) {
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		InfoExecutionPeriod infoExecutionPeriod =
			Cloner.copyIExecutionPeriod2InfoExecutionPeriod(executionCourse.getExecutionPeriod());

		copyObjectProperties(infoExecutionCourse, executionCourse);

		infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
		return infoExecutionCourse;
	}

	/**
	 * Method copyInfoLesson2Lesson.
	 * @param lessonExample
	 * @return IAula
	 */
	public static IAula copyInfoLesson2Lesson(InfoLesson infoLesson) {
		IAula lesson = new Aula();
		ISala sala = null;

		try {
			BeanUtils.copyProperties(lesson, infoLesson);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}

		sala = Cloner.copyInfoRoom2Room(infoLesson.getInfoSala());
		lesson.setSala(sala);

		return lesson;
	}
	/**
	 * Method copyInfoRoom2Room.
	 * @param infoRoom
	 */
	public static ISala copyInfoRoom2Room(InfoRoom infoRoom) {
		ISala room = new Sala();
		copyObjectProperties(room, infoRoom);
		return room;
	}
	/**
	 * 
	 * @param room
	 * @return IAula
	 */
	public static InfoRoom copyRoom2InfoRoom(ISala room) {

		if (room != null) {
			InfoRoom infoRoom = new InfoRoom();

			copyObjectProperties(infoRoom, room);
			return infoRoom;
		}
		return null;
	}

	/**
	 * Method copyInfoLesson2Lesson.
	 * @param lessonExample
	 * @return IAula
	 */
	public static InfoLesson copyILesson2InfoLesson(IAula lesson) {
		InfoLesson infoLesson = new InfoLesson();
		InfoExecutionCourse infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(lesson.getDisciplinaExecucao());

		InfoRoom infoRoom = Cloner.copyRoom2InfoRoom(lesson.getSala());

		copyObjectProperties(infoLesson, lesson);

		infoLesson.setInfoSala(infoRoom);
		infoLesson.setInfoDisciplinaExecucao(infoExecutionCourse);
		return infoLesson;
	}

	/**
	 * Method copyInfoShift2Shift.
	 * @param infoShift
	 * @return ITurno
	 */
	public static ITurno copyInfoShift2IShift(InfoShift infoShift) {
		if (infoShift == null)
			return null;
		ITurno shift = new Turno();
		IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoShift.getInfoDisciplinaExecucao());

		copyObjectProperties(shift, infoShift);

		shift.setDisciplinaExecucao(executionCourse);

		return shift;
	}
	/**
	 * Method copyInfoShift2Shift.
	 * @param infoShift
	 * @return ITurno
	 */
	public static InfoShift copyShift2InfoShift(ITurno shift) {
		InfoShift infoShift = new InfoShift();

		InfoExecutionCourse infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(shift.getDisciplinaExecucao());

		List infoLessonList = (List) CollectionUtils.collect(shift.getAssociatedLessons(), new Transformer() {
			public Object transform(Object arg0) {
				return copyILesson2InfoLesson((IAula) arg0);
			}
		});

		List infoClassesList = (List) CollectionUtils.collect(shift.getAssociatedClasses(), new Transformer() {
			public Object transform(Object arg0) {
				return copyClass2InfoClass((ITurma) arg0);
			}
		});

		System.out.println("infoLessonList.size= " + infoLessonList.size());

		copyObjectProperties(infoShift, shift);
		infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
		infoShift.setInfoLessons(infoLessonList);
		infoShift.setInfoClasses(infoClassesList);

		return infoShift;
	}

	/**
	 * Method copyInfoShift2Shift.
	 * @param infoShift
	 * @return ITurno
	 */
	public static InfoClass copyClass2InfoClass(ITurma classD) {
		InfoClass infoClass = new InfoClass();
		InfoExecutionDegree infoExecutionDegree = Cloner.copyIExecutionDegree2InfoExecutionDegree(classD.getExecutionDegree());
		InfoExecutionPeriod infoExecutionPeriod = Cloner.copyIExecutionPeriod2InfoExecutionPeriod(classD.getExecutionPeriod());

		copyObjectProperties(infoClass, classD);

		infoClass.setInfoExecutionDegree(infoExecutionDegree);
		infoClass.setInfoExecutionPeriod(infoExecutionPeriod);
		return infoClass;
	}
	/**
	 * Method copyIExecutionPeriod2InfoExecutionPeriod.
	 * @param iExecutionPeriod
	 * @return InfoExecutionPeriod
	 */

	public static InfoExecutionPeriod copyIExecutionPeriod2InfoExecutionPeriod(IExecutionPeriod executionPeriod) {
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
		InfoExecutionYear infoExecutionYear = Cloner.copyIExecutionYear2InfoExecutionYear(executionPeriod.getExecutionYear());

		copyObjectProperties(infoExecutionPeriod, executionPeriod);

		infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
		return infoExecutionPeriod;
	}

	private static void copyObjectProperties(Object destination, Object source) {
		if (source != null)
			try {
				BeanUtils.copyProperties(destination, source);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
	}

	/**
	 * 
	 * @param infoExecutionDegree
	 * @return ICursoExecucao
	 */
	public static ICursoExecucao copyInfoExecutionDegree2ExecutionDegree(InfoExecutionDegree infoExecutionDegree) {

		ICursoExecucao executionDegree = new CursoExecucao();
		IDegreeCurricularPlan degreeCurricularPlan =
			Cloner.copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoExecutionDegree.getInfoDegreeCurricularPlan());

		IExecutionYear executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionDegree.getInfoExecutionYear());
		ITeacher coordinator = null;
		if (infoExecutionDegree.getInfoCoordinator() != null)
			coordinator = Cloner.copyInfoTeacher2Teacher(infoExecutionDegree.getInfoCoordinator());

		copyObjectProperties(executionDegree, infoExecutionDegree);

		executionDegree.setCoordinator(coordinator);
		executionDegree.setExecutionYear(executionYear);
		executionDegree.setCurricularPlan(degreeCurricularPlan);

		return executionDegree;

	}

	/**
	 * 
	 * @param executionDegree
	 * @return InfoExecutionDegree
	 */

	public static InfoExecutionDegree copyIExecutionDegree2InfoExecutionDegree(ICursoExecucao executionDegree) {

		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();

		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(executionDegree.getCurricularPlan());
		InfoTeacher infoCoordinator = null;
		if (executionDegree.getCoordinator() != null) {
			infoCoordinator = Cloner.copyITeacher2InfoTeacher(executionDegree.getCoordinator());
		}

		InfoExecutionYear infoExecutionYear = Cloner.copyIExecutionYear2InfoExecutionYear(executionDegree.getExecutionYear());
		try {
			BeanUtils.copyProperties(infoExecutionDegree, executionDegree);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}

		infoExecutionDegree.setInfoCoordinator(infoCoordinator);
		infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
		infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
		infoExecutionDegree.setTemporaryExamMap(executionDegree.getTemporaryExamMap());

		return infoExecutionDegree;

	}
	/**
	 * Method copyInfoExecutionYear2IExecutionYear.
	 * @param infoExecutionYear
	 * @return IExecutionYear
	 */
	public static IExecutionYear copyInfoExecutionYear2IExecutionYear(InfoExecutionYear infoExecutionYear) {
		IExecutionYear executionYear = new ExecutionYear();
		try {
			BeanUtils.copyProperties(executionYear, infoExecutionYear);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		return executionYear;
	}
	/**
	 * Method copyInfoExecutionYear2IExecutionYear.
	 * @param infoExecutionYear
	 * @return IExecutionYear
	 */
	public static InfoExecutionYear copyIExecutionYear2InfoExecutionYear(IExecutionYear executionYear) {
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		copyObjectProperties(infoExecutionYear, executionYear);
		return infoExecutionYear;
	}

	/**
	 * Method copyIDegree2InfoDegree.
	 * @param iCurso
	 * @return InfoDegree
	 */
	public static InfoDegree copyIDegree2InfoDegree(ICurso degree) {
		InfoDegree infoDegree = new InfoDegree();
		try {
			BeanUtils.copyProperties(infoDegree, degree);

			// FIXME : See InfoDegree variables for root cause.
			if (degree != null && degree.getTipoCurso() != null) {
				infoDegree.setDegreeTypeString(degree.getTipoCurso().toString());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return infoDegree;
	}
	/**
	 * Method copyInfoDegree2IDegree.
	 * @param infoDegree
	 * @return ICurso
	 */
	public static ICurso copyInfoDegree2IDegree(InfoDegree infoDegree) {
		ICurso degree = new Curso();
		try {
			BeanUtils.copyProperties(degree, infoDegree);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return degree;

	}
	/**
	 * Method copyInfoExecutionPeriod2IExecutionPeriod.
	 * @param infoExecutionPeriod
	 * @return IExecutionPeriod
	 */
	public static IExecutionPeriod copyInfoExecutionPeriod2IExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {

		IExecutionPeriod executionPeriod = new ExecutionPeriod();

		IExecutionYear executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionPeriod.getInfoExecutionYear());

		copyObjectProperties(executionPeriod, infoExecutionPeriod);

		executionPeriod.setExecutionYear(executionYear);

		return executionPeriod;
	}
	/**
	 * Method copyInfoClass2Class.
	 * @param infoTurma
	 * @return ITurma
	 */
	public static ITurma copyInfoClass2Class(InfoClass infoClass) {
		ITurma domainClass = new Turma();

		IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoClass.getInfoExecutionPeriod());
		ICursoExecucao executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(infoClass.getInfoExecutionDegree());

		copyObjectProperties(domainClass, infoClass);

		domainClass.setExecutionDegree(executionDegree);
		domainClass.setExecutionPeriod(executionPeriod);
		return domainClass;
	}
	/**
	 * Method copyIShift2InfoShift.
	 * @param elem
	 * @return Object
	 */
	public static InfoShift copyIShift2InfoShift(ITurno shift) {

		if (shift == null)
			return null;
		InfoShift infoShift = new InfoShift();
		InfoExecutionCourse infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(shift.getDisciplinaExecucao());
		List infoLessonList = (List) CollectionUtils.collect(shift.getAssociatedLessons(), new Transformer() {
			public Object transform(Object arg0) {
				return copyILesson2InfoLesson((IAula) arg0);
			}
		});

		List infoClassesList = (List) CollectionUtils.collect(shift.getAssociatedClasses(), new Transformer() {
			public Object transform(Object arg0) {
				return copyClass2InfoClass((ITurma) arg0);
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
	 * @param infoStudent
	 * @return IStudent
	 */
	public static IStudent copyInfoStudent2IStudent(InfoStudent infoStudent) {
		IStudent student = new Student();
		IPessoa person = Cloner.copyInfoPerson2IPerson(infoStudent.getInfoPerson());
		IStudentKind studentGroupInfo = Cloner.copyInfoStudentKind2IStudentKind(infoStudent.getInfoStudentKind());
		copyObjectProperties(student, infoStudent);
		student.setPerson(person);
		student.setStudentKind(studentGroupInfo);

		//by gedl at august the 5th, 2003
		student.setIdInternal(infoStudent.getIdInternal());
		return student;
	}

	/**
	 * Method copyIStudent2InfoStudent.
	 * @param elem
	 * @return Object
	 */
	public static InfoStudent copyIStudent2InfoStudent(IStudent student) {
		InfoStudent infoStudent = new InfoStudent();
		copyObjectProperties(infoStudent, student);
		infoStudent.setInfoPerson(Cloner.copyIPerson2InfoPerson(student.getPerson()));
		infoStudent.setInfoStudentKind(Cloner.copyIStudentKind2InfoStudentKind(student.getStudentKind()));

		//by gedl at august the 5th, 2003
		infoStudent.setIdInternal(student.getIdInternal());
		return infoStudent;
	}

	/**
	 * Method copyInfoPerson2IPerson.
	 * @param infoPerson
	 * @return IPessoa
	 */
	public static IPessoa copyInfoPerson2IPerson(InfoPerson infoPerson) {
		IPessoa person = null;
		if (infoPerson != null) {
			person = new Pessoa();
			ICountry country = Cloner.copyInfoCountry2ICountry(infoPerson.getInfoPais());
			copyObjectProperties(person, infoPerson);
			person.setPais(country);
		}
		return person;
	}

	/**
	 * Method copyInfoPerson2IPerson.
	 * @param infoPerson
	 * @return IPessoa
	 */
	public static InfoPerson copyIPerson2InfoPerson(IPessoa person) {
		InfoPerson infoPerson = null;
		if (person != null) {
			infoPerson = new InfoPerson();
			InfoCountry infoCountry = Cloner.copyICountry2InfoCountry(person.getPais());

			copyObjectProperties(infoPerson, person);
			infoPerson.setInfoPais(infoCountry);

			if (person.getAdvisories() != null) {
				infoPerson.setInfoAdvisories((List) CollectionUtils.collect(person.getAdvisories(), new Transformer() {
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
	 * Method copyInfoCandidateSituation2ICandidateSituation
	 * @param infoCandidateSituation
	 * @return
	 */
	public static ICandidateSituation copyInfoCandidateSituation2ICandidateSituation(InfoCandidateSituation infoCandidateSituation) {
		ICandidateSituation candidateSituation = new CandidateSituation();
		copyObjectProperties(candidateSituation, infoCandidateSituation);
		return candidateSituation;
	}

	public static InfoCandidateSituation copyICandidateSituation2InfoCandidateSituation(ICandidateSituation candidateSituation) {
		InfoCandidateSituation infoCandidateSituation = new InfoCandidateSituation();
		copyObjectProperties(infoCandidateSituation, candidateSituation);
		return infoCandidateSituation;
	}

	/**
	 * Method copyInfoMasterDegreeCandidate2IMasterDegreCandidate
	 * @param infoMasterDegreeCandidate
	 * @return IMasterDegreeCandidate
	 */
	public static IMasterDegreeCandidate copyInfoMasterDegreeCandidate2IMasterDegreCandidate(InfoMasterDegreeCandidate infoMasterDegreeCandidate) {
		IMasterDegreeCandidate masterDegreeCandidate = new MasterDegreeCandidate();
		IPessoa person = Cloner.copyInfoPerson2IPerson(infoMasterDegreeCandidate.getInfoPerson());
		ICursoExecucao executionDegree =
			Cloner.copyInfoExecutionDegree2ExecutionDegree(infoMasterDegreeCandidate.getInfoExecutionDegree());
		copyObjectProperties(masterDegreeCandidate, infoMasterDegreeCandidate);

		masterDegreeCandidate.setPerson(person);
		masterDegreeCandidate.setExecutionDegree(executionDegree);
		return masterDegreeCandidate;
	}

	/**
	 * Method copyIMasterDegreeCandidate2InfoMasterDegreCandidate
	 * @param masterDegreeCandidate 
	 * @return InfoMasterDegreeCandidate 
	 */
	public static InfoMasterDegreeCandidate copyIMasterDegreeCandidate2InfoMasterDegreCandidate(IMasterDegreeCandidate masterDegreeCandidate) {
		if (masterDegreeCandidate == null)
			throw new IllegalArgumentException("ERRO -----------------------------");

		InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate();

		InfoExecutionDegree infoExecutionDegree =
			Cloner.copyIExecutionDegree2InfoExecutionDegree(masterDegreeCandidate.getExecutionDegree());
		infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);

		InfoPerson infoPerson = Cloner.copyIPerson2InfoPerson(masterDegreeCandidate.getPerson());
		infoMasterDegreeCandidate.setInfoPerson(infoPerson);

		copyObjectProperties(infoMasterDegreeCandidate, masterDegreeCandidate);
		return infoMasterDegreeCandidate;
	}

	/**
	 * Method copyInfoCountry2ICountry
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

	public static IBibliographicReference copyInfoBibliographicReference2IBibliographicReference(InfoBibliographicReference infoBibliographicReference) {
		IBibliographicReference bibliographicReference = new BibliographicReference();
		IDisciplinaExecucao executionCourse =
			Cloner.copyInfoExecutionCourse2ExecutionCourse(infoBibliographicReference.getInfoExecutionCourse());
		copyObjectProperties(bibliographicReference, infoBibliographicReference);
		bibliographicReference.setExecutionCourse(executionCourse);
		return bibliographicReference;
	}

	public static InfoBibliographicReference copyIBibliographicReference2InfoBibliographicReference(IBibliographicReference bibliographicReference) {
		InfoBibliographicReference infoBibliographicReference = new InfoBibliographicReference();
		InfoExecutionCourse infoExecutionCourse =
			Cloner.copyIExecutionCourse2InfoExecutionCourse(bibliographicReference.getExecutionCourse());
		copyObjectProperties(infoBibliographicReference, bibliographicReference);
		infoBibliographicReference.setInfoExecutionCourse(infoExecutionCourse);
		return infoBibliographicReference;
	}
	/**
	* Method copyInfoSite2ISite.
	* @param infoSite
	* @return ISite
	*/

	public static ISite copyInfoSite2ISite(InfoSite infoSite) {
		ISite site = new Site();
		IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoSite.getInfoExecutionCourse());

		//		ISection initialSection = Cloner.copyInfoSection2ISection(
		//			infoSite.getInitialInfoSection());

		//		List sections = Cloner.copyListInfoSections2ListISections(infoSite.getInfoSections());
		//		List announcements = Cloner.copyListInfoAnnouncements2ListIAnnouncements(infoSite.getInfoAnnouncements());

		copyObjectProperties(site, infoSite);
		site.setExecutionCourse(executionCourse);
		//		site.setInitialSection(initialSection);
		//		site.setSections(sections);
		//		site.setAnnouncements(announcements);

		return site;
	}

	/**
	 * Method copyISite2InfoSite.
	 * @param site
	 * @return InfoSite
	 */

	public static InfoSite copyISite2InfoSite(ISite site) {
		InfoSite infoSite = new InfoSite();

		InfoExecutionCourse infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(site.getExecutionCourse());

		//		InfoSection initialInfoSection = Cloner.copyISection2InfoSection(
		//					site.getInitialSection());

		//		List infoSections = Cloner.copyListISections2ListInfoSections(site.getSections());
		//		List infoAnnouncements = Cloner.copyListIAnnouncements2ListInfoAnnouncements(site.getAnnouncements());
		//		

		copyObjectProperties(infoSite, site);
		infoSite.setInfoExecutionCourse(infoExecutionCourse);
		//		infoSite.setInitialInfoSection(initialInfoSection);
		//		infoSite.setInfoSections(infoSections);
		//		infoSite.setInfoAnnouncements(infoAnnouncements);

		return infoSite;
	}

	/**
		 * Method copyInfoSection2ISection.
		 * @param infoSection
		 * @return ISection
		 **/

	public static ISection copyInfoSection2ISection(InfoSection infoSection) {

		ISection section = new Section();

		ISection fatherSection = null;

		ISite site = Cloner.copyInfoSite2ISite(infoSection.getInfoSite());

		InfoSection infoSuperiorSection = (InfoSection) infoSection.getSuperiorInfoSection();

		if (infoSuperiorSection != null) {
			fatherSection = Cloner.copyInfoSection2ISection(infoSuperiorSection);
		}

		//		List inferiorSections = Cloner.copyListInfoSections2ListISections(infoSection.getInferiorInfoSections());
		//
		//		List items=Cloner.copyListInfoItems2ListIItems(infoSection.getInfoItems());

		copyObjectProperties(section, infoSection);

		section.setSuperiorSection(fatherSection);
		section.setSite(site);
		section.setIdInternal(infoSection.getIdInternal());

		//		section.setInferiorSections(inferiorSections);
		//		section.setItems(items);

		return section;

	}

	/**
	 * Method copyISection2InfoSection.
	 * @param section
	 * @return InfoSection
	 **/

	public static InfoSection copyISection2InfoSection(ISection section) {

		InfoSection infoSection = new InfoSection();

		InfoSection fatherInfoSection = null;

		InfoSite infoSite = Cloner.copyISite2InfoSite(section.getSite());

		ISection superiorSection = (ISection) section.getSuperiorSection();

		if (superiorSection != null) {
			fatherInfoSection = Cloner.copyISection2InfoSection(superiorSection);
		}

		//		List inferiorInfoSections = Cloner.copyListISections2ListInfoSections(section.getInferiorSections());
		//
		//		List infoItems=Cloner.copyListIItems2ListInfoItems(section.getItems());

		copyObjectProperties(infoSection, section);

		infoSection.setSuperiorInfoSection(fatherInfoSection);
		infoSection.setInfoSite(infoSite);
		infoSection.setIdInternal(section.getIdInternal());

		//		infoSection.setInferiorInfoSections(inferiorInfoSections);
		//		infoSection.setInfoItems(infoItems);

		return infoSection;

	}

	/**
	* 
	* @param listInfoSections
	* @return listISections
	*/

	public static List copyListInfoSections2ListISections(List listInfoSections) {

		List listSections = new ArrayList(listInfoSections.size());

		Iterator iterListInfoSections = listInfoSections.iterator();

		while (iterListInfoSections.hasNext()) {
			InfoSection infoSection = (InfoSection) iterListInfoSections.next();
			ISection section = Cloner.copyInfoSection2ISection(infoSection);
			listSections.add(section);
		}

		return listSections;
	}

	/**
	* 
	* @param listISections
	* @return listInfoSections
	*/

	public static List copyListISections2ListInfoSections(List listISections) {

		List listInfoSections = new ArrayList(listISections.size());
		Iterator iterListISections = listISections.iterator();

		while (iterListISections.hasNext()) {

			ISection section = (ISection) iterListISections.next();

			InfoSection infoSection = Cloner.copyISection2InfoSection(section);

			listInfoSections.add(infoSection);

		}

		return listInfoSections;
	}

	/**
	 * Method copyInfoItem2IItem.
	 * @param infoItem
	 * @return IItem
	 **/

	public static IItem copyInfoItem2IItem(InfoItem infoItem) {

		IItem item = new Item();

		ISection section = Cloner.copyInfoSection2ISection(infoItem.getInfoSection());

		copyObjectProperties(item, infoItem);

		item.setSection(section);

		return item;

	}

	/**
	* Method copyIItem2InfoItem.
	* @param item
	* @return InfoItem
	**/

	public static InfoItem copyIItem2InfoItem(IItem item) {

		InfoItem infoItem = new InfoItem();
		InfoSection infoSection = Cloner.copyISection2InfoSection(item.getSection());

		copyObjectProperties(infoItem, item);

		infoItem.setInfoSection(infoSection);

		return infoItem;

	}

	/**
	* 
	* @param listInfoItems
	* @return listIItems
	*/

	public static List copyListInfoItems2ListIItems(List listInfoItems) {
		List listItems = new ArrayList(listInfoItems.size());

		Iterator iterListInfoItems = listInfoItems.iterator();

		while (iterListInfoItems.hasNext()) {
			InfoItem infoItem = (InfoItem) iterListInfoItems.next();
			IItem item = Cloner.copyInfoItem2IItem(infoItem);
			listItems.add(item);
		}

		return listItems;
	}

	/**
	* 
	* @param listIItems
	* @return listInfoItems
	*/

	public static List copyListIItems2ListInfoItems(List listIItems) {
		List listInfoItems = new ArrayList(listIItems.size());

		Iterator iterListIItems = listIItems.iterator();

		while (iterListIItems.hasNext()) {
			IItem item = (IItem) iterListIItems.next();
			InfoItem infoItem = Cloner.copyIItem2InfoItem(item);
			listInfoItems.add(infoItem);
		}

		return listInfoItems;
	}

	/**
		 * Method copyInfoAnnouncement2IAnnouncement.
		 * @param infoAnnouncement
		 * @return IAnnouncement
		 */
	public static IAnnouncement copyInfoAnnouncement2IAnnouncement(InfoAnnouncement infoAnnouncement) {
		IAnnouncement announcement = new Announcement();

		ISite site = Cloner.copyInfoSite2ISite(infoAnnouncement.getInfoSite());

		copyObjectProperties(announcement, infoAnnouncement);
		announcement.setSite(site);

		return announcement;
	}

	/**
	 * Method copyIAnnouncement2InfoAnnouncement.
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
	* 
	* @param listInfoAnnouncements
	* @return listIAnnouncements
	*/

	private static List copyListInfoAnnouncements2ListIAnnouncements(List listInfoAnnouncements) {
		List listAnnouncements = null;

		Iterator iterListInfoAnnouncements = listInfoAnnouncements.iterator();

		while (iterListInfoAnnouncements.hasNext()) {
			InfoAnnouncement infoAnnouncement = (InfoAnnouncement) iterListInfoAnnouncements.next();
			IAnnouncement announcement = Cloner.copyInfoAnnouncement2IAnnouncement(infoAnnouncement);
			listAnnouncements.add(announcement);
		}

		return listAnnouncements;
	}

	/**
	* 
	* @param listIAnnouncements
	* @return listInfoAnnouncements
	*/

	private static List copyListIAnnouncements2ListInfoAnnouncements(List listIAnnouncements) {
		List listInfoAnnouncements = null;

		Iterator iterListIAnnouncements = listIAnnouncements.iterator();

		while (iterListIAnnouncements.hasNext()) {
			IAnnouncement announcement = (IAnnouncement) iterListIAnnouncements.next();
			InfoAnnouncement infoAnnouncement = Cloner.copyIAnnouncement2InfoAnnouncement(announcement);
			listInfoAnnouncements.add(infoAnnouncement);
		}

		return listInfoAnnouncements;
	}

	/**
	 * 
	 * @param curriculum
	 * @return InfoCurriculum
	 */
	public static InfoCurriculum copyICurriculum2InfoCurriculum(ICurriculum curriculum) {
		InfoCurriculum infoCurriculum = new InfoCurriculum();

		InfoCurricularCourse infoCurricularCourse = Cloner.copyCurricularCourse2InfoCurricularCourse(curriculum.getCurricularCourse());

		copyObjectProperties(infoCurriculum, curriculum);
		infoCurriculum.setInfoCurricularCourse(infoCurricularCourse);

		return infoCurriculum;
	}

	/**
	 * 
	 * @param infoCurriculum
	 * @return ICurriculum
	 */
	public static ICurriculum copyInfoCurriculum2ICurriculum(InfoCurriculum infoCurriculum) {
		ICurriculum curriculum = new Curriculum();

		ICurricularCourse curricularCourse =
			Cloner.copyInfoCurricularCourse2CurricularCourse(infoCurriculum.getInfoCurricularCourse());

		copyObjectProperties(curriculum, infoCurriculum);
		curriculum.setCurricularCourse(curricularCourse);

		return curriculum;
	}

	/**
	 * 
	 * @param exam
	 * @return InfoExam
	 */
	public static InfoExam copyIExam2InfoExam(IExam exam) {
		InfoExam infoExam = new InfoExam();

		copyObjectProperties(infoExam, exam);
		List infoRooms = new ArrayList();
		if (exam != null && exam.getAssociatedRooms() != null && exam.getAssociatedRooms().size() > 0) {

			for (int i = 0; i < exam.getAssociatedRooms().size(); i++) {
				infoRooms.add(copyRoom2InfoRoom((ISala) exam.getAssociatedRooms().get(i)));
			}
		}
		infoExam.setAssociatedRooms(infoRooms);

		return infoExam;
	}

	/**
	 * 
	 * @param infoExam
	 * @return IExam
	 */
	public static IExam copyInfoExam2IExam(InfoExam infoExam) {
		IExam exam = new Exam();

		copyObjectProperties(exam, infoExam);

		if (infoExam != null && infoExam.getAssociatedRooms() != null && infoExam.getAssociatedRooms().size() > 0) {
			List rooms = new ArrayList();
			for (int i = 0; i < infoExam.getAssociatedRooms().size(); i++) {
				rooms.add(copyInfoRoom2Room((InfoRoom) infoExam.getAssociatedRooms().get(i)));
			}
			exam.setAssociatedRooms(rooms);
		}

		return exam;
	}
	/**
	 * 
	 * @param teacher
	 * @return
	 */
	public static InfoTeacher copyITeacher2InfoTeacher(ITeacher teacher) {
		InfoTeacher infoTeacher = new InfoTeacher();
		InfoPerson infoPerson = new InfoPerson();
		infoPerson = copyIPerson2InfoPerson(teacher.getPerson());
		copyObjectProperties(infoTeacher, teacher);
		infoTeacher.setInfoPerson(infoPerson);

		return infoTeacher;
	}
	/**
	 * 
	 * @param infoTeacher
	 * @return
	 */
	public static ITeacher copyInfoTeacher2Teacher(InfoTeacher infoTeacher) {
		ITeacher teacher = new Teacher();
		IPessoa person = new Pessoa();
		person = copyInfoPerson2IPerson(infoTeacher.getInfoPerson());
		copyObjectProperties(teacher, infoTeacher);
		teacher.setPerson(person);

		return teacher;
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
	 * 
	 * @param contributor
	 * @return IContributor
	 */
	public static IContributor copyInfoContributor2IContributor(InfoContributor infoContributor) {

		IContributor contributor = new Contributor();
		copyObjectProperties(contributor, infoContributor);
		return contributor;
	}

	/**
	 * 
	 * @param infoGuide
	 * @return IGuide
	 */
	public static IGuide copyInfoGuide2IGuide(InfoGuide infoGuide) {
		IGuide guide = new Guide();
		copyObjectProperties(guide, infoGuide);

		guide.setContributor(Cloner.copyInfoContributor2IContributor(infoGuide.getInfoContributor()));
		guide.setPerson(Cloner.copyInfoPerson2IPerson(infoGuide.getInfoPerson()));
		guide.setExecutionDegree(Cloner.copyInfoExecutionDegree2ExecutionDegree(infoGuide.getInfoExecutionDegree()));

		if (infoGuide.getInfoGuideEntries() != null) {
			Iterator iterator = infoGuide.getInfoGuideEntries().iterator();
			List guideEntries = new ArrayList();
			while (iterator.hasNext()) {
				guideEntries.add(Cloner.copyInfoGuideEntry2IGuideEntry((InfoGuideEntry) iterator.next()));
			}

			guide.setGuideEntries(guideEntries);
		}

		return guide;
	}

	/**
	 * 
	 * @param guide
	 * @return InfoGuide
	 */
	public static InfoGuide copyIGuide2InfoGuide(IGuide guide) {

		InfoGuide infoGuide = new InfoGuide();
		copyObjectProperties(infoGuide, guide);
		infoGuide.setInfoContributor(Cloner.copyIContributor2InfoContributor(guide.getContributor()));
		infoGuide.setInfoPerson(Cloner.copyIPerson2InfoPerson(guide.getPerson()));
		infoGuide.setInfoExecutionDegree(Cloner.copyIExecutionDegree2InfoExecutionDegree(guide.getExecutionDegree()));

		List infoGuideEntries = new ArrayList();
		if (guide.getGuideEntries() != null) {
			Iterator iterator = guide.getGuideEntries().iterator();
			while (iterator.hasNext()) {
				infoGuideEntries.add(Cloner.copyIGuideEntry2InfoGuideEntry((IGuideEntry) iterator.next()));
			}
		}
		infoGuide.setInfoGuideEntries(infoGuideEntries);

		List infoGuideSituations = new ArrayList();
		if (guide.getGuideSituations() != null) {
			Iterator iterator = guide.getGuideSituations().iterator();
			while (iterator.hasNext()) {
				InfoGuideSituation infoGuideSituation =
					Cloner.copyIGuideSituation2InfoGuideSituation((IGuideSituation) iterator.next());
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
	 * 
	 * @param guideEntry
	 * @return InfoGuideEntry
	 */
	public static InfoGuideEntry copyIGuideEntry2InfoGuideEntry(IGuideEntry guideEntry) {
		InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
		copyObjectProperties(infoGuideEntry, guideEntry);
		//		infoGuideEntry.setInfoGuide(Cloner.copyIGuide2InfoGuide(guideEntry.getGuide()));
		return infoGuideEntry;
	}

	/**
	 * 
	 * @param infoGuideEntry
	 * @return IGuideEntry
	 */
	public static IGuideEntry copyInfoGuideEntry2IGuideEntry(InfoGuideEntry infoGuideEntry) {
		IGuideEntry guideEntry = new GuideEntry();
		copyObjectProperties(guideEntry, infoGuideEntry);
		//		guideEntry.setGuide(Cloner.copyInfoGuide2IGuide(infoGuideEntry.getInfoGuide()));
		return guideEntry;
	}

	/**
	 * 
	 * @param guideSituation
	 * @return InfoGuideSituation
	 */
	public static InfoGuideSituation copyIGuideSituation2InfoGuideSituation(IGuideSituation guideSituation) {
		InfoGuideSituation infoGuideSituation = new InfoGuideSituation();
		copyObjectProperties(infoGuideSituation, guideSituation);
		//		infoGuideSituation.setInfoGuide(Cloner.copyIGuide2InfoGuide(guideSituation.getGuide()));
		return infoGuideSituation;
	}

	/**
	 * 
	 * @param infoGuideSituation
	 * @return IGuideSituation
	 */
	public static IGuideSituation copyInfoGuideSituation2IGuideSituation(InfoGuideSituation infoGuideSituation) {
		IGuideSituation guideSituation = new GuideSituation();
		copyObjectProperties(guideSituation, infoGuideSituation);
		guideSituation.setGuide(Cloner.copyInfoGuide2IGuide(infoGuideSituation.getInfoGuide()));
		return guideSituation;
	}

	//	---------------------------------------------- DCS-RJAO -----------------------------------------------

	/**
	 * @author dcs-rjao
	 * @param InfoDegreeCurricularPlan
	 * @return IDegreeCurricularPlan
	 */
	public static IDegreeCurricularPlan copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
		IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();

		ICurso degree = Cloner.copyInfoDegree2IDegree(infoDegreeCurricularPlan.getInfoDegree());

		copyObjectProperties(degreeCurricularPlan, infoDegreeCurricularPlan);

		degreeCurricularPlan.setDegree(degree);

		return degreeCurricularPlan;
	}

	/**
	 * @author dcs-rjao
	 * @param IDegreeCurricularPlan
	 * @return InfoDegreeCurricularPlan
	 */
	public static InfoDegreeCurricularPlan copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {

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
		IDegreeCurricularPlan degreeCurricularPlan =
			Cloner.copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoBranch.getInfoDegreeCurricularPlan());
		copyObjectProperties(branch, infoBranch);
		branch.setDegreeCurricularPlan(degreeCurricularPlan);
		return branch;
	}

	/**
	 * @author dcs-rjao
	 * @param IBranch
	 * @return InfoBranch
	 */
	public static InfoBranch copyIBranch2InfoBranch(IBranch branch) {

		InfoBranch infoBranch = new InfoBranch();
		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(branch.getDegreeCurricularPlan());
		copyObjectProperties(infoBranch, branch);
		infoBranch.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
		return infoBranch;
	}

	/**
	 * @author dcs-rjao
	 * @param InfoCurricularCourse
	 * @return ICurricularCourse
	 */
	public static ICurricularCourse copyInfoCurricularCourse2CurricularCourse(InfoCurricularCourse infoCurricularCourse) {

		ICurricularCourse curricularCourse = new CurricularCourse();

		IDegreeCurricularPlan planoCurricularCurso =
			copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoCurricularCourse.getInfoDegreeCurricularPlan());

		copyObjectProperties(curricularCourse, infoCurricularCourse);

		curricularCourse.setDegreeCurricularPlan(planoCurricularCurso);

		return curricularCourse;
	}

	/**
	 * @author dcs-rjao
	 * @param ICurricularCourse
	 * @return InfoCurricularCourse
	 */

	public static InfoCurricularCourse copyCurricularCourse2InfoCurricularCourse(ICurricularCourse curricularCourse) {

		InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();

		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(curricularCourse.getDegreeCurricularPlan());

		copyObjectProperties(infoCurricularCourse, curricularCourse);

		infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

		return infoCurricularCourse;
	}

	/**
	 * @author dcs-rjao
	 * @param ICurricularCourseScope
	 * @return InfoCurricularCourseScope
	 */

	public static InfoCurricularCourseScope copyICurricularCourseScope2InfoCurricularCourseScope(ICurricularCourseScope curricularCourseScope) {

		InfoCurricularCourseScope infoCurricularCourseScope = new InfoCurricularCourseScope();

		InfoCurricularCourse infoCurricularCourse =
			copyCurricularCourse2InfoCurricularCourse(curricularCourseScope.getCurricularCourse());
		InfoCurricularSemester infoCurricularSemester =
			copyCurricularSemester2InfoCurricularSemester(curricularCourseScope.getCurricularSemester());
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
	public static ICurricularCourseScope copyInfoCurricularCourseScope2ICurricularCourseScope(InfoCurricularCourseScope infoCurricularCourseScope) {

		ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();

		ICurricularCourse curricularCourse =
			copyInfoCurricularCourse2CurricularCourse(infoCurricularCourseScope.getInfoCurricularCourse());
		ICurricularSemester curricularSemester =
			copyInfoCurricularSemester2CurricularSemester(infoCurricularCourseScope.getInfoCurricularSemester());
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
	public static ICurricularSemester copyInfoCurricularSemester2CurricularSemester(InfoCurricularSemester infoCurricularSemester) {
		//		List infoCurricularCoursesList = null;
		//		List curricularCoursesList = new ArrayList();
		ICurricularSemester curricularSemester = new CurricularSemester();

		ICurricularYear curricularYear = copyInfoCurricularYear2CurricularYear(infoCurricularSemester.getInfoCurricularYear());

		//		infoCurricularCoursesList = infoCurricularSemester.getAssociatedInfoCurricularCourses();
		//		if (infoCurricularCoursesList != null && !infoCurricularCoursesList.isEmpty()) {
		//			ListIterator iterator = infoCurricularCoursesList.listIterator();
		//			while (iterator.hasNext()) {
		//				InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iterator.next();
		//				ICurricularCourse curricularCourse = copyInfoCurricularCourse2CurricularCourse(infoCurricularCourse);
		//				curricularCoursesList.add(curricularCourse);
		//			}
		//		}

		copyObjectProperties(curricularSemester, infoCurricularSemester);
		curricularSemester.setCurricularYear(curricularYear);
		//		curricularSemester.setAssociatedCurricularCourses(curricularCoursesList);

		return curricularSemester;
	}

	/**
	 * @author dcs-rjao
	 * @param ICurricularSemester
	 * @return InfoCurricularSemester
	 */
	public static InfoCurricularSemester copyCurricularSemester2InfoCurricularSemester(ICurricularSemester curricularSemester) {
		//		List infoCurricularCoursesList = new ArrayList();
		//		List curricularCoursesList = null;
		InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester();

		InfoCurricularYear infoCurricularYear = copyCurricularYear2InfoCurricularYear(curricularSemester.getCurricularYear());

		//		curricularCoursesList = curricularSemester.getAssociatedCurricularCourses();
		//		if (curricularCoursesList != null && !curricularCoursesList.isEmpty()) {
		//			ListIterator iterator = curricularCoursesList.listIterator();
		//			while (iterator.hasNext()) {
		//				ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
		//				InfoCurricularCourse infoCurricularCourse = copyCurricularCourse2InfoCurricularCourse(curricularCourse);
		//				infoCurricularCoursesList.add(infoCurricularCourse);
		//			}
		//		}

		copyObjectProperties(infoCurricularSemester, curricularSemester);

		infoCurricularSemester.setInfoCurricularYear(infoCurricularYear);
		//		infoCurricularSemester.setAssociatedInfoCurricularCourses(infoCurricularCoursesList);

		return infoCurricularSemester;
	}

	/**
	 * @author dcs-rjao
	 * @param InfoCurricularYear
	 * @return ICurricularYear
	 */
	public static ICurricularYear copyInfoCurricularYear2CurricularYear(InfoCurricularYear infoCurricularYear) {
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
	public static IStudentCurricularPlan copyInfoStudentCurricularPlan2IStudentCurricularPlan(InfoStudentCurricularPlan infoStudentCurricularPlan) {

		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();

		IStudent student = Cloner.copyInfoStudent2IStudent(infoStudentCurricularPlan.getInfoStudent());
		IBranch branch = Cloner.copyInfoBranch2IBranch(infoStudentCurricularPlan.getInfoBranch());
		IDegreeCurricularPlan degreeCurricularPlan =
			Cloner.copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoStudentCurricularPlan.getInfoDegreeCurricularPlan());

		try {
			BeanUtils.copyProperties(studentCurricularPlan, infoStudentCurricularPlan);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		studentCurricularPlan.setStudent(student);
		studentCurricularPlan.setBranch(branch);
		studentCurricularPlan.setDegreeCurricularPlan(degreeCurricularPlan);

		return studentCurricularPlan;
	}

	/**
	 * @author dcs-rjao
	 * @param copyIStudentCurricularPlan2InfoStudentCurricularPlan
	 * @return InfoStudentCurricularPlan
	 */
	public static InfoStudentCurricularPlan copyIStudentCurricularPlan2InfoStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) {

		InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();

		InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(studentCurricularPlan.getStudent());
		InfoBranch infoBranch = Cloner.copyIBranch2InfoBranch(studentCurricularPlan.getBranch());
		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(studentCurricularPlan.getDegreeCurricularPlan());

		try {
			BeanUtils.copyProperties(infoStudentCurricularPlan, studentCurricularPlan);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		infoStudentCurricularPlan.setInfoStudent(infoStudent);
		infoStudentCurricularPlan.setInfoBranch(infoBranch);
		infoStudentCurricularPlan.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

		return infoStudentCurricularPlan;
	}

	/**
		* @author dcs-rjao
		* @param copyIEnrolment2InfoEnrolment
		* @return InfoEnrolment
		*/
	public static InfoEnrolment copyIEnrolment2InfoEnrolment(IEnrolment enrolment) {

		InfoEnrolment infoEnrolment = null;
		InfoCurricularCourse infoCurricularCourseOption = null;

		InfoStudentCurricularPlan infoStudentCurricularPlan =
			Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(enrolment.getStudentCurricularPlan());
		//InfoCurricularCourse infoCurricularCourse = Cloner.copyCurricularCourse2InfoCurricularCourse(enrolment.getCurricularCourse());
		InfoExecutionPeriod infoExecutionPeriod = Cloner.copyIExecutionPeriod2InfoExecutionPeriod(enrolment.getExecutionPeriod());
		InfoCurricularCourseScope infoCurricularCourseScope =
			Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(enrolment.getCurricularCourseScope());

		List infoEnrolmentEvaluationsList = new ArrayList();
		List enrolmentEvaluationsList = enrolment.getEvaluations();

		if (enrolmentEvaluationsList != null && !enrolmentEvaluationsList.isEmpty()) {
			Iterator iterator = enrolmentEvaluationsList.iterator();
			while (iterator.hasNext()) {
				IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();
				InfoEnrolmentEvaluation infoEnrolmentEvaluation =
					Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrolmentEvaluation);
				infoEnrolmentEvaluationsList.add(infoEnrolmentEvaluation);
			}
		}

		if (enrolment instanceof IEnrolmentInOptionalCurricularCourse) {
			infoEnrolment = new InfoEnrolmentInOptionalCurricularCourse();
			infoCurricularCourseOption =
				Cloner.copyCurricularCourse2InfoCurricularCourse(
					((IEnrolmentInOptionalCurricularCourse) enrolment).getCurricularCourseForOption());
			((InfoEnrolmentInOptionalCurricularCourse) infoEnrolment).setInfoCurricularCourseForOption(infoCurricularCourseOption);
		} else if (enrolment instanceof IEnrolmentInExtraCurricularCourse) {
			infoEnrolment = new InfoEnrolmentInExtraCurricularCourse();
		} else {
			infoEnrolment = new InfoEnrolment();
		}

		copyObjectProperties(infoEnrolment, enrolment);

		//			infoEnrolment.setInfoCurricularCourse(infoCurricularCourse);
		infoEnrolment.setInfoCurricularCourseScope(infoCurricularCourseScope);
		infoEnrolment.setInfoExecutionPeriod(infoExecutionPeriod);
		infoEnrolment.setInfoStudentCurricularPlan(infoStudentCurricularPlan);
		infoEnrolment.setInfoEvaluations(infoEnrolmentEvaluationsList);

		return infoEnrolment;
	}

	/**
		* @author dcs-rjao
		* @param copyInfoEnrolment2IEnrolment
		* @return IEnrolment
		*/
	public static IEnrolment copyInfoEnrolment2IEnrolment(InfoEnrolment infoEnrolment) {

		IEnrolment enrolment = null;
		ICurricularCourse curricularCourseOption = null;

		IStudentCurricularPlan studentCurricularPlan =
			Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoEnrolment.getInfoStudentCurricularPlan());
		//		ICurricularCourse curricularCourse = Cloner.copyInfoCurricularCourse2CurricularCourse(infoEnrolment.getInfoCurricularCourse());
		ICurricularCourseScope curricularCourseScope =
			Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoEnrolment.getInfoCurricularCourseScope());
		IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoEnrolment.getInfoExecutionPeriod());

		if (infoEnrolment instanceof InfoEnrolmentInOptionalCurricularCourse) {
			enrolment = new EnrolmentInOptionalCurricularCourse();
			curricularCourseOption =
				Cloner.copyInfoCurricularCourse2CurricularCourse(
					((InfoEnrolmentInOptionalCurricularCourse) infoEnrolment).getInfoCurricularCourseForOption());
			((IEnrolmentInOptionalCurricularCourse) enrolment).setCurricularCourseForOption(curricularCourseOption);
		} else {
			enrolment = new Enrolment();
		}

		List enrolmentEvaluationsList = new ArrayList();
		List infoEnrolmentEvaluationsList = infoEnrolment.getInfoEvaluations();
		if (infoEnrolmentEvaluationsList != null && !infoEnrolmentEvaluationsList.isEmpty()) {
			Iterator iterator = infoEnrolmentEvaluationsList.iterator();
			while (iterator.hasNext()) {
				InfoEnrolmentEvaluation infoEnrolmentEvaluation = (InfoEnrolmentEvaluation) iterator.next();
				IEnrolmentEvaluation enrolmentEvaluation = copyInfoEnrolmentEvaluation2IEnrolmentEvaluation(infoEnrolmentEvaluation);
				enrolmentEvaluationsList.add(enrolmentEvaluation);
			}
		}

		copyObjectProperties(enrolment, infoEnrolment);

		enrolment.setCurricularCourseScope(curricularCourseScope);
		//		enrolment.setCurricularCourse(curricularCourse);
		enrolment.setExecutionPeriod(executionPeriod);
		enrolment.setStudentCurricularPlan(studentCurricularPlan);
		enrolment.setEvaluations(enrolmentEvaluationsList);

		return enrolment;
	}

	/**
		* @author dcs-rjao
		* @param copyIEquivalence2InfoEquivalence
		* @return InfoEquivalence
		*/
	//	public static InfoEquivalence copyIEquivalence2InfoEquivalence(IEnrolmentEquivalence equivalence) {
	//
	//		InfoEquivalence infoEquivalence = new InfoEquivalence();
	//
	//		InfoEnrolment infoEnrolment = Cloner.copyIEnrolment2InfoEnrolment(equivalence.getEnrolment());
	//		InfoEnrolment infoEquivalentEnrolment = Cloner.copyIEnrolment2InfoEnrolment(equivalence.getEquivalentEnrolment());
	//
	//		copyObjectProperties(infoEquivalence, equivalence);
	//
	//		infoEquivalence.setInfoEnrolment(infoEnrolment);
	//		infoEquivalence.setInfoEquivalentEnrolment(infoEquivalentEnrolment);
	//
	//		return infoEquivalence;
	//	}

	/**
		* @author dcs-rjao
		* @param copyInfoEquivalence2IEquivalence
		* @return IEnrolmentEquivalence
		*/
	//	public static IEnrolmentEquivalence copyInfoEquivalence2IEquivalence(InfoEquivalence infoEquivalence) {
	//
	//		IEnrolmentEquivalence equivalence = new EnrolmentEquivalence();
	//
	//		IEnrolment enrolment = Cloner.copyInfoEnrolment2IEnrolment(infoEquivalence.getInfoEnrolment());
	//		IEnrolment equivalentEnrolment = Cloner.copyInfoEnrolment2IEnrolment(infoEquivalence.getInfoEquivalentEnrolment());
	//
	//		copyObjectProperties(equivalence, infoEquivalence);
	//
	//		equivalence.setEnrolment(enrolment);
	//		equivalence.setEquivalentEnrolment(equivalentEnrolment);
	//
	//		return equivalence;
	//	}

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
	public static InfoEnrolmentEvaluation copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(IEnrolmentEvaluation enrolmentEvaluation) {
		//		properties of infoEnrolment are not copied for not to get into loop
		InfoEnrolmentEvaluation infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
		InfoPerson infoPerson = copyIPerson2InfoPerson(enrolmentEvaluation.getPersonResponsibleForGrade());
		copyObjectProperties(infoEnrolmentEvaluation, enrolmentEvaluation);
		infoEnrolmentEvaluation.setInfoPersonResponsibleForGrade(infoPerson);
		return infoEnrolmentEvaluation;
	}

	/**
	 * @author dcs-rjao
	 * @param IEnrolmentEvaluation
	 * @return InfoEnrolmentEvaluation
	 */
	public static IEnrolmentEvaluation copyInfoEnrolmentEvaluation2IEnrolmentEvaluation(InfoEnrolmentEvaluation infoEnrolmentEvaluation) {
		//		properties of infoEnrolment are not copied for not to get into loop
		IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
		IPessoa pessoa = copyInfoPerson2IPerson(infoEnrolmentEvaluation.getInfoPersonResponsibleForGrade());
		copyObjectProperties(enrolmentEvaluation, infoEnrolmentEvaluation);
		enrolmentEvaluation.setPersonResponsibleForGrade(pessoa);
		return enrolmentEvaluation;
	}

	//	---------------------------------------------- DCS-RJAO -----------------------------------------------
	public static IEvaluationMethod copyInfoEvaluationMethod2IEvaluationMethod(InfoEvaluationMethod infoEvaluationMethod) {

		IEvaluationMethod evaluationMethod = new EvaluationMethod();
		ICurricularCourse curricularCourse =
			Cloner.copyInfoCurricularCourse2CurricularCourse(infoEvaluationMethod.getInfoCurricularCourse());

		copyObjectProperties(evaluationMethod, infoEvaluationMethod);

		evaluationMethod.setCurricularCourse(curricularCourse);

		return evaluationMethod;
	}

	public static InfoEvaluationMethod copyIEvaluationMethod2InfoEvaluationMethod(IEvaluationMethod evaluationMethod) {

		InfoEvaluationMethod infoEvaluationMethod = new InfoEvaluationMethod();
		InfoCurricularCourse infoCurricularCourse =
			Cloner.copyCurricularCourse2InfoCurricularCourse(evaluationMethod.getCurricularCourse());

		copyObjectProperties(infoEvaluationMethod, evaluationMethod);

		infoEvaluationMethod.setInfoCurricularCourse(infoCurricularCourse);

		return infoEvaluationMethod;
	}
	public static IEvaluation copyInfoEvaluation2IEvaluation(InfoEvaluation infoEvaluation) {

		IEvaluation evaluation = null;

		if (infoEvaluation instanceof InfoExam) {
			evaluation = new Exam();
		} else if (infoEvaluation instanceof InfoFinalEvaluation) {
			evaluation = new FinalEvaluation();
		}

		copyObjectProperties(evaluation, infoEvaluation);

		return evaluation;
	}
	public static InfoEvaluation copyIEvaluation2InfoEvaluation(IEvaluation evaluation) {

		InfoEvaluation infoEvaluation = null;

		if (evaluation instanceof IExam) {
			infoEvaluation = new InfoExam();
			infoEvaluation.setEvaluationType(EvaluationType.EXAM_TYPE);
		} else if (evaluation instanceof IFinalEvaluation) {
			infoEvaluation = new InfoFinalEvaluation();
			infoEvaluation.setEvaluationType(EvaluationType.FINAL_TYPE);
		}

		copyObjectProperties(infoEvaluation, evaluation);

		return infoEvaluation;
	}

	public static InfoTeacherShiftPercentage copyITeacherShiftPercentage2InfoTeacherShiftPercentage(ITeacherShiftPercentage teacherShiftPercentage) {
		InfoTeacherShiftPercentage infoTeacherShiftPercentage = new InfoTeacherShiftPercentage();

		InfoProfessorShip infoProfessorShip = Cloner.copyIProfessorShip2InfoProfessorShip(teacherShiftPercentage.getProfessorShip());
		InfoShift infoShift = Cloner.copyIShift2InfoShift(teacherShiftPercentage.getShift());

		copyObjectProperties(infoTeacherShiftPercentage, teacherShiftPercentage);

		infoTeacherShiftPercentage.setInfoProfessorship(infoProfessorShip);
		infoTeacherShiftPercentage.setInfoShift(infoShift);

		return infoTeacherShiftPercentage;
	}

	public static InfoProfessorShip copyIProfessorShip2InfoProfessorShip(IProfessorship professorship) {
		InfoProfessorShip infoProfessorShip = new InfoProfessorShip();

		InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(professorship.getTeacher());
		InfoExecutionCourse infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(professorship.getExecutionCourse());

		copyObjectProperties(infoProfessorShip, professorship);

		infoProfessorShip.setInfoTeacher(infoTeacher);
		infoProfessorShip.setInfoExecutionCourse(infoExecutionCourse);

		return infoProfessorShip;
	}

	public static ITeacherShiftPercentage copyInfoTeacherPercentage2ITeacherShiftPercentage(InfoTeacherShiftPercentage infoTeacherShiftPercentage) {
		InfoShift infoShift = infoTeacherShiftPercentage.getInfoShift();
		InfoProfessorShip infoProfessorShip = infoTeacherShiftPercentage.getInfoProfessorship();

		ITeacherShiftPercentage teacherShiftPercentage = new TeacherShiftPercentage();
		IProfessorship professorship = Cloner.copyInfoProfessorShip2IProfessorShip(infoProfessorShip);
		ITurno shift = Cloner.copyInfoShift2IShift(infoShift);

		copyObjectProperties(teacherShiftPercentage, infoTeacherShiftPercentage);

		teacherShiftPercentage.setPercentage(infoTeacherShiftPercentage.getPercentage());
		teacherShiftPercentage.setShift(shift);
		teacherShiftPercentage.setProfessorShip(professorship);

		return teacherShiftPercentage;
	}

	public static IProfessorship copyInfoProfessorShip2IProfessorShip(InfoProfessorShip infoProfessorShip) {
		InfoExecutionCourse infoExecutionCourse = infoProfessorShip.getInfoExecutionCourse();
		InfoTeacher infoTeacher = infoProfessorShip.getInfoTeacher();

		IProfessorship professorship = new Professorship();
		IDisciplinaExecucao disciplinaExecucao = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
		ITeacher teacher = Cloner.copyInfoTeacher2Teacher(infoTeacher);

		copyObjectProperties(professorship, infoProfessorShip);

		professorship.setExecutionCourse(disciplinaExecucao);
		professorship.setTeacher(teacher);

		return professorship;
	}

	public static ICredits copyInfoCreditsTeacher2ICreditsTeacher(InfoCredits infoCreditsTeacher) {

		InfoTeacher infoTeacher = infoCreditsTeacher.getInfoTeacher();
		ITeacher teacher = Cloner.copyInfoTeacher2Teacher(infoTeacher);

		InfoExecutionPeriod infoExecutionPeriod = infoCreditsTeacher.getInfoExecutionPeriod();
		IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

		ICredits creditsTeacher = new Credits();
		copyObjectProperties(creditsTeacher, infoCreditsTeacher);

		creditsTeacher.setTeacher(teacher);
		creditsTeacher.setExecutionPeriod(executionPeriod);

		return creditsTeacher;
	}

	public static InfoCredits copyICreditsTeacher2InfoCreditsTeacher(ICredits creditsTeacher) {
		ITeacher teacher = creditsTeacher.getTeacher();
		InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

		IExecutionPeriod executionPeriod = creditsTeacher.getExecutionPeriod();
		InfoExecutionPeriod infoExecutionPeriod = Cloner.copyIExecutionPeriod2InfoExecutionPeriod(executionPeriod);

		InfoCredits infoCreditsTeacher = new InfoCredits();
		copyObjectProperties(infoCreditsTeacher, creditsTeacher);

		infoCreditsTeacher.setInfoTeacher(infoTeacher);
		infoCreditsTeacher.setInfoExecutionPeriod(infoExecutionPeriod);

		return infoCreditsTeacher;
	}

	/**
	 * 
	 * @param mark
	 * @return
	 */
	public static InfoMark copyIMark2InfoMark(IMark mark) {
		InfoMark infoMark = new InfoMark();

		InfoFrequenta infoFrequenta = new InfoFrequenta();
		infoFrequenta = copyIFrequenta2InfoFrequenta(mark.getAttend());

		InfoEvaluation infoEvaluation = new InfoExam();
		infoEvaluation = copyIEvaluation2InfoEvaluation(mark.getEvaluation());

		copyObjectProperties(infoMark, mark);
		infoMark.setInfoFrequenta(infoFrequenta);
		infoMark.setInfoEvaluation(infoEvaluation);

		return infoMark;
	}
	/**
	 * 
	 * @param infoMark
	 * @return IMark
	 */
	public static IMark copyInfoMark2IMark(InfoMark infoMark) {
		IMark mark = new Mark();

		InfoEvaluation infoEvaluation = infoMark.getInfoEvaluation();
		IEvaluation evaluation = Cloner.copyInfoEvaluation2IEvaluation(infoEvaluation);

		InfoFrequenta infoFrequenta = infoMark.getInfoFrequenta();
		IFrequenta frequenta = Cloner.copyInfoFrequenta2IFrequenta(infoFrequenta);

		copyObjectProperties(mark, infoMark);
		mark.setEvaluation(evaluation);
		mark.setAttend(frequenta);
		return mark;
	}

	/**
	 * 
	 * @param IFrquenta
	 * @return InfoFrequenta
	 */
	public static InfoFrequenta copyIFrequenta2InfoFrequenta(IFrequenta frequenta) {
		InfoFrequenta infoFrequenta = new InfoFrequenta();

		InfoStudent infoStudent = new InfoStudent();
		infoStudent = Cloner.copyIStudent2InfoStudent(frequenta.getAluno());

		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(frequenta.getDisciplinaExecucao());

		InfoEnrolment infoEnrolment = null;
		if (frequenta.getEnrolment() != null) {
			infoEnrolment = Cloner.copyIEnrolment2InfoEnrolment(frequenta.getEnrolment());
		}

		//copyObjectProperties(infoFrequenta, frequenta);

		infoFrequenta.setAluno(infoStudent);
		infoFrequenta.setDisciplinaExecucao(infoExecutionCourse);
		infoFrequenta.setInfoEnrolment(infoEnrolment);

		return infoFrequenta;
	}

	/**
	 * 
	 * @param infoFrequenta
	 * @return IFrequenta
	 */
	public static IFrequenta copyInfoFrequenta2IFrequenta(InfoFrequenta infoFrequenta) {
		IFrequenta frequenta = new Frequenta();

		InfoStudent infoStudent = infoFrequenta.getAluno();
		IStudent student = Cloner.copyInfoStudent2IStudent(infoStudent);

		InfoExecutionCourse infoExecutionCourse = infoFrequenta.getDisciplinaExecucao();
		IDisciplinaExecucao disciplinaExecucao = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);

		InfoEnrolment infoEnrolment = infoFrequenta.getInfoEnrolment();
		IEnrolment enrolment = null;
		if (infoEnrolment != null) {
			enrolment = Cloner.copyInfoEnrolment2IEnrolment(infoEnrolment);
		}

		//copyObjectProperties(frequenta, infoFrequenta);

		frequenta.setAluno(student);
		frequenta.setDisciplinaExecucao(disciplinaExecucao);
		frequenta.setEnrolment(enrolment);

		return frequenta;
	}

	/**
	 * 
	 * @param infoPrice
	 * @return IPrice
	 */
	public static IPrice copyInfoPrice2IPrice(InfoPrice infoPrice) {
		IPrice price = new Price();
		copyObjectProperties(price, infoPrice);
		return price;
	}

	/**
	 * 
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
	public static InfoExamStudentRoom copyIExamStudentRoom2InfoExamStudentRoom(IExamStudentRoom examStudentRoom) {

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

	public static IDepartment copyInfoDepartment2IDepartment(InfoDepartment infoDepartment) {
		IDepartment department = new Department();
		copyObjectProperties(department, infoDepartment);
		return department;
	}

	public static InfoSummary copyISummary2InfoSummary(ISummary summary) {
		InfoSummary infoSummary = new InfoSummary();
		copyObjectProperties(infoSummary, summary);
		InfoExecutionCourse infoExecutionCourse = copyIExecutionCourse2InfoExecutionCourse(summary.getExecutionCourse());
		infoSummary.setInfoExecutionCourse(infoExecutionCourse);
		return infoSummary;
	}

	public static ISummary copyInfoSummary2ISummary(InfoSummary infoSummary) {
		ISummary summary = new Summary();
		copyObjectProperties(summary, infoSummary);
		IDisciplinaExecucao executionCourse = copyInfoExecutionCourse2ExecutionCourse(infoSummary.getInfoExecutionCourse());
		summary.setExecutionCourse(executionCourse);
		return summary;
	}

	/**
	 * 
	 * @param infoGratuity
	 * @return IGratuity
	 */
	public static IGratuity copyInfoGratuity2IGratuity(InfoGratuity infoGratuity) {
		IGratuity gratuity = new Gratuity();
		copyObjectProperties(gratuity, infoGratuity);

		gratuity.setStudentCurricularPlan(
			Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoGratuity.getInfoStudentCurricularPlan()));
		return gratuity;
	}

	/**
	 * 
	 * @param gratuity
	 * @return InfoGratuity
	 */
	public static InfoGratuity copyIGratuity2InfoGratuity(IGratuity gratuity) {

		InfoGratuity infoGratuity = new InfoGratuity();
		copyObjectProperties(infoGratuity, gratuity);

		infoGratuity.setInfoStudentCurricularPlan(
			Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(gratuity.getStudentCurricularPlan()));
		return infoGratuity;
	}

	/**
	 * 
	 * @param infoCandidateEnrolment
	 * @return ICandidateEnrolment
	 */
	public static ICandidateEnrolment copyInfoCandidateEnrolment2ICandidateEnrolment(InfoCandidateEnrolment infoCandidateEnrolment) {

		ICandidateEnrolment candidateEnrolment = new CandidateEnrolment();

		candidateEnrolment.setMasterDegreeCandidate(
			Cloner.copyInfoMasterDegreeCandidate2IMasterDegreCandidate(infoCandidateEnrolment.getInfoMasterDegreeCandidate()));
		candidateEnrolment.setCurricularCourseScope(
			Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCandidateEnrolment.getInfoCurricularCourseScope()));

		return candidateEnrolment;
	}

	/**
	 * 
	 * @param candidateEnrolment
	 * @return InfoCandidateEnrolment
	 */
	public static InfoCandidateEnrolment copyICandidateEnrolment2InfoCandidateEnrolment(ICandidateEnrolment candidateEnrolment) {

		InfoCandidateEnrolment infoCandidateEnrolment = new InfoCandidateEnrolment();
		infoCandidateEnrolment.setInfoCurricularCourseScope(
			Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(candidateEnrolment.getCurricularCourseScope()));
		infoCandidateEnrolment.setInfoMasterDegreeCandidate(
			Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(candidateEnrolment.getMasterDegreeCandidate()));

		return infoCandidateEnrolment;
	}

	/**
		* @param groupProperties
		* @return infoGroupProperties
		*/

	public static InfoGroupProperties copyIGroupProperties2InfoGroupProperties(IGroupProperties groupProperties) {
		InfoGroupProperties infoGroupProperties = new InfoGroupProperties();
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();

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

		infoExecutionCourse = copyIExecutionCourse2InfoExecutionCourse(groupProperties.getExecutionCourse());
		infoGroupProperties.setInfoExecutionCourse(infoExecutionCourse);
		return infoGroupProperties;
	}

	/**
	* 
	* @param infoGroupProperties
	* @return IGroupProperties
	*/

	public static IGroupProperties copyInfoGroupProperties2IGroupProperties(InfoGroupProperties infoGroupProperties) {
		IGroupProperties groupProperties = new GroupProperties();

		IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
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
		
		executionCourse = copyInfoExecutionCourse2ExecutionCourse(infoGroupProperties.getInfoExecutionCourse());
		groupProperties.setExecutionCourse(executionCourse);
		return groupProperties;
	}

	/**
	* @param studentGroup
	* @return infoStudentGroup
	*/

	public static InfoStudentGroup copyIStudentGroup2InfoStudentGroup(IStudentGroup studentGroup) {
		InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
		InfoGroupProperties infoGroupProperties = new InfoGroupProperties();
		InfoShift infoShift = new InfoShift();

		infoGroupProperties = copyIGroupProperties2InfoGroupProperties(studentGroup.getGroupProperties());

		infoShift = copyIShift2InfoShift(studentGroup.getShift());

		infoStudentGroup.setGroupNumber(studentGroup.getGroupNumber());
		infoStudentGroup.setIdInternal(studentGroup.getIdInternal());
		infoStudentGroup.setInfoGroupProperties(infoGroupProperties);
		infoStudentGroup.setInfoShift(infoShift);

		return infoStudentGroup;
	}

	/**
	* 
	* @param infoStudentGroup
	* @return IStudentGroup
	*/

	public static IStudentGroup copyInfoStudentGroup2IStudentGroup(InfoStudentGroup infoStudentGroup) {

		IStudentGroup studentGroup = new StudentGroup();
		IGroupProperties groupProperties = new GroupProperties();
		ITurno shift = new Turno();
		groupProperties = copyInfoGroupProperties2IGroupProperties(infoStudentGroup.getInfoGroupProperties());
		shift = copyInfoShift2IShift(infoStudentGroup.getInfoShift());
		studentGroup.setGroupNumber(infoStudentGroup.getGroupNumber());
		studentGroup.setIdInternal(infoStudentGroup.getIdInternal());
		studentGroup.setGroupProperties(groupProperties);

		return studentGroup;
	}

	/**
	* @param studentGroupAttend
	* @return infoStudentGroupAttend
	*/

	public static InfoStudentGroupAttend copyIStudentGroupAttend2InfoStudentGroupAttend(IStudentGroupAttend studentGroupAttend) {
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

	/**
	* 
	* @param infoStudentGroupAttend
	* @return IStudentGroupAttend
	*/

	public static IStudentGroupAttend copyInfoStudentGroupAttend2IStudentGroupAttend(InfoStudentGroupAttend infoStudentGroupAttend) {

		IStudentGroupAttend studentGroupAttend = new StudentGroupAttend();
		IStudentGroup studentGroup = new StudentGroup();
		IFrequenta attend = new Frequenta();

		studentGroup = copyInfoStudentGroup2IStudentGroup(infoStudentGroupAttend.getInfoStudentGroup());
		attend = copyInfoFrequenta2IFrequenta(infoStudentGroupAttend.getInfoAttend());

		studentGroupAttend.setStudentGroup(studentGroup);
		studentGroupAttend.setAttend(attend);
		studentGroupAttend.setIdInternal(infoStudentGroupAttend.getIdInternal());
		return studentGroupAttend;
	}

	public static InfoCurricularYear copyICurricularYear2InfoCurricularYear(ICurricularYear curricularYear) {
		InfoCurricularYear infoCurricularYear = new InfoCurricularYear();
		copyObjectProperties(infoCurricularYear, curricularYear);
		return infoCurricularYear;
	}

	//by gedl AT rnl DOT ist DOT utl DOT pt (August the 3rd, 2003)
	public static InfoModality copyIModality2InfoModality(IModality modality) {
		InfoModality infoModality = new InfoModality();
		copyObjectProperties(infoModality, modality);

		return infoModality;
	}

	//by gedl AT rnl DOT ist DOT utl DOT pt (August the 4rd, 2003)
	public static InfoTheme copyITheme2InfoTheme(ITheme theme) {
		InfoTheme infoTheme = new InfoTheme();
		copyObjectProperties(infoTheme, theme);

		return infoTheme;
	}

	//by gedl AT rnl DOT ist DOT utl DOT pt (August the 4rd, 2003)
	public static InfoCaseStudy copyICaseStudy2InfoCaseStudy(ICaseStudy caseStudy) {
		InfoCaseStudy infoCaseStudy = new InfoCaseStudy();
		infoCaseStudy.setCode(caseStudy.getCode());
		infoCaseStudy.setDescription(caseStudy.getDescription());
		infoCaseStudy.setIdInternal(caseStudy.getIdInternal());
		infoCaseStudy.setName(caseStudy.getName());
		infoCaseStudy.setSeminaryCandidacies(caseStudy.getSeminaryCandidacies());
		infoCaseStudy.setThemeName(caseStudy.getSeminaryTheme().getName());

		return infoCaseStudy;
	}

	//by gedl AT rnl DOT ist DOT utl DOT pt (August the 4th, 2003)
	public static InfoEquivalency copyIEquivalency2InfoEquivalency(ICourseEquivalency equivalency) {
		InfoEquivalency infoEquivalency = new InfoEquivalency();

		infoEquivalency.setIdInternal(equivalency.getIdInternal());
		infoEquivalency.setModalityIdInternal(equivalency.getModalityIdInternal());
		infoEquivalency.setSeminaryIdInternal(equivalency.getSeminaryIdInternal());
		infoEquivalency.setSeminaryName(equivalency.getSeminary().getName());
		infoEquivalency.setCurricularCourseIdInternal(equivalency.getCurricularCourseIdInternal());

		infoEquivalency.setCurricularCourse(Cloner.copyCurricularCourse2InfoCurricularCourse(equivalency.getCurricularCourse()));
		infoEquivalency.setModality(Cloner.copyIModality2InfoModality(equivalency.getModality()));
		List themes = new LinkedList();
		for (Iterator iterator = equivalency.getThemes().iterator(); iterator.hasNext();) {
			ITheme theme = (ITheme) iterator.next();
			InfoTheme infoTheme = copyITheme2InfoTheme(theme);
			themes.add(infoTheme);
		}
		infoEquivalency.setThemes(themes);

		return infoEquivalency;
	}

	//  by gedl AT rnl DOT ist DOT utl DOT pt (July the 31th, 2003)
	public static InfoSeminary copyISeminary2InfoSeminary(ISeminary seminary) {
		InfoSeminary infoSeminary = null;
		if (seminary != null) {
			infoSeminary = new InfoSeminary();
			infoSeminary.setDescription(seminary.getDescription());
			infoSeminary.setIdInternal(seminary.getIdInternal());
			infoSeminary.setName(seminary.getName());
			infoSeminary.setAllowedCandidaciesPerStudent(seminary.getAllowedCandidaciesPerStudent());
			List equivalencies = new LinkedList();
			for (Iterator iterator = seminary.getEquivalencies().iterator(); iterator.hasNext();) {
				ICourseEquivalency courseEquivalency = (ICourseEquivalency) iterator.next();
				InfoEquivalency infoEquivalency = copyIEquivalency2InfoEquivalency(courseEquivalency);
				equivalencies.add(infoEquivalency);
			}
			infoSeminary.setEquivalencies(equivalencies);
		}

		return infoSeminary;
	}

	//  by gedl AT rnl DOT ist DOT utl DOT pt (August the 5th, 2003)
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
				caseStudy.setCaseStudyIdInternal(element.getCaseStudyIdInternal());
				caseStudyChoices.add(caseStudy);
			}

			candidacy.setCaseStudyChoices(caseStudyChoices);
			candidacy.setCurricularCourseIdInternal(infoCandidacy.getCurricularCourseIdInternal());
			candidacy.setStudentIdInternal(infoCandidacy.getStudentIdInternal());
			candidacy.setThemeIdInternal(infoCandidacy.getThemeIdInternal());
			candidacy.setModalityIdInternal(infoCandidacy.getModalityIdInternal());
			candidacy.setSeminaryIdInternal(infoCandidacy.getSeminaryIdInternal());
			candidacy.setIdInternal(infoCandidacy.getIdInternal());
		}
		return candidacy;
	}

	//  by gedl AT rnl DOT ist DOT utl DOT pt (August the 5th, 2003)
	public static InfoCandidacy copyICandicacy2InfoCandidacy(ICandidacy candidacy) {
		InfoCandidacy infoCandidacy = null;
		if (candidacy != null) {
			infoCandidacy = new InfoCandidacy();
			List caseStudyChoices = new LinkedList();
			infoCandidacy.setMotivation(candidacy.getMotivation());
			for (Iterator iter = candidacy.getCaseStudyChoices().iterator(); iter.hasNext();) {
				CaseStudyChoice element = (CaseStudyChoice) iter.next();
				InfoCaseStudyChoice infoCaseStudy = new InfoCaseStudyChoice();
				infoCaseStudy.setOrder(element.getOrder());
				infoCaseStudy.setCaseStudyIdInternal(element.getCaseStudyIdInternal());
				caseStudyChoices.add(infoCaseStudy);
			}

			infoCandidacy.setCaseStudyChoices(caseStudyChoices);
			infoCandidacy.setCurricularCourseIdInternal(candidacy.getCurricularCourseIdInternal());
			infoCandidacy.setStudentIdInternal(candidacy.getStudentIdInternal());
			infoCandidacy.setThemeIdInternal(candidacy.getThemeIdInternal());
			infoCandidacy.setModalityIdInternal(candidacy.getModalityIdInternal());
			infoCandidacy.setSeminaryIdInternal(candidacy.getSeminaryIdInternal());
			infoCandidacy.setIdInternal(candidacy.getIdInternal());
		}
		return infoCandidacy;
	}

	/**
	 * Method copyIDepartmentCourse2InfoDepartmentCourse.
	 * @param IDepartmentCourse
	 * @return InfoDepartmentCourse
	 */
	public static InfoDepartmentCourse copyIDepartmentCourse2InfoDepartmentCourse(IDisciplinaDepartamento departmentCourse) {
		InfoDepartmentCourse infoDepartmentCourse = new InfoDepartmentCourse();
		infoDepartmentCourse.setCode(departmentCourse.getSigla());
		infoDepartmentCourse.setName(departmentCourse.getNome());
		//			infoDepartmentCourse.setIdInternal(departmentCourse.getIdInternal());
		infoDepartmentCourse.setInfoDepartment(Cloner.copyIDepartment2InfoDepartment(departmentCourse.getDepartamento()));
		return infoDepartmentCourse;
	}
	/**
	 * Method copyInfoDepartmentCourse2IDepartmentCourse.
	 * @param InfoDepartmentCourse
	 * @return IDepartmentCourse
	 */
	public static IDisciplinaDepartamento copyInfoDepartmentCourse2IDepartmentCourse(InfoDepartmentCourse infoDepartmentCourse) {
		IDisciplinaDepartamento departmentCourse = new DisciplinaDepartamento();
		departmentCourse.setSigla(infoDepartmentCourse.getCode());
		departmentCourse.setNome(infoDepartmentCourse.getName());
		//		departmentCourse.setIdInternal(infoDepartmentCourse.getIdInternal());
		departmentCourse.setDepartamento(Cloner.copyInfoDepartment2IDepartment(infoDepartmentCourse.getInfoDepartment()));
		return departmentCourse;
	}

	public static InfoMetadata copyIMetadata2InfoMetadata(IMetadata metadata) {
		InfoMetadata infoMetadata = new InfoMetadata();
		copyObjectProperties(infoMetadata, metadata);
		InfoExecutionCourse infoExecutionCourse = copyIExecutionCourse2InfoExecutionCourse(metadata.getExecutionCourse());
		infoMetadata.setInfoExecutionCourse(infoExecutionCourse);
		return infoMetadata;
	}

	public static InfoQuestion copyIQuestion2InfoQuestion(IQuestion question) {
		InfoQuestion infoQuestion = new InfoQuestion();
		copyObjectProperties(infoQuestion, question);
		InfoMetadata infoMetadata = copyIMetadata2InfoMetadata(question.getMetadata());
		infoQuestion.setInfoMetadata(infoMetadata);
		return infoQuestion;
	}
	public static InfoTest copyITest2InfoTest(ITest test) {
		InfoTest infoTest = new InfoTest();
		copyObjectProperties(infoTest, test);
		InfoExecutionCourse infoExecutionCourse = copyIExecutionCourse2InfoExecutionCourse(test.getExecutionCourse());
		infoTest.setInfoExecutionCourse(infoExecutionCourse);
		return infoTest;
	}

	public static InfoTestQuestion copyITestQuestion2InfoTestQuestion(ITestQuestion testQuestion) {
		InfoTestQuestion infoTestQuestion = new InfoTestQuestion();
		//copyObjectProperties(infoTestQuestion,testQuestion);
		infoTestQuestion.setIdInternal(testQuestion.getIdInternal());
		infoTestQuestion.setTestQuestionOrder(testQuestion.getTestQuestionOrder());
		infoTestQuestion.setTestQuestionValue(testQuestion.getTestQuestionValue());
		InfoTest infoTest = copyITest2InfoTest(testQuestion.getTest());
		infoTestQuestion.setTest(infoTest);
		InfoQuestion infoQuestion = copyIQuestion2InfoQuestion(testQuestion.getQuestion());
		infoTestQuestion.setQuestion(infoQuestion);
		return infoTestQuestion;
	}

	public static InfoDistributedTest copyIDistributedTest2InfoDistributedTest(IDistributedTest distributedTest) {
		InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
		copyObjectProperties(infoDistributedTest, distributedTest);
		InfoTest infoTest = copyITest2InfoTest(distributedTest.getTest());
		infoDistributedTest.setInfoTest(infoTest);
		return infoDistributedTest;
	}

}