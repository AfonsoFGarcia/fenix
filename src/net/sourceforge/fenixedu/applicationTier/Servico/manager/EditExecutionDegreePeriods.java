package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditExecutionDegreePeriods implements IService {

    public void run(InfoExecutionDegree infoExecutionDegree) throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final ExecutionDegree oldExecutionDegree = (ExecutionDegree) persistentSuport
                .getIPersistentObject().readByOID(ExecutionDegree.class,
                        infoExecutionDegree.getIdInternal(), false);

        OccupationPeriod periodLessonsFirstSemester = setCompositePeriod(infoExecutionDegree
                .getInfoPeriodLessonsFirstSemester());
        oldExecutionDegree.setPeriodLessonsFirstSemester(periodLessonsFirstSemester);

        OccupationPeriod periodLessonsSecondSemester = setCompositePeriod(infoExecutionDegree
                .getInfoPeriodLessonsSecondSemester());
        oldExecutionDegree.setPeriodLessonsSecondSemester(periodLessonsSecondSemester);

        OccupationPeriod periodExamsFirstSemester = setCompositePeriod(infoExecutionDegree
                .getInfoPeriodExamsFirstSemester());
        oldExecutionDegree.setPeriodExamsFirstSemester(periodExamsFirstSemester);

        OccupationPeriod periodExamsSecondSemester = setCompositePeriod(infoExecutionDegree
                .getInfoPeriodExamsSecondSemester());
        oldExecutionDegree.setPeriodExamsSecondSemester(periodExamsSecondSemester);
    }

    // retorna o primeiro period do executiondegree
    private OccupationPeriod setCompositePeriod(InfoPeriod infoPeriod) throws ExcepcaoPersistencia {
        List<InfoPeriod> infoPeriodList = new ArrayList<InfoPeriod>();

        infoPeriodList.add(infoPeriod);
        while (infoPeriod.getNextPeriod() != null) {
            infoPeriodList.add(infoPeriod.getNextPeriod());
            infoPeriod = infoPeriod.getNextPeriod();
        }

        int infoPeriodListSize = infoPeriodList.size();
        InfoPeriod infoPeriodNew = infoPeriodList.get(infoPeriodListSize - 1);

        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentPeriod periodDAO = persistentSuport.getIPersistentPeriod();
        OccupationPeriod period = (OccupationPeriod) periodDAO.readByCalendarAndNextPeriod(
                infoPeriodNew.getStartDate().getTime(), infoPeriodNew.getEndDate().getTime(), null);

        if (period == null) {
            Calendar startDate = infoPeriodNew.getStartDate();
            Calendar endDate = infoPeriodNew.getEndDate();
            period = DomainFactory.makeOccupationPeriod(startDate.getTime(), endDate.getTime());
        }

        for (int i = infoPeriodListSize - 2; i >= 0; i--) {
            Integer keyNextPeriod = period.getIdInternal();

            OccupationPeriod nextPeriod = period;

            infoPeriodNew = infoPeriodList.get(i);

            period = (OccupationPeriod) periodDAO.readByCalendarAndNextPeriod(infoPeriodNew
                    .getStartDate().getTime(), infoPeriodNew.getEndDate().getTime(), keyNextPeriod);

            if (period == null) {
                Calendar startDate = infoPeriodNew.getStartDate();
                Calendar endDate = infoPeriodNew.getEndDate();
                period = DomainFactory.makeOccupationPeriod(startDate.getTime(), endDate.getTime());
                period.setNextPeriod(nextPeriod);
            }
        }

        return period;
    }

}
