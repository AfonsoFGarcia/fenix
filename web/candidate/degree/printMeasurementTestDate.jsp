<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@page import="org.joda.time.LocalDate"%><html xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT" xml:lang="pt-PT">

<html:xhtml/>

<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />



</head>



<body>


<p>
Caro/a  aluno/a <bean:write name="registration" property="student.name"/>, <bean:write name="registration" property="student.number"/>, 
</p>

<p>
O IST em associa��o com a Sociedade Portuguesa de Matem�tica promove 
este ano lectivo uma prova de aferi��o para todos os alunos ingressados no
1� ano. Esta prova � obrigat�ria e poder� ter influ�ncia na classifica��o da 
cadeira de C�lculo Diferencial e Integral I ou Matem�tica I.
</p>

<p>
A prova realizar-se-� no dia <bean:write name="registration" property="measurementTestRoom.shift.date"/> 23 de Setembro,  pelo que, neste dia,  ser�o suspensas as aulas do 1� ano no per�odo da tarde. 
</p>

<p>
A realiza��o da sua prova de aferi��o ter� lugar na sala <bean:write name="registration" property="measurementTestRoom.name"/>,  onde dever� comparecer  �s <bean:write name="registration" property="measurementTestRoom.shift.date"/> horas munido de BI ou cart�o de cidad�o.
</p>

<p>
P'lo Conselho de Gest�o
Palmira Ferreira da Silva
</p>

</body>
</html>
