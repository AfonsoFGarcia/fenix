/*
 * Created on 7/Out/2004
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */

public class CreateStudentGroupWithoutShift implements IService {

    private IPersistentStudentGroup persistentStudentGroup = null;

    private void checkIfStudentGroupExists(Integer groupNumber, IGroupProperties groupProperties)
            throws FenixServiceException, ExcepcaoPersistencia {

        IStudentGroup studentGroup = null;

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();

        studentGroup = persistentStudentGroup.readStudentGroupByAttendsSetAndGroupNumber(groupProperties
                .getAttendsSet().getIdInternal(), groupNumber);

        if (studentGroup != null)
            throw new ExistingServiceException();
    }

    public boolean run(Integer executionCourseCode, Integer groupNumber, Integer groupPropertiesCode,
            List studentCodes) throws FenixServiceException, ExcepcaoPersistencia {

        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
        IPersistentGroupProperties persistentGroupProperites = null;
        IPersistentStudent persistentStudent = null;

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        persistentGroupProperites = persistentSupport.getIPersistentGroupProperties();
        IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperites.readByOID(
                GroupProperties.class, groupPropertiesCode);

        checkIfStudentGroupExists(groupNumber, groupProperties);

        persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
        IAttendsSet attendsSet = groupProperties.getAttendsSet();
        IStudentGroup newStudentGroup = DomainFactory.makeStudentGroup(groupNumber, attendsSet);
        attendsSet.addStudentGroup(newStudentGroup);
        persistentStudent = persistentSupport.getIPersistentStudent();
        persistentStudentGroupAttend = persistentSupport.getIPersistentStudentGroupAttend();

        List allStudentGroup = groupProperties.getAttendsSet().getStudentGroups();
        Iterator iterGroups = allStudentGroup.iterator();

        while (iterGroups.hasNext()) {
            IStudentGroup existingStudentGroup = (IStudentGroup) iterGroups.next();
            IStudentGroupAttend newStudentGroupAttend = null;
            Iterator iterator = studentCodes.iterator();

            while (iterator.hasNext()) {
                IStudent student = persistentStudent.readByUsername((String) iterator.next());

                Iterator iterAttends = groupProperties.getAttendsSet().getAttends().iterator();
                boolean found = false;
                IAttends attend = null;
                while (iterAttends.hasNext() && !found) {
                    attend = (IAttends) iterAttends.next();
                    if (attend.getAluno().equals(student)) {
                        found = true;
                    } else {
                        attend = null;
                    }
                }

                if (attend == null) {
                    throw new InvalidArgumentsServiceException();
                }

                newStudentGroupAttend = persistentStudentGroupAttend.readByStudentGroupAndAttend(
                        existingStudentGroup.getIdInternal(), attend.getIdInternal());

                if (newStudentGroupAttend != null)
                    throw new InvalidSituationServiceException();

            }
        }

        Iterator iter = studentCodes.iterator();

        while (iter.hasNext()) {

            IStudent student = persistentStudent.readByUsername((String) iter.next());

            Iterator iterAttends = groupProperties.getAttendsSet().getAttends().iterator();
            boolean found = false;
            IAttends attend = null;
            while (iterAttends.hasNext() && !found) {
                attend = (IAttends) iterAttends.next();
                if (attend.getAluno().equals(student)) {
                    found = true;
                } else {
                    attend = null;
                }
            }

            if (attend == null) {
                throw new InvalidArgumentsServiceException();
            }

            IStudentGroupAttend notExistingSGAttend = DomainFactory.makeStudentGroupAttend(newStudentGroup, attend);
        }
        return true;

    }

}
