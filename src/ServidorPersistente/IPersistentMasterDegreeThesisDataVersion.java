/*
 * Created on Oct 14, 2003
 *
 */
package ServidorPersistente;

import Dominio.IMasterDegreeThesis;
import Dominio.IMasterDegreeThesisDataVersion;
import Dominio.IStudentCurricularPlan;

/**
 * @author :
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */
public interface IPersistentMasterDegreeThesisDataVersion extends IPersistentObject {
	public abstract IMasterDegreeThesisDataVersion readActiveByMasterDegreeThesis(IMasterDegreeThesis masterDegreeThesis) throws ExcepcaoPersistencia;
	public IMasterDegreeThesisDataVersion readActiveByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;
}