
/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.commons.student;

import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadStudentCurricularPlan implements IServico {
    
    private static ReadStudentCurricularPlan servico = new ReadStudentCurricularPlan();
    
    /**
     * The singleton access method of this class.
     **/
    public static ReadStudentCurricularPlan getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private ReadStudentCurricularPlan() { 
    }
    
    /**
     * Returns The Service Name */
    
    public final String getNome() {
        return "ReadStudentCurricularPlan";
    }
    
    
    public InfoStudentCurricularPlan run(Integer studentCurricularPlanID) throws ExcepcaoInexistente, FenixServiceException {
        ISuportePersistente sp = null;
        
		IStudentCurricularPlan studentCurricularPlan = null;
         
        try {
            sp = SuportePersistenteOJB.getInstance();
            
            // The student Curricular plan
            
            IStudentCurricularPlan studentCurricularPlanTemp = new StudentCurricularPlan();
            studentCurricularPlanTemp.setIdInternal(studentCurricularPlanID);

            studentCurricularPlan = (IStudentCurricularPlan) sp.getIStudentCurricularPlanPersistente().readByOId(studentCurricularPlanTemp, false);
          
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        } 

		if (studentCurricularPlan == null){
			throw new NonExistingServiceException();
		}

		return Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan);
    }
}