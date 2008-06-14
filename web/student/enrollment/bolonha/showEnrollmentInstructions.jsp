<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/joda.tld" prefix="joda"%>

<logic:present role="STUDENT">

<em><bean:message bundle="STUDENT_RESOURCES"  key="title.student.portalTitle" /></em>
<h2><bean:message key="label.enrollment.courses.instructions" bundle="STUDENT_RESOURCES"/></h2>

<p class="mtop2 mbottom05">Antes de prosseguir com a inscri��o em disciplinas, para facilitar o processo tome conhecimento de:</p>
<ul class="mtop05">
	<li>regulamento oficial de inscri��es: <a href="http://www.ist.utl.pt/files/ensino/reg_1e2ciclo_20072008.pdf">Regulamentos dos Cursos de 1� e 2� Ciclo</a> (PDF, 159KB) "Regulamento de Matr�culas e Inscri��es" (p�g. 21)</li>
	<li>regras de inscri��o do seu curso</li>
	<li>disciplinas a que se pretende inscrever</li>
</ul>


<h3 class="mtop15 separator2">Introdu��o</h3>

<p>Na tabela encontra representados os grupos de disciplinas (linhas cinzentas), disciplinas em que n�o est� inscrito (nas linhas a branco), disciplinas com inscri��o confirmada (linhas verdes), disciplinas com inscri��o provis�ria (linhas amarelas) e disciplinas com inscri��o imposs�vel (linhas vermelhas).  As disciplinas que j� conclu�u n�o aparecem, s� aparecem as disciplinas e grupos a que se pode inscrever. Os grupos de disciplinas est�o hierarquizados de acordo com a estrutura do curso.</p>


<h3 class="mtop15 separator2">Como Proceder</h3>

<p><b>Aten��o:</b> As inscri��es em grupos e em disciplinas devem ser feitas separadamente. Se seleccionar um grupo e uma disciplina e fizer Guardar vai ocorrer um erro. Dever� escolher apenas disciplinas ou apenas grupos de cada vez que fizer Guardar.</p>

<ul class="list4">
<li><strong>Inscrever em disciplinas:</strong><br/> Seleccione a(s) "checkbox(es)" e fa�a Guardar.</li>
<li><strong>Desinscrever de disciplinas:</strong><br/> Desmarque a(s) "checkbox(es)" e fa�a Guardar.</li>
<li><strong>Escolher grupos de disciplinas:</strong><br/> Seleccione a "checkbox" do grupo e fa�a Guardar. S� depois de Guardar � que aparecem as disciplinas e sub-grupos que o constituem.</li>
<li><strong>Escolher disciplinas de op��o:</strong><br/> Carregar no link "Escolher Op��o". Dever� inscrever-se primeiro nas disciplinas e nos grupos do curr�culo e s� no final �s disciplinas de op��o. <em>Aten��o: quando carrega em "Escolher Op��o" as altera��es que n�o foram guardadas s�o perdidas. Antes de carregar em  "Escolher Op��o" certifique-se que guardou as altera��es.</em></li>
<li><strong>Terminar o processo de inscri��o:</strong><br/> Depois de terminado o processo de inscri��o pode consultar o seu Curr�culo do Aluno para e ver a totalidade das disciplinas em que est� inscrito.</li>
</ul>


<h3 class="mtop15 separator2">Tipos de Inscri��o</h3>

<ul class="list4">
	<li><strong>Inscri��es confirmadas:</strong> <span class="se_enrolled">(linhas verdes)</span></li>
	<li><strong>Inscri��es provis�rias:</strong> <span class="se_temporary">(linhas amarelas)</span><br/> S�o inscri��es ainda por confirmar, porque os requisitos m�nimos necess�rios para efectivar a inscri��o ainda n�o est�o assegurados. Acontece em casos como:
		<ul>
			<li>Tem uma inscri��o provis�ria na disciplina A que tem preced�ncia � disciplina B. Ainda n�o recebeu nota na disciplina B logo a inscri��o fica "provis�ria" at� se oficializar a aprova��o na disciplina A.</li>
			<li>Tem uma inscri��o provis�ria na disciplina C que tem exclusividade com a disciplina D. Ainda n�o recebeu nota na disciplina D logo a inscri��o fica "provis�ria" at� se oficializar o resultado na disciplina A.</li>
		</ul>
	</li>
	<li><strong>Inscri��es imposs�veis:</strong> <span class="se_impossible">(linhas vermelhas)</span><br/> N�o foram preenchidos os requesitos para confirmar a inscri��o. Dever� refazer a inscri��o noutra(s) disciplina(s) antes de terminar o prazo.</li>
</ul>


<h3 class="mtop15 separator2">Inscri��es em 2� e 3� Ciclo</h3>

<p class="mbottom05">Para efectuar inscri��es no 2� e 3� ciclos dever� inscrever-se no grupo a que corresponde o ciclo. A seguir � direccionado para uma p�gina onde ser�o listadas op��es onde ter� que escolher o curso desejado. Depois de escolhido o curso, voltar� � p�gina de inscri��es onde poder� proceder com as inscri��es em disciplinas no novo ciclo.</p>
<p class="mtop05">Se desejar mudar o curso do novo ciclo ter� de se desinscrever a todas as disciplinas do novo ciclo e depois poder� escolher um novo.</p>

</logic:present>

