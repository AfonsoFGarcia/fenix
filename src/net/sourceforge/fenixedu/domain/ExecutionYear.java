package net.sourceforge.fenixedu.domain;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.fileSuport.INode;

/**
 * Created on 11/Fev/2003
 * 
 * @author Jo�o Mota ciapl Dominio
 * 
 */
public class ExecutionYear extends ExecutionYear_Base {

    public String toString() {
        String result = "[EXECUTION_YEAR";
        result += ", internalCode=" + getIdInternal();
        result += ", year=" + getYear();
        result += ", begin=" + getBeginDate();
        result += ", end=" + getEndDate();
        result += "]";
        return result;
    }

    public String getSlideName() {
        String result = "/EY" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        return null;
    }

    public Collection<IExecutionDegree> getExecutionDegreesByType(final DegreeType degreeType) {

        return CollectionUtils.select(getExecutionDegrees(), new Predicate() {
            public boolean evaluate(Object arg0) {
                IExecutionDegree executionDegree = (IExecutionDegree) arg0;
                return executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                        degreeType);
            }
        });

    }

}
