<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="title.student.thesis.declaration"/></h2>

<ul>
    <li>
        <html:link page="/thesisSubmission.do?method=prepareThesisSubmission">
            <bean:message key="link.student.declaration.goBack"/>
        </html:link>
    </li>
</ul>

<bean:define id="name" name="thesis" property="student.person.name"/>
<bean:define id="number" name="thesis" property="student.number"/>
<bean:define id="degree" name="thesis" property="degree.name"/>
<bean:define id="title" name="thesis" property="title.content"/>

<div style="background: #f5f5f5; color: #444; border: 1px solid #ddd; padding: 0.75em 1em;">

<p style="line-height: 1.9em;">
    <strong><%= name %></strong>, aluno do Instituto Superior T�cnico n� <strong><%= number %></strong>, autor da
    disserta��o para obten��o do <strong>Grau de Mestre em <%= degree %></strong> com o
    t�tulo <strong><%= title %></strong>, autorizo o Instituto Superior T�cnico a inserir,
    em formato pdf, a vers�o final desta disserta��o e o seu resumo alargado na sua
    <strong><fr:view name="thesis" property="visibility"/></strong>
    , possibilitando assim o seu conhecimento a todos os que
    possam aceder �quele meio, com a ressalva de que estes n�o possam, sem a minha
    expressa autoriza��o, reproduzir, por qualquer meio, o texto daquela minha
    disserta��o para al�m dos limites fixados no C�digo do Direito de Autor e dos
    Direitos Conexos.
</p>

<p style="line-height: 1.9em;">
    Mais autorizo, com car�cter de n�o exclusividade, o Instituto Superior T�cnico a
    reproduzir, no todo ou em parte, aquela minha disserta��o para assim responder a
    pedidos que lhe sejam formulados, por parte de institui��es de ensino ou de
    investiga��o bem como por parte de Centros de Documenta��o ou de Bibliotecas, e
    desde que desses pedidos resulte que a reprodu��o solicitada da minha
    disserta��o apenas se destina a fins pedag�gicos ou de investiga��o.
</p>

</div>

<p style="float: right;">
    <fr:view name="thesis" property="declarationAcceptedTime">
        <fr:layout name="as-date">
            <fr:property name="format" value="'Aceite em' d 'de' MMMM 'de' yyyy '�s' HH:mm"/>
        </fr:layout>
    </fr:view>
</p>
