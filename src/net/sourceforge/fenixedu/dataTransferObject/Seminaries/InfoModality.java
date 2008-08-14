/*
 * Created on 1/Ago/2003, 21:14:43
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.Seminaries.Modality;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 1/Ago/2003, 21:14:43
 * 
 */
public class InfoModality extends InfoObject {

    private String description;

    private String name;

    /**
     * @return
     */
    public String getDescription() {
	return description;
    }

    /**
     * @return
     */
    public String getName() {
	return name;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
	description = string;
    }

    /**
     * @param string
     */
    public void setName(String string) {
	name = string;
    }

    public void copyFromDomain(Modality modality) {
	super.copyFromDomain(modality);
	if (modality != null) {
	    setDescription(modality.getDescription());
	    setName(modality.getName());
	}
    }

    public static InfoModality newInfoFromDomain(Modality modality) {
	InfoModality infoModality = null;

	if (modality != null) {
	    infoModality = new InfoModality();
	    infoModality.copyFromDomain(modality);
	}
	return infoModality;
    }

}