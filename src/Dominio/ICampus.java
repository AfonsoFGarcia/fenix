package Dominio;

/**
 * @author T�nia Pous�o
 * Create on 10/Nov/2003
 */
public interface ICampus extends IDomainObject {
	public ICurso getDegree();
	public String getName();
	
	public void setDegree(ICurso degree);
	public void setName(String name);
}
