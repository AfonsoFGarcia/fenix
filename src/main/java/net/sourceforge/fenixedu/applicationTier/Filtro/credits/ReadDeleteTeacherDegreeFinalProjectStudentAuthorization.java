/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;

/**
 * @author jpvl
 */
public class ReadDeleteTeacherDegreeFinalProjectStudentAuthorization extends AbstractTeacherDepartmentAuthorization<Integer> {

    public static final ReadDeleteTeacherDegreeFinalProjectStudentAuthorization instance =
            new ReadDeleteTeacherDegreeFinalProjectStudentAuthorization();
    public final static ReadDeleteTeacherDegreeFinalProjectStudentAuthorization filter =
            new ReadDeleteTeacherDegreeFinalProjectStudentAuthorization();

    public static ReadDeleteTeacherDegreeFinalProjectStudentAuthorization getInstance() {
        return filter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization
     * #getTeacherId(java.lang.Object[])
     */
    @Override
    protected Integer getTeacherId(Integer teacherDegreeFinalProjectStudentId) {
        TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent =
                RootDomainObject.getInstance().readTeacherDegreeFinalProjectStudentByOID(teacherDegreeFinalProjectStudentId);
        return teacherDegreeFinalProjectStudent != null ? teacherDegreeFinalProjectStudent.getTeacher().getIdInternal() : null;
    }

}