<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/rssutils.tld" prefix="rss" %>

<!-- index.jsp -->

<h2>
	<bean:message bundle="ALUMNI_RESOURCES" key="label.alumni.main.title"/>
</h2>

<style>
ul.material {
list-style: none;
padding: 0;
margin: 0;
}
ul.material li {
padding: 0.25em 0;
}
ul.material li.alerts { background: url(<%= request.getContextPath() %>/images/alumni/icon_alerts.png) no-repeat 10px 50%; padding-left: 35px; }
ul.material li.briefcase { background: url(<%= request.getContextPath() %>/images/alumni/icon_briefcase.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.homepage { background: url(<%= request.getContextPath() %>/images/alumni/icon_homepage.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.mailfwd { background: url(<%= request.getContextPath() %>/images/alumni/icon_mailfwd.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.newsletter { background: url(<%= request.getContextPath() %>/images/alumni/icon_newsletter.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.p_search { background: url(<%= request.getContextPath() %>/images/alumni/icon_peoplesearch.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.storage { background: url(<%= request.getContextPath() %>/images/alumni/icon_storage.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.m-list { background: url(<%= request.getContextPath() %>/images/alumni/icon_mlist.gif) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.library { background: url(<%= request.getContextPath() %>/images/alumni/icon_library.gif) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.feedback { background: url(<%= request.getContextPath() %>/images/alumni/icon_feedback.png) no-repeat 13px 50%; padding-left: 35px; }
</style>


<p>
	Bem-vindo � comunidade Alumni do IST. Todos temos de regressar � Escola, por isto ou por aquilo. Esta forma de regressar ser�, com certeza e mais uma vez, bem sucedida.
	Este servi�o est� no in�cio. O IST espera a contribui��o dos Alumni quer pela sua utiliza��o intensiva, quer pelos coment�rios, cr�ticas e sugest�es que s�o encorajados a fazer.
	O desenvolvimento do servi�o depende, em grande medida, desses dois factores.
</p>

<logic:present name="displayWarning">

	<!--
	<h3>Informa��o Pessoal</h3>
	-->
	<h3 class="mbottom075"><bean:message key="title.information.status" bundle="ALUMNI_RESOURCES"/></h3>
	<div class="infoop2 mbottom2">
		<p class="mvert05"><bean:message key="message.alumni.status.title" bundle="ALUMNI_RESOURCES"/>:</p>
		<ul class="mbottom05">
			<logic:present name="showContactsMessage">
				<li>
					<bean:define id="url"><%= request.getContextPath() %>/person/visualizePersonalInfo.do?contentContextPath_PATH=/pessoal/pessoal</bean:define>
					<b><bean:message key="label.alumni.contacts" bundle="ALUMNI_RESOURCES"/>:</b> 
					<bean:message key="message.alumni.contacts" bundle="ALUMNI_RESOURCES"/>
					<html:link href="<%= url %>">
						(<bean:message key="link.update.data" bundle="ALUMNI_RESOURCES" />)
					</html:link>
				</li>
			</logic:present>
			<li><b><bean:message key="link.professional.information" bundle="ALUMNI_RESOURCES"/>:</b>
				<bean:define id="professionalStatus" name="professionalStatus" type="java.lang.String"/> 
				<logic:present name="professionalNoData">
					<bean:message key="message.professional.nodata" bundle="ALUMNI_RESOURCES"/> 
					<html:link page="/professionalInformation.do?method=innerProfessionalInformation">
						(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES" />)
					</html:link>
				</logic:present>
				<logic:present name="professionalInsufficientData">
					<bean:message key="message.professional.insufficientData" arg0="<%= professionalStatus %>" bundle="ALUMNI_RESOURCES"/> 
					<html:link page="/professionalInformation.do?method=innerProfessionalInformation">
						(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES" />)
					</html:link>
				</logic:present>
				<logic:present name="professionalSufficientData">
					<bean:message key="message.professional.sufficientData" arg0="<%= professionalStatus %>" bundle="ALUMNI_RESOURCES"/> 
					<logic:notPresent name="dontShowJobComplete">
						<html:link page="/professionalInformation.do?method=innerProfessionalInformation">
							(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES" />)
						</html:link>
					</logic:notPresent>
				</logic:present>
			</li>
			<li><b><bean:message key="link.graduate.education" bundle="ALUMNI_RESOURCES"/>:</b>
				<bean:define id="educationStatus" name="educationStatus" type="java.lang.String"/> 
				<logic:present name="educationNoData">
					<bean:message key="message.education.nodata" bundle="ALUMNI_RESOURCES"/> 
					<html:link page="/formation.do?method=innerFormationManagement">
						(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES"/>)
					</html:link>
				</logic:present>
				<logic:present name="educationInsufficientData">
					<bean:message key="message.education.insufficientData" arg0="<%= educationStatus %>" bundle="ALUMNI_RESOURCES"/> 
					<html:link page="/formation.do?method=innerFormationManagement">
						(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES"/>)
					</html:link>
				</logic:present>
				<logic:present name="educationSufficientData">
					<bean:message key="message.education.sufficientData" arg0="<%= educationStatus %>" bundle="ALUMNI_RESOURCES"/> 	
					<logic:notPresent name="dontShowFormationComplete">				 
						<html:link page="/formation.do?method=innerFormationManagement">
							(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES"/>)
						</html:link>
					</logic:notPresent>
				</logic:present>
			</li>
		</ul>
	</div>
</logic:present>
<logic:notPresent name="displayWarning">
	<div>
		<p class="mvert05"><bean:message key="message.alumni.status.title" bundle="ALUMNI_RESOURCES"/>:</p>
		<bean:define id="professionalStatus" name="professionalStatus" type="java.lang.String"/>
		<bean:define id="educationStatus" name="educationStatus" type="java.lang.String"/>
		<ul class="mbottom05">		
			<li><b><bean:message key="link.professional.information" bundle="ALUMNI_RESOURCES"/>:</b> 
				<bean:message key="message.education.sufficientData" arg0="<%= professionalStatus %>" bundle="ALUMNI_RESOURCES"/>
				<logic:notPresent name="dontShowJobComplete"> 
					<html:link page="/professionalInformation.do?method=innerProfessionalInformation">
						(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES" />)
					</html:link>
				</logic:notPresent>
			</li>
			<li><b><bean:message key="link.graduate.education" bundle="ALUMNI_RESOURCES"/>:</b> 
				<bean:message key="message.education.sufficientData" arg0="<%= educationStatus %>" bundle="ALUMNI_RESOURCES"/>
				<logic:notPresent name="dontShowFormationComplete"> 				 
					<html:link page="/formation.do?method=innerFormationManagement">
						(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES"/>)
					</html:link>
				</logic:notPresent>
			</li>
		</ul>
	</div>
</logic:notPresent>

<rss:feed url="http://twitter.com/statuses/user_timeline/32443401.rss" feedId="istTwitter"/>
<bean:define id="isOnline">Twitter online?<rss:channelTitle feedId="istTwitter"/></bean:define>
<%
	if(isOnline.replace("Twitter online?","").isEmpty()){
    	request.setAttribute("displayTwitter","false");
	} else {
	    request.setAttribute("displayTwitter","true");
	}
%>

<h3 class="mbottom05"><bean:message key="title.news" bundle="ALUMNI_RESOURCES"/></h3>
<logic:equal name="displayTwitter" value="true">	
	<p class="mtop0">
		<bean:define id="channelTitle"><rss:channelTitle feedId="istTwitter"/></bean:define>
		<bean:message key="message.alumni.twitter" bundle="ALUMNI_RESOURCES" arg0="<%= channelTitle  %>"/> (<rss:channelLink feedId="istTwitter" asLink="true"/>)
	</p>
	<ul class="mbottom2">
	  <rss:forEachItem feedId="istTwitter" startIndex="0" endIndex="4">
	  	<bean:define id="twitt"><rss:itemDescription feedId="istTwitter"/></bean:define>
	  	<%  if(twitt.startsWith("istecnico: ")){
	  	  		String newtwitt = twitt.replace("istecnico: ","");
	  	  		String[] splittedTwitt = newtwitt.split("\n");
	  	  		if(splittedTwitt.length > 1){
		  			request.setAttribute("beforeUrl", splittedTwitt[0]);
		  			request.setAttribute("twittUrl", splittedTwitt[1]);
	  	  		} else {
	  	  		    int index = newtwitt.lastIndexOf("http://");
	  	  		    if(index >= 0) {
	  	  				String twittUrl = newtwitt.substring(index);
	  	  				splittedTwitt = newtwitt.split("http://");
	  	  				request.setAttribute("beforeUrl", splittedTwitt[0]);
			  			request.setAttribute("twittUrl", twittUrl);
	  	  		    }	  	  		
	  	  		}
	  		}
	  	%>
	    <li>
	    	<p class="mvert05">
	    		<logic:present name="beforeUrl">
	    			<bean:write name="beforeUrl"/>
	    			<html:link href="<%= request.getAttribute("twittUrl").toString() %>" target="_blank">
	    				<bean:write name="twittUrl"/>
	    			</html:link>
	    		</logic:present>
	    		<logic:notPresent name="beforeUrl"><bean:write name="twitt"/></logic:notPresent>
	    	</p>    	
	    </li>    
	  </rss:forEachItem>
	</ul>
</logic:equal>
<logic:equal name="displayTwitter" value="false">
	<em><bean:message key="message.alumni.twitter.noAccess" bundle="ALUMNI_RESOURCES"/></em>
</logic:equal>

<h3>Vantagens</h3>
<div style="background: #f5f5f5; border: 1px solid #ddd; padding: 0.75em 0.5em;">
	<p class="indent1 mtop025 mbottom05">Al�m das op��es vis�veis nos menus, lembramos que o leque de vantagens j� dispon�veis inclui:</p>
	<ul class="material">
		<li class="mailfwd">endere�o de <html:link target="_blank" href="https://ciist.ist.utl.pt/servicos/mail.php">mail personalizado</html:link> e, se necess�rio, com <em>forward</em> autom�tico (se j� possui mail, efectue a sua <html:link target="_blank" href="https://ciist.ist.utl.pt/servicos/self_service">activa��o</html:link>)</li>
		<li class="homepage">alojamento de p�gina web institucional (cf. �rea Pessoal)</li>
		<li class="library">acesso � <a target="_blank" href="http://bist.ist.utl.pt">Biblioteca do IST</a> (cart�o de utilizador + recursos electr�nicos)</li>
	</ul>
</div>

<p>Recordamos, ainda, que os servi�os a seguir listados, est�o � vossa disposi��o, prontos a corresponder �s solicita��es que lhes forem dirigidas:</p>

<h3>Descontos especiais</h3>
<ul>
	<li>na aquisi��o de publica��es da <a target="_blank" href="http://www.istpress.ist.utl.pt/">IST Press</a>;</li>
	<li>na aquisi��o de produtos de <a target="_blank" href="http://gcrp.ist.utl.pt/html/relacoespublicas/produtos.shtml">merchandising</a>;</li>
	<li>na utiliza��o de espa�os do <a target="_blank" href="http://centrocongressos.ist.utl.pt/">Centro de Congressos do IST</a>.</li>
</ul>

<h3>Oportunidades, aconselhamento e apoio informativo</h3>
<ul>
	<li><a target="_blank" href="http://www.ist.utl.pt/html/ensino/">Ensino, P�s-gradua��es e Forma��o</a></li>
	<li><a target="_blank" href="http://galtec.ist.utl.pt/">Licenciamento de Tecnologia</a></li>
	<li><a target="_blank" href="http://www.istpress.ist.utl.pt/">Oportunidades de publica��o de livros</a></li>
	<li><a href="mailto:empreendedorismo@ist.utl.pt">Empreendedorismo</a></li>
	<li><a target="_blank" href="http://gep.ist.utl.pt/html/oe">Empregabilidade</a></li>
	<li><a target="_blank" href="http://gep.ist.utl.pt/">Estudos, Projectos e Estat�sticas do IST</a></li>
	<li><a target="_blank" href="http://namp.ist.utl.pt/">Apoio M�dico e Psicol�gico</a></li>
	<li><a target="_blank" href="http://nape.ist.utl.pt">Cultura e Desporto</a></li>
</ul>

<h3>Links �teis</h3>
<ul>
	<li><a target="_blank" href="http://aaa.ist.utl.pt" title="Uma ponte entre o T�cnico e os seus Antigos Alunos">Associa��o dos Antigos Alunos do <bean:message key="institution.name" bundle="GLOBAL_RESOURCES"/> (AAAIST)</a></li>
	<li><a target="_blank" href="http://www.ordemengenheiros.pt">Ordem dos Engenheiros</a></li>
	<li><a target="_blank" href="http://www.academia-engenharia.org">Academia da Engenharia</a></li>
	<li><a target="_blank" href="http://www.apengsaude.org">Associa��o Portuguesa de Engenharia da Sa�de</a></li>
	<li><a target="_blank" href="http://www.dec.uc.pt/aciv/index.php?section=18">Associa��o para o Desenvolvimento da Engenharia Civil</a></li>
	<li><a target="_blank" href="http://www.apea.pt">Associa��o Portuguesa de Engenharia do Ambiente</a></li>
</ul>

<h3>Dados Pessoais</h3>
<ul>
	N�o se esque�a de activar, no seu Perfil Pessoal, as autoriza��es relativas aos dados pessoais disponibilizados.
</ul>


<h3>Coment�rios ou sugest�es</h3>
<ul class="material">
	<li class="feedback">A sua opini�o � importante. Se tem alguma sugest�o, crit�ca ou coment�rio <a href="mailto:alumni@ist.utl.pt?subject=Alumni_feedback">escreva-nos um e-mail</a>. N�s prometemos uma resposta!</li>
</ul>

<p><em>O IST, hoje como ontem, continua ao seu servi�o.</em></p>
