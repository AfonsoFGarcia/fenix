<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<tiles:importAttribute />
<bean:size id="rowSize" name="rows" />	
<bean:define id="emptyListMessage" >
	<tiles:getAsString name="emptyRows" />
</bean:define>		
<logic:equal name="rowSize" value="0">
	<bean:message name="emptyListMessage" />
</logic:equal>	
<logic:notEqual name="rowSize" value="0">
<bean:size id="headerSize" name="headers" />	
	<h3 style="text-align: center;">
		<bean:define id="title" >
		  	<tiles:getAsString name="title" />
		</bean:define>	
		<bean:message name="title" />
	</h3>	
<table>	
	<tr>
		<logic:iterate id="header" name="headers">
			<td class="listClasses-header"><bean:write name="header" /></th>	
		</logic:iterate>
	</tr>
	<logic:iterate id="row" name="rows" indexId="rowIndex">
		<bean:define id="mod">
			<%= String.valueOf(rowIndex.intValue() % headerSize.intValue()) %>
		</bean:define>
		<bean:define id="startTr" value="" />
		<bean:define id="closeTr" value="" />
		<logic:equal name="mod" value="0">
			<bean:define id="startTr">
				<%= "<tr>" %>
			</bean:define>
		</logic:equal>
		<logic:equal name="mod" value="<%= String.valueOf(headerSize.intValue() - 1) %>">
			<logic:notEqual name="rowIndex" value="0">
				<bean:define id="closeTr">
					<%= "</tr>" %>						
				</bean:define>
			</logic:notEqual>
		</logic:equal>
		<bean:write name="startTr" filter="false"></bean:write>
			<td nowrap="nowrap" class="verbete-td"><bean:write name="row" filter="false"/></td>
		<bean:write name="closeTr" filter="false"></bean:write>
	</logic:iterate>
</table> 
</logic:notEqual>