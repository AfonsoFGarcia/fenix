/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTest;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Susana Fernandes
 */
public class ReadTests extends Service {

    public List<InfoTest> run(Integer executionCourseId) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List<Test> tests = persistentSupport.getIPersistentTest().readByTestScope(ExecutionCourse.class.getName(), executionCourseId);
        List<InfoTest> infoTestList = new ArrayList<InfoTest>();
        for (Test test : tests) {
            infoTestList.add(InfoTest.newInfoFromDomain(test));
        }
        return infoTestList;
    }
}