<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<bean:define id="executionCourseIdInternal" name="<%= PresentationConstants.INFO_SITE %>" property="infoExecutionCourse.idInternal" />
<ul>
  <li><html:link page='<%= "/executionCourseShiftsPercentageManager.do?method=show&amp;executionCourseInternalCode=" + executionCourseIdInternal.toString() %>'>Administra��o de cr�ditos</html:link></li>
</ul>
