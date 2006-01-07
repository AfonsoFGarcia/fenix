package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularYear;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author dcs-rjao 20/Mar/2003
 */

public class CurricularYearOJB extends PersistentObjectOJB implements IPersistentCurricularYear {

    public CurricularYearOJB() {
    }

    public CurricularYear readCurricularYearByYear(Integer year) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("year", year);
        return (CurricularYear) queryObject(CurricularYear.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(CurricularYear.class, new Criteria());
    }
}