<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{scouncilBundle['scientificCouncil']}</i>" escape="false"/>
	<h:outputText value="<h2>#{scouncilBundle['curricularPlansManagement']}</h2>" escape="false"/>

	<h:outputLink value="createDegree.faces">
		<h:outputFormat value="#{scouncilBundle['create.param']}">
			<f:param value="#{scouncilBundle['degree']}"/>
		</h:outputFormat>
	</h:outputLink>
	<h:outputText value="<br/><br/>" escape="false" />

	<h:messages errorClass="error0" infoClass="success0"/>
	
	<fc:dataRepeater value="#{ScientificCouncilDegreeManagement.bolonhaDegrees}" var="degree">
		<h:outputText value="<table style='width: 750px' class='showinfo1'>" escape="false"/>
		<h:outputText value="<tr class='bgcolor1'><th style='width: 70px'>#{scouncilBundle['degree']}:</th>" escape="false"/>

		<h:outputText value="<td><strong>#{enumerationBundle[degree.bolonhaDegreeType.name]} #{degree.nome} (#{degree.acronym})</strong></td>" escape="false"/>
		<h:outputText value="<td style='width: 180px'>" escape="false"/>
		<h:outputLink value="editDegree.faces">
			<h:outputFormat value="#{scouncilBundle['edit']}"/>
			<f:param name="degreeId" value="#{degree.idInternal}"/>
		</h:outputLink>
		<h:outputText value=" , " escape="false"/>
		<h:outputLink value="deleteDegree.faces">
			<h:outputFormat value="#{scouncilBundle['delete']}"/>
			<f:param name="degreeId" value="#{degree.idInternal}"/>
		</h:outputLink>
		<h:outputText value=" , " escape="false"/>		
		<h:outputLink value="createCurricularPlan.faces">
			<h:outputFormat value="#{scouncilBundle['create.param']}">
				<f:param value="#{scouncilBundle['plan']}"/>
			</h:outputFormat>
			<f:param name="degreeId" value="#{degree.idInternal}"/>
		</h:outputLink>
		<h:outputText value="</td></tr>" escape="false"/>
		
		<h:outputText value="<tr><td colspan='3' align='center'><i>#{scouncilBundle['no.curricularPlan']}.</i></td></tr>" escape="false" rendered="#{empty degree.degreeCurricularPlans}"/>

<%--#{scouncilBundle['curricularPlans']}--%>

		<fc:dataRepeater value="#{degree.degreeCurricularPlans}" var="degreeCurricularPlan" rendered="#{!empty degree.degreeCurricularPlans}" rowIndexVar="index">
			<h:outputText value="<tr>" escape="false"/>
			<h:outputText rendered="#{index == 0}" value="<th>#{scouncilBundle['plans']}:</th>" escape="false"/>
			<h:outputText rendered="#{index > 0}" value="<th></th>" escape="false"/>

			<h:outputText value="<td><em style='color: #777; background-color: #ffa;'>#{enumerationBundle[degreeCurricularPlan.curricularStage.name]}</em> #{degreeCurricularPlan.name}</td>" escape="false" />

			<h:outputText value="<td>" escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces">
				<h:outputText value="#{scouncilBundle['view']}" />
				<f:param name="dcpId" value="#{degreeCurricularPlan.idInternal}"/>
				<f:param name="action" value="view"/>
			</h:outputLink>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="editCurricularPlan.faces">
				<h:outputText value="#{scouncilBundle['edit']}" />
				<f:param name="dcpId" value="#{degreeCurricularPlan.idInternal}"/>
			</h:outputLink>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="deleteCurricularPlan.faces">
				<h:outputFormat value="#{scouncilBundle['delete']}"/>
				<f:param name="dcpId" value="#{degreeCurricularPlan.idInternal}"/>
			</h:outputLink>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="editCurricularPlanMembersGroup.faces">
				<h:outputText value="#{scouncilBundle['group']}" />
				<f:param name="dcpId" value="#{degreeCurricularPlan.idInternal}"/>
			</h:outputLink>
			<h:outputText value="</td></tr>" escape="false"/>
		</fc:dataRepeater>

		<h:outputText value="</table>" escape="false"/>
	</fc:dataRepeater>

</ft:tilesView>
