package Dominio;

import java.util.List;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class Branch implements IBranch {

	private Integer internalID;
	private Integer keyDegreeCurricularPlan;
	
	private String name;
	private String code;
	private List scopes;
	private IDegreeCurricularPlan degreeCurricularPlan; 

	public Branch() {
		setName(null);
		setCode(null);
		setInternalID(null);
		setScopes(null);
		setDegreeCurricularPlan(null);
	}

	public Branch(String name, String code) {
		this();
		setName(name);
		setCode(code);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof Branch) {
			Branch branch = (Branch) obj;
			resultado = this.getName().equals(branch.getName()) && this.getCode().equals(branch.getCode()) &&
						this.getDegreeCurricularPlan().equals(branch.getDegreeCurricularPlan());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "idInternal = " + this.internalID + "; ";
		result += "name = " + this.name + "; ";
		result += "code = " + this.code + "]\n";
		return result;
	}

	
	/**
	 * @return String
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return Integer
	 */
	public Integer getInternalID() {
		return internalID;
	}

	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the code.
	 * @param code The code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Sets the internalID.
	 * @param internalID The internalID to set
	 */
	public void setInternalID(Integer internalID) {
		this.internalID = internalID;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return List
	 */
	public List getScopes() {
		return scopes;
	}

	/**
	 * Sets the scopes.
	 * @param scopes The scopes to set
	 */
	public void setScopes(List scopes) {
		this.scopes = scopes;
	}

	/**
	 * @return
	 */
	public IDegreeCurricularPlan getDegreeCurricularPlan() {
		return degreeCurricularPlan;
	}

	/**
	 * @return
	 */
	public Integer getKeyDegreeCurricularPlan() {
		return keyDegreeCurricularPlan;
	}

	/**
	 * @param plan
	 */
	public void setDegreeCurricularPlan(IDegreeCurricularPlan plan) {
		degreeCurricularPlan = plan;
	}

	/**
	 * @param integer
	 */
	public void setKeyDegreeCurricularPlan(Integer integer) {
		keyDegreeCurricularPlan = integer;
	}

}