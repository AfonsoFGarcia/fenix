<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<logic:present name="publico.infoClasses" scope="session">
<table>
		<tr>
			<th>
				<bean:message key="property.name"/>
			</th>
			<th>
				<bean:message key="property.context.semester"/>
			</th>
				<th>
				<bean:message key="property.context.curricular.year"/> 
			</th>
		</tr>		
	<logic:iterate id="classview" name="publico.infoClasses" scope="session"	>
		<tr>
			<td><html:link page="/viewClassTimeTable.do" paramId="className" paramName="classview" paramProperty="nome">
			<jsp:getProperty name="classview" property="nome"/>
			</html:link>
			</td>
			<td>
			<bean:write name="classview" property="infoExecutionPeriod.name"/>
			</td>
			<td>
			<jsp:getProperty name="classview" property="anoCurricular"/>
			</td>
		</tr>	
	</logic:iterate>
</table>
</logic:present>
<logic:notPresent name="publico.infoClasses" scope="session">
<table align="center" border="0" cellpadding="0" cellspacing="0">
			<tr align="center">
				<td>
					<span class="error"><bean:message key="message.public.notfound.classes"/></span>
				</td>
			</tr>
</table>
</logic:notPresent>
