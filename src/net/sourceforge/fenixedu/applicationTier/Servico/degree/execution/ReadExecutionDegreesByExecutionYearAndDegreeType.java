/*
 * Created on Dec 11, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degree.execution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class ReadExecutionDegreesByExecutionYearAndDegreeType implements IService {

    /**
     *  
     */
    public ReadExecutionDegreesByExecutionYearAndDegreeType() {
        super();
    }

    public List run(InfoExecutionYear infoExecutionYear, TipoCurso degreeType)
            throws FenixServiceException {

        List infoExecutionDegreeList = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

            IExecutionYear executionYear = null;
            if (infoExecutionYear == null) {
                IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
                executionYear = persistentExecutionYear.readCurrentExecutionYear();
            } else {
                executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionYear);
            }

            List executionDegrees = null;

            if (degreeType == null) {
                executionDegrees = executionDegreeDAO.readByExecutionYear(executionYear.getYear());
            } else {
                executionDegrees = executionDegreeDAO.readByExecutionYearAndDegreeType(executionYear,
                        degreeType);
            }

            infoExecutionDegreeList = (ArrayList) CollectionUtils.collect(executionDegrees,
                    new Transformer() {

                        public Object transform(Object input) {
                            IExecutionDegree executionDegree = (IExecutionDegree) input;
                            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner
                                    .get(executionDegree);
                            return infoExecutionDegree;
                        }
                    });
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException("Problems on database!", ex);
        }
        return infoExecutionDegreeList;
    }
}