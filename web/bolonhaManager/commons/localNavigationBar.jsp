<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>	

<logic:present role="BOLONHA_MANAGER">	
	<ul>
		<li>
			<html:link page="/competenceCourses/competenceCoursesManagement.faces">
				<bean:message key="navigation.competenceCoursesManagement"/>
			</html:link>
			<!--<ul>
				<li>
					<html:link page="/competenceCourses/searchCompetenceCourses.do?method=search">
						<bean:message key="navigation.search.competenceCourses"/>
					</html:link>
				</li>
			</ul> -->
		</li>
		
		  
		<li>
			<html:link page="/competenceCourses/manageVersions.do?method=prepare">
				<bean:message key="label.manage.versions"/>
			</html:link>
		</li>
		
		
		<li>
			<html:link page="/curricularPlans/curricularPlansManagement.faces">
				<bean:message key="navigation.curricularPlansManagement"/>
			</html:link>
		</li>

	</ul>	
</logic:present>
