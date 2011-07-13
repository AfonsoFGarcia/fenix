package net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses.operator;

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

@Mapping(module = "operator", path = "/createEditCompetenceCourse", attribute = "createEditCompetenceCourseForm", formBean = "createEditCompetenceCourseForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "edit", path = "df.page.editCompetenceCourse"),
		@Forward(name = "createCompetenceCourse", path = "df.page.createCompetenceCourse"),
		@Forward(name = "showCompetenceCourse", path = "df.page.showCompetenceCourse") })
public class CreateEditCompetenceCourseDispatchActionForOperator extends net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses.CreateEditCompetenceCourseDispatchAction {
}