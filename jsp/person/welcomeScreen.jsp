<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<logic:present name="<%= SessionConstants.LIST_ADVISORY %>" scope="request">
	<bean:size id="numberAdvisories" name="<%= SessionConstants.LIST_ADVISORY %>" scope="request"/>
	<p><bean:message key="label.have"/>
	<span class="emphasis"><bean:write name="numberAdvisories"/></span>
	<bean:message key="label.advisories"/></p>
<table width="90%" cellspacing="0" cellpadding="0" style="border: 1px solid #333;">
	<logic:notEqual name="numberAdvisories" value="0">
		<tr>
			<td colspan="3" style="background: #333; color:#fff; padding: 5px 0 5px 10px;"><strong>AVISOS</strong></td>
		</tr>
		<logic:iterate id="advisory" name="<%= SessionConstants.LIST_ADVISORY %>" scope="request">
			<bean:define id="activeAdvisory" name="activeAdvisory"/>
			<logic:equal name="advisory" property="idInternal" value="<%= activeAdvisory.toString() %>">
				<tr>
					<td colspan="3" style="background: #EBECED; padding: 10px 0 0 10px;"><strong><bean:message key="label.from"/></strong> <bean:write name="advisory" property="sender"/></td>
				</tr>
				<tr>
					<td colspan="3" style="background: #EBECED; padding: 5px 0 0 10px"><strong><bean:message key="label.sendDate"/></strong> <date:format pattern="yyyy.MM.dd"><bean:write name="advisory" property="created.time"/></date:format></td
				</tr>
				<tr>
					<td colspan="3" style="background: #EBECED; padding: 5px 0 0 10px"><strong><bean:message key="label.subject"/></strong> <bean:write name="advisory" property="subject"/><td>
				</tr>
				<tr>
					<td colspan="3" style="background: #EBECED; padding: 5px 0 10px 10px; border-bottom: 1px solid #333;"><bean:write name="advisory" property="message"/></td>
				</tr>
			</logic:equal>
			<logic:notEqual name="advisory" property="idInternal" value="<%= activeAdvisory.toString() %>">
				<tr>
					<td width="25%" style="background: #ccc; border-bottom: 1px solid #333; padding: 5px 0 5px 10px"><bean:write name="advisory" property="sender"/></td>
					<td width="60%" style="background: #fff; border-bottom: 1px solid #333; padding: 5px 0 5px 10px"><html:link page="/index.do" paramId="activeAdvisory" paramName="advisory" paramProperty="idInternal"><bean:message key="label.subject"/> <bean:write name="advisory" property="subject"/> </html:link></td>
					<td width="15%" style="background: #ccc; border-bottom: 1px solid #333; padding: 5px 0 5px 10px"><%--<bean:message key="label.sendDate"/>--%> <date:format pattern="yyyy.MM.dd"><bean:write name="advisory" property="created.time"/></date:format></td>
				</tr>	
			</logic:notEqual>
		</logic:iterate>
	</logic:notEqual>
</logic:present>
</table>
<p>Bem-vindo � sua �rea pessoal.<br /> Poder� visualizar e alterar a sua informa��o pessoal, proceder � altera��o da sua password ler os avisos que lhe s�o enviados pelos org�os de gest�o da escola.</p>
