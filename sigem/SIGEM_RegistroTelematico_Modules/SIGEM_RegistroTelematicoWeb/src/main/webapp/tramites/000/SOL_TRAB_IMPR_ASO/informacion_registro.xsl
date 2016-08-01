<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.docIdentidad" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>
	<xsl:variable name="lang.numRegistro" select="'Número de registro'"/>
	<xsl:variable name="lang.fechaPresentacion" select="'Fecha de presentación'"/>
	<xsl:variable name="lang.fechaEfectiva" select="'Fecha efectiva'"/>
	<xsl:variable name="lang.asunto" select="'Asunto'"/>

	<xsl:variable name="lang.infoRegistro1" select="'Solicitud enviada correctamente.'"/>
	<xsl:variable name="lang.infoRegistro2" select="'Se ha enviado un correo electrónico a la persona de contacto, si no lo recibe o la dirección indicada no es correcta, revise la carpeta de SPAM de su buzón de correo o póngase en contacto con la Imprenta Provincial.'"/>
	<xsl:variable name="lang.infoRegistro3" select="'Teléfono: 926 25 59 50 exts. 543 o 540'"/>
	<xsl:variable name="lang.infoRegistro4" select="'Fax: 926 25 02 53'"/>
	<xsl:variable name="lang.infoRegistro5" select="'e-mail: imprenta@dipucr.es'"/>
	
	<xsl:template match="/">
		<div class="col">
			<div style="color: grey; text-align:justify;">
				<label class="gr">
					<xsl:attribute name="style">width:650px; color:blue; font-size:13px; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.infoRegistro1"/>
				</label>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">width:650px; color:#FE2E2E;</xsl:attribute>
					<xsl:value-of select="$lang.infoRegistro2"/>
				</label>
			</div>
			<br/>
			<div style="color: grey; text-align:center;">

				<label class="gr">
					<xsl:attribute name="style">width:650px; color:blue; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.infoRegistro3"/>
				</label>
				<label class="gr">
					<xsl:attribute name="style">width:650px; color:blue; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.infoRegistro4"/>
				</label>
				<label class="gr">
					<xsl:attribute name="style">width:650px; color:blue; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.infoRegistro5"/>
				</label>
			</div>
		</div>
		<br/>
		<div class="col" xml:space="preserve">
			<label class="gr" style="position: relative; width:170px;">
				<xsl:value-of select="$lang.docIdentidad"/>:	
			</label>
			<label class="gr" style="position: relative; width:460px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Documento_Identificacion/Numero"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:170px;">
				<xsl:value-of select="$lang.nombre"/>:	
			</label>
			<label class="gr" style="position: relative; width:460px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Nombre"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:170px;">
				<xsl:value-of select="$lang.email"/>:	
			</label>
			<label class="gr" style="position: relative; width:460px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Correo_Electronico"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:170px;">
				<xsl:value-of select="$lang.numRegistro"/>:	
			</label>
			<label class="gr" style="position: relative; width:460px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Registro/Numero_Registro"/>
			</label>
			<input type="hidden" id="numeroRegistro" name="numeroRegistro">
				<xsl:attribute name="value"><xsl:value-of select="Solicitud_Registro/Datos_Registro/Numero_Registro"/></xsl:attribute>
			</input>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:170px;">
				<xsl:value-of select="$lang.fechaPresentacion"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:460px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Registro/Hora_Presentacion"/>, 
				<xsl:call-template name="transformaFecha">
				   <xsl:with-param name="node" select="Solicitud_Registro/Datos_Registro/Fecha_Presentacion"/>
				</xsl:call-template>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:170px;">
				<xsl:value-of select="$lang.fechaEfectiva"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:460px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Registro/Hora_Efectiva"/>, 
				<xsl:call-template name="transformaFecha">
				   <xsl:with-param name="node" select="Solicitud_Registro/Datos_Registro/Fecha_Efectiva"/>
				</xsl:call-template>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:170px;">
				<xsl:value-of select="$lang.asunto"/>:	
			</label>
			<label class="gr" style="position: relative; width:460px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Asunto/Descripcion"/>
			</label>
			<br/>
		</div><br/>
	</xsl:template>
	<xsl:template name="transformaFecha">
		<xsl:param name="node"/>
		<xsl:variable name="date" select="concat(substring(string($node),9,2),'-',substring(string($node),6,2),'-',substring(string($node),1,4))"/>
		<xsl:value-of select="$date"/>
	</xsl:template>
</xsl:stylesheet>