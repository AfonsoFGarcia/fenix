package Dominio;

import java.util.ArrayList;
import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AreaType;

/**
 * @author Jo�o Mota in Aug 10, 2004
 */

public class DegreeCurricularPlanLEICTAGUS extends DegreeCurricularPlan implements IDegreeCurricularPlan {

    public DegreeCurricularPlanLEICTAGUS() {
        ojbConcreteClass = getClass().getName();
    }

    public List getCurricularCoursesFromArea(IBranch area, AreaType areaType) {

        List curricularCourses = new ArrayList();

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                    .getIPersistentCurricularCourseGroup();

            List groups = curricularCourseGroupDAO.readByBranchAndAreaType(area, areaType);

            int groupsSize = groups.size();

            for (int i = 0; i < groupsSize; i++) {
                ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) groups.get(i);

                List courses = curricularCourseGroup.getCurricularCourses();

                int coursesSize = courses.size();

                for (int j = 0; j < coursesSize; j++) {
                    ICurricularCourse curricularCourse = (ICurricularCourse) courses.get(j);

                    if (!curricularCourses.contains(curricularCourse)) {
                        curricularCourses.add(curricularCourse);
                    }
                }
            }

        } catch (ExcepcaoPersistencia e) {
            throw new RuntimeException(e);
        }

        return curricularCourses;
    }

    public List getSecundaryAreas() {
        return getSpecializationAreas();
    }

}