<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/jcaptcha.tld" prefix="jcaptcha"%>

<!-- alumniPublicAccess.jsp -->

<h1>Alumni</h1>

<div class="alumnilogo">

<%-- <div class="col_right_alumni"><img src="http://www.ist.utl.pt/img/alumni/alumni_deco_3.gif" alt="[Image] Alumni" /></div> --%>


<p class="greytxt"><bean:message key="label.alumni.welcome.a" bundle="ALUMNI_RESOURCES" /></p>
<p class="greytxt"><bean:message key="label.alumni.welcome.b" bundle="ALUMNI_RESOURCES" /></p>

<logic:present name="alumniPublicAccessMessage">
	<span class="error0"><bean:write name="alumniPublicAccessMessage" scope="request" /></span><br/>
</logic:present>

<table>
	<tr>
		<td>
			<span class="btt_inscrever"><a href="#" id="show" onclick="document.getElementById('registration').style.display='block'"><bean:message key="label.alumni.register" bundle="ALUMNI_RESOURCES" /></a></span>
		</td>
		<td style="padding-left: 0.5em;">
			<span class="btt_login"><a href="<bean:message key="label.fenix.login.hostname" bundle="ALUMNI_RESOURCES" />"><bean:message key="label.alumni.register" bundle="ALUMNI_RESOURCES" /></a></span>
		</td>
	</tr>
</table>

<logic:present name="showForm">
	<logic:equal name="showForm" value="true">
	<div id="registration" >
	</logic:equal>
	<logic:equal name="showForm" value="false">
	<div id="registration" class="switchInline">
	</logic:equal>
</logic:present>

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
</html:messages>


<div class="reg_form">	

	<fr:form action="/alumni.do?method=validateFenixAcessData">

		<fieldset style="display: block;">
			<legend><bean:message key="label.alumni.form" bundle="ALUMNI_RESOURCES" /></legend>
			<p>
				<bean:message key="label.alumni.registration.process" bundle="ALUMNI_RESOURCES" />
				<html:link href="<%= request.getContextPath() + "/publico/alumni.do?method=requestIdentityCheck"%>"><bean:message bundle="ALUMNI_RESOURCES" key="link.request.identity.check"/></html:link>
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
</div>

<h2>Sobre o Projecto Alumni</h2>
<p>O projecto Alumni consiste na cria&ccedil;&atilde;o de uma rede de contactos com os Antigos Alunos do T&eacute;cnico, com vista &agrave; manuten&ccedil;&atilde;o da liga&ccedil;&atilde;o com a escola na perspectiva da forma&ccedil;&atilde;o ao longo da vida, da actualiza&ccedil;&atilde;o de informa&ccedil;&atilde;o e conhecimentos, e do refor&ccedil;o de uma comunidade orientada para a produ&ccedil;&atilde;o cient&iacute;fica e tecnol&oacute;gica.</p>

<div class="h_box">
<p>Vantagens que o IST oferece aos Antigos Alunos inscritos:</p>
<ul class="material">
	<li class="briefcase">secretaria online</li>
	<li class="m-list">subscri��o de <html:link href="<%= request.getContextPath() + "/publico/alumni.do?method=checkLists"%>"><bean:message bundle="ALUMNI_RESOURCES" key="link.check.mailing.lists"/></html:link></li>
	<li class="p_search">procura de colegas</li>	
	<li class="alerts">um servi�o de alertas com mensagens �teis e concisas</li>
	<li class="mailfwd">endere�o de <html:link target="_blank" href="https://ciist.ist.utl.pt/servicos/mail.php#webmail">mail personalizado</html:link> e, se necess�rio, com <em>forward</em> autom�tico</li>
	<li class="homepage">alojamento de p�gina web institucional</li>
	<li class="library">acesso � <a target="_blank" href="http://bist.ist.utl.pt">Biblioteca do IST</a> (cart�o de utilizador + recursos electr�nicos)</li>
</ul>

</div>
<h2>Descontos especiais</h2>
<ul>
	<li>na aquisi��o de publica��es da <a target="_blank" href="http://www.istpress.ist.utl.pt/">IST Press</a>;</li>
	<li>na aquisi��o de produtos de <a target="_blank" href="http://gcrp.ist.utl.pt/html/relacoespublicas/produtos.shtml">merchandising</a>;</li>
	<li>na utiliza��o de espa�os do <a target="_blank" href="http://centrocongressos.ist.utl.pt/">Centro de Congressos do IST</a>.</li>

</ul>
<h2>Oportunidades, aconselhamento e apoio informativo</h2>
<ul>
	<li><a target="_blank" href="http://gcrp.ist.utl.pt/html/recrutamento/index.shtml">Procura/Oferta de Est�gio/Emprego</a></li>
	<li><a target="_blank" href="http://www.ist.utl.pt/html/ensino">Ensino, P�s-gradua��es e Forma��o</a></li>
	<li><a target="_blank" href="http://galtec.ist.utl.pt">Licenciamento de Tecnologia</a></li>
	<li><a target="_blank" href="http://www.istpress.ist.utl.pt/">Oportunidades de publica��o de livros</a></li>
	<li><a href="mailto:empreendedorismo@ist.utl.pt">Empreendedorismo</a></li>
	<li><a target="_blank" href="http://gep.ist.utl.pt/html/oe">Empregabilidade</a></li>
	<li><a target="_blank" href="http://gep.ist.utl.pt">Estudos, Projectos e Estat�sticas do IST</a></li>
	<li><a target="_blank" href="http://namp.ist.utl.pt">Apoio M�dico e Psicol�gico</a></li>
	<li><a target="_blank" href="http://nape.ist.utl.pt">Cultura e Desporto</a></li>	
</ul>

<h2>Links �teis</h2>
<ul>
	<li><a target="_blank" href="http://aaa.ist.utl.pt" title="Uma ponte entre o T�cnico e os seus Antigos Alunos">Associa��o dos Antigos Alunos do <bean:message key="institution.name" bundle="GLOBAL_RESOURCES"/> (AAAIST)</a></li>
	<li><a target="_blank" href="http://www.ordemengenheiros.pt">Ordem dos Engenheiros</a></li>
	<li><a target="_blank" href="http://www.academia-engenharia.org">Academia da Engenharia</a></li>
	<li><a target="_blank" href="http://www.apengsaude.org">Associa��o Portuguesa de Engenharia da Sa�de</a></li>
	<li><a target="_blank" href="http://www.dec.uc.pt/aciv/index.php?section=18">Associa��o para o Desenvolvimento da Engenharia Civil</a></li>
	<li><a target="_blank" href="http://www.apea.pt">Associa��o Portuguesa de Engenharia do Ambiente</a></li>
</ul>

<h2>Coment�rios ou sugest�es</h2>
<ul class="material">
	<li class="feedback">A sua opini�o � importante. <span class="marker">Se tem alguma sugest�o, crit�ca ou coment�rio <a href="mailto:alumni@ist.utl.pt?subject=Alumni_feedback">escreva-nos um e-mail</a></span>. N�s prometemos uma resposta!</li>

</ul>


<!-- END CONTENTS -->
</div>





