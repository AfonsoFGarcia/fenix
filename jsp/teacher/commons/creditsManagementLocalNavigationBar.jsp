<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<bean:define id="executionCourseIdInternal" name="<%= SessionConstants.INFO_SITE %>" property="infoExecutionCourse.idInternal" />
<ul>
  <li><html:link page='<%= "/executionCourseShiftsPercentageManager.do?method=show&amp;executionCourseInternalCode=" + executionCourseIdInternal.toString() %>'>Administra��o de cr�ditos</html:link></li>
</ul>
