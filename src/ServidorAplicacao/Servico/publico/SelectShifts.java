package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Jo�o Mota
 *
 */
public class SelectShifts implements IServico {

	private static SelectShifts _servico = new SelectShifts();

	/**
	  * The actor of this class.
	  **/

	private SelectShifts() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "SelectShifts";
	}

	/**
	 * Returns the _servico.
	 * @return SelectShifts
	 */
	public static SelectShifts getService() {
		return _servico;
	}

	public Object run(InfoShift infoShift) {
		List shifts = new ArrayList();
		List infoShifts = new ArrayList();

		ITurno shift = Cloner.copyInfoShift2IShift(infoShift);

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			shifts =
				sp.getITurnoPersistente().readByExecutionCourse(shift.getDisciplinaExecucao());

			
			
			for (int i = 0; i < shifts.size(); i++) {
				ITurno taux = (ITurno) shifts.get(i);
				infoShifts.add(Cloner.copyShift2InfoShift(taux));
			}
		} catch (ExcepcaoPersistencia e) {
		}

		return infoShifts;
	}

}