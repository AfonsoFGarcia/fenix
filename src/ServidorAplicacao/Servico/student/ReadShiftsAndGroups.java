/*
 * Created on 28/Ago/2003
 *  
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import DataBeans.ISiteComponent;
import DataBeans.InfoShift;
import DataBeans.InfoSiteGroupsByShift;
import DataBeans.InfoSiteShift;
import DataBeans.InfoSiteShiftsAndGroups;
import DataBeans.InfoSiteStudentGroup;
import DataBeans.InfoStudentGroup;
import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */
public class ReadShiftsAndGroups implements IServico {

    private static ReadShiftsAndGroups service = new ReadShiftsAndGroups();

    /**
     * The singleton access method of this class.
     */
    public static ReadShiftsAndGroups getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private ReadShiftsAndGroups() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "ReadShiftsAndGroups";
    }

    /**
     * Executes the service.
     */
    public ISiteComponent run(Integer groupPropertiesCode)
            throws FenixServiceException {

        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = new InfoSiteShiftsAndGroups();
        List infoSiteShiftsAndGroupsList = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            IPersistentStudentGroup persistentStudentGroup = sp
                    .getIPersistentStudentGroup();

            IGroupProperties groupProperties = (IGroupProperties) sp
                    .getIPersistentGroupProperties().readByOID(
                            GroupProperties.class, groupPropertiesCode);


            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
			.getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
			.getGroupEnrolmentStrategyInstance(groupProperties);
            
            if(strategy.checkHasShift(groupProperties)){
            	infoSiteShiftsAndGroupsList = new ArrayList();
            Iterator iterExecutionCourses = groupProperties.getExecutionCourses().iterator();
            List allShifts = new ArrayList();
            List allShiftsAux = new ArrayList();
            while (iterExecutionCourses.hasNext()){
                allShiftsAux = persistentShift.readByExecutionCourseAndType(
                (IExecutionCourse)iterExecutionCourses.next(), groupProperties.getShiftType().getTipo());
                allShifts.addAll(allShiftsAux);
            }
            List allStudentsGroup = groupProperties.getAttendsSet().getStudentGroupsWithShift();
            if (allStudentsGroup.size() != 0) {

                Iterator iterator = allStudentsGroup.iterator();
                while (iterator.hasNext()) {
                    ITurno shift = ((IStudentGroup) iterator.next()).getShift();
                    if (!allShifts.contains(shift)) {
                        allShifts.add(shift);

                    }
                }
            }

            if (allShifts.size() != 0) {
                Iterator iter = allShifts.iterator();
                infoSiteShiftsAndGroupsList = new ArrayList();
                InfoSiteGroupsByShift infoSiteGroupsByShift = null;
                InfoSiteShift infoSiteShift = null;

                while (iter.hasNext()) {

                    ITurno shift = (ITurno) iter.next();
                    List allStudentGroups = persistentStudentGroup
                            .readAllStudentGroupByAttendsSetAndShift(
                                    groupProperties.getAttendsSet(), shift);

                    infoSiteShift = new InfoSiteShift();
                    infoSiteShift.setInfoShift((InfoShift) Cloner.get(shift));
                    
                    List infoLessons = infoSiteShift.getInfoShift().getInfoLessons();
                    
                    ComparatorChain chainComparator = new ComparatorChain();
                    chainComparator.addComparator(new BeanComparator(
                          "diaSemana.diaSemana"));
                    chainComparator.addComparator(new BeanComparator(
                           "inicio"));
                    chainComparator.addComparator(new BeanComparator(
                    		  "fim"));
                    chainComparator.addComparator(new BeanComparator(
                    		"infoSala.nome"));
                   
                    Collections.sort(infoLessons, chainComparator);

                    if (groupProperties.getGroupMaximumNumber() != null) {

                        int vagas = groupProperties.getGroupMaximumNumber()
                                .intValue()
                                - allStudentGroups.size();
                        //if (vagas >= 0)
                        infoSiteShift.setNrOfGroups(new Integer(vagas));
                        //else
                        //	infoSiteShift.setNrOfGroups(new Integer(0));
                    } else
                        infoSiteShift.setNrOfGroups("Sem limite");

                    infoSiteGroupsByShift = new InfoSiteGroupsByShift();
                    infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);

                    List infoSiteStudentGroupsList = null;
                    if (allStudentGroups.size() != 0) {
                        infoSiteStudentGroupsList = new ArrayList();
                        Iterator iterGroups = allStudentGroups.iterator();

                        while (iterGroups.hasNext()) {
                            InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
                            InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
                            infoStudentGroup = Cloner
                                    .copyIStudentGroup2InfoStudentGroup((IStudentGroup) iterGroups
                                            .next());
                            infoSiteStudentGroup
                                    .setInfoStudentGroup(infoStudentGroup);
                            infoSiteStudentGroupsList.add(infoSiteStudentGroup);

                        }
                        Collections.sort(infoSiteStudentGroupsList,
                                new BeanComparator(
                                        "infoStudentGroup.groupNumber"));

                    }

                    infoSiteGroupsByShift
                            .setInfoSiteStudentGroupsList(infoSiteStudentGroupsList);

                    infoSiteShiftsAndGroupsList.add(infoSiteGroupsByShift);
                }
                /* Sort the list of shifts */

                ComparatorChain chainComparator = new ComparatorChain();
                chainComparator.addComparator(new BeanComparator(
                        "infoSiteShift.infoShift.tipo"));
                chainComparator.addComparator(new BeanComparator(
                        "infoSiteShift.infoShift.nome"));

                Collections.sort(infoSiteShiftsAndGroupsList, chainComparator);
            }
                
                if(!groupProperties.getAttendsSet().getStudentGroupsWithoutShift().isEmpty()){
                   	InfoSiteGroupsByShift infoSiteGroupsByShift = null;
                   	InfoSiteShift infoSiteShift = new InfoSiteShift();

                   infoSiteGroupsByShift = new InfoSiteGroupsByShift();                   
                   List allStudentGroups = groupProperties.getAttendsSet().getStudentGroupsWithoutShift();

                   if (groupProperties.getGroupMaximumNumber() != null) {

       				int vagas = 
       					groupProperties.getGroupMaximumNumber().intValue()- allStudentGroups.size();
       				infoSiteShift.setNrOfGroups(new Integer(vagas));

       			} else
       			{
       				infoSiteShift.setNrOfGroups("Sem limite");
       			}                
                   
                   infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);
                   
                   
                   List infoSiteStudentGroupsList = null;
                   if (allStudentGroups.size() != 0) {
                   	infoSiteStudentGroupsList = new ArrayList();
                   	Iterator iterGroups = allStudentGroups.iterator();

                    while (iterGroups.hasNext()) {
                        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
                        InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
                        infoStudentGroup = InfoStudentGroup.newInfoFromDomain((IStudentGroup) iterGroups.next());
                        infoSiteStudentGroup
                                .setInfoStudentGroup(infoStudentGroup);
                        infoSiteStudentGroupsList.add(infoSiteStudentGroup);
                    }
                   }
                   
                       
                   infoSiteGroupsByShift
    			   .setInfoSiteStudentGroupsList(infoSiteStudentGroupsList);

                   infoSiteShiftsAndGroupsList.add(infoSiteGroupsByShift);
                   
                	}
            }
            else{
            	
            	 infoSiteShiftsAndGroupsList = new ArrayList();
                InfoSiteGroupsByShift infoSiteGroupsByShift = null;
                InfoSiteShift infoSiteShift = new InfoSiteShift();

                infoSiteGroupsByShift = new InfoSiteGroupsByShift();
                    
                List allStudentGroups = groupProperties.getAttendsSet().getStudentGroupsWithoutShift();

                if (groupProperties.getGroupMaximumNumber() != null) {

    				int vagas = 
    					groupProperties.getGroupMaximumNumber().intValue()- allStudentGroups.size();
    				infoSiteShift.setNrOfGroups(new Integer(vagas));

    			} else
    			{
    				infoSiteShift.setNrOfGroups("Sem limite");
    			}                
                
                infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);
                
                    List infoSiteStudentGroupsList = null;
                    if (allStudentGroups.size() != 0) {
                        infoSiteStudentGroupsList = new ArrayList();
                        Iterator iterGroups = allStudentGroups.iterator();

                        while (iterGroups.hasNext()) {
                            InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
                            InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
                            infoStudentGroup = InfoStudentGroup.newInfoFromDomain((IStudentGroup) iterGroups.next());
                            infoSiteStudentGroup
                                    .setInfoStudentGroup(infoStudentGroup);
                            infoSiteStudentGroupsList.add(infoSiteStudentGroup);

                        }
                    }
                    
                    infoSiteGroupsByShift
                    .setInfoSiteStudentGroupsList(infoSiteStudentGroupsList);

                    infoSiteShiftsAndGroupsList.add(infoSiteGroupsByShift);
            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(
                    "error.impossibleReadShiftsAndGroups");
        }
        infoSiteShiftsAndGroups
                .setInfoSiteGroupsByShiftList(infoSiteShiftsAndGroupsList);

        return infoSiteShiftsAndGroups;
    }
}