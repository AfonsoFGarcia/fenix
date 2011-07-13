package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity.directiveCouncil;

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

@Mapping(module = "directiveCouncil", path = "/studentsGratuityList", input = "/studentsGratuityList.do?method=prepareChooseDegree&page=0", attribute = "studentsGratuityListForm", formBean = "studentsGratuityListForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "studentsGratuityList", path = "studentsGratuityList"),
		@Forward(name = "choose", path = "/studentsGratuityList.do?method=chooseExecutionYear&page=0"),
		@Forward(name = "chooseStudentsGratuityList", path = "chooseStudentsGratuityList") })
public class StudentsGratuityListActionForDirectiveCouncil extends net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity.StudentsGratuityListAction {
}