<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

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
ul.material li.alerts { background: url(http://www.ist.utl.pt/img/alumni/icon_alerts.png) no-repeat 10px 50%; padding-left: 35px; }
ul.material li.briefcase { background: url(http://www.ist.utl.pt/img/alumni/icon_briefcase.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.homepage { background: url(http://www.ist.utl.pt/img/alumni/icon_homepage.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.mailfwd { background: url(http://www.ist.utl.pt/img/alumni/icon_mailfwd.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.newsletter { background: url(http://www.ist.utl.pt/img/alumni/icon_newsletter.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.p_search { background: url(http://www.ist.utl.pt/img/alumni/icon_peoplesearch.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.storage { background: url(http://www.ist.utl.pt/img/alumni/icon_storage.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.m-list { background: url(http://www.ist.utl.pt/img/alumni/icon_mlist.gif) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.library { background: url(http://www.ist.utl.pt/img/alumni/icon_library.gif) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.feedback { background: url(http://www.ist.utl.pt/img/alumni/icon_feedback.png) no-repeat 13px 50%; padding-left: 35px; }
</style>


<!--
<p>Bem-vindo � comunidade Alumni do IST. Todos temos de regressar � Escola, por isto ou por aquilo. Esta forma de regressar ser�, com certeza e mais uma vez, bem sucedida.</p>
<p>Este servi�o est� no in�cio. O IST espera a contribui��o dos Alumni quer pela sua utiliza��o intensiva, quer pelos coment�rios, cr�ticas e sugest�es que s�o encorajados a fazer.</p>
<p>O desenvolvimento do servi�o depende, em grande medida, desses dois factores.</p>
-->

<p>
	Bem-vindo � comunidade Alumni do IST. Todos temos de regressar � Escola, por isto ou por aquilo. Esta forma de regressar ser�, com certeza e mais uma vez, bem sucedida.
	Este servi�o est� no in�cio. O IST espera a contribui��o dos Alumni quer pela sua utiliza��o intensiva, quer pelos coment�rios, cr�ticas e sugest�es que s�o encorajados a fazer.
	O desenvolvimento do servi�o depende, em grande medida, desses dois factores.
</p>


<h3>Vantagens</h3>


<div style="background: #f5f5f5; border: 1px solid #ddd; padding: 0.75em 0.5em;">
	<p class="indent1 mtop025 mbottom05">Al�m das op��es vis�veis nos menus, lembramos que o leque de vantagens j� dispon�veis inclui:</p>
	<ul class="material">
		<li class="m-list">subscri��o de <html:link target="_blank" href="<%= request.getContextPath() + "/publico/alumni.do?method=checkLists"%>"><bean:message bundle="ALUMNI_RESOURCES" key="link.check.mailing.lists"/></html:link> por �reas de licenciatura pr�-Bolonha</li>
		<li class="alerts">um servi�o de alertas com mensagens do IST acerca da agenda de realiza��es em curso: Confer�ncias, Congressos, Col�quios, Cursos, Ac��es de Forma��o e outras not�cias de interesse. Para beneficiar deste servi�o dever� subscrever uma das <html:link href="<%= request.getContextPath() + "/publico/alumni.do?method=checkLists"%>"><bean:message bundle="ALUMNI_RESOURCES" key="link.check.mailing.lists"/></html:link></li>
		<li class="mailfwd">endere�o de <html:link target="_blank" href="https://ciist.ist.utl.pt/servicos/mail.php">mail personalizado</html:link> e, se necess�rio, com <em>forward</em> autom�tico</li>
		<li class="homepage">alojamento de p�gina web institucional</li>
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
	<li><a target="_blank" href="http://gcrp.ist.utl.pt/html/recrutamento/index.shtml">Procura/Oferta de Est�gio/Emprego</a></li>
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
	<li><a target="_blank" href="http://aaa.ist.utl.pt" title="Uma ponte entre o T�cnico e os seus Antigos Alunos">Associa��o dos Antigos Alunos do Instituto Superior T�cnico (AAAIST)</a> <em></li>
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