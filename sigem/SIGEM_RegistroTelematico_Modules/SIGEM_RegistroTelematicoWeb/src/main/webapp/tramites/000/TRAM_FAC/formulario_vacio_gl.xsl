<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_vacio.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.docIdentidad" select="'GAL-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'GAL-Nombre'"/>
	<xsl:variable name="lang.cifProveedorOrigen" select="'GAL-CIF del proveedor o NIF del ciudadano'"/>
	<xsl:variable name="lang.razonSocialOrigen" select="'GAL-Razón social'"/>
	<xsl:variable name="lang.nifRepresentanteOrigen" select="'GAL-NIF del representante o apoderado del certificado digital'"/>
	<xsl:variable name="lang.cifEntidadDestino" select="'GAL-CIF de la entidad'"/>
	<xsl:variable name="lang.razonSocialDestino" select="'GAL-Razón social'"/>
	<xsl:variable name="lang.asuntoSolicitud" select="'GAL-Asunto'"/>
	<xsl:variable name="lang.numFacturaSolicitud" select="'GAL-Nº Factura'"/>
	<xsl:variable name="lang.fechaFacturaSolicitud" select="'GAL-Fecha factura'"/>
	<xsl:variable name="lang.importe" select="'GAL-Importe'"/>
    
    <xsl:variable name="lang.origenTitle" select="'GAL-ORIGEN'"/>
    <xsl:variable name="lang.destinoTitle" select="'GAL-DESTINO'"/>
    <xsl:variable name="lang.solicitudTitle" select="'GAL-SOLICITUD'"/>
	<xsl:variable name="lang.datosFacturaTitle" select="'GAL-DATOS FACTURA'"/>
</xsl:stylesheet>