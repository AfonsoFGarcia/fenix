/*
 * CurriculumOJB.java
 * 
 * Created on 6 de Janeiro de 2003, 20:44
 */
package ServidorPersistente.OJB;
/**
 * @author Jo�o Mota
 */
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Curriculum;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurriculum;
/**
 * @author EP 15
 */
public class CurriculumOJB extends ObjectFenixOJB implements IPersistentCurriculum
{
    public ICurriculum readCurriculumByCurricularCourse(ICurricularCourse curricularCourse)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyCurricularCourse", curricularCourse.getIdInternal());
        List curricularInformation = queryList(Curriculum.class, criteria, "lastModificationDate", false);
        return (curricularInformation != null 
                && curricularInformation.size() > 0?(ICurriculum) curricularInformation.get(0):null);
    }
    public List readCurriculumHistoryByCurricularCourse(ICurricularCourse curricularCourse)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyCurricularCourse", curricularCourse.getIdInternal());
        return queryList(Curriculum.class, criteria);
    }
    public ICurriculum readCurriculumByCurricularCourseAndExecutionYear(
        ICurricularCourse curricularCourse,
        IExecutionYear executionYear)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyCurricularCourse", curricularCourse.getIdInternal());
        criteria.addLessOrEqualThan("lastModificationDate", executionYear.getEndDate());
        List curricularInformation = queryList(Curriculum.class, criteria, "lastModificationDate", false);
        return (curricularInformation != null 
                && curricularInformation.size() > 0?(ICurriculum) curricularInformation.get(0):null);
    }
    
    public List readAll() throws ExcepcaoPersistencia
    {
        return queryList(Curriculum.class, new Criteria());
    }
    public void delete(ICurriculum curriculum) throws ExcepcaoPersistencia
    {
        super.delete(curriculum);
    }
    
}
