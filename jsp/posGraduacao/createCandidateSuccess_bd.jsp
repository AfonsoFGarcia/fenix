<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
  <head>
    <title><bean:message key="title.candidate.visualizeApplicationInfo" /></title>
  </head>
  <body>
    <div align="center">
     <font color="#023264" size="-1">
        <h2>          Candidato Criado !         </h2>
      </font>
    </div>
    <table>
      <logic:present name="newCandidate">
          <!-- Name -->
          <tr>
            <td><bean:message key="label.candidate.name" /></td>
            <td><bean:write name="newCandidate" property="infoPerson.nome"/></td>
          </tr>

          <!-- Candidate Number -->
          <tr>
            <td><bean:message key="label.candidate.number" /></td>
            <td><bean:write name="newCandidate" property="candidateNumber"/></td>
          </tr>

          <!-- Specialization -->
          <tr>
            <td><bean:message key="label.candidate.specialization" /></td>
            <td><bean:write name="newCandidate" property="specialization"/></td>
          </tr>

          <!-- Degree  -->
          <tr>
            <td><bean:message key="label.candidate.degree" /></td>
            <td><bean:write name="newCandidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> - 
                <bean:write name="newCandidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla"/>
            </td>
          </tr>

          <!-- Identification Document Number -->
          <tr>
            <td><bean:message key="label.candidate.identificationDocumentNumber" /></td>
            <td><bean:write name="newCandidate" property="infoPerson.numeroDocumentoIdentificacao"/></td>
          </tr>

          <!-- Specialization -->
          <tr>
            <td><bean:message key="label.candidate.identificationDocumentType" /></td>
            <td><bean:write name="newCandidate" property="infoPerson.tipoDocumentoIdentificacao"/></td>
          </tr>

      </logic:present>
    </table>
  </body>
</html>

