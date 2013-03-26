package net.sourceforge.fenixedu.domain.grant.owner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class GrantOwner extends GrantOwner_Base {

    final static Comparator<GrantOwner> NUMBER_COMPARATOR = new BeanComparator("number");

    public GrantOwner() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public GrantContract readGrantContractByNumber(final Integer contractNumber) {
        for (final GrantContract grantContract : this.getGrantContractsSet()) {
            if (grantContract.getContractNumber().equals(contractNumber)) {
                return grantContract;
            }
        }
        return null;
    }

    public GrantContract readGrantContractWithMaximumContractNumber() {
        List<GrantContract> grantContracts = this.getGrantContracts();
        return (!grantContracts.isEmpty()) ? Collections.max(this.getGrantContractsSet(), new Comparator<GrantContract>() {
            @Override
            public int compare(GrantContract o1, GrantContract o2) {
                return o1.getContractNumber().compareTo(o2.getContractNumber());
            }
        }) : null;
    }

    public static Integer readMaxGrantOwnerNumber() {
        List<GrantOwner> grantOwners = RootDomainObject.getInstance().getGrantOwners();
        return (!grantOwners.isEmpty()) ? Collections.max(grantOwners, NUMBER_COMPARATOR).getNumber() : null;
    }

    public static GrantOwner readGrantOwnerByNumber(Integer grantOwnerNumber) {
        for (final GrantOwner grantOwner : RootDomainObject.getInstance().getGrantOwners()) {
            if (grantOwner.getNumber().equals(grantOwnerNumber)) {
                return grantOwner;
            }
        }
        return null;
    }

    public static Integer countAllGrantOwnerByName(String name) {
        return readAllGrantOwnersByName(name).size();
    }

    public static List<GrantOwner> readGrantOwnerByName(String personName, Integer startIndex, Integer numberOfElementsInSpan) {
        List<GrantOwner> grantOwners = readAllGrantOwnersByName(personName);
        if (startIndex != null && numberOfElementsInSpan != null && !grantOwners.isEmpty()) {
            int finalIndex = Math.min(grantOwners.size(), startIndex + numberOfElementsInSpan);
            grantOwners.subList(startIndex, finalIndex);
        }
        return grantOwners;
    }

    private static List<GrantOwner> readAllGrantOwnersByName(String name) {
        List<GrantOwner> grantOwners = new ArrayList();
        for (final GrantOwner grantOwner : RootDomainObject.getInstance().getGrantOwnersSet()) {
            final Person person = grantOwner.getPerson();
            if (person != null && StringUtils.verifyContainsWithEquality(person.getName(), name)) {
                grantOwners.add(person.getGrantOwner());
            }
        }
        return grantOwners;
    }

    public static Integer countAllByCriteria(Boolean justActiveContracts, Boolean justDesactiveContracts, Date dateBeginContract,
            Date dateEndContract, GrantType grantType) {

        Date currentDate = Calendar.getInstance().getTime();
        int counter = 0;
        boolean exit = false;

        for (GrantOwner grantOwner : RootDomainObject.getInstance().getGrantOwners()) {
            for (GrantContract grantContract : grantOwner.getGrantContracts()) {
                if (grantType != null && grantContract.getGrantType().equals(grantType)) {
                    for (GrantContractRegime grantContractRegime : grantContract.getContractRegimes()) {
                        if (justActiveContracts != null && justActiveContracts.booleanValue()
                                && grantContractRegime.getDateEndContract().before(currentDate)
                                && !grantContract.getEndContractMotive().equals("")) {
                            continue;
                        }
                        if (justDesactiveContracts != null && justDesactiveContracts.booleanValue()
                                && grantContractRegime.getDateEndContract().after(currentDate)
                                && grantContract.getEndContractMotive().equals("")) {
                            continue;
                        }
                        if (dateBeginContract != null && grantContractRegime.getDateBeginContract().before(dateBeginContract)) {
                            continue;
                        }
                        if (dateEndContract != null && grantContractRegime.getDateEndContract().after(dateEndContract)) {
                            continue;
                        }
                        counter++;
                        exit = true;
                        break;
                    }
                    if (exit) {
                        exit = false;
                        break;
                    }
                }
            }
        }
        return counter;
    }

    public boolean hasCurrentContract() {
        for (GrantContract grantContract : getGrantContracts()) {
            if (grantContract.getRescissionDate() == null && grantContract.hasActiveRegimes()) {
                return true;
            }
        }
        return false;
    }

    public boolean isActive() {
        for (GrantContract grantContract : getGrantContracts()) {
            if (grantContract.getRescissionDate() == null && grantContract.hasActiveRegimes()) {
                return true;
            }
        }
        return false;
    }

    public void delete() {
        if (!canBeDeleted()) {
            throw new DomainException("error.GrantOwner.cannot.be.deleted");
        }
        removePerson();
        removeRootDomainObject();
        deleteDomainObject();
    }

    private boolean canBeDeleted() {
        return !hasAnyGrantContracts();
    }

    public GrantContract getCurrentContract() {
        final YearMonthDay today = new YearMonthDay();
        for (GrantContract grantContract : getGrantContracts()) {
            if (grantContract.getRescissionDate() == null && grantContract.hasActiveRegimes()) {
                return grantContract;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Date getDateSendCGD() {
        org.joda.time.YearMonthDay ymd = getDateSendCGDYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setDateSendCGD(java.util.Date date) {
        if (date == null) {
            setDateSendCGDYearMonthDay(null);
        } else {
            setDateSendCGDYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

}
