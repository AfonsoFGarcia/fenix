<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<style>
.thtop { vertical-align: top; }
.biggerTextarea textarea { width: 400px; height: 100px; }
.biggerInputText input[type="text"] { width: 400px !important; }
.smallerInputText input[type="text"] { width: 50px !important; }
</style>


<em><bean:message key="title.teacherPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.teachingInquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<table class="tstyle2 tdtop">
	<tr>
		<td><bean:message key="label.curricularCourse.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="teachingInquiry" property="professorship.executionCourse.nome" /></td>
	</tr>
</table>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<div class="forminline dinline">
	<div class="relative">
		<fr:form action="/teachingInquiry.do?method=showInquiries3rdPage">

			<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.cuEvaluationMethod" bundle="INQUIRIES_RESOURCES"/></h4>
			<bean:message key="subtitle.teachingInquiries.cuEvaluationMethod" bundle="INQUIRIES_RESOURCES"/>
			<div class="smallerInputText">
				<fr:edit name="teachingInquiry" property="secondPageFourthBlock" />
			</div>
			<div class="biggerInputText">
				<fr:edit name="teachingInquiry" property="secondPageFourthBlockThirdPart" />
			</div>
			

			<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.cuGlobalEvaluation" bundle="INQUIRIES_RESOURCES"/></h4>
			<div class="biggerInputText">
				<fr:edit name="teachingInquiry" property="secondPageFifthBlockFirstPart" />
			</div>
			<fr:edit name="teachingInquiry" property="secondPageFifthBlockSecondPart" />

			<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.cuStudentsCompetenceAcquisitionAndDevelopmentLevel" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="teachingInquiry" property="secondPageSixthBlock" />

			<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.globalClassificationOfThisCU" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="teachingInquiry" property="secondPageSeventhBlock" />

			<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.cuTeachingProcess" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="teachingInquiry" property="secondPageEighthBlock" >
				<fr:layout name="tabular-editable" >
					<fr:property name="columnClasses" value="thtop,biggerTextarea,,,,,,"/>
				</fr:layout>		
			</fr:edit>				

			<fr:edit name="teachingInquiry" id="teachingInquiry" visible="false"/>
		
			<html:submit styleClass="bright"><bean:message key="button.continue" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
		
		<br/>
		
		<fr:form action="/teachingInquiry.do?method=showInquiries1stPage">
			<fr:edit name="teachingInquiry" id="teachingInquiry" visible="false"/>
			<html:submit styleClass="bleft"><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>		
		
		<br/>
	</div>
</div>	