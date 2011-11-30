<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<html:xhtml/>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>">Licenciaturas</a> &gt;
	Concurso Nacional de Acesso
</div>

<div id="contextual_nav">
<h2 class="brown">Nesta p&aacute;gina</h2>
	<ul>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#con">Ingresso pelo Concurso Nacional de Acesso</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#rec">Recursos</a></li>
		<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#doc">Documentos</a></li>
   </ul>
</div>
<h1>Candidato: Licenciatura (1� Ciclo)</h1>
<p>Os estudantes que pretendam candidatar-se ao IST com vista a obter uma forma��o superior dever�o optar por um dos seguintes percursos, consoante a �rea de estudos pretendida:</p>
<ul>
	<li>inscri��o em mestrado integrado (ciclo integrado), com a dura��o de 5 anos e atribui��o do grau de licenciado ao fim dos primeiros 3 anos;</li>

	<li>inscri��o em licenciatura (1� ciclo), com a dura��o de 3 anos, seguida da frequ�ncia de um mestrado (2� ciclo) de 2 anos, conducente ao grau de mestre.</li>
</ul>
<p>A forma��o em Arquitectura e em oito cursos de Engenharia est� organizada em mestrados integrados. As restantes forma��es em Engenharia e a forma��o em Matem�tica Aplicada seguem tamb�m um modelo de dois ciclos.</p>
<p>Nas Engenharias, ao fim dos 3 primeiros anos curriculares (180 ECTS), � conferido o grau de licenciado em Engenharia numa dada especialidade. Este grau atesta uma forte forma��o em ci�ncias b�sicas, como Matem�tica, F�sica, Qu�mica e Programa��o, e em ci�ncias b�sicas de engenharia numa dada �rea, para al�m de compet�ncias transversais, como comunica��o oral e escrita, gest�o, lideran�a, trabalho em equipa e empreendedorismo. Esta forma��o, n�o tendo objectivos profissionalizantes, confere um conjunto de compet�ncias que permitem a empregabilidade em algumas �reas do mercado de trabalho. A forma��o de 1� ciclo em Arquitectura segue o mesmo modelo conferindo o grau de Licenciado em Estudos de Arquitectura.</p>
<p>A forma��o completa do Engenheiro e Arquitecto, que capacita para a concep��o, a inova��o e o desenvolvimento de projectos complexos, s� � alcan�ada ao fim de 5 anos, com a aquisi��o de compet�ncias cient�ficas e tecnol�gicas avan�adas inerentes ao grau de mestre. A Ordem dos Engenheiros e a Ordem dos Arquitectos definiram como condi��o necess�ria para admiss�o nas mesmas a titularidade de uma forma��o superior de 5 anos.</p>
<h2 id="con">Ingresso pelo Concurso Nacional de Acesso</h2>
<h3>Ano Lectivo 2008/2009</h3>
<p>Ap�s a conclus�o do ensino secund�rio, os estudantes que pretendam obter uma forma��o superior no IST dever�o candidatar-se, atrav�s do Concurso Nacional de Acesso, ao ingresso num curso de 1� ciclo ou num curso de ciclo integrado, consoante a �rea de estudos pretendida. As candidaturas s�o formuladas junto dos Servi�os de Acesso do Minist�rio da Ci�ncia, Tecnologia e Ensino Superior, tendo em conta as condi��es de acesso definidas para cada curso.</p>
<h3 class="spaced">Cursos do 1� Ciclo</h3>

	<table class="tab_lay" width="99%" cellspacing="0" summary="Informa��o para o ingresso nos cursos do 1� Ciclo (ano lectivo 2007-2008)">
		<tr>
			<th>Cursos</th>
			<th>C�digo</th>
			<th>Campus</th>
			<th>Vagas</th>
			<th style="width: 25%;">Provas de ingresso</th>

		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/leamb?locale=pt">Licenciatura em Engenharia do Ambiente</a></td>
			<td>0807/9099</td>
			<td>Alameda</td>
			<td>35</td>
			<td>Matem�tica + F�sica e Qu�mica ou Matem&aacute;tica + Biologia e Geologia</td>

		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/lean?locale=pt">Licenciatura em Engenharia e Arquitectura Naval</a></td>
			<td>0807/9911</td>
			<td>Alameda</td>
			<td>10</td>
			<td>Matem�tica + F�sica e Qu�mica</td>

		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/lee?locale=pt">Licenciatura em Engenharia Electr�nica</a></td>
			<td>0808/9912</td>
			<td>Taguspark</td>			
			<td>33</td>
			<td>Matem�tica + F�sica e Qu�mica</td>

		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/legm?locale=pt">Licenciatura em Engenharia Geol�gica e de Minas</a></td>
			<td>0807/9913</td>
			<td>Alameda</td>
			<td>15</td>
			<td>Matem�tica + F�sica e Qu�mica ou Matem�tica + Biologia e Geologia</td>

		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/legi?locale=pt">Licenciatura em Engenharia e Gest�o Industrial</a></td>
			<td>0808/9104</td>
			<td>Taguspark</td>			
			<td>40</td>
			<td>Matem�tica + F�sica e Qu�mica</td>

		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/leic-a?locale=pt">Licenciatura em Engenharia Inform�tica e de Computadores</a></td>
			<td>0807/9121</td>
			<td>Alameda</td>
			<td>170</td>
			<td>Matem�tica + F�sica e Qu�mica ou Matem�tica + Biologia e Geologia</td>

		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/leic-t?locale=pt">Licenciatura em Engenharia Inform�tica e de Computadores</a></td>
			<td>0808/9121</td>
			<td>Taguspark</td>			
			<td>98</td>
			<td>Matem�tica + F�sica e Qu�mica ou Matem�tica + Biologia e Geologia</td>

		</tr>	
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/lemat?locale=pt">Licenciatura em Engenharia de Materiais</a></td>
			<td>0807/9096</td>
			<td>Alameda</td>
			<td>20</td>
			<td>Matem�tica + F�sica e Qu�mica ou Matem�tica + Biologia e Geologia</td>

		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/lerc?locale=pt">Licenciatura em Engenharia de Redes de Comunica��es</a></td>
			<td>0808/9746</td>
			<td>Taguspark</td>			
			<td>68</td>
			<td>Matem�tica + F�sica e Qu�mica ou Matem�tica + Biologia e Geologia</td>

		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/lmac?locale=pt">Licenciatura em Matem�tica Aplicada e Computa��o</a></td>
			<td>0807/9345</td>
			<td>Alameda</td>			
			<td>30</td>
			<td>Matem�tica + F�sica e Qu�mica ou Matem�tica + Biologia e Geologia</td>

		</tr>
</table>

<h3 class="spaced">Cursos Integrados</h3>
<table class="tab_lay" width="99%" cellspacing="0" summary="Informa��o para o ingresso nos cursos integrads (ano lectivo 2007-2008)">
		<tr>
			<th>Cursos</th>
			<th>C�digo</th>
			<th>Campus</th>

			<th style="white-space: nowrap;">Vagas</th>
			<th style="width: 25%;">Provas de ingresso</th>
		</tr>

		<tr>
			<td><a href="http://fenix.ist.utl.pt/ma?locale=pt">Mestrado em Arquitectura</a></td>
			<td>0807/9257</td>

			<td>Alameda</td>
			<td>50</td>
			<td>Matem�tica + Geometria Descritiva ou Desenho + Matem�tica ou Matem�tica</td>
		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/meaer?locale=pt">Mestrado em Engenharia Aeroespacial</a></td>
			<td>0807/9357</td>

			<td>Alameda</td>
			<td>65</td>
			<td>Matem�tica + F�sica e Qu�mica</td>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/mebiol?locale=pt">Mestrado em Engenharia Biol�gica</a></td>
			<td>0807/9358</td>

			<td>Alameda</td>
			<td>65</td>
			<td>Matem�tica + F�sica e Qu�mica ou Matem�tica + Biologia e Geologia</td>
		</tr>		
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/mebiom?locale=pt">Mestrado em Engenharia Biom�dica</a></td>
			<td>0807/9359</td>

			<td>Alameda</td>
			<td>50</td>
			<td>Matem�tica + F�sica e Qu�mica ou Matem�tica + Biologia e Geologia</td>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/mec?locale=pt">Mestrado em Engenharia Civil</a></td>
			<td>0807/9360</td>

			<td>Alameda</td>
			<td>185</td>
			<td>Matem�tica + F�sica e Qu�mica</td>
		</tr>		
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/meec?locale=pt">Mestrado em Engenharia Electrot�cnica e de Computadores</a></td>
			<td>0807/9367</td>

			<td>Alameda</td>
			<td>205</td>
			<td>Matem�tica + F�sica e Qu�mica</td>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/meft?locale=pt">Mestrado em Engenharia F�sica Tecnol�gica</a></td>
			<td>0807/9458</td>

			<td>Alameda</td>
			<td>60</td>
			<td>Matem�tica + F�sica e Qu�mica</td>
		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/memec?locale=pt">Mestrado em Engenharia Mec�nica</a></td>	
			<td>0807/9369</td>	
			<td>Alameda</td>	
			<td>165</td>	
			<td>Matem�tica + F�sica e Qu�mica</td>	
		</tr>

		<tr>
			<td><a href="http://fenix.ist.utl.pt/meq?locale=pt">Mestrado em Engenharia Qu�mica</a></td>
			<td>0807/9461</td>
			<td>Alameda</td>
			<td>70</td>
			<td>Matem�tica + F�sica e Qu�mica</td>

		</tr>
</table>
<h3>Classifica&ccedil;&otilde;es M&iacute;nimas de Acesso</h3>
<p>As condi&ccedil;&otilde;es exigidas para a candidatura aos Cursos ministrados no IST s&atilde;o as seguintes (expressas numa escala de 0 a 200 pontos):</p>
<ul>
	<li>Classifica&ccedil;&atilde;o m&iacute;nima de 100 em cada uma das provas de ingresso (exames nacionais do ensino secund&aacute;rio), exceptuando o curso de Licenciatura em Matem&aacute;tica Aplicada e Computa&ccedil;&atilde;o em que a classifica&ccedil;&atilde;o m&iacute;nima exigida &eacute; de 120, e;</li>

	<li>Classifica&ccedil;&atilde;o m&iacute;nima de 120 na nota de candidatura, exceptuando o curso de Licenciatura em Matem&aacute;tica Aplicada e Computa&ccedil;&atilde;o em que a classifica&ccedil;&atilde;o m&iacute;nima exigida &eacute; de 140. A nota de candidatura (NC) &eacute; calculada utilizando um peso de 50% para a classifica&ccedil;&atilde;o do Ensino Secund&aacute;rio (MS) e um peso de 50% para a classifica&ccedil;&atilde;o das provas de ingresso (PI). - F&oacute;rmula de C&aacute;lculo da Nota de Candidatura: NC = MS x 50% + PI x 50% (ou seja, m&eacute;dia aritm&eacute;tica da classifica&ccedil;&atilde;o final do Ensino Secund&aacute;rio e da classifica&ccedil;&atilde;o das provas de ingresso).</li>

</ul>
<p>Nota: n&atilde;o se exigem pr&eacute;-requisitos.</p>
<p>Mais informa��es no <a href="http://www.dges.mctes.pt/DGES/pt">website da Direc��o Geral do Ensino Superior (DGES)</a>.</p>
<div class="h_box">
<h2 id="rec">Recursos</h2>
<ul> 
	<li><a href="http://nape.ist.utl.pt/acesso/notas.php">Vagas e notas m&iacute;nimas de seria&ccedil;&atilde;o dos &uacute;ltimos 5 anos lectivos</a></li> 
	<li><a href="http://www.ist.utl.pt/html/campus/tagus/sat/">Servi&ccedil;os Administrativos do IST-Taguspark (SAT)</a></li> 
	<li><a href="http://guiatecnico.aeist.pt/">Guia do T&eacute;cnico </a> &nbsp;(Publica&ccedil;&atilde;o da responsabilidade da <a href="http://ae.ist.utl.pt/">Associa&ccedil;&atilde;o de Estudantes do IST</a>) </li> 
	<li><a href="http://guialisboa.aeist.pt/">Guia de Lisboa</a>&nbsp;(Publica&ccedil;&atilde;o da responsabilidade da <a href="http://ae.ist.utl.pt/">Associa&ccedil;&atilde;o de Estudantes do IST</a>) </li> 

</ul>	
</div>
<h2 id="doc">Documentos</h2>
<ul class="material">
	<li class="pdf"><a href="http://www.ist.utl.pt/files/ensino/reg_1e2ciclo_20082009.pdf">Regulamento do 1� e 2� ciclo para 2008/2009</a> (PDF, 826KB)</li>
	<li class="pdf"><a href="http://www.ist.utl.pt/files/ensino/propinas_20082009.pdf">Propinas do 1� e 2� ciclo para 2008/2009</a> (PDF, 28KB)</li>
</ul>
