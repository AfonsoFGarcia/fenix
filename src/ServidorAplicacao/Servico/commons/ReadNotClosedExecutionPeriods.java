/*
 * Created on 28/04/2003
 *  
 */
package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ReadNotClosedExecutionPeriods implements IServico {

	private static ReadNotClosedExecutionPeriods service =
		new ReadNotClosedExecutionPeriods();
	/**
	 * The singleton access method of this class.
	 */
	public static ReadNotClosedExecutionPeriods getService() {
		return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadNotClosedExecutionPeriods";
	}

	public List run() throws FenixServiceException {

		List result = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionPeriod executionPeriodDAO =
				sp.getIPersistentExecutionPeriod();

			List executionPeriods =
				executionPeriodDAO.readNotClosedExecutionPeriods();

			if (executionPeriods != null) {
				for (int i = 0; i < executionPeriods.size(); i++) {
					result.add(
						Cloner.copyIExecutionPeriod2InfoExecutionPeriod(
							(IExecutionPeriod) executionPeriods.get(i)));
				}
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}

		return result;
	}
}
