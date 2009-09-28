<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
<bean:define id="executionDegreeOID" name="executionDegree" property="externalId" type="String"/>
<h2>
	<bean:message key="title.final.degree.work.administration"/>
</h2>	

<h3>
	<bean:message key="message.final.degree.work.administration"/>
	<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>
</h3>	

<ul>
	<li><html:link page="<%= "/manageFinalDegreeWork.do?method=showProposals&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID %>">Propostas</html:link></li>
	<li><html:link page="<%= "/manageFinalDegreeWork.do?method=showCandidates&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID %>">Candidatos</html:link></li>
</ul>

<logic:present name="executionDegree" property="scheduling">
<logic:notEqual name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName" value="1">
<div class="infoop2">
	<p>
		<strong>
			<bean:message key="message.final.degree.work.other.execution.degrees"/>
		</strong>
	</p>
		<logic:iterate id="currentExecutionDegree" name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName">
			<logic:notEqual name="currentExecutionDegree" property="externalId" value="<%= executionDegreeOID %>">
				<p class="mvvert05">
					<bean:write name="currentExecutionDegree" property="degreeCurricularPlan.presentationName"/>
				</p>
			</logic:notEqual>
		</logic:iterate>
</div>
</logic:notEqual>
</logic:present>

<!--<table class="tstyle5 mvert05">-->
<!--	<tr>-->
<!---->
<!--		<td>Ano lectivo:</td>-->
<!--		<td>-->
<!--			<select>-->
<!--				<option>2009/2010</option>-->
<!--				<option>2008/2009</option>-->
<!--				<option>2007/2008</option>-->
<!--			</select>-->
<!---->
<!--		</td>-->
<!--	</tr>-->
<!--</table>-->
<!--			-->
<!--<h3 class="">MEIC-A 2006 (2008/2009)</h3>-->


			
			<table class="tdtop mtop0">
				<tr>
					<td style="padding-right: 20px;">
						<p class="mbottom05 mtop05"><strong><bean:message key="title.proposals"/></strong></p>
						<fr:view name="summary" property="proposalsSummary" schema="thesis.bean.proposals.summary">
							<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle1 thlight thleft tdright mtop05"/>
								<fr:property name="labelTerminator" value=""/>
							</fr:layout>
						</fr:view>
					</td>
					<td>
						<p class="mbottom05 mtop05"><strong><bean:message key="title.candidacy"/></strong></p>
						<fr:view name="summary" property="candidaciesSummary" schema="thesis.bean.candidacies.summary">
							<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle1 thlight thleft tdright mtop05"/>
								<fr:property name="labelTerminator" value=""/>
							</fr:layout>
						</fr:view>
					</td>

				</tr>			
			</table>
			
			<bean:define id="count" name="summary" property="candidaciesSummary.candidatesWithoutDissertationEnrolment"/>
                        <logic:greaterThan name="count" value="0">
                          <p class="mtop05">
                            <html:link page="<%= "/manageFinalDegreeWork.do?method=showCandidatesWithoutDissertationEnrolments&executionDegreeOID=" +  executionDegreeOID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
                              <bean:write name="count"/> <bean:message key="message.candidatesWithoutDissertationEnrolment"/>
                            </html:link>
                          </p>
			</logic:greaterThan>
                        <logic:equal name="count" value="0">
                          <bean:write name="count"/> <bean:message key="message.candidatesWithoutDissertationEnrolment"/>
			</logic:equal>
                        <p class="mbottom05 mtop05"><strong><bean:message key="title.dissertations"/></strong></p>
			
			<table class="tstyle1 thlight thleft tdright mtop05">
				<logic:iterate id="stateCount" name="summary" property="thesisSummary.thesisCount">
					<bean:define id="key" name="stateCount" property="key"/>
					<tr>
						<th>
							<bean:write name="key" property="label"/>
						</th>
						<td>
							<logic:notEqual name="stateCount" property="value" value="0">
                                                          <logic:present name="executionYearId">
                                                                <bean:define id="executionYearId" name="executionYearId"/>
								<html:link page="<%= "/manageThesis.do?method=listThesis&executionYearId=" + executionYearId + "&degreeCurricularPlanID=" + degreeCurricularPlanID + "&filter=" + key %>">
									<bean:write name="stateCount" property="value"/>
								</html:link>
                                                          </logic:present>
                                                        </logic:notEqual>
							<logic:equal name="stateCount" property="value" value="0">0</logic:equal>
                                                        <logic:notEqual name="stateCount" property="value" value="0">
                                                          <logic:notPresent name="executionYearId">
								<bean:write name="stateCount" property="value"/>
                                                          </logic:notPresent>
                                                        </logic:notEqual>
						</td>
					</tr>
				</logic:iterate>
			</table>
			
			
			<p class="mbottom05"><strong><bean:message key="title.periods"/></strong></p>
			<fr:view name="summary" schema="thesis.bean.periods.summary" >
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thleft tdright mtop05 mbottom05"/>
					<fr:property name="labelTerminator" value=""/>
				</fr:layout>
			</fr:view>

			<ul class="list5 mtop05 mbottom15"><li><html:link page="<%= "/manageFinalDegreeWork.do?method=editFinalDegreePeriods&executionDegreeOID=" +  executionDegreeOID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>"><bean:message key="label.edit.periods"/></html:link></li></ul>
		

			<p class="mbottom05"><strong><bean:message key="finalDegreeWorkCandidacy.setRequirements.header"/></strong></p>
			<fr:view name="executionDegree" property="scheduling" schema="thesis.requirements">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thleft tdright mtop05 mbottom05"/>
					<fr:property name="labelTerminator" value=""/>
				</fr:layout>
			</fr:view>
			<ul class="list5 mtop05"><li><html:link page="<%= "/manageFinalDegreeWork.do?method=editFinalDegreeRequirements&executionDegreeOID=" +  executionDegreeOID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>"><bean:message key="label.edit.requirements"/></html:link></li></ul>
	

			
		</div>
		<!-- End Wrap -->
