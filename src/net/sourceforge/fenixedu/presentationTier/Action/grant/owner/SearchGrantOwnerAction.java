package net.sourceforge.fenixedu.presentationTier.Action.grant.owner;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.presentationTier.Action.framework.SearchAction;
import net.sourceforge.fenixedu.presentationTier.Action.grant.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.mapping.framework.SearchActionMapping;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class SearchGrantOwnerAction extends SearchAction {

    protected Object[] getSearchServiceArgs(HttpServletRequest request, ActionForm form) throws Exception {
        DynaValidatorForm searchGrantOwnerForm = (DynaValidatorForm) form;
        String name = (String) searchGrantOwnerForm.get("name");
        String idNumber = (String) searchGrantOwnerForm.get("idNumber");
        String idType = (String) searchGrantOwnerForm.get("idType");
        Integer startIndex = (Integer) searchGrantOwnerForm.get("startIndex");
        String justGrantOwner = (String) searchGrantOwnerForm.get("justGrantOwner");

        Boolean onlyGrantOwner = new Boolean(false);
        if (justGrantOwner.equals("on")) {
            onlyGrantOwner = new Boolean(true);
        }
        request.setAttribute("justGrantOwner", justGrantOwner);
        request.setAttribute("name", name);       

        final IDDocumentType documentType;
        if (idType != null && idType.length() > 0) {
            documentType = IDDocumentType.valueOf(idType);
        } else {
            documentType = null;
        }

        Object[] args = { name, idNumber, documentType, null, onlyGrantOwner, startIndex };
        return args;
    }

    protected Collection treateServiceResult(SearchActionMapping mapping, HttpServletRequest request, Collection result) throws Exception {
        if (result != null && result.size() == 3) {
            Iterator iterator = result.iterator();
            Object object0 = iterator.next();
            if (object0 instanceof Integer) {
                //Lets set up the span
                Integer numberOfElementsOfSearch = (Integer) object0;
                Integer startIndex = (Integer) iterator.next();
                List infoListGrantOwner = (List) iterator.next();

                if (hasBeforeSpan(startIndex, numberOfElementsOfSearch)) {
                    request.setAttribute("beforeSpan", getBeforeSpan(startIndex, numberOfElementsOfSearch));
                }
                if (hasNextSpan(startIndex, numberOfElementsOfSearch)) {
                    request.setAttribute("nextSpan", getNextSpan(startIndex, numberOfElementsOfSearch));
                }
                result = infoListGrantOwner;
                request.setAttribute("numberOfTotalElementsInSearch", numberOfElementsOfSearch);
                request.setAttribute("actualPage", getActualPage(startIndex));
                request.setAttribute("numberOfPages", getTotalNumberOfPages(numberOfElementsOfSearch));
            }
        }
        
        return result;
    }

    protected void prepareFormConstants(ActionMapping mapping, HttpServletRequest request,
            ActionForm form) throws Exception {
        /*List documentTypeList = TipoDocumentoIdentificacao.toIntegerArrayList();
        request.setAttribute("documentTypeList", documentTypeList);*/
    }

    private boolean hasNextSpan(Integer startIndex, Integer numberOfElementsInResult) {
        return (startIndex + SessionConstants.NUMBER_OF_ELEMENTS_IN_SPAN - 1) < numberOfElementsInResult;
    }

    private boolean hasBeforeSpan(Integer startIndex, Integer numberOfElementsInResult) {
        return (startIndex - SessionConstants.NUMBER_OF_ELEMENTS_IN_SPAN + 1) > 0;
    }

    private Integer getNextSpan(Integer startIndex, Integer numberOfElementsInResult) {
        return Integer.valueOf(startIndex + SessionConstants.NUMBER_OF_ELEMENTS_IN_SPAN);
    }

    private Integer getBeforeSpan(Integer startIndex, Integer numberOfElementsInResult) {
        return Integer.valueOf(startIndex - SessionConstants.NUMBER_OF_ELEMENTS_IN_SPAN);
    }

    private Integer getActualPage(Integer startIndex) {
        return Integer.valueOf((startIndex / SessionConstants.NUMBER_OF_ELEMENTS_IN_SPAN) + 1);
    }

    private Integer getTotalNumberOfPages(Integer numberOfElements) {
        return Integer.valueOf((numberOfElements / SessionConstants.NUMBER_OF_ELEMENTS_IN_SPAN) + 1);
    }
    
}
