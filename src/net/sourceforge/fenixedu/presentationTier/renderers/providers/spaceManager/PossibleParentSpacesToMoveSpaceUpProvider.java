package net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager;

import java.util.ArrayList;

import net.sourceforge.fenixedu.dataTransferObject.spaceManager.MoveSpaceBean;
import net.sourceforge.fenixedu.domain.space.Space;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PossibleParentSpacesToMoveSpaceUpProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		MoveSpaceBean bean = (MoveSpaceBean) source;
		Space space = bean.getSpace();
		if (space != null) {
			return space.getPossibleParentSpacesToMoveSpaceUp();
		}
		return new ArrayList<Space>();
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}
}
