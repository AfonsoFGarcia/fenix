<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.ArrayList" %>

<logic:present name="publico.infoRooms" >
  <h2><bean:message key="title.chooseRoom"/></h2>
  <br/>
  <span class="error"><html:errors/></span>	
			<html:form action="/viewRoom.do" method="GET">

			<html:hidden name="roomForm" property="name"/>
			<html:hidden name="roomForm" property="building"/>
			<html:hidden name="roomForm" property="floor"/>
			<html:hidden name="roomForm" property="type"/>
			<html:hidden name="roomForm" property="capacityNormal"/>
			<html:hidden name="roomForm" property="capacityExame"/>

			<table border='0' cellpadding='10' cellspacing='0'>		
			<logic:iterate id="infoRoom" name="publico.infoRooms" indexId="i_index">
			<bean:define id="i" value="i_index" />
                <tr>
                    <td>
	                    <html:radio idName="infoRoom" property="roomName" value="nome"/>
                    </td>
                    <td>
	                    <bean:write name="infoRoom" property="nome"/>
                    </td>
                </tr>
			</logic:iterate>
		</table>
		<br/>
		<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden  property="method" value="roomViewer" />	
		<html:submit styleClass="inputbutton">
			<bean:message key="label.choose"/>
		</html:submit>
	</html:form>
	</logic:present>
	
	<logic:notPresent name="publico.infoRooms" >
		<table align="center" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<span class="error"><bean:message key="message.public.notfound.rooms"/></span>
				</td>
			</tr>
		</table>
	</logic:notPresent>