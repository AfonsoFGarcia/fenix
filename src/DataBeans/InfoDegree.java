/*
 * InfoDegree.java
 * 
 * Created on 25 de Novembro de 2002, 1:07
 */

package DataBeans;

import java.util.List;

import Dominio.ICurso;
import Util.TipoCurso;

/**
 * @author tfc130
 */
public class InfoDegree extends InfoObject implements Comparable {
	protected String sigla;
	protected String nome;
	protected TipoCurso tipoCurso;

	private List infoDegreeCurricularPlans;
	private List infoDegreeInfos; //added by T�nia Pous�o

	public InfoDegree() {
	}

	public InfoDegree(String sigla, String nome) {
		setSigla(sigla);
		setNome(nome);
	}

	public InfoDegree(String sigla, String nome, TipoCurso degreeType) {
		setSigla(sigla);
		setNome(nome);
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
	 * @return TipoCurso
	 */
	public TipoCurso getTipoCurso() {
		return tipoCurso;
	}

	/**
	 * Sets the tipoCurso.
	 * 
	 * @param degreeType
	 *          The degreeType to set
	 */
	public void setTipoCurso(TipoCurso tipoCurso) {
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
	 *          The infoDegreeCurricularPlans to set
	 */
	public void setInfoDegreeCurricularPlans(List infoDegreeCurricularPlans) {
		this.infoDegreeCurricularPlans = infoDegreeCurricularPlans;
	}

	//alphabetic order
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
    
     /**
     * @param degree
     * @return
     */
    public static InfoDegree copyFromDomain(ICurso degree) {
        InfoDegree infoDegree = null;
        if (degree != null) {
            infoDegree = new InfoDegree();
            infoDegree.setIdInternal(degree.getIdInternal());
            infoDegree.setNome(degree.getNome());
            infoDegree.setSigla(degree.getSigla());
            infoDegree.setTipoCurso(degree.getTipoCurso());
        }
        return infoDegree;
    }
}
