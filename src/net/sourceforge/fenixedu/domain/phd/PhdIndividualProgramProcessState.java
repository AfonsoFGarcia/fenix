package net.sourceforge.fenixedu.domain.phd;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdIndividualProgramProcessState implements PhdProcessStateType {

    CANDIDACY(true),

    NOT_ADMITTED,

    CANCELLED,

    WORK_DEVELOPMENT(true),

    SUSPENDED,

    THESIS_DISCUSSION(true),

    FLUNKED,

    CONCLUDED;

    private boolean activeState;

    private PhdIndividualProgramProcessState(boolean activeState) {
	this.activeState = activeState;
    }

    private PhdIndividualProgramProcessState() {
	this(false);
    }

    public boolean isActive() {
	return activeState;
    }
    
    @Override
    public String getName() {
	return name();
    }

    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }
}
