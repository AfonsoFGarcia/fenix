package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.functionalities.Functionality;

/**
 * This bean is used to receive a file from the user with the structure to
 * import.
 * 
 * @author cfgi
 */
public class StructureBean implements Serializable {

    /**
     * Serialization id.
     */
    private static final long serialVersionUID = 1L;

    private transient InputStream stream;

    /**
     * This property chooses if the principal attribute of the imported
     * functionalities should be preserved or if all the imported
     * functionalities will be considered as duplicates, that is, will have the
     * value of their principal property set to <code>false</code>.
     * 
     * @see Functionality#setPrincipal(Boolean)
     */
    private boolean principalPreserved;

    private boolean uuidUsed;
    private boolean currentParentUsed;

    public StructureBean() {
        super();

        setPrincipalPreserved(true);
        setUuidUsed(false);
        setCurrentParentUsed(true);
    }

    public InputStream getStream() {
        return this.stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public boolean isPrincipalPreserved() {
        return this.principalPreserved;
    }

    public void setPrincipalPreserved(boolean principalPreserved) {
        this.principalPreserved = principalPreserved;
    }

    public boolean isUuidUsed() {
        return this.uuidUsed;
    }

    public void setUuidUsed(boolean uuidUsed) {
        this.uuidUsed = uuidUsed;
    }

    public boolean isCurrentParentUsed() {
        return this.currentParentUsed;
    }

    public void setCurrentParentUsed(boolean currentParentUsed) {
        this.currentParentUsed = currentParentUsed;
    }

}
