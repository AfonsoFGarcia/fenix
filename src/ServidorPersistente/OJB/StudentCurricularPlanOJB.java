/*
 * StudentCurricularPlanOJB.java
 *
 * Created on 21 of December of 2002, 17:01
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

import java.util.List;

import org.odmg.QueryException;

import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import Util.StudentCurricularPlanState;

public class StudentCurricularPlanOJB extends ObjectFenixOJB implements IStudentCurricularPlanPersistente {
    
    /** Creates a new instance of StudentCurricularPlanOJB */
    public StudentCurricularPlanOJB() {
    }
    
    public IStudentCurricularPlan readActiveStudentCurricularPlan(int studentNumber /*, StudentType studentType */) throws ExcepcaoPersistencia {
        try {
            IStudentCurricularPlan studentCurricularPlan = null;
            String oqlQuery = "select all from " + StudentCurricularPlan.class.getName();
            oqlQuery += " where student.number = " + studentNumber;
            oqlQuery += " and CURRENT_STATE = " + StudentCurricularPlanState.ACTIVE;

// ACRESCENTAR AQUI A VERIFICACAO DO TIPO DE ALUNO

            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0) 
                studentCurricularPlan = (IStudentCurricularPlan) result.get(0);
            return studentCurricularPlan;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public void lockWrite(IStudentCurricularPlan curricularPlan) throws ExcepcaoPersistencia {
        super.lockWrite(curricularPlan);
    }
    
    public void delete(IStudentCurricularPlan curricularPlan) throws ExcepcaoPersistencia {
        super.delete(curricularPlan);
    }
    
     public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + StudentCurricularPlan.class.getName();
        super.deleteAll(oqlQuery);
   }

    public List readAllFromStudent(int studentNumber /*, StudentType studentType */) throws ExcepcaoPersistencia {
        try {
           String oqlQuery = "select all from " + StudentCurricularPlan.class.getName();
            oqlQuery += " where student.number = " + studentNumber;

// ACRESCENTAR AQUI A VERIFICACAO DO TIPO DE ALUNO
            
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }   

}
