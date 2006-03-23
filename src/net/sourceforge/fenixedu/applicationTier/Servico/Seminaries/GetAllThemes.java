/*
 * Created on 3/Set/2003, 16:21:47
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 3/Set/2003, 16:21:47
 * 
 */
public class GetAllThemes extends Service {

	public List run() throws BDException, ExcepcaoPersistencia {
		List seminariesInfo = new LinkedList();

		List themes = Theme.getAllThemes();
		for (Iterator iterator = themes.iterator(); iterator.hasNext();) {

			InfoTheme infoTheme = InfoTheme.newInfoFromDomain((Theme) iterator.next());

			seminariesInfo.add(infoTheme);
		}

		return seminariesInfo;
	}

}