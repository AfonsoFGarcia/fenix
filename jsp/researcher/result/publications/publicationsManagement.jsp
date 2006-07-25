<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<style>
		table.search {
		background-color: #f5f5f5;
		border-collapse: collapse;
		}
		table.search tr td {
		border: 1px solid #fff;
		padding: 0.3em;
		}
		.leftcolumn {
		text-align: right;
		}
		
		h3.cd_heading {
		font-weight: normal;
		margin-top: 3em;
		border-top: 1px solid #e5e5e5;
		background-color: #fafafa;
		padding: 0.25em 0 0em 0.25em;
		padding: 0.5em 0.25em;
		}
		h3.cd_heading span {
		margin-top: 2em;
		border-bottom: 2px solid #fda;
		}
		
		div.cd_block {
		background-color: #fed;
		padding: 0.5em 0.5em 0.5em 0.5em;
		}
		
		table.cd {
		border-collapse: collapse;
		}
		table.cd th {
		border: 1px solid #ccc;
		background-color: #eee;
		padding: 0.5em;
		text-align: center;
		}
		table.cd td {
		border: 1px solid #ccc;
		background-color: #fff;
		padding: 0.5em;
		text-align: center;
		}
		
		p.insert {
		padding-left: 2em;
		}
		div.cd_float {
		width: 100%;
		float: left;
		padding: 0 2.5em;
		padding-bottom: 1em;
		}
		ul.cd_block {
		width: 43%;
		list-style: none;
		float: left; 
		margin: 0;
		padding: 0;
		padding: 1em;
		}
		ul.cd_block li {
		}
		ul.cd_nostyle {
		list-style: none;
		}
</style>

<logic:present role="RESEARCHER">
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsManagement"/></h2>

	<html:link page="/publications/publicationsManagement.do?method=prepareCreatePublication"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsManagement.insertPublication" /></html:link>
	
	<logic:notEmpty name="books">
		<h3 id='books' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.books"/> </span> </h3>
		<logic:iterate id="book" name="books">
			<fr:view name="book" layout="tabular" schema="result.publication.resume.Book"/>
	 		<bean:define id="bookId" name="book" property="idInternal"/>
	 		<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ bookId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
	 		<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ bookId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
	  		<br /><br />
		</logic:iterate>
	</logic:notEmpty>

	<logic:notEmpty name="bookParts">
		<h3 id='booksParts' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.bookParts"/> </span> </h3>
		<logic:iterate id="bookPart" name="bookParts">
			<fr:view name="bookPart" layout="tabular" schema="result.publication.resume.BookPart"/>
	 		<bean:define id="bookPartId" name="bookPart" property="idInternal"/>
	 		<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ bookPartId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
	 		<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ bookPartId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
	  		<br /><br />
		</logic:iterate>
	</logic:notEmpty>

	<logic:notEmpty name="articles">
		<h3 id='articles' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.articles"/> </span> </h3>
		<logic:iterate id="article" name="articles">
			<fr:view name="article" layout="tabular" schema="result.publication.resume.Article"/>
			<bean:define id="articleId" name="article" property="idInternal"/>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ articleId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ articleId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:notEmpty name="theses">
		<h3 id='theses' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.theses"/> </span> </h3>
		<logic:iterate id="thesis" name="theses">
			<fr:view name="thesis" layout="tabular" schema="result.publication.resume.Thesis"/>
			<bean:define id="thesisId" name="thesis" property="idInternal"/>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ thesisId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ thesisId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:notEmpty name="manuals">
		<h3 id='manuals' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.manuals"/> </span> </h3>
		<logic:iterate id="manual" name="manuals">
			<fr:view name="manual" layout="tabular" schema="result.publication.resume.Manual"/>
			<bean:define id="manualId" name="manual" property="idInternal"/>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ manualId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ manualId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:notEmpty name="technicalReports">
		<h3 id='technicalReports' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.technicalReports"/> </span> </h3>
		<logic:iterate id="technicalReport" name="technicalReports">
			<fr:view name="technicalReport" layout="tabular" schema="result.publication.resume.TechnicalReport"/>
			<bean:define id="technicalReportId" name="technicalReport" property="idInternal"/>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ technicalReportId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ technicalReportId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:notEmpty name="booklets">
		<h3 id='booklets' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.booklets"/> </span> </h3>
		<logic:iterate id="booklet" name="booklets">
			<fr:view name="booklet" layout="tabular" schema="result.publication.resume.Booklet"/>
			<bean:define id="bookletId" name="booklet" property="idInternal"/>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ bookletId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ bookletId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:notEmpty name="miscs">
		<h3 id='miscs' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.miscs"/> </span> </h3>
		<logic:iterate id="misc" name="miscs">
			<fr:view name="misc" layout="tabular" schema="result.publication.resume.Misc"/>
			<bean:define id="miscId" name="misc" property="idInternal"/>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ miscId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ miscId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:notEmpty name="unpublisheds">
		<h3 id='unpublisheds' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.unpublisheds"/> </span> </h3>
		<logic:iterate id="unpublished" name="unpublisheds">
			<fr:view name="unpublished" layout="tabular" schema="result.publication.resume.Unpublished"/>
			<bean:define id="unpublishedId" name="unpublished" property="idInternal"/>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ unpublishedId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ unpublishedId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>

    <%--   
		<logic:iterate id="publication" name="publications">
			<bean:define id="resultType" name="publication" property="publicationType" type="net.sourceforge.fenixedu.domain.research.result.publication.PublicationType"/>
		  	<u><bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.result.publication.publicationType."+resultType.toString().toLowerCase() %>"/></u>
 			<fr:view name="publication" layout="tabular" schema="<%="result.publication.resume."+resultType.toString().toLowerCase() %>"/>
  			<br /><hr />
		</logic:iterate>
	--%>

	<br />	<br />
	<html:link page="/publications/publicationsManagement.do?method=prepareCreatePublication"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsManagement.insertPublication" /></html:link>
</logic:present>
