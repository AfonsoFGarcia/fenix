package net.sourceforge.fenixedu.domain.documents;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Pedro Santos (pmrsa)
 */
public class DocumentRequestGeneratedDocument extends DocumentRequestGeneratedDocument_Base {
    protected DocumentRequestGeneratedDocument(DocumentRequest source, Party addressee, Person operator, String filename,
	    byte[] content) {
	super();
	setSource(source);
	init(GeneratedDocumentType.determineType(source.getDocumentRequestType()), addressee, operator, filename, content);
    }

    @Override
    public void delete() {
	removeSource();
	super.delete();
    }

    @Service
    public static void store(DocumentRequest source, String filename, byte[] content) {
	if (PropertiesManager.getBooleanProperty(CONFIG_DSPACE_DOCUMENT_STORE)) {
	    new DocumentRequestGeneratedDocument(source, source.getStudent().getPerson(), AccessControl.getPerson(), filename,
		    content);
	}
    }
}
