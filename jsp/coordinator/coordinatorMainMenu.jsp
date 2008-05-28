<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<%@ page import="net.sourceforge.fenixedu.domain.Degree" %>
<%@ page import="net.sourceforge.fenixedu.domain.RootDomainObject" %>

<bean:define id="person" name="UserView" property="person" type="net.sourceforge.fenixedu.domain.Person"/>

<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session" type="InfoExecutionDegree" />
	<bean:define id="infoDegreeCurricularPlan" name="infoExecutionDegree" property="infoDegreeCurricularPlan" />
	<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" type="java.lang.Integer"/>
	<bean:define id="executionDegreeID" name="infoExecutionDegree" property="idInternal" />

    <%
        Degree degree = RootDomainObject.getInstance().readDegreeCurricularPlanByOID(degreeCurricularPlanID).getDegree();
        if (degree.isCoordinatorInSomeExecutionYear(person)) {
            request.setAttribute("isCoordinator", true);
        }
        
        if (degree.isMemberOfAnyScientificCommission(person)) {
            request.setAttribute("isScientificCommissionMember", true);
        }
    %>

	<ul>

        <%--  start of isCoordinator logic, search for isCoordinator --%>
        <logic:present name="isCoordinator">
                
		<%-- Start of Common Options --%>	
		<li class="navheader">
			<bean:message key="label.coordinator.management"/>		
		</li>
		<li>
			<html:link page="<%= "/viewCoordinationTeam.do?method=chooseExecutionYear&degreeCurricularPlanID=" + degreeCurricularPlanID.toString()  %>" >
				<bean:message key="link.coordinator.degreeCurricularPlan.coordinationTeam"/>
			</html:link> 
		</li>
		<li>
			<html:link page="<%= "/scientificCommissionTeamDA.do?method=manage&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString()  %>" >
				<bean:message key="link.coordinator.degreeCurricularPlan.scientificCommissionTeam"/>
			</html:link> 
		</li>
		<li>
			<html:link page="<%= "/degreeSiteManagement.do?method=subMenu&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString()%>">
				<bean:message key="link.coordinator.degreeSite.management"/>		
			</html:link> 
		</li>
		<logic:equal name="infoExecutionDegree" property="bolonhaDegree" value="true">
			<li>
				<html:link page="<%="/degreeCurricularPlan/showDegreeCurricularPlanBolonha.faces?degreeCurricularPlanID=" + degreeCurricularPlanID + "&amp;organizeBy=groups&amp;showRules=false&amp;hideCourses=false"%>">
					<bean:message key="link.coordinator.degreeCurricularPlan.management"/>
				</html:link> 
			</li>
		</logic:equal>
        <li>
            <html:link page="<%="/sendMail.do?method=prepare&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString() %>">
                <bean:message key="link.coordinator.sendMail" />
            </html:link>
        </li>

		<logic:equal name="infoExecutionDegree" property="bolonhaDegree" value="false">
			<li>
				<html:link page="<%="/degreeCurricularPlanManagement.do?method=showActiveCurricularCourses&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
					<bean:message key="link.coordinator.degreeCurricularPlan.management"/>
				</html:link> 
			</li>
			<li>
				<html:link page="<%= "/executionCoursesInformation.do?method=prepareChoiceForCoordinator&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
					<bean:message key="link.coordinator.executionCoursesInformation"/>
				</html:link>
			</li>

			<li>
				<html:link page="<%= "/teachersInformation.do?executionDegreeId=" + executionDegreeID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">				
					<bean:message key="link.coordinator.teachersInformation"/>
				</html:link>
			</li>
 
			<li class="navheader">
				<bean:message key="label.coordinator.manageEvaluations"/>		
			</li>
			<li>
				<html:link page="<%="/evaluation/evaluationsCalendar.faces?degreeCurricularPlanID=" + degreeCurricularPlanID.toString() %>">
		        	 <bean:message key="link.calendar" />
		        </html:link>
			</li>
	<%--
			<li>
				<html:link page="<%="/evaluation/showWrittenTestsForExecutionCourses.faces?degreeCurricularPlanID=" + degreeCurricularPlanID.toString() %>">
		        	<bean:message key="link.writtenTests" />
		        </html:link>
			</li>
			<li>
				<html:link page="<%="/evaluation/showProjectsForExecutionCourses.faces?degreeCurricularPlanID=" + degreeCurricularPlanID.toString() %>">
		        	<bean:message key="link.projects" />
		        </html:link>
			</li>
	--%>
		</logic:equal>	

		<%-- Start of Master Degree Coordinator Options --%>
		<logic:equal name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" value="<%= DegreeType.MASTER_DEGREE.toString() %>">
			<li class="navheader">
				<bean:message key="link.coordinator.candidate"/>
			</li>
	       	<li>
	        	<html:link page="<%= "/candidateOperation.do?method=getCandidates&action=visualize&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
	        	<bean:message key="link.coordinator.visualizeCandidate" /></html:link>
	        </li>
	       	<li>
	       		<bean:define id="link">/prepareCandidateApproval.do?method=chooseExecutionDegree&page=0&degreeCurricularPlanID=
	       		</bean:define>
	        	<bean:define id="prepareCandidateApprovalLink">
	  					<bean:write name="link"/><bean:write name="degreeCurricularPlanID"/>
	  				</bean:define> 	
	        	
	        	<html:link page='<%= pageContext.findAttribute("prepareCandidateApprovalLink").toString() %>'>
	        	<bean:message key="link.coordinator.approveCandidates" /></html:link>	        	
	        </li>
			<li>
				<bean:define id="link2">/displayCandidateListToMakeStudyPlan.do?method=prepareSelectCandidates&page=0&degreeCurricularPlanID=
	       		</bean:define>
	        	<bean:define id="displayCandidateListToMakeStudyPlanLink">
	  					<bean:write name="link2"/><bean:write name="degreeCurricularPlanID"/>
	  				</bean:define> 	
				<html:link titleKey="link.masterDegree.administrativeOffice.makeStudyPlan.title" page="<%= pageContext.findAttribute("displayCandidateListToMakeStudyPlanLink").toString() %>">
				<bean:message key="link.masterDegree.administrativeOffice.makeStudyPlan" /></html:link>
			</li>
			<li>
				<bean:define id="link3">/printAllCandidatesList.do?method=prepare&degreeCurricularPlanID=
	       		</bean:define>
	        	<bean:define id="printAllCandidatesListLink">
	  					<bean:write name="link3"/><bean:write name="degreeCurricularPlanID"/>
	  				</bean:define> 	
				<html:link titleKey="link.masterDegree.candidateListFilter.printListAllCandidatesFilterMenu.title" page="<%= pageContext.findAttribute("printAllCandidatesListLink").toString() %>">
				<bean:message key="link.masterDegree.candidateListFilter.printListAllCandidatesFilterMenu"/></html:link>
			</li>
	
			<li class="navheader">
				<bean:message key="link.coordinator.student"/>
			</li>
			<li>
				<bean:define id="link21">/sendMail.do?method=prepare&students=true&degreeCurricularPlanID=
		      		</bean:define>
		       	<bean:define id="listStudentsForCoordinator">
		 					<bean:write name="link21"/><bean:write name="degreeCurricularPlanID"/>
		 				</bean:define> 	
				<html:link page="<%= pageContext.findAttribute("listStudentsForCoordinator").toString() %>">
				<bean:message key="link.sendEmailToAllStudents" /></html:link>
			</li>			
			<li><span>Listagens</span>
				<ul>
					<li>
						<bean:define id="link2">/listStudentsForCoordinator.do?method=getStudentsFromDCP&page=0&degreeCurricularPlanID=
				      		</bean:define>
				       	<bean:define id="listStudentsForCoordinator">
				 					<bean:write name="link2"/><bean:write name="degreeCurricularPlanID"/>
				 				</bean:define> 	
						<html:link titleKey="link.coordinator.studentListByDegree.title" page="<%= pageContext.findAttribute("listStudentsForCoordinator").toString() %>">
						<bean:message key="link.coordinator.studentListByDegree" /></html:link>
					</li>
					<li>				
						<bean:define id="link2">/studentListByDegree.do?method=getCurricularCourses&jspTitle=title.studentListByCourse&page=0&degreeCurricularPlanID=
			       		</bean:define>
			        	<bean:define id="studentListByDegree">
			  					<bean:write name="link2"/><bean:write name="degreeCurricularPlanID"/>
			  				</bean:define> 	
						<html:link titleKey="link.coordinator.studentListByCourse.title" page="<%= pageContext.findAttribute("studentListByDegree").toString() %>">
						<bean:message key="link.coordinator.studentListByCourse" /></html:link>					
					</li>
					<li>
						<html:link titleKey="link.coordinator.studentAndGratuityListByDegree.title" page='<%= "/studentsGratuityList.do?method=coordinatorStudentsGratuityList&amp;order=studentNumber&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString()%>'>
							<bean:message key="link.coordinator.studentAndGratuityListByDegree"/>
						</html:link>
					</li>
					<li>
						<html:link titleKey="link.coordinator.studentByThesis.title" page='<%= "/student/displayStudentThesisList.faces?degreeCurricularPlanID=" + degreeCurricularPlanID.toString()%>'>
							<bean:message key="link.coordinator.studentByThesis"/>
						</html:link>
					</li>
				</ul>
			</li>
		</logic:equal>
		
		<%-- Start of non-Master Degree Coordinator Options --%>
		<logic:notEqual name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" value="<%= DegreeType.MASTER_DEGREE.toString() %>">

			<li class="navheader">
				<bean:message key="label.coordinator.degreeSite.tutorship"/>
			</li>
			<li>
				<html:link href="http://gep.ist.utl.pt/html/tutorado" target="_blank">
					<bean:message key="link.coordinator.gepTutorshipPage" />
				</html:link>
			</li>
			<li>
				<html:link page="<%= "/createTutorship.do?method=prepareCreateTutorships&executionDegreeId=" + executionDegreeID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">				
					<bean:message key="link.coordinator.createTutorships"/>
				</html:link>
			</li>
			<li>
				<html:link page="<%= "/tutorManagement.do?method=prepare&forwardTo=prepareChooseTutor&executionDegreeId=" + executionDegreeID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">				
					<bean:message key="link.coordinator.tutorshipManagement"/>
				</html:link>
			</li>
			<li>
				<html:link page="<%= "/tutorManagement.do?method=prepare&forwardTo=prepareChooseTutorHistory&executionDegreeId=" + executionDegreeID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">				
					<bean:message key="link.coordinator.tutorshipHistory"/>
				</html:link>
			</li>
			
			<li class="navheader">
				<bean:message key="label.coordinator.degreeSite.students"/>
			</li>
			<li>
				<html:link page="<%= "/viewStudentCurriculum.do?method=prepare&amp;executionDegreeId=" + executionDegreeID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
				    <bean:message key="label.coordinator.studentInformation"/>
				</html:link>			
			</li>		
			<li>
				<html:link page="<%= "/students.faces?degreeCurricularPlanID=" + degreeCurricularPlanID + "&amp;executionDegreeId=" + executionDegreeID %>">
				    <bean:message key="list.students"/>
				</html:link>			
			</li>			
			<logic:equal name="infoExecutionDegree" property="bolonhaDegree" value="false">
				<li>
					<html:link page="<%= "/sendMail.do?method=prepare&amp;students=true&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
					    <bean:message key="sendMail.students"/>
					</html:link>			
				</li>
			</logic:equal>
				<li>
					<html:link page="<%= "/weeklyWorkLoad.do?method=prepare&amp;page=0&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
					    <bean:message key="link.weekly.work.load"/>
					</html:link>			
				</li>
		</logic:notEqual>
        
        		<li class="navheader"><bean:message key="label.executionCourseManagement.menu.communication"/></li>
        		<li> 
        			<html:link page="<%= "/sendDegreeMail.do?method=send&degreeId=" + degree.getIdInternal() %>">
        				 <bean:message key="link.coordinator.sendMail"/>
        			</html:link>
        		</li>
        </logic:present>
        <%-- end of isCoordinator logic --%>
        
        <logic:present name="isScientificCommissionMember">
            <li class="navheader">
                <bean:message key="label.coordinator.thesis"/>
            </li>
            <li>
              <html:link page="<%= "/manageThesis.do?method=searchStudent&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString()%>">
                    <bean:message key="link.coordinator.thesis.viewStudent" />
              </html:link>
            </li>
            <li>
              <html:link page="<%= "/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString()%>">
                    <bean:message key="link.coordinator.thesis.list" />
              </html:link>
            </li>
        </logic:present>

	        <li>
	    	  <html:link page="<%= "/manageFinalDegreeWork.do?method=showChooseExecutionDegreeForm&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString()%>">
		      	<bean:message key="link.coordinator.managefinalDegreeWorks" />
	          </html:link>
	        </li>

	</ul>
</logic:present>
