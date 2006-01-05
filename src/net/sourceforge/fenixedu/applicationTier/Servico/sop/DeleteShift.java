/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteShift implements IService {

    public Object run(InfoShift infoShift) throws FenixServiceException, ExcepcaoPersistencia {

        boolean result = false;

        if (infoShift != null) {
            deleteShift(infoShift.getIdInternal());
            result = true;
        }

        return Boolean.valueOf(result);
    }

    public static void deleteShift(final Integer shiftID) throws ExcepcaoPersistencia, FenixServiceException {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IShift shift = (IShift) sp.getITurnoPersistente().readByOID(Shift.class, shiftID);
        if (shift != null) {
            List studentShifts = shift.getStudents();
            if (studentShifts != null && studentShifts.size() > 0) {
                throw new FenixServiceException("error.deleteShift.with.students");
            }

            // if the shift has student groups associated it can't be
            // deleted
            List studentGroupShift = shift.getAssociatedStudentGroups();

            if (studentGroupShift.size() > 0) {
                throw new FenixServiceException("error.deleteShift.with.studentGroups");
            }

            // if the shift has summaries it can't be deleted
            IPersistentSummary persistentSummary = sp.getIPersistentSummary();
            List summariesShift = persistentSummary.readByShift(shift.getDisciplinaExecucao()
                    .getIdInternal(), shift.getIdInternal());
            if (summariesShift != null && summariesShift.size() > 0) {
                throw new FenixServiceException("error.deleteShift.with.summaries");
            }

            for (final List<IShiftProfessorship> shiftProfessorship = shift.getAssociatedShiftProfessorship();
                    !shiftProfessorship.isEmpty();
                    shiftProfessorship.get(0).delete());

            for (final List<ILesson> lessons = shift.getAssociatedLessons();
                    !lessons.isEmpty();
                    DeleteLessons.deleteLesson(sp, lessons.get(0)));

            for (final List<ISchoolClass> schoolClasses = shift.getAssociatedClasses();
                    !schoolClasses.isEmpty(); shift.removeAssociatedClasses(schoolClasses.get(0)));

            shift.setDisciplinaExecucao(null);
            sp.getITurnoPersistente().deleteByOID(Shift.class, shift.getIdInternal());
        }
    }

}