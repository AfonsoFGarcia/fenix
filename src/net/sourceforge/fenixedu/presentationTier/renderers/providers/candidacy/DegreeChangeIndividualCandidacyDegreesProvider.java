package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeChangeIndividualCandidacyDegreesProvider implements DataProvider {

    private static final String DEGREE_TO_REMOVE_FIRST_CYCLE_CHEMISTRY = "LQ";
    private static final String DEGREE_TO_REMOVE_FIRST_CYCLE_TERRITORY = "LET";
    private static final String DEGREE_TO_REMOVE_FIRST_CYCLE_ENVIRONMENT = "LEAmb";

    public Object provide(Object source, Object currentValue) {
	List<Degree> degrees = Degree.readAllByDegreeType(DegreeType.BOLONHA_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);

	degrees.remove(Degree.readBySigla(DEGREE_TO_REMOVE_FIRST_CYCLE_CHEMISTRY));
	degrees.remove(Degree.readBySigla(DEGREE_TO_REMOVE_FIRST_CYCLE_TERRITORY));
	degrees.remove(Degree.readBySigla(DEGREE_TO_REMOVE_FIRST_CYCLE_ENVIRONMENT));

	return degrees;

    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
