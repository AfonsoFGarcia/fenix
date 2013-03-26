/*
 * Created on Jun 24, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.list;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoListGrantSubsidy extends InfoObject {

    private InfoGrantSubsidy infoGrantSubsidy;

    private List infoGrantParts;

    /**
     * @return Returns the infoGrantParts.
     */
    public List getInfoGrantParts() {
        return infoGrantParts;
    }

    /**
     * @param infoGrantParts
     *            The infoGrantParts to set.
     */
    public void setInfoGrantParts(List infoGrantParts) {
        this.infoGrantParts = infoGrantParts;
    }

    /**
     * @return Returns the infoGrantSubsidy.
     */
    public InfoGrantSubsidy getInfoGrantSubsidy() {
        return infoGrantSubsidy;
    }

    /**
     * @param infoGrantSubsidy
     *            The infoGrantSubsidy to set.
     */
    public void setInfoGrantSubsidy(InfoGrantSubsidy infoGrantSubsidy) {
        this.infoGrantSubsidy = infoGrantSubsidy;
    }
}