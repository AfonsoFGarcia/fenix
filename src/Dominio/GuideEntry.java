

package Dominio;

import Util.DocumentType;
import Util.GraduationType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideEntry implements IGuideEntry {
  protected Integer internalCode;
  protected Integer keyGuide;
  
  
  
  protected GraduationType graduationType;
  protected DocumentType documentType;
  protected String description;
  protected Double price;
  protected Integer quantity;
  
  protected IGuide guide;
  
 public GuideEntry() { }
    
  public GuideEntry(GraduationType graduationType,DocumentType documentType,String description,Integer quantity,Double price,IGuide guide) {
	this.description = description;
	this.guide = guide;
	this.documentType = documentType;
	this.graduationType = graduationType;
	this.price = price;
	this.quantity = quantity;


  }
  
  
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof IGuideEntry) {
		IGuideEntry guideEntry = (IGuideEntry)obj;

      resultado = getGuide().equals(guideEntry.getGuide()) &&
                  getGraduationType().equals(guideEntry.getGraduationType()) &&
				  getDocumentType().equals(guideEntry.getDocumentType()) &&
				  getDescription().equals(guideEntry.getDescription());
    }

    return resultado;
  }
  
  public String toString() {
    String result = "[GUIDE ENTRY";
    result += ", codInt=" + internalCode;
    result += ", description=" + description;
    result += ", guide=" + guide;
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
	public IGuide getGuide() {
		return guide;
	}
	
	/**
	 * @return
	 */
	public Integer getInternalCode() {
		return internalCode;
	}
	
	/**
	 * @return
	 */
	public Integer getKeyGuide() {
		return keyGuide;
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
	public void setGuide(IGuide guide) {
		this.guide = guide;
	}
	
	/**
	 * @param integer
	 */
	public void setInternalCode(Integer integer) {
		internalCode = integer;
	}
	
	/**
	 * @param integer
	 */
	public void setKeyGuide(Integer integer) {
		keyGuide = integer;
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
