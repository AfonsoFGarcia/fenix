<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

<em>Gest�o de Canais</em>
<h2><bean:message bundle="MESSAGING_RESOURCES" key="messaging.annoucenment.add.label"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>
<bean:define id="extraParameters" name="extraParameters" />
<bean:define id="announcementBoardId" name="announcementBoard" property="idInternal"/>
<bean:define id="announcementBoard" name="announcementBoard"/>
<%							
	int indexOfLastSlash = contextPrefix.lastIndexOf("/");
	int indexOfDot = contextPrefix.lastIndexOf(".");
	String prefix = contextPrefix.substring(0,indexOfLastSlash+1);
	String suffix = contextPrefix.substring(indexOfDot,contextPrefix.length());
%>

<bean:define id="action"><%= "method=viewAnnouncementBoard&announcementBoardId=" + announcementBoardId + "&" + extraParameters %></bean:define>

<fr:form action="<%=  contextPrefix + action %>">

<%--
<p class="mtop2 mbottom025"><strong><bean:message key="label.requiredFields" bundle="MESSAGING_RESOURCES"/>:</strong></p>
--%>

<p class="mvert15 color888 smalltxt"><em><bean:message key="label.fieldsWith" bundle="SITE_RESOURCES"/><span class="required">*</span><bean:message key="label.areRequired" bundle="SITE_RESOURCES"/></em></p>


<table class="tstyle5 thlight thtop thright mtop025">

<%-- T�tulo --%>
	<tr>
		<th style="width: 125px;">
			<span class="required">*</span> <bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.title.label"/>:
		</th>
		<td>
			<fr:create id="announcement-subject-validated" type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="subject" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredMultiLanguageStringValidator">
				<fr:layout>
					<fr:property name="size" value="50" />
				</fr:layout>
			</fr:create>
		</td>
		<td class="tdclear">
			<span class="error0"><fr:message for="announcement-subject-validated"/></span>
		</td>
	</tr>
<%-- Corpo --%>	
	<tr>
		<th>
			<span class="required">*</span> <bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.body.label"/>:
		</th>
		<td>
			<fr:create id="announcement-body-validated" type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="body" 
					validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredMultiLanguageStringValidator">
				<fr:layout name="rich-text">
					<fr:property name="safe" value="true" />
			   		<fr:property name="columns" value="70"/>
			   		<fr:property name="rows" value="15"/>
			   		<fr:property name="config" value="advanced" />
				</fr:layout>
			</fr:create>
		</td>
		<td class="tdclear">
			<span class="error0"><fr:message for="announcement-body-validated"/></span>
		</td>
	</tr>
	<logic:notEmpty name="announcementBoard" property="files">
		<tr>
		<th>
			<bean:message key="link.insertFile" bundle="SITE_RESOURCES"/>
		</th>
		<td>
				<div style="height: 80px; overflow: auto; border: 1px solid #aaa; padding: 0.25em; background: #fafafa;">
				<logic:iterate id="file" name="announcementBoard" property="filesSortedByDate">
				
				
				<p><fr:view name="file" property="displayName"/>
				   <span class="color888">(<fr:view name="file" property="filename"/>)
				   <fr:view name="file" property="permittedGroup" layout="null-as-label" type="net.sourceforge.fenixedu.domain.accessControl.Group">
	                                        <fr:layout>
	                                            <fr:property name="label" value="<%= String.format("label.%s", net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup.class.getName()) %>"/>
	                                            <fr:property name="key" value="true"/>
	                                            <fr:property name="bundle" value="SITE_RESOURCES"/>
	                                            <fr:property name="subLayout" value="values"/>
	                                            <fr:property name="subSchema" value="permittedGroup.class.text"/>
	                                        </fr:layout>
	                                    </fr:view>
	                </span> - <bean:define id="downloadUrl" name="file" property="downloadUrl"/>
					<bean:define id="displayName" name="file" property="displayName"/>
                    <%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#" onclick="<%= "insertLink('" + downloadUrl + "', '"+ displayName + "');"%>"><bean:message key="link.insert.file.in.editor" bundle="SITE_RESOURCES"/></a>
                    </p>
			</logic:iterate>
			</div>
		</td>
		</tr>
	</logic:notEmpty>
</table>

<script type="text/javascript">
            function hasTinyMCE() {
                return tinyMCE.configs.length > 0;
            }
            
            function insertLink(url, name) {
                if (hasTinyMCE()) {
                    tinyMCE.execCommand('mceInsertContent', false, '<a href="' + url + '">' + name + '</a> ');
                }
            }
    
            if (hasTinyMCE()) {
                switchGlobal();
            }
            </script>



<p class="mtop2 mbottom025"><strong>Campos opcionais:</strong></p>
<table class="tstyle5 thlight thtop thright mtop025">
<%-- Excerto --%>
	<tr>
		<th style="width: 125px;">
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.excerpt.label"/>:
		</th>
		<td>
			<fr:create type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="excerpt" layout="area">
				<fr:layout>
					<fr:property name="rows" value="3"/>
					<fr:property name="columns" value="70"/>
				</fr:layout>
			</fr:create>
		</td>
	</tr>


<%-- Palavras-Chave --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.keywords.label"/>:
		</th>
		<td>
			<fr:create type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="keywords">
				<fr:layout>
					<fr:property name="size" value="30"/>
				</fr:layout>
			</fr:create>
		</td>
	</tr>
	
<%-- Nome do autor --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.authorName.label"/>:
		</th>
		<td>
			<fr:create type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="author">
				<fr:layout>			
					<fr:property name="size" value="40"/>
				</fr:layout>
			</fr:create>
		</td>
	</tr>

<%-- E-mail do autor --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.authorEmail.label"/>:
		</th>
		<td>
			<fr:create type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="authorEmail">
				<fr:layout>
					<fr:property name="size" value="40"/>
				</fr:layout>
			</fr:create>
		</td>
	</tr>

<%-- In�cio do Evento --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.referedSubjectBegin.label"/>:
		</th>
		<td>
			<fr:create id="referedSubjectBegin-validated" type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="referedSubjectBegin" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.DateTimeValidator"/>
			 <span class="error0"><fr:message for="referedSubjectBegin-validated"/></span>
		</td>
	</tr>

<%-- Fim do Evento --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.referedSubjectEnd.label"/>:
		</th>
		<td>
			<fr:create id="referedSubjectEnd-validated" type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="referedSubjectEnd" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.DateTimeValidator"/>
			<span class="error0"><fr:message for="referedSubjectEnd-validated"/></span>
		</td>
	</tr>

<%-- Local --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.place.label"/>:
		</th>
		<td>
			<fr:create type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="place">
				<fr:layout>
					<fr:property name="size" value="30"/>
				</fr:layout>
			</fr:create>
		</td>
	</tr>



<%-- An�ncio dispon�vel apartir... --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.publicationBegin.label"/>:
		</th>
		<td>
			<fr:create id="publicationBegin-validated" type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="publicationBegin" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.DateTimeValidator"/>
			<span class="error0"><fr:message for="publicationBegin-validated"/></span>
		</td>
	</tr>

<%-- An�ncio dispon�vel at�... --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.publicationEnd.label"/>:
		</th>
		<td>
			<fr:create id="publicationEnd-validated" type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="publicationEnd" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.DateTimeValidator"/>
			<span class="error0"><fr:message for="publicationEnd-validated"/></span>
		</td>
	</tr>

<%-- Vis�vel --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.visible.label"/>:
		</th>
		<td>
			<fr:create type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="visible">
				<fr:default value="true" slot="visible"/>
			</fr:create>
		</td>
	</tr>
</table>

	
	<p>
	<fr:create type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="creator">
		<fr:hidden name="person"/>
	</fr:create>
	</p>

	<p>
	<fr:create type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="announcementBoard">
		<fr:hidden name="announcementBoard"/>
	</fr:create>			
	</p>


	<p class="mtop1">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.save.button"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.cancel.button"/></html:cancel>
	</p>


</fr:form>
