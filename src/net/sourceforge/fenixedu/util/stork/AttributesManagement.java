package net.sourceforge.fenixedu.util.stork;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.StorkAttributeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.StorkAttributesList;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.YearMonthDay;

public class AttributesManagement {

    private Map<StorkAttributeType, Attribute> attributes;


    public static final String STORK_RETURN_CODE_OK = "OK";

    public AttributesManagement(Map<StorkAttributeType, Attribute> attributes) {
	this.attributes = attributes;
    }
    
    public AttributesManagement(String attrList) {
	this.attributes = buildStorkAttributes(attrList);
    }

    /* ADDRESS */

    public String getTextAddress() {
	Attribute attribute = attributes.get(StorkAttributeType.STORK_TEXT_RESIDENCE_ADDRESS);

	if (attribute == null) {
	    return null;
	}

	return attribute.getSemanticValue();
    }

    /* END OF ADDRESS */

    /* PERSONAL DATA */

    public Gender getGender() {
	Attribute attribute = attributes.get(StorkAttributeType.STORK_GENDER);

	if (attribute == null) {
	    return null;
	}

	if (attribute.getSemanticValue() == null) {
	    return null;
	}

	if ("M".equals(attribute.getSemanticValue())) {
	    return Gender.MALE;
	} else if ("F".equals(attribute.getSemanticValue())) {
	    return Gender.FEMALE;
	}

	return null;
    }

    public YearMonthDay getBirthDate() {
	Attribute attr = attributes.get(StorkAttributeType.STORK_BIRTHDATE);
	String birthDateText = attr != null ? attr.getSemanticValue() : null;

	if (birthDateText == null || !birthDateText.matches("\\d{2}-\\d{2}-\\d{4}")) {
	    return null;
	}

	String[] compounds = birthDateText.split("-");
	return new YearMonthDay(Integer.valueOf(compounds[2]), Integer.valueOf(compounds[1]), Integer.valueOf(compounds[0]));
    }

    /* NAMES */

    public String getStorkName() {
	Attribute attr = attributes.get(StorkAttributeType.STORK_NAME);
	return attr != null ? attr.getSemanticValue() : null;
    }

    public String getStorkSurname() {
	Attribute attr = attributes.get(StorkAttributeType.STORK_SURNAME);
	return attr != null ? attr.getSemanticValue() : null;
    }

    public String getStorkFamilyName() {
	Attribute attr = attributes.get(StorkAttributeType.STORK_FAMILY_NAME);
	return attr != null ? attr.getSemanticValue() : null;
    }

    public String getStorkAdoptedName() {
	Attribute attr = attributes.get(StorkAttributeType.STORK_ADOPTED_FAMILY_NAME);
	return attr != null ? attr.getSemanticValue() : null;
    }

    public String getStorkFullname() {
	String name = getStorkName();
	String surname = getStorkSurname();
	String familyName = getStorkFamilyName();
	String adoptedName = getStorkAdoptedName();

	StringBuilder fullNameBuilder = new StringBuilder();

	if (!StringUtils.isEmpty(name)) {
	    fullNameBuilder.append(name);
	}

	if (!StringUtils.isEmpty(surname)) {
	    fullNameBuilder.append(" ").append(surname);
	} else if (!StringUtils.isEmpty(familyName)) {
	    fullNameBuilder.append(" ").append(familyName);
	} else if (!StringUtils.isEmpty(adoptedName)) {
	    fullNameBuilder.append(" ").append(adoptedName);
	}

	return fullNameBuilder.toString().trim();
    }

    /* END OF NAMES */

    /* COUNTRIES */
    public Country getStorkNationality() {
	Attribute attr = attributes.get(StorkAttributeType.STORK_NATIONALITY);
	String nationalityCode = attr != null ? attr.getSemanticValue() : null;

	if (nationalityCode == null || !nationalityCode.matches("\\w{2,3}")) {
	    return null;
	}

	return nationalityCode.length() == 2 ? Country.readByTwoLetterCode(nationalityCode) : Country
		.readByThreeLetterCode(nationalityCode);
    }

    public Country getStorkCountryOfBirth() {
	Attribute attr = attributes.get(StorkAttributeType.STORK_COUNTRY_OF_BIRTH);
	String countryOfBirthCode = attr != null ? attr.getSemanticValue() : null;

	if (countryOfBirthCode == null || !countryOfBirthCode.matches("\\w{2,3}")) {
	    return null;
	}

	return countryOfBirthCode.length() == 2 ? Country.readByTwoLetterCode(countryOfBirthCode) : Country
		.readByThreeLetterCode(countryOfBirthCode);
    }

    /* END OF COUNTRIES */

    /* SERVICE RETURN CODE AND ERROR MESSAGES */
    public String getStorkReturnCode() {
	Attribute attr = attributes.get(StorkAttributeType.STORK_RETURN_CODE);
	return attr != null ? attr.getSemanticValue() : null;
    }

    public String getStorkErrorCode() {
	Attribute attr = attributes.get(StorkAttributeType.STORK_ERROR_CODE);
	return attr != null ? attr.getSemanticValue() : null;
    }

    public String getStorkErrorMessage() {
	Attribute attr = attributes.get(StorkAttributeType.STORK_ERROR_MESSAGE);
	return attr != null ? attr.getSemanticValue() : null;
    }

    /* END OF SERVICE RETURN CODE AND ERROR MESSAGES */

    /* CONTACTS */

    public String getStorkPhoneContact() {
	Attribute attr = attributes.get(StorkAttributeType.STORK_PHONE_CONTACT);
	return attr != null ? attr.getSemanticValue() : null;
    }

    public String getEmail() {
	Attribute attr = attributes.get(StorkAttributeType.STORK_EMAIL);
	return attr != null ? attr.getSemanticValue() : null;
    }

    /* END OF CONTACTS */

    /* IDENTIFICATION */

    public String getIdentificationNumber() {
	String eIdentifier = getEIdentifier();
	return eIdentifier.substring(6);
    }

    public String getEIdentifier() {
	Attribute attribute = attributes.get(StorkAttributeType.STORK_EIDENTIFIER);

	if (attribute == null) {
	    return null;
	}

	return attribute.getSemanticValue();
    }

    /* END OF IDENTIFICATION */

    private Map<StorkAttributeType, Attribute> buildStorkAttributes(String attrList) {
	StringTokenizer st = new StringTokenizer(attrList, ";");
	Map<StorkAttributeType, Attribute> attributes = new HashMap<StorkAttributeType, Attribute>();

	int i = 1;
	while (st.hasMoreTokens()) {
	    String[] params = st.nextToken().split("=");

	    if (params.length < 2) {
		throw new DomainException(String.format("error.erasmus.stork.attributes.list.invalid - %s", st.nextToken()));
	    }

	    String attrName = params[0];
	    String value = null;
	    if (StorkAttributeType.STORK_RETURN_CODE.getStorkName().equals(attrName)) {
		value = params[1].equals("null") ? null : params[1];
	    } else {
		value = params[1].equals("null") ? null : params[1].substring(1, params[1].length() - 1);
	    }

	    attributes.put(StorkAttributeType.getTypeFromStorkName(attrName), new Attribute(i++, StorkAttributeType
		    .getTypeFromStorkName(attrName), false, value));
	}

	return attributes;
    }

    public StorkAttributesList getStorkAttributesList() {
	Set<StorkAttributeType> attrList = new HashSet<StorkAttributeType>();

	for (StorkAttributeType type : attributes.keySet()) {
	    Attribute attr = attributes.get(type);

	    if (!StringUtils.isEmpty(attr.getSemanticValue())) {
		attrList.add(type);
	    }
	}

	return new StorkAttributesList(attrList);
    }

}
