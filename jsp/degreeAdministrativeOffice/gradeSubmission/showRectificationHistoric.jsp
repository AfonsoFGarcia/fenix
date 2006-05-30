<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message key="label.markSheet.rectificationHistoric"/></h2>

<fr:view name="enrolmentEvaluation" property="markSheet"
		schema="markSheet.view"
		layout="tabular">
</fr:view>

<h3><bean:write name="enrolmentEvaluation" property="enrolment.studentCurricularPlan.student.person.name"/> (<bean:write name="enrolmentEvaluation" property="enrolment.studentCurricularPlan.student.number"/>)</h3>

<html:errors/>
<logic:messagesPresent message="true">
	<html:messages id="messages" message="true">
		<span class="error0"><bean:write name="messages" /></span>
	</html:messages>
	<br/><br/>
</logic:messagesPresent>

<logic:present name="enrolmentEvaluation">
	<table>
	  <tr>
	    <td><bean:message key="label.markSheet.original.grade"/></td>
	    <td><bean:write name="enrolmentEvaluation" property="grade"/> (<dt:format pattern="dd/MM/yyyy"><bean:write name="enrolmentEvaluation" property="examDate.time"/></dt:format>)</td>
	  </tr>
	  <logic:iterate id="evaluation" name="rectificationEvaluations">
		  <tr>
		    <td><bean:message key="label.markSheet.rectification"/></td>
		    <td>
		    	<bean:write name="evaluation" property="grade"/> (<dt:format pattern="dd/MM/yyyy"><bean:write name="evaluation" property="examDate.time"/></dt:format>)<br/>
  			    	<logic:notEmpty name="evaluation" property="markSheet.reason">
	 				<bean:message key="label.markSheet.reason"/>:
			    	<bean:write name="evaluation" property="markSheet.reason"/>
		    	</logic:notEmpty>
		    </td>
		  </tr>  
	  </logic:iterate>
	</table>
	
	<html:form action="/rectifyMarkSheet.do">
		<html:hidden property="method" value="prepareRectifyMarkSheet" />
		<html:hidden name="markSheetManagementForm" property="epID" />
		<html:hidden name="markSheetManagementForm" property="dID" />
		<html:hidden name="markSheetManagementForm" property="dcpID" />
		<html:hidden name="markSheetManagementForm" property="ccID"  />			
		<html:hidden name="markSheetManagementForm" property="tn" />
		<html:hidden name="markSheetManagementForm" property="ed"/>
		<html:hidden name="markSheetManagementForm" property="mss" />
		<html:hidden name="markSheetManagementForm" property="mst" />
		
		<bean:define id="markSheetID" name="enrolmentEvaluation" property="markSheet.idInternal" />
		<html:hidden property="msID" value="<%= markSheetID.toString() %>"/>
		<br/>
		<html:submit styleClass="inputbutton"><bean:message key="label.back"/></html:submit>
	</html:form>
	
</logic:present>
