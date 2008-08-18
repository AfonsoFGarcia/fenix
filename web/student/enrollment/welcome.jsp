<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/joda.tld" prefix="joda"%>
<html:xhtml/>

<logic:present role="STUDENT">

<em>Portal do Estudante</em>
<h2>Inscri��o em Disciplinas</h2>

<div class="infoop6">

<p class="mtop05">
As inscri��es em unidades curriculares e a reserva de turnos para o 1� semestre de 2008/2009 decorrer�o online atrav�s do sistema F�nix a partir do dia <strong>01 de Setembro de 2008 pelas 00h</strong> e <strong>at�</strong> ao dia <strong>8 de Setembro de 2008</strong>. 
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
Um aluno n�o poder� inscrever-se em cada semestre a um conjunto de unidades curriculares que correspondam a mais de 40,5 cr�ditos ECTS. Para este efeito, considera-se que o n�mero de cr�ditos ECTS de uma unidade curricular em repet�ncia de inscri��o � ponderado em 75%.

No caso de inscri��o na unidade curricular de Disserta��o, as normas espec�ficas de cada curso, a definir pela coordena��o de curso, dever�o prever qual o n�mero m�nimo de cr�ditos ECTS aprovados necess�rios para a inscri��o.
</p>

<p class="mtop05">
<strong>4. Inscri��o simult�nea em 1� e 2� ciclo</strong><br/> 
No caso de cursos n�o integrados e de acordo com o DL 74/2006 de 24 de Mar�o o acesso ao 2o ciclo est� condicionado � titularidade de um curso de 1o ciclo. Contudo � permitida a frequ�ncia de unidades curriculares de ciclos subsequentes. Neste sentido, no ano lectivo de 2008/2009 ser� permitido aos alunos de cursos de 1ociclo a frequ�ncia de unidades curriculares de 2o ciclo, com excep��o da unidade curricular de Disserta��o, dentro das seguintes condi��es:
</p>
<p class="indent1">     
1 - Exist�ncia de coer�ncia cient�fica entre o curso de 1o ciclo frequentado e o curso de 2o ciclo cujas unidades curriculares se pretende frequentar;
</p>
<p class="indent1"> 
2 - Aprova��o em mais de 120 ECTS do curso de 1o ciclo;
</p>
<p class="indent1"> 
3 - Inscri��o em todas as unidades curriculares do curso de 1o ciclo necess�rias para a conclus�o do mesmo e que estejam em funcionamento nesse semestre;
</p>
<p class="indent1"> 
4 - A soma do n�mero de cr�ditos j� aprovados em unidades curriculares de 2o ciclo com o n�mero de cr�ditos de 2o ciclo em que se inscreve, ECTS 2ociclo, ter� que verificar a seguinte desigualdade: ECTS 2ociclo &lt;= 1.4 x ECTS 1ociclo - 168, onde ECTS 1ociclo corresponde � soma do n�mero de cr�ditos ECTS de unidades curriculares do 1o ciclo conclu�das.
</p>
<p class="indent1"> 
5 - Respeito das restantes regras e limites de inscri��o.
</p>
<p class="indent1"> 
6 - A inscri��o na unidade curricular de Disserta��o est� vedada a alunos do 1o ciclo, apenas podendo ser efectuada por alunos do 2o ciclo. 
</p>

<p class="mtop05">
<strong>5. Inscri��o em Melhoria de Nota</strong><br/>
Estas inscri��es s�o realizadas <strong>exclusivamente junto dos Servi�os Acad�micos</strong> de acordo com os prazos constantes no <html:link href="http://www.ist.utl.pt/files/ensino/reg_1e2ciclo_20082009.pdf">regulamento</html:link> 
</p>

<p class="mtop05">
<strong>6. Estudantes em regime de tempo parcial</strong><br/>
Um aluno em tempo parcial n�o poder� inscrever-se em unidades curriculares cujo somat�rio de ECTS ultrapasse 50% do n�mero m�ximo de ECTS a que � permitida a inscri��o a um aluno do IST em regime de tempo integral.
</p>

<p class="mtop05">
<strong>7.</strong> Relembramos que durante o per�odo de inscri��es pode acrescentar/alterar/corrigir a sua inscri��o novamente no sistema.
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

