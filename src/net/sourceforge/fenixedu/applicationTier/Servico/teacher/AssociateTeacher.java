package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quit�rio
 */
public class AssociateTeacher implements IService {

    /**
     * Executes the service.
     * @throws ExcepcaoPersistencia 
     */
    public boolean run(Integer infoExecutionCourseCode, Integer teacherNumber)
            throws FenixServiceException, ExcepcaoPersistencia {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            ITeacher iTeacher = persistentTeacher.readByNumber(teacherNumber);
            if (iTeacher == null) {
                throw new InvalidArgumentsServiceException();
            }

            IExecutionCourse iExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, infoExecutionCourseCode);

            if (lectures(iTeacher, iExecutionCourse.getProfessorships())) {
                throw new ExistingServiceException();
            }

            Professorship.create(false, iExecutionCourse, iTeacher, null);
            
        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        }
        return true;
    }

    protected boolean lectures(final ITeacher teacher, final List professorships) {
        return CollectionUtils.find(professorships, new Predicate() {

            public boolean evaluate(Object arg0) {
                IProfessorship professorship = (IProfessorship) arg0;
                return professorship.getTeacher().getIdInternal().equals(teacher.getIdInternal());
            }}) != null;
    }
}