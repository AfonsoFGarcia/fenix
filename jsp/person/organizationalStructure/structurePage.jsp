<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>


<ft:tilesView definition="df.page.structure" attributeName="body-inline">

<script language="JavaScript">
function check(e,v)
{

	if (e.style.display == "none")
	  {
	  e.style.display = "";
	  v.src = "<%= request.getContextPath() %>/images/toggle_minus10.gif";
	  }
	else
	  {
	  e.style.display = "none";
	  v.src = "<%= request.getContextPath() %>/images/toggle_plus10.gif";
	  }
}
</script>

	<f:loadBundle basename="ServidorApresentacao/DepartmentAdmOfficeResources" var="bundle"/>
	<f:loadBundle basename="ServidorApresentacao/DegreeAdministrativeOfficeResources" var="bundleDegreeAdministrativeOffice"/>
	<h:form>
		<h:outputText value="<em>#{bundle['label.orgUnit']}</em>" escape="false"/>	
		<h:outputText value="<h2>#{organizationalStructure.instituitionName}</h2>" escape="false"/>
		<h:outputText value="#{organizationalStructure.units}<br/>" escape="false"/>
	</h:form>	
</ft:tilesView>
