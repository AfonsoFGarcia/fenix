/*
 * Created on 17/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.guide;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> 17/Nov/2003
 */
public interface IPersistentReimbursementGuide extends IPersistentObject {

    public Integer generateReimbursementGuideNumber() throws ExcepcaoPersistencia;
}