<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="title.application.name.over23" bundle="CANDIDATE_RESOURCES"/>
</div>

<div id="contextual_nav">
<h2 class="brown">Nesta p�gina</h2>
	<ul>
    	<li><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#critsel">Crit�rios de selec��o</a></li>
    	<li><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#vag">Vagas</a></li>
    	<li><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#prop">Propinas</a></li>
    	<li><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#praz">Prazos</a></li>
    	<li><a href="<%= fullPath + "?method=preparePreCreationOfCandidacy" %>"><b>Submeter Candidatura</b></a></li>
    	<li><a href="<%= fullPath + "?method=prepareApplicationAccessRecovery" %>"><b>Recuperar Acesso</b></a></li>
   </ul>
</div>



<h1>Maiores de 23 anos</h1>

<p>Ao Concurso Especial de Acesso destinado a Maiores de 23 anos podem candidatar-se os indiv�duos que, cumulativamente:</p>

<ul>
	<li>Completem 23 anos de idade at� ao dia 31 de Dezembro do ano que antecede a candidatura;</li>
	<li>N�o sejam titulares de habilita��o de acesso ao ensino superior;</li>
	<li>N�o sejam titulares de um curso superior, nem tenham frequ�ncia anterior do ensino superior.</li>
</ul>

<p><a href="http://www.ist.utl.pt/files/ensino/reg_maiores23.pdf">Regulamento</a></p>


<h2 id="critsel">Crit�rios de selec��o</h2>

<p>A avalia��o da capacidade para a frequ�ncia de um curso de licenciatura ou do 1� ciclo de um curso integrado do IST consta das seguintes componentes:</p>

<ol>
	<li>Aprecia��o do curr�culo escolar e profissional do candidato;</li>
	<li>Realiza��o de uma prova escrita de avalia��o de conhecimentos em interpreta��o e express�o escrita;</li>
	<li>Realiza��o de uma prova escrita de avalia��o da capacidade cient�fica;</li>
	<li>Avalia��o das motiva��es do candidato atrav�s da realiza��o de uma entrevista.</li>
</ol>

<p>A prova a que se referem as al�neas 2) e 3) constar� de um conjunto de perguntas, elaboradas por um j�ri, ter� a dura��o m�xima
de tr�s horas e ser� realizada numa �nica chamada. As componentes da prova poder�o variar consoante o(s) curso(s) a que o
candidato pretenda aceder.</p>

<p><a href="http://www.ist.utl.pt/files/ensino/programas_23anos_20082009.pdf">Programa das provas de avalia��o (PDF, 136KB)</a></p>


<h2 id="vag">Vagas</h2>

<table class="tab_lay" width="99%" cellspacing="0" summary="Informa��es sobre vagas por licenciatura para candidatos titulares de Curso M�dio e Superior">
	<tr>
		<th>Cursos</th>
		<th>Campus</th>
		<th>Vagas</th>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/lean?locale=pt">Licenciatura em Engenharia e Arquitectura Naval</a></td>
		<td>Alameda</td>
		<td>1</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/legm?locale=pt">Licenciatura em Engenharia Geol&oacute;gica e de Minas</a></td>
		<td>Alameda</td>
		<td>1</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/leic-a?locale=pt">Licenciatura em Engenharia Inform&aacute;tica e de Computadores</a></td>
		<td>Alameda</td>
		<td>9</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/lemat?locale=pt">Licenciatura em Engenharia de Materiais</a></td>
		<td>Alameda</td>
		<td>1</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/lmac?locale=pt">Licenciatura em Matem&aacute;tica Aplicada e Computa&ccedil;&atilde;o</a></td>
		<td>Alameda</td>
		<td>2</td>	
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/lee?locale=pt">Licenciatura em Engenharia Electr&oacute;nica</a></td>
		<td>Taguspark</td>
		<td>2</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/legi?locale=pt">Licenciatura em Engenharia e Gest&atilde;o Industrial</a></td>
		<td>Taguspark</td>
		<td>1</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/leic-t?locale=pt">Licenciatura em Engenharia Inform&aacute;tica e de Computadores</a></td>
		<td>Taguspark</td>
		<td>5</td>
	</tr>	
	<tr>
		<td><a href="http://fenix.ist.utl.pt/lerc?locale=pt">Licenciatura em Engenharia de Redes de Comunica&ccedil;&otilde;es</a></td>
		<td>Taguspark</td>
		<td>4</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/ma?locale=pt">Mestrado em Arquitectura</a></td>
		<td>Alameda</td>
		<td>3</td>	
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/meaer?locale=pt">Mestrado em Engenharia Aeroespacial</a></td>
		<td>Alameda</td>
		<td>3</td>	
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/mebiol?locale=pt">Mestrado em Engenharia do Ambiente</a></td>
		<td>Alameda</td>
		<td>2</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/mebiol?locale=pt">Mestrado em Engenharia Biol&oacute;gica</a></td>
		<td>Alameda</td>
		<td>3</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/mebiom?locale=pt">Mestrado em Engenharia Biom&eacute;dica</a></td>
		<td>Alameda</td>
		<td>2</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/mec?locale=pt">Mestrado em Engenharia Civil</a></td>
		<td>Alameda</td>
		<td>9</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meec?locale=pt">Mestrado em Engenharia Electrot&eacute;cnica e de Computadores</a></td>
		<td>Alameda</td>
		<td>10</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/meft?locale=pt">Mestrado em Engenharia F&iacute;sica Tecnol&oacute;gica</a></td>
		<td>Alameda</td>
		<td>3</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/memec?locale=pt">Mestrado em Engenharia Mec&acirc;nica</a></td>
		<td>Alameda</td>
		<td>8</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/meq?locale=pt">Mestrado em Engenharia Qu&iacute;mica</a></td>
		<td>Alameda</td>
		<td>4</td>
	</tr>
</table>

<h2 id="prop">Propinas</h2>
<p>Para o ano lectivo de 2008/2009 a propina para as Licenciaturas de 1� ciclo e 1� ciclo de Mestrados Integrados foi fixada no valor de 996,85 Euros.</p>


<h2 id="praz">Prazos</h2>

<table class="tab_simpler" summary="Prazos para candidatos em mudança ou transferência de curso" cellspacing="0">
	<tr>
	<td class="align_r"><span class="marker">16 de Abril de 2009</span></td>
	<td>Afixa��o dos programas das provas</td>
	</tr>
	<tr>
	<td class="align_r"><span class="marker">4 a 29 de Maio de 2009</span></td>
	<td><a href="<%= fullPath + "?method=preparePreCreationOfCandidacy" %>">Apresenta��o de candidaturas </a></td>
	</tr>
	<tr>
	<td class="align_r"><span class="marker">23 de Junho de 2009</span></td>
	<td>Realiza��o dos exames</td>
	</tr>
	<tr>
	<td class="align_r"><span class="marker">6 de Julho de 2009</span></td>
	<td>Afixa��o de resultados dos exames e marca��o das entrevistas</td>
	</tr>
	<tr>
	<td class="align_r"><span class="marker">13 a 17 de Julho de 2009</span></td>
	<td>Entrevistas</td>
	</tr>
	<tr>
	<td class="align_r"><span class="marker">17 de Julho de 2009</span></td>
	<td>Afixa��o dos resultados finais</td>
	</tr>
	<tr>
	<td class="align_r"><span class="marker">21 de Julho de 2009</span></td>
	<td>Data limite para apresenta��o de recursos ao Presidente do IST</td>
	</tr>
	<tr>
	<td class="align_r"><span class="marker">24 de Julho de 2009</span></td>
	<td>Afixa��o dos resultados dos recursos</td>
	</tr>
</table>
