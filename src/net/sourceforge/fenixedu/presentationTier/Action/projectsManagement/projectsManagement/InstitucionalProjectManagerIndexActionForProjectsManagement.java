package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.projectsManagement;

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

@Mapping(module = "projectsManagement", path = "/institucionalProjectIndex", scope = "session")
@Forwards(value = { @Forward(name = "success", path = "/projectsManagement/firstPage.jsp", tileProperties = @Tile(navLocal = "/projectsManagement/costCenterNavBar.jsp")) })
public class InstitucionalProjectManagerIndexActionForProjectsManagement extends net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.InstitucionalProjectManagerIndexAction {
}