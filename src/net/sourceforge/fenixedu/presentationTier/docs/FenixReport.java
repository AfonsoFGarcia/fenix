package net.sourceforge.fenixedu.presentationTier.docs;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class FenixReport implements Serializable {

    protected final Map<String, Object> parameters = new HashMap<String, Object>();
    
    protected ResourceBundle resourceBundle;
    
    protected Collection dataSource;
    
    public final Map<String, Object> getParameters() {
	return parameters;
    }
    
    public final ResourceBundle getResourceBundle() {
	return resourceBundle;
    }
    
    public final Collection getDataSource() {
	return dataSource;
    }

    public abstract String getReportTemplateKey();
    
    public abstract String getReportFileName();
    
    protected abstract void fillReport();
    
}
