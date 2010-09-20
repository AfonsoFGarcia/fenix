package net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class PhdCandidacyFeedbackRequestDocument extends PhdCandidacyFeedbackRequestDocument_Base {

    private PhdCandidacyFeedbackRequestDocument() {
	super();
    }

    public PhdCandidacyFeedbackRequestDocument(PhdCandidacyFeedbackRequestElement element, String remarks, byte[] content,
	    String filename, Person uploader) {
	this();

	// first set jury element and then init document
	check(element.getProcess(), "error.phd.PhdProgramProcessDocument.candidacyProcess.cannot.be.null");
	check(element, "error.PhdCandidacyFeedbackRequestDocument.invalid.element");
	setElement(element);

	init(element.getProcess(), PhdIndividualProgramDocumentType.CANDIDACY_FEEDBACK_DOCUMENT, remarks, content, filename, uploader);
    }

    @Override
    protected void checkParameters(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType, byte[] content,
	    String filename, Person uploader) {

	if (documentType == null || content == null || content.length == 0 || StringUtils.isEmpty(filename)) {
	    throw new DomainException("error.phd.PhdProgramProcessDocument.documentType.and.file.cannot.be.null");
	}
    }

    @Override
    protected void setDocumentVersion(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType) {
	if (documentType.isVersioned()) {
	    if (process != null) {
		final Set<PhdProgramProcessDocument> documentsByType = process.getDocumentsByType(documentType);
		super.setDocumentVersion(documentsByType.isEmpty() ? 1 : documentsByType.size() + 1);

	    } else {
		super.setDocumentVersion(getElement().getFeedbackDocumentsCount() + 1);
	    }
	} else {
	    super.setDocumentVersion(1);
	}
    }

    /**
     * <pre>
     * Format /PhdIndividualProgram/{processId}
     * </pre>
     * 
     * @return
     */
    protected VirtualPath getVirtualPath() {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode("PhdIndividualProgram", "PhdIndividualProgram"));
	filePath.addNode(new VirtualPathNode(getElement().getProcess().getIndividualProgramProcess().getIdInternal().toString(),
		getElement().getProcess().getIndividualProgramProcess().getIdInternal().toString()));
	return filePath;
    }
}
