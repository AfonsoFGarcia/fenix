<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<span class="error">
	<html:errors/>
</span>
<br />
<h2><bean:message key="title.exportGroupProperties"/></h2>
<br />

<br/>

	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop">
				<bean:message key="label.teacher.exportGroupProperties.description" />
			</td>
		</tr>
	</table>
	<br/>


<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
<bean:parameter id="nextPage" name="nextPage" />
<bean:parameter id="inputPage" name="inputPage" />
<html:form action="<%=path%>" method="GET">
	<input type="hidden" name="method" value="nextPagePublic"/>
	<input type="hidden" name="nextPage" value="<%= nextPage %>"/>
	<input type="hidden" name="inputPage" value="<%= inputPage %>"/>
	<html:hidden property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
	<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
	<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	<html:hidden property="page" value="1"/>
	<p class="infoop">
		<bean:message key="message.public.degree.choose"/>
	</p>
	<p>
		<bean:message key="property.context.degree"/>
		:
		<html:select property="index" size="1">
			<html:options collection="degreeList" property="value" labelProperty="label"/>
		</html:select>
	</p>
	<p class="infoop">
		<bean:message key="label.chooseYear" />
	</p>
	<p>
	<p>
		<bean:message key="property.context.curricular.year"/>
		:
		<html:select property="curYear" size="1">
			<html:options collection="curricularYearList" property="value" labelProperty="label"/>
		</html:select>
	</p>
	<br />
	<html:submit value="Submeter" styleClass="inputbutton">
		<bean:message key="label.next"/>
	</html:submit>
</html:form>