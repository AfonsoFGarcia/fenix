/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author Jo�o Mota
 *  
 */
public class InfoTeacherWithPerson extends InfoTeacher {

    public void copyFromDomain(ITeacher teacher) {
        super.copyFromDomain(teacher);
        if (teacher != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(teacher.getPerson()));
        }
    }

    public static InfoTeacher newInfoFromDomain(ITeacher teacher) {
        InfoTeacherWithPerson infoTeacher = null;
        if (teacher != null) {
            infoTeacher = new InfoTeacherWithPerson();
            infoTeacher.copyFromDomain(teacher);
        }
        return infoTeacher;
    }

    public void copyToDomain(InfoTeacher infoTeacher, ITeacher teacher) {
        super.copyToDomain(infoTeacher, teacher);
        teacher.setPerson(InfoPersonWithInfoCountry.newDomainFromInfo(infoTeacher.getInfoPerson()));
    }

    public static ITeacher newDomainFromInfo(InfoTeacher infoTeacher) {
        ITeacher teacher = null;
        InfoTeacherWithPerson infoTeacherWithPerson = null;
        if (infoTeacher != null) {
            teacher = new Teacher();
            infoTeacherWithPerson = new InfoTeacherWithPerson();
            infoTeacherWithPerson.copyToDomain(infoTeacher, teacher);
        }
        return teacher;
    }
}