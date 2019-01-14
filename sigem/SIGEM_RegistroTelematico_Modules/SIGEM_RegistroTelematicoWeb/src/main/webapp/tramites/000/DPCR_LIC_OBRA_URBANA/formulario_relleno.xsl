<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../TRAM_COMUNES/templates_comunes.xsl" />

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:include href="../TRAM_COMUNES/lang_comunes_es.xsl" />

	<xsl:variable name="lang.finalidad" select="'Finalidad de las Obras'"/>
	
	<xsl:template match="/">

		<xsl:call-template name="DATOS_SOLICITUD_RELLENOS" />
		<br/>
		
		<div class="col">
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.datosSolicitud"/></b>	
			</label>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.finalidad"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Finalidad"/>
			</label>
			<br/>
		</div>
		
		<br/><br/>
		
		<xsl:call-template name="TEXTO_LEGAL_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_AUTOFIRMA_COMUN_RELLENO" />
		<br/>

	</xsl:template>

</xsl:stylesheet>
