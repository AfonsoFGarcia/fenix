/*
 * Created on 17/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

/**
 * @author asnr and scpo
 *  
 */

public class PrepareEditStudentGroupMembers implements IServico {

    private static PrepareEditStudentGroupMembers service = new PrepareEditStudentGroupMembers();

    /**
     * The singleton access method of this class.
     */
    public static PrepareEditStudentGroupMembers getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private PrepareEditStudentGroupMembers() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "PrepareEditStudentGroupMembers";
    }

    /**
     * Executes the service.
     */

    public List run(Integer executionCourseCode, Integer studentGroupCode)
            throws FenixServiceException {

        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
        List frequentas = new ArrayList();
        List infoStudentList = new ArrayList();

        try {

            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentStudentGroupAttend = ps
                    .getIPersistentStudentGroupAttend();

            IStudentGroup studentGroup = (IStudentGroup) ps
                    .getIPersistentStudentGroup().readByOID(StudentGroup.class,
                            studentGroupCode);

            frequentas.addAll(studentGroup.getAttendsSet().getAttends());

            List allStudentsGroups = studentGroup.getAttendsSet().getStudentGroups();

            List allStudentGroupAttend = null;

            Iterator iterator = allStudentsGroups.iterator();
            while (iterator.hasNext()) {

                allStudentGroupAttend = persistentStudentGroupAttend
                        .readAllByStudentGroup((IStudentGroup) iterator.next());

                Iterator iterator2 = allStudentGroupAttend.iterator();
                IAttends frequenta = null;
                while (iterator2.hasNext()) {

                    frequenta = ((IStudentGroupAttend) iterator2.next())
                            .getAttend();
                    if (frequentas.contains(frequenta)) {
                        frequentas.remove(frequenta);
                    }
                }
            }

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        IStudent student = null;
        Iterator iterator3 = frequentas.iterator();

        while (iterator3.hasNext()) {

            student = ((IAttends) iterator3.next()).getAluno();
            infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));
        }
        return infoStudentList;

    }
}

