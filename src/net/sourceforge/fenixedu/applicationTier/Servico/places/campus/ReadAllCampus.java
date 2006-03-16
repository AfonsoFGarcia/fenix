/*
 * Created on Dec 5, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.places.campus;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCampus;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 */
public class ReadAllCampus extends Service {

	public List run() throws FenixServiceException, ExcepcaoPersistencia {
		List infoCampusList;
		List campusList = (List) persistentObject.readAll(Campus.class);
		infoCampusList = (List) CollectionUtils.collect(campusList, new Transformer() {

			public Object transform(Object input) {
				Campus campus = (Campus) input;
				InfoCampus infoCampus = InfoCampus.newInfoFromDomain(campus);
				return infoCampus;
			}
		});

		return infoCampusList;

	}
}