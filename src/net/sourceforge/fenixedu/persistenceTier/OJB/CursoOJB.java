/*
 * CursoOJB.java
 * 
 * Created on 1 de Novembro de 2002, 12:37
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author rpfi
 * @author jpvl
 */

import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.ojb.broker.query.Criteria;

public class CursoOJB extends PersistentObjectOJB implements ICursoPersistente {

    /** Creates a new instance of CursoOJB */
    public CursoOJB() {
    }

    public IDegree readBySigla(String sigla) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("sigla", sigla);
        return (IDegree) queryObject(Degree.class, criteria);
    }

    public IDegree readByIdInternal(Integer idInternal) throws ExcepcaoPersistencia {
        return (IDegree) readByOID(Degree.class, idInternal);
    }

    public void delete(IDegree degree) throws ExcepcaoPersistencia {
        super.delete(degree);
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(Degree.class, criteria);
    }

    public List readAllByDegreeType(TipoCurso degreeType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("tipoCurso", degreeType);
        return queryList(Degree.class, criteria, "nome", true);
    }

    public IDegree readByNameAndDegreeType(String name, TipoCurso degreeType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("nome", name);
        criteria.addEqualTo("tipoCurso", degreeType);
        return (IDegree) queryObject(Degree.class, criteria);
    }

}