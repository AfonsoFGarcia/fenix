/*
 * ReadShiftEnrolment.java
 *
 * Created on December 20th, 2002, 03:39
 */

package ServidorAplicacao.Servico.student;

/**
 * Service ReadShiftSignup
 *
 * @author tfc130
 **/
import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoCourseExecutionAndListOfTypeLessonAndInfoShift;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.InfoStudent;
import DataBeans.TypeLessonAndInfoShift;
import DataBeans.util.Cloner;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DisciplinaExecucao;
import Dominio.Frequenta;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.StudentCurricularPlan;
import Dominio.Turma;
import Dominio.TurmaTurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.StudentCurricularPlanState;
import Util.TipoAula;

public class ReadOtherCoursesWithShifts implements IServico {

  private static ReadOtherCoursesWithShifts _servico = new ReadOtherCoursesWithShifts();
  /**
   * The singleton access method of this class.
   **/
  public static ReadOtherCoursesWithShifts getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private ReadOtherCoursesWithShifts() { }

  /**
   * Returns service name */
  public final String getNome() {
    return "ReadOtherCoursesWithShifts";
  }


/**
 * 
 * @author tfc130
 * :FIXME: � preciso dar outras voltas... Isto assim ficou mal depois das
 * altera��es
 */
  public Object run(InfoStudent infoStudent) {
	List infoCoursesWithShifts = new ArrayList();
	
    try {
    	IStudent student = Cloner.copyInfoStudent2IStudent(infoStudent);

		IStudentCurricularPlan sCP = new StudentCurricularPlan();
		sCP.setStudent(student);
		sCP.setCurrentState(new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));

		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		List lsCP = sp.getIStudentCurricularPlanPersistente().readByCriteria(sCP);

		if (lsCP == null || lsCP.size() != 1)
			return null;

		sCP = (IStudentCurricularPlan) lsCP.get(0);

		ICurso c = new Curso();
		c.setSigla(sCP.getCourseCurricularPlan().getCurso().getSigla());
		ICursoExecucao cE = new CursoExecucao();
	//	cE.setCurso(c);
		IDisciplinaExecucao dE = new DisciplinaExecucao();
	//	dE.setLicenciaturaExecucao(cE);
		List ldE = sp.getIDisciplinaExecucaoPersistente().readByCriteria(dE);
		
		IFrequenta freq = new Frequenta();
		freq.setAluno(student); 
		List frequencies = sp.getIFrequentaPersistente().readByCriteria(freq);

		for(int j = 0; j < frequencies.size(); j++) {
			freq = (IFrequenta) frequencies.get(j);
			ldE.remove(freq.getDisciplinaExecucao());
		}

		for(int i = 0; i < ldE.size(); i++) {
			IDisciplinaExecucao disciplinaExecucao = (IDisciplinaExecucao) ldE.get(i);
			
			InfoExecutionCourse infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(disciplinaExecucao);
			
//			InfoDegree iD = new InfoDegree();
//			iD.setNome(disciplinaExecucao.getLicenciaturaExecucao().getCurso().getNome());
//			iD.setSigla(disciplinaExecucao.getLicenciaturaExecucao().getCurso().getSigla());
			
//			InfoExecutionDegree iED = new InfoExecutionDegree();
//			iED.setAnoLectivo(disciplinaExecucao.getLicenciaturaExecucao().getAnoLectivo());
//			iED.setInfoLicenciatura(iD);
//			
//			InfoExecutionCourse iEC = new InfoExecutionCourse();
//			iEC.setNome(disciplinaExecucao.getNome());
//			iEC.setSigla(disciplinaExecucao.getSigla());
//			iEC.setPrograma(disciplinaExecucao.getPrograma());
//			iEC.setInfoLicenciaturaExecucao(iED);


//////////			
			ICurso degree = new Curso();
//			degree.setSigla(disciplinaExecucao.getLicenciaturaExecucao().getCurso().getSigla());
			ICursoExecucao executionCoure = new CursoExecucao();
//			executionCoure.setExecutionYear();
//			executionCoure.setCurricularPlan();
			
			
//			executionCoure.setAnoLectivo(disciplinaExecucao.getLicenciaturaExecucao().getAnoLectivo());
//			executionCoure.setCurso(degree);
			IDisciplinaExecucao executionDegree = new DisciplinaExecucao();
			executionDegree.setSigla(disciplinaExecucao.getSigla());
			ITurno shift1 = new Turno();
			shift1.setDisciplinaExecucao(executionDegree);
		
			ITurma tempClass = new Turma();
			// ###################################################################
			//	As insci��es agora para o segundo semestre s� se vai ler os turnos
			//  de turmas do segundo semestre. Quando a aplica��o tiver recurso a
			//  uma especie de calend�rio isto ser� mais din�mico e ter� em conta
			//  a data actual em que se est� a efectuar a inscri��o. 
			// ###################################################################
//			tempClass.setSemestre(new Integer(2));
		
			ITurmaTurno classShift = new TurmaTurno();
			classShift.setTurma(tempClass);
			classShift.setTurno(shift1);

			List turmaTurnos = sp.getITurmaTurnoPersistente().readByCriteria(classShift);
			List turnos = new ArrayList();
			for (int j = 0; j < turmaTurnos.size(); j++) {
				turnos.add(((ITurmaTurno) turmaTurnos.get(j)).getTurno());
			}
//////////

/*			List turnos =
				sp.getITurnoPersistente().readByDisciplinaExecucao(
					iEC.getSigla(),
					iEC.getInfoLicenciaturaExecucao().getAnoLectivo(),
					iEC
						.getInfoLicenciaturaExecucao()
						.getInfoLicenciatura()
						.getSigla());*/



			if (!turnos.isEmpty()) {
				List pairList = new ArrayList();

				// Determine types of shifts for specifies course
				List typesOfShifts = new ArrayList();
				for (int j = 0; j < turnos.size(); j++) {
					ITurno turno = (ITurno) turnos.get(j);
					if (!(typesOfShifts.contains(turno.getTipo()))) {
						typesOfShifts.add(turno.getTipo());
					}
				}
				// Determine pairs(Shitf,Student)
				for (int k = 0; k < typesOfShifts.size(); k++) {
					ITurno shift =
						sp
							.getITurnoAlunoPersistente()
							.readByStudentIdAndShiftType(
							infoStudent.getNumber(),
							(TipoAula) typesOfShifts.get(k),
							infoExecutionCourse.getNome());
					InfoShift infoShift = null;
					if (shift != null)
						infoShift = Cloner.copyIShift2InfoShift(shift);

					TypeLessonAndInfoShift pair =
						new TypeLessonAndInfoShift(
							(TipoAula) typesOfShifts.get(k),
							infoShift);
					pairList.add(pair);
				}
				InfoCourseExecutionAndListOfTypeLessonAndInfoShift pairDeShiftList =
					new InfoCourseExecutionAndListOfTypeLessonAndInfoShift(
						infoExecutionCourse,
						pairList);
				infoCoursesWithShifts.add(pairDeShiftList);
			}
		}

    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }

    return infoCoursesWithShifts;
  }

}