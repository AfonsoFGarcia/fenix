<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:html>
    <head>
        <title> Adicionar Aulas </title>
    </head>
    <body>
        <jsp:include page="context.jsp"/><br/>
        <br/>
        <center><font color='#034D7A' size='5'> <b> Adicionar Aulas </b> </font></center>
        <br/>
        <br/>
        <html:errors/>
        <html:form action="/adicionarAulasTmp">
            <center> <b> Aulas dispon�veis </b> </center>
            <br/>
            <!-- Cria a tabela das aulas -->
            <logic:present name="listaAulasDeDisciplinaETipoBean" scope="session">
                <table align="center" border=1 cellpadding='5'>
                    <%! int i; %>
                    <% i = 0; %>
                    <tr align='center'>
                        <th>
                        </th>
                        <th>
                            Dia da Semana
                        </th>
                        <th>
                            In�cio
                        </th>
                        <th>
                            Fim
                        </th>
                        <th>
                            Tipo
                        </th>
                        <th>
                            Sala
                        </th>
                    </tr>
                    <logic:iterate id="elem" name="listaAulasDeDisciplinaETipoBean" scope="session">
                       <tr align="center">
                            <td>
                                <html:radio name="posicaoAulaFormBean" property="posicao" value="<%= (new Integer(i)).toString() %>"/>
                            </td>
                            <td>
                                <bean:write name="elem" property="diaSemana" />
                            </td>
                            <td>
                                <bean:write name="elem" property="horaInicio"/> : <bean:write name="elem" property="minutosInicio"/>
                            </td>
                            <td>
                                <bean:write name="elem" property="horaFim"/> : <bean:write name="elem" property="minutosFim"/>
                            </td>
                            <td>
                                <bean:write name="elem" property="tipo"/>
                            </td>
                            <td>
                                <bean:write name="elem" property="nomeSala"/>
                            </td>
                        </tr>
                        <% i++; %>
                    </logic:iterate>
                </table>
            </logic:present>
            <logic:notPresent name="listaAulasDeDisciplinaETipoBean" scope="session">
                <table align="center" border='1' cellpadding='5'>
                    <tr align="center">
                        <td>
                            <font color='red'> N�o existem aulas da disciplina e tipo escolhidos </font>
                        </td>
                    </tr>
                </table>
            </logic:notPresent>
            <br/>
            <br/>
            <table align="center">
                <tr align="center">
                    <td>
                        <html:submit property="operation">
                            Adicionar Aula
                        </html:submit>
                    </td>
                </tr>
            </table>
            <br/>
        </html:form>
    </body>
</html:html>
