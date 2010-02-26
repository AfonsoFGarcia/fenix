<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.tutorshipSummary" bundle="APPLICATION_RESOURCES" />: <bean:write name="createSummaryBean" property="teacher.person.name" /></h2>
<h3>
<bean:message key="label.curricular.course.semester" bundle="APPLICATION_RESOURCES" />
<bean:write name="createSummaryBean" property="executionSemester.semester" /> -
<bean:write name="createSummaryBean" property="executionSemester.executionYear.year" />
</h3>
<h3>
<bean:message key="label.degree.name" bundle="APPLICATION_RESOURCES" />:
<bean:write name="createSummaryBean" property="degree.sigla" />
</h3>

<p class="mtop2 mbottom1 separator2"/>
	<b><bean:message key="label.tutorshipSummary.create" bundle="APPLICATION_RESOURCES"/></b>
</p>

<fr:form action="/tutorshipSummary.do">
	<html:hidden property="method" value="processCreateSummary" />
	
	<fr:messages />
	
	<!-- first data: name, course, year, indicators -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">1</span></td>
			<td class="infoop"><strong><bean:message key="label.tutorshipSummary.create.contacts" bundle="APPLICATION_RESOURCES"/></strong></td>
		</tr>
	</table>
	<br />
	
	<fr:edit name="createSummaryBean" layout="tabular" schema="tutorship.tutorate.createSummary">
		<fr:layout>
			<fr:property name="classes" value="tstyle1 thlight mtop0 mbottom15 tdcenter"/>
		<fr:property name="columnClasses" value="aleft,acenter"/>
		</fr:layout>
	</fr:edit>

	<!-- student participation -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">2</span></td>
			<td class="infoop"><strong><bean:message key="label.tutorshipSummary.create.studentParticipation" bundle="APPLICATION_RESOURCES"/></strong></td>
		</tr>
	</table>
	
	<fr:edit name="createSummaryBean" property="tutorshipRelations" schema="tutorship.tutorate.createSummaryRelation">
		<fr:layout name="tabular-editable">
		    <fr:property name="classes" value="tstyle1 printborder tpadding1"/>
			<fr:property name="columnClasses" value="bgcolor3 aleft,acenter,acenter,acenter,acenter,acenter,acenter,acenter" />
			<fr:property name="headerClasses" value="acenter" />
		</fr:layout>
	</fr:edit>
	
	<div class="simpleblock3">
	<dl>
		<dt><bean:message key="label.tutorshipSummary.form.participationType" bundle="APPLICATION_RESOURCES"/>:</dt> 
		<dt><bean:message key="label.tutorshipSummary.form.participationRegularly" bundle="APPLICATION_RESOURCES"/>:</dt> 
		<dt><bean:message key="label.tutorshipSummary.form.participationNone" bundle="APPLICATION_RESOURCES"/>: </dt>
		<dt><bean:message key="label.tutorshipSummary.form.outOfTouch" bundle="APPLICATION_RESOURCES"/>: </dt>
		<dt><bean:message key="label.tutorshipSummary.form.highPerformance" bundle="APPLICATION_RESOURCES"/>: </dt>
		<dt><bean:message key="label.tutorshipSummary.form.lowPerformance" bundle="APPLICATION_RESOURCES"/>:</dt>
	</dl> 
	</div>
		
	<!-- program participation -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">3</span></td>
			<td class="infoop"><strong><bean:message key="label.tutorshipSummary.create.programParticipation" bundle="APPLICATION_RESOURCES"/></strong></td>
		</tr>
	</table>
	
	<h3>Principais problemas apresentados <u>pelos alunos</u></h3>
	
	<table class="tstyle1 printborder tpadding1">
		<tr>
			<td><fr:edit name="createSummaryBean" slot="problemsR1" /></td>
			<td>Hor�rios/Inscri��es</td>
			<td><fr:edit name="createSummaryBean" slot="problemsR2" /></td>
			<td>M�todos de Estudo</td>
			<td><fr:edit name="createSummaryBean" slot="problemsR3" /></td>
			<td>Gest�o de Tempo/Volume de Trabalho</td>
		</tr>
		<tr>
			<td><fr:edit name="createSummaryBean" slot="problemsR4" /></td>
			<td>Acesso a Informa��o (ex.:aspectos administrativos; ERASMUS; etc.)</td>
			<td><fr:edit name="createSummaryBean" slot="problemsR5" /></td>
			<td>Transi��o Ensino Secund�rio/Ensino Superior</td>
			<td><fr:edit name="createSummaryBean" slot="problemsR6" /></td>
			<td>Problemas Vocacionais</td>
		</tr>
		<tr>
			<td><fr:edit name="createSummaryBean" slot="problemsR7" /></td>
			<td>Rela��o Professor - Aluno</td>
			<td><fr:edit name="createSummaryBean" slot="problemsR8" /></td>
			<td>Desempenho Acad�mico (ex.: taxas de aprova��o)</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td><fr:edit name="createSummaryBean" slot="problemsR9" /></td>
			<td>Avalia��o (ex.: metodologia, datas de exames; etc.)</td>
			<td><fr:edit name="createSummaryBean" slot="problemsR10" /></td>
			<td>Adapta��o ao IST</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td colspan="6">Outro:
				<fr:edit name="createSummaryBean" slot="problemsOther">
					<fr:layout>
						<fr:property name="size" value="80" />
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
	</table>	

	
	<h3>Principais ganhos percepcionados <u>para os alunos</u></h3>
	
	<table class="tstyle1 printborder tpadding1">
		<tr>
			<td><fr:edit name="createSummaryBean" slot="gainsR1" /></td>
			<td>Maior responsabiliza��o/autonomiza��o do Aluno</td>
			<td><fr:edit name="createSummaryBean" slot="gainsR2" /></td>
			<td>Altera��o dos m�todos de estudo</td>
			<td><fr:edit name="createSummaryBean" slot="gainsR3" /></td>
			<td>Planeamento do semestre/Avalia��o</td>
		</tr>
		<tr>
			<td><fr:edit name="createSummaryBean" slot="gainsR4" /></td>
			<td>Acompanhamento mais individualizado</td>
			<td><fr:edit name="createSummaryBean" slot="gainsR5" /></td>
			<td>Maior motiva��o para o curso</td>
			<td><fr:edit name="createSummaryBean" slot="gainsR6" /></td>
			<td>Melhor desempenho acad�mico</td>
		</tr>
		<tr>
			<td><fr:edit name="createSummaryBean" slot="gainsR7" /></td>
			<td>Maior proximidade Professor-Aluno</td>
			<td><fr:edit name="createSummaryBean" slot="gainsR8" /></td>
			<td>Transi��o do Ensino Secund�rio para o Ensino Superior mais f�cil</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td><fr:edit name="createSummaryBean" slot="gainsR9" /></td>
			<td>Melhor adapta��o ao IST</td>
			<td><fr:edit name="createSummaryBean" slot="gainsR10" /></td>
			<td>Apoio na tomada de decis�es/Resolu��o de problemas</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td colspan="6">Outro:
				<fr:edit name="createSummaryBean" slot="gainsOther">
					<fr:layout>
						<fr:property name="size" value="80" />
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
	</table>	
	
	<!-- conclusions -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">4</span></td>
			<td class="infoop"><strong><bean:message key="label.tutorshipSummary.create.conclusions" bundle="APPLICATION_RESOURCES"/></strong></td>
		</tr>
	</table>
	<br />		


	<table class="tstyle1 thlight mtop0 mbottom15 tdleft">
		<tr>
			<td>Os meus tutorandos est�o satisfeitos com o que fiz e com o modo como o fiz?</td>
			<td><fr:edit name="createSummaryBean" slot="tutorshipSummarySatisfaction" /></td>
		</tr>
		<tr>
			<td>Aprecia��o Global do Programa:</td>
			<td><fr:edit name="createSummaryBean" slot="tutorshipSummaryProgramAssessment" /></td>
		</tr>
	</table>
	
	<table class="tstyle1 thlight mtop0 mbottom15 tdleft">
		<tr>
			<td>Dificuldades sentidas <u>pelo(a) Tutor(a)</u> no acompanhamento dos estudantes</td>
			<td>
				<fr:edit name="createSummaryBean" slot="difficulties">
					<fr:layout name="longText">
						<fr:property name="columns" value="60" />
						<fr:property name="rows" value="3" />
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		<tr>
			<td>Ganhos sentidos <u>pelo(a) Tutor(a)</u> no acompanhamento dos estudantes</td>
			<td>
				<fr:edit name="createSummaryBean" slot="gains">
					<fr:layout name="longText">
						<fr:property name="columns" value="60" />
						<fr:property name="rows" value="3" />
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
	</table>

	<table class="tstyle1 thlight mtop0 mbottom15 tdleft">
		<tr>
			<td>Sugest�es</td>
			<td>
				<fr:edit name="createSummaryBean" slot="suggestions">
					<fr:layout name="longText">
						<fr:property name="columns" value="60" />
						<fr:property name="rows" value="10" />
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
	</table>

	
	<fr:edit id="createSummaryBean" name="createSummaryBean" visible="false" />

	<html:submit><bean:message key="button.forward" bundle="APPLICATION_RESOURCES"/></html:submit>

</fr:form>
