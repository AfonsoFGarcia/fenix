package ServidorAplicacao.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/*
 * 
 * @author Fernanda Quit�rio 22/Dez/2003
 */

public class ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear
        implements IServico {

    private static ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear _servico = new ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear();

    /**
     * The actor of this class.
     */

    private ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear
     */
    public static ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear getService() {
        return _servico;
    }

    public Object run(Integer executionDegreeId, Integer executionPeriodId,
            Integer curricularYear) throws FenixServiceException {

        List infoExecutionCourseList = new ArrayList();
        try {
            List executionCourseList = null;
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse executionCourseDAO = sp
                    .getIPersistentExecutionCourse();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp
                    .getIPersistentExecutionPeriod();
            ICursoExecucaoPersistente persistentExecutionDegree = sp
                    .getICursoExecucaoPersistente();

            if (executionPeriodId == null) {
                throw new FenixServiceException("nullExecutionPeriodId");
            }

            IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod
                    .readByOID(ExecutionPeriod.class, executionPeriodId);

            if (executionDegreeId == null && curricularYear == null) {
                executionCourseList = executionCourseDAO
                        .readByExecutionPeriodWithNoCurricularCourses(executionPeriod);

            } else {
                ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree
                        .readByOID(CursoExecucao.class, executionDegreeId);

                executionCourseList = executionCourseDAO
                        .readByCurricularYearAndExecutionPeriodAndExecutionDegree(
                                curricularYear, executionPeriod,
                                executionDegree);
            }

            CollectionUtils.collect(executionCourseList, new Transformer() {
                public Object transform(Object input) {
                    IExecutionCourse executionCourse = (IExecutionCourse) input;
                    return Cloner.get(executionCourse);
                }
            }, infoExecutionCourseList);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }

        return infoExecutionCourseList;
    }
}