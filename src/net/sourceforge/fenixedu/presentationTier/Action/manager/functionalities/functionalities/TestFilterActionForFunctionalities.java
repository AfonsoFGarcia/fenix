package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.functionalities;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "functionalities", path = "/private/filter", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "index", path = "/manager/functionalities/filter/index.jsp"),
		@Forward(name = "results", path = "/manager/functionalities/filter/index.jsp") })
public class TestFilterActionForFunctionalities extends
		net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.TestFilterAction {
}