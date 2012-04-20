<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT" xml:lang="pt-PT">
<head>
	<title>.IST</title>
	<meta http-equiv="Content-Type" content="text/html; charset=<%= net.sourceforge.fenixedu._development.PropertiesManager.DEFAULT_CHARSET %>" />
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
.aright { text-align: right !important; }
.aleft { text-align: left !important; }
th:first-child {
width: 250px;
}
body.survey table { }
body.survey table th { vertical-align: bottom; }
body.survey table td { text-align: center; }
table.td50px td { width: 60px; }
table tr.top th { border-top: 4px solid #ddd; }
table tr.top td { border-top: 4px solid #ddd; }
body.survey table { }
.thtop th { vertical-align: top; }
.vatop { vertical-align: top !important; }
.vamiddle { vertical-align: middle !important; }
.tdright td { text-align: right !important; }

a {
color: #105c93;
}
a.help, a.helpleft {
position: relative;
text-decoration: none;
/*color: black !important;*/
border: none !important;
width: 20px;
}
a.help span, a.helpleft span { display: none; }
a.help:hover, a.helpleft:hover {
/*background: none;*/ /* IE hack */
z-index: 100;
border-bottom-width: 1px;
border-bottom-style: solid;
border-bottom-color: transparent;
}
a.help:hover span {
display: block !important;
display: inline-block;
width: 250px;
position: absolute;
top: 10px;
left: 30px;
text-align: left;
padding: 7px 10px;
background: #48869e;
color: #fff;
border: 3px solid #97bac6;
/*cursor: help;*/
}
a.helpleft:hover span {
display: block !important;
display: inline-block;
width: 250px;
position: absolute;
top: 10px;
left: -280px;
text-align: left;
padding: 7px 10px;
background: #48869e;
color: #fff;
border: 3px solid #97bac6;
/*cursor: help;*/
}
a.helpleft[class]:hover span {
right: 20px;
}

table th.separatorright {
border-right: 3px solid #ddd;
padding-right: 8px;
}
table td.separatorright {
border-right: 3px solid #ddd;
padding-right: 8px;
}
</style>

<p class="mtop0" style="float: right;"><em>Informa��o do sistema, recolhida a 5 Novembro 2008</em></p>

<h2>QUC - Garantia da Qualidade das UC - Resultados dos inqu�ritos aos alunos</h2>

<div class="infoop2" style="font-size: 1.4em; padding: 0.5em 1em; margin: 1em 0;">
	<p style="margin: 0.75em 0;">Semestre: 
		<bean:write name="inquiryResult" property="executionCourse.executionPeriod.semester"/>
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" locale="pt_PT" key="public.degree.information.label.ordinal.semester.abbr" />
		<bean:message bundle="APPLICATION_RESOURCES" locale="pt_PT" key="of" /> 
		<bean:write name="inquiryResult" property="executionCourse.executionYear.name"/></span></p>
	<p style="margin: 0.75em 0;">Curso: <bean:write name="inquiryResult" property="executionDegree.degree.presentationName"/></span></p>
	<p style="margin: 0.75em 0;">Unidade curricular: <bean:write name="inquiryResult" property="executionCourse.nome"/></p>
</div>

<bean:define id="result" name="inquiryResult" type="net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult" />
<table class="tstyle1 thlight thleft td50px thbgnone tdright">
	<tr class="top">
		<th>N� de inscritos</th>
		<td><c:out value="${inquiryResult.numberOfEnrolled}" /></td>
	</tr>
	<tr>
		<th>Avaliados <a href="#" class="helpleft">[?] <span>N� avaliados / N� inscritos. N�o s�o contabilizados resultados de �pocas especiais e/ou melhorias.</span></a></th>
		<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.evaluatedRatioForPresentation}" /></td>
	</tr>
	<tr>
		<th>Aprovados <a href="#" class="helpleft">[?] <span>N� aprovados / N� avaliados . N�o s�o contabilizados resultados de �pocas especiais e/ou melhorias.</span></a></th>
		<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.approvedRatioForPresentation}" /></td>
	</tr>
	<tr>
		<th>M�dia notas <a href="#" class="helpleft">[?] <span>N�o s�o contabilizados resultados de �pocas especiais e/ou melhorias.</span></a></th>
		<td><c:out value="${inquiryResult.gradeAverageForPresentation}" /></td>
	</tr>
	<tr>
		<th>Sujeita a inqu�rito <a href="#" class="helpleft">[?] <span>Algumas UC n�o foram sujeitas a inqu�rito, para mais informa��es ver regulamento QUC e FAQ's em http://quc.ist.utl.pt</span></a></th>
		<td><bean:message key="<%= "label." + result.getAvailableToInquiry().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
	</tr>
</table>
<em>(informa��o do sistema)</em>

<logic:equal name="inquiryResult" property="availableToInquiry" value="true">
	<h3 class="mtop15 mbottom0"><strong>Estat�stica de preenchimento e representatividade</strong></h3>
	
	<table class="tstyle1 thlight thleft td50px tdright">
		<tr class="top">
			<th></th>
			<th class="aright">N</th>
			<th class="aright">%</th>
		</tr>
		<tr>
			<th>Respostas v�lidas quadro inicial (carga de trabalho)</th>
			<td><fmt:formatNumber type="number" maxFractionDigits="0" value="${inquiryResult.validInitialFormAnswersNumber}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.validInitialFormAnswersRatio}" /></td>
		</tr>
		<tr>
			<th>Respostas v�lidas inqu�rito � UC <a href="#" class="helpleft">[?] <span>Respostas v�lidas - se os valores percentagem de NHTA e NDE n�o fossem simultaneamente iguais a zero, e a resposta ao inqu�rito foi submetida ap�s a disponibiliza��o da op��o de n�o responder ao inqu�rito.</span></a></th>
			<td><fmt:formatNumber type="number" maxFractionDigits="0" value="${inquiryResult.validInquiryAnswersNumber}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.validInquiryAnswersRatio}" /></td>
		</tr>
		<tr>
			<th>N�o respostas � UC</th>
			<td><fmt:formatNumber type="number" maxFractionDigits="0" value="${inquiryResult.noInquiryAnswersNumber}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.noInquiryAnswersRatio}" /></td>
		</tr>
		<tr>
			<th>Respostas inv�lidas inqu�rito � UC</th>
			<td><fmt:formatNumber type="number" maxFractionDigits="0" value="${inquiryResult.invalidInquiryAnswersNumber}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.invalidInquiryAnswersRatio}" /></td>
		</tr>
	</table>
									
	
	<table class="tstyle1 thlight thleft tdcenter">
		<tr class="top">
			<th></th>
			<th class="aright">Respons�veis pela gest�o acad�mica <a href="#" class="helpleft">[?] <span>Representatividade - n� de respostas superior a 5 e a 10% do n� inscritos.</span></a></th>
			<th class="aright">Comunidade acad�mica IST <a href="#" class="helpleft">[?] <span>Representatividade - n� de respostas superior a 5 e a 50% do n� inscritos.</span></a></th>
		</tr>
		<tr>
			<th>Representatividade para divulga��o</th>
			<td><bean:message key="<%= "label." + result.getInternalDisclosure().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
			<td><bean:message key="<%= "label." + result.getPublicDisclosure().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
		</tr>
	</table>
	
	
	<table class="tstyle1 thlight thleft tdcenter">
		<tr class="top">
			<th></th>
			<th class="aright">Organiza��o da UC <a href="#" class="helpleft">[?] <span>Resultados a melhorar se mais 25% alunos classifica como abaixo ou igual a 3 (Discordo) 2 das 4 quest�es do grupo.</span></a></th>
			<th class="aright">Avalia��o da UC <a href="#" class="helpleft">[?] <span>Resultados a melhorar se mais 25% alunos classifica como abaixo ou igual a 3 (Discordo) a quest�o e/ou taxa de avalia��o <50% e/ou taxa de aprova��o <50%.</span></a></th>
			<th class="aright">Pass�vel de Auditoria <a href="#" class="helpleft">[?] <span>Pass�vel de Auditoria se 2 grupos com resultados a melhorar.</span></a></th>
		</tr>
		<tr>
			<th>Resultados a melhorar</th>
			<td><bean:message key="<%= "label." + result.getUnsatisfactoryResultsCUOrganization().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
			<td><bean:message key="<%= "label." + result.getUnsatisfactoryResultsCUEvaluation().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
			<td><bean:message key="<%= "label." + result.getAuditCU().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
		</tr>
	</table>
</logic:equal>
<logic:notEqual name="inquiryResult" property="availableToInquiry" value="true">
<!-- TEXTO 2 -->
</logic:notEqual>


<logic:equal name="inquiryResult" property="publicDisclosure" value="true">
	
	<h3 class="mtop15 mbottom0"><strong>Acompanhamento e carga de trabalho da UC ao longo do semestre</strong></h3>
	
	<table class="tstyle1 thlight thleft td50px">
		<tr class="top">
			<th>Carga Hor�ria da UC</th>
			<td><c:out value="${inquiryResult.scheduleLoadForPresentation}" /></td>
		</tr>
		<tr>
			<th>N� ECTS da UC</th>
			<td><c:out value="${inquiryResult.ectsForPresentation}" /></td>
		</tr>
	</table>
	<em>(informa��o do sistema)</em>
	
	<h3 class="mtop15 mbottom0"><strong>Auto-avalia��o dos alunos</strong></h3>
	
	<table class="tstyle1 thlight thleft tdright td50px">
		<tr class="top">
			<th></th>
			<th class="aright">N</th>
			<th class="aright">M�dia</th>
			<th class="aright">Desvio padr�o</th>
		</tr>
		<tr>
			<th>N� m�dio de horas de trabalho aut�nomo por semana com a UC</th>
			<td><c:out value="${inquiryResult.number_perc_NHTA}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.average_perc_weeklyHoursForPresentation}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.standardDeviation_perc_NHTAForPresentation}" /></td>
		</tr>
		<tr>
			<th>N� de dias de estudo da UC na �poca de exames</th>
			<td><c:out value="${inquiryResult.number_NDE}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.average_NDEForPresentation}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.standardDeviation_NDEForPresentation}" /></td>
		</tr>
		<tr>
			<th>N� m�dio ECTS estimado <a href="#" class="helpleft">[?] <span>ECTS ESTIMADO = ((N� de horas de trabalho aut�nomo por semana com a UC + Carga Hor�ria da UC)* 14+ N� de dias de estudo da UC na �poca de exames * 8)/28.</span></a></th>
			<td><c:out value="${inquiryResult.estimatedEctsNumber}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.estimatedEctsAverageForPresentation}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.estimatedEctsStandardDeviationForPresentation}" /></td>
		</tr>
	</table>
	
	
	<table class="tstyle1 thlight thleft tdright td50px">
		<tr class="top">
			<th></th>
			<th class="aright">N</th>
			<th class="aright">[10; 12]</th>
			<th class="aright">[13; 14]</th>
			<th class="aright">[15; 16]</th>
			<th class="aright">[17; 18]</th>
			<th class="aright">[19; 20]</th>
			<th class="aright">Reprovado</th>
			<th class="aright">N�o avaliado</th>
		</tr>
		<tr>
			<th>Gama de valores da classifica��o dos alunos</th>
			<td><fmt:formatNumber type="number" maxFractionDigits="0" value="${inquiryResult.number_P1_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_10_12ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_13_14ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_15_16ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_17_18ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_19_20ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_flunkedForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_nonEvaluatedForPresentation}" /></td>
		</tr>
	</table>
	
	
	
	<p class="mtop15 mbottom0"><strong>Para os alunos com uma carga de trabalho elevada, esta deveu-se a:</strong></p>
	
	<table class="tstyle1 thlight thleft tdright td50px">
		<tr class="top">
			<th></th>
			<th class="aright">N</th>
			<th class="aright">%</th>
		</tr>
		<tr>
			<th>Trabalhos/projectos complexos</th>
			<td><fmt:formatNumber type="number" maxFractionDigits="0" value="${inquiryResult.number_P1_2_a}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc__P1_2_aForPresentation}" /></td>
		</tr>
		<tr>
			<th>Trabalhos/projectos extensos</th>
			<td><fmt:formatNumber type="number" maxFractionDigits="0" value="${inquiryResult.number_P1_2_b}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc__P1_2_bForPresentation}" /></td>
		</tr>
		<tr>
			<th>Trabalhos/projectos em n�mero elevado</th>
			<td><fmt:formatNumber type="number" maxFractionDigits="0" value="${inquiryResult.number_P1_2_c}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc__P1_2_cForPresentation}" /></td>
		</tr>
		<tr>
			<th>Falta de prepara��o anterior exigindo mais trabalho/estudo</th>
			<td><fmt:formatNumber type="number" maxFractionDigits="0" value="${inquiryResult.number_P1_2_d}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc__P1_2_dForPresentation}" /></td>
		</tr>
		<tr>
			<th>Extens�o do programa face ao n� de aulas previstas</th>
			<td><fmt:formatNumber type="number" maxFractionDigits="0" value="${inquiryResult.number_P1_2_e}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc__P1_2_eForPresentation}" /></td>
		</tr>
		<tr>
			<th>Pouco acompanhamento das aulas ao longo do semestre</th>
			<td><fmt:formatNumber type="number" maxFractionDigits="0" value="${inquiryResult.number_P_1_2_f}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc__P1_2_fForPresentation}" /></td>
		</tr>
		<tr>
			<th>Outras raz�es</th>
			<td><fmt:formatNumber type="number" maxFractionDigits="0" value="${inquiryResult.number_P1_2_g}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc__P1_2_gForPresentation}" /></td>
		</tr>
	</table>
	
	<table class="tstyle1 thlight thleft tdright td50px">
		<tr class="top">
			<th></th>
			<th class="aright">N</th>
			<th class="aright">M�dia</th>
			<th class="aright separatorright">Desvio padr�o</th>
			<th class="aright">Discordo totalmente<br/><b>1</b></th>
			<th class="aright"><b>2</b></th>
			<th class="aright">Discordo<br/><b>3</b></th>
			<th class="aright"><b>4</b></th>
			<th class="aright">N�o concordo nem discordo<br/><b>5</b></th>
			<th class="aright"><b>6</b></th>
			<th class="aright">Concordo<br/><b>7</b></th>
			<th class="aright"><b>8</b></th>
			<th class="aright">Concordo totalmente<br/><b>9</b></th>
		</tr>
		<tr>
			<th>Conhecimentos anteriores suficientes para o acompanhamento da UC</th>
			<td><c:out value="${inquiryResult.number_P1_3}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.average_P1_3}" /></td>
			<td class="separatorright"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.standardDeviation_P1_3ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P1_3_1ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P1_3_2ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P1_3_3ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P1_3_4ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P1_3_5ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P1_3_6ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P1_3_7ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P1_3_8ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P1_3_9ForPresentation}" /></td>
		</tr>
	</table>
	
	
	<table class="tstyle1 thlight thleft tdright td50px">
		<tr class="top">
			<th></th>
			<th class="aright">N</th>
			<th class="aright">M�dia</th>
			<th class="aright separatorright">Desvio padr�o</th>
			<th class="aright">Passiva<br/><b>1</b></th>
			<th class="aright">Activa quando solicitada<br/><b>2</b></th>
			<th class="aright">Activa por iniciativa pr�pria<br/><b>3</b></th>
		</tr>
		<tr>
			<th>Participa��o dos alunos na UC</th>
			<td><c:out value="${inquiryResult.number_P1_4}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.average_P1_4ForPresentation}" /></td>
			<td class="separatorright"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.standardDeviation_P1_4ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P1_4_1ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P1_4_2ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P1_4_3ForPresentation}" /></td>
		</tr>
	</table>
	
	
	<p class="mtop15 mbottom0"><strong>A UC contribuiu para a aquisi��o e/ou desenvolvimento das seguintes compet�ncias</strong></p>
	
	<table class="tstyle1 thlight thleft tdright td50px">
		<tr class="top">
			<th></th>
			<th class="aright">N</th>
			<th class="aright">M�dia</th>
			<th class="aright separatorright">Desvio padr�o</th>
			<th class="aright">N�o sabe / N�o responde / N�o aplic�vel</th>
			<th class="aright">N�o contribuiu<br/><b>1</b></th>
			<th class="aright">Contribuiu<br/><b>2</b></th>
			<th class="aright">Contribuiu muito<br/><b>3</b></th>
		</tr>
		<tr>
			<th>Conhecimento e compreens�o do tema da UC</th>
			<td><c:out value="${inquiryResult.number_P2_1}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.average_P2_1ForPresentation}" /></td>
			<td class="separatorright"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.standardDeviation_P2_1ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_1_0ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_1_1ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_1_2ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_1_3ForPresentation}" /></td>
		</tr>
		<tr>
			<th>Aplica��o do conhecimento sobre o tema da UC</th>
			<td><c:out value="${inquiryResult.number_P2_2}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.average_P2_2ForPresentation}" /></td>
			<td class="separatorright"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.standardDeviation_P2_2ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_2_0ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_2_1ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_2_2ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_2_3ForPresentation}" /></td>
		</tr>
		<tr>
			<th>Sentido cr�tico e esp�rito reflexivo</th>
			<td><c:out value="${inquiryResult.number_P2_3}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.average_P2_3ForPresentation}" /></td>
			<td class="separatorright"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.standardDeviation_P2_3ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_3_0ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_3_1ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_3_2ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_3_3ForPresentation}" /></td>
		</tr>
		<tr>
			<th>Capacidade de coopera��o e comunica��o</th>
			<td><c:out value="${inquiryResult.number_P2_4}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.average_P2_4ForPresentation}" /></td>
			<td class="separatorright"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.standardDeviation_P2_4ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_4_0ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_4_1ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_4_2ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P2_4_3ForPresentation}" /></td>
		</tr>
	</table>
	
	
	<p class="mtop15 mbottom0"><strong>Organiza��o da UC</strong></p>
	
	<table class="tstyle1 thlight thleft tdright td50px">
		<tr class="top">
			<th></th>
			<th class="aright">N</th>
			<th class="aright">M�dia</th>
			<th class="aright separatorright">Desvio padr�o</th>
			<th class="aright">Discordo totalmente<br/><b>1</b></th>
			<th class="aright"><b>2</b></th>
			<th class="aright">Discordo<br/><b>3</b></th>
			<th class="aright"><b>4</b></th>
			<th class="aright">N�o concordo nem discordo<br/><b>5</b></th>
			<th class="aright"><b>6</b></th>
			<th class="aright">Concordo<br/><b>7</b></th>
			<th class="aright"><b>8</b></th>
			<th class="aright">Concordo totalmente<br/><b>9</b></th>
		</tr>
		<tr>
			<th>O programa previsto foi leccionado</th>
			<td><c:out value="${inquiryResult.number_P3_1}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.average_P3_1ForPresentation}" /></td>
			<td class="separatorright"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.standardDeviation_P3_1ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_1_1ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_1_2ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_1_3ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_1_4ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_1_5ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_1_6ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_1_7ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_1_8ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_1_9ForPresentation}" /></td>
		</tr>
		<tr>
			<th>A UC encontra-se bem estruturada</th>
			<td><c:out value="${inquiryResult.number_P3_2}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.average_P3_2ForPresentation}" /></td>
			<td class="separatorright"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.standardDeviation_P3_2ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_2_1ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_2_2ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_2_3ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_2_4ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_2_5ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_2_6ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_2_7ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_2_8ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_2_9ForPresentation}" /></td>
		</tr>
		<tr>
			<th>A bibliografia foi importante</th>
			<td><c:out value="${inquiryResult.number_P3_3}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.average_P3_3ForPresentation}" /></td>
			<td class="separatorright"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.standardDeviation_P3_3ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_3_1ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_3_2ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_3_3ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_3_4ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_3_5ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_3_6ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_3_7ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_3_8ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_3_9ForPresentation}" /></td>
		</tr>
		<tr>
			<th>Os materiais de apoio foram bons</th>
			<td><c:out value="${inquiryResult.number_P3_4}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.average_P3_4ForPresentation}" /></td>
			<td class="separatorright"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.standardDeviation_P3_4ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_4_1ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_4_2ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_4_3ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_4_4ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_4_5ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_4_6ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_4_7ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_4_8ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P3_4_9ForPresentation}" /></td>
		</tr>
	</table>
	
	
	<p class="mtop15 mbottom0"><strong>M�todo de avalia��o da UC</strong></p>
	
	<table class="tstyle1 thlight thleft tdright td50px">
		<tr class="top">
			<th></th>
			<th class="aright">N</th>
			<th class="aright">M�dia</th>
			<th class="aright separatorright">Desvio padr�o</th>
			<th class="aright">Discordo totalmente<br/><b>1</b></th>
			<th class="aright"><b>2</b></th>
			<th class="aright">Discordo<br/><b>3</b></th>
			<th class="aright"><b>4</b></th>
			<th class="aright">N�o concordo nem discordo<br/><b>5</b></th>
			<th class="aright"><b>6</b></th>
			<th class="aright">Concordo<br/><b>7</b></th>
			<th class="aright"><b>8</b></th>
			<th class="aright">Concordo totalmente<br/><b>9</b></th>
		</tr>
		<tr>
			<th>Os m�todos de avalia��o foram justos e apropriados</th>
			<td><c:out value="${inquiryResult.number_P4}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.average_P4ForPresentation}" /></td>
			<td class="separatorright"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.standardDeviation_P4ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P4_1ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P4_2ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P4_3ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P4_4ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P4_5ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P4_6ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P4_7ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P4_8ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P4_9ForPresentation}" /></td>
		</tr>
	</table>
	
	
	<p class="mtop15 mbottom0"><strong>Avalia��o global da UC</strong></p>
	
	<table class="tstyle1 thlight thleft tdright td50px">
		<tr class="top">
			<th></th>
			<th class="aright">N</th>
			<th class="aright">M�dia</th>
			<th class="aright separatorright">Desvio padr�o</th>
			<th class="aright">Discordo totalmente<br/><b>1</b></th>
			<th class="aright"><b>2</b></th>
			<th class="aright">Discordo<br/><b>3</b></th>
			<th class="aright"><b>4</b></th>
			<th class="aright">N�o concordo nem discordo<br/><b>5</b></th>
			<th class="aright"><b>6</b></th>
			<th class="aright">Concordo<br/><b>7</b></th>
			<th class="aright"><b>8</b></th>
			<th class="aright">Concordo totalmente<br/><b>9</b></th>
		</tr>
		<tr>
			<th>Avalia��o do funcionamento da UC</th>
			<td><c:out value="${inquiryResult.number_P5}" /></td>
			<td><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.average_P5ForPresentation}" /></td>
			<td class="separatorright"><fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${inquiryResult.standardDeviation_P5ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P5_1ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P5_2ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P5_3ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P5_4ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P5_5ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P5_6ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P5_7ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P5_8ForPresentation}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="0" minFractionDigits="0" value="${inquiryResult.perc_P5_9ForPresentation}" /></td>
		</tr>
	</table>
</logic:equal>

<logic:notEqual name="inquiryResult" property="publicDisclosure" value="true">
<!--  TEXTO 3 -->
</logic:notEqual>


</body>
</html>
