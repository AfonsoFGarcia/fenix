<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<bean:write name="inquiryResult" />

<%--

<h2>Resultados do Inqu�rito</h2>

<div class="infoop2" style="font-size: 1.4em; padding: 0.5em 1em; margin: 1em 0;">
	<p style="margin: 0.75em 0;">2� semestre de 2007/2008</span></p>
	<p style="margin: 0.75em 0;">Ci�ncias de Engenharia - Engenharia Inform�tica e de Computadores - Alameda</span></p>
	<p style="margin: 0.75em 0;">Mec�nica e Ondas</p>
</div>


<table class="tstyle1 thlight thleft td50px thbgnone">
	<tr>
		<th>N� de inscritos:</th>
		<td>57</td>
	</tr>
	<tr>
		<th>Avaliados (%):</th>
		<td>98%</td>
	</tr>
	<tr>
		<th>Aprovados (%):</th>
		<td>95%</td>
	</tr>
	<tr>
		<th>M�dia notas:</th>
		<td>15</td>
	</tr>
	<tr>
		<th>Sujeita a inqu�rito:</th>
		<td>Sim</td>
	</tr>
</table>


<h3 class="mtop15 mbottom0"><strong>Estat�stica de preenchimento e representatividade</strong></h3>

<table class="tstyle1 thlight thleft td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">%</th>
	</tr>
	<tr>
		<th>Respostas validas quadro inicial:</th>
		<td>57</td>
		<td>88%</td>
	</tr>
	<tr>
		<th>Respostas validas inqu�rito � UC:</th>
		<td>52</td>
		<td>91%</td>
	</tr>
	<tr>
		<th>N�o respostas � UC:</th>
		<td>3</td>
		<td>5%</td>
	</tr>
	<tr>
		<th>Respostas invalidas inqu�rito � UC:</th>
		<td>0</td>
		<td>0%</td>
	</tr>
</table>
								

<table class="tstyle1 thlight thleft tdcenter">
	<tr>
		<th></th>
		<th class="acenter">Respons�veis pela gest�o acad�mica</th>
		<th class="acenter">Comunidade acad�mica IST</th>
	</tr>
	<tr>
		<th>Representatividade para divulga��o:</th>
		<td>Sim</td>
		<td>N�o</td>
	</tr>
</table>


<table class="tstyle1 thlight thleft tdcenter">
	<tr>
		<th></th>
		<th class="acenter">Organiza��o da UC</th>
		<th class="acenter">Avalia��o da UC</th>
		<th class="acenter">Pass�vel de Auditoria</th>
	</tr>
	<tr>
		<th>Resultados a melhorar:</th>
		<td>Sim</td>
		<td>Sim</td>
		<td>Sim</td>
	</tr>
</table>


<h3 class="mtop15 mbottom0"><strong>Acompanhamento e carga de trabalho da UC ao longo do semestre</strong></h3>

<table class="tstyle1 thlight thleft td50px">
	<tr>
		<th>Carga Hor�ria da UC:</th>
		<td>-</td>
	</tr>
	<tr>
		<th>N� ECTS da UC:</th>
		<td>-</td>
	</tr>
</table>

<h3 class="mtop15 mbottom0"><strong>Auto-avalia��o dos alunos</strong></h3>

<table class="tstyle1 thlight thleft tdcenter td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">M�dia</th>
		<th class="acenter">Desvio padr�o</th>
	</tr>
	<tr>
		<th>N� m�dio de horas de trabalho aut�nomo por semana com a UC:</th>
		<td>48</td>
		<td>6.6</td>
		<td>2.4</td>
	</tr>
	<tr>
		<th>N� de dias de estudo da UC na �poca de exames:</th>
		<td>48</td>
		<td>2.2</td>
		<td>2.9</td>
	</tr>
	<tr>
		<th>N� m�dio ECTS estimado:</th>
		<td colspan="3">48</td>
	</tr>
</table>


<table class="tstyle1 thlight thleft tdcenter td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">[10; 12]</th>
		<th class="acenter">[13; 14]</th>
		<th class="acenter">[15; 16]</th>
		<th class="acenter">[17; 18]</th>
		<th class="acenter">[19; 20]</th>
		<th class="acenter">Reprovado</th>
		<th class="acenter">N�o avaliado</th>
	</tr>
	<tr>
		<th>Gama de valores da classifica��o dos alunos:</th>
		<td>47</td>
		<td>17%</td>
		<td>19%</td>
		<td>43%</td>
		<td>17%</td>
		<td>2%</td>
		<td>2%</td>
		<td>0%</td>
	</tr>
</table>



<p class="mtop15 mbottom0"><strong>Carga de trabalho elevada devido a</strong></p>

<table class="tstyle1 thlight thleft td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">%</th>
	</tr>
	<tr>
		<th>Trabalhos/projectos complexos:</th>
		<td>19</td>
		<td>39%</td>
	</tr>
	<tr>
		<th>Trabalhos/projectos extensos:</th>
		<td>28</td>
		<td>57%</td>
	</tr>
	<tr>
		<th>Trabalhos/projectos em n�mero elevado:</th>
		<td>11</td>
		<td>22%</td>
	</tr>
	<tr>
		<th>Falta de prepara��o anterior exigindo mais trabalho/estudo:</th>
		<td>8</td>
		<td>16%</td>
	</tr>
	<tr>
		<th>Extens�o do programa face ao n� de aulas previstas:</th>
		<td>1</td>
		<td>2%</td>
	</tr>
	<tr>
		<th>Pouco acompanhamento das aulas ao longo do semestre:</th>
		<td>3</td>
		<td>6%</td>
	</tr>
	<tr>
		<th>Outras raz�es:</th>
		<td>3</td>
		<td>6%</td>
	</tr>
</table>

<table class="tstyle1 thlight thleft tdcenter td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">M�dia</th>
		<th class="acenter">Desvio padr�o</th>
		<th class="acenter">Discordo totalmente<br/>1</th>
		<th class="acenter">2</th>
		<th class="acenter">Discordo<br/>3</th>
		<th class="acenter">4</th>
		<th class="acenter">N�o concordo nem discordo<br/>5</th>
		<th class="acenter">6</th>
		<th class="acenter">Concordo<br/>7</th>
		<th class="acenter">8</th>
		<th class="acenter">Concordo totalmente<br/>9</th>
	</tr>
	<tr>
		<th>Conhecimentos anteriores suficientes para o acompanhamento da UC:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
</table>


<table class="tstyle1 thlight thleft tdcenter td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">M�dia</th>
		<th class="acenter">Desvio padr�o</th>
		<th class="acenter">Passiva<br/>1</th>
		<th class="acenter">Activa quando solicitada<br/>2</th>
		<th class="acenter">Activa por iniciativa pr�pria<br/>3</th>
	</tr>
	<tr>
		<th>Participa��o dos alunos na UC:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
</table>


<p class="mtop15 mbottom0"><strong>A UC contribuiu para a aquisi��o e/ou desenvolvimento das seguintes compet�ncias</strong></p>

<table class="tstyle1 thlight thleft tdcenter td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">M�dia</th>
		<th class="acenter">Desvio padr�o</th>
		<th class="acenter">N�o sabe / N�o responde / N�o aplic�vel</th>
		<th class="acenter">N�o contribuiu<br/>1</th>
		<th class="acenter">Contribuiu<br/>2</th>
		<th class="acenter">Contribuiu muito<br/>3</th>
	</tr>
	<tr>
		<th>Conhecimento e compreens�o do tema da UC:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
	<tr>
		<th>Aplica��o do conhecimento sobre o tema da UC:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
	<tr>
		<th>Sentido cr�tico e esp�rito reflexivo:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
	<tr>
		<th>Capacidade de coopera��o e comunica��o:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
</table>


<p class="mtop15 mbottom0"><strong>Organiza��o da UC</strong></p>

<table class="tstyle1 thlight thleft tdcenter td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">M�dia</th>
		<th class="acenter">Desvio padr�o</th>
		<th class="acenter">Discordo totalmente<br/>1</th>
		<th class="acenter">2</th>
		<th class="acenter">Discordo<br/>3</th>
		<th class="acenter">4</th>
		<th class="acenter">N�o concordo nem discordo<br/>5</th>
		<th class="acenter">6</th>
		<th class="acenter">Concordo<br/>7</th>
		<th class="acenter">8</th>
		<th class="acenter">Concordo totalmente<br/>9</th>
	</tr>
	<tr>
		<th>O programa previsto foi leccionado:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
	<tr>
		<th>A UC encontra-se bem estruturada:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
	<tr>
		<th>A bibliografia foi importante:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
	<tr>
		<th>Os materiais de apoio foram bons:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
</table>


<p class="mtop15 mbottom0"><strong>M�todo de avalia��o da UC</strong></p>

<table class="tstyle1 thlight thleft tdcenter td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">M�dia</th>
		<th class="acenter">Desvio padr�o</th>
		<th class="acenter">Discordo totalmente<br/>1</th>
		<th class="acenter">2</th>
		<th class="acenter">Discordo<br/>3</th>
		<th class="acenter">4</th>
		<th class="acenter">N�o concordo nem discordo<br/>5</th>
		<th class="acenter">6</th>
		<th class="acenter">Concordo<br/>7</th>
		<th class="acenter">8</th>
		<th class="acenter">Concordo totalmente<br/>9</th>
	</tr>
	<tr>
		<th>Os m�todos de avalia��o foram justos e apropriados:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
</table>


<p class="mtop15 mbottom0"><strong>Avalia��o global da UC</strong></p>

<table class="tstyle1 thlight thleft tdcenter td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">M�dia</th>
		<th class="acenter">Desvio padr�o</th>
		<th class="acenter">Discordo totalmente<br/>1</th>
		<th class="acenter">2</th>
		<th class="acenter">Discordo<br/>3</th>
		<th class="acenter">4</th>
		<th class="acenter">N�o concordo nem discordo<br/>5</th>
		<th class="acenter">6</th>
		<th class="acenter">Concordo<br/>7</th>
		<th class="acenter">8</th>
		<th class="acenter">Concordo totalmente<br/>9</th>
	</tr>
	<tr>
		<th>Avalia��o do funcionamento da UC:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
</table>

--%>