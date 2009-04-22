<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<logic:present name="employee">
	<bean:define id="month" value='<%=request.getParameter("month")%>'/>
	<bean:define id="year" value='<%=request.getParameter("year")%>'/>
	<bean:define id="employeeNumber" name="employee" property="employeeNumber" />
	<bean:define id="personID" name="employee" property="person.idInternal" />
	
	<p class="invisible"><bean:message key="label.show"/>: <html:link
		page="<%="/viewEmployeeAssiduousness.do?method=showWorkSheet&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="link.workSheet" />
	</html:link>, <html:link
		page="<%="/viewEmployeeAssiduousness.do?method=showSchedule&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="label.schedule" />
	</html:link>, <html:link
		page="<%="/viewEmployeeAssiduousness.do?method=showClockings&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="link.clockings" />
	</html:link>, <html:link
		page="<%="/viewEmployeeAssiduousness.do?method=showJustifications&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="link.justifications" />
 	</html:link>, <html:link
		page="<%="/viewEmployeeAssiduousness.do?method=showBalanceResume&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="link.balanceResume" />
	</html:link>, <html:link
		page="<%="/viewEmployeeAssiduousness.do?method=showVacations&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="link.vacations" />
	</html:link>, <html:link
		page="<%="/viewEmployeeAssiduousness.do?method=showStatus&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="title.showStatus" />
<%-- 	</html:link>, <html:link
		page="<%="/viewEmployeeAssiduousness.do?entryID=1&method=showVacationsMap&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="link.showVacationsMap" />
		--%>
	</html:link></p>
	
	<logic:present name="showJustificationsLinks">
	<%
	net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session.getAttribute(pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE);
	if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) {%>
		<logic:notPresent name="employeeJustificationFactory">
			<p><bean:message key="link.insert" />: 
			<html:link page="<%="/employeeAssiduousness.do?method=prepareCreateEmployeeJustification&correction=JUSTIFICATION&employeeNumber="+employeeNumber.toString()+"&month="+month.toString()+"&year="+year.toString()%>"><bean:message key="label.justification" /></html:link>,
			<bean:message key="label.regularization" /> (<html:link page="<%="/employeeAssiduousness.do?method=prepareCreateEmployeeJustification&correction=REGULARIZATION&employeeNumber="+employeeNumber.toString()+"&month="+month.toString()+"&year="+year.toString()%>"><bean:message key="link.dayRegularization" /></html:link>, 
											 			 <html:link page="<%="/employeeAssiduousness.do?method=prepareCreateMissingClockingMonth&correction=REGULARIZATION&employeeNumber="+employeeNumber.toString()+"&month="+month.toString()+"&year="+year.toString()%>"><bean:message key="link.monthRegularization" /></html:link>)
			</p>
		</logic:notPresent>
	<%}%>
	</logic:present>
	
		
	<%--
	<span class="toprint"><br /></span>
	--%>
	
	<table border="0">
		<tr><td class="invisible">
		<html:img src="<%= request.getContextPath() +"/personnelSection/viewEmployeeAssiduousness.do?method=showPhoto&amp;personID="+personID.toString() %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
		</td><td>
		<fr:view name="employee" schema="show.employeeInformation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="showinfo1 thbold" />
			</fr:layout>
		</fr:view>
		</td></tr>
	</table>

	<logic:present name="yearMonth">
		<bean:define id="yearMonthSchema" value='<%=request.getParameter("yearMonthSchema")%>'/>
		<logic:notEqual name="yearMonthSchema" value="false">
			<bean:define id="method" value='<%=request.getParameter("method")%>'/>
			<div class="mvert1 invisible">
				<fr:form action="<%="/viewEmployeeAssiduousness.do?method="+method.toString()%>">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="employeeForm" property="method" value="<%=method.toString()%>" />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.employeeNumber" name="employeeForm" property="employeeNumber" value="<%=employeeNumber.toString()%>"/>
					<fr:edit id="yearMonth" name="yearMonth" schema="<%= yearMonthSchema.toString()%>">
						<fr:layout>
							<fr:property name="classes" value="thlight thright mtop05 mbottom0" />
						</fr:layout>
					</fr:edit>
					<p>
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
							<bean:message key="button.submit" />
						</html:submit>
					</p>
				</fr:form>
			</div>
			<div class="toprint">
				<p class="bold mbottom0">
					 <bean:message key="<%=month.toString()%>" bundle="ENUMERATION_RESOURCES" /> 
					 <bean:write name="year" />
				</p><br/>
			</div>
		</logic:notEqual>
	</logic:present>
	
	<fr:view name="employee" property="assiduousness.assiduousnessStatusHistoriesOrdered" schema="show.employeeStatus">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thbold tdcenter" />
		</fr:layout>
	</fr:view>
</logic:present>	