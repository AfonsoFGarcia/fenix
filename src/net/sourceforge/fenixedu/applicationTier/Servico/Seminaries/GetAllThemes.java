/*
 * Created on 3/Set/2003, 16:21:47
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryTheme;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 3/Set/2003, 16:21:47
 * 
 */
public class GetAllThemes implements IService {

	public List run() throws BDException, ExcepcaoPersistencia {
		List seminariesInfo = new LinkedList();

		ISuportePersistente persistenceSupport = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		IPersistentSeminaryTheme persistentTheme = persistenceSupport.getIPersistentSeminaryTheme();
		List themes = persistentTheme.readAll();
		for (Iterator iterator = themes.iterator(); iterator.hasNext();) {

			InfoTheme infoTheme = InfoTheme.newInfoFromDomain((Theme) iterator.next());

			seminariesInfo.add(infoTheme);
		}

		return seminariesInfo;
	}

}