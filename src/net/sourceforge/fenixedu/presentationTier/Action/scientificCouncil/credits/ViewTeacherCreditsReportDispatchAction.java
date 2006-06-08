/**
 * Jan 23, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits.ReadTeachersCreditsResumeByPeriodAndUnit.TeacherCreditsReportDTO;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.NumberUtils;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ViewTeacherCreditsReportDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        Collection<ExecutionYear> executionYears = rootDomainObject.getExecutionYears();
        List filteredExecutionYears = filterExecutionYears(executionYears);

        Collection<Department> departments = rootDomainObject.getDepartments();
        Iterator departmentOrderedIterator = new OrderedIterator(departments.iterator(),
                new BeanComparator("name"));

        request.setAttribute("executionYears", filteredExecutionYears);
        request.setAttribute("departments", departmentOrderedIterator);
        return mapping.findForward("prepare");
    }

    public ActionForward viewCreditsReport(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, InvalidPeriodException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer fromExecutionYearID = Integer.parseInt(dynaForm.getString("fromExecutionYearID"));
        Integer untilExecutionYearID = Integer.parseInt(dynaForm.getString("untilExecutionYearID"));

        request.setAttribute("fromExecutionYearID", fromExecutionYearID);
        request.setAttribute("untilExecutionYearID", untilExecutionYearID);

        ExecutionYear fromExecutionYear = rootDomainObject.readExecutionYearByOID(fromExecutionYearID);
        ExecutionYear untilExecutionYear = rootDomainObject.readExecutionYearByOID(untilExecutionYearID);

        ExecutionPeriod fromExecutionPeriod = fromExecutionYear.readExecutionPeriodForSemester(1);
        ExecutionPeriod untilExecutionPeriod = untilExecutionYear.readExecutionPeriodForSemester(2);

        if (!validExecutionPeriodsChoice(fromExecutionPeriod, untilExecutionPeriod)) {
            throw new InvalidPeriodException();
        }

        List<TeacherCreditsReportDTO> teacherCreditsReportList = new ArrayList<TeacherCreditsReportDTO>();
        Integer departmentID = (Integer) dynaForm.get("departmentID");
        Map<Department, List<TeacherCreditsReportDTO>> teachersCreditsByDepartment = new HashMap<Department, List<TeacherCreditsReportDTO>>();
        if (departmentID == 0) {
            Collection<Department> departments = rootDomainObject.getDepartments();
            for (Department department : departments) {
                Unit unit = department.getDepartmentUnit();
                teacherCreditsReportList = (List<TeacherCreditsReportDTO>) ServiceUtils.executeService(
                        userView, "ReadTeachersCreditsResumeByPeriodAndUnit", new Object[] { unit,
                                fromExecutionPeriod, untilExecutionPeriod });
                teachersCreditsByDepartment.put(department, teacherCreditsReportList);
            }
            request.setAttribute("departmentID", 0);
        } else {            
            Department department = rootDomainObject.readDepartmentByOID(departmentID);
            Unit departmentUnit = department.getDepartmentUnit();
            teacherCreditsReportList = (List<TeacherCreditsReportDTO>) ServiceUtils.executeService(
                    userView, "ReadTeachersCreditsResumeByPeriodAndUnit", new Object[] { departmentUnit,
                            fromExecutionPeriod, untilExecutionPeriod });
            teachersCreditsByDepartment.put(department, teacherCreditsReportList);
            request.setAttribute("department", department);
            request.setAttribute("departmentID", department.getIdInternal());
        }

        if (teachersCreditsByDepartment != null && !teachersCreditsByDepartment.isEmpty()) {
            Map<Department, Map<Unit, List>> teachersCreditsDisplayMap = new TreeMap<Department, Map<Unit, List>>(
                    new BeanComparator("name"));
            for (Department department : teachersCreditsByDepartment.keySet()) {
                Map<Unit, List> teachersCreditsByUnit = new TreeMap<Unit, List>(new BeanComparator(
                        "name"));
                List<TeacherCreditsReportDTO> list = teachersCreditsByDepartment.get(department);
                for (TeacherCreditsReportDTO creditsReportDTO : list) {
                    List mapLineList = teachersCreditsByUnit.get(creditsReportDTO.getUnit());
                    if (mapLineList == null) {
                        mapLineList = new ArrayList<TeacherCreditsReportDTO>();
                        mapLineList.add(creditsReportDTO);
                        teachersCreditsByUnit.put(creditsReportDTO.getUnit(), mapLineList);
                    } else {
                        mapLineList.add(creditsReportDTO);
                    }
                }
                for (List teachersCreditMapLine : teachersCreditsByUnit.values()) {
                    Collections.sort(teachersCreditMapLine, new BeanComparator("teacher.teacherNumber"));
                }
                teachersCreditsDisplayMap.put(department, teachersCreditsByUnit);
            }
            if (!teacherCreditsReportList.isEmpty()) {
                request.setAttribute("executionPeriodHeader", teacherCreditsReportList.iterator().next()
                        .getCreditsByExecutionPeriod());
            }
            request.setAttribute("teachersCreditsDisplayMap", teachersCreditsDisplayMap);
        }
        return mapping.findForward("showCreditsReport");
    }

    private boolean validExecutionPeriodsChoice(ExecutionPeriod fromExecutionPeriod,
            ExecutionPeriod untilExecutionPeriod) {
        ExecutionPeriod tempExecutionPeriod = fromExecutionPeriod;
        if (fromExecutionPeriod == untilExecutionPeriod) {
            return true;
        }
        while (tempExecutionPeriod != untilExecutionPeriod && tempExecutionPeriod != null) {
            tempExecutionPeriod = tempExecutionPeriod.getNextExecutionPeriod();
            if (tempExecutionPeriod == untilExecutionPeriod) {
                return true;
            }
        }
        return false;
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {

        Map<Department, Map<Unit, List>> teachersCreditsByDepartment = getTeachersCreditsMap(request);

        try {
            String filename = "RelatorioCreditos:" + getFileName(Calendar.getInstance().getTime());
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");

            ServletOutputStream writer = response.getOutputStream();
            exportToXls(teachersCreditsByDepartment, writer);

            writer.flush();
            response.flushBuffer();

        } catch (IOException e) {
            throw new FenixServiceException();
        }
        return null;
    }

    private void exportToXls(Map<Department, Map<Unit, List>> teachersCreditsByDepartment,
            ServletOutputStream outputStream) throws IOException {
        final Spreadsheet spreadsheet = new Spreadsheet("Relat�rio de Cr�ditos");

        final HSSFWorkbook workbook = new HSSFWorkbook();
        final ExcelStyle excelStyle = new ExcelStyle(workbook);

        for (Department department : teachersCreditsByDepartment.keySet()) {
            Set<ExecutionPeriod> executionPeriods = null;
            String deptName = department.getRealName();
            deptName = deptName.replaceAll("DEPARTAMENTO", "DEP. ");
            deptName = deptName.replaceAll("ENGENHARIA", "ENG. ");
            spreadsheet.setName(deptName);                      
            Map<Unit, List> creditsByUnit = teachersCreditsByDepartment.get(department);
            Double unitTotalCredits = 0.0;
            for (Unit unit : creditsByUnit.keySet()) {
                List<TeacherCreditsReportDTO> teachersCreditReportDTOs = creditsByUnit.get(unit);
                if (executionPeriods == null) {
                    executionPeriods = teachersCreditReportDTOs.get(0).getCreditsByExecutionPeriod()
                            .keySet();
                }
                spreadsheet.addRow();
                final Row row = spreadsheet.addRow();
                row.setCell(unit.getName());
                setHeaders(executionPeriods, spreadsheet);
                unitTotalCredits += fillSpreadSheet(teachersCreditReportDTOs, spreadsheet);
            }
            spreadsheet.exportToXLSSheet(workbook, excelStyle.getHeaderStyle(), excelStyle
                    .getStringStyle());
            spreadsheet.setRows(new ArrayList<Row>());
        }
        workbook.write(outputStream);
    }

    private Double fillSpreadSheet(final List<TeacherCreditsReportDTO> allListElements,
            final Spreadsheet spreadsheet) {
        Double listTotalCredits = 0.0;
        int numberOfCells = 0;
        for (final TeacherCreditsReportDTO teacherCreditsReportDTO : allListElements) {
            final Row row = spreadsheet.addRow();
            row.setCell(teacherCreditsReportDTO.getTeacher().getTeacherNumber().toString());
            row.setCell(teacherCreditsReportDTO.getTeacher().getPerson().getNome());
            Double pastCredits = NumberUtils.formatNumber((Double) teacherCreditsReportDTO
                    .getPastCredits(), 2);
            row.setCell(pastCredits.toString().replace('.', ','));
            Set<ExecutionPeriod> executionPeriods = teacherCreditsReportDTO
                    .getCreditsByExecutionPeriod().keySet();
            Double totalCredits = 0.0;
            totalCredits += teacherCreditsReportDTO.getPastCredits();
            numberOfCells = 2;
            for (ExecutionPeriod executionPeriod : executionPeriods) {
                numberOfCells += 1;
                Double credits = teacherCreditsReportDTO.getCreditsByExecutionPeriod().get(
                        executionPeriod);
                credits = NumberUtils.formatNumber(credits, 2);
                row.setCell(credits.toString().replace('.', ','));
                totalCredits += credits;
                if (executionPeriod.getSemester() == 2) {
                    numberOfCells += 1;                    
                    totalCredits = NumberUtils.formatNumber(totalCredits, 2);
                    row.setCell(totalCredits.toString().replace('.', ','));
                }                
            }
            listTotalCredits += totalCredits;
        }
        final Row row = spreadsheet.addRow();
        row.setCell(numberOfCells - 1, "Total Unidade");
        row.setCell(numberOfCells, NumberUtils.formatNumber(listTotalCredits, 2).toString().replace('.', ','));
        return listTotalCredits;
    }

    private void setHeaders(Set<ExecutionPeriod> executionPeriods, Spreadsheet spreadsheet) {
        final Row row = spreadsheet.addRow();
        row.setCell("N�mero");
        row.setCell("Nome");
        row.setCell("Saldo at� "
                + executionPeriods.iterator().next().getPreviousExecutionPeriod().getExecutionYear()
                        .getYear());
        for (ExecutionPeriod executionPeriod : executionPeriods) {
            String semester = null;
            if (executionPeriod.getName().equalsIgnoreCase("1 Semestre")) {
                semester = "1� Sem - ";
            } else {
                semester = "2� Sem - ";
            }
            StringBuilder stringBuilder = new StringBuilder(semester);
            stringBuilder.append(executionPeriod.getExecutionYear().getYear());
            row.setCell(stringBuilder.toString());
            if (executionPeriod.getSemester() == 2) {
                row.setCell("Saldo Final " + executionPeriod.getExecutionYear().getYear());
            }
        }
    }

    private Map<Department, Map<Unit, List>> getTeachersCreditsMap(HttpServletRequest request)
            throws FenixFilterException, FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer fromExecutionYearID = Integer.parseInt(request.getParameter("fromExecutionYearID"));
        Integer untilExecutionYearID = Integer.parseInt(request.getParameter("untilExecutionYearID"));

        ExecutionYear fromExecutionYear = rootDomainObject.readExecutionYearByOID(fromExecutionYearID);
        ExecutionYear untilExecutionYear = rootDomainObject.readExecutionYearByOID(untilExecutionYearID);

        ExecutionPeriod fromExecutionPeriod = fromExecutionYear.readExecutionPeriodForSemester(1);
        ExecutionPeriod untilExecutionPeriod = untilExecutionYear.readExecutionPeriodForSemester(2);

        List<TeacherCreditsReportDTO> teacherCreditsReportList = new ArrayList<TeacherCreditsReportDTO>();
        Integer departmentID = Integer.parseInt(request.getParameter("departmentID"));
        Map<Department, List<TeacherCreditsReportDTO>> teachersCreditsByDepartment = new HashMap<Department, List<TeacherCreditsReportDTO>>();
        if (departmentID == 0) {
            Collection<Department> departments = rootDomainObject.getDepartments();
            for (Department department : departments) {
                Unit unit = department.getDepartmentUnit();
                teacherCreditsReportList = (List<TeacherCreditsReportDTO>) ServiceUtils.executeService(
                        userView, "ReadTeachersCreditsResumeByPeriodAndUnit", new Object[] { unit,
                                fromExecutionPeriod, untilExecutionPeriod });
                teachersCreditsByDepartment.put(department, teacherCreditsReportList);
            }
        } else {
            Department department = rootDomainObject.readDepartmentByOID(departmentID);
            Unit unit = department.getDepartmentUnit();

            teacherCreditsReportList = (List<TeacherCreditsReportDTO>) ServiceUtils.executeService(
                    userView, "ReadTeachersCreditsResumeByPeriodAndUnit", new Object[] { unit,
                            fromExecutionPeriod, untilExecutionPeriod });

            teachersCreditsByDepartment.put(department, teacherCreditsReportList);
            request.setAttribute("department", department);
        }

        if (teachersCreditsByDepartment != null && !teachersCreditsByDepartment.isEmpty()) {
            Map<Department, Map<Unit, List>> teachersCreditsDisplayMap = new TreeMap<Department, Map<Unit, List>>(
                    new BeanComparator("name"));
            for (Department department : teachersCreditsByDepartment.keySet()) {
                Map<Unit, List> teachersCreditsByUnit = new TreeMap(new BeanComparator("name"));
                List<TeacherCreditsReportDTO> list = teachersCreditsByDepartment.get(department);
                for (TeacherCreditsReportDTO creditsReportDTO : list) {
                    List mapLineList = teachersCreditsByUnit.get(creditsReportDTO.getUnit());
                    if (mapLineList == null) {
                        mapLineList = new ArrayList<TeacherCreditsReportDTO>();
                        mapLineList.add(creditsReportDTO);
                        teachersCreditsByUnit.put(creditsReportDTO.getUnit(), mapLineList);
                    } else {
                        mapLineList.add(creditsReportDTO);
                    }
                }
                for (List teachersCreditMapLine : teachersCreditsByUnit.values()) {
                    Collections.sort(teachersCreditMapLine, new BeanComparator("teacher.teacherNumber"));
                }
                teachersCreditsDisplayMap.put(department, teachersCreditsByUnit);
            }
            return teachersCreditsDisplayMap;
        }
        return null;
    }

    private List filterExecutionYears(Collection<ExecutionYear> executionYears) {
        List filteredExecutionYears = new ArrayList();
        ExecutionYear executionYear0304 = ExecutionYear.readExecutionYearByName("2003/2004");
        for (ExecutionYear executionYear : executionYears) {
            if (!executionYear.getBeginDate().before(executionYear0304.getBeginDate())) {
                String label = executionYear.getYear();
                filteredExecutionYears.add(new LabelValueBean(label, executionYear.getIdInternal()
                        .toString()));
            }
        }
        Collections.sort(filteredExecutionYears, new BeanComparator("label"));
        return filteredExecutionYears;
    }

    private String getFileName(Date date) throws FenixFilterException, FenixServiceException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return (day + "-" + month + "-" + year + "_" + hour + ":" + minutes);
    }

    public class InvalidPeriodException extends FenixActionException {
    }
}
