/*
 * EditarAula.java
 *
 * Created on 27 de Outubro de 2002, 19:05
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o EditarAula.
 *
 * @author tfc130
 **/
import DataBeans.InfoLesson;
import DataBeans.InfoLessonServiceResult;
import DataBeans.KeyLesson;
import Dominio.Aula;
import Dominio.IAula;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EditarAula implements IServico {

  private static EditarAula _servico = new EditarAula();
  /**
   * The singleton access method of this class.
   **/
  public static EditarAula getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private EditarAula() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "EditarAula";
  }

  public Object run(KeyLesson aulaAntiga, InfoLesson aulaNova) {

    IAula aula = null;
	InfoLessonServiceResult result = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      
      ISala salaAntiga = sp.getISalaPersistente().readByNome(aulaAntiga.getKeySala().getNomeSala());
      ISala salaNova = sp.getISalaPersistente().readByNome(aulaNova.getInfoSala().getNome());
      aula = sp.getIAulaPersistente().readByDiaSemanaAndInicioAndFimAndSala(aulaAntiga.getDiaSemana(),
                    aulaAntiga.getInicio(), aulaAntiga.getFim(), salaAntiga);
      
	IAula newLesson =
		new Aula(
			aulaNova.getDiaSemana(),
			aulaNova.getInicio(),
			aulaNova.getFim(),
			aulaNova.getTipo(),
			salaNova,
			null);
      
      if (aula != null) {
		result = valid(newLesson);

		if ( result.isSUCESS() ) {
          aula.setDiaSemana(aulaNova.getDiaSemana());
          aula.setInicio(aulaNova.getInicio());
          aula.setFim(aulaNova.getFim());
          aula.setTipo(aulaNova.getTipo());
          aula.setSala(salaNova);
          sp.getIAulaPersistente().lockWrite(aula);
		}
      }
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return result;
  }

  private InfoLessonServiceResult valid(IAula lesson) {
	  InfoLessonServiceResult result = new InfoLessonServiceResult();

	  if ( lesson.getInicio().getTime().getTime() >= lesson.getFim().getTime().getTime() ) {
		  result.setMessageType(InfoLessonServiceResult.INVALID_TIME_INTERVAL);
	  }

	  return result;
  }
  
}