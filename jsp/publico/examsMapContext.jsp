<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:present name="exeDegree"  >
	<bean:define id="infoDegree" name="exeDegree" property="infoDegreeCurricularPlan.infoDegree" />   	
	<jsp:getProperty name="infoDegree" property="nome" />
	<br/>
	<bean:write name="ePName" />
</logic:present>