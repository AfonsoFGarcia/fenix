package net.sourceforge.fenixedu._development;

import antlr.ANTLRException;
import dml.DmlCompiler;
import dml.DomainModel;

public class MetadataManager {

    private static MetadataManager instance;

    private final DomainModel domainModel;

    private final org.apache.ojb.broker.metadata.MetadataManager ojbMetadataManager = org.apache.ojb.broker.metadata.MetadataManager.getInstance();

    private MetadataManager(final String domainModelPath) {
        super();
        try {
            domainModel = DmlCompiler.getDomainModel(new String[] { domainModelPath }, true);
        } catch (ANTLRException e) {
            throw new Error(e);
        }
    }

    public static void init(final String domainModelPath) {
        synchronized (MetadataManager.class) {
            if (instance == null) {
                instance = new MetadataManager(domainModelPath);
            }
        }
    }

    public static DomainModel getDomainModel() {
        return instance != null ? instance.domainModel : null;
    }

    public static org.apache.ojb.broker.metadata.MetadataManager getOjbMetadataManager() {
        return instance != null ? instance.ojbMetadataManager : null;
    }

}