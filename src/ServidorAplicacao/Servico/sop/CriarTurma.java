/*
 * CriarTurma.java
 *
 * Created on 25 de Outubro de 2002, 18:34
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o CriarTurma
 *
 * @author tfc130
 **/
import DataBeans.InfoClass;
import DataBeans.util.Cloner;
import Dominio.ITurma;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class CriarTurma implements IServico {

  private static CriarTurma _servico = new CriarTurma();
  /**
   * The singleton access method of this class.
   **/
  public static CriarTurma getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private CriarTurma() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "CriarTurma";
  }

  public Object run(InfoClass infoTurma) {
                        
    ITurma turma = null;
    boolean result = false;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      
      turma = Cloner.copyInfoClass2Class(infoTurma);
      
      IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
      ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();
      
      turma.setExecutionDegree(executionDegreeDAO.readByDegreeCurricularPlanAndExecutionYear(turma.getExecutionDegree().getCurricularPlan(), turma.getExecutionDegree().getExecutionYear()));
      
      
      turma.setExecutionPeriod(executionPeriodDAO.readByNameAndExecutionYear(turma.getExecutionPeriod().getName(), turma.getExecutionPeriod().getExecutionYear()));
      
      
      sp.getITurmaPersistente().lockWrite(turma);
      
      result = true;
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return new Boolean (result);
  }
  
}