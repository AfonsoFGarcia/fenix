package DataBeans;

import java.util.List;

/*
 * InfoMasterDegreeCandidate.java
 *
 * Created on 29 de Novembro de 2002, 15:57
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */


public class InfoMasterDegreeCandidate extends InfoObject{
 
    private String majorDegree = null;
    private Integer candidateNumber = null;
    private String majorDegreeSchool = null;
    private Integer majorDegreeYear = null;
    private Double average = null;
	private InfoPerson infoPerson = null;
	private InfoExecutionDegree infoExecutionDegree = null;
    private String specialization = null;
	private InfoCandidateSituation infoCandidateSituation = null;
	private List situationList = null;
	private String specializationArea;
	private Integer substituteOrder;
	private Double givenCredits;
	private String givenCreditsRemarks;
	
	
    	
	/**
	 * @return
	 */
	public String getGivenCreditsRemarks() {
		return givenCreditsRemarks;
	}

	/**
	 * @param givenCreditsRemarks
	 */
	public void setGivenCreditsRemarks(String givenCreditsRemarks) {
		this.givenCreditsRemarks = givenCreditsRemarks;
	}

    /** Construtor sem argumentos p�blico requerido pela moldura de objectos OJB */
    public InfoMasterDegreeCandidate() {} 
    
	public InfoMasterDegreeCandidate(InfoPerson person, InfoExecutionDegree executionDegree, Integer candidateNumber, String specialization, 
			   String majorDegree, String majorDegreeSchool, Integer majorDegreeYear, Double average){
				this.infoPerson = person;
				this.infoExecutionDegree = executionDegree;
				this.candidateNumber = candidateNumber;
				this.specialization = specialization;
				this.majorDegree = majorDegree;
				this.majorDegreeSchool = majorDegreeSchool;
				this.majorDegreeYear = majorDegreeYear;
				this.average = average;
    	   	
		}
		
		
    public boolean equals(Object o) {
			return
			((o instanceof InfoMasterDegreeCandidate) &&
		
			((this.infoPerson.equals(((InfoMasterDegreeCandidate)o).getInfoPerson())) &&
			 (this.specialization.equals(((InfoMasterDegreeCandidate)o).getSpecialization())) &&
			 (this.infoExecutionDegree.equals(((InfoMasterDegreeCandidate)o).getInfoExecutionDegree()))) ||
		 
			((this.infoExecutionDegree.equals(((InfoMasterDegreeCandidate)o).getInfoExecutionDegree())) &&
			 (this.candidateNumber.equals(((InfoMasterDegreeCandidate)o).getCandidateNumber())) &&
			 (this.specialization.equals(((InfoMasterDegreeCandidate)o).getSpecialization()))));       
        
		}
		
	


	/**
	 * @return Specialization Area
	 */
	public String getSpecializationArea() {
		return specializationArea;
	}

	/**
	 * @param specializationArea
	 */
	public void setSpecializationArea(String specializationArea) {
		this.specializationArea = specializationArea;
	}

	public String toString() {
	   String result = "Master Degree Candidate :\n";
	   result += "\n  - Person : " + infoPerson;
	   result += "\n  - Major Degree : " + majorDegree;
	   result += "\n  - Candidate Number : " + candidateNumber;
	   result += "\n  - Specialization : " + specialization;
	   result += "\n  - Major Degree School : " + majorDegreeSchool;
	   result += "\n  - Major Degree Year : " + majorDegreeYear;
	   result += "\n  - Major Degree Average : " + average;
	   result += "\n  - Master Degree : " + infoExecutionDegree;
	   result += "\n  - Specialization Area  : " + specializationArea;
	   result += "\n  - Substitute Order  : " + substituteOrder;
	   result += "\n  - Given Credits  : " + givenCredits;
	   result += "\n  - Given Credits Remarks  : " + givenCreditsRemarks;

        
		   return result;
	   }
	     
	/**
	 * @return
	 */
	public Double getAverage() {
		return average;
	}

	/**
	 * @return
	 */
	public Integer getCandidateNumber() {
		return candidateNumber;
	}

	/**
	 * @return
	 */
	public InfoCandidateSituation getInfoCandidateSituation() {
		return infoCandidateSituation;
	}

	/**
	 * @return
	 */
	public InfoExecutionDegree getInfoExecutionDegree() {
		return infoExecutionDegree;
	}

	/**
	 * @return
	 */
	public InfoPerson getInfoPerson() {
		return infoPerson;
	}

	/**
	 * @return
	 */
	public String getMajorDegree() {
		return majorDegree;
	}

	/**
	 * @return
	 */
	public String getMajorDegreeSchool() {
		return majorDegreeSchool;
	}

	/**
	 * @return
	 */
	public Integer getMajorDegreeYear() {
		return majorDegreeYear;
	}

	/**
	 * @return
	 */
	public List getSituationList() {
		return situationList;
	}

	/**
	 * @return
	 */
	public String getSpecialization() {
		return specialization;
	}

	/**
	 * @param double1
	 */
	public void setAverage(Double double1) {
		average = double1;
	}

	/**
	 * @param integer
	 */
	public void setCandidateNumber(Integer integer) {
		candidateNumber = integer;
	}

	/**
	 * @param situation
	 */
	public void setInfoCandidateSituation(InfoCandidateSituation situation) {
		infoCandidateSituation = situation;
	}

	/**
	 * @param degree
	 */
	public void setInfoExecutionDegree(InfoExecutionDegree degree) {
		infoExecutionDegree = degree;
	}

	/**
	 * @param person
	 */
	public void setInfoPerson(InfoPerson person) {
		infoPerson = person;
	}

	/**
	 * @param string
	 */
	public void setMajorDegree(String string) {
		majorDegree = string;
	}

	/**
	 * @param string
	 */
	public void setMajorDegreeSchool(String string) {
		majorDegreeSchool = string;
	}

	/**
	 * @param integer
	 */
	public void setMajorDegreeYear(Integer integer) {
		majorDegreeYear = integer;
	}

	/**
	 * @param list
	 */
	public void setSituationList(List list) {
		situationList = list;
	}

	/**
	 * @param string
	 */
	public void setSpecialization(String string) {
		specialization = string;
	}

	/**
	 * @return
	 */
	public Integer getSubstituteOrder() {
		return substituteOrder;
	}

	/**
	 * @param integer
	 */
	public void setSubstituteOrder(Integer integer) {
		substituteOrder = integer;
	}

	/**
	 * @return
	 */
	public Double getGivenCredits() {
		return givenCredits;
	}

	/**
	 * @param double1
	 */
	public void setGivenCredits(Double double1) {
		givenCredits = double1;
	}

}