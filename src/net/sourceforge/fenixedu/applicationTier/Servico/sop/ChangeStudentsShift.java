package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.SendMail;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;

public class ChangeStudentsShift extends Service {

    public void run(IUserView userView, Integer oldShiftId, Integer newShiftId)
            throws ExcepcaoPersistencia, FenixServiceException {
        IPessoaPersistente persistentPerson = persistentSupport.getIPessoaPersistente();

        Shift oldShift = (Shift) persistentObject.readByOID(Shift.class, oldShiftId);
        Shift newShift = (Shift) persistentObject.readByOID(Shift.class, newShiftId);

        if (oldShift == null || newShift == null || !oldShift.getTipo().equals(newShift.getTipo())
                || !oldShift.getDisciplinaExecucao().getIdInternal().equals(newShift.getDisciplinaExecucao().getIdInternal())) {
            throw new UnableToTransferStudentsException();
        }

        SendMail sendMail = new SendMail();
        List<String> emptyList = new ArrayList<String>();
        List<String> toMails = new ArrayList<String>();

        final List<Student> oldStudents = oldShift.getStudents();
        final List<Student> newStudents = newShift.getStudents();
        while (!oldStudents.isEmpty()) {
            final Student student = oldStudents.get(0);
            if (!newStudents.contains(student)) {
                newStudents.add(student);

                Person person = student.getPerson();
                if (person.getEmail() != null && person.getEmail().length() > 0) {
                    toMails.add(person.getEmail());
                }
            } else {
                oldStudents.remove(student);
            }
        }

            
        Person person = Person.readPersonByUsername(userView.getUtilizador());
        sendMail.run(emptyList, emptyList, toMails, person.getNome(), person.getEmail(), 
                "Altera��o de turnos",
                "Devido a altera��es nos hor�rios, a sua reserva no turno "
                + oldShift.getNome() 
                + " foi transferida para o turno "
                + newShift.getNome());
    }

    public class UnableToTransferStudentsException extends FenixServiceException {
    }

}