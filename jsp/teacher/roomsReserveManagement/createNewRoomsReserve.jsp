<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="create.rooms.reserve.title" bundle="APPLICATION_RESOURCES"/></h2>

<logic:present role="TEACHER">

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>		

	<logic:notEmpty name="roomsReserveBean">
		<fr:edit id="roomsReserveWithDescriptions" name="roomsReserveBean" schema="CreateRoomsPunctualOccupationRequest"
			action="/roomsReserveManagement.do?method=createRoomsReserve" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 vamiddle thlight" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>		
			<fr:destination name="cancel" path="/roomsReserveManagement.do?method=viewReserves"/>						
		</fr:edit>
	</logic:notEmpty>
	
</logic:present>

