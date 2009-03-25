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

<%-- confirmation --%>
<logic:present name="confirmRejectWithFiles">
    <div class="warning0" style="padding: 1em;">
        <p class="mtop0 mbottom05"><bean:message key="label.thesis.declaration.reject.deleteFiles"/></p>
        <p class="mtop05 mbottom0">
        <fr:form action="/thesisSubmission.do?method=changeDeclaration">
            <html:submit property="confirmReject">
                <bean:message key="button.confirm"/>
            </html:submit>
        </fr:form>
        </p>
    </div>
</logic:present>

<%-- error message --%>
<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<bean:define id="name" name="thesis" property="student.person.name"/>
<bean:define id="number" name="thesis" property="student.number"/>
<bean:define id="degree" name="thesis" property="degree.name"/>
<bean:define id="title" name="thesis" property="title.content"/>

<fr:form action="/thesisSubmission.do?method=changeDeclaration">
    <fr:edit id="declarationBean" name="bean" visible="false"/>

<div style="background: #f5f5f5; color: #444; border: 1px solid #ddd; padding: 0.75em 1em;">

<p style="line-height: 1.9em;">
    <strong><%= name %></strong>, aluno do <bean:message key="institution.name" bundle="GLOBAL_RESOURCES"/> n� <strong><%= number %></strong>, autor da
    disserta��o para obten��o do <strong>Grau de Mestre em <%= degree %></strong> com o
    t�tulo <strong><%= title %></strong>, autorizo o <bean:message key="institution.name" bundle="GLOBAL_RESOURCES"/> a inserir,
    em formato pdf, a vers�o final desta disserta��o e o seu resumo alargado na sua
    <fr:edit id="visibility" name="bean" slot="visibility"/>
    , possibilitando assim o seu conhecimento a todos os que
    possam aceder �quele meio, com a ressalva de que estes n�o possam, sem a minha
    expressa autoriza��o, reproduzir, por qualquer meio, o texto daquela minha
    disserta��o para al�m dos limites fixados no C�digo do Direito de Autor e dos
    Direitos Conexos.
</p>

<p style="line-height: 1.9em;">
    Mais autorizo, com car�cter de n�o exclusividade, o <bean:message key="institution.name" bundle="GLOBAL_RESOURCES"/> a
    reproduzir, no todo ou em parte, aquela minha disserta��o para assim responder a
    pedidos que lhe sejam formulados, por parte de institui��es de ensino ou de
    investiga��o bem como por parte de Centros de Documenta��o ou de Bibliotecas, e
    desde que desses pedidos resulte que a reprodu��o solicitada da minha
    disserta��o apenas se destina a fins pedag�gicos ou de investiga��o.
</p>

</div>

<p>
    <html:submit property="accept">
        <bean:message key="button.thesis.declaration.accept"/>
    </html:submit>
    
    <html:submit property="reject">
        <bean:message key="button.thesis.declaration.reject"/>
    </html:submit>
</p>

</fr:form>
