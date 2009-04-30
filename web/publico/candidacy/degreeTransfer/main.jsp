<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<!--LANGUAGE SWITCHER -->
<div id="version">
	<img class="activeflag" src="Candidato%20%20Licenciatura%20_%20IST_files/icon_pt.gif" alt="Portugu�s">
	<a href="http://www.ist.utl.pt/en/htm/profile/pstudent/lic/"><img src="Candidato%20%20Licenciatura%20_%20IST_files/icon_en.gif" alt="English" border="0"></a>
</div>
<!--END LANGUAGE SWITCHER -->
<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="#">IST</a> &gt;
	<a href="#">Candidato</a> &gt;
	<a href="#">Candidaturas</a> &gt;
	<a href='<%= fullPath + "?method=candidacyIntro" %>'>Licenciaturas</a> &gt;
	Transfer�ncias
</div>


<div id="contextual_nav">
<h2 class="brown">Nesta p�gina</h2>
	<ul>
    	<li><a href="#1">Crit�rios de selec��o</a></li>
    	<li><a href="#2">Vagas</a></li>
    	<li><a href="#3">Propinas</a></li>
    	<li><a href="#4">Prazos</a></li>
		<li><a href="#5">FAQs</a></li>
    	<li><a href="<%= fullPath + "?method=preparePreCreationOfCandidacy" %>"><b>Submeter Candidatura</b></a></li>
    	<li><a href="<%= fullPath + "?method=prepareAutheticationCandidacyRequest" %>"><b>Consultar Candidatura</b></a></li>
   </ul>
</div>


<h1>Candidatura: Transfer�ncias</h1>


<p>O regime de ingresso por transfer�ncia destina-se aos estudantes que estando ou
tendo estado inscritos no 1� ciclo de um curso de Licenciatura ou de um Mestrado
Integrado pretendam inscrever-se no mesmo curso mas numa institui��o diferente
daquela em que praticaram a �ltima inscri��o. A �ltima inscri��o pode ter ocorrido
numa escola nacional ou estrangeira.</p>


<h2 id="1">Crit�rios de selec��o</h2>
<p>Os candidatos a Transfer�ncias ser�o seriados pela coordena��o do curso a que se candidatam tendo em conta os seguintes crit�rios:</p>
<ul>
	<li>Afinidade entre o curso que possuem e o curso a que se candidatam;</li>
	<li>Natureza do grau que possuem;</li>
	<li>Sucesso escolar no curso que frequentam/frequentaram.</li>
</ul>


<h2 id="2">Vagas</h2>

<table class="tab_lay" width="99%" cellspacing="0" summary="Informações sobre vagas por licenciatura para candidatos titulares de Curso Médio e Superior ">
	<tr>
		<th>Cursos</th>
		<th>Campus</th>
		<th style="text-align: center;">Transfer&ecirc;ncias</th>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/leamb?locale=pt">Licenciatura em Engenharia do Ambiente</a></td>
		<td>Alameda</td>
		<td class="center">1</td>
	</tr>	
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/lean?locale=pt">Licenciatura em Engenharia e Arquitectura Naval</a></td>
		<td>Alameda</td>
		<td class="center">1</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/lee?locale=pt">Licenciatura em Engenharia Electr&oacute;nica</a></td>
		<td>Taguspark</td>
		<td class="center">1</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/legm?locale=pt">Licenciatura em Engenharia Geol&oacute;gica e de Minas</a></td>
		<td>Alameda</td>
		<td class="center">1</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/legi?locale=pt">Licenciatura em Engenharia e Gest&atilde;o Industrial</a></td>
		<td>Taguspark</td>
		<td class="center">1</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/leic-a?locale=pt">Licenciatura em Engenharia Inform&aacute;tica e de Computadores</a></td>
		<td>Alameda</td>
		<td class="center">2</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/leic-t?locale=pt">Licenciatura em Engenharia Inform&aacute;tica e de Computadores</a></td>
		<td>Taguspark</td>
		<td class="center">1</td>
	</tr>	
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/lemat?locale=pt">Licenciatura em Engenharia de Materiais</a></td>
		<td>Alameda</td>
		<td class="center">1</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/lerc?locale=pt">Licenciatura em Engenharia de Redes de Comunica&ccedil;&otilde;es</a></td>
		<td>Taguspark</td>
		<td class="center">1</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/lmac?locale=pt">Licenciatura em Matem&aacute;tica Aplicada e Computa&ccedil;&atilde;o</a></td>
		<td>Alameda</td>
		<td class="center">1</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/ma?locale=pt">Mestrado em Arquitectura</a></td>
		<td>Alameda</td>
		<td class="center">1</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meaer?locale=pt">Mestrado em Engenharia Aeroespacial</a></td>
		<td>Alameda</td>
		<td class="center">1</td>
	</tr>	
	<tr>
		<td><a href="http://fenix.ist.utl.pt/mebiol?locale=pt">Mestrado em Engenharia Biol&oacute;gica</a></td>
		<td>Alameda</td>
		<td class="center">1</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/mebiom?locale=pt">Mestrado em Engenharia Biom&eacute;dica</a></td>
		<td>Alameda</td>
		<td class="center">1</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/mec?locale=pt">Mestrado em Engenharia Civil</a></td>
		<td>Alameda</td>
		<td class="center">2</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meec?locale=pt">Mestrado em Engenharia Electrot&eacute;cnica e de Computadores</a></td>
		<td>Alameda</td>
		<td class="center">3</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/meft?locale=pt">Mestrado em Engenharia F&iacute;sica Tecnol&oacute;gica</a></td>
		<td>Alameda</td>
		<td class="center">1</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/memec?locale=pt">Mestrado em Engenharia Mec&acirc;nica</a></td>
		<td>Alameda</td>
		<td class="center">2</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/meq?locale=pt">Mestrado em Engenharia Qu&iacute;mica</a></td>
		<td>Alameda</td>
		<td class="center">1</td>
	</tr>
</table>




<h2 id="3">Propinas</h2>
<p>Para o ano lectivo de 2008/2009 a propina para as Licenciaturas de 1� ciclo e 1� ciclo de Mestrados Integrados foi fixada no valor de 972,14 �.</p>


<h2 id="4">Prazos</h2>

<table class="tab_simpler" summary="Prazos para candidatos em mudança ou transferência de curso" cellspacing="0">
	<tr>
		<td class="align_r"><span class="marker">1 a 29 de Agosto de 2008</span></td>
		<td><a href="#">Apresenta��o de candidaturas</a></td>
	</tr>
	<tr>
		<td class="align_r"><span class="marker">16 de Setembro de 2008</span></td>
		<td>Afixa��o dos editais de coloca��o</td>
	</tr>

	<tr>
		<td class="align_r"><span class="marker">16 a 23 de Setembro de 2008</span></td>
		<td>Matr�cula e inscri��o</td>
	</tr>
	<tr>
		<td class="align_r"><span class="marker">16 a 23 de Setembro de 2008</span></td>
		<td>Reclama��o sobre as coloca��es</td>

	</tr>
	<tr>
		<td class="align_r"><span class="marker">16 de Outubro de 2008</span></td>
		<td>Decis�o sobre as reclama��es</td>
	</tr>
	<tr>
		<td class="align_r"><span class="marker">23 de Outubro de 2008</span></td>
		<td>Matr�cula para reclama��es atendidas</td>
	</tr>
</table>



<h2 id="5">FAQ's</h2>
<p>Em falta.</p>
