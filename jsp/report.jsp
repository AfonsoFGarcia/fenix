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
<table style="border: 1px solid #000;" cellspacing="2"> 	
	<tr>
		<logic:iterate id="header" name="headers" indexId="headerIndex">
				<bean:define id="classHeader" toScope="request">{border-right: 1px solid #000; border-bottom: 1px solid #000;}</bean:define>
			<logic:equal name="headerIndex" value="<%= String.valueOf(headerSize.intValue() - 1) %>">
			<%-- ultima coluna --%>			
				<bean:define id="classHeader" toScope="request">{border-bottom: 1px solid #000; border-right: 0px;}</bean:define>
			</logic:equal>
			
			<td style='<bean:write name="classHeader" />'><bean:write name="header" />    <bean:write name="headerIndex"/>			</td>	
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



		
		<logic:equal name="mod" value="<%= String.valueOf(headerSize.intValue() - 1) %>">
			<%-- ultima coluna --%>
			<bean:define id="classTd">{border-bottom: 1px dotted #666;}</bean:define>
			
			<logic:greaterEqual name="rowIndex" value="<%= String.valueOf(rowSize.intValue() - headerSize.intValue()) %>">
				<logic:lessEqual name="rowIndex" value="<%= String.valueOf(rowSize.intValue() - 1) %>">
				<%-- ultima coluna e ultima linha --%>
				
				<bean:define id="classTd">{}</bean:define>			
				</logic:lessEqual>	
			</logic:greaterEqual>
		</logic:equal>
		<logic:notEqual name="mod" value="<%= String.valueOf(headerSize.intValue() - 1) %>">
			<%-- nao e a ultima coluna --%>
			<bean:define id="classTd">{border-right: 1px solid #000; border-bottom: 1px dotted #666;}</bean:define>			
	
			<logic:greaterEqual name="rowIndex" value="<%= String.valueOf(rowSize.intValue() - headerSize.intValue() - 1) %>">
				<logic:lessEqual name="rowIndex" value="<%= String.valueOf(rowSize.intValue() - 1) %>">
					<%-- nao e a ultima coluna e e' a ultima linha --%>
					<bean:define id="classTd">{border-right: 1px solid #000;}</bean:define>	
				</logic:lessEqual>	
			</logic:greaterEqual>
			
		</logic:notEqual>

			<td nowrap="nowrap" style="<bean:write name='classTd'/>"><bean:write name="row" filter="false"/></td>



		<bean:write name="closeTr" filter="false"></bean:write>
	</logic:iterate>
</table> 
</logic:notEqual>