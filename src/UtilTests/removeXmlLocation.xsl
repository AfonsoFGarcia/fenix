<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:param name="xmlDocument"/>

   <xsl:template match="lom/ist/members/location[.=$xmlDocument]">
   </xsl:template>
   
    <xsl:template match="/|*">	
	<xsl:copy>
	  <xsl:for-each select="@*">
	    <xsl:copy><xsl:value-of select="."/></xsl:copy>
	  </xsl:for-each>
	  <xsl:apply-templates select="node()"/>
	</xsl:copy>
	</xsl:template>

</xsl:stylesheet>