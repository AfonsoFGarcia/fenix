/*
 * Created on Sep 8, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.operator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.util.RandomStringGenerator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class GenerateNewStudentsPasswordsService implements IService {

    public GenerateNewStudentsPasswordsService() {
    }

    public List run(Integer fromNumber, Integer toNumber) throws ExistingPersistentException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = null;

        List studentsList = null;
        List infoPersonList = new ArrayList();
        IPersistentStudent persistentStudent = null;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentStudent = sp.getIPersistentStudent();
        studentsList = persistentStudent.readAllBetweenNumbers(fromNumber, toNumber);
        Set<IStudent> studentsUniqueList = new HashSet(studentsList);

        for (IStudent student : studentsUniqueList) {

            IPerson person = student.getPerson();
            boolean isFirstTimeStudent = CollectionUtils.exists(person.getPersonRoles(),
                    new Predicate() {

                        public boolean evaluate(Object arg0) {
                            IRole role = (IRole) arg0;
                            return role.getRoleType().equals(RoleType.FIRST_TIME_STUDENT);
                        }
                    });

            if (!isFirstTimeStudent) {
                String password = RandomStringGenerator.getRandomStringGenerator(8);

                InfoPerson infoPerson = InfoPerson.newInfoFromDomain(person);
                infoPerson.setPassword(password);
                infoPersonList.add(infoPerson);

                person.setPassword(PasswordEncryptor.encryptPassword(password));
            }
        }

        return infoPersonList;

    }

}