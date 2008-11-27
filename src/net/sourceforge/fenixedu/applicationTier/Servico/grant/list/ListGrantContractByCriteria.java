package net.sourceforge.fenixedu.applicationTier.Servico.grant.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoListGrantOwnerByOrder;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoSpanByCriteriaListGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.grant.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class ListGrantContractByCriteria extends FenixService {

    /**
     * Query the grant owner by criteria of grant contract
     * 
     * @throws ExcepcaoPersistencia
     * 
     * @returns an array of objects object[0] List of result object[1]
     *          IndoSpanCriteriaListGrantOwner
     */
    @Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
    @Service
    public static Object[] run(InfoSpanByCriteriaListGrantContract infoSpanByCriteriaListGrantOwner)
	    throws FenixServiceException, FenixFilterException, Exception {

	// Read the grant contracts ordered by persistentSupportan
	List<GrantContractRegime> grantContractBySpanAndCriteria = readAllContractsByCriteria(
		propertyOrderBy(infoSpanByCriteriaListGrantOwner.getOrderBy()), infoSpanByCriteriaListGrantOwner
			.getJustActiveContract(), infoSpanByCriteriaListGrantOwner.getJustDesactiveContract(),
		infoSpanByCriteriaListGrantOwner.getBeginContract(), infoSpanByCriteriaListGrantOwner.getEndContract(),
		infoSpanByCriteriaListGrantOwner.getSpanNumber(), SessionConstants.NUMBER_OF_ELEMENTS_IN_SPAN,
		infoSpanByCriteriaListGrantOwner.getGrantTypeId(), infoSpanByCriteriaListGrantOwner.getValidToTheDate());

	List<InfoListGrantOwnerByOrder> listGrantContract = null;

	if (grantContractBySpanAndCriteria != null && grantContractBySpanAndCriteria.size() != 0) {
	    // Construct the info list and add to the result.
	    listGrantContract = new ArrayList<InfoListGrantOwnerByOrder>();

	    for (GrantContractRegime grantContractRegime : grantContractBySpanAndCriteria) {
		convertToInfoListGrantOwnerByOrder(grantContractRegime, infoSpanByCriteriaListGrantOwner, listGrantContract);
	    }
	}

	if (infoSpanByCriteriaListGrantOwner.getTotalElements() == null) {
	    // Setting the search attributes
	    infoSpanByCriteriaListGrantOwner.setTotalElements(countAllByCriteria(infoSpanByCriteriaListGrantOwner
		    .getJustActiveContract(), infoSpanByCriteriaListGrantOwner.getJustDesactiveContract(),
		    infoSpanByCriteriaListGrantOwner.getBeginContract(), infoSpanByCriteriaListGrantOwner.getEndContract(),
		    infoSpanByCriteriaListGrantOwner.getGrantTypeId(), infoSpanByCriteriaListGrantOwner.getValidToTheDate()));
	}
	Object[] result = { listGrantContract, infoSpanByCriteriaListGrantOwner };
	return result;
    }

    /**
     * For each Grant Owner 1- Read all grant contracts that are in the criteria
     * 1.1 - Read The active regime of each contract 1.2 - Read the insurance of
     * each contract 2- Construct the info and put it on the list result
     */
    private static void convertToInfoListGrantOwnerByOrder(GrantContractRegime grantContractRegime,
	    InfoSpanByCriteriaListGrantContract infoSpanByCriteriaListGrantOwner, List<InfoListGrantOwnerByOrder> result) {

	GrantOwner grantOwner = grantContractRegime.getGrantContract().getGrantOwner();
	InfoListGrantOwnerByOrder infoListGrantOwnerByOrder = new InfoListGrantOwnerByOrder(grantOwner);

	infoListGrantOwnerByOrder.setContractNumber(grantContractRegime.getGrantContract().getContractNumber());
	infoListGrantOwnerByOrder.setGrantType(grantContractRegime.getGrantContract().getGrantType().getSigla());

	infoListGrantOwnerByOrder.setBeginContract(grantContractRegime.getDateBeginContract());
	infoListGrantOwnerByOrder.setEndContract(grantContractRegime.getDateEndContract());

	List<GrantSubsidy> grantSubsidyList = grantContractRegime.getGrantContract().getAssociatedGrantSubsidies();
	for (GrantSubsidy grantSubsidy : grantSubsidyList) {
	    infoListGrantOwnerByOrder.setTotalOfGrantPayment(grantSubsidy.getTotalCost());
	    infoListGrantOwnerByOrder.setValueOfGrantPayment(grantSubsidy.getValue());
	    for (GrantPart grantPart : rootDomainObject.getGrantParts()) {
		if (grantSubsidy.equals(grantPart.getGrantSubsidy())) {
		    if (grantPart.getGrantPaymentEntity() != null) {
			infoListGrantOwnerByOrder.setInsurancePaymentEntity(grantPart.getGrantPaymentEntity().getNumber());
			infoListGrantOwnerByOrder.setNumberPaymentEntity(grantPart.getGrantPaymentEntity().getNumber());
			infoListGrantOwnerByOrder.setDesignation(grantPart.getGrantPaymentEntity().getDesignation());
		    }
		}
	    }
	}

	result.add(infoListGrantOwnerByOrder);
    }

    /*
     * Returns the order string to add to the criteria
     */
    private static String propertyOrderBy(String orderBy) {
	String result = null;
	if (orderBy.equals("orderByGrantOwnerNumber")) {
	    result = "grantOwner.number";
	} else if (orderBy.equals("orderByGrantContractNumber")) {
	    result = "contractNumber";
	} else if (orderBy.equals("orderByFirstName")) {
	    result = "grantOwner.person.name";
	} else if (orderBy.equals("orderByGrantType")) {
	    result = "grantType.sigla";
	} else if (orderBy.equals("orderByDateBeginContract")) {
	    result = "contractRegimes.dateBeginContract";
	} else if (orderBy.equals("orderByDateEndContract")) {
	    result = "contractRegimes.dateEndContract";
	}
	return result;
    }

    public static List<GrantContractRegime> readAllContractsByCriteria(String orderBy, Boolean justActiveContracts,
	    Boolean justDesactiveContracts, Date dateBeginContract, Date dateEndContract, Integer spanNumber,
	    Integer numberOfElementsInSpan, Integer grantTypeId, Date validToTheDate) throws FenixFilterException,
	    FenixServiceException, Exception {

	List<GrantContractRegime> result = new ArrayList<GrantContractRegime>();
	Date ToTheDate = null;

	List<GrantContractRegime> grantContractRegimes = new ArrayList<GrantContractRegime>();
	grantContractRegimes.addAll(rootDomainObject.getGrantContractRegimes());
	List<GrantContractRegime> grantList = new ArrayList<GrantContractRegime>(grantContractRegimes);

	for (GrantContractRegime regime : grantContractRegimes) {
	    final GrantContract grantContract = regime.getGrantContract();
	    if (grantContract == null) {
		grantList.remove(regime);
	    }
	}
	ComparatorChain comparatorChain = new ComparatorChain(new BeanComparator("grantContract.grantOwner.number"), true);
	Collections.sort(grantList, comparatorChain);
	Collections.reverse(grantList);
	for (final GrantContractRegime grantContractRegime : (grantList)) {
	    final GrantContract grantContract = grantContractRegime.getGrantContract();
	    if (grantContract == null) {
		continue;
	    }

	    if ((validToTheDate == null || validToTheDate.equals(""))
		    && (dateBeginContract == null || dateBeginContract.equals(""))
		    && (dateEndContract == null || dateEndContract.equals(""))) {
		if (justActiveContracts != null && justActiveContracts.booleanValue()) {
		    if (grantContractRegime.getGrantContract().getEndContractMotive() != null
			    && !grantContractRegime.getGrantContract().getEndContractMotive().equals("")) {
			continue;
		    }
		    if (!grantContractRegime.getContractRegimeActive()) {
			continue;
		    }
		}
		if (justDesactiveContracts != null && justDesactiveContracts.booleanValue()) {
		    if (grantContractRegime.getContractRegimeActive()
			    && (grantContractRegime.getGrantContract().getEndContractMotive() == null || grantContractRegime
				    .getGrantContract().getEndContractMotive().equals(""))) {
			continue;
		    }
		}
	    }

	    if (validToTheDate != null) {
		if (grantContractRegime.getDateBeginContract() == null || grantContractRegime.getDateEndContract() == null) {
		    continue;
		}
		if (DateFormatUtil.isBefore("yyyy-MM-dd", grantContractRegime.getDateEndContract(), validToTheDate)) {
		    continue;
		}
		if (DateFormatUtil.isAfter("yyyy-MM-dd", grantContractRegime.getDateBeginContract(), validToTheDate)) {
		    continue;
		}
		if (grantContractRegime.getGrantContract().getEndContractMotive() != null
			&& !grantContractRegime.getGrantContract().getEndContractMotive().equals("")) {
		    continue;
		}

	    }

	    if ((dateBeginContract != null && !dateBeginContract.equals(""))
		    && (dateEndContract != null && !dateEndContract.equals(""))) {

		if (grantContractRegime.getDateBeginContract() == null || grantContractRegime.getDateEndContract() == null) {
		    continue;
		}

		if (!grantContractRegime.belongsToPeriod(dateBeginContract, dateEndContract)) {
		    continue;
		}

		if (justActiveContracts != null && justActiveContracts.booleanValue()) {
		    if (!grantContractRegime.getState().equals(Integer.valueOf(1))) {
			continue;
		    }
		    if (grantContractRegime.getGrantContract().getEndContractMotive() != null
			    && !grantContractRegime.getGrantContract().getEndContractMotive().equals("")) {
			continue;
		    }
		}

		if (justDesactiveContracts != null && justDesactiveContracts.booleanValue()) {
		    if (grantContractRegime.getState().equals(Integer.valueOf(1))
			    && (grantContractRegime.getGrantContract().getEndContractMotive() == null || grantContractRegime
				    .getGrantContract().getEndContractMotive().equals(""))) {
			continue;
		    }

		}
	    }
	    if (grantTypeId != null) {
		if (!grantContractRegime.getGrantContract().getGrantType().getIdInternal().equals(grantTypeId)) {
		    continue;
		}
	    }
	    result.add(grantContractRegime);
	}

	int begin = (spanNumber - 1) * numberOfElementsInSpan;
	int end = begin + numberOfElementsInSpan;

	return result.subList(begin, Math.min(end, result.size()));
    }

    public static List<GrantContract> readBySpan(Integer spanNumber, Integer numberOfElementsInSpan,
	    List<GrantContract> grantContract) {
	List<GrantContract> result = new ArrayList<GrantContract>();
	Iterator iter = grantContract.iterator();

	int begin = (spanNumber.intValue() - 1) * numberOfElementsInSpan.intValue();
	int end = begin + numberOfElementsInSpan.intValue();
	if (begin != 0) {
	    for (int j = 0; j < (begin - 1) && iter.hasNext(); j++) {
		iter.next();
	    }
	}

	for (int i = begin; i < end && iter.hasNext(); i++) {
	    GrantContract grantContract1 = (GrantContract) iter.next();

	    result.add(grantContract1);
	}

	return result;
    }

    public static Integer countAllByCriteria(Boolean justActiveContracts, Boolean justDesactiveContracts, Date dateBeginContract,
	    Date dateEndContract, Integer grantTypeId, Date validToTheDate) throws FenixServiceException, FenixFilterException {
	Integer result = Integer.valueOf(0);

	List<GrantContractRegime> grantContractRegimes = new ArrayList<GrantContractRegime>();
	grantContractRegimes.addAll(rootDomainObject.getGrantContractRegimes());
	List<GrantContractRegime> grantList = new ArrayList<GrantContractRegime>(grantContractRegimes);

	for (GrantContractRegime regime : grantContractRegimes) {
	    final GrantContract grantContract = regime.getGrantContract();
	    if (grantContract == null) {
		grantList.remove(regime);
	    }
	}
	Collections.sort(grantList, new BeanComparator("grantContract.grantOwner.number"));
	Collections.reverse(grantList);
	for (final GrantContractRegime grantContractRegime : (grantList)) {
	    final GrantContract grantContract = grantContractRegime.getGrantContract();

	    if (grantContract == null) {
		continue;
	    }

	    if ((validToTheDate == null || validToTheDate.equals(""))
		    && (dateBeginContract == null || dateBeginContract.equals(""))
		    && (dateEndContract == null || dateEndContract.equals(""))) {
		if (justActiveContracts != null && justActiveContracts.booleanValue()) {

		    if (grantContractRegime.getGrantContract().getEndContractMotive() != null
			    && !grantContractRegime.getGrantContract().getEndContractMotive().equals("")) {
			continue;
		    }
		    if (!grantContractRegime.getContractRegimeActive()) {
			continue;
		    }
		}
		if (justDesactiveContracts != null && justDesactiveContracts.booleanValue()) {

		    if (grantContractRegime.getContractRegimeActive()
			    && (grantContractRegime.getGrantContract().getEndContractMotive() == null || grantContractRegime
				    .getGrantContract().getEndContractMotive().equals(""))) {
			continue;
		    }
		}
	    }

	    if (validToTheDate != null) {

		if (grantContractRegime.getDateBeginContract() == null || grantContractRegime.getDateEndContract() == null) {
		    continue;
		}
		if (DateFormatUtil.isBefore("yyyy-MM-dd", grantContractRegime.getDateEndContract(), validToTheDate)) {
		    continue;
		}
		if (DateFormatUtil.isAfter("yyyy-MM-dd", grantContractRegime.getDateBeginContract(), validToTheDate)) {
		    continue;
		}
		if (grantContractRegime.getGrantContract().getEndContractMotive() != null
			&& !grantContractRegime.getGrantContract().getEndContractMotive().equals("")) {
		    continue;
		}

	    }

	    if ((dateBeginContract != null && !dateBeginContract.equals(""))
		    && (dateEndContract != null && !dateEndContract.equals(""))) {

		if (grantContractRegime.getDateBeginContract() == null || grantContractRegime.getDateEndContract() == null) {
		    continue;
		}

		if (!grantContractRegime.belongsToPeriod(dateBeginContract, dateEndContract)) {
		    continue;
		}

		if (justActiveContracts != null && justActiveContracts.booleanValue()) {
		    if (!grantContractRegime.getState().equals(Integer.valueOf(1))) {
			continue;
		    }
		    if (grantContractRegime.getGrantContract().getEndContractMotive() != null
			    && !grantContractRegime.getGrantContract().getEndContractMotive().equals("")) {
			continue;
		    }
		}

		if (justDesactiveContracts != null && justDesactiveContracts.booleanValue()) {
		    if (grantContractRegime.getState().equals(Integer.valueOf(1))
			    && (grantContractRegime.getGrantContract().getEndContractMotive() == null || grantContractRegime
				    .getGrantContract().getEndContractMotive().equals(""))) {
			continue;
		    }

		}

	    }

	    if (grantTypeId != null) {
		if (!grantContractRegime.getGrantContract().getGrantType().getIdInternal().equals(grantTypeId)) {
		    continue;
		}
	    }

	    result++;
	}
	return result;
    }

}