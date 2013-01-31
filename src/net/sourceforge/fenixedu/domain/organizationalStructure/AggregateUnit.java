package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class AggregateUnit extends AggregateUnit_Base {

	private AggregateUnit() {
		super();
		super.setType(PartyTypeEnum.AGGREGATE_UNIT);
	}

	public static AggregateUnit createNewAggregateUnit(MultiLanguageString name, String unitNameCard, Integer costCenterCode,
			String acronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, AccountabilityType accountabilityType,
			String webAddress, UnitClassification classification, Boolean canBeResponsibleOfSpaces, Campus campus) {

		AggregateUnit aggregateUnit = new AggregateUnit();
		aggregateUnit.init(name, unitNameCard, costCenterCode, acronym, beginDate, endDate, webAddress, classification, null,
				canBeResponsibleOfSpaces, campus);
		aggregateUnit.addParentUnit(parentUnit, accountabilityType);

		return aggregateUnit;
	}

	@Override
	public void setType(PartyTypeEnum partyTypeEnum) {
		throw new DomainException("unit.impossible.set.type");
	}

	@Override
	public boolean isAggregateUnit() {
		return true;
	}
}
