<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h1 class="mtop0 mbottom03 cnone"><fr:view name="site" property="unit.name"/></h1>

<h2 class="mtop15"><bean:message key="label.messaging.events.title" bundle="MESSAGING_RESOURCES"/></h2>

<bean:define id="action" name="announcementActionVariable" toScope="request"/>

<logic:iterate id="announcement" name="announcements">
<bean:define id="announcementID" name="announcement" property="idInternal"/>

<div class="announcement mtop15 mbottom25">

<h3 class="mvert025">
	<html:link page="<%= action + "?method=viewEvent&amp;siteID=" + request.getParameter("siteID") + "&amp;announcementId=" + announcementID %>">
		<fr:view name="announcement" property="subject"/>
	</html:link>
</h3>


<p class="mvert025 smalltxt greytxt2">
	<img src="<%= request.getContextPath() + "/images/dotist_post.gif"%>"/>
	De
	<fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="no-time" />
	<logic:present name="announcement" property="referedSubjectEnd">
		<bean:message key="label.listAnnouncements.event.occurs.to" bundle="MESSAGING_RESOURCES"/>
		<fr:view name="announcement" property="referedSubjectEnd" type="org.joda.time.DateTime" layout="no-time" />
	</logic:present>
</p>

				
	<div class="usitebody mvert025">
		<fr:view name="announcement" property="body" layout="html"/> 
	</div>

	<p>
		<em class="color888 smalltxt">
			<bean:message key="label.messaging.author" bundle="MESSAGING_RESOURCES"/>: 
			<bean:define id="userName" name="announcement" property="creator.username"/>
			<html:link target="blank" href="<%= request.getContextPath() + "/homepage/" + userName %>"><fr:view name="announcement" property="creator.nickname"/></html:link>
		</em>
	</p>

</logic:iterate>