/*
 * Created on 7/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

/**
 * @author dcs-rjao
 *
 * 7/Jul/2003
 */
public interface IEnrolmentEquivalenceRestriction extends IDomainObject {
	//	public boolean equals(Object obj) {
	public abstract IEnrolmentEquivalence getEnrolmentEquivalence();
	public abstract IEnrolment getEquivalentEnrolment();
	public abstract void setEnrolmentEquivalence(IEnrolmentEquivalence equivalence);
	public abstract void setEquivalentEnrolment(IEnrolment enrolment);
}