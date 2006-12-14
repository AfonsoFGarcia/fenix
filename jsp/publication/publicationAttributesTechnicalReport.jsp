<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

	<%-- MANDATORY FIELDS --%>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.title" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.title" size="20" property="title"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.numberPages" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.numberPages" size="20" property="numberPages"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:define id="valueMonth" name="insertPublicationForm" property="infoPublicationTypeId"/>
			<logic:equal name="valueMonth" value="3">
				<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.monthInith" />
			</logic:equal>
			<logic:notEqual name="valueMonth" value="3">
				<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.month" />
			</logic:notEqual>	
		</td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" property="month">
				<logic:iterate id="month" name="monthList" >
					<html:option value='<%=month.toString()%>'>
						<bean:write name="month" />		
					</html:option>
				</logic:iterate>
			</html:select>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:define id="valueYear" name="insertPublicationForm" property="infoPublicationTypeId"/>
			<logic:equal name="valueYear" value="3">
				<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.yearInith" />
			</logic:equal>
			<logic:notEqual name="valueYear" value="3">
				<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.year" />
			</logic:notEqual>
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.year" size="9" property="year"/>
		</td>
	</tr>
	
	
	<%-- NON-MANDATORY FIELDS --%>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.language" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.language" size="20" property="language"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.format" />
		</td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" property="format">
				<html:options collection="formatList" property="format" />
			</html:select>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.instituition" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.instituition" size="60" property="instituition"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.number" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.number" size="10" property="number"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.serie" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.serie" size="20" property="serie"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.url" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.url" size="70" property="url"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.observations" />
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.observation" size="20" property="observation"/>
		</td>
	</tr>