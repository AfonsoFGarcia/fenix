/*
 * Created on 17/Ago/2004
 */
package Dominio;


/**
 * @author joaosa & rmalo
 */
 
public interface IAttendInAttendsSet extends IDomainObject {
		
	public Integer getKeyAttend();
	public IFrequenta getAttend();
	public Integer getKeyAttendsSet();
	public IAttendsSet getAttendsSet();
	public void setKeyAttend(Integer keyAttend);
	public void setAttend(IFrequenta attend);
	public void setKeyAttendsSet(Integer keyAttendsSet);
	public void setAttendsSet(IAttendsSet attendsSet);
	
}
