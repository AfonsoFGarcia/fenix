<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>

<link href="<%= request.getContextPath() %>/CSS/inquiries_style.css" rel="stylesheet" type="text/css" />

<p class="center">
	<bean:message key="title.inquiries.GEP" bundle="INQUIRIES_RESOURCES"/>
</p>
<h2 class="center caps">
	<bean:message key="title.inquiries.course.evaluation" bundle="INQUIRIES_RESOURCES"/>
</h2>
<h3 class="center caps">
	<bean:message key="title.inquiries.student.inquiry" bundle="INQUIRIES_RESOURCES"/>
</h3>

<br/>


<logic:present name='<%= InquiriesUtil.DEGREE_CURRICULAR_PLANS_LIST %>'>
		<p class="caps"><strong>
			<bean:message key="title.inquiries.edit.reminder" bundle="INQUIRIES_RESOURCES"/>
		</strong></p>
		
		<font class="underline italic">
			<bean:message key="title.inquiries.reminder.sender.email" bundle="INQUIRIES_RESOURCES"/>
		</font>
		&nbsp;
		<bean:message key="message.inquiries.email.reminder.origin.mail" bundle="INQUIRIES_RESOURCES"/>
		<br/>

	<html:form action="/sendEmailReminder">
		<html:hidden property="method" value="sendEmails" />

		<p class="underline italic">
			<bean:message key="title.inquiries.reminder.subject" bundle="INQUIRIES_RESOURCES"/>
		</p>
		<bean:define id="subject">
			<bean:message key="message.inquiries.email.reminder.subject" bundle="INQUIRIES_RESOURCES"/>
		</bean:define>
		<html:text property="bodyTextSubject" styleClass="reminder" value='<%= subject %>' />
			
		<p class="underline italic">
			<bean:message key="title.inquiries.reminder.body" bundle="INQUIRIES_RESOURCES"/>
		</p>
		<bean:define id="bodyIntro">
			<bean:message key="message.inquiries.email.reminder.body.intro" bundle="INQUIRIES_RESOURCES"/>
		</bean:define>
		<html:textarea property="bodyTextIntro" styleClass="reminder" rows="10" value='<%= bodyIntro %>' />
	
		<br/>
		<strong>
			<bean:message key="title.inquiries.reminder.unevaluated.courses" bundle="INQUIRIES_RESOURCES"/>
		</strong>
		<br/>

		<bean:define id="bodyEnd">
			<bean:message key="message.inquiries.email.reminder.body.end" bundle="INQUIRIES_RESOURCES"/>
		</bean:define>
		<html:textarea property="bodyTextEnd" styleClass="reminder" rows="5" value='<%= bodyEnd %>' />
		<br/>
		<br/>

	<p class="caps"><strong>
		<bean:message key="title.inquiries.choose.curricular.plans" bundle="INQUIRIES_RESOURCES"/>
	</strong></p>

		<%--ul style="list-style-type: none;"--%>
		<table>
			<tr>
				<td class="listClasses-header">
					<bean:message key="table.header.curricular.plan" bundle="INQUIRIES_RESOURCES"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="table.header.acronym" bundle="INQUIRIES_RESOURCES"/>
				</td>
				<td class="listClasses-header" />
			</tr>
			

			<logic:iterate id="degreeCurricularPlan" name='<%= InquiriesUtil.DEGREE_CURRICULAR_PLANS_LIST %>' type="net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan">
				<bean:define id="degreeType">
					<bean:write name="degreeCurricularPlan" property="infoDegree.tipoCurso.name" />	
				</bean:define>
				<c:if test="${degreeType == 'DEGREE'}">
					<tr>
						<td class="listClasses">
							<bean:write name="degreeCurricularPlan" property="infoDegree.nome" /> - <bean:write name="degreeCurricularPlan" property="name" />
						</td>
						<td class="listClasses">
							<bean:write name="degreeCurricularPlan" property="infoDegree.sigla" />
						</td>
						<td class="listClasses">
							<html:multibox property="degreeCurricularPlanIds">
								<bean:write name="degreeCurricularPlan" property="idInternal" />
							</html:multibox>
						</td>
					</tr>

				</c:if>
			</logic:iterate>
		</table>		

		<br/>
		<br/>

		<table class="reminder">
			<tr>
				<td align="right">
					<html:submit styleClass="inputbuttonSmall">
						<bean:message key="button.inquiries.send.email.reminder" bundle="INQUIRIES_RESOURCES"/>
					</html:submit>
				</td>
			</tr>
		</table>			


	</html:form>

</logic:present>
