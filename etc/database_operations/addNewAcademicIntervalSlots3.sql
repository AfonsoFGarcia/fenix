select concat('update EXECUTION_PERIOD set EXECUTION_INTERVAL = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicSemesterCE:' , ACADEMIC_CALENDAR_ENTRY.ID_INTERNAL , '" where ID_INTERNAL = ' , ACADEMIC_CALENDAR_ENTRY.REFERENCE_KEY , ';') as "" from ACADEMIC_CALENDAR_ENTRY where OJB_CONCRETE_CLASS like '%Semester%';
select concat('update EXECUTION_YEAR set EXECUTION_INTERVAL = "net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYearCE:' , ACADEMIC_CALENDAR_ENTRY.ID_INTERNAL , '" where ID_INTERNAL = ' , ACADEMIC_CALENDAR_ENTRY.REFERENCE_KEY , ';') as "" from ACADEMIC_CALENDAR_ENTRY where OJB_CONCRETE_CLASS like '%Year%';
