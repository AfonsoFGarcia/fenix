package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRegularSections;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quit�rio
 *  
 */
public class TeacherAdministrationSiteComponentServiceRegularSectionsTest extends TestCaseReadServices {

    /**
     * @param testName
     */
    public TeacherAdministrationSiteComponentServiceRegularSectionsTest(String testName) {
        super(testName);
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "TeacherAdministrationSiteComponentService";
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        Object[] args = { new Integer(27), new InfoSiteCommon(), new InfoSiteRegularSections(), null,
                new Integer(6), null };
        return args;
    }

    protected Object getObjectToCompare() {
        ISuportePersistente sp = null;
        InfoExecutionCourse infoExecutionCourse = null;
        ISite site = null;
        List sections = null;
        List regularSectionsList = null;
        ISection iSuperiorSection = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IPersistentSite persistentSite = sp.getIPersistentSite();
            IPersistentSection persistentSection = sp.getIPersistentSection();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, new Integer(27));
            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);

            site = persistentSite.readByExecutionCourse(executionCourse);

            sections = persistentSection.readBySite(site);

            regularSectionsList = persistentSection.readBySite(site);

            iSuperiorSection = (ISection) persistentSection.readByOID(Section.class, new Integer(6));

            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
            e.printStackTrace();
        }

        List infoSections = new ArrayList();
        ListIterator iter = sections.listIterator();

        while (iter.hasNext()) {
            InfoSection infoSection = Cloner.copyISection2InfoSection((ISection) iter.next());
            infoSections.add(infoSection);
        }

        List infoRegularSectionsList = new ArrayList();

        if (regularSectionsList != null && regularSectionsList.isEmpty() == false) {
            Iterator iterSections = regularSectionsList.iterator();
            while (iterSections.hasNext()) {
                ISection section = (ISection) iterSections.next();
                if (section.getSuperiorSection() != null
                        && section.getSuperiorSection().equals(iSuperiorSection)) {
                    infoRegularSectionsList.add(Cloner.copyISection2InfoSection(section));
                }

            }
        }
        Collections.sort(infoRegularSectionsList);

        InfoSiteRegularSections infoSiteRegularSections = new InfoSiteRegularSections();
        infoSiteRegularSections.setRegularSections(infoRegularSectionsList);

        InfoSite infoSite = Cloner.copyISite2InfoSite(site);
        Collections.sort(infoSections);
        InfoSiteCommon infoSiteCommon = new InfoSiteCommon();
        infoSiteCommon.setExecutionCourse(infoExecutionCourse);
        infoSiteCommon.setMail(infoSite.getMail());
        infoSiteCommon.setSections(infoSections);
        infoSiteCommon.setTitle(infoExecutionCourse.getNome());

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(infoSiteCommon,
                infoSiteRegularSections);

        return siteView;
    }

    /**
     * This method must return 'true' if the service needs authorization to be
     * runned and 'false' otherwise.
     */
    protected boolean needsAuthorization() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentListOfServiceToBeTestedUnsuccessfuly()
     */
    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
     */
    protected int getNumberOfItemsToRetrieve() {
        return 0;
    }

}