package Dominio;

/**
 * @author jorge
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface IPrivilegio {
    public IPessoa getPessoa();
    public String getServico();
        
    public void setPessoa(IPessoa pessoa);
    public void setServico(String servico);
}
