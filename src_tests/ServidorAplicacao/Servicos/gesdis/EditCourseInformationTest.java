/*
 * Created on 7/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.gesdis;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditCourseInformationTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param testName
     */
    public EditCourseInformationTest(String name) {
        super(name);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/gesdis/testEditCourseInformationDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "EditCourseInformation";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {

        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {

        String[] args = { "jorge", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {

        String[] args = { "jccm", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Integer courseReportId = new Integer(1);

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        infoExecutionYear.setIdInternal(new Integer(1));

        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        infoExecutionPeriod.setIdInternal(new Integer(1));
        infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
        infoExecutionCourse.setIdInternal(new Integer(24));
        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

        InfoCourseReport infoCourseReport = new InfoCourseReport();
        infoCourseReport.setIdInternal(courseReportId);
        infoCourseReport.setReport("novo relatorio da disciplina");
        infoCourseReport.setInfoExecutionCourse(infoExecutionCourse);

        Object[] args = { courseReportId, infoCourseReport };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfullWithCourseReport() {
        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    getAuthorizeArguments());

            // verifica as alteracoes da base de dados
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/gesdis/testExpectedEditCourseInformationWithCourseReportDataSet.xml");
        } catch (Exception ex) {
            fail("Editing a Course Information with a CourseReport " + ex);
        }
    }

    public void testSuccessfullWithoutCourseReport() {
        try {
            String[] args = { "maria", "pass", getApplication() };
            IUserView userView = authenticateUser(args);

            InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
            infoExecutionYear.setIdInternal(new Integer(1));

            InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
            infoExecutionPeriod.setIdInternal(new Integer(1));
            infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);

            InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
            infoExecutionCourse.setIdInternal(new Integer(25));
            infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

            InfoCourseReport infoCourseReport = new InfoCourseReport();
            infoCourseReport.setReport("relatorio da disciplina");
            infoCourseReport.setInfoExecutionCourse(infoExecutionCourse);

            Object[] serviceArgs = { null, infoCourseReport };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    serviceArgs);

            // verifica as alteracoes da base de dados
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/gesdis/testExpectedEditCourseInformationWithoutCourseReportDataSet.xml");
        } catch (Exception ex) {
            fail("Editing a Course Information without a CourseReport " + ex);
        }
    }
}