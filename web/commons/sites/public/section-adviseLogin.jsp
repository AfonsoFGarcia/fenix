<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<logic:present name="section">
    <bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>

    <h2>
        <fr:view name="section" property="name" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/>
        <span class="permalink1">(<app:contentLink name="section"><bean:message key="label.link" bundle="SITE_RESOURCES"/></app:contentLink>)</span>
    </h2>

    <bean:define id="sectionId" name="section" property="idInternal"/>
    <p>
       <em><bean:message key="message.section.view.mustLogin" bundle="SITE_RESOURCES"/></em>
       <html:link page="<%= String.format("%s?method=sectionWithLogin&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
            <bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
       </html:link>.
    </p>
    <bean:message key="label.permittedGroup" bundle="SITE_RESOURCES"/>

	<logic:present name="section" property="availabilityPolicy">
		<logic:present name="section" property="availabilityPolicy.targetGroup">
			<logic:present name="section" property="availabilityPolicy.targetGroup.name">
				<fr:view name="section" property="availabilityPolicy.targetGroup.name">
				</fr:view>
			</logic:present>
		</logic:present>
	</logic:present>
	<logic:notPresent name="section" property="availabilityPolicy">
		<bean:message key="link.section.no.availability.policy" bundle="SITE_RESOURCES"/>
	</logic:notPresent>

</logic:present>
