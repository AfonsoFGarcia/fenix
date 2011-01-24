<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries.student" bundle="INQUIRIES_RESOURCES"/></h2>

<h3 class="separator2 mtop2">
	<span style="font-weight: normal ;"><bean:message key="title.inquiries.separator.teachers" bundle="INQUIRIES_RESOURCES"/>:</span>
	<b><bean:write name="inquiryBean" property="inquiryRegistry.executionCourse.nome" /></b>
</h3>

<logic:notEmpty name="inquiryBean" property="teachersInquiries">
	<p class="mtop2"><bean:message key="message.inquiries.atentionBeforeFillInTeachers" bundle="INQUIRIES_RESOURCES"/>:</p>
	<ul class="mbottom15">
		<li><bean:message key="message.inquiries.theFillingIsOptional" bundle="INQUIRIES_RESOURCES"/></li>
		<li><bean:message key="message.inquiries.fillOnlyIfAttendedEnoughClasses" bundle="INQUIRIES_RESOURCES"/></li>
	</ul>
	
	<table class="tstyle1 thlight mtop05">
		<tr>
			<th></th>
			<th><bean:message key="label.teacher" bundle="INQUIRIES_RESOURCES"/></th>	
			<th><bean:message key="label.typeOfClass" bundle="INQUIRIES_RESOURCES"/></th>
		</tr>
		<logic:iterate id="teacherInquiry" name="inquiryBean" property="teachersInquiries">
				<tr>
					<td class="acenter">
						<logic:present name="teacherInquiry" property="key.personID">
							<bean:define id="personID" name="teacherInquiry" property="key.personID"/>
							<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
						</logic:present>					
					</td>
					<td>				
						<bean:write name="teacherInquiry" property="key.name"/>
					</td>
					<td>
						<table class="tstyle2 thwhite thleft tdleft width100">					
							<logic:iterate id="teacherInquiryByShift" name="teacherInquiry" property="value">
								<tr>
									<th style="width: 80px;">
										<bean:message name="teacherInquiryByShift" property="shiftType.name" bundle="ENUMERATION_RESOURCES"/>
									</th>
									<td>
										<fr:form action="/studentInquiry.do?method=showTeacherInquiry">
											<fr:edit name="teacherInquiryByShift" id="teacherInquiry" visible="false"/>
											<fr:edit name="inquiryBean" id="inquiryBean" visible="false"/>
											<html:submit><bean:message key="link.inquiries.answer" bundle="INQUIRIES_RESOURCES"/></html:submit>
										</fr:form>								
									</td>
									<td>
										<c:if test="${teacherInquiryByShift.filled}"><span class="success0 smalltxt"><bean:message key="label.filled" bundle="INQUIRIES_RESOURCES"/></span></c:if>
									</td>
								</tr>
							</logic:iterate>
						</table>
					</td>
				</tr>
		</logic:iterate>
	</table>
	
	<p class="mtop2"><bean:message key="message.inquiries.submitInquiryWhenFinnish" bundle="INQUIRIES_RESOURCES"/></p>
</logic:notEmpty>

<logic:empty name="inquiryBean" property="teachersInquiries">
	<p class="mvert15"> 
		[Frase a explicar porque n�o aparecem docentes]
	</p>
	<p class="mtop1 mbottom2"> 
		Ap�s a submiss�o n�o ser� poss�vel alterar as respostas. As respostas submetidas ser�o guardadas sem qualquer liga��o � identifica��o do aluno.
	</p> 
</logic:empty>

<p class="mbottom0">
	<div class="forminline dinline">
		<fr:form action="/studentInquiry.do?method=showCurricularInquiry">
			<fr:edit name="inquiryBean" id="inquiryBean" visible="false"/>
			<html:submit><bean:message key="button.backWithArrow" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>	
		<fr:form action="/studentInquiry.do?method=showPreview">
			<fr:edit name="inquiryBean" id="inquiryBean" visible="false"/>
			<html:submit><bean:message key="button.submitInquiry" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
	</div>
</p>
