/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 *
 * Created on 2003/08/12
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 **/
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoClass;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Turma;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadShiftsByClass implements IServico {

	private static ReadShiftsByClass _servico = new ReadShiftsByClass();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadShiftsByClass getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadShiftsByClass() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadShiftsByClass";
	}

	public Object run(InfoClass infoClass) throws FenixServiceException {

		List infoShifts = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ITurma shcoolClass =
				(ITurma) sp.getITurmaPersistente().readByOID(
					Turma.class,
					infoClass.getIdInternal());

			List shifts = sp.getITurmaTurnoPersistente().readByClass(shcoolClass);

			infoShifts =
				(List) CollectionUtils.collect(shifts, new Transformer() {
				public Object transform(Object arg0) {
					ITurno shift = (ITurno) arg0;
					InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
					infoShift
						.setInfoLessons(
							(List) CollectionUtils
							.collect(
								shift.getAssociatedLessons(),
								new Transformer() {
						public Object transform(Object arg0) {
							return Cloner.copyILesson2InfoLesson((IAula) arg0);
						}
					}));
					return infoShift;
				}
			});
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}

		return infoShifts;
	}

}