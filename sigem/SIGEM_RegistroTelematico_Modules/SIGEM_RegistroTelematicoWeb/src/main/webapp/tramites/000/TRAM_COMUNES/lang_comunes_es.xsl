<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>
	
	<xsl:variable name="lang.datosSolicitante" select="'Datos del Solicitante'"/>		
	<xsl:variable name="lang.datosInteresado" select="'Datos del Interesado'"/>
	<xsl:variable name="lang.datosRepresentante" select="'Datos del Representante (opcional)'"/>
	<xsl:variable name="lang.interesado" select="'Interesado'"/>
	<xsl:variable name="lang.representante" select="'Representante (opcional)'"/>
	<xsl:variable name="lang.nif" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre" select="'Apellidos y Nombre o Denominación Social'"/>
	<xsl:variable name="lang.calle" select="'Domicilio'"/>
	<xsl:variable name="lang.direccion" select="'Dirección'"/>
	<xsl:variable name="lang.c_postal" select="'Código Postal'"/>
	<xsl:variable name="lang.ciudad" select="'Ciudad'"/>
	<xsl:variable name="lang.region" select="'Región / País'"/>
	<xsl:variable name="lang.movil" select="'Número de teléfono móvil'"/>
	<xsl:variable name="lang.d_email" select="'Dirección de correo electrónico'"/>

	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.anexarInfo3" select="'3.- Cuando se actúe por representación es obligatorio anexar el documento que acredite la representación (escritura notarial, apoderamiento apud acta ...)'"/>

	<xsl:variable name="lang.documento1" select="'Documento ODT/DOC'"/>
	<xsl:variable name="lang.documento2" select="'Documento PDF'"/>
	<xsl:variable name="lang.documento3" select="'Documento JPEG'"/>
	<xsl:variable name="lang.documento4" select="'Documento ZIP'"/>
	
	<!-- Rellenar estos parámetros en el formulario vacío 
		   Se ponen aquí para que no provoque error el template -->
	<xsl:variable name="doc.docodt" select="'NOMBRE_D1'"/>
	<xsl:variable name="doc.pdf" select="'NOMBRE_D2'"/>
	<xsl:variable name="doc.jpg" select="'NOMBRE_D3'"/>
	<xsl:variable name="doc.zip" select="'NOMBRE_D4'"/>
	
	<!-- Información del registro -->
	<xsl:variable name="lang.numRegistro" select="'Número de registro'"/>
	<xsl:variable name="lang.fechaPresentacion" select="'Fecha de presentación'"/>
	<xsl:variable name="lang.fechaEfectiva" select="'Fecha efectiva'"/>
	<xsl:variable name="lang.asunto" select="'Asunto'"/>

</xsl:stylesheet>
