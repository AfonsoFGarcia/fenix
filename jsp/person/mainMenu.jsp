<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="label.person.main.title" /></title>
  </head>
  <body>
    <html:link page="/visualizePersonalInfo.do"><bean:message key="label.person.visualizeInformation" /></html:link><br/>
    <html:link page="/changePersonalInfoDispatchAction.do?method=prepare"><bean:message key="label.person.changeInformation" /></html:link><br/>
    <html:link page="/changePasswordForward.do"><bean:message key="label.person.changePassword" /></html:link><br/>
   <html:link forward="logoff">
	<bean:message key="link.logout"/>
</html:link><br/>
  </body>
</html>