/*
 * Created on 14/Mai/2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.credits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoShiftPercentage;
import DataBeans.teacher.credits.InfoShiftProfessorship;
import DataBeans.teacher.professorship.TeacherExecutionCourseProfessorshipShiftsDTO;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.IShiftProfessorship;
import Dominio.ITeacher;
import Dominio.ITurno;
import Dominio.Teacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

/**
 * @author T�nia & Alexandra
 */
public class ReadTeacherExecutionCourseShiftsPercentage implements IServico {
    private static ReadTeacherExecutionCourseShiftsPercentage service = new ReadTeacherExecutionCourseShiftsPercentage();

    /**
     * The singleton access method of this class.
     */
    public static ReadTeacherExecutionCourseShiftsPercentage getService() {
        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadTeacherExecutionCourseProfessorshipShifts";
    }

    public TeacherExecutionCourseProfessorshipShiftsDTO run(
            InfoTeacher infoTeacher, InfoExecutionCourse infoExecutionCourse)
            throws FenixServiceException {

        TeacherExecutionCourseProfessorshipShiftsDTO result = new TeacherExecutionCourseProfessorshipShiftsDTO();

        List infoShiftPercentageList = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = readExecutionCourse(
                    infoExecutionCourse, sp);
            ITeacher teacher = readTeacher(infoTeacher, sp);

            result.setInfoExecutionCourse((InfoExecutionCourse) Cloner
                    .get(executionCourse));
            result.setInfoTeacher(Cloner.copyITeacher2InfoTeacher(teacher));

            ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

            List executionCourseShiftsList = null;

            executionCourseShiftsList = shiftDAO
                    .readByExecutionCourseID(executionCourse.getIdInternal());

            Iterator iterator = executionCourseShiftsList.iterator();
            while (iterator.hasNext()) {
                ITurno shift = (ITurno) iterator.next();

                InfoShiftPercentage infoShiftPercentage = new InfoShiftPercentage();
                infoShiftPercentage.setShift((InfoShift) Cloner.get(shift));
                double availablePercentage = 100;
                InfoShiftProfessorship infoShiftProfessorship = null;

                Iterator iter = shift.getAssociatedShiftProfessorship()
                        .iterator();
                while (iter.hasNext()) {
                    IShiftProfessorship shiftProfessorship = (IShiftProfessorship) iter
                            .next();
                    /**
                     * if shift's type is LABORATORIAL the shift professorship
                     * percentage can exceed 100%
                     */
                    if ((shift.getTipo().getTipo().intValue() != TipoAula.LABORATORIAL)
                            && (!shiftProfessorship.getProfessorship()
                                    .getTeacher().equals(teacher))) {
                        availablePercentage -= shiftProfessorship
                                .getPercentage().doubleValue();
                    }
                    infoShiftProfessorship = Cloner
                            .copyIShiftProfessorship2InfoShiftProfessorship(shiftProfessorship);
                    infoShiftPercentage
                            .addInfoShiftProfessorship(infoShiftProfessorship);
                }

                List infoLessons = (List) CollectionUtils.collect(shift
                        .getAssociatedLessons(), new Transformer() {
                    public Object transform(Object input) {
                        IAula lesson = (IAula) input;
                        return Cloner.copyILesson2InfoLesson(lesson);
                    }
                });
                infoShiftPercentage.setInfoLessons(infoLessons);

                infoShiftPercentage.setAvailablePercentage(new Double(
                        availablePercentage));

                infoShiftPercentageList.add(infoShiftPercentage);
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
        result.setInfoShiftPercentageList(infoShiftPercentageList);
        return result;
    }

    private ITeacher readTeacher(InfoTeacher infoTeacher, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

        ITeacher teacher = (ITeacher) teacherDAO.readByOID(Teacher.class,
                infoTeacher.getIdInternal());
        return teacher;
    }

    private IExecutionCourse readExecutionCourse(
            InfoExecutionCourse infoExecutionCourse, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentExecutionCourse executionCourseDAO = sp
                .getIPersistentExecutionCourse();

        IExecutionCourse executionCourse = (IExecutionCourse) executionCourseDAO
                .readByOID(ExecutionCourse.class, infoExecutionCourse
                        .getIdInternal());
        return executionCourse;
    }
}