package ServidorPersistenteJDBC;

import java.util.ArrayList;

import Dominio.Modalidade;

/**
 *
 * @author  Fernanda Quit�rio e Tania Pous�o
 */
public interface IModalidadePersistente {
  public boolean alterarModalidade(Modalidade modalidade);
  public boolean apagarModalidade(String designacao);
  public boolean escreverModalidade(Modalidade modalidade);
  public Modalidade lerModalidade(String designacao);
  public Modalidade lerModalidade(int codigoInterno);
  public ArrayList lerModalidades();    
}