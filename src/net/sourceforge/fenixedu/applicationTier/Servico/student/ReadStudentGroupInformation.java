/*
 * Created on 26/Ago/2003
 *

 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroupWithAttendsAndGroupingAndShift;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 * 
 */
public class ReadStudentGroupInformation implements IService {

    public ISiteComponent run(Integer studentGroupCode) throws FenixServiceException,
            ExcepcaoPersistencia {

        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
        StudentGroup studentGroup = null;
        Grouping grouping = null;
        List groupAttendsList = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        studentGroup = (StudentGroup) sp.getIPersistentStudentGroup().readByOID(StudentGroup.class,
                studentGroupCode);

        if (studentGroup == null) {
            return null;
        }

        List studentGroupInformationList = new ArrayList();
        grouping = studentGroup.getGrouping();
        groupAttendsList = studentGroup.getAttends();

        Iterator iter = groupAttendsList.iterator();
        InfoSiteStudentInformation infoSiteStudentInformation = null;
        Attends attend = null;

        while (iter.hasNext()) {
            infoSiteStudentInformation = new InfoSiteStudentInformation();

            attend = (Attends) iter.next();

            infoSiteStudentInformation.setNumber(attend.getAluno().getNumber());

            infoSiteStudentInformation.setName(attend.getAluno().getPerson().getNome());

            infoSiteStudentInformation.setEmail(attend.getAluno().getPerson().getEmail());

            infoSiteStudentInformation.setUsername(attend.getAluno().getPerson().getUsername());

            studentGroupInformationList.add(infoSiteStudentInformation);

        }

        Collections.sort(studentGroupInformationList, new BeanComparator("number"));
        infoSiteStudentGroup.setInfoSiteStudentInformationList(studentGroupInformationList);
        infoSiteStudentGroup.setInfoStudentGroup(InfoStudentGroupWithAttendsAndGroupingAndShift.newInfoFromDomain(studentGroup));

        if (grouping.getMaximumCapacity() != null) {

            int vagas = grouping.getMaximumCapacity().intValue() - groupAttendsList.size();

            infoSiteStudentGroup.setNrOfElements(new Integer(vagas));
        } else
            infoSiteStudentGroup.setNrOfElements("Sem limite");

        return infoSiteStudentGroup;
    }
}