/*
 * Created on 21/Mar/2003
 *
 * 
 */
package Dominio;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class Contributor implements IContributor{
	private Integer idInternal;
	private Integer contributorNumber;
	private String contributorName;
	private String contributorAddress;
	
	public Contributor() {
		contributorNumber = null;
		contributorName = null;
		contributorAddress = null;
	} 
		
	public Contributor(Integer contributorNumber, String contributorName, String contributorAddress){
	 		this.contributorNumber = contributorNumber;
			this.contributorName = contributorName;
			this.contributorAddress = contributorAddress;
	}
	
	public boolean equals(Object o) {
		return
		((o instanceof IContributor) &&
		(contributorNumber.equals(((IContributor)o).getContributorNumber())) &&
		(contributorName.equals(((IContributor)o).getContributorName())) &&
		(contributorAddress.equals(((IContributor)o).getContributorAddress())));

	}

	public String toString() {
		String result = "Contributor:\n";
		result += "\n  - Internal Code : " + idInternal;
		result += "\n  - Contributor Number : " + contributorNumber;
		result += "\n  - Contributor Name : " + contributorName;
		result += "\n  - Contributor Address : " + contributorAddress;
		return result;
	}
    
	/**
	 * @return String
	 */
	public String getContributorAddress() {
		return contributorAddress;
	}

	/**
	 * @return String
	 */
	public String getContributorName() {
		return contributorName;
	}

	/**
	 * @return Integer
	 */
	public Integer getContributorNumber() {
		return contributorNumber;
	}

	/**
	 * @return Integer
	 */
	public Integer getIdInternal() {
		return idInternal;
	}

	/**
	 * Sets the contributorAddress.
	 * @param contributorAddress The contributorAddress to set
	 */
	public void setContributorAddress(String contributorAddress) {
		this.contributorAddress = contributorAddress;
	}

	/**
	 * Sets the contributorName.
	 * @param contributorName The contributorName to set
	 */
	public void setContributorName(String contributorName) {
		this.contributorName = contributorName;
	}

	/**
	 * Sets the contributorNumber.
	 * @param contributorNumber The contributorNumber to set
	 */
	public void setContributorNumber(Integer contributorNumber) {
		this.contributorNumber = contributorNumber;
	}

	/**
	 * Sets the idInternal.
	 * @param idInternal The idInternal to set
	 */
	public void setIdInternal(Integer idInternal) {
		this.idInternal = idInternal;
	}

}
