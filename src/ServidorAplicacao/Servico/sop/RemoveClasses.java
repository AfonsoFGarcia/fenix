/*
 *
 * Created on 2003/08/15
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o AdicionarTurno.
 *
 * @author Luis Cruz & Sara Ribeiro
 **/
import java.util.List;

import DataBeans.InfoShift;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Turma;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class RemoveClasses implements IServico {

	private static RemoveClasses _servico = new RemoveClasses();
	/**
	 * The singleton access method of this class.
	 **/
	public static RemoveClasses getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private RemoveClasses() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "RemoveClasses";
	}

	public Boolean run(InfoShift infoShift, List classOIDs)
		throws FenixServiceException {

		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ITurno shift =
				(ITurno) sp.getITurnoPersistente().readByOID(
					Turno.class,
					infoShift.getIdInternal());

			sp.getITurnoPersistente().lockWrite(shift);

			for (int i = 0; i < classOIDs.size(); i++) {
				ITurma schoolClass =
					(ITurma) sp.getITurmaPersistente().readByOID(
						Turma.class,
						(Integer) classOIDs.get(i));

				shift.getAssociatedClasses().remove(schoolClass);
			}

			result = true;
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}

		return new Boolean(result);
	}

}