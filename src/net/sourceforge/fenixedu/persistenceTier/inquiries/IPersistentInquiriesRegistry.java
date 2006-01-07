/*
 * Created on 11/Abr/2005 - 16:10:18
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Jo�o Fialho & Rita Ferreira
 *
 */
public interface IPersistentInquiriesRegistry extends IPersistentObject {

	public List<InquiriesRegistry> readByStudentId(Integer studentId)
	throws ExcepcaoPersistencia;
	
	public List<InquiriesRegistry> readByExecutionPeriodId(Integer executionPeriodId)
	throws ExcepcaoPersistencia;


}
