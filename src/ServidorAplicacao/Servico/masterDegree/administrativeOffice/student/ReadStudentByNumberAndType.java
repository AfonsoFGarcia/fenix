/*
 * LerAula.java
 *
 * Created on December 16th, 2002, 1:58
 */

package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student;

/**
 * Servi�o LerAluno.
 *
 * @author tfc130
 **/

import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;


public class ReadStudentByNumberAndType implements IServico {

  private static ReadStudentByNumberAndType _servico = new ReadStudentByNumberAndType();
  /**
   * The singleton access method of this class.
   **/
  public static ReadStudentByNumberAndType getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private ReadStudentByNumberAndType() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "ReadStudentByNumberAndType";
  }

  // FIXME: We have to read the student by type also !!

  public Object run(Integer number,TipoCurso degreeType) {

    InfoStudent infoStudent = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      //////////////////////////////////////////////////////////////////////////////////////////////////////////
      // Isto n�o � para ficar assim. Est� assim temporariamente at� se saber como � feita de facto a distin��o
      // dos aluno, referente ao tipo, a partir da p�gina de login.
	  //////////////////////////////////////////////////////////////////////////////////////////////////////////
      IStudent student = sp.getIPersistentStudent().readByNumero(number, degreeType);

      if (student != null) { 	
      	infoStudent = Cloner.copyIStudent2InfoStudent(student);
      }
      
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
	
    return infoStudent;
  }

}