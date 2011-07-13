package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.manager.functionalities;

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

@Mapping(module = "manager/functionalities", path = "/groupLanguage", scope = "session")
@Forwards(value = { @Forward(name = "show", path = "/manager/functionalities/group-language.jsp") })
public class GroupLanguageActionForManagerFunctionalities extends net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.GroupLanguageAction {
}