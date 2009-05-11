package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ReadTeacherInformation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteTeacherInformation;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;

public class ReadTeachersInformation extends FenixService {

    public List run(Integer executionDegreeId, Boolean basic, String executionYearString) throws FenixServiceException {

	List<Professorship> professorships = null;
	ExecutionYear executionYear = null;

	if (executionYearString != null && !executionYearString.equals("")) {
	    executionYear = ExecutionYear.readExecutionYearByName(executionYearString);
	} else {
	    executionYear = ExecutionYear.readCurrentExecutionYear();
	}
	if (executionDegreeId == null) {
	    List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllByExecutionYear(executionYear.getYear());
	    List<DegreeCurricularPlan> degreeCurricularPlans = getDegreeCurricularPlans(executionDegrees);
	    ExecutionYear executionDegressExecutionYear = (!degreeCurricularPlans.isEmpty()) ? executionDegrees.get(0)
		    .getExecutionYear() : null;

	    if (basic == null) {
		professorships = Professorship.readByDegreeCurricularPlansAndExecutionYear(degreeCurricularPlans,
			executionDegressExecutionYear);
	    } else {
		professorships = Professorship.readByDegreeCurricularPlansAndExecutionYearAndBasic(degreeCurricularPlans,
			executionDegressExecutionYear, basic);
	    }
	} else {
	    ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);

	    if (basic == null) {
		professorships = Professorship.readByDegreeCurricularPlanAndExecutionYear(executionDegree
			.getDegreeCurricularPlan(), executionDegree.getExecutionYear());
	    } else {
		professorships = Professorship.readByDegreeCurricularPlanAndExecutionYearAndBasic(executionDegree
			.getDegreeCurricularPlan(), executionDegree.getExecutionYear(), basic);
	    }
	}

	Set<Teacher> teachers = new HashSet<Teacher>();
	for (Professorship professorship : professorships) {
	    if (professorship.hasTeacher()) {
		teachers.add(professorship.getTeacher());
	    }
	}

	List<InfoSiteTeacherInformation> infoSiteTeachersInformation = new ArrayList<InfoSiteTeacherInformation>();
	ReadTeacherInformation readTeacherInformationService = new ReadTeacherInformation();

	for (Teacher teacher : teachers) {
	    InfoSiteTeacherInformation information = (InfoSiteTeacherInformation) readTeacherInformationService.run(
		    teacher.getPerson().getUsername(), executionYearString).getComponent();
	    infoSiteTeachersInformation.add(information);
	}
	Collections.sort(infoSiteTeachersInformation, new Comparator() {
	    public int compare(Object o1, Object o2) {
		InfoSiteTeacherInformation information1 = (InfoSiteTeacherInformation) o1;
		InfoSiteTeacherInformation information2 = (InfoSiteTeacherInformation) o2;
		return information1.getInfoTeacher().getInfoPerson().getNome().compareTo(
			information2.getInfoTeacher().getInfoPerson().getNome());
	    }
	});
	return infoSiteTeachersInformation;

    }

    private List<DegreeCurricularPlan> getDegreeCurricularPlans(final List<ExecutionDegree> executionDegrees) {
	final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	for (final ExecutionDegree executionDegree : executionDegrees) {
	    result.add(executionDegree.getDegreeCurricularPlan());
	}
	return result;
    }

}