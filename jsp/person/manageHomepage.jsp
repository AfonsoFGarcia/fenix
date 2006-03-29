<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="title.manage.homepage" bundle="HOMEPAGE_RESOURCES"/></h2>

<br/>

<bean:message key="message.homepage.info" bundle="HOMEPAGE_RESOURCES"/>

<br/>
<br/>

<html:form action="/manageHomepage">
	<html:hidden property="method" value="submitHomepage"/>

	<bean:message key="label.homepage.activated" bundle="HOMEPAGE_RESOURCES"/>
	<html:radio property="activated" value="true" onchange="this.form.submit()"/><bean:message key="label.homepage.activated.yes" bundle="HOMEPAGE_RESOURCES"/>
	<html:radio property="activated" value="false" onchange="this.form.submit()"/><bean:message key="label.homepage.activated.no" bundle="HOMEPAGE_RESOURCES"/>

	<br/>

	<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
	<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>

	<bean:define id="homepageURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/homepage/<bean:write name="UserView" property="person.user.userUId"/></bean:define>
	<bean:message key="person.homepage.url" bundle="HOMEPAGE_RESOURCES"/>

	<logic:notPresent name="UserView" property="person.homepage">
		<bean:write name="homepageURL"/>
	</logic:notPresent>

	<logic:present name="UserView" property="person.homepage">
		<logic:notPresent name="UserView" property="person.homepage.activated">
				<bean:write name="homepageURL"/>
		</logic:notPresent>
		<logic:present name="UserView" property="person.homepage.activated">
			<logic:equal name="UserView" property="person.homepage.activated" value="true">
				<html:link href="<%= homepageURL %>"><bean:write name="homepageURL"/></html:link>
			</logic:equal>
			<logic:equal name="UserView" property="person.homepage.activated" value="false">
				<bean:write name="homepageURL"/>
			</logic:equal>
		</logic:present>
	</logic:present>

	<br/>
	<br/>

	<bean:message key="label.homepage.name" bundle="HOMEPAGE_RESOURCES"/>
	<html:text property="name"/>
	<br/>
	<bean:message key="label.homepage.showPhoto" bundle="HOMEPAGE_RESOURCES"/>
	<html:checkbox property="showPhoto" value="true"/>
	<br/>
	<bean:message key="label.homepage.showUnit" bundle="HOMEPAGE_RESOURCES"/>
	<html:checkbox property="showUnit" value="true"/>
	<logic:present name="UserView" property="person.employee.currentContract.workingUnit">
		<bean:write name="UserView" property="person.employee.currentContract.workingUnit.name"/>
	</logic:present>
	<logic:iterate id="student" name="UserView" property="person.students">
		<logic:present name="student" property="activeStudentCurricularPlan">
			<bean:write name="student" property="activeStudentCurricularPlan.degreeCurricularPlan.degree.presentationName"/>
		</logic:present>
	</logic:iterate>
	<br/>
	<logic:present name="UserView" property="person.teacher">
		<logic:present name="UserView" property="person.employee.currentContract">
			<bean:message key="label.homepage.showCategory" bundle="HOMEPAGE_RESOURCES"/>
			<html:checkbox property="showCategory" value="true"/>
			<logic:present name="UserView" property="person.teacher">
				<bean:write name="UserView" property="person.teacher.category.longName"/>
			</logic:present>
			<br/>
		</logic:present>
	</logic:present>
	<bean:message key="label.homepage.showEmail" bundle="HOMEPAGE_RESOURCES"/>
	<html:checkbox property="showEmail" value="true"/>
	<bean:write name="UserView" property="person.email"/>
	<br/>
	<bean:message key="label.homepage.showTelephone" bundle="HOMEPAGE_RESOURCES"/>
	<html:checkbox property="showTelephone" value="true"/>
	<bean:write name="UserView" property="person.phone"/>
	<br/>
	<bean:message key="label.homepage.showWorkTelephone" bundle="HOMEPAGE_RESOURCES"/>
	<html:checkbox property="showWorkTelephone" value="true"/>
	<bean:write name="UserView" property="person.workPhone"/>
	<br/>
	<bean:message key="label.homepage.showMobileTelephone" bundle="HOMEPAGE_RESOURCES"/>
	<html:checkbox property="showMobileTelephone" value="true"/>
	<bean:write name="UserView" property="person.mobile"/>
	<br/>
	<bean:message key="label.homepage.showAlternativeHomepage" bundle="HOMEPAGE_RESOURCES"/>
	<html:checkbox property="showAlternativeHomepage" value="true"/>
	<bean:write name="UserView" property="person.webAddress"/>
	<br/>
	<logic:present name="UserView" property="person.teacher">
		<logic:present name="UserView" property="person.employee.currentContract">
			<bean:message key="label.homepage.showCurrentExecutionCourses" bundle="HOMEPAGE_RESOURCES"/>
			<html:checkbox property="showCurrentExecutionCourses" value="true"/>
			<logic:iterate id="executionCourse" name="UserView" property="person.homepage.currentExecutionCourses" length="1">
				<bean:write name="executionCourse" property="nome"/>
			</logic:iterate>
			<logic:iterate id="executionCourse" name="UserView" property="person.homepage.currentExecutionCourses" offset="1">
				, <bean:write name="executionCourse" property="nome"/>
			</logic:iterate>
			<br/>
		</logic:present>
	</logic:present>
<!--
	<bean:message key="label.homepage.showResearchUnitHomepage" bundle="HOMEPAGE_RESOURCES"/>
	<html:checkbox property="showResearchUnitHomepage" value="true"/>
	<br/>
-->

	<html:submit><bean:message key="person.homepage.submit" bundle="HOMEPAGE_RESOURCES"/></html:submit>
</html:form>

<!--
<br/>
<br/>

<logic:notPresent name="UserView" property="person.homepage">
	<fr:create type="net.sourceforge.fenixedu.domain.homepage.Homepage" schema="person.homepage.manage">
		<fr:layout name="tabular">
			<fr:hidden slot="person" name="UserView" property="person"/>
			<fr:property name="classes" value="style1" />
			<fr:property name="columnClasses" value="listClasses" />
		</fr:layout>
	</fr:create>
</logic:notPresent>

<logic:present name="UserView" property="person.homepage">
	<fr:edit name="UserView" property="person.homepage"
			type="net.sourceforge.fenixedu.domain.homepage.Homepage"
			schema="person.homepage.manage">
		<fr:layout name="tabular">
			<fr:property name="classes" value="style1" />
			<fr:property name="columnClasses" value="listClasses" />
		</fr:layout>
	</fr:edit>
</logic:present>
-->