package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.DFACandidacyBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionDegreesForDegreeCurricularPlan implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        final DFACandidacyBean dfaCandidacyBean = (DFACandidacyBean) source;
        final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        if (dfaCandidacyBean.getDegree() != null && dfaCandidacyBean.getDegreeCurricularPlan() != null) {
            result.addAll(dfaCandidacyBean.getDegreeCurricularPlan().getExecutionDegreesSet());
            Collections.sort(result, new BeanComparator("year"));
        } else {
            dfaCandidacyBean.setExecutionDegree(null);
        }
        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
