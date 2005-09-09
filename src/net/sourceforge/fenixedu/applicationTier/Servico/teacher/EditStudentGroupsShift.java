/*
 * Created on 17/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonValidChangeServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa and rmalo
 * 
 */

public class EditStudentGroupsShift implements IService {

    public Boolean run(Integer executionCourseCode, Integer groupPropertiesCode, Integer shiftCode,
            List studentGroupsCodes) throws FenixServiceException {

        IPersistentGrouping persistentGroupProperties = null;
        ITurnoPersistente persistentShift = null;
        IPersistentStudentGroup persistentStudentGroup = null;

        try {

            ISuportePersistente persistentSupport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();
            persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
            persistentGroupProperties = persistentSupport.getIPersistentGrouping();
            persistentShift = persistentSupport.getITurnoPersistente();

            IGrouping grouping = (IGrouping) persistentGroupProperties.readByOID(Grouping.class,
                    groupPropertiesCode);

            if (grouping == null) {
                throw new ExistingServiceException();
            }

            IShift shift = (IShift) persistentShift.readByOID(Shift.class, shiftCode);

            if (shift == null) {
                throw new InvalidChangeServiceException();
            }
            
            grouping.checkShiftCapacity(shift);
            
            if (grouping.getShiftType() == null
                    || !grouping.getShiftType().equals(shift.getTipo())) {
                throw new NonValidChangeServiceException();
            }

            List<IStudentGroup> studentGroups = buildStudentGroupsList(studentGroupsCodes, persistentStudentGroup);

            for(IStudentGroup studentGroup : studentGroups){
                if (!studentGroup.getGrouping().equals(grouping)) {
                    throw new InvalidArgumentsServiceException();
                } 
            }
                       
            for(IStudentGroup studentGroup : studentGroups){                                    
                studentGroup.editShift(shift);
            }

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        return Boolean.TRUE;
    }

    private List buildStudentGroupsList(List studentGroupsCodes,
            IPersistentStudentGroup persistentStudentGroup) throws ExcepcaoPersistencia,
            InvalidSituationServiceException {
        
        List studentGroups = new ArrayList();
        Iterator iterStudentGroupsCodes = studentGroupsCodes.iterator();
        
        while (iterStudentGroupsCodes.hasNext()) {
            Integer studentGroupCode = (Integer) iterStudentGroupsCodes.next();
            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOID(
                    StudentGroup.class, studentGroupCode);
            
            if (studentGroup == null) 
                throw new InvalidSituationServiceException("error.studentGroupNotInList");
        
            studentGroups.add(studentGroup);
        }
        return studentGroups;
    }
}