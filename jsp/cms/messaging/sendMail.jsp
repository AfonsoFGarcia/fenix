<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="sendMailActioName" name="sendMailForm" property="sendMailActioName"/>
<jsp:include page="/cms/manager/context.jsp"/>

<logic:present name="groupsToChooseFrom">
	<bean:define id="groups" name="sendMailForm" property="groupsToChooseFrom"/>
</logic:present>

<html:form method="post" action="<%=sendMailActioName.toString()%>">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="send"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.group" property="group"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.returnURL" property="returnURL"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.previousRequestParameters" property="previousRequestParameters"/>

<logic:present name="groups">
	<div style="width:800px">
		<ul>
			<logic:iterate id="group" name="groups" type="net.sourceforge.fenixedu.domain.PersonalGroup">
				<li class="fleft" style="width: 250px;">
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedPersonalGroupsIds" property="selectedPersonalGroupsIds">
	       				 <bean:write name="group" property="idInternal"/>
				    </html:multibox>
			        <bean:write name="group" property="name"/>
				</li>
			</logic:iterate>			
		</ul>			
	</div>
</logic:present>

<div style="clear: both;">
	<br/>
	<fieldset class="lfloat2">
	<p><label>Nome do Remetente:</label><html:text bundle="HTMLALT_RESOURCES" altKey="text.fromPersonalName" property="fromPersonalName" size="50"/></p>	
	<p><label>Endere�o do Remetente:</label> <html:text bundle="HTMLALT_RESOURCES" altKey="text.fromAddress" property="fromAddress" size="50"/></p>
	<p><label>C�pia para:</label> <html:text bundle="HTMLALT_RESOURCES" altKey="text.copyTo" property="copyTo" size="50"/><font size="-2"> (separe os endere�os por v�rgulas)</font></p>
	<p><label>C�pia para o remetente</label><html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.copyToSender" property="copyToSender"/>(assinale caso deseje receber uma c�pia da mensagem no endere�o do remetente)</p> 
	<p><label>Assunto:</label> <html:text bundle="HTMLALT_RESOURCES" altKey="text.subject" property="subject" size="50"/></p>
	<p><label>Mensagem:</label> <html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.message" rows="20" cols="100" property="message"/></p>
	</fieldset>
</div>

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='send';this.form.submit();">
	Enviar
</html:submit>

</html:form>