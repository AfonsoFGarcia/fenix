<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="candidate.titleMain" /></title>
  </head>
  <body>
    <html:link page="/visualizeApplicationInfo.do">Visualizar a Informa��o de Candidatura</html:link><br/>
    <html:link page="/changeApplicationInfo.do">Alterar a Informa��o de Candidatura</html:link><br/>
    <html:link page="/logoff.do">LogOff</html:link><br/>
    
  </body>
</html>