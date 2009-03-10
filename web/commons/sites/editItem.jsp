<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2>
	<bean:message key="title.item.edit" bundle="SITE_RESOURCES"/>
</h2>

<logic:messagesPresent message="true">
    <div class="mvert15">
        <span class="error0">
            <html:messages id="error" message="true" bundle="SITE_RESOURCES"> 
                <bean:write name="error"/>
            </html:messages>
        </span>
    </div>
</logic:messagesPresent>


<bean:define id="siteId" name="site" property="idInternal"/>
<bean:define id="sectionId" name="item" property="section.idInternal"/>

<fr:form action="<%= String.format("%s?method=section&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
    <fr:edit id="edit-item" name="item" type="net.sourceforge.fenixedu.domain.Item" schema="net.sourceforge.fenixedu.domain.ItemEditor">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
    </fr:edit>
    
    <logic:notEmpty name="item" property="fileItems">
        <h3 class="mbottom05">
            <bean:message key="title.item.files" bundle="SITE_RESOURCES"/>
        </h3>
        
        <div class="infoop2 mtop05 mbottom1">
        	<bean:message key="label.item.edit.add.file.instructions" bundle="SITE_RESOURCES"/>
        </div>
    
        <fr:edit id="edit-files" name="item" property="sortedAttachmentNodes" schema="site.item.files.visible">
            <fr:layout name="tabular-editable">
	            <fr:property name="classes" value="tstyle5 thlight mvert0"/>
	            <fr:property name="columnClasses" value="acenter"/>
                <fr:property name="customLink(add)">
                    <span class="switchInline">
                        (<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#" onclick="insertLink('${child.file.downloadUrl}', '${child.file.displayName}');"><bean:message key="link.item.edit.add.file" bundle="SITE_RESOURCES"/></a>)
                    </span>
                </fr:property>
            </fr:layout>
        </fr:edit>
        
		
		<%= ContentInjectionRewriter.BLOCK_HAS_CONTEXT_PREFIX %>
        <script type="text/javascript">
            function hasTinyMCE() {
                return tinyMCE.settings != null;
            }
            
            function insertLink(url, name) {
                if (hasTinyMCE()) {
                    tinyMCE.execCommand('mceInsertContent', false, '<a href="' + url + '">' + name + '</a>');
                }
            }
    
            if (hasTinyMCE()) {
                switchGlobal();
            }
        </script>
        <%= ContentInjectionRewriter.END_BLOCK_HAS_CONTEXT_PREFIX %>
 
    </logic:notEmpty>

    <p class="mtop15">
	    <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.editItemButton" property="editItemButton">
	        <bean:message key="button.item.edit.submit" bundle="SITE_RESOURCES"/>
	    </html:submit>
	
	    <html:cancel>
	        <bean:message key="button.item.edit.cancel" bundle="SITE_RESOURCES"/>
	    </html:cancel>
    </p>
</fr:form>
