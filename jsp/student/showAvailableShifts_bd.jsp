<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="DataBeans.InfoLesson" %>
<br/>
<bean:define id="link"><%= request.getContextPath() %>/dotIstPortal.do?prefix=/student&amp;page=/index.do</bean:define>
<html:link href='<%= link %>'><b>Sair do processo de inscri��o</b></html:link>
<br/>
<br/>

<br/>
<table >
	<tr>
		<td style="text-align: center;"><h2 class="redtxt">Informa��es de utiliza��o:</h2>
		</td>
	</tr>
	<tr>
		<td style="text-align: left;">Estes s�o os agrupamentos de aulas correspondentes � turma que escolheu.
			 Seleccione os que deseja frequentar. Note que:
			<ul>
				<li>As inscri��es em laborat�rios s�o da responsabilidade dos docentes da disciplina.</li>
				<li>Carregue no bot�o inscrever para proceder � submiss�o das suas altera��es.</li> 
				<li>Poder� seguir o link "Visualizar Turmas e Hor�rio" para voltar � p�gina de escolha de turma e visualiza��o
			 		do hor�rio e estado da inscri��o.</li> 
				<li>Lembre-se que a qualquer momento, durante o per�odo de inscri��o, pode efectuar altera��es.</li> 
				<li>Em caso de d�vida ou se necessitar de ajuda, contacte-nos utilizando: <a href="mailto:suporte@dot.ist.utl.pt">suporte@dot.ist.utl.pt</a></li> 
			</ul>
		</td>
	</tr>
</table>
<bean:define id="infoStudentShiftEnrolment" name="<%= SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY %>" />
<div align="center"><h3><bean:write name="infoStudentShiftEnrolment" property="infoStudent.infoPerson.nome"/></h3></div>
<div align="center"><h3>
<html:link page="/studentShiftEnrolmentManager.do?method=initializeShiftEnrolment">
	(Visualizar Turmas e Hor�rio)
</html:link>
</h3></div>
<logic:present name="infoStudentShiftEnrolment">
				
<div align="center"><table width="50%">
	<tr>
		<td class="listClasses-header">
			Turnos onde est� inscrito:
		</td>
		<td class="listClasses-header">
			Tipo:
		</td>
		<td class="listClasses-header">
			Aulas:
		</td>
	</tr>
		
	<logic:iterate name="infoStudentShiftEnrolment"  id="enroledShift" property="currentEnrolment"  type="DataBeans.InfoShift">
	<tr>		
		<td class="listClasses">	
						<bean:write name="enroledShift" property="infoDisciplinaExecucao.nome"/>
		</td>
		<td class="listClasses">
						<bean:write name="enroledShift" property="tipo.fullNameTipoAula"/>
		</td>
		<td class="listClasses">	
			<logic:iterate id="lesson" name="enroledShift" property="infoLessons">
								<bean:write name="lesson" property="diaSemana"/>
								das
								<dt:format pattern="HH:mm">
									<bean:write name="lesson" property="inicio.time.time"/>
								</dt:format>
								at� as 
								<dt:format pattern="HH:mm">
									<bean:write name="lesson" property="fim.time.time"/>
								</dt:format>
								na sala
								<bean:write name="lesson" property="infoSala.nome"/> 
								<br/>
			</logic:iterate> 
		</td>
	</tr>		
	</logic:iterate>
</table></div>	


				
				<br /> 
<logic:notEmpty name="infoStudentShiftEnrolment" property="dividedList">
<div align="center"><h3>Turnos onde se pode inscrever:</h3></div>
<div align="center"><table width="50%">
				<html:form action="studentShiftEnrolmentManager">
					<html:hidden property="method" value="validateAndConfirmShiftEnrolment"/>
					<bean:define id="index" value="0"/>

				<%--<bean:size id="shiftNumber" name="infoStudentShiftEnrolment" property="dividedList"	/>	
					<logic:notEqual name="shiftNumber" value="<%=  shiftNumber%>"	>	--%>						

					<logic:iterate name="infoStudentShiftEnrolment"  id="list" property="dividedList" indexId="courseIndex">

<tr>
	<td class="listClasses-header">
					<bean:write name="list" property="type"/>
	</td>
	<td class="listClasses-header">
				Aulas:
	</td>	
	<td class="listClasses-header">
		&nbsp;
	</td>
</tr>
						<logic:iterate name="list" id="sublist" property="list" indexId="groupIndex">
		<bean:size id="rowspan" name="sublist" property="list" />
		<tr  >

		<td class="listClasses" rowspan="<bean:write name='rowspan'/>">
			<bean:write name="sublist" property="type"/>
		</td>
							<logic:iterate id="shiftWithLessons" name="sublist"  property="list"  offset="0" length="1">
											<bean:define id="shift" name="shiftWithLessons" property="infoShift" />
						
									<td class="listClasses">				
														<logic:iterate id="lesson" name="shiftWithLessons" property="infoLessons">
														
														<bean:write name="lesson" property="diaSemana"/>
															das
															<dt:format pattern="HH:mm">
																<bean:write name="lesson" property="inicio.time.time"/>
															</dt:format>
															at� as 
															<dt:format pattern="HH:mm">
																<bean:write name="lesson" property="fim.time.time"/>
															</dt:format>
															na sala
															<bean:write name="lesson" property="infoSala.nome"/>
															<br />
														</logic:iterate>
										</td>
									<td class="listClasses">
										<html:radio property='<%= "shifts[" + index + "]" %>' idName="shift" value="idInternal" />			
									</td>					
							</logic:iterate>



		</tr>		
							<logic:iterate id="shiftWithLessons" name="sublist"  property="list" offset="1">
								<tr>
											<bean:define id="shift" name="shiftWithLessons" property="infoShift" />
						
									<td class="listClasses">				
														<logic:iterate id="lesson" name="shiftWithLessons" property="infoLessons">
														
														<bean:write name="lesson" property="diaSemana"/>
															das
															<dt:format pattern="HH:mm">
																<bean:write name="lesson" property="inicio.time.time"/>
															</dt:format>
															at� as 
															<dt:format pattern="HH:mm">
																<bean:write name="lesson" property="fim.time.time"/>
															</dt:format>
															na sala
															<bean:write name="lesson" property="infoSala.nome"/>
															<br />
														</logic:iterate>
										</td>
									<td class="listClasses">
										<html:radio property='<%= "shifts[" + index + "]" %>' idName="shift" value="idInternal" />			
									</td>					
								</tr>
							</logic:iterate>
					<bean:define id="index" value="<%=  (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
				
						</logic:iterate>
					</logic:iterate>
					

</table></div>
<br/>
<br/>
			<div align="center"><html:submit value="Inscrever"/></div>
				</html:form> 
	</logic:notEmpty>	


</logic:present>			
