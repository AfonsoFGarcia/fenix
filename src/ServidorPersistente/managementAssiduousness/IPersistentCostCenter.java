/*
 * Created on 16/Dez/2004
 */
package ServidorPersistente.managementAssiduousness;

import java.util.List;

import ServidorPersistente.IPersistentObject;

/**
 * @author T�nia Pous�o
 *
 */
public interface IPersistentCostCenter  extends IPersistentObject {
    public Integer countAllCostCenter() throws Exception;
    public List readAll() throws Exception;
}
