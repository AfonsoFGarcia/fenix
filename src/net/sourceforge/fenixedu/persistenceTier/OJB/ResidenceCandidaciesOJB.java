/*
 * Created on Aug 4, 2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.student.ResidenceCandidacies;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResidenceCandidacies;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ResidenceCandidaciesOJB extends PersistentObjectOJB implements
        IPersistentResidenceCandidacies {

    public ResidenceCandidaciesOJB() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentResidenceCandidacies#readResidenceCandidaciesByStudent()
     */
    public List readResidenceCandidaciesByStudent(Student student) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", student.getIdInternal());
        return queryList(ResidenceCandidacies.class, criteria);
    }

}