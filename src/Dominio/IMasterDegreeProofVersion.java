/*
 * Created on Oct 10, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package Dominio;

import java.sql.Timestamp;

import Util.MasterDegreeClassification;
import Util.State;

/**
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */
public interface IMasterDegreeProofVersion extends IDomainObject {

	public void setResponsibleEmployee(IEmployee responsibleEmployee);
	public IEmployee getResponsibleEmployee();

	public abstract void setLastModification(Timestamp lastModification);
	public abstract Timestamp getLastModification();

	public abstract void setMasterDegreeThesis(IMasterDegreeThesis masterDegreeThesis);
	public abstract IMasterDegreeThesis getMasterDegreeThesis();

	public abstract void setProofDate(Timestamp proofDate);
	public abstract Timestamp getProofDate();

	public abstract void setThesisDeliveryDate(Timestamp thesisDeliveryDate);
	public abstract Timestamp getThesisDeliveryDate();

	public abstract void setFinalResult(MasterDegreeClassification finalResult);
	public abstract MasterDegreeClassification getFinalResult();

	public abstract void setAttachedCopiesNumber(Integer attachedCopiesNumber);
	public abstract Integer getAttachedCopiesNumber();

	public void setCurrentState(State currentState);
	public State getCurrentState();

}