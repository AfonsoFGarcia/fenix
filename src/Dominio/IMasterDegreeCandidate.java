/*
 * IMasterDegreeCandidate.java
 *
 * Created on 17 de Outubro de 2002, 10:29
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package Dominio;

import java.util.List;

import Util.Specialization;


public interface IMasterDegreeCandidate extends IDomainObject{
    
    // Set Methods
    void setMajorDegree(String majorDegree);
    void setCandidateNumber(Integer candidateNumber);
    void setSpecialization(Specialization specialization);
    void setMajorDegreeSchool(String majorDegreeSchool);
    void setMajorDegreeYear(Integer majorDegreeYear);
    void setAverage(Double average);
    void setExecutionDegree(ICursoExecucao executionDegree);    
    void setSituations(List situations);
    void setPerson(IPessoa person);
    void setSpecializationArea(String specializationArea);
	void setSubstituteOrder(Integer substituteOrder);
	void setGivenCredits(Double givenCredits);
    
    // Get Methods
    String getMajorDegree();
    Integer getCandidateNumber();
    Specialization getSpecialization();
    String getMajorDegreeSchool();
    Integer getMajorDegreeYear();
    Double getAverage();
    ICursoExecucao getExecutionDegree();    
	List getSituations();
    IPessoa getPerson();
    String getSpecializationArea();
    Integer getSubstituteOrder();
    Double getGivenCredits();
    
    /**
     * 
     * @return The candidate's active Situation
     */
    ICandidateSituation getActiveCandidateSituation();
    
    
}

