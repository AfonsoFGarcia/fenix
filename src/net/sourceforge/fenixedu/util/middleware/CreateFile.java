package net.sourceforge.fenixedu.util.middleware;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author T�nia Pous�o
 * 
 */
public class CreateFile {

    public static final int MAX_MAIL = 39;

    //��������������������������������������������������������������������������
    // �����������������������������������
    // fileWithMarksList
    //��������������������������������������������������������������������������
    // �����������������������������������
    public static List fileWithMarksList(List curricularCoursesList, List enrolmentEvaluationList) throws Exception {
	List fileList = new ArrayList();
	Iterator iterador = curricularCoursesList.listIterator();
	while (iterador.hasNext()) {
	    final CurricularCourse curricularCourse = (CurricularCourse) iterador.next();

	    if (curricularCourse.getDegreeCurricularPlan().getDegree().getDegreeType().equals(DegreeType.DEGREE)) {
		List enrolmentEvaluationCurricularCourseList = new ArrayList();

		enrolmentEvaluationCurricularCourseList = (List) CollectionUtils.select(enrolmentEvaluationList, new Predicate() {
		    public boolean evaluate(Object obj) {
			EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) obj;
			return enrolmentEvaluation.getEnrolment().getCurricularCourse().equals(curricularCourse);
		    }
		});
		File file = buildFile(enrolmentEvaluationCurricularCourseList);
		if (file != null) {
		    fileList.add(file);
		}
	    }
	}
	return fileList;
    }

    public static File buildFile(List enrolmentEvaluationList) throws Exception {

	if (enrolmentEvaluationList == null) {
	    return null;
	}
	String fileName = null;
	File file = null;
	BufferedWriter writer = null;
	try {
	    fileName = nameFileMarks((EnrolmentEvaluation) enrolmentEvaluationList.get(0));
	    file = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
	    writer = new BufferedWriter(new FileWriter(file));

	    String mail = ((EnrolmentEvaluation) enrolmentEvaluationList.get(0)).getPersonResponsibleForGrade().getEmail();

	    int length = mail.length();
	    while (length < MAX_MAIL) {
		mail = mail + " ";
		length++;
	    }

	    writer.write(mail);
	    writer.newLine();

	    Iterator iterator = enrolmentEvaluationList.listIterator();
	    while (iterator.hasNext()) {
		EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) iterator.next();

		RowMarksFile rowMarksFile = new RowMarksFile(enrolmentEvaluation);

		writer.write(rowMarksFile.toWriteInFile());
		writer.newLine();
	    }

	    writer.close();

	    return file;
	} catch (Exception exception) {
	    exception.printStackTrace();
	    throw new Exception();
	}
    }

    private static String nameFileMarks(EnrolmentEvaluation enrolmentEvaluation) {
	String fileName = new String();

	RowMarksFile rowMarksFile = new RowMarksFile(enrolmentEvaluation);

	fileName = rowMarksFile.fileName();

	return fileName;
    }
}