<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<style>
<!--


#operations {
    margin:20px 0 20px;
}

.grey-box {
    max-width:340px;
    height:110px;
    display:block;
    margin:0 0 10px 0;
    padding:5px 20px 10px;
    float:left;
}

.grey-box,
.infoop7 {
    background:whiteSmoke !important;
    border:1px solid #ececec !important;
    border-radius:3px;
}

.first-box {
    margin-right:30px;
}
.micro-pagamentos .infoop7 .tstyle2 td,
.micro-pagamentos .infoop7 .tstyle2 th {
    background:transparent;
    border-bottom: 1px solid #ddd;
    border-top: 1px solid #ddd;
}


.montante input[type="text"] {
    font-size:18px;
}
.montante input[type="text"] {
    padding:4px;
    text-align:right;
}


.cf:before,
.cf:after {
    content:"";
    display:block;
}
.cf:after {
    clear:both;
}
.cf {
    zoom:1;
}
-->
</style>

<html:xhtml />

<h2><bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.header"/></h2>

<p>
	<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.description"/>
</p>

<div id="operations" class="cf"> 
	<div class="grey-box first-box"> 
		<jsp:include page="searchPersonForm.jsp"/>
	</div>

	<div class="grey-box"> 
		<h3>Consultar Movimentos</h3> 
		<p> 
			Movimentos de:
			<select> 
				<option selected="selected">Operador de reprografia</option> 
				<option>Cliente</option> 
				<option>Unidade</option> 
			</select> 
		</p> 
		<p>Cliente (IST ID): <input type="text"/> <input type="button" value="Pesquisar"  onClick="parent.location='reprografia_movimentos_01.html'"/></p> 
					<!--
					<p>Operador: <select><option>Joaquim da Silva Arimateia (ist167200)</option><option>Joaquim da Silva Arimateia (ist167200)</option></select></p>
					<p>Unidade: <select><option>Reprografia do Central .012</option><option>Reprografia do Central .012</option></select></p>
					--> 
	</div> 
</div> 

<logic:present name="people">
	<logic:notEmpty name="people">
		<table class="tstyle1" width="71%">
			<logic:iterate id="person" name="people">
				<tr>
					<th>
						<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="person" property="username"/></bean:define>
						<img width="60" height="60" src="<%= request.getContextPath() + url %>"/>
					</th>
					<td style="padding-left: 15px;">
						<strong>
							<bean:write name="person" property="name"/>
						</strong>
						(<bean:write name="person" property="username"/>)
						<br/>
						<html:link action="/operator.do?method=showPerson" paramId="personOid" paramName="person" paramProperty="externalId">
							<bean:message bundle="ACCOUNTING_RESOURCES" key="label.view.profile" />
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>
