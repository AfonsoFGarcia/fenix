<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<html:xhtml/>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1>Licenciaturas</h1>

<p>O ingresso nas licenciaturas do IST poder� ser efectuado atrav�s dos seguintes mecanismos de acesso:</p>

<ul>
	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/lic/concurso_nacional_acesso" %>">Concurso Nacional de Acesso</a></li>
	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/html/perfil/candidato/lic/mud-transf-curso.shtml">Mudan�as de Curso e Transfer�ncias</a></li>
	<li>Concurso Especial de Acesso
		<ul style="margin-top: 0.5em;">
			<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/html/perfil/candidato/lic/curso-medio.shtml">Titulares de Cursos M�dios e Superiores</a></li>
			<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= request.getContextPath() + "/candidaturas/lic/maiores_vinte_tres" %>'>Maiores de 23 anos</a></li>
		</ul>
	</li>
</ul>
