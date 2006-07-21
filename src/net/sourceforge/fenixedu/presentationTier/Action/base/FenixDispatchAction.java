package net.sourceforge.fenixedu.presentationTier.Action.base;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSessionActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.util.struts.StrutsMessageResourceProvider;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.plugin.ExceptionHandler;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

public abstract class FenixDispatchAction extends DispatchAction implements ExceptionHandler {

    protected static final RootDomainObject rootDomainObject = RootDomainObject.getInstance();

    private static final String ACTION_MESSAGES_REQUEST_KEY = "FENIX_ACTION_MESSAGES";

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final ActionMessages actionMessages = new ActionMessages();
        request.setAttribute(ACTION_MESSAGES_REQUEST_KEY, actionMessages);
        final ActionForward actionForward = super.execute(mapping, actionForm, request, response);
        if (!actionMessages.isEmpty()) {
            saveMessages(request, actionMessages);
        }

        return actionForward;
    }

    protected static IUserView getUserView(HttpServletRequest request) {
        return SessionUtils.getUserView(request);
    }

    protected Object executeService(final HttpServletRequest request, final String serviceName,
            final Object[] serviceArgs) throws FenixFilterException, FenixServiceException {
        return ServiceUtils.executeService(getUserView(request), serviceName, serviceArgs);
    }

    protected DomainObject readDomainObject(final HttpServletRequest request, final Class clazz,
            final Integer idInternal) {
        return rootDomainObject.readDomainObjectByOID(clazz, idInternal);
    }

    protected Collection readAllDomainObjects(final HttpServletRequest request, final Class clazz) {
        return rootDomainObject.readAllDomainObjects(clazz);
    }

    protected Person getLoggedPerson(HttpServletRequest request) throws FenixFilterException,
            FenixServiceException {
        return getUserView(request).getPerson();
    }

    protected HttpSession getSession(HttpServletRequest request) throws InvalidSessionActionException {
        HttpSession result = request.getSession(false);
        if (result == null)
            throw new InvalidSessionActionException();

        return result;
    }

    /*
     * Sets an error to display later in the Browser and sets the mapping
     * forward.
     */
    protected ActionForward setError(HttpServletRequest request, ActionMapping mapping,
            String errorMessage, String forwardPage, Object actionArg) {
        ActionErrors errors = new ActionErrors();
        String notMessageKey = errorMessage;
        ActionError error = new ActionError(notMessageKey, actionArg);
        errors.add(notMessageKey, error);
        saveErrors(request, errors);

        if (forwardPage != null) {
            return mapping.findForward(forwardPage);
        }

        return mapping.getInputForward();

    }

    /*
     * Verifies if a property of type String in a FormBean is not empty. Returns
     * true if the field is present and not empty. False otherwhise.
     */
    protected boolean verifyStringParameterInForm(DynaValidatorForm dynaForm, String field) {
        if (dynaForm.get(field) != null && !dynaForm.get(field).equals("")) {
            return true;
        }
        return false;
    }

    /*
     * Verifies if a parameter in a Http Request is not empty. Return true if
     * the field is not empty. False otherwise.
     */
    protected boolean verifyParameterInRequest(HttpServletRequest request, String field) {
        if (request.getParameter(field) != null && !request.getParameter(field).equals("")) {
            return true;
        }
        return false;
    }

    protected Integer getInteger(final DynaActionForm dynaActionForm, final String string) {
        final String value = dynaActionForm.getString(string);
        return value == null || value.length() == 0 ? null : Integer.valueOf(value);
    }

    protected Integer getRequestParameterAsInteger(HttpServletRequest request, String parameterName) {
        String requestParameter = request.getParameter(parameterName);

        if (requestParameter != null) {
            return Integer.valueOf(requestParameter);
        } else {
            return null;
        }
    }

    /**
     * Searches in request parameters first and next in request attributed
     * 
     * @param request
     * @param name
     * @return
     */
    protected Object getFromRequest(HttpServletRequest request, String name) {
        final String requestParameter = request.getParameter(name);
        return (requestParameter != null) ? requestParameter : request.getAttribute(name);
    }

    public ActionForward processException(HttpServletRequest request, ActionMapping mapping,
            ActionForward input, Exception e) {
        if (!(e instanceof DomainException)) {
            return null;
        }

        DomainException domainException = (DomainException) e;
        ActionMessages messages = getMessages(request);

        if (messages == null) {
            messages = new ActionMessages();
        }

        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(domainException.getKey(),
                domainException.getArgs()));
        saveMessages(request, messages);

        IViewState viewState = RenderUtils.getViewState();
        ViewDestination destination = viewState.getDestination("exception");
        if (destination != null) {
            return destination.getActionForward();
        } else {
            // show exception in output to ease finding the problem when
            // messages are not shown in page
            e.printStackTrace();
            return input;
        }
    }

    protected Object executeFactoryMethod(final HttpServletRequest request) throws FenixFilterException,
            FenixServiceException {
        final Object[] args = { getFactoryObject() };
        return executeService(request, "ExecuteFactoryMethod", args);
    }

    protected FactoryExecutor getFactoryObject() {
        return (FactoryExecutor) RenderUtils.getViewState().getMetaObject().getObject();
    }

    protected void addActionMessage(HttpServletRequest request, String key, String... args) {
        final ActionMessages actionMessages = (ActionMessages) request
                .getAttribute(ACTION_MESSAGES_REQUEST_KEY);
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key, args));
    }
    
    
    protected String[] solveLabelFormatterArgs(HttpServletRequest request,
            LabelFormatter[] labelFormatterArgs) {
        final String[] args = new String[labelFormatterArgs.length];
        int i = 0;
        final StrutsMessageResourceProvider messageResourceProvider = getMessageResourceProvider(request);
        for (final LabelFormatter labelFormatter : labelFormatterArgs) {
            args[i++] = labelFormatter.toString(messageResourceProvider);
        }

        return args;
    }

    protected StrutsMessageResourceProvider getMessageResourceProvider(HttpServletRequest request) {
        final StrutsMessageResourceProvider strutsMessageResourceProvider = new StrutsMessageResourceProvider(
                getLocale(request), getServlet().getServletContext(), request);
        strutsMessageResourceProvider.addMapping("enum", "ENUMERATION_RESOURCES");
        strutsMessageResourceProvider.addMapping("application", "DEFAULT");

        return strutsMessageResourceProvider;
    }

}
