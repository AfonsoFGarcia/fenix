/*
 * Created on 3/Out/2003 by jpvl
 *
 */
package middleware.studentMigration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Dominio.IDisciplinaExecucao;
import Util.TipoAula;

public abstract class ReportDistribution
{
	private static HashMap capacityExceed = new HashMap();
	private static List notFoundStudents = new ArrayList();
	private static HashMap capacityExceedAux = new HashMap();
	public static void addNotFoundStudent(String studentNumber)
	{
		int indexOf = notFoundStudents.indexOf(studentNumber);
		if (indexOf == -1)
		{
			notFoundStudents.add(studentNumber);
		}
	}

	public static void addCapacityExceed(IDisciplinaExecucao executionCourse, String className, TipoAula lessonType)
	{
		List errors = (List) capacityExceed.get(executionCourse);
		if (errors == null)
		{
			errors = new ArrayList();
			capacityExceed.put(executionCourse, errors);
		}
		String message = "class=" + className + ";lessonType=" + lessonType;
		Integer times = (Integer) capacityExceedAux.get(message);
		if (times == null) {
			capacityExceedAux.put(message, new Integer(1));
		}else {
			capacityExceedAux.put(message, new Integer(times.intValue() + 1));
		}
		errors.add(message);
	}

	public static void printReport()
	{
		System.out.println("Report -------------------------------------------");
		System.out.println("N�mero de alunos que n�o encontrados:" + notFoundStudents.size());
		System.out.println("Disciplinas com problemas de capacidade:" + capacityExceed.keySet().size());
		System.out.println("----------------------------------------------------");
	}

	public static void fullReport()
	{
		printReport();
		Iterator iterator = capacityExceed.keySet().iterator();
		while (iterator.hasNext())
		{
			IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) iterator.next();
			System.out.println(executionCourse.getIdInternal() + "-" + executionCourse.getNome());
			List list = (List) capacityExceed.get(executionCourse);
			
			for (int i = 0; i < list.size(); i++)
			{
				Integer times = (Integer) capacityExceedAux.get(list.get(i));
				if (times != null ) {
					System.out.println("\t"+list.get(i) + " - n�mero de vezes =" + times);
				}
				capacityExceedAux.remove(list.get(i));
			}

		}
	}
}
