/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.degree.finalProject;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoObject;
import DataBeans.InfoStudent;
import DataBeans.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import DataBeans.util.Cloner;
import Dominio.Credits;
import Dominio.ExecutionPeriod;
import Dominio.ICredits;
import Dominio.IDomainObject;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.ITeacher;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import ServidorAplicacao.Servico.credits.calcutation.CreditsCalculator;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.credits.IPersistentCredits;
import ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class EditTeacherDegreeFinalProjectStudentByOID extends
        EditDomainObjectService {
    /**
     * @author jpvl
     */
    public class ExecutionPeriodNotFoundException extends FenixServiceException {

        /**
         *  
         */
        public ExecutionPeriodNotFoundException() {
            super();
        }
    }

    /**
     * @author jpvl
     */
    public class StudentNotFoundServiceException extends FenixServiceException {
        public StudentNotFoundServiceException() {
            super();
        }
    }

    /**
     * @author jpvl
     */
    public class StudentPercentageExceed extends FenixServiceException {
        private List infoTeacherDegreeFinalProjectStudentList;

        /**
         * @param infoTeacherDegreeFinalProjectStudentList
         */
        public StudentPercentageExceed(
                List infoTeacherDegreeFinalProjectStudentList) {
            this.infoTeacherDegreeFinalProjectStudentList = infoTeacherDegreeFinalProjectStudentList;
        }

        /**
         * @return Returns the infoTeacherDegreeFinalProjectStudentList.
         */
        public List getInfoTeacherDegreeFinalProjectStudentList() {
            return this.infoTeacherDegreeFinalProjectStudentList;
        }
    }

    private static EditTeacherDegreeFinalProjectStudentByOID service = new EditTeacherDegreeFinalProjectStudentByOID();

    /**
     * The singleton access method of this class.
     */
    public static EditTeacherDegreeFinalProjectStudentByOID getService() {
        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
     */
    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = (InfoTeacherDegreeFinalProjectStudent) infoObject;
        return Cloner
                .copyInfoTeacherDegreeFinalProjectStudent2ITeacherDegreeFinalProjectStudent(infoTeacherDegreeFinalProjectStudent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doBeforeLock(Dominio.IDomainObject,
     *      DataBeans.InfoObject, ServidorPersistente.ISuportePersistente)
     */
    protected void doBeforeLock(IDomainObject domainObjectToLock,
            InfoObject infoObject, ISuportePersistente sp)
            throws FenixServiceException {
        try {
            InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = (InfoTeacherDegreeFinalProjectStudent) infoObject;

            InfoStudent infoStudent = infoTeacherDegreeFinalProjectStudent
                    .getInfoStudent();

            IPersistentStudent studentDAO = sp.getIPersistentStudent();
            IPersistentExecutionPeriod executionPeriodDAO = sp
                    .getIPersistentExecutionPeriod();

            IStudent student = studentDAO.readStudentByNumberAndDegreeType(
                    infoStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

            IExecutionPeriod executionPeriod = new ExecutionPeriod(
                    infoTeacherDegreeFinalProjectStudent
                            .getInfoExecutionPeriod().getIdInternal());
            executionPeriod = (IExecutionPeriod) executionPeriodDAO.readByOID(
                    ExecutionPeriod.class, infoTeacherDegreeFinalProjectStudent
                            .getInfoExecutionPeriod().getIdInternal());

            if (student == null) {
                throw new StudentNotFoundServiceException();
            }
            if (executionPeriod == null) {
                throw new ExecutionPeriodNotFoundException();
            }

            IPersistentTeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudentDAO = sp
                    .getIPersistentTeacherDegreeFinalProjectStudent();
            List teacherDegreeFinalProjectStudentList = teacherDegreeFinalProjectStudentDAO
                    .readByStudentAndExecutionPeriod(student, executionPeriod);

            double requestedPercentage = ((InfoTeacherDegreeFinalProjectStudent) infoObject)
                    .getPercentage().doubleValue();

            Iterator iterator = teacherDegreeFinalProjectStudentList.iterator();
            double percentage = requestedPercentage;
            while (iterator.hasNext()) {
                ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (ITeacherDegreeFinalProjectStudent) iterator
                        .next();

                if (teacherDegreeFinalProjectStudent.getTeacher()
                        .getIdInternal().intValue() != infoTeacherDegreeFinalProjectStudent
                        .getInfoTeacher().getIdInternal().intValue()) {
                    percentage += teacherDegreeFinalProjectStudent
                            .getPercentage().doubleValue();
                }
            }
            if (percentage > 100) {
                List infoTeacherDegreeFinalProjectStudentList = (List) CollectionUtils
                        .collect(teacherDegreeFinalProjectStudentList,
                                new Transformer() {

                                    public Object transform(Object input) {
                                        ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (ITeacherDegreeFinalProjectStudent) input;
                                        InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = Cloner
                                                .copyITeacherDegreeFinalProjectStudent2InfoTeacherDegreeFinalProjectStudent(teacherDegreeFinalProjectStudent);
                                        return infoTeacherDegreeFinalProjectStudent;
                                    }
                                });
                throw new StudentPercentageExceed(
                        infoTeacherDegreeFinalProjectStudentList);
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }
    } /*
       * (non-Javadoc)
       * 
       * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
       */

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentTeacherDegreeFinalProjectStudent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "EditTeacherDegreeFinalProjectStudentByOID";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#readObjectByUnique(Dominio.IDomainObject,
     *      ServidorPersistente.ISuportePersistente)
     */
    protected IDomainObject readObjectByUnique(IDomainObject domainObject,
            ISuportePersistente sp) throws ExcepcaoPersistencia,
            FenixServiceException {
        ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (ITeacherDegreeFinalProjectStudent) domainObject;
        IStudent student = teacherDegreeFinalProjectStudent.getStudent();
        IPersistentStudent studentDAO = sp.getIPersistentStudent();

        student = studentDAO.readStudentByNumberAndDegreeType(student
                .getNumber(), TipoCurso.LICENCIATURA_OBJ);
        if (student == null) {
            throw new StudentNotFoundServiceException();
        }
        teacherDegreeFinalProjectStudent.setStudent(student);

        IPersistentTeacherDegreeFinalProjectStudent teacherDFPStudentDAO = sp
                .getIPersistentTeacherDegreeFinalProjectStudent();

        teacherDegreeFinalProjectStudent = teacherDFPStudentDAO
                .readByUnique(teacherDegreeFinalProjectStudent);

        return teacherDegreeFinalProjectStudent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doAfterLock(Dominio.IDomainObject,
     *      DataBeans.InfoObject, ServidorPersistente.ISuportePersistente)
     */
    protected void doAfterLock(IDomainObject domainObjectLocked,
            InfoObject infoObject, ISuportePersistente sp)
            throws FenixServiceException {
        ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (ITeacherDegreeFinalProjectStudent) domainObjectLocked;

        CreditsCalculator creditsCalculator = CreditsCalculator.getInstance();

        ITeacher teacher = teacherDegreeFinalProjectStudent.getTeacher();
        IExecutionPeriod executionPeriod = teacherDegreeFinalProjectStudent
                .getExecutionPeriod();

        try {
            Double dfpStudentsCredits = creditsCalculator
                    .calcuteDegreeFinalProjectStudentAfterInsert(teacher,
                            executionPeriod, teacherDegreeFinalProjectStudent,
                            sp);
            IPersistentCredits creditsDAO = sp.getIPersistentCredits();
            ICredits credits = creditsDAO.readByTeacherAndExecutionPeriod(
                    teacher, executionPeriod);
            if (credits == null) {
                credits = new Credits();
            }
            creditsDAO.simpleLockWrite(credits);
            credits.setExecutionPeriod(executionPeriod);
            credits.setTeacher(teacher);
            credits.setDegreeFinalProjectStudents(dfpStudentsCredits);

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("Problems on database!", e);
        }
    }

}