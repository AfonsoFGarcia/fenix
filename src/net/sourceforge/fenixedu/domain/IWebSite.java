package net.sourceforge.fenixedu.domain;

/**
 * @author Fernanda Quit�rio 23/09/2003
 *  
 */
public interface IWebSite extends IDomainObject {
    public String getName();

    public void setName(String name);

    public String getMail();

    public void setMail(String mail);

    public String getStyle();

    public void setStyle(String style);

    public String toString();

}