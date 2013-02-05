package net.sourceforge.fenixedu.domain.functionalities;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupContext;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;

/**
 * A wrapper to a {@link FunctionalityContext} so that it can be used as a {@link GroupContext}.
 * 
 * @author cfgi
 */
public class GroupContextFromFunctionality implements GroupContext {

    private FunctionalityContext context;
    private Map<String, Object> variables;

    public GroupContextFromFunctionality(FunctionalityContext context) {
        this.context = context;

        setupVariables();
    }

    /**
     * @return the functionality context wrapped by this group context
     */
    public FunctionalityContext getContext() {
        return this.context;
    }

    private void setupVariables() {
        this.variables = new HashMap<String, Object>();

        this.variables.put(VAR_USER, getContext().getLoggedUser());
        this.variables.put(VAR_USERVIEW, getContext().getLoggedUser());
        this.variables.put(VAR_REQUEST, getContext().getRequest());
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getParameter(String name) {
        return getContext().getRequest().getParameter(name);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object getVariable(String name) throws VariableNotDefinedException {
        if (!this.variables.containsKey(name)) {
            throw new VariableNotDefinedException(name);
        }

        return this.variables.get(name);
    }

}
