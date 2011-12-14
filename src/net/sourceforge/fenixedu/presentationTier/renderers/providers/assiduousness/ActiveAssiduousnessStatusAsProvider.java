package net.sourceforge.fenixedu.presentationTier.renderers.providers.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatus;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ActiveAssiduousnessStatusAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	List<AssiduousnessStatus> assiduousnessStatusList = new ArrayList<AssiduousnessStatus>();
	for (AssiduousnessStatus assiduousnessStatus : RootDomainObject.getInstance().getAssiduousnessStatus()) {
	    if (assiduousnessStatus.getState().equals(AssiduousnessState.ACTIVE)) {
		assiduousnessStatusList.add(assiduousnessStatus);
	    }
	}

	return assiduousnessStatusList;

    }

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }

}
