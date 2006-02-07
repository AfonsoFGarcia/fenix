<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<style>
.alignright {
text-align: right;
}
.valigntop {
vertical-align: top;
}
</style>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ManagerResources" var="bundle"/>

	<h:form>	
		<h:outputText value="<h2>#{bundle['title.send.mails']}</h2><br/>" escape="false"/>

		<h:panelGrid columns="2" styleClass="infoop" columnClasses="alignright,,"  rowClasses=",,,valigntop">
			<h:outputText value="From: " escape="false"/>
			<h:inputText id="from" value="#{SendMailBackingBean.from}"/>

			<h:outputText value="To: " escape="false"/>
			<h:inputText id="to" value="#{SendMailBackingBean.to}"/>

			<h:outputText value="Cc: " escape="false"/>
			<h:inputText id="ccs" value="#{SendMailBackingBean.ccs}"/>

			<h:outputText value="Bc: " escape="false"/>
			<h:inputText id="bccs" value="#{SendMailBackingBean.bccs}"/>

			<h:outputText value="Subject: " escape="false"/>
			<h:inputText id="subject" value="#{SendMailBackingBean.subject}"/>

		</h:panelGrid>

		<h:outputText value="<br/><br/>" escape="false"/>

		<h:outputText value="Message:<br/>" escape="false"/>
		<h:inputTextarea rows="10" cols="80" id="message" value="#{SendMailBackingBean.message}"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.teachers}"/>
		<h:outputText value="Send to all teachers." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.employees}"/>
		<h:outputText value="Send to all employees." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.degreeStudents}"/>
		<h:outputText value="Send to all degree students." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.masterDegreeStudents}"/>
		<h:outputText value="Send to all master degree students." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:commandButton action="#{SendMailBackingBean.send}"
				styleClass="inputbutton" value="#{bundle['button.send']}"/>

	</h:form>
</ft:tilesView>