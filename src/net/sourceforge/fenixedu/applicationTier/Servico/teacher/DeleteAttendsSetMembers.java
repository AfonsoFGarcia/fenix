/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.AttendsSet;
import net.sourceforge.fenixedu.domain.IAttendInAttendsSet;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendInAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 *
 */

public class DeleteAttendsSetMembers implements IServico {

    private static DeleteAttendsSetMembers service = new DeleteAttendsSetMembers();

    /**
     * The singleton access method of this class.
     */
    public static DeleteAttendsSetMembers getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private DeleteAttendsSetMembers() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "DeleteAttendsSetMembers";
    }

    /**
     * Executes the service.
     */

    public boolean run(Integer executionCourseCode, Integer attendsSetCode,
            List studentUsernames) throws FenixServiceException {

        
        IFrequentaPersistente persistentAttend = null;
        IPersistentAttendsSet persistentAttendsSet = null;
        IPersistentAttendInAttendsSet persistentAttendInAttendsSet = null;
        IPersistentStudent persistentStudent = null;
        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
        
        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentAttend = persistentSupport.getIFrequentaPersistente();
            persistentAttendsSet = persistentSupport.getIPersistentAttendsSet();
            persistentAttendInAttendsSet = persistentSupport.getIPersistentAttendInAttendsSet();
            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentStudentGroupAttend = persistentSupport.getIPersistentStudentGroupAttend();

            IAttendsSet attendsSet = (IAttendsSet) persistentAttendsSet
            		.readByOID(AttendsSet.class, attendsSetCode);

            if (attendsSet == null) {
                throw new ExistingServiceException();
            }
            
            IGroupProperties groupProperties = attendsSet.getGroupProperties();
            
            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
            .getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
            .getGroupEnrolmentStrategyInstance(groupProperties);
            
            if(!strategy.checkStudentsUserNamesInAttendsSet(studentUsernames,groupProperties)){
                throw new InvalidSituationServiceException();
            }
            

            Iterator iterator = studentUsernames.iterator();
            while (iterator.hasNext()) {
            	String username = (String)iterator.next();
            	List attendInAttendsSetList = attendsSet.getAttendInAttendsSet();
				Iterator iterAttendInAttendsSet = attendInAttendsSetList.iterator();
				IAttendInAttendsSet attendInAttendsSet=null;
				boolean found1 = false;
            	while(iterAttendInAttendsSet.hasNext() && !found1){
            	 	attendInAttendsSet = (IAttendInAttendsSet)iterAttendInAttendsSet.next();
            	 	if(attendInAttendsSet.getAttend().getAluno().getPerson().getUsername().equals(username)){
            	 		found1= true;
            	 	}
            	 }
                     
           	    IAttends attend = attendInAttendsSet.getAttend();
                
                boolean found = false;
                Iterator iterStudentsGroups = attendsSet.getStudentGroups().iterator();
                while (iterStudentsGroups.hasNext() && !found) {
                        
                	IStudentGroupAttend oldStudentGroupAttend = persistentStudentGroupAttend
														.readBy((IStudentGroup)iterStudentsGroups.next(), attend);
                	if (oldStudentGroupAttend != null) {
                		persistentStudentGroupAttend.delete(oldStudentGroupAttend);
                		found = true;
                	} 
                }                	

                
                attendsSet.removeAttendInAttendsSet(attendInAttendsSet);
                attend.removeAttendInAttendsSet(attendInAttendsSet);
                persistentAttendInAttendsSet.delete(attendInAttendsSet);
                
            }            
            
            

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;
    }
}