/*
 * IStudentCurricularPlan.java
 *
 * Created on 21 of December de 2002, 16:57
 */

package ServidorPersistente;

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

import java.util.List;

import Dominio.IDegreeCurricularPlan;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.Specialization;
import Util.TipoCurso;

public interface IStudentCurricularPlanPersistente extends IPersistentObject {
    
    /**
     * 
     * @param studentNumber
     * @param degreeType
     * @return
     * @throws ExcepcaoPersistencia
     */
    IStudentCurricularPlan readActiveStudentCurricularPlan(Integer studentNumber, TipoCurso degreeType ) throws ExcepcaoPersistencia;
    
    /**
     * 
     * @param studentCurricularPlan
     * @throws ExcepcaoPersistencia
     * @throws ExistingPersistentException
     */
    public void lockWrite(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia, ExistingPersistentException;
    
    /**
     * 
     * @param studentCurricularPlan
     * @throws ExcepcaoPersistencia
     */
    public void delete(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;
    
    /**
     * 
     * @throws ExcepcaoPersistencia
     */
    public void deleteAll() throws ExcepcaoPersistencia;
    
    /**
     * 
     * @param studentNumber
     * @return
     * @throws ExcepcaoPersistencia
     */
    public List readAllFromStudent(int studentNumber /*, StudentType studentType */) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param studentNumber
	 * @param degreeType
	 * @param specialization
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public IStudentCurricularPlan readActiveStudentAndSpecializationCurricularPlan(Integer studentNumber, TipoCurso degreeType, Specialization specialization ) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param degreeCurricularPlan
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param username
	 * @return List with the Student's Curricular Plans
	 * @throws ExcepcaoPersistencia
	 */
	public List readByUsername(String username) throws ExcepcaoPersistencia;
	

}
