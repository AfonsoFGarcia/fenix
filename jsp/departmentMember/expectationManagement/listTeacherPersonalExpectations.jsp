<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.departmentMember"/></em>
<h2><bean:message key="label.see.teachers.personal.expectations"/></h2>

<logic:present role="DEPARTMENT_MEMBER">

	<logic:notEmpty name="executionYearBean">
		<p>
			<fr:form>
				<b><bean:message key="label.common.executionYear"/>:</b> 
				<fr:edit id="executionYear" name="executionYearBean" slot="executionYear"> 
					<fr:layout name="menu-select-postback">
						<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsToViewTeacherPersonalExpectationsProvider"/>
						<fr:property name="format" value="${year}"/>
						<fr:destination name="postback" path="/listTeachersPersonalExpectations.do?method=listTeachersPersonalExpectationsForSelectedExecutionYear"/>
					</fr:layout>
				</fr:edit>	
			</fr:form>
		</p>
	
		<logic:empty name="teachersPersonalExpectations">
			<p><bean:message key="label.undefined.visualization.period" bundle="DEPARTMENT_MEMBER_RESOURCES"/></p>
		</logic:empty>
	
		<logic:notEmpty name="teachersPersonalExpectations">
		
			<bean:define id="executionYear" name="executionYearBean" property="executionYear" type="net.sourceforge.fenixedu.domain.ExecutionYear"/>
		
			<table class="tstyle2 thleft">			
				<tr>
					<th><bean:message key="label.teacher.name" bundle="DEPARTMENT_MEMBER_RESOURCES"/></th>
					<th><bean:message key="label.teacher.number" bundle="DEPARTMENT_MEMBER_RESOURCES"/></th>	
					<th><bean:message key="label.teacher.category" bundle="DEPARTMENT_MEMBER_RESOURCES"/></th>				
					<th><bean:message key="label.teacher.expectation" bundle="DEPARTMENT_MEMBER_RESOURCES"/></th>	
					<th><bean:message key="label.teacher.auto.evaluation" bundle="DEPARTMENT_MEMBER_RESOURCES"/></th>					
				</tr>
				<logic:iterate id="mapEntry" name="teachersPersonalExpectations">		
					<bean:define id="teacher" name="mapEntry" property="key" type="net.sourceforge.fenixedu.domain.Teacher"/>				
					<tr>
						<td>
							<logic:notEmpty name="mapEntry" property="value">
								<html:link page="/listTeachersPersonalExpectations.do?method=seeTeacherPersonalExpectation" paramId="teacherPersonalExpectationID" paramName="mapEntry" paramProperty="value.idInternal">
									<bean:write name="teacher" property="person.name"/>
								</html:link>
							</logic:notEmpty>
							<logic:empty name="mapEntry" property="value">					
								<bean:write name="teacher" property="person.name"/>
							</logic:empty>							
						</td>							
						<td><bean:write name="teacher" property="teacherNumber"/></td>
						<td>
							<logic:notEmpty name="teacher" property="category">
								<bean:write name="teacher" property="category.code"/>
							</logic:notEmpty>
							<logic:empty name="teacher" property="category">
								--
							</logic:empty>
						</td>
						<td>
							<logic:empty name="mapEntry" property="value">
								<bean:message key="label.no" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
							</logic:empty>
							<logic:notEmpty name="mapEntry" property="value">
								<bean:message key="label.yes" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
							</logic:notEmpty>
						</td>
						<td>
							<logic:empty name="mapEntry" property="value">
								<bean:message key="label.no" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
							</logic:empty>
							<logic:notEmpty name="mapEntry" property="value">
								<logic:notEmpty name="mapEntry" property="value.autoEvaluation">
									<bean:message key="label.yes" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
								</logic:notEmpty>
								<logic:empty name="mapEntry" property="value.autoEvaluation">
									<bean:message key="label.no" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
								</logic:empty>	
							</logic:notEmpty>
						</td>	
					</tr>																							
				</logic:iterate>			
			</table>	
			
		</logic:notEmpty>
	</logic:notEmpty>	
</logic:present>
