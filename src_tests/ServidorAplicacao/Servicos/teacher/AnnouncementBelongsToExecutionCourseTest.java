package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Barbosa
 * @author Pica Created on 8/Out/2003
 */

public abstract class AnnouncementBelongsToExecutionCourseTest extends
        ServiceNeedsAuthenticationTestCase {

    protected AnnouncementBelongsToExecutionCourseTest(String name) {
        super(name);
    }

    public void testAnnouncementNotBelongsToExecutionCourse() {
        Object[] serviceArguments = getAnnouncementUnsuccessfullArguments();
        IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    serviceArguments);
            fail("Fail AnnouncementBelongsToExecutionCourseTestUnsuccessful"
                    + getNameOfServiceToBeTested());
        } catch (NotAuthorizedException ex) {
            /*
             * O an�ncio existe mas n�o pertence � disciplina. Os pr�-filtros
             * lan�am uma excepcao NotAuthorizedException, o servi�o n�o chega a
             * ser invocado
             */
            //Comparacao do dataset
            compareDataSetUsingExceptedDataSetTablesAndColumns(getExpectedUnsuccessfullDataSetFilePath());
            System.out
                    .println("AnnouncementBelongsToExecutionCourseTestUnsuccessful was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());

        } catch (Exception ex) {
            fail("Fail AnnouncementBelongsToExecutionCourseTestUnsuccessful"
                    + getNameOfServiceToBeTested());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase
     */
    protected abstract String[] getAuthenticatedAndAuthorizedUser();

    protected abstract String[] getAuthenticatedAndUnauthorizedUser();

    protected abstract String[] getNotAuthenticatedUser();

    protected abstract String getNameOfServiceToBeTested();

    protected abstract Object[] getAuthorizeArguments();

    protected abstract String getDataSetFilePath();

    protected abstract String getApplication();

    /*
     * An�ncio que n�o pertence � disciplina escolhida
     */
    protected abstract Object[] getAnnouncementUnsuccessfullArguments();

    /*
     * DataSet igual ao dataset carregado na base de dados (sem altera��es).
     */
    protected abstract String getExpectedUnsuccessfullDataSetFilePath();
}