<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:include href="templates_comunes_rellenos.xsl" />	
	<xsl:template match="/">
		<xsl:call-template name="DATOS_SOLICITANTE" />
		<xsl:call-template name="DATOS_BENEFICIARIO" />
		<xsl:call-template name="DATOS_FAMILIA" />
		<xsl:call-template name="DATOS_PROPUESTA_1" />
		<!--<xsl:call-template name="DATOS_FAMILIA2" />-->
		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_AUTOFIRMA_COMUN_RELLENO" />

		<div class="col">
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:600px;</xsl:attribute>
				<b>Documentación que se aporta: </b>
			</label>
			<label class="gr" style="position: relative; width:600px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/documentos"/>				
			</label>
		</div>
	</xsl:template>
</xsl:stylesheet>
