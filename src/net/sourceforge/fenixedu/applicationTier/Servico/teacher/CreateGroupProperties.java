/*
 * Created on 28/Jul/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupProperties;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttendInAttendsSet;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendInAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.ProposalState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 *  
 */

public class CreateGroupProperties implements IService {

    public boolean run(Integer executionCourseCode, InfoGroupProperties infoGroupProperties)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();
        IPersistentGroupProperties persistentGroupProperties = persistentSupport
                .getIPersistentGroupProperties();
        IPersistentGroupPropertiesExecutionCourse persistentGroupPropertiesExecutionCourse = persistentSupport
                .getIPersistentGroupPropertiesExecutionCourse();
        IPersistentAttendsSet persistentAttendsSet = persistentSupport.getIPersistentAttendsSet();
        IPersistentAttendInAttendsSet persistentAttendInAttendsSet = persistentSupport
                .getIPersistentAttendInAttendsSet();
        IFrequentaPersistente persistentFrequenta = persistentSupport.getIFrequentaPersistente();

        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseCode);

        // Checks if already exists a groupProperties with the same name,
        // related with the executionCourse
        if (executionCourse.getGroupPropertiesByName(infoGroupProperties.getName()) != null) {
            throw new ExistingServiceException();
        }
        List attends = new ArrayList();
        attends = persistentFrequenta.readByExecutionCourse(executionCourseCode);

        IGroupProperties newGroupProperties = DomainFactory.makeGroupProperties();
        
        newGroupProperties.setEnrolmentBeginDay(infoGroupProperties.getEnrolmentBeginDay());
        newGroupProperties.setEnrolmentEndDay(infoGroupProperties.getEnrolmentEndDay());
        newGroupProperties.setMaximumCapacity(infoGroupProperties.getMaximumCapacity());
        newGroupProperties.setMinimumCapacity(infoGroupProperties.getMinimumCapacity());
        newGroupProperties.setIdealCapacity(infoGroupProperties.getIdealCapacity());
        newGroupProperties.setGroupMaximumNumber(infoGroupProperties.getGroupMaximumNumber());
        newGroupProperties.setEnrolmentPolicy(infoGroupProperties.getEnrolmentPolicy());
        newGroupProperties.setName(infoGroupProperties.getName());
        newGroupProperties.setShiftType(infoGroupProperties.getShiftType());
        newGroupProperties.setProjectDescription(infoGroupProperties.getProjectDescription());

        IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = DomainFactory.makeGroupPropertiesExecutionCourse(newGroupProperties,
                executionCourse);
        groupPropertiesExecutionCourse.setProposalState(new ProposalState(new Integer(1)));

        IAttendsSet attendsSet = DomainFactory.makeAttendsSet();
        attendsSet.setName(executionCourse.getNome());

        IAttendInAttendsSet attendInAttendsSet;
        Iterator iterAttends = attends.iterator();
        while (iterAttends.hasNext()) {
            IAttends frequenta = (IAttends) iterAttends.next();

            attendInAttendsSet = DomainFactory.makeAttendInAttendsSet(frequenta, attendsSet);
            attendsSet.addAttendInAttendsSet(attendInAttendsSet);
            frequenta.addAttendInAttendsSet(attendInAttendsSet);
        }
        attendsSet.setGroupProperties(newGroupProperties);

        newGroupProperties.setAttendsSet(attendsSet);
        newGroupProperties.addGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);
        executionCourse.addGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);

        return true;
    }
}
