<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoExecutionPeriod" %>

<jsp:include page="contextExecutionCourseAndExecutionDegreeAndCurricularYear.jsp"/>

<logic:present name="<%= SessionConstants.CLASS_VIEW %>" scope="request">
	<bean:define id="school_class"
				 name="<%= SessionConstants.CLASS_VIEW %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="classOID"
				 type="java.lang.Integer"
				 name="school_class"
				 property="idInternal"
				 toScope="request"
				 scope="request"/>

	<br />
	######### classOID= 
	<bean:write name="classOID"/>
</logic:present>