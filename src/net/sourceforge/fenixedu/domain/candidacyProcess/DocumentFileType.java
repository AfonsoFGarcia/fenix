package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.StringAppender;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum DocumentFileType {
    DOCUMENT_IDENTIFICATION,
    PAYMENT_DOCUMENT,
    HABILITATION_CERTIFICATE_DOCUMENT,
    FIRST_CYCLE_ACCESS_HABILITATION_DOCUMENT,
    CV_DOCUMENT,
    REPORT_OR_WORK_DOCUMENT,
    HANDICAP_PROOF_DOCUMENT,
    VAT_CARD_DOCUMENT,
    PHOTO;
    
    protected String localizedName(Locale locale) {
	return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(qualifiedName());
    }

    protected String qualifiedName() {
	return StringAppender.append(this.getClass().getSimpleName(), ".", name());
    }

    protected String localizedName() {
	return localizedName(Language.getLocale());
    }

    public String getLocalizedName() {
	return localizedName();
    }
    
}
