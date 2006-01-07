package relations;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PublicationTeacherTeacher extends PublicationTeacherTeacher_Base {

    public static void add(net.sourceforge.fenixedu.domain.publication.PublicationTeacher teacherPublications, net.sourceforge.fenixedu.domain.Teacher teacher) throws DomainException {
	if (teacher.canAddPublicationToTeacherInformationSheet(teacherPublications.getPublicationArea())) {
	    PublicationTeacherTeacher_Base.add(teacherPublications, teacher);
	} else {
	    throw new DomainException("error.teacherSheetFull", teacherPublications.getPublicationArea().getName());
	}
    }
}
