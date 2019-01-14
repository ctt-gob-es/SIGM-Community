<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />
<xsl:include href="../REC_COMUNES/template_IBAN.xsl" />

	<xsl:variable name="lang.datosIdentificativos" select="'Datos identificativos'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.pres_nif" select="'NIF Presentador'"/>
	<xsl:variable name="lang.pres_nombre" select="'Nombre Presentador'"/>
	<xsl:variable name="lang.repres_nif" select="'NIF/CIF Representante'"/>
	<xsl:variable name="lang.repres_nombre" select="'Nombre Representante'"/>
	<xsl:variable name="lang.nif" select="'NIF/CIF Contribuyente'"/>
	<xsl:variable name="lang.nombre" select="'Nombre Contribuyente'"/>
	<xsl:variable name="lang.direccion" select="'Dirección'"/>
	<xsl:variable name="lang.calle" select="'Calle'"/>
	<xsl:variable name="lang.numero" select="'Numero'"/>
	<xsl:variable name="lang.escalera" select="'Escalera'"/>
	<xsl:variable name="lang.planta_puerta" select="'Planta/Puerta'"/>
	<xsl:variable name="lang.c_postal" select="'Código Postal'"/>

	<xsl:variable name="lang.motivo" select="'Motivo'"/>
	<xsl:variable name="lang.municipio" select="'Municipio'"/>
	<xsl:variable name="lang.otro_motivo" select="'Otros motivos'"/>
	<xsl:variable name="lang.liquidaciones" select="'Liquidaciones'"/>

	<xsl:variable name="lang.cuantia" select="'Cuantía solicitada'"/>
	<xsl:variable name="lang.anios" select="'de los años'"/>
	<xsl:variable name="lang.cuenta" select="'Código de Cuenta Corriente'"/>	
	<xsl:variable name="lang.anexos" select="'Anexos'"/>
	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>

	<xsl:variable name="lang.liquidaciones" select="'LIQUIDACIONES OBJETO DE COMPENSACIÓN*'"/>

	<xsl:variable name="lang.expone1" select="'EXPONE'"/>
	<xsl:variable name="lang.expone2" select="', que ha recibido la liquidación/es que a continuación se relaciona/n:'"/>
	<xsl:variable name="lang.expone3" select="'Que se efectuó el pago de las liquidaciones que se indican, correspondientes a la misma finca y ejercicios, por lo que solicita la anulación de la liquidación/es arriba identificadas, y se proceda a compensar las cantidades ya pagadas por la causa que se expresa (seleccionar una de las causas)'"/>
	
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
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.liquidaciones"/></b>	
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:500px;">
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">1</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">2</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">3</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">4</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">5</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">6</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">7</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">8</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">9</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">10</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">11</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">12</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">13</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">14</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">15</xsl:with-param></xsl:call-template>
			</label>
		</div>
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
		<div class="col">			
			<xsl:call-template name="CUENTA_CORRIENTE_IBAN_RELLENOS" />
		</div>
		
		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_AUTOFIRMA_COMUN_RELLENO" />
	</xsl:template>
	
	<xsl:template name="FIELDS">
	    <xsl:param name="row_id" />
	    <xsl:variable name="param_mun">Descripcion_mun_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_liq">liq_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_pag">pag_<xsl:value-of select="$row_id"/></xsl:variable>

		<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_liq]='')">
			<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_liq]"/><br/>
			<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_pag]"/><br/>
			<br/>
		</xsl:if>
	</xsl:template>


	<xsl:template name="FIELDS2">
	    <xsl:param name="row_id1" />
	    <xsl:variable name="param_mun">Descripcion_mun_<xsl:value-of select="$row_id1"/></xsl:variable>
		<xsl:variable name="param_liq">liq1_<xsl:value-of select="$row_id1"/></xsl:variable>
		<xsl:variable name="param_pag">pag1_<xsl:value-of select="$row_id1"/></xsl:variable>

		<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_liq]='')">
			<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_liq]"/><br/>
			<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_pag]"/><br/>
			<br/>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>
