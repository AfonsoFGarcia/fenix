/*
 * Created on 12/Mai/2003
 *
 */
package ServidorPersistente;
import java.util.List;

import Dominio.IAttendsSet;
import Dominio.IStudentGroup;
import Dominio.ITurno;

/**
 * @author asnr and scpo
 *
 */
public interface IPersistentStudentGroup extends IPersistentObject{
    public void delete(IStudentGroup studentGroup) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public IStudentGroup readStudentGroupByAttendsSetAndGroupNumber (IAttendsSet attendsSet,Integer studentGroupNumber) throws ExcepcaoPersistencia;

    public List readAllStudentGroupByAttendsSet (IAttendsSet attendsSet) throws ExcepcaoPersistencia;

    public List readAllStudentGroupByAttendsSetAndShift (IAttendsSet attendsSet, ITurno shift) throws ExcepcaoPersistencia;

    public List readAllStudentGroupByShift (ITurno shift) throws ExcepcaoPersistencia;	
}
