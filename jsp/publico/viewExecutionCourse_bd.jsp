<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<logic:notPresent name="siteView">
	<span class="error"><p><bean:message key="errors.invalidSiteExecutionCourse"/></p></span>
</logic:notPresent>

<logic:present name="siteView">
    <logic:notPresent name="siteView" property="component">
		<span class="error"><p><bean:message key="message.public.notfound.executionCourse"/></p></span>
	</logic:notPresent>

	<bean:define id="component" name="siteView" property="commonComponent" />
	<bean:define id="curricularCoursesList" name="component" property="associatedDegreesByDegree" />
	<div id="contextual_nav">
		<h2 class="brown"><bean:message key="label.curricular.information"/></h2>
		<ul>	
			<logic:iterate id="curricularCourses" name="curricularCoursesList">
				<bean:size id="size" name="curricularCourses" />

				<logic:iterate id="curricularCourse" name="curricularCourses">
					<bean:define id="curricularCourseId" name="curricularCourse" property="idInternal" />
					<bean:define id="degreeID" name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.idInternal" />
					<bean:define id="degreeCurricularPlanID" name="curricularCourse" property="infoDegreeCurricularPlan.idInternal" />
					<bean:define id="initialDate" name="curricularCourse" property="infoDegreeCurricularPlan.initialDate" />
					<bean:define id="initialYear" value='<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>' />
		
					<li><html:link page="<%= "/showCourseSite.do?method=showCurricularCourseSite&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseId") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  pageContext.getAttribute("degreeID") %>"> 	
						<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>	
		
						<logic:greaterThan name="size" value="1">
							<logic:match name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" value="Licenciatura">
								<bean:write name="initialYear" />
							</logic:match>
						</logic:greaterThan>

						<logic:match name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" value="Mestrado">	
							<bean:define id="endDate" name="curricularCourse" property="infoDegreeCurricularPlan.endDate" />
							<bean:define id="endYear" value='<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")+1) %>' />
							<bean:write name="initialYear" />/<bean:write name="endYear" />
						</logic:match>
			
					</html:link></li>
				</logic:iterate>
			</logic:iterate>
		</ul>
	</div>

	<logic:present name="siteView" property="component">
		<bean:define id="component" name="siteView" property="component" />
		<logic:notEmpty name="component" property="initialStatement">
			<div class="citation">
				<p><bean:write name="component" property="initialStatement" filter="false"/></p>
			</div>
		</logic:notEmpty>
		
        <logic:notEmpty name="component" property="lastAnnouncement">		
			<bean:define id="announcement" name="component" property="lastAnnouncement"/>
			<div id="announcs">
				<h2 class="announcs-head"><bean:message key="label.lastAnnouncements"/></h2>
				<div class="last-announc">
					<div class="last-announc-name"><bean:write name="announcement" property="title"/></div>
					<div class="last-announc-post-date">
						<dt:format pattern="dd/MM/yyyy  HH:mm">
							<bean:write name="announcement" property="lastModifiedDate.time"/>
						</dt:format>
					</div>
					<p class="last-announc-info"><bean:write name="announcement" property="information" filter="false"/></p>
				</div>
				<logic:empty name="component" property="lastFiveAnnouncements" ></div></logic:empty>
		</logic:notEmpty>

		<logic:notEmpty name="component" property="lastFiveAnnouncements">		    	
				<ul class="more-announc">
				<logic:iterate id="announcement" name="component" property="lastFiveAnnouncements">
					<bean:define id="announcementId" name ="announcement" property="idInternal" />
					<li class="more-announc"><span class="more-announc-date"><dt:format pattern="dd/MM/yyyy">
						<bean:write name="announcement" property="lastModifiedDate.time"/></dt:format> - </span>
						<html:link  page="<%="/viewSite.do"+"?method=announcements&amp;objectCode=" + pageContext.findAttribute("objectCode")%>"
									anchor="<%= announcementId.toString() %>">
							<bean:write name="announcement" property="title"/>
						</html:link></li>
				</logic:iterate>
				</ul>
			</div>		    
        </logic:notEmpty>

		<logic:notEmpty name="component" property="alternativeSite">
			<h2><bean:message key="message.siteAddress" /></h2>
			<bean:define id="alternativeSite" name="component" property="alternativeSite"/>
			<html:link href="<%=(String)pageContext.findAttribute("alternativeSite") %>" target="_blank">
				<bean:write name="alternativeSite" />
			</html:link>
		</logic:notEmpty>			

		<logic:notEmpty name="component" property="introduction">
			<h2><bean:message key="message.introduction" /></h2>
			<p><bean:write name="component" property="introduction" filter="false" /></p>
         </logic:notEmpty>
	
        <logic:notEmpty name="component" property="responsibleTeachers">	
			<h2><bean:message key="label.lecturingTeachers"/></h2>	
            	<logic:iterate id="infoResponsableTeacher" name="component" property="responsibleTeachers">
				<bean:write name="infoResponsableTeacher" property="infoPerson.nome" /><bean:message key="label.responsible"/>
			</logic:iterate>	
        </logic:notEmpty>
        
		<logic:notEmpty name="component" property="lecturingTeachers">	
			<logic:empty name="component" property="responsibleTeachers">	
                <h2><bean:message key="label.lecturingTeachers"/></h2>	
			</logic:empty>
            <logic:iterate id="infoTeacher" name="component" property="lecturingTeachers">
				<br />
				<bean:write name="infoTeacher" property="infoPerson.nome" />
            </logic:iterate>	
        </logic:notEmpty>
		
	</logic:present>
</logic:present>
