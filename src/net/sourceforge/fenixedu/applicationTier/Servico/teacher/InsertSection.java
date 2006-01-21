package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;

/**
 * @author Fernanda Quit�rio
 */
public class InsertSection extends Service {


    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, String sectionName,
            Integer sectionOrder) throws FenixServiceException, ExcepcaoPersistencia {
     
        final IPersistentSite persistentSite = persistentSupport.getIPersistentSite();
        final IPersistentSection persistentSection = persistentSupport.getIPersistentSection();
        final Site site = persistentSite.readByExecutionCourse(infoExecutionCourseCode);

        Section parentSection = null;
        if (sectionCode != null) {
            parentSection = (Section) persistentSection.readByOID(Section.class, sectionCode);
        }
        site.createSection(sectionName, parentSection, sectionOrder);
         
        return new Boolean(true);
    }
}