
/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.IDegreeCurricularPlan;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

public class ReadStudentsFromDegreeCurricularPlan implements IServico {
    
    private static ReadStudentsFromDegreeCurricularPlan servico = new ReadStudentsFromDegreeCurricularPlan();
    
    /**
     * The singleton access method of this class.
     **/
    public static ReadStudentsFromDegreeCurricularPlan getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private ReadStudentsFromDegreeCurricularPlan() { 
    }
    
    /**
     * Returns The Service Name */
    
    public final String getNome() {
        return "ReadStudentsFromDegreeCurricularPlan";
    }
    
    
    public List run(Integer degreeCurricularPlanID, TipoCurso degreeType)
	    throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;
        
        List students = null;
         
        try {
            sp = SuportePersistenteOJB.getInstance();
            
            // Read the Students
            
            IDegreeCurricularPlan degreeCurricularPlanTemp = new DegreeCurricularPlan();
            degreeCurricularPlanTemp.setIdInternal(degreeCurricularPlanID);
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp.getIPersistentDegreeCurricularPlan().readByOId(degreeCurricularPlanTemp, false);
            
			students = sp.getIStudentCurricularPlanPersistente().readByDegreeCurricularPlan(degreeCurricularPlan);
          
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        } 

		if (students == null){
			throw new NonExistingServiceException();
		}
				
		Iterator iterator = students.iterator();
		List result = new ArrayList();
		while(iterator.hasNext()){
			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();

			result.add(Cloner.copyIStudent2InfoStudent(studentCurricularPlan.getStudent()));
		}
		
		return result;
    }
}