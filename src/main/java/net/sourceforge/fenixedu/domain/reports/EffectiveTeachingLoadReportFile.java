package net.sourceforge.fenixedu.domain.reports;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingServiceCorrection;
import net.sourceforge.fenixedu.domain.teacher.OtherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class EffectiveTeachingLoadReportFile extends EffectiveTeachingLoadReportFile_Base {

    public EffectiveTeachingLoadReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem das CLE";
    }

    @Override
    protected String getPrefix() {
        return "Listagem das CLE";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {
        ExecutionYear executionYear = getExecutionYear();
        spreadsheet.setName("CLE_" + executionYear.getQualifiedName().replace("/", ""));
        spreadsheet.setHeader("OID_EXECUTION_COURSE");
        spreadsheet.setHeader("OID_TEACHER");
        spreadsheet.setHeader("IstId");
        spreadsheet.setHeader("CLE");

        Map<Teacher, Map<ExecutionCourse, BigDecimal>> teachingLoad = getLoad(executionYear);
        for (Teacher teacher : teachingLoad.keySet()) {
            Map<ExecutionCourse, BigDecimal> executionCourseLoad = teachingLoad.get(teacher);
            for (ExecutionCourse executionCourse : executionCourseLoad.keySet()) {
                final Row row = spreadsheet.addRow();
                row.setCell(executionCourse.getExternalId());
                row.setCell(teacher.getExternalId());
                row.setCell(teacher.getPerson().getUsername());
                row.setCell(executionCourseLoad.get(executionCourse));
            }
        }
        spreadsheet.exportToXLSSheet(new File("CLE_" + executionYear.getQualifiedName().replace("/", "") + ".xls"));
    }

    protected Map<Teacher, Map<ExecutionCourse, BigDecimal>> getLoad(ExecutionYear executionYear) {
        Map<Teacher, Map<ExecutionCourse, BigDecimal>> teachingLoad = new HashMap<Teacher, Map<ExecutionCourse, BigDecimal>>();
        for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
            for (TeacherService teacherService : executionSemester.getTeacherServices()) {
                for (DegreeTeachingService degreeTeachingService : teacherService.getDegreeTeachingServices()) {
                    double efectiveLoad = degreeTeachingService.getEfectiveLoad();
                    if (!degreeTeachingService.getShift().getExecutionCourse().getProjectTutorialCourse() && efectiveLoad != 0.0) {
                        Map<ExecutionCourse, BigDecimal> executionCourseLoad =
                                teachingLoad.get(degreeTeachingService.getTeacherService().getTeacher());
                        if (executionCourseLoad == null) {
                            executionCourseLoad = new HashMap<ExecutionCourse, BigDecimal>();
                        }
                        BigDecimal load = executionCourseLoad.get(degreeTeachingService.getShift().getExecutionCourse());
                        if (load == null) {
                            load = BigDecimal.ZERO;
                        }
                        load = load.add(new BigDecimal(efectiveLoad));
                        executionCourseLoad.put(degreeTeachingService.getShift().getExecutionCourse(), load);
                        teachingLoad.put(degreeTeachingService.getTeacherService().getTeacher(), executionCourseLoad);
                    }
                }
                for (OtherService otherService : teacherService.getOtherServices()) {
                    if (otherService instanceof DegreeTeachingServiceCorrection) {
                        DegreeTeachingServiceCorrection degreeTeachingServiceCorrection =
                                (DegreeTeachingServiceCorrection) otherService;
                        if (!degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse().getProjectTutorialCourse()) {
                            Map<ExecutionCourse, BigDecimal> executionCourseLoad =
                                    teachingLoad.get(degreeTeachingServiceCorrection.getTeacherService().getTeacher());
                            if (executionCourseLoad == null) {
                                executionCourseLoad = new HashMap<ExecutionCourse, BigDecimal>();
                            }
                            BigDecimal load =
                                    executionCourseLoad.get(degreeTeachingServiceCorrection.getProfessorship()
                                            .getExecutionCourse());
                            if (load == null) {
                                load = BigDecimal.ZERO;
                            }
                            load = load.add(degreeTeachingServiceCorrection.getCorrection());
                            executionCourseLoad
                                    .put(degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse(), load);
                            teachingLoad.put(degreeTeachingServiceCorrection.getTeacherService().getTeacher(),
                                    executionCourseLoad);
                        }
                    }
                }
            }
        }
        return teachingLoad;
    }

}
