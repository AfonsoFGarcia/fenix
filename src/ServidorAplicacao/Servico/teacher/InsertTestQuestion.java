/*
 * Created on 29/Jul/2003
 *
 */

package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoQuestion;
import DataBeans.util.Cloner;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Metadata;
import Dominio.Test;
import Dominio.TestQuestion;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class InsertTestQuestion implements IService
{
	private String path = new String();

	public InsertTestQuestion()
	{
	}

	public boolean run(Integer executionCourseId, Integer testId, String[] metadataId,
			Integer questionOrder, Integer questionValue, String path) throws FenixServiceException
	{
		this.path = path.replace('\\', '/');
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
			for (int i = 0; i < metadataId.length; i++)
			{
				IMetadata metadata = new Metadata(new Integer(metadataId[i]));
				metadata = (IMetadata) persistentMetadata.readByOId(metadata, false);
				if (metadata == null)
				{
					throw new InvalidArgumentsServiceException();
				}
				IQuestion question = null;
				if (metadata.getVisibleQuestions() != null && metadata.getVisibleQuestions().size() != 0)
				{
					question = (IQuestion) metadata.getVisibleQuestions().get(0);
				} else
				{
					throw new InvalidArgumentsServiceException();
				}
				if (question == null)
				{
					throw new InvalidArgumentsServiceException();
				}
				IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
				ITest test = new Test(testId);
				test = (ITest) persistentTest.readByOId(test, false);
				if (test == null)
				{
					throw new InvalidArgumentsServiceException();
				}
				IPersistentTestQuestion persistentTestQuestion = persistentSuport
						.getIPersistentTestQuestion();
				List testQuestionList = persistentTestQuestion.readByTest(test);
				if (testQuestionList != null)
				{
					if (questionOrder == null || questionOrder.equals(new Integer(-1)))
					{
						questionOrder = new Integer(testQuestionList.size() + 1);
					} else
						questionOrder = new Integer(questionOrder.intValue() + 1);
					Iterator it = testQuestionList.iterator();
					while (it.hasNext())
					{
						ITestQuestion iterTestQuestion = (ITestQuestion) it.next();
						Integer iterQuestionOrder = iterTestQuestion.getTestQuestionOrder();
						if (questionOrder.compareTo(iterQuestionOrder) <= 0)
						{
							persistentTestQuestion.simpleLockWrite(iterTestQuestion);
							iterTestQuestion.setTestQuestionOrder(new Integer(iterQuestionOrder
									.intValue() + 1));
						}
					}
				}
				if (questionValue == null)
				{
					ParseQuestion parseQuestion = new ParseQuestion();
					try
					{
						InfoQuestion infoQuestion = Cloner.copyIQuestion2InfoQuestion(question);
						infoQuestion = parseQuestion.parseQuestion(infoQuestion.getXmlFile(),
								infoQuestion, this.path);
						questionValue = infoQuestion.getQuestionValue();
					} catch (Exception e)
					{
						throw new FenixServiceException(e);
					}
					if (questionValue == null)
						questionValue = new Integer(0);
				}
				ITestQuestion testQuestion = new TestQuestion();
				persistentTestQuestion.simpleLockWrite(testQuestion);
				persistentTest.simpleLockWrite(test);
				test.setNumberOfQuestions(new Integer(test.getNumberOfQuestions().intValue() + 1));
				test.setLastModifiedDate(Calendar.getInstance().getTime());
				testQuestion.setQuestion(question);
				testQuestion.setTest(test);
				testQuestion.setTestQuestionOrder(questionOrder);
				testQuestion.setTestQuestionValue(questionValue);
			}
			return true;
		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
	}
}
