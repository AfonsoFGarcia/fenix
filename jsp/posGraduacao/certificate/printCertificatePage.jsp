<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" scope="session" />
 <table width="100%" border='1' >
	  <tr><td>&nbsp;</td></tr>
	  <tr><td>     O/A CHEFE DE SEC��O DE P�S-GRADUA��O DO INSTITUTO SUPERIOR T�CNICO DA UNIVERSIDADE T�CNICA DE LISBOA 
	     CERTIFICA, a requerimento do interessado que do seu processo individual organizado e arquivado nesta secretaria, consta que:
	   </td></tr>
      
	<%--Certificate --%>
	<jsp:include page="./certificateTemplate1.jsp" flush="true" />

	<logic:present name="<%= SessionConstants.MATRICULA%>">
		<jsp:include page="./certificateTemplate2.jsp" flush="true" />
	</logic:present>
	
	<logic:present name="<%= SessionConstants.MATRICULA_ENROLMENT%>">
		<jsp:include page="./certificateTemplate2.jsp" flush="true" />
	</logic:present>
	
	<logic:present name="<%= SessionConstants.DURATION_DEGREE%>">
    	  <logic:equal name="infoStudentCurricularPlan" property="specialization" value="Mestrado">
    	  	 <jsp:include page="./certificateTemplate2.jsp" flush="true" />
    	 	 <jsp:include page="./certificateTemplate3.jsp" flush="true" />
    	  </logic:equal >	
    	  <jsp:include page="./certificateTemplate2.jsp" flush="true" />	  
	</logic:present>

	 <logic:present name="<%= SessionConstants.ENROLMENT%>">
		<jsp:include page="./certificateTemplate2.jsp" flush="true" />
		<tr>
		<td>
		
		nas seguintes disciplinas:
		</td>
	    </tr>
	    
	</logic:present>
	
	<logic:present name="<%= SessionConstants.DOCUMENT_REASON_LIST%>">
		<jsp:include page="./templateDocumentReason.jsp" flush="true" />
    </logic:present>
    
	<%-- Date --%>
	<jsp:include page="./templateFinal.jsp" flush="true" />
	
</table>
	 Aluno <bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/>
	
	
	
	<%--<logic:equal name="infoStudentCurricularPlan" property="specialization" value="Mestrado">
    	<%-- Candidate Information if necessary --%>
   		<%--<jsp:include page="./declarationTemplate2.jsp" flush="true" />
	</logic:equal >	

	<jsp:include page="./declarationTemplate3.jsp" flush="true" />--%>
	  	