<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<!-- NOTA: N�o foram incluidas tags do beans tipo <bean:message key="title.listClasses"/> -->
<p><strong>&raquo; Gest&atilde;o de Turmas</strong></p>
<ul>
  <li><html:link page="/ClassManagerDA.do?method=createClass">Criar turma</html:link></li>
  <li><html:link page="/ClassesManagerDA.do?method=listClasses">Listar turmas</html:link></li>
</ul>
<p><strong>&raquo; Gest&atilde;o de Turnos</strong></p>
<ul>
  <li><html:link page="/prepararCriarTurno.do">Criar turno</html:link></li>
<%--  <li><html:link page="/apagarTurno.do">Apagar turno</html:link></li> --%>
  <li><html:link page="/escolherDisciplinaETipoForm.do">Listar turnos por disciplina</html:link></li>
</ul>
<p><strong>&raquo; Gest&atilde;o de Aulas</strong></p>
<ul>
  <li><html:link page="/prepararAula.do">Criar aulas</html:link></li>
  <li><html:link page="/escolherDisciplinaExecucaoForm.do">Alterar aulas</html:link></li>
</ul>

