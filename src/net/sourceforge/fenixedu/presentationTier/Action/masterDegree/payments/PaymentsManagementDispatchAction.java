/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsManagementDTO;
import net.sourceforge.fenixedu.domain.accounting.DebtEvent;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public abstract class PaymentsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward firstPage(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("success");
    }

    protected Integer getCandidacyNumber(final DynaActionForm form) {
        final Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        return (candidacyNumber == null || candidacyNumber.intValue() == 0) ? null : candidacyNumber;
    }

    protected PaymentsManagementDTO searchEventsForCandidacy(Integer candidacyNumber) {

        PaymentsManagementDTO managementDTO = null;
        final Candidacy candidacy = (candidacyNumber != null) ? Candidacy
                .readByCandidacyNumber(candidacyNumber) : null;
        if (candidacy != null) {
            managementDTO = new PaymentsManagementDTO(candidacy);
            for (final DebtEvent event : candidacy.getPerson().getDebtEventsSet()) {
                if (!event.isClosed()) {
                    managementDTO.getEntryDTOs().addAll(event.calculateEntries());
                }
            }
        }
        return managementDTO;
    }

}
