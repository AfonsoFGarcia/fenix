<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/joda.tld" prefix="joda"%>

<logic:present role="STUDENT">

<em><bean:message bundle="STUDENT_RESOURCES"  key="title.student.portalTitle" /></em>
<h2><bean:message key="label.enrollment.courses.instructions" bundle="STUDENT_RESOURCES"/></h2>

<!--<p class="mtop2 mbottom05">Antes de prosseguir com a inscrição em disciplinas, para facilitar o processo tome conhecimento de:</p>-->
<!--<ul class="mtop05">-->
<!--	<li>regulamento oficial de inscrições: <a href="http://www.ist.utl.pt/files/alunos/reg_1e2ciclo_20092010.pdf">Regulamentos dos Cursos de 1º e 2º Ciclo</a> "Regulamento de Matrículas e Inscrições" (pág. 10)</li>-->
<!--	<li>regras de inscrição do seu curso</li>-->
<!--	<li>disciplinas a que se pretende inscrever</li>-->
<!--</ul>-->


<h3 class="mtop15 separator2">Introdução</h3>

<p>Na tabela encontra representados os grupos de disciplinas (linhas cinzentas), disciplinas em que não está inscrito (nas linhas a branco), disciplinas com inscrição confirmada (linhas verdes), disciplinas com inscrição provisória (linhas amarelas) e disciplinas com inscrição impossível (linhas vermelhas).  As disciplinas que já concluíu não aparecem, só aparecem as disciplinas e grupos a que se pode inscrever. Os grupos de disciplinas estão hierarquizados de acordo com a estrutura do curso.</p>


<h3 class="mtop15 separator2">Como Proceder</h3>

<p><b>Atenção:</b> As inscrições em grupos e em disciplinas devem ser feitas separadamente. Se seleccionar um grupo e uma disciplina e fizer Guardar vai ocorrer um erro. Deverá escolher apenas disciplinas ou apenas grupos de cada vez que fizer Guardar.</p>

<ul class="list4">
<li><strong>Inscrever em disciplinas:</strong><br/> Seleccione a(s) "checkbox(es)" e faça Guardar.</li>
<li><strong>Desinscrever de disciplinas:</strong><br/> Desmarque a(s) "checkbox(es)" e faça Guardar.</li>
<li><strong>Escolher grupos de disciplinas:</strong><br/> Seleccione a "checkbox" do grupo e faça Guardar. Só depois de Guardar é que aparecem as disciplinas e sub-grupos que o constituem.</li>
<li><strong>Escolher disciplinas de opção:</strong><br/> Carregar no link "Escolher Opção". Deverá inscrever-se primeiro nas disciplinas e nos grupos do currículo e só no final às disciplinas de opção. <em>Atenção: quando carrega em "Escolher Opção" as alterações que não foram guardadas são perdidas. Antes de carregar em  "Escolher Opção" certifique-se que guardou as alterações.</em></li>
<li><strong>Terminar o processo de inscrição:</strong><br/> Depois de terminado o processo de inscrição pode consultar o seu Currículo do Aluno para e ver a totalidade das disciplinas em que está inscrito.</li>
</ul>


<h3 class="mtop15 separator2">Tipos de Inscrição</h3>

<ul class="list4">
	<li><strong>Inscrições confirmadas:</strong> <span class="se_enrolled">(linhas verdes)</span></li>
	<li><strong>Inscrições provisórias:</strong> <span class="se_temporary">(linhas amarelas)</span><br/> São inscrições ainda por confirmar, porque os requisitos mínimos necessários para efectivar a inscrição ainda não estão assegurados. Acontece em casos como:
		<ul>
			<li>Tem uma inscrição provisória na disciplina A que tem precedência à disciplina B. Ainda não recebeu nota na disciplina B logo a inscrição fica "provisória" até se oficializar a aprovação na disciplina A.</li>
			<li>Tem uma inscrição provisória na disciplina C que tem exclusividade com a disciplina D. Ainda não recebeu nota na disciplina D logo a inscrição fica "provisória" até se oficializar o resultado na disciplina A.</li>
		</ul>
	</li>
	<li><strong>Inscrições impossíveis:</strong> <span class="se_impossible">(linhas vermelhas)</span><br/> Não foram preenchidos os requesitos para confirmar a inscrição. Deverá refazer a inscrição noutra(s) disciplina(s) antes de terminar o prazo.</li>
</ul>


<h3 class="mtop15 separator2">Inscrições em 2º e 3º Ciclo</h3>

<p class="mbottom05">Para efectuar inscrições no 2º e 3º ciclos deverá inscrever-se no grupo a que corresponde o ciclo. A seguir é direccionado para uma página onde serão listadas opções onde terá que escolher o curso desejado. Depois de escolhido o curso, voltará à página de inscrições onde poderá proceder com as inscrições em disciplinas no novo ciclo.</p>
<p class="mtop05">Se desejar mudar o curso do novo ciclo terá de se desinscrever a todas as disciplinas do novo ciclo e depois poderá escolher um novo.</p>

</logic:present>

