<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="DataBeans.InfoLesson" %>
	   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoselected"><p>O curso seleccionado
              &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
        </table>
        <br/>
   	<% ArrayList iA = (ArrayList) session.getAttribute("infoAulasDeDisciplinaExecucao"); %>
        <center><font color='#034D7A' size='5'> <b> <bean:message key="title.adicionarAulas"/> </b> </font></center>
        <br/>
        <br/>
		 <center>  <bean:message key="message.add.lessons.to.shift"/>  </center>
            <br/>
        <span class="error"><html:errors/></span>
        <html:form action="/adicionarAulasForm">
            <center> <b> <bean:message key="listAulas.available"/> </b> </center>
            <br/>
			
            <!-- Cria a tabela das aulas -->
            <logic:present name="infoAulasDeDisciplinaExecucao" scope="session">
                <table align="center"  cellpadding='5'>
                    <tr align='center'>
                        <td class="listClasses-header">
							&nbsp;
                        </td>
                        <td class="listClasses-header">
                            <bean:message key="property.aula.weekDay"/>
                        </td>
                        <td class="listClasses-header">
                            <bean:message key="property.aula.time.begining"/>
                        </td>
                        <td class="listClasses-header">
                            <bean:message key="property.aula.time.end"/>
                        </td>
                        <td class="listClasses-header">
                            <bean:message key="property.aula.type"/>
                        </td>
                        <td class="listClasses-header">
                            <bean:message key="property.aula.sala"/>
                        </td class="listClasses-header">
                    </tr>

		            <% int i = 0; %>
					<bean:define id="lessonsList" name="infoAulasDeDisciplinaExecucao" />
					
                    <logic:iterate id="elem" 
                    	name="infoAulasDeDisciplinaExecucao" type="DataBeans.InfoLesson">
			<bean:define id="idInternal" name="elem" property="idInternal"/>
                       <% Integer iH = new Integer(((InfoLesson) iA.get(i)).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer iM = new Integer(((InfoLesson) iA.get(i)).getInicio().get(Calendar.MINUTE)); %>
                       <% Integer fH = new Integer(((InfoLesson) iA.get(i)).getFim().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer fM = new Integer(((InfoLesson) iA.get(i)).getFim().get(Calendar.MINUTE)); %>
                       <tr align="center">
					<td class="listClasses">
						<html:multibox  property="lessonsList" value="<%=  idInternal.toString() %>" />
					</td>
                            <td class="listClasses">
                                <bean:write name="elem" property="diaSemana" />
                            </td>
                            <td class="listClasses">
                            	<%= iH.toString()%> : <%= iM.toString()%>
                            </td>
                            <td class="listClasses">
                            	<%= fH.toString()%> : <%= fM.toString()%>
                            </td>
                            <td class="listClasses">
                            	<%= ((InfoLesson) iA.get(i)).getTipo().toString()%>
                            </td>
                            <td class="listClasses">
                                <bean:write name="elem" property="infoSala.nome"/>
                            </td>
                        </tr>
                        <% i++; %>
                    </logic:iterate>           

                </table>

				<br/>

	            <br/>

	            <table align="center">

                <tr align="center">

                    <td>

                        <html:submit property="operation">

                            <bean:message key="label.aula.add"/>

                        </html:submit>

                    </td>

                </tr>

            </table>
            </logic:present>
            <logic:notPresent name="infoAulasDeDisciplinaExecucao" scope="session">
                <table align="center" border='1' cellpadding='5'>
                    <tr align="center">
                        <td>
                            <font color='red'> <bean:message key="errors.existAulasDeDisciplina"/> </font>
                        </td>
                    </tr>
                </table>
            </logic:notPresent>
        </html:form>
