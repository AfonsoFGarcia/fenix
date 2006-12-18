<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>



		<bean:define id="currentSchema" name="currentSchema" scope="request" type="java.lang.String"/>
		<ul style="width: 600px;">
		<logic:iterate id="result" name="results" scope="request">
 			<bean:define id="resultId" name="result" property="idInternal"/>
				<li class="mtop1">
		 			<fr:view name="result" layout="nonNullValues" schema="<%=currentSchema%>">
		 				<fr:layout>
		 					<fr:property name="htmlSeparator" value=", "/>
		 					<fr:property name="indentation" value="false"/>
		 				</fr:layout>
		 			</fr:view> (<html:link target="_blank" page="<%="/publications/bibtexManagement.do?method=exportPublicationToBibtex&publicationId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.exportToBibTeX" /></html:link>)
		 			<p class="mtop025">
			 		<html:link page="<%="/resultPublications/prepareEdit.do?resultId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link" /></html:link>, 
		 			<html:link page="<%="/resultPublications/prepareDelete.do?&resultId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.delete" /></html:link> 
			 		</p>
	 			</li>
		</logic:iterate>
		</ul>
