<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:notPresent name="siteView">
	<tiles:insert definition="definition.public.mainPageIST" flush="true">
		<tiles:put name="body" value="/publico/degreeSite/errorExecutionCourseSite.jsp"/>
		<tiles:put name="title" value="public.degree.information.errors.invalidSiteExecutionCourse"/>
		<tiles:put name="bundle" value="PUBLIC_DEGREE_INFORMATION"/>
	</tiles:insert>
</logic:notPresent>
<logic:present name="siteView">
	<bean:define id="commonComponent" name="siteView" property="commonComponent"/>
	<tiles:insert definition="definition.publico.executionCourseSitePage" flush="true">
		<tiles:put name="title" beanName="commonComponent" beanProperty="title" />
		<tiles:put name="executionCourseName" value="/publico/executionCourseName.jsp"/>
	</tiles:insert>
</logic:present>
