package net.sourceforge.fenixedu.presentationTier.Action.manager.renderers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "manager", path = "/renderers/searchPersons", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "input", path = "/manager/renderers/search-input.jsp"),
		@Forward(name = "results", path = "/manager/renderers/search-results.jsp") })
public class SearchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	SearchBean bean = new SearchBean();
	bean.setDate(new Date());

	request.setAttribute("bean", bean);
	return mapping.findForward("input");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	SearchBean bean = getBean(request);

	List<FoundPerson> foundPeople = new ArrayList<FoundPerson>();

	foundPeople.add(new FoundPerson("Jos� Ant�nio", bean.getMinAge(), bean.getGender()));
	foundPeople.add(new FoundPerson("Ant�nio Jos�", bean.getMaxAge(), bean.getGender()));
	foundPeople.add(new FoundPerson("Ant� Jos�nio", (bean.getMaxAge() + bean.getMinAge()) / 2, bean.getGender()));

	request.setAttribute("found", foundPeople);

	return mapping.findForward("results");
    }

    private SearchBean getBean(HttpServletRequest request) {
	IViewState viewState = (IViewState) RenderUtils.getViewState();
	return (SearchBean) viewState.getMetaObject().getObject();
    }
}
