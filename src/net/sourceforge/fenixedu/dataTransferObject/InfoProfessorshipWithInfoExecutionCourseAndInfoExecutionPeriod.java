/*
 * Created on 6/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IProfessorship;

/**
 * @author T�nia Pous�o
 *  
 */
public class InfoProfessorshipWithInfoExecutionCourseAndInfoExecutionPeriod extends InfoProfessorship {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship#copyFromDomain(Dominio.IProfessorship)
     */
    public void copyFromDomain(IProfessorship professorship) {
        super.copyFromDomain(professorship);
        if (professorship != null) {
            setInfoExecutionCourse(InfoExecutionCourseWithExecutionPeriod
                    .newInfoFromDomain(professorship.getExecutionCourse()));
        }
    }

    public static InfoProfessorship newInfoFromDomain(IProfessorship professorship) {
        InfoProfessorshipWithInfoExecutionCourseAndInfoExecutionPeriod infoProfessorship = null;
        if (professorship != null) {
            infoProfessorship = new InfoProfessorshipWithInfoExecutionCourseAndInfoExecutionPeriod();
            infoProfessorship.copyFromDomain(professorship);
        }
        return infoProfessorship;
    }
}