<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<h2><bean:message key="label.editGuide" /></h2>

<html:form action="/guideManagement">
	<html:hidden property="method" value="editGuide" />
	<html:hidden property="page" value="1" />
	<html:hidden property="number" />
	<html:hidden property="year" />
	<html:hidden property="version" />
	<html:hidden property="guideID" />
	<html:hidden property="selectedGuideEntryDocumentType" />
	<html:hidden property="selectedGuideEntryID" />
	<html:hidden property="guideSituationID" />
	

	<bean:define id="paymentTransactions" name="paymentTransactions" type="java.util.ArrayList"/>

	<table>
		<tr>
			<td colspan="4"><b>Nome Aluno:</b> <bean:write name="guide" property="infoPerson.nome" />
			</td>
		</tr>
		<tr>
			<td><b>N�mero:</b> <bean:write name="guide" property="number" /></td>
		</tr>
		<tr>
			<td><b>Ano:</b> <bean:write name="guide" property="year" /></td>
		</tr>
		<tr>
			<td><b>Vers�o:</b> <bean:write name="guide" property="version" /></td>
		</tr>		
		<tr>
			<td colspan="5">
				<b>Curso:</b> 
				<html:select property="newDegreeCurricularPlanID">
					<html:options collection="degreeCurricularPlans" property="value"
						labelProperty="label" />
				</html:select>
				 - 
				<html:select property="newExecutionYear">
					<html:options collection="executionYears" property="value"
						labelProperty="label" />
				</html:select>
			</td>
		</tr>		
		<tr>
			<td><b>Total:</b> <bean:write name="guide" property="total" /></td>
		</tr>
		<tr>
			<td><b>Tipo Pagamento:</b>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.transactions.PaymentType" bundle="ENUMERATION_RESOURCES"/>
	            <html:select property="newPaymentType">
	               	<html:option key="dropDown.Default" value=""/>
	                <html:options collection="values" property="value" labelProperty="label"/>
	            </html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:define id="behavior" >
					this.form.method.value='deleteGuide';				
				</bean:define>
				<html:submit value="Apagar Vers�o" onclick="<%= behavior %>" />
				
				<html:submit value="Submeter"
				onclick="this.form.method.value='editExecutionDegree'" />
			</td>
		</tr>
		
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><h2>Entradas de Guia:</h2></td>
		</tr>
				<tr>
			<td>&nbsp;</td>
		</tr>
	<tr>
		<td><b>Tipo</b></td><td><b>Descri��o</b></td><td><b>Quantidade</b></td><td><b>Pre�o</b></td>
	</tr>
		
			<logic:iterate id="guideEntry" name="guide"
				property="infoGuideEntries" indexId="index">
		<tr>				
				<td>
					<bean:define id="documentType"><bean:write name="guideEntry" property="documentType"/></bean:define>
					<bean:message name="documentType" bundle="ENUMERATION_RESOURCES" />
				</td><td>
				<bean:write name="guideEntry" property="description" /> </td> <td>
					<bean:write name="guideEntry" property="quantity" />  </td><td>
					<bean:write name="guideEntry" property="price" /> </td><td>
					
				<bean:define id="behavior" >
					this.form.method.value='deleteGuideEntry';
					this.form.selectedGuideEntryID.value='<bean:write name="guideEntry" property="idInternal" />';					
				</bean:define>
				<html:submit value="Apagar Entrada" onclick="<%= behavior %>" />					
												
				<% if (paymentTransactions.get(index.intValue()) != null){ %>
					Tem Transa��o
				<% }else{ %>
					<bean:define id="behavior" >
						this.form.method.value='createPaymentTransaction';
						this.form.selectedGuideEntryID.value='<bean:write name="guideEntry" property="idInternal" />';
						this.form.selectedGuideEntryDocumentType.value='<bean:write name="documentType" />';
					</bean:define>
					<html:submit value="Criar Transa��o" onclick="<%= behavior %>" />				
				<% }%>	
								
				<br />
		
				</tr>
			</logic:iterate>
		

		<tr>
			<td>&nbsp;</td>
		</tr>

		<tr>
			<td><b>Nova entrada de Guia:</b></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td ><b>Tipo de Documento: </b></td>
			<td><b>Descri��o: </b></td>
			<td><b>Quantidade: </b></td>
			<td><b>Pre�o: </b></td>
		</tr>
		<tr>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.DocumentType" bundle="ENUMERATION_RESOURCES"/>
				<html:select property="newEntryDocumentType">
					<html:option key="dropDown.Default" value=""/>
					<html:options collection="values" property="value" labelProperty="label" />
				</html:select>
			</td>
			<td><html:text property="newEntryDescription" /></td>
			<td><html:text size="4" property="newEntryQuantity" /></td>
			<td><html:text size="4" property="newEntryPrice" /></td>
		</tr>
				
		<tr>
			<td><html:submit value="Adicionar Entrada"
				onclick="this.form.method.value='addGuideEntry'" /></td>
		</tr>

		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><h2>Situa��es de Guia</h2></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>		
		
		<logic:iterate id="guideSituation" name="guide" property="infoGuideSituations" >
			<tr>
				<bean:define id="behavior" >
					this.form.method.value='deleteGuideSituation';
					this.form.guideSituationID.value='<bean:write name="guideSituation" property="idInternal" />';
				</bean:define>
				
				<td colspan="5"><bean:write name="guideSituation" property="remarks" />
				- <bean:write name="guideSituation" property="idInternal" />
				- <bean:write name="guideSituation" property="situation.name" />
				- <bean:write name="guideSituation" property="state" />
				- <html:submit value="Apagar Situa��o" onclick="<%= behavior %>" />	</td>	
			</tr>		
		</logic:iterate>
		
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b>Nova Situa��o:</b></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td ><b>Descri��o:</b></td>
			<td colspan="2"><b>Data: </b></td>
			<td colspan="2"><b>Situa��o: </b></td>			
		</tr>
			<td ><html:text size="30" property="newSituationRemarks" /></td>
			<td colspan="2"> 				
				<html:select property="newSituationDay">
					<html:options collection="days" property="value"
						labelProperty="label" />
				</html:select>
				/
				<html:select property="newSituationMonth">
					<html:options collection="months" property="value"
						labelProperty="label" />
				</html:select>				
				/
				<html:select property="newSituationYear">
					<html:options collection="years" property="value"
						labelProperty="label" />
				</html:select>					
			</td>
			<td colspan="2">
			<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.GuideState" bundle="ENUMERATION_RESOURCES"/>
			<html:select property="newSituationType">
				<html:option key="dropDown.Default" value=""/>
				<html:options collection="values" property="value" labelProperty="label" />
			</html:select></td>
		</tr>		
		<tr>
			<td><html:submit value="Adicionar Situa��o"
				onclick="this.form.method.value='addGuideSituation'" /></td>
		</tr>

		<tr>
			<td>&nbsp;</td>
		</tr>

	</table>

</html:form>
