package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.credits;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class ManagementPositionCreditLineVO extends VersionedObjectsBase implements
        IPersistentManagementPositionCreditLine {

    public List readByTeacherAndExecutionPeriod(Integer teacherId, Date executionPeriodBeginDate,
            Date executionPeriodEndDate) throws ExcepcaoPersistencia {

        List<ManagementPositionCreditLine> mpCreditLines = (List<ManagementPositionCreditLine>) readAll(ManagementPositionCreditLine.class);
        List<ManagementPositionCreditLine> result = null;

        for (ManagementPositionCreditLine line : mpCreditLines) {
            if (line.getTeacher().getIdInternal().equals(teacherId)
                    && line.getEnd().getTime() > executionPeriodBeginDate.getTime()
                    && line.getStart().getTime() < executionPeriodEndDate.getTime()) {
                result.add(line);
            }
        }

        return result;
    }

    public List readByTeacher(Integer teacherId) throws ExcepcaoPersistencia {
        List<ManagementPositionCreditLine> mpCreditLines = (List<ManagementPositionCreditLine>) readAll(ManagementPositionCreditLine.class);
        List<ManagementPositionCreditLine> result = null;

        for (ManagementPositionCreditLine line : mpCreditLines) {
            if (line.getTeacher().getIdInternal().equals(teacherId)) {
                result.add(line);
            }
        }

        return result;
    }

}