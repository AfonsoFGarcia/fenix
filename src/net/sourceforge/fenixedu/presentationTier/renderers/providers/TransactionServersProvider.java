package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixframework.pstm.TransactionReport;

public class TransactionServersProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final TransactionReport transactionReport = (TransactionReport) source;
	return transactionReport.getServers();
    }

    public Converter getConverter() {
        return null;
    }

}
