/*
 * ApagarTurma.java
 *
 * Created on 27 de Outubro de 2002, 18:13
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * Servi�o ApagarTurma.
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho
 */
import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;

public class ApagarTurma extends FenixService {

    public Boolean run(InfoClass infoClass) {
	rootDomainObject.readSchoolClassByOID(infoClass.getIdInternal()).delete();
	return Boolean.TRUE;
    }

}
