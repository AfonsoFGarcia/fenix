/*
 * Created on 3/Set/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoStudentTestQuestion;
import DataBeans.util.Cloner;

import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestQuestionImage implements IServico
{
    private static ReadStudentTestQuestionImage service = new ReadStudentTestQuestionImage();
    private String path = new String();

    public static ReadStudentTestQuestionImage getService()
    {
        return service;
    }

    public String getNome()
    {
        return "ReadStudentTestQuestionImage";
    }

    public String run(
        String userName,
        Integer distributedTestId,
        Integer questionId,
        Integer imageId,
        String path)
        throws FenixServiceException
    {
        this.path = path.replace('\\', '/');
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IStudent student = persistentSuport.getIPersistentStudent().readByUsername(userName);
            if (student == null)
                throw new FenixServiceException();

            IDistributedTest distributedTest = new DistributedTest(distributedTestId);
            distributedTest =
                (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOId(
                    distributedTest,
                    false);
            if (distributedTest == null)
                throw new FenixServiceException();

            List studentTestQuestionList =
                persistentSuport.getIPersistentStudentTestQuestion().readByStudentAndDistributedTest(
                    student,
                    distributedTest);

            Iterator it = studentTestQuestionList.iterator();
            while (it.hasNext())
            {
                IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
                if (studentTestQuestion.getKeyQuestion().equals(questionId))
                {
                    ParseQuestion parse = new ParseQuestion();
                    InfoStudentTestQuestion infoStudentTestQuestion =
                        Cloner.copyIStudentTestQuestion2InfoStudentTestQuestion(studentTestQuestion);
                    try
                    {
                        infoStudentTestQuestion.setQuestion(
                            parse.parseQuestion(
                                infoStudentTestQuestion.getQuestion().getXmlFile(),
                                infoStudentTestQuestion.getQuestion(),
                                this.path));
                        infoStudentTestQuestion = parse.getOptionsShuffle(infoStudentTestQuestion, this.path);
                    } catch (Exception e)
                    {
                        throw new FenixServiceException(e);
                    }
                    Iterator optionit = infoStudentTestQuestion.getQuestion().getOptions().iterator();
                    int imgIndex = 0;
                    while (optionit.hasNext())
                    {
                        LabelValueBean lvb = (LabelValueBean) optionit.next();
                        if (lvb.getLabel().startsWith("image/"))
                        {
                            imgIndex++;
                            if (imgIndex == imageId.intValue())
                                return lvb.getValue();
                        }
                    }
                }

            }

        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        return null;
    }

}
