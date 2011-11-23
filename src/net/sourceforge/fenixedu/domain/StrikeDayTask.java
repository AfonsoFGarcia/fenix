package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecordMonthIndex;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatus;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.smtp.EmailSender;
import pt.utl.ist.fenix.tools.util.StringNormalizer;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class StrikeDayTask extends StrikeDayTask_Base {

    private static class ReportFile {

	StringBuilder builder = new StringBuilder();
	final String filename;
	final String fileType;

	private ReportFile(final String filename, final String fileType) {
	    this.filename = filename;
	    this.fileType = fileType;
	}

	public byte[] getAttachment() {
	    return builder.toString().getBytes();
	}

    }

    private static final Set<String> possibleStrikeKeywords = new HashSet<String>();

    private static void addStrikeKeyword(final String keyword) {
	possibleStrikeKeywords.add(normalize(keyword));
    }

    static {
	addStrikeKeyword("GREVE");
	addStrikeKeyword("FALTA");
	addStrikeKeyword("FALTEI");
	addStrikeKeyword("N�O HOUVE AULA");
	addStrikeKeyword("N�O DEI AULA");
	addStrikeKeyword("N�O COMPARECI");
	addStrikeKeyword("AULA N�O LECCIONADA");
    }

    @Override
    public void runTask() {
	Language.setLocale(Language.getDefaultLocale());
	if (isStrikeDay()) {
	    report();
	}
    }

    private boolean isStrikeDay() {
	final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
	final LocalDate today = new LocalDate();
	for (final StrikeDay strikeDay : rootDomainObject.getStrikeDaySet()) {
	    if (strikeDay.matches(today)) {
		return true;
	    }
	}
	return false;
    }

    public static void report() {
	final StringBuilder report = new StringBuilder();
	final Set<ReportFile> reportFiles = new HashSet<StrikeDayTask.ReportFile>();

	report.append("Dados da Grave do Dia Gerado em: ");
	report.append(new DateTime().toString("yyyy-MM-dd HH:mm:ss") + "\n\n\n");

	reportEmployees(report, reportFiles);
	reportTeachers(report, reportFiles);

	sendReport(report, reportFiles);
    }

    private static void reportEmployees(final StringBuilder report, final Set<ReportFile> reportFiles) {
	final Map<Assiduousness, List<AssiduousnessRecord>> result = new HashMap<Assiduousness, List<AssiduousnessRecord>>();
	final Map<Assiduousness, List<AssiduousnessRecord>> leaves = new HashMap<Assiduousness, List<AssiduousnessRecord>>();
	final Set<Assiduousness> requestedForExternal = new HashSet<Assiduousness>();

	DateTime today = new DateTime();
	LocalDate date = today.toLocalDate();

	getClockingsAndMissingClockings(report, date, result, leaves, requestedForExternal);
	int personsTotal = 0;
	int adistTotal = 0;
	int personsIn = 0;
	int personsOut = 0;

	final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
	for (Assiduousness assiduousness : rootDomainObject.getAssiduousnesss()) {
	    if (!requestedForExternal.contains(assiduousness)) {
		Map<LocalDate, WorkSchedule> workScheduleMap = assiduousness.getWorkSchedulesBetweenDates(date, date);
		if (workScheduleMap.get(date) != null
			&& !workScheduleMap.get(date).getWorkScheduleType().getScheduleClockingType()
				.equals(ScheduleClockingType.NOT_MANDATORY_CLOCKING) && assiduousness.isStatusActive(date, date)) {
		    if (assiduousness.getCurrentStatus() == null) {
			report.append("??? Sem status: " + assiduousness.getEmployee().getEmployeeNumber()  + "\n");
		    } else {
			personsTotal = personsTotal + 1;
			if (assiduousness.getCurrentStatus().isADISTEmployee()
				|| assiduousness.getCurrentStatus().isIstIdEmployee()) {
			    adistTotal = adistTotal + 1;
			} else {
			    List<AssiduousnessRecord> leavesList = leaves.get(assiduousness);
			    if (leavesList == null) {
				List personClockings = result.get(assiduousness);
				if (personClockings == null || personClockings.size() == 0) {
				    personsOut = personsOut + 1;
				    // report.append(assiduousness.getEmployee().getEmployeeNumber());
				} else {
				    personsIn = personsIn + 1;
				}
			    }
			}
		    }
		}
	    }
	}
	// report.append("Total de funcion�rios ADIST: " + adistTotal);
	report.append("### Funcion�rios N�o Docentes\n\n");
	report.append("1) Total de funcion�rios que deveriam picar o ponto: " + (personsTotal - adistTotal) +"\n");
	report.append("2) Total de funcion�rios com justifica��es para o ponto: " + leaves.size() +"\n");
	report.append("3) Total de funcion�rios que registaram o ponto: " + personsIn +"\n");
	report.append("4) Total de funcion�rios em greve (1 - 2 - 3): " + personsOut +"\n\n\n");
    }

    private static void getClockingsAndMissingClockings(final StringBuilder report, LocalDate day,
	    final Map<Assiduousness, List<AssiduousnessRecord>> result,
	    final Map<Assiduousness, List<AssiduousnessRecord>> leaves,
	    final Set<Assiduousness> requestedForExternal) {
	Interval interval = new Interval(day.toDateTime(new LocalTime(5, 30, 0, 0)),
		day.toDateTime(Assiduousness.defaultEndWorkDay));

	final Set<AssiduousnessRecord> assiduousnessRecordBetweenDates = AssiduousnessRecordMonthIndex
		.getAssiduousnessRecordBetweenDates(interval.getStart(), interval.getEnd().plusDays(1));
	for (AssiduousnessRecord assiduousnessRecord : assiduousnessRecordBetweenDates) {
	    AssiduousnessStatus currentStatus = assiduousnessRecord.getAssiduousness().getCurrentStatus();
	    if (currentStatus != null && !currentStatus.isADISTEmployee() && !currentStatus.isIstIdEmployee()) {
		Map<LocalDate, WorkSchedule> workScheduleMap = assiduousnessRecord.getAssiduousness()
			.getWorkSchedulesBetweenDates(day, day);
		if (workScheduleMap.get(day) != null
			&& !workScheduleMap.get(day).getWorkScheduleType().getScheduleClockingType()
				.equals(ScheduleClockingType.NOT_MANDATORY_CLOCKING)) {
		    if ((assiduousnessRecord.getAssiduousness().isStatusActive(day, day))
			    && (assiduousnessRecord.isClocking() || assiduousnessRecord.isMissingClocking())
			    && (!assiduousnessRecord.isAnulated()) && interval.contains(assiduousnessRecord.getDate())) {
			List<AssiduousnessRecord> list = result.get(assiduousnessRecord.getAssiduousness());
			if (list == null) {
			    list = new ArrayList<AssiduousnessRecord>();
			}
			list.add(assiduousnessRecord);
			result.put(assiduousnessRecord.getAssiduousness(), list);
		    } else if ((assiduousnessRecord.getAssiduousness().isStatusActive(day, day))
			    && (assiduousnessRecord.isLeave()) && (!assiduousnessRecord.isAnulated())) {
			Leave leave = (Leave) assiduousnessRecord;
			if (leave.getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE)) {
			    Interval leaveInterval = new Interval(assiduousnessRecord.getDate(), leave.getEndDate().plus(1));
			    if (leaveInterval.overlaps(interval)) {

				if (leave.getJustificationMotive().getJustificationGroup() != null
					&& leave.getJustificationMotive().getJustificationGroup()
						.equals(JustificationGroup.ABSENCES)) {
				    requestedForExternal.add(assiduousnessRecord.getAssiduousness());
				} else {
				    List<AssiduousnessRecord> list = leaves.get(assiduousnessRecord.getAssiduousness());
				    if (list == null) {
					list = new ArrayList<AssiduousnessRecord>();
				    }
				    list.add(assiduousnessRecord);
				    leaves.put(assiduousnessRecord.getAssiduousness(), list);
				    if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("Greve")) {
					report.append("Greve - vai ignorar"
						+ assiduousnessRecord.getAssiduousness().getEmployee().getEmployeeNumber() + "\n");
				    }
				}
			    }
			}
		    }
		}
	    }
	}
	for (Assiduousness assiduousness : leaves.keySet()) {
	    result.remove(assiduousness);
	}
    }

    private static void reportTeachers(final StringBuilder report, final Set<ReportFile> reportFiles) {
	int teachersToday = 0;
	int noSummaryCounter = 0;
	int teachersOnStike = 0;
	int teachersNotOnStrike = 0;

	final Set<Teacher> noSummaryTeachers = new HashSet<Teacher>();
	final Set<Teacher> hasSummaryTeachers = new HashSet<Teacher>();
	final Set<Teacher> hasLessonsTeachers = new HashSet<Teacher>();

	StringBuffer stringBuffer = new StringBuffer();
	StringBuffer stringBufferS = new StringBuffer();
	StringBuffer stringBufferT = new StringBuffer();

	final YearMonthDay strikeDate = new YearMonthDay();

	final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
	for (Teacher teacher : rootDomainObject.getTeachersSet()) {

	    final Set<Shift> shifts = new HashSet<Shift>();
	    boolean hasSomeLessonToday = false;
	    boolean hasSomeStrikeSummary = false;
	    boolean hasSomeOtherSummary = false;
	    for (final Professorship professorship : teacher.getProfessorshipsSet()) {
		final ExecutionCourse executionCourse = professorship.getExecutionCourse();
		if (!executionCourse.getExecutionPeriod().isCurrent()) {
		    continue;
		}
		if (teacher.getCurrentCategory() == null) {
		    // isto foi for causa dos docentes externos que n�o devem ser contabilizados.
//		    if (teacher.getPerson().getEmployee() != null) {
//			report.append("Employee? " + teacher.getPerson().getEmployee().getEmployeeNumber() + "\n");
//		    }
		} else {
		    for (final Lesson lesson : executionCourse.getLessons()) {
			for (final YearMonthDay yearMonthDay : lesson.getAllLessonDates()) {
			    if (yearMonthDay.isEqual(strikeDate) && hasSomeSummaryFor(professorship, teacher, lesson.getShift())) {
				hasSomeLessonToday = true;
				hasLessonsTeachers.add(teacher);
			    }
			}
			for (final LessonInstance lessonInstance : lesson.getLessonInstancesSet()) {
			    if (lessonInstance.getDay().equals(strikeDate)) {
				final Summary summary = lessonInstance.getSummary();
				if (summary != null) {
				    // report.append("-----------------------------");
				    // report.append("---> " +
				    // summary.getTitle());
				    // report.append("---> " +
				    // summary.getSummaryText());
				    // stringBuffer.append("\n<br/>\n<br/>").append(summary.getProfessorship().getPerson().getIstUsername())
				    // .append("\n<br/>").append(summary.getTitle()).append("\n<br/>").append(summary.getSummaryText());
				    final MultiLanguageString titleMLS = summary.getTitle();
				    final String title = titleMLS.getContent(Language.pt);
				    final MultiLanguageString summaryText = summary.getSummaryText();
				    final String text = summaryText.getContent(Language.pt);

				    // verificar "n�o fiz greve"
				    if ((summary.getProfessorship() == professorship || summary.getTeacher() == teacher)) {
					if (flagText(title) || flagText(text)) {
					    hasSomeStrikeSummary = true;

					    stringBufferS.append("\n<br/>\n<br/>")
						    .append(summary.getProfessorship().getPerson().getIstUsername())
						    .append("\n<br/>").append(summary.getTitle()).append("\n<br/>")
						    .append(summary.getSummaryText());
					} else {
					    hasSomeOtherSummary = true;

					    stringBuffer.append("\n<br/>\n<br/>")
						    .append(summary.getProfessorship().getPerson().getIstUsername())
						    .append("\n<br/>").append(summary.getTitle()).append("\n<br/>")
						    .append(summary.getSummaryText());
					}
					hasSummaryTeachers.add(teacher);
				    }
				}
			    }
			}
		    }
		}
	    }

	    if (hasSomeLessonToday) {
		teachersToday++;
	    }
	    if (hasSomeStrikeSummary) {
		teachersOnStike++;
	    }
	    if (hasSomeOtherSummary && !hasSomeStrikeSummary) {
		teachersNotOnStrike++;
	    }
	    if (hasSomeLessonToday && !hasSomeOtherSummary && !hasSomeStrikeSummary) {
		noSummaryCounter++;
	    }

	}

	final ReportFile reportFile = new ReportFile("OutrosSumarios.html", "text/html");
	final ReportFile reportFileS = new ReportFile("SumariosComGreve.html", "text/html");
	final ReportFile reportFileT = new ReportFile("DocentesSemSumarios.txt", "text/plain");

	reportFile.builder.append("<html><body>");
	reportFileS.builder.append("<html><body>");

	hasLessonsTeachers.removeAll(hasSummaryTeachers);
	for (Teacher teacher : hasLessonsTeachers) {
	    reportFileT.builder.append(teacher.getPerson().getEmployee().getEmployeeNumber() + "\t" + teacher.getPerson().getName() + "\n");
	}
	reportFile.builder.append(stringBuffer.toString());
	reportFileS.builder.append(stringBufferS.toString());

	reportFile.builder.append("</html></body>");
	reportFileS.builder.append("</html></body>");

	reportFiles.add(reportFile);
	reportFiles.add(reportFileS);
	reportFiles.add(reportFileT);

	report.append("### Docentes\n\n");
	report.append("Total docentes com sum�rios a preencher: " + teachersToday + "\n");
	report.append("Total docentes sem sum�rio: " + noSummaryCounter + "\n");
	report.append("Total docentes com 'sum�rio de greve': " + teachersOnStike + "\n");
	report.append("Total docentes com sum�rios sem a palavra 'greve': " + teachersNotOnStrike + "\n");
	report.append("Total docentes em greve: " + (teachersOnStike + noSummaryCounter) + "\n");
    }

    private static boolean hasSomeSummaryFor(final Professorship professorship, final Teacher teacher, final Shift shift) {
	for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
	    for (final LessonInstance lessonInstance : lesson.getLessonInstancesSet()) {
		final Summary summary = lessonInstance.getSummary();
		if (summary != null && (summary.getProfessorship() == professorship || summary.getTeacher() == teacher)) {
		    return true;
		}
	    }
	}
	return false;
    }

    private static boolean flagText(final String text) {
	if (text != null) {
	    final String n = normalize(text);
	    for (final String keyword : possibleStrikeKeywords) {
		if (n.indexOf(keyword) >= 0) {
		    return true;
		}
	    }
	}
	return false;
    }

    private static String normalize(final String text) {
	return text == null ? null : StringNormalizer.normalize(text.toLowerCase()).toLowerCase();
    }

    private static void sendReport(final StringBuilder report, final Set<ReportFile> reportFiles) {
	final String fromName = "Sistema F�nix";
	final String fromAddress = "noreply@ist.utl.pt";
	final String[] replyTos = new String[0];
	final Collection<String> toAddresses = new ArrayList<String>();
	toAddresses.add("luis.cruz@ist.utl.pt");
	toAddresses.add("susana.fernandes@ist.utl.pt");
	final Collection<String> ccAddresses = Collections.emptyList();
	final Collection<String> bccAddresses = Collections.emptyList();
	final String subject = "Estat�sticas de Greve: " + new DateTime().toString("yyyy-MM-dd");
	final String body = report.toString();

	final int s = reportFiles.size();
	final byte[][] attachments = new byte[s][0];
	final String[] attachmentFilenames = new String[s];
	final String[] attachmentTypes = new String[s];
	int i = 0;
	for (final ReportFile reportFile : reportFiles) {
	    attachments[i] = reportFile.getAttachment();
	    attachmentFilenames[i] = reportFile.filename;
	    attachmentTypes[i] = reportFile.fileType;
	    i++;
	}

	EmailSender.send(fromName, fromAddress, replyTos, toAddresses, ccAddresses, bccAddresses, subject, body,
		attachments, attachmentFilenames, attachmentTypes);
    }

}
