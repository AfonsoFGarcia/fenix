<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- alumniRegistrationResult.jsp -->

<h1>Alumni</h1>

<h2>Mailing Lists</h2>

<p><a href="<%= request.getContextPath() + "/conteudos-publicos/registo-alumni"%>">� Registo P�blico</a></p>

<div class="alumnilogo">
	
	<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
		<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
	</html:messages>

<p class="greytxt">
	Ao subscrever as listas abaixo especificadas, estreita o la�o comunicacional com a comunidade IST: pode trocar mensagens com os restantes membros da lista; receber informa��o �til e beneficiar do servi�o de alertas (agenda de realiza��es organizadas no IST e outras not�cias de interesse).
</p>

<p class="greytxt">
	Ao efectuar a subscri��o, indique o seu nome e n� de aluno(a) ou n� de BI para efeito de autentica��o.
</p>

<table class="tab_lay" id="table1" cellpadding="5" cellspacing="0" width="65%">
    <tbody>

        <tr>
            <th>
                �rea Ci�ntifica
            </th>
            <th>
                Moderador
            </th>
            <th>
                Administrador
            </th>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-ambiente@mlists.ist.utl.pt">Ambiente</a>
            </td>
            <td>
                Luis Castro
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-arquit@mlists.ist.utl.pt">Arquitectura</a>
            </td>
            <td>
                Luis Castro
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-lea@mlists.ist.utl.pt">Engenharia Aeroespacial</a>
            </td>
            <td>
                Fernando Lau
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-biologica@mlists.ist.utl.pt">Engenharia Biol�gica</a>
            </td>
            <td>
                M. Ros�rio Ribeiro
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-lebm@mlists.ist.utl.pt">Engenharia Biom�dica</a>
            </td>
            <td>
                -
            </td>
            <td>
                Engenharia Biom�dica
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-civil@mlists.ist.utl.pt">Engenharia Civil</a>
            </td>
            <td>
                Luis Castro
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:legi.graduados@mlists.ist.utl.pt">Engenharia de Gest�o Industrial</a>
            </td>
            <td>
                Jos� Figueiredo
            </td>
            <td>
                Jos� Figueiredo
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-lemat@mlists.ist.utl.pt">Engenharia de Materiais</a>
            </td>
            <td>
                Patr�cia Almeida Carvalho
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-minas@mlists.ist.utl.pt">Engenharia de Minas e Georecursos</a>
            </td>
            <td>
                Ant�nio Maur�cio
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-naval@mlists.ist.utl.pt">Engenharia e Arquitectura Naval</a>
            </td>
            <td>
                Yordan Garbatov
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-leec@mlists.ist.utl.pt">Engenharia Electrot�cnica e Computadores</a>
            </td>
            <td>
                Paula Queluz
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-left@mlists.ist.utl.pt">Engenharia F�sica e Tecnol�gica</a>
            </td>
            <td>
                Hor�cio Fernandes
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-leic@mlists.ist.utl.pt">Engenharia Inform�tica e Computadores</a>
            </td>
            <td>
                Jos� Borbinha
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-mec@mlists.ist.utl.pt">Engenharia Mec�nica</a>
            </td>
            <td>
                Pedro Coelho
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-eq@mlists.ist.utl.pt">Engenharia Qu�mica</a>
            </td>
            <td>
                M. Ros�rio Ribeiro
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-matapcomp@mlists.ist.utl.pt">Matem�tica Aplicada e Computa��o</a>
            </td>
            <td>
                Jo�o Pimentel Nunes
                <br>
                Jaime Ramos
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-quimica@mlists.ist.utl.pt">Qu�mica</a>
            </td>
            <td>
                M. Ros�rio Ribeiro
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-terr@mlists.ist.utl.pt">Territ�rio</a>
            </td>
            <td>
                Luis Castro
            </td>
            <td>
                GEP
            </td>
        </tr>
    </tbody>
</table>
		
	
	<!-- END CONTENTS -->
</div>