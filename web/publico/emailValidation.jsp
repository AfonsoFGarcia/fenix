<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

	<h1>Valida��o de Email</h1>
	
	<logic:equal name="state" value="VALID">
		O seu email foi validado com sucesso!
	</logic:equal>			
	<logic:equal name="state" value="INVALID">
		N�o foi poss�vel validar o seu email. Verifique o link que lhe foi enviado. 
		Disp�e de <bean:write name="tries"/> tentativas.
	</logic:equal>
	<logic:equal name="state" value="REFUSED">
		O seu pedido de valida��o foi recusado. Execedeu o n�mero de tentativas poss�veis.
	</logic:equal>
