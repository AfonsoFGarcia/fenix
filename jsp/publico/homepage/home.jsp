<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="string" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<html:xhtml/>

<logic:present name="homepage">
	<logic:equal name="homepage" property="activated" value="true">

		<h1 id="no">
			<%-- <bean:message bundle="HOMEPAGE_RESOURCES" key="title.homepage.of"/>: --%>
			<bean:write name="homepage" property="ownersName"/>
		</h1>

	<table class="invisible thleft">
		<!-- photo -->
		<logic:equal name="homepage" property="showPhoto" value="true">
        <tr>
            <th></th>
            <td>
                <logic:notEmpty name="homepage" property="person.personalPhoto">
            			<bean:define id="homepageID" name="homepage" property="idInternal"/>
            			<html:img src="<%= request.getContextPath() +"/publico/viewHomepage.do?method=retrievePhoto&amp;homepageID=" + homepageID.toString() %>" style="padding: 1em 0;" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
                </logic:notEmpty>
<%--
                <logic:empty name="homepage" property="person.personalPhoto">
                    <html:img src="<%= request.getContextPath() +"/images/photoPlaceHolder.jpg" %>" style="padding: 1em 0;" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
                </logic:empty>
--%>                
            </td>
        </tr>
		</logic:equal>

		<!-- units -->
		<logic:equal name="homepage" property="showUnit" value="true">
		<tr>
			<th><bean:message key="label.homepage.showUnit" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<logic:present name="homepage" property="person.employee.currentWorkingContract.workingUnit">
					<bean:define id="currentUnit" name="homepage" property="person.employee.currentWorkingContract.workingUnit" toScope="request"/>
					<jsp:include page="unitStructure.jsp"/>
				</logic:present>
			</td>
		</tr>
		</logic:equal>
		
		<!-- categories -->
		<logic:equal name="homepage" property="showCategory" value="true">
		<tr>
			<th><bean:message key="label.homepage.showCategory" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
			<logic:present name="homepage" property="person.teacher">
				<logic:present name="homepage" property="person.employee.currentWorkingContract">
					<string:capitalizeAllWords>
						<string:lowerCase>
							<bean:write name="homepage" property="person.teacher.category.name.content"/>
						</string:lowerCase>
					</string:capitalizeAllWords>
				</logic:present>
			</logic:present>
			</td>
		</tr>
		</logic:equal>
		

		<!-- research unit -->
		<logic:equal name="homepage" property="showResearchUnitHomepage" value="true">
		<%-- <logic:empty name="homepage" property="person.workingResearchUnits"> --%>
			<logic:present name="homepage" property="person.teacher">
				<logic:present name="homepage" property="person.employee.currentWorkingContract">
					<logic:present name="homepage" property="researchUnitHomepage">
						<logic:present name="homepage" property="researchUnit">
							<bean:define id="researchUnitHomepage" type="java.lang.String" name="homepage" property="researchUnitHomepage"/>
							<logic:notEmpty name="researchUnitHomepage">
								<tr>
									<th><bean:message key="label.homepage.showResearchUnitHomepage" bundle="HOMEPAGE_RESOURCES"/>:</th>
									<logic:match name="homepage" property="researchUnitHomepage" value="http://">
										<bean:define id="url" type="java.lang.String" name="homepage" property="researchUnitHomepage"/>
										<td>
											<%= ContentInjectionRewriter.HAS_CONTEXT_PREFIX %><html:link href="<%= url %>"><bean:write name="homepage" property="researchUnit.content"/></html:link>
										</td>
									</logic:match>
									<logic:notMatch name="homepage" property="researchUnitHomepage" value="http://">
										<logic:match name="homepage" property="researchUnitHomepage" value="https://">
											<bean:define id="url" type="java.lang.String" name="homepage" property="researchUnitHomepage"/>
											<td>
												<%= ContentInjectionRewriter.HAS_CONTEXT_PREFIX %><html:link href="<%= url %>"><bean:write name="homepage" property="researchUnit.content"/></html:link>
											</td>
										</logic:match>
										<logic:notMatch name="homepage" property="researchUnitHomepage" value="https://">
											<td>
												<bean:define id="url" type="java.lang.String">http://<bean:write name="homepage" property="researchUnitHomepage"/></bean:define>
												<%= ContentInjectionRewriter.HAS_CONTEXT_PREFIX %><html:link href="<%= url %>"><bean:write name="homepage" property="researchUnit.content"/></html:link>
											</td>
										</logic:notMatch>
									</logic:notMatch>
								</tr>
							</logic:notEmpty>
						</logic:present>
					</logic:present>
				</logic:present>
			</logic:present>
		<%-- </logic:empty>
		<logic:notEmpty name="homepage" property="person.workingResearchUnits">
			<tr>
				<th><bean:message key="label.homepage.showResearchUnitHomepage" bundle="HOMEPAGE_RESOURCES"/>:</th>
				<td>
					<logic:iterate id="unit" name="homepage" property="person.workingResearchUnits" length="1">
						<fr:view name="unit">
							<fr:layout name="unit-link">
								<fr:property name="unitLayout" value="values"/>
								<fr:property name="unitSchema" value="unit.name"/>
								<fr:property name="targetBlank" value="true"/>
								<fr:property name="parenteShown" value="true"/>
								<fr:property name="separator" value="<br/>"/>
							</fr:layout>
						</fr:view>
					</logic:iterate>
				</td>
				<logic:iterate id="unit" name="homepage" property="person.workingResearchUnits" offset="1">
					<td>&nbsp;</td>
					<td>
						<fr:view name="unit">
							<fr:layout name="unit-link">
								<fr:property name="unitLayout" value="values"/>
								<fr:property name="unitSchema" value="unit.name"/>
								<fr:property name="targetBlank" value="true"/>
								<fr:property name="parenteShown" value="true"/>
								<fr:property name="separator" value="<br/>"/>
							</fr:layout>
						</fr:view>
					</td>
				</logic:iterate>
			</tr>
		</logic:notEmpty>
		--%>
		</logic:equal>


		<!--  -->
		<logic:equal name="homepage" property="showActiveStudentCurricularPlans" value="true">
            <logic:notEmpty name="homepage" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName">
		<tr>
			<th><bean:message key="label.homepage.showActiveStudentCurricularPlans" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<logic:iterate id="studentCurricularPlan" name="homepage" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName" length="1">
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/showDegreeSite.do?method=showDescription&amp;degreeID=<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
					<html:link href="<%= url %>">
						<logic:present name="studentCurricularPlan" property="specialization.name">
							<logic:equal name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:equal>
							<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notEqual>
						</logic:present>
						<logic:notPresent name="studentCurricularPlan" property="specialization.name">
							<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
						</logic:notPresent>
						<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
						<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
					</html:link>
				</logic:iterate>
				<logic:iterate id="studentCurricularPlan" name="homepage" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName" offset="1">
					,
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/showDegreeSite.do?method=showDescription&amp;degreeID=<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
					<html:link href="<%= url %>">
						<logic:present name="studentCurricularPlan" property="specialization.name">
							<logic:equal name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:equal>
							<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notEqual>
						</logic:present>
						<logic:notPresent name="studentCurricularPlan" property="specialization.name">
							<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
						</logic:notPresent>
						<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
						<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
					</html:link>
				</logic:iterate>
			</td>
		</tr>
        </logic:notEmpty>
		</logic:equal>


		<!--  -->
		<logic:notEmpty name="personAttends">
		<logic:equal name="homepage" property="showCurrentAttendingExecutionCourses" value="true">
		<tr>
			<th><bean:message key="label.homepage.showCurrentAttendingExecutionCourses" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<logic:iterate id="attend" name="personAttends" length="1">
					<bean:define id="executionCourse" name="attend" property="disciplinaExecucao"/>
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/executionCourse.do?method=firstPage&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>
					<html:link href="<%= url %>">
						<bean:write name="executionCourse" property="nome"/>
					</html:link>
				</logic:iterate>
				<logic:iterate id="attend" name="personAttends" offset="1">
					,
					<bean:define id="executionCourse" name="attend" property="disciplinaExecucao"/>
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/executionCourse.do?method=firstPage&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>
					<html:link href="<%= url %>">
						<bean:write name="executionCourse" property="nome"/>
					</html:link>
				</logic:iterate>
			</td>
		</tr>
		</logic:equal>
		</logic:notEmpty>


		<!--  -->
		<logic:equal name="homepage" property="showAlumniDegrees" value="true">
        <logic:notEmpty name="homepage" property="person.completedStudentCurricularPlansSortedByDegreeTypeAndDegreeName">
		<tr>
			<th><bean:message key="label.homepage.showAlumniDegrees" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<logic:iterate id="studentCurricularPlan" name="homepage" property="person.completedStudentCurricularPlansSortedByDegreeTypeAndDegreeName" length="1">
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/showDegreeSite.do?method=showDescription&amp;degreeID=<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
					<html:link href="<%= url %>">
						<logic:present name="studentCurricularPlan" property="specialization.name">
							<logic:equal name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:equal>
							<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notEqual>
						</logic:present>
						<logic:notPresent name="studentCurricularPlan" property="specialization.name">
							<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
						</logic:notPresent>
						<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
						<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
					</html:link>
				</logic:iterate>
				<logic:iterate id="studentCurricularPlan" name="homepage" property="person.completedStudentCurricularPlansSortedByDegreeTypeAndDegreeName" offset="1">
					,
					<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/publico/showDegreeSite.do?method=showDescription&amp;degreeID=<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
					<html:link href="<%= url %>">
						<logic:present name="studentCurricularPlan" property="specialization.name">
							<logic:equal name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:equal>
							<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notEqual>
						</logic:present>
						<logic:notPresent name="studentCurricularPlan" property="specialization.name">
							<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
						</logic:notPresent>
						<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
						<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
					</html:link>
				</logic:iterate>
			</td>
		</tr>
        </logic:notEmpty>
		</logic:equal>
		
		
		<!--  E-mail -->
		<logic:equal name="homepage" property="showEmail" value="true">
        <logic:notEmpty name="homepage" property="person.email">
		<tr>
			<th>E-mail:</th>
			<td>
				<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
				<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>
				<bean:define id="emailURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/publico/viewHomepage.do?method=emailPng&amp;homepageID=<bean:write name="homepage" property="idInternal"/></bean:define>
				<html:img align="middle" src="<%= emailURL %>" altKey="email" bundle="IMAGE_RESOURCES"/>
			</td>
		</tr>
        </logic:notEmpty>
		</logic:equal>
		
		
		<!--  Telephone-->
		<logic:equal name="homepage" property="showTelephone" value="true">
        <logic:notEmpty name="homepage" property="person.phone">
		<tr>
			<th><bean:message key="label.homepage.showTelephone" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td><bean:write name="homepage" property="person.phone"/></td>
		</tr>
        </logic:notEmpty>
		</logic:equal>
		
	
		<!--  -->
		<logic:equal name="homepage" property="showWorkTelephone" value="true">
        <logic:notEmpty name="homepage" property="person.workPhone">
		<tr>
			<th><bean:message key="label.homepage.showWorkTelephone" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td><bean:write name="homepage" property="person.workPhone"/></td>
		</tr>
        </logic:notEmpty>
		</logic:equal>
		
		<!--  -->
		<logic:equal name="homepage" property="showMobileTelephone" value="true">
        <logic:notEmpty name="homepage" property="person.mobile">
		<tr>
			<th><bean:message key="label.homepage.showMobileTelephone" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td><bean:write name="homepage" property="person.mobile"/></td>
		</tr>
        </logic:notEmpty>
		</logic:equal>
		
		<!--  -->
		<logic:equal name="homepage" property="showAlternativeHomepage" value="true">
        <logic:notEmpty name="homepage" property="person.webAddress">
		<tr>
			<th><bean:message key="label.homepage.showAlternativeHomepage" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<bean:define id="url" type="java.lang.String" name="homepage" property="person.webAddress"/>
				<html:link href="<%= url %>">
					<bean:write name="homepage" property="person.webAddress"/>
				</html:link>
			</td>
		</tr>
        </logic:notEmpty>
		</logic:equal>
		

		<!--  -->
		<logic:equal name="homepage" property="showCurrentExecutionCourses" value="true">
            <logic:present name="homepage" property="person.teacher">
                <logic:present name="homepage" property="person.employee.currentWorkingContract">
                		<tr>
                			<th><bean:message key="label.homepage.showCurrentExecutionCourses" bundle="HOMEPAGE_RESOURCES"/>:</th>
                			<td>

        						<logic:iterate id="executionCourse" name="homepage" property="person.teacher.currentExecutionCourses" length="1">        						
	        						<app:contentLink name="executionCourse" property="site">
										<bean:write name="executionCourse" property="nome"/>
									</app:contentLink>		        							        						
        						</logic:iterate>

        						<logic:iterate id="executionCourse" name="homepage" property="person.teacher.currentExecutionCourses" offset="1">
        							,        							
        							<app:contentLink name="executionCourse" property="site">
										<bean:write name="executionCourse" property="nome"/>
									</app:contentLink>	        						        						
        						</logic:iterate>        						
                			</td>
                		</tr>
                </logic:present>
            </logic:present>
		</logic:equal>
						
		</table>

<!--
		<logic:equal name="homepage" property="showResearchUnitHomepage" value="true">
			<br/>
		</logic:equal>
-->
		
	</logic:equal>
</logic:present>