<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<% request.setAttribute("original", true); %>
	<%-- The Original CreditNote --%>
	<jsp:include page="./printCreditNoteTemplate.jsp" flush="true" />

	<% request.setAttribute("original", false); %>
	<%-- Copy of CreditNote --%>
	<jsp:include page="./printCreditNoteTemplate.jsp" flush="true" />

</logic:present>
	