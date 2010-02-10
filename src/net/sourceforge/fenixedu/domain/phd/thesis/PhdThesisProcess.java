package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.AddJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.AddPresidentJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.DeleteJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadFinalThesisDocument;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadProvisionalThesisDocument;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadThesisRequirement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.JuryDocumentsDownload;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.JuryReporterFeedbackExternalUpload;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.JuryReporterFeedbackUpload;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.PhdThesisActivity;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.PrintJuryElementsDocument;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RejectJuryElements;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RequestJuryElements;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RequestJuryReviews;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SubmitJuryElementsDocuments;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SubmitThesis;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SwapJuryElementsOrder;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.ValidateJury;

import org.joda.time.DateTime;

public class PhdThesisProcess extends PhdThesisProcess_Base {

    @StartActivity
    static public class RequestThesis extends PhdThesisActivity {

	@Override
	public void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	    // Activity on main process ensures access control
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    final PhdThesisProcess result = new PhdThesisProcess();
	    final PhdThesisProcessBean phdThesisProcessBean = (PhdThesisProcessBean) object;
	    result.createState(PhdThesisProcessStateType.NEW, userView.getPerson(), phdThesisProcessBean.getRemarks());
	    return result;
	}
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new RequestJuryElements());
	activities.add(new SubmitJuryElementsDocuments());
	activities.add(new AddJuryElement());
	activities.add(new DeleteJuryElement());
	activities.add(new SwapJuryElementsOrder());
	activities.add(new AddPresidentJuryElement());
	activities.add(new ValidateJury());
	activities.add(new PrintJuryElementsDocument());
	activities.add(new RejectJuryElements());
	activities.add(new SubmitThesis());
	activities.add(new DownloadProvisionalThesisDocument());
	activities.add(new DownloadFinalThesisDocument());
	activities.add(new DownloadThesisRequirement());
	activities.add(new RequestJuryReviews());
	activities.add(new JuryDocumentsDownload());
	activities.add(new JuryReporterFeedbackUpload());
	activities.add(new JuryReporterFeedbackExternalUpload());
    }

    private PhdThesisProcess() {
	super();
    }

    public boolean isJuryValidated() {
	return getWhenJuryValidated() != null;
    }

    public void swapJuryElementsOrder(ThesisJuryElement e1, ThesisJuryElement e2) {
	if (hasThesisJuryElements(e1) && hasThesisJuryElements(e2)) {
	    final Integer order1 = e1.getElementOrder();
	    final Integer order2 = e2.getElementOrder();
	    e1.setElementOrder(order2);
	    e2.setElementOrder(order1);
	}
    }

    public void deleteJuryElement(ThesisJuryElement element) {
	if (hasThesisJuryElements(element)) {
	    final Integer elementOrder = element.getElementOrder();
	    element.delete();
	    reorderJuryElements(elementOrder);
	}
    }

    private void reorderJuryElements(Integer removedElementOrder) {
	for (final ThesisJuryElement element : getOrderedThesisJuryElements()) {
	    if (element.getElementOrder().compareTo(removedElementOrder) > 0) {
		element.setElementOrder(Integer.valueOf(element.getElementOrder().intValue() - 1));
	    }
	}
    }

    public TreeSet<ThesisJuryElement> getOrderedThesisJuryElements() {
	final TreeSet<ThesisJuryElement> result = new TreeSet<ThesisJuryElement>(ThesisJuryElement.COMPARATOR_BY_ELEMENT_ORDER);
	result.addAll(getThesisJuryElementsSet());
	return result;
    }

    public void createState(PhdThesisProcessStateType type, Person person, String remarks) {
	new PhdThesisProcessState(this, type, person, remarks);
    }

    @Override
    public Person getPerson() {
	return getIndividualProgramProcess().getPerson();
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return false;
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/PhdResources").getString(getClass().getSimpleName());
    }

    @Override
    public PhdThesisProcessState getMostRecentState() {
	return (PhdThesisProcessState) super.getMostRecentState();
    }

    @Override
    public PhdThesisProcessStateType getActiveState() {
	return (PhdThesisProcessStateType) super.getActiveState();
    }

    // TODO: find clean solution to return specific documents
    // grouped??
    public PhdProgramProcessDocument getProvisionalThesisDocument() {
	return getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.PROVISIONAL_THESIS);
    }

    public boolean hasProvisionalThesisDocument() {
	return getProvisionalThesisDocument() != null;
    }

    public PhdProgramProcessDocument getFinalThesisDocument() {
	return getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.FINAL_THESIS);
    }

    public boolean hasFinalThesisDocument() {
	return getFinalThesisDocument() != null;
    }

    public PhdProgramProcessDocument getThesisRequirementDocument() {
	return getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.THESIS_REQUIREMENT);
    }

    public boolean hasThesisRequirementDocument() {
	return getThesisRequirementDocument() != null;
    }

    boolean isConcluded() {
	return getActiveState() == PhdThesisProcessStateType.CONCLUDED;
    }

    public PhdProgramProcessDocument getJuryElementsDocument() {
	return getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.JURY_ELEMENTS);
    }

    public PhdProgramProcessDocument getJuryPresidentDocument() {
	return getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.JURY_PRESIDENT_ELEMENT);
    }

    public DateTime getWhenRequestJury() {
	return getLastExecutionDateOf(RequestJuryElements.class);
    }

    public DateTime getWhenReceivedJury() {
	return getLastExecutionDateOf(SubmitJuryElementsDocuments.class);
    }

    public DateTime getWhenRequestedJuryReviews() {
	return getLastExecutionDateOf(RequestJuryReviews.class);
    }

    public PhdParticipant getParticipant(final Person person) {
	return getIndividualProgramProcess().getParticipant(person);
    }

    public boolean isParticipant(final Person person) {
	return getIndividualProgramProcess().isParticipant(person);
    }

    public ThesisJuryElement getThesisJuryElement(Person person) {
	for (final ThesisJuryElement element : getThesisJuryElements()) {
	    if (element.isFor(person)) {
		return element;
	    }
	}
	return null;
    }

    public List<PhdProgramProcessDocument> getThesisDocumentsToFeedback() {
	final List<PhdProgramProcessDocument> documents = new ArrayList<PhdProgramProcessDocument>(3);
	documents.add(getCandidacyProcess().getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.CV));
	documents.add(getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.PROVISIONAL_THESIS));

	// TODO: add remaining documents here (Abstract /resume)

	return documents;
    }

    private PhdProgramProcess getCandidacyProcess() {
	return getIndividualProgramProcess().getCandidacyProcess();
    }

    public String getProcessNumber() {
	return getIndividualProgramProcess().getProcessNumber();
    }

}
