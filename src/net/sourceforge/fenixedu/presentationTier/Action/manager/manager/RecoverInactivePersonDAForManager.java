package net.sourceforge.fenixedu.presentationTier.Action.manager.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "manager",
		path = "/recoverInactivePerson",
		input = "/recoverInactivePerson.do?method=prepare&page=0",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(name = "showSearchForm", path = "/manager/personManagement/recoverInactivePerson.jsp") })
public class RecoverInactivePersonDAForManager extends
		net.sourceforge.fenixedu.presentationTier.Action.manager.RecoverInactivePersonDA {
}