<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<table class="tstyle2 tdtop">
	<tr>
		<td><bean:message key="label.teacher" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="teacherInquiry" property="teacherDTO.name" /></td>		
	</tr>
	<tr>
		<td><bean:message key="label.typeOfClass" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:message name="teacherInquiry" property="shiftType.name" bundle="ENUMERATION_RESOURCES"/></td>
	</tr>
</table>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<h3 class="separator2 mtop2"><span style="font-weight: normal ;"><bean:message key="title.inquiries.separator.teachers_1" bundle="INQUIRIES_RESOURCES"/></span></h3>

<div class="forminline dinline">
	<div class="relative">
		<fr:form action="/studentInquiry.do">
			<html:hidden property="method" value="fillTeacherInquiry"/>
		
			<fr:edit name="inquiryBean" id="inquiryBean" visible="false"/>
			<fr:edit name="teacherInquiry" id="teacherInquiry" visible="false" />
			
			<logic:iterate id="inquiryBlockDTO" name="teacherInquiry" property="teacherInquiryBlocks">
				<logic:notEmpty name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader">
					<h4 class="mtop15 mbottom05"><fr:view name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader.title"/></h4>
				</logic:notEmpty>				
				<logic:iterate id="inquiryGroup" name="inquiryBlockDTO" property="inquiryGroups"indexId="index">
					<fr:edit id="<%= "iter" + index --%>" name="inquiryGroup"/>
				</logic:iterate>
			</logic:iterate>
									
			<p class="mtop025 mbottom15"><em><bean:message key="message.inquiries.requiredFieldsMarkedWithAsterisk" bundle="INQUIRIES_RESOURCES"/></em></p>
			<html:submit styleClass="bright"><bean:message key="button.continue" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
		
		<fr:form action="/studentInquiry.do?method=showTeachersToAnswer">
			<fr:edit name="inquiryBean" id="inquiryBean" visible="false"/>
			<html:submit styleClass="bleft"><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>		
		<br/>
	</div>
</div>

<style>

input.bright { position: absolute; bottom: 0; left: 70px; }

.question {
border-collapse: collapse;
margin: 10px 0;
width: 900px;
}
.question th {
padding: 5px 10px;
font-weight: normal;
text-align: left;
border: none;
border-top: 1px solid #ccc;
border-bottom: 1px solid #ccc;
background: #f5f5f5;
vertical-align: bottom;
}
.question td {
padding: 5px;
text-align: center;
border: none;
border-bottom: 1px solid #ccc;
border-top: 1px solid #ccc;
background-color: #fff;
}

th.firstcol {
width: 300px;
text-align: left;
}

.q1col td { text-align: left; }

.q9col .col1, .q9col .col9  { width: 30px; }
.q10col .col1, .q10col .col2, .q10col .col10  { width: 20px; }
.q11col .col1, .q11col .col2, .q11col .col3, .q11col .col11  { width: 20px; }


th.col1, th.col2, th.col3, th.col4, th.col5, th.col6, th.col7, th.col8, th.col9, th.col10, th.col11 {
text-align: center !important;
}

</style>