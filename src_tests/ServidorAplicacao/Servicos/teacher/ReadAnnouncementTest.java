package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Barbosa
 * @author Pica
 * 
 * NOTA: TODO... os pr� filtros ainda n�o verificam se um an�ncio pertence �
 * disciplina. Devido a isso alguns dos testes seguintes podem falhar.
 */
public class ReadAnnouncementTest extends AnnouncementBelongsToExecutionCourseTest {

    /**
     * @param testName
     */
    public ReadAnnouncementTest(String testName) {
        super(testName);
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    protected String getNameOfServiceToBeTested() {
        return "TeacherAdministrationSiteComponentService";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadAnnouncementDataSet.xml";
    }

    /*
     * @see ServidorAplicacao.Servicos.teacher.AnnouncementBelongsToExecutionCourseTest#getExpectedUnsuccessfullDataSetFilePath()
     */
    protected String getExpectedUnsuccessfullDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testExpectedReadAnnouncementDataSet.xml";
    }

    /*
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    /*
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getUnauthorizedUser()
     */
    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "nmsn", "pass", getApplication() };
        return args;
    }

    /*
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNonTeacherUser()
     */
    protected String[] getNotAuthenticatedUser() {
        String[] args = { "fiado", "pass", getApplication() };
        return args;
    }

    /*
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments() {
        Integer infoExecutionCourseCode = new Integer(24);
        Integer infoSiteCode = new Integer(1);
        InfoSiteCommon commonComponent = new InfoSiteCommon();
        InfoAnnouncement bodyComponent = new InfoAnnouncement();
        Integer announcementCode = new Integer(1);
        Object obj2 = null;

        Object[] args = { infoExecutionCourseCode, commonComponent, bodyComponent, infoSiteCode,
                announcementCode, obj2 };
        return args;
    }

    /*
     * @see ServidorAplicacao.Servicos.teacher.AnnouncementBelongsToExecutionCourseTest#getAnnouncementUnsuccessfullArguments()
     */
    protected Object[] getAnnouncementUnsuccessfullArguments() {
        Integer infoExecutionCourseCode = new Integer(24);
        Integer infoSiteCode = new Integer(1);
        InfoSiteCommon commonComponent = new InfoSiteCommon();
        InfoAnnouncement bodyComponent = new InfoAnnouncement();
        Integer announcementCode = new Integer(5);
        //TODO.. erro.. os pr� filtros ainda n�o verificam se o anuncio
        // pertence � disciplina
        Object obj2 = null;

        Object[] args = { infoExecutionCourseCode, commonComponent, bodyComponent, infoSiteCode,
                announcementCode, obj2 };
        return args;
    }

    /*
     * Teste de leitura com sucesso de um an�ncio.
     */
    public void testReadAnnouncementSuccessfull() {
        try {
            //Argumentos do servi�o
            Integer infoExecutionCourseCode = new Integer(24);
            Integer infoSiteCode = new Integer(1);
            InfoSiteCommon commonComponent = new InfoSiteCommon();
            InfoAnnouncement bodyComponent = new InfoAnnouncement();
            Integer announcementCode = new Integer(1);
            Object obj2 = null;
            Object[] argserv = { infoExecutionCourseCode, commonComponent, bodyComponent, infoSiteCode,
                    announcementCode, obj2 };

            //Utilizador V�lido
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);

            //Execu��o do servi�o
            SiteView siteView = null;
            siteView = (SiteView) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), argserv);

            //Leu alguma coisa?
            if (siteView == null) {
                fail("Reading an Announcement for a Site.");
            }

            //Anuncio lido pelo servi�o
            InfoAnnouncement info = (InfoAnnouncement) siteView.getComponent();

            //Verificar se o que foi lido pelo servi�o est� correcto
            try {
                //Anuncio lido

                IAnnouncement iAnnouncement = null;

                //Ler o an�ncio da base de dados.
                ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
                sp.iniciarTransaccao();
                iAnnouncement = (IAnnouncement) sp.getIPersistentAnnouncement().readByOID(
                        Announcement.class, announcementCode);
                sp.confirmarTransaccao();

                //Se o an�ncio n�o existir..?!?!?!
                if (iAnnouncement == null) {
                    fail("Reading an Announcement for a Site.");
                }

                //Verificar se o an�ncio esta correcto
                assertEquals(iAnnouncement.getTitle(), info.getTitle());
                assertEquals(iAnnouncement.getInformation(), info.getInformation());
                assertEquals(info.getLastModifiedDate(), iAnnouncement.getLastModifiedDate());
                assertEquals(info.getIdInternal(), iAnnouncement.getIdInternal());
                assertEquals(iAnnouncement.getCreationDate(), info.getCreationDate());

            } catch (ExcepcaoPersistencia e) {
                fail("Reading an Announcement for a Site " + e);
            }

            //Verificar se a base de dados foi alterada
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedUnsuccessfullDataSetFilePath());

            System.out.println("ReadAnnouncementTest was SUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());

        } catch (FenixServiceException e) {
            fail("Reading an Announcement for a Site " + e);
        } catch (Exception e) {
            fail("Reading an Announcement for a Site " + e);
        }
    }

    /*
     * Leitura de um an�ncio que n�o existe
     */
    public void testReadAnnouncementUnsuccessfull() {
        try {
            //Argumentos inv�lidos.. an�ncio inexistente
            Integer infoExecutionCourseCode = new Integer(24);
            Integer infoSiteCode = new Integer(1);
            InfoSiteCommon commonComponent = new InfoSiteCommon();
            InfoAnnouncement bodyComponent = new InfoAnnouncement();
            Integer announcementCode = new Integer(121312);
            Object obj2 = null;
            Object[] argserv = { infoExecutionCourseCode, commonComponent, bodyComponent, infoSiteCode,
                    announcementCode, obj2 };

            //Utilizador v�lido
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);

            //Execu��o do servi�o
            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), argserv);

            fail("Reading an Announcement for a Site ");
        } catch (NotAuthorizedException e) {
            /*
             * Levantada a excep��o pelos pr�-flitros. O servi�o n�o chega a ser
             * chamado.
             */
            //Verificar se a base de dados foi alterada
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedUnsuccessfullDataSetFilePath());
            System.out.println("ReadAnnouncementTest was SUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading an Announcement for a Site " + e);
        } catch (Exception e) {
            fail("Reading an Announcement for a Site " + e);
        }
    }
}