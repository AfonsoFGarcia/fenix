/*
 * Created on 05/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico.teachersBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithAll;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> 19/Dez/2003
 *  
 */
public class ReadProfessorshipsAndResponsibilitiesByDepartmentAndExecutionPeriod extends Service {

    public List run(Integer departmentId, Integer executionYearId, Integer semester, Integer teacherType)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentObject persistentObject = persistentSupport.getIPersistentObject();

        //Execution Year
        ExecutionYear executionYear = null;
        if (executionYearId != null) {

            executionYear = (ExecutionYear) persistentObject.readByOID(ExecutionYear.class,
                    executionYearId);
        }

        //Execution period
        IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
        ExecutionPeriod executionPeriod = persistentExecutionPeriod.readBySemesterAndExecutionYear(
                semester, executionYear.getYear());

        //Departement

        Department department = (Department) persistentObject
                .readByOID(Department.class, departmentId);
        
        List teachers = department.getCurrentTeachers();

        Iterator iter = teachers.iterator();
        IPersistentProfessorship persistentProfessorship = persistentSupport.getIPersistentProfessorship();
      
        List professorships = new ArrayList();
        List responsibleFors = new ArrayList();
        while (iter.hasNext()) {
            Teacher teacher = (Teacher) iter.next();
            List teacherProfessorships = null;
            if (executionYear == null) {
                teacherProfessorships = persistentProfessorship.readByTeacher(teacher.getIdInternal());
            } else {
                if (semester.intValue() == 0) {

                    teacherProfessorships = persistentProfessorship.readByTeacherAndExecutionYear(
                            teacher.getIdInternal(), executionYear.getIdInternal());
                } else {
                    teacherProfessorships = persistentProfessorship.readByTeacherAndExecutionPeriod(
                            teacher.getIdInternal(), executionPeriod.getIdInternal());
                }
            }
            if (teacherProfessorships != null) {
                professorships.addAll(teacherProfessorships);
            }

            List teacherResponsibleFors;
            List<Professorship> teacherResponsibleForsAux = null;
            
            if (executionYear == null) {
                teacherResponsibleFors = teacher.responsibleFors();
            } else {
                teacherResponsibleForsAux = teacher.responsibleFors();
				teacherResponsibleFors = new ArrayList<Professorship>();
                for(Professorship professorship : teacherResponsibleForsAux){
                    if(professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear().equals(executionYear))
                        teacherResponsibleFors.add(professorship);
                }         
            }
            if (teacherResponsibleFors != null) {
                responsibleFors.addAll(teacherResponsibleFors);
            }
        }

        List detailedProfessorships = getDetailedProfessorships(professorships, responsibleFors, persistentSupport,
                teacherType);

        //Cleaning out possible null elements inside the list
        Iterator itera = detailedProfessorships.iterator();
        while (itera.hasNext()) {
            Object dp = itera.next();
            if (dp == null) {
                itera.remove();
            }
        }

        Collections.sort(detailedProfessorships, new Comparator() {

            public int compare(Object o1, Object o2) {

                DetailedProfessorship detailedProfessorship1 = (DetailedProfessorship) o1;
                DetailedProfessorship detailedProfessorship2 = (DetailedProfessorship) o2;
                int result = detailedProfessorship1.getInfoProfessorship().getInfoExecutionCourse()
                        .getIdInternal().intValue()
                        - detailedProfessorship2.getInfoProfessorship().getInfoExecutionCourse()
                                .getIdInternal().intValue();
                if (result == 0
                        && (detailedProfessorship1.getResponsibleFor().booleanValue() || detailedProfessorship2
                                .getResponsibleFor().booleanValue())) {
                    if (detailedProfessorship1.getResponsibleFor().booleanValue()) {
                        return -1;
                    }
                    if (detailedProfessorship2.getResponsibleFor().booleanValue()) {
                        return 1;
                    }
                }

                return result;
            }

        });

        List result = new ArrayList();
        iter = detailedProfessorships.iterator();
        List temp = new ArrayList();
        while (iter.hasNext()) {
            DetailedProfessorship detailedProfessorship = (DetailedProfessorship) iter.next();
            if (temp.isEmpty()
                    || ((DetailedProfessorship) temp.get(temp.size() - 1)).getInfoProfessorship()
                            .getInfoExecutionCourse().equals(
                                    detailedProfessorship.getInfoProfessorship()
                                            .getInfoExecutionCourse())) {
                temp.add(detailedProfessorship);
            } else {
                result.add(temp);
                temp = new ArrayList();
                temp.add(detailedProfessorship);
            }
        }
        if (!temp.isEmpty()) {
            result.add(temp);
        }
        return result;
    }

    protected List getDetailedProfessorships(List professorships, final List responsibleFors,
            ISuportePersistente persistentSupport, final Integer teacherType) {

        List detailedProfessorshipList = (List) CollectionUtils.collect(professorships,
                new Transformer() {

                    public Object transform(Object input) {

                        Professorship professorship = (Professorship) input;

                        InfoProfessorship infoProfessorShip = InfoProfessorshipWithAll
                                .newInfoFromDomain(professorship);

                        List executionCourseCurricularCoursesList = getInfoCurricularCourses(professorship
                                .getExecutionCourse());

                        DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

                        Boolean isResponsible = Boolean.valueOf(professorship.getResponsibleFor());

                        if ((teacherType.intValue() == 1) && (!isResponsible.booleanValue())) {
                            return null;
                        }

                        detailedProfessorship.setResponsibleFor(isResponsible);

                        detailedProfessorship.setInfoProfessorship(infoProfessorShip);
                        detailedProfessorship
                                .setExecutionCourseCurricularCoursesList(executionCourseCurricularCoursesList);

                        return detailedProfessorship;
                    }

                    private List getInfoCurricularCourses(ExecutionCourse executionCourse) {

                        List infoCurricularCourses = (List) CollectionUtils.collect(executionCourse
                                .getAssociatedCurricularCourses(), new Transformer() {

                            public Object transform(Object input) {

                                CurricularCourse curricularCourse = (CurricularCourse) input;

                                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                                        .newInfoFromDomain(curricularCourse);
                                return infoCurricularCourse;
                            }
                        });
                        return infoCurricularCourses;
                    }
                });

        return detailedProfessorshipList;
    }

}