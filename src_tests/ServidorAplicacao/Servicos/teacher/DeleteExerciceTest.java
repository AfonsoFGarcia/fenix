/*
 * Created on 25/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import net.sourceforge.fenixedu.domain.IMetadata;
import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.Metadata;
import net.sourceforge.fenixedu.domain.Question;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class DeleteExerciceTest extends ServiceNeedsAuthenticationTestCase {

    public DeleteExerciceTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testEditDistributedTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "DeleteExercice";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {

        String[] args = { "D2543", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {

        String[] args = { "L48283", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {

        String[] args = { "L48283", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Integer executionCourseId = new Integer(34882);
        Integer metadataId = new Integer(142);
        String path = new String("e:\\eclipse\\workspace\\fenix\\build\\standalone\\");

        Object[] args = { executionCourseId, metadataId, path };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfull() {

        try {
            IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
            Object[] args = getAuthorizeArguments();
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            Criteria criteria = new Criteria();
            criteria.addEqualTo("idInternal", args[1]);
            Query queryCriteria = new QueryByCriteria(Metadata.class, criteria);
            IMetadata metadata = (IMetadata) broker.getObjectByQuery(queryCriteria);

            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            criteria = new Criteria();
            criteria.addEqualTo("keyMetadata", args[1]);
            queryCriteria = new QueryByCriteria(Question.class, criteria);
            List questionList = (List) broker.getCollectionByQuery(queryCriteria);
            broker.close();

            if (metadata != null) {

                System.out.println("metadata " + metadata.getIdInternal());
                if (metadata.getVisibility().booleanValue() != false)
                    fail("DeleteExerciceTest " + "N�o apagou, nem mudou visibilidade do metadata");
            }
            Iterator it = questionList.iterator();
            while (it.hasNext()) {
                IQuestion question = (IQuestion) it.next();
                System.out.println("Question " + question.getIdInternal());
                if (question.getVisibility().booleanValue() != false)
                    fail("DeleteExerciceTest "
                            + "N�o apagou, nem mudou visibilidade das perguntas do metadata");

            }

        } catch (FenixServiceException ex) {
            fail("DeleteExerciceTest " + ex);
        } catch (Exception ex) {
            fail("DeleteExerciceTest " + ex);
        }
    }
}