/*
 * Created on Jul 8, 2004
 *
 */
package Dominio;

/**
 * @author Jo�o Mota
 *
 */
public class StudentCurricularPlanLEIC extends StudentCurricularPlan implements
        IStudentCurricularPlan {

    public boolean getCanChangeSpecializationArea() {
        return false;
    }
}
