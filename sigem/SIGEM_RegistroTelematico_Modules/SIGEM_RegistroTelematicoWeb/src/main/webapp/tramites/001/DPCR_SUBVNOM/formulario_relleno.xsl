<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.datosInteresado" select="'Datos del interesado'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.ayuntamiento" select="'Nombre del Ayuntamiento'"/>	
	<xsl:variable name="lang.nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.representante" select="'En representación del Ayuntamiento de'"/>
	<xsl:variable name="lang.cif" select="'con CIF'"/>
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>

	<xsl:variable name="lang.asunto" select="'Asunto'"/>
	<xsl:variable name="lang.finalidad" select="'Finalidad (Obra, Suministro o Actividad)'"/>	
	<xsl:variable name="lang.presupuesto" select="'Presupuesto Total Estimado'"/>
	<xsl:variable name="lang.subvencion" select="'Subvención que se solicita'"/>
	<xsl:variable name="lang.fecha" select="'Fecha prevista de realización actividad / suministro / terminación de las obras'"/>
	
	
	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>
	
	<xsl:variable name="lang.declaro1" select="'Que la entidad solicitante no se encuentra incursa en ninguna de las circunstancias de prohibición para la obtención de la condición de beneficiario de ayuda o subvención, así como que se encuentra al corriente en el cumplimiento de sus obligaciones fiscales con la Diputación Provincial de Ciudad Real.'"/>
	<xsl:variable name="lang.declaro2" select="'Que esta entidad se encuentra al corriente en el cumplimiento de sus obligaciones tributarias y frente a la Seguridad Social, y no es deudora por resolución de procedencia de reintegro de subvenciones.'"/>	
		

	
	<xsl:template match="/">
		<div class="col">
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.datosInteresado"/></b>	
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.nif"/>:	
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
		<div class="col">
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.datosSolicitud"/></b>	
			</label>
			<br/>
		</div>
		<br/>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ayuntamiento">
			<div class="col">
				<label class="gr" style="position: relative; width:200px;font-weight: bold;">
					<xsl:value-of select="$lang.ayuntamiento"/>:	
				</label>
				<label class="gr" style="position: relative; width:450px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_ayuntamiento"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/asunto">
			<div class="col">
				<label class="gr" style="position: relative; width:200px;font-weight: bold;">
					<xsl:value-of select="$lang.asunto"/>:	
				</label>
				<label class="gr" style="position: relative; width:450px;">
					Solicitud de Subvención Nominativa para Entidades Locales
				</label>
				<br/>
			</div>
		</xsl:if>
		<div class="col">
					<label class="gr" style="position: relative; width:200px;font-weight: bold;">
						<xsl:value-of select="$lang.finalidad"/>:	
					</label>
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:450px;</xsl:attribute>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/finalidad"/>
					</label>
					<br/>
		</div>
		<div class="col">
					<label class="gr" style="position: relative; width:200px;font-weight: bold;">
						<xsl:value-of select="$lang.presupuesto"/>:	
					</label>
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:450px;</xsl:attribute>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/presupuesto"/>
					</label>
					<br/>
		</div>
		<div class="col">
					<label class="gr" style="position: relative; width:200px;font-weight: bold;">
						<xsl:value-of select="$lang.subvencion"/>:	
					</label>
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:450px;</xsl:attribute>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/subvencion"/>
					</label>
					<br/>
		</div>
		<div class="col">
					<label class="gr" style="position: relative; width:200px;font-weight: bold;">
						<xsl:value-of select="$lang.fecha"/>:	
					</label>
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:450px;</xsl:attribute>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/fecha"/>
					</label>
					<br/>
		</div>

		<div class="col">
			<label class="gr" style="position: relative; width:640px;text-align:justify;">
				<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/expone">
					<div style="position: relative; width:640px;text-align:justify;">
						<label class="gr" style="position: relative; width:150px;font-weight: bold;">
							<xsl:value-of select="$lang.expone"/>:	
						</label>
						<label class="gr" style="position: relative; width:640px;">
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/expone"/>
						</label>
						<br/>
					</div>
				</xsl:if>
				<br/>
				<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/solicita">
					<div class="col">
						<label class="gr" style="position: relative; width:150px;font-weight: bold;">
							<xsl:value-of select="$lang.solicita"/>:	
						</label>
						<label class="gr" style="position: relative; width:640px;">
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/solicita"/>
						</label>
						<br/>
					</div>
				</xsl:if>
				<br/>
				<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/declaro1='Si'">
					<div class="col">		
						<label class="gr" style="position: relative;font-weight: bold; width:640px;">
							<xsl:value-of select="$lang.declaro1"/>
						</label>			
					</div>
				</xsl:if>
				<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/declaro2='Si'">
					<div class="col">		
						<label class="gr" style="position: relative;font-weight: bold; width:640px;">
							<xsl:value-of select="$lang.declaro2"/>
						</label>			
					</div>
				</xsl:if>
				
		<br/>			
	</label>
			<br/>
		</div>
		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_AUTOFIRMA_COMUN_RELLENO" />
	</xsl:template>
</xsl:stylesheet>