/*
 * Created on Aug 24, 2004
 *
 */
package DataBeans.Seminaries;

import java.util.List;

import DataBeans.DataTranferObject;

/**
 * @author Jo�o Mota
 *  
 */
public class SelectCandidaciesDTO extends DataTranferObject {

    private List seminaries;

    private List candidacies;

    /**
     * @return Returns the candidacies.
     */
    public List getCandidacies() {
        return candidacies;
    }

    /**
     * @param candidacies
     *            The candidacies to set.
     */
    public void setCandidacies(List candidacies) {
        this.candidacies = candidacies;
    }

    /**
     * @return Returns the seminaries.
     */
    public List getSeminaries() {
        return seminaries;
    }

    /**
     * @param seminaries
     *            The seminaries to set.
     */
    public void setSeminaries(List seminaries) {
        this.seminaries = seminaries;
    }

    /**
     *  
     */
    public SelectCandidaciesDTO() {

    }

}