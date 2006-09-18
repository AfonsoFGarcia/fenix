<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>
<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="label.parking" bundle="PARKING_RESOURCES" /></h2>


<logic:present name="parkingParty">
	<p class="mbottom0"><strong><bean:message
		key="label.person.title.personal.info" /></strong>:</p>

	<fr:view name="parkingParty" property="party" schema="viewPersonInfo">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
		</fr:layout>
	</fr:view>
	<logic:empty name="parkingParty" property="parkingRequests">
		<logic:equal name="parkingParty"
			property="hasAllNecessaryPersonalInfo" value="false">
			<p class="infoop2"><bean:message key="message.personalDataCondition"
				bundle="PARKING_RESOURCES" /></p>
			<p><bean:message key="message.no.necessaryPersonalInfo"
				bundle="PARKING_RESOURCES" /></p>
		</logic:equal>
		<logic:notEqual name="parkingParty"
			property="hasAllNecessaryPersonalInfo" value="false">
			<logic:equal name="parkingParty" property="acceptedRegulation"
				value="false">
				<p class="infoop2"><bean:message
					key="message.acceptRegulationCondition" bundle="PARKING_RESOURCES" /></p>
				<bean:message key="message.acceptRegulation"
					bundle="PARKING_RESOURCES" />
				<ul>
					<li><html:link page="/parking.do?method=acceptRegulation">
						<bean:message key="button.acceptRegulation"
							bundle="PARKING_RESOURCES" />
					</html:link></li>
				</ul>
				
			</logic:equal>
			<logic:notEqual name="parkingParty" property="acceptedRegulation"
				value="false">
				<bean:write name="parkingParty" property="parkingAcceptedRegulationMessage" filter="false"/>
				<ul>
					<li><html:link page="/parking.do?method=prepareEditParking">
						<bean:message key="label.editParkingDocuments"
							bundle="PARKING_RESOURCES" />
					</html:link></li>
				</ul>
			</logic:notEqual>
		</logic:notEqual>
	</logic:empty>
	<logic:notEmpty name="parkingParty" property="parkingRequests">
		<ul>
			<li><html:link page="/parking.do?method=prepareEditParking">
				<bean:message key="label.editParkingDocuments"
					bundle="PARKING_RESOURCES" />
			</html:link></li>
		</ul>


		<p class="mbottom0"><strong><bean:message key="label.driverLicense"
			bundle="PARKING_RESOURCES" /></strong>:</p>

		<fr:view name="parkingParty" property="firstRequest"
			schema="parkingRequest.driverLicense">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
			</fr:layout>
		</fr:view>

		<p class="mbottom0"><strong><bean:message key="label.firstCar"
			bundle="PARKING_RESOURCES" /></strong>:</p>
		<fr:view name="parkingParty" property="firstRequest"
			schema="parkingRequest.firstCar">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
			</fr:layout>
		</fr:view>

		<p class="mbottom0"><strong><bean:message key="label.secondCar"
			bundle="PARKING_RESOURCES" /></strong>:</p>
		<fr:view name="parkingParty" property="firstRequest"
			schema="parkingRequest.secondCar">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
