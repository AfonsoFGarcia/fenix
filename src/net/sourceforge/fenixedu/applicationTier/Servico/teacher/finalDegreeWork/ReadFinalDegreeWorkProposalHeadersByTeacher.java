/*
 * Created on 2004/03/13
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupStudent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkProposalHeadersByTeacher extends Service {

	public List run(Integer teacherOID) throws FenixServiceException, ExcepcaoPersistencia {
		List finalDegreeWorkProposalHeaders = new ArrayList();

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
				.getIPersistentFinalDegreeWork();

		List finalDegreeWorkProposals = persistentFinalDegreeWork
				.readFinalDegreeWorkProposalsByTeacher(teacherOID);

		if (finalDegreeWorkProposals != null) {
			finalDegreeWorkProposalHeaders = new ArrayList();
			for (int i = 0; i < finalDegreeWorkProposals.size(); i++) {
				Proposal proposal = (Proposal) finalDegreeWorkProposals.get(i);

				if (proposal != null) {
					FinalDegreeWorkProposalHeader finalDegreeWorkProposalHeader = new FinalDegreeWorkProposalHeader();

					finalDegreeWorkProposalHeader.setIdInternal(proposal.getIdInternal());
					finalDegreeWorkProposalHeader.setExecutionDegreeOID(proposal.getExecutionDegree()
							.getIdInternal());
					finalDegreeWorkProposalHeader.setTitle(proposal.getTitle());
					finalDegreeWorkProposalHeader.setExecutionYear(proposal.getExecutionDegree()
							.getExecutionYear().getYear());
					finalDegreeWorkProposalHeader.setProposalNumber(proposal.getProposalNumber());
					if (proposal.getOrientator() != null) {
						finalDegreeWorkProposalHeader.setOrientatorOID(proposal.getOrientator()
								.getIdInternal());
						finalDegreeWorkProposalHeader.setOrientatorName(proposal.getOrientator()
								.getPerson().getNome());
					}
					if (proposal.getCoorientator() != null) {
						finalDegreeWorkProposalHeader.setCoorientatorOID(proposal.getCoorientator()
								.getIdInternal());
						finalDegreeWorkProposalHeader.setCoorientatorName(proposal.getCoorientator()
								.getPerson().getNome());
					}
					finalDegreeWorkProposalHeader.setCompanyLink(proposal.getCompanionName());
					finalDegreeWorkProposalHeader.setDegreeCode(proposal.getExecutionDegree()
							.getDegreeCurricularPlan().getDegree().getSigla());

					Scheduleing scheduleing = persistentFinalDegreeWork
							.readFinalDegreeWorkScheduleing(proposal.getExecutionDegree()
									.getIdInternal());
					if (scheduleing != null
							&& scheduleing.getStartOfProposalPeriod() != null
							&& scheduleing.getEndOfProposalPeriod() != null
							&& scheduleing.getStartOfProposalPeriod().before(
									Calendar.getInstance().getTime())
							&& scheduleing.getEndOfProposalPeriod().after(
									Calendar.getInstance().getTime())) {
						finalDegreeWorkProposalHeader.setEditable(new Boolean(true));
					} else {
						finalDegreeWorkProposalHeader.setEditable(new Boolean(false));
					}

					if (proposal.getGroupProposals() != null && !proposal.getGroupProposals().isEmpty()) {
						finalDegreeWorkProposalHeader.setGroupProposals(new ArrayList());
						for (int j = 0; j < proposal.getGroupProposals().size(); j++) {
							GroupProposal groupProposal = proposal.getGroupProposals().get(j);
							if (groupProposal != null) {
								InfoGroupProposal infoGroupProposal = new InfoGroupProposal();
								infoGroupProposal.setIdInternal(groupProposal.getIdInternal());
								infoGroupProposal.setOrderOfPreference(groupProposal
										.getOrderOfPreference());
								Group group = groupProposal.getFinalDegreeDegreeWorkGroup();
								if (group != null) {
									InfoGroup infoGroup = new InfoGroup();
									infoGroup.setIdInternal(group.getIdInternal());
									if (group.getGroupStudents() != null) {
										infoGroup.setGroupStudents(new ArrayList());
										for (int k = 0; k < group.getGroupStudents().size(); k++) {
											GroupStudent groupStudent = group.getGroupStudents().get(k);

											if (groupStudent != null) {
												InfoGroupStudent infoGroupStudent = new InfoGroupStudent();
												infoGroupStudent.setIdInternal(groupStudent
														.getIdInternal());
												infoGroupStudent
														.setFinalDegreeDegreeWorkGroup(infoGroup);

												Student student = groupStudent.getStudent();
												if (student != null) {
													InfoStudent infoStudent = new InfoStudent();
													infoStudent.setIdInternal(student.getIdInternal());
													infoStudent.setNumber(student.getNumber());
													Person person = student.getPerson();
													if (person != null) {
														InfoPerson infoPerson = new InfoPerson();
														infoPerson.setIdInternal(person.getIdInternal());
														infoPerson.setUsername(person.getUsername());
														infoPerson.setNome(person.getNome());
														infoPerson.setEmail(person.getEmail());
														infoPerson.setTelefone(person.getTelefone());
														infoStudent.setInfoPerson(infoPerson);
													}
													infoGroupStudent.setStudent(infoStudent);
												}
												infoGroup.getGroupStudents().add(infoGroupStudent);
											}
										}
									}
									infoGroupProposal.setInfoGroup(infoGroup);

									if (proposal.getGroupAttributedByTeacher() != null
											&& proposal.getGroupAttributedByTeacher().getIdInternal()
													.equals(group.getIdInternal())) {
										finalDegreeWorkProposalHeader
												.setGroupAttributedByTeacher(infoGroup);
									}

									finalDegreeWorkProposalHeader.getGroupProposals().add(
											infoGroupProposal);
								}
							}
						}
					}

					finalDegreeWorkProposalHeaders.add(finalDegreeWorkProposalHeader);
				}
			}
		}

		return finalDegreeWorkProposalHeaders;
	}
}