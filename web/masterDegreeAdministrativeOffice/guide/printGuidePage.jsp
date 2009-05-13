<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>


	<%-- The Original Guide --%>
	<jsp:include page="./guideTemplate1.jsp" flush="true" />

	<%-- The Original Guide --%>
	<jsp:include page="./guideTemplate1.jsp" flush="true" />


    <logic:present name="<%= PresentationConstants.PRINT_PASSWORD %>">
    	<%-- Candidate Information if necessary --%>
    	<jsp:include page="./informationTemplate1.jsp" flush="true" />
	</logic:present >	

    <logic:present name="<%= PresentationConstants.PRINT_INFORMATION %>">
    	<jsp:include page="./informationTemplate1.jsp" flush="true" />
	</logic:present >	
