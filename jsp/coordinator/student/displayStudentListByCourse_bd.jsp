<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
 <%@ page import="java.util.List" %>
<%@ page import="Util.EnrollmentState" %>
<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>
  <span class="error"><html:errors/></span>
  <bean:define id="enrolmentList" name="enrolment_list" scope="request" />
  <bean:define id="link">/studentCurriculum.do?method=getCurriculum<%= "&" %>page=0<%= "&" %>studentCPID=</bean:define>
  <p>
    <h3><%= ((List) enrolmentList).size()%> <bean:message key="label.masterDegree.administrativeOffice.studentsFound"/></h3>        
    <% if (((List) enrolmentList).size() != 0) { %>
        </p>
        <bean:message key="label.masterDegree.chooseOne"/><br><br><br>
        
        <logic:equal name="viewPhoto" value="true">
	        <html:link page="/listStudentsForCoordinator.do?method=getStudentsFromDCP&page=0&amp;viewPhoto=false">
    	    	<bean:message key="label.notViewPhoto"/>
        	</html:link>
        </logic:equal>
        <logic:notEqual name="viewPhoto" value="true">
	        <html:link page="/listStudentsForCoordinator.do?method=getStudentsFromDCP&page=0&amp;viewPhoto=true">
    	    	<bean:message key="label.viewPhoto"/>
        	</html:link>
        </logic:notEqual>
    
        <table>
        	<tr>
        		<logic:equal name="viewPhoto" value="true">
					<td class="listClasses-header"><bean:message key="label.photo" /></td>
			 	</logic:equal>
    			<td class="listClasses-header"><bean:message key="label.candidate.number" /></td>
    			<td class="listClasses-header"><bean:message key="label.person.name" /></td>
    			<td class="listClasses-header"><bean:message key="label.masterDegree.administrativeOffice.mark" /></td>
    			
    		</tr>
     	<logic:iterate id="enrolment" name="enrolmentList">
        	<bean:define id="studentLink">
        		<bean:write name="link"/><bean:write name="enrolment" property="infoStudentCurricularPlan.idInternal"/>&amp;
				<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
					<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
		    			&amp;executionDegreeId=<bean:write name="infoExecutionDegree" property="idInternal"/>&amp;
	    		</logic:present>
        	</bean:define>
        <tr>
        	<logic:equal name="viewPhoto" value="true">
				<td class="listClasses-header">
					<bean:define id="personID" name="enrolment" property="infoStudentCurricularPlan.infoStudent.infoPerson.idInternal"/>
					<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/viewPhoto.do?personCode="+personID.toString()%>"/>
			   </td>
			</logic:equal>
        	<td class="listClasses">
        	<html:link page='<%= pageContext.findAttribute("studentLink").toString() %>'>
    			<bean:write name="enrolment" property="infoStudentCurricularPlan.infoStudent.number"/>
    		</html:link>
            </td>
            <td class="listClasses">
    	        <bean:write name="enrolment" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
    	    </td>
            <td class="listClasses">
    	       	<logic:notEqual name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
					<bean:message name="enrolment" property="enrollmentState.name" bundle="ENUMERATION_RESOURCES" />
				</logic:notEqual>
				
				<logic:equal name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
					<bean:write name="enrolment" property="infoEnrolmentEvaluation.grade"/>
				</logic:equal>
    	    </td>
    	</tr>
        </logic:iterate>
      	</table>    	
   	<% } %>  
