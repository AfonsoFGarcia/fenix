/*
 * Created on 29/Jun/2004
 *
 */
package DataBeans;

import Dominio.ICurricularSemester;

/**
 * @author T�nia Pous�o
 *
 */
public class InfoCurricularSemesterWithInfoCurricularYear extends
        InfoCurricularSemester {

    /* (non-Javadoc)
     * @see DataBeans.InfoCurricularSemester#copyFromDomain(Dominio.ICurricularSemester)
     */
    public void copyFromDomain(ICurricularSemester curricularSemester) {
        super.copyFromDomain(curricularSemester);
        if(curricularSemester != null) {
            setInfoCurricularYear(InfoCurricularYear.newInfoFromDomain(curricularSemester.getCurricularYear()));
        }
    }
    
    public static InfoCurricularSemester newInfoFromDomain(ICurricularSemester curricularSemester) {
        InfoCurricularSemesterWithInfoCurricularYear infoCurricularSemester = null;
        if(curricularSemester != null) {
            infoCurricularSemester = new InfoCurricularSemesterWithInfoCurricularYear();
            infoCurricularSemester.copyFromDomain(curricularSemester);
        }
        return infoCurricularSemester;
    }
}
