<%@page contentType="text/html"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
  <table align="center">
    <tr>
      <td> 
        <span class="error"><html:errors/></span>
      </td>
    </tr>
    <tr>
      <td width="325">&nbsp;&nbsp;</td>
      <td width="275" align="left">
          <br>                         
          <bean:write name="numMecanografico" scope="session"/><p style="margin-top: -2px; margin-bottom: -2px">&nbsp;</p>
		  <bean:write name="pessoa" property="nome" scope="session" /><p style="margin-top: -2px; margin-bottom: -2px">&nbsp;</p>
          <bean:write name="centroCusto" property="sigla" scope="session" />
          <bean:write name="centroCusto" property="departamento" scope="session" /><br>
          <bean:write name="centroCusto" property="seccao1" scope="session" /><br>
          <bean:write name="centroCusto" property="seccao2" scope="session" /><br>           
      </td>
    </tr>
    <tr>
      <td colspan="2">
      <br />
      </td>
    </tr>
    <tr>
      <td colspan="2">
		<bean:define id="headers" name="MostrarListaForm" property="headers" />      	
		<bean:define id="body" name="MostrarListaForm" property="body" />	
		<tiles:insert definition="definition.report" flush="true">
			<tiles:put name="title" value="consultar.verbete" />
			<tiles:put name="headers" beanName="headers" />
			<tiles:put name="rows" beanName="body" />
		</tiles:insert>
      </td>
    </tr>    
    <tr>
      <td colspan="2" align="left">
      <br />
      </td>
    </tr>
    <tr>
      <td colspan='2'>
      	<bean:define id="headers" name="MostrarListaForm" property="headers2" />      	
		<bean:define id="body" name="MostrarListaForm" property="body2" />	
		<tiles:insert definition="definition.report" flush="true">
			<tiles:put name="title" value="resumo.valores" />
			<tiles:put name="headers" beanName="headers" />
			<tiles:put name="rows" beanName="body" />
		</tiles:insert>
      </td>
    </tr>
    <tr>
      <td colspan="2" align="left"><br></td>
    </tr>
    <tr>
      <td colspan="2" align="center">
      <br />
      </td>
    </tr>
    <tr>
      <td  colspan="2">
        <table border="0">
      	  <tr>
            <td >
              <html:form action="/consultarFuncionarioMostrar" focus="submit">
                <html:submit property="submit" styleClass="inputbutton">
                  <bean:message key="link.novaConsulta"/>
                </html:submit>
    	      </html:form>	
            </td>
            <td >
              <html:form action="/imprimirVerbetes" focus="submit">
                <html:submit property="submit" styleClass="inputbutton">
                  <bean:message key="link.imprimir"/>
                </html:submit>
    	      </html:form>		   			              
            </td>            
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <br />