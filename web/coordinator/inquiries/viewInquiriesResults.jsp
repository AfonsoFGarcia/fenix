<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:xhtml />

<fmt:setBundle basename="resources.InquiriesResources" var="INQUIRIES_RESOURCES"/>

<h2><bean:message key="title.inquiries.resultsWithDescription" bundle="INQUIRIES_RESOURCES"/></h2>

<p class="separator2 mtop2"><b><bean:message key="title.inquiries.studentResults" bundle="INQUIRIES_RESOURCES"/></b></p>
<bean:define id="courseResult" name="studentInquiriesCourseResult" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiriesCourseResultBean"/>
    <table>
        <tr>
            <td valign="top">
            	<bean:message key="link.teachingInquiries.cuResults" bundle="INQUIRIES_RESOURCES"/> - 
            </td>
            <td valign="top">
                <html:link href="<%= request.getContextPath() + "/teacher/teachingInquiry.do?method=showInquiryCourseResult&resultId=" + courseResult.getStudentInquiriesCourseResult().getIdInternal() %>" target="_blank">            
            		<strong><bean:write name="courseResult" property="studentInquiriesCourseResult.executionCourse.nome" /> -
            		<bean:write name="courseResult" property="studentInquiriesCourseResult.executionDegree.degreeCurricularPlan.name" /></strong>
            	</html:link>
                - <c:out value="${executionCourse.executionPeriod.qualifiedName}" />
                <br/>
                <bean:define id="executionCourseLink"><c:out value="${pageContext.request.contextPath}" /><c:out value="${executionCourse.site.reversePath}" />/pagina-inicial</bean:define>
                <!-- NO_CHECKSUM --><!-- HAS_CONTEXT --><html:link href="<%= executionCourseLink %>" target="_blank">
                    <em><bean:message key="link.curricularUnit.website" bundle="INQUIRIES_RESOURCES"/></em>
                </html:link>
            
            </td>
        </tr>
    </table>

<logic:notEmpty name="courseResult" property="studentInquiriesTeachingResults">
	<ul>
		<logic:iterate id="teachingResult" name="courseResult" property="studentInquiriesTeachingResults" type="net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult">
			<li>
                <html:link href="<%= request.getContextPath() + "/teacher/teachingInquiry.do?method=showInquiryTeachingResult&resultId=" + teachingResult.getIdInternal() %>" target="_blank">            
					<bean:write name="teachingResult" property="professorship.person.name" />
					&nbsp;(<bean:message name="teachingResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/>)<br/>
                </html:link>
			</li>
		</logic:iterate>
	</ul>
</logic:notEmpty>

<p class="separator2 mtop25"><b><bean:message key="title.teachingInquiries.resultsToImprove" bundle="INQUIRIES_RESOURCES"/></b></p>

<table class="tstyle1 thlight thleft tdcenter">
    <tr class="top">
        <th class="aright">Organiza��o da UC <a href="#" class="help">[?] <span>Resultados a melhorar se mais de 25% alunos (no m�nimo de 10 respostas) classifica como abaixo ou igual a 3 (Discordo) 2 das 4 quest�es do grupo. - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
        <th class="aright">Avalia��o da UC <a href="#" class="help">[?] <span>Resultados a melhorar se mais 25% alunos (no m�nimo de 10 respostas) classifica como abaixo ou igual a 3 (Discordo) a quest�o e/ou taxa de avalia��o <50% e/ou taxa de aprova��o <50%. - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
        <th class="aright">Pass�vel de Auditoria <a href="#" class="help">[?] <span>Se os grupos da organiza��o e avalia��o da UC apresentarem ambos resultados a melhorar e, pelo menos, metade do corpo docnete (pares docente/tipo de aulas) apresentar resultados a melhorar no m�nimo de dois grupos - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
    </tr>
    <tr>
        <td><bean:message key="<%= "label.colored." + courseResult.getStudentInquiriesCourseResult().getUnsatisfactoryResultsCUOrganization().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
        <td><bean:message key="<%= "label.colored." + courseResult.getStudentInquiriesCourseResult().getUnsatisfactoryResultsCUEvaluation().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
        <td><bean:message key="<%= "label.colored." + courseResult.getStudentInquiriesCourseResult().getAuditCU().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
    </tr>
</table>

<logic:notEmpty name="courseResult" property="studentInquiriesTeachingResults">
    <table class="tstyle1 thlight tdcenter">
        <tr>        
            <th class="nowrap"><bean:message key="label.teacher" bundle="INQUIRIES_RESOURCES"/></th>
            <th><bean:message key="label.typeOfClass" bundle="INQUIRIES_RESOURCES"/></th>
            <th><bean:message key="label.teachingInquiries.unsatisfactoryResultsAssiduity" bundle="INQUIRIES_RESOURCES"/> <a href="#" class="help">[?] <span>Resultados a melhorar se mais de 25% classifica como abaixo ou igual a 3 (De vez em quando) - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
            <th><bean:message key="label.teachingInquiries.unsatisfactoryResultsPresencialLearning" bundle="INQUIRIES_RESOURCES"/> <a href="#" class="help">[?] <span>Resultados a melhorar se, entre os alunos que frequentaram as aulas (alunos que responderam igual so superior a 3 na pergunta da assiduidade �s aulas, no m�nimo de 10), mais 25% classifica como abaixo ou igual a 3 (Discordo) 2 das 3 quest�es do grupo. - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
            <th><bean:message key="label.teachingInquiries.unsatisfactoryResultsPedagogicalCapacity" bundle="INQUIRIES_RESOURCES"/> <a href="#" class="help">[?] <span>Resultados a melhorar se, entre os alunos que frequentaram as aulas (alunos que responderam igual so superior a 3 na pergunta da assiduidade �s aulas, no m�nimo de 10), mais 25% classifica como abaixo ou igual a 3 (Discordo) 2 das 3 quest�es do grupo. - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
            <th><bean:message key="label.teachingInquiries.unsatisfactoryResultsStudentInteraction" bundle="INQUIRIES_RESOURCES"/> <a href="#" class="help">[?] <span>Resultados a melhorar se, entre os alunos que frequentaram as aulas (alunos que responderam igual so superior a 3 na pergunta da assiduidade �s aulas, no m�nimo de 10), mais 25% classifica como abaixo ou igual a 3 (Discordo) 2 das 3 quest�es do grupo. - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
            <c:if test="${!isToImproove}">
                <th><bean:message key="label.teachingInquiries.unsatisfactoryResultsAuditable" bundle="INQUIRIES_RESOURCES"/></th>
            </c:if>
        </tr>
        <logic:iterate id="teachingResult" name="courseResult" property="studentInquiriesTeachingResults" type="net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult">
            <tr>        
                <td class="aleft nowrap"><c:out value="${teachingResult.professorship.person.name}" /></td>
                <td><bean:message name="teachingResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/></td>
                <td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsAssiduity().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
                <td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsPresencialLearning().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
                <td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsPedagogicalCapacity().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
                <td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsStudentInteraction().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
                <c:if test="${!isToImproove}">
                    <td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsAuditable().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
                </c:if>
            </tr>       
        </logic:iterate>
    </table>
</logic:notEmpty>

<p class="separator2 mtop25"><b><bean:message key="title.teachingInquiries.excellentResults" bundle="INQUIRIES_RESOURCES"/></b></p>

<logic:notEmpty name="courseResult" property="studentInquiriesTeachingResults">
    <table class="tstyle1 thlight tdcenter">
        <tr>        
            <th class="nowrap"><bean:message key="label.teacher" bundle="INQUIRIES_RESOURCES"/></th>
            <th><bean:message key="label.typeOfClass" bundle="INQUIRIES_RESOURCES"/></th>
            <th class="aright">Assiduidade dos alunos <a href="#" class="help">[?] <span>Resultados excelentes se mais de 75% classifica como acima ou igual a 3 (De vez em quando) - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
            <th class="aright">Proveito da aprendizagem presencial <a href="#" class="help">[?] <span>Resultados excelentes se, entre os alunos que frequentaram as aulas (alunos que responderam igual so superior a 3 na pergunta da assiduidade �s aulas, no m�nimo de 10), mais de 75% classifica como acima ou igual a 7 (Concordo) todas as quest�es do grupo (excepto a quest�o da assiduidade) e a m�dia de respostas nos outros grupos (Capacidade Pedag�gica e Interac��o com os alunos) superior a 7 (Concordo). - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
            <th class="aright">Capacidade pedag�gica <a href="#" class="help">[?] <span>Resultados excelentes se, entre os alunos que frequentaram as aulas, mais de 75% classifica como acima ou igual a 7 (Concordo) todas as quest�es do grupo e a m�dia de respostas nos outros grupos (Proveito da aprendizagem presencial (excepto a quest�o da assiduidade dos alunos) e Interac��o com os alunos) superior a 7 (Concordo). - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
            <th class="aright">Interac��o com os alunos <a href="#" class="help">[?] <span>Resultados excelentes se, entre os alunos que frequentaram as aulas, mais de 75% classifica como acima ou igual a 7 (Concordo) todas as quest�es do grupo e a m�dia de respostas nos outros grupos (Proveito da aprendizagem presencial (excepto a quest�o da assiduidade dos alunos) e Capacidade pedag�gica) superior a 7 (Concordo). - conforme revis�o do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
        </tr>
        </tr>
        <logic:iterate id="teachingResult" name="courseResult" property="studentInquiriesTeachingResults" type="net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult">
            <tr>
                <td class="aleft nowrap"><c:out value="${teachingResult.professorship.person.name}" /></td>
                <td><bean:message name="teachingResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/></td>
                <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${teachingResult.valuesMap['Res_excelentes_assiduidade']}" /></td>
                <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${teachingResult.valuesMap['Res_excelentes_prov_aprend_pres']}" /></td>
                <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${teachingResult.valuesMap['Res_excelentes_cap_pedag']}" /></td>
                <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${teachingResult.valuesMap['Res_excelentes_int_alunos']}" /></td>
            </tr>
        </logic:iterate>
    </table>
</logic:notEmpty>


<p class="separator2 mtop25"><b><bean:message key="title.inquiries.teachingReports" bundle="INQUIRIES_RESOURCES"/></b></p>

<ul>
	<logic:iterate id="professorship" name="executionCourse" property="professorships" >
		<li>
			<logic:notEmpty name="professorship" property="teachingInquiry">
				<bean:define id="teachingInquiryID" name="professorship" property="teachingInquiry.idInternal" />
				<html:link page="<%= "/viewInquiriesResults.do?method=showFilledTeachingInquiry&filledTeachingInquiryId=" + teachingInquiryID + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID")  %>" target="_blank">
					<bean:write name="professorship" property="person.name"/>
				</html:link>
			</logic:notEmpty>
			<logic:empty name="professorship" property="teachingInquiry">
				<bean:write name="professorship" property="person.name"/>
			</logic:empty>
            <bean:define id="emailAddress" name="professorship" property="person.institutionalOrDefaultEmailAddressValue" />
            <html:link href="<%= "mailto:" + emailAddress %>" target="_blank" style="border: none" titleKey="link.email" bundle="INQUIRIES_RESOURCES">
                <img src="<%=request.getContextPath()%>/images/icon_email.gif"/>
            </html:link>
            
            <logic:present role="PEDAGOGICAL_COUNCIL">
	            <bean:define id="professorshipID" name="professorship" property="oid"/>
		    	<html:link page="<%= "/viewInquiriesResults.do?method=showOthersTeacherCourses&professorshipID=" +  professorshipID%>">
		        	(<bean:message key="label.inquiries.courses.other" bundle="INQUIRIES_RESOURCES"/>)
		        </html:link>
		    </logic:present>
            
		</li>
	</logic:iterate>
</ul>

<div class="infoop8 mtop15">
	<p class="mvert025 color777">
		<em>
			Nota: <bean:message key="message.inquiries.teachers.inquiries.instructions1" bundle="INQUIRIES_RESOURCES"/>
		</em>
	</p>
	<p class="mtop025 color777">
		<em>
			Nota: <bean:message key="message.inquiries.teachers.inquiries.instructions2" bundle="INQUIRIES_RESOURCES"/>
		</em>
	</p>
</div>

<p class="separator2 mtop25"><b><bean:message key="title.inquiries.delegateReports" bundle="INQUIRIES_RESOURCES"/></b></p>
<ul>
    <logic:iterate id="delegateInquiry" name="executionCourse" property="yearDelegateCourseInquiries">
        <li>
            <bean:define id="delegateInquiryID" name="delegateInquiry" property="idInternal" />
            <html:link page="<%= "/viewInquiriesResults.do?method=showFilledYearDelegateInquiry&filledYearDelegateInquiryId=" + delegateInquiryID + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID")  %>" target="_blank">
                <bean:write name="delegateInquiry" property="delegate.registration.student.person.name"/>
            </html:link>
            <bean:define id="emailAddress" name="delegateInquiry" property="delegate.registration.student.person.institutionalOrDefaultEmailAddressValue" />
            <html:link href="<%= "mailto:" + emailAddress %>" target="_blank" style="border: none" titleKey="link.email" bundle="INQUIRIES_RESOURCES">
                <img src="<%=request.getContextPath()%>/images/icon_email.gif"/>
            </html:link>
        </li>
    </logic:iterate>
</ul>

<p class="separator2 mtop25"><b><bean:message key="title.inquiries.courseResults.coordinatorComments" bundle="INQUIRIES_RESOURCES"/></b></p>
<logic:empty name="courseResultsCoordinatorCommentEdit">
    <fr:view name="courseResult" property="studentInquiriesCourseResult" schema="studentInquiriesCourseResult.courseResultsCoordinatorComment" >
        <fr:layout name="tabular">
            <fr:property name="labelTerminator" value=""/>
        </fr:layout>
    </fr:view>
    
    <br/><br/>
    <logic:equal name="canComment" value="true">
        <html:form action="<%= "/viewInquiriesResults.do?method=selectExecutionCourse&courseResultsCoordinatorCommentEdit=true&degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") %>" >
            <html:hidden property="executionCourseID"/>
            <html:hidden property="executionDegreeID"/>
            <html:submit><bean:message key="label.inquiries.courseResults.coordinatorComments.edit" bundle="INQUIRIES_RESOURCES"/></html:submit>
        </html:form>
    </logic:equal>
</logic:empty>
<logic:notEmpty name="courseResultsCoordinatorCommentEdit">
    <fr:edit name="courseResult" property="studentInquiriesCourseResult" schema="studentInquiriesCourseResult.courseResultsCoordinatorComment" >
        <fr:layout>
            <fr:property name="labelTerminator" value=""/>
        </fr:layout>
    </fr:edit>
</logic:notEmpty>
<br/>
<html:form action="<%= "/viewInquiriesResults.do?method=selectexecutionSemester&courseResultsCoordinatorCommentEdit=true&degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") %>" >
    <html:hidden property="degreeCurricularPlanID"/>
    <bean:define id="executionSemesterID" name="executionCourse" property="executionPeriod.oid" type="java.lang.Long" />
    <html:hidden property="executionSemesterID" value="<%= executionSemesterID.toString() %>" />
    <html:submit><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
</html:form>