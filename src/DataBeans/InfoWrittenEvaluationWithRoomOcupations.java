/*
 * Created on 8/Jul/2004
 *
 */
package DataBeans;

import java.util.List;

import org.apache.commons.collections.Transformer;

import commons.CollectionUtils;

import Dominio.IRoomOccupation;
import Dominio.IWrittenEvaluation;

/**
 * @author T�nia Pous�o
 *  
 */
public class InfoWrittenEvaluationWithRoomOcupations extends InfoWrittenEvaluation {

    public void copyFromDomain(IWrittenEvaluation writtenEvaluation) {
        super.copyFromDomain(writtenEvaluation);
        if (writtenEvaluation != null) {
            setAssociatedRoomOccupation(copyIRoomOccupation2InfoRoomOccupation(writtenEvaluation
                    .getAssociatedRoomOccupation()));
        }
    }

    /**
     * @param associatedRoomOccupation
     * @return
     */
    private List copyIRoomOccupation2InfoRoomOccupation(List associatedRoomOccupation) {
        List infoRoomOccupation = null;

        infoRoomOccupation = (List) CollectionUtils.collect(associatedRoomOccupation, new Transformer() {

            public Object transform(Object arg0) {
                IRoomOccupation roomOccupation = (IRoomOccupation) arg0;
                return InfoRoomOccupationWithInfoRoom.newInfoFromDomain(roomOccupation);
            }
        });

        return infoRoomOccupation;
    }

    public static InfoWrittenEvaluation newInfoFromDomain(
            IWrittenEvaluation writtenEvaluation) {
        InfoWrittenEvaluationWithRoomOcupations infoWrittenEvaluation = null;
        if (writtenEvaluation != null) {
            infoWrittenEvaluation = new InfoWrittenEvaluationWithRoomOcupations();
            infoWrittenEvaluation.copyFromDomain(writtenEvaluation);
        }
        return infoWrittenEvaluation;
    }
}