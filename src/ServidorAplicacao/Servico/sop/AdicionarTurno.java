/*
 * AdicionarTurno.java Created on 27 de Outubro de 2002, 12:31
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o AdicionarTurno.
 * 
 * @author tfc130
 */
import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoClass;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IExecutionDegree;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ISchoolClass;
import Dominio.ISchoolClassShift;
import Dominio.IShift;
import Dominio.SchoolClassShift;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class AdicionarTurno implements IService {

    /**
     * The actor of this class.
     */
    public AdicionarTurno() {
    }

    public Boolean run(InfoClass infoClass, InfoShift infoShift) throws FenixServiceException {

        ISchoolClassShift turmaTurno = null;
        boolean result = false;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoClass
                    .getInfoExecutionPeriod());
            IExecutionDegree executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(infoClass
                    .getInfoExecutionDegree());
            IExecutionCourse executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoShift
                    .getInfoDisciplinaExecucao());

            ISchoolClass group = sp.getITurmaPersistente().readByNameAndExecutionDegreeAndExecutionPeriod(
                    infoClass.getNome(), executionDegree, executionPeriod);
            IShift shift = sp.getITurnoPersistente().readByNameAndExecutionCourse(infoShift.getNome(),
                    executionCourse);

            turmaTurno = new SchoolClassShift(group, shift);

            try {
                sp.getITurmaTurnoPersistente().simpleLockWrite(turmaTurno);
            } catch (ExistingPersistentException e) {
                throw new ExistingServiceException(e);
            }

            result = true;
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex.getMessage());
        }

        return new Boolean(result);
    }

}