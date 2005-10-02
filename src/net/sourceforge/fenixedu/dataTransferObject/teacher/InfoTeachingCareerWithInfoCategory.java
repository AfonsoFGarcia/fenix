/*
 * Created on 30/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.teacher.ITeachingCareer;

/**
 * @author T�nia Pous�o
 *  
 */
public class InfoTeachingCareerWithInfoCategory extends InfoTeachingCareer {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.teacher.InfoTeachingCareer#copyFromDomain(Dominio.teacher.ITeachingCareer)
     */
    public void copyFromDomain(ITeachingCareer teachingCareer) {
        super.copyFromDomain(teachingCareer);
        if (teachingCareer != null) {
            setInfoCategory(InfoCategory.newInfoFromDomain(teachingCareer.getCategory()));
			setInfoTeacher(InfoTeacher.newInfoFromDomain(teachingCareer.getTeacher()));
        }
    }

    public static InfoTeachingCareer newInfoFromDomain(ITeachingCareer teachingCareer) {
        InfoTeachingCareerWithInfoCategory infoTeachingCareer = null;
        if (teachingCareer != null) {
            infoTeachingCareer = new InfoTeachingCareerWithInfoCategory();
            infoTeachingCareer.copyFromDomain(teachingCareer);
        }
        return infoTeachingCareer;
    }
}