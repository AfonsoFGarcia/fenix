package net.sourceforge.fenixedu.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Servico.credits.ReadAllTeacherCredits;
import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.domain.teacher.OtherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherAdviseService;
import net.sourceforge.fenixedu.domain.teacher.TeacherMasterDegreeService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class TeacherCreditsDocument extends TeacherCreditsDocument_Base {

    public TeacherCreditsDocument(Teacher teacher, ExecutionSemester executionSemester, TeacherService teacherService) {
	super();
	byte[] content = null;
	try {
	    content = getTeacherCreditsFile(teacher, executionSemester, teacherService).getBytes();
	} catch (ParseException e) {
	    throw new DomainException("");
	}
	String filename = getFilename(teacher, executionSemester);
	init(getfilePath(filename), filename, filename, null, content, null);
    }

    private String getFilename(Teacher teacher, ExecutionSemester executionSemester) {
	return (teacher.getTeacherNumber() + "_" + executionSemester.getName() + "_"
		+ executionSemester.getExecutionYear().getYear() + ".html").replaceAll(" ", "_").replaceAll("/", "_");
    }

    private VirtualPath getfilePath(String filename) {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode("TeacherCreditsDocuments", "TeacherCredits Documents"));
	filePath.addNode(new VirtualPathNode("TeacherCreditsDocument" + getIdInternal(), filename));
	return filePath;
    }

    private String getTeacherCreditsFile(Teacher teacher, ExecutionSemester executionSemester, TeacherService teacherService)
	    throws ParseException {
	ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	CreditLineDTO creditLineDTO = ReadAllTeacherCredits.readCreditLineDTO(executionSemester, teacher);
	Unit lastWorkingUnit = teacher.getLastWorkingUnit(executionSemester.getBeginDateYearMonthDay(),
		executionSemester.getEndDateYearMonthDay());

	final StringBuilder htmlText = new StringBuilder();
	htmlText.append("<html><body><div style=\"font-family: Arial, Helvetica, sans-serif;font-size: 100%;\">");
	htmlText.append("<style type=\"text/css\">");
	htmlText.append(".tb01 {width: 100%; border-collapse: collapse;font-size: 65%;}");
	htmlText.append(".tb01 th {background-color: #eee; border: 1pt solid #aaa; height: 1.5em;font-weight: normal; }");
	htmlText.append(".tb01 td { border: 1pt solid #aaa; height: 1.5em;}</style>");

	htmlText.append("<h1 style=\"text-align: center;\">").append(executionSemester.getName()).append(" ")
		.append(executionSemester.getExecutionYear().getYear()).append("</h1>");
	htmlText.append("<table class=\"tb01\"><tr><td><strong>Nome:</strong>").append(teacher.getPerson().getName());
	htmlText.append("</td><td><strong>N�mero:</strong>").append(teacher.getTeacherNumber());
	htmlText.append("</td></tr><tr><td><strong>Categoria:</strong>");
	Category category = teacher.getCategoryForCreditsByPeriod(executionSemester);
	if (category != null) {
	    htmlText.append(category.getCode());
	}
	htmlText.append("</td><td><strong>Sec��o:</strong>");
	if (lastWorkingUnit != null) {
	    htmlText.append(lastWorkingUnit.getName());
	}
	htmlText.append("</td></tr><tr><td><strong>Departamento:</strong>");
	if (lastWorkingUnit != null && lastWorkingUnit.getDepartmentUnit() != null
		&& lastWorkingUnit.getDepartmentUnit().getDepartment() != null) {
	    htmlText.append(lastWorkingUnit.getDepartmentUnit().getDepartment().getRealName());
	}
	htmlText.append("</td><td><strong>Centro de Custo:</strong>");
	if (lastWorkingUnit != null) {
	    htmlText.append(lastWorkingUnit.getCostCenterCode());
	}
	htmlText.append("</td></tr></table>");

	htmlText.append("<h3>Resumo de cr�ditos</h3>");
	htmlText.append("<ul style=\"list-style: none; font-size: 65%;\"><li><b>")
		.append(creditLineDTO.getTeachingDegreeCredits())
		.append("</b> (A) - Cr�ditos provenientes de aulas de licenciatura</li>");
	htmlText.append("<li><b>").append(creditLineDTO.getSupportLessonHours())
		.append("</b> (D) - Horas semanais de aulas de d�vidas</li>");
	htmlText.append("<li><b>").append(creditLineDTO.getMasterDegreeCredits())
		.append("</b> (M) - Cr�ditos provenientes de aulas de mestrado</li>");
	htmlText.append("<li><b>").append(creditLineDTO.getTfcAdviseCredits())
		.append("</b> (TFC) - Cr�ditos provenientes de alunos de trabalho final de curso</li>");
	htmlText.append("<li><b>").append(creditLineDTO.getThesesCredits())
		.append("</b> (Diss) - Cr�ditos provenientes de Disserta��es</li>");
	htmlText.append("<li><b>").append(creditLineDTO.getInstitutionWorkingHours())
		.append("</b> (P) - Horas semanais de perman�ncia no IST</li>");
	htmlText.append("<li><b>").append(creditLineDTO.getOtherCredits())
		.append("</b> (O) - Cr�ditos enquadrados na categoria outros</li>");
	htmlText.append("<li><b>").append(creditLineDTO.getManagementCredits())
		.append("</b> (CG) - Cr�ditos provenientes de cargos de gest�o</li>");
	htmlText.append("<li><b>").append(creditLineDTO.getServiceExemptionCredits())
		.append("</b> (SNE) - Cr�ditos provenientes de situa��es em n�o exerc�cio</li></ul>");

	htmlText.append("<h3>1) Disciplinas Leccionadas em Gradua��o</h3>");
	List<Professorship> professorships = teacher.getProfessorships(executionSemester);
	if (professorships.isEmpty()) {
	    htmlText.append("<p style=\"font-size: 65%;\">O professor n�o lecciona nenhuma disciplina neste semestre.</p>");
	} else {
	    for (Professorship professorship : professorships) {
		htmlText.append("<p style=\"font-size: 65%;\"><strong>").append(professorship.getExecutionCourse().getName())
			.append(" (").append(professorship.getDegreeSiglas()).append(")</strong></p>");

		htmlText.append("<p style=\"font-size: 65%;\">Aulas:</p>");

		if (professorship.getDegreeTeachingServicesOrderedByShift().isEmpty()) {
		    htmlText.append("<p style=\"font-size: 65%;\">N�o foram encontrados registos de aulas.</p>");
		} else {
		    htmlText.append("<table class=\"tb01\"><tr><th>Turno</th><th>Tipo</th><th>Horas</th><th>% que lecciona</th></tr>");
		    for (DegreeTeachingService degreeTeachingService : professorship.getDegreeTeachingServicesOrderedByShift()) {
			htmlText.append("<tr><td>").append(degreeTeachingService.getShift().getNome()).append("</td>");
			htmlText.append("<td>").append(degreeTeachingService.getShift().getShiftTypesPrettyPrint())
				.append("</td>");
			htmlText.append("<td>").append(degreeTeachingService.getShift().getTotalHours()).append("</td>");
			htmlText.append("<td>").append(degreeTeachingService.getPercentage()).append("</td></tr>");
		    }
		    htmlText.append("</table>");
		}

		htmlText.append("<p style=\"font-size: 65%;\">Aulas de d�vidas:</p>");

		if (professorship.getSupportLessonsOrderedByStartTimeAndWeekDay().isEmpty()) {
		    htmlText.append("<p style=\"font-size: 65%;\">N�o foram encontrados registos de aulas de d�vidas.</p>");
		} else {

		    htmlText.append("<table class=\"tb01\"><tr><th>Dia da semana</th><th>In�cio</th><th>Fim</th><th>Local</th></tr>");
		    for (SupportLesson supportLesson : professorship.getSupportLessonsOrderedByStartTimeAndWeekDay()) {
			htmlText.append("<tr><td>").append(supportLesson.getWeekDay()).append("</td>");
			htmlText.append("<td>").append(supportLesson.getStartTimeHourMinuteSecond().toString()).append("</td>");
			htmlText.append("<td>").append(supportLesson.getEndTimeHourMinuteSecond().toString()).append("</td>");
			htmlText.append("<td>").append(supportLesson.getPlace()).append("</td></tr>");
		    }
		    htmlText.append("</table>");
		}
	    }
	}

	htmlText.append("<h3>2) Disciplinas Leccionadas em P�s-Gradua��o</h3>");

	if (teacherService == null || teacherService.getMasterDegreeServices().isEmpty()) {
	    htmlText.append("<p style=\"font-size: 65%;\">N�o foram encontrados registos de disciplinas de mestrado.</p>");
	} else {
	    htmlText.append("<table class=\"tb01\"><tr><th>Plano(s) curriculare(s)</th><th>Disciplina</th><th>Horas</th><th>Cr�ditos</th></tr>");
	    for (TeacherMasterDegreeService teacherMasterDegreeService : teacherService.getMasterDegreeServices()) {
		if (teacherMasterDegreeService.getProfessorship() != null) {
		    htmlText.append("<tr><td>").append(teacherMasterDegreeService.getProfessorship().getDegreePlanNames());
		    htmlText.append("</td><td>").append(
			    teacherMasterDegreeService.getProfessorship().getExecutionCourse().getName());
		} else {
		    htmlText.append("<tr><td></td><td>");
		}
		htmlText.append("</td><td>").append(teacherMasterDegreeService.getHours()).append("</td>");
		htmlText.append("<td>").append(teacherMasterDegreeService.getCredits()).append("</td></tr>");
	    }
	    htmlText.append("</table>");
	}

	if (teacherService != null && teacherService.getTeacherServiceNotes() != null
		&& !StringUtils.isEmpty(teacherService.getTeacherServiceNotes().getMasterDegreeTeachingNotes())) {
	    htmlText.append("<p style=\"font-size: 65%;\">")
		    .append(teacherService.getTeacherServiceNotes().getMasterDegreeTeachingNotes()
			    .replaceAll("(\r\n)|(\n)", "<br />")).append("</p>");
	}

	htmlText.append("<h3>3) Trabalhos Finais de Curso (at� Ano Lectivo de 2006/2007)</h3>");
	List<TeacherAdviseService> teacherAdviseServices = new ArrayList<TeacherAdviseService>();
	if (teacherService != null) {
	    teacherAdviseServices.addAll(teacherService.getTeacherAdviseServices());
	}
	List<TeacherAdviseService> tfcAdvises = (List<TeacherAdviseService>) CollectionUtils.select(teacherAdviseServices,
		new Predicate() {
		    public boolean evaluate(Object arg0) {
			TeacherAdviseService teacherAdviseService = (TeacherAdviseService) arg0;
			return teacherAdviseService.getAdvise().getAdviseType().equals(AdviseType.FINAL_WORK_DEGREE);
		    }
		});

	if (teacherAdviseServices.isEmpty() || tfcAdvises.isEmpty()) {
	    htmlText.append("<p style=\"font-size: 65%;\">N�o foram encontrados registos de alunos de trabalho final de curso.</p>");
	} else {
	    Collections.sort(tfcAdvises, new BeanComparator("advise.student.number"));

	    htmlText.append("<p style=\"font-size: 65%;\"><strong>Alunos:</strong></p><table class=\"tb01\"><tr><th>N�mero do aluno</th><th>Nome do aluno</th><th>Percentagem</th></tr>");
	    for (TeacherAdviseService teacherAdviseService : tfcAdvises) {
		htmlText.append("<tr><td>").append(teacherAdviseService.getAdvise().getStudent().getNumber()).append("</td>");
		htmlText.append("<td>").append(teacherAdviseService.getAdvise().getStudent().getPerson().getName())
			.append("</td>");
		htmlText.append("<td>").append(teacherAdviseService.getPercentage()).append("</td></tr>");
	    }
	    htmlText.append("</table>");
	}

	htmlText.append("<h3>4) Disserta��es de Mestrado</h3>");

	Collection<ThesisEvaluationParticipant> thesisEvaluationParticipants = teacher.getPerson()
		.getThesisEvaluationParticipants(executionSemester);

	if (thesisEvaluationParticipants.isEmpty()) {
	    htmlText.append("<p style=\"font-size: 65%;\">N�o foram encontradas Disserta��es de Mestrado.</p>");
	} else {
	    htmlText.append("<p style=\"font-size: 65%;\"><strong>Alunos:</strong></p><table class=\"tb01\"><tbody><tr><th style=\"width: 5em;\">N�mero</th><th>Nome</th><th style=\"width: 7em;\">Lecciona (%)</th></tr>");
	    for (ThesisEvaluationParticipant participant : thesisEvaluationParticipants) {
		htmlText.append("<tr><td>").append(participant.getThesis().getStudent().getNumber());
		htmlText.append("</td><td>").append(participant.getThesis().getStudent().getPerson().getName());
		htmlText.append("</td><td>").append(participant.getCreditsDistribution()).append("</td></tr>");
	    }
	    htmlText.append("</tbody></table>");
	}

	if (teacherService != null && teacherService.getTeacherServiceNotes() != null
		&& !StringUtils.isEmpty(teacherService.getTeacherServiceNotes().getThesisNote())) {
	    htmlText.append("<p style=\"font-size: 65%;\">")
		    .append(teacherService.getTeacherServiceNotes().getThesisNote().replaceAll("(\r\n)|(\n)", "<br />"))
		    .append("</p>");
	}

	htmlText.append("<h3>5) Perman�ncia no IST</h3>");
	ComparatorChain comparatorChain = new ComparatorChain();
	BeanComparator weekDayComparator = new BeanComparator("weekDay");
	BeanComparator startTimeComparator = new BeanComparator("startTime");
	comparatorChain.addComparator(weekDayComparator);
	comparatorChain.addComparator(startTimeComparator);

	List<InstitutionWorkTime> institutionWorkingTimes = new ArrayList<InstitutionWorkTime>();
	if (teacherService != null) {
	    institutionWorkingTimes.addAll(teacherService.getInstitutionWorkTimes());
	    Collections.sort(institutionWorkingTimes, comparatorChain);
	}

	if (!institutionWorkingTimes.isEmpty()) {
	    htmlText.append("<p style=\"font-size: 65%;\">N�o foram encontrados registos.</p>");
	} else {
	    htmlText.append("<table class=\"tb01\"><tr>");

	    for (WeekDay weekDay : WeekDay.values()) {
		if (!weekDay.equals(WeekDay.SUNDAY)) {
		    htmlText.append("<th style=\"width: 16.5em;\">").append(bundleEnumeration.getString(weekDay.name()))
			    .append("</th>");
		}
	    }
	    htmlText.append("</tr><tr>");
	    for (WeekDay weekDay : WeekDay.values()) {
		if (!weekDay.equals(WeekDay.SUNDAY)) {
		    htmlText.append("<td style=\"text-align: center;\">");
		    for (InstitutionWorkTime institutionWorkTime : institutionWorkingTimes) {
			if (institutionWorkTime.getWeekDay().equals(weekDay)) {
			    htmlText.append(institutionWorkTime.getStartTimeHourMinuteSecond().toString()).append(" �s ")
				    .append(institutionWorkTime.getEndTimeHourMinuteSecond().toString()).append("<br/>");
			}
		    }
		    htmlText.append("</td>");
		}
	    }
	    htmlText.append("</tr></table>");
	}

	htmlText.append("<h3>6) Acumula��o de Fun��es</h3>");
	if (teacherService != null && teacherService.getTeacherServiceNotes() != null
		&& !StringUtils.isEmpty(teacherService.getTeacherServiceNotes().getFunctionsAccumulation())) {
	    htmlText.append("<p style=\"font-size: 65%;\">")
		    .append(teacherService.getTeacherServiceNotes().getFunctionsAccumulation()
			    .replaceAll("(\r\n)|(\n)", "<br />")).append("</p>");
	} else {
	    htmlText.append("<p style=\"font-size: 65%;\">N�o foram encontrados registos de acumula��o de fun��es.</p>");
	}

	htmlText.append("<h3>7) Outros</h3>");
	if (teacherService == null || teacherService.getOtherServices().isEmpty()) {
	    htmlText.append("<p style=\"font-size: 65%;\">N�o foram encontrados registos da categoria outros neste semestre.</p>");
	} else {
	    htmlText.append("<table class=\"tb01\"><tr><th>Raz�o</th><th>Cr�ditos</th></tr>");
	    for (OtherService otherService : teacherService.getOtherServices()) {
		htmlText.append("<tr><td>").append(otherService.getReason()).append("</td>");
		htmlText.append("<td>").append(otherService.getCredits()).append("</td></tr>");
	    }
	    htmlText.append("</table>");
	}

	if (teacherService != null && teacherService.getTeacherServiceNotes() != null
		&& !StringUtils.isEmpty(teacherService.getTeacherServiceNotes().getOthersNotes())) {
	    htmlText.append("<p style=\"font-size: 65%;\">")
		    .append(teacherService.getTeacherServiceNotes().getOthersNotes().replaceAll("(\r\n)|(\n)", "<br />"))
		    .append("</p>");
	}

	htmlText.append("<h3>8) Cargos de Gest�o</h3>");
	List<PersonFunction> personFuntions = teacher.getPersonFuntions(executionSemester.getBeginDateYearMonthDay(),
		executionSemester.getEndDateYearMonthDay());

	if (personFuntions.isEmpty()) {
	    htmlText.append("<p style=\"font-size: 65%;\">N�o foram encontrados registos de cargos de gest�o.</p>");
	} else {
	    Collections.sort(personFuntions, new BeanComparator("beginDate"));
	    htmlText.append("<table class=\"tb01\"><tr><th>Cargo</th><th>Unidade</th><th>Cr�ditos</th><th>In�cio</th><th>Fim</th></tr>");
	    for (PersonFunction personFunction : personFuntions) {
		htmlText.append("<tr><td>").append(personFunction.getFunction().getName());
		htmlText.append("</td><td>").append(personFunction.getParentParty().getName());
		htmlText.append("</td><td>").append(personFunction.getCredits());
		htmlText.append("</td><td>").append(personFunction.getBeginDate());
		htmlText.append("</td><td>").append(personFunction.getEndDate()).append("</td></tr>");
	    }
	    htmlText.append("</table>");
	}

	if (teacherService != null && teacherService.getTeacherServiceNotes() != null
		&& !StringUtils.isEmpty(teacherService.getTeacherServiceNotes().getManagementFunctionNotes())) {
	    htmlText.append("<p style=\"font-size: 65%;\">")
		    .append(teacherService.getTeacherServiceNotes().getManagementFunctionNotes()
			    .replaceAll("(\r\n)|(\n)", "<br />")).append("</p>");
	}

	htmlText.append("<h3>9) Situa��es em N�o Exerc�cio</h3>");

	List<TeacherServiceExemption> serviceExemptions = teacher.getServiceExemptionsWithoutMedicalSituations(
		executionSemester.getBeginDateYearMonthDay(), executionSemester.getEndDateYearMonthDay());

	if (serviceExemptions.isEmpty()) {
	    htmlText.append("<p style=\"font-size: 65%;\">N�o foram encontrados registos da categoria situa��es em n�o exerc�cio.</p>");
	} else {
	    Collections.sort(serviceExemptions, new BeanComparator("start"));
	    htmlText.append("<table class=\"tb01\"><tr><th>Situa��o</th><th>Organiza��o</th><th>In�cio</th><th>Fim</th></tr>");
	    for (TeacherServiceExemption teacherServiceExemption : serviceExemptions) {
		htmlText.append("<tr><td>")
			.append(bundleEnumeration.getString(teacherServiceExemption.getSituationType().name()))
			.append("</td><td>");
		if (!StringUtils.isEmpty(teacherServiceExemption.getInstitution())) {
		    htmlText.append(teacherServiceExemption.getInstitution());
		}
		htmlText.append("</td><td>").append(teacherServiceExemption.getBeginDateYearMonthDay());
		htmlText.append("</td><td>").append(teacherServiceExemption.getEndDateYearMonthDay()).append("</td></tr>");
	    }
	    htmlText.append("</table>");
	}

	if (teacherService != null && teacherService.getTeacherServiceNotes() != null
		&& !StringUtils.isEmpty(teacherService.getTeacherServiceNotes().getServiceExemptionNotes())) {
	    htmlText.append("<p style=\"font-size: 65%;\">")
		    .append(teacherService.getTeacherServiceNotes().getServiceExemptionNotes()
			    .replaceAll("(\r\n)|(\n)", "<br />")).append("</p>");
	}
	htmlText.append("</div></body></html>");

	return htmlText.toString();
    }
}
