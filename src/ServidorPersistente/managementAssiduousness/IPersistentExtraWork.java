/*
 * Created on 16/Dez/2004
 */
package ServidorPersistente.managementAssiduousness;

import java.util.Date;

import Dominio.managementAssiduousness.IExtraWork;
import ServidorPersistente.IPersistentObject;

/**
 * @author T�nia Pous�o
 *
 */
public interface IPersistentExtraWork  extends IPersistentObject{
    public IExtraWork readExtraWorkByDay(Date day) throws Exception;
}
