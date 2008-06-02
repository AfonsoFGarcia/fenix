package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.GradeScale;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import org.apache.commons.lang.StringUtils;

public class InfoDegree extends InfoObject implements Comparable {

    private final DomainReference<Degree> degreeDomainReference;

    private boolean showEnVersion = (Language.getUserLanguage() == Language.en);

    public InfoDegree(final Degree degree) {
	degreeDomainReference = new DomainReference<Degree>(degree);
    }

    public Degree getDegree() {
        return degreeDomainReference == null ? null : degreeDomainReference.getObject();
    }

    public String toString() {
	return getDegree().toString();
    }

    public String getSigla() {
	return getDegree().getSigla();
    }

    public String getNome() {
	return showEnVersion && !StringUtils.isEmpty(getNameEn()) ? getNameEn() : getDegree().getNome();
    }

    public String getNameEn() {
	return getDegree().getNameEn();
    }

    public boolean equals(Object obj) {
	return obj instanceof InfoDegree && getDegree().equals(((InfoDegree) obj).getDegree());
    }

    public boolean isBolonhaDegree() {
	return getDegree().isBolonhaDegree();
    }

    public Enum getTipoCurso() {
	return getDegree().getTipoCurso();
    }

    public List getInfoDegreeCurricularPlans() {
	final List<InfoDegreeCurricularPlan> infoDegreeCurricularPlans = new ArrayList<InfoDegreeCurricularPlan>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegree().getDegreeCurricularPlansSet()) {
	    infoDegreeCurricularPlans.add(InfoDegreeCurricularPlan
		    .newInfoFromDomain(degreeCurricularPlan));
	}
	return infoDegreeCurricularPlans;
    }

    public int compareTo(Object arg0) {
	InfoDegree degree = (InfoDegree) arg0;
	return this.getNome().compareTo(degree.getNome());
    }

    public List getInfoDegreeInfos() {
	final List<InfoDegreeInfo> infoDegreeInfos = new ArrayList<InfoDegreeInfo>();
	for (final DegreeInfo degreeInfo : getDegree().getDegreeInfosSet()) {
	    infoDegreeInfos.add(InfoDegreeInfo.newInfoFromDomain(degreeInfo));
	}
	return infoDegreeInfos;
    }

    public GradeScale getGradeScale() {
	return getDegree().getGradeScale();
    }

    public static InfoDegree newInfoFromDomain(final Degree degree) {
	return degree == null ? null : new InfoDegree(degree);
    }

    @Override
    public Integer getIdInternal() {
	return getDegree().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

}
