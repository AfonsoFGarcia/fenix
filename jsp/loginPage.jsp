<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>.IST - Login</title>
    <link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  </head>
<body>
<div id="container">
	<div id="dotist_id">
		<img alt="Logo dotist" src="<%= request.getContextPath() %>/images/dotist-id.gif"/>
	</div>
	<div id="txt">
	<h1>Login</h1>
	<p>
		O servi&ccedil;o <strong>.IST</strong> permite aos utilizadores certificados - alunos, docentes,
		funcion&aacute;rios - do <a href="http://www.ist.utl.pt">Instituto Superior T&eacute;cnico</a>
		utilizar diversos servi&ccedil;os e funcionalidades de forma a simplificar o quotidiano de todos
		aqueles que estudam ou trabalham.
	</p>
<%--
	<p>
		Caso o utilizador n�o consiga aceder ao sistema por qualquer motivo, incluindo a perda de
		password, ou necessite de qualquer esclarecimento adicional deve contactar a equipa de
		suporte t�cnico atrav�s do endere�o: <a href="mailto:suporte@dot.ist.utl.pt">suporte@dot.ist.utl.pt</a>.
	</p>       

	<p>
		O username dos alunos � constitu�do pelo seu n�mero mecanogr�fico precedido da letra "L" (por
		exemplo, L90123). Os pedidos de novas passwords devem ser efectuados na Recep��o do CIIST.
	</p>
--%>
	<p><font color='#FF0000'><strong>
		Aviso:<br /><br />
		Por raz�es de seguran�a, foram alteradas as passwords de acesso de todos os utilizadores 
		que tinham como password o n�mero de BI.<br /><br />
		Os pedidos de novas passwords devem ser efectuados na Recep��o do CIIST.
	</strong></font></p>

	<div id="alert">	
		<p><span class="error"><html:errors property="invalidAuthentication"/></span></p>
		<p><span class="error"><html:errors property="errors.noAuthorization"/></span></p>
		<p><span class="error"><html:errors property="error.invalid.session"/></span></p>
	</div>
	</div>
		<html:form action="/login" focus="username" >
		<!-- input utilizador -->
		<table align="center" border="0">
			<tr>
				<td colspan="2"><span class="error"><html:errors property="username"/></span></td>
			</tr>
			<tr>
		    	<td>Username:</td>
		        <td><html:text property="username"/></td>
		  	</tr>
		<!-- input password -->
			<tr>
				<td colspan="2"><span class="error"><html:errors property="password" /></span></td>
			</tr>
			<tr>
		    	<td>Password:</td>
		        <td><html:password property="password" redisplay="false"/></td>
		 	</tr>
	    </table>
		<br />
		<div class="wrapper">
			<html:submit value="Submeter" styleClass="button" property="ok"/>
			<html:reset value="Limpar" styleClass="button"/>
		</html:form>
		</div>
</div>
     <%-- Invalidate session. 
     	  This is to work with FenixActionServlet --%>
     <% 	try{
     			session.invalidate();
     		}catch (Exception e){}
     %>
  </body>
</html>
