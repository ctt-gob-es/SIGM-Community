<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="formulario_relleno.xsl" />
<xsl:output encoding="ISO-8859-1" method="html"/>
    <xsl:variable name="lang.docIdentidad" select="'CAT-Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'CAT-Nombre'"/>
	<xsl:variable name="lang.cifProveedorOrigen" select="'CAT-CIF del proveedor o NIF del ciudadano'"/>
	<xsl:variable name="lang.razonSocialOrigen" select="'CAT-Razón social'"/>
	<xsl:variable name="lang.nifRepresentanteOrigen" select="'CAT-NIF del representante o apoderado del certificado digital'"/>
	<xsl:variable name="lang.cifEntidadDestino" select="'CAT-CIF de la entidad'"/>
	<xsl:variable name="lang.razonSocialDestino" select="'CAT-Razón social'"/>
	<xsl:variable name="lang.asuntoSolicitud" select="'CAT-Asunto'"/>
	<xsl:variable name="lang.numFacturaSolicitud" select="'CAT-Nº Factura'"/>
	<xsl:variable name="lang.fechaFacturaSolicitud" select="'CAT-Fecha factura'"/>
	<xsl:variable name="lang.importe" select="'CAT-Importe'"/>
    
    <xsl:variable name="lang.origenTitle" select="'CAT-ORIGEN'"/>
    <xsl:variable name="lang.destinoTitle" select="'CAT-DESTINO'"/>
    <xsl:variable name="lang.solicitudTitle" select="'CAT-SOLICITUD'"/>
</xsl:stylesheet>