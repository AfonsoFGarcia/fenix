package Dominio;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class University extends DomainObject implements IUniversity {

	private String code;
	private String name;

	public University() {
	}

	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof IUniversity) {
			IUniversity universityCode = (IUniversity) obj;

			resultado = (this.getCode().equals(universityCode.getCode())) && (this.getName().equals(universityCode.getName()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "code = " + this.getCode() + "; ";
		result += "name = " + this.getName() + "; ";
		return result;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String string) {
		code = string;
	}

	public String getName() {
		return name;
	}

	public void setName(String string) {
		name = string;
	}

}
