<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<h2><bean:message key="title.teacherInformation"/></h2>

<logic:present name="infoSiteTeacherInformation"> 
	<bean:define id="datePattern" value="dd-MM-yyyy"/>
		<table class="infoselected" width="100%">
			<tr>
				<td width="70%">
					<bean:message key="message.teacherInformation.name" />
					&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nome" /></td> 
				<td width="30%">
					<bean:message key="message.teacherInformation.birthDate" />
					&nbsp;<dt:format patternId="datePattern"><bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nascimento.time" /></dt:format>
				</td>	
			</tr>
			<tr>
				<td>
					<bean:message key="message.teacherInformation.category" />
					<logic:notEmpty name="infoSiteTeacherInformation" property="infoTeacher">
						<logic:notEmpty name="infoSiteTeacherInformation" property="infoTeacher.infoCategory">
							&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoCategory.shortName" />
						</logic:notEmpty>
					</logic:notEmpty>
				</td>
			</tr>
		</table>
		<br/>
		<p class="infoop"><span class="emphasis-box">1</span>
		<bean:message key="message.teacherInformation.qualifications" /></p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoQualifications">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<tr>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.year" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.school" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsDegree" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsTitle" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsMark" /></th>
				</tr>
				<logic:iterate id="infoQualification" name="infoSiteTeacherInformation" property="infoQualifications">
					<tr>
						<td class="listClasses">
								<logic:notEmpty name="infoQualification" property="year">
									<bean:write name="infoQualification" property="year" />							
								</logic:notEmpty>			 								
								<logic:empty name="infoQualification" property="year">
									<logic:notEmpty name="infoQualification" property="date">
										<dt:format pattern="yyyy">
											<bean:write name="infoQualification" property="date.time" />							
										</dt:format>
									</logic:notEmpty>
								</logic:empty>
								<logic:empty name="infoQualification" property="year">
									<logic:empty name="infoQualification" property="date">
										--
									</logic:empty>
								</logic:empty>
						</td>
						<td class="listClasses"><bean:write name="infoQualification" property="school" /></td>
						<td class="listClasses"><bean:write name="infoQualification" property="degree" /></td>
						<logic:notEmpty name="infoQualification" property="type">												
							<bean:define id="qualificationType" name="infoQualification" property="type.name" />
							<td class="listClasses"><bean:message name="qualificationType" bundle="ENUMERATION_RESOURCES"/></td>
						</logic:notEmpty>
						<logic:empty name="infoQualification" property="type">																			
							<td class="listClasses"><bean:write name="infoQualification" property="title"/></td>
						</logic:empty>	
						<logic:present name="infoQualification" property="mark">												
							<td class="listClasses">&nbsp;<bean:write name="infoQualification" property="mark" /></td>																
						</logic:present>
						<logic:notPresent name="infoQualification" property="mark">												
							<td class="listClasses">--</td>																
						</logic:notPresent>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoQualifications">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>			
			</logic:empty>
		<br/>
		<p class="infoop"><span class="emphasis-box">2</span>
		<bean:message key="message.teacherInformation.teachingCareer" /></p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoTeachingCareers">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<tr>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.years" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.careerCategory" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.careerPositions" /></th>
				</tr>
				<logic:iterate id="infoTeachingCareer" name="infoSiteTeacherInformation" property="infoTeachingCareers">
				<tr>
					<td class="listClasses"><bean:write name="infoTeachingCareer" property="beginYear"/>
						-<bean:write name="infoTeachingCareer" property="endYear"/>
					</td>
					<td class="listClasses">				
						<logic:notEmpty name="infoTeachingCareer" property="infoCategory" >
							<bean:write name="infoTeachingCareer" property="infoCategory.shortName" />
						</logic:notEmpty>
					</td>
					<td class="listClasses"><bean:write name="infoTeachingCareer" property="courseOrPosition" /></td>
				</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoTeachingCareers">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>			
		</logic:empty>
		<br />
		<p class="infoop"><span class="emphasis-box">3</span>
		<bean:message key="message.teacherInformation.professionalCareer" /></p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoProfessionalCareers">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<tr>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.years" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.entity" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.functions" /></th>
				</tr>
				<logic:iterate id="infoProfessionalCareer" 
							   name="infoSiteTeacherInformation" 
							   property="infoProfessionalCareers">
					<tr>
						<td class="listClasses">
							<bean:write name="infoProfessionalCareer" property="beginYear"/>
							-<bean:write name="infoProfessionalCareer" property="endYear"/>
						</td>
						<td class="listClasses"><bean:write name="infoProfessionalCareer" property="entity" /></td>
						<td class="listClasses"><bean:write name="infoProfessionalCareer" property="function" /></td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoProfessionalCareers">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		<br/>
		<p class="infoop"><span class="emphasis-box">4</span>
			<bean:message key="message.teacherInformation.externalActivities" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoExternalActivities">
			<table width="100%">
				<logic:iterate id="infoExternalActivity" name="infoSiteTeacherInformation" property="infoExternalActivities">
					<tr>
						<td class="listClasses" style="text-align:left">
							<bean:write name="infoExternalActivity" property="activity" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoExternalActivities">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		<br/>
		<p class="infoop"><span class="emphasis-box">5</span>
		<bean:message key="message.teacherInformation.ownPublications" /></p>
		<!-- DELETE (replace by resultpublications)  -->
		<%--  
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoDidaticPublications">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<logic:iterate id="infoPublicationDidatic" name="infoSiteTeacherInformation" property="infoDidaticPublications">
					<tr>
						<td class="listClasses" style="text-align:left">
							<bean:write name="infoPublicationDidatic" property="publicationString" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoDidaticPublications">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		--%>
		<!-- END DELETE  -->
		<!-- DIDATIC PUBLICATIONS (Sergio Patricio & Luis Santos)-->
		<logic:notEmpty name="didaticResults">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<logic:iterate id="didaticResult" name="didaticResults">
					<tr>
						<td class="listClasses" style="text-align:left">
							<bean:write name="didaticResult" property="resume"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="didaticResults">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		<!--  END DIDATIC PUBLICATIONS -->
		<br />
		<p class="infoop"><span class="emphasis-box">6</span>
		<bean:message key="message.teacherInformation.cientificPublications" /></p>
		<!-- DELETE (replace by resultpublications)  -->
		<%-- 
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoCientificPublications">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<logic:iterate id="infoPublicationCientific" name="infoSiteTeacherInformation" property="infoCientificPublications">
					<tr>
						<td class="listClasses" style="text-align:left">
							<bean:write name="infoPublicationCientific" property="publicationString" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoCientificPublications">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		--%>
		<!-- END DELETE  -->
		<!-- CIENTIFIC PUBLICATIONS (Sergio Patricio & Luis Santos)-->
		<logic:notEmpty name="cientificResults">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<logic:iterate id="cientificResult" name="cientificResults">
					<tr>
						<td class="listClasses" style="text-align:left">
							<bean:write name="cientificResult" property="resume"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="cientificResults">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		<!--  END CIENTIFIC PUBLICATIONS -->
		<br />
		<p class="infoop"><span class="emphasis-box">7</span>
		<bean:message key="message.teacherInformation.numberOfPublications" /></p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.number" /></th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.national" /></th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.international" /></th>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.comunicationsPublications" /></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoComunicationPublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoComunicationPublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.articlesPublications" /></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoMagArticlePublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoMagArticlePublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.bookAuthorPublications" /></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoAuthorBookPublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoAuthorBookPublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.bookEditorPublications" /></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoEditBookPublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoEditBookPublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.articlesAndChaptersPublications" /></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoArticleChapterPublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoArticleChapterPublicationsNumber.international" />
				</td>
			</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">8</span>
			<bean:message key="message.teacherInformation.serviceRegime" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<logic:present name="infoSiteTeacherInformation" property="infoServiceProviderRegime.providerRegimeType">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<tr>
					<td>
						<bean:message name="infoSiteTeacherInformation" 
									  property="infoServiceProviderRegime.providerRegimeType.name" 
									  bundle="ENUMERATION_RESOURCES"/>
					</td>
				</tr>
			</table>
		</logic:present>
		<logic:notPresent name="infoSiteTeacherInformation" property="infoServiceProviderRegime.providerRegimeType">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>			
		</logic:notPresent>
		<br />
		<p class="infoop"><span class="emphasis-box">9</span>
			<bean:message key="message.teacherInformation.lectureCourses" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.semester" /></th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.lectureCourse" /></th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsDegree" /></th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.typeOfClass" /></th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.numberOfClass" /></th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.numberOfWeeklyHours" /></th>
			</tr>
			<logic:iterate id="infoExecutionCourse" name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses">
				<logic:iterate id="infoCurricularCourse" name="infoExecutionCourse" property="associatedInfoCurricularCourses">
					<tr>
							<td class="listClasses">
								<bean:write name="infoExecutionCourse" 
											property="infoExecutionPeriod.semester" />
							</td>
							<td class="listClasses"><bean:write name="infoExecutionCourse" property="nome" /></td>
							<td class="listClasses">
								<bean:write name="infoCurricularCourse" 
											property="infoDegreeCurricularPlan.infoDegree.nome" />
							</td>
							<td class="listClasses">&nbsp;<%--<bean:write name="infoExecutionCourse" property="" />--%></td>
							<td class="listClasses">&nbsp;<%--<bean:write name="infoExecutionCourse" property="" />--%></td>
							<td class="listClasses">&nbsp;<%--<bean:write name="infoExecutionCourse" property="" />--%></td>
					</tr>
				</logic:iterate>
			</logic:iterate>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">10</span>
			<bean:message key="message.teacherInformation.orientations" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<th class="listClasses-header">&nbsp;</th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.numberOfStudents" /></th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.description" /></th>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.tfc" /></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoDegreeOrientation.numberOfStudents" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoDegreeOrientation.description" />&nbsp;
				</td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.masterThesis" /></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoMasterOrientation.numberOfStudents" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoMasterOrientation.description" />&nbsp;
				</td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.phdThesis" /></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoPhdOrientation.numberOfStudents" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoPhdOrientation.description" />&nbsp;
				</td>
			</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">11</span>
			<bean:message key="message.teacherInformation.weeklySpendTime" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<th class="listClasses-header">&nbsp;</th>
			<th class="listClasses-header"><bean:message key="message.teacherInformation.teachers" /></th>
			<th class="listClasses-header"><bean:message key="message.teacherInformation.supportLessons" /></th>
			<th class="listClasses-header"><bean:message key="message.teacherInformation.investigation" /></th>
			<th class="listClasses-header"><bean:message key="message.teacherInformation.managementWorks" /></th>
			<th class="listClasses-header"><bean:message key="message.teacherInformation.others" /></th>
		</tr>
		<tr>
			<td class="listClasses"><bean:message key="message.teacherInformation.numberOfHours" /></td>
			<td class="listClasses">
				<bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.lecture" />
			</td>
			<td class="listClasses">
				<bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.support" />
			</td>
			<td class="listClasses">
				<bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.research" />
			</td>
			<td class="listClasses">
				<bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.management" />
			</td>
			<td class="listClasses">
				<bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.other" />
			</td>
		</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">12</span>
			<bean:message key="message.teacherInformation.managerPosition" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
			<bean:define id="datePattern" value="dd-MM-yyyy"/>
			<logic:notEmpty name="infoSiteTeacherInformation" property="personFunctions">		
				<tr>
					<th class="listClasses-header" style="text-align:left"><bean:message key="label.managementPosition.position" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					<th class="listClasses-header" style="text-align:left"><bean:message key="label.managementPosition.unit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>										
					<th class="listClasses-header" width="10%"><bean:message key="label.managementPosition.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>					
					<th class="listClasses-header" width="10%"><bean:message key="label.managementPosition.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				</tr>
				<logic:iterate id="personFunction" name="infoSiteTeacherInformation" property="personFunctions">
					<tr>
						<td class="listClasses" style="text-align:left">
							<bean:write name="personFunction" property="function.name"/>
						</td>
						<td class="listClasses" style="text-align:left">
							<bean:write name="personFunction" property="function.unit.presentationNameWithParents"/>	
						</td>				
						<td class="listClasses">
							<bean:define id="beginDate" type="org.joda.time.YearMonthDay" name="personFunction" property="beginDate"/>
							<bean:define id="beginDateTime" ><%= beginDate.toDateTimeAtCurrentTime().toDate().getTime() %></bean:define>
							<dt:format patternId="datePattern">
								<bean:write name="beginDateTime"/>
							</dt:format>
						</td>
						<logic:notEmpty name="personFunction" property="endDate">
							<td class="listClasses">
								<bean:define id="endDate" type="org.joda.time.YearMonthDay" name="personFunction" property="endDate"/>
								<bean:define id="endDateTime" ><%= endDate.toDateTimeAtCurrentTime().toDate().getTime() %></bean:define>
								<dt:format patternId="datePattern">
									<bean:write name="endDateTime"/>
								</dt:format>
							</td>
						</logic:notEmpty>
						<logic:empty name="personFunction" property="endDate">
						-			
						</logic:empty>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="infoSiteTeacherInformation" property="personFunctions">
				<tr>
					<td colspan="4" class="listClasses"> 
						<i><bean:message key="message.managementPositions.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
					</td>
				</tr>
			</logic:empty>		
		</table>
</logic:present>
