<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitante"/></h1>
   		</div>
   		<div class="cuadro" style="">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.docIdentidad"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px;</xsl:attribute>
					<xsl:attribute name="name">documentoIdentidad</xsl:attribute>
					<xsl:attribute name="id">documentoIdentidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Documento_Identificacion/Numero"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nombre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">cifProveedorOrigen</xsl:attribute>
					<xsl:attribute name="id">cifProveedorOrigen</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Nombre"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<h1><xsl:value-of select="$lang.origenTitle"/></h1>
            <div id="origen">
                <div class="col">
                    <label class="gr">
                        <xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
                        <xsl:value-of select="$lang.emailSolicitante"/>:
                    </label>
                    <input type="text">
                        <xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
                        <xsl:attribute name="name">emailSolicitante</xsl:attribute>
                        <xsl:attribute name="id">emailSolicitante</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/RegistroFactura/Origen/CIF_Proveedor"/></xsl:attribute>
                    </input>
                </div>
                
                <div class="col">
                    <label class="gr">
                        <xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
                        <xsl:value-of select="$lang.razonSocialOrigen"/>:
                    </label>
                    <input type="text">
                        <xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
                        <xsl:attribute name="name">razonSocialOrigen</xsl:attribute>
                        <xsl:attribute name="id">razonSocialOrigen</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/RegistroFactura/Origen/Razon_Social"/></xsl:attribute>
                    </input>
                </div>
                
                <div class="col">
                    <label class="gr">
                        <xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
                        <xsl:value-of select="$lang.nifRepresentanteOrigen"/>:
                    </label>
                    <input type="text">
                        <xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
                        <xsl:attribute name="name">nifRepresentanteOrigen</xsl:attribute>
                        <xsl:attribute name="id">nifRepresentanteOrigen</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/RegistroFactura/Origen/NIF_Representante"/></xsl:attribute>
                    </input>
                </div>
            </div>
            
            <h1><xsl:value-of select="$lang.destinoTitle"/></h1>
            <div id="destino">
                <div class="col">
                    <label class="gr">
                        <xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
                        <xsl:value-of select="$lang.cifEntidadDestino"/>:
                    </label>
                    <input type="text">
                        <xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
                        <xsl:attribute name="name">cifEntidadDestino</xsl:attribute>
                        <xsl:attribute name="id">cifEntidadDestino</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/RegistroFactura/Destino/CIF_Entidad"/></xsl:attribute>
                    </input>
                </div>
                
                <div class="col">
                    <label class="gr">
                        <xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
                        <xsl:value-of select="$lang.razonSocialDestino"/>:
                    </label>
                    <input type="text">
                        <xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
                        <xsl:attribute name="name">razonSocialDestino</xsl:attribute>
                        <xsl:attribute name="id">razonSocialDestino</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/RegistroFactura/Destino/Razon_Social"/></xsl:attribute>
                    </input>
                </div>
                
            </div>
            
            <h1><xsl:value-of select="$lang.solicitudTitle"/></h1>
            <div id="solicitud">
                <div class="col">
                    <label class="gr">
                        <xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
                        <xsl:value-of select="$lang.asuntoSolicitud"/>:
                    </label>
                    <input type="text">
                        <xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
                        <xsl:attribute name="name">asuntoSolicitud</xsl:attribute>
                        <xsl:attribute name="id">asuntoSolicitud</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/RegistroFactura/Solicitud/Asunto"/></xsl:attribute>
                    </input>
                </div>
                
                <div class="col">
                    <label class="gr">
                        <xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
                        <xsl:value-of select="$lang.numFacturaSolicitud"/>:
                    </label>
                    <input type="text">
                        <xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
                        <xsl:attribute name="name">numFacturaSolicitud</xsl:attribute>
                        <xsl:attribute name="id">numFacturaSolicitud</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/RegistroFactura/Solicitud/Num_Factura"/></xsl:attribute>
                    </input>
                </div>
                
                <div class="col">
                    <label class="gr">
                        <xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
                        <xsl:value-of select="$lang.fechaFacturaSolicitud"/>:
                    </label>
                    <input type="text">
                        <xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
                        <xsl:attribute name="name">fechaFacturaSolicitud</xsl:attribute>
                        <xsl:attribute name="id">fechaFacturaSolicitud</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/RegistroFactura/Solicitud/Fecha_Factura"/></xsl:attribute>
                    </input>
                </div>
				
				<div class="col">
                    <label class="gr">
                        <xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
                        <xsl:value-of select="$lang.importe"/>:
                    </label>
                    <input type="text">
                        <xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
                        <xsl:attribute name="name">importe</xsl:attribute>
                        <xsl:attribute name="id">importe</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/RegistroFactura/Solicitud/Importe"/></xsl:attribute>
                    </input>
                </div>
            </div>
			
	</xsl:template>
</xsl:stylesheet>