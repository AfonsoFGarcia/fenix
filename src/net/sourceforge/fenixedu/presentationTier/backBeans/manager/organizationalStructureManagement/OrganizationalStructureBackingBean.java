/*
 * Created on Nov 21, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.manager.organizationalStructureManagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class OrganizationalStructureBackingBean extends FenixBackingBean {

    private String unitName, unitCostCenter, unitTypeName, unitBeginDate, unitEndDate, unitAcronym;

    private String functionName, functionTypeName, functionBeginDate, functionEndDate, unitWebAddress,
            unitRelationTypeValue;

    private String listingTypeValueToUnits, listingTypeValueToFunctions, departmentID, degreeID;

    private Integer principalFunctionID;

    private Unit unit, chooseUnit;

    private Function function;

    private HtmlInputHidden unitIDHidden, chooseUnitIDHidden, functionIDHidden,
            listingTypeValueToFunctionsHidden, listingTypeValueToUnitsHidden;

    private HashMap<Integer, String> unitRelationsAccountabilityTypes;

    private Boolean toRemoveParentUnit;

    public OrganizationalStructureBackingBean() {
        if (getRequestParameter("unitID") != null
                && !getRequestParameter("unitID").toString().equals("")) {
            getUnitIDHidden().setValue(Integer.valueOf(getRequestParameter("unitID").toString()));
        }
        if (getRequestParameter("chooseUnitID") != null
                && !getRequestParameter("chooseUnitID").toString().equals("")) {
            getChooseUnitIDHidden().setValue(
                    Integer.valueOf(getRequestParameter("chooseUnitID").toString()));
        }
        if (getRequestParameter("functionID") != null
                && !getRequestParameter("functionID").toString().equals("")) {
            getFunctionIDHidden()
                    .setValue(Integer.valueOf(getRequestParameter("functionID").toString()));
        }
        if (getRequestParameter("principalFunctionID") != null
                && !getRequestParameter("principalFunctionID").toString().equals("")) {
            this.principalFunctionID = Integer.valueOf(getRequestParameter("principalFunctionID")
                    .toString());
        }
        if (getRequestParameter("isToRemoveParentUnit") != null
                && !getRequestParameter("isToRemoveParentUnit").toString().equals("")) {
            this.toRemoveParentUnit = Boolean.valueOf(getRequestParameter("isToRemoveParentUnit")
                    .toString());
        } else {
            this.toRemoveParentUnit = false;
        }
    }

    public List<Unit> getAllSubUnits() throws FenixFilterException, FenixServiceException {
        YearMonthDay currentDate = new YearMonthDay();
        boolean active = this.getListingTypeValueToUnitsHidden().getValue().toString().equals("0");
        return getSubUnits(active, this.getUnit(), currentDate);
    }

    public List<Function> getAllNonInherentFunctions() throws FenixFilterException,
            FenixServiceException {

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

    public String getAllUnitsToChooseParentUnit() throws FenixFilterException, FenixServiceException,
            ExcepcaoPersistencia {

        StringBuilder buffer = new StringBuilder();
        YearMonthDay currentDate = new YearMonthDay();
        if (this.getUnit().getType() != null
                && this.getUnit().getType().equals(PartyTypeEnum.EXTERNAL_INSTITUTION)) {
            getUnitTreeToChooseParentUnit(buffer, UnitUtils.readExternalInstitutionUnit(), currentDate);
        } else {
            getUnitTreeToChooseParentUnit(buffer, UnitUtils.readInstitutionUnit(), currentDate);
        }
        return buffer.toString();
    }

    public void getUnitTreeToChooseParentUnit(StringBuilder buffer, Unit parentUnit,
            YearMonthDay currentDate) throws FenixFilterException, FenixServiceException {
        buffer.append("<ul class='padding1 nobullet'>");
        getUnitsListToChooseParentUnit(parentUnit, buffer, currentDate);
        closeULTag(buffer);
    }

    private void getUnitsListToChooseParentUnit(Unit parentUnit, StringBuilder buffer,
            YearMonthDay currentDate) throws FenixFilterException, FenixServiceException {

        openLITag(buffer);

        List<Unit> subUnits = null;
        if (this.getUnit().isActive(currentDate)) {
            subUnits = getSubUnits(true, parentUnit, currentDate);
        } else {
            subUnits = getSubUnits(false, parentUnit, currentDate);
        }

        if (!subUnits.isEmpty()) {
            putImage(parentUnit, buffer);
        }

        buffer.append("<a href=\"").append(getContextPath()).append(
                "/manager/organizationalStructureManagament/").append("chooseParentUnit.faces?").append(
                "unitID=").append(this.getUnit().getIdInternal()).append("&chooseUnitID=").append(
                parentUnit.getIdInternal()).append("\">").append(parentUnit.getName()).append("</a>")
                .append("</li>");

        if (!subUnits.isEmpty()) {
            openULTag(parentUnit, buffer);
            Collections.sort(subUnits, new BeanComparator("name"));
        }

        for (Unit subUnit : subUnits) {
            if (!subUnit.equals(this.getUnit())) {
                getUnitsListToChooseParentUnit(subUnit, buffer, currentDate);
            }
        }

        if (!subUnits.isEmpty()) {
            closeULTag(buffer);
        }
    }

    private List<Unit> getSubUnits(boolean active, Unit unit, YearMonthDay currentDate) {
        return (active) ? unit.getActiveSubUnits(currentDate) : unit.getInactiveSubUnits(currentDate);
    }

    private List<Unit> getAllSubUnits(boolean active, Unit unit, YearMonthDay currentDate) {
        return (active) ? unit.getAllActiveSubUnits(currentDate) : unit
                .getAllInactiveSubUnits(currentDate);
    }

    public String getUnits() throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {
        StringBuilder buffer = new StringBuilder();
        List<Unit> allUnitsWithoutParent = UnitUtils.readAllUnitsWithoutParents();
        Collections.sort(allUnitsWithoutParent, new BeanComparator("name"));
        YearMonthDay currentDate = new YearMonthDay();

        for (Unit unit : allUnitsWithoutParent) {
            boolean active = this.getListingTypeValueToUnitsHidden().getValue().toString().equals("0");
            if (active) {
                if (unit.isActive(currentDate) || !getAllSubUnits(active, unit, currentDate).isEmpty()) {
                    getUnitTree(buffer, unit, getSubUnits(active, unit, currentDate), currentDate,
                            active);
                }
            } else {
                if (!unit.isActive(currentDate) || !getAllSubUnits(active, unit, currentDate).isEmpty()) {
                    getUnitTree(buffer, unit, getSubUnits(active, unit, currentDate), currentDate,
                            active);
                }
            }
        }

        return buffer.toString();
    }

    public void getUnitTree(StringBuilder buffer, Unit parentUnit, List<Unit> subUnits,
            YearMonthDay currentDate, boolean active) {
        buffer.append("<ul class='padding1 nobullet'>");
        getUnitsList(parentUnit, subUnits, buffer, currentDate, active);
        buffer.append("</ul>");
    }

    private void getUnitsList(Unit parentUnit, List<Unit> subUnits, StringBuilder buffer,
            YearMonthDay currentDate, boolean active) {

        openLITag(buffer);

        if (!subUnits.isEmpty()) {
            putImage(parentUnit, buffer);
        }

        buffer.append("<a href=\"").append(getContextPath()).append(
                "/manager/organizationalStructureManagament/").append("unitDetails.faces?").append(
                "unitID=").append(parentUnit.getIdInternal()).append("\">").append(parentUnit.getName())
                .append("</a>").append("</li>");

        if (!subUnits.isEmpty()) {
            openULTag(parentUnit, buffer);
            Collections.sort(subUnits, new BeanComparator("name"));
        }

        for (Unit subUnit : subUnits) {
            getUnitsList(subUnit, getSubUnits(active, subUnit, currentDate), buffer, currentDate, active);
        }

        if (!subUnits.isEmpty()) {
            closeULTag(buffer);
        }
    }

    public String getUnitsToChoosePrincipalFunction() throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {

        YearMonthDay currentDate = new YearMonthDay();
        StringBuilder buffer = new StringBuilder();
        getUnitTreeToChoosePrincipalFunction(buffer, UnitUtils.readInstitutionUnit(), currentDate);
        return buffer.toString();
    }

    public void getUnitTreeToChoosePrincipalFunction(StringBuilder buffer, Unit parentUnit,
            YearMonthDay currentDate) throws FenixFilterException, FenixServiceException {
        buffer.append("<ul class='padding1 nobullet'>");
        getUnitsListToChoosePrincipalFunction(parentUnit, buffer, currentDate);
        buffer.append("</ul>");
    }

    private void getUnitsListToChoosePrincipalFunction(Unit parentUnit, StringBuilder buffer,
            YearMonthDay currentDate) throws FenixFilterException, FenixServiceException {

        openLITag(buffer);

        List<Unit> subUnits = null;
        if (this.getFunction().isActive(currentDate)) {
            subUnits = getSubUnits(true, parentUnit, currentDate);
        } else {
            subUnits = getSubUnits(false, parentUnit, currentDate);
        }

        if (!subUnits.isEmpty()) {
            putImage(parentUnit, buffer);
        }

        buffer.append("<a href=\"").append(getContextPath()).append(
                "/manager/organizationalStructureManagament/").append("chooseFunction.faces?").append(
                "unitID=").append(this.getUnit().getIdInternal()).append("&chooseUnitID=").append(
                parentUnit.getIdInternal()).append("&functionID=").append(
                this.getFunction().getIdInternal()).append("\">").append(parentUnit.getName()).append(
                "</a>").append("</li>");

        if (!subUnits.isEmpty()) {
            openULTag(parentUnit, buffer);
            Collections.sort(subUnits, new BeanComparator("name"));
        }

        for (Unit subUnit : subUnits) {
            getUnitsListToChoosePrincipalFunction(subUnit, buffer, currentDate);
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
            selectItem.setLabel(bundle.getString(type.getName()));
            selectItem.setValue(type.getName());
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
                if (degree.getBolonhaDegreeType().equals(BolonhaDegreeType.DEGREE)) {
                    selectItem.setLabel("(L-B) " + degree.getNome());
                } else if (degree.getBolonhaDegreeType().equals(BolonhaDegreeType.MASTER_DEGREE)) {
                    selectItem.setLabel("(M-B) " + degree.getNome());
                } else if (degree.getBolonhaDegreeType().equals(
                        BolonhaDegreeType.INTEGRATED_MASTER_DEGREE)) {
                    selectItem.setLabel("(MI) " + degree.getNome());
                } else if (degree.getBolonhaDegreeType().equals(
                        BolonhaDegreeType.ADVANCED_FORMATION_DIPLOMA)) {
                    selectItem.setLabel("(DFA) " + degree.getNome());
                } else if (degree.getBolonhaDegreeType().equals(
                        BolonhaDegreeType.ADVANCED_STUDIES_DIPLOMA)) {
                    selectItem.setLabel("(DEA) " + degree.getNome());
                } else if (degree.getBolonhaDegreeType().equals(BolonhaDegreeType.SPECIALIZATION_DEGREE)) {
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
            selectItem.setLabel(bundle.getString(type.getName()));
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
            selectItem = new SelectItem();
            selectItem.setLabel(bundle.getString(type.getName()));
            selectItem.setValue(type.getName());
            list.add(selectItem);
        }
        Collections.sort(list, new BeanComparator("label"));

        addDefaultSelectedItem(list, bundle);

        return list;
    }

    private void addDefaultSelectedItem(List<SelectItem> list, ResourceBundle bundle) {
        SelectItem firstItem = new SelectItem();
        firstItem.setLabel(bundle.getString("dropDown.Default"));
        firstItem.setValue("#");
        list.add(0, firstItem);
    }

    public String createTopUnit() throws FenixFilterException, FenixServiceException {

        PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        PartyTypeEnum type = getUnitType();
        CreateNewUnitParameters parameters = new CreateNewUnitParameters(this, 1);

        final Object[] argsToRead = { null, this.getUnitName(), this.getUnitCostCenter(),
                this.getUnitAcronym(), datesResult.getBeginDate(), datesResult.getEndDate(), type,
                parameters.getDepartmentID(), parameters.getDegreeID(), null, this.getUnitWebAddress() };

        return executeUnitsManagementService(argsToRead, "listAllUnits", "CreateUnit");
    }

    public String createSubUnit() throws FenixFilterException, FenixServiceException {

        if (getUnitRelationTypeValue().equals("#")) {
            ResourceBundle bundle = getResourceBundle("resources/ManagerResources");
            addErrorMessage(bundle.getString("error.no.unit.relation.type"));
            return "";
        }

        PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        AccountabilityType accountabilityType = AccountabilityType
                .readAccountabilityTypeByType(AccountabilityTypeEnum.valueOf(getUnitRelationTypeValue()));

        PartyTypeEnum type = getUnitType();
        CreateNewUnitParameters parameters = new CreateNewUnitParameters(this, 1);

        final Object[] argsToRead = { this.getUnit().getIdInternal(), this.getUnitName(),
                this.getUnitCostCenter(), this.getUnitAcronym(), datesResult.getBeginDate(),
                datesResult.getEndDate(), type, parameters.getDepartmentID(), parameters.getDegreeID(),
                accountabilityType, this.getUnitWebAddress() };

        return executeUnitsManagementService(argsToRead, "backToUnitDetails", "CreateUnit");
    }

    public String editUnit() throws FenixFilterException, FenixServiceException {
        PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        PartyTypeEnum type = getUnitType();
        CreateNewUnitParameters parameters = new CreateNewUnitParameters(this, 1);

        final Object[] argsToRead = { this.getChooseUnit().getIdInternal(), this.getUnitName(),
                this.getUnitCostCenter(), this.getUnitAcronym(), datesResult.getBeginDate(),
                datesResult.getEndDate(), type, parameters.getDepartmentID(), parameters.getDegreeID(),
                this.getUnitWebAddress() };

        return executeUnitsManagementService(argsToRead, "backToUnitDetails", "EditUnit");
    }

    private PartyTypeEnum getUnitType() throws FenixFilterException, FenixServiceException {
        PartyTypeEnum type = null;
        if (!this.getUnitTypeName().equals("#")) {
            type = PartyTypeEnum.valueOf(this.getUnitTypeName());
        }
        return type;
    }

    public String associateParentUnit() throws FenixFilterException, FenixServiceException {
        if (getUnitRelationTypeValue().equals("#")) {
            ResourceBundle bundle = getResourceBundle("resources/ManagerResources");
            addErrorMessage(bundle.getString("error.no.unit.relation.type"));
            return "";
        }

        AccountabilityType accountabilityType = AccountabilityType
                .readAccountabilityTypeByType(AccountabilityTypeEnum.valueOf(getUnitRelationTypeValue()));

        final Object[] argsToRead = { this.getUnit().getIdInternal(),
                this.getChooseUnit().getIdInternal(), accountabilityType };

        return executeUnitsManagementService(argsToRead, "backToUnitDetails", "AssociateParentUnit");
    }

    public String disassociateParentUnit() throws FenixFilterException, FenixServiceException {
        final Object[] argsToRead = { this.getUnit().getIdInternal(),
                this.getChooseUnit().getIdInternal() };

        return executeUnitsManagementService(argsToRead, "backToUnitDetails", "DisassociateParentUnit");
    }

    private String executeUnitsManagementService(final Object[] argsToRead, String defaultReturn,
            String serviceName) throws FenixFilterException {
        try {
            ServiceUtils.executeService(getUserView(), serviceName, argsToRead);
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

        PrepareDatesResult datesResult = prepareDates(this.getFunctionBeginDate(), this
                .getFunctionEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        FunctionType type = getFunctionType();
        final Object[] argsToRead = { this.getFunctionName(), datesResult.getBeginDate(),
                datesResult.getEndDate(), type, this.getUnit().getIdInternal() };

        return executeFunctionsManagementService(argsToRead, "CreateFunction");
    }

    public String editFunction() throws FenixFilterException, FenixServiceException {

        PrepareDatesResult datesResult = prepareDates(this.getFunctionBeginDate(), this
                .getFunctionEndDate());

        if (datesResult.isTest()) {
            return "";
        }

        FunctionType type = getFunctionType();
        final Object[] argsToRead = { this.getFunction().getIdInternal(), this.getFunctionName(),
                datesResult.getBeginDate(), datesResult.getEndDate(), type };

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
            ServiceUtils.executeService(getUserView(), serviceName, argsToRead);
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
            ServiceUtils.executeService(getUserView(), "RemoveParentInherentFunction", argsToRead);
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return "";
    }

    public String deleteSubUnit() throws FenixFilterException {
        final Object[] argsToRead = { Integer
                .valueOf(this.getChooseUnitIDHidden().getValue().toString()) };
        try {
            ServiceUtils.executeService(getUserView(), "DeleteUnit", argsToRead);

        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e1) {
            setErrorMessage(e1.getMessage());
            return "";
        }
        return "";
    }

    public String deleteUnit() throws FenixFilterException {
        final Object[] argsToRead = { Integer
                .valueOf(this.getChooseUnitIDHidden().getValue().toString()) };
        try {
            ServiceUtils.executeService(getUserView(), "DeleteUnit", argsToRead);

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
            ServiceUtils.executeService(getUserView(), "DeleteFunction", argsToRead);

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

    private void openULTag(Unit parentUnit, StringBuilder buffer) {
        buffer.append("<ul class='mvert0' id=\"").append("aa").append(parentUnit.getIdInternal())
                .append("\" ").append("style='display:none'>\r\n");
    }

    private void putImage(Unit parentUnit, StringBuilder buffer) {
        buffer.append("<img ").append("src='").append(getContextPath()).append(
                "/images/toggle_plus10.gif' id=\"").append(parentUnit.getIdInternal()).append("\" ")
                .append("indexed='true' onClick=\"").append("check(document.getElementById('").append(
                        "aa").append(parentUnit.getIdInternal()).append("'),document.getElementById('")
                .append(parentUnit.getIdInternal()).append("'));return false;").append("\"> ");
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

    private void getParentUnitsRelationTypes() {
        ResourceBundle bundle = getResourceBundle("resources/EnumerationResources");
        getUnitRelationsAccountabilityTypes().clear();
        for (Accountability accountability : unit.getParentsSet()) {
            if (accountability.getParentParty() instanceof Unit) {
                getUnitRelationsAccountabilityTypes().put(
                        accountability.getParentParty().getIdInternal(),
                        bundle.getString(accountability.getAccountabilityType().getType().getName()));
            }
        }
    }

    private void getSubUnitsRelationTypes() {
        ResourceBundle bundle = getResourceBundle("resources/EnumerationResources");
        getUnitRelationsAccountabilityTypes().clear();
        for (Accountability accountability : unit.getChildsSet()) {
            if (accountability.getChildParty() instanceof Unit) {
                getUnitRelationsAccountabilityTypes().put(
                        accountability.getChildParty().getIdInternal(),
                        bundle.getString(accountability.getAccountabilityType().getType().getName()));
            }
        }
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    private String processDate(Date date) throws FenixFilterException, FenixServiceException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return (day + "/" + month + "/" + year);
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
            this.functionName = this.getFunction().getName();
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

        public Date getBeginDate() {
            return beginDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public boolean isTest() {
            return test;
        }
    }

    public class CreateNewUnitParameters {

        private Integer degreeID, departmentID;

        public CreateNewUnitParameters(OrganizationalStructureBackingBean bean, int mode)
                throws NumberFormatException, FenixFilterException, FenixServiceException {

            if (mode == 1) {
                this.departmentID = (bean.getDepartmentID() != null && !bean.getDepartmentID().equals(
                        "#")) ? Integer.valueOf(bean.getDepartmentID()) : null;
                this.degreeID = (bean.getDegreeID() != null && !bean.getDegreeID().equals("#")) ? Integer
                        .valueOf(bean.getDegreeID())
                        : null;
            } else if (mode == 2) {
                this.departmentID = (bean.getUnit().getDepartment() != null) ? bean.getUnit()
                        .getDepartment().getIdInternal() : null;
                this.degreeID = (bean.getUnit().getDegree() != null) ? bean.getUnit().getDegree()
                        .getIdInternal() : null;
            }
        }

        public Integer getDegreeID() {
            return degreeID;
        }

        public Integer getDepartmentID() {
            return departmentID;
        }
    }
}