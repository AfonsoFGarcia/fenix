<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="STUDENT">


<%-- ### Title #### --%>
<em><bean:message  key="label.phd.student.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.student.enrolments" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>



<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>

<div class="infoop6">

<logic:notEmpty name="enrolmentPeriod">
	<p class="mtop05">
		As inscri��es em unidades curriculares e a reserva de turnos para o <bean:write name="enrolmentPeriod" property="executionPeriod.qualifiedName" /> decorrer�o online atrav�s do sistema F�nix a partir do dia <span class="error0"><strong><fr:view name="enrolmentPeriod" property="startDateDateTime" layout="as-date"><fr:layout><fr:property name="format" value="dd 'de' MMMM 'de' yyyy 'pelas' HH:mm"/></fr:layout></fr:view></strong> e <strong>at�</strong> ao dia <strong><fr:view name="enrolmentPeriod" property="endDateDateTime" layout="as-date"><fr:layout><fr:property name="format" value="dd 'de' MMMM 'de' yyyy 'pelas' HH:mm"/></fr:layout></fr:view></strong></span>. 
	</p>
</logic:notEmpty>

<p class="mtop05">
Antes de efectuar a sua inscri��o dever�o ser tidas em conta as seguintes situa��es:
</p>

<p class="mtop05">
<strong>1. Classifica��es por lan�ar</strong><br/>
Se existirem classifica��es por lan�ar que <strong>impe�am a sua normal inscri��o,</strong> deve contactar os respons�vel(eis) da(s) unidade(s) curricular(es) para que o lan�amento da(s) classifica��o(�es) se fa�a antes de concretizar a sua inscri��o.
</p>

<p class="mtop05">
<strong>2. Equival�ncias em falta</strong><br/>

Se existir(em) equival�ncia(s) que n�o se encontre(m) registada(s) no seu curr�culo, n�o dever� proceder � sua inscri��o. Nesta situa��o dever� obter, junto da Coordena��o do curso, a aprova��o/correc��o das equival�ncias em falta e proceder � sua entrega na Secretaria dos Servi�os Acad�micos.
</p>

<p class="mtop05">
<strong>3. N�mero m�ximo de inscri��es</strong><br/> 
Um aluno n�o poder� inscrever-se em cada semestre a um conjunto de unidades curriculares que correspondam a mais de 40,5 cr�ditos ECTS. Para este efeito, considera-se que o n�mero de cr�ditos ECTS de uma unidade curricular em repet�ncia de inscri��o � ponderado <strong>de forma igual ao de uma unidade curricular em 1� inscri��o</strong>.
</p>

<p class="mtop05">
<strong>4. Preced�ncias</strong><br/>
N�o � poss�vel a inscri��o em qualquer unidade curricular se n�o estiver garantida a inscri��o em todas as unidades curriculares, em funcionamento, correspondentes a semestres curriculares anteriores. Poder�o existir, para al�m desta regra geral de inscri��o, regras espec�ficas de preced�ncia para cada curso ou grupo de unidades curriculares.
</p>

<p class="mtop05">
<strong>5. Inscri��o em Melhoria de Nota</strong><br/>
Estas inscri��es s�o realizadas <strong>exclusivamente junto dos Servi�os Acad�micos</strong> de acordo com os prazos constantes no <a href="http://www.ist.utl.pt/files/alunos/reg_3ciclo.pdf">Regulamento de 3� Ciclo</a> 

</p>

<p class="mtop05">
<strong>6. Estudantes em regime de tempo parcial</strong><br/>
Um aluno em tempo parcial n�o poder� inscrever-se em unidades curriculares cujo somat�rio de ECTS ultrapasse 50% do n�mero m�ximo de ECTS a que � permitida a inscri��o a um aluno do IST em regime de tempo integral.
</p>

<p class="mtop05">
<strong>7.</strong> Relembramos que durante o per�odo de inscri��es pode acrescentar/alterar/corrigir a sua inscri��o novamente no sistema.
</p>

<p class="mtop05">
<strong>8.</strong> Para qualquer esclarecimento adicional dever� consultar o <a href="http://www.ist.utl.pt/files/alunos/reg_3ciclo.pdf">Regulamento de 3� Ciclo</a>.
</p>

<p class="mtop05">
<strong>9.</strong> Para apoio ao processo de inscri��es: 
<a href="/student/exceptionHandlingAction.do?method=prepareSupportHelp&contextId=94639&amp;contentContextPath_PATH=/estudante/estudante" target="_blank">Suporte</a>
</p>

<p class="mtop05">
<strong>10.</strong> Quando terminar o processo de inscri��o deve efectuar a reserva de turmas em  <a href="/student/studentShiftEnrollmentManager.do?method=prepare&amp;contentContextPath_PATH=/estudante/estudante" title="Reserva de Turmas">Turmas</a>
</p>

</div>


<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<bean:define id="registrationOid" name="registration" property="externalId" />

<fr:form action="<%= "/phdStudentEnrolment.do?method=prepare&registrationOid=" + registrationOid.toString() %>">
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message  key="label.continue" bundle="PHD_RESOURCES"/></html:submit>
	</p>
</fr:form>

<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>


</logic:present>