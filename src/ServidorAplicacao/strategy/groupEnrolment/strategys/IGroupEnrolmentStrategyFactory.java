/*
 * Created on 28/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.strategy.groupEnrolment.strategys;

import Dominio.IGroupProperties;

/**
 * @author scpo and asnr
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IGroupEnrolmentStrategyFactory {
	public IGroupEnrolmentStrategy getGroupEnrolmentStrategyInstance(IGroupProperties groupProperties);
		
}
