<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<!-- alumni/inquiries/alumniInquiryMain.jsp -->

<em><bean:message key="label.portal.alumni" bundle="ALUMNI_RESOURCES" /></em>
<h2><bean:message key="inquiries.alumni.title" bundle="ALUMNI_RESOURCES" /></h2>


<p>
	H� mais de 15 anos que o IST acompanha com regularidade o percurso s�cio-profissional dos seus diplomados no �mbito dos processos de avalia��o e acredita��o de cada um dos seus cursos. Desde 1998, que este acompanhamento foi sistematizado a todos os cursos, estando neste momento a decorrer a IV edi��o do inqu�rito, lan�ado pelo Observat�rio de Empregabilidade do IST (OEIST) em estreita colabora��o com o projecto Alumni.
</p>
<p>
	A sua colabora��o � fundamental para a institui��o, dado que os requisitos de Bolonha atribuiram � empregabilidade um lugar de destaque e de grande relevo na avalia��o dos cursos. Por conseguinte, � muito importante para o IST compreender o percurso dos seus diplomados, de forma n�o s� a responder a estes requisitos mas tamb�m de modo a aproximar a escola do mercado de trabalho.
</p>
<p>
	Dada a rela��o entre o projecto Alumni e o OEIST, fica feito o convite ao preenchimento do inqu�rito (no link que se segue).
</p>


<p>
	<strong>
		<html:link page="/alumniInquiries.do?method=initInquiry">
			Preencher o inqu�rito �
		</html:link>
	</strong>
</p>

<%--
<p>
	<strong>
		<a target="_blank" href="http://groups.ist.utl.pt/gep/inqweb/index.php?sid=85246">
			<bean:message key="inquiries.alumni.link.two" bundle="ALUMNI_RESOURCES" />
		</a>
	</strong>
</p>
--%>

