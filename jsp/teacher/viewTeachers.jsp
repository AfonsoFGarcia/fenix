<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>	



<logic:notPresent name="<%= SessionConstants.TEACHERS_LIST %>">

</logic:notPresent>

<logic:present name="<%= SessionConstants.TEACHERS_LIST %>" >

<bean:message key="title.teachers"/>
<table>
<tr>
<th><bean:message key="label.teacherNumber"/>	
</th>
<th><bean:message key="label.name"/>	
</th>
</tr>	
<logic:iterate id="infoTeacher" name="<%= SessionConstants.TEACHERS_LIST %>">

<tr>
	<td><bean:write name="infoTeacher"  property="teacherNumber"/>	
	</td>
	<td>
	<bean:write name="infoTeacher" property="infoPerson.nome" /> 
	</td>
	<logic:equal name="<%= SessionConstants.IS_RESPONSIBLE %>" value="true">
	<td><html:link page="/teachersManagerDA.do?method=removeTeacher" paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
	<bean:message key="link.removeTeacher"/>
</html:link>
	</td>
	</logic:equal>
</tr>
</logic:iterate>	
</table>
</logic:present>

<logic:equal name="<%= SessionConstants.IS_RESPONSIBLE %>" value="true">
<table>
<tr><td>
	<html:link page="/teacherManagerDA.do?method=prepareAssociateTeacher">
	<bean:message key="link.addTeacher"/>
</html:link>
</tr>

</table>
</logic:equal>