/*
 * Created on 10/11/2003
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.grant.owner;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwnerWithPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import net.sourceforge.fenixedu.presentationTier.Action.grant.utils.SessionConstants;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class SearchGrantOwner implements IService {

	public List run(String name, String IdNumber, IDDocumentType IdType, Integer grantOwnerNumber,
			Boolean onlyGrantOwner, Integer startIndex) throws FenixServiceException,
			ExcepcaoPersistencia {
		ISuportePersistente persistentSupport = null;
		IPessoaPersistente persistentPerson = null;
		IPersistentGrantOwner persistentGrantOwner = null;
		List grantOwnerList = new ArrayList();
		List personList = new ArrayList();
		List infoGrantOwnerList = new ArrayList();
		GrantOwner grantOwner = null;
		Person person = null;

		persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		persistentPerson = persistentSupport.getIPessoaPersistente();
		persistentGrantOwner = persistentSupport.getIPersistentGrantOwner();

		// Search by Grant Owner Number
		if (grantOwnerNumber != null) {
			grantOwner = persistentGrantOwner.readGrantOwnerByNumber(grantOwnerNumber);
			if (grantOwner != null) {
				InfoGrantOwner newInfoGrantOwner = InfoGrantOwnerWithPerson
						.newInfoFromDomain(grantOwner);
				infoGrantOwnerList.add(newInfoGrantOwner);
				return infoGrantOwnerList;
			}
		}
		// Search by ID number and ID type
		if ((IdNumber != null) && (IdType != null)) {
			if (onlyGrantOwner.booleanValue())
				grantOwner = persistentGrantOwner.readGrantOwnerByPersonID(IdNumber, IdType);
			else
				person = persistentPerson.lerPessoaPorNumDocIdETipoDocId(IdNumber, IdType);
		}
		Integer numberOfResultsByName = null;
		// Search by name IF search by ID has failed
		if (person == null && grantOwner == null) {
			if (name != null) {
				Integer tempNumberOfElementInSpan = new Integer(
						SessionConstants.NUMBER_OF_ELEMENTS_IN_SPAN.intValue() - 1);
				if (onlyGrantOwner.booleanValue()) {
					grantOwnerList = persistentGrantOwner.readGrantOwnerByPersonName(name, startIndex,
							tempNumberOfElementInSpan);
					numberOfResultsByName = persistentGrantOwner.countAllGrantOwnerByName(name);
				} else {
					personList = persistentPerson.findPersonByName(name, startIndex,
							tempNumberOfElementInSpan);
					numberOfResultsByName = persistentPerson.countAllPersonByName(name);
				}
			}
		} else if (grantOwner != null) {
			grantOwnerList.add(grantOwner);
		} else {
			personList.add(person);
		}
		if ((personList.size() > 0) && !onlyGrantOwner.booleanValue()) {
			// Get all the grantOwners associated with each person in list
			for (int i = 0; i < personList.size(); i++) {
				InfoGrantOwner infoGrantOwner = new InfoGrantOwner();
				Person newPerson = (Person) personList.get(i);
				grantOwner = persistentGrantOwner.readGrantOwnerByPerson(newPerson.getIdInternal());
				if (grantOwner != null)
					// The person is a GrantOwner
					infoGrantOwner = InfoGrantOwnerWithPerson.newInfoFromDomain(grantOwner);
				else {
					// The person is NOT a GrantOwner
					infoGrantOwner.setPersonInfo(InfoPersonWithInfoCountry.newInfoFromDomain(newPerson));
				}
				infoGrantOwnerList.add(infoGrantOwner);
			}
		} else if ((grantOwnerList.size() > 0) && onlyGrantOwner.booleanValue()) {
			for (int i = 0; i < grantOwnerList.size(); i++) {
				InfoGrantOwner infoGrantOwner = InfoGrantOwnerWithPerson
						.newInfoFromDomain((GrantOwner) grantOwnerList.get(i));
				infoGrantOwnerList.add(infoGrantOwner);
			}
		}

		if (numberOfResultsByName != null) {
			/**
			 * Set an List with: position 0: result size position 1: start index
			 * used position 2: list with results
			 */
			List result = new ArrayList();
			result.add(0, numberOfResultsByName);
			result.add(1, startIndex);
			result.add(2, infoGrantOwnerList);
			return result;
		}
		return infoGrantOwnerList;
	}
}