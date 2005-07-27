<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


	<%-- MANDATORY FIELDS --%>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.subType" />
		</td>
		<td>
			<html:select property="subtype">
				<html:options collection="subTypeList" property="subtype" />
			</html:select>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.title" />
		</td>
		<td>
			<html:text size="20" property="title"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.editor" />
		</td>
		<td>
			<html:text size="30" property="editor"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.numberPages" />
		</td>
		<td>
			<html:text size="20" property="numberPages"/>
		</td>
	</tr>
	
	<%-- NON-MANDATORY FIELDS --%>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.editorCity" />
		</td>
		<td>
			<html:text size="20" property="editorCity"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.country" />
		</td>
		<td>
			<html:text size="20" property="country"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.language" />
		</td>
		<td>
			<html:text size="20" property="language"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.format" />
		</td>
		<td>
			<html:select property="format">
				<html:options collection="formatList" property="format" />
			</html:select>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="message.publicationAttribute.scope" />
		</td>
		<td>
			<html:select property="scope">
				<logic:iterate id="scopes" name="scopeList" >
					<html:option value='<%=scopes.toString()%>'>
						<bean:write name="scopes" />
					</html:option>
				</logic:iterate>
			</html:select>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.edition" />
		</td>
		<td>
			<html:text size="20" property="edition"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.volume" />
		</td>
		<td>
			<html:text size="20" property="volume"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.fascicle" />
		</td>
		<td>
			<html:text size="20" property="fascicle"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.serie" />
		</td>
		<td>
			<html:text size="20" property="serie"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.isbn" />
		</td>
		<td>
			<html:text size="20" property="isbn"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.url" />
		</td>
		<td>
			<html:text size="70" property="url"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="message.publicationAttribute.observations" />
		</td>
		<td>
			<html:text size="20" property="observation"/>
		</td>
	</tr>

	<tr>
		<td>
			<bean:define id="valueYear" name="insertPublicationForm" property="infoPublicationTypeId"/>
			<logic:equal name="valueYear" value="3">
				<bean:message key="message.publicationAttribute.yearInith" />
			</logic:equal>
			<logic:notEqual name="valueYear" value="3">
				<bean:message key="message.publicationAttribute.year" />
			</logic:notEqual>
		</td>
		<td>
			<html:text size="9" property="year"/>
		</td>
	</tr>