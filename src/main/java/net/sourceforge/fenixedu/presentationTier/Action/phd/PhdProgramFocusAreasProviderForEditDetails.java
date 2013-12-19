package net.sourceforge.fenixedu.presentationTier.Action.phd;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class PhdProgramFocusAreasProviderForEditDetails extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object current) {
        final PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) source;
        return bean.hasPhdProgram() ? bean.getPhdProgram().getPhdProgramFocusAreas() : Bennu.getInstance()
                .getPhdProgramFocusAreasSet();
    }
}
