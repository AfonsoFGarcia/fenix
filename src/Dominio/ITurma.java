/*
 * ITurma.java
 *
 * Created on 17 de Outubro de 2002, 18:35
 */

package Dominio;

/**
 *
 * @author  tfc130
 */

public interface ITurma {
  public String getNome();
  public Integer getSemestre();
  public Integer getAnoCurricular();
  public ICurso getLicenciatura();  

  public void setNome(String nome);
  public void setSemestre(Integer semestre);
  public void setAnoCurricular(Integer anoCurricular);
  public void setLicenciatura(ICurso licenciatura);    
}
