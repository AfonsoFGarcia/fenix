package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoSection;
import DataBeans.InfoSite;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Ivo Brand�o
 */
public class EditSectionDispatchAction extends FenixDispatchAction {

	public ActionForward prepareEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession();

		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		InfoSection currentSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);
		List all = (List) session.getAttribute(SessionConstants.SECTIONS);
		List allSections = new ArrayList();
		allSections.addAll(all);

		session.removeAttribute(SessionConstants.POSSIBLE_PARENT_SECTIONS);

		//remove parent section, current section and all of it's daughters
		allSections.remove(currentSection.getSuperiorInfoSection());
		allSections.remove(currentSection);
		
		try {
			allSections = this.removeDaughters(userView, infoSite, currentSection, allSections);
		} catch (FenixActionException fenixActionException) {
			throw fenixActionException;
		}

		session.setAttribute(SessionConstants.POSSIBLE_PARENT_SECTIONS, allSections);

		//relative to children sections
		ArrayList sections;
		Object args[] = { infoSite, currentSection.getSuperiorInfoSection()};
		GestorServicos manager = GestorServicos.manager();
		try {
			sections = (ArrayList) manager.executar(userView, "ReadSectionsBySiteAndSuperiorSection", args);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		if (sections.size() != 0) {
			sections.remove(currentSection);
			Collections.sort(sections);
			session.removeAttribute(SessionConstants.CHILDREN_SECTIONS);
			session.setAttribute(SessionConstants.CHILDREN_SECTIONS, sections);
		} else
			session.removeAttribute(SessionConstants.CHILDREN_SECTIONS);
		//end - relative to children sections		

		return mapping.findForward("editSection");
	}

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		DynaActionForm sectionForm = (DynaValidatorForm) form;
		String sectionName = (String) sectionForm.get("name");
		Integer order = (Integer) sectionForm.get("sectionOrder");
		order=new Integer(order.intValue()-1);
		HttpSession session = request.getSession();

		String parentName = (String) sectionForm.get("parentSection");

		

		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
		InfoSection oldSection =
			(InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);

			Object readArgs[] = { infoSite };
								ArrayList sections;
			GestorServicos manager = GestorServicos.manager();					
		if (order.intValue()==-2) {
			//inserir no fim
			
					try {
						sections =
							(ArrayList) manager.executar(
								userView,
								"ReadSections",
								readArgs);
					} catch (FenixServiceException fenixServiceException) {
						throw new FenixActionException(fenixServiceException.getMessage());
					}
			if (sections!=null && sections.size()!=0)		
			{order=new Integer(sections.size()-2);} 	
			else {
				order = new Integer(0);
			}
			
		}

		InfoSection newSection =
			new InfoSection(
				sectionName,
				order,
				infoSite,
				oldSection.getSuperiorInfoSection());

		//perform edition
		Object editionArgs[] = { oldSection, newSection };
		
		try {
			manager.executar(userView, "EditSection", editionArgs);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		//read sections
		try {
			sections =
				(ArrayList) manager.executar(
					userView,
					"ReadSections",
					readArgs);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		Collections.sort(sections);
		session.setAttribute(SessionConstants.SECTIONS, sections);

		return mapping.findForward("viewSite");
	}

	public ActionForward prepareChangeParent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession();
		
		return mapping.findForward("changeParent");		
	}
	
	public ActionForward changeParent(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession();

		DynaActionForm sectionForm = (DynaValidatorForm) form;
		Integer index = (Integer) sectionForm.get("sectionIndex");
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		List allSections = (List) session.getAttribute(SessionConstants.POSSIBLE_PARENT_SECTIONS);

		InfoSection newParent = null;
		if (index!=null) newParent = (InfoSection) allSections.get(index.intValue());

		InfoSection oldSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);
		InfoSection newSection = new InfoSection(oldSection.getName(), oldSection.getSectionOrder(), oldSection.getInfoSite(), newParent);

		GestorServicos manager = GestorServicos.manager();

		//the new order should be after the last child section
		//read child sections for newParent, get the size of the list and use size as section order
		InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
		Object args[] = { infoSite, newParent };
		List sisters = null;
		try {
			sisters = (List) manager.executar(userView, "ReadSectionsBySiteAndSuperiorSection", args);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		if (sisters!=null) newSection.setSectionOrder(new Integer(sisters.size()));
		else newSection.setSectionOrder(new Integer(0));
		
		//perform edition
		Object editionArgs[] = { oldSection, newSection };
		
		try {
			manager.executar(userView, "EditSection", editionArgs);
		} catch (ExistingServiceException fenixServiceException) {
			throw new ExistingActionException("Uma Subsec��o com este nome e essa secc��o pai ",fenixServiceException);
		} 
		
		catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		
		session.removeAttribute(SessionConstants.INFO_SECTION);
		session.setAttribute(SessionConstants.INFO_SECTION, newSection);
		List sections= null;
		Object readArgs[] = { newSection.getInfoSite()	};
		try {
			sections=(List) manager.executar(userView, "ReadSections", readArgs);
		} catch (FenixServiceException fenixServiceException) {
				throw new FenixActionException(fenixServiceException.getMessage());
		}
		session.removeAttribute(SessionConstants.SECTIONS);
		session.setAttribute(SessionConstants.SECTIONS, sections);

		return mapping.findForward("viewSite");
	}

	private List removeDaughters(UserView userView, InfoSite infoSite, InfoSection infoSection, List allSections)
		throws FenixActionException {

		List sections = new ArrayList();
		Object args[] = { infoSite, infoSection };
		GestorServicos manager = GestorServicos.manager();
		try {
			sections = (List) manager.executar(userView, "ReadSectionsBySiteAndSuperiorSection", args);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		allSections.removeAll(sections);

		Iterator iterator = sections.iterator();
		while (iterator.hasNext()) {
			InfoSection infoSection2 = (InfoSection) iterator.next();
			allSections = removeDaughters(userView, infoSite, infoSection2, allSections);
		}

		return allSections;
	}
}