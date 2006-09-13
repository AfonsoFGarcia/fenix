<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>

<logic:messagesPresent property="error.exception.notAuthorized">
	<span class="error"><!-- Error messages go here -->
		<bean:message key="label.notAuthorized.courseInformation" />
	</span>	
</logic:messagesPresent>

<logic:messagesNotPresent property="error.exception.notAuthorized">
	<img src="<%= request.getContextPath() %>/images/title_adminDisc.gif" alt="<bean:message key="title_adminDisc" bundle="IMAGE_RESOURCES" />" />
	<div class="mvert1">
		<bean:message key="label.instructions" />

<%-- 
		<p>Neste portal poder�, entre outras funcionalidades, personalizar a p�gina p�blica da disciplina e a informa��o disponibilizada. Regularmente iremos introduzir novas funcionalidades e melhorar as existentes. Assim, todas as cr�ticas e sugest�es s�o importantes. Contacte-nos atrav�s do e-mail <a href="mailto:suporte@dot.ist.utl.pt ">suporte@dot.ist.utl.pt</a></p>
		<ul class="list4">
			<li>A sec��o <b>Personaliza��o</b> permitem introduzir alguns textos para a p�gina inicial da disciplina, assim como, indicar a URL da p�gina alternativa da disciplina - caso esta exista - e o seu contacto.</li>
			<li>Poder� gerir os <b>An�ncios</b> (acrescentar, editar ou apagar) escolhendo a op��o correspondente.</li>
			<li>Tem ainda a possibilidade de criar <b>Sec��es</b> (e sub-sec��es opcionais) nas p�ginas da disciplina para conterem informa��o como listas de problemas, material de apoio, etc.</li>
			<li>Poder� introduzir os <b>Sum�rios</b> referentes �s suas aulas, e inclusivamente criar o sum�rio a partir de um sum�rio/planeamento j� existente.</li>
			<li>Se existirem outros docentes a leccionar a disciplina podem ser indicados atrav�s da op��o <b>Docentes</b>, permitindo a estes 	administrar tamb�m a p�gina de disciplina.</li>
			<li>A op��o <b>Alunos</b> permite obter, sob a forma de uma tabela, listas com informa��o dos alunos a frequentar a disciplina. � poss�vel filtrar a lista de modo a obter apenas os alunos inscritos num determinado turno. O sistema permite tamb�m enviar uma mensagem de correio electr�nico para os todos os alunos da lista vis�vel num determinado momento, assim 	como exportar a lista para uma folha de c�lculo.</li>
			<li>O <b>Planeamento</b> permite elaborar um plano para as aulas que ir� leccionar durante o semestre. Para facilitar a cria��o do 	planeamento � poss�vel importar sum�rios ou planos de aula j� criados em anos anteriores, e posteriormente fazer altera��es.</li>
			<li>Em <b>Avalia��o</b> pode introduzir e alterar notas, afixar notas na p�gina da disciplina e submeter as notas � Secretaria. Pode tamb�m marcar Testes fora do per�odo de Exames.</li>
			<li>As op��es <b>Objectivos, Programa, M�todos de Avalia��o e Bibliografia</b> devem ser preenchidas e/ou actualizadas (note que no caso das disciplinas b�sicas, os objectivos, o programa e os m�todos de avalia��o s�o da responsabilidade do Conselho Cient�fico) pois para al�m de serem disponibilizadas nas p�ginas da disciplina, tamb�m servem para os relat�rios e avalia��es das licenciaturas.</li>
			<li>Se o desejar, pode efectuar a gest�o de <b>Grupos</b>, criando e editando agrupamentos da disciplina, podendo ainda export�-los para outra(s) disciplina(s). Dentro de cada agrupamento pode-se visualizar, criar e apagar grupos, com alunos pertencentes a um conjunto que pode gerir. Cada grupo pode estar associado a um turno alter�vel e os elementos do grupo podem ser modificados de acordo com as permiss�es dadas pelo conjunto.</li>
		</ul>
--%>
	
	</div>
</logic:messagesNotPresent>