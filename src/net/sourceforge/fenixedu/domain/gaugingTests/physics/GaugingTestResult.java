/*
 * Created on 26/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.domain.gaugingTests.physics;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> 26/Nov/2003
 *  
 */
public class GaugingTestResult extends GaugingTestResult_Base {

	public GaugingTestResult() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}