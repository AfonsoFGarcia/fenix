/*
 * Created on 8/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoLessons;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentAndGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */
public class ReadStudentsAndGroupsByShiftID implements IService {

    public InfoSiteStudentsAndGroups run(Integer groupPropertiesId, Integer shiftId)
            throws FenixServiceException, ExcepcaoPersistencia {
        InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentGrouping persistentGroupProperties = sp.getIPersistentGrouping();
        ITurnoPersistente persistentShift = sp.getITurnoPersistente();

        Grouping groupProperties = (Grouping) persistentGroupProperties.readByOID(Grouping.class,
                groupPropertiesId);
        infoSiteStudentsAndGroups.setInfoGrouping(InfoGrouping.newInfoFromDomain(groupProperties));
        Shift shift = (Shift) persistentShift.readByOID(Shift.class, shiftId);

        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        infoSiteStudentsAndGroups.setInfoShift(InfoShiftWithInfoLessons.newInfoFromDomain(shift));
        List infoSiteStudentsAndGroupsList = new ArrayList();
        List studentGroups = getStudentGroupsByShiftAndGroupProperties(groupProperties, shift);
        Iterator iterStudentGroups = studentGroups.iterator();
        while (iterStudentGroups.hasNext()) {

            List studentGroupAttendList = new ArrayList();
            StudentGroup studentGroup = (StudentGroup) iterStudentGroups.next();

            studentGroupAttendList = studentGroup.getAttends();

            Iterator iterStudentGroupAttendList = studentGroupAttendList.iterator();
            InfoSiteStudentInformation infoSiteStudentInformation = null;
            InfoSiteStudentAndGroup infoSiteStudentAndGroup = null;
            Attends attend = null;

            while (iterStudentGroupAttendList.hasNext()) {
                infoSiteStudentInformation = new InfoSiteStudentInformation();
                infoSiteStudentAndGroup = new InfoSiteStudentAndGroup();

                attend = (Attends) iterStudentGroupAttendList.next();

                infoSiteStudentAndGroup.setInfoStudentGroup(InfoStudentGroup
                        .newInfoFromDomain(studentGroup));

                infoSiteStudentInformation.setNumber(attend.getAluno().getNumber());

                infoSiteStudentInformation.setName(attend.getAluno().getPerson().getNome());

                infoSiteStudentInformation.setUsername(attend.getAluno().getPerson().getUsername());

                infoSiteStudentInformation.setEmail(attend.getAluno().getPerson().getEmail());

                infoSiteStudentAndGroup.setInfoSiteStudentInformation(infoSiteStudentInformation);

                infoSiteStudentsAndGroupsList.add(infoSiteStudentAndGroup);
            }
        }

        Collections.sort(infoSiteStudentsAndGroupsList, new BeanComparator(
                "infoSiteStudentInformation.number"));

        infoSiteStudentsAndGroups.setInfoSiteStudentsAndGroupsList(infoSiteStudentsAndGroupsList);
        return infoSiteStudentsAndGroups;
    }

    private List getStudentGroupsByShiftAndGroupProperties(Grouping groupProperties, Shift shift) {
        List result = new ArrayList();
        List studentGroups = groupProperties.getStudentGroupsWithShift();
        Iterator iter = studentGroups.iterator();
        while (iter.hasNext()) {
            StudentGroup sg = (StudentGroup) iter.next();
            if (sg.getShift().equals(shift)) {
                result.add(sg);
            }
        }
        return result;
    }
}
