<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<H2><bean:message bundle="SPACE_RESOURCES" key="link.create.space"/></H2>
<br/>

<bean:define id="selectedSpace" name="selectedSpaceInformation" property="space"/>
<bean:define id="suroundingSpaceID" type="java.lang.Integer" name="selectedSpace" property="idInternal"/>
<bean:define id="suroundingSpaceInformationID" type="java.lang.Integer" name="selectedSpace" property="spaceInformation.idInternal"/>

<html:form action="/manageSpaces">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showCreateSubSpaceForm"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.spaceInformationID" property="spaceInformationID" value="<%= suroundingSpaceInformationID.toString() %>"/>

	<html:select bundle="HTMLALT_RESOURCES" altKey="select.classname" property="classname" onchange="this.form.submit()">
		<html:option value=""/>
		<html:option bundle="SPACE_RESOURCES" key="select.item.building" value="net.sourceforge.fenixedu.domain.space.Building"/>
		<html:option bundle="SPACE_RESOURCES" key="select.item.floor" value="net.sourceforge.fenixedu.domain.space.Floor"/>
		<html:option bundle="SPACE_RESOURCES" key="select.item.room" value="net.sourceforge.fenixedu.domain.space.Room"/>
	</html:select>
</html:form>
<br/>

<logic:equal name="spaceContextForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Building">
	<fr:create type="net.sourceforge.fenixedu.domain.space.Building$BuildingFactoryCreator"
			schema="BuildingFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
		<fr:hidden slot="surroundingSpace" name="selectedSpace"/>
	</fr:create>
</logic:equal>
<logic:equal name="spaceContextForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Floor">
	<fr:create type="net.sourceforge.fenixedu.domain.space.Floor$FloorFactoryCreator"
			schema="FloorFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
		<fr:hidden slot="surroundingSpace" name="selectedSpace"/>
	</fr:create>
</logic:equal>
<logic:equal name="spaceContextForm" property="classname" value="net.sourceforge.fenixedu.domain.space.Room">
	<fr:create type="net.sourceforge.fenixedu.domain.space.Room$RoomFactoryCreator"
			schema="RoomFactoryCreator"
			action="/manageSpaces.do?method=executeFactoryMethod">
		<fr:hidden slot="surroundingSpace" name="selectedSpace"/>
	</fr:create>
</logic:equal>
