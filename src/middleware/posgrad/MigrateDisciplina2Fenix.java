/*
 * Created on 28/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.posgrad;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.CurricularCourse;
import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Util.CurricularCourseExecutionScope;
import Util.CurricularCourseType;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class MigrateDisciplina2Fenix {

	PersistenceBroker broker = null;
	
	
	public MigrateDisciplina2Fenix() {
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	}


	public static void main(String args[]) throws Exception{
		MigrateDisciplina2Fenix migrateDisciplina2Fenix = new MigrateDisciplina2Fenix();
		
		migrateDisciplina2Fenix.broker.beginTransaction();
		migrateDisciplina2Fenix.broker.clearCache();
		migrateDisciplina2Fenix.migratePosGradDisciplina2Fenix();
		
		migrateDisciplina2Fenix.broker.commitTransaction();
	}



	public void migratePosGradDisciplina2Fenix() throws Exception{
		ICurricularCourse curricularCourse2Write = null;
		Posgrad_disciplina curricularCourse2Convert = null;
		List result = null;
		Query query = null;
		Criteria criteria = null;
		QueryByCriteria queryByCriteria = null;
		int coursesWritten = 0;
		int coursesNotWritten = 0;
		try {
			System.out.print("Reading PosGrad Curricular Courses ...");

			broker.beginTransaction();
			broker.clearCache();

			List curricularCoursesPG = getDisciplinas();
			System.out.println("  Done !");

			broker.commitTransaction();
			
			System.out.println("Migrating " + curricularCoursesPG.size() + " PosGrad Curricular Courses to Fenix ...");
			Iterator iterator = curricularCoursesPG.iterator();
			while(iterator.hasNext()){
				curricularCourse2Convert = (Posgrad_disciplina) iterator.next();
				
				broker.beginTransaction();
				broker.clearCache();

				
				// Delete unwanted courses
				if ((curricularCourse2Convert.getNome().indexOf("CR�DITOS") != -1) ||
					(curricularCourse2Convert.getNome().indexOf("CURRICULAR") != -1) ||
					(curricularCourse2Convert.getCodigocursomestrado() == 15) ||
					(curricularCourse2Convert.getCodigocursomestrado() == 31) ||
					(curricularCourse2Convert.getCodigocursomestrado() == 50)){
						coursesNotWritten++;
						broker.commitTransaction();
						continue;
				}
				
				
				
				// Get the old degree corresponding to this Curricular Course
				
				criteria = new Criteria();
				criteria.addEqualTo("codigointerno", new Integer(String.valueOf(curricularCourse2Convert.getCodigocursomestrado())));
				query = new QueryByCriteria(Posgrad_curso_mestrado.class,criteria);
				result = (List) broker.getCollectionByQuery(query);		
		
				if (result.size() == 0){
					throw new Exception("Error Reading OLD Degree (" + curricularCourse2Convert.getNome() + ")");
				}

				Posgrad_curso_mestrado oldDegree = (Posgrad_curso_mestrado) result.get(0);
				
				criteria = new Criteria();
				criteria.addEqualTo("nome", oldDegree.getNomemestrado());
				criteria.addEqualTo("tipoCurso", TipoCurso.MESTRADO_OBJ);
				query = new QueryByCriteria(Curso.class,criteria);
				result = (List) broker.getCollectionByQuery(query);
				
				if (result.size() == 0){
					throw new Exception("Error Reading NEW Degree (" + oldDegree.getNomemestrado() + ")");
				}
				
				Curso degree = (Curso) result.get(0);
				

				// Get the Degree Curricular Plan for the new Degree
				
				criteria = new Criteria();
				criteria.addEqualTo("degreeKey", degree.getIdInternal());
				query = new QueryByCriteria(DegreeCurricularPlan.class,criteria);
				result = (List) broker.getCollectionByQuery(query);		
				
				if (result.size() == 0){
					System.out.println("Error Reading Degree Curricular Plan (" + degree.getNome() + ")");
					throw new Exception("Cannot Read Fenix Degree Curricular Plan");
				}	
				
				DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) result.get(0);			
				
				
				// Check to see if the Curricular Course Already Exists
				
				criteria = new Criteria();
				criteria.addEqualTo("name", curricularCourse2Convert.getNome());
				criteria.addEqualTo("degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());
				query = new QueryByCriteria(CurricularCourse.class,criteria);
				result = (List) broker.getCollectionByQuery(query);		
				
				if (result.size() != 0) {
					System.out.println("- " + curricularCourse2Convert.getNome() + "[" + result.size() + "]");
					broker.commitTransaction();
					continue;
				}
				
				curricularCourse2Write = new CurricularCourse();

				
				// Read The Credits

				curricularCourse2Write.setCredits(curricularCourse2Convert.getCreditos());
				curricularCourse2Write.setMandatory(Boolean.FALSE);
				
				curricularCourse2Write.setCurricularCourseExecutionScope(CurricularCourseExecutionScope.SEMESTRIAL_OBJ);
				curricularCourse2Write.setDegreeCurricularPlan(degreeCurricularPlan);
				curricularCourse2Write.setDepartmentCourse(null);
				curricularCourse2Write.setLabHours(new Double(0.0));
				curricularCourse2Write.setName(curricularCourse2Convert.getNome());
				curricularCourse2Write.setPraticalHours(new Double(0.0));
				curricularCourse2Write.setTheoPratHours(new Double(0.0));
				curricularCourse2Write.setTheoreticalHours(new Double(0.0));
				
				curricularCourse2Write.setType(getType(curricularCourse2Convert));

				// Check if the Curricular Course Exists
				curricularCourse2Write.setCode(curricularCourse2Convert.getSigla());
				
				boolean writableCourse = false;
				int i = 1;
				while(!writableCourse){
					
					criteria = new Criteria();
					criteria.addEqualTo("code", curricularCourse2Write.getCode());
					criteria.addEqualTo("name", curricularCourse2Write.getName());
					criteria.addEqualTo("degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());
					query = new QueryByCriteria(CurricularCourse.class,criteria);
					result = (List) broker.getCollectionByQuery(query);
					
					if (result.size() == 0){
						writableCourse = true;
					} else {
						curricularCourse2Write.setCode(NameUtils.generateCode(curricularCourse2Convert.getNome(), i++));
					}
				}
				
				broker.store(curricularCourse2Write);
				coursesWritten++;
				curricularCourse2Convert.setCodigoCurricularCourse(curricularCourse2Write.getIdInternal());
				broker.store(curricularCourse2Convert);
				broker.commitTransaction();
				
			}
			System.out.println("  Curricular Courses Written : " + coursesWritten);
			System.out.println("  Curricular Courses NOT Written : " + coursesNotWritten);
			System.out.println("  Done !");
		} catch (Exception e){
			System.out.println();
			throw new Exception("Error Migrating Curricular Course " + curricularCourse2Convert.getNome() , e);
			
		}
	}

	private CurricularCourseType getType(Posgrad_disciplina curricularCourse2Convert){
		// Check the Curricular Course type
		
		if (curricularCourse2Convert.getTipo() != null) {
			if (curricularCourse2Convert.getTipo().equalsIgnoreCase("lm")){
				return CurricularCourseType.ML_TYPE_COURSE_OBJ;
			} else if (curricularCourse2Convert.getTipo().equalsIgnoreCase("dm")){
				return CurricularCourseType.DM_TYPE_COURSE_OBJ;
			} else if (curricularCourse2Convert.getTipo().equalsIgnoreCase("p")){
				return CurricularCourseType.P_TYPE_COURSE_OBJ;
			} else if (curricularCourse2Convert.getTipo().equalsIgnoreCase("a")){
				return CurricularCourseType.A_TYPE_COURSE_OBJ;
			}
	    } else if (curricularCourse2Convert.getNome().indexOf("(M)") != -1){
			return CurricularCourseType.M_TYPE_COURSE_OBJ;
		} else if (curricularCourse2Convert.getNome().indexOf("(P)") != -1){
			return CurricularCourseType.P_TYPE_COURSE_OBJ;
		} else if (curricularCourse2Convert.getNome().indexOf("(D/M)") != -1){
			return CurricularCourseType.DM_TYPE_COURSE_OBJ;
		} else if (curricularCourse2Convert.getNome().indexOf("(A)") != -1){
			return CurricularCourseType.A_TYPE_COURSE_OBJ;
		} else if (curricularCourse2Convert.getNome().indexOf("(M/L)") != -1){
			return CurricularCourseType.ML_TYPE_COURSE_OBJ;
		} 
		
		return CurricularCourseType.NORMAL_COURSE_OBJ;
	}



	private List getDisciplinas() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Posgrad_disciplina.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}

	private Boolean checkIfMandatory(Posgrad_disciplina curricularCourse2Convert) {
		
		if (curricularCourse2Convert.getOptativa() == null){
			return Boolean.TRUE;
		} else if (curricularCourse2Convert.getOptativa().equalsIgnoreCase("f")){
			return Boolean.TRUE;
		} else if (curricularCourse2Convert.getOptativa().equalsIgnoreCase("o")){
			return Boolean.FALSE;
		}

		
		
		return Boolean.FALSE;
	}




}
