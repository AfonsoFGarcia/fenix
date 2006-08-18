<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.teacher.TeacherAdministrationViewerDispatchAction"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.messaging.ExecutionCourseAliasExpandingAction"%>

<ul>
	<li>
		<html:link page="/executionCourse.do?method=firstPage"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.inicialPage"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=announcements"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.announcements"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=summaries"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.summaries"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=evaluationMethod"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.evaluationMethod"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=bibliographicReference"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.bibliography"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=schedule"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="label.schedule"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=shifts"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.shifts"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=evaluations"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.evaluations"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=groupings"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.groupings"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=section"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.section"/>
		</html:link>
	</li>
	<logic:notEmpty name="executionCourse" property="site.associatedSections">
		<logic:iterate id="section" name="executionCourse" property="site.orderedTopLevelSections">
			<li>
				<html:link page="/executionCourse.do?method=section"
						paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
					<bean:write name="section" property="name"/>
				</html:link>
			</li>
			<bean:define id="subSections" name="section" property="orderedSubSections"/>
			<logic:notEmpty name="subSections">
				<li>
					<dl>
						<logic:iterate id="subSection" name="subSections">
							<dd style="padding: 0pt 0pt 0pt 20px;">
								<html:link page="/executionCourse.do?method=section"
										paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
									<bean:write name="subSection" property="name"/>
								</html:link>
							</dd>
						</logic:iterate>
					</dl>
				</li>
			</logic:notEmpty>
		</logic:iterate>
	</logic:notEmpty>
	<li>
		<bean:define id="imageURL" type="java.lang.String">
			background: url(<%= request.getContextPath() %>/images/rss_ico.gif) 10px 3px no-repeat; padding-left: 32px;
		</bean:define>
		<html:link page="/executionCourse.do?method=rss" style="<%=imageURL%>"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.rss"/>
		</html:link>
	</li>
</ul>

<logic:equal name="executionCourse" property="site.dynamicMailDistribution" value="true">
	<%
		StringBuffer buffer = new StringBuffer();
		buffer.append(ExecutionCourseAliasExpandingAction.emailAddressPrefix);
		buffer.append(request.getParameter("objectCode")).append("@");
		buffer.append(TeacherAdministrationViewerDispatchAction.mailingListDomainConfiguration());
	%>
	<bean:define id="advisoryText">
		<bean:message  key="send.email.dynamicMailDistribution.link" bundle="PUBLIC_DEGREE_INFORMATION"/>
	</bean:define>
	<div class="email"><p>
		<html:link href="<%="mailto:" +buffer.toString() %>" titleKey="send.email.dynamicMailDistribution.title" bundle="PUBLIC_DEGREE_INFORMATION">
			<bean:message key="send.email.dynamicMailDistribution.link" bundle="PUBLIC_DEGREE_INFORMATION"/>
		</html:link>
	</p></div>
</logic:equal>

<logic:notEqual name="executionCourse" property="site.dynamicMailDistribution" value="true">
	<logic:notEmpty name="component" property="mail" >	
		<bean:define id="siteMail" name="component" property="mail" />
		<div class="email"><p>
		<html:link href="<%= "mailto:" + pageContext.findAttribute("siteMail") %>" titleKey="send.email.singleMail.title" bundle="PUBLIC_DEGREE_INFORMATION">
			<bean:message key="send.email.dynamicMailDistribution.link" bundle="PUBLIC_DEGREE_INFORMATION"/>
		</html:link>
		</p></div>
	</logic:notEmpty>
</logic:notEqual>
