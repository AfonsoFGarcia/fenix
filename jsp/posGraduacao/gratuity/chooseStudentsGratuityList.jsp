<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<h2><bean:message key="link.masterDegree.administrativeOffice.gratuity.listStudents"/></h2>
<span class="error"><html:errors/></span>

<html:form action="/studentsGratuityList" >
<logic:notPresent name="showNextSelects">
	<html:hidden property="method" value="prepareChooseDegree"/>
	<html:hidden property="page" value="1"/>
</logic:notPresent>
<logic:present name="showNextSelects">
	<html:hidden property="method" value="studentsGratuityList"/>
	<html:hidden property="executionYear"/>
	<html:hidden property="page" value="2"/>
</logic:present>
<table>
		<tr>
			<td>
				<bean:message key="label.masterDegree.gratuity.executionYear"/>
			</td>
			<td>
				<html:select property="executionYear" onchange="document.studentsGratuityListForm.method.value='prepareChooseDegree';document.studentsGratuityListForm.submit();">
					<html:option value="" key="label.manager.executionCourseManagement.select">
						<bean:message key="label.manager.executionCourseManagement.select"/>
					</html:option>
					<html:optionsCollection name="executionYears"/>
				</html:select>
			</td>
		</tr>
		<logic:present name="showNextSelects">			
			<tr>
				<td>
					<bean:message key="label.qualification.degree"/>
				</td>
				<td>
					<html:select property="degree">
						<html:option value="" key="label.manager.executionCourseManagement.select">
							<bean:message key="label.manager.executionCourseManagement.select"/>
						</html:option>
						<html:optionsCollection name="<%=SessionConstants.DEGREES%>"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="label.masterDegree.gratuity.specializationArea"/>
				</td>
				<td>
					<html:select property="specialization">
						<html:option value="" key="label.manager.executionCourseManagement.select">
							<bean:message key="label.manager.executionCourseManagement.select"/>
						</html:option>
						<html:option value="all" key="label.masterDegree.gratuity.all">
							<bean:message key="label.gratuitySituationType.all"/>
						</html:option>
						<html:optionsCollection name="specializations"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="label.masterDegree.gratuity.situation"/>
				</td>
				<td>
					<html:select property="situation">
						<html:option value="">
							<bean:message key="label.manager.executionCourseManagement.select"/>
						</html:option>						
						<html:option value="all">
							<bean:message key="label.gratuitySituationType.all"/>
						</html:option>
						<logic:iterate id="gratuitySituation" name="situations">
							<bean:define id="gratuitySituationName" name="gratuitySituation" property="name"/>
							<bean:define id="gratuitySituationNameKEY" value="<%= "label.gratuitySituationType." + gratuitySituationName.toString() %>"/>						
							<html:option value="<%= gratuitySituationName.toString() %>" >
								<bean:message name="gratuitySituationNameKEY"/>
							</html:option>
						</logic:iterate>
					</html:select>
				</td>
			</tr>
		</logic:present>
	</table>
	<br />
	<logic:present name="showNextSelects">
		<html:submit styleClass="inputbutton">
			<bean:message key="button.masterDegree.gratuity.list"/>
		</html:submit>
	</logic:present>		
</table>	
</html:form>	