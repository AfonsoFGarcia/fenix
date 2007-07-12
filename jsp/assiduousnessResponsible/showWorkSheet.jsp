<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousnessResponsible" bundle="ASSIDUOUSNESS_RESOURCES"/></em>
<h2><bean:message key="link.showEmployeeWorkSheet" bundle="ASSIDUOUSNESS_RESOURCES"/></h2>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="ASSIDUOUSNESS_RESOURCES">
		<p><span class="error0"><bean:write name="message"/></span></p>
	</html:messages>
	<br/>
</logic:messagesPresent>

<logic:present name="employeeWorkSheet">
	<bean:define id="yearMonth" name="yearMonth"/>
	<bean:define id="month" name="yearMonth" property="month" />
	<bean:define id="year" name="yearMonth" property="year" />
	<bean:define id="employee" name="employeeWorkSheet" property="employee"/>
	<bean:define id="employeeNumber" name="employee" property="employeeNumber" />
	<%request.setAttribute("employee", employee);
	request.setAttribute("yearMonth", yearMonth);%>
	<jsp:include page="common/employeeAssiduousnessMenu.jsp">
		<jsp:param name="month" value="<%=month.toString() %>" />
		<jsp:param name="year" value="<%=year.toString() %>" />
	</jsp:include>
		
	<!-- escrever mes ano -->
	
	<logic:empty name="employeeWorkSheet" property="workDaySheetList">
		<p class="mbottom05">
			<em><bean:message key="message.employee.noWorkSheet" bundle="ASSIDUOUSNESS_RESOURCES"/></em>
		</p>
	</logic:empty>
	<logic:notEmpty name="employeeWorkSheet" property="workDaySheetList">
		<fr:view name="employeeWorkSheet" property="workDaySheetList" schema="show.workDaySheet">
			<fr:layout name="tabular">
			    <fr:property name="classes" value="tstyle1 printborder tpadding1 mtop05"/>
				<fr:property name="columnClasses" value="bgcolor3 acenter,,acenter,aright,aright,aleft,aleft" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>
			
		<logic:notEmpty name="displayCurrentDayNote">
			<bean:message key="message.employee.currentDayIgnored" bundle="ASSIDUOUSNESS_RESOURCES"/>
		</logic:notEmpty>
		
		<logic:present name="employeeWorkSheet" property="totalBalance">
			<p class="mvert05"><bean:message key="label.totalBalance" bundle="ASSIDUOUSNESS_RESOURCES"/>: <b><bean:write name="employeeWorkSheet" property="totalBalanceString"/></b></p>
		</logic:present>
		<logic:present name="employeeWorkSheet" property="unjustifiedBalance">
			<p class="mvert05"><bean:message key="label.totalUnjustified" bundle="ASSIDUOUSNESS_RESOURCES"/>: <b><bean:write name="employeeWorkSheet" property="unjustifiedBalanceString"/></b></p>
		</logic:present>
<%--		<logic:present name="employeeWorkSheet" property="complementaryWeeklyRest">
			<p class="mvert05"><bean:message key="label.totalSaturday" />: <b><bean:write name="employeeWorkSheet" property="complementaryWeeklyRestString"/></b></p>
		</logic:present>
		<logic:present name="employeeWorkSheet" property="weeklyRest">
			<p class="mvert05"><bean:message key="label.totalSunday" />: <b><bean:write name="employeeWorkSheet" property="weeklyRestString"/></b></p>
		</logic:present>		 --%>
	</logic:notEmpty>
</logic:present>