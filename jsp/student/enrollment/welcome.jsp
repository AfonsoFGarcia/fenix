<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/joda.tld" prefix="joda"%>

<logic:present role="STUDENT">

<em>Portal do Estudante</em>
<h2>Inscri��o em Disciplinas</h2>

<div class="infoop6">

	Caro(a) Aluno(a)<br/>
	
	<p class="mtop05">
	As inscri��es em unidades curriculares e a reserva de turnos para o 1� semestre de 2007/2008 decorrer�o online atrav�s do sistema F�nix 
	a partir do dia <strong>1 de Setemrbo de 2007 pelas 15h</strong> e <strong>at�</strong> ao dia <strong>10 de Setembro de 2007</strong>.
	No entanto e devido ao processo de transi��o curricular associado ao processo de Bolonha, haver� que ter em conta as seguintes restri��es
	</p>
	
	<p class="mtop15">
	<strong>1.</strong> Os alunos <strong>n�o dever�o proceder � sua inscri��o</strong> nas seguintes situa��es, pois comprometer�o a transi��o do seu curr�culo 
	para o novo curr�culo de Bolonha:
	</p>
	
	<p class="indent1">
	a)<strong> Alunos com classifica��es por lan�ar</strong><br/>
	Devem <strong>contactar os respons�vel(eis)</strong> da(s) unidade(s) curricular(es) para que o lan�amento da(s) classifica��o(�es) se fa�a antes da inscri��o em qualquer 
	unidade curricular.
	</p>
	
	<p class="indent1">
	b)<strong> Alunos com equival�ncias em falta</strong><br/>
	Devem <strong>contactar as coordena��es dos cursos</strong> para que a equival�ncia seja considerada antes de formalizarem a sua inscri��o em qualquer unidade curricular.
	</p>
	
	<p class="indent1">
	c)<strong> Alunos com acesso a �poca especial.</strong><br/>
	Dever�o proceder � sua <strong>inscri��o apenas no per�odo de 8 a 12 de Outubro de 2007</strong>, caso contr�rio n�o ser� poss�vel contabilizar as unidades curriculares realizadas em �poca especial.
	</p>
	
	<p class="indent1">
	d)<strong> Alunos que tenham sido considerados como prescritos para o ano lectivo de 2007/2008</strong><br/> 
	<strong>N�o poder�o realizar a sua inscri��o nesta fase</strong>, s� o podendo fazer no caso dos seus recursos de prescri��o terem sido aceites e nas datas definidas no regulamento de inscri��es (8 a 12 de Outubro de 2007).
	</p>
	
	<p class="indent1">
	e)<strong> Alunos com d�vidas de propinas ou que tenham requerido mudan�a de curso</strong><br/>
	S� poder�o realizar a sua <strong>inscri��o ap�s regulariza��o</strong> da sua situa��o junto dos Servi�os Acad�micos.
	</p>
	
	<p class="indent1">
	Nos casos das al�neas a) e b), os alunos poder�o realizar a sua inscri��o ap�s a regulariza��o da sua situa��o e at� ao dia 15 de Setembro 2007 sem agravamento na taxa de inscri��o.
	</p>
	
	<p class="mtop15">
	<strong>2.</strong> Os alunos podem realizar inscri��es at� um <strong>m�ximo de 40,5 ECTS por semestre</strong>.
	</p>
	
	<p class="mtop15">
	<strong>3.</strong> Os alunos de cursos n�o integrados que ainda <strong>n�o tenham conclu�do o 1� ciclo</strong> e que avancem para inscri��o num 2� ciclo, ou seja, realizem inscri��es nos dois ciclos, s� poder�o ter <strong>inscri��es no 2� ciclo</strong> se as unidades curriculares aprovadas mais as inscritas nesse 2� ciclo <strong>n�o excederem os 80 ECTS</strong>. Todos os alunos que n�o tenham conclu�do o 1� ciclo estar�o impedidos de inscri��o na unidade curricular de Disserta��o/Projecto.
	</p>
	
	<p class="mtop15">
	<strong>4.</strong> A interface de inscri��o foi alterada possibilitando a inscri��o simult�nea nas unidades curriculares. Para mais informa��es consulte as instru��es na p�gina seguinte.
	</p> 
	
	<p class="mtop15">
	30 de Agosto de 2007<br/>
	O Conselho Directivo do IST
	</p>

</div>

<html:form action="/studentEnrollmentManagement.do?method=prepare">
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message  key="label.continue" bundle="APPLICATION_RESOURCES"/></html:submit>
	</p>
</html:form>


</logic:present>

