/*
 * Created on 14/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadAllTeachers implements IServico {

  private static ReadAllTeachers service = new ReadAllTeachers();

  /**
   * The singleton access method of this class.
   */
  public static ReadAllTeachers getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadAllTeachers() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadAllTeachers";
  }

  /**
   * Executes the service. Returns the current collection of infoTeachers.
   */
  public List run() throws FenixServiceException {
	ISuportePersistente sp;
	List allTeachers = null;

	try {
			sp = SuportePersistenteOJB.getInstance();
			allTeachers = sp.getIPersistentTeacher().readAll();
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}

	if(allTeachers == null || allTeachers.isEmpty()) 
		return allTeachers;

	// build the result of this service
	Iterator iterator = allTeachers.iterator();
	List result = new ArrayList(allTeachers.size());
    
	while (iterator.hasNext())
		result.add(Cloner.copyITeacher2InfoTeacher((ITeacher) iterator.next()));

	return result;
  }
}