/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;

/**
 * @author jpvl
 */
public class CreditsServiceWithInfoTeacherArgumentAuthorization extends AbstractTeacherDepartmentAuthorization<InfoTeacher> {

    public static final CreditsServiceWithInfoTeacherArgumentAuthorization instance =
            new CreditsServiceWithInfoTeacherArgumentAuthorization();
    public final static CreditsServiceWithInfoTeacherArgumentAuthorization filter =
            new CreditsServiceWithInfoTeacherArgumentAuthorization();

    public static CreditsServiceWithInfoTeacherArgumentAuthorization getInstance() {
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
    protected Integer getTeacherId(InfoTeacher infoTeacher) {
        return infoTeacher != null ? infoTeacher.getIdInternal() : null;
    }

}