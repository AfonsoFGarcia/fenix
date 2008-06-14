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

<p class="mtop05">
As inscri��es em unidades curriculares e a reserva de turnos para o 2� semestre de 2007/2008 decorrer�o online atrav�s do sistema F�nix a partir do dia <strong>16 de Fevereiro de 2008 pelas 16h</strong> e <strong>at�</strong> ao dia <strong>22 de Fevereiro de 2008</strong>. 
</p>

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
Devido � transi��o para Bolonha, alunos que tenham estado inscritos o ano passado em Licenciatura (5 anos) encontram-se pela primeira vez nos cursos Bolonha. Todas as inscri��es em disciplinas que seriam repet�ncias s�o consideradas como em 1� vez nos cursos Bolonha e contabilizados os ECTS a 100%.<br/> 
Assim, para dar resposta a esta situa��o, foi decidido pelo Conselho Directivo, para o ano lectivo de 2007/2008, aumentar para 42 ECTS o n�mero m�ximo de cr�ditos para inscri��o por semestre.<br/>
Esta regra n�o se aplica a inscri��es em disciplinas em que n�o houve aprova��o j� nos cursos Bolonha, valendo nesse caso os ECTS a 75% na pr�xima inscri��o.
</p>

<p class="mtop05">
<strong>4. Inscri��o simult�nea em 1� e 2� ciclo</strong><br/> 
Os alunos de cursos n�o integrados que ainda n�o tenham conclu�do o 1� ciclo e que se inscrevam em unidade(s) curricular(es) de 2� ciclo dever�o respeitar os seguintes limites:
</p>
<p class="indent1">     
- a soma de n�mero de cr�ditos entre as unidades curriculares aprovadas e as inscritas no 2� ciclo n�o podem exceder os 80 ECTS;
</p>
<p class="indent1"> 
- os alunos que n�o tenham conclu�do o 1� ciclo est�o impedidos de inscri��o na unidade curricular de Disserta��o/Projecto.
</p>

<p class="mtop05">
<strong>5. Inscri��o em Melhoria de Nota</strong><br/>
Estas inscri��es s�o realizadas <strong>exclusivamente junto dos Servi�os Acad�micos</strong> de acordo com os prazos constantes no <html:link href="http://www.ist.utl.pt/files/ensino/reg_1e2ciclo_20072008.pdf">regulamento</html:link> 
</p>

<p class="mtop05">
<strong>6.</strong> Relembramos que durante o per�odo de inscri��es pode acrescentar/alterar/corrigir a sua inscri��o novamente no sistema.
</p>

<p class="mtop05">
<strong>7. Alunos com inscri��o apenas no 2� semestre</strong><br/>
Se ainda <strong>n�o realizou a transi��o</strong> e por isso n�o consegue inscrever-se, ent�o <strong>envie-nos o seu pedido por email.</strong><br/>
Deve indicar "Transitar para Bolonha" no assunto do mail a enviar para o endere�o <html:link href="mailto:inscricoes@ist.utl.pt?subject=Transitar para Bolonha">inscricoes@ist.utl.pt</html:link><br/>
Assim que tiver a matr�cula Bolonha dispon�vel deve proceder � sua inscri��o.<br/>
</p>

<p class="mtop05">
<strong>8.</strong> Para apoio ao processo de inscri��es deste semestre foi criado um endere�o espec�fico para onde dever� encaminhar as suas d�vidas ou dificuldades que n�o se vejam respondidas pelas instru��es acima:     
</p>

<p class="indent1">   
<html:link href="mailto:inscricoes@ist.utl.pt">inscricoes@ist.utl.pt</html:link>
</p>

</div>

<html:form action="/studentEnrollmentManagement.do?method=prepare">
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message  key="label.continue" bundle="APPLICATION_RESOURCES"/></html:submit>
	</p>
</html:form>


</logic:present>

