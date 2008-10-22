package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.OriginInformationBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class SchoolTimeDistrictSubdivisionForOriginInformationFormProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	final OriginInformationBean originInformationForm = (OriginInformationBean) source;
	if (originInformationForm.getSchoolTimeDistrictOfResidence() != null) {
	    return originInformationForm.getSchoolTimeDistrictOfResidence().getDistrictSubdivisions();
	}

	return Collections.emptyList();
    }

}
