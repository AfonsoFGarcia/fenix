package ServidorPersistente;

import java.util.List;

import Dominio.IStudentCurricularPlan;

/**
 * @author David Santos
 * Jan 14, 2004
 */

public interface IPersistentCreditsInAnySecundaryArea extends IPersistentObject
{
	public List readAllByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;
}
