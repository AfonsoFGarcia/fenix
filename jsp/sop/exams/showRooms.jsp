<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>


 
<html:form action="/roomSearch">
		
	<logic:iterate indexId="i" id="room" name="availableRooms" type="DataBeans.InfoRoom">
			<html:hidden property="availableRoomsId" value="<%= room.getIdInternal().toString() %>" />
	</logic:iterate>

	<h2>Salas livres dia <bean:write name="<%=SessionConstants.DATE%>"/>
 das <bean:write name="<%=SessionConstants.START_TIME%>"/>
 �s <bean:write name="<%=SessionConstants.END_TIME%>"/></h2>
 
	<html:hidden property="method" value="sort"/>
	
	
	<logic:present name="<%=SessionConstants.AVAILABLE_ROOMS%>">		
		<bean:define id="availableRooms" name="<%=SessionConstants.AVAILABLE_ROOMS%>"/>
		<table border="1">
			<tr>
				<th><html:radio property="sortParameter" value="name" onclick="this.form.method.value='sort';this.form.submit();"/> Nome</th>
				<th><html:radio property="sortParameter" value="type" onclick="this.form.method.value='sort';this.form.submit();"/> Tipo</th>
				<th><html:radio property="sortParameter" value="building" onclick="this.form.method.value='sort';this.form.submit();"/> Edificio</th>
				<th><html:radio property="sortParameter" value="floor" onclick="this.form.method.value='sort';this.form.submit();"/> Piso</th>
				<th><html:radio property="sortParameter" value="normal" onclick="this.form.method.value='sort';this.form.submit();"/> Capacidade Normal</th>
				<th><html:radio property="sortParameter" value="exam" onclick="this.form.method.value='sort';this.form.submit();"/> Capacidade Exame</th>
			</tr>
				<logic:iterate id ="infoRoom" name="availableRooms">
			<tr>
				<td>
					<bean:write name="infoRoom" property="nome"/>
				</td>
				<td>
					 <bean:define id="tipo" name="infoRoom" property="tipo"/>
					 <logic:equal name="tipo" value="A">Anfiteatro</logic:equal>
					 <logic:equal name="tipo" value="P">Plana</logic:equal>
					 <logic:equal name="tipo" value="L">Laborat�rio</logic:equal>
				</td>
				<td>
					 <bean:write name="infoRoom" property="edificio"/>
				</td>
				<td>
					 <bean:write name="infoRoom" property="piso"/>
				</td>
				<td>
					<bean:write name="infoRoom" property="capacidadeNormal"/> lugares
				</td>
				<td>
					<bean:write name="infoRoom" property="capacidadeExame"/> lugares
				</td>
			</tr>
				</logic:iterate>
		</table>
	</logic:present>
	<logic:notPresent name="<%=SessionConstants.AVAILABLE_ROOMS%>">
		N�o existem salas dispon�veis.
	</logic:notPresent>
	<html:hidden property="day" />
	<html:hidden property="month" />
	<html:hidden property="year" />
	<html:hidden property="beginningHour" />
	<html:hidden property="beginningMinute" />
	<html:hidden property="endHour" />
	<html:hidden property="endMinute" />

</html:form> 