package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.researcher;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.RegistrationDA;
import net.sourceforge.fenixedu.presentationTier.Action.research.FinalWorkManagementActionForResearcher;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "researcher", path = "/registration", functionality = FinalWorkManagementActionForResearcher.class)
@Forwards(value = {
        @Forward(name = "view-registration-curriculum", path = "/student/curriculum/viewRegistrationCurriculum.jsp"),
        @Forward(name = "chooseCycleForViewRegistrationCurriculum",
                path = "/student/curriculum/chooseCycleForViewRegistrationCurriculum.jsp") })
public class RegistrationDAForResearcher extends RegistrationDA {
}