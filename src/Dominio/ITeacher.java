/*
 * ITeacher.java
 */
package Dominio;
/**
 *
 * @author  EP15
 * @author Ivo Brand�o
 */
public interface ITeacher {
    public Integer getTeacherNumber();

	public IPessoa getPerson();
	

    public void setTeacherNumber(Integer number);
	public void setPerson(IPessoa person);
	
}