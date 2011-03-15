<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html xml:lang="pt-PT" xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT"> 
<head> 
<title>QUC</title> 
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
 
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/inquiries/jquery.min.js"></script> 
<script type="text/javascript">jQuery.noConflict();</script> 
<script type="text/javascript">var example = 'bar-basic', theme = 'default';</script> 
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/jquery/scripts.js"></script>
 
<style media="print"> 
 
div.xpto {
page-break-inside: avoid;
page-break-after: always;
}
 
</style> 

<style> 

body.survey-results {
font-size: 12px;
line-height: 15px;
font-family: Arial;
background: #eee;
text-align: center;
margin: 40px 20px 80px 20px;
}
body.survey-results h1 {
font-size: 22px;
line-height: 30px;
margin: 15px 0;
}
body.survey-results h2 {
font-size: 17px;
line-height: 30px;
margin: 40px 0 10px 0;
}
body.survey-results h2 span {
line-height: 15px;
}
body.survey-results p {
margin: 10px 0 5px 0;
}

#page {
margin: 20px auto;
text-align: left;
padding: 20px 30px;
width: 900px;
background: #fff;
border: 1px solid #ddd;
/*
-moz-border-radius: 4px;
border-radius: 4px;
*/
}

/* ---------------------------
      STRUCTURAL TABLE 
--------------------------- */

table.structural {
border-collapse: collapse;
}
table.structural tr td {
padding: 0;
vertical-align: top;
}

/* ---------------------------
      TABLE GRAPH 
--------------------------- */

div.graph {
margin: 15px 0px 30px 0px;
}

table.graph, table.graph-2col {
color: #555;
}

table.graph {
border-collapse: collapse;
margin: 5px 0 5px 0;
/* width: 1000px; */
}
table.graph th {
text-align: left;
border-top: none;
border-bottom: 1px solid #ccc;
padding: 5px 0px;
font-weight: normal;
}
table.graph td {
text-align: left;
border-top: none;
border-bottom: 1px solid #ccc;
padding: 5px 5px !important;
text-align: center;
	vertical-align: middle !important;
}
table.graph tr.thead th {
border-bottom: 2px solid #ccc;
}
table.graph tr th:first-child {
padding: 5px 0px 5px 0px;
}
table.graph tr.thead th {
vertical-align: bottom;
text-align: center;
color: #555;
text-transform: uppercase;
font-size: 9px;
padding: 5px 5px;
background: #f5f5f5;
}
/*
table.graph tr th {
width: 300px;
}
table.graph tr.thead th {
width: 55px;
}
*/
table.graph tr.thead th.first {
width: 300px;
}

table.graph tr td.x1, table tr td.x2, table tr td.x3, table tr td.x4 {
background: #f5f5f5;
width: 55px;
}
table.graph tr td.x1 {
border-right: 1px solid #ccc;
}

/* specific */

table.general-results  {
width: 100%;
}

table.general-results td {
width: 30px;
height: 32px;
}

table.teacher-results tr td {
width: 30px;
}
table.teacher-results tr th {
width: auto;
padding-left: 10px !important;
}

div.workload-left, div.workload-right   {
float: left;
width: 435px;
margin-top: 30px;
}
div.workload-left   {
padding-right: 30px;
}
div.workload-right table td { 
text-align: left; /* fixes IE text alignment issue*/
}


tr.sub th {
padding-left: 20px !important;
}


div.result-audit {
margin: 20px 0 -20px 0;
}
div.result-audit span {
background: #C04439;
padding: 5px 10px;
color: #fff;
}

tr.result-audit th span, tr.result-analysis th span {
}
tr.result-audit, tr.result-analysis {
background: #f5f5f5;
}


/* ---------------------------
      TABLE GRAPH 2COL
--------------------------- */

table.graph-2col {
border-collapse: collapse;
margin: 5px 0 5px 0;
/* width: 1000px; */
}
table.graph-2col th {
text-align: left;
border-top: none;
border-bottom: 1px solid #ccc;
padding: 5px 0px;
font-weight: normal;
/* width: 380px; */
}
table.graph-2col td {
text-align: right !important;
border-top: none;
border-bottom: 1px solid #ccc !important;
padding: 5px 5px !important;
text-align: center;
}

/* ---------------------------
      INSIDE TABLE 
--------------------------- */

table.graph table {
width: 500px;
border-collapse: collapse;
}
table.graph table tr td {
border: none;
padding: 0 !important;
}
table.graph table tr td div {
}

/* ---------------------------
      GRAPH BARS 
--------------------------- */

div.graph-bar-horz {
height: 21px;
-moz-border-radius: 3px;
border-radius: 3px;
float: left;
background: #3573a5;
}
div.graph-bar-horz-number {
float: left;
padding-top: 2px;
padding-left: 6px;
}

/* right-aligned bars */

table.bar-right div {
float: right;
text-align: right;
}

table.bar-right div.graph-bar-horz-number {
padding-right: 10px;
}


div.bar-yellow, div.bar-red, div.bar-green, div.bar-blue, div.bar-purple, div.bar-grey  {
width: 30px;
height: 19px;
-moz-border-radius: 3px;
border-radius: 3px;
text-align: center;
color: #fff;
padding-top: 2px;
font-weight: bold;
}
div.bar-yellow { background: #DDB75B; }
div.bar-red { background: #C04439; }
div.bar-green { background: #478F47; }
div.bar-blue { background: #3574A5; }
div.bar-purple { background: #743E8C; }
div.bar-grey { background: #888888; }


div.first-bar {
-moz-border-radius-topleft: 3px;
-moz-border-radius-bottomleft: 3px;
border-top-left-radius: 3px;
border-bottom-left-radius: 3px;
}
div.last-bar {
-moz-border-radius-topright: 3px;
-moz-border-radius-bottomright: 3px;
border-top-right-radius: 3px;
border-bottom-right-radius: 3px;
}


div.graph-bar-16-1,
div.graph-bar-16-2,
div.graph-bar-16-3,
div.graph-bar-16-4,
div.graph-bar-16-5,
div.graph-bar-16-6,
div.graph-bar-19-1,
div.graph-bar-19-2,
div.graph-bar-19-3,
div.graph-bar-19-4,
div.graph-bar-19-5,
div.graph-bar-19-6,
div.graph-bar-19-7,
div.graph-bar-19-8,
div.graph-bar-19-9,
div.graph-bar-grey {
height: 18px;
/*
-moz-border-radius: 3px;
border-radius: 3px;
*/
text-align: center;
color: #fff;
padding-top: 2px;
font-weight: bold;
}
div.graph-bar-yellow {
background: #e9a73d;
}
div.graph-bar-red {
background: #ce423d;
}
div.graph-bar-green {
background: #2d994a;
}
div.graph-bar-blue {
background: #006ca2;
}
div.graph-bar-grey {
background: #eee;
color: #888;
font-weight: normal;
-moz-border-radius: 3px;
border-radius: 3px;
}


.neutral div.graph-bar-19-1, .neutral div.graph-bar-16-1 { background: #92B7C6; }
.neutral div.graph-bar-19-2 { background: #7BA9C3; }
.neutral div.graph-bar-19-3, .neutral div.graph-bar-16-2 { background: #669BC0; }
.neutral div.graph-bar-19-4, .neutral div.graph-bar-16-3 { background: #5591BD; }
.neutral div.graph-bar-19-5, .neutral div.graph-bar-16-4 { background: #4983B5; }
.neutral div.graph-bar-19-6 { background: #4076AD; }
.neutral div.graph-bar-19-7, .neutral div.graph-bar-16-5 { background: #376AA5; }
.neutral div.graph-bar-19-8 { background: #2A5A9A; }
.neutral div.graph-bar-19-9, .neutral div.graph-bar-16-6 { background: #225093; }

table.neutral table {
border-collapse: separate !important;
}

.classification div.graph-bar-19-1, .classification div.graph-bar-16-1 { background: #c04439; } /* red */ 
.classification div.graph-bar-19-2 { background: #ca623a; } /* red */
.classification div.graph-bar-19-3, .classification div.graph-bar-16-2 { background: #cc7d3f; } /* red */
.classification div.graph-bar-19-4, .classification div.graph-bar-16-3 { background: #ddb75b; } /* yellow */
.classification div.graph-bar-19-5, .classification div.graph-bar-16-4 { background: #91a749; } /* green */
.classification div.graph-bar-19-6 { background: #74a14e; } /* green */
.classification div.graph-bar-19-7, .classification div.graph-bar-16-5 { background: #5c9b4e; } /* green */
.classification div.graph-bar-19-8 { background: #478f47; } /* green */
.classification div.graph-bar-19-9, .classification div.graph-bar-16-6 { background: #438a43; } /* green */

span.legend-bar,
span.legend-bar-first,
span.legend-bar-last {
padding: 0 3px;
font-size: 8px;
}

span.legend-bar-first span.text { padding-right: 5px; }
span.legend-bar-last span.text { padding-left: 5px; }

span.legend-bar-16-1,
span.legend-bar-16-2,
span.legend-bar-16-3,
span.legend-bar-16-4,
span.legend-bar-16-5,
span.legend-bar-16-6,
span.legend-bar-19-1,
span.legend-bar-19-2,
span.legend-bar-19-3,
span.legend-bar-19-4,
span.legend-bar-19-5,
span.legend-bar-19-6,
span.legend-bar-19-7,
span.legend-bar-19-8,
span.legend-bar-19-9 {
-moz-border-radius: 3px;
border-radius: 3px;
padding: 2px 4px;
font-size: 8px;
font-weight: bold;
color: #fff;
}


/*
#528FBD
#4C87B8
#457EB2
#3F76AC
#396DA7
#3265A1
#2C5D9C
#255495
#204D91
*/

/*
#92B7C6
#7BA9C3
#669BC0
#5591BD
#4983B5
#4076AD
#376AA5
#2A5A9A
#225093
*/



table.neutral span.legend-bar-19-1, table.neutral span.legend-bar-16-1 { background: #92B7C6; }
table.neutral span.legend-bar-19-2 { background: #7BA9C3; }
table.neutral span.legend-bar-19-3, table.neutral span.legend-bar-16-2 { background: #669BC0; }
table.neutral span.legend-bar-19-4, table.neutral span.legend-bar-16-3 { background: #5591BD; }
table.neutral span.legend-bar-19-5, table.neutral span.legend-bar-16-4 { background: #4983B5; }
table.neutral span.legend-bar-19-6 { background: #4076AD; }
table.neutral span.legend-bar-19-7, table.neutral span.legend-bar-16-5 { background: #376AA5; }
table.neutral span.legend-bar-19-8 { background: #2A5A9A; }
table.neutral span.legend-bar-19-9, table.neutral span.legend-bar-16-6 { background: #225093; }


table.classification span.legend-bar-19-1, table.classification span.legend-bar-16-1 { background: #c04439; } /* red */ 
table.classification span.legend-bar-19-2 { background: #ca623a; } /* red */
table.classification span.legend-bar-19-3, table.classification span.legend-bar-16-2 { background: #cc7d3f; } /* red */
table.classification span.legend-bar-19-4, table.classification span.legend-bar-16-3 { background: #ddb75b; } /* yellow */
table.classification span.legend-bar-19-5, table.classification span.legend-bar-16-4 { background: #91a749; } /* green */
table.classification span.legend-bar-19-6 { background: #74a14e; } /* green */
table.classification span.legend-bar-19-7, table.classification span.legend-bar-16-5 { background: #5c9b4e; } /* green */
table.classification span.legend-bar-19-8 { background: #478f47; } /* green */
table.classification span.legend-bar-19-9, table.classification span.legend-bar-16-6 { background: #438a43; } /* green */


ul.legend-general {
list-style: none;
padding: 0;
margin: 10px 0;
color: #555;
}
ul.legend-general li {
/*display: inline;*/
padding-right: 10px;
padding: 2px 0;
}

ul.legend-general-teacher {
list-style: none;
padding: 0;
margin: 10px 0;
color: #555;
}
ul.legend-general-teacher li {
display: inline;
padding: 2px 0;
padding-right: 5px;
}


span.legend-bar-1,
span.legend-bar-2,
span.legend-bar-3,
span.legend-bar-4,
span.legend-bar-5,
span.legend-bar-6 {
-moz-border-radius: 3px;
border-radius: 3px;
padding: 2px 5px 0px 5px;
font-size: 8px;
font-weight: bold;
}


span.legend-bar-1 { background: #3574A5; }
span.legend-bar-2 { background: #478F47; }
span.legend-bar-3 { background: #DDB75B; }
span.legend-bar-4 { background: #C04439; }
span.legend-bar-5 { background: #743E8C; }
span.legend-bar-6 { background: #888888; }


/* ---------------------------
      SUMMARY
--------------------------- */

div.summary table th {
border: none;
padding: 3px 0;
width: 200px;
}
div.summary table td {
text-align: left;
border: none;
padding: 3px 0;
}


/* ---------------------------
      TOOLTIPS
--------------------------- */

a {
color: #105c93;
}
a.help, a.helpleft {
position: relative;
text-decoration: none;
border: none !important;
width: 20px;
text-transform: none;
font-size: 12px;
font-weight: normal;
}
a.help span, a.helpleft span { display: none; }
a.help:hover, a.helpleft:hover {
z-index: 100;
border: 1px solic transparent;
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
}
a.helpleft:hover span {
display: block !important;
display: inline-block;
width: 250px;
position: absolute;
top: 20px;
left: 20px;
text-align: left;
padding: 7px 10px;
background: #48869e;
color: #fff;
border: 3px solid #97bac6;
}
a.helpleft[class]:hover span {
right: 20px;
}

/* ---------------------------
      CHARTS
--------------------------- */

div.chart {
clear:both;
/* min-width: 600px; */
background: #eee;
padding: 10px;
/*
-moz-border-radius: 3px;
border-radius: 3px;
*/
}


table.graph tr td div.chart {
clear:both;
/* min-width: 600px; */
background: none;
padding: 0;
}

/* ---------------------------
      REPORTS
--------------------------- */


div#report div.graph {
width: 900px;
}

span.link {
text-decoration: none;
color: #105c93;
border-bottom: 1px solid #97b7ce;
cursor: pointer;
}


div#report div.bar-yellow,
div#report div.bar-red,
div#report div.bar-green,
div#report div.bar-blue,
div#report div.bar-purple {
margin: 3px 0;
padding: 0 3px;
display: inline;
background: #fff;
}
div#report div.bar-yellow div,
div#report div.bar-red div,
div#report div.bar-green div,
div#report div.bar-blue div,
div#report div.bar-purple div {
padding: 2px 9px;
-moz-border-radius: 3px;
border-radius: 3px;
text-align: center;
color: #fff;
padding-top: 2px;
font-weight: bold;
margin: auto;
display: inline;
}

div#report div.bar-yellow div { background: #DDB75B; }
div#report div.bar-red div { background: #C04439; }
div#report div.bar-green div { background: #478F47; }
div#report div.bar-blue div { background: #3574A5; }
div#report div.bar-purple div { background: #743E8C; }
div#report div.bar-grey div { background: #888888; }


td.comment {
background: #f5f5f5;
text-align: left !important;
}
td.comment div {
width: auto;
padding: 0 5px 10px 5px;
}


div.workload-left div.graph, div.workload-right div.graph {
width: auto !important;
}

</style>

</head>

<body class="survey-results">  
 
<div id="page"> 
 
<fmt:setBundle basename="resources.InquiriesResources" var="INQUIRIES_RESOURCES"/>

<p>
	<em style="float: left;"><bean:write name="executionPeriod" property="semester"/>� Semestre de <bean:write name="executionPeriod" property="executionYear.year"/></em>
	<em style="float: right;">Data de produ��o dos resultados: <fr:view name="resultsDate" layout="no-time"/></em>
</p>

<div style="clear: both;"></div>
<h1>QUC - Resultados dos Inqu�ritos aos Alunos: <bean:write name="executionCourse" property="name"/></h1>

<p>Docente: <b><bean:write name="professorship" property="person.name"/></b></p>
<p>Tipo de aula: <b><bean:message name="shiftType" property="name"  bundle="ENUMERATION_RESOURCES"/></b></p>

<h2>Resultados gerais do Docente</h2>
<%-- <table class="structural" style="margin-top: 5px;"> 
	<tr> 
		<td style="padding-right: 20px;">--%> 
		<div style="width: 300px;">
			<fr:view name="teacherGroupResultsSummaryBean" layout="general-result-resume"/>
			</div>
			<ul class="legend-general-teacher" style="margin-top: 15px;">
	<li>Legenda:</li>
	<li><span class="legend-bar-1">&nbsp;</span> Excelente</li>
	<li><span class="legend-bar-2">&nbsp;</span> Regular</li>
	<li><span class="legend-bar-3">&nbsp;</span> A melhorar</li>
	<li><span class="legend-bar-4">&nbsp;</span> Inadequado</li>
	<li><span class="legend-bar-6">&nbsp;</span> Sem representatividade</li>
</ul>
		<%-- </td>
	</tr> 
</table> --%>

<logic:iterate indexId="iter" id="blockResult" name="blockResultsSummaryBeans">
	<h2 style="clear: both"><bean:write name="blockResult" property="inquiryBlock.inquiryQuestionHeader.title"/></h2>
	<logic:iterate id="groupResult" name="blockResult" property="groupsResults">		
		<fr:view name="groupResult" />
	</logic:iterate>
</logic:iterate>

</div>

</body>
</html>
