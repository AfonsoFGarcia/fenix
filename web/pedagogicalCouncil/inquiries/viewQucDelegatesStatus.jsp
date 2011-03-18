<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<h2>QUC - Garantia da Qualidade das UC</h2>

<br/>
<h4>Lista de Delegados com quest�es obrigat�rias por responder:</h4>
<logic:present name="delegateInquiryOID">	
	<p><html:link action="qucDelegatesStatus.do?method=dowloadReport" paramId="delegateInquiryOID" paramName="delegateInquiryOID">Ver ficheiro</html:link></p>
</logic:present>

<logic:notPresent name="delegateInquiryOID">
	<p>O inqu�rito ao Delegado encontra-se fechado.</p>
</logic:notPresent>