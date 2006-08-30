<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<style>
		h3.cd_heading {
		font-weight: normal;
		margin-top: 3em;
		border-top: 1px solid #e5e5e5;
		background-color: #fafafa;
		padding: 0.25em 0 0em 0.25em;
		padding: 0.5em 0.25em;
		}
		h3.cd_heading span {
		margin-top: 2em;
		border-bottom: 2px solid #fda;
		}
</style>
		
<logic:present role="RESEARCHER">
	<h2/><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.management.title"/></h2>
		
	<html:link module="/researcher" page="/resultPatents/prepareCreate.do">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.create.link" />
	</html:link>
	
	<%-- Action messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<%-- Result Patents Listing --%>
	<h3 class='cd_heading'><span><bean:message key="researcher.ResultPatent.list.label" bundle="RESEARCHER_RESOURCES"/></span></h3>
	<logic:empty name="resultPatents">
		<p><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.emptyList"/></p>
	</logic:empty>
	
	<logic:notEmpty name="resultPatents">
		<fr:view name="resultPatents" layout="tabular" schema="result.patentShortList" >
			<fr:layout>
				<fr:property name="classes" value="tstyle4"/>
				<fr:property name="columnClasses" value=",,,acenter"/>
				<fr:property name="sortBy" value="lastModificationDate=desc"/>
				
				<fr:property name="link(edit)" value="/resultPatents/prepareEdit.do"/>
				<fr:property name="param(edit)" value="idInternal/resultId"/>
				<fr:property name="key(edit)" value="link.edit"/>
				<fr:property name="bundle(edit)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(edit)" value="1"/>
	
				<fr:property name="link(delete)" value="/resultPatents/prepareDelete.do"/>
				<fr:property name="param(delete)" value="idInternal/resultId"/>
				<fr:property name="key(delete)" value="link.delete"/>
				<fr:property name="bundle(delete)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(delete)" value="2"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
<br/>
