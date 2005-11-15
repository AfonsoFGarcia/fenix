<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<bean:define id="targetAction" name="targetAction" type="java.lang.String"/>
<bean:define id="userGroupTypeToAdd" type="net.sourceforge.fenixedu.domain.cms.UserGroupTypes" name="userGroupTypeToAdd"/>
<e:define id="userGroupTypeToAddString" enumeration="userGroupTypeToAdd" bundle="ENUMERATION_RESOURCES"/>
<h2><bean:message bundle="CMS_RESOURCES" key="cms.userGroupsManagement.title.label"/></h2>
<bean:message bundle="CMS_RESOURCES" key="cms.userGroupsManagement.creating.label"/> <b><bean:write name="userGroupTypeToAddString"/></b><br/>
<b><bean:message bundle="CMS_RESOURCES" key="cms.name.label"/>:</b> <bean:write property="name" name="userGroupForm"/><br/>
<b><bean:message bundle="CMS_RESOURCES" key="cms.description.label"/>:</b> <bean:write name="userGroupForm" property="description"/><br/>

<logic:present name="executionPeriods">
	<html:form action="<%=targetAction%>" method="get">  
		<html:hidden property="name"/>
		<html:hidden property="description"/>		
		<html:hidden property="userGroupType"/>
		<html:hidden property="method" value="prepareChooseExecDegreeAndCurYear" />
		<table width="100%">
			<tr>
				<td width="10%">
					<bean:message bundle="CMS_RESOURCES" key="cms.userGroupsManagement.executionCourseStudentsOptions.executionPeriodSelection.label" />
					:
				</td>
				<td width="90%">
					<html:select property="executionPeriodID">
						<html:options collection="executionPeriods" property="idInternal" labelProperty="qualifiedName"/>
					</html:select>
				</td>
			</tr>
		</table>
		<br />
		<html:submit styleClass="inputbutton"><bean:message bundle="CMS_RESOURCES" key="cms.userGroupsManagement.choose.button"/></html:submit>
	</html:form>
</logic:present>

<logic:present name="degrees">	
	<html:form action="<%=targetAction%>" method="get">
		<html:hidden property="name"/>
		<html:hidden property="description"/>		
		<html:hidden property="userGroupType"/>
		<html:hidden property="executionPeriodID"/>
		<html:hidden property="method" value="prepareChooseExecutionCourse" />

		<table width="100%">		
			<tr>
				<td witdh="10%">
					<bean:message bundle="CMS_RESOURCES" key="cms.userGroupsManagement.executionCourseStudentsOptions.degreeSelection.label"/>
					:
				</td>
				<td witdh="90%">
					<html:select property="executionDegreeID">
						<html:options collection="degrees" property="idInternal" labelProperty="qualifiedName"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td witdh="10%">
					<bean:message bundle="CMS_RESOURCES" key="cms.userGroupsManagement.executionCourseStudentsOptions.curricularYearSelection.label"/>
					:
				</td>
				<td witdh="90%">
					<html:select property="curricularYear">
						<html:option value="1">1</html:option>
						<html:option value="2">2</html:option>
						<html:option value="3">3</html:option>
						<html:option value="4">4</html:option>
						<html:option value="5">5</html:option>
					</html:select>
				</td>
			</tr>		
		</table>
		<br />
		<html:submit styleClass="inputbutton">
			<bean:message bundle="CMS_RESOURCES" key="cms.userGroupsManagement.choose.button"/>
		</html:submit>
	</html:form>
</logic:present>


<logic:present name="courses">
	<html:form action="<%=targetAction%>" method="get">
		<html:hidden property="name"/>
		<html:hidden property="description"/>		
		<html:hidden property="userGroupType"/>
		<html:hidden property="method" value="createGroup" />	
			<table width="100%">
				<tr>
					<td valign="top" width="10%">
					<bean:message bundle="CMS_RESOURCES" key="cms.userGroupsManagement.executionCourseStudentsOptions.courseSelection.label"/>
					:
					</td>
					<td width="100%">
						<table width="90%">
							<tr>	
								<td>		
									<logic:notEmpty name="courses">
										<table width="100%" cellpadding="0" border="0">
											<tr>
												<td>&nbsp;
												</td>
												<td class="listClasses-header"><bean:message bundle="CMS_RESOURCES" key="cms.course.label" />
												</td>
												<td>&nbsp;
												</td>
											</tr>													
											<bean:define id="viewAction" name="viewAction" type="java.lang.String"/>				
											<logic:iterate id="executionCourse" name="courses" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse">
												<bean:define id="infoExecutionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
												<tr>	 			
													<td class="listClasses">
														<html:radio idName="executionCourse" property="executionCourseID" value="idInternal"/>
													</td>
													<td class="listClasses" style="text-align:left"><bean:write name="executionCourse" property="nome"/>
													</td>
													<td class="listClasses">											
														<%
														Map params = new HashMap();
														params.put("method","viewExecutionCourseGroupElements");
														params.put("executionCourseID",executionCourse.getIdInternal());
														request.setAttribute("params",params);
														 %>
														 
														<html:link name="params" action="<%=viewAction%>" target="_blank" ><bean:message bundle="CMS_RESOURCES" key="cms.userGroupsManagement.viewElements.link"/></html:link>
													</td>
								 				</tr>
								 			</logic:iterate>						
										</table>
									</logic:notEmpty>	 	
								</td>
							</tr>
						</table>						
					</td>
				</tr>
			</table>
	<html:submit styleClass="inputbutton">
			<bean:message bundle="CMS_RESOURCES" key="cms.userGroupsManagement.save.button"/>
		</html:submit>
	</html:form>
</logic:present>

