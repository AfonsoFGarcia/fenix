<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<span class="error"><html:errors/></span>

<html:form action="/chooseExamsMapContextDA">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="choose"/>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
    	<tr>
        	<td bgcolor="#FFFFFF" class="infoop">Por favor, proceda &agrave; escolha
            da licenciatura pretendida.</td>
        </tr>
    </table>
	<br />
    <p><bean:message key="property.context.degree"/>:
	<html:select property="index" size="1">
    	<html:options collection="<%=SessionConstants.DEGREES %>" property="value" labelProperty="label"/>
    </html:select>
	</p>
	<br />
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
    	<tr>
        	<td bgcolor="#FFFFFF" class="infoop"><bean:message key="label.select.curricularYears" /></td>
        </tr>
    </table>
	<br />
	<br />   
   	<bean:message key="property.context.curricular.year"/>:<br/>
	<logic:present name="<%= SessionConstants.CURRICULAR_YEAR_LIST_KEY %>" scope="session">
		<logic:iterate id="item" name="<%= SessionConstants.CURRICULAR_YEAR_LIST_KEY %>">
			<html:multibox property="selectedCurricularYears">
				<bean:write name="item"/>
			</html:multibox>
			<bean:write name="item"/> � ano <br/>
		</logic:iterate>
		<html:checkbox property="selectAllCurricularYears">
			<bean:message key="checkbox.show.all"/><br/>
		</html:checkbox>
	</logic:present>
	<br/>
   <p><html:submit value="Submeter" styleClass="inputbutton">
   		<bean:message key="label.next"/>
   </html:submit></p>
</html:form>
