<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.TipoCurso" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="degreesList">

	<logic:iterate id="infoDegree" name="degreesList"  length="1">		
			<bean:define id="degreeType" name="infoDegree" property="tipoCurso" />	
	</logic:iterate>

				
<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a>  > Ensino</div>
				<div class="version"><span class="px10"><a href="#">english version</a> <img src="<%= request.getContextPath() %>/images/icon_uk.gif" alt="Icon: English version!" width="16" height="12" /></span></div> 

				<h1><bean:message key="label.education" /></h1>
				<p class="greytxt">
					<logic:equal name="degreeType" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
						<bean:message key="text.masterDegree" />
					</logic:equal>
					<logic:equal name="degreeType" value="<%= TipoCurso.LICENCIATURA_OBJ.toString() %>">
						<bean:message key="text.nonMasterDegree" />
					</logic:equal>				
				</p>
			  
				<table width="100%"  border="0" cellspacing="0" summary="Esta tabela cont�m links para as licenciaturas, mestrados, doutoramentos e p�s-gradua��es existentes no IST">
				  <tr>
				    <td colspan="2" class="bottom_border"><h2><bean:write name="degreeType" />s<%-- plural --%></h2></td>
				  </tr>
				  <tr>
				    <td width="50%" valign="top">
							<ul> <%-- class="treemenu" --%>
	
								<logic:iterate id="infoDegree" name="degreesList" indexId="index">		
								<bean:define id="degreeID" name="infoDegree" property="idInternal" />
								
									<li>
										<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + pageContext.findAttribute("degreeID").toString()+ "&amp;index=" + pageContext.findAttribute("index").toString() %>" >
											<bean:write name="infoDegree" property="nome" />&nbsp;(<bean:write name="infoDegree" property="sigla"/>)
										</html:link>
									</li>
								</logic:iterate>
							</ul>			    
				    </td>
				  </tr>
				</table>

</logic:present>

<logic:notPresent name="degreesList">
	<p><h1><bean:message key="error.impossibleDegreeList" /></h1></p>
</logic:notPresent>


