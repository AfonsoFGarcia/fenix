/*
 * ChangeCandidateApplicationFormAction.java
 * 
 * 
 * Created on 14 de Dezembro de 2002, 12:31
 * 
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

public class EditStudentInfoDispatchAction extends DispatchAction {

    public ActionForward change(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {
            DynaActionForm changeApplicationInfoForm = (DynaActionForm) form;
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            Integer personCode = Integer.valueOf(getFromRequest("idInternal", request));
            Integer number = Integer.valueOf(getFromRequest("number", request));
            // Create Dates

            Calendar birthDate = Calendar.getInstance();
            Calendar idDocumentIssueDate = Calendar.getInstance();
            Calendar idDocumentExpirationDate = Calendar.getInstance();

            InfoPerson infoPerson = new InfoPerson();
            infoPerson.setIdInternal(personCode);
            String dayString = (String) changeApplicationInfoForm.get("birthDay");
            String monthString = (String) changeApplicationInfoForm.get("birthMonth");
            String yearString = (String) changeApplicationInfoForm.get("birthYear");

            Integer day = null;
            Integer month = null;
            Integer year = null;

            if ((dayString == null) || (monthString == null) || (yearString == null)
                    || (dayString.length() == 0) || (monthString.length() == 0)
                    || (yearString.length() == 0)) {
                infoPerson.setNascimento(null);
            } else {
                day = new Integer((String) changeApplicationInfoForm.get("birthDay"));
                month = new Integer((String) changeApplicationInfoForm.get("birthMonth"));
                year = new Integer((String) changeApplicationInfoForm.get("birthYear"));

                birthDate.set(year.intValue(), month.intValue(), day.intValue());
                infoPerson.setNascimento(birthDate.getTime());
            }

            dayString = (String) changeApplicationInfoForm.get("idIssueDateDay");
            monthString = (String) changeApplicationInfoForm.get("idIssueDateMonth");
            yearString = (String) changeApplicationInfoForm.get("idIssueDateYear");

            if ((dayString == null) || (monthString == null) || (yearString == null)
                    || (dayString.length() == 0) || (monthString.length() == 0)
                    || (yearString.length() == 0)) {
                infoPerson.setDataEmissaoDocumentoIdentificacao(null);
            } else {
                day = new Integer((String) changeApplicationInfoForm.get("idIssueDateDay"));
                month = new Integer((String) changeApplicationInfoForm.get("idIssueDateMonth"));
                year = new Integer((String) changeApplicationInfoForm.get("idIssueDateYear"));

                idDocumentIssueDate.set(year.intValue(), month.intValue(), day.intValue());
                infoPerson.setDataEmissaoDocumentoIdentificacao(idDocumentIssueDate.getTime());
            }

            dayString = (String) changeApplicationInfoForm.get("idExpirationDateDay");
            monthString = (String) changeApplicationInfoForm.get("idExpirationDateMonth");
            yearString = (String) changeApplicationInfoForm.get("idExpirationDateYear");

            if ((dayString == null) || (monthString == null) || (yearString == null)
                    || (dayString.length() == 0) || (monthString.length() == 0)
                    || (yearString.length() == 0)) {
                infoPerson.setDataValidadeDocumentoIdentificacao(null);
            } else {
                day = new Integer((String) changeApplicationInfoForm.get("idExpirationDateDay"));
                month = new Integer((String) changeApplicationInfoForm.get("idExpirationDateMonth"));
                year = new Integer((String) changeApplicationInfoForm.get("idExpirationDateYear"));

                idDocumentExpirationDate.set(year.intValue(), month.intValue(), day.intValue());
                infoPerson.setDataValidadeDocumentoIdentificacao(idDocumentExpirationDate.getTime());
            }

            InfoCountry infoCountry = new InfoCountry();
            infoCountry.setNationality((String) changeApplicationInfoForm.get("nationality"));

            infoPerson.setTipoDocumentoIdentificacao(IDDocumentType.valueOf((String) changeApplicationInfoForm.get("identificationDocumentType")));
            infoPerson.setNumeroDocumentoIdentificacao((String) changeApplicationInfoForm
                    .get("identificationDocumentNumber"));
            infoPerson.setLocalEmissaoDocumentoIdentificacao((String) changeApplicationInfoForm
                    .get("identificationDocumentIssuePlace"));
            infoPerson.setNome((String) changeApplicationInfoForm.get("name"));

            String aux = (String) changeApplicationInfoForm.get("sex");
            if ((aux == null) || (aux.length() == 0))
                infoPerson.setSexo(null);
            else
                infoPerson.setSexo(Gender.valueOf(aux));

            aux = (String) changeApplicationInfoForm.get("maritalStatus");
            if ((aux == null) || (aux.length() == 0))
                infoPerson.setMaritalStatus(null);
            else
                infoPerson.setMaritalStatus(MaritalStatus.valueOf(aux));
            infoPerson.setInfoPais(infoCountry);
            infoPerson.setNomePai((String) changeApplicationInfoForm.get("fatherName"));
            infoPerson.setNomeMae((String) changeApplicationInfoForm.get("motherName"));
            infoPerson.setFreguesiaNaturalidade((String) changeApplicationInfoForm
                    .get("birthPlaceParish"));
            infoPerson.setConcelhoNaturalidade((String) changeApplicationInfoForm
                    .get("birthPlaceMunicipality"));
            infoPerson.setDistritoNaturalidade((String) changeApplicationInfoForm
                    .get("birthPlaceDistrict"));
            infoPerson.setMorada((String) changeApplicationInfoForm.get("address"));
            infoPerson.setLocalidade((String) changeApplicationInfoForm.get("place"));
            infoPerson.setCodigoPostal((String) changeApplicationInfoForm.get("postCode"));
            infoPerson.setFreguesiaMorada((String) changeApplicationInfoForm.get("addressParish"));
            infoPerson.setConcelhoMorada((String) changeApplicationInfoForm.get("addressMunicipality"));
            infoPerson.setDistritoMorada((String) changeApplicationInfoForm.get("addressDistrict"));
            infoPerson.setTelefone((String) changeApplicationInfoForm.get("telephone"));
            infoPerson.setTelemovel((String) changeApplicationInfoForm.get("mobilePhone"));
            infoPerson.setEmail((String) changeApplicationInfoForm.get("email"));
            infoPerson.setEnderecoWeb((String) changeApplicationInfoForm.get("webSite"));
            infoPerson.setNumContribuinte((String) changeApplicationInfoForm.get("contributorNumber"));
            infoPerson.setProfissao((String) changeApplicationInfoForm.get("occupation"));
            infoPerson.setUsername((String) changeApplicationInfoForm.get("username"));
            infoPerson.setLocalidadeCodigoPostal((String) changeApplicationInfoForm
                    .get("areaOfAreaCode"));

            Object args[] = { infoPerson };
            InfoPerson newInfoPerson = new InfoPerson();
            try {
                newInfoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(userView,
                        "ChangePersonalStudentInfo", args);
            
            } catch (DomainException e) {
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("", new ActionMessage(e.getMessage()));
                saveMessages(request, actionMessages); 
                return mapping.getInputForward();
            }

            request.removeAttribute("idInternal");
            request.setAttribute("studentNumber", number);
            request.setAttribute("infoPerson", newInfoPerson);
            populateForm(changeApplicationInfoForm, newInfoPerson);

            return mapping.findForward("Success");
        }

        throw new Exception();
    }

    public static void populateForm(DynaActionForm changeApplicationInfoForm, InfoPerson infoPerson) {
    	ReadPersonInfoOfStudentsAction.populateForm(changeApplicationInfoForm, infoPerson);
    }

    public static String getFromRequest(String parameter, HttpServletRequest request) {
    	return ReadPersonInfoOfStudentsAction.getFromRequest(parameter, request);
    }

}