<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>

<html:xhtml />
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>


<em><bean:message key="label.person.main.title" /></em>

<logic:notEmpty name="authorizations">
<h2>
	<bean:message key="oauthapps.label.app.details" bundle="APPLICATION_RESOURCES" />
</h2>

<h2></h2>

<fr:view name="application" layout="tabular" schema="oauthapps.view.app.details" type="net.sourceforge.fenixedu.domain.ExternalApplication">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright"/>
	</fr:layout>
</fr:view>


<h2>
	<bean:message key="oauthapps.label.manage.authorizations" bundle="APPLICATION_RESOURCES" />
</h2>



		<fr:view name="authorizations" schema="oauthapps.view.authorizations">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thcenter thcenter"/>
				<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, tdcenter, tdcenter"/>
				
				<fr:property name="linkFormat(revokeAuthorization)" value="<%= "/externalApps.do?method=revokeAuth&authorizationOid=${externalId}" %>" />
				<fr:property name="key(revokeAuthorization)" value="oauthapps.label.revoke.authorization"/>
				<fr:property name="bundle(revokeAuthorization)" value="APPLICATION_RESOURCES"/>
			</fr:layout>
		</fr:view>


<p>
	<html:link page="/externalApps.do?method=revokeApplication" paramId="appOid" paramName="application" paramProperty="externalId">
		<bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.revoke.all.authorizations"/>
	</html:link>
</p>
	
<script type="text/javascript">
		$("table img").width("75px").height("75px");
		$("a[href*=revokeAuth]").click(function(e) {
			   answer = confirm('Deseja mesmo revogar ?');
			   return answer;
			});
		$("a[href*=revokeApplication]").click(function(e) {
			   answer = confirm('Ao revogar a aplicação irá apagar todas as autorizações concedidas à mesma. Deseja continuar ?');
			   return answer;
			});
</script>

</logic:notEmpty>
	