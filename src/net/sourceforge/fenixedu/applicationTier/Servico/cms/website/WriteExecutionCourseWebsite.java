/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.cms.website;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cms.website.ExecutionCourseWebsite;
import net.sourceforge.fenixedu.domain.cms.website.WebsiteType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/> <br/> Created on
 *         13:53:22,7/Dez/2005
 * @version $Id$
 */
public class WriteExecutionCourseWebsite extends CmsService {
    public static class ExecutionCourseAlreadyHasWebsiteException extends FenixServiceException {
        private static final long serialVersionUID = -8968236930724948984L;

        public ExecutionCourseAlreadyHasWebsiteException(String msg) {
            super(msg);
        }
    }

    static public class WriteExecutionCourseWebsiteParameters {
        private String name;

        private String description;

        private Integer executionCourseID;

        private Person person;

        private Integer websiteTypeID;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getExecutionCourseID() {
            return executionCourseID;
        }

        public void setExecutionCourseID(Integer executionCourseID) {
            this.executionCourseID = executionCourseID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }

        public Integer getWebsiteTypeID() {
            return this.websiteTypeID;
        }

        public void setWebsiteTypeID(Integer websiteTypeID) {
            this.websiteTypeID = websiteTypeID;
        }

    }

    public ExecutionCourseWebsite run(WriteExecutionCourseWebsiteParameters parameters)
            throws ExcepcaoPersistencia, FenixServiceException {
        ExecutionCourseWebsite website = new ExecutionCourseWebsite();
        website.setName(parameters.getName());
        website.setDescription(parameters.getDescription());

        ExecutionCourse executionCourse = (ExecutionCourse) persistentObject.readByOID(
                ExecutionCourse.class, parameters.getExecutionCourseID());
        if (executionCourse.getExecutionCourseWebsite() != null) {
            throw new ExecutionCourseAlreadyHasWebsiteException(
                    "The selected execution course already have a website");
        }
        
        WebsiteType websiteType = (WebsiteType) persistentObject.readByOID(WebsiteType.class, parameters.getWebsiteTypeID());
        if (websiteType == null) {
            throw new FenixServiceException("The website type could not be found");
        }
        
        website.setExecutionCourse(executionCourse);
        website.setWebsiteType(websiteType);
        website.setCreator(parameters.getPerson());
        website.addOwners(parameters.getPerson());
        
        this.updateRootObjectReferences(website);
        
        return website;
    }

    private void updateRootObjectReferences(ExecutionCourseWebsite website) throws ExcepcaoPersistencia {
        this.readFenixCMS().addContents(website);
        this.readFenixCMS().addUsers(website.getCreator());
    }
}
