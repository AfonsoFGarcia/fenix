
package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.InfoPrice;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.exceptions.InvalidInformationInFormActionException;
import ServidorApresentacao.Action.exceptions.NoChangeMadeActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class StudentGuideDispatchAction extends DispatchAction {

	
	public ActionForward createReady(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm studentGuideForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			InfoGuide infoGuide = (InfoGuide) session.getAttribute(SessionConstants.GUIDE);
						
			List certificateList = (List) session.getAttribute(SessionConstants.CERTIFICATE_LIST);
			
			// Fill in the quantity List
			Enumeration arguments = request.getParameterNames();

			String[] quantityList = request.getParameterValues("quantityList");
			
			Iterator iterator = certificateList.iterator();
			
			int position = 0;
			infoGuide.setInfoGuideEntries(new ArrayList());

			while(iterator.hasNext()){
				iterator.next();
				Integer quantity = null;
				
				try {
				 	quantity = new Integer(quantityList[position]);
				 	if (quantity.intValue() < 0)
				 		throw new NumberFormatException(); 
				} catch(NumberFormatException e){
					throw new InvalidInformationInFormActionException(null);
				}

				if (quantity.intValue() > 0){
					InfoPrice infoPrice = (InfoPrice) certificateList.get(position);					
					InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
					infoGuideEntry.setDescription(infoPrice.getDescription());
					infoGuideEntry.setDocumentType(infoPrice.getDocumentType());
					infoGuideEntry.setGraduationType(infoPrice.getGraduationType());
					infoGuideEntry.setPrice(infoPrice.getPrice());
					infoGuideEntry.setQuantity(quantity);
					infoGuide.getInfoGuideEntries().add(infoGuideEntry);
				}
				
				position++;				
 
			}
			
			if (infoGuide.getInfoGuideEntries().size() == 0)			
				throw new NoChangeMadeActionException("error.exception.noCertificateChosen");
				
				
			request.setAttribute(SessionConstants.GUIDE, infoGuide);
			
			return mapping.findForward("CreateStudentGuideReady");			

		} else
			throw new Exception();   
	}
		  
}
