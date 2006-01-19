/*
 * CriarTurno.java Created on 27 de Outubro de 2002, 18:48
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o CriarTurno
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho
 */
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoExecutionCourse;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class CriarTurno extends Service {

    public InfoShift run(InfoShift infoTurno) throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

        final Shift existingShift = shiftDAO.readByNameAndExecutionCourse(infoTurno.getNome(),
                infoTurno.getInfoDisciplinaExecucao().getIdInternal());

        if (existingShift != null) {
            throw new ExistingServiceException("Duplicate Entry: " + infoTurno.getNome());
        }

        Shift newShift = DomainFactory.makeShift();
        Integer availabilityFinal = new Integer(new Double(Math.ceil(1.10 * infoTurno.getLotacao()
                .doubleValue())).intValue());
        newShift.setAvailabilityFinal(availabilityFinal);
        ExecutionCourse executionCourse = (ExecutionCourse) sp.getIPersistentExecutionCourse()
                .readByOID(ExecutionCourse.class, infoTurno.getInfoDisciplinaExecucao().getIdInternal());
        newShift.setDisciplinaExecucao(executionCourse);
        newShift.setNome(infoTurno.getNome());
        newShift.setLotacao(infoTurno.getLotacao());
        newShift.setTipo(infoTurno.getTipo());

        InfoShift infoShift = InfoShiftWithInfoExecutionCourse.newInfoFromDomain(newShift);

        return infoShift;

    }

}
