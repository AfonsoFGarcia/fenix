/*
 * Created on Aug 2, 2004
 *
 */
package Dominio;

import java.util.List;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IPersonAccount extends IDomainObject {
    public abstract Double getBalance();

    public abstract void setBalance(Double balance);

    public abstract IPessoa getPerson();

    public abstract void setPerson(IPessoa person);

    public abstract List getTransactions();

    public abstract void setTransactions(List transactions);
}