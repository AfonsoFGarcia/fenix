package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeFunctionsAccumulation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeGrantOwnerEquivalent;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeProfessionalContract;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeProfessionalExemption;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeProfessionalRegime;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeProfessionalRelation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeSabbatical;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeServiceExemption;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/professionalInformation", module = "manager")
@Forwards( { @Forward(name = "showProfessionalInformation", path = "/manager/personManagement/contracts/showProfessionalInformation.jsp") })
public class ProfessionalInformationDA extends FenixDispatchAction {

    public ActionForward showSituations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = (Person) rootDomainObject.readPartyByOID(getIntegerFromRequest(request, "personId"));

	List<EmployeeContractSituation> situations = new ArrayList<EmployeeContractSituation>();
	if (person.getEmployee() != null) {
	    for (EmployeeContractSituation employeeContractSituation : person.getEmployee().getEmployeeContractSituations()) {
		if (employeeContractSituation.getAnulationDate() == null) {
		    situations.add(employeeContractSituation);
		}
	    }
	}
	request.setAttribute("situations", situations);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showCategories(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = (Person) rootDomainObject.readPartyByOID(getIntegerFromRequest(request, "personId"));

	List<EmployeeProfessionalCategory> categories = new ArrayList<EmployeeProfessionalCategory>();
	if (person.getEmployee() != null) {
	    for (EmployeeProfessionalCategory employeeProfessionalCategory : person.getEmployee()
		    .getEmployeeProfessionalCategories()) {
		if (employeeProfessionalCategory.getAnulationDate() == null) {
		    categories.add(employeeProfessionalCategory);
		}
	    }
	}
	request.setAttribute("categories", categories);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showRegimes(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = (Person) rootDomainObject.readPartyByOID(getIntegerFromRequest(request, "personId"));

	List<EmployeeProfessionalRegime> regimes = new ArrayList<EmployeeProfessionalRegime>();
	if (person.getEmployee() != null) {
	    for (EmployeeProfessionalRegime employeeProfessionalRegime : person.getEmployee().getEmployeeProfessionalRegimes()) {
		if (employeeProfessionalRegime.getAnulationDate() == null) {
		    regimes.add(employeeProfessionalRegime);
		}
	    }
	}
	request.setAttribute("regimes", regimes);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showRelations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = (Person) rootDomainObject.readPartyByOID(getIntegerFromRequest(request, "personId"));

	List<EmployeeProfessionalRelation> relations = new ArrayList<EmployeeProfessionalRelation>();
	if (person.getEmployee() != null) {
	    for (EmployeeProfessionalRelation employeeProfessionalRelation : person.getEmployee()
		    .getEmployeeProfessionalRelations()) {
		if (employeeProfessionalRelation.getAnulationDate() == null) {
		    relations.add(employeeProfessionalRelation);
		}
	    }
	}
	request.setAttribute("relations", relations);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showContracts(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = (Person) rootDomainObject.readPartyByOID(getIntegerFromRequest(request, "personId"));

	List<EmployeeProfessionalContract> contracts = new ArrayList<EmployeeProfessionalContract>();
	if (person.getEmployee() != null) {
	    for (EmployeeProfessionalContract employeeProfessionalContract : person.getEmployee()
		    .getEmployeeProfessionalContracts()) {
		if (employeeProfessionalContract.getAnulationDate() == null) {
		    contracts.add(employeeProfessionalContract);
		}
	    }
	}
	request.setAttribute("contracts", contracts);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showFunctionsAccumulations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = (Person) rootDomainObject.readPartyByOID(getIntegerFromRequest(request, "personId"));

	List<EmployeeFunctionsAccumulation> functionsAccumulations = new ArrayList<EmployeeFunctionsAccumulation>();
	if (person.getEmployee() != null) {
	    for (EmployeeFunctionsAccumulation employeeFunctionsAccumulation : person.getEmployee()
		    .getEmployeeFunctionsAccumulations()) {
		if (employeeFunctionsAccumulation.getAnulationDate() == null) {
		    functionsAccumulations.add(employeeFunctionsAccumulation);
		}
	    }
	}
	request.setAttribute("functionsAccumulations", functionsAccumulations);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showSabbaticals(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = (Person) rootDomainObject.readPartyByOID(getIntegerFromRequest(request, "personId"));

	List<EmployeeProfessionalExemption> sabbaticals = new ArrayList<EmployeeProfessionalExemption>();
	if (person.getEmployee() != null) {
	    for (EmployeeProfessionalExemption employeeProfessionalExemption : person.getEmployee()
		    .getEmployeeProfessionalExemptions()) {
		if (employeeProfessionalExemption instanceof EmployeeSabbatical
			&& employeeProfessionalExemption.getAnulationDate() == null) {
		    sabbaticals.add(employeeProfessionalExemption);
		}
	    }
	}
	request.setAttribute("sabbaticals", sabbaticals);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showServiceExemptions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = (Person) rootDomainObject.readPartyByOID(getIntegerFromRequest(request, "personId"));

	List<EmployeeProfessionalExemption> serviceExemptions = new ArrayList<EmployeeProfessionalExemption>();
	if (person.getEmployee() != null) {
	    for (EmployeeProfessionalExemption employeeProfessionalExemption : person.getEmployee()
		    .getEmployeeProfessionalExemptions()) {
		if (employeeProfessionalExemption instanceof EmployeeServiceExemption
			&& employeeProfessionalExemption.getAnulationDate() == null) {
		    serviceExemptions.add(employeeProfessionalExemption);
		}
	    }
	}
	request.setAttribute("serviceExemptions", serviceExemptions);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showGrantOwnerEquivalences(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = (Person) rootDomainObject.readPartyByOID(getIntegerFromRequest(request, "personId"));

	List<EmployeeProfessionalExemption> grantOwnerEquivalences = new ArrayList<EmployeeProfessionalExemption>();
	if (person.getEmployee() != null) {
	    for (EmployeeProfessionalExemption employeeProfessionalExemption : person.getEmployee()
		    .getEmployeeProfessionalExemptions()) {
		if (employeeProfessionalExemption instanceof EmployeeGrantOwnerEquivalent
			&& employeeProfessionalExemption.getAnulationDate() == null) {
		    grantOwnerEquivalences.add(employeeProfessionalExemption);
		}
	    }
	}
	request.setAttribute("grantOwnerEquivalences", grantOwnerEquivalences);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

}