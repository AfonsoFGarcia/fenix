/*
 * Created on 22/Dez/2003
 *  
 */
package ServidorAplicacao.Servico.person;

import java.util.HashMap;
import java.util.List;

import DataBeans.InfoObject;
import DataBeans.InfoPerson;
import Dominio.IDomainObject;
import Dominio.IPessoa;
import ServidorAplicacao.Servico.framework.SearchService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;

/**
 * @author T�nia Pous�o
 *  
 */
public class SearchPerson extends SearchService
{
	static private SearchPerson service = new SearchPerson();

	private SearchPerson()
	{

	}

	public static SearchPerson getService()
	{
		return service;
	}

	public String getNome()
	{
		return "SearchPerson";
	}

	protected InfoObject cloneDomainObject(IDomainObject object)
	{
		InfoPerson infoPerson = InfoPerson.newInfoFromDomain((IPessoa) object);
		
		return infoPerson;
	}

	protected List doSearch(HashMap parametersSearch, ISuportePersistente sp)
		throws ExcepcaoPersistencia
	{
		IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();

		String name = (String) parametersSearch.get(new String("name"));
		String email = (String) parametersSearch.get(new String("email"));
		String username = (String) parametersSearch.get(new String("username"));
		String documentIdNumber = (String) parametersSearch.get(new String("documentIdNumber"));

		return persistentPerson.findPersonByNameAndEmailAndUsernameAndDocumentId(name, email, username, documentIdNumber);
	}

}
