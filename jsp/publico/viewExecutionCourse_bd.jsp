<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.Calendar" %>

<logic:notPresent name="siteView" property="component">
<table align="center"  cellpadding='0' cellspacing='0'>
			<tr align="center">
				<td>
					<span class="error"> <bean:message key="message.public.notfound.executionCourse"/> </span>
				</td>
			</tr>
		</table>
</logic:notPresent>


<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	
 	<logic:notEmpty name="component" property="initialStatement">
	<table align="center" cellspacing="0" width="90%">
        <tr>
          <td class="citation">
            <p><bean:write name="component" property="initialStatement" /></p>
          </td>
        </tr>
      </table>		
      <br/>
      <br/>
	</logic:notEmpty>
		
 <logic:notEmpty name="component" property="lastAnnouncement" >		
 	<bean:define id="announcement" name="component" property="lastAnnouncement"/>
        <table id="anuncios" align="center" cellspacing="0" width="90%">
          	<tr>
            	<td  class="ultAnuncioAviso"> 
            		<img alt="" border="0"  src="<%= request.getContextPath() %>/images/icon_warning.gif"  />
            		<bean:message key="message.lastAnnouncement"/> 
             	</td>      
           </tr>
           <tr>
           		<td class="ultAnuncio">
           			<img alt="" border="0"  src="<%= request.getContextPath() %>/images/icon_anuncio.gif"  />
           			<html:link  page="<%="/viewSite.do"+"?method=announcements&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
            		<bean:write name="announcement" property="title"/>:
            		</html:link>	
            		<br>
            		<center><bean:write name="announcement" property="information" filter="false"/></center>
           		</td>
           </tr>
           <tr>		
           		<td class="ultAnuncio-date"><bean:message key="message.createdOn"/>
           			<bean:write name="announcement" property="creationDateFormatted"/>
           			<br/>		
           			<bean:message key="message.modifiedOn"/>
           			<bean:write name="announcement" property="lastModifiedDateFormatted"/></td>
           </tr>           
        </table>
        </logic:notEmpty>
<br/>
<br/>
<br/>
<br/>
<br/>
 
    
     <logic:notEmpty 	name="component" property="alternativeSite" >	
     	<h2><bean:message key="message.siteAddress" /></h2>
	<bean:define id="alternativeSite" name="component" property="alternativeSite"/>
	<html:link href="<%=(String)pageContext.findAttribute("alternativeSite") %>" target="_blank">
			<bean:write name="alternativeSite" />
	</html:link>
			<br/>
			<br/>
	</logic:notEmpty>			

   
     <logic:notEmpty name="component" property="introduction">
     	
	<h2><bean:message key="message.introduction" /></h2>
      <p><bean:write name="component" property="introduction" filter="false" /></p>
      <br/>
      <br/>
      </logic:notEmpty>
	
	

	<%-- FIXME:as soon as we have responsible teachers remove this commment
	 <logic:empty name="component" property="responsibleTeachers">
		<table >
	<tr>
		<td>
			<h2><bean:message key="label.responsableProfessor"/></h2>	
		</td>
	</tr>
	<tr>
		<td>		
         <bean:message key="message.teachers.not.available" />	
		</td>
	</tr>
	</table>
	<br/>
	<html:link href="mailto:suporte@dot.ist.utl.pt">suporte@dot.ist.utl.pt</html:link>
	</logic:empty> --%>

	<logic:notEmpty name="component" property="responsibleTeachers">	
	<table>
	<tr>
		<th>
			<h2><bean:message key="label.responsableProfessor"/></h2>	
		</th>
	</tr>	

	<logic:iterate id="infoResponsableTeacher" name="component" property="responsibleTeachers">
		<tr>
			<td>
				<bean:write name="infoResponsableTeacher" property="infoPerson.nome" /> 
			</td>
		</tr>
	</logic:iterate>	
</table>
</BR>
</logic:notEmpty> 


<logic:notEmpty name="component" property="lecturingTeachers" >	
<table>
	<tr>
		<th>
			<h2><bean:message key="label.professorShips"/></h2>	
		</th>
	</tr>	

	<logic:iterate id="infoTeacher" name="component" property="lecturingTeachers">
		<tr>
			<td>
				<bean:write name="infoTeacher" property="infoPerson.nome" /> 
			</td>
		</tr>
	</logic:iterate>	
</table>
</logic:notEmpty>

</logic:present>