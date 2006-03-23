package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Locale;
import java.util.StringTokenizer;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.branch.BranchType;

/**
 * @author dcs-rjao
 * @author Fernanda Quit�rio
 * 
 * 19/Mar/2003
 */

public class InfoBranch extends InfoObject {

    private String name;

    private String nameEn;
    
    private String code;

    private Integer specializationCredits;

    private Integer secondaryCredits;

    private InfoDegreeCurricularPlan infoDegreeCurricularPlan;

    private BranchType branchType;

    private String acronym;

    public InfoBranch() {
        setName(null);
        setNameEn(null);
        setCode(null);
        setInfoDegreeCurricularPlan(null);

    }

    public InfoBranch(String name, String code) {
        this();
        setName(name);
        setNameEn(nameEn);
        setCode(code);
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "name = " + this.name + "; ";
        result += "code = " + this.code + "; ";
        result += "idInternal = " + this.getIdInternal() + "]";
        result += "nameEn = " + this.nameEn + "; ";
        return result;
    }

    /**
     * @author Fernanda Quit�rio
     */
    public Boolean representsCommonBranch() {
        if (this.name != null && this.name.equals("") && this.code != null && this.code.equals("")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * @author Fernanda Quit�rio
     */
    /*
     * returns an empty string if there is no branch or branch initials in case
     * it exists
     */
    public String getPrettyCode() {
        if (representsCommonBranch().booleanValue()) {
            return new String("");
        }
        StringBuilder prettyCode = new StringBuilder();
        String namePart = null;
        StringTokenizer stringTokenizer = new StringTokenizer(this.name, " ");
        while (stringTokenizer.hasMoreTokens()) {
            namePart = stringTokenizer.nextToken();
            if (!namePart.equalsIgnoreCase("RAMO") && namePart.length() > 2) {
                prettyCode = prettyCode.append(namePart.substring(0, 1));
            }
        }
        return prettyCode.toString();
    }

    /**
     * @return String
     */
    public String getCode() {
        return code;
    }

    /**
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the code.
     * 
     * @param code
     *            The code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return infoDegreeCurricularPlan;
    }

    public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan plan) {
        infoDegreeCurricularPlan = plan;
    }

    /**
     * @author Nuno Correia
     * @author Ricardo Rodrigues
     *  
     */
    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    /**
     * @return Returns the secondaryCredits.
     */
    public Integer getSecondaryCredits() {
        return secondaryCredits;
    }

    /**
     * @param secondaryCredits
     *            The secondaryCredits to set.
     */
    public void setSecondaryCredits(Integer secondaryCredits) {
        this.secondaryCredits = secondaryCredits;
    }

    /**
     * @return Returns the specializationCredits.
     */
    public Integer getSpecializationCredits() {
        return specializationCredits;
    }

    /**
     * @param specializationCredits
     *            The specializationCredits to set.
     */
    public void setSpecializationCredits(Integer specializationCredits) {
        this.specializationCredits = specializationCredits;
    }

    /**
     * @return Returns the branchType.
     */
    public BranchType getBranchType() {
        return branchType;
    }

    /**
     * @param branchType
     *            The branchType to set.
     */
    public void setBranchType(BranchType branchType) {
        this.branchType = branchType;
    }

    public void copyFromDomain(Branch branch) {
        super.copyFromDomain(branch);
        if (branch != null) {
            setAcronym(branch.getAcronym());
            setBranchType(branch.getBranchType());
            setCode(branch.getCode());
            setName(branch.getName());
            setNameEn(branch.getNameEn());

            setSecondaryCredits(branch.getSecondaryCredits());
            setSpecializationCredits(branch.getSpecializationCredits());
        }
    }

    public static InfoBranch newInfoFromDomain(Branch branch) {
        InfoBranch infoBranch = null;
        if (branch != null) {
            infoBranch = new InfoBranch();
            infoBranch.copyFromDomain(branch);
        }
        return infoBranch;
    }

    public String getNameEn() {
        return nameEn;
    }
    

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    
    public void prepareEnglishPresentation(Locale locale) {
        if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) { 
            if (!(this.nameEn==null)&&!(this.nameEn.length()==0)&&!(this.nameEn=="")){
                this.name = this.nameEn;
            }
        }
    }
    
}
