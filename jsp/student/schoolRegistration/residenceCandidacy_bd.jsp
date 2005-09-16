<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:form action="/enrollStudent?method=enrollStudent">
<html:hidden property="page" value="6"/>

<p><strong>P�gina 5 de 6</strong></p>

 <h2 style="text-align: center;"><bean:message key="schoolRegistration.Header.residenceTitle"/></h2>
	<p align="center"><span class="error"><html:errors property="residenceCandidate"/></span></p>	
	<p align="center"><span class="error"><html:errors property="dislocated"/></span></p>
	<p align="center"><span class="error"><html:errors property="observations"/></span></p>

<div style="width:70%; margin: 0 15%;">
<div class="infoop">
	<p>
	<bean:message key="label.schoolRegistration.residenceCandidate"/>
	<span style="padding-left: 1em;">Sim<html:radio property="residenceCandidate" value="true" /></span>
	<span>N�o<html:radio property="residenceCandidate" value="false" /></span>
	</p>

	<p>
	<bean:message key="label.schoolRegistration.observations"/>
	<html:textarea cols="60" rows="5" property="observations" onkeyup="document.schoolRegistrationForm.charCount.value=240-document.schoolRegistrationForm.observations.value.length;"/>
	</p>

	<p><bean:message key="label.person.remainingChars" bundle="DEFAULT"/>: <html:text property="charCount" size="4" maxlength="3" readonly="true" value="240" /></p>
</div>

<p><html:submit value="Continuar" styleClass="inputbutton"/></p>
</html:form>

</div>
