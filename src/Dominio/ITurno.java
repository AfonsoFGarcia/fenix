/*
 * ITurno.java
 *
 * Created on 17 de Outubro de 2002, 19:25
 */

package Dominio;

/**
 *
 * @author  tfc130
 */
import java.util.List;

import Util.TipoAula;

public interface ITurno extends IDomainObject {
  public String getNome();
  public TipoAula getTipo();
  public Integer getLotacao();
  public Integer getAvailabilityFinal();
  public IDisciplinaExecucao getDisciplinaExecucao();

  public void setAvailabilityFinal(Integer i);
  public void setNome(String nome);
  public void setTipo(TipoAula tipo);
  public void setLotacao(Integer Lotacao);
  public void setDisciplinaExecucao(IDisciplinaExecucao disciplinaExecucao);
  
  public List getAssociatedTeacherProfessorShipPercentage();
  public void setAssociatedTeacherProfessorShipPercentage(List list);
  
  List getAssociatedLessons();
  void setAssociatedLessons(List lessons);
}
