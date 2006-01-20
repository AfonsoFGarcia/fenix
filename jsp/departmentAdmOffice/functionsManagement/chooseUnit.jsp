<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<script language="JavaScript">
function check(e,v)
{	
	var contextPath = '<%= request.getContextPath() %>';	
	if (e.style.display == "none")
	  {
	  e.style.display = "";
	  v.src = contextPath + '/images/toggle_minus10.gif';
	  }
	else
	  {
	  e.style.display = "none";
	  v.src = contextPath + '/images/toggle_plus10.gif';
	  }
}
</script>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">

	<f:loadBundle basename="ServidorApresentacao/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>
	
		<h:inputHidden binding="#{functionsManagementBackingBean.personIDHidden}"/>
				
		<h:outputText value="<H2>#{bundle['label.chooseUnit']}</H2>" escape="false"/>		

		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGroup styleClass="infoop">
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{functionsManagementBackingBean.person.nome}" escape="false"/>		
		</h:panelGroup>
		<h:outputText value="<br/><br/><br/>" escape="false" />
						
		<h:outputText value="<p><strong>#{bundle['label.units']}</strong>:</p>" escape="false"/>		
					
		<h:outputText value="#{functionsManagementBackingBean.units}<br/>" escape="false"/>
		
		<h:commandButton styleClass="inputbutton" value="#{bundle['button.choose.new.person']}" action="backToList"/>
						    				
	</h:form>

</ft:tilesView>