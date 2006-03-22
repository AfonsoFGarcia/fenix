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
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;

public class OrganizationalStructureBackingBean extends FenixBackingBean {

    private String unitName, unitCostCenter, unitTypeName, unitBeginDate, unitEndDate, unitAcronym;

    private String functionName, functionTypeName, functionBeginDate, functionEndDate;

    private String listingTypeValueToUnits, listingTypeValueToFunctions, departmentID, degreeID;

    private Integer principalFunctionID;

    private Unit unit, chooseUnit;

    private Function function;

    private HtmlInputHidden unitIDHidden, chooseUnitIDHidden, functionIDHidden,
            listingTypeValueToFunctionsHidden, listingTypeValueToUnitsHidden;

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
    }

    public List<Unit> getAllSubUnits() throws FenixFilterException, FenixServiceException {
        List<Unit> allSubUnits = new ArrayList<Unit>();
        Date currentDate = Calendar.getInstance().getTime();
        for (Unit unit : this.getUnit().getSubUnits()) {
            if ((this.getListingTypeValueToUnitsHidden().getValue().toString().equals("0") && unit
                    .isActive(currentDate))
                    || (this.getListingTypeValueToUnitsHidden().getValue().toString().equals("1") && !unit
                            .isActive(currentDate))) {
                allSubUnits.add(unit);
            }
        }
        return allSubUnits;
    }

    public List<Function> getAllNonInherentFunctions() throws FenixFilterException,
            FenixServiceException {

        List<Function> allNonInherentFunctions = new ArrayList<Function>();
        Date currentDate = Calendar.getInstance().getTime();
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
        Date currentDate = Calendar.getInstance().getTime();
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

    private List<Unit> readAllUnits() throws FenixServiceException, FenixFilterException {
        return RootDomainObject.readAllUnits();
    }

    public String getAllUnitsToChooseParentUnit() throws FenixFilterException, FenixServiceException,
            ExcepcaoPersistencia {
        StringBuilder buffer = new StringBuilder();
        List<Unit> allUnitsWithoutParent = UnitUtils.readAllUnitsWithoutParents();
        Collections.sort(allUnitsWithoutParent, new BeanComparator("name"));
        for (Unit unit : allUnitsWithoutParent) {
            if (!unit.equals(this.getUnit())) {
                getUnitTreeToChooseParentUnit(buffer, unit);
            }
        }
        return buffer.toString();
    }

    public void getUnitTreeToChooseParentUnit(StringBuilder buffer, Unit parentUnit)
            throws FenixFilterException, FenixServiceException {
        buffer.append("<ul class='padding1 nobullet'>");
        getUnitsListToChooseParentUnit(parentUnit, buffer);
        closeULTag(buffer);
    }

    private void getUnitsListToChooseParentUnit(Unit parentUnit, StringBuilder buffer)
            throws FenixFilterException, FenixServiceException {

        openLITag(buffer);

        if (parentUnit.hasAnySubUnits()) {
            putImage(parentUnit, buffer);
        }

        buffer.append("<a href=\"").append(getContextPath()).append(
                "/manager/organizationalStructureManagament/").append("chooseParentUnit.faces?").append(
                "unitID=").append(this.getUnit().getIdInternal()).append("&chooseUnitID=").append(
                parentUnit.getIdInternal()).append("\">").append(parentUnit.getName()).append("</a>")
                .append("</li>");

        if (parentUnit.hasAnySubUnits()) {
            openULTag(parentUnit, buffer);
        }

        for (Unit subUnit : parentUnit.getSubUnits()) {
            if (!subUnit.equals(this.getUnit())) {
                getUnitsListToChooseParentUnit(subUnit, buffer);
            }
        }

        if (parentUnit.hasAnySubUnits()) {
            closeULTag(buffer);
        }
    }

    public String getUnits() throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {
        StringBuilder buffer = new StringBuilder();
        List<Unit> allUnitsWithoutParent = UnitUtils.readAllUnitsWithoutParents();
        Collections.sort(allUnitsWithoutParent, new BeanComparator("name"));
        Date currentDate = Calendar.getInstance().getTime();

        for (Unit unit : allUnitsWithoutParent) {
            if (this.getListingTypeValueToUnitsHidden().getValue().toString().equals("0")
                    && (unit.isActive(currentDate) || !unit.getAllActiveSubUnits(currentDate).isEmpty())) {
                getUnitTree(buffer, unit, unit.getActiveSubUnits(currentDate), currentDate, true);
            } else if (this.getListingTypeValueToUnitsHidden().getValue().toString().equals("1")
                    && (!unit.isActive(currentDate) || !unit.getAllInactiveSubUnits(currentDate)
                            .isEmpty())) {
                getUnitTree(buffer, unit, unit.getInactiveSubUnits(currentDate), currentDate, false);
            }
        }

        return buffer.toString();
    }

    public void getUnitTree(StringBuilder buffer, Unit parentUnit, List<Unit> subUnits,
            Date currentDate, boolean active) {
        buffer.append("<ul class='padding1 nobullet'>");
        getUnitsList(parentUnit, subUnits, buffer, currentDate, active);
        buffer.append("</ul>");
    }

    private void getUnitsList(Unit parentUnit, List<Unit> subUnits, StringBuilder buffer,
            Date currentDate, boolean active) {

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
        }

        Collections.sort(subUnits, new BeanComparator("name"));

        for (Unit subUnit : subUnits) {
            if (active) {
                getUnitsList(subUnit, subUnit.getActiveSubUnits(currentDate), buffer, currentDate,
                        active);
            } else {
                getUnitsList(subUnit, subUnit.getInactiveSubUnits(currentDate), buffer, currentDate,
                        active);
            }
        }

        if (parentUnit.hasAnySubUnits()) {
            closeULTag(buffer);
        }
    }

    public String getUnitsToChoosePrincipalFunction() throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {
        StringBuilder buffer = new StringBuilder();
        List<Unit> allUnitsWithoutParent = UnitUtils.readAllUnitsWithoutParents();
        Collections.sort(allUnitsWithoutParent, new BeanComparator("name"));

        for (Unit unit : allUnitsWithoutParent) {
            if (!unit.equals(this.getUnit())) {
                getUnitTreeToChoosePrincipalFunction(buffer, unit);
            }
        }

        return buffer.toString();
    }

    public void getUnitTreeToChoosePrincipalFunction(StringBuilder buffer, Unit parentUnit)
            throws FenixFilterException, FenixServiceException {
        buffer.append("<ul class='padding1 nobullet'>");
        getUnitsListToChoosePrincipalFunction(parentUnit, buffer);
        buffer.append("</ul>");
    }

    private void getUnitsListToChoosePrincipalFunction(Unit parentUnit, StringBuilder buffer)
            throws FenixFilterException, FenixServiceException {

        openLITag(buffer);

        if (parentUnit.hasAnySubUnits()) {
            putImage(parentUnit, buffer);
        }

        buffer.append("<a href=\"").append(getContextPath()).append(
                "/manager/organizationalStructureManagament/").append("chooseFunction.faces?").append(
                "unitID=").append(this.getUnit().getIdInternal()).append("&chooseUnitID=").append(
                parentUnit.getIdInternal()).append("&functionID=").append(
                this.getFunction().getIdInternal()).append("\">").append(parentUnit.getName()).append(
                "</a>").append("</li>");

        if (parentUnit.hasAnySubUnits()) {
            openULTag(parentUnit, buffer);
        }

        for (Unit subUnit : parentUnit.getSubUnits()) {
            if (!subUnit.equals(this.getUnit())) {
                getUnitsListToChoosePrincipalFunction(subUnit, buffer);
            }
        }

        if (parentUnit.hasAnySubUnits()) {
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

        List<Department> allDepartments = readAllDomainObjects(Department.class);

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

        List<Degree> allDegrees = readAllDomainObjects(Degree.class);

        for (Degree degree : allDegrees) {
            selectItem = new SelectItem();
            if (degree.getTipoCurso() != null) {
                if (degree.getTipoCurso().equals(DegreeType.DEGREE)) {
                    selectItem.setLabel("(L) " + degree.getNome());
                } else if (degree.getTipoCurso().equals(DegreeType.MASTER_DEGREE)) {
                    selectItem.setLabel("(M) " + degree.getNome());
                }
            } else if (degree.getBolonhaDegreeType() != null) {
                if (degree.getBolonhaDegreeType().equals(BolonhaDegreeType.DEGREE)) {
                    selectItem.setLabel("(L) " + degree.getNome());
                } else if (degree.getBolonhaDegreeType().equals(BolonhaDegreeType.MASTER_DEGREE)) {
                    selectItem.setLabel("(M) " + degree.getNome());
                } else if (degree.getBolonhaDegreeType().equals(
                        BolonhaDegreeType.INTEGRATED_MASTER_DEGREE)) {
                    selectItem.setLabel("(MI) " + degree.getNome());
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

    private void addDefaultSelectedItem(List<SelectItem> list, ResourceBundle bundle) {
        SelectItem firstItem = new SelectItem();
        firstItem.setLabel(bundle.getString("dropDown.Default"));
        firstItem.setValue("#");
        list.add(0, firstItem);
    }

    public String createTopUnit() throws FenixFilterException, FenixServiceException {

        if (verifyCostCenterCode()) {
            return "";
        }

        PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        PartyTypeEnum type = getUnitType();
        CreateNewUnitParameters parameters = new CreateNewUnitParameters(this, 1);

        final Object[] argsToRead = { null, null, this.getUnitName(), this.getUnitCostCenter(),
                this.getUnitAcronym(), datesResult.getBeginDate(), datesResult.getEndDate(), type,
                parameters.getDepartmentID(), parameters.getDegreeID() };

        return executeCreateNewUnitService(argsToRead, "listAllUnits");
    }

    public String createSubUnit() throws FenixFilterException, FenixServiceException {

        if (verifyCostCenterCode()) {
            return "";
        }

        PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        PartyTypeEnum type = getUnitType();
        CreateNewUnitParameters parameters = new CreateNewUnitParameters(this, 1);

        final Object[] argsToRead = { null, this.getUnit().getIdInternal(), this.getUnitName(),
                this.getUnitCostCenter(), this.getUnitAcronym(), datesResult.getBeginDate(),
                datesResult.getEndDate(), type, parameters.getDepartmentID(), parameters.getDegreeID() };

        return executeCreateNewUnitService(argsToRead, "backToUnitDetails");
    }

    public String editUnit() throws FenixFilterException, FenixServiceException {

        if (verifyCostCenterCode()) {
            return "";
        }

        PrepareDatesResult datesResult = prepareDates(this.getUnitBeginDate(), this.getUnitEndDate());
        if (datesResult.isTest()) {
            return "";
        }

        PartyTypeEnum type = getUnitType();
        CreateNewUnitParameters parameters = new CreateNewUnitParameters(this, 1);

        final Object[] argsToRead = { this.getChooseUnit().getIdInternal(), null, this.getUnitName(),
                this.getUnitCostCenter(), this.getUnitAcronym(), datesResult.getBeginDate(),
                datesResult.getEndDate(), type, parameters.getDepartmentID(), parameters.getDegreeID() };

        return executeCreateNewUnitService(argsToRead, "backToUnitDetails");
    }

    private PartyTypeEnum getUnitType() throws FenixFilterException, FenixServiceException {
        PartyTypeEnum type = null;
        if (!this.getUnitTypeName().equals("#")) {
            type = PartyTypeEnum.valueOf(this.getUnitTypeName());
        }
        return type;
    }

    public String associateParentUnit() throws FenixFilterException, FenixServiceException {

        String costCenterCodeString = getValidCosteCenterCode();

        CreateNewUnitParameters parameters = new CreateNewUnitParameters(this, 2);

        final Object[] argsToRead = { this.getUnit().getIdInternal(),
                this.getChooseUnit().getIdInternal(), this.getUnit().getName(), costCenterCodeString,
                this.getUnitAcronym(), this.getUnit().getBeginDate(), this.getUnit().getEndDate(),
                this.getUnit().getType(), parameters.getDepartmentID(), parameters.getDegreeID() };

        return executeCreateNewUnitService(argsToRead, "backToUnitDetails");
    }

    public String disassociateParentUnit() throws FenixFilterException, FenixServiceException {

        String costCenterCodeString = getValidCosteCenterCode();

        CreateNewUnitParameters parameters = new CreateNewUnitParameters(this, 2);

        final Object[] argsToRead = { this.getUnit().getIdInternal(),
                this.getChooseUnit().getIdInternal(), this.getUnit().getName(), costCenterCodeString,
                this.getUnitAcronym(), this.getUnit().getBeginDate(), this.getUnit().getEndDate(),
                this.getUnit().getType(), parameters.getDepartmentID(), parameters.getDegreeID() };

        return executeCreateNewUnitService(argsToRead, "backToUnitDetails");
    }

    private String executeCreateNewUnitService(final Object[] argsToRead, String defaultReturn)
            throws FenixFilterException {
        try {
            ServiceUtils.executeService(getUserView(), "CreateNewUnit", argsToRead);
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
            return "";
        }

        return defaultReturn;
    }

    private String getValidCosteCenterCode() throws FenixFilterException, FenixServiceException {
        Integer costCenterCode = this.getUnit().getCostCenterCode();
        String costCenterCodeString = null;
        if (costCenterCode != null) {
            costCenterCodeString = costCenterCode.toString();
        }
        return costCenterCodeString;
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

        final Object[] argsToRead = { null, this.getUnit().getIdInternal(), this.getFunctionName(),
                datesResult.getBeginDate(), datesResult.getEndDate(), type, null };

        return executeCreateNewFunctionService(argsToRead);
    }

    public String editFunction() throws FenixFilterException, FenixServiceException {

        PrepareDatesResult datesResult = prepareDates(this.getFunctionBeginDate(), this
                .getFunctionEndDate());

        if (datesResult.isTest()) {
            return "";
        }

        Function parentInherentFunction = this.getFunction().getParentInherentFunction();
        Integer parentInherentFunctionID = null;
        if (parentInherentFunction != null) {
            parentInherentFunctionID = parentInherentFunction.getIdInternal();
        }

        FunctionType type = getFunctionType();

        final Object[] argsToRead = { this.getFunction().getIdInternal(),
                this.getFunction().getUnit().getIdInternal(), this.getFunctionName(),
                datesResult.getBeginDate(), datesResult.getEndDate(), type, parentInherentFunctionID };

        return executeCreateNewFunctionService(argsToRead);
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

    public String associateInherentParentFunction() throws FenixFilterException, FenixServiceException {

        Function function = this.getFunction();

        final Object[] argsToRead = { function.getIdInternal(), function.getUnit().getIdInternal(),
                function.getName(), function.getBeginDate(), function.getEndDate(), function.getType(),
                this.principalFunctionID };

        return executeCreateNewFunctionService(argsToRead);
    }

    private String executeCreateNewFunctionService(final Object[] argsToRead)
            throws FenixFilterException {
        try {
            ServiceUtils.executeService(getUserView(), "CreateNewFunction", argsToRead);
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

        final Object[] argsToRead = { function.getIdInternal(), function.getUnit().getIdInternal(),
                function.getName(), function.getBeginDate(), function.getEndDate(), function.getType(),
                null };

        try {
            ServiceUtils.executeService(getUserView(), "CreateNewFunction", argsToRead);
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

            this.unit = (Unit) readDomainObject(Unit.class, Integer.valueOf(this.getUnitIDHidden()
                    .getValue().toString()));
        }
        return unit;
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

            this.function = (Function) readDomainObject(Function.class, Integer.valueOf(this
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
                && this.getFunction().getType() != null) {
            this.functionTypeName = this.getFunction().getType().getName();
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

            this.chooseUnit = (Unit) readDomainObject(Unit.class, Integer.valueOf(this
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

    private boolean verifyCostCenterCode() throws FenixFilterException, FenixServiceException {
        List<Unit> allUnits = readAllUnits();
        for (Unit unit : allUnits) {
            if (unit.getCostCenterCode() != null && !unit.equals(this.getChooseUnit())
                    && this.getUnitCostCenter().equals(String.valueOf(unit.getCostCenterCode()))
                    && unit.isActive(Calendar.getInstance().getTime())) {
                setErrorMessage("error.costCenter.alreadyExists");
                return true;
            }
        }
        return false;
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