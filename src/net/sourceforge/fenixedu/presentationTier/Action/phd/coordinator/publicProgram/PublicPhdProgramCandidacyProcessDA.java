package net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.publicProgram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.phd.candidacy.EPFLPhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice.PhdProgramCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/candidacies/phdProgramCandidacyProcess", module = "coordinator")
@Forwards(tileProperties = @Tile(navLocal = "/coordinator/localNavigationBar.jsp"), value = {

@Forward(name = "listProcesses", path = "/phd/coordinator/publicProgram/listProcesses.jsp"),

@Forward(name = "viewProcess", path = "/phd/coordinator/publicProgram/viewProcess.jsp"),

@Forward(name = "viewCandidacyRefereeLetter", path = "/phd/coordinator/publicProgram/viewCandidacyRefereeLetter.jsp")

})
public class PublicPhdProgramCandidacyProcessDA extends PhdProgramCandidacyProcessDA {

    public ActionForward listProcesses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	SelectPhdCandidacyPeriodBean selectPeriodBean = getSelectPhdCandidacyPeriodBean();

	if (selectPeriodBean == null) {
	    selectPeriodBean = new SelectPhdCandidacyPeriodBean(EPFLPhdCandidacyPeriod.getMostRecentCandidacyPeriod());
	}

	final Statistics statistics = new Statistics();

	final List<PublicPhdCandidacyBean> candidacyHashCodes = new ArrayList<PublicPhdCandidacyBean>();
	for (final PublicCandidacyHashCode hashCode : RootDomainObject.getInstance().getCandidacyHashCodesSet()) {
	    if (hashCode.isFromPhdProgram()) {
		final PhdProgramPublicCandidacyHashCode phdHashCode = (PhdProgramPublicCandidacyHashCode) hashCode;

		if (!selectPeriodBean.getPhdCandidacyPeriod().contains(phdHashCode.getWhenCreated())) {
		    continue;
		}

		statistics.plusTotalRequests();

		if (phdHashCode.hasCandidacyProcess()) {
		    statistics.plusTotalCandidates();
		}

		if (phdHashCode.hasCandidacyProcess() && phdHashCode.getPhdProgramCandidacyProcess().isValidatedByCandidate()) {
		    statistics.plusTotalValidated();
		}

		candidacyHashCodes.add(new PublicPhdCandidacyBean(phdHashCode));
	    }
	}

	request.setAttribute("candidacyHashCodes", candidacyHashCodes);
	request.setAttribute("statistics", statistics);
	request.setAttribute("selectPeriodBean", selectPeriodBean);

	RenderUtils.invalidateViewState();

	return mapping.findForward("listProcesses");
    }

    private SelectPhdCandidacyPeriodBean getSelectPhdCandidacyPeriodBean() {
	return (SelectPhdCandidacyPeriodBean) getObjectFromViewState("select-period-bean");
    }

    public ActionForward viewProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("hashCode", getDomainObject(request, "hashCodeId"));
	return mapping.findForward("viewProcess");
    }

    public ActionForward viewCandidacyRefereeLetter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyRefereeLetter", ((PhdCandidacyReferee) getDomainObject(request, "candidacyRefereeId"))
		.getLetter());
	return mapping.findForward("viewCandidacyRefereeLetter");
    }

    static public class SelectPhdCandidacyPeriodBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private PhdCandidacyPeriod candidacyPeriod;

	public PhdCandidacyPeriod getPhdCandidacyPeriod() {
	    return this.candidacyPeriod;
	}

	public void setPhdCandidacyPeriod(final PhdCandidacyPeriod candidacyPeriod) {
	    this.candidacyPeriod = candidacyPeriod;
	}

	public SelectPhdCandidacyPeriodBean(final PhdCandidacyPeriod candidacyPeriod) {
	    this.candidacyPeriod = candidacyPeriod;
	}

    }

    static public final class PhdCandidacyPeriodDataProvider extends AbstractDomainObjectProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
	    return getCandidacyPeriods();
	}

	private List<PhdCandidacyPeriod> getCandidacyPeriods() {
	    List<PhdCandidacyPeriod> candidacyPeriodList = new ArrayList<PhdCandidacyPeriod>();

	    CollectionUtils.select(RootDomainObject.getInstance().getCandidacyPeriods(), new Predicate() {

		@Override
		public boolean evaluate(Object arg0) {
		    return arg0 instanceof PhdCandidacyPeriod;
		}

	    }, candidacyPeriodList);

	    return candidacyPeriodList;
	}

    }

    static public class Statistics implements Serializable {
	static private final long serialVersionUID = 1L;

	private int totalRequests = 0;
	private int totalCandidates = 0;
	private int totalValidated = 0;

	Statistics() {
	}

	public int getTotalRequests() {
	    return totalRequests;
	}

	private void plusTotalRequests() {
	    totalRequests++;
	}

	public int getTotalCandidates() {
	    return totalCandidates;
	}

	private void plusTotalCandidates() {
	    totalCandidates++;
	}

	public int getTotalValidated() {
	    return totalValidated;
	}

	private void plusTotalValidated() {
	    totalValidated++;
	}
    }

    static public class PublicPhdCandidacyBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private PhdProgramPublicCandidacyHashCode hashCode;

	private String email;
	private String name;
	private String phdFocusArea;
	private boolean candidate;
	private boolean validated;

	public PublicPhdCandidacyBean() {
	}

	public PublicPhdCandidacyBean(final PhdProgramPublicCandidacyHashCode hashCode) {
	    setHashCode(hashCode);

	    setEmail(hashCode.getEmail());
	    setName(hashCode.hasCandidacyProcess() ? hashCode.getPerson().getName() : null);
	    setPhdFocusArea(hashCode.hasCandidacyProcess()
		    && hashCode.getIndividualProgramProcess().getPhdProgramFocusArea() != null ? hashCode
		    .getIndividualProgramProcess().getPhdProgramFocusArea().getName().getContent() : null);
	    setCandidate(hashCode.hasCandidacyProcess());
	    setValidated(hashCode.hasCandidacyProcess() ? hashCode.getPhdProgramCandidacyProcess().isValidatedByCandidate()
		    : false);
	}

	public PhdProgramPublicCandidacyHashCode getHashCode() {
	    return this.hashCode;
	}

	public void setHashCode(PhdProgramPublicCandidacyHashCode hashCode) {
	    this.hashCode = hashCode;
	}

	public String getEmail() {
	    return email;
	}

	public void setEmail(String email) {
	    this.email = email;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getPhdFocusArea() {
	    return phdFocusArea;
	}

	public void setPhdFocusArea(String phdFocusArea) {
	    this.phdFocusArea = phdFocusArea;
	}

	public boolean isCandidate() {
	    return candidate;
	}

	public void setCandidate(boolean candidate) {
	    this.candidate = candidate;
	}

	public boolean isValidated() {
	    return validated;
	}

	public void setValidated(boolean validated) {
	    this.validated = validated;
	}

    }
}
