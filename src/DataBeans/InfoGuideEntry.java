/*
 * Created on 21/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package DataBeans;

import java.io.Serializable;

import Util.DocumentType;
import Util.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoGuideEntry extends InfoObject {
	
	protected GraduationType graduationType;
	protected DocumentType documentType;
	protected String description;
	protected Double price;
	protected Integer quantity;
  
	protected InfoGuide infoGuide;
	
	public InfoGuideEntry() {}
		
	public InfoGuideEntry(GraduationType graduationType, DocumentType documentType, String description, Double price, Integer quantity, InfoGuide infoGuide){	 		
		this.description = description;
		this.documentType = documentType;
		this.graduationType = graduationType;
		this.infoGuide = infoGuide;
		this.price = price;
		this.quantity = quantity;
	}
	
	public boolean equals(Object obj) {
	  boolean resultado = false;
	  if (obj instanceof InfoGuideEntry) {
		InfoGuideEntry infoGuideEntry = (InfoGuideEntry)obj;

		resultado = getInfoGuide().equals(infoGuideEntry.getInfoGuide()) &&
					getGraduationType().equals(infoGuideEntry.getGraduationType()) &&
					getDocumentType().equals(infoGuideEntry.getDocumentType()) &&
					getDescription().equals(infoGuideEntry.getDescription());
	  }
	  return resultado;
	}

	public String toString() {
	  String result = "[GUIDE ENTRY";
	  result += ", description=" + description;
	  result += ", infoGuide=" + infoGuide;
	  result += ", documentType=" + documentType;
	  result += ", graduationType=" + graduationType;
	  result += ", price=" + price;
	  result += ", quantity=" + quantity;
	  result += "]";
	  return result;
	}
    

	

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public DocumentType getDocumentType() {
		return documentType;
	}

	/**
	 * @return
	 */
	public GraduationType getGraduationType() {
		return graduationType;
	}

	/**
	 * @return
	 */
	public InfoGuide getInfoGuide() {
		return infoGuide;
	}

	/**
	 * @return
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @return
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param type
	 */
	public void setDocumentType(DocumentType type) {
		documentType = type;
	}

	/**
	 * @param type
	 */
	public void setGraduationType(GraduationType type) {
		graduationType = type;
	}

	/**
	 * @param guide
	 */
	public void setInfoGuide(InfoGuide guide) {
		infoGuide = guide;
	}

	/**
	 * @param double1
	 */
	public void setPrice(Double double1) {
		price = double1;
	}

	/**
	 * @param integer
	 */
	public void setQuantity(Integer integer) {
		quantity = integer;
	}

}
