package Dominio;

/**
 * @author T�nia Pous�o
 *  
 */
public class FinalEvaluation extends Evaluation implements IFinalEvaluation {

    public boolean equals(Object obj) {
        if (obj instanceof FinalEvaluation) {
            FinalEvaluation finalEvaluationObj = (FinalEvaluation) obj;
            return this.getIdInternal().equals(finalEvaluationObj.getIdInternal());
        }

        return false;
    }

}