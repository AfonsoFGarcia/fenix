/*
 * LerAulasDeSalaEmSemestre.java Created on 29 de Outubro de 2002, 15:44
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeSalaEmSemestre.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.ILesson;
import Dominio.IExecutionPeriod;
import Dominio.IRoom;
import Dominio.IShift;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerAulasDeSalaEmSemestre implements IService {

    public LerAulasDeSalaEmSemestre() {
    }

    public List run(InfoExecutionPeriod infoExecutionPeriod, InfoRoom infoRoom, Integer executionPeriodId) {
        List infoAulas = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IAulaPersistente lessonDAO = sp.getIAulaPersistente();
            IExecutionPeriod executionPeriod = null;
            if (executionPeriodId != null) {
                executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(
                        ExecutionPeriod.class, executionPeriodId, false);
            } else {
                executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
            }

            IRoom room = Cloner.copyInfoRoom2Room(infoRoom);

            List lessonList = lessonDAO.readByRoomAndExecutionPeriod(room, executionPeriod);

            Iterator iterator = lessonList.iterator();
            infoAulas = new ArrayList();
            while (iterator.hasNext()) {
                ILesson elem = (ILesson) iterator.next();
                InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
                IShift shift = elem.getShift();
                if (shift == null) {
                    continue;
                }
                InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
                infoLesson.setInfoShift(infoShift);

                infoAulas.add(infoLesson);
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoAulas;
    }

}