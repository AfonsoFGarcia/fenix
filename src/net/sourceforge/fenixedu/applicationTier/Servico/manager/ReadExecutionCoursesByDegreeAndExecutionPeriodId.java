/*
 * Created on 3/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> 3/Dez/2003
 *  
 */
public class ReadExecutionCoursesByDegreeAndExecutionPeriodId implements IService {

    public List run(Integer degreeId, Integer executionPeriodId) throws FenixServiceException, ExcepcaoPersistencia {
            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod persistentExecutionPeriod = ps.getIPersistentExecutionPeriod();
            ICursoPersistente persistentDegree = ps.getICursoPersistente();

            final IExecutionPeriod executionPeriod2Compare = (IExecutionPeriod) persistentExecutionPeriod
                    .readByOID(ExecutionPeriod.class, executionPeriodId);
            if (executionPeriod2Compare == null) {
                throw new InvalidArgumentsServiceException();
            }
            IDegree degree = (IDegree) persistentDegree.readByOID(Degree.class, degreeId);
            if (degree == null) {
                throw new InvalidArgumentsServiceException();
            }
            List<IDegreeCurricularPlan> curricularPlans = degree.getDegreeCurricularPlans();
            List curricularCourses = new ArrayList();
            for (IDegreeCurricularPlan degreeCurricularPlan : curricularPlans) {
                if (degreeCurricularPlan.getState().equals(DegreeCurricularPlanState.ACTIVE) && degreeCurricularPlan.getCurricularStage().equals(CurricularStage.OLD)) {
                    curricularCourses.addAll(degreeCurricularPlan.getCurricularCourses());
                }
            }
            List executionCourses = new ArrayList();
            Iterator iter = curricularCourses.iterator();
            while (iter.hasNext()) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
                executionCourses.addAll(CollectionUtils.select(curricularCourse
                        .getAssociatedExecutionCourses(), new Predicate() {
                    public boolean evaluate(Object arg0) {
                        IExecutionCourse executionCourse = (IExecutionCourse) arg0;

                        return executionCourse.getExecutionPeriod().equals(executionPeriod2Compare);
                    }
                }));
            }
            List infoExecutionCourses = (List) CollectionUtils.collect(executionCourses,
                    new Transformer() {
                        public Object transform(Object arg0) {
                            return InfoExecutionCourse.newInfoFromDomain((IExecutionCourse) arg0);
                        }
                    });
            return infoExecutionCourses;

    }
}