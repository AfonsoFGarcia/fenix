/**
 * @author Goncalo Luiz, 29/8/2005
 */

package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.ICurricularCourse;

public class InfoMinimalCurricularCourse extends InfoCurricularCourse{

    String name;
    
    public void copyFromDomain(ICurricularCourse curricularCourse) {
        if (curricularCourse != null) {
            setName(curricularCourse.getName());            
        }
    }

    public static InfoMinimalCurricularCourse newInfoFromDomain(ICurricularCourse curricularCourse) {
        InfoMinimalCurricularCourse infoCurricularCourse = null;
        if (curricularCourse != null) {
            infoCurricularCourse = new InfoMinimalCurricularCourse();
            infoCurricularCourse.copyFromDomain(curricularCourse);
        }
        return infoCurricularCourse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
