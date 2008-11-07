<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<logic:present name="candidacy">

<h2><bean:message key="label.internship.candidacy.title" bundle="INTERNSHIP_RESOURCES" /></h2>

	<p><strong>Processo conclu�do com sucesso.</strong></p>

	<p>Caro(a) <bean:write name="candidacy" property="name" />,</p>

	<p>a sua candidatura foi submetida com sucesso. Foi-lhe atribu�do o c�digo de inscri��o n� <strong
		class="highlight1"><bean:write name="candidacyNumber" /></strong>, que dever� utilizar em
	contactos futuros. Esta mensagem foi-lhe tamb�m enviada para o email que preencheu na candidatura.</p>

</logic:present>