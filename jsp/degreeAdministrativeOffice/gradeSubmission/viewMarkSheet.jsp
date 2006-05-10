<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<em><bean:message key="degree.administrative.office"/></em>
<h2><bean:message key="label.viewMarkSheet"/></h2>

<html:form action="/markSheetManagement.do?method=printMarkSheet">
	
	<html:hidden property="epID" />
	<html:hidden property="dID" />
	<html:hidden property="dcpID" />
	<html:hidden property="ccID"  />	
	<html:hidden property="msID" />
	<html:hidden property="tn" />
	<html:hidden property="ed"/>
	<html:hidden property="mss" />
	<html:hidden property="mst" />
	
	<fr:view name="markSheet" schema="markSheet.view.extended" layout="tabular" />

	<br/>
	<table>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td align="left"><bean:message key="label.evaluationDate"/></td>
			<td align="left"><bean:message key="label.grades"/></td>
			<td align="left">&nbsp;</td>
		</tr>
		<logic:iterate id="enrolmentEvaluation" name="markSheet" property="enrolmentEvaluationsSortedByStudentNumber" type="net.sourceforge.fenixedu.domain.EnrolmentEvaluation">
			<tr>
				<td>
					<bean:write name="enrolmentEvaluation" property="student.person.name"/>
				</td>
				<td>
					(L<bean:write name="enrolmentEvaluation" property="student.number"/>)
				</td>
				<td align="left">
                    <dt:format pattern="dd-MM-yyyy">
						<bean:write name="enrolmentEvaluation" property="examDate.time"/>
                    </dt:format>
				</td>
				<td align="left">
					<bean:write name="enrolmentEvaluation" property="grade"/>
				</td>
				<td align="left">
					<% if(enrolmentEvaluation.getEnrolmentEvaluationState() == net.sourceforge.fenixedu.util.EnrolmentEvaluationState.RECTIFIED_OBJ) { %>
						<html:link action='<%= "/markSheetManagement.do?method=prepareViewRectificationMarkSheet"%>' paramId="eeID" paramName="enrolmentEvaluation" paramProperty="idInternal">
							<bean:message key="label.rectified"/>
						</html:link>
					<% } %>
				</td>
			</tr>
		</logic:iterate>
	</table>

	<br/><br/>
	<html:cancel styleClass="inputbutton"><bean:message key="label.back"/></html:cancel>
	<html:submit styleClass="inputbutton"><bean:message key="label.print"/></html:submit>
	
</html:form>
