<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="java.util.Map" %>

	<logic:present name="seminaries">
		<logic:present name="cases">
			<logic:present name="modalities">
				<logic:present name="cases">
						<h2>
							<bean:message key="label.candicaciesGrid.Title"/>
						</h2>
						<h3>
							<bean:message key="message.candidaciesGridHints"/>
						</h3>
						<html:form action="/showCandidacies.do" method="get">
							<table>
								<tr>
									<td>
										<bean:message key="label.seminary.candidaciesGrid"/>
									</td>
									<td>
										<html:select property="seminaryID">
												<html:option value="-1" key="label.seminary.candidaciesGrid.select">
													<bean:message key="label.seminary.candidaciesGrid.select"/>
												</html:option>
											<html:options collection="seminaries" property="idInternal" labelProperty="name"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.degree.candidaciesGrid"/>
									</td>
									<td>
										<html:select property="degreeID">
											<html:option value="-1" key="label.degree.candidaciesGrid.select">
												<bean:message key="label.degree.candidaciesGrid.select"/>
											</html:option>
											<html:options collection="degrees" property="idInternal" labelProperty="name"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.course.candidaciesGrid"/>
									</td>
									<td>
										<html:select property="courseID">
											<html:option value="-1" key="label.course.candidaciesGrid.select">
												<bean:message key="label.course.candidaciesGrid.select"/>
											</html:option>
											<html:options collection="courses" property="idInternal" labelProperty="name"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.modality.candidaciesGrid"/>
									</td>
									<td>
										<html:select property="modalityID">
											<html:option value="-1" key="label.modality.candidaciesGrid.select">
												<bean:message key="label.modality.candidaciesGrid.select"/>
											</html:option>
											<html:options collection="modalities" property="idInternal" labelProperty="name"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.theme.candidaciesGrid"/>
									</td>
									<td>
										<html:select property="themeID">
											<html:option value="-1" key="label.theme.candidaciesGrid.select">
												<bean:message key="label.theme.candidaciesGrid.select"/>
											</html:option>
											<html:options collection="themes" property="idInternal" labelProperty="name"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.case1.candidaciesGrid"/>
									</td>
									<td>
										<html:select property="case1ID">
											<html:option value="-1" key="label.case1.candidaciesGrid.select">
												<bean:message key="label.case1.candidaciesGrid.select"/>
											</html:option>
											<logic:iterate name="cases" id="caseStudy" type="DataBeans.Seminaries.InfoCaseStudy">
												<option value="<bean:write name="caseStudy" property="idInternal"/>"
														title="<bean:write name="caseStudy" property="name"/>">
													<bean:write name="caseStudy" property="code"/>
												</option>
											</logic:iterate>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.case2.candidaciesGrid"/>
									</td>
									<td>
										<html:select property="case2ID">
											<html:option value="-1" key="label.case2.candidaciesGrid.select">
												<bean:message key="label.case2.candidaciesGrid.select"/>
											</html:option>
											<logic:iterate name="cases" id="caseStudy" type="DataBeans.Seminaries.InfoCaseStudy">
												<option value="<bean:write name="caseStudy" property="idInternal"/>"
														title="<bean:write name="caseStudy" property="name"/>">
													<bean:write name="caseStudy" property="code"/>
												</option>
											</logic:iterate>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.case3.candidaciesGrid"/>
									</td>
									<td>
										<html:select property="case3ID">
											<html:option value="-1" key="label.case3.candidaciesGrid.select">
												<bean:message key="label.case3.candidaciesGrid.select"/>
											</html:option>
											<logic:iterate name="cases" id="caseStudy" type="DataBeans.Seminaries.InfoCaseStudy">
												<option value="<bean:write name="caseStudy" property="idInternal"/>"
														title="<bean:write name="caseStudy" property="name"/>">
													<bean:write name="caseStudy" property="code"/>
												</option>
											</logic:iterate>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.case4.candidaciesGrid"/>
									</td>
									<td>
										<html:select property="case4ID">
											<html:option value="-1" key="label.case4.candidaciesGrid.select">
												<bean:message key="label.case4.candidaciesGrid.select"/>
											</html:option>
											<logic:iterate name="cases" id="caseStudy" type="DataBeans.Seminaries.InfoCaseStudy">
												<option value="<bean:write name="caseStudy" property="idInternal"/>"
														title="<bean:write name="caseStudy" property="name"/>">
													<bean:write name="caseStudy" property="code"/>
												</option>
											</logic:iterate>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.case5.candidaciesGrid"/>
									</td>
									<td>
										<html:select property="case5ID">
											<html:option value="-1" key="label.case5.candidaciesGrid.select">
												<bean:message key="label.case5.candidaciesGrid.select"/>
											</html:option>
											<logic:iterate name="cases" id="caseStudy" type="DataBeans.Seminaries.InfoCaseStudy">
												<option value="<bean:write name="caseStudy" property="idInternal"/>"
														title="<bean:write name="caseStudy" property="name"/>">
													<bean:write name="caseStudy" property="code"/>
												</option>
											</logic:iterate>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<html:submit style="width:50%" styleClass="button" value="OK" property="submition"/>
										<html:reset style="width:50%" value="Limpar"/>
									</td>
								</tr>
								<tr>
									<td>
									<%Map parameters = request.getParameterMap();
									request.setAttribute("parameters",parameters);%>
									<bean:define id="args" type="java.util.Map" name="parameters"/>
								<html:link page="/getTabSeparatedCandidacies.do" name="args">
									<bean:message key="link.getExcelSpreadSheet"/><br/><br/>
								</html:link>
								<bean:define name="candidacies" type="java.util.List" id="candidacies"/>
								<%=candidacies.size()%> <bean:message key="message.enrolledStudents"/>
									</td>
								</tr>
							</table>
							<table>
								<tr>
									<td class="listClasses-header">
										Detalhes
									</td>
									<td class="listClasses-header">
										N�
									</td>
									<td class="listClasses-header">
										Nome
									</td>
									<td class="listClasses-header">
										Semin�rio
									</td>
									<td class="listClasses-header">
										Curso
									</td>
									<td class="listClasses-header">
										Disciplina
									</td>
									<td class="listClasses-header">
										Modalidade
									</td>
									<td class="listClasses-header">
										Tema
									</td>
									<td class="listClasses-header">
										Motiva��o
									</td>
									<td class="listClasses-header">
										Caso 1
									</td>
									<td class="listClasses-header">
										Caso 2
									</td>
									<td class="listClasses-header">
										Caso 3
									</td>
									<td class="listClasses-header">
										Caso 4
									</td>
									<td class="listClasses-header">
										Caso 5
									</td>
								</tr>
				<logic:present name="candidacies">
						<logic:notEmpty name="candidacies">
							<logic:iterate name="candidacies" id="candidacy" type="DataBeans.Seminaries.InfoCandidacyDetails">
								<tr>
									<td class="listClasses">
										<html:link page="/candidacyDetails.do" 
												paramId="objectCode" 
												paramName="candidacy" 
												paramProperty="idInternal">
											Ver
										</html:link>
									</td>
									<td class="listClasses">
										<bean:write name="candidacy" property="student.number"/>
									</td>
									<td  class="listClasses" title="<bean:write name="candidacy" property="student.infoPerson.nome"/>">
										<%
										String shortName = candidacy.getStudent().getInfoPerson().getNome();
										String[] names = shortName.split(" ");
										String firstName = names[0];
										String lastName = names[names.length-1];
										out.print(firstName + " " + lastName);
										%>
									</td>
									<td class="listClasses">
										<html:link page="/showCandidacies.do" 
											  paramId="seminaryID" 
											  paramName="candidacy" 
											  paramProperty="seminary.idInternal">
											<bean:write name="candidacy" property="seminary.name"/>
										</html:link>
									</td>
									<td  class="listClasses" title="<bean:write name="candidacy" property="curricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/>">
										<html:link page="/showCandidacies.do"
											  paramId="degreeID"
											  paramName="candidacy"
											  paramProperty="curricularCourse.infoDegreeCurricularPlan.idInternal">
											<bean:write name="candidacy" property="curricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/>
										</html:link>
									</td>
									<td class="listClasses">
										<html:link page="/showCandidacies.do"
											  paramId="courseID"
											  paramName="candidacy"
											  paramProperty="curricularCourse.idInternal">
											<bean:write name="candidacy" property="curricularCourse.name"/>
										</html:link>
									</td>
									<td class="listClasses">
										<html:link page="/showCandidacies.do"
											  paramId="modalityID"
											  paramName="candidacy"
											  paramProperty="modality.idInternal">
											<bean:write name="candidacy" property="modality.name"/>
										</html:link>
									</td>
									<td class="listClasses">
										<logic:notEmpty name="candidacy" property="theme">
											<html:link page="/showCandidacies.do"
													paramId="themeID"
													paramName="candidacy"
													paramProperty="theme.idInternal">
												<bean:write name="candidacy" property="theme.name"/>
											</html:link>
										</logic:notEmpty>
										<logic:empty name="candidacy" property="theme">
												N/A
										</logic:empty>
									</td>
									<td class="listClasses">
										<bean:write name="candidacy" property="motivation"/>
									</td>
									<logic:iterate indexId="index" name="candidacy" property="cases" id="caseStudy" type="DataBeans.Seminaries.InfoCaseStudy">
										<td  class="listClasses" title="<bean:write name="caseStudy" property="name"/>">
											<html:link page="/showCandidacies.do"
														paramId="<%="case" + (index.intValue()+1) +"ID"%>"
														paramName="caseStudy"
														paramProperty="idInternal">
												<bean:write name="caseStudy" property="code"/>
											</html:link>
										</td>
									</logic:iterate>
								</tr>
							</logic:iterate>
							</table>
					</logic:notEmpty>
				</logic:present>
			</html:form>
			</logic:present>
		</logic:present>
	</logic:present>
</logic:present>