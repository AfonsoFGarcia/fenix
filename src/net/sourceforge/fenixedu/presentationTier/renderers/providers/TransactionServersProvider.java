package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import eu.ist.fenixframework.pstm.TransactionReport;

public class TransactionServersProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final TransactionReport transactionReport = (TransactionReport) source;
	return transactionReport.getServers();
    }

    public Converter getConverter() {
        return null;
    }

}
