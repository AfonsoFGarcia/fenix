package Dominio;

import java.util.List;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class Branch extends DomainObject implements IBranch {

	private Integer keyDegreeCurricularPlan;

	private String name;
	private String code;
	private List scopes;
	private IDegreeCurricularPlan degreeCurricularPlan;
	//added by Nuno Correia e Ricardo Rodrigues
	private String acronym;
	private Integer specializationCredits;
	private Integer secondaryCredits;


	public Branch() {
		setName(null);
		setCode(null);
		setScopes(null);
		setDegreeCurricularPlan(null);
		setAcronym(null);
	}

	public Branch(String name, String code) {
		this();
		setName(name);
		setCode(code);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof IBranch) {
			IBranch branch = (IBranch) obj;
			resultado =
				this.getCode().equals(branch.getCode())
					&& this.getDegreeCurricularPlan().equals(branch.getDegreeCurricularPlan());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "idInternal = " + getIdInternal() + "; ";
		result += "name = " + this.name + "; ";
		result += "code = " + this.code + "; ";
		result += "acronym = " + this.acronym + "]\n";
		return result;
	}

	/**
	 * @author Fernanda Quit�rio
	 */
	public Boolean representsCommonBranch() {
		if (this.name != null && this.name.equals("") && this.code != null && this.code.equals("")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * @return String
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the code.
	 * 
	 * @param code
	 *          The code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *          The name to set
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
	 * 
	 * @param scopes
	 *          The scopes to set
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

	/**
	 * @author Nuno Correia
	 * @author Ricardo Rodrigues
	 */

	/**
	 * 
	 * @param string
	 */
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getAcronym() {
		return acronym;
	}
	
	/**
	 * @return
	 */
	public Integer getSecondaryCredits()
	{
		return secondaryCredits;
	}

	/**
	 * @param secondaryCredits
	 */
	public void setSecondaryCredits(Integer secondaryCredits)
	{
		this.secondaryCredits = secondaryCredits;
	}

	/**
	 * @return
	 */
	public Integer getSpecializationCredits()
	{
		return specializationCredits;
	}

	/**
	 * @param specializationCredits
	 */
	public void setSpecializationCredits(Integer specializationCredits)
	{
		this.specializationCredits = specializationCredits;
	}

}