<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

  <html:select property="courseInitials" size="1">
  	 <option value=""><bean:message key="label.choose.executionCourse"/></option>
     <html:options	property="sigla" 
     				labelProperty="nome" 
					collection="<%= SessionConstants.EXECUTION_COURSE_LIST_KEY %>" />
  </html:select>
