/*
 * Created on 2004/03/13
 *  
 */
package ServidorAplicacao.Servico.teacher.finalDegreeWork;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.finalDegreeWork.FinalDegreeWorkProposalHeader;
import Dominio.finalDegreeWork.IProposal;
import Dominio.finalDegreeWork.IScheduleing;
import Dominio.finalDegreeWork.Proposal;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *  
 */
public class ReadFinalDegreeWorkProposalHeadersByTeacher implements IService
{

	public ReadFinalDegreeWorkProposalHeadersByTeacher()
	{
		super();
	}

	public List run(Integer teacherOID) throws FenixServiceException
	{
		List finalDegreeWorkProposalHeaders = new ArrayList();

		try
		{
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentFinalDegreeWork persistentFinalDegreeWork =
				persistentSupport.getIPersistentFinalDegreeWork();

			List finalDegreeWorkProposals =
				persistentFinalDegreeWork.readFinalDegreeWorkProposalsByTeacher(teacherOID);

			if (finalDegreeWorkProposals != null)
			{
				finalDegreeWorkProposalHeaders = new ArrayList();
				for (int i = 0; i < finalDegreeWorkProposals.size(); i++)
				{
					IProposal proposal = (Proposal) finalDegreeWorkProposals.get(i);

					if (proposal != null)
					{
						FinalDegreeWorkProposalHeader finalDegreeWorkProposalHeader =
							new FinalDegreeWorkProposalHeader();

						finalDegreeWorkProposalHeader.setIdInternal(proposal.getIdInternal());
						finalDegreeWorkProposalHeader.setTitle(proposal.getTitle());
						finalDegreeWorkProposalHeader.setProposalNumber(proposal.getProposalNumber());
						if (proposal.getOrientator() != null)
						{
							finalDegreeWorkProposalHeader.setOrientatorOID(
								proposal.getOrientator().getIdInternal());
							finalDegreeWorkProposalHeader.setOrientatorName(
								proposal.getOrientator().getPerson().getNome());
						}
						if (proposal.getCoorientator() != null)
						{
							finalDegreeWorkProposalHeader.setCoorientatorOID(
								proposal.getCoorientator().getIdInternal());
							finalDegreeWorkProposalHeader.setCoorientatorName(
								proposal.getCoorientator().getPerson().getNome());
						}
						finalDegreeWorkProposalHeader.setCompanyLink(proposal.getCompanionName());
						finalDegreeWorkProposalHeader.setDegreeCode(
							proposal.getExecutionDegree().getCurricularPlan().getDegree().getSigla());

						IScheduleing scheduleing =
							persistentFinalDegreeWork.readFinalDegreeWorkScheduleing(
								proposal.getExecutionDegree().getIdInternal());
						if (scheduleing != null
							&& scheduleing.getStartOfProposalPeriod() != null
							&& scheduleing.getEndOfProposalPeriod() != null
							&& scheduleing.getStartOfProposalPeriod().before(
								Calendar.getInstance().getTime())
							&& scheduleing.getEndOfProposalPeriod().after(
								Calendar.getInstance().getTime()))
						{
							finalDegreeWorkProposalHeader.setEditable(new Boolean(true));
						}
						else
						{
							finalDegreeWorkProposalHeader.setEditable(new Boolean(false));
						}	

						finalDegreeWorkProposalHeaders.add(finalDegreeWorkProposalHeader);
					}
				}
			}
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}

		return finalDegreeWorkProposalHeaders;
	}
}
