/*
 * ExecutionCourseKeyAndLessonType.java
 *
 * Created on 04 de Novembro de 2002, 18:36
 */

package DataBeans;

/**
 *
 * @author  tfc130
 */
import Util.TipoAula;

public class ExecutionCourseKeyAndLessonType {
  protected TipoAula _tipoAula;
  protected String _sigla;
//  protected + UNIQUE de LicenciaturaExecucao

  public ExecutionCourseKeyAndLessonType() { }

  public ExecutionCourseKeyAndLessonType(TipoAula tipoAula, String sigla) {
    setTipoAula(tipoAula);
    setSigla(sigla);
  }

  public TipoAula getTipoAula() {
    return _tipoAula;
  }
    
  public void setTipoAula(TipoAula tipoAula) {
    _tipoAula = tipoAula;
  }  
  
  public String getSigla() {
    return _sigla;
  }

  public void setSigla(String sigla) {
    _sigla = sigla;
  }

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof ExecutionCourseKeyAndLessonType) {
      ExecutionCourseKeyAndLessonType tipoAulaAndKeyDisciplinaExecucao = (ExecutionCourseKeyAndLessonType)obj;

      resultado = (getTipoAula().equals(tipoAulaAndKeyDisciplinaExecucao.getTipoAula())) &&
                  (getSigla().equals(tipoAulaAndKeyDisciplinaExecucao.getSigla()));
    }

    return resultado;
  }
  
  public String toString() {
    String result = "[TIPOAULAANDKEYDISCIPLINAEXECUCAO";
    result += ", tipo aula=" + _tipoAula;
    result += ", sigla =" + _sigla;
    result += "]";
    return result;
  }

}
