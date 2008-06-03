/*
 * Created on 12/Mar/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCategory;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author Jo�o Mota
 */
public class InfoTeacher extends InfoObject  {

    private DomainReference<Teacher> teacher;

    // This constructor is needed because of CRUDActionByOID. Do not delete it.
    public InfoTeacher() { }

    public InfoTeacher(final Teacher teacher) {
        this.teacher = new DomainReference<Teacher>(teacher);
    }

    public List getProfessorShipsExecutionCourses() {
        final List<InfoProfessorship> infoProfessorships = new ArrayList<InfoProfessorship>();
        for (final Professorship professorship : getTeacher().getProfessorshipsSet()) {
            infoProfessorships.add(InfoProfessorship.newInfoFromDomain(professorship));
        }
        return infoProfessorships;
    }

    public List getResponsibleForExecutionCourses() {
        final List<InfoProfessorship> infoProfessorships = new ArrayList<InfoProfessorship>();
        for (final Professorship professorship : getTeacher().getProfessorshipsSet()) {
            if (professorship.isResponsibleFor()) {
                infoProfessorships.add(InfoProfessorship.newInfoFromDomain(professorship));
            }
        }
        return infoProfessorships;
    }

    public Integer getTeacherNumber() {
        return getTeacher().getTeacherNumber();
    }

    public InfoPerson getInfoPerson() {
        return InfoPerson.newInfoFromDomain(getTeacher().getPerson());
    }

    public InfoCategory getInfoCategory() {
        return InfoCategory.newInfoFromDomain(getTeacher().getCategory());
    }

    public boolean equals(Object obj) {
        return obj != null && getTeacher() == ((InfoTeacher) obj).teacher.getObject();
    }

    public String toString() {
        return getTeacher().toString();
    }

    public static InfoTeacher newInfoFromDomain(final Teacher teacher) {
        return teacher == null ? null : new InfoTeacher(teacher);
    }

    @Override
    public Integer getIdInternal() {
        return getTeacher().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
        // This attribution is needed because of CRUDActionByOID. Do not delete it.
        this.teacher = new DomainReference<Teacher>(RootDomainObject.getInstance().readTeacherByOID(integer));
    }

    public Teacher getTeacher() {
        return teacher == null ? null : teacher.getObject();
    }

}