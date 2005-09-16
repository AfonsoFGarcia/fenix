<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/changePassword?method=start">
<html:hidden property="page" value="1"/>

	<div align="center" style="text-align: center;">
	<h4 class="registration"><bean:message key="label.registration.title" /></h4>
	<div class="infoop" style="text-align: justify; width: 70%; margin: 0 14%;">
		<p><strong>Leia com aten��o:</strong></p>
		<p><bean:message key="label.registration.info1" />:</p>
		<ul style="list-style: none;">
		<li><bean:message key="label.registration.info.step1" /></li>
		<li><bean:message key="label.registration.info.step2" /></li>
		<li><bean:message key="label.registration.info.step3" /></li>
		<li><bean:message key="label.registration.info.step4" /></li>
		<li><bean:message key="label.registration.info.step5" /></li>
		<li><bean:message key="label.registration.info.step6" /></li>
		</ul>
		<p><bean:message key="label.registration.info2" /></p>
		<p><bean:message key="label.registration.info3" /></p>
	</div>
	
	</div>


<br />
<p align="center"><html:submit value="Continuar" styleClass="inputbutton"/></p>
</html:form>