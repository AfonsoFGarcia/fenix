<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2>
	<bean:message key="title.manage.schedule"/>
</h2>

<span class="error"><html:errors/></span>

<html:form action="/chooseContext">

	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="choose"/>
	<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>

	<table width="100%" border="0" cellpadding="0" cellspacing="0">
    	<tr>
        	<td bgcolor="#FFFFFF" class="infoop">
        		Por favor, proceda &agrave; escolha do curso pretendido.
        	</td>
        </tr>
    </table>

	<br />
	<p>
		<bean:message key="property.context.degree"/>:
		<html:select property="executionDegreeOID" size="1">
       		<html:options collection="licenciaturas"
       					  property="value"
       					  labelProperty="label"/>
       </html:select>
	</p>

	<br />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td bgcolor="#FFFFFF" class="infoop"><bean:message key="label.chooseYear" /></td>
		</tr>
	</table>

	<br />
	<br />   
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td nowrap class="formTD">
				<bean:message key="property.context.curricular.year"/>:
			</td>
			<td nowrap class="formTD">
				<html:select property="curricularYear" size="1">
		       		<html:options collection="anosCurriculares"
		       					  property="value"
		       					  labelProperty="label"/>
		       	</html:select>
		    </td>
		</tr>
	</table>

	<br />
	<p>
		<html:submit value="Submeter" styleClass="inputbutton">
             <bean:message key="label.next"/>
       </html:submit>
	</p>
</html:form>