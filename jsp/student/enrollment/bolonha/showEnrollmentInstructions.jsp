<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/joda.tld" prefix="joda"%>

<logic:present role="STUDENT">

<em><bean:message bundle="STUDENT_RESOURCES"  key="title.student.portalTitle" /></em>
<h2><bean:message key="label.enrollment.courses.instructions" bundle="STUDENT_RESOURCES"/></h2>

<p class="mtop2 mbottom05">Para facilitar o processo, antes de prosseguir com a inscri��o em disciplinas, tome conhecimento de:</p>
<ul class="mtop05">
	<li>regras de inscri��o do seu curso</li>
	<li>disciplinas a que se pretende inscrever antes de prosseguir com a inscri��o</li>
</ul>


<h3 class="mtop15 separator2">Introdu��o</h3>

<p>Na tabela encontra representados os grupos de disciplinas (linhas amarelas), disciplinas que ainda n�o conclu�u (nas linhas a branco), disciplinas em que se encontra inscrito (linhas verdes), disciplinas com inscri��o provis�ria (linhhas amarelas) e disciplinas com inscri��o imposs�vel (linhas vermelhas).  As disciplinas que j� conclu�u n�o aparecem, s� aparecem as disciplinas e grupos a que se pode inscrever. Os grupos de disciplinas est�o hierarquizados de acordo com a estrutura do curso.</p>


<h3 class="mtop15 separator2">Como Proceder</h3>

<ul class="list4">
<li><strong>Inscrever em disciplinas:</strong><br/> Seleccione as "checkboxes" e fa�a Guardar.</li>
<li><strong>Desinscrever de disciplinas:</strong><br/> Desseleccione as "checkboxes" e fa�a Guardar.</li>
<li><strong>Escolher grupos de disciplinas:</strong><br/> Seleccione a "checkbox" do grupo e fa�a Guardar. S� depois de Guardar � que aparecem as disciplinas e sub-grupos que o constituem.</li>
<li><strong>Escolher disciplinas de op��o (qualquer disciplina do IST):</strong><br/> Carregar no link "Escolher Op��o". Dever� inscrever-se primeiro nas disciplinas e nos grupos do curr�culo e s� no final �s disciplinas de op��o. <em>Aten��o: quando carrega em "Escolher Op��o" as op��es que n�o foram guardadas s�o perdidas. Antes de carregar em  "Escolher Op��o" certifique-se que guardou as altera��es.</em></li>
<li><strong>Confirmar a inscri��o em disciplinas:</strong><br/> As linhas que aparecem a verde correpondem �s disciplinas em que se encontra inscrito. Tamb�m pode consultar o seu Curr�culo do Aluno para ver a totalidade das disciplinas a que est� inscrito.</li>
</ul>


<h3 class="mtop15 separator2">Tipos de Inscri��o</h3>

<ul class="list4">
	<li><strong>Inscri��es confirmadas</strong> (linhas verdes)</li>
	<li><strong>Inscri��es provis�rias:</strong> (linhas amarelas)<br/> S�o inscri��es ainda por confirmar, porque os requisitos m�nimos necess�rios para efectivar a inscri��o ainda n�o est�o assegurados. Acontece em casos como:
		<ul>
			<li>Tem uma inscri��o provis�ria na disciplina A que tem preced�ncia � disciplina B. Ainda n�o recebeu nota na disciplina B logo a inscri��o fica "provis�ria" at� se oficializar a aprova��o na disciplina A.</li>
			<li>Tem uma inscri��o provis�ria na disciplina C que tem exclusividade com a disciplina D. Ainda n�o recebeu nota na disciplina D logo a inscri��o fica "provis�ria" at� se oficializar o resultado na disciplina A.</li>
		</ul>
	</li>
	<li><strong>Inscri��es imposs�veis:</strong> (linhas vermelhas)<br/> Inscri��es nas quais n�o foram preenchidos os requesitos para confirmar a inscri��o. Dever� refazer a inscri��o noutra disciplina antes de terminar o prazo.</li>
</ul>


</logic:present>

