/*
 * Created on Oct 10, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.domain;

/**
 * Authors : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 * (naat@mega.ist.utl.pt)
 */
public interface IExternalPerson extends IDomainObject {

    public abstract void setKeyPerson(Integer keyPerson);

    public abstract Integer getKeyPerson();

    public abstract void setPerson(IPerson person);

    public abstract IPerson getPerson();

    public abstract void setWorkLocation(IWorkLocation workLocation);

    public abstract IWorkLocation getWorkLocation();
}