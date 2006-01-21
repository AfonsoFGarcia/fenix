/*
 * Created on Mar 11, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.OutOfPeriodException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class SubmitFinalWorkProposal extends Service {

    public void run(InfoProposal infoProposal) throws FenixServiceException, ExcepcaoPersistencia {
        IPersistentFinalDegreeWork persistentFinalWork = persistentSupport
                .getIPersistentFinalDegreeWork();
        IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
        IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();
        IPersistentBranch persistentBranch = persistentSupport.getIPersistentBranch();

        Integer executionDegreeId = infoProposal.getExecutionDegree().getIdInternal();
        ExecutionDegree executionDegree = (ExecutionDegree) persistentExecutionCourse.readByOID(
                ExecutionDegree.class, executionDegreeId);

        Scheduleing scheduleing = persistentFinalWork.readFinalDegreeWorkScheduleing(executionDegreeId);
        if (scheduleing == null) {
            throw new OutOfPeriodException(null, null, null);
        }

        Proposal proposal = null;
        if (infoProposal.getIdInternal() != null) {
            proposal = (Proposal) persistentFinalWork.readByOID(Proposal.class, infoProposal
                    .getIdInternal());
        }
        if (proposal == null) {
            proposal = DomainFactory.makeProposal();
            int proposalNumber = scheduleing.getCurrentProposalNumber().intValue();
            proposal.setProposalNumber(new Integer(proposalNumber));
            scheduleing.setCurrentProposalNumber(new Integer(proposalNumber + 1));
        }

        proposal.setCompanionName(infoProposal.getCompanionName());
        proposal.setCompanionMail(infoProposal.getCompanionMail());
        proposal.setCompanionPhone(infoProposal.getCompanionPhone());
        proposal.setCompanyAdress(infoProposal.getCompanyAdress());
        proposal.setCompanyName(infoProposal.getCompanyName());

        if (infoProposal.getCoorientator() != null) {
            Integer coorientatorId = infoProposal.getCoorientator().getIdInternal();
            Teacher coorientator = (Teacher) persistentTeacher
                    .readByOID(Teacher.class, coorientatorId);
            proposal.setCoorientator(coorientator);
        } else {
            proposal.setCoorientator(null);
        }

        proposal.setCoorientatorsCreditsPercentage(infoProposal.getCoorientatorsCreditsPercentage());
        proposal.setDegreeType(infoProposal.getDegreeType());
        proposal.setDeliverable(infoProposal.getDeliverable());
        proposal.setDescription(infoProposal.getDescription());

        proposal.setExecutionDegree(executionDegree);
        proposal.setFraming(infoProposal.getFraming());
        proposal.setLocation(infoProposal.getLocation());

        proposal.setMaximumNumberOfGroupElements(infoProposal.getMaximumNumberOfGroupElements());
        proposal.setMinimumNumberOfGroupElements(infoProposal.getMinimumNumberOfGroupElements());
        proposal.setObjectives(infoProposal.getObjectives());
        proposal.setObservations(infoProposal.getObservations());

        Integer orientatorId = infoProposal.getOrientator().getIdInternal();
        Teacher orientator = (Teacher) persistentTeacher.readByOID(Teacher.class, orientatorId);

        proposal.setOrientator(orientator);
        proposal.setOrientatorsCreditsPercentage(infoProposal.getOrientatorsCreditsPercentage());
        proposal.setRequirements(infoProposal.getRequirements());
        proposal.setTitle(infoProposal.getTitle());
        proposal.setUrl(infoProposal.getUrl());

        proposal.getBranches().clear();
        if (infoProposal.getBranches() != null && !infoProposal.getBranches().isEmpty()) {
            for (int i = 0; i < infoProposal.getBranches().size(); i++) {
                InfoBranch infoBranch = (InfoBranch) infoProposal.getBranches().get(i);
                if (infoBranch != null && infoBranch.getIdInternal() != null) {
                    Branch branch = (Branch) persistentBranch.readByOID(Branch.class, infoBranch
                            .getIdInternal());
                    if (branch != null) {
                        proposal.getBranches().add(branch);
                    }
                }
            }
        }

        proposal.setStatus(infoProposal.getStatus());
    }
}
