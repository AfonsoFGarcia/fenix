/*
 * Created on Nov 15, 2004
 *
 */
package net.sourceforge.fenixedu.domain.oldInquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesSummary_Base;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class OldInquiriesSummary extends OldInquiriesSummary_Base {

    public OldInquiriesSummary() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeExecutionPeriod();
	removeDegree();
	deleteDomainObject();
    }

}
