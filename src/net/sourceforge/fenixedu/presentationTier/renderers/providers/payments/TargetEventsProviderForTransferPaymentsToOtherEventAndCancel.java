package net.sourceforge.fenixedu.presentationTier.renderers.providers.payments;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.TransferPaymentsToOtherEventAndCancelBean;
import net.sourceforge.fenixedu.domain.accounting.Event;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class TargetEventsProviderForTransferPaymentsToOtherEventAndCancel implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final TransferPaymentsToOtherEventAndCancelBean transferPaymentsBetweenEventsBean = (TransferPaymentsToOtherEventAndCancelBean) source;
	final Set<Event> result = new HashSet<Event>();
	result.addAll(transferPaymentsBetweenEventsBean.getSourceEvent().getPerson().getEvents());
	result.remove(transferPaymentsBetweenEventsBean.getSourceEvent());

	return result;

    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
