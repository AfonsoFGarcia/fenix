/*
 * ISala.java
 *
 * Created on 17 de Outubro de 2002, 16:45
 */

package Dominio;

/**
 *
 * @author  tfc130
 */
import Util.TipoSala;

public interface ISala {
  public String getNome();
  public String getEdificio();
  public Integer getPiso();
  public TipoSala getTipo();
  public Integer getCapacidadeNormal();
  public Integer getCapacidadeExame();

  public void setNome(String nome);
  public void setEdificio(String edificio);
  public void setPiso(Integer piso);
  public void setTipo(TipoSala tipo);
  public void setCapacidadeNormal(Integer capacidadeNormal);
  public void setCapacidadeExame(Integer capacidadeExame); 
}
