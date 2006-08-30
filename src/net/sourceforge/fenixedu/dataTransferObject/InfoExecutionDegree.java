package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

public class InfoExecutionDegree extends InfoObject {

	public static final Comparator<InfoExecutionDegree> COMPARATOR_BY_DEGREE_TYPE_AND_NAME = new ComparatorChain();
	static {
		((ComparatorChain) COMPARATOR_BY_DEGREE_TYPE_AND_NAME).addComparator(new BeanComparator("executionDegree.degreeCurricularPlan.degree.tipoCurso"));
		((ComparatorChain) COMPARATOR_BY_DEGREE_TYPE_AND_NAME).addComparator(new BeanComparator("executionDegree.degreeCurricularPlan.degree.nome"));
	}

    private final DomainReference<ExecutionDegree> executionDegreeDomainReference;

    private String qualifiedName;

    private boolean getNextExecutionYear = false;

    public InfoExecutionDegree(final ExecutionDegree executionDegree) {
    	executionDegreeDomainReference = new DomainReference<ExecutionDegree>(executionDegree);
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegreeDomainReference == null ? null : executionDegreeDomainReference.getObject();
    }

    public InfoExecutionYear getInfoExecutionYear() {
        return InfoExecutionYear.newInfoFromDomain(getNextExecutionYear ?
        		getExecutionDegree().getExecutionYear().getNextExecutionYear() : getExecutionDegree().getExecutionYear());
    }

    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return InfoDegreeCurricularPlan.newInfoFromDomain(getExecutionDegree().getDegreeCurricularPlan());
    }

    public Boolean getTemporaryExamMap() {
        return getExecutionDegree().getTemporaryExamMap();
    }

    public InfoCampus getInfoCampus() {
        return InfoCampus.newInfoFromDomain(getExecutionDegree().getCampus());
    }

    public List<InfoCoordinator> getCoordinatorsList() {
    	final List<InfoCoordinator> infoCoordinators = new ArrayList<InfoCoordinator>();
    	for (final Coordinator coordinator : getExecutionDegree().getCoordinatorsListSet()) {
    		infoCoordinators.add(InfoCoordinator.newInfoFromDomain(coordinator));
    	}
        return infoCoordinators;
    }

    public InfoPeriod getInfoPeriodExamsFirstSemester() {
        return InfoPeriod.newInfoFromDomain(getExecutionDegree().getPeriodExamsFirstSemester());
    }

    public InfoPeriod getInfoPeriodExamsSecondSemester() {
        return InfoPeriod.newInfoFromDomain(getExecutionDegree().getPeriodExamsSecondSemester());
    }

    public InfoPeriod getInfoPeriodLessonsFirstSemester() {
        return InfoPeriod.newInfoFromDomain(getExecutionDegree().getPeriodLessonsFirstSemester());
    }

    public InfoPeriod getInfoPeriodLessonsSecondSemester() {
        return InfoPeriod.newInfoFromDomain(getExecutionDegree().getPeriodLessonsSecondSemester());
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public boolean isBolonha() {
        return getExecutionDegree().isBolonha();
    }

    public boolean equals(Object obj) {
    	return obj != null && getExecutionDegree() == ((InfoExecutionDegree) obj).getExecutionDegree();
    }

    public String toString() {
    	return getExecutionDegree().toString();
    }

    public static List buildLabelValueBeansForList(List executionDegrees,
            MessageResources messageResources) {
        List copyExecutionDegrees = new ArrayList();
        copyExecutionDegrees.addAll(executionDegrees);
        List result = new ArrayList();
        Iterator iter = executionDegrees.iterator();
        while (iter.hasNext()) {
            final InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iter.next();
            List equalDegrees = (List) CollectionUtils.select(copyExecutionDegrees, new Predicate() {
                public boolean evaluate(Object arg0) {
                    InfoExecutionDegree infoExecutionDegreeElem = (InfoExecutionDegree) arg0;
                    if (infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla()
                            .equals(
                                    infoExecutionDegreeElem.getInfoDegreeCurricularPlan()
                                            .getInfoDegree().getSigla())) {
                        return true;
                    }
                    return false;
                }
            });
            if (equalDegrees.size() == 1) {
                copyExecutionDegrees.remove(infoExecutionDegree);

                String degreeType = null;
                if (messageResources != null) {
                    degreeType = messageResources.getMessage(infoExecutionDegree
                            .getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso().toString());
                }
                if (degreeType == null)
                    degreeType = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                            .getTipoCurso().toString();

                result.add(new LabelValueBean(degreeType + "  "
                        + infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome(),
                        infoExecutionDegree.getIdInternal().toString()));
            } else {
                String degreeType = null;
                if (messageResources != null) {
                    degreeType = messageResources.getMessage(infoExecutionDegree
                            .getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso().toString());
                }
                if (degreeType == null)
                    degreeType = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                            .getTipoCurso().toString();

                result.add(new LabelValueBean(degreeType + "  "
                        + infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome()
                        + " - " + infoExecutionDegree.getInfoDegreeCurricularPlan().getName(),
                        infoExecutionDegree.getIdInternal().toString()));
            }
        }
        return result;

    }

    public static InfoExecutionDegree newInfoFromDomain(ExecutionDegree executionDegree) {
    	return executionDegree == null ? null : new InfoExecutionDegree(executionDegree);
    }

	@Override
	public Integer getIdInternal() {
		return getExecutionDegree().getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

	public void setGetNextExecutionYear(boolean getNextExecutionYear) {
		this.getNextExecutionYear = getNextExecutionYear;
	}

}
