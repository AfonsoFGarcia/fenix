<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.publications.Management"/></h2>
<html:form action="/prepareSearchPerson">
<logic:notPresent name="publicationTypesList">
	N�o existem os tipos
</logic:notPresent>
<logic:present name="publicationTypesList">
<br/>
<h3>

<bean:message key="message.publications.insertPublication" />

</h3>
<p class="infoop"><span class="emphasis-box">1</span>
	<bean:message key="message.publications.choosePublicationType" />
</p>
	<span class="error">
		<html:errors/>
	</span>
	<br />
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal"/>
	<html:hidden property="teacherId"/>
	<html:hidden property="typePublication"/>
	<html:hidden property="method" value="prepareSearchPerson"/>

<div class="infoop"><bean:message key="message.publications.insertPublication.Unstructured"/></div>

<table>
	<tr>
		<td><bean:message key="message.publications.publicationType"/> &nbsp;</td>
		<td></td>
		<td>
			<html:select property="infoPublicationTypeId">
				<html:options collection="publicationTypesList" property="idInternal" labelProperty="publicationType"/>
			</html:select>
		</td>
	</tr>
	<tr>
		&nbsp;
	</tr>
	<tr>
		<td>
			<html:button property="idInternal" value="Escolher" styleClass="inputbutton" onclick="this.form.submit()"/>
		</td>
	</tr>
</table>
<br/> 
</logic:present>
</html:form>