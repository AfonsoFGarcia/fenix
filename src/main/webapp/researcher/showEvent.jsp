<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.event"/></h2>
 
<fr:view name="event" layout="tabular-nonNullValues" schema="presentEvent">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thright thlight thtop"/>
		<fr:property name="rowClasses" value="tdbold,,,,"/>
	</fr:layout>
</fr:view>

<logic:notEmpty name="event" property="articles">
	<p class="mbottom05"><bean:message key="label.conferenceArticleList" bundle="RESEARCHER_RESOURCES"/>:</p>
	<fr:view name="event" property="articles" schema="presentArticlesInEvent">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thtop mtop05"/>
			<fr:property name="rowClasses" value=",bgcolorfafafa"/>
			<fr:property name="columnClasses" value="acenter,acenter,,acenter,acenter,acenter,acenter"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="event" property="articles">
	<bean:message key="label.noArticlesAssociated" bundle="RESEARCHER_RESOURCES"/>
</logic:empty>