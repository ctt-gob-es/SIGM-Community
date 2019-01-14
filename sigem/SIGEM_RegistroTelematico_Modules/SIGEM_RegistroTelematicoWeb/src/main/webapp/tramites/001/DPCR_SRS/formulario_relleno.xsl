<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>

	<xsl:variable name="lang.organoAsignado" select="'Órgano al que se dirige: Registro General de la Entidad Local.'"/>
	
	<xsl:template match="/">

		<xsl:call-template name="DATOS_PRESENTADOR_RELLENO" />
		<br/>
		<xsl:call-template name="DATOS_SOLICITANTE_REPRESENTANTE_RELLENO" />
		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/expone">
				<div class="col" style="height: 35px;">
					<label class="gr">
						<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
						<xsl:value-of select="$lang.expone"/>:	
					</label>
					<label class="gr">
						<xsl:attribute name="style">position: relative;  width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/expone"/>
					</label>
					<br/>
				</div>
			</xsl:if>
			<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/solicita">
				<div class="col" style="height: 35px;">
					<label class="gr">
						<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
						<xsl:value-of select="$lang.solicita"/>:	
					</label>
					<label class="gr">
						<xsl:attribute name="style">position: relative;  width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/solicita"/>
					</label>
					<br/>
				</div>
			</xsl:if>
		</div>
		<br/>
		<div class="submenu">
			<h1><xsl:value-of select="$lang.organoAsignado"/></h1>
		</div>   		
   		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_AUTOFIRMA_COMUN_RELLENO" />
	</xsl:template>
</xsl:stylesheet>