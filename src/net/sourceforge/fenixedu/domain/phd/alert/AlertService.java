package net.sourceforge.fenixedu.domain.phd.alert;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.MasterDegreeAdministrativeOfficeGroup;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProcessesManager;
import net.sourceforge.fenixedu.domain.phd.permissions.PhdPermissionType;
import net.sourceforge.fenixedu.domain.util.email.Message;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class AlertService {

    static private final String PREFIX_PHD_LABEL = "label.phds";
    static private final String PHD_RESOURCES = "resources.PhdResources";

    public static String getSubjectPrefixed(PhdIndividualProgramProcess process, String subjectKey) {
	return getProcessNumberPrefix(process) + getMessageFromResource(subjectKey);
    }

    static public String getProcessNumberPrefix(PhdIndividualProgramProcess process) {
	return "[" + getMessageFromResource(PREFIX_PHD_LABEL) + " - " + process.getProcessNumber() + "] ";
    }

    static public String getMessageFromResource(String key) {
	return ResourceBundle.getBundle(PHD_RESOURCES, Language.getLocale()).getString(key);
    }

    static private String getBodyCommonText(final PhdIndividualProgramProcess process) {
	final StringBuilder builder = new StringBuilder();

	builder.append("------------------------------------------------------\n");

	if (process.getPerson().hasStudent()) {
	    builder.append(getSlotLabel(PhdIndividualProgramProcess.class, "student.number"));
	    builder.append(": ").append(process.getPerson().getStudent().getNumber());
	}
	
	builder.append(getSlotLabel(PhdIndividualProgramProcess.class, "processNumber"));
	builder.append(": ").append(process.getPhdIndividualProcessNumber().getFullProcessNumber()).append("\n");

	builder.append(getSlotLabel(PhdIndividualProgramProcess.class, "person.name"));
	builder.append(": ").append(process.getPerson().getName()).append("\n");
	
	builder.append(getSlotLabel(PhdIndividualProgramProcess.class, "phdProgram"));
	if (process.hasPhdProgram()) {
	    builder.append(": ").append(process.getPhdProgram().getName());
	}
	builder.append("\n");

	builder.append(getSlotLabel(PhdIndividualProgramProcess.class, "activeState"));
	builder.append(": ").append(process.getActiveState().getLocalizedName()).append("\n");

	if (process.hasCandidacyProcess()) {
	    builder.append(getMessageFromResource("label.phd.candidacy")).append(": ");
	    builder.append(process.getCandidacyProcess().getActiveState().getLocalizedName()).append("\n");
	}

	if (process.hasSeminarProcess()) {
	    builder.append(getMessageFromResource("label.phd.publicPresentationSeminar")).append(": ");
	    builder.append(process.getSeminarProcess().getActiveState().getLocalizedName()).append("\n");
	}

	if (process.hasThesisProcess()) {
	    builder.append(getMessageFromResource("label.phd.thesis")).append(": ");
	    builder.append(process.getThesisProcess().getActiveState().getLocalizedName()).append("\n");
	}

	builder.append(getSlotLabel(PhdIndividualProgramProcess.class, "executionYear"));
	builder.append(": ").append(process.getExecutionYear().getQualifiedName()).append("\n");

	builder.append("------------------------------------------------------\n\n");

	return builder.toString();
    }

    static public String getBodyText(PhdIndividualProgramProcess process, String bodyText) {
	return getBodyCommonText(process) + getMessageFromResource(bodyText);
    }

    static private String getSlotLabel(Class<? extends DomainObject> clazz, String slotName) {
	return getMessageFromResource("label." + clazz.getName() + "." + slotName);
    }

    static public void alertStudent(PhdIndividualProgramProcess process, String subjectKey, String bodyKey) {
	final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, false);

	alertBean.setSubject(getSubjectPrefixed(process, subjectKey));
	alertBean.setBody(getBodyText(process, bodyKey));
	alertBean.setFireDate(new LocalDate());
	alertBean.setTargetGroup(new FixedSetGroup(process.getPerson()));

	new PhdCustomAlert(alertBean);
    }

    static public void alertGuiders(PhdIndividualProgramProcess process, String subjectKey, String bodyKey) {

	final Set<Person> toNotify = new HashSet<Person>();

	for (final PhdParticipant guiding : process.getGuidingsAndAssistantGuidings()) {
	    if (guiding.isInternal()) {
		toNotify.add(((InternalPhdParticipant) guiding).getPerson());
	    } else {
		new Message(RootDomainObject.getInstance().getSystemSender(), Collections.EMPTY_LIST, Collections.EMPTY_LIST,
			subjectKey, bodyKey, Collections.singleton(guiding.getEmail()));
	    }
	}

	final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, false);
	alertBean.setSubject(getSubjectPrefixed(process, subjectKey));
	alertBean.setBody(getBodyText(process, bodyKey));
	alertBean.setFireDate(new LocalDate());
	alertBean.setTargetGroup(new FixedSetGroup(toNotify));

	new PhdCustomAlert(alertBean);

    }

    static public void alertAcademicOffice(PhdIndividualProgramProcess process, String subjectKey, String bodyKey) {
	final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, true);
	alertBean.setSubject(getSubjectPrefixed(process, subjectKey));
	alertBean.setBody(getBodyText(process, bodyKey));
	alertBean.setFireDate(new LocalDate());
	alertBean.setTargetGroup(new MasterDegreeAdministrativeOfficeGroup());

	new PhdCustomAlert(alertBean);
    }

    static public void alertAcademicOffice(PhdIndividualProgramProcess process, PhdPermissionType permissionType,
	    String subjectKey, String bodyKey) {
	final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, true);
	alertBean.setSubject(getSubjectPrefixed(process, subjectKey));
	alertBean.setBody(getBodyText(process, bodyKey));
	alertBean.setFireDate(new LocalDate());
	alertBean.setTargetGroup(getTargetGroup(permissionType));

	new PhdCustomAlert(alertBean);
    }

    static protected AdministrativeOffice getAdministrativeOffice() {
	return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE);
    }

    static protected PhdProcessesManager getProcessesManager() {
	return getAdministrativeOffice().getPhdProcessesManager();
    }

    static private Group getTargetGroup(PhdPermissionType permissionType) {
	final Group group = getProcessesManager().getPermissionGroup(permissionType);
	return group != null ? group : new MasterDegreeAdministrativeOfficeGroup();
    }

    static public void alertCoordinator(PhdIndividualProgramProcess process, String subjectKey, String bodyKey) {
	final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, false);
	alertBean.setSubject(getSubjectPrefixed(process, subjectKey));
	alertBean.setBody(getBodyText(process, bodyKey));
	alertBean.setTargetGroup(new FixedSetGroup(process.getCoordinatorsFor(ExecutionYear.readCurrentExecutionYear())));
	alertBean.setFireDate(new LocalDate());

	new PhdCustomAlert(alertBean);
    }

    static public void alertCoordinator(PhdIndividualProgramProcess process, AlertMessage subject, AlertMessage body) {
	final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, false);
	alertBean.setSubject(getSubjectPrefixed(process, subject));
	alertBean.setBody(getBodyText(process, body));
	alertBean.setTargetGroup(new FixedSetGroup(process.getCoordinatorsFor(ExecutionYear.readCurrentExecutionYear())));
	alertBean.setFireDate(new LocalDate());
	new PhdCustomAlert(alertBean);
    }

    static public String getSubjectPrefixed(PhdIndividualProgramProcess process, AlertMessage message) {
	return getProcessNumberPrefix(process) + message.getMessage();
    }

    static public String getBodyText(PhdIndividualProgramProcess process, AlertMessage body) {
	return getBodyCommonText(process) + body.getMessage();
    }

    static public class AlertMessage {
	private String label;
	private Object[] args;
	private boolean isKey = true;

	public AlertMessage label(String label) {
	    this.label = label;
	    return this;
	}

	public AlertMessage args(Object... args) {
	    this.args = args;
	    return this;
	}

	public AlertMessage isKey(boolean value) {
	    this.isKey = value;
	    return this;
	}

	public String getMessage() {
	    return isKey ? MessageFormat.format(getMessageFromResource(label), args) : label;
	}

	static public AlertMessage create(String label, Object... args) {
	    return new AlertMessage().label(label).args(args);
	}

	static public String get(String label, Object... args) {
	    return new AlertMessage().label(label).args(args).getMessage();
	}
    }
}
