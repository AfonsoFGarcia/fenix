/*
 * CurriculumOJB.java
 * 
 * Created on 6 de Janeiro de 2003, 20:44
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author Jo�o Mota
 */
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;

import org.apache.ojb.broker.query.Criteria;

public class CurriculumOJB extends PersistentObjectOJB implements IPersistentCurriculum {

    public Curriculum readCurriculumByCurricularCourse(Integer curricularCourseOID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourseOID);
        List curricularInformation = queryList(Curriculum.class, criteria, "lastModificationDate", false);
        return (curricularInformation != null && curricularInformation.size() > 0 ? (Curriculum) curricularInformation
                .get(0)
                : null);
    }

    public Curriculum readCurriculumByCurricularCourseAndExecutionYear(
            Integer curricularCourseOID, Date executionYearEndDate)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourseOID);
        criteria.addLessOrEqualThan("lastModificationDate", executionYearEndDate);
        List curricularInformation = queryList(Curriculum.class, criteria, "lastModificationDate", false);
        return (curricularInformation != null && curricularInformation.size() > 0 ? (Curriculum) curricularInformation
                .get(0)
                : null);
    }
}