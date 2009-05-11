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
	<bean:message key="title.application.name.secondCycle" bundle="CANDIDATE_RESOURCES"/>
</div>


<div id="contextual_nav">
<h2 class="brown">Nesta p�gina</h2>
	<ul>
    	<li><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#desc">Descri��o</a></li>
    	<li><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#vag">Vagas</a></li>
    	<li><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#prop">Propinas</a></li>
    	<li><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#praz">Prazos</a></li>
    	<li><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#critsel">Crit�rios de selec��o</a></li>
    	<li><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#docnec">Documentos necess�rios</a></li>
    	<li><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#emol">Emolumentos de candidatura</a></li>
    	<li><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#faq">FAQs</a></li>
    	<li><a href="<%= fullPath + "?method=preparePreCreationOfCandidacy" %>"><b>Submeter Candidatura</b></a></li>
    	<li><a href="<%= fullPath + "?method=prepareApplicationAccessRecovery" %>"><b>Recuperar Accesso</b></a></li>
   </ul>
</div>

<h1><bean:message key="title.application.name.secondCycle" bundle="CANDIDATE_RESOURCES"/></h1>


<h2 id="desc">Descri��o</h2>

<p>Podem candidatar-se a um Mestrado de 2� ciclo do IST, ou a um 2� ciclo de um Mestrado Integrado do IST, os estudantes que estejam nas seguintes condi��es:</p>

<ul>
	<li>tenham terminado no IST uma Licenciatura de 1� ciclo, ou o 1� ciclo de um Mestrado Integrado, sem <a href="http://www.ist.utl.pt/files/ensino/coerencias_cientificas_0910.pdf">coer�ncia cient�fica</a> com o curso de 2� ciclo a que se candidatam;</li>
	<li>sejam titulares de uma forma��o de 1� ciclo na �rea de Ci�ncias e Tecnologia (exceptua-se o caso do 2� ciclo em Arquitectura que pressup�e uma forma��o de 1� ciclo em Arquitectura);</li>
	<li>sejam detentores de um curr�culo escolar, cient�fico ou profissional, que ateste a sua capacidade para realiza��o do Mestrado a que se candidatam.</li>
</ul>

<p>Os candidatos que se encontrem a terminar no IST uma Licenciatura de 1� ciclo, ou o 1� ciclo de um Mestrado Integrado, poder�o submeter a sua candidatura nos prazos estipulados para o efeito, ficando esta condicionada ao t�rmino do referido ciclo at� ao dia 30 de Setembro de 2009.</p>

<p><a href="http://www.ist.utl.pt/files/ensino/coerencias_cientificas_0910.pdf">Tabela de Coer�ncia Cient�fica entre cursos do IST</a></p>

<h2 id="vag">Vagas 2� ciclo</h2>

	<table class="tab_lay" width="70%" cellspacing="0" summary="Informa��es sobre os mestrados (2� ciclo), no �mbito de Bolonha, dispon�veis no IST">
	<tr>	
		<th>Curso</th>

		<th>Campus</th>
		<th>Vagas</th>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/ma?locale=pt">Mestrado em Arquitectura</a></td>
		<td>Alameda</td>
		<td>5</td>
	</tr>
	<tr>
		<td><a href="https://fenix.ist.utl.pt/cursos/mbionano?locale=pt">Mestrado em Bioengenharia e Nanossistemas</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr class="bluecell">
		<td><a href="https://fenix.ist.utl.pt/cursos/mbiotec?locale=pt">Mestrado em Biotecnologia</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/meaer?locale=pt">Mestrado em Engenharia Aeroespacial</a></td>
		<td>Alameda</td>

		<td>15</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meamb?locale=pt">Mestrado em Engenharia do Ambiente</a></td>
		<td>Alameda</td>
		<td>15</td>
	</tr>

	<tr>
		<td><a href="http://fenix.ist.utl.pt/mean?locale=pt">Mestrado em Engenharia e Arquitectura Naval</a></td>
		<td>Alameda</td>
		<td>15</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/mebiol?locale=pt">Mestrado em Engenharia Biol�gica</a></td>

		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/mebiom?locale=pt">Mestrado em Engenharia Biom�dica</a></td>
		<td>Alameda</td>
		<td>15</td>

	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/mec?locale=pt">Mestrado em Engenharia Civil</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/mee?locale=pt">Mestrado em Engenharia Electr�nica</a></td>
		<td>Taguspark</td>
		<td>15</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meec?locale=pt">Mestrado em Engenharia Electrot�cnica e de Computadores</a></td>
		<td>Alameda</td>
		<td>90</td>
	</tr>
	<tr>
		<td><a href="https://fenix.ist.utl.pt/cursos/MEFarm?locale=pt">Mestrado em Engenharia Farmac&ecirc;utica</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meft?locale=pt">Mestrado em Engenharia F�sica Tecnol�gica</a></td>
		<td>Alameda</td>
		<td>30</td>
	</tr>
	<tr>

		<td><a href="http://fenix.ist.utl.pt/megm?locale=pt">Mestrado em Engenharia Geol�gica e de Minas</a></td>
		<td>Alameda</td>
		<td>15</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/megi?locale=pt">Mestrado em Engenharia e Gest�o Industrial</a></td>
		<td>Taguskpark</td>
		<td>30</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/meic-a?locale=pt">Mestrado em Engenharia Inform�tica e de Computadores</a></td>
		<td>Alameda</td>
		<td>30</td>
	</tr>

	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meic-t?locale=pt">Mestrado em Engenharia Inform�tica e de Computadores</a></td>
		<td>Taguspark</td>
		<td>20</td>
	</tr>
	<tr >
		<td><a href="http://fenix.ist.utl.pt/memat?locale=pt">Mestrado em Engenharia de Materiais</a></td>

		<td>Alameda</td>
		<td>15</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/memec?locale=pt">Mestrado em Engenharia Mec&acirc;nica</a></td>
		<td>Alameda</td>
		<td>35</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/meq?locale=pt">Mestrado em Eng&ordf; Qu&iacute;mica</a></td>
		<td>Alameda</td>
		<td>20</td>

	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/merc?locale=pt">Mestrado em Engenharia de Redes de Comunica&ccedil;&otilde;es</a></td>
		<td>Taguspark</td>
		<td>20</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/mma?locale=pt">Mestrado em Matem&aacute;tica e Aplica&ccedil;&otilde;es</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/mq?locale=pt">Mestrado em Qu&iacute;mica</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr>
		<td><a href="http://www.civil.ist.utl.pt/nispt/mit/ctis/">Mestrado em Sistemas Complexos de Infraestruturas de Transportes (MIT-Portugal)</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://www.civil.ist.utl.pt/?locale=pt">Mestrado em Urbanismo e Ordenamento do Territ�rio</a></td>
		<td>Alameda</td>
		<td>25</td>
	</tr>
</table>

<h2 id="prop">Propinas</h2>

<p>Para o ano lectivo de 2009/2010 a propina para os Mestrados de 2� ciclo e 2� ciclo de Mestrados Integrados est� prevista no valor de <b>996,85 Euros</b>, com excep��o do curso "Mestrados em Engenharia Farmac�utica" em que a propina est� fixada no valor de <b>5000 Euros</b>. Relativamente ao "Mestrado em Sistemas Complexos de Infra-estruturas de Transportes" � favor consultar o <a href="http://www.civil.ist.utl.pt/nispt/mit/ctis/">site pr�prio</a>.</p>


<h2 id="praz">Prazos</h2>
<table class="tab_simpler" summary="Prazos para candidatos do 2� ciclo" cellspacing="0">
	<tr>
		<td class="align_r"><span class="marker">4 de Maio a 15 de Junho de 2009</span></td>
		<td><a href="<%= fullPath + "?method=preparePreCreationOfCandidacy" %>">Apresenta��o de candidaturas</a></td>
	</tr>
	<tr>
		<td class="align_r"><span class="marker">29 de Junho de 2009</span></td>

		<td>Afixa��o dos editais de coloca��o</td>
	</tr>
	<tr>
		<td class="align_r"><span class="marker">29 a 3 de Julho de 2009</span></td>
		<td>Matr�cula e inscri��o</td>

	</tr>
	<tr>
		<td class="align_r"><span class="marker">29 a 3 de Julho de 2009</span></td>
		<td>Reclama��o sobre as coloca��es </td>
	</tr>
	<tr>
		<td class="align_r"><span class="marker">17 de Julho de 2009</span></td>
		<td>Decis�o sobre as reclama��es</td>
	</tr>
	<tr>
		<td class="align_r"><span class="marker">17 a 21 de Julho de 2009</span></td>
		<td>Matr�cula para reclama��es atendidas</td>
	</tr>
</table> 

<h2 id="critsel">Crit�rios de Selec��o</h2>

<p>Os candidatos a um Mestrado de 2� ciclo do IST, ou a um 2� ciclo de um Mestrado Integrado do IST, ser�o seriados pela coordena��o do curso a que se candidatam tendo em conta os seguintes crit�rios:</p>
<ol>
	<li>Afinidade entre o curso que possuem e o curso a que se candidatam;</li>
	<li>Natureza do grau que possuem;</li>

	<li>Sucesso escolar no curso que frequentam.</li>
</ol>
<p>Nos casos dos candidatos em que se considere que a forma��o de 1� ciclo n�o corresponde �s compet�ncias necess�rias para a forma��o a que se candidatam, poder� o j�ri de selec��o excluir o candidato ou propor a admiss�o condicionada � frequ�ncia e aprova��o num conjunto de unidades curriculares proped�uticas.</p>
<p>O conjunto de unidades curriculares proped�uticas nunca poder� exceder os 30 ECTS e a aprova��o nas mesmas condicionar� a conclus�o do curso. As classifica��es obtidas nestas unidades curriculares n�o ser�o contabilizadas para a classifica��o final do curso.</p>


<h2 id="docnec">Documentos necess�rios</h2>
<p>Para completar o processo de candidatura � necess�rio submeter os seguintes documentos digitalizados:</p>

<ul>
	<li>Foto actual</li>
	<li>Curriculum vitae</li>
	<li>Certificados de habilita��es discriminado com m�dia</li>
	<li>Documento de identifica��o</li>
	<li>Cart�o de contribuinte</li>
	<li>Comprovativo de pagamento dos emolumentos de candidatura</li>
</ul>


<h2 id="emol">Emolumentos de candidatura</h2>

<strong>Nome do banco:</strong> Millennium BCP<br />
<strong>NIB:</strong> 0033 0000 00007920342 05<br />

<strong>IBAN:</strong> PT50 0033 0000 00007920342 05<br />
<strong>SWIFT/BIC:</strong> BCOMPTPL<br />
<strong>Montante:</strong> 100 euros

<p/>


<h2 id="faq">FAQ</h2>

<h3>Q1: Sou finalista do 1� ciclo de um curso de Licenciatura (ou Mestrado Integrado) no IST. Terei que fazer alguma coisa para prosseguir os estudos no 2� ciclo do mesmo curso?</h3>
<p>A1: N�o, nestes casos a transi��o para o 2� ciclo � autom�tica.</p>

<h3>Q2: Sou finalista do 1� ciclo de um curso de Licenciatura (ou Mestrado Integrado) no IST. Em que condi��es a mudan�a para outro 2� ciclo diferente � autom�tica?</h3>
<p>A2: � sempre poss�vel mudar de curso e prosseguir estudos noutra �rea ap�s completar o 1� ciclo desde que esteja garantida a coer�ncia cient�fica entre os dois cursos do IST. Esta coer�ncia verifica-se sempre que as compet�ncias de forma��o do 1� ciclo respeitem as necessidades de forma��o para ingresso no 2� ciclo. A <a href="http://www.ist.utl.pt/files/ensino/coerencias_cientificas_0910.pdf">Tabela de Coer�ncia Cient�fica entre cursos do IST</a> mostra-te em que situa��es a mudan�a para um 2� ciclo diferente � autom�tica.</p>

<h3>Q3: Sou finalista do 1� ciclo de um curso de Licenciatura (ou Mestrado Integrado) no IST. O que devo fazer se pretender prosseguir os estudos num 2� ciclo do IST sem coer�ncia cient�fica com o curso de 1� ciclo?</h3>
<p>A3: Deves submeter um processo de candidatura ao 2� ciclo de acordo com os prazos e regulamento em vigor, sendo o processo analisado juntamente com as candidaturas de alunos externos que pretendem ingressar num curso de 2� ciclo do IST.</p>

<h3>Q4: Sou titular de uma licenciatura pr�-bolonha do IST. Posso candidatar-me a um mestrado de 2� ciclo, na mesma �rea de forma��o?</h3>
<p>A4: Sim. </p>

