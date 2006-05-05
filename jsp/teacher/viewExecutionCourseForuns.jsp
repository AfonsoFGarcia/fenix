<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<span class="error"><html:errors/></span>

<h2><bean:message key="label.teacher.executionCourseManagement.viewForuns.title" /></h2>
<br/>

<logic:notEmpty name="foruns">
	<fr:view name="foruns" layout="tabular-list">
		<fr:layout>
			<fr:property name="subLayout" value="values"/>
			<fr:property name="subSchema" value="executionCourseForum.view.nameOnly"/>
		
			<fr:property name="link(view)" value="/forunsManagement.do?method=viewForum"/>
			<fr:property name="module(view)" value="/messaging"/>
			<fr:property name="key(view)" value="link.teacher.executionCourseManagement.foruns.viewForum"/>
			<fr:property name="param(view)" value="idInternal/forumId"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="foruns">
	<span class="error"><bean:message key="label.teacher.executionCourseManagement.viewForuns.noForuns"/></span>
</logic:empty>
  
