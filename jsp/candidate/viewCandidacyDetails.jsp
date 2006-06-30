<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.detail" /></h2></span></p>
<fr:view name="candidacy" schema="candidacy.short" >
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.activeSituation" /></h2></span></p>
<fr:view name="candidacy" property="activeCandidacySituation" schema="candidacySituation.full" >
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<!-- 
<fr:form action="/viewCandidacies.do?method=uploadDocuments">
	<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.documents" /></h2></span></p>
	<p>Por favor...</p>
	<fr:edit id="candidacyDocuments" name="candidacyDocuments" schema="candidacyDocuments.full" layout="tabular-editable"/>
	<html:submit>up!</html:submit>
</fr:form>
 -->
	