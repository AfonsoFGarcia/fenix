<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="thesisId" name="thesis" property="idInternal"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.confirm"/></h2>

<ul>
    <li>
        <html:link page="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s", dcpId) %>">
            <bean:message key="title.coordinator.thesis.back"/>
        </html:link>
    </li>
</ul>


<%-- Dissertation --%>
<h3 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.confirm.details"/></h3>

<fr:view name="thesis" schema="thesis.jury.proposal.information">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
		<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>


<%-- Resumo --%>
<h3 class="mtop15 mbottom05"><bean:message key="label.thesis.abstract"/></h3>

<logic:notEqual name="thesis" property="thesisAbstractInBothLanguages" value="true">
    <p><em><bean:message key="label.thesis.abstract.empty"/></em></p>
</logic:notEqual>

<logic:equal name="thesis" property="thesisAbstractInBothLanguages" value="true">
    <p>
        <fr:view name="thesis" property="thesisAbstract">
            <fr:layout>
                <fr:property name="language" value="pt"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </p>
    
    <p>
        <fr:view name="thesis" property="thesisAbstract">
            <fr:layout>
                <fr:property name="language" value="en"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </p>
</logic:equal>


<%-- Palavras-Chave --%>
<h3 class="mtop15 mbottom05"><bean:message key="label.thesis.keywords"/></h3>

<logic:notEqual name="thesis" property="keywordsInBothLanguages" value="true">
    <p><em><bean:message key="label.thesis.keywords.empty"/></em></p>
</logic:notEqual>

<logic:equal name="thesis" property="keywordsInBothLanguages" value="true">
    <p>
        <fr:view name="thesis" property="keywords">
            <fr:layout>
                <fr:property name="language" value="pt"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </p>
    
    <p>
        <fr:view name="thesis" property="keywords">
            <fr:layout>
                <fr:property name="language" value="en"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </p>
</logic:equal>


<%-- Resumo Extendido --%>
<h3 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.confirm.extendedAbstract"/></h3>

<logic:empty name="thesis" property="extendedAbstract">
	<p>
		<em><bean:message key="label.coordinator.thesis.confirm.noExtendedAbstract"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="thesis" property="extendedAbstract">
	<fr:view name="thesis" property="extendedAbstract" layout="values" schema="coordinator.thesis.file"/>
	(<fr:view name="thesis" property="extendedAbstract.size" layout="fileSize"/>)
</logic:notEmpty>


<%-- Resumo Extendido II --%>
<h3 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.confirm.dissertation"/></h3>

<logic:empty name="thesis" property="dissertation">
	<p><em><bean:message key="label.coordinator.thesis.confirm.noDissertation"/></em></p>
</logic:empty>

<logic:notEmpty name="thesis" property="dissertation">
	<fr:view name="thesis" property="dissertation" layout="values" schema="coordinator.thesis.file"/>
	(<fr:view name="thesis" property="dissertation.size" layout="fileSize"/>)
</logic:notEmpty>


<%-- Confirma��o --%>
<h3 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.confirm.section"/></h3>

<p>
    <em><bean:message key="label.coordinator.thesis.confirm.message"/></em>
</p>

<logic:messagesPresent message="true">
    <html:messages id="message" message="true">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<fr:edit name="thesis" schema="coordinator.thesis.evaluate"
         action="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s", dcpId) %>">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05 mbottom05"/>
		<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
    </fr:layout>
    
    <fr:destination name="cancel" path="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s", dcpId) %>"/>
</fr:edit>



<%-- Jury --%>
<h3 class="mtop2 separator2"><bean:message key="title.coordinator.thesis.edit.section.jury"/></h3>


<%-- Orientator --%>
<h4 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.orientation.orientator"/></h4>

<logic:empty name="thesis" property="orientator">
    <p>
        <em><bean:message key="title.coordinator.thesis.edit.orientator.empty"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="orientator">
    <fr:view name="thesis" property="orientator" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
			<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>


<%-- Coorientator --%>
<h4 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.orientation.coorientator"/></h4>

<logic:empty name="thesis" property="coorientator">
    <p>
        <em><bean:message key="title.coordinator.thesis.edit.coorientator.empty"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="coorientator">
    <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
			<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>


<%-- Jury/President --%>
<h4 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.jury.president"/></h4>

<logic:empty name="thesis" property="president">
    <p>
        <em><bean:message key="title.coordinator.thesis.edit.president.empty"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="president">
    <fr:view name="thesis" property="president" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
			<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>


<%-- Jury/"Vowels" --%>
<h4 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.vowels"/></h4>

<logic:empty name="thesis" property="vowels">
    <p>
        <em><bean:message key="title.coordinator.thesis.edit.vowels.empty"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="vowels">
    <logic:iterate id="vowel" name="thesis" property="vowels">
        <fr:view name="vowel" layout="tabular" schema="thesis.jury.proposal.person">
            <fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
				<fr:property name="columnClasses" value="width12em,,"/>
            </fr:layout>
        </fr:view>
    </logic:iterate>
</logic:notEmpty>
