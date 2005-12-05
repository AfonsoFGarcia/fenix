package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;

import org.apache.ojb.broker.query.Criteria;

public class CompetenceCourseOJB extends ObjectFenixOJB implements IPersistentCompetenceCourse{

    public List<ICompetenceCourse> readByCurricularStage(CurricularStage curricularStage) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularStage", curricularStage);
        return queryList(CompetenceCourse.class, criteria);
    }

}
