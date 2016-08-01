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
	<xsl:variable name="lang.expediente" select="'Nº de expediente'"/>
	<xsl:variable name="lang.cuantia" select="'Cuantía solicitada'"/>
	<xsl:variable name="lang.cuenta" select="'Código de Cuenta Corriente'"/>
	<xsl:variable name="lang.liquidaciones" select="'Liquidaciones'"/>
	
	<xsl:variable name="lang.anexos" select="'Anexos'"/>

	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>
	
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
				<xsl:value-of select="$lang.observaciones"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/observaciones"/>
			</label>
		</div>


		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.cuantia"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/cuantia"/>
			</label>
		</div>
		<xsl:call-template name="CUENTA_CORRIENTE_IBAN_RELLENOS" />
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.liquidaciones"/>:	
			</label>
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
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">16</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">17</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">18</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">19</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">20</xsl:with-param></xsl:call-template>
			</label>
		</div>
		<br/>
		<br/>
	</xsl:template>
	
	<xsl:template name="FIELDS">
	    <xsl:param name="row_id" />
	    <xsl:variable name="param_mun">mun_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_liq">liq_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_epi">ent_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref">imp_<xsl:value-of select="$row_id"/></xsl:variable>
		
		<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_liq]='')">
			<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_liq]"/><br/>
			<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_epi]"/><br/>
			<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref]"/><br/>
		<br/>
		</xsl:if>

	</xsl:template>
	
</xsl:stylesheet>
