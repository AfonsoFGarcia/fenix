package net.sourceforge.fenixedu.domain.thesis;

import net.sourceforge.fenixedu.domain.accessControl.NoOneGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ThesisSite extends ThesisSite_Base {

    public ThesisSite(Thesis thesis) {
	super();
	setThesis(thesis);
    }

    @Override
    public IGroup getOwner() {
	return new NoOneGroup();
    }

    @Override
    public MultiLanguageString getName() {
	return MultiLanguageString.i18n().add("pt", String.valueOf(getThesis().getIdInternal())).finish();
    }

    @Override
    protected void disconnect() {
	removeThesis();
	super.disconnect();

    }
}
