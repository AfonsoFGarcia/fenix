package net.sourceforge.fenixedu.presentationTier.Action.manager.scientificCouncil;

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

@Mapping(module = "scientificCouncil", path = "/readTeacherInCharge", input = "/readCurricularCourse.do", attribute = "masterDegreeCreditsForm", formBean = "masterDegreeCreditsForm", scope = "request")
@Forwards(value = { @Forward(name = "readExecutionCourseTeachers", path = "/scientificCouncil/credits/readTeachers_bd.jsp") })
public class ReadTeacherInChargeActionForScientificCouncil extends net.sourceforge.fenixedu.presentationTier.Action.manager.ReadTeacherInChargeAction {
}