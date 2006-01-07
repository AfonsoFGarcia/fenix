/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class CreditsServiceWithInfoProfessorshipArgumentAuthorization extends
        AbstractTeacherDepartmentAuthorization {

    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        InfoProfessorship infoProfessorship = (InfoProfessorship) arguments[0];

        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
        Teacher teacher = teacherDAO
                .readByNumber(infoProfessorship.getInfoTeacher().getTeacherNumber());

        return teacher == null ? null : teacher.getIdInternal();
    }

}