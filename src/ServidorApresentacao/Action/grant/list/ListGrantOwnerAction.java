/*
 * Created on Jun 21, 2004
 *
 */
package ServidorApresentacao.Action.grant.list;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.grant.list.InfoListGrantOwnerComplete;
import DataBeans.grant.list.InfoSpanListGrantOwner;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;


/**
 * @author Pica
 * @author Barbosa
 */
public class ListGrantOwnerAction extends FenixDispatchAction
{
	public ActionForward prepareFirstTimeListGrantOwner(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		Integer spanNumber = null;
		Integer numberOfElementsInSpan = null;
		String orderBy = null;
		
		if (verifyParameterInRequest(request,"spanNumber"))
		{
			spanNumber = new Integer(request.getParameter("spanNumber"));
		}
		if (verifyParameterInRequest(request,"numberOfElementsInSpan"))
		{
			numberOfElementsInSpan = new Integer(request.getParameter("numberOfElementsInSpan"));
		}
		if (verifyParameterInRequest(request,"orderBy"))
		{
			orderBy = request.getParameter("orderBy");
		}
		
		if (spanNumber != null && numberOfElementsInSpan != null && orderBy != null && !orderBy.equals(""))
		{
		    InfoSpanListGrantOwner infoSpanListGrantOwner = new InfoSpanListGrantOwner();
		    infoSpanListGrantOwner.setNumberOfElementsInSpan(numberOfElementsInSpan);
		    infoSpanListGrantOwner.setOrderBy(orderBy);
		    infoSpanListGrantOwner.setSpanNumber(spanNumber);
		    return listGrantOwner(mapping, request, form,response, infoSpanListGrantOwner);
		}
		else
		{
			return setError(request,mapping,"errors.grant.unrecoverable","list-grant-owner",null);
		}
	}
	
	public ActionForward prepareListGrantOwner(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception
		{
	    	DynaValidatorForm listForm = (DynaValidatorForm) form;
	    	
	    	InfoSpanListGrantOwner infoSpanListGrantOwner = populateInfoFromForm(listForm);
	    	if(infoSpanListGrantOwner.getSpanNumber().intValue() > infoSpanListGrantOwner.getNumberOfSpans().intValue())
	    	{
	    	    return setError(request,mapping,"errors.grant.list.invalidSpan","list-grant-owner",null);
	    	}
	    	return listGrantOwner(mapping, request, form, response, infoSpanListGrantOwner);
		}
	
	private ActionForward listGrantOwner(ActionMapping mapping,
		HttpServletRequest request,
		ActionForm form,
		HttpServletResponse response,
		InfoSpanListGrantOwner infoSpanListGrantOwner){

		try
		{
		    IUserView userView = SessionUtils.getUserView(request);

		    //Read the grant owners
			Object[] args = { infoSpanListGrantOwner };
			List listGrantOwners = (List) ServiceUtils.executeService(userView, "ListGrantOwners", args);
			
			if(listGrantOwners != null && listGrantOwners.size() != 0)
			{
			    if(listGrantOwners.size() > infoSpanListGrantOwner.getNumberOfElementsInSpan().intValue())
			    {
			        infoSpanListGrantOwner = (InfoSpanListGrantOwner)listGrantOwners.get(listGrantOwners.size() - 1);
			        listGrantOwners.remove(listGrantOwners.size() - 1);
			    }
			    
				//Setting the request
			    DynaValidatorForm listForm = (DynaValidatorForm) form;
			    setForm(listForm, infoSpanListGrantOwner);
			    request.setAttribute("listGrantOwner", listGrantOwners);
				request.setAttribute("spanNumber", infoSpanListGrantOwner.getSpanNumber());
				request.setAttribute("numberOfElementsInSpan", infoSpanListGrantOwner.getNumberOfElementsInSpan());
				request.setAttribute("orderBy", infoSpanListGrantOwner.getOrderBy());
				request.setAttribute("totalElements", infoSpanListGrantOwner.getTotalElements());
				
				if(infoSpanListGrantOwner.hasBeforeSpan())
				{
				    request.setAttribute("beforeSpan", infoSpanListGrantOwner.getBeforeSpan());
				}
				if(infoSpanListGrantOwner.hasAfterSpan())
				{
				    request.setAttribute("afterSpan", infoSpanListGrantOwner.getAfterSpan());
				}
				request.setAttribute("numberOfSpans", infoSpanListGrantOwner.getNumberOfSpans());
			}
			else
			{
			    throw new Exception();
			}
		}
		catch (FenixServiceException e)
		{
			return setError(request,mapping,"errors.grant.unrecoverable","list-grant-owner",null);
			
		}
		catch (Exception e)
		{
			return setError(request,mapping,"errors.grant.unrecoverable","list-grant-owner",null);
		}
		
		return mapping.findForward("list-grant-owner");	    
	}
	
	public ActionForward showGrantOwner(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception
		{
			Integer grantOwnerId = null;
			if (verifyParameterInRequest(request,"grantOwnerId"))
			{
				grantOwnerId = new Integer(request.getParameter("grantOwnerId"));
			}
			
			IUserView userView = SessionUtils.getUserView(request);

			if (grantOwnerId != null)
			{
				try
				{
					//Read all the information about the grant owner
					Object[] args = { grantOwnerId };
					InfoListGrantOwnerComplete listGrantOwnerCompleteInfo = (InfoListGrantOwnerComplete) ServiceUtils.executeService(userView, "ShowGrantOwner", args);

					if(listGrantOwnerCompleteInfo != null)
					{
						//Set the request
						request.setAttribute("infoGrantOwner",listGrantOwnerCompleteInfo.getInfoGrantOwner());
						request.setAttribute("infoQualificationList", listGrantOwnerCompleteInfo.getInfoQualifications());
						request.setAttribute("infoListGrantContractList", listGrantOwnerCompleteInfo.getInfoListGrantContracts());
					}
				}
				catch (FenixServiceException e)
				{
					return setError(request,mapping,"errors.grant.unrecoverable","show-grant-owner",null);
				}
				catch (Exception e)
				{
					return setError(request,mapping,"errors.grant.unrecoverable","show-grant-owner",null);
				}
			}
			else
			{
				return setError(request,mapping,"errors.grant.unrecoverable","show-grant-owner",null);
			}
			return mapping.findForward("show-grant-owner");
		}
	

	private void setForm(DynaValidatorForm form, InfoSpanListGrantOwner infoSpanListGrantOwner)
	throws Exception
	{
	    form.set("spanNumber", infoSpanListGrantOwner.getSpanNumber());	
	    form.set("numberOfElementsInSpan", infoSpanListGrantOwner.getNumberOfElementsInSpan());
	    form.set("totalElements", infoSpanListGrantOwner.getTotalElements());
	    form.set("orderBy", infoSpanListGrantOwner.getOrderBy());
	}

	
	private InfoSpanListGrantOwner populateInfoFromForm(DynaValidatorForm form)
		throws Exception
	{
	    InfoSpanListGrantOwner infoSpanListGrantOwner = new InfoSpanListGrantOwner();
	    
		infoSpanListGrantOwner.setNumberOfElementsInSpan((Integer) form.get("numberOfElementsInSpan"));
		infoSpanListGrantOwner.setSpanNumber((Integer) form.get("spanNumber"));
		infoSpanListGrantOwner.setTotalElements((Integer) form.get("totalElements"));
		infoSpanListGrantOwner.setOrderBy((String) form.get("orderBy"));
        
		return infoSpanListGrantOwner;
	}


}
