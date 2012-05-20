<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<div id="foot_links">
	<logic:notEmpty name="site" property="sortedFooterLinks">
		<fr:view name="site" property="sortedFooterLinks">
			<fr:layout name="flowLayout">
				<fr:property name="eachLayout" value="values"/>
				<fr:property name="eachSchema" value="showFooterLink"/>
				<fr:property name="htmlSeparator" value="|"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</div>

<div id="foot_copy">
	<bean:message bundle="GLOBAL_RESOURCES" key="footer.copyright.label"/>
	<dt:format pattern="yyyy"><dt:currentTime/></dt:format>
	-
	<logic:present name="site">
		<bean:write name="site" property="unit.name"/>
	</logic:present>
	<logic:notPresent name="site">
		<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%>
	</logic:notPresent>
</div>

<div class="clear"></div>