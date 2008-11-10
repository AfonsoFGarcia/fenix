<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT" xml:lang="pt-PT">
<head>
	<title>.IST</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/layout.css"  media="screen"  />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/general.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/color.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/print.css" media="print" />
	
</head>

<body class="survey">

<style>
body.survey {
background: #fff;
margin: 2em;
font-size: 70%;
}
.acenter { text-align: center !important; }
th:first-child {
width: 250px;
}
body.survey table {
}
body.survey table th {
vertical-align: bottom;
}
body.survey table td {
text-align: center;
}
table.td50px td {
width: 60px;
}
body.survey table {
border-top: 4px solid #ddd;
}

</style>

<h2>Resultados do Inqu�rito</h2>

<div class="infoop2" style="font-size: 1.4em; padding: 0.5em 1em; margin: 1em 0;">
	<p style="margin: 0.75em 0;"><bean:write name="inquiryResult" property="executionCourse.executionPeriod.name"/> <bean:write name="inquiryResult" property="executionCourse.executionYear.name"/></span></p>
	<p style="margin: 0.75em 0;"><bean:write name="inquiryResult" property="executionDegree.degree.presentationName"/></span></p>
	<p style="margin: 0.75em 0;"><bean:write name="inquiryResult" property="executionCourse.nome"/></p>
</div>


<table class="tstyle1 thlight thleft td50px thbgnone">
	<tr>
		<th>N� de inscritos:</th>
		<td><c:out value="${inquiryResult.numberOfEnrolled}" /></td>
	</tr>
	<tr>
		<th>Avaliados (%):</th>
		<td><c:out value="${inquiryResult.evaluatedRatio * 100} %" /></td>
	</tr>
	<tr>
		<th>Aprovados (%):</th>
		<td><c:out value="${inquiryResult.approvedRatio * 100} %" /></td>
	</tr>
	<tr>
		<th>M�dia notas:</th>
		<td><c:out value="${inquiryResult.gradeAverage}" /></td>
	</tr>
	<tr>
		<th>Sujeita a inqu�rito:</th>
		<td><c:out value="${inquiryResult.availableToInquiry}" /></td>
	</tr>
</table>

<logic:equal name="inquiryResult" property="availableToInquiry" value="true">
	<h3 class="mtop15 mbottom0"><strong>Estat�stica de preenchimento e representatividade</strong></h3>
	
	<table class="tstyle1 thlight thleft td50px">
		<tr>
			<th></th>
			<th class="acenter">N</th>
			<th class="acenter">%</th>
		</tr>
		<tr>
			<th>Respostas validas quadro inicial:</th>
			<td><c:out value="${inquiryResult.validInitialFormAnswersNumber}" /></td>
			<td><c:out value="${inquiryResult.validInitialFormAnswersRatio * 100} %" /></td>
		</tr>
		<tr>
			<th>Respostas validas inqu�rito � UC:</th>
			<td><c:out value="${inquiryResult.validInquiryAnswersNumber}" /></td>
			<td><c:out value="${inquiryResult.validInquiryAnswersRatio * 100} %" /></td>
		</tr>
		<tr>
			<th>N�o respostas � UC:</th>
			<td><c:out value="${inquiryResult.noInquiryAnswersNumber}" /></td>
			<td><c:out value="${inquiryResult.noInquiryAnswersRatio * 100} %" /></td>
		</tr>
		<tr>
			<th>Respostas invalidas inqu�rito � UC:</th>
			<td><c:out value="${inquiryResult.invalidInquiryAnswersNumber}" /></td>
			<td><c:out value="${inquiryResult.invalidInquiryAnswersRatio * 100} %" /></td>
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
			<td><c:out value="${inquiryResult.internalDisclosure}" /></td>
			<td><c:out value="${inquiryResult.publicDisclosure}" /></td>
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
			<td><c:out value="${inquiryResult.unsatisfactoryResultsCUOrganization}" /></td>
			<td><c:out value="${inquiryResult.unsatisfactoryResultsCUEvaluation}" /></td>
			<td><c:out value="${inquiryResult.auditCU}" /></td>
		</tr>
	</table>
</logic:equal>
<logic:notEqual name="inquiryResult" property="availableToInquiry" value="true">
TEXTO 2
</logic:notEqual>


<logic:equal name="inquiryResult" property="internalDisclosure" value="true">
	
	<h3 class="mtop15 mbottom0"><strong>Acompanhamento e carga de trabalho da UC ao longo do semestre</strong></h3>
	
	<table class="tstyle1 thlight thleft td50px">
		<tr>
			<th>Carga Hor�ria da UC:</th>
			<td><c:out value="${inquiryResult.scheduleLoad}" /></td>
		</tr>
		<tr>
			<th>N� ECTS da UC:</th>
			<td><c:out value="${inquiryResult.ects}" /></td>
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
			<td><c:out value="${inquiryResult.number_perc_NHTA}" /></td>
			<td><c:out value="${inquiryResult.average_perc_weeklyHours}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_perc_NHTA}" /></td>
		</tr>
		<tr>
			<th>N� de dias de estudo da UC na �poca de exames:</th>
			<td><c:out value="${inquiryResult.number_NDE}" /></td>
			<td><c:out value="${inquiryResult.average_NDE}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_NDE}" /></td>
		</tr>
		<tr>
			<th>N� m�dio ECTS estimado:</th>
			<td><c:out value="${inquiryResult.estimatedEctsNumber}" /></td>
			<td><c:out value="${inquiryResult.estimatedEctsAverage}" /></td>
			<td><c:out value="${inquiryResult.estimatedEctsStandardDeviation}" /></td>
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
			<td><c:out value="${inquiryResult.number_P1_1 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_10_12 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_13_14 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_15_16 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_17_18 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_19_20 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_flunked * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_nonEvaluated * 100} %" /></td>
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
			<td><c:out value="${inquiryResult.number_P1_2_a}" /></td>
			<td><c:out value="${inquiryResult.perc__P1_2_a * 100} %" /></td>
		</tr>
		<tr>
			<th>Trabalhos/projectos extensos:</th>
			<td><c:out value="${inquiryResult.number_P1_2_b}" /></td>
			<td><c:out value="${inquiryResult.perc__P1_2_b * 100} %" /></td>
		</tr>
		<tr>
			<th>Trabalhos/projectos em n�mero elevado:</th>
			<td><c:out value="${inquiryResult.number_P1_2_c}" /></td>
			<td><c:out value="${inquiryResult.perc__P1_2_c * 100} %" /></td>
		</tr>
		<tr>
			<th>Falta de prepara��o anterior exigindo mais trabalho/estudo:</th>
			<td><c:out value="${inquiryResult.number_P1_2_d}" /></td>
			<td><c:out value="${inquiryResult.perc__P1_2_d * 100} %" /></td>
		</tr>
		<tr>
			<th>Extens�o do programa face ao n� de aulas previstas:</th>
			<td><c:out value="${inquiryResult.number_P1_2_e}" /></td>
			<td><c:out value="${inquiryResult.perc__P1_2_e * 100} %" /></td>
		</tr>
		<tr>
			<th>Pouco acompanhamento das aulas ao longo do semestre:</th>
			<td><c:out value="${inquiryResult.number_P_1_2_f}" /></td>
			<td><c:out value="${inquiryResult.perc__P1_2_f * 100} %" /></td>
		</tr>
		<tr>
			<th>Outras raz�es:</th>
			<td><c:out value="${inquiryResult.number_P1_2_g}" /></td>
			<td><c:out value="${inquiryResult.perc__P1_2_g * 100} %" /></td>
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
			<td><c:out value="${inquiryResult.number_P1_3}" /></td>
			<td><c:out value="${inquiryResult.average_P1_3}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P1_3}" /></td>
			<td><c:out value="${inquiryResult.perc_P1_3_1 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P1_3_2 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P1_3_3 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P1_3_4 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P1_3_5 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P1_3_6 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P1_3_7 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P1_3_8 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P1_3_9 * 100} %" /></td>
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
			<td><c:out value="${inquiryResult.number_P1_4}" /></td>
			<td><c:out value="${inquiryResult.average_P1_4}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P1_4}" /></td>
			<td><c:out value="${inquiryResult.perc_P1_4_1 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P1_4_2 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P1_4_3 * 100} %" /></td>
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
			<td><c:out value="${inquiryResult.number_P2_1}" /></td>
			<td><c:out value="${inquiryResult.average_P2_1}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P2_1}" /></td>
			<td><c:out value="${inquiryResult.perc_P2_1_0 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P2_1_1 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P2_1_2 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P2_1_3 * 100} %" /></td>
		</tr>
		<tr>
			<th>Aplica��o do conhecimento sobre o tema da UC:</th>
			<td><c:out value="${inquiryResult.number_P2_2}" /></td>
			<td><c:out value="${inquiryResult.average_P2_2}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P2_2}" /></td>
			<td><c:out value="${inquiryResult.perc_P2_2_0 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P2_2_1 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P2_2_2 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P2_2_3 * 100} %" /></td>
		</tr>
		<tr>
			<th>Sentido cr�tico e esp�rito reflexivo:</th>
			<td><c:out value="${inquiryResult.number_P2_3}" /></td>
			<td><c:out value="${inquiryResult.average_P2_3}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P2_3}" /></td>
			<td><c:out value="${inquiryResult.perc_P2_3_0 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P2_3_1 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P2_3_2 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P2_3_3 * 100} %" /></td>
		</tr>
		<tr>
			<th>Capacidade de coopera��o e comunica��o:</th>
			<td><c:out value="${inquiryResult.number_P2_4}" /></td>
			<td><c:out value="${inquiryResult.average_P2_4}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P2_4}" /></td>
			<td><c:out value="${inquiryResult.perc_P2_4_0 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P2_4_1 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P2_4_2 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P2_4_3 * 100} %" /></td>
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
			<td><c:out value="${inquiryResult.number_P3_1}" /></td>
			<td><c:out value="${inquiryResult.average_P3_1}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P3_1}" /></td>
			<td><c:out value="${inquiryResult.perc_P3_1_1 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_1_2 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_1_3 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_1_4 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_1_5 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_1_6 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_1_7 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_1_8 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_1_9 * 100} %" /></td>
		</tr>
		<tr>
			<th>A UC encontra-se bem estruturada:</th>
			<td><c:out value="${inquiryResult.number_P3_2}" /></td>
			<td><c:out value="${inquiryResult.average_P3_2}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P3_2}" /></td>
			<td><c:out value="${inquiryResult.perc_P3_2_1 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_2_2 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_2_3 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_2_4 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_2_5 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_2_6 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_2_7 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_2_8 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_2_9 * 100} %" /></td>
		</tr>
		<tr>
			<th>A bibliografia foi importante:</th>
			<td><c:out value="${inquiryResult.number_P3_3}" /></td>
			<td><c:out value="${inquiryResult.average_P3_3}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P3_3}" /></td>
			<td><c:out value="${inquiryResult.perc_P3_3_1 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_3_2 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_3_3 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_3_4 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_3_5 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_3_6 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_3_7 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_3_8 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_3_9 * 100} %" /></td>
		</tr>
		<tr>
			<th>Os materiais de apoio foram bons:</th>
			<td><c:out value="${inquiryResult.number_P3_4}" /></td>
			<td><c:out value="${inquiryResult.average_P3_4}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P3_4}" /></td>
			<td><c:out value="${inquiryResult.perc_P3_4_1 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_4_2 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_4_3 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_4_4 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_4_5 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_4_6 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_4_7 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_4_8 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P3_4_9 * 100} %" /></td>
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
			<td><c:out value="${inquiryResult.number_P4}" /></td>
			<td><c:out value="${inquiryResult.average_P4}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P4}" /></td>
			<td><c:out value="${inquiryResult.perc_P4_1 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P4_2 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P4_3 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P4_4 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P4_5 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P4_6 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P4_7 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P4_8 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P4_9 * 100} %" /></td>
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
			<td><c:out value="${inquiryResult.number_P5}" /></td>
			<td><c:out value="${inquiryResult.average_P5}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P5}" /></td>
			<td><c:out value="${inquiryResult.perc_P5_1 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P5_2 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P5_3 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P5_4 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P5_5 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P5_6 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P5_7 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P5_8 * 100} %" /></td>
			<td><c:out value="${inquiryResult.perc_P5_9 * 100} %" /></td>
		</tr>
	</table>
</logic:equal>

<logic:notEqual name="inquiryResult" property="internalDisclosure" value="true">
TEXTO 3
</logic:notEqual>


</body>
</html>
