<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message key="label.internship.candidacy.title" bundle="INTERNSHIP_RESOURCES" /></h2>

<logic:present name="candidacy">

<style>
th { width: 150px; }
</style>

<fr:form action="/internship.do?method=submitCandidacy">

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internship.candidacy.section.studentinfo" bundle="INTERNSHIP_RESOURCES" />
    </strong></p>
    <fr:edit id="studentinfo" name="candidacy" schema="internship.candidacy.studentinfo">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internship.candidacy.section.personalinfo" bundle="INTERNSHIP_RESOURCES" />
    </strong></p>
    <fr:edit id="personalinfo" name="candidacy" schema="internship.candidacy.personalinfo">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internship.candidacy.section.iddocument" bundle="INTERNSHIP_RESOURCES" />
    </strong></p>
    <fr:edit id="documents.bi" name="candidacy" schema="internship.candidacy.personaldocuments.bi">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internship.candidacy.section.passport" bundle="INTERNSHIP_RESOURCES" />
    </strong></p>
    <fr:edit id="documents.passport" name="candidacy" schema="internship.candidacy.personaldocuments.passport">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internship.candidacy.section.address" bundle="INTERNSHIP_RESOURCES" />
    </strong></p>
    <fr:edit id="address" name="candidacy" schema="internship.candidacy.address">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internship.candidacy.section.contacts" bundle="INTERNSHIP_RESOURCES" />
    </strong></p>
    <fr:edit id="contacts" name="candidacy" schema="internship.candidacy.contacts">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
	</fr:edit>

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internship.candidacy.section.destinations" bundle="INTERNSHIP_RESOURCES" />
    </strong></p>
    <fr:edit id="destination" name="candidacy" schema="internship.candidacy.destination">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internship.candidacy.section.languages" bundle="INTERNSHIP_RESOURCES" />
    </strong></p>
    <fr:edit id="languages" name="candidacy" schema="internship.candidacy.languages">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <fr:edit id="previous" name="candidacy" schema="internship.candidacy.previous">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <input type="hidden" name="method" />
    <p>
        <html:submit onclick="this.form.method.value='submitCandidacy';">
            <bean:message bundle="COMMON_RESOURCES" key="button.continue" />
        </html:submit>
    </p>
</fr:form>

</logic:present>

<logic:notPresent name="candidacy">
	<h3>As candidaturas a est�gios internacionais abrem �s 9h do dia 10 de Novembro de 2008</h3>
</logic:notPresent>