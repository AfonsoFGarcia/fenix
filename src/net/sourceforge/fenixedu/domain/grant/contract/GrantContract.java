package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.print.attribute.HashPrintRequestAttributeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class GrantContract extends GrantContract_Base {

    private GrantContract() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public GrantContract(GrantOwner grantOwner, Integer contractNumber) {
	this();
	setGrantOwner(grantOwner);
	setContractNumber(contractNumber);
    }

    public void delete() {
	removeGrantCostCenter();
	if (hasGrantInsurance()) {
	    getGrantInsurance().delete();
	}
	removeGrantOwner();
	removeGrantType();
	for (; hasAnyGrantOrientationTeachers(); getGrantOrientationTeachers().get(0).delete())
	    ;
	for (; hasAnyAssociatedGrantContractMovements(); getAssociatedGrantContractMovements().get(0).delete())
	    ;
	for (; hasAnyAssociatedGrantSubsidies(); getAssociatedGrantSubsidies().get(0).delete())
	    ;
	for (; hasAnyContractRegimes(); getContractRegimes().get(0).delete())
	    ;
	removeRootDomainObject();
	deleteDomainObject();
    }

    public Set<GrantSubsidy> findGrantSubsidiesByState(final Integer state) {
	final Set<GrantSubsidy> grantSubsidies = new HashSet<GrantSubsidy>();
	for (final GrantSubsidy grantSubsidy : getAssociatedGrantSubsidiesSet()) {
	    grantSubsidies.add(grantSubsidy);
	}
	return grantSubsidies;
    }

    public List<GrantContractRegime> readGrantContractRegimeByGrantContract() {
	List<GrantContractRegime> grantContractRegimes = new ArrayList();
	grantContractRegimes.addAll(getContractRegimes());
	Collections.sort(grantContractRegimes, new ReverseComparator(GrantContractRegime.BEGIN_DATE_CONTRACT_COMPARATOR));
	return grantContractRegimes;
    }

    public List<GrantContractRegime> readGrantContractRegimeByGrantContractAndState(Integer state) {
	List<GrantContractRegime> grantContractRegimes = new ArrayList();
	for (GrantContractRegime grantContractRegime : getContractRegimes()) {
	    if (grantContractRegime.getState().equals(state)) {
		grantContractRegimes.add(grantContractRegime);
	    }
	}
	Collections.sort(grantContractRegimes, new ReverseComparator(GrantContractRegime.BEGIN_DATE_CONTRACT_COMPARATOR));
	return grantContractRegimes;
    }

    public GrantOrientationTeacher readActualGrantOrientationTeacher() {
	return (!this.getGrantOrientationTeachers().isEmpty()) ? Collections.max(this.getGrantOrientationTeachers(),
		GrantOrientationTeacher.BEGIN_DATE_COMPARATOR) : null;

    }

    public boolean hasActiveRegimes() {
	for (final GrantContractRegime grantContractRegime : this.getContractRegimesSet()) {
	    if (grantContractRegime.isActive()) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasRegimesInPeriod(Date beginContractDate, Date endContractDate) {
	final String dateFormat = "yyyy/MM/dd";
	for (final GrantContractRegime grantContractRegime : this.getContractRegimesSet()) {
	    if (beginContractDate != null
		    && DateFormatUtil.isBefore(dateFormat, grantContractRegime.getDateBeginContract(), beginContractDate)) {
		continue;
	    }
	    if (endContractDate != null
		    && DateFormatUtil.isAfter(dateFormat, grantContractRegime.getDateEndContract(), endContractDate)) {
		continue;
	    }
	    return true;
	}
	return false;
    }

    public LocalDate getRescissionDate() {
	final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd-MM-yyyy");
	if (!StringUtils.isEmpty(getEndContractMotive())) {
	    try {
		LocalDate rescissionDate = dateFormat.parseDateTime(getEndContractMotive().trim()).toLocalDate();
		return rescissionDate;
	    } catch (IllegalArgumentException e) {
	    }
	}
	return null;
    }
}
