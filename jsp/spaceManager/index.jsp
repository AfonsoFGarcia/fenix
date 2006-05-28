<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/>
<br/>
<br/>

<html:link page="/manageSpaces.do?method=showCreateSpaceForm&page=0">
	<bean:message bundle="SPACE_RESOURCES" key="link.create.space"/>
</html:link>
<br/>
<br/>

<logic:present name="selectedSpace">
	<bean:write name="selectedSpace" property="idInternal"/>
	<br/>
	<br/>
</logic:present>

<logic:present name="spaces">
	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message bundle="SPACE_RESOURCES" key="title.space.type"/>
			</td>
			<td class="listClasses-header">
				<bean:message bundle="SPACE_RESOURCES" key="title.space.Space"/>
			</td>
			<td class="listClasses-header">
				<bean:message bundle="SPACE_RESOURCES" key="title.space.number.subspaces"/>
			</td>
			<td class="listClasses-header">
			</td>
		</tr>
		<logic:iterate id="space" name="spaces">
			<logic:notPresent name="space" property="suroundingSpace">
				<tr>
					<td class="listClasses">
						<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
							<bean:message bundle="SPACE_RESOURCES" key="select.item.campus"/>
						</logic:equal>
						<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Building">
							<bean:message bundle="SPACE_RESOURCES" key="select.item.building"/>
						</logic:equal>
					</td>
					<td class="listClasses">
						<html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceInformationID" paramName="space" paramProperty="spaceInformation.idInternal">
							<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
								<bean:write name="space" property="spaceInformation.name"/>
							</logic:equal>
							<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Building">
								<bean:write name="space" property="spaceInformation.name"/>
							</logic:equal>
						</html:link>
					</td>
					<td class="listClasses">
						<bean:write name="space" property="containedSpacesCount"/>
					</td>
					<td class="listClasses">
						<html:link page="/manageSpaces.do?method=deleteSpace&page=0" paramId="spaceID" paramName="space" paramProperty="idInternal">
							<bean:message bundle="SPACE_RESOURCES" key="link.delete.space"/>
						</html:link>
					</td>
				</tr>
			</logic:notPresent>
		</logic:iterate>
	</table>
</logic:present>