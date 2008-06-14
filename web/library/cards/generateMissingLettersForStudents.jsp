<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.card.generateMissingLetters.studentsAndGrantOwner"/>
</h2>

<p class="infoop2">
	<bean:message key="text.title.library.card.generateMissingLetters.studentsAndGrantOwner" bundle="LIBRARY_RESOURCES"/>
</p>

<logic:notPresent name="nothingMissing">
	<p>
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link page="/cardManagement.do?method=generateMissingLetters&amp;students=yes">
			<bean:message key="link.card.generateMissingLetters.studentsAndGrantOwner" bundle="LIBRARY_RESOURCES"/>
		</html:link>
	</p>
</logic:notPresent>

<logic:present name="nothingMissing">
	<p><em><bean:message key="message.card.noMissingLetters" bundle="LIBRARY_RESOURCES"/></em></p>
</logic:present>