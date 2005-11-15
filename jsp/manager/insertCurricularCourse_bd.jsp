<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.curricularCourse" /></h2>

<br>

<span class="error"><html:errors/></span>

<html:form action="/insertCurricularCourse" method ="get">  
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="insert"/>
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>	
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.name"/>
			</td>
			<td>
				<html:text size="60" property="name" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.nameEn"/>
			</td>
			<td>
				<html:text size="60" property="nameEn" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourse.code"/>
			</td>
			<td>
				<html:text size="12" property="code" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.acronym"/>
			</td>
			<td>
				<html:text size="12" property="acronym" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.type"/>
			</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType" bundle="ENUMERATION_RESOURCES"/>
				<html:select property="type">
					<html:options collection="values" property="value" labelProperty="label"/>
    			</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.mandatory"/>
			</td>
			<td>
				<html:checkbox property="mandatory" value ="true"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.basic"/>
			</td>
			<td>
				<html:checkbox property="basic" value ="true"/>
			</td>
		</tr>		
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.theoreticalHours"/>
			</td>
			<td>
				<html:text size="5" property="theoreticalHours" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.praticalHours"/>
			</td>
			<td>
				<html:text size="5" property="praticalHours" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.theoPratHours"/>
			</td>
			<td>
				<html:text size="5" property="theoPratHours" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.labHours"/>
			</td>
			<td>
				<html:text size="5" property="labHours" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.maxIncrementNac"/>
			</td>
			<td>
				<html:text size="5" property="maxIncrementNac" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.minIncrementNac"/>
			</td>
			<td>
				<html:text size="5" property="minIncrementNac" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.credits" />
			</td>
			<td>
				<html:text size="5" property="credits" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.ectsCredits"/>
			</td>
			<td>
				<html:text size="5" property="ectsCredits" />
			</td>
		</tr>			
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.weight"/>
			</td>
			<td>
				<html:text size="5" property="weight" />
			</td>
		</tr>			

		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.enrollmentWeigth"/>
			</td>
			<td>
				<html:text size="5" property="enrollmentWeigth"/>
			</td>
		</tr>			
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.mandatoryEnrollment"/>
			</td>
			<td>
				<html:checkbox property="mandatoryEnrollment" value ="true"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.enrollmentAllowed"/>
			</td>
			<td>
				<html:checkbox property="enrollmentAllowed" value ="true"/>
			</td>
		</tr>
	</table>
	
	<br>
	
	<html:submit styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</html:form>