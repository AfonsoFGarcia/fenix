<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.create"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.convokes"/></h2>

<p class="mvert15 breadcumbs"><span><bean:message key="label.vigilancy.firstStep" bundle="VIGILANCY_RESOURCES"/></span> > <span class="actual"><bean:message key="label.vigilancy.secondStep" bundle="VIGILANCY_RESOURCES"/></span></p>

<fr:form id="back" action="/vigilancy/convokeManagement.do?method=revertConvokes">
	<fr:edit id="convoke" name="bean" visible="false"/>
	<ul>
		<li><a href="javascript:document.getElementById('back').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.back"/></a></li>
	</ul>
</fr:form>

<fr:view name="bean" schema="confirmConvokes">
	<fr:layout>
		<fr:property name="classes" value="tstyle1 thlight thright thtop ulnomargin"/>
		<fr:property name="columnClasses" value="inobullet"/>
	</fr:layout>
</fr:view>

<fr:form action="/vigilancy/convokeManagement.do?method=createConvokes">
	<fr:edit name="bean" schema="showEmail" id="confirmConvokes" action="/vigilancy/convokeManagement.do?method=createConvokes">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright"/>
		</fr:layout>
	</fr:edit>
	<p><html:submit><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit></p>
</fr:form>





