<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<span class="error"><html:errors/></span>
<html:messages id="message" message="true" bundle="RESEARCHER_RESOURCES">
	<span class="error">
		<bean:write name="message"/>
	</span>
</html:messages>

<logic:present role="RESEARCHER">
	<bean:define id="patentId" name="patent" property="idInternal" />
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.editPatentUseCase.title"/></h2>

	<%-- Participations --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></h3>	
	<fr:view name="patent" property="resultParticipations" schema="result.participations" layout="tabular">
		<fr:layout><fr:property name="sortBy" value="personOrder"/></fr:layout>
	</fr:view>
	
	<html:link page="<%="/result/resultParticipationManagement.do?method=prepareEditParticipation&resultId=" + patentId %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.editParticipations" />
	</html:link>
	<br/>	
 	<br/>

	<%-- Data --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/></h3>
	<fr:view name="patent" schema="patent.viewEditData">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:view>
	<html:link page="<%="/result/patents/patentsManagement.do?method=prepareEditPatentData&patentId=" + patentId %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.editResultData" />
	</html:link>
	
	<br/>	
	<br/>
	
	<html:link page="<%= "/result/patents/patentsManagement.do?method=listPatents" %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
	</html:link>
	
	
</logic:present>
<br/>