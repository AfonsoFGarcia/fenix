/*
 * Created on 16/Nov/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Pedro Santos & Rita Carvalho
 */
public class ReadExecutionDegreeRecentByDegreeId implements IService {

    public InfoExecutionDegree run(final Integer degreeId) throws ExcepcaoPersistencia{
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
        List listExecutionDegrees = persistentExecutionDegree.readAll();
        CollectionUtils.filter(listExecutionDegrees, new Predicate(){

            public boolean evaluate(Object arg0) {
                ExecutionDegree executionDegree = (ExecutionDegree) arg0;
                
                return executionDegree.getDegreeCurricularPlan().getDegree().getIdInternal().equals(degreeId);
            }
        });
        
        
        ComparatorChain chain = new ComparatorChain(new BeanComparator("degreeCurricularPlan.initialDate"));
        chain.addComparator(new BeanComparator("executionYear.year"));
        ExecutionDegree executionDegree = (ExecutionDegree) Collections.max(listExecutionDegrees, chain);
        
        InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
        
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(executionDegree.getDegreeCurricularPlan());
        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        
        
        return infoExecutionDegree;
    }
}
