<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants" %>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.visualize"/></h2>
<center>
<span class="error"><html:errors/></span>

<br/>
<logic:present name="<%= SessionConstants.WORK_LOCATIONS_LIST %>" scope="request">
	<html:form action="/visualizeExternalPersons.do?method=visualize" >
		<html:hidden property="page" value="1" />
		<table>
			<tr>
				<td>
					<bean:message key="label.masterDegree.administrativeOffice.externalPersonInstitution"/>:&nbsp;					
					<html:select property="institutionId">
			    		<html:options collection="<%= SessionConstants.WORK_LOCATIONS_LIST %>" property="value" labelProperty="label" />
			   		</html:select> 
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<!-- Submit button -->
					<html:submit styleClass="inputbuttonSmall">
						<bean:message key="button.submit.masterDegree.externalPerson.find"/>
					</html:submit>	
				</td>
			</tr>
		</table>
	</html:form>
</logic:present>
</center>