/*
 * Created on Jun 9, 2004
 *  
 */
package DataBeans;

import Dominio.IPessoa;

/**
 * @author Jo�o Mota
 *  
 */
public class InfoPersonWithInfoCountry extends InfoPerson {

    public void copyFromDomain(IPessoa person) {
        super.copyFromDomain(person);
        if (person != null) {
            setInfoPais(InfoCountry.newInfoFromDomain(person.getPais()));
        }
    }

    public static InfoPerson newInfoFromDomain(IPessoa person) {
        InfoPersonWithInfoCountry infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPersonWithInfoCountry();
            infoPerson.copyFromDomain(person);
        }
        return infoPerson;
    }
    
    public IPessoa copyToDomain()
    {
        IPessoa person = super.copyToDomain();
        if (person != null)
    	{
            InfoCountry infoCountry = super.getInfoPais();
    		person.setPais(infoCountry.newDomainFromInfo());
    	}
        return person;
    }
    
    public IPessoa newDomainFromInfo()
    {
        return this.copyToDomain();
    }
	
}