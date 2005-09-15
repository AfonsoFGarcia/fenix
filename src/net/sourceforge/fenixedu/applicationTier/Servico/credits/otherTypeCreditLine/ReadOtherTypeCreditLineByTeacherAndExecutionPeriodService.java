/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.otherTypeCreditLine;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPersonAndCategory;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoOtherTypeCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.credits.TeacherOtherTypeCreditLineDTO;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.IOtherTypeCreditLine;
import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentOtherTypeCreditLine;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class ReadOtherTypeCreditLineByTeacherAndExecutionPeriodService implements IService {

    /**
     * @author jpvl
     */
    public class TeacherNotFound extends FenixServiceException {

    }

    /**
     * @author jpvl
     */
    public class ExecutionPeriodNotFound extends FenixServiceException {

    }

    public TeacherOtherTypeCreditLineDTO run(Integer teacherId, Integer executionPeriodId)
            throws FenixServiceException {
        TeacherOtherTypeCreditLineDTO teacherOtherTypeCreditLineDTO = new TeacherOtherTypeCreditLineDTO();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentOtherTypeCreditLine otherTypeCreditLineDAO = sp
                    .getIPersistentOtherTypeCreditLine();

            ITeacher teacher = (ITeacher) teacherDAO.readByOID(Teacher.class, teacherId);

            if (teacher == null) {
                throw new TeacherNotFound();
            }

            IExecutionPeriod executionPeriod = null;

            if (executionPeriodId == null || executionPeriodId.intValue() == 0) {
                executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
            } else {
                executionPeriod = (IExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class,
                        executionPeriodId);
            }

            if (executionPeriod == null) {
                throw new ExecutionPeriodNotFound();
            }

            List otherTypesList = otherTypeCreditLineDAO.readByTeacherAndExecutionPeriod(teacher.getIdInternal(),
                    executionPeriod.getIdInternal());

            List infoOtherTypesList = (List) CollectionUtils.collect(otherTypesList, new Transformer() {

                public Object transform(Object input) {
                    IOtherTypeCreditLine otherTypeCreditLine = (IOtherTypeCreditLine) input;
                    InfoOtherTypeCreditLine infoOtherTypeCreditLine = InfoOtherTypeCreditLine.newInfoFromDomain(otherTypeCreditLine);
                    return infoOtherTypeCreditLine;
                }
            });

            teacherOtherTypeCreditLineDTO.setCreditLines(infoOtherTypesList);
            teacherOtherTypeCreditLineDTO.setInfoExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));
            teacherOtherTypeCreditLineDTO.setInfoTeacher(InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher));
            return teacherOtherTypeCreditLineDTO;
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("Problems with database!");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return OtherTypeCreditLine.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentOtherTypeCreditLine();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
     */
    protected InfoObject clone2InfoObject(IDomainObject domainObject) {
        return InfoOtherTypeCreditLine.newInfoFromDomain(((IOtherTypeCreditLine) domainObject));
    }

}