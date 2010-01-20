package net.sourceforge.fenixedu.domain.contacts;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Country;

import org.apache.commons.lang.StringUtils;

public class PhysicalAddressData implements Serializable {

    private String address;
    private String areaCode;
    private String areaOfAreaCode;
    private String area;
    private String parishOfResidence;
    private String districtSubdivisionOfResidence;
    private String districtOfResidence;

    private Country countryOfResidence;

    public PhysicalAddressData() {
    }

    public PhysicalAddressData(PhysicalAddress address) {
	setAddress(address.getAddress());
	setAreaCode(address.getAreaCode());
	setAreaOfAreaCode(address.getAreaOfAreaCode());
	setArea(address.getArea());
	setParishOfResidence(address.getParishOfResidence());
	setDistrictSubdivisionOfResidence(address.getDistrictSubdivisionOfResidence());
	setDistrictOfResidence(address.getDistrictOfResidence());
	setCountryOfResidence(address.getCountryOfResidence());
    }

    public PhysicalAddressData(final String address, final String areaCode, final String areaOfAreaCode, final String area) {
	this(address, areaCode, areaOfAreaCode, area, null, null, null, null);
    }

    public PhysicalAddressData(final String address, final String areaCode, final String areaOfAreaCode, final String area,
	    final String parishOfResidence, final String districtSubdivisionOfResidence, final String districtOfResidence,
	    final Country countryOfResidence) {

	setAddress(address);
	setAreaCode(areaCode);
	setAreaOfAreaCode(areaOfAreaCode);
	setArea(area);
	setParishOfResidence(parishOfResidence);
	setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
	setDistrictOfResidence(districtOfResidence);
	setCountryOfResidence(countryOfResidence);
    }

    public String getAddress() {
	return address;
    }

    public PhysicalAddressData setAddress(String address) {
	this.address = address;
	return this;
    }

    public String getArea() {
	return area;
    }

    public PhysicalAddressData setArea(String area) {
	this.area = area;
	return this;
    }

    public String getAreaCode() {
	return areaCode;
    }

    public PhysicalAddressData setAreaCode(String areaCode) {
	this.areaCode = areaCode;
	return this;
    }

    public String getAreaOfAreaCode() {
	return areaOfAreaCode;
    }

    public PhysicalAddressData setAreaOfAreaCode(String areaOfAreaCode) {
	this.areaOfAreaCode = areaOfAreaCode;
	return this;
    }

    public String getDistrictOfResidence() {
	return districtOfResidence;
    }

    public PhysicalAddressData setDistrictOfResidence(String districtOfResidence) {
	this.districtOfResidence = districtOfResidence;
	return this;
    }

    public String getDistrictSubdivisionOfResidence() {
	return districtSubdivisionOfResidence;
    }

    public PhysicalAddressData setDistrictSubdivisionOfResidence(String districtSubdivisionOfResidence) {
	this.districtSubdivisionOfResidence = districtSubdivisionOfResidence;
	return this;
    }

    public String getParishOfResidence() {
	return parishOfResidence;
    }

    public PhysicalAddressData setParishOfResidence(String parishOfResidence) {
	this.parishOfResidence = parishOfResidence;
	return this;
    }

    public Country getCountryOfResidence() {
	return this.countryOfResidence;
    }

    public PhysicalAddressData setCountryOfResidence(Country countryOfResidence) {
	this.countryOfResidence = countryOfResidence;
	return this;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof PhysicalAddressData) {
	    PhysicalAddressData data = (PhysicalAddressData) obj;
	    return address.equals(data.getAddress()) && areaCode.equals(data.getAreaCode())
		    && areaOfAreaCode.equals(data.getAreaOfAreaCode()) && area.equals(data.getArea())
		    && parishOfResidence.equals(data.getParishOfResidence())
		    && districtSubdivisionOfResidence.equals(data.getDistrictSubdivisionOfResidence())
		    && districtOfResidence.equals(data.getDistrictOfResidence())
		    && countryOfResidence.equals(data.getCountryOfResidence());
	} else
	    return false;
    }

    public boolean isEmpty() {
	return StringUtils.isEmpty(address) && StringUtils.isEmpty(areaCode) && StringUtils.isEmpty(areaOfAreaCode)
		&& StringUtils.isEmpty(area) && StringUtils.isEmpty(parishOfResidence)
		&& StringUtils.isEmpty(districtSubdivisionOfResidence) && StringUtils.isEmpty(districtOfResidence)
		&& countryOfResidence == null;
    }

}
