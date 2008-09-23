package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;

public class DeleteResultDocumentFile extends FenixService {

    public void run(Integer oid) {
	final ResearchResultDocumentFile documentFile = ResearchResultDocumentFile.readByOID(oid);
	documentFile.getResult().removeDocumentFile(documentFile);
    }
}
