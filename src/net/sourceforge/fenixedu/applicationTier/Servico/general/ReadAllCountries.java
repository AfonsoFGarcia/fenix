/*
 * ReadAllCountries.java
 * 
 * O Servico ReadAllCountries devolve a lista de paises existentes
 * 
 * Created on 16 de Dezembro de 2002, 12:54
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.general;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadAllCountries extends Service {

    public Object run() throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {
        List countries = persistentSupport.getIPersistentCountry().readAllCountrys();

        if (countries.size() == 0) {
            throw new ExcepcaoInexistente("Non existing Countries !!");
        }

        return CollectionUtils.collect(countries, new Transformer() {

            public Object transform(Object input) {
                Country country = (Country) input;
                return InfoCountry.newInfoFromDomain(country);
            }
        });

    }
}