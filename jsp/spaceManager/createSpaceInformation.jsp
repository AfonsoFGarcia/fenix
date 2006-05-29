<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="SpaceFactoryEditor">
	<bean:define id="space" name="SpaceFactoryEditor" property="space" toScope="request"/>
	<jsp:include page="spaceCrumbs.jsp"/>
	<br/>
	<br/>

	<bean:message bundle="SPACE_RESOURCES" key="link.edit.space"/>
	<br/>
	<br/>
	<br/>

	<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
		<fr:edit name="SpaceFactoryEditor" schema="CampusFactoryEditor"
				action="/manageSpaces.do?method=executeFactoryMethod"/>
	</logic:equal>
	<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Building">
		<fr:edit name="SpaceFactoryEditor" schema="BuildingFactoryEditor"
				action="/manageSpaces.do?method=executeFactoryMethod"/>
	</logic:equal>
	<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
		<fr:edit name="SpaceFactoryEditor" schema="FloorFactoryEditor"
				action="/manageSpaces.do?method=executeFactoryMethod"/>
	</logic:equal>
	<logic:equal name="space" property="class.name" value="net.sourceforge.fenixedu.domain.space.Room">
		<fr:edit name="SpaceFactoryEditor" schema="RoomFactoryEditor"
				action="/manageSpaces.do?method=executeFactoryMethod"/>
	</logic:equal>
	<br/>
	<br/>
</logic:present>
