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

<fmt:setBundle basename="resources.InquiriesResources" var="INQUIRIES_RESOURCES"/>

<p class="mtop0" style="float: right;"><em>Informa��o do sistema, recolhida a <c:out value="${inquiryResult.resultsDate}" /></em></p>

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
		<td><c:out value="${inquiryResult.valuesMap['N_inscritos']}" /></td>
	</tr>
	<tr>
		<th>Avaliados <a href="#" class="help">[?] <span>N� avaliados / N� inscritos. N�o s�o contabilizados resultados de �pocas especiais e/ou melhorias.</span></a></th>
		<td><c:out value="${inquiryResult.valuesMap['perc_Avaliados']}" /></td>
	</tr>
	<tr>
		<th>Aprovados <a href="#" class="help">[?] <span>N� aprovados / N� avaliados . N�o s�o contabilizados resultados de �pocas especiais e/ou melhorias.</span></a></th>
		<td><c:out value="${inquiryResult.valuesMap['perc_Aprovados']}" /></td>
	</tr>
	<tr>
		<th>M�dia notas <a href="#" class="help">[?] <span>N�o s�o contabilizados resultados de �pocas especiais e/ou melhorias.</span></a></th>
		<td><c:out value="${inquiryResult.valuesMap['Media_Notas']}" /></td>
	</tr>
	<tr>
		<th>Sujeita a inqu�rito <a href="#" class="help">[?] <span>Algumas UC n�o foram sujeitas a inqu�rito, para mais informa��es ver regulamento QUC e FAQ's em http://quc.ist.utl.pt</span></a></th>
		<td><bean:message key="<%= "label." + result.getExecutionCourse().getAvailableForInquiries().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
	</tr>
</table>
<em>(informa��o do sistema)</em>

<logic:equal name="inquiryResult" property="executionCourse.availableForInquiries" value="true">
	<h3 class="mtop15 mbottom0"><strong>Estat�stica de preenchimento e representatividade</strong></h3>
	
	<table class="tstyle1 thlight thleft td50px tdright">
		<tr class="top">
			<th></th>
			<th class="aright">N</th>
			<th class="aright">%</th>
		</tr>
		<tr>
			<th>Respostas ao quadro inicial (carga de trabalho)</th>
			<td><c:out value="${inquiryResult.valuesMap['N_respostasQI']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['perc_respQI']}" /></td>
		</tr>
		<tr>
			<th>Inqu�ritos submetidos � UC</th>
			<td><c:out value="${inquiryResult.valuesMap['N_Inq_sub_UC']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['perc_inq_sub_UC']}" /></td>
		</tr>
		<tr>
			<th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;N�o respostas � UC</th>
			<td><c:out value="${inquiryResult.valuesMap['N_NaoRespostasUC']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['perc_NaoRespostasUC']}" /></td>
		</tr>
		<tr>
			<th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Respostas v�lidas � UC</th>
			<td><c:out value="${inquiryResult.valuesMap['N_Inq_aval_UC']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['perc_Inq_aval_UC']}" /></td>
		</tr>
	</table>
									
	
	<table class="tstyle1 thlight thleft tdcenter">
		<tr class="top">
			<th></th>
			<th class="aright">Respons�veis pela gest�o acad�mica <a href="#" class="helpleft">[?] <span>Representatividade - n� de inscritos superior a 10, n� de respostas superior a 5 e a 10% do n� inscritos. - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
			<th class="aright">Comunidade acad�mica IST <a href="#" class="helpleft">[?] <span>Representatividade - n� de inscritos superior a 10, n� de respostas superior a 5 e a 50% do n� inscritos. - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
		</tr>
		<tr>
			<th>Representatividade para divulga��o</th>
			<td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['Repres_div_interna']}" /></td>
            <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['Repres_div_publica']}" /></td>
		</tr>
	</table>
	
	
	<table class="tstyle1 thlight thleft tdcenter">
		<tr class="top">
			<th></th>
			<th class="aright">Organiza��o da UC <a href="#" class="helpleft">[?] <span>Representatividade - n� de inscritos superior a 10, n� de respostas superior a 5 e a 50% do n� inscritos. - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
			<th class="aright">Avalia��o da UC <a href="#" class="helpleft">[?] <span>Resultados a melhorar se mais 25% alunos (no m�nimo de 10 respostas) classifica como abaixo ou igual a 3 (Discordo) a quest�o e/ou taxa de avalia��o <50% e/ou taxa de aprova��o <50%. - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
			<th class="aright">Pass�vel de Auditoria <a href="#" class="helpleft">[?] <span>Pass�vel de auditoria se organiza��o e avalia��o da UC com resultados a melhorar e, pelo menos, metade do corpo docente com resultados a melhorar no m�nimo de dois grupos.</span></a></th>
		</tr>
		<tr>
			<th>Resultados a melhorar</th>
            <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['ResInsatisfat_OrganizUC']}" /></td>
            <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['ResInsatisf_avaliacaoUC']}" /></td>
            <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['UC_auditoria']}" /></td>
		</tr>
	</table>


</logic:equal>
<logic:notEqual name="inquiryResult" property="executionCourse.availableForInquiries" value="true">
<!-- TEXTO 2 -->
</logic:notEqual>

<c:if test="${((empty publicContext || !publicContext) && inquiryResult.valuesMap['Repres_div_interna'] == '1' || (not empty publicContext && publicContext) && inquiryResult.publicDisclosure)}" >

	<h3 class="mtop15 mbottom0"><strong>Acompanhamento e carga de trabalho da UC ao longo do semestre</strong></h3>
	
	<table class="tstyle1 thlight thleft td50px">
		<tr class="top">
			<th>Carga Hor�ria da UC</th>
			<td><c:out value="${inquiryResult.valuesMap['CargaHoraria']}" /></td>
		</tr>
		<tr>
			<th>N� ECTS da UC</th>
			<td><c:out value="${inquiryResult.valuesMap['ECTS']}" /></td>
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
			<td><c:out value="${inquiryResult.valuesMap['N_NHTA']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['M_NHTA_']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['dp_NHTA_']}" /></td>
		</tr>
		<tr>
			<th>N� de dias de estudo da UC na �poca de exames</th>
            <td><c:out value="${inquiryResult.valuesMap['N_NDE']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['M_NDE_']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['dp_NDE_']}" /></td>
		</tr>
		<tr>
			<th>N� m�dio ECTS estimado <a href="#" class="helpleft">[?] <span>ECTS ESTIMADO = ((N� de horas de trabalho aut�nomo por semana com a UC + Carga Hor�ria da UC)* 14+ N� de dias de estudo da UC na �poca de exames * 8)/28.</span></a></th>
            <td><c:out value="${inquiryResult.valuesMap['N_ECTS_estimados']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['M_ECTSEstimados']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['dp_ECTSestimados']}" /></td>
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
			<td><c:out value="${inquiryResult.valuesMap['N_P1_1']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_10_12']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_13_14']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_15_16']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_17_18']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_19_20']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_flunked']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_nonEvaluated']}" /></td>
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
			<td><c:out value="${inquiryResult.valuesMap['N_P1_2_a']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['perc_P1_2_a']}" /></td>
		</tr>
		<tr>
			<th>Trabalhos/projectos extensos</th>
			<td><c:out value="${inquiryResult.valuesMap['N_P1_2_b']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['perc_P1_2_b']}" /></td>
		</tr>
		<tr>
			<th>Trabalhos/projectos em n�mero elevado</th>
			<td><c:out value="${inquiryResult.valuesMap['N_P1_2_c']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['perc_P1_2_c']}" /></td>
		</tr>
		<tr>
			<th>Falta de prepara��o anterior exigindo mais trabalho/estudo</th>
			<td><c:out value="${inquiryResult.valuesMap['N_P1_2_d']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['perc_P1_2_d']}" /></td>
		</tr>
		<tr>
			<th>Extens�o do programa face ao n� de aulas previstas</th>
			<td><c:out value="${inquiryResult.valuesMap['N_P1_2_e']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['perc_P1_2_e']}" /></td>
		</tr>
		<tr>
			<th>Pouco acompanhamento das aulas ao longo do semestre</th>
			<td><c:out value="${inquiryResult.valuesMap['N_P_1_2_f']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['perc_P1_2_f']}" /></td>
		</tr>
        <tr>
            <th>Problemas na organiza��o administrativa da UC</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P_1_2_g']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['perc_P1_2_g']}" /></td>
        </tr>
        <tr>
            <th>Problemas pessoais/com os colegas de grupo</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P_1_2_h']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['perc_P1_2_h']}" /></td>
        </tr>
		<tr>
			<th>Outras raz�es</th>
			<td><c:out value="${inquiryResult.valuesMap['N_P1_2_i']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['perc_P1_2_i']}" /></td>
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
			<td><c:out value="${inquiryResult.valuesMap['N_P1_3']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['M_P1_3_']}" /></td>
			<td class="separatorright"><c:out value="${inquiryResult.valuesMap['dp_P1_3_']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_P1_3_1']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_P1_3_2']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_P1_3_3']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_P1_3_4']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_P1_3_5']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_P1_3_6']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_P1_3_7']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_P1_3_8']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_P1_3_9']}" /></td>
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
			<td><c:out value="${inquiryResult.valuesMap['N_P1_4']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['M_P1_4']}" /></td>
			<td class="separatorright"><c:out value="${inquiryResult.valuesMap['dp_P1_4']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_P1_4_1']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_P1_4_2']}" /></td>
			<td><c:out value="${inquiryResult.valuesMap['Perc_P1_4_3']}" /></td>
		</tr>
	</table>
    
    <table class="tstyle1 thlight thleft tdright td50px">
        <tr class="top">
            <th>Caracteriza��o dos meios e m�todos de estudo mais usados</th>
            <th class="aright">N</th>
            <th class="aright">%</th>
        </tr>
        <tr>
            <th>Assistir �s aulas</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P1_5_a']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['perc_P1_5_a']}" /></td>
        </tr>
        <tr>
            <th>Bibliografia sugerida</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P1_5_b']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['perc_P1_5_b']}" /></td>
        </tr>
        <tr>
            <th>Apontamentos e outros documentos do professor</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P1_5_c']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['perc_P1_5_c']}" /></td>
        </tr>
        <tr>
            <th>Apontamentos e outros documentos do aluno</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P1_5_d']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['perc_P1_5_d']}" /></td>
        </tr>
        <tr>
            <th>Outros</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P1_5_e']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['perc_P1_5_e']}" /></td>
        </tr>
    </table>    
    
                            
	
	<p class="mtop15 mbottom0"><strong>A UC contribuiu para a aquisi��o e/ou desenvolvimento das seguintes compet�ncias</strong></p>
	
	<table class="tstyle1 thlight thleft tdright td50px">
		<tr class="top">
			<th>A frequ�ncia desta UC contribuiu para:</th>
            <th class="aright">N�o sabe/N�o se aplica</th>
			<th class="aright separatorright">N</th>
			<th class="aright">M�dia</th>
			<th class="aright separatorright">Desvio padr�o</th>
			<th class="aright">N�o sabe</th>
            <th class="aright">N�o se aplica</th>
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
			<th>Desenvolver o conhecimento e compreens�o do tema</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P2_1_ns']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['N_P2_1_comp']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['M_P2_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['dp_P2_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_1_menos1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_1_0']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_1_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_1_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_1_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_1_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_1_5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_1_6']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_1_7']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_1_8']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_1_9']}" /></td>
		</tr>
        <tr>
            <th>Aumentar a capacidade de aplicar o conhecimento adquirido sobre o tema</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P2_2_ns']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['N_P2_2_comp']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['M_P2_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['dp_P2_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_2_menos1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_2_0']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_2_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_2_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_2_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_2_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_2_5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_2_6']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_2_7']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_2_8']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_2_9']}" /></td>
        </tr>
        <tr>
            <th>Desenvolver o sentido cr�tico e a capacidade de reflex�o sobre o tema</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P2_3_ns']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['N_P2_3_comp']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['M_P2_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['dp_P2_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_3_menos1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_3_0']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_3_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_3_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_3_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_3_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_3_5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_3_6']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_3_7']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_3_8']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_3_9']}" /></td>
        </tr>
        <tr>
            <th>Promover a capacidade de coopera��o e comunica��o</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P2_4_ns']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['N_P2_4_comp']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['M_P2_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['dp_P2_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_4_menos1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_4_0']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_4_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_4_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_4_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_4_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_4_5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_4_6']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_4_7']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_4_8']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_4_9']}" /></td>
        </tr>
        <tr>
            <th>Aumentar a capacidade de aprendizagem aut�noma</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P2_5_ns']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['N_P2_5_comp']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['M_P2_5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['dp_P2_5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_5_menos1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_5_0']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_5_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_5_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_5_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_5_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_5_5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_5_6']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_5_7']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_5_8']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_5_9']}" /></td>
        </tr>
        <tr>
            <th>Aprofundar a capacidade de an�lise sobre as implica��es do tema no contexto social e profissional</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P2_6_ns']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['N_P2_6_comp']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['M_P2_6']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['dp_P2_6']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_6_menos1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_6_0']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_6_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_6_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_6_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_6_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_6_5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_6_6']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_6_7']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_6_8']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P2_6_9']}" /></td>
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
            <td><c:out value="${inquiryResult.valuesMap['N_P3_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['M_P3_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['dp_P3_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_1_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_1_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_1_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_1_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_1_5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_1_6']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_1_7']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_1_8']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_1_9']}" /></td>
		</tr>
        <tr>
			<th>A UC encontra-se bem estruturada</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P3_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['M_P3_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['dp_P3_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_2_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_2_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_2_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_2_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_2_5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_2_6']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_2_7']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_2_8']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_2_9']}" /></td>
        </tr>
        <tr>
            <th>A bibliografia foi importante</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P3_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['M_P3_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['dp_P3_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_3_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_3_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_3_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_3_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_3_5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_3_6']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_3_7']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_3_8']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_3_9']}" /></td>
        </tr>
        <tr>
            <th>Os materiais de apoio foram bons</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P3_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['M_P3_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['dp_P3_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_4_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_4_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_4_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_4_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_4_5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_4_6']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_4_7']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_4_8']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P3_4_9']}" /></td>
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
            <td><c:out value="${inquiryResult.valuesMap['N_P4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['M_P4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['dp_P4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P4_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P4_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P4_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P4_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P4_5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P4_6']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P4_7']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P4_8']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P4_9']}" /></td>
		</tr>
	</table>
	
	
	<p class="mtop15 mbottom0"><strong>Avalia��o global da UC</strong></p>
	
	<table class="tstyle1 thlight thleft tdright td50px">
		<tr class="top">
			<th></th>
			<th class="aright">N</th>
			<th class="aright">M�dia</th>
			<th class="aright separatorright">Desvio padr�o</th>
            <th class="aright">Muito mau<br/><b>1</b></th>
            <th class="aright"><b>2</b></th>
            <th class="aright">Mau<br/><b>3</b></th>
            <th class="aright"><b>4</b></th>
            <th class="aright">Nem bom nem mau<br/><b>5</b></th>
            <th class="aright"><b>6</b></th>
            <th class="aright">Bom<br/><b>7</b></th>
            <th class="aright"><b>8</b></th>
            <th class="aright">Muito bom<br/><b>9</b></th>            
		</tr>
		<tr>
			<th>Avalia��o do funcionamento da UC</th>
            <td><c:out value="${inquiryResult.valuesMap['N_P5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['M_P5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['dp_P5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P5_1']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P5_2']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P5_3']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P5_4']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P5_5']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P5_6']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P5_7']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P5_8']}" /></td>
            <td><c:out value="${inquiryResult.valuesMap['Perc_P5_9']}" /></td>
		</tr>
	</table>
</c:if>

<c:if test="${inquiryResult.valuesMap['Repres_div_interna'] != '1'}" >
<!--  TEXTO 3 -->
</c:if>

</body>
</html>
