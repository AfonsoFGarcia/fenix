<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="titulo.extraWork.money.cost.center" /></h2>
<html:form action="/managementMoneyCostCenter.do" focus="year">
	<html:hidden property="method" value="readAllByYear"/>
	<html:hidden property="page" value="1"/>
	<table>
		<tr>
			<td colspan="2" class="infoop">
				<bean:message key="info.managementAssiduousness.extraWork.money.cost.center.year"/>
			</td>		
		</tr>
		<logic:messagesPresent>	
			<tr>
				<td colspan="2" >
					<br /><br />
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<span class="error"><html:errors/></span>
				</td>		
			</tr>
		</logic:messagesPresent>
		<tr>
			<td colspan="2" >
				<br /><br />
			</td>
		</tr>
		<tr>
			<td width="1%" nowrap>
				<bean:message key="label.year" />:
			</td>
			<td  width="99%">
				<html:text property="year" size="4" maxlength="4"/>
			</td>		
		</tr>
		<tr>
			<td colspan="2">
				<br /><br />
			</td>		
		</tr>
	</table>

	<html:submit styleClass="inputbutton">
		<bean:message key="botao.avan�ar"/>
	</html:submit>
</html:form>