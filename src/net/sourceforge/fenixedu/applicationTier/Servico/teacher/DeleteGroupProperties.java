/*
 * Created on 2/Abr/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.AttendsSet;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IAttendInAttendsSet;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendInAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author T�nia Pous�o
 *  
 */
public class DeleteGroupProperties implements IService
{

	public DeleteGroupProperties()
	{

	}

	public Boolean run(Integer executionCourseId, Integer groupPropertiesId)
			throws FenixServiceException
	{

		Boolean result = Boolean.FALSE;

		if (groupPropertiesId == null)
		{
			return result;
		}

		
		try
		{

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentGroupProperties persistentGroupProperties = sp.getIPersistentGroupProperties();
			IPersistentGroupPropertiesExecutionCourse persistentGroupPropertiesExecutionCourse = sp.getIPersistentGroupPropertiesExecutionCourse();
			IPersistentAttendsSet persistentAttendsSet = sp.getIPersistentAttendsSet();
			IPersistentAttendInAttendsSet persistentAttendInAttendsSet = sp.getIPersistentAttendInAttendsSet();

			IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties.readByOID(GroupProperties.class,
			        groupPropertiesId);

			if (groupProperties == null) {
                throw new ExistingServiceException();
            }
			
			if(!groupProperties.getAttendsSet().getStudentGroups().isEmpty()){
				throw new InvalidSituationServiceException();
			} 
			
			IAttendsSet attendsSet = groupProperties.getAttendsSet();
			Iterator iterAttendInAttendsSet = attendsSet.getAttendInAttendsSet().iterator();
			while(iterAttendInAttendsSet.hasNext()){
				IAttendInAttendsSet attendInAttendsSet = (IAttendInAttendsSet)iterAttendInAttendsSet.next();
				IAttends frequenta = attendInAttendsSet.getAttend();
				frequenta.removeAttendInAttendsSet(attendInAttendsSet);
				persistentAttendInAttendsSet.delete(attendInAttendsSet);
			}
			
			Iterator iterGroupPropertiesExecutionCourse = groupProperties.getGroupPropertiesExecutionCourse().iterator();
			while(iterGroupPropertiesExecutionCourse.hasNext()){
				IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = 
					(IGroupPropertiesExecutionCourse)(iterGroupPropertiesExecutionCourse.next());
				IExecutionCourse executionCourse = groupPropertiesExecutionCourse.getExecutionCourse();
				executionCourse.removeGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);
				persistentGroupPropertiesExecutionCourse.delete(groupPropertiesExecutionCourse);
				
			}
				persistentAttendsSet.deleteByOID(AttendsSet.class, groupProperties.getAttendsSet().getIdInternal());
				persistentGroupProperties.deleteByOID(GroupProperties.class, groupPropertiesId);
			

			result = Boolean.TRUE;
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("error.groupProperties.delete");
		}

		return result;
	}
}
