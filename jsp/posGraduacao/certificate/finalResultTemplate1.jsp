<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoFinalResult" name="<%= SessionConstants.INFO_FINAL_RESULT%>" />
<bean:define id="conclusionDate" name="<%= SessionConstants.CONCLUSION_DATE %>" scope="session" />
<bean:define id="date" name="<%= SessionConstants.DATE %>" scope="session" />
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN%>" />

 <table width="80%" border='1'>
 <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
 <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr> <td>M�dia ponderada <bean:write name="infoFinalResult" property="averageWeighted" /></td>
    <td align="center" >O coordenador do curso, </td>
    </tr>
    <tr> <td>M�dia simples <bean:write name="infoFinalResult" property="averageSimple" /></td>
    <td align="center" >&nbsp;</td>
    </tr>
    <tr><td>Resultado final <bean:write name="infoFinalResult" property="finalAverage" /> valores</td>
    <td align="center" >_____________________________ </td>
    </tr>
 </table>   
 <table width="80%" border='1'>

    <tr><td>INFORMA��O:</td></tr>
    <tr><td>Concluiu a parte escolar do curso de <bean:write name="infoStudentCurricularPlan" property="specialization"/> em <bean:write name="conclusionDate"/> com a m�dia final de 
    <bean:write name="infoFinalResult" property="finalAverage" /> valores.</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr><td align='center'>� considera��o de V.Ex�.</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr><td>Secretaria dos Servi�os Acad�micos, em <bean:write name="date"/></td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr><td align='right'>______________________________________</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Homologo</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;_____________________________</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr><td>O Aluno n� <bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/></td></tr>
 </table>   
 
