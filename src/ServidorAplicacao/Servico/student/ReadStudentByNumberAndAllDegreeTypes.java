package ServidorAplicacao.Servico.student;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentWithInfoPerson;
import Dominio.IStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * 
 * @author Fernanda Quit�rio 1/Mar/2004
 *  
 */
public class ReadStudentByNumberAndAllDegreeTypes implements IService {

    public ReadStudentByNumberAndAllDegreeTypes() {
    }

    public Object run(Integer number) throws FenixServiceException {

        InfoStudent infoStudent = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IStudent student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
                    TipoCurso.LICENCIATURA_OBJ);

            if (student == null) {
                student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
                        TipoCurso.MESTRADO_OBJ);
            }
            if (student != null) {

                //CLONER
                infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
                //infoStudent = Cloner.copyIStudent2InfoStudent(student);
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            throw new FenixServiceException(ex);
        }

        return infoStudent;
    }

}