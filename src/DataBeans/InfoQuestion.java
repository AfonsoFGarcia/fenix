/*
 * Created on 25/Jul/2003
 */
package DataBeans;

import java.util.List;

import Util.tests.CardinalityType;
import Util.tests.QuestionType;
import Util.tests.Render;

/**
 * @author Susana Fernandes
 */
public class InfoQuestion extends InfoObject {

    private String xmlFile;

    private String xmlFileName;

    private Boolean visibility;

    private InfoMetadata infoMetadata;

    private List question;

    private List options;

    private Render render;

    //This is the list with all of the instructions for the response
    // processing.
    private List responseProcessingInstructions;

    //This is the list with ONE correct response (to use with fenix correction
    // formulas)
    // private List correctResponse;

    private Double questionValue;

    private Integer optionNumber;

    private QuestionType questionType;

    private CardinalityType cardinalityType;

    public InfoQuestion() {
    }

    public InfoMetadata getInfoMetadata() {
        return infoMetadata;
    }

    public String getXmlFile() {
        return xmlFile;
    }

    public String getXmlFileName() {
        return xmlFileName;
    }

    public void setInfoMetadata(InfoMetadata metadata) {
        infoMetadata = metadata;
    }

    public void setXmlFile(String string) {
        xmlFile = string;
    }

    public void setXmlFileName(String string) {
        xmlFileName = string;
    }

    public List getQuestion() {
        return question;
    }

    public void setQuestion(List list) {
        question = list;
    }

    public List getOptions() {
        return options;
    }

    public void setOptions(List list) {
        options = list;
    }

    public Double getQuestionValue() {
        return questionValue;
    }

    public void setQuestionValue(Double value) {
        questionValue = value;
    }

    /*
     * public List getCorrectResponse() { return correctResponse; }
     * 
     * public void setCorrectResponse(List list) { correctResponse = list; }
     */
    public Integer getOptionNumber() {
        return optionNumber;
    }

    public void setOptionNumber(Integer integer) {
        optionNumber = integer;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean boolean1) {
        visibility = boolean1;
    }

    public CardinalityType getCardinalityType() {
        return cardinalityType;
    }

    public void setCardinalityType(CardinalityType cardinalityType) {
        this.cardinalityType = cardinalityType;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List getResponseProcessingInstructions() {
        return responseProcessingInstructions;
    }

    public void setResponseProcessingInstructions(
            List responseProcessingInstructions) {
        this.responseProcessingInstructions = responseProcessingInstructions;
    }

    public Render getRender() {
        return render;
    }

    public void setRender(Render render) {
        this.render = render;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoQuestion) {
            InfoQuestion infoQuestion = (InfoQuestion) obj;
            result = getIdInternal().equals(infoQuestion.getIdInternal());
            result = result
                    || (getXmlFile().equals(infoQuestion.getXmlFile())
                            && (getXmlFileName().equals(infoQuestion
                                    .getXmlFileName()))
                            && (getInfoMetadata().equals(infoQuestion
                                    .getInfoMetadata()))
                            && (getQuestionValue().equals(infoQuestion
                                    .getQuestionValue()))
                            && (getQuestion().containsAll(infoQuestion
                                    .getQuestion()))
                            && (infoQuestion.getQuestion()
                                    .containsAll(getQuestion()))
                            && (getOptions().containsAll(infoQuestion
                                    .getOptions()))
                            && (infoQuestion.getOptions()
                                    .containsAll(getOptions()))
                            //                && (getCorrectResponse().containsAll(infoQuestion
                            //                      .getCorrectResponse()))
                            //            &&
                            // (infoQuestion.getCorrectResponse().containsAll(getCorrectResponse()))

                            && (getOptionNumber().equals(infoQuestion
                                    .getOptionNumber()))
                            && (getVisibility().equals(infoQuestion
                                    .getVisibility()))
                            && (getCardinalityType().getType()
                                    .equals(infoQuestion.getCardinalityType()
                                            .getType())) && (getQuestionType()
                            .getType().equals(infoQuestion.getQuestionType()
                            .getType())));
        }
        return result;
    }
}