<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col_photo.jsp" flush="true">
  <tiles:put name="title" value=".IST - SOP" />
  <tiles:put name="serviceName" value="SOP - Servi�o de Organiza��o Pedag�gica" />
  <tiles:put name="navGeral" value="/sop/commonNavGeralSopExecutionCourse.jsp" />
  <tiles:put name="photos" value="/sop/commonEntrPhotosSop.jsp" />
  <tiles:put name="body-context" value="" />  
  <tiles:put name="body" value="/sop/searchExecutionCourse_bd.jsp" />
  <tiles:put name="footer" value="/sop/commonFooterSop.jsp" />
</tiles:insert>