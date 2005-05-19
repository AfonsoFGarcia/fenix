/*
 * Created on Dec 19, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoResponsibleFor;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 */
public class ResponsibleForValidator {
    /**
     * @author jpvl
     */
    public class InvalidCategory extends FenixServiceException {

    }

    /**
     * @author jpvl
     */
    public class MaxResponsibleForExceed extends FenixServiceException {
        private InfoExecutionCourse infoExecutionCourse;

        private List infoResponsiblefors;

        public MaxResponsibleForExceed(InfoExecutionCourse infoExecutionCourse, List infoResponsiblefors) {
            this.infoResponsiblefors = infoResponsiblefors;
            this.infoExecutionCourse = infoExecutionCourse;
        }

        /**
         * @return Returns the infoExecutionCourse.
         */
        public InfoExecutionCourse getInfoExecutionCourse() {
            return this.infoExecutionCourse;
        }

        /**
         * @return Returns the infoResponsiblefors.
         */
        public List getInfoResponsiblefors() {
            return this.infoResponsiblefors;
        }
    }

    private static ResponsibleForValidator validator = new ResponsibleForValidator();

    public static ResponsibleForValidator getInstance() {
        return validator;
    }

    private final int MAX_RESPONSIBLEFOR_BY_EXECUTION_COURSE = 3;

    private ResponsibleForValidator() {
    }

    public void validateResponsibleForList(ITeacher teacher, IExecutionCourse executionCourse,
            IResponsibleFor responsibleForAdded, IPersistentResponsibleFor responsibleForDAO)
            throws ExcepcaoPersistencia, MaxResponsibleForExceed, InvalidCategory {
        if (!teacher.getCategory().getCanBeExecutionCourseResponsible().booleanValue()) {
            throw new InvalidCategory();
        }
        List responsibleFors = responsibleForDAO.readByExecutionCourse(executionCourse.getIdInternal());

        if ((!responsibleFors.contains(responsibleForAdded))
                && (responsibleFors.size() >= MAX_RESPONSIBLEFOR_BY_EXECUTION_COURSE)) {
            List infoResponsibleFors = (List) CollectionUtils.collect(responsibleFors,
                    new Transformer() {

                        public Object transform(Object input) {
                            IResponsibleFor responsibleFor = (IResponsibleFor) input;
                            InfoResponsibleFor infoResponsibleFor = Cloner
                                    .copyIResponsibleFor2InfoResponsibleFor(responsibleFor);
                            return infoResponsibleFor;
                        }
                    });
            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
            throw new MaxResponsibleForExceed(infoExecutionCourse, infoResponsibleFors);
        }
    }
}