/*
 * Created on 30/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;

/**
 * @author Jo�o Mota
 *  
 */
public interface IPersistentWrittenEvaluationEnrolment extends IPersistentObject {

    public List readByExamOID(Integer examOID) throws ExcepcaoPersistencia;

    public List readByStudentOID(Integer studentOID) throws ExcepcaoPersistencia;

    public WrittenEvaluationEnrolment readBy(Integer examOID, Integer studentOID) throws ExcepcaoPersistencia;

}