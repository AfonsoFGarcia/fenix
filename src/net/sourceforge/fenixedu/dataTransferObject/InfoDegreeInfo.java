package net.sourceforge.fenixedu.dataTransferObject;

import java.sql.Timestamp;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionYear;

/**
 * @author T�nia Pous�o Created on 30/Out/2003
 */
public class InfoDegreeInfo extends InfoObject implements ISiteComponent {

    private InfoDegree infoDegree;

    private String description;

    private String objectives;

    private String history;

    private String professionalExits;

    private String additionalInfo;

    private String links;

    private String testIngression;

    private Integer driftsInitial;

    private Integer driftsFirst;

    private Integer driftsSecond;

    private String classifications;

    private Double markMin;

    private Double markMax;

    private Double markAverage;

    private ExecutionYear executionYear;
    
    private String qualificationLevel;
    
    private String recognitions;

    //Data in english
    private String descriptionEn;

    private String objectivesEn;

    private String historyEn;

    private String professionalExitsEn;

    private String additionalInfoEn;

    private String linksEn;

    private String testIngressionEn;

    private String classificationsEn;

    private String qualificationLevelEn;
    
    private String recognitionsEn;
    
    public InfoDegreeInfo() {
    }

    public InfoDegreeInfo(InfoDegree infoDegree, String objectives, String history,
            String professionalExits, String additionalInfo, String links, String testIngression,
            Integer driftsInitial, Integer driftsFirst, Integer driftsSecond, String classifications,
            Double markMin, Double markMax, Double markAverage, Timestamp lastModificationDate) {
        this.infoDegree = infoDegree;
        this.objectives = objectives;
        this.history = history;
        this.professionalExits = professionalExits;
        this.additionalInfo = additionalInfo;
        this.links = links;
        this.testIngression = testIngression;
        this.driftsInitial = driftsInitial;
        this.driftsFirst = driftsFirst;
        this.driftsSecond = driftsSecond;
        this.classifications = classifications;
        this.markMin = markMin;
        this.markMax = markMax;
        this.markAverage = markAverage;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getClassifications() {
        return classifications;
    }

    public void setClassifications(String classifications) {
        this.classifications = classifications;
    }

    public Integer getDriftsFirst() {
        return driftsFirst;
    }

    public void setDriftsFirst(Integer driftsFirst) {
        this.driftsFirst = driftsFirst;
    }

    public Integer getDriftsInitial() {
        return driftsInitial;
    }

    public void setDriftsInitial(Integer driftsInitial) {
        this.driftsInitial = driftsInitial;
    }

    public Integer getDriftsSecond() {
        return driftsSecond;
    }

    public void setDriftsSecond(Integer driftsSecond) {
        this.driftsSecond = driftsSecond;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public InfoDegree getInfoDegree() {
        return infoDegree;
    }

    public void setInfoDegree(InfoDegree infoDegree) {
        this.infoDegree = infoDegree;
    }

    public InfoExecutionYear getLastModificationDate() {
        return InfoExecutionYear.newInfoFromDomain(executionYear);
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public Double getMarkAverage() {
        return markAverage;
    }

    public void setMarkAverage(Double markAverage) {
        this.markAverage = markAverage;
    }

    public Double getMarkMax() {
        return markMax;
    }

    public void setMarkMax(Double markMax) {
        this.markMax = markMax;
    }

    public Double getMarkMin() {
        return markMin;
    }

    public void setMarkMin(Double markMin) {
        this.markMin = markMin;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getProfessionalExits() {
        return professionalExits;
    }

    public void setProfessionalExits(String professionalExits) {
        this.professionalExits = professionalExits;
    }

    public String getTestIngression() {
        return testIngression;
    }

    public void setTestIngression(String testIngression) {
        this.testIngression = testIngression;
    }

    public String getAdditionalInfoEn() {
        return additionalInfoEn;
    }

    public void setAdditionalInfoEn(String additionalInfoEn) {
        this.additionalInfoEn = additionalInfoEn;
    }

    public String getClassificationsEn() {
        return classificationsEn;
    }

    public void setClassificationsEn(String classificationsEn) {
        this.classificationsEn = classificationsEn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getHistoryEn() {
        return historyEn;
    }

    public void setHistoryEn(String historyEn) {
        this.historyEn = historyEn;
    }

    public String getLinksEn() {
        return linksEn;
    }

    public void setLinksEn(String linksEn) {
        this.linksEn = linksEn;
    }

    public String getObjectivesEn() {
        return objectivesEn;
    }

    public void setObjectivesEn(String objectivesEn) {
        this.objectivesEn = objectivesEn;
    }

    public String getProfessionalExitsEn() {
        return professionalExitsEn;
    }

    public void setProfessionalExitsEn(String professionalExitsEn) {
        this.professionalExitsEn = professionalExitsEn;
    }

    public String getTestIngressionEn() {
        return testIngressionEn;
    }

    public void setTestIngressionEn(String testIngressionEn) {
        this.testIngressionEn = testIngressionEn;
    }


    public String getQualificationLevel() {
        return qualificationLevel;
    }

    public void setQualificationLevel(String qualificationLevel) {
        this.qualificationLevel = qualificationLevel;
    }

    public String getQualificationLevelEn() {
        return qualificationLevelEn;
    }

    public void setQualificationLevelEn(String qualificationLevelEn) {
        this.qualificationLevelEn = qualificationLevelEn;
    }

    public String getRecognitions() {
        return recognitions;
    }

    public void setRecognitions(String recognitions) {
        this.recognitions = recognitions;
    }

    public String getRecognitionsEn() {
        return recognitionsEn;
    }

    public void setRecognitionsEn(String recognitionsEn) {
        this.recognitionsEn = recognitionsEn;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoDegreeInfo) {
            InfoDegreeInfo infoDegreeInfo = (InfoDegreeInfo) obj;
            result = getInfoDegree().equals(infoDegreeInfo.getInfoDegree());
        }
        return result;
    }

    public String toString() {
        String result = "[INFODEGREE_INFO:";
        result += " codigo interno= " + getIdInternal();
        result += " degree= " + getInfoDegree();
        result += " descri��o= " + getDescription();
        result += " objectivos= " + getObjectives();
        result += " historial= " + getHistory();
        result += " saidas profissionais=" + getProfessionalExits();
        result += " informa��o adicional= " + getAdditionalInfo();
        result += " links= " + getLinks();
        result += " provas de ingresso= " + getTestIngression();
        result += " classifica��es= " + getClassifications();
        result += " descri��o(En)= " + getDescriptionEn();
        result += " objectivos(En)= " + getObjectivesEn();
        result += " historial(En)= " + getHistoryEn();
        result += " saidas profissionais(En)=" + getProfessionalExitsEn();
        result += " informa��o adicional(En)= " + getAdditionalInfoEn();
        result += " links(En)= " + getLinksEn();
        result += " provas de ingresso(En)= " + getTestIngressionEn();
        result += " classifica��es(En)= " + getClassificationsEn();
        result += " vagas iniciais= " + getDriftsInitial();
        result += " vagas 1� fase= " + getDriftsFirst();
        result += " vagas 2�fase= " + getDriftsSecond();
        result += " nota minima= " + getMarkMin();
        result += " nota m�xima= " + getMarkMax();
        result += " nota m�dia= " + getMarkAverage();
        result += " data �ltima modifica��o= " + getLastModificationDate();
        result += "]";
        return result;
    }

    
    public void prepareEnglishPresentation(Locale locale) {
        if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            this.additionalInfo = this.additionalInfoEn;
            this.classifications = this.classificationsEn;
            this.description = this.descriptionEn;
            this.history = this.historyEn;
            this.links = this.linksEn;
            this.objectives = this.objectivesEn;
            this.professionalExits = this.professionalExitsEn;
            this.testIngression = this.testIngressionEn;    
            this.recognitions = this.recognitionsEn;
            this.qualificationLevel = this.qualificationLevelEn;
            this.infoDegree.prepareEnglishPresentation(locale);
  
        }
    }

    public void copyFromDomain(DegreeInfo degreeInfo) {
        super.copyFromDomain(degreeInfo);
        if (degreeInfo != null) {
            setAdditionalInfo(degreeInfo.getAdditionalInfo());
            setAdditionalInfoEn(degreeInfo.getAdditionalInfoEn());
            setClassifications(degreeInfo.getClassifications());
            setClassificationsEn(degreeInfo.getClassificationsEn());
            setDescription(degreeInfo.getDescription());
            setDescriptionEn(degreeInfo.getDescriptionEn());
            setDriftsFirst(degreeInfo.getDriftsFirst());
            setDriftsInitial(degreeInfo.getDriftsInitial());
            setDriftsSecond(degreeInfo.getDriftsSecond());
            setHistory(degreeInfo.getHistory());
            setHistoryEn(degreeInfo.getHistoryEn());
            setLinks(degreeInfo.getLinks());
            setLinksEn(degreeInfo.getLinksEn());
            setMarkAverage(degreeInfo.getMarkAverage());
            setMarkMax(degreeInfo.getMarkMax());
            setMarkMin(degreeInfo.getMarkMin());
            setObjectives(degreeInfo.getObjectives());
            setObjectivesEn(degreeInfo.getObjectivesEn());
            setProfessionalExits(degreeInfo.getProfessionalExits());
            setProfessionalExitsEn(degreeInfo.getProfessionalExitsEn());
            setTestIngression(degreeInfo.getTestIngression());
            setTestIngressionEn(degreeInfo.getTestIngressionEn());
            setQualificationLevel(degreeInfo.getQualificationLevel());
            setQualificationLevelEn(degreeInfo.getQualificationLevelEn());
            setRecognitions(degreeInfo.getRecognitions());
            setRecognitionsEn(degreeInfo.getRecognitionsEn());
            setInfoDegree(InfoDegree.newInfoFromDomain(degreeInfo.getDegree()));
        }
    }

    public static InfoDegreeInfo newInfoFromDomain(DegreeInfo degreeInfo) {
        InfoDegreeInfo infoDegreeInfo = null;
        if (degreeInfo != null) {
            infoDegreeInfo = new InfoDegreeInfo();
            infoDegreeInfo.copyFromDomain(degreeInfo);
        }
        return infoDegreeInfo;
    }

}