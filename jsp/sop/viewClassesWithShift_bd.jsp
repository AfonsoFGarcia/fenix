<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List"%>
   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoselected"><p>O curso seleccionado
              &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
    </table>
    <br />
    <center><font color='#034D7A' size='5'> <b> <bean:message key="title.shift.classes"/> </b> </font></center>
    <br/>
    <br/>

        <table align="center" border=1 cellpadding='5'>
            <tr>
            	<th>
            		<bean:message key="property.class.name"/>
            	</th>
            	<th>
            		<bean:message key="label.class"/>
            	</th>
            </tr>
            <logic:iterate id="infoClass" name="classesWithShift">
            	<tr align="center">
                    <td class="listClasses">
                        <bean:write name="infoClass" property="nome"/>
                    </td>
                    <td class="listClasses">
						<html:link page="/ClassManagerDA.do?method=viewClass" paramId="className" paramName="infoClass" paramProperty="nome">
		            		<bean:message key="link.view"/>							
						</html:link>
                    </td>
                </tr>
            </logic:iterate>
		</table>
