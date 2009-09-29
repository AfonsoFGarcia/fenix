<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.extraCurricularActivities.import" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<div class="infoop2">
    <p class="mbottom05">Esta �rea permite a importa��o de Actividades Extra Curriculares. A importa��o � feita atrav�s de ficheiros Excel com o seguinte formato:</p>
    <ul class="mtop05 mbottom05">
        <li>Coluna 1: N� de aluno</li>
        <li>Coluna 2: Primeiro e �ltimo nome</li>
        <li>Coluna 3: Data de in�cio</li>
        <li>Coluna 4: Data de fim</li>
    </ul>
    Linhas j� importadas pr�viamente (todos os campos iguais aos de uma importa��o pr�via) ser�o ignoradas, linhas com data de fim posterior (mas com a mesma data de in�cio) ser�o consideradas uma extens�o de uma actividade j� existente que ser� modificada para reflectir essa extens�o.
</div>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
    <p>
        <span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
    </p>
</html:messages>

<fr:form action="/importData.do?method=importExtraCurricularActivities" encoding="multipart/form-data">
	<fr:edit id="importFile" name="file" schema="student.extraCurricularActivity.import">
	    <fr:layout name="tabular" >
	        <fr:property name="classes" value="tstyle5 thmiddle thright thlight"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	</fr:edit>
	<html:submit />
</fr:form>

<logic:present name="result">
    <p class="mtop2 mbottom05"><bean:message key="info.extraCurricularActivities.successfully.imported" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>
    <fr:view name="result" schema="student.extraCurricularActivities.import.status">
		<fr:layout name="tabular">
		    <fr:property name="classes" value="tstyle1 thlight tdcenter mtop05"/>
		</fr:layout>
    </fr:view>
</logic:present>