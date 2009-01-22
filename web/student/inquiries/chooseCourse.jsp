<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<logic:present name="executionSemester" property="inquiryResponsePeriod">
	<logic:notEmpty name="executionSemester" property="inquiryResponsePeriod.introduction">	
		<div>
			<bean:write name="executionSemester" property="inquiryResponsePeriod.introduction" filter="false"/>
		</div>
	</logic:notEmpty>
</logic:present>

<h3 class="separator2 mtop2"><span style="font-weight: normal ;"><bean:message key="title.inquiries.separator.introduction" bundle="INQUIRIES_RESOURCES"/></span></h3>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<c:if test="${student.weeklySpentHoursSubmittedForOpenInquiriesResponsePeriod}">

	<bean:message key="label.weeklySpentHours" bundle="INQUIRIES_RESOURCES"/>: <b><c:out value="${student.openInquiriesStudentExecutionPeriod.weeklyHoursSpentInClassesSeason}" /></b> <bean:message key="label.hoursPerWeek.a" bundle="INQUIRIES_RESOURCES"/>

	<fr:view name="courses" schema="curricularCourseInquiriesRegistryDTO.submitHoursAndDays" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight tdcenter tdwith90px"/>
			<fr:property name="columnClasses" value="nowrap aleft,,,,,,acenter nowrap"/>
			<fr:property name="suffixes" value=",,h,%,dias,,"/>
			<fr:property name="linkGroupSeparator" value=" | "/>
			
			<fr:property name="linkFormat(answerNow)" value="/studentInquiry.do?method=showInquiries1stPage&amp;inquiriesRegistryID=${inquiriesRegistry.idInternal}" />
			<fr:property name="key(answerNow)" value="link.inquiries.answerNow"/>
			<fr:property name="bundle(answerNow)" value="INQUIRIES_RESOURCES"/>
			<fr:property name="contextRelative(answerNow)" value="true"/>      
			<fr:property name="order(answerNow)" value="2"/>
			<fr:property name="visibleIf(answerNow)" value="inquiriesRegistry.toAnswerLater"/>

<%-- 
--%> 			
			<fr:property name="linkFormat(dontRespond)" value="/studentInquiry.do?method=showJustifyNotAnswered&amp;inquiriesRegistryID=${inquiriesRegistry.idInternal}" />
			<fr:property name="key(dontRespond)" value="link.inquiries.dontRespond"/>
			<fr:property name="bundle(dontRespond)" value="INQUIRIES_RESOURCES"/>
			<fr:property name="contextRelative(dontRespond)" value="true"/>      
			<fr:property name="order(dontRespond)" value="1"/>
			<fr:property name="visibleIf(dontRespond)" value="inquiriesRegistry.toAnswerLater"/>
			
			<fr:property name="visibleIf(notAnswered)" value="inquiriesRegistry.notAnswered"/>
			<fr:property name="customLink(notAnswered)" >
				<em><bean:message key="label.notAnswered" bundle="INQUIRIES_RESOURCES" /></em>
			</fr:property>
			
			<fr:property name="visibleIf(answered)" value="inquiriesRegistry.answered"/>
			<fr:property name="customLink(answered)" >
				<span class="success0"><bean:message key="label.answered" bundle="INQUIRIES_RESOURCES" /></span>
			</fr:property>
			
			<fr:property name="visibleIf(notAvailableToInquiries)" value="inquiriesRegistry.notAvailableToInquiries"/>
			<fr:property name="customLink(notAvailableToInquiries)" >
				<em><bean:message key="label.notAvailableToInquiries" bundle="INQUIRIES_RESOURCES" /></em>
			</fr:property>			
			
		</fr:layout>
	</fr:view>

</c:if>

<c:if test="${!student.weeklySpentHoursSubmittedForOpenInquiriesResponsePeriod}">

<p>Considera-se como <b>trabalho aut�nomo</b> o tempo dedicado por um estudante na pesquisa, estudo, elabora��o individual e colectiva de trabalhos (trabalho de
campo, resolu��o de problemas, estudos de caso, desenvolvimento de projectos, etc.), ou seja, todo o trabalho desenvolvido pelo aluno no �mbito das unidades
curriculares em que se encontra inscrito no semestre fora das aulas (horas de contacto).</p>
<b>Exemplos:</b><br/>
<table cellpadding="10">
<tr>
<th valign="top">
UC1
</th>
<td>
avalia��o por projectos (2) + testes (2)<br/>
	                das 14 semanas de aulas,<br/>
	                3 semanas foram devotadas aos projectos (4h por dia) --> 4h * 6dias * 3 semanas (=72)<br/>
	                estudaram para os testes 3h/semana ou estudaram intensamente na semana do teste<br/>
	                <b>Totalizando o trabalho aut�nomo para esta UC = 72h/14 + 3h = <span style="color: red">8,1h/semana</span></b>
</td>
</tr>
<tr>
<th valign="top">
UC2
</th>
<td>
avalia��o por testes + exame<br/>
	                estudaram para os testes 3,5h/semana ou estudaram intensamente na semana do teste.<br/>
	                <b>Totalizando o trabalho aut�nomo tem-se para esta UC <span style="color: red">3,5h/semana</span>.</b>
</td>	                
<tr>
<th valign="top">
UC3
</th>
<td>
.......
</td>
</table>

<p><b>NHTA = <span style="color: red">soma trabalho aut�nomo para todas as UC</span></b></p>

	<fr:form action="/studentInquiry.do">
		<html:hidden property="method" value="submitWeeklySpentHours"/>

		<bean:message key="label.weeklySpentHours" bundle="INQUIRIES_RESOURCES"/>: 
		<fr:edit id="weeklySpentHours" name="weeklySpentHours" schema="inquiriesStudentExecutionPeriod.submitWeeklySpentHours" type="net.sourceforge.fenixedu.dataTransferObject.VariantBean" >
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
				<fr:property name="validatorClasses" value="error0"/>
			</fr:layout>
		</fr:edit>			
		 <bean:message key="label.hoursPerWeek" bundle="INQUIRIES_RESOURCES"/>

		<fr:edit id="hoursAndDaysByCourse" name="courses" schema="curricularCourseInquiriesRegistryDTO.submitHoursAndDays.edit" >
			<fr:layout name="tabular-editable" >
				<fr:property name="classes" value="tstyle1 thlight tdcenter tdwith90px"/>
				<fr:property name="columnClasses" value="nowrap aleft,,,,,"/>
				<fr:property name="suffixes" value=",h,%,dias,,,"/>
				<fr:property name="validatorClasses" value="error0"/>
				<fr:property name="hideValidators" value="false"/>
			</fr:layout>
		</fr:edit>
		
		<p><bean:message key="message.inquiries.estimatedECTS" bundle="INQUIRIES_RESOURCES"/></p><br/>

		<html:submit><bean:message key="button.submit" bundle="INQUIRIES_RESOURCES"/></html:submit>	
	</fr:form>
</c:if>
