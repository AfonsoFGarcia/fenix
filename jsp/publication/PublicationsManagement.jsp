<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<h2><bean:message key="title.publications.Management"/></h2>
<logic:present name="infoSitePublications"> 
		<logic:messagesPresent>
		<span class="error">
			<html:errors/>
		</span>
		</logic:messagesPresent>
		
		<html:messages id="msg" message="true">
			<br/>
			<span class="sucessfulOperarion"><bean:write name="msg"/></span><br>
			<br/>
		</html:messages>

		
		<table class="listClasses" width="100%">
			<tr>
				<td align="left">
					<b><bean:message key="message.publications.warning" /></b>
				</td>
			</tr>
		</table>
		</br>
		<table class="infoselected" width="100%">
			<tr>
				<td width="70%"><b><bean:message key="message.teacherInformation.name" /></b>
					&nbsp;<bean:write name="infoSitePublications" property="infoTeacher.infoPerson.nome" /> </td> 
				<td width="30%"><b><bean:message key="message.teacherInformation.birthDate" /></b>
					&nbsp;<bean:write name="infoSitePublications" property="infoTeacher.infoPerson.nascimento" /> </td>	
			</tr>
			<tr>
				<td><b><bean:message key="message.teacherInformation.category" /></b>
					&nbsp;<bean:write name="infoSitePublications" property="infoTeacher.infoCategory.shortName" /></td>
			</tr>
		</table>
		<br> </br>
		<table class="listClasses" width="100%">
			<tr>
				<td>
					<p align="left"><bean:message key="message.publications.management.warning" /></p>
				</td>
			</tr>
		</table>


		<br />
		<bean:message key="message.publications.managementEditPublication"/>
		<bean:message key="message.publications.managementInsertPublication"/>
		
		<br/>
		<table style="text-align:left" width="100%">	
			<logic:iterate id="infoPublication" name="infoSitePublications" property="infoPublications">
				<tr>
					<td class="listClasses" style="text-align:left">
						<bean:write name="infoPublication" property="publicationString" />
					</td>
					<td class="listClasses" style="text-align:left">
						<bean:define id="publicationTypeId" name="infoPublication" property="keyPublicationType"/>
						<%-- Tratar isto como uma qualquer publicação e não como sendo especificamente didatica --%>
						<html:link page='<%= "/publicationDidatic.do?method=prepareEdit&amp;typePublication=Didatic&amp;page=0&amp;infoPublicationTypeId=" + publicationTypeId %>'
							paramId="idInternal"
							paramName="infoPublication" 
							paramProperty="idInternal">
							<bean:message key="label.edit" />
						</html:link>
					</td>
					<td class="listClasses" style="text-align:left">
						<bean:define id="publicationTypeId" name="infoPublication" property="keyPublicationType"/>
						<html:link page="/prepareDeletePublication.do"
							paramId="idInternal"
							paramName="infoPublication" 
							paramProperty="idInternal">
							<bean:message key="label.delete" />
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
		<br />
		<div class="gen-button">
				<html:link page="/readPublicationTypes.do?method=prepareEdit&amp;typePublication=Didatic&amp;page=0"
					paramId="infoTeacher#idInternal" 
					paramName="infoSitePublications" 
					paramProperty="infoTeacher.idInternal">
					<bean:message key="message.publications.insert" />
				</html:link>
			</div>
		<br />
</logic:present>