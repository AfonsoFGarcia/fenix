/*
 * Created on 29/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CurricularCourse;

/**
 * @author T�nia Pous�o
 *  
 */
public class InfoCurricularCourseWithInfoDegreeCurricularPlan extends InfoCurricularCourse {

    public InfoCurricularCourseWithInfoDegreeCurricularPlan(CurricularCourse curricularCourse) {
		super(curricularCourse);
	}

	public void copyFromDomain(CurricularCourse curricularCourse) {
        super.copyFromDomain(curricularCourse);
        if (curricularCourse != null) {
            setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan.newInfoFromDomain(curricularCourse
                    .getDegreeCurricularPlan()));
        }
    }

    public static InfoCurricularCourse newInfoFromDomain(CurricularCourse curricularCourse) {
        InfoCurricularCourseWithInfoDegreeCurricularPlan infoCurricularCourse = null;
        if (curricularCourse != null) {
            infoCurricularCourse = new InfoCurricularCourseWithInfoDegreeCurricularPlan(curricularCourse);
            infoCurricularCourse.copyFromDomain(curricularCourse);
        }

        return infoCurricularCourse;
    }
}