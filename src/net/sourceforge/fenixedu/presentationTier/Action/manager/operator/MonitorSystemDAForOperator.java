package net.sourceforge.fenixedu.presentationTier.Action.manager.operator;

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

@Mapping(module = "operator", path = "/monitorSystem", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "Show", path = "/operator/systemManagement/monitorSystem_bd.jsp") })
public class MonitorSystemDAForOperator extends net.sourceforge.fenixedu.presentationTier.Action.manager.MonitorSystemDA {
}