package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.servlets.filters.I18NFilter;

public class ChangeLocale extends FenixAction {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final Locale locale = constructNewLocale(request);
		I18NFilter.setLocale(request, request.getSession(true), locale);

		// HACK: remove locale parameter if present
		String windowLocation = filterLocaleFromLocation(request.getParameter("windowLocation"));
		return forward(windowLocation);
	}

	private String filterLocaleFromLocation(String parameter) {
		if (parameter == null) {
			return parameter;
		}

		return parameter.replaceAll("&locale=[\\p{Alpha}-]*", "").replaceAll("\\?locale=[\\p{Alpha}-]*", "");
	}

	private Locale constructNewLocale(HttpServletRequest request) {
		final String newLanguage = request.getParameter("newLanguage");
		final String newCountry = request.getParameter("newCountry");
		final String newVariant = request.getParameter("newVariant");
		return newVariant == null ? new Locale(newLanguage, newCountry) : new Locale(newLanguage, newCountry, newVariant);
	}

	private ActionForward forward(final String windowLocation) {
		final ActionForward actionForward = new ActionForward();
		actionForward.setName(windowLocation);
		actionForward.setPath(windowLocation);
		actionForward.setRedirect(true);
		return actionForward;
	}

}