/*
 * PrepararAlterarInfoCandidaturaFormAction.java
 *
 * 
 * Created on 14 de Dezembro de 2002, 13:35
 *
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
 
package ServidorApresentacao.Action.masterDegree.candidate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoCountry;
import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Data;
import Util.EstadoCivil;
import Util.Sexo;
import Util.SituationName;
import Util.TipoDocumentoIdentificacao;


public class PrepareApplicationInfoChangeFormAction extends ServidorApresentacao.Action.FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
    throws Exception {

		// FIXME: Verificar o que acontece com campos nao preenchidos na BD
		SessionUtils.validSessionVerification(request, mapping);
	  	HttpSession session = request.getSession(false);
	  	
	  	if (session != null) {
		  DynaActionForm changePersonalInfoForm = (DynaActionForm) form;

	      IUserView userView = (IUserView) session.getAttribute("UserView");
	      GestorServicos gestor = GestorServicos.manager();
	
	      Object changeArgs[] = new Object[1];
	      changeArgs[0] = userView;
	
		  Object result = null;
	       
	      result = gestor.executar(userView, "ReadMasterDegreeCandidateByUsername", changeArgs);
		
		  InfoMasterDegreeCandidate masterDegreeCandidate = (InfoMasterDegreeCandidate) result; 

		  // If the Candidate's situation is already resolved then he can't change it
		  
		 if(!masterDegreeCandidate.getInfoCandidateSituation().getSituation().equals(SituationName.PENDENTE_STRING)){
		 	// Put information in Session
		 	
		 	request.setAttribute("situation", masterDegreeCandidate.getInfoCandidateSituation());
		 	return mapping.findForward("Unchangeable");		
		 }

		  changePersonalInfoForm.set("identificationDocumentNumber", masterDegreeCandidate.getInfoPerson().getNumeroDocumentoIdentificacao());
		  changePersonalInfoForm.set("identificationDocumentType", masterDegreeCandidate.getInfoPerson().getTipoDocumentoIdentificacao());
		  changePersonalInfoForm.set("identificationDocumentIssuePlace", masterDegreeCandidate.getInfoPerson().getLocalEmissaoDocumentoIdentificacao());
		  changePersonalInfoForm.set("name", masterDegreeCandidate.getInfoPerson().getNome());
		  changePersonalInfoForm.set("maritalStatus", masterDegreeCandidate.getInfoPerson().getEstadoCivil());
		  
		  Calendar birthDate = Calendar.getInstance();
		  if (masterDegreeCandidate.getInfoPerson().getNascimento() == null){
			  changePersonalInfoForm.set("birthDay", Data.OPTION_DEFAULT.toString());
			  changePersonalInfoForm.set("birthMonth", Data.OPTION_DEFAULT.toString());
			  changePersonalInfoForm.set("birthYear", Data.OPTION_DEFAULT.toString());
		  } else {
			  birthDate.setTime(masterDegreeCandidate.getInfoPerson().getNascimento());
			  changePersonalInfoForm.set("birthDay", new Integer(birthDate.get(Calendar.DAY_OF_MONTH)).toString());
			  changePersonalInfoForm.set("birthMonth", new Integer(birthDate.get(Calendar.MONTH)).toString());
			  changePersonalInfoForm.set("birthYear", new Integer(birthDate.get(Calendar.YEAR)).toString());
		  }
		  
		  		  
		  Calendar identificationDocumentIssueDate = Calendar.getInstance();
		  if (masterDegreeCandidate.getInfoPerson().getDataEmissaoDocumentoIdentificacao() == null){
			  changePersonalInfoForm.set("idIssueDateDay", Data.OPTION_DEFAULT.toString());
			  changePersonalInfoForm.set("idIssueDateMonth", Data.OPTION_DEFAULT.toString());
			  changePersonalInfoForm.set("idIssueDateYear", Data.OPTION_DEFAULT.toString());
		  } else {
			  identificationDocumentIssueDate.setTime(masterDegreeCandidate.getInfoPerson().getDataEmissaoDocumentoIdentificacao());
			  changePersonalInfoForm.set("idIssueDateDay", new Integer(identificationDocumentIssueDate.get(Calendar.DAY_OF_MONTH)).toString());
			  changePersonalInfoForm.set("idIssueDateMonth", new Integer(identificationDocumentIssueDate.get(Calendar.MONTH)).toString());
			  changePersonalInfoForm.set("idIssueDateYear", new Integer(identificationDocumentIssueDate.get(Calendar.YEAR)).toString());
		  }

		  Calendar identificationDocumentExpirationDate = Calendar.getInstance();
		  if (masterDegreeCandidate.getInfoPerson().getDataValidadeDocumentoIdentificacao() == null){
			  changePersonalInfoForm.set("idExpirationDateDay", Data.OPTION_DEFAULT.toString());
			  changePersonalInfoForm.set("idExpirationDateMonth", Data.OPTION_DEFAULT.toString());
			  changePersonalInfoForm.set("idExpirationDateYear", Data.OPTION_DEFAULT.toString());
		  } else {
			  identificationDocumentIssueDate.setTime(masterDegreeCandidate.getInfoPerson().getDataValidadeDocumentoIdentificacao());
			  changePersonalInfoForm.set("idExpirationDateDay", new Integer(identificationDocumentExpirationDate.get(Calendar.DAY_OF_MONTH)).toString());
			  changePersonalInfoForm.set("idExpirationDateMonth", new Integer(identificationDocumentExpirationDate.get(Calendar.MONTH)).toString());
			  changePersonalInfoForm.set("idExpirationDateYear", new Integer(identificationDocumentExpirationDate.get(Calendar.YEAR)).toString());
		  }


		  changePersonalInfoForm.set("fatherName", masterDegreeCandidate.getInfoPerson().getNomePai()); 
		  changePersonalInfoForm.set("motherName", masterDegreeCandidate.getInfoPerson().getNomeMae()); 
		  changePersonalInfoForm.set("nationality", masterDegreeCandidate.getInfoPerson().getNacionalidade());
		  changePersonalInfoForm.set("birthPlaceParish", masterDegreeCandidate.getInfoPerson().getFreguesiaNaturalidade());
		  changePersonalInfoForm.set("birthPlaceMunicipality", masterDegreeCandidate.getInfoPerson().getConcelhoNaturalidade()); 
		  changePersonalInfoForm.set("birthPlaceDistrict", masterDegreeCandidate.getInfoPerson().getDistritoNaturalidade()); 
		  changePersonalInfoForm.set("address", masterDegreeCandidate.getInfoPerson().getMorada());
		  changePersonalInfoForm.set("place", masterDegreeCandidate.getInfoPerson().getLocalidade()); 
		  changePersonalInfoForm.set("postCode", masterDegreeCandidate.getInfoPerson().getCodigoPostal());
		  changePersonalInfoForm.set("addressParish", masterDegreeCandidate.getInfoPerson().getFreguesiaMorada());
		  changePersonalInfoForm.set("addressMunicipality", masterDegreeCandidate.getInfoPerson().getConcelhoMorada()); 
		  changePersonalInfoForm.set("addressDistrict", masterDegreeCandidate.getInfoPerson().getDistritoMorada()); 
		  changePersonalInfoForm.set("telephone", masterDegreeCandidate.getInfoPerson().getTelefone()); 
		  changePersonalInfoForm.set("mobilePhone", masterDegreeCandidate.getInfoPerson().getTelemovel()); 
		  changePersonalInfoForm.set("email", masterDegreeCandidate.getInfoPerson().getEmail()); 
 		  changePersonalInfoForm.set("webSite", masterDegreeCandidate.getInfoPerson().getEnderecoWeb());
		  changePersonalInfoForm.set("contributorNumber", masterDegreeCandidate.getInfoPerson().getNumContribuinte());
		  changePersonalInfoForm.set("occupation", masterDegreeCandidate.getInfoPerson().getProfissao());
		  changePersonalInfoForm.set("majorDegree", masterDegreeCandidate.getMajorDegree());
		  changePersonalInfoForm.set("majorDegreeSchool", masterDegreeCandidate.getMajorDegreeSchool()); 
		  changePersonalInfoForm.set("majorDegreeYear", masterDegreeCandidate.getMajorDegreeYear()); 
		  changePersonalInfoForm.set("average", String.valueOf(masterDegreeCandidate.getAverage())); 
		  changePersonalInfoForm.set("country", masterDegreeCandidate.getInfoPerson().getInfoPais().getName());
		  changePersonalInfoForm.set("specialization", masterDegreeCandidate.getSpecialization());

		  changePersonalInfoForm.set("candidateNumber", masterDegreeCandidate.getCandidateNumber());
		  changePersonalInfoForm.set("applicationYear", masterDegreeCandidate.getInfoExecutionDegree().getInfoExecutionYear().getYear());
		  changePersonalInfoForm.set("sex", masterDegreeCandidate.getInfoPerson().getSexo());


		  // Get List of available Countries
		  result = null;
 	      result = gestor.executar(userView, "ReadAllCountries", null);
		  ArrayList country = (ArrayList) result;
		  
		  // Build List of Countries for the Form
    	  Iterator iterador = country.iterator();
    	  
    	  ArrayList countryList = new ArrayList();
		  ArrayList nationalityList = new ArrayList();
    	  while (iterador.hasNext()) {
			InfoCountry countryTemp = (InfoCountry) iterador.next();
			countryList.add(new LabelValueBean(countryTemp.getName(), countryTemp.getName()));
			nationalityList.add(new LabelValueBean(countryTemp.getNationality(), countryTemp.getNationality()));
		  }
		
		  session.setAttribute("countryList", countryList);
		  session.setAttribute("nationalityList", nationalityList);
		  session.setAttribute("maritalStatusList", new EstadoCivil().toArrayList());
		  session.setAttribute("identificationDocumentTypeList", TipoDocumentoIdentificacao.toArrayList());
		  session.setAttribute("sexList", new Sexo().toArrayList());   		 
		  session.setAttribute("candidateInformation", masterDegreeCandidate);   
		  session.setAttribute("monthDays", Data.getMonthDays());
		  session.setAttribute("months", Data.getMonths());
		  session.setAttribute("years", Data.getYears());
		  
		  session.setAttribute("expirationYears", Data.getExpirationYears());
		  
		  		 



 	      return mapping.findForward("Success");
	    } else
    	  throw new Exception();  
    }
}
	  