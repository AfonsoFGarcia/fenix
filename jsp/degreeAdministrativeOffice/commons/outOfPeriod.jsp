<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:errors/>
<br/>
<br/>
<b><bean:message key="message.enrolment.wish.to.continue"/></b>
<br/>
<br/>
<html:form action="/curricularCourseEnrolmentWithRulesManager.do">
	<html:hidden property="method" value="outOfPeriod"/>
	<html:submit styleClass="inputbutton"><bean:message key="button.yes"/></html:submit>
	<html:cancel styleClass="inputbutton"><bean:message key="button.no"/></html:cancel>
</html:form>
