<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<br />
<br />
<div align="right" style="margin-right: 100px;">Coordenador do N�cleo de P�s-Gradua��o e Forma��o Cont�nua,</div>
<br />
<div align="right" style="margin-right: 100px;">(Nuno Riscado)</div>
<br />
<p><bean:write name="<%= SessionConstants.DATE %>" /></p>