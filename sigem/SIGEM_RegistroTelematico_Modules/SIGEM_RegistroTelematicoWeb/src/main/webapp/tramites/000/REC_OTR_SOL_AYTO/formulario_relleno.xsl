<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />
	<xsl:include href="../REC_COMUNES/template_IBAN.xsl" />

	<xsl:variable name="lang.datosIdentificativos" select="'Datos identificativos'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.pres_nif" select="'NIF Presentador'"/>
	<xsl:variable name="lang.pres_nombre" select="'Nombre Presentador'"/>
	<xsl:variable name="lang.repres_nif" select="'NIF/CIF Representante'"/>
	<xsl:variable name="lang.repres_nombre" select="'Nombre Representante'"/>

	<xsl:variable name="lang.datosInteresado" select="'Datos de la Entidad Local solicitante'"/>
	<xsl:variable name="lang.representante" select="'Ayuntamiento'"/>
	<xsl:variable name="lang.cif" select="'CIF'"/>
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>

	<xsl:variable name="lang.datosRepresentante" select="'Datos del representante o presentador autorizado'"/>
	<xsl:variable name="lang.direccion" select="'Dirección'"/>
	<xsl:variable name="lang.calle" select="'Calle'"/>
	<xsl:variable name="lang.numero" select="'Numero'"/>
	<xsl:variable name="lang.escalera" select="'Escalera'"/>
	<xsl:variable name="lang.planta_puerta" select="'Planta/Puerta'"/>
	<xsl:variable name="lang.c_postal" select="'Código Postal'"/>
	<xsl:variable name="lang.movil" select="'Número de teléfono móvil'"/>
	<xsl:variable name="lang.d_email" select="'Dirección de correo electrónico'"/>

	<xsl:variable name="lang.solicita" select="'Solicita'"/>
	<xsl:variable name="lang.expone" select="'Expone'"/>

	<xsl:variable name="lang.organoAsignado" select="'Órgano al que se dirige: Servicio de Gestión Tributaria, Inspección y Recaudación.'"/>

	<xsl:variable name="lang.telefono" select="'Teléfono'"/>

	<xsl:variable name="lang.anexos" select="'Anexos'"/>

	<xsl:variable name="lang.info_legal" select="'Los datos personales, identificativos y de contacto, aportados mediante esta comunicación se entienden facilitados voluntariamente, y serán incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del ámbito de las competencias de esta Administración Pública así como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telemáticos y enviarle invitaciones para eventos y felicitaciones en fechas señaladas. Entenderemos que presta su consentimiento tácito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podrá ejercer sus derechos de acceso, rectificación, cancelación y oposición ante el Responsable del Fichero, la Diputación Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - España, siempre acreditando conforme a Derecho su identidad en la comunicación. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigiéndose a la dirección citada ut supra o bien al correo electrónico lopd@dipucr.es o bien por teléfono al número gratuito 900 714 080.'"/>
	
	
	<xsl:template match="/">
		<div class="submenu">
			<h1><xsl:value-of select="$lang.datosIdentificativos"/></h1>
		</div>
		<div class="cuadro" style="">		
			<div class="col">
				<label class="gr" style="position: relative; width:150px; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.pres_nif"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Documento_Identificacion/Numero"/>
				</label>
				<br/>
			</div>
			<div class="col">
				<label class="gr" style="position: relative; width:150px; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.pres_nombre"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Nombre"/>
				</label>
				<br/>
			</div>
		</div>
		<div class="submenu">
			<h1><xsl:value-of select="$lang.datosRepresentante"/></h1>
		</div>
		<div class="cuadro" style="">
			<div class="col">
				<label class="gr" style="position: relative; width:200px; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.repres_nif"/>:	
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; </xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_nif"/>
				</label>
				<br/>
			</div>
			<div class="col">
				<label class="gr" style="position: relative; width:500px; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.repres_nombre"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_nombre"/>
				</label>
				<br/>
			</div>
			<div class="col">
				<label class="gr" style="position: relative; width:500px; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.direccion"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rcalle"/>,
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rciudad"/>, 
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rregion"/>, 
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rc_postal"/>
				</label>
				<br/>
			</div>
			<div class="col">
				<label class="gr" style="position: relative; width:200px; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.movil"/>:	
				</label>
				<label class="gr" style="position: relative;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_movil"/>
				</label>
				<br/>
			</div>
			<div class="col">
				<label class="gr" style="position: relative; width:250px; font-weight: bold; left:10px;">
					<xsl:value-of select="$lang.d_email"/>:	
				</label>
				<label class="gr" style="position: relative;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_d_email"/>
				</label>
				<br/>
			</div>
		</div>

		<div class="submenu">
			<h1><xsl:value-of select="$lang.datosInteresado"/></h1>
		</div>
		<div class="cuadro" style="">
			<div class="col">
				<label class="gr" style="position: relative; width:100%; font-weight: bold; left:10px">
					<xsl:value-of select="$lang.representante"/>:	
				</label>
				<label class="gr" style="position: relative; width:100%; left:20px">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_ayuntamiento"/>
				</label>
				<br/>
			</div>
			<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/cif">
				<div class="col">
					<label class="gr" style="position: relative; font-weight: bold; left:10px;">
						<xsl:value-of select="$lang.cif"/>:	
					</label>
					<label class="gr" style="position: relative; width:500px; left:10px;">
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/cif"/>
					</label>
					<br/>
				</div>
			</xsl:if>
			<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/domicilioNotificacion">
				<div class="col">
					<label class="gr" style="position: relative; width:100%; font-weight: bold; left:10px;">
						<xsl:value-of select="$lang.direccion"/>:	
					</label>
					<label class="gr" style="position: relative; width:100%; left:20px;">
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/domicilioNotificacion"/>
					</label>
					<br/>
				</div>
			</xsl:if>
			<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/localidad">
				<div class="col">
					<label class="gr" style="position: relative; font-weight: bold; left:10px;">
						<xsl:value-of select="$lang.localidad"/>:	
					</label>
					<label class="gr" style="position: relative; width:500px; left:10px;">
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/localidad"/>
					</label>
					<br/>
				</div>
			</xsl:if>
			<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/provincia">
				<div class="col">
					<label class="gr" style="position: relative; font-weight: bold; left:10px;">
						<xsl:value-of select="$lang.provincia"/>:	
					</label>
					<label class="gr" style="position: relative; width:500px; left:10px;">
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/provincia"/>
					</label>
					<br/>
				</div>
			</xsl:if>
			<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/codigoPostal">
				<div class="col">
					<label class="gr" style="position: relative; font-weight: bold; left:10px;">
						<xsl:value-of select="$lang.cp"/>:	
					</label>
					<label class="gr" style="position: relative; width:500px; left:10px;">
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/codigoPostal"/>
					</label>
					<br/>
				</div>
			</xsl:if>
			<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/telefono">
				<div class="col">
					<label class="gr" style="position: relative; font-weight: bold; left:10px;">
						<xsl:value-of select="$lang.telefono"/>:	
					</label>
					<label class="gr" style="position: relative; width:500px; left:10px;">
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Telefono"/>
					</label>
					<br/>
				</div>
			</xsl:if>
			<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/emailSolicitante">
				<div class="col">
					<label class="gr" style="position: relative; width:150px; font-weight: bold; left:10px;">
						<xsl:value-of select="$lang.email"/>:	
					</label>
					<label class="gr" style="position: relative; width:300px; left:10px;">
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/emailSolicitante"/>
					</label>
					<br/>
				</div>
			</xsl:if>
		</div>

		<div class="submenu">
			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
		</div>
		<div class="cuadro" style="">
			<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/expone">
				<div class="col" style="height: 35px;">
					<label class="gr" style="position: relative; width:150px; font-weight: bold; left:10px;">
						<xsl:value-of select="$lang.expone"/>:	
					</label>
					<label class="gr" style="position: relative; width:500px;">
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/expone"/>
					</label>
					<br/>
				</div>
			</xsl:if>
			<br/>
			<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/solicita">
				<div class="col" style="height: 35px;">
					<label class="gr" style="position: relative; width:150px; font-weight: bold; left:10px;">
						<xsl:value-of select="$lang.solicita"/>:	
					</label>
					<label class="gr" style="position: relative; width:500px;">
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/solicita"/>
					</label>
					<br/>
				</div>
			</xsl:if>
		</div>
		<div class="submenu">
			<h1><xsl:value-of select="$lang.organoAsignado"/></h1>
		</div>

		<div class="col" style="color: grey; text-align:justify">
			<label class="gr" style="position: relative; width:650px">
				<xsl:value-of select="$lang.info_legal"/>
			</label>
		</div>		
	</xsl:template>
</xsl:stylesheet>
