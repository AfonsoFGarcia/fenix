<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>

<p class="mtop2 mbottom05"><strong>Turmas associadas ao turno:</strong></p>

<logic:present name="shift" property="infoClasses">
  <html:form action="/manageShiftMultipleItems">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="removeClasses"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

    <html:hidden alt="<%= SessionConstants.ACADEMIC_INTERVAL %>" property="<%= SessionConstants.ACADEMIC_INTERVAL %>"
                 value="<%= pageContext.findAttribute(SessionConstants.ACADEMIC_INTERVAL).toString() %>"/>
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
				<bean:message key="label.name"/>
			</th>
			<th>
				<bean:message key="link.schedules.remove"/>
			</th>				
		</tr>		
	
		<bean:define id="deleteConfirm">
			return confirm('<bean:message key="message.confirm.remove.class"/>')
		</bean:define>
	<logic:iterate id="shiftClass" name="shift" property="infoClasses">
		<bean:define id="classOID" name="shiftClass" property="idInternal"/>
			<tr>
              	<td>
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedItems" property="selectedItems">
						<bean:write name="shiftClass" property="idInternal"/>
					</html:multibox>
				</td>
				<td>
					<html:link page="<%= "/manageClass.do?method=prepare&amp;"
							+ SessionConstants.CLASS_VIEW_OID
							+ "="
							+ pageContext.findAttribute("classOID")
							+ "&amp;"
							+ SessionConstants.ACADEMIC_INTERVAL
							+ "="
							+ pageContext.findAttribute(SessionConstants.ACADEMIC_INTERVAL)
							+ "&amp;"
							+ SessionConstants.CURRICULAR_YEAR_OID
							+ "="
							+ pageContext.findAttribute("curricularYearOID")
							+ "&amp;"
							+ SessionConstants.EXECUTION_DEGREE_OID
							+ "="
							+ pageContext.findAttribute("executionDegreeOID") %>">
						<div align="center">
							<jsp:getProperty name="shiftClass" property="nome" />
						</div>
					</html:link>
				</td>
				<td>
					<div align="center">
						<html:link page="<%= "/manageShift.do?method=removeClass&amp;"
								+ SessionConstants.CLASS_VIEW_OID
							  	+ "="
							  	+ pageContext.findAttribute("classOID")
							  	+ "&amp;"
								+ SessionConstants.SHIFT_OID
							  	+ "="
							  	+ pageContext.findAttribute("shiftOID")
							  	+ "&amp;"
								+ SessionConstants.EXECUTION_COURSE_OID
							  	+ "="
							  	+ pageContext.findAttribute("executionCourseOID")
							  	+ "&amp;"
							  	+ SessionConstants.ACADEMIC_INTERVAL
					            + "="
					            + pageContext.findAttribute(SessionConstants.ACADEMIC_INTERVAL)
							  	+ "&amp;"
							  	+ SessionConstants.CURRICULAR_YEAR_OID
								+ "="
							  	+ pageContext.findAttribute("curricularYearOID")
							  	+ "&amp;"
								+ SessionConstants.EXECUTION_DEGREE_OID
							  	+ "="
								+ pageContext.findAttribute("executionDegreeOID") %>"
									onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
							<bean:message key="link.schedules.remove"/>
						</html:link>
					</div>
				</td>
			</tr>
	</logic:iterate>
	</table>
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
			<bean:message key="link.schedules.remove"/>
		</html:submit>
	</p>
  </html:form>
</logic:present>

<logic:notPresent name="shift" property="infoClasses">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<bean:message key="message.shift.classes.none"/>
		</span>
	</p>
</logic:notPresent>
