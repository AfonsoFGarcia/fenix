<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors /></span>
<h2><bean:write name="<%=SessionConstants.INFO_SECTION %>" property="name"/></h2>
<table	 cellpadding="0" border="0">
	<tr> 
		<td><div class="gen-button"><html:link page="/editSection.do?method=prepareEdit">
			<bean:message key="button.editSection"/>
			</html:link>
			</div>
		</td>
		<td><div class="gen-button"><html:link page="/deleteSection.do">
			<bean:message key="button.deleteSection"/>
			</html:link>
			</div>
		</td>
		<td><div class="gen-button"><img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="" /> <html:link page="/prepareInsertSection.do?method=prepareInsertRegularSection">
			<bean:message key="button.insertSubSection"/>
			</html:link>
			</div>
		</td>
		<td><div class="gen-button"><img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="" /> <html:link page="/insertItem.do?method=prepareInsert">
			<bean:message key="button.insertItem"/>
			</html:link>
			</div>
		</td>
</tr>
</table>
<br />   
<logic:iterate id="item" name="<%= SessionConstants.INFO_SECTION_ITEMS_LIST %>">
<h4><bean:write name="item" property="name"/></h4>
    <logic:equal name="item" property="urgent" value="true"><font color="red"></logic:equal> 		
  	<bean:write name="item" property="information" filter="false" />
  	<logic:equal name="item" property="urgent" value="true"></font></logic:equal>
<br />
<br />
<table>
	<tr>			
		<td><div class="gen-button"><html:link page="/deleteItem.do" indexed="true"><bean:message key="button.deleteItem"/>
			</html:link>
			</div>
		</td>
		<td><div class="gen-button"><html:link page="/editItem.do?method=prepareEdit" indexed="true"><bean:message key="button.editItem"/>
			</html:link>
			</div>
		</td>
	</tr>
</table>  
<br> 
</logic:iterate>