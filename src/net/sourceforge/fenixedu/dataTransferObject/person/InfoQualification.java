/*
 * Created on 05/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.person;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IQualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Barbosa
 * @author Pica
 */

public class InfoQualification extends InfoObject implements ISiteComponent {
    private String mark;

    private String school;

    private String title;

    private String degree;

    private InfoPerson infoPerson;

    private Date date;

    private String branch;

    private String specializationArea;

    private String degreeRecognition;

    private Date equivalenceDate;

    private String equivalenceSchool;

    private InfoCountry infoCountry;

    public InfoQualification() {
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && obj instanceof InfoQualification) {
            result = getSchool().equals(((InfoQualification) obj).getSchool())
                    && getDate().equals(((InfoQualification) obj).getDate())
                    && getInfoPerson().equals(((InfoQualification) obj).getInfoPerson());
        }
        return result;
    }

    /**
     * @return InfoPerson
     */
    public InfoPerson getInfoPerson() {
        return infoPerson;
    }

    /**
     * @return String
     */
    public String getMark() {
        return mark;
    }

    /**
     * @return String
     */
    public String getSchool() {
        return school;
    }

    /**
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the personInfo.
     * 
     * @param infoPerson
     *            The personInfo to set
     */
    public void setInfoPerson(InfoPerson infoPerson) {
        this.infoPerson = infoPerson;
    }

    /**
     * Sets the mark of the qualification
     * 
     * @param mark.
     */
    public void setMark(String mark) {
        this.mark = mark;
    }

    /**
     * Sets the school of qualification
     * 
     * @param school;
     *            The school to set
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * Sets the title of qualification
     * 
     * @param title;
     *            The title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Returns the degree.
     */
    public String getDegree() {
        return degree;
    }

    /**
     * @param degree
     *            The degree to set.
     */
    public void setDegree(String degree) {
        this.degree = degree;
    }

    /**
     * @return Returns the branch.
     */
    public String getBranch() {
        return branch;
    }

    /**
     * @param branch
     *            The branch to set.
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     * @return Returns the degreeRecognition.
     */
    public String getDegreeRecognition() {
        return degreeRecognition;
    }

    /**
     * @param degreeRecognition
     *            The degreeRecognition to set.
     */
    public void setDegreeRecognition(String degreeRecognition) {
        this.degreeRecognition = degreeRecognition;
    }

    /**
     * @return Returns the equivalenceDate.
     */
    public Date getEquivalenceDate() {
        return equivalenceDate;
    }

    /**
     * @param equivalenceDate
     *            The equivalenceDate to set.
     */
    public void setEquivalenceDate(Date equivalenceDate) {
        this.equivalenceDate = equivalenceDate;
    }

    /**
     * @return Returns the equivalenceSchool.
     */
    public String getEquivalenceSchool() {
        return equivalenceSchool;
    }

    /**
     * @param equivalenceSchool
     *            The equivalenceSchool to set.
     */
    public void setEquivalenceSchool(String equivalenceSchool) {
        this.equivalenceSchool = equivalenceSchool;
    }

    /**
     * @return Returns the infoCountry.
     */
    public InfoCountry getInfoCountry() {
        return infoCountry;
    }

    /**
     * @param infoCountry
     *            The infoCountry to set.
     */
    public void setInfoCountry(InfoCountry infoCountry) {
        this.infoCountry = infoCountry;
    }

    /**
     * @return Returns the qualificationDate.
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param qualificationDate
     *            The qualificationDate to set.
     */
    public void setDate(Date qualificationDate) {
        this.date = qualificationDate;
    }

    /**
     * @return Returns the specializationArea.
     */
    public String getSpecializationArea() {
        return specializationArea;
    }

    /**
     * @param specializationArea
     *            The specializationArea to set.
     */
    public void setSpecializationArea(String specializationArea) {
        this.specializationArea = specializationArea;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(IQualification qualification) {
        super.copyFromDomain(qualification);
        if (qualification != null) {
            setTitle(qualification.getTitle());
            setDate(qualification.getDate());
            setMark(qualification.getMark());
            setSchool(qualification.getSchool());
            setSpecializationArea(qualification.getSpecializationArea());
            setMark(qualification.getMark());
            setBranch(qualification.getBranch());
            setDegree(qualification.getDegree());
            setDegreeRecognition(qualification.getDegreeRecognition());
            setEquivalenceDate(qualification.getEquivalenceDate());
            setEquivalenceSchool(qualification.getEquivalenceSchool());
        }
    }

    public static InfoQualification newInfoFromDomain(IQualification qualification) {
        InfoQualification infoQualification = null;
        if (qualification != null) {
            infoQualification = new InfoQualification();
            infoQualification.copyFromDomain(qualification);
        }
        return infoQualification;
    }

    public void copyToDomain(InfoQualification infoQualification, IQualification qualification)
            throws ExcepcaoPersistencia {
        super.copyToDomain(infoQualification, qualification);

        qualification.setTitle(infoQualification.getTitle());
        qualification.setDate(infoQualification.getDate());
        qualification.setMark(infoQualification.getMark());
        qualification.setSchool(infoQualification.getSchool());
        qualification.setSpecializationArea(infoQualification.getSpecializationArea());
        qualification.setMark(infoQualification.getMark());
        qualification.setBranch(infoQualification.getBranch());
        qualification.setDegree(infoQualification.getDegree());
        qualification.setDegreeRecognition(infoQualification.getDegreeRecognition());
        qualification.setEquivalenceDate(infoQualification.getEquivalenceDate());
        qualification.setEquivalenceSchool(infoQualification.getEquivalenceSchool());
    }

}
