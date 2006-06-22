<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<div id="version">
	<html:form action="/changeLocaleTo.do">
		<html:hidden property="windowLocation" value=""/>
		<html:hidden property="newCountry" value="<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("language") %>"/>
		<html:hidden property="newCountry" value="<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("location") %>"/>
		<html:hidden property="newVariant" value="<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>"/>

		<logic:notEqual name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
			<INPUT type="image"
					src="<%= request.getContextPath() %>/images/flags/pt.gif"
					alt="Portugu�s"
					title="Portugu�s"
					value="PT"
					onclick="this.form.newLanguage.value='pt';this.form.newCountry.value='PT';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();" />
			<INPUT class="activeflag"
					type="image" src="<%= request.getContextPath() %>/images/flags/en.gif"
					alt="English"
					title="English"
					value="EN" 
					onclick="this.form.newLanguage.value='en';this.form.newCountry.value='EN';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();"/>
		</logic:notEqual>
					
		<logic:notEqual name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">			
			<INPUT class="activeflag"
					type="image" src="<%= request.getContextPath() %>/images/flags/pt.gif"
					alt="Portugu�s"
					title="Portugu�s"
					value="PT"
					onclick="this.form.newLanguage.value='pt';this.form.newCountry.value='PT';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();" />
			<INPUT type="image" src="<%= request.getContextPath() %>/images/flags/en.gif"
					alt="English"
					title="English"
					value="EN" 
					onclick="this.form.newLanguage.value='en';this.form.newCountry.value='EN';this.form.newInstitution.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();"/>
		</logic:notEqual>
	</html:form>
</div>