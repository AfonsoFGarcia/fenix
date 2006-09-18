<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html xhtml="true">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
		<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
		<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />
		<title><bean:message key="title.student.schedule"/></title>
	</head>
	<body>
		<logic:present name="UserView">
			
			<table>
				<tr>
					<td align="right">
						<strong>Nome:</strong>
					</td>
					<td>
						<bean:write name="UserView" property="person.name" />
					</td>
				</tr>
				<tr>
					<td align="right">
						<strong>N�mero:</strong>
					</td>
					<td>
						<bean:write name="UserView" property="person.student.number" />
					</td>
				</tr>
				<tr>
					<td align="right" >
						<strong>IST Username:</strong>
					</td>
					<td>
						<bean:write name="UserView" property="person.istUsername" />
					</td>
				</tr>
			</table>
			<br/>
			<br/>
			<br/>
		</logic:present>
		
		<bean:define id="infoLessons" name="infoLessons"/>
		<div align="center">
			<app:gerarHorario name="infoLessons" type="<%= TimeTableType.CLASS_TIMETABLE %>" application="<%= request.getContextPath() %>"/>
		</div> 
	</body>
</html:html>