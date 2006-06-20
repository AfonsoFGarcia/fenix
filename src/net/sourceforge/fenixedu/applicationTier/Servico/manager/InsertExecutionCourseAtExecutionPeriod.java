/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author lmac1 modified by Fernanda Quit�rio
 */
public class InsertExecutionCourseAtExecutionPeriod extends Service {

    public void run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException, ExcepcaoPersistencia {
        ExecutionCourse executionCourse = new ExecutionCourse();
        ExecutionPeriod executionPeriod = null;
        ExecutionCourse existentExecutionCourse = null;

        executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoExecutionCourse.getInfoExecutionPeriod().getIdInternal());

        if (executionPeriod == null) {
        	throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
        }

        existentExecutionCourse = executionPeriod.getExecutionCourseByInitials(infoExecutionCourse.getSigla());

        if (existentExecutionCourse != null) {
        	throw new ExistingServiceException("A disciplina execu��o com sigla "
        			+ existentExecutionCourse.getSigla() + " e per�odo execu��o "
        			+ executionPeriod.getName() + "-" + executionPeriod.getExecutionYear().getYear());
        }

        executionCourse.setNome(infoExecutionCourse.getNome());
        executionCourse.createForum(executionCourse.getNome(), executionCourse.getNome());
        executionCourse.setExecutionPeriod(executionPeriod);
        executionCourse.setSigla(infoExecutionCourse.getSigla());
        executionCourse.setLabHours(infoExecutionCourse.getLabHours());
        executionCourse.setPraticalHours(infoExecutionCourse.getPraticalHours());
        executionCourse.setTheoPratHours(infoExecutionCourse.getTheoPratHours());
        executionCourse.setTheoreticalHours(infoExecutionCourse.getTheoreticalHours());
        executionCourse.setComment(infoExecutionCourse.getComment());

        executionCourse.createSite();            
	}

}