/*
 * Created on 20/Out/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.IWrittenTest;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWrittenTest;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

/**
 * @author Ana e Ricardo
 *  
 */
public class WrittenTestOJB extends PersistentObjectOJB implements IPersistentWrittenTest {
    public List readBy(Calendar day, Calendar beginning) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("day", day);
        criteria.addEqualTo("beginning", beginning);
        return queryList(WrittenTest.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + WrittenTest.class.getName();
            //oqlQuery += " order by season asc";
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public void delete(IWrittenTest writtenTest) throws ExcepcaoPersistencia {
        // TO DO apagar tb as associa��es com outras tabelas

        //		Criteria criteria = new Criteria();
        //		criteria.addEqualTo("keyWrittenTest", writtenTest.getIdInternal());
        //		List examEnrollments = queryList(ExamStudentRoom.class, criteria);
        //		if (examEnrollments != null && !examEnrollments.isEmpty()) {
        //			throw new notAuthorizedPersistentDeleteException();
        //		}
        //		else{
        //
        //			List associatedExecutionCourses =
        //				exam.getAssociatedExecutionCourses();
        //
        //			if (associatedExecutionCourses != null) {
        //				for (int i = 0; i < associatedExecutionCourses.size(); i++) {
        //					IDisciplinaExecucao executionCourse =
        //						(IDisciplinaExecucao) associatedExecutionCourses.get(i);
        //					executionCourse.getAssociatedExams().remove(exam);
        //
        //					IExamExecutionCourse examExecutionCourseToDelete =
        //						SuportePersistenteOJB
        //							.getInstance()
        //							.getIPersistentExamExecutionCourse()
        //							.readBy(
        //							exam,
        //							executionCourse);
        //
        //					SuportePersistenteOJB
        //						.getInstance()
        //						.getIPersistentExamExecutionCourse()
        //						.delete(
        //						examExecutionCourseToDelete);
        //				}
        //			}
        //
        //			exam.setAssociatedExecutionCourses(null);

        super.delete(writtenTest);
        //		}
    }

    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + WrittenTest.class.getName();
        super.deleteAll(oqlQuery);
    }

}