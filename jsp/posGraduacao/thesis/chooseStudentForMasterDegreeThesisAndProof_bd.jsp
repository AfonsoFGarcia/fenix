<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.thesis.title"/></h2>
<center>
<span class="error"><html:errors/></span>

<br/>
<br/>
<html:form action="/prepareStudentForMasterDegreeThesisAndProof.do">
	<html:hidden property="method" value="getStudentAndDegreeTypeForThesisOperations"/>
	<html:hidden property="degreeType" value="2"/>
	<html:hidden property="page" value="1"/>
	<table border="0">
		<tr>
			<td align="left"><bean:message key="label.choose.student"/>&nbsp;</td>
			<td align="left">
				<input type="text" name="studentNumber" size="5" value=""/>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.submit.degree.type.and.student"/>
	</html:submit>
</html:form>

</center>