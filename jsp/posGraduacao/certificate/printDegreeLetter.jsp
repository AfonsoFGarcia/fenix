<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN%>" type="net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan" />
<bean:define id="infoThesisDataVersion" name="<%= SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION%>" />
 
<html>

<head>
<title></title>
<style type="text/css" media="screen, print">
body {
font-family: Times New Roman, Times, serif;
font-weight: bold;
text-transform: uppercase;
font-style: italic;
font-size: 14px;
}
#container {
line-height: 209%;  /* -------- */
margin-top: 13.96cm;  /* -------- */
margin-left: 4.70cm;  /* -------- */ 
width: 22.5cm;
}
#container p { margin: 0; padding: 0; }
#container span.hide {
color: #aaa;
text-transform: none;
font-size: 18px;
visibility: hidden;
}
	
.fline1:first-line {
padding-left: 4em;
margin-left: 4em;
background: #e0a;
}
	</style>
</head>

<body>


<%
	String parish = infoStudentCurricularPlan.getInfoStudent().getInfoPerson().getFreguesiaNaturalidade();
	String subDivision = infoStudentCurricularPlan.getInfoStudent().getInfoPerson().getConcelhoNaturalidade();
	String birth = parish.equals(subDivision) ? parish : parish + " - " + subDivision;
%>



<div id="container">

<span class="hide">Fa�o saber que ao licenciado</span> <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/><br/>
<span class="hide">Filho de</span> <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nomePai"/><br/>
<span class="hide">e de</span> <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nomeMae"/><br/>
<span class="hide">natural de</span> <%= birth %> <span class="hide">tendo frequentado com aproveitamento</span><br/>
<span class="hide">o curso de mestrado em</span> <bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/><br/>
<span class="hide">no Instituto Superior T�cnico desta Universidade e defendido, perante um j�ri legalmente constitu�do, a disserta-</span><br/>

<p style="margin-bottom: 1.00cm; position: relative;">
	<span class="hide">��o com o t�tulo &nbsp;&nbsp;</span>
	<span style="position: absolute; left: 0; top: -3px;">
		&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
		<bean:write name="infoThesisDataVersion" property="dissertationTitle"/>
	</span>
</p>

 
<p style="margin:0; padding:0; position: relative;">
		<span style="position: absolute; top: -2px;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
			 <bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>
		</span>
	<span style="margin-left: 4.7cm;" class="hide">lhe foi concebido em,</span>
	<span style="width: 14em; position: absolute; top: -3px;"><bean:write name="<%= SessionConstants.CONCLUSION_DATE%>" /></span>
	<span style="padding-left: 11em;" class="hide">o grau de mestre em &nbsp;&nbsp;</span> 
	<span style="margin:0; padding:0; width: 20em; position: absolute; right: 5.6cm; top: 1.8em;"><span class="hide"> com a qualifica��o de &nbsp;</span><span> <bean:message name="<%= SessionConstants.FINAL_RESULT %>" bundle="ENUMERATION_RESOURCES"/></span></span>
</p>

<p style="margin-top: 0.80cm;"><span class="hide">que, em conformidade com as disposi��es legais em vigor, lhe mandei passar a presente carta de curso</span></p>
<p style="margin-top: 0.38cm; margin-left: 3.8cm;"><span class="hide">Universidade T�CNICA de Lisboa, em</span>&nbsp;&nbsp; <%= java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG, new java.util.Locale("pt", "PT")).format(new java.util.Date()) %><br/></p>
<p style="visibility: hidden;">cutting problem solved</p>

</div>



</body>

</html>

 
