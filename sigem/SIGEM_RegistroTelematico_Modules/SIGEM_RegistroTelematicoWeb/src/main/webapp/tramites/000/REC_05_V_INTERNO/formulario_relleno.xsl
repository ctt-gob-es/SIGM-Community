<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />
	<xsl:include href="../REC_COMUNES/template_IBAN.xsl" />

	<xsl:variable name="lang.datosIdentificativos" select="'Datos identificativos'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	<xsl:variable name="lang.datosObligado" select="'Datos del contribuyente'"/>
	<xsl:variable name="lang.datosRepresentante" select="'Datos del representante o presentador autorizado'"/>

	<xsl:variable name="lang.pres_nif" select="'NIF Presentador'"/>
	<xsl:variable name="lang.pres_nombre" select="'Nombre Presentador'"/>
	<xsl:variable name="lang.repres_nif" select="'NIF/CIF Representante'"/>
	<xsl:variable name="lang.repres_nombre" select="'Nombre Representante'"/>
	<xsl:variable name="lang.repres_movil" select="'Número de teléfono móvil Representante'"/>
	<xsl:variable name="lang.repres_d_email" select="'Dirección de correo electrónico Representante'"/>	
	<xsl:variable name="lang.direccion" select="'Dirección'"/>
	<xsl:variable name="lang.calle" select="'Domicilio'"/>
	<xsl:variable name="lang.numero" select="'Numero'"/>
	<xsl:variable name="lang.escalera" select="'Escalera'"/>
	<xsl:variable name="lang.planta_puerta" select="'Planta/Puerta'"/>
	<xsl:variable name="lang.c_postal" select="'Código Postal'"/>
	<xsl:variable name="lang.movil" select="'Número de teléfono móvil'"/>
	<xsl:variable name="lang.d_email" select="'Dirección de correo electrónico'"/>
	<xsl:variable name="lang.nif" select="'NIF/CIF Contribuyente: '"/>
	<xsl:variable name="lang.nombre" select="'Nombre Contribuyente: '"/>
	
	<xsl:variable name="lang.municipio" select="'Municipio'"/>
	<xsl:variable name="lang.motivo" select="'Motivo'"/>
	<xsl:variable name="lang.otro_motivo" select="'Otros motivos'"/>
	<xsl:variable name="lang.municipio" select="'Nombre del municipio'"/>
	<xsl:variable name="lang.liquidacion" select="'Nº de liquidación'"/>
	<xsl:variable name="lang.referencia" select="'Referencia catastral'"/>
	<xsl:variable name="lang.situacion" select="'Situación del inmueble'"/>
	<xsl:variable name="lang.years" select="'Años'"/>

	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>

	<xsl:variable name="lang.ccc" select="'siguiente entidad y cuenta corriente :'"/>
	<xsl:variable name="lang.cuantia" select="'Cuantía de la devolución que se solicita:'"/>
	<xsl:variable name="lang.cuenta" select="'Código de Cuenta Corriente:'"/>

	<xsl:variable name="lang.notas" select="'INFORMACIÓN PARA EL CONTRIBUYENTE:'"/>
	<xsl:variable name="lang.notas1" select="'1.- El plazo máximo de resolución de su solicitud es de 6 meses, siendo los efectos del silencio administrativo NEGATIVOS.'"/>
	<xsl:variable name="lang.notas2" select="'2.- Es necesario consignar obligatoriamente los campos de nº de liquidación y referencia catastral con sus 20 dígitos.'"/>
	<xsl:variable name="lang.notas3" select="'3.- En el caso de que la solicitud venga referida a liquidaciones a nombre de otro obligado tributario, deberá acreditarse la representación legal o voluntaria (ver modelo en la página) del mismo.'"/>
	
	<xsl:variable name="lang.info_legal" select="'Los datos personales, identificativos y de contacto, aportados mediante esta comunicación se entienden facilitados voluntariamente, y serán incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del ámbito de las competencias de esta Administración Pública así como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telemáticos y enviarle invitaciones para eventos y felicitaciones en fechas señaladas. Entenderemos que presta su consentimiento tácito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podrá ejercer sus derechos de acceso, rectificación, cancelación y oposición ante el Responsable del Fichero, la Diputación Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - España, siempre acreditando conforme a Derecho su identidad en la comunicación. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigiéndose a la dirección citada ut supra o bien al correo electrónico lopd@dipucr.es o bien por teléfono al número gratuito 900 714 080.'"/>
	
	
	<xsl:variable name="lang.anexos" select="'Anexos'"/>
	
	<xsl:template match="/">
		<xsl:call-template name="DATOS_SOLICITUD_RELLENOS_PRESENTADOR" />
		
		<br/>	
		<div class="col">
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.datosSolicitud"/></b>	
			</label>
			<br/>
		</div>

		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.municipio"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_mun_1"/>
			</label>
		</div>

		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.motivo"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_motivo"/>
			</label>
		</div>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/motivo='MOTIVO_5'">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.otro_motivo"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/otro_motivo"/>
				</label>
			</div>
		</xsl:if>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.years"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/years"/>
			</label>
		</div>
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
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">16</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">17</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">18</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">19</xsl:with-param></xsl:call-template>
		<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">20</xsl:with-param></xsl:call-template>
		<br/>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.observaciones"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/observaciones"/>
			</label>
		</div>

		<br/>
		<xsl:call-template name="SOLICITA_BENEF_IBI" />
		<br/>
		<br/>		
		<div class="col" style="color: grey; text-align:justify">
			<label class="gr" style="position: relative; width:650px">
				<i><xsl:value-of select="$lang.notas"/></i>
			</label>
			<label class="gr" style="position: relative; width:650px">
				<i><xsl:value-of select="$lang.notas1"/></i>
			</label>
			<label class="gr" style="position: relative; width:650px">
				<i><xsl:value-of select="$lang.notas2"/></i>
			</label>
			<label class="gr" style="position: relative; width:650px">
				<i><xsl:value-of select="$lang.notas3"/></i>
			</label>
		</div>
		<br/>
		<br/>
		<div class="col" style="color: grey; text-align:justify">
			<label class="gr" style="position: relative; width:650px">
				<xsl:value-of select="$lang.info_legal"/>
			</label>
		</div>
	</xsl:template>

	<xsl:template name="FIELDS">
	    <xsl:param name="row_id" />
	    <xsl:variable name="param_mun">mun_<xsl:value-of select="$row_id"/></xsl:variable>
	    <xsl:variable name="param_liq">liq_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref1">ref1_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref2">ref2_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref3">ref3_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref4">ref4_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_sit">sit_<xsl:value-of select="$row_id"/></xsl:variable>
		
		<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref1]='')">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.municipio"/>:<br/>
					<xsl:value-of select="$lang.liquidacion"/>:<br/>
					<xsl:value-of select="$lang.referencia"/>:<br/>
					<xsl:value-of select="$lang.situacion"/>:
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_mun]"/><br/>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_liq]"/><br/>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref1]"/> .
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref2]"/> .
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref3]"/> .
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref4]"/><br/>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_sit]"/>
					<br/>
				</label>
			</div>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
