package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.DegreeInfo;
import Dominio.ICurso;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeInfo;

/**
 * @author T�nia Pous�o Created on 30/Out/2003
 */
public class DegreeInfoOJB extends PersistentObjectOJB implements IPersistentDegreeInfo {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentDegreeInfo#readDegreeInfoByDegree(Dominio.ICurso)
     */
    public List readDegreeInfoByDegree(ICurso degree) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeKey", degree.getIdInternal());

        return queryList(DegreeInfo.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentDegreeInfo#readDegreeInfoByDegreeAndExecutionYear(Dominio.ICurso,
     *      Dominio.IExecutionYear)
     */
    public List readDegreeInfoByDegreeAndExecutionYear(ICurso degree, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeKey", degree.getIdInternal());
        criteria.addBetween("lastModificationDate", executionYear.getBeginDate(), executionYear
                .getEndDate());

        return queryList(DegreeInfo.class, criteria);
    }
}