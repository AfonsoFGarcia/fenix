package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 *  
 */
public class InsertItemTest extends SectionBelongsExecutionCourseTest {

    /**
     * @param testName
     */
    public InsertItemTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertItem";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testInsertItemDataSet.xml";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "3", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {
        String[] args = { "13", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        InfoItem infoItem = new InfoItem();
        infoItem.setInformation("informacao");
        infoItem.setName("nome");
        infoItem.setItemOrder(new Integer(0));
        infoItem.setUrgent(new Boolean(false));

        Object[] args = { new Integer(27), new Integer(6), infoItem };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    /*
     * Refers to the test in the upper class, SectionBelongExecutionCourseTest
     */
    protected Object[] getTestSectionSuccessfullArguments() {
        InfoItem infoItem = new InfoItem();
        infoItem.setInformation("informacao");
        infoItem.setName("nome");
        infoItem.setItemOrder(new Integer(0));
        infoItem.setUrgent(new Boolean(false));

        Object[] args = { new Integer(27), new Integer(6), infoItem };
        return args;
    }

    /*
     * Refers to the test in the upper class, SectionBelongExecutionCourseTest
     */
    protected Object[] getTestSectionUnsuccessfullArguments() {
        InfoItem infoItem = new InfoItem();
        infoItem.setInformation("informacao");
        infoItem.setName("nome");
        infoItem.setItemOrder(new Integer(0));
        infoItem.setUrgent(new Boolean(false));

        Object[] args = { new Integer(27), new Integer(7), infoItem };
        return args;
    }

    public void testInsertExistingItem() {
        InfoItem infoItem = new InfoItem();
        infoItem.setInformation("informacao");
        infoItem.setName("Item1dePO");
        infoItem.setItemOrder(new Integer(0));
        infoItem.setUrgent(new Boolean(false));

        Object[] args = { new Integer(27), new Integer(6), infoItem };

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            System.out.println("testInsertExistingItem was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testInsertExistingItem");

        } catch (ExistingServiceException e) {
            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedInsertExistingItemDataSet.xml");
            System.out.println("testInsertExistingItem was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testInsertExistingItem was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testInsertExistingItem");
        }
    }

    public void testInsertNonExistingItemBeforeFirst() {
        InfoItem infoItem = new InfoItem();
        infoItem.setInformation("informacao");
        infoItem.setName("novoItem");
        infoItem.setItemOrder(new Integer(0));
        infoItem.setUrgent(new Boolean(false));

        Object[] args = { new Integer(27), new Integer(6), infoItem };

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentSuport.iniciarTransaccao();

            ISection iSection = (ISection) persistentSuport.getIPersistentSection().readByOID(
                    Section.class, new Integer(6));
            IPersistentItem persistentItem = persistentSuport.getIPersistentItem();
            IItem iItem = persistentItem.readBySectionAndName(iSection, "novoItem");
            InfoItem novoInfoItem = Cloner.copyIItem2InfoItem(iItem);

            assertEquals(novoInfoItem.getInfoSection(), infoItem.getInfoSection());
            assertEquals(novoInfoItem.getItemOrder(), infoItem.getItemOrder());
            assertEquals(novoInfoItem.getInformation(), infoItem.getInformation());
            assertEquals(novoInfoItem.getName(), infoItem.getName());
            assertEquals(novoInfoItem.getUrgent(), infoItem.getUrgent());

            persistentItem.delete(iItem);
            persistentSuport.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedInsertItemBeforeFirstDataSet.xml");
            System.out.println("testInsertNonExistingItemBeforeFirst was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out
                    .println("testInsertNonExistingItemBeforeFirst was UNSUCCESSFULY runned by class: "
                            + this.getClass().getName());
            fail("testInsertNonExistingItemBeforeFirst");
        }
    }

    public void testInsertNonExistingItemInMiddle() {
        InfoItem infoItem = new InfoItem();
        infoItem.setInformation("informacao");
        infoItem.setName("novoItem");
        infoItem.setItemOrder(new Integer(1));
        infoItem.setUrgent(new Boolean(false));

        Object[] args = { new Integer(27), new Integer(6), infoItem };

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentSuport.iniciarTransaccao();

            ISection iSection = (ISection) persistentSuport.getIPersistentSection().readByOID(
                    Section.class, new Integer(6));

            IPersistentItem persistentItem = persistentSuport.getIPersistentItem();
            IItem iItem = persistentItem.readBySectionAndName(iSection, "novoItem");
            InfoItem novoInfoItem = Cloner.copyIItem2InfoItem(iItem);

            assertEquals(novoInfoItem.getInfoSection(), infoItem.getInfoSection());
            assertEquals(novoInfoItem.getItemOrder(), infoItem.getItemOrder());
            assertEquals(novoInfoItem.getInformation(), infoItem.getInformation());
            assertEquals(novoInfoItem.getName(), infoItem.getName());
            assertEquals(novoInfoItem.getUrgent(), infoItem.getUrgent());

            persistentItem.delete(iItem);
            persistentSuport.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedInsertItemInMiddleDataSet.xml");
            System.out.println("testInsertNonExistingItemInMiddle was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testInsertNonExistingItemInMiddle was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testInsertNonExistingItemInMiddle");
        }
    }

    public void testInsertNonExistingItemBeforeEnd() {
        InfoItem infoItem = new InfoItem();
        infoItem.setInformation("informacao");
        infoItem.setName("novoItem");
        infoItem.setItemOrder(new Integer(2));
        infoItem.setUrgent(new Boolean(false));

        Object[] args = { new Integer(27), new Integer(6), infoItem };

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentSuport.iniciarTransaccao();

            ISection iSection = (ISection) persistentSuport.getIPersistentSection().readByOID(
                    Section.class, new Integer(6));

            IPersistentItem persistentItem = persistentSuport.getIPersistentItem();
            IItem iItem = persistentItem.readBySectionAndName(iSection, "novoItem");
            InfoItem novoInfoItem = Cloner.copyIItem2InfoItem(iItem);

            assertEquals(novoInfoItem.getInfoSection(), infoItem.getInfoSection());
            assertEquals(novoInfoItem.getItemOrder(), infoItem.getItemOrder());
            assertEquals(novoInfoItem.getInformation(), infoItem.getInformation());
            assertEquals(novoInfoItem.getName(), infoItem.getName());
            assertEquals(novoInfoItem.getUrgent(), infoItem.getUrgent());

            persistentItem.delete(iItem);
            persistentSuport.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedInsertItemBeforeEndDataSet.xml");
            System.out.println("testInsertNonExistingItemBeforeEnd was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testInsertNonExistingItemBeforeEnd was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testInsertNonExistingItemBeforeEnd");
        }
    }

}