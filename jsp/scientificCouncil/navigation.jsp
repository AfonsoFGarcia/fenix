<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:present role="SCIENTIFIC_COUNCIL">
	<ul>
		<li class="navheader">
			<bean:message key="bolonha.process"/>
		</li>
		
		<li>
			<html:link page="/competenceCourses/competenceCoursesManagement.faces">
				<bean:message key="navigation.competenceCoursesManagement"/>
			</html:link>
		</li>

		<li>
			<html:link page="/curricularPlans/curricularPlansManagement.faces">
				<bean:message key="navigation.curricularPlansManagement"/>
			</html:link>
		</li>
<%-- 
        <li class="navheader">
            <bean:message key="scientificCouncil.thesis.process"/>
        </li>
        <li>
            <html:link page="/scientificCouncilManageThesis.do?method=listThesis">
                <bean:message key="navigation.list.jury.proposals"/>
            </html:link>  
        </li>
--%>
		<br/>
		<li class="navheader">
			<bean:message key="label.credits.navigation.header"/>
		</li>
		<li>
		  	<html:link page="/defineCreditsPeriods.do?method=showPeriods">
		  		<bean:message key="link.define.periods"/>
		  	</html:link>  
		</li>		
		<li>
			<html:link page="/functionsManagement/personSearchForFunctionsManagement.faces" >
				<bean:message key="link.managementPositions.management"/>
			</html:link>
		</li>
		<li>
			<html:link page="/masterDegreeCreditsManagement.do?method=prepare">
		  		<bean:message key="link.credits.masterDegree"/>
		  	</html:link>
		</li>
		<li>
		  	<html:link page="/showAllTeacherCreditsResume.do?method=prepareTeacherSearch">
		  		<bean:message key="link.teacher.sheet"/>
		  	</html:link>  
		</li>			
		<li>
			<html:link page="/creditsReport.do?method=prepare">
		  		<bean:message key="link.credits.viewReport"/>
		  	</html:link>
		</li>		
<%--		<br />
		<li class="navheader">
			Merge
		</li>
		<li>
			<html:link page="/mergeScientificJournal.do?method=prepare">
		  		ScientificJournal Merge
		  	</html:link>
		</li>		
		<li>
			<html:link page="/mergeEvents.do?method=prepare">
		  		Event Merge
		  	</html:link>
		</li>		--%>
	</ul> 
</logic:present>
