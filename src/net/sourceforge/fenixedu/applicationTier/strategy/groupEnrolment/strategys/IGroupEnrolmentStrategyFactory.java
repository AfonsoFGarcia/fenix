/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import net.sourceforge.fenixedu.domain.Grouping;

/**
 * @author scpo and asnr
 *  
 */
public interface IGroupEnrolmentStrategyFactory {
    public IGroupEnrolmentStrategy getGroupEnrolmentStrategyInstance(Grouping grouping);

}