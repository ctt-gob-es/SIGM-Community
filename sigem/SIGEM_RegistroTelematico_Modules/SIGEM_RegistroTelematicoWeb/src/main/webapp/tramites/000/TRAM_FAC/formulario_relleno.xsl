<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.docIdentidad" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.cifProveedorOrigen" select="'CIF del proveedor o NIF del ciudadano'"/>
	<xsl:variable name="lang.razonSocialOrigen" select="'Razón social'"/>
	<xsl:variable name="lang.nifRepresentanteOrigen" select="'NIF del representante o apoderado del certificado digital'"/>
	<xsl:variable name="lang.cifEntidadDestino" select="'CIF de la entidad'"/>
	<xsl:variable name="lang.razonSocialDestino" select="'Razón social'"/>
	<xsl:variable name="lang.asuntoSolicitud" select="'Asunto'"/>
	<xsl:variable name="lang.numFacturaSolicitud" select="'Nº Factura'"/>
	<xsl:variable name="lang.fechaFacturaSolicitud" select="'Fecha factura'"/>
	<xsl:variable name="lang.importe" select="'Importe'"/>
    
	<xsl:variable name="lang.origenTitle" select="'ORIGEN'"/>
	<xsl:variable name="lang.destinoTitle" select="'DESTINO'"/>
	<xsl:variable name="lang.solicitudTitle" select="'SOLICITUD'"/>
	
	<xsl:template match="/">
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.docIdentidad"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Documento_Identificacion/Numero"/>
				</label>
				<br/>
			</div>
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.nombre"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Nombre"/>
				</label>
				<br/>
			</div>
		<br/>
		<h1><xsl:value-of select="$lang.origenTitle"/></h1>
		<div id="origen">
		    <xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Origen/CIF_Proveedor">
			<div class="col">
			    <label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.cifProveedorOrigen"/>:	
			    </label>
			    <label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Origen/CIF_Proveedor"/>
			    </label>
			    <br/>
			</div>
		    </xsl:if>
		    <xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Origen/Razon_Social">
			<div class="col">
			    <label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.razonSocialOrigen"/>:	
			    </label>
			    <label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Origen/Razon_Social"/>
			    </label>
			    <br/>
			</div>
		    </xsl:if>
		    <xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Origen/NIF_Representante">
			<div class="col">
			    <label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.nifRepresentanteOrigen"/>:	
			    </label>
			    <label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Origen/NIF_Representante"/>
			    </label>
			    <br/>
			</div>
		    </xsl:if>
		</div>
		<br/>
		<h1><xsl:value-of select="$lang.destinoTitle"/></h1>
		<div id="origen">
		    <xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Destino/CIF_Entidad">
			<div class="col">
			    <label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.cifEntidadDestino"/>:	
			    </label>
			    <label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Destino/CIF_Entidad"/>
			    </label>
			    <br/>
			</div>
		    </xsl:if>
		    <xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Destino/Razon_Social">
			<div class="col">
			    <label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.razonSocialDestino"/>:	
			    </label>
			    <label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Destino/Razon_Social"/>
			    </label>
			    <br/>
			</div>
		    </xsl:if>
		</div>
		<br/>
		<h1><xsl:value-of select="$lang.solicitudTitle"/></h1>
		<div id="origen">
		    <xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Solicitud/Asunto">
			<div class="col">
			    <label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.asuntoSolicitud"/>:	
			    </label>
			    <label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Solicitud/Asunto"/>
			    </label>
			    <br/>
			</div>
		    </xsl:if>
		    <xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Solicitud/Num_Factura">
			<div class="col">
			    <label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.numFacturaSolicitud"/>:	
			    </label>
			    <label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Solicitud/Num_Factura"/>
			    </label>
			    <br/>
			</div>
		    </xsl:if>
		    <xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Solicitud/Fecha_Factura">
			<div class="col">
			    <label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.fechaFacturaSolicitud"/>:	
			    </label>
			    <label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Solicitud/Fecha_Factura"/>
			    </label>
			    <br/>
			</div>
		    </xsl:if>
				<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Solicitud/Importe">
			<div class="col">
			    <label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.importe"/>:	
			    </label>
			    <label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/RegistroFactura/Solicitud/Importe"/>
			    </label>
			    <br/>
			</div>
		    </xsl:if>
		</div>
		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_AUTOFIRMA_COMUN_RELLENO" />
	</xsl:template>
</xsl:stylesheet>