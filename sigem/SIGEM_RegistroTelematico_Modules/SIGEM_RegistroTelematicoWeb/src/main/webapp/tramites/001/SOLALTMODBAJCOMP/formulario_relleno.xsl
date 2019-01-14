<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>
	
		
	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>
	<xsl:variable name="lang.cargo" select="'Cargo'"/>
	
	<xsl:variable name="lang.datosInteresado" select="'Datos de la entidad solicitante'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de personas en Comparece'"/>
	<xsl:variable name="lang.datosDeclarativos" select="'Manifiesta'"/>

	<xsl:variable name="lang.tipo_entidad" select="'Tipo de entidad'"/>
	<xsl:variable name="lang.tipo_cpv" select="'CPV'"/>
	<xsl:variable name="lang.nombre_entidad" select="'Nombre de la Entidad'"/>
	<xsl:variable name="lang.cif" select="'CIF'"/>
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>
	
	<xsl:variable name="lang.tercero_tipo_operacion" select="'Tipo de operación'"/>
	<xsl:variable name="lang.tercero_nombre" select="'Nombre'"/>
	<xsl:variable name="lang.tercero_dni" select="'DNI'"/>
	<xsl:variable name="lang.tercero_email" select="'Email'"/>
	<xsl:variable name="lang.tercero_movil" select="'Teléfono móvil'"/>
	<xsl:variable name="lang.tercero_cargo" select="'Cargo o puesto'"/>
	
	<xsl:variable name="lang.declaro1" select="'- Que al objeto de agilizar los trámites administrativos y reducir los tiempos en la gestión de los asuntos públicos, solicito a la Diputación Provincial de Ciudad Real que se de ALTA, BAJA o MODIFICACIÓN de sus datos de la persona que se indica en el siguiente cuadro (según la opción que se escoja en el campo TIPO DE OPERACIÓN) como representante de la entidad arriba indicada, en la Plataforma Provincial de Notificaciones Electrónicas para que, de forma preferente, a través del sistema de comparecencia electrónica COMPARECE de esa institución provincial, cuyo funcionamiento manifiesto conocer, le sean notificados todos los acuerdos y resoluciones  adoptados por esa Diputación y cualquier otra comunicación provincial de interés para esta entidad.'"/>
	<xsl:variable name="lang.declaro2" select="'- Que entiendo que la notificación que se practique será efectiva a todos los efectos legales a partir del momento en el que se acceda a ella por cualquiera de las personas indicadas, en el marco de lo dispuesto en el art. 28.5 de la ley 11/2007, de Acceso Electrónico de los Ciudadanos a los Servicios Públicos, en el art. 40 del RD 1671/2009 de 6 noviembre, de desarrollo parcial de la Ley 11/2007, y en el manual de instrucciones de la aplicación Comparece, instalado en la sede electrónica provincial https://sede.dipucr.es/documentacion.'"/>
	<xsl:variable name="lang.declaro3" select="'- Que, mientras no se indique lo contrario por esta entidad arriba indicada, los avisos, notificaciones y escritos de la Diputación Provincial de Ciudad Real, se deberán remitir electrónicamente a través de la Plataforma de Notificaciones Telemáticas a las personas que se den de ALTA a través de este formulario.'"/>	
		
	<xsl:template match="/">
		<div class="col">
				<label class="gr" style="position: relative; width:350px;">
					<xsl:value-of select="$lang.datosInteresado"/>:	
				</label>				
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.nombre_entidad"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre_entidad"/>
				</label>
				<br/>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.tipo_entidad"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipo_entidad"/>
				</label>
				<br/>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.cif"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/cif"/>
				</label>
				<br/>
		</div>
		<div class="col">	
			<label class="gr" style="position: relative; width:500px;">
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">1</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">2</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">3</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">4</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">5</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">6</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">7</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">8</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">9</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">10</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">11</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">12</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">13</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">14</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">15</xsl:with-param></xsl:call-template>
			</label>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.direccion"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/domicilioNotificacion"/>
				</label>
				<br/>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.localidad"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/localidad"/>
				</label>
				<br/>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.provincia"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/provincia"/>
				</label>
				<br/>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.cp"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/codigoPostal"/>
				</label>
				<br/>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.telefono"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/telefono"/>
				</label>
				<br/>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.email"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/emailSolicitante"/>
				</label>
				<br/>
		</div>		
		
		
		
		
		
		
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.id_nif"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/documentoIdentidad"/>
				</label>
				<br/>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.id_nombre"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombreSolicitante"/>
				</label>
				<br/>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.cargo"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/cargo"/>
				</label>
				<br/>
		</div>
		
		
		
		
		
		
		
			<div class="col">
				<label class="gr" style="position: relative; width:350px;">
					<xsl:value-of select="$lang.datosSolicitud"/>:	
				</label>				
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.tercero_nombre"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/tercero_nombre"/>
				</label>
				<br/>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.tercero_dni"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/tercero_dni"/>
				</label>
				<br/>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.tercero_tipo_operacion"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/tercero_tipo_operacion"/>
				</label>
				<br/>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.tercero_movil"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/tercero_movil"/>
				</label>
				<br/>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.tercero_email"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/tercero_email"/>
				</label>
				<br/>
		</div>
		<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.tercero_cargo"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/tercero_cargo"/>
				</label>
				<br/>
		</div>
		
		
		
		
		
	
				
		<div class="col">
		
			<label class="gr" style="position: relative; width:640px;text-align:justify;">

				
					<div class="col">		
						<label class="gr" style="position: relative;font-weight: bold; width:640px;">
							<xsl:value-of select="$lang.declaro1"/>
						</label>			
					</div>
				
				
					<div class="col">		
						<label class="gr" style="position: relative;font-weight: bold; width:640px;">
							<xsl:value-of select="$lang.declaro2"/>
						</label>			
					</div>
		
					<div class="col">		
						<label class="gr" style="position: relative;font-weight: bold; width:640px;">
							<xsl:value-of select="$lang.declaro3"/>
						</label>			
					</div>
				
				
		<br/>			
	</label>
			<br/>
		</div>
		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_AUTOFIRMA_COMUN_RELLENO" />
	</xsl:template>
	
	<xsl:template name="FIELDS">
	    <xsl:param name="row_id" />
	    <xsl:variable name="param_mun">Descripcion_mun_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_pag">pag_<xsl:value-of select="$row_id"/></xsl:variable>

		<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_pag]='')">
			<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_pag]"/><br/>
			<br/>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>