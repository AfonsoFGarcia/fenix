<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
<html:xhtml/>

<p class="mtop2 mbottom05"><strong>Aulas j� atribuidas ao turno:</strong></p>

<logic:present name="shift" property="infoLessons">
  <html:form action="/manageShiftMultipleItems">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteLessons"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_COURSE_OID %>" property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
				 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.SHIFT_OID %>" property="<%= SessionConstants.SHIFT_OID %>"
				 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

		<table class="tstyle4 thlight tdcenter">
			<tr>
				<th>
				</th>
				<th>
					<bean:message key="property.weekday"/>
				</th>
				<th>
					<bean:message key="property.time.start"/>
				</th>
		        <th>
		        	<bean:message key="property.time.end"/>
	    	    </th>
				<th>
					<bean:message key="property.room"/>
				</th>
				<th>
		        	<bean:message key="property.capacity"/>
		        </th>
				<th> </th>
				<th> </th>
		        <th> </th>
			</tr>
			<bean:define id="deleteConfirm">
				return confirm('<bean:message key="message.confirm.delete.lesson"/>')
			</bean:define>			
			<logic:iterate id="lesson" name="shift" property="infoLessons">
				<bean:define id="lessonOID" name="lesson" property="idInternal"/>
				<tr>
              		<td>
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedItems" property="selectedItems">
							<bean:write name="lesson" property="idInternal"/>
						</html:multibox>
					</td>
					<td>
						<bean:write name="lesson" property="diaSemana"/>
					</td>
					<td>
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="inicio.timeInMillis"/>
						</dt:format>
					</td>
					<td>
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="fim.timeInMillis"/>
						</dt:format>
					</td>
					<td>
						<logic:notEmpty name="lesson" property="infoSala">
							<bean:write name="lesson" property="infoSala.nome"/>
						</logic:notEmpty>	
					</td>
					<td>
						<logic:notEmpty name="lesson" property="infoSala">
							<bean:write name="lesson" property="infoSala.capacidadeNormal"/>
						</logic:notEmpty>
					</td>
					<td>
	               		<html:link page="<%= "/manageLesson.do?method=prepareEdit&amp;page=0&amp;"
    	           							+ SessionConstants.LESSON_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("lessonOID")
               					   			+ "&amp;"
    	           							+ SessionConstants.SHIFT_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("shiftOID")
               					   			+ "&amp;"
			  								+ SessionConstants.EXECUTION_COURSE_OID
  											+ "="
  											+ pageContext.findAttribute("executionCourseOID")
               				   				+ "&amp;"
			  								+ SessionConstants.EXECUTION_PERIOD_OID
  											+ "="
	  										+ pageContext.findAttribute("executionPeriodOID")
  											+ "&amp;"
  											+ SessionConstants.CURRICULAR_YEAR_OID
				  							+ "="
  											+ pageContext.findAttribute("curricularYearOID")
  											+ "&amp;"
			  								+ SessionConstants.EXECUTION_DEGREE_OID
  											+ "="
  											+ pageContext.findAttribute("executionDegreeOID") %>">
							<bean:message key="link.edit"/>
						</html:link>
					</td>
					<td>
						<html:link page="<%= "/manageLesson.do?method=deleteLesson&amp;page=0&amp;"
    	           							+ SessionConstants.LESSON_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("lessonOID")
               					   			+ "&amp;"
    	           							+ SessionConstants.SHIFT_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("shiftOID")
               					   			+ "&amp;"
			  								+ SessionConstants.EXECUTION_COURSE_OID
  											+ "="
  											+ pageContext.findAttribute("executionCourseOID")
               				   				+ "&amp;"
			  								+ SessionConstants.EXECUTION_PERIOD_OID
  											+ "="
	  										+ pageContext.findAttribute("executionPeriodOID")
  											+ "&amp;"
  											+ SessionConstants.CURRICULAR_YEAR_OID
				  							+ "="
  											+ pageContext.findAttribute("curricularYearOID")
  											+ "&amp;"
			  								+ SessionConstants.EXECUTION_DEGREE_OID
  											+ "="
  											+ pageContext.findAttribute("executionDegreeOID") %>"
  								onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
								<bean:message key="link.delete"/>
						</html:link>					
					</td>
					<td>
						<html:link page="<%= "/manageLesson.do?method=viewAllLessonDates&amp;page=0&amp;"
    	           							+ SessionConstants.LESSON_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("lessonOID")
               					   			+ "&amp;"
    	           							+ SessionConstants.SHIFT_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("shiftOID")
               					   			+ "&amp;"
			  								+ SessionConstants.EXECUTION_COURSE_OID
  											+ "="
  											+ pageContext.findAttribute("executionCourseOID")
               				   				+ "&amp;"
			  								+ SessionConstants.EXECUTION_PERIOD_OID
  											+ "="
	  										+ pageContext.findAttribute("executionPeriodOID")
  											+ "&amp;"
  											+ SessionConstants.CURRICULAR_YEAR_OID
				  							+ "="
  											+ pageContext.findAttribute("curricularYearOID")
  											+ "&amp;"
			  								+ SessionConstants.EXECUTION_DEGREE_OID
  											+ "="
  											+ pageContext.findAttribute("executionDegreeOID") %>">
								<bean:message key="link.show.all.lesson.dates"/>
						</html:link>					
					</td>
				</tr>
			</logic:iterate>
		</table>
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
				<bean:message key="link.delete"/>
			</html:submit>
		</p>
	  </html:form>
	</logic:present>

	<logic:notPresent name="shift" property="infoLessons">
		<p>
			<span class="error"><!-- Error messages go here -->
				<bean:message key="message.shift.lessons.none"/>
			</span>
		</p>
	</logic:notPresent>