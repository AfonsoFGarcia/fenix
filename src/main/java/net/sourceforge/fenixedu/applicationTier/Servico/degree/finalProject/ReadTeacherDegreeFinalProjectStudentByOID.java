/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import pt.ist.fenixframework.DomainObject;

/**
 * @author jpvl
 */
public class ReadTeacherDegreeFinalProjectStudentByOID extends ReadDomainObjectService {

    @Override
    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson
                .newInfoFromDomain((TeacherDegreeFinalProjectStudent) domainObject);
    }

    @Override
    protected DomainObject readDomainObject(final Integer idInternal) {
        return rootDomainObject.readTeacherDegreeFinalProjectStudentByOID(idInternal);
    }

}
