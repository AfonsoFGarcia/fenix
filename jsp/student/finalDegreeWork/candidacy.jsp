<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message key="title.finalDegreeWork.candidacy"/></h2>
<span class="error"><html:errors /></span>
<html:form action="/finalDegreeWorkCandidacy" focus="executionDegreeOID">
	<html:hidden property="method" value="somemethod"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal"/>
	<html:hidden property="studentToRemove"/>
	<html:hidden property="selectedGroupProposal"/>

	<bean:message key="label.finalDegreeWork.degree"/>:
	<br />
	<html:select property="executionDegreeOID" size="1"
				 onchange='this.form.method.value=\'selectExecutionDegree\';this.form.page.value=\'0\';this.form.submit();'>
	<html:option value=""/>
		<html:options property="idInternal"
					  labelProperty="infoDegreeCurricularPlan.infoDegree.nome"
					  collection="infoExecutionDegrees" />
	</html:select>
	<br />
	<br />
	<logic:present name="infoGroup">
		<bean:message key="label.finalDegreeWork.group"/>:
		<br />
		<logic:present name="infoGroup" property="groupStudents">
			<table>
				<tr>
					<td class="listClasses-header">
						<bean:message key="label.finalDegreeWork.group.username"/>
					</td>
					<td class="listClasses-header">
						<bean:message key="label.finalDegreeWork.group.name"/>
					</td>
					<td class="listClasses-header">
					</td>
				</tr>
			<logic:iterate id="groupStudent" name="infoGroup" property="groupStudents">
				<bean:define id="student" name="groupStudent" property="student"/>

				<tr>
					<td class="listClasses">
						<bean:write name="student" property="infoPerson.username"/>
					</td>
					<td class="listClasses">
						<bean:write name="student" property="infoPerson.nome"/>
					</td>
					<td class="listClasses">
						<bean:define id="onClick">
							this.form.method.value='removeStudent';this.form.studentToRemove.value='<bean:write name="student" property="idInternal"/>';
						</bean:define>
						<html:submit styleClass="inputbutton" onclick='<%= onClick.toString() %>'>
							<bean:message key="label.finalDegreeWork.group.remove"/>
						</html:submit>
					</td>
				</tr>
			</logic:iterate>
				<tr>
					<td class="listClasses">
						<html:text property="studentUsernameToAdd" size="6"/>
					</td>
					<td class="listClasses">
					</td>
					<td class="listClasses">
						<html:submit styleClass="inputbutton" onclick='this.form.method.value=\'addStudent\';'>
							<bean:message key="label.finalDegreeWork.group.add"/>
						</html:submit>
					</td>
				</tr>
			</table>
		</logic:present>
		<br />
		<bean:message key="label.finalDegreeWork.groupProposals"/>:
		<br />
		<logic:present name="infoGroup" property="groupProposals">
			<table>
				<tr>
					<td class="listClasses-header" rowspan="2">
						<bean:message key="label.finalDegreeWork.proposal.orderOfPreference"/>
					</td>
					<td class="listClasses-header" rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.number"/>
					</td>
					<td class="listClasses-header" rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.title"/>
					</td>
					<td class="listClasses-header">
						<bean:message key="finalDegreeWorkProposalHeader.orientatorName"/>
					</td>
					<td class="listClasses-header" rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.companyLink"/>
					</td>
					<td class="listClasses-header" rowspan="2">
					</td>
				</tr>
				<tr>
					<td class="listClasses-header">
						<bean:message key="finalDegreeWorkProposalHeader.coorientatorName"/>
					</td>
				</tr>
				<logic:iterate id="groupProposal" name="infoGroup" property="groupProposals">

					<tr>
						<td class="listClasses" rowspan="2">
							<bean:define id="groupProposalOrderOfPreference" name="groupProposal" property="orderOfPreference"/>
							<bean:define id="onChange">
								this.form.method.value='changePreferenceOrder';this.form.selectedGroupProposal.value='<bean:write name="groupProposal" property="idInternal"/>';this.form.submit();
							</bean:define>
							<bean:define id="propertyName">orderOfProposalPreference<bean:write name="groupProposal" property="idInternal"/></bean:define>
							<html:text property='<%= propertyName %>' size="2"
									   value='<%= groupProposalOrderOfPreference.toString() %>'
									   onchange='<%= onChange.toString() %>'
								/>
						</td>
						<td class="listClasses" rowspan="2">
							<bean:write name="groupProposal" property="finalDegreeWorkProposal.proposalNumber"/>
						</td>
						<td class="listClasses" rowspan="2">
							<bean:define id="proposalID" name="groupProposal" property="finalDegreeWorkProposal.idInternal"/>
							<html:link target="_blank" href="<%= request.getContextPath() + "/publico/finalDegreeWorks.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + proposalID.toString() %>">
								<bean:write name="groupProposal" property="finalDegreeWorkProposal.title"/>
					        </html:link>
						</td>
						<td class="listClasses">
							<bean:write name="groupProposal" property="finalDegreeWorkProposal.orientator.infoPerson.nome"/>
						</td>
						<td class="listClasses" rowspan="2">
							<bean:write name="groupProposal" property="finalDegreeWorkProposal.companionName"/>
						</td>
						<td class="listClasses" rowspan="2">
							<bean:define id="onClick">
								this.form.method.value='removeProposal';this.form.selectedGroupProposal.value='<bean:write name="groupProposal" property="idInternal"/>';
							</bean:define>
							<html:submit styleClass="inputbutton" onclick='<%= onClick.toString() %>'>
								<bean:message key="label.finalDegreeWork.group.remove"/>
							</html:submit>
						</td>
					</tr>
					<tr>
						<td class="listClasses">
							<logic:present name="groupProposal" property="finalDegreeWorkProposal.coorientator">
								<bean:write name="groupProposal" property="finalDegreeWorkProposal.coorientator.infoPerson.nome"/>
							</logic:present>
						</td>
					</tr>

				</logic:iterate>
			</table>
		</logic:present>
		<br />
		<html:submit onclick='this.form.method.value=\'selectProposals\';'>
			<bean:message key="link.finalDegreeWork.selectProposals"/>
		</html:submit>			
	</logic:present>
</html:form>
<br />