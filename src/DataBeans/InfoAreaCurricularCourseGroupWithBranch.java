/*
 * Created on 26/Nov/2003
 *  
 */
package DataBeans;

import Dominio.ICurricularCourseGroup;

/**
 * @author Jo�o Mota
 */

public class InfoAreaCurricularCourseGroupWithBranch extends InfoAreaCurricularCourseGroup {



    public InfoAreaCurricularCourseGroupWithBranch() {
    }

   

    public void copyFromDomain(ICurricularCourseGroup curricularCourseGroup) {
        super.copyFromDomain(curricularCourseGroup);
        if (curricularCourseGroup != null) {            
           setInfoBranch(InfoBranch.newInfoFromDomain(curricularCourseGroup.getBranch()));
        }
    }
    public static InfoCurricularCourseGroup newInfoFromDomain(
            ICurricularCourseGroup curricularCourseGroup) {
        InfoCurricularCourseGroup infoCurricularCourseGroup = null;
        if(curricularCourseGroup != null) {
            infoCurricularCourseGroup = new InfoAreaCurricularCourseGroupWithBranch();
            infoCurricularCourseGroup.copyFromDomain(curricularCourseGroup);
        }
        return infoCurricularCourseGroup;
       
    }
    
}