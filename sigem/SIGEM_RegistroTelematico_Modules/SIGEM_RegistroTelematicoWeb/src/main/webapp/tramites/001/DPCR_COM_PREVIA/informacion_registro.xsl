<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
<xsl:include href="../TRAM_COMUNES/templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>
	
	<xsl:include href="../TRAM_COMUNES/lang_comunes_es.xsl" />
	
	<xsl:template match="/">
		<xsl:call-template name="INFORMACION_REGISTRO" />
	</xsl:template>
	
</xsl:stylesheet>