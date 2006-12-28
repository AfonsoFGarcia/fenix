<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="label.executionCourseManagement.menu.sections"/>
</h2>

<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>

<p>
	<span>
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="/manageExecutionCourse.do?method=createSection" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message key="link.createSection"/>
		</html:link>
	</span>
	
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="/manageExecutionCourse.do?method=prepareImportSections" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message key="label.import.sections"/>
		</html:link>
	</span>
</p>

<logic:empty name="executionCourse" property="site.orderedTopLevelSections">
    <p>
        <span class="warning0">
            <bean:message key="message.sections.empty" bundle="SITE_RESOURCES"/>
        </span>
    </p>
</logic:empty>

<logic:notEmpty name="executionCourse" property="site.orderedTopLevelSections">
    <fr:form action="<%= "/manageExecutionCourse.do?method=saveSectionsOrder&amp;executionCourseID=" + executionCourseId %>">
        <input alt="input.sectionsOrder" id="sections-order" type="hidden" name="sectionsOrder" value=""/>
    </fr:form>
    
    <% String treeId = "sectionsTree" + executionCourseId; %>
    
    <div class="section1">
        <fr:view name="executionCourse" property="site.orderedTopLevelSections">
            <fr:layout name="tree">
                <fr:property name="treeId" value="<%= treeId %>"/>
                <fr:property name="fieldId" value="sections-order"/>
                
                <fr:property name="eachLayout" value="values"/>
                <fr:property name="schemaFor(Section)" value="site.section.name"/>
                <fr:property name="childrenFor(Section)" value="orderedSubSections"/>
            </fr:layout>
            <fr:destination name="section.view" path="<%= "/manageExecutionCourse.do?method=section&amp;sectionID=${idInternal}&amp;executionCourseID=" + executionCourseId %>"/>
        </fr:view>
	
		<p class="mtop15">
	    <fr:form action="<%= "/manageExecutionCourse.do?method=sections&amp;executionCourseID=" + executionCourseId %>">
	        <html:button bundle="HTMLALT_RESOURCES" altKey="button.saveButton" property="saveButton" onclick="<%= "treeRenderer_saveTree('" + treeId + "');" %>">
	            <bean:message key="button.sections.order.save" bundle="SITE_RESOURCES"/>
	        </html:button>
	        <html:submit>
	            <bean:message key="button.sections.order.reset" bundle="SITE_RESOURCES"/>
	        </html:submit>
	    </fr:form>
	    </p>
    </div>
    
<p style="color: #888;">
	<em><bean:message key="message.section.reorder.tip" bundle="SITE_RESOURCES"/></em>
</p>

</logic:notEmpty>
