package ServidorPersistente.grant;

/**
 * 
 * @author Barbosa
 * @author Pica
 */
import java.util.Date;
import java.util.List;

import Dominio.grant.owner.IGrantOwner;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import Util.TipoDocumentoIdentificacao;

public interface IPersistentGrantOwner extends IPersistentObject {

    public IGrantOwner readGrantOwnerByNumber(Integer grantOwnerNumber) throws ExcepcaoPersistencia;

    public IGrantOwner readGrantOwnerByPerson(Integer personIdInternal) throws ExcepcaoPersistencia;

    public List readGrantOwnerByPersonName(String personName) throws ExcepcaoPersistencia;

    public List readGrantOwnerByPersonName(String personName, Integer startIndex,
            Integer numberOfElementsInSpan) throws ExcepcaoPersistencia;

    public IGrantOwner readGrantOwnerByPersonID(String idNumber, TipoDocumentoIdentificacao idType)
            throws ExcepcaoPersistencia;

    public Integer readMaxGrantOwnerNumber() throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public List readAllGrantOwnersBySpan(Integer spanNumber, Integer numberOfElementsInSpan,
            String orderBy) throws ExcepcaoPersistencia;

    public Integer countAll();

    public Integer countAllGrantOwnerByName(String personName);

    public Integer countAllByCriteria(Boolean justActiveContracts, Boolean justDesactiveContracts,
            Date dateBeginContract, Date dateEndContract, Integer grantTypeId)
            throws ExcepcaoPersistencia;
}