package DataBeans;

/**
 * @author Fernanda Quit�rio
 *  
 */
public class InfoSiteInstructions extends DataTranferObject implements ISiteComponent {

    public boolean equals(Object objectToCompare) {

        return (objectToCompare instanceof InfoSiteInstructions);

    }
}