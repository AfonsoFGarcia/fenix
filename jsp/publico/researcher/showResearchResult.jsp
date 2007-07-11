<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

	<logic:notPresent name="hideResultPageTitle">
		<bean:define id="resultType" name="result" property="class.simpleName"/>
		<logic:notEqual name="resultType" value="ResearchResultPatent">
		<h1><bean:message key="label.publication" bundle="RESEARCHER_RESOURCES"/></h1>
		</logic:notEqual>
		<logic:equal name="resultType" value="ResearchResultPatent">
			<h1><bean:message key="label.patent" bundle="RESEARCHER_RESOURCES"/></h1>
		</logic:equal>
	</logic:notPresent>
	
	<h2><fr:view name="result" property="title"/></h2>
	
	<logic:notEqual name="resultType" value="ResearchResultPatent">
	<bean:define id="schema" name="result" property="schema"/>				
	<fr:view name="result" schema="<%= schema + ".mainInfo" %>">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
		<fr:property name="columnClasses" value="width10em, width50em"/>
	</fr:layout>
		<fr:destination name="view.prize" path="/prizes/showPrizes.do?method=showPrize&oid=${idInternal}"/>
	</fr:view>
	</logic:notEqual>

	<logic:equal name="resultType" value="ResearchResultPatent">
	<fr:view name="result" schema="patent.viewEditData">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
		<fr:property name="columnClasses" value="width10em, width50em"/>
	</fr:layout>
		<fr:destination name="view.prize" path="/prizes/showPrizes.do?method=showPrize&oid=${idInternal}"/>
	</fr:view>
	</logic:equal>

	
	<logic:notPresent name="hideResultFiles">
		<%-- Documents --%>
		
		<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></b>
		</p>
		<jsp:include page="/researcher/result/commons/viewDocumentFiles.jsp"/>
	</logic:notPresent>
