<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree"%>


<logic:present name="<%= PresentationConstants.MASTER_DEGREE %>"  >

	<bean:define id="infoExecutionDegree" name="<%= PresentationConstants.MASTER_DEGREE %>" scope="session"/>
	<em>
		<bean:message bundle="ENUMERATION_RESOURCES" key="<%=((InfoExecutionDegree)infoExecutionDegree).getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso().toString()%>" />
		<bean:message bundle="GLOBAL_RESOURCES" key="in"/>
		<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" />
		<%--&nbsp;&nbsp;>&nbsp;&nbsp;
		<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" />--%>
	</em>
</logic:present>
