/*
 * Created on 26/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CurricularCourseGroup;

/**
 * @author Jo�o Mota
 */

public class InfoOptionalCurricularCourseGroupWithBranch extends InfoOptionalCurricularCourseGroup {

    public InfoOptionalCurricularCourseGroupWithBranch() {
    }

    public void copyFromDomain(CurricularCourseGroup curricularCourseGroup) {
        super.copyFromDomain(curricularCourseGroup);
        if (curricularCourseGroup != null) {
            setInfoBranch(InfoBranch.newInfoFromDomain(curricularCourseGroup.getBranch()));
        }
    }

    public static InfoCurricularCourseGroup newInfoFromDomain(
            CurricularCourseGroup curricularCourseGroup) {
        InfoCurricularCourseGroup infoCurricularCourseGroup = null;
        if (curricularCourseGroup != null) {
            infoCurricularCourseGroup = new InfoOptionalCurricularCourseGroupWithBranch();
            infoCurricularCourseGroup.copyFromDomain(curricularCourseGroup);
        }
        return infoCurricularCourseGroup;
    }
}