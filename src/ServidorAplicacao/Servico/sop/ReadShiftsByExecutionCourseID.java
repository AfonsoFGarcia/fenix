
package ServidorAplicacao.Servico.sop;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourseOccupancy;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.NumberUtils;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class ReadShiftsByExecutionCourseID implements IServico {

	private static ReadShiftsByExecutionCourseID _servico = new ReadShiftsByExecutionCourseID();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadShiftsByExecutionCourseID getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadShiftsByExecutionCourseID() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadShiftsByExecutionCourseID";
	}

	public InfoExecutionCourseOccupancy run(Integer executionCourseID) throws FenixServiceException {

		InfoExecutionCourseOccupancy infoExecutionCourseOccupancy = new InfoExecutionCourseOccupancy();
		infoExecutionCourseOccupancy.setInfoShifts(new ArrayList());
		
		
		try {
			SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
			
			IDisciplinaExecucao executionCourseTemp = new DisciplinaExecucao();
			executionCourseTemp.setIdInternal(executionCourseID);
			
			
			IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
			executionCourse = (IDisciplinaExecucao) sp.getIDisciplinaExecucaoPersistente().readByOId(executionCourseTemp, false);
	
	
			List shifts = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);
		
		
			infoExecutionCourseOccupancy.setInfoExecutionCourse(Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse));
			
			Iterator iterator = shifts.iterator();
			while(iterator.hasNext()) {
				ITurno shift = (ITurno) iterator.next();
		
				List studentsInShift = sp.getITurnoAlunoPersistente().readByShift(shift);
		
				shift.setOcupation(new Integer(studentsInShift.size()));
				shift.setPercentage(NumberUtils.formatNumber(new Double(shift.getOcupation().floatValue() * 100 / shift.getLotacao().floatValue()), 1));
				infoExecutionCourseOccupancy.getInfoShifts().add(Cloner.copyIShift2InfoShift(shift));		
			}
		
						
		
		} catch(ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		
		return infoExecutionCourseOccupancy;
	}
	
	
}