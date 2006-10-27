<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.workSheet" /></h2>

<div class="warning0"><bean:message key="message.employee.testPhase" />
</div>

<logic:present name="employeeWorkSheet">
	<logic:present name="yearMonth">
		<bean:define id="month" name="yearMonth" property="month" />
		<bean:define id="year" name="yearMonth" property="year" />
		<bean:define id="employeeNumber" name="employeeWorkSheet"
			property="employee.employeeNumber" />
		<p><bean:message key="label.show"/>: <html:link
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
		</html:link></p>

		<span class="toprint"><br />
		</span>
		<fr:view name="employeeWorkSheet" property="employee"
			schema="show.employeeInformation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="showinfo1 thbold" />
			</fr:layout>
		</fr:view>

		<logic:messagesPresent message="true">
			<html:messages id="message" message="true">
				<p><span class="error0"><bean:write name="message" /></span></p>
			</html:messages>
		</logic:messagesPresent>


		<div class="mvert1 invisible"><fr:form
			action="/viewEmployeeAssiduousness.do">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method"
				name="employeeForm" property="method" value="showWorkSheet" />
			<html:hidden bundle="HTMLALT_RESOURCES"
				altKey="hidden.employeeNumber" name="employeeForm"
				property="employeeNumber" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page"
				name="employeeForm" property="page" value="0" />
			<fr:edit name="yearMonth" schema="choose.date">
				<fr:layout>
					<fr:property name="classes" value="thlight thright" />
				</fr:layout>
			</fr:edit>
			<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
				styleClass="invisible">
				<bean:message key="button.submit" />
			</html:submit></p>
		</fr:form></div>

		<div class="toprint">
		<p class="bold mbottom0"><bean:define id="month" name="yearMonth"
			property="month" /> <bean:message key="<%=month.toString()%>"
			bundle="ENUMERATION_RESOURCES" /> <bean:write name="yearMonth"
			property="year" /></p>
		<br />
		</div>
	</logic:present>

	<logic:empty name="employeeWorkSheet" property="workDaySheetList">
		<bean:message key="message.employee.noWorkSheet" />
	</logic:empty>
	<logic:notEmpty name="employeeWorkSheet" property="workDaySheetList">
		<fr:view name="employeeWorkSheet" property="workDaySheetList"
			schema="show.workDaySheet">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder tpadding1" />
				<fr:property name="columnClasses"
					value="bgcolor3 acenter,,acenter,aright,aright,aleft,aleft" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>

		<logic:notEmpty name="displayCurrentDayNote">
			<bean:message key="message.employee.currentDayIgnored" />
		</logic:notEmpty>

		<logic:present name="employeeWorkSheet" property="totalBalance">
			<p class="mvert05"><bean:message key="label.totalBalance" />: <b><bean:write
				name="employeeWorkSheet" property="totalBalanceString" /></b></p>
		</logic:present>
		<logic:present name="employeeWorkSheet" property="unjustifiedBalance">
			<p class="mvert05"><bean:message key="label.totalUnjustified" />: <b><bean:write
				name="employeeWorkSheet" property="unjustifiedBalanceString" /></b></p>
		</logic:present>
		<%--		<logic:present name="employeeWorkSheet" property="complementaryWeeklyRest">
			<p class="mvert05"><bean:message key="label.totalComplementaryWeeklyRest" />: <b><bean:write name="employeeWorkSheet" property="complementaryWeeklyRestString"/></b></p>
		</logic:present>
		<logic:present name="employeeWorkSheet" property="weeklyRest">
			<p class="mvert05"><bean:message key="label.totalWeeklyRest" />: <b><bean:write name="employeeWorkSheet" property="weeklyRestString"/></b></p>
		</logic:present> --%>
	</logic:notEmpty>

</logic:present>

<logic:notPresent name="employeeWorkSheet">
	<bean:message key="message.employee.noWorkSheet" />
</logic:notPresent>
