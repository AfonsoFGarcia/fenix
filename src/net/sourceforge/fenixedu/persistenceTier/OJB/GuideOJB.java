package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuide;
import net.sourceforge.fenixedu.util.State;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GuideOJB extends PersistentObjectOJB implements IPersistentGuide {

    public Integer generateGuideNumber(Integer year) {

        Integer guideNumber = new Integer(0);

        Criteria criteria = new Criteria();
        criteria.addEqualTo("year", year);

        Iterator iterator = readIteratorByCriteria(Guide.class, criteria, "number", false);

        if (iterator.hasNext()) {
            guideNumber = ((Guide) iterator.next()).getNumber();
        }

        return new Integer(guideNumber.intValue() + 1);

    }

    public Guide readByNumberAndYearAndVersion(Integer number, Integer year, Integer version)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("number", number);
        crit.addEqualTo("year", year);
        crit.addEqualTo("version", version);
        return (Guide) queryObject(Guide.class, crit);

    }

    /**
     * IMPORTANT: If the ordering is changed here you must change the private
     * method getLatestVersions in the ChooseGuide Service
     */
    public List readByYear(Integer year) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        //QueryByCriteria query = new QueryByCriteria(Guide.class, criteria);

        //	criteria.addOrderByAscending("number");
        //	criteria.addOrderByDescending("version");

        criteria.addEqualTo("year", year);

        List result = queryList(Guide.class, criteria);
        //(List)
        // PersistenceBrokerFactory.defaultPersistenceBroker().getCollectionByQuery(query);

        if ((result == null) || (result.size() == 0)) {
            return null;
        }

        return result;
    }

    public Guide readLatestVersion(Integer year, Integer number) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        QueryByCriteria query = new QueryByCriteria(Guide.class, criteria);
        query.addOrderByDescending("version");

        criteria.addEqualTo("year", year);
        criteria.addEqualTo("number", number);

        List result = queryList(Guide.class, criteria);

        if (result.size() != 0) {
            return (Guide) result.get(0);
        }

        return null;
    }

    public List readByPerson(String identificationDocumentNumber,
            IDDocumentType identificationDocumentType) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        //QueryByCriteria query = new QueryByCriteria(Guide.class, criteria);

        //		query.addGroupBy("number");

        criteria.addEqualTo("person.documentIdNumber", identificationDocumentNumber);
        criteria.addEqualTo("person.idDocumentType", identificationDocumentType);

        List result = queryList(Guide.class, criteria);

        return result;
    }

    public List readByYearAndState(Integer guideYear, GuideState situationOfGuide)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        if (guideYear != null) {
            criteria.addEqualTo("year", guideYear);
        }

        if (situationOfGuide != null) {
            criteria.addEqualTo("guideSituations.state", new State(State.ACTIVE));
            criteria.addEqualTo("guideSituations.situation", situationOfGuide);
        }

        return queryList(Guide.class, criteria);
    }

    public List readByNumberAndYear(Integer number, Integer year) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("number", number);
        criteria.addEqualTo("year", year);

        List result = queryList(Guide.class, criteria, "version", true);

        return result;
    }

}