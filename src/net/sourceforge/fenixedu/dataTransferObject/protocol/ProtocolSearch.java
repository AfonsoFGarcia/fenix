package net.sourceforge.fenixedu.dataTransferObject.protocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.domain.protocols.ProtocolHistory;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolActionType;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class ProtocolSearch implements Serializable {

	public enum SearchNationalityType {
		NATIONAL, INTERNATIONAL, WITHOUT_NATIONALITY, COUNTRY;
	}

	private String protocolNumber;

	private Integer year;

	private YearMonthDay beginProtocolBeginDate;

	private YearMonthDay endProtocolBeginDate;

	private YearMonthDay beginProtocolEndDate;

	private YearMonthDay endProtocolEndDate;

	private YearMonthDay beginSignedDate;

	private YearMonthDay endSignedDate;

	private List<ProtocolActionType> protocolActionTypes;

	private String otherProtocolActionTypes;

	private UnitName partnerName;

	private String partnerNameString;

	private Country country;

	private boolean actives;

	private boolean inactives;

	private SearchNationalityType searchNationalityType;

	public ProtocolSearch() {
		super();
		setActives(true);
	}

	public List<Protocol> getSearch() {
		List<Protocol> protocols = new ArrayList<Protocol>();
		for (Protocol protocol : RootDomainObject.getInstance().getProtocols()) {
			if (satisfiedProtocolNumber(protocol)
					&& satisfiesAnyProtocolHistoryDate(getBeginProtocolBeginDate(), getEndProtocolBeginDate(), protocol)
					&& satisfiedDates(getBeginSignedDate(), getEndSignedDate(), protocol.getSignedDate())
					&& satisfiedOtherProtocolActionTypes(protocol) && satiefiedProtocolActionTypes(protocol)
					&& satisfiedProtocolPartner(protocol) && satisfiedNationality(protocol) && satisfiedActivity(protocol)
					&& satisfiedActiveInYear(protocol)) {
				protocols.add(protocol);
			}
		}
		return protocols;
	}

	private boolean satisfiesAnyProtocolHistoryDate(YearMonthDay beginProtocolBeginDate, YearMonthDay endProtocolBeginDate,
			Protocol protocol) {
		for (ProtocolHistory protocolHistory : protocol.getProtocolHistories()) {
			if (satisfiedDates(getBeginProtocolBeginDate(), getEndProtocolBeginDate(), protocolHistory.getBeginDate())
					&& satisfiedDates(getBeginProtocolEndDate(), getEndProtocolEndDate(), protocolHistory.getBeginDate())) {
				return true;
			}
		}
		return false;
	}

	private boolean satisfiedActiveInYear(Protocol protocol) {
		if (getYear() != null) {
			for (ProtocolHistory protocolHistory : protocol.getProtocolHistories()) {
				if (protocolHistory.getEndDate() == null) {
					return protocolHistory.getBeginDate() == null || protocolHistory.getBeginDate().getYear() <= getYear();
				} else {
					return (protocolHistory.getBeginDate() == null || protocolHistory.getBeginDate().getYear() <= getYear())
							&& protocolHistory.getEndDate().getYear() >= getYear();
				}
			}
		}
		return true;
	}

	public boolean satisfiedActivity(Protocol protocol) {
		if ((protocol.isActive() && isActives()) || (isInactives() && !protocol.isActive())) {
			return true;
		}
		return false;
	}

	private boolean satisfiedNationality(Protocol protocol) {
		if (getSearchNationalityType() == null || getSearchNationalityType().equals(SearchNationalityType.COUNTRY)) {
			return satisfiedCountry(protocol);
		}

		if (!hasAnyPartnerWithoutNationality(protocol.getPartners())) {

			if (getSearchNationalityType().equals(SearchNationalityType.NATIONAL)
					&& !hasAnyInternationalPartner(protocol.getPartners())) {
				return true;
			}

			if (getSearchNationalityType().equals(SearchNationalityType.INTERNATIONAL)
					&& hasAnyInternationalPartner(protocol.getPartners())) {
				return true;
			}
		} else if (getSearchNationalityType().equals(SearchNationalityType.WITHOUT_NATIONALITY)) {
			return true;
		}
		return false;
	}

	private boolean hasAnyInternationalPartner(List<Unit> partners) {
		for (Unit partner : partners) {
			if (partner.getCountry() == null) {
				return false;
			}
			if (!partner.getCountry().getName().equalsIgnoreCase(Country.PORTUGAL)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasAnyPartnerWithoutNationality(List<Unit> partners) {
		for (Unit partner : partners) {
			if (partner.getCountry() == null) {
				return true;
			}
		}
		return false;
	}

	private boolean satisfiedCountry(Protocol protocol) {
		if (getCountry() == null) {
			return true;
		}
		for (Unit partner : protocol.getPartners()) {
			if (partner.getCountry() != null && partner.getCountry().getName().equals(getCountry().getName())) {
				return true;
			}
		}
		return false;
	}

	private boolean satiefiedProtocolActionTypes(Protocol protocol) {
		return (getProtocolActionTypes() == null || protocol.getProtocolAction().contains(getProtocolActionTypes()));
	}

	private boolean satisfiedProtocolPartner(Protocol protocol) {
		if (getPartnerName() != null && getPartnerName().getUnit() != null) {
			return protocol.getPartners().contains(getPartnerName().getUnit());
		}
		if (getPartnerName() == null && (!org.apache.commons.lang.StringUtils.isEmpty(getPartnerNameString()))) {
			String[] values = StringNormalizer.normalize(getPartnerNameString()).toLowerCase().split("\\p{Space}+");
			boolean result = false;
			for (Unit unit : protocol.getPartners()) {
				String normalizedValue = StringNormalizer.normalize(unit.getName()).toLowerCase();
				for (String value : values) {
					String part = value;
					if (!normalizedValue.contains(part)) {
						result = false;
						break;
					} else {
						result = true;
					}
				}
				if (result) {
					return result;
				}
			}
			return result;
		}
		return true;
	}

	private boolean satisfiedOtherProtocolActionTypes(Protocol protocol) {
		return org.apache.commons.lang.StringUtils.isEmpty(getOtherProtocolActionTypes())
				|| StringUtils.verifyContainsWithEquality(protocol.getProtocolAction().getOtherTypes(),
						getOtherProtocolActionTypes());
	}

	private boolean satisfiedDates(YearMonthDay beginDate, YearMonthDay endDate, YearMonthDay date) {
		if (beginDate != null && date != null) {
			if (endDate != null) {
				Interval interval = new Interval(beginDate.toDateTimeAtMidnight(), endDate.toDateTimeAtMidnight().plus(1));
				return interval.contains(date.toDateTimeAtMidnight());
			} else {
				return !beginDate.isAfter(date);
			}
		}
		return true;
	}

	private boolean satisfiedProtocolNumber(Protocol protocol) {
		return (org.apache.commons.lang.StringUtils.isEmpty(getProtocolNumber()) || StringUtils.normalize(
				protocol.getProtocolNumber()).indexOf(StringUtils.normalize(getProtocolNumber())) != -1);
	}

	public YearMonthDay getBeginProtocolBeginDate() {
		return beginProtocolBeginDate;
	}

	public void setBeginProtocolBeginDate(YearMonthDay beginProtocolBeginDate) {
		this.beginProtocolBeginDate = beginProtocolBeginDate;
	}

	public YearMonthDay getBeginProtocolEndDate() {
		return beginProtocolEndDate;
	}

	public void setBeginProtocolEndDate(YearMonthDay beginProtocolEndDate) {
		this.beginProtocolEndDate = beginProtocolEndDate;
	}

	public YearMonthDay getBeginSignedDate() {
		return beginSignedDate;
	}

	public void setBeginSignedDate(YearMonthDay beginSignedDate) {
		this.beginSignedDate = beginSignedDate;
	}

	public YearMonthDay getEndProtocolBeginDate() {
		return endProtocolBeginDate;
	}

	public void setEndProtocolBeginDate(YearMonthDay endProtocolBeginDate) {
		this.endProtocolBeginDate = endProtocolBeginDate;
	}

	public YearMonthDay getEndProtocolEndDate() {
		return endProtocolEndDate;
	}

	public void setEndProtocolEndDate(YearMonthDay endProtocolEndDate) {
		this.endProtocolEndDate = endProtocolEndDate;
	}

	public YearMonthDay getEndSignedDate() {
		return endSignedDate;
	}

	public void setEndSignedDate(YearMonthDay endSignedDate) {
		this.endSignedDate = endSignedDate;
	}

	public String getOtherProtocolActionTypes() {
		return otherProtocolActionTypes;
	}

	public void setOtherProtocolActionTypes(String otherProtocolActionTypes) {
		this.otherProtocolActionTypes = otherProtocolActionTypes;
	}

	public UnitName getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(UnitName partnerName) {
		this.partnerName = partnerName;
	}

	public List<ProtocolActionType> getProtocolActionTypes() {
		return protocolActionTypes;
	}

	public void setProtocolActionTypes(List<ProtocolActionType> protocolActionTypes) {
		this.protocolActionTypes = protocolActionTypes;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public String getPartnerNameString() {
		return partnerNameString;
	}

	public void setPartnerNameString(String partnerNameString) {
		this.partnerNameString = partnerNameString;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public boolean isActives() {
		return actives;
	}

	public void setActives(boolean actives) {
		this.actives = actives;
	}

	public boolean isInactives() {
		return inactives;
	}

	public void setInactives(boolean inactives) {
		this.inactives = inactives;
	}

	public SearchNationalityType getSearchNationalityType() {
		return searchNationalityType;
	}

	public void setSearchNationalityType(SearchNationalityType searchNationalityType) {
		this.searchNationalityType = searchNationalityType;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
