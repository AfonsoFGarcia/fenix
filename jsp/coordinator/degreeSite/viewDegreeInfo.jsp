<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:form action="/degreeSiteManagement">
	<logic:notPresent name="inEnglish">
		<h2><bean:message key="title.coordinator.degreeSite.edit"/></h2>
	</logic:notPresent>
	<logic:present name="inEnglish">
		<bean:define id="inEnglish" name="inEnglish" />
		<html:hidden property="inEnglish" value="<%=  inEnglish.toString() %>"/>
		
		<h2><bean:message key="title.coordinator.degreeSite.editEnglish"/></h2>
	</logic:present>

	<bean:define id="infoExecutionDegreeId" name="infoExecutionDegreeId"/>
	<html:hidden property="infoExecutionDegreeId" value="<%=  infoExecutionDegreeId.toString() %>"/>
	
	<bean:define id="infoDegreeInfoId" name="infoDegreeInfoId"/>
	<html:hidden property="infoDegreeInfoId" value="<%=  infoDegreeInfoId.toString() %>"/>

	<html:hidden property="method" value="editDegreeInformation" />

	<table>	
		<logic:notPresent name="inEnglish">	
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.objectives"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="objectives" cols="80" rows="8"/></td>
				</tr>
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.history"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="history" cols="80" rows="8"/></td>
				</tr>		

				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.professionalExits"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="professionalExits" cols="80" rows="8"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.additionalInfo"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="additionalInfo" cols="80" rows="8"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.links"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="links" cols="80" rows="8"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.testIngression"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="testIngression" cols="80" rows="3"/></td>
				</tr>	

				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.classifications"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="classifications" cols="80" rows="3"/></td>
				</tr>	
				
				<tr>
					<td>
						<table>	
							<tr>			
								<td>
									<p align="left"><strong><bean:message key="label.coordinator.degreeSite.drifts"/>:&nbsp;</strong></p>
								</td>								
								<td>
									<p align="right"><strong><bean:message key="label.coordinator.degreeSite.drifts.initial"/></strong></p>
								</td>
								<td>
									<html:text property="driftsInitial" size="5"/>
								</td>
								<td>
									<p align="right"><strong><bean:message key="label.coordinator.degreeSite.drifts.first"/></strong></p>
								</td>
								<td>
									<html:text property="driftsFirst" size="5"/>
								</td>
								<td>					
									<p align="right"><strong><bean:message key="label.coordinator.degreeSite.drifts.second"/></strong></p>
								</td>
								<td>
									<html:text property="driftsSecond" size="5"/>
								</td>
							</tr>	
						
							<tr>
								<td>
									<p align="left"><strong><bean:message key="label.coordinator.degreeSite.marks"/>:&nbsp;</strong></p>
								</td>							
								<td>
									<p align="right"><strong><bean:message key="label.coordinator.degreeSite.mark.min"/></strong></p>
								</td>
								<td>
									<html:text property="markMin" size="5"/>
								</td>	
								<td>
									<p align="right"><strong><bean:message key="label.coordinator.degreeSite.mark.max"/></strong></p>
								</td>
								<td>
								  <html:text property="markMax" size="5"/>
								</td>
								<td>
								  <p align="right"><strong><bean:message key="label.coordinator.degreeSite.mark.average"/></strong></p>
								</td>
								<td>
									<html:text property="markAverage" size="5"/>
							  </td>
						  </tr>
					  </table>
				  </td>
				</tr>		
				
				<html:hidden property="objectivesEn" />	
				<html:hidden property="historyEn" />
				<html:hidden property="professionalExitsEn" />
				<html:hidden property="additionalInfoEn" />
				<html:hidden property="linksEn" />
				<html:hidden property="testIngressionEn" />
				<html:hidden property="classificationsEn" />
		</logic:notPresent>																																																
		<logic:present name="inEnglish">	
				<html:hidden property="objectives" />	
				<html:hidden property="history" />
				<html:hidden property="professionalExits" />
				<html:hidden property="additionalInfo" />
				<html:hidden property="links" />
				<html:hidden property="testIngression" />
				<html:hidden property="driftsInitial" />	
				<html:hidden property="driftsFirst" />	
				<html:hidden property="driftsSecond" />	
				<html:hidden property="classifications" />
				<html:hidden property="markMin" />	
				<html:hidden property="markMax" />	
				<html:hidden property="markAverage" />	
						
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.objectives"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="objectivesEn" cols="80" rows="8"/></td>
				</tr>
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.history"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="historyEn" cols="80" rows="8"/></td>
				</tr>		

				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.professionalExits"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="professionalExitsEn" cols="80" rows="8"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.additionalInfo"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="additionalInfoEn" cols="80" rows="8"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.links"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="linksEn" cols="80" rows="8"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.testIngression"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="testIngressionEn" cols="80" rows="3"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.classifications"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="classificationsEn" cols="80" rows="3"/></td>
				</tr>			
		</logic:present>
	</table>
	
	</br></br>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>                    		         	
	</html:submit>       
	<html:reset styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>  
	</br></br>
	
	<logic:notPresent name="inEnglish">		
		<div class="gen-button">
			<html:link page="<%= "/degreeSiteManagement.do?method=viewInformation&amp;inEnglish=true&amp;infoExecutionDegreeId=" + infoExecutionDegreeId.toString()%>">
				<bean:message key="link.coordinator.degreeSite.editEnglish"/>
			</html:link>
		</div>
	</logic:notPresent>

	<logic:present name="inEnglish">		
		<div class="gen-button">
			<html:link page="<%= "/degreeSiteManagement.do?method=viewInformation&amp;infoExecutionDegreeId=" + infoExecutionDegreeId.toString()%>">
				<bean:message key="link.coordinator.degreeSite.editPortuguese"/>
			</html:link>
		</div>
	</logic:present>	
</html:form> 