package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateWeeklyWorkLoad extends Service {

    public void run(final Integer attendsID, final Integer contact, final Integer autonomousStudy, final Integer other)
            throws ExcepcaoPersistencia {
        final Attends attends = (Attends) persistentObject.readByOID(Attends.class, attendsID);
        attends.createWeeklyWorkLoad(contact, autonomousStudy, other);
    }

}