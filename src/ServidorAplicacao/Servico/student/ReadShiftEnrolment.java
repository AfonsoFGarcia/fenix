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
import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudent;
import DataBeans.TypeLessonAndInfoShift;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.Turma;
import Dominio.TurmaTurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

public class ReadShiftEnrolment implements IServico {

  private static ReadShiftEnrolment _servico = new ReadShiftEnrolment();
  /**
   * The singleton access method of this class.
   **/
  public static ReadShiftEnrolment getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private ReadShiftEnrolment() { }

  /**
   * Returns service name */
  public final String getNome() {
    return "ReadShiftEnrolment";
  }
/**
 * :FIXME: Isto ficou depois das altera��es � preciso dar outras voltas... :(
 * @param infoStudent
 * @return Object
 */
  public Object run(InfoStudent infoStudent) {
  	InfoShiftEnrolment infoShiftSignup = null;
    ArrayList infoSignupWithShift = new ArrayList();
	ArrayList infoSignupWithOutShift = new ArrayList();

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      List frequencies = sp.getIFrequentaPersistente().readByStudentNumber(infoStudent.getNumber());
      for(int i = 0; i < frequencies.size(); i++) {
		IFrequenta frequent = (IFrequenta) frequencies.get(i);
		IDisciplinaExecucao de = frequent.getDisciplinaExecucao();
		
	//	InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
		
				
		 
		InfoExecutionCourse ide = Cloner.copyIExecutionCourse2InfoExecutionCourse(de);


	//	ICurso degree = new Curso();
//		degree.setSigla(de.getLicenciaturaExecucao().getCurso().getSigla());

		//ICursoExecucao executionCoure = new CursoExecucao();
//		executionCoure.setAnoLectivo(de.getLicenciaturaExecucao().getAnoLectivo());
//		executionCoure.setCurso(degree);

		IDisciplinaExecucao executionDegree = new DisciplinaExecucao();
		executionDegree.setSigla(de.getSigla());

		ITurno shift1 = new Turno();
		shift1.setDisciplinaExecucao(executionDegree);
		
      	ITurma tempClass = new Turma();
      	// ###################################################################
      	//	As insci��es agora para o segundo semestre s� se vai ler os turnos
      	//  de turmas do segundo semestre. Quando a aplica��o tiver recurso a
      	//  uma especie de calend�rio isto ser� mais din�mico e ter� em conta
      	//  a data actual em que se est� a efectuar a inscri��o. 
      	// ###################################################################
//      	tempClass.setSemestre(new Integer(2));
		
		ITurmaTurno classShift = new TurmaTurno();
		classShift.setTurma(tempClass);
		classShift.setTurno(shift1);

      	List turmaTurnos = sp.getITurmaTurnoPersistente().readByCriteria(classShift);
		List turnos = new ArrayList();
      	for (int j = 0; j < turmaTurnos.size(); j++) {
      		turnos.add(((ITurmaTurno) turmaTurnos.get(j)).getTurno());
      	}


      	if (turnos.isEmpty()) {
			//infoSignupWithOutShift.add(ide);
		} else {
			List pairList = new ArrayList();

			// Determine types of shifts for specifies course
			List typesOfShifts = new ArrayList();
			for(int j = 0; j < turnos.size(); j++) {
				ITurno turno = (ITurno) turnos.get(j);
				if (!(typesOfShifts.contains(turno.getTipo()))) {
					typesOfShifts.add(turno.getTipo());
				}
			}
			// Determine pairs(Shitf,Student)
			for(int k = 0; k < typesOfShifts.size(); k++) {
				ITurno shift = sp.getITurnoAlunoPersistente().readByStudentIdAndShiftType(
				                    infoStudent.getNumber(),
				                    (TipoAula) typesOfShifts.get(k),
				                    ide.getNome());
				InfoShift infoShift = null;
				if (shift != null)
					infoShift = new InfoShift(shift.getNome(),
				                              shift.getTipo(),
				                              shift.getLotacao(),
				                             ide);
				TypeLessonAndInfoShift pair =
					new TypeLessonAndInfoShift(
						(TipoAula) typesOfShifts.get(k),
						infoShift);
				pairList.add(pair);
			}
			InfoCourseExecutionAndListOfTypeLessonAndInfoShift pairDeShiftList =
				new InfoCourseExecutionAndListOfTypeLessonAndInfoShift(
					ide,
					pairList);
			infoSignupWithShift.add(pairDeShiftList);
		}
      }

    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
	infoShiftSignup = new InfoShiftEnrolment(infoSignupWithShift, infoSignupWithOutShift);
    return infoShiftSignup;
  }

}