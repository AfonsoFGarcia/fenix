package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.HostAccessControl;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class GroupCheck extends ExternalInterfaceDispatchAction {

    private static final String SUCCESS_CODE = "SUCCESS";

    private static final String NON_EXISTING_GROUP_CODE = "NON_EXISTING_GROUP";

    private static final String NOT_AUTHORIZED_CODE = "NOT_AUTHORIZED";

    private static final String UNEXPECTED_ERROR_CODE = "UNEXPECTED_ERROR";

    /**
     * Checks if the user belongs to the group specified in query.
     * 
     * The response has the following format: <RESPONSE_CODE>\n<BOOLEAN_VALUE>
     * 
     * <RESPONSE_CODE> = NON_EXISTING_GROUP || SUCESS || NOT_AUTHORIZED ||
     * UNEXPECTED_ERROR <BOOLEAN_VALUE> = TRUE || FALSE
     * 
     */
    public ActionForward check(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String query = request.getParameter("query");

        Boolean result = false;
        String responseCode;

        if (!HostAccessControl.isAllowed(this, request)) {
            responseCode = NOT_AUTHORIZED_CODE;
        } else {
            try {
                result = (Boolean) ServiceUtils.executeService(null, "GroupCheckService",
                        new Object[] { query });

                responseCode = SUCCESS_CODE;
            } catch (NonExistingServiceException e) {
                responseCode = NON_EXISTING_GROUP_CODE;
            } catch (Exception e) {
                responseCode = UNEXPECTED_ERROR_CODE;
            }

        }

        writeResponse(response, responseCode, result.toString().toUpperCase());

        return null;
    }

}
