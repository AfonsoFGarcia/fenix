/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.ICursoExecucao;

/**
 * @author Jo�o Mota
 *  
 */
public class InfoExecutionDegreeWithInfoExecutionYear extends
        InfoExecutionDegree {

    public void copyFromDomain(ICursoExecucao executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setInfoExecutionYear(InfoExecutionYear
                    .newInfoFromDomain(executionDegree.getExecutionYear()));
        }
    }

    /**
     * @param executionDegree
     * @return
     */
    public static InfoExecutionDegree newInfoFromDomain(
            ICursoExecucao executionDegree) {
        InfoExecutionDegreeWithInfoExecutionYear infoExecutionDegree = null;
        if (executionDegree != null) {
            infoExecutionDegree.copyFromDomain(executionDegree);
        }
        return infoExecutionDegree;
    }
}