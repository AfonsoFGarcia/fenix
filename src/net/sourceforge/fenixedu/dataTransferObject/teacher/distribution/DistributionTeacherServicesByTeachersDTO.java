package net.sourceforge.fenixedu.dataTransferObject.teacher.distribution;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;

/**
 *  amak, jpmsit
 * 
 */
public class DistributionTeacherServicesByTeachersDTO extends DataTranferObject {

	public class ExecutionCourseTeacherServiceDTO {
		private Integer executionCourseIdInternal;

		private String executionCourseName;

		private Integer hoursSpentByTeacher;
		
		private Set<String> courseDegreesList;
		
		private String executionPeriodName;
		
		private Set<String> executionYearsSet;
		

		public ExecutionCourseTeacherServiceDTO(Integer idInternal,
				String name, Integer hours, Set<String> executionCourseDegreesNameSet, Set<String> executionYearsSet, String periodName) {
			super();

			this.executionCourseIdInternal = idInternal;
			this.hoursSpentByTeacher = hours;
			this.executionCourseName = name;
			this.executionYearsSet = executionYearsSet;
			this.executionPeriodName = periodName;
			this.courseDegreesList = executionCourseDegreesNameSet;
		}

		public Integer getExecutionCourseIdInternal() {
			return executionCourseIdInternal;
		}

		public String getExecutionCourseName() {
			return executionCourseName;
		}

		public Integer getHoursSpentByTeacher() {
			return hoursSpentByTeacher;
		}

		public Set<String> getCourseDegreesList() {
			return courseDegreesList;
		}
		
		public void addToCourseDegreesList(String degreeName){
			courseDegreesList.add(degreeName);
		}

		public String getExecutionPeriodName() {
			return executionPeriodName;
		}

		public Set getExecutionYearsSet() {
			return executionYearsSet;
		}

				
		public String getDescription(){
			StringBuilder finalString = new StringBuilder(getExecutionCourseName());
			String[] stringArrayDegrees = (String[]) courseDegreesList.toArray(new String[] {});
						
			if(stringArrayDegrees.length > 0) {
				finalString.append(" (");
				finalString.append(stringArrayDegrees[0]);
			
				for(int i = 1;  i < stringArrayDegrees.length; i++) {
					finalString.append(", ");
					finalString.append(stringArrayDegrees[i]);
				}
				finalString.append(") ");
			}
		
			
			String[] stringArrayYears = (String[]) executionYearsSet.toArray(new String[]{});
			
			if(stringArrayYears.length > 0) {	
				finalString.append("(");
				finalString.append(stringArrayYears[0]);
				
				for(int i = 1;  i < stringArrayYears.length; i++) {
					finalString.append("�,");
					finalString.append(stringArrayYears[i]);
				}
				
				finalString.append("�ano/");
				finalString.append(executionPeriodName);
				finalString.append(")");
				}
			finalString.append(" - ");
			finalString.append(hoursSpentByTeacher);
		
		
			return finalString.toString();
		}
		
	}

	
	public class TeacherDistributionServiceEntryDTO implements Comparable {
		private Integer teacherIdInternal;
		
		private Integer teacherNumber;

		private String teacherCategory;

		private String teacherName;

		private Integer teacherRequiredHours;

		private Double teacherSpentCredits;
		
		private Double teacherAccumulatedCredits;
				

		List<ExecutionCourseTeacherServiceDTO> executionCourseTeacherServiceList;

		
		public TeacherDistributionServiceEntryDTO(Integer internal, Integer teacherNumber, String category,  String name, Integer hours, Double credits, Double accumulatedCredits) {
			this.teacherNumber = teacherNumber;
			teacherCategory = category;
			teacherIdInternal = internal;
			teacherName = name;
			teacherRequiredHours = hours;
			teacherSpentCredits = credits;
			teacherAccumulatedCredits = accumulatedCredits;
			
			executionCourseTeacherServiceList = new ArrayList<ExecutionCourseTeacherServiceDTO>();
		}


		public List<ExecutionCourseTeacherServiceDTO> getExecutionCourseTeacherServiceList() {
			return executionCourseTeacherServiceList;
		}


		public String getTeacherCategory() {
			return teacherCategory;
		}


		public Integer getTeacherIdInternal() {
			return teacherIdInternal;
		}


		public String getTeacherName() {
			return teacherName;
		}


		public Integer getTeacherRequiredHours() {
			return teacherRequiredHours;
		}


		public Double getTeacherSpentCredits() {
			return teacherSpentCredits;
		}


		public void addExecutionCourse(ExecutionCourseTeacherServiceDTO executionCourse) {	
			executionCourseTeacherServiceList.add(executionCourse);
		}


		public Integer getTeacherNumber() {
			return teacherNumber;
		}
		
		private String getFormattedValue(Double value){
			StringBuilder sb = new StringBuilder();
			Formatter formatter = new Formatter(sb);
			
			formatter.format("%.1f", value);
			return sb.toString();
		}
		
		public String getFormattedTeacherSpentCredits() {

			return getFormattedValue(getTeacherSpentCredits());
		}
		
		public String getFormattedTeacherAccumulatedCredits() {

			return getFormattedValue(getAccumulatedCredits());
		}
		
		public Integer getTotalLecturedHours() {
			int totalHours = 0;
			
			for(ExecutionCourseTeacherServiceDTO executionCourse: executionCourseTeacherServiceList) {
				totalHours += executionCourse.getHoursSpentByTeacher();
			}
			
			return totalHours;
		}
		
		public Integer getAvailability(){
			double availability = getTeacherRequiredHours() - getTotalLecturedHours() - getTeacherSpentCredits();
			
			return new Double(StrictMath.ceil(StrictMath.abs(availability)) * StrictMath.signum(availability)).intValue();
		}
		
		public Double getAccumulatedCredits() {
			return teacherAccumulatedCredits;
		}

		public int compareTo(Object obj) {
			if(obj instanceof TeacherDistributionServiceEntryDTO) {
				TeacherDistributionServiceEntryDTO teacher1 = (TeacherDistributionServiceEntryDTO) obj;	
				return this.getTeacherNumber().compareTo(teacher1.getTeacherNumber());
			}
			return 0;
		}


		public void setTeacherRequiredHours(Integer teacherRequiredHours) {
			this.teacherRequiredHours = teacherRequiredHours;
		}
	}

	
	
	private Map<Integer, TeacherDistributionServiceEntryDTO> teachersMap;

	public DistributionTeacherServicesByTeachersDTO() {
		teachersMap = new HashMap<Integer, TeacherDistributionServiceEntryDTO>();
	}

	public void addTeacher(Integer key, Integer teacherNumber, String category, String name,
			Integer hours, Double credits, Double accumulatedCredits) {
		TeacherDistributionServiceEntryDTO t = new TeacherDistributionServiceEntryDTO(key, teacherNumber, category, name, hours, credits, accumulatedCredits);

		if(!teachersMap.containsKey(key)) {
			teachersMap.put(key, t);
		}
	}

	public void addExecutionCourseToTeacher(Integer keyTeacher, Integer executionCourseIdInternal, String executionCourseName,
			Integer hours, Set<String> executionCourseDegreesNameSet, Set<String> curricularYearsSet, String periodName) {
		ExecutionCourseTeacherServiceDTO executionCourse = new ExecutionCourseTeacherServiceDTO(executionCourseIdInternal, executionCourseName, hours, executionCourseDegreesNameSet, curricularYearsSet, periodName);
		
		teachersMap.get(keyTeacher).addExecutionCourse(executionCourse);	
			
	}
	
	public void addHoursToTeacher(Integer keyTeacher, Integer hours){
		TeacherDistributionServiceEntryDTO teacher = teachersMap.get(keyTeacher);
		
		if(teacher != null){
			teacher.setTeacherRequiredHours(teacher.getTeacherRequiredHours() + hours);
		}
	}
	
	public boolean isTeacherPresent(Integer keyTeacher){
		return teachersMap.containsKey((keyTeacher));
	}
	
	public Map<Integer, TeacherDistributionServiceEntryDTO> getTeachersMap() {
		return teachersMap;
	}
}
