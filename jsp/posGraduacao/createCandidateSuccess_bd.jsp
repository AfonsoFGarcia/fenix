<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

    <div align="center">
     <font color="#023264" size="-1">
        <h2>          Candidato Criado !         </h2>
      </font>
    </div>
    <table>
    <bean:define id="newCandidate" name="<%= SessionConstants.NEW_MASTER_DEGREE_CANDIDATE %>" scope="request" />
      <logic:present name="newCandidate">
          <!-- Name -->
          <tr>
            <td><bean:message key="label.candidate.name" /></td>
            <td><bean:write name="newCandidate" property="infoPerson.nome"/></td>
          </tr>

          <!-- Candidate Number -->
          <tr>
            <td><bean:message key="label.candidate.candidateNumber" /></td>
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
                <bean:write name="newCandidate" property="infoExecutionDegree.infoDegreeCurricularPlan.name"/>
            </td>
          </tr>

          <!-- Execution Year  
          <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.executionYear" /></td>
            <td><bean:write name="newCandidate" property="infoExecutionDegree.infoExecutionYear.year"/></td>
          </tr>
			-- >
          <!-- Identification Document Number -->
          <tr>
            <td><bean:message key="label.candidate.identificationDocumentNumber" /></td>
            <td><bean:write name="newCandidate" property="infoPerson.numeroDocumentoIdentificacao"/></td>
          </tr>

          <!-- Specialization -->
          <tr>
            <td><bean:message key="label.candidate.identificationDocumentType" /></td>
            <td>
            	<bean:define id="idType" name="newCandidate" property="infoPerson.tipoDocumentoIdentificacao"/>
            	<bean:message key='<%=idType.toString()%>'/>
            </td>
          </tr>

      </logic:present>
    </table>

