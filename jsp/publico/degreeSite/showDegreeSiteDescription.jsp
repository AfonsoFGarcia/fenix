<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<%@ page import="java.util.Locale" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.mapping.MappingUtils" %>

<bean:define id="institutionUrl" type="java.lang.String">
	<bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
</bean:define>
<div class="breadcumbs">
	<a href="<%= institutionUrl %>">
		<bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/>
	</a>
	<bean:define id="institutionUrlTeaching" type="java.lang.String">
		<bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
		<bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/>
	</bean:define>
	&nbsp;&gt;&nbsp;
	<a href="<%=institutionUrlTeaching%>">
		<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/>
	</a>
	<logic:present name="degree">
		&nbsp;&gt;&nbsp;
		<bean:write name="degree" property="sigla"/>
	</logic:present>
</div>

<!-- COURSE NAME -->
<h1>
	<logic:equal name="degree" property="bolonhaDegree" value="true">
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="bolonhaDegreeType.name"/>
	</logic:equal>
	<logic:equal name="degree" property="bolonhaDegree" value="false">
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="tipoCurso.name"/>
	</logic:equal>
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in"/>
	<logic:present name="inEnglish">
		<logic:equal name="inEnglish" value="false">
			<bean:write name="degree" property="nome"/>
		</logic:equal>
		<logic:equal name="inEnglish" value="true">
			<bean:write name="degree" property="nameEn"/>
		</logic:equal>
	</logic:present>
</h1>

<em><span class="error0"><html:errors/></span></em>

<logic:present name="infoExecutionDegrees">

	<!-- CAMPUS -->
  	<h2 class="greytxt">
  		<bean:define id="campus" value=""/>
  		<bean:size id="campusSize" name="infoExecutionDegrees"/>
  		<logic:iterate id="executionDegree" name="infoExecutionDegrees" indexId="indexCampus" >
			<bean:define id="campusName" name="executionDegree" property="infoCampus.name"/>
  			<logic:notMatch name="campus" value="<%= campusName.toString()%>">
				<bean:write name="campusName"/>
		  		<bean:define id="campus" value="<%= campus.toString().concat(campusName.toString()) %>"/>	
  			</logic:notMatch>
		</logic:iterate>
	</h2>

	<!-- COORDINATORS -->
	<p><strong><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.coordinators"/></strong></p>
	<bean:define id="coordinators" value=""/>
	<bean:size id="executionDegreesSize" name="infoExecutionDegrees"/>
	<logic:iterate id="infoExecutionDegree" name="infoExecutionDegrees" indexId="executionDegreesSize" >
		<logic:iterate id="infoCoordinator" name="infoExecutionDegree" property="coordinatorsList">
 				<logic:equal name="infoCoordinator" property="responsible" value="true">
				<bean:define id="coordinatorName" name="infoCoordinator" property="infoTeacher.infoPerson.nome"/>
				<logic:notMatch name="coordinators" value="<%= coordinatorName.toString()%>">
					<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.title.coordinator"/>&nbsp; 
							
					<logic:notEmpty name="infoCoordinator" property="infoTeacher.infoPerson.enderecoWeb">
						<bean:define id="homepage" name="infoCoordinator" property="infoTeacher.infoPerson.enderecoWeb"/>
						<a href=" <%= homepage %>"><bean:write name="infoCoordinator" property="infoTeacher.infoPerson.nome"/></a>
					</logic:notEmpty>		
							
					<logic:empty name="infoCoordinator" property="infoTeacher.infoPerson.enderecoWeb">
						<logic:notEmpty name="infoCoordinator" property="infoTeacher.infoPerson.email">
							<bean:define id="email" name="infoCoordinator" property="infoTeacher.infoPerson.email"/>	
								<a href="mailto: <%= email %>"><bean:write name="infoCoordinator" property="infoTeacher.infoPerson.nome"/></a>											
						</logic:notEmpty>						
					</logic:empty>		
							
					<logic:empty name="infoCoordinator" property="infoTeacher.infoPerson.enderecoWeb">
						<logic:empty name="infoCoordinator" property="infoTeacher.infoPerson.email">
							<bean:write name="infoCoordinator" property="infoTeacher.infoPerson.nome"/>											
						</logic:empty>						
					</logic:empty>	
							
					<logic:lessThan name="executionDegreesSize" value="executionDegreesSize" >
						<br/>
					</logic:lessThan>
				
					<bean:define id="coordinators" value="<%= coordinators.toString().concat(coordinatorName.toString()) %>"/>
				</logic:notMatch>
			</logic:equal>
		 </logic:iterate>
	</logic:iterate>

</logic:present>			


<%--
	<div class="degree_imageplacer">
		IMAGEM REFERENTE �? LICENCIATURA  width="250" height="150"
	</div>
--%>

<logic:present name="infoDegreeInfo">
	<logic:notEmpty name="infoDegreeInfo" property="description" >			 	
		<!-- OVERVIEW -->
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.overview"/></h2>
		<p><bean:write name="infoDegreeInfo" property="description" filter="false"/></p>
	</logic:notEmpty>
	
	<logic:notEmpty name="infoDegreeInfo" property="objectives" >
		<!-- OBJECTIVES -->
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.objectives"/></h2>
	 	<p><bean:write name="infoDegreeInfo" property="objectives" filter="false"/></p>
	</logic:notEmpty>
				  
	<div class="col_right">
		<logic:notEmpty name="infoDegreeInfo" property="additionalInfo" >	
			<!-- ADDITIONAL INFO -->	
			<table class="box" cellspacing="0">
				<tr>
					<td class="box_header"><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.additionalInfo"/></strong></td>
				</tr>						
				<tr>
					<td class="box_cell"><p><bean:write name="infoDegreeInfo" property="additionalInfo" filter="false"/></p></td>						
				</tr>
			<logic:empty name="infoDegreeInfo" property="links" >
				</table>
			</logic:empty>
		</logic:notEmpty>
		
		<logic:notEmpty name="infoDegreeInfo" property="links" >
			<!-- LINKS -->	
			<logic:empty name="infoDegreeInfo" property="additionalInfo" >	
				<table class="box" cellspacing="0">	
			</logic:empty>
				<tr>
					<td class="box_header"><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.links"/></strong></td>
				</tr>
				<tr>
					<td class="box_cell"><p><bean:write name="infoDegreeInfo" property="links" filter="false"/></p></td>	
				</tr>
			</table>
		</logic:notEmpty>
	</div>
	
	<logic:notEmpty name="infoDegreeInfo" property="professionalExits" >
		<!-- PROFESSIONAL EXITS -->
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.professionalExits"/></h2>
		<p><bean:write name="infoDegreeInfo" property="professionalExits" filter="false"/></p>  
	</logic:notEmpty>

	<logic:notEmpty name="infoDegreeInfo" property="history" >
		<!-- HISTORY -->
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.history"/></h2>
		<p><bean:write name="infoDegreeInfo" property="history" filter="false"/></p>
	</logic:notEmpty>

	<logic:empty name="infoDegreeInfo" property="description">
	<logic:empty name="infoDegreeInfo" property="objectives">
	<logic:empty name="infoDegreeInfo" property="additionalInfo">
	<logic:empty name="infoDegreeInfo" property="links">
	<logic:empty name="infoDegreeInfo" property="professionalExits">
	<logic:empty name="infoDegreeInfo" property="history">
		<p><i><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="not.available" /></i></p>
	</logic:empty>
	</logic:empty>
	</logic:empty>	
	</logic:empty>	
	</logic:empty>	
	</logic:empty>

	<div class="clear"></div>
</logic:present>

<p style="margin-top: 2em;">
	<span class="px10">
		<i>
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.responsability.information.degree" />
		</i>
	</span>
</p>
