<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/jcaptcha.tld" prefix="jcaptcha"%>

<!-- alumniPublicAccess.jsp -->

<h1><bean:message key="label.alumni.registration" bundle="ALUMNI_RESOURCES" /></h1>

<h2><bean:message key="label.alumni.registration.form" bundle="ALUMNI_RESOURCES" /> <span class="color777 fwnormal"><bean:message key="label.step.1.3" bundle="ALUMNI_RESOURCES" /></span></h2>

<div class="alumnilogo">
<logic:present name="alumniPublicAccessMessage">
	<span class="error0"><bean:write name="alumniPublicAccessMessage" scope="request" /></span><br/>
</logic:present>

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	<logic:present name="showReportError">
		<bean:define id="documentIdNumber" name="alumniBean" property="documentIdNumber"/>
		<bean:define id="email" name="alumniBean" property="email"/>
		<bean:define id="studentNumber" name="alumniBean" property="studentNumber"/>
		<html:link  action="<%= "alumni.do?method=prepareSendEmailReportingError&amp;documentIdNumber=" + documentIdNumber + 
								"&amp;email=" + email + "&amp;studentNumber=" + studentNumber %>"
					paramId="errorMessage" paramName="errorMessageKey">
			<bean:message key="label.public.report.error" bundle="ALUMNI_RESOURCES"/>
		</html:link>
	</logic:present>
</html:messages>

<div class="reg_form">	

	<fr:form action="/alumni.do?method=validateFenixAcessData">

		<fieldset style="display: block;">
			<h3>Identifica��o <%-- <bean:message key="label.alumni.form" bundle="ALUMNI_RESOURCES" /> --%></h3>
			<p>
				<bean:message key="label.alumni.registration.process" bundle="ALUMNI_RESOURCES" />				
			</p>
		
			<fr:edit id="alumniBean" name="alumniBean" visible="false" />

			<label for="student_number" class="student_number">
				<bean:message key="label.student.number" bundle="ALUMNI_RESOURCES" />:
			</label>
			<fr:edit id="studentNumber-validated" name="alumniBean" slot="studentNumber" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
				<fr:destination name="invalid" path="/alumni.do?method=initFenixPublicAccess&showForm=true"/>
				<fr:layout>
					<fr:property name="size" value="30"/>
					<fr:property name="style" value="display: inline;"/>
				</fr:layout>
			</fr:edit>
			<span class="error0"><fr:message for="studentNumber-validated" /></span>
			<html:link href="<%= request.getContextPath() + "/publico/alumni.do?method=requestIdentityCheck"%>"><bean:message bundle="ALUMNI_RESOURCES" key="link.request.identity.check"/></html:link>						
					
			<label for="bi_number" class="bi_number">
				<bean:message key="label.document.id.number" bundle="ALUMNI_RESOURCES" />:
			</label>
			<fr:edit id="documentIdNumber-validated" name="alumniBean" slot="documentIdNumber" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:destination name="invalid" path="/alumni.do?method=initFenixPublicAccess&showForm=true"/>
				<fr:layout>
					<fr:property name="size" value="30"/>
					<fr:property name="style" value="display: inline;"/>
				</fr:layout>
			</fr:edit>
			<span class="error0"><fr:message for="documentIdNumber-validated" /></span>
			
			<label for="email">
				<bean:message key="label.email" bundle="ALUMNI_RESOURCES" />:
			</label>
			<fr:edit id="email-validated" name="alumniBean" slot="email" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredEmailValidator">
				<fr:destination name="invalid" path="/alumni.do?method=initFenixPublicAccess&showForm=true"/>
				<fr:layout>
					<fr:property name="size" value="40"/>
					<fr:property name="style" value="display: inline;"/>
				</fr:layout>
			</fr:edit>
			<span class="error0"><fr:message for="email-validated" /></span>

			<label for="captcha">
				<bean:message key="label.captcha" bundle="ALUMNI_RESOURCES" />:
			</label>
			<div class="mbottom05"><img src="<%= request.getContextPath() + "/publico/jcaptcha.do" %>"/><br/></div>
			<span class="color777"><bean:message key="label.captcha.process" bundle="ALUMNI_RESOURCES" /></span><br/>
			<input type="text" name="j_captcha_response" size="30" style="margin-bottom: 1em;"/>
			
			<logic:present name="captcha.unknown.error">
				<p style="margin: 0;">
					<span class="error0">
						<bean:message key="captcha.unknown.error" bundle="ALUMNI_RESOURCES" />
					</span>
				</p>
			</logic:present>

			<br/>

			<fr:edit id="privacyPolicy-validated" name="alumniBean" slot="privacyPolicy" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:layout>
					<fr:property name="style" value="display: inline;"/>
				</fr:layout>
			</fr:edit>
			<label style="display: inline;">
				<bean:message key="label.privacy.policy.a" bundle="ALUMNI_RESOURCES" />
				<html:link href="#" onclick="document.getElementById('policyPrivacy').style.display='block'" >
					<bean:message key="label.privacy.policy.b" bundle="ALUMNI_RESOURCES" />
				</html:link>
			</label>

			<div id="policyPrivacy" class="switchInline mtop1">
				<bean:message key="label.privacy.policy.text" bundle="ALUMNI_RESOURCES" />
			</div>
						
			<logic:present name="privacyPolicyPublicAccessMessage">
				<span class="error0">
					<bean:message key="privacy.policy.acceptance" bundle="ALUMNI_RESOURCES" />
				</span>
			</logic:present>
			
			<p class="comment"><bean:message key="label.all.required.fields" bundle="ALUMNI_RESOURCES" /></p>

			<html:submit>
				<bean:message key="label.submit" bundle="ALUMNI_RESOURCES" />
			</html:submit>
		</fieldset>
	</fr:form>
</div>
<div class="alumni-faq color777">
		<h3>FAQ</h3>
		<ol>
			<li>
				<h4>Como recuperar a minha IST-ID?</h4>
				<p>Poder� contactar a Dire��o de Servi�os de Inform�tica (ci@ist.utl.pt), fornecendo o seu n�mero de identifica��o (BI, Cart�o do Cidad�o...).</p>
			</li>
			<li>
				<h4>Como recuperar a password?</h4>
				<p>Existem v�rias possibilidades de recupera��o, que podem ser consultadas no seguinte endere�o: <a href="https://id.ist.utl.pt/password/recover.php?language=pt" title="Recuperar password">https://id.ist.utl.pt/password/recover.php?language=pt</a>.</p>
			</li>
			<li>
				<h4>Como alterar a IST-ID?</h4>
				<p>N�o � poss�vel alterar a IST-ID, uma vez que � um n�mero de identifica��o gerado uma �nica vez, utilizado para o acesso aos servi�os inform�ticos do IST, correspondendo na maior parte dos casos ao n�mero de Aluno/Docente (ex: N�de Aluno 55000 corresponde ao IST-ID ist155000).</p>
			</li>
			<li>
				<h4>Tive mais do que um n�mero de aluno. Qual o n�mero que deverei facultar?</h4>
				<p>Poder� facultar qualquer n�mero de Aluno (Licenciatura, Mestrado, Doutoramento), uma vez que a IST-ID agrupa todas as Matriculas que teve enquanto Aluno.</p>
			</li>
			<li>
				<h4>Como alterar o n�mero de telem�vel?</h4>
				<p>Ter� de solicitar ao N�cleo correspondente ao seu Curso a atualiza��o dos seus dados Pessoais (neste caso, telem�vel).<br>
				N�cleo de Gradua��o - <a href="mailto:nucleo.graduacao@ist.utl.pt" title="Enviar email">nucleo.graduacao@ist.utl.pt</a><br>
				N�cleo de P�s-Gradua��o e Forma��o Cont�nua - <a href="mailto:npfc@ist.utl.pt" title="Enviar email">npfc@ist.utl.pt</a></p>
			</li>
		</ol>
	</div>
</div>

<!-- END CONTENTS -->
</div>