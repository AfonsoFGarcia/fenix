package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IPrice;
import Dominio.Price;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPrice;
import Util.DocumentType;
import Util.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PriceOJB extends PersistentObjectOJB implements IPersistentPrice {

    public PriceOJB() {
    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(Price.class, new Criteria());
    }

    public List readByGraduationType(GraduationType graduationType) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("graduationType", graduationType.getType());
        return queryList(Price.class, crit);

    }

    public IPrice readByGraduationTypeAndDocumentTypeAndDescription(GraduationType graduationType,
            DocumentType documentType, String description) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("graduationType", graduationType.getType());
        crit.addEqualTo("documentType", documentType.getType());
        crit.addEqualTo("description", description);
        return (IPrice) queryObject(Price.class, crit);

    }

    public List readByGraduationTypeAndDocumentType(GraduationType graduationType,
            DocumentType documentType) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("graduationType", graduationType.getType());
        crit.addEqualTo("documentType", documentType.getType());

        return queryList(Price.class, crit);

    }

    public List readByGraduationTypeAndDocumentType(GraduationType graduationType, List types)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        Criteria criteriaDocs = new Criteria();
        criteria.addEqualTo("graduationType", graduationType.getType());
        List typesInteger = new ArrayList();
        Iterator iterator = types.iterator();
        while (iterator.hasNext()) {
            typesInteger.add(((DocumentType) iterator.next()).getType());

        }
        criteriaDocs.addIn("documentType", typesInteger);
        criteria.addAndCriteria(criteriaDocs);
        List result = queryList(Price.class, criteria);
        return result;

    }
}