<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message key="link.createSection"/>
</h2>

<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=section&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
<fr:edit name="section" type="net.sourceforge.fenixedu.domain.Section" schema="net.sourceforge.fenixedu.domain.SectionEditor"
		action="<%= url %>">
</fr:edit>