/*
 * Created on Nov 21, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.manager.organizationalStructureManagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitClassification;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class OrganizationalStructureBackingBean extends FenixBackingBean {

    
    private String unitName, unitCostCenter, unitTypeName, unitBeginDate, unitEndDate, unitAcronym, administrativeOfficeID;

    private String functionName, functionTypeName, functionBeginDate, functionEndDate, unitWebAddress, unitRelationTypeValue;

    private String listingTypeValueToUnits, listingTypeValueToFunctions, departmentID, degreeID, unitClassificationName, campusID;

    private String functionNameEn, unitNameEn;
    
    private Integer principalFunctionID;

    private Integer accountabilityID;

    private Unit unit, chooseUnit;

    private Function function;

    private HtmlInputHidden unitIDHidden, chooseUnitIDHidden, functionIDHidden, listingTypeValueToFunctionsHidden, listingTypeValueToUnitsHidden;

    private HashMap<Integer, String> unitRelationsAccountabilityTypes;

    private Boolean toRemoveParentUnit, viewExternalUnits, institutionUnit, externalInstitutionUnit, earthUnit, canBeResponsibleOfSpaces;
    
    private Boolean viewUnitsWithoutParents;
    

    
    public OrganizationalStructureBackingBean() {
	if (!StringUtils.isEmpty(getRequestParameter("unitID"))) {
	    getUnitIDHidden().setValue(Integer.valueOf(getRequestParameter("unitID").toString()));
	}
	if (!StringUtils.isEmpty(getRequestParameter("chooseUnitID"))) {
	    getChooseUnitIDHidden().setValue(Integer.valueOf(getRequestParameter("chooseUnitID").toString()));
	}
	if (!StringUtils.isEmpty(getRequestParameter("functionID"))) {
	    getFunctionIDHidden().setValue(Integer.valueOf(getRequestParameter("functionID").toString()));
	}
	if (!StringUtils.isEmpty(getRequestParameter("principalFunctionID"))) {
	    this.principalFunctionID = Integer.valueOf(getRequestParameter("principalFunctionID").toString());
	}
	if (!StringUtils.isEmpty(getRequestParameter("accountabilityID"))) {
	    this.accountabilityID = Integer.valueOf(getRequestParameter("accountabilityID").toString());
	}
	if (!StringUtils.isEmpty(getRequestParameter("isToRemoveParentUnit"))) {
	    this.toRemoveParentUnit = Boolean.valueOf(getRequestParameter("isToRemoveParentUnit").toString());
	} else {
	    this.toRemoveParentUnit = false;
	}
	if (getViewExternalUnits() == null) {
	    setViewExternalUnits(Boolean.FALSE);
	}
	if (getViewUnitsWithoutParents() == null) {
	    setViewUnitsWithoutParents(Boolean.FALSE);
	}
    }
       
    public List<Unit> getAllSubUnits() throws FenixFilterException, FenixServiceException {
	YearMonthDay currentDate = new YearMonthDay();
	boolean active = this.getListingTypeValueToUnitsHidden().getValue().toString().equals("0");
	return getSubUnits(active, this.getUnit(), currentDate);
    }

    public List<Function> getAllNonInherentFunctions() throws FenixFilterException, FenixServiceException {
	List<Function> allNonInherentFunctions = new ArrayList<Function>();
	YearMonthDay currentDate = new YearMonthDay();
	for (Function function : this.getUnit().getFunctions()) {
	    if (!function.isInherentFunction()
		    && ((this.getListingTypeValueToFunctionsHidden().getValue().toString().equals("0") && function
			    .isActive(currentDate)) || (this.getListingTypeValueToFunctionsHidden()
			    .getValue().toString().equals("1") && !function.isActive(currentDate)))) {
		allNonInherentFunctions.add(function);
	    }
	}
	return allNonInherentFunctions;
    }

    public List<Function> getAllInherentFunctions() throws FenixFilterException, FenixServiceException {
	List<Function> allInherentFunctions = new ArrayList<Function>();
	YearMonthDay currentDate = new YearMonthDay();
	for (Function function : this.getUnit().getFunctions()) {
	    if (function.isInherentFunction()
		    && ((this.getListingTypeValueToFunctionsHidden().getValue().toString().equals("0") && function
			    .isActive(currentDate)) || (this.getListingTypeValueToFunctionsHidden()
			    .getValue().toString().equals("1") && !function.isActive(currentDate)))) {
		allInherentFunctions.add(function);
	    }
	}
	return allInherentFunctions;
    }

    public String getAllUnitsToChooseParentUnit() throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {
	StringBuilder buffer = new StringBuilder();
	YearMonthDay currentDate = new YearMonthDay();
	if (this.getUnit().isNoOfficialExternal()) {
	    buffer.append("<ul class='padding1 nobullet'>");
	    getSubUnitsListToChooseParentUnit(UnitUtils.readExternalInstitutionUnit(), null, buffer, currentDate);
	    closeULTag(buffer);
	} else {	  
	    Unit earthUnit = UnitUtils.readEarthUnit();	     
	    if(!this.getUnit().equals(earthUnit)) {
		getSubUnitsListToChooseParentUnit(earthUnit, null, buffer, currentDate);
	    }
	    closeULTag(buffer);
	}
	return buffer.toString();
    }

    private void getSubUnitsListToChooseParentUnit(Unit parentUnit, Unit parentUnitParent,
	    StringBuilder buffer, YearMonthDay currentDate) throws FenixFilterException,
	    FenixServiceException {

	openLITag(buffer);

	List<Unit> subUnits = null;
	if (this.getUnit().isActive(currentDate)) {
	    subUnits = getSubUnits(true, parentUnit, currentDate);
	} else {
	    subUnits = getSubUnits(false, parentUnit, currentDate);
	}

	if (!subUnits.isEmpty()) {	    	    
	    putImage(parentUnit, buffer, parentUnitParent);
	}

	buffer.append("<a href=\"").append(getContextPath()).append(
		"/manager/organizationalStructureManagament/").append("chooseParentUnit.faces?").append(
		"unitID=").append(this.getUnit().getIdInternal()).append("&chooseUnitID=").append(
		parentUnit.getIdInternal()).append("\">").append(parentUnit.getNameWithAcronym())
		.append("</a>").append("</li>");

	if (!subUnits.isEmpty()) {
	    openULTag(parentUnit, buffer, parentUnitParent);
	    Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);
	}

	for (Unit subUnit : subUnits) {
	    if (!subUnit.equals(this.getUnit())) {
		getSubUnitsListToChooseParentUnit(subUnit, parentUnit, buffer, currentDate);
	    }
	}

	if (!subUnits.isEmpty()) {
	    closeULTag(buffer);
	}
    }

    private List<Unit> getSubUnits(boolean active, Unit unit, YearMonthDay currentDate) {
	List<Unit> subUnits = (active) ? unit.getActiveSubUnits(currentDate) : unit.getInactiveSubUnits(currentDate);
	if(!subUnits.isEmpty()) {
	    Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);
	}
	return subUnits;
    }

    private List<Unit> getAllSubUnits(boolean active, Unit unit, YearMonthDay currentDate) {
	List<Unit> subUnits = (active) ? unit.getAllActiveSubUnits(currentDate) : unit.getAllInactiveSubUnits(currentDate);
	if(!subUnits.isEmpty()) {
	    Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);
	}
	return subUnits;
    }

    public String prepareListAllUnits() {
	HtmlInputHidden hidden = new HtmlInputHidden();
	hidden.setValue("0");
	this.setListingTypeValueToUnitsHidden(hidden);
	this.setListingTypeValueToUnits("0");
	this.setViewExternalUnits(Boolean.FALSE);
	return "listAllUnits";
    }

    public String getUnits() throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {
	
	StringBuilder buffer = new StringBuilder();
	List<Unit> unitsToShow = null;
	YearMonthDay currentDate = new YearMonthDay();

	if (getViewExternalUnits()) {	    	    
	    unitsToShow = new ArrayList<Unit>();
	    unitsToShow.add(UnitUtils.readExternalInstitutionUnit());
	    
	} else if(getViewUnitsWithoutParents()){
	    unitsToShow = UnitUtils.readAllUnitsWithoutParents();
	    
	} else {
	    unitsToShow = new ArrayList<Unit>();
	    unitsToShow.add(UnitUtils.readEarthUnit());
	}
	
	Collections.sort(unitsToShow, Unit.COMPARATOR_BY_NAME_AND_ID);
	for (Unit unit : unitsToShow) {
	    boolean active = this.getListingTypeValueToUnitsHidden().getValue().toString().equals("0");
	    if (active) {
		if (unit.isActive(currentDate) || !getAllSubUnits(active, unit, currentDate).isEmpty()) {
		    buffer.append("<ul class='padding1 nobullet'>");
		    getSubUnitsList(unit, null, getSubUnits(active, unit, currentDate), buffer, currentDate, active);
		    buffer.append("</ul>");
		}
	    } else {
		if (!unit.isActive(currentDate) || !getAllSubUnits(active, unit, currentDate).isEmpty()) {
		    buffer.append("<ul class='padding1 nobullet'>");
		    getSubUnitsList(unit, null, getSubUnits(active, unit, currentDate), buffer, currentDate, active);
		    buffer.append("</ul>");
		}
	    }
	}
	return buffer.toString();
    }

    private void getSubUnitsList(Unit parentUnit, Unit parentUnitParent, List<Unit> subUnits,
	    StringBuilder buffer, YearMonthDay currentDate, boolean active) {

	openLITag(buffer);

	if (!subUnits.isEmpty()) {
	    putImage(parentUnit, buffer, parentUnitParent);
	}

	buffer.append("<a href=\"").append(getContextPath()).append(
		"/manager/organizationalStructureManagament/").append("unitDetails.faces?").append(
		"unitID=").append(parentUnit.getIdInternal()).append("\">").append(
		parentUnit.getNameWithAcronym()).append("</a>").append("</li>");

	if (!subUnits.isEmpty()) {
	    openULTag(parentUnit, buffer, parentUnitParent);
	    Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);
	}

	for (Unit subUnit : subUnits) {
	    getSubUnitsList(subUnit, parentUnit, getSubUnits(active, subUnit, currentDate), buffer, currentDate, active);
	}

	if (!subUnits.isEmpty()) {
	    closeULTag(buffer);
	}
    }

    public String getUnitsToChoosePrincipalFunction() throws FenixFilterException,
	    FenixServiceException, ExcepcaoPersistencia {

	YearMonthDay currentDate = new YearMonthDay();
	StringBuilder buffer = new StringBuilder();
	buffer.append("<ul class='padding1 nobullet'>");
	getSubUnitsListToChoosePrincipalFunction(UnitUtils.readInstitutionUnit(), null, buffer,	currentDate);
	buffer.append("</ul>");
	return buffer.toString();
    }

    private void getSubUnitsListToChoosePrincipalFunction(Unit parentUnit, Unit parentUnitParent,
	    StringBuilder buffer, YearMonthDay currentDate) throws FenixFilterException,
	    FenixServiceException {

	openLITag(buffer);

	List<Unit> subUnits = null;
	if (this.getFunction().isActive(currentDate)) {
	    subUnits = getSubUnits(true, parentUnit, currentDate);
	} else {
	    subUnits = getSubUnits(false, parentUnit, currentDate);
	}

	if (!subUnits.isEmpty()) {
	    putImage(parentUnit, buffer, parentUnitParent);
	}

	buffer.append("<a href=\"").append(getContextPath()).append(
		"/manager/organizationalStructureManagament/").append("chooseFunction.faces?").append(
		"unitID=").append(this.getUnit().getIdInternal()).append("&chooseUnitID=").append(
		parentUnit.getIdInternal()).append("&functionID=").append(
		this.getFunction().getIdInternal()).append("\">")
		.append(parentUnit.getNameWithAcronym()).append("</a>").append("</li>");

	if (!subUnits.isEmpty()) {
	    openULTag(parentUnit, buffer, parentUnitParent);
	    Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);
	}

	for (Unit subUnit : subUnits) {
	    getSubUnitsListToChoosePrincipalFunction(subUnit, parentUnit, buffer, currentDate);
	}

	if (!subUnits.isEmpty()) {
	    closeULTag(buffer);
	}
    }

    public List<SelectItem> getValidUnitType() {
	List<SelectItem> list = new ArrayList<SelectItem>();
	ResourceBundle bundle = getResourceBundle("resources/EnumerationResources");

	SelectItem selectItem = null;
	for (PartyTypeEnum type : PartyTypeEnum.values()) {
	    selectItem = new SelectItem();	    
	    selectItem.setLabel(hasKey(bundle, type.getName()) ? bundle.getString(type.getName()) : type.getName());
	    selectItem.setValue(type.getName());
	    list.add(selectItem);
	}
	Collections.sort(list, new BeanComparator("label"));

	addDefaultSelectedItem(list, bundle);

	return list;
    }
    
    public List<SelectItem> getValidUnitClassifications() {
	List<SelectItem> list = new ArrayList<SelectItem>();
	ResourceBundle bundle = getResourceBundle("resources/EnumerationResources");

	SelectItem selectItem = null;
	for (UnitClassification classification : UnitClassification.values()) {
	    selectItem = new SelectItem();	    
	    selectItem.setLabel(hasKey(bundle, classification.getName()) ? bundle.getString(classification.getName()) : classification.getName());	    
	    selectItem.setValue(classification.getName());
	    list.add(selectItem);
	}
	Collections.sort(list, new BeanComparator("label"));

	addDefaultSelectedItem(list, bundle);

	return list;
    }

    public List<SelectItem> getDepartments() throws FenixFilterException, FenixServiceException {
	List<SelectItem> list = new ArrayList<SelectItem>();
	SelectItem selectItem = null;

	List<Department> allDepartments = rootDomainObject.getDepartments();

	for (Department department : allDepartments) {
	    selectItem = new SelectItem();
	    selectItem.setLabel(department.getRealName());
	    selectItem.setValue(department.getIdInternal().toString());
	    list.add(selectItem);
	}

	Collections.sort(list, new BeanComparator("label"));
	ResourceBundle bundle = getResourceBundle("resources/EnumerationResources");
	addDefaultSelectedItem(list, bundle);
	return list;
    }
    
    public List<SelectItem> getAdministrativeOffices() throws FenixFilterException, FenixServiceException {
	List<SelectItem> list = new ArrayList<SelectItem>();
	SelectItem selectItem = null;

	List<AdministrativeOffice> allAdministrativeOffices = rootDomainObject.getAdministrativeOffices();
	ResourceBundle bundle = getResourceBundle("resources/EnumerationResources");
	
	for (AdministrativeOffice administrativeOffice : allAdministrativeOffices) {
	    selectItem = new SelectItem();	    
	    String name = administrativeOffice.getAdministrativeOfficeType().getClass().getSimpleName() + "." + administrativeOffice.getAdministrativeOfficeType().getName();
	    selectItem.setLabel(hasKey(bundle, name) ? bundle.getString(name) : name);	    	    
	    selectItem.setValue(administrativeOffice.getIdInternal().toString());
	    list.add(selectItem);
	}

	Collections.sort(list, new BeanComparator("label"));	
	addDefaultSelectedItem(list, bundle);
	return list;
    }

    public List<SelectItem> getDegrees() throws FenixFilterException, FenixServiceException {
	List<SelectItem> list = new ArrayList<SelectItem>();
	SelectItem selectItem = null;

	List<Degree> allDegrees = rootDomainObject.getDegrees();

	for (Degree degree : allDegrees) {
	    selectItem = new SelectItem();

	    if (!degree.isBolonhaDegree()) {
		if (degree.getTipoCurso().equals(DegreeType.DEGREE)) {
		    selectItem.setLabel("(L) " + degree.getNome());
		} else if (degree.getTipoCurso().equals(DegreeType.MASTER_DEGREE)) {
		    selectItem.setLabel("(M) " + degree.getNome());
		}
	    } else if (degree.isBolonhaDegree()) {
		if (degree.getDegreeType().equals(DegreeType.BOLONHA_DEGREE)) {
		    selectItem.setLabel("(L-B) " + degree.getNome());
		} else if (degree.getDegreeType().equals(DegreeType.BOLONHA_MASTER_DEGREE)) {
		    selectItem.setLabel("(M-B) " + degree.getNome());
		} else if (degree.getDegreeType().equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)) {
		    selectItem.setLabel("(MI) " + degree.getNome());
		} else if (degree.getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)) {
		    selectItem.setLabel("(DFA) " + degree.getNome());
		} else if (degree.getDegreeType().equals(DegreeType.BOLONHA_PHD_PROGRAM)) {
		    selectItem.setLabel("(DEA) " + degree.getNome());
		} else if (degree.getDegreeType().equals(DegreeType.BOLONHA_SPECIALIZATION_DEGREE)) {
		    selectItem.setLabel("(SD) " + degree.getNome());
		}
	    }

	    selectItem.setValue(degree.getIdInternal().toString());
	    list.add(selectItem);
	}

	Collections.sort(list, new BeanComparator("label"));
	ResourceBundle bundle = getResourceBundle("resources/EnumerationResources");
	addDefaultSelectedItem(list, bundle);
	return list;
    }

    public List<SelectItem> getListingTypeToUnits() {
	List<SelectItem> list = new ArrayList<SelectItem>();

	SelectItem selectItem = new SelectItem();
	selectItem.setLabel("Activas");
	selectItem.setValue("0");
	SelectItem selectItem2 = new SelectItem();
	selectItem2.setLabel("Inactivas");
	selectItem2.setValue("1");

	list.add(selectItem);
	list.add(selectItem2);

	return list;
    }
    
    public List<SelectItem> getCampuss() {
	List<SelectItem> list = new ArrayList<SelectItem>();	
	List<Campus> activeCampus = Campus.getAllActiveCampus();	
	for (Campus campus : activeCampus) {
	    SelectItem selectItem = new SelectItem();
	    selectItem.setLabel(campus.getName());
	    selectItem.setValue(campus.getIdInternal().toString());
	    list.add(selectItem);
	}		
	Collections.sort(list, new BeanComparator("label"));
	ResourceBundle bundle = getResourceBundle("resources/EnumerationResources");
	addDefaultSelectedItem(list, bundle);
	return list;
    }

    public List<SelectItem> getListingTypeToFunctions() {
	List<SelectItem> list = new ArrayList<SelectItem>();

	SelectItem selectItem = new SelectItem();
	selectItem.setLabel("Activos");
	selectItem.setValue("0");
	SelectItem selectItem2 = new SelectItem();
	selectItem2.setLabel("Inactivos");
	selectItem2.setValue("1");

	list.add(selectItem);
	list.add(selectItem2);

	return list;
    }

    public List<SelectItem> getValidFunctionType() {
	List<SelectItem> list = new ArrayList<SelectItem>();

	ResourceBundle bundle = getResourceBundle("resources/EnumerationResources");

	SelectItem selectItem = null;
	for (FunctionType type : FunctionType.values()) {
	    selectItem = new SelectItem();	    	    
	    selectItem.setLabel(hasKey(bundle, type.getName()) ? bundle.getString(type.getName()) : type.getName());	    
	    selectItem.setValue(type.getName());
	    list.add(selectItem);
	}
	Collections.sort(list, new BeanComparator("label"));

	addDefaultSelectedItem(list, bundle);

	return list;
    }

    public List<SelectItem> getUnitRelationTypes() {
	List<SelectItem> list = new ArrayList<SelectItem>();

	ResourceBundle bundle = getResourceBundle("resources/EnumerationResources");

	SelectItem selectItem = null;
	for (AccountabilityTypeEnum type : AccountabilityTypeEnum.values()) {
	    if (type.equals(AccountabilityTypeEnum.ACADEMIC_STRUCTURE)
		    || type.equals(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE)
		    || type.equals(AccountabilityTypeEnum.ADMINISTRATIVE_STRUCTURE)
		    || type.equals(AccountabilityTypeEnum.GEOGRAPHIC)) {
		
		selectItem = new SelectItem();
		selectItem.setLabel(hasKey(bundle, type.getName()) ? bundle.getString(type.getName()) : type.getName());		
		selectItem.setValue(type.getName());
		list.add(selectItem);
	    }
	}
	Collections.sort(list, new BeanComparator("label"));

	addDefaultSelectedItem(list, bundle);

	return list;
    }

    private void addDefaultSelectedItem(List<SelectItem> list, ResourceBundle bundle) {
	SelectItem firstItem = new SelectItem();
	firstItem.setLabel(hasKey(bundle, "dropDown.Default") ? bundle.getString("dropDown.Default") : "dropDown.Default");	
	firstItem.setValue("#");
	list.add(0, firstItem);
    }

    public String createTopUnit() throws FenixFilterException, FenixServiceException {

	PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
	if (datesResult.isTest()) {
	    return "";
	}

	CreateNewUnitParameters parameters = new CreateNewUnitParameters(this, 1);

	MultiLanguageString unitName = new MultiLanguageString();
	unitName.setContent(Language.pt, this.getUnitName());
	unitName.setContent(Language.en, this.getUnitNameEn());
	
	final Object[] argsToRead = { null, unitName, this.getUnitCostCenter(),
		this.getUnitAcronym(), datesResult.getBeginDate(), datesResult.getEndDate(), getUnitType(),
		parameters.getDepartmentID(), parameters.getDegreeID(), parameters.getAdministrativeOfficeID(),
		null, this.getUnitWebAddress(), this.getUnitClassification(), this.getCanBeResponsibleOfSpaces(),
		parameters.getCampusID()};

	return executeUnitsManagementService(argsToRead, "listAllUnits", "CreateUnit");
    }

    public String createSubUnit() throws FenixFilterException, FenixServiceException {

	if (getUnitRelationTypeValue().equals("#")) {
	    ResourceBundle bundle = getResourceBundle("resources/ManagerResources");	   
	    addErrorMessage(hasKey(bundle, "error.no.unit.relation.type") ? bundle.getString("error.no.unit.relation.type") : "error.no.unit.relation.type");
	    return "";
	}

	PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
	if (datesResult.isTest()) {
	    return "";
	}

	AccountabilityType accountabilityType = AccountabilityType
		.readAccountabilityTypeByType(AccountabilityTypeEnum.valueOf(getUnitRelationTypeValue()));

	CreateNewUnitParameters parameters = new CreateNewUnitParameters(this, 1);

	MultiLanguageString unitName = new MultiLanguageString();
	unitName.setContent(Language.pt, this.getUnitName());
	unitName.setContent(Language.en, this.getUnitNameEn());
	
	final Object[] argsToRead = { this.getUnit(), unitName,
		this.getUnitCostCenter(), this.getUnitAcronym(), datesResult.getBeginDate(),
		datesResult.getEndDate(), getUnitType(), parameters.getDepartmentID(), parameters.getDegreeID(),
		parameters.getAdministrativeOfficeID(), accountabilityType, this.getUnitWebAddress(),
		this.getUnitClassification(), this.getCanBeResponsibleOfSpaces(),
		parameters.getCampusID()};

	return executeUnitsManagementService(argsToRead, "backToUnitDetails", "CreateUnit");
    }

    public String editUnit() throws FenixFilterException, FenixServiceException {
	PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
	if (datesResult.isTest()) {
	    return "";
	}
	
	CreateNewUnitParameters parameters = new CreateNewUnitParameters(this, 1);

	MultiLanguageString unitName = new MultiLanguageString();
	unitName.setContent(Language.pt, this.getUnitName());
	unitName.setContent(Language.en, this.getUnitNameEn());
	
	final Object[] argsToRead = { this.getChooseUnit().getIdInternal(), unitName,
		this.getUnitCostCenter(), this.getUnitAcronym(), datesResult.getBeginDate(),
		datesResult.getEndDate(), parameters.getDepartmentID(), parameters.getDegreeID(),
		parameters.getAdministrativeOfficeID(), this.getUnitWebAddress(), 
		this.getUnitClassification(), this.getCanBeResponsibleOfSpaces(), 
		parameters.getCampusID()};

	return executeUnitsManagementService(argsToRead, "backToUnitDetails", "EditUnit");
    }

    private PartyTypeEnum getUnitType() throws FenixFilterException, FenixServiceException {
	PartyTypeEnum type = null;
	if (!this.getUnitTypeName().equals("#")) {
	    type = PartyTypeEnum.valueOf(this.getUnitTypeName());
	}
	return type;
    }
    
    private UnitClassification getUnitClassification() throws FenixFilterException, FenixServiceException {
	UnitClassification classification = null;
	if (!this.getUnitClassificationName().equals("#")) {
	    classification = UnitClassification.valueOf(this.getUnitClassificationName());
	}
	return classification;
    }

    public String associateParentUnit() throws FenixFilterException, FenixServiceException {
	if (getUnitRelationTypeValue().equals("#")) {
	    ResourceBundle bundle = getResourceBundle("resources/ManagerResources");	    
	    addErrorMessage(hasKey(bundle, "error.no.unit.relation.type") ? bundle.getString("error.no.unit.relation.type") : "error.no.unit.relation.type");
	    return "";
	}

	AccountabilityType accountabilityType = AccountabilityType
		.readAccountabilityTypeByType(AccountabilityTypeEnum.valueOf(getUnitRelationTypeValue()));

	final Object[] argsToRead = { this.getUnit().getIdInternal(),
		this.getChooseUnit().getIdInternal(), accountabilityType };

	return executeUnitsManagementService(argsToRead, "backToUnitDetails", "AssociateParentUnit");
    }

    public String disassociateParentUnit() throws FenixFilterException, FenixServiceException {
	final Object[] argsToRead = { this.getAccountabilityID() };

	return executeUnitsManagementService(argsToRead, "backToUnitDetails", "DisassociateParentUnit");
    }

    private String executeUnitsManagementService(final Object[] argsToRead, String defaultReturn,
	    String serviceName) throws FenixFilterException {
	
	try {
	    ServiceUtils.executeService( serviceName, argsToRead);
	
	} catch (FenixServiceException e) {
	    setErrorMessage(e.getMessage());
	    return "";
	} catch (DomainException e) {
	    setErrorMessage(e.getMessage());
	    return "";
	}

	return defaultReturn;
    }

    private FunctionType getFunctionType() throws FenixFilterException, FenixServiceException {
	FunctionType type = null;
	if (!this.getFunctionTypeName().equals("#")) {
	    type = FunctionType.valueOf(this.getFunctionTypeName());
	}
	return type;
    }

    public String createFunction() throws FenixFilterException, FenixServiceException {

	PrepareDatesResult datesResult = prepareDates(this.getFunctionBeginDate(), this.getFunctionEndDate());
	if (datesResult.isTest()) {
	    return "";
	}

	FunctionType type = getFunctionType();
	
	MultiLanguageString functionName = new MultiLanguageString();
	functionName.setContent(Language.pt, this.getFunctionName());
	functionName.setContent(Language.en, this.getFunctionNameEn());
	
	final Object[] argsToRead = { functionName, datesResult.getBeginDate(), datesResult.getEndDate(), type, this.getUnit().getIdInternal() };
	return executeFunctionsManagementService(argsToRead, "CreateFunction");
    }

    public String editFunction() throws FenixFilterException, FenixServiceException {

	PrepareDatesResult datesResult = prepareDates(this.getFunctionBeginDate(), this.getFunctionEndDate());

	if (datesResult.isTest()) {
	    return "";
	}

	FunctionType type = getFunctionType();
	
	MultiLanguageString functionName = new MultiLanguageString();
	functionName.setContent(Language.pt, this.getFunctionName());
	functionName.setContent(Language.en, this.getFunctionNameEn());
	
	final Object[] argsToRead = { this.getFunction().getIdInternal(), functionName, datesResult.getBeginDate(), datesResult.getEndDate(), type };

	return executeFunctionsManagementService(argsToRead, "EditFunction");
    }

    public String associateInherentParentFunction() throws FenixFilterException, FenixServiceException {
	Function function = this.getFunction();
	final Object[] argsToRead = { function.getIdInternal(), this.principalFunctionID };
	return executeFunctionsManagementService(argsToRead, "AddParentInherentFunction");
    }

    public String prepareAssociateInherentParentFunction() throws FenixFilterException,
	    FenixServiceException {

	Function function = this.getFunction();
	if (!function.getPersonFunctions().isEmpty()) {
	    setErrorMessage("error.becomeInherent");
	    return "";
	}
	return "chooseInherentParentFunction";
    }

    private String executeFunctionsManagementService(final Object[] argsToRead, String serviceName)
	    throws FenixFilterException {
	try {
	    ServiceUtils.executeService( serviceName, argsToRead);
	} catch (FenixServiceException e) {
	    setErrorMessage(e.getMessage());
	    return "";
	} catch (DomainException e) {
	    setErrorMessage(e.getMessage());
	    return "";
	}

	return "backToUnitDetails";
    }

    public String disassociateInherentFunction() throws FenixFilterException, FenixServiceException {

	Function function = this.getFunction();

	final Object[] argsToRead = { function.getIdInternal() };

	try {
	    ServiceUtils.executeService( "RemoveParentInherentFunction", argsToRead);
	} catch (FenixServiceException e) {
	    setErrorMessage(e.getMessage());
	    return "";
	}

	return "";
    }

    public String deleteSubUnit() throws FenixFilterException {
	final Object[] argsToRead = { Integer.valueOf(this.getChooseUnitIDHidden().getValue().toString()) };
	try {
	    ServiceUtils.executeService( "DeleteUnit", argsToRead);

	} catch (FenixServiceException e) {
	    setErrorMessage(e.getMessage());
	} catch (DomainException e1) {
	    setErrorMessage(e1.getMessage());
	    return "";
	}
	return "";
    }

    public String deleteUnit() throws FenixFilterException {
	final Object[] argsToRead = { Integer.valueOf(this.getChooseUnitIDHidden().getValue().toString()) };
	try {
	    ServiceUtils.executeService( "DeleteUnit", argsToRead);

	} catch (FenixServiceException e) {
	    setErrorMessage(e.getMessage());
	    return "";
	} catch (DomainException e1) {
	    setErrorMessage(e1.getMessage());
	    return "";
	}
	return "listAllUnits";
    }

    public String deleteFunction() throws FenixFilterException {
	final Object[] argsToRead = { Integer.valueOf(this.getFunctionIDHidden().getValue().toString()) };
	try {
	    ServiceUtils.executeService( "DeleteFunction", argsToRead);

	} catch (FenixServiceException e) {
	    setErrorMessage(e.getMessage());
	} catch (DomainException e1) {
	    setErrorMessage(e1.getMessage());
	}
	return "";
    }

    private PrepareDatesResult prepareDates(String beginDate, String endDate)
	    throws FenixFilterException, FenixServiceException {
	boolean test = false;
	DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	Date beginDate_ = null, endDate_ = null;
	try {
	    beginDate_ = format.parse(beginDate);
	    if (endDate != null && !endDate.equals("")) {
		endDate_ = format.parse(endDate);
	    }
	} catch (ParseException exception) {
	    setErrorMessage("error.date.format");
	    test = true;
	}

	return new PrepareDatesResult(test, beginDate_, endDate_);
    }

    private void openULTag(Unit parentUnit, StringBuilder buffer, Unit parentUnitParent) {
	buffer.append("<ul class='mvert0 nobullet' id=\"").append("aa").append(
		parentUnit.getIdInternal()).append(
		(parentUnitParent != null) ? parentUnitParent.getIdInternal() : "").append("\" ")
		.append("style='display:none'>\r\n");
    }

    private void putImage(Unit parentUnit, StringBuilder buffer, Unit parentUnitParent) {

	buffer.append("<img ").append("src='").append(getContextPath()).append(
		"/images/toggle_plus10.gif' id=\"").append(parentUnit.getIdInternal()).append(
		(parentUnitParent != null) ? parentUnitParent.getIdInternal() : "").append("\" ")
		.append("indexed='true' onClick=\"").append("check(document.getElementById('").append(
			"aa").append(parentUnit.getIdInternal()).append(
			(parentUnitParent != null) ? parentUnitParent.getIdInternal() : "").append(
			"'),document.getElementById('").append(parentUnit.getIdInternal()).append(
			(parentUnitParent != null) ? parentUnitParent.getIdInternal() : "").append(
			"'));return false;").append("\"> ");
    }

    private void closeULTag(StringBuilder buffer) {
	buffer.append("</ul>");
    }

    private void openLITag(StringBuilder buffer) {
	buffer.append("<li>");
    }

    public String getUnitCostCenter() throws FenixFilterException, FenixServiceException {
	if (this.unitCostCenter == null && this.getChooseUnit() != null
		&& this.getChooseUnit().getCostCenterCode() != null) {
	    this.unitCostCenter = this.getChooseUnit().getCostCenterCode().toString();
	}
	return unitCostCenter;
    }

    public void setUnitCostCenter(String costCenter) {
	this.unitCostCenter = costCenter;
    }

    public String getUnitBeginDate() throws FenixFilterException, FenixServiceException {
	if (this.unitBeginDate == null && this.getChooseUnit() != null) {
	    this.unitBeginDate = processDate(this.getChooseUnit().getBeginDate());
	}
	return unitBeginDate;
    }

    public void setUnitBeginDate(String unitBeginDate) {
	this.unitBeginDate = unitBeginDate;
    }

    public String getUnitEndDate() throws FenixFilterException, FenixServiceException {
	if (this.unitEndDate == null && this.getChooseUnit() != null
		&& this.getChooseUnit().getEndDate() != null) {
	    this.unitEndDate = processDate(this.getChooseUnit().getEndDate());
	}
	return unitEndDate;
    }

    public void setUnitEndDate(String unitEndDate) {
	this.unitEndDate = unitEndDate;
    }

    public String getUnitAcronym() throws FenixFilterException, FenixServiceException {
	if (this.unitAcronym == null && this.getChooseUnit() != null) {
	    this.unitAcronym = this.getChooseUnit().getAcronym();
	}
	return unitAcronym;
    }

    public void setUnitAcronym(String unitAcronym) {
	this.unitAcronym = unitAcronym;
    }

    public String getUnitWebAddress() throws FenixFilterException, FenixServiceException {
	if (this.unitWebAddress == null && this.getChooseUnit() != null) {
	    this.unitWebAddress = this.getChooseUnit().getWebAddress();
	}
	return unitWebAddress;
    }

    public void setUnitWebAddress(String webAddress) {
	this.unitWebAddress = webAddress;
    }

    public String getUnitName() throws FenixFilterException, FenixServiceException {
	if (this.unitName == null && this.getChooseUnit() != null) {
	    this.unitName = this.getChooseUnit().getName();
	}
	return unitName;
    }

    public void setUnitName(String unitName) {
	this.unitName = unitName;
    }

    public String getUnitTypeName() throws FenixFilterException, FenixServiceException {
	if (this.unitTypeName == null && this.getChooseUnit() != null
		&& this.getChooseUnit().getType() != null) {
	    this.unitTypeName = this.getChooseUnit().getType().getName();
	}
	return unitTypeName;
    }

    public void setUnitTypeName(String unitTypeName) {
	this.unitTypeName = unitTypeName;
    }

    public String getUnitClassificationName() throws FenixFilterException, FenixServiceException {
	if (this.unitClassificationName == null && this.getChooseUnit() != null
		&& this.getChooseUnit().getClassification() != null) {
	    this.unitClassificationName = this.getChooseUnit().getClassification().getName();
	}
	return unitClassificationName;
    }

    public void setUnitClassificationName(String unitClassificationName) {
        this.unitClassificationName = unitClassificationName;
    }
    
    public Unit getUnit() throws FenixFilterException, FenixServiceException {
	if (this.unit == null && this.getUnitIDHidden() != null
		&& this.getUnitIDHidden().getValue() != null
		&& !this.getUnitIDHidden().getValue().equals("")) {

	    this.unit = (Unit) rootDomainObject.readPartyByOID(Integer.valueOf(this.getUnitIDHidden()
		    .getValue().toString()));
	}
	if (toRemoveParentUnit) {
	    getParentUnitsRelationTypes();
	} else {
	    getSubUnitsRelationTypes();
	}
	return unit;
    }

    public List<Accountability> getParentAccountabilities() throws FenixFilterException, FenixServiceException {
	return new ArrayList<Accountability>((Collection<Accountability>) getUnit().getParentAccountabilitiesByParentClass(Unit.class));
    }

    public List<Accountability> getChildAccountabilities() throws FenixFilterException, FenixServiceException {
	return new ArrayList<Accountability>((Collection<Accountability>) getUnit().getChildAccountabilitiesByChildClass(Unit.class));
    }

    private void getParentUnitsRelationTypes() throws FenixFilterException, FenixServiceException {	
	if (unit != null) {
	    ResourceBundle bundle = getResourceBundle("resources/EnumerationResources");
	    getUnitRelationsAccountabilityTypes().clear();
	    for (Accountability accountability : unit.getParentsSet()) {
		String accountabilityTypeName = accountability.getAccountabilityType().getType().getName();
		if (accountability.getParentParty().isUnit()) {		    		    
		    getUnitRelationsAccountabilityTypes().put(accountability.getIdInternal(), 
				    hasKey(bundle, accountabilityTypeName) ? bundle.getString(accountabilityTypeName) : accountabilityTypeName);
		}
	    }
	}
    }

    private void getSubUnitsRelationTypes() throws FenixFilterException, FenixServiceException {		
	if (unit != null) {
	    ResourceBundle bundle = getResourceBundle("resources/EnumerationResources");
	    getUnitRelationsAccountabilityTypes().clear();
	    for (Accountability accountability : unit.getChildsSet()) {
		String accountabilityTypeName = accountability.getAccountabilityType().getType().getName();
		if (accountability.getChildParty().isUnit()) {
		    Integer subUnitIdInternal = accountability.getChildParty().getIdInternal();
		    if (getUnitRelationsAccountabilityTypes().containsKey(subUnitIdInternal)) {			
			getUnitRelationsAccountabilityTypes().put(subUnitIdInternal,
				getUnitRelationsAccountabilityTypes().get(subUnitIdInternal).concat(", " + 
					(hasKey(bundle, accountabilityTypeName) ? bundle.getString(accountabilityTypeName) : accountabilityTypeName)));
		    } else {
			getUnitRelationsAccountabilityTypes().put(
				subUnitIdInternal,
				(hasKey(bundle, accountabilityTypeName) ? bundle.getString(accountabilityTypeName) : accountabilityTypeName));
		    }
		}
	    }
	}
    }

    public void setUnit(Unit unit) {
	this.unit = unit;
    }

    private String processDate(Date date) throws FenixFilterException, FenixServiceException {
	return DateFormatUtil.format("dd/MM/yyyy", date);
    }

    public HtmlInputHidden getUnitIDHidden() {
	if (this.unitIDHidden == null) {
	    this.unitIDHidden = new HtmlInputHidden();
	}
	return unitIDHidden;
    }

    public void setUnitIDHidden(HtmlInputHidden unitIDHidden) {
	this.unitIDHidden = unitIDHidden;
    }

    public Function getFunction() throws FenixFilterException, FenixServiceException {
	if (this.function == null && this.getFunctionIDHidden() != null
		&& this.getFunctionIDHidden().getValue() != null
		&& !this.getFunctionIDHidden().getValue().equals("")) {

	    this.function = (Function) rootDomainObject.readAccountabilityTypeByOID(Integer.valueOf(this
		    .getFunctionIDHidden().getValue().toString()));
	}
	return function;
    }

    public void setFunction(Function function) {
	this.function = function;
    }

    public String getFunctionBeginDate() throws FenixFilterException, FenixServiceException {
	if (this.functionBeginDate == null && this.getFunction() != null
		&& this.getFunction().getBeginDate() != null) {
	    this.functionBeginDate = processDate(this.getFunction().getBeginDate());
	}
	return functionBeginDate;
    }

    public void setFunctionBeginDate(String functionBeginDate) {
	this.functionBeginDate = functionBeginDate;
    }

    public String getFunctionEndDate() throws FenixFilterException, FenixServiceException {
	if (this.functionEndDate == null && this.getFunction() != null
		&& this.getFunction().getEndDate() != null) {
	    this.functionEndDate = processDate(this.getFunction().getEndDate());
	}
	return functionEndDate;
    }

    public void setFunctionEndDate(String functionEndDate) {
	this.functionEndDate = functionEndDate;
    }

    public String getFunctionName() throws FenixFilterException, FenixServiceException {
	if (this.functionName == null && this.getFunction() != null) {
	    this.functionName = this.getFunction().getTypeName().getContent(Language.pt);
	}
	return functionName;
    }

    public void setFunctionName(String functionName) {
	this.functionName = functionName;
    }

    public String getFunctionTypeName() throws FenixFilterException, FenixServiceException {
	if (this.functionTypeName == null && this.getFunction() != null
		&& this.getFunction().getFunctionType() != null) {
	    this.functionTypeName = this.getFunction().getFunctionType().getName();
	}
	return functionTypeName;
    }

    public void setFunctionTypeName(String functionTypeName) {
	this.functionTypeName = functionTypeName;
    }

    public Unit getChooseUnit() throws FenixFilterException, FenixServiceException {
	if (this.chooseUnit == null && this.getChooseUnitIDHidden() != null
		&& this.getChooseUnitIDHidden().getValue() != null
		&& !this.getChooseUnitIDHidden().getValue().equals("")) {

	    this.chooseUnit = (Unit) rootDomainObject.readPartyByOID(Integer.valueOf(this
		    .getChooseUnitIDHidden().getValue().toString()));
	}

	return chooseUnit;
    }

    public void setChooseUnit(Unit chooseUnit) {
	this.chooseUnit = chooseUnit;
    }

    public HtmlInputHidden getChooseUnitIDHidden() {
	if (this.chooseUnitIDHidden == null) {
	    this.chooseUnitIDHidden = new HtmlInputHidden();
	}
	return chooseUnitIDHidden;
    }

    public void setChooseUnitIDHidden(HtmlInputHidden chooseUnitIDHidden) {
	this.chooseUnitIDHidden = chooseUnitIDHidden;
    }

    public HtmlInputHidden getFunctionIDHidden() {
	if (this.functionIDHidden == null) {
	    this.functionIDHidden = new HtmlInputHidden();
	}
	return functionIDHidden;
    }

    public void setFunctionIDHidden(HtmlInputHidden functionIDHidden) {
	this.functionIDHidden = functionIDHidden;
    }

    public Integer getPrincipalFunctionID() {
	return principalFunctionID;
    }

    public void setPrincipalFunctionID(Integer principalFunctionID) {
	this.principalFunctionID = principalFunctionID;
    }

    public String getListingTypeValueToUnits() {
	if (listingTypeValueToUnits == null) {
	    this.listingTypeValueToUnits = "0";
	}
	return listingTypeValueToUnits;
    }

    public void setListingTypeValueToUnits(String listingTypeValue) {
	this.listingTypeValueToUnits = listingTypeValue;
	this.listingTypeValueToUnitsHidden.setValue(listingTypeValue);
    }

    public String getListingTypeValueToFunctions() {
	if (listingTypeValueToFunctions == null) {
	    listingTypeValueToFunctions = "0";
	}
	return listingTypeValueToFunctions;
    }

    public void setListingTypeValueToFunctions(String listingTypeValueToFunctions) {
	this.listingTypeValueToFunctions = listingTypeValueToFunctions;
	this.listingTypeValueToFunctionsHidden.setValue(listingTypeValueToFunctions);
    }

    public HtmlInputHidden getListingTypeValueToFunctionsHidden() {
	if (listingTypeValueToFunctionsHidden == null) {
	    listingTypeValueToFunctionsHidden = new HtmlInputHidden();
	    listingTypeValueToFunctionsHidden.setValue(getListingTypeValueToFunctions());
	}
	return listingTypeValueToFunctionsHidden;
    }

    public void setListingTypeValueToFunctionsHidden(HtmlInputHidden listingTypeValueToFunctionsHidden) {
	this.listingTypeValueToFunctionsHidden = listingTypeValueToFunctionsHidden;
    }

    public HtmlInputHidden getListingTypeValueToUnitsHidden() {
	if (listingTypeValueToUnitsHidden == null) {
	    listingTypeValueToUnitsHidden = new HtmlInputHidden();
	    listingTypeValueToUnitsHidden.setValue(getListingTypeValueToUnits());
	}
	return listingTypeValueToUnitsHidden;
    }

    public void setListingTypeValueToUnitsHidden(HtmlInputHidden listingTypeValueToUnitsHidden) {
	this.listingTypeValueToUnitsHidden = listingTypeValueToUnitsHidden;
    }

    public String getAdministrativeOfficeID() throws FenixFilterException, FenixServiceException {
	if (this.administrativeOfficeID == null && this.getChooseUnit() != null
		&& this.getChooseUnit().getAdministrativeOffice() != null) {
	    this.administrativeOfficeID = this.getChooseUnit().getAdministrativeOffice().getIdInternal().toString();
	}	
        return administrativeOfficeID;
    }

    public void setAdministrativeOfficeID(String administrativeOfficeID) {
        this.administrativeOfficeID = administrativeOfficeID;
    }
    
    public String getDegreeID() throws FenixFilterException, FenixServiceException {
	if (this.degreeID == null && this.getChooseUnit() != null
		&& this.getChooseUnit().getDegree() != null) {
	    this.degreeID = this.getChooseUnit().getDegree().getIdInternal().toString();
	}
	return degreeID;
    }

    public void setDegreeID(String degreeID) {
	this.degreeID = degreeID;
    }

    public String getDepartmentID() throws FenixFilterException, FenixServiceException {
	if (this.departmentID == null && this.getChooseUnit() != null
		&& this.getChooseUnit().getDepartment() != null) {
	    this.departmentID = this.getChooseUnit().getDepartment().getIdInternal().toString();
	}
	return departmentID;
    }

    public void setDepartmentID(String departmentID) {
	this.departmentID = departmentID;
    }
    
    public String getCampusID() throws FenixFilterException, FenixServiceException {
	if (this.campusID == null && this.getChooseUnit() != null && this.getChooseUnit().getCampus() != null) {
	    this.campusID = this.getChooseUnit().getCampus().getIdInternal().toString();
	}
	return this.campusID;
    }

    public void setCampusID(String campusID) {
        this.campusID = campusID;
    }    

    public String getUnitRelationTypeValue() {
	return unitRelationTypeValue;
    }

    public void setUnitRelationTypeValue(String unitRelationTypeValue) {
	this.unitRelationTypeValue = unitRelationTypeValue;
    }

    public HashMap<Integer, String> getUnitRelationsAccountabilityTypes() {
	if (unitRelationsAccountabilityTypes == null) {
	    unitRelationsAccountabilityTypes = new HashMap<Integer, String>();
	}
	return unitRelationsAccountabilityTypes;
    }

    public void setUnitRelationsAccountabilityTypes(
	    HashMap<Integer, String> unitRelationsAccountabilityTypes) {
	this.unitRelationsAccountabilityTypes = unitRelationsAccountabilityTypes;
    }

    public Boolean getToRemoveParentUnit() {
	return toRemoveParentUnit;
    }

    public void setToRemoveParentUnit(Boolean toRemoveParentUnit) {
	this.toRemoveParentUnit = toRemoveParentUnit;
    }

    public class PrepareDatesResult {

	private boolean test;

	private Date beginDate, endDate;

	public PrepareDatesResult(boolean test, Date beginDate, Date endDate) {
	    this.test = test;
	    this.beginDate = beginDate;
	    this.endDate = endDate;
	}

	public YearMonthDay getBeginDate() {
	    return beginDate != null ? YearMonthDay.fromDateFields(beginDate) : null;
	}

	public YearMonthDay getEndDate() {
	    return endDate != null ? YearMonthDay.fromDateFields(endDate) : null;
	}

	public boolean isTest() {
	    return test;
	}
    }

    public class CreateNewUnitParameters {

	private Integer degreeID, departmentID, administrativeOfficeID, campusID;
	
	public CreateNewUnitParameters(OrganizationalStructureBackingBean bean, int mode)
		throws NumberFormatException, FenixFilterException, FenixServiceException {

	    if (mode == 1) {
		this.departmentID = (bean.getDepartmentID() != null && !bean.getDepartmentID().equals("#")) ? Integer.valueOf(bean.getDepartmentID()) : null;
		this.degreeID = (bean.getDegreeID() != null && !bean.getDegreeID().equals("#")) ? Integer.valueOf(bean.getDegreeID()) : null;
		this.administrativeOfficeID = (bean.getAdministrativeOfficeID() != null && !bean.getAdministrativeOfficeID().equals("#")) ? Integer.valueOf(bean.getAdministrativeOfficeID()) : null;
		this.campusID = (bean.getCampusID() != null && !bean.getCampusID().equals("#")) ? Integer.valueOf(bean.getCampusID()) : null;
				
	    } else if (mode == 2) {
		this.departmentID = (bean.getUnit().getDepartment() != null) ? bean.getUnit().getDepartment().getIdInternal() : null;
		this.degreeID = (bean.getUnit().getDegree() != null) ? bean.getUnit().getDegree().getIdInternal() : null;
		this.administrativeOfficeID = (bean.getUnit().getAdministrativeOffice() != null) ? bean.getUnit().getAdministrativeOffice().getIdInternal() : null;
		this.campusID = (bean.getUnit().getCampus() != null) ? bean.getUnit().getCampus().getIdInternal() : null;
	    }
	}

	public Integer getDegreeID() {
	    return degreeID;
	}

	public Integer getDepartmentID() {
	    return departmentID;
	}

	public Integer getAdministrativeOfficeID() {
	    return administrativeOfficeID;
	}

	public Integer getCampusID() {
	    return campusID;
	}
    }

    public Integer getAccountabilityID() {
	return accountabilityID;
    }

    public void setAccountabilityID(Integer accountabilityID) {
	this.accountabilityID = accountabilityID;
    }

    public Boolean getViewExternalUnits() {
	return viewExternalUnits;
    }

    public void setViewExternalUnits(Boolean viewExternalUnits) {
	this.viewExternalUnits = viewExternalUnits;
    }
    
    public Boolean getViewUnitsWithoutParents() {
        return viewUnitsWithoutParents;
    }

    public void setViewUnitsWithoutParents(Boolean viewUnitsWithoutParents) {
        this.viewUnitsWithoutParents = viewUnitsWithoutParents;
    }     

    public Boolean getExternalInstitutionUnit() throws FenixFilterException, FenixServiceException {
	if (getUnit() != null && UnitUtils.readExternalInstitutionUnit() != null
		&& UnitUtils.readExternalInstitutionUnit().equals(getUnit())) {
	    this.externalInstitutionUnit = Boolean.TRUE;
	}
	return externalInstitutionUnit;
    }

    public void setExternalInstitutionUnit(Boolean externalInstitutionUnit) throws FenixFilterException,
	    FenixServiceException {

	if (externalInstitutionUnit
		&& getUnit() != null
		&& (UnitUtils.readExternalInstitutionUnit() == null || !UnitUtils
			.readExternalInstitutionUnit().equals(getUnit()))) {

	    final Object[] argsToRead = { getUnit(), Boolean.FALSE};
	    ServiceUtils.executeService( "SetRootUnit", argsToRead);
	    this.externalInstitutionUnit = externalInstitutionUnit;
	}
    }

    public Boolean getInstitutionUnit() throws FenixFilterException, FenixServiceException {
	if (getUnit() != null && UnitUtils.readInstitutionUnit() != null
		&& UnitUtils.readInstitutionUnit().equals(getUnit())) {
	    this.institutionUnit = Boolean.TRUE;
	}
	return institutionUnit;
    }

    public void setInstitutionUnit(Boolean institutionUnit) throws FenixFilterException,
	    FenixServiceException {

	if (institutionUnit
		&& getUnit() != null
		&& (UnitUtils.readInstitutionUnit() == null || !UnitUtils.readInstitutionUnit().equals(
			getUnit()))) {

	    final Object[] argsToRead = { getUnit(), Boolean.TRUE };
	    ServiceUtils.executeService( "SetRootUnit", argsToRead);
	    this.institutionUnit = institutionUnit;
	}
    }

    public Boolean getEarthUnit() throws FenixFilterException, FenixServiceException {
	if (getUnit() != null && UnitUtils.readEarthUnit() != null && getUnit() == UnitUtils.readEarthUnit()) {
	    this.earthUnit = Boolean.TRUE;
	}
        return this.earthUnit;
    }

    public void setEarthUnit(Boolean earthUnit) throws FenixFilterException, FenixServiceException {
	if (earthUnit && getUnit() != null && UnitUtils.readEarthUnit() != getUnit()) {
	    ServiceUtils.executeService( "SetRootUnit", new Object[] {getUnit(), null});
	    this.earthUnit = earthUnit;
	} 
    }

    public Boolean getCanBeResponsibleOfSpaces() throws FenixFilterException, FenixServiceException {
	if (this.canBeResponsibleOfSpaces == null && this.getChooseUnit() != null) {
	    this.canBeResponsibleOfSpaces = this.getChooseUnit().getCanBeResponsibleOfSpaces();
	}	
        return canBeResponsibleOfSpaces;
    }

    public void setCanBeResponsibleOfSpaces(Boolean toBeResponsibleOfSpaces) {
        this.canBeResponsibleOfSpaces = toBeResponsibleOfSpaces;
    }
    
    public String getParentUnitsLinks() throws FenixFilterException, FenixServiceException {
	StringBuilder result = new StringBuilder();
	Unit unit = this.getUnit();
	List<Unit> parentUnitsPathWithoutAggregateUnits = unit.getParentUnitsPath();
	int index = 1;
	for (Unit parentUnit : parentUnitsPathWithoutAggregateUnits) {
	    if (index == parentUnitsPathWithoutAggregateUnits.size()) {
		result.append("<a href=\"").append(getContextPath()).append("/manager/organizationalStructureManagament/unitDetails.faces?unitID=").append(parentUnit.getIdInternal()).append("\">");
		result.append(parentUnit.getNameWithAcronym());
		result.append("</a>");
	    } else {
		result.append("<a href=\"").append(getContextPath()).append("/manager/organizationalStructureManagament/unitDetails.faces?unitID=").append(parentUnit.getIdInternal()).append("\">");
		result.append(parentUnit.getNameWithAcronym());
		result.append("</a> - ");
	    }
	    index++;	    	   
	}
	return result.toString();
    }

    public String getFunctionNameEn() throws FenixFilterException, FenixServiceException {
	if (this.functionNameEn == null && this.getFunction() != null) {
	    this.functionNameEn = this.getFunction().getTypeName().getContent(Language.en);
	}
	return functionNameEn;
    }

    public void setFunctionNameEn(String functionNameEn) {
        this.functionNameEn = functionNameEn;
    }

    public String getUnitNameEn() throws FenixFilterException, FenixServiceException {
	if (this.unitNameEn == null && this.getChooseUnit() != null) {
	    this.unitNameEn = this.getChooseUnit().getPartyName().getContent(Language.en);
	}
	return unitNameEn;	
    }

    public void setUnitNameEn(String unitNameEn) {
        this.unitNameEn = unitNameEn;
    }
    
    private boolean hasKey(ResourceBundle bundle, String key) {
	Enumeration<String> keys = bundle.getKeys();
	while(keys.hasMoreElements()) {
	    String nextKey = keys.nextElement();
	    if(nextKey.equals(key)) {
		return true;
	    }
	}
	return false;
    }
}