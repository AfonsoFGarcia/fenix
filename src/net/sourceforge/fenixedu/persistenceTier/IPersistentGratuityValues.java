/*
 * Created on 9/Jan/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.GratuityValues;

/**
 * @author T�nia Pous�o
 *  
 */
public interface IPersistentGratuityValues extends IPersistentObject {
    public GratuityValues readGratuityValuesByExecutionDegree(Integer executionDegreeID)
            throws ExcepcaoPersistencia;
}