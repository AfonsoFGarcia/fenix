/*
 * InfoCountry.java
 *
 * Created on 13 de Dezembro de 2002, 16:28
 */

package DataBeans;

import Dominio.ICountry;

/**
 *
 * @author  tfc130
 */
public class InfoCountry {
    private String name;
    private String code;
    private String nationality;
        
    public InfoCountry() { }    
    
    public InfoCountry(String name, String code, String nationality) {
		setName(name);
		setCode(code);
		setNationality(nationality);
    }
    
    
    public InfoCountry(ICountry country) {
		this.code = country.getCode();
		this.name = country.getName();
		this.nationality = country.getNationality();
		
    }
    
    public boolean equals(Object obj) {
    	boolean result = false;
    	if (obj instanceof InfoCountry ) {
    		InfoCountry d = (InfoCountry)obj;
			result = (getName().equals(d.getName())) &&
			(getCode().equals(d.getCode())) &&
			(getNationality().equals(d.getNationality()));
    	}
    	return result;
    }



	/**
	 * Returns the code.
	 * @return String
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Returns the name.
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
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the nationality.
	 * @return String
	 */
	public String getNationality() {
		return nationality;
	}

	/**
	 * Sets the nationality.
	 * @param nationality The nationality to set
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

}