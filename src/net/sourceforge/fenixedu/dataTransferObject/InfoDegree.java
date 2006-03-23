/*
 * InfoDegree.java
 * 
 * Created on 25 de Novembro de 2002, 1:07
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

/**
 * @author tfc130
 */
public class InfoDegree extends InfoObject implements Comparable {

    protected String sigla;

    protected String nome;

    protected String nameEn;

    protected DegreeType tipoCurso;

    private List infoDegreeCurricularPlans = null;

    private List infoDegreeInfos = null; // added by T�nia Pous�o

	private List infoCoordinators;
	
	private GradeScale gradeScale;

    public InfoDegree() {
    }

    public InfoDegree(String sigla, String nome) {
        setSigla(sigla);
        setNome(nome);
    }

    public InfoDegree(String sigla, String nome, String nameEn, DegreeType degreeType) {
        setSigla(sigla);
        setNome(nome);
        setNameEn(nameEn);
        setTipoCurso(degreeType);
    }

    public String getSigla() {
        return this.sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoDegree) {
            InfoDegree iL = (InfoDegree) obj;
            resultado = (getSigla().equals(iL.getSigla()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[INFOCURSO";
        result += ", sigla=" + this.sigla;
        result += ", nome=" + this.nome;
        result += ", tipoCurso=" + this.tipoCurso;
        result += "]";
        return result;
    }

    /**
     * @return DegreeType
     */
    public DegreeType getTipoCurso() {
        return tipoCurso;
    }

    /**
     * Sets the tipoCurso.
     * 
     * @param degreeType
     *            The degreeType to set
     */
    public void setTipoCurso(DegreeType tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    /**
     * @return List
     */
    public List getInfoDegreeCurricularPlans() {
        return infoDegreeCurricularPlans;
    }

    /**
     * Sets the infoDegreeCurricularPlans.
     * 
     * @param infoDegreeCurricularPlans
     *            The infoDegreeCurricularPlans to set
     */
    public void setInfoDegreeCurricularPlans(List infoDegreeCurricularPlans) {
        this.infoDegreeCurricularPlans = infoDegreeCurricularPlans;
    }

    // alphabetic order
    public int compareTo(Object arg0) {
        InfoDegree degree = (InfoDegree) arg0;
        return this.getNome().compareTo(degree.getNome());
    }

    public List getInfoDegreeInfos() {
        return infoDegreeInfos;
    }

    public void setInfoDegreeInfos(List infoDegreeInfos) {
        this.infoDegreeInfos = infoDegreeInfos;
    }

	public void copyFromDomain(Degree degree){
		super.copyFromDomain(degree);
		if (degree != null){
			setNome(degree.getNome());
			setSigla(degree.getSigla());
			setTipoCurso(degree.getTipoCurso());
			setNameEn(degree.getNameEn());
			setGradeScale(degree.getGradeScale());

            List<InfoDegreeCurricularPlan> degreeCurricularPlans = new ArrayList<InfoDegreeCurricularPlan>();
            for (DegreeCurricularPlan dcp : degree.getDegreeCurricularPlans()) {
                if (dcp.getCurricularStage().equals(CurricularStage.OLD)) {
                    degreeCurricularPlans.add(InfoDegreeCurricularPlan.newInfoFromDomain(dcp));
                }
            }
            
            setInfoDegreeCurricularPlans(degreeCurricularPlans);
        }
    }

    /**
     * @param degree
     * @return
     */
    public static InfoDegree newInfoFromDomain(Degree degree) {
        InfoDegree infoDegree = null;
        if (degree != null) {
            infoDegree = new InfoDegree();
            infoDegree.copyFromDomain(degree);
        }
        return infoDegree;
    }

    public void copyToDomain(InfoDegree infoDegree, Degree degree) {
        super.copyToDomain(infoDegree, degree);
        degree.setNome(infoDegree.getNome());
        degree.setSigla(infoDegree.getSigla());
        degree.setTipoCurso(infoDegree.getTipoCurso());
        degree.setNameEn(infoDegree.getNameEn());
		degree.setGradeScale(infoDegree.getGradeScale());        
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public void prepareEnglishPresentation(Locale locale) {
        if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            if (!(this.nameEn == null) && !(this.nameEn.length() == 0) && !(this.nameEn == "")) {
                this.nome = this.nameEn;
            }
        }
    }

    public List getInfoCoordinators() {
        return infoCoordinators;
    }

	public void setInfoCoordinators(List infoCoordinators)
	{
		this.infoCoordinators = infoCoordinators;
	}

	public GradeScale getGradeScale() {
		return this.gradeScale;
	}

	public void setGradeScale(GradeScale gradeScale) {
		this.gradeScale = gradeScale;
	}

}