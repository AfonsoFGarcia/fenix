<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>
<h1><bean:message key="label.section.name" /></h1> 	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td class="infoop">
    			<html:link page="/chooseContextDA.do?method=prepare&amp;nextPage=classSearch&amp;inputPage=chooseContext" ><strong><bean:message key="link.classes.consult"/></strong></html:link>
    		</td>
  		</tr>
	</table>
<br />
<p>
Nesta �rea poder� efectuar uma pesquisa por curso (actualmente s� encontrar� licenciaturas) e ano lectivo.
O semestre � o actual.
</p>
<br />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop"><html:link page="/chooseContextDA.do?method=prepare&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext"><strong><bean:message key="link.executionCourse.consult"/></strong></html:link></td>
  		</tr>
	</table>
<br />
<p>
Nesta �rea encontrar� a informa��o relativamente � disciplina que pretende. Informa��o essa que �: 
turnos, hor�rio e carga curricular
</p>
<br />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop"><html:link page="/prepareConsultRooms.do"><strong><bean:message key="link.rooms.consult"/></strong></html:link></td>
  		</tr>
	</table>
<br />
<p>
Nesta �rea encontrar� a informa��o relativamente a uma sala, onde poder� consultar o hor�rio lectivo dessa mesma sala.
</p>  