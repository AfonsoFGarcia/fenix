/*
 * Created on 03/Dec/2003
 *  
 */

package ServidorApresentacao.Action.grant.owner;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import ServidorApresentacao.Action.framework.SearchAction;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Barbosa
 * @author Pica
 */
public class SearchGrantOwnerAction extends SearchAction
{
	protected Object[] getSearchServiceArgs(HttpServletRequest request, ActionForm form)
		throws Exception
	{
		//Read attributes from FormBean
		DynaValidatorForm searchGrantOwnerForm = (DynaValidatorForm) form;
		String name = (String) searchGrantOwnerForm.get("name");
		String idNumber = (String) searchGrantOwnerForm.get("idNumber");
		Integer idType = (Integer) searchGrantOwnerForm.get("idType");

		//Prepare arguments of service to search
		Object[] args = { name, idNumber, idType, null };
		return args;
	}

	/*
	 * Populate form
	 */
	protected void prepareFormConstants(
		ActionMapping mapping,
		HttpServletRequest request,
		ActionForm form)
		throws Exception
	{
		List documentTypeList = TipoDocumentoIdentificacao.toIntegerArrayList();
		request.setAttribute("documentTypeList", documentTypeList);
	}

}