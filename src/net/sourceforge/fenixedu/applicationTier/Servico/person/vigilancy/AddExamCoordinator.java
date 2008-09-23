package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;

public class AddExamCoordinator extends FenixService {

    public void run(Person person, ExecutionYear executionYear, Unit unit) {

	person.addPersonRoleByRoleType(RoleType.EXAM_COORDINATOR);
	new ExamCoordinator(person, executionYear, unit);

    }

}
