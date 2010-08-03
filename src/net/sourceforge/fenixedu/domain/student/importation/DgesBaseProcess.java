package net.sourceforge.fenixedu.domain.student.importation;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import pt.utl.ist.fenix.tools.loaders.DataLoaderFromFile;

/**
 * 
 * @author naat
 * 
 */
public abstract class DgesBaseProcess {

    static Map<String, Ingression> CONTINGENT_TO_INGRESSION_CONVERSION = new HashMap<String, Ingression>();

    static {
	// Contingente Geral
	CONTINGENT_TO_INGRESSION_CONVERSION.put("1", Ingression.CNA01);
	// Contingente A�ores
	CONTINGENT_TO_INGRESSION_CONVERSION.put("2", Ingression.CNA02);
	// Contingente Madeira
	CONTINGENT_TO_INGRESSION_CONVERSION.put("3", Ingression.CNA03);
	// Contingente Emigrantes
	CONTINGENT_TO_INGRESSION_CONVERSION.put("5", Ingression.CNA05);
	// Contingente Militar
	CONTINGENT_TO_INGRESSION_CONVERSION.put("6", Ingression.CNA06);
	// Contingente Deficientes
	CONTINGENT_TO_INGRESSION_CONVERSION.put("D", Ingression.CNA07);
    }

    public DgesBaseProcess() {

    }

    protected List<DegreeCandidateDTO> parseDgesFile(InputStream dgesInputStream, int streamSize, String university,
	    String entryPhase) {

	final Collection<DegreeCandidateDTO> result = new DataLoaderFromFile<DegreeCandidateDTO>().load(DegreeCandidateDTO.class,
		dgesInputStream, streamSize);
	setConstantFields(university, entryPhase, result);
	return (List<DegreeCandidateDTO>) result;

    }

    private void setConstantFields(String university, String entryPhase, final Collection<DegreeCandidateDTO> result) {
	for (final DegreeCandidateDTO degreeCandidateDTO : result) {
	    degreeCandidateDTO.setIstUniversity(university);
	    degreeCandidateDTO.setEntryPhase(EntryPhase.valueOf(entryPhase));
	}
    }

}