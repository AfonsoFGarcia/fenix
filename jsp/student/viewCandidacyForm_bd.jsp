<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %> 

<logic:present name="equivalency" >
<bean:define id="equivalency" type="DataBeans.Seminaries.InfoEquivalency" scope="request" name="equivalency"/>

<h2><bean:message key="label.candidacyFormTitle"/></h2>	
<html:form action="submitCandidacyFirstInfo.do" method="post">
<table width="90%" align="center">
	<tr>
		<td width="0%" valign="top">
		<b><bean:message key="label.seminaries.motivation"/></b>
		</td>
		<td>
			<html:textarea cols="50" rows="10" property="motivation"/>
		</td>
	</tr>
	<tr>
	<logic:notEqual name="equivalency" property="modality.name" value="Completa">
		<td valign="top">
			<b><bean:message key="label.seminaries.theme"/></b>
		</td>
		<td>
			<html:select property="themeID">
					<html:optionsCollection name="equivalency" property="themes" label="name" value="idInternal"/>
			</html:select>
		</td>
	</logic:notEqual>
	</tr>
		<td>
		</td>
		<td>
			<html:hidden property="idInternal" name="equivalency"/>			
		</td>
	</tr>
	<tr>
		<td>
		</td>
		<td>
			<html:submit styleClass="button" value="Submeter" property="submition"/>			
		</td>
	</tr>
</table>


</html:form>



</logic:present>
<br/>
<br/>
<html:errors/>