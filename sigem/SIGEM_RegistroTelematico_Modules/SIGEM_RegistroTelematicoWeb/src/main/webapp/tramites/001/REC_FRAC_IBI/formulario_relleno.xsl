<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />
	<xsl:include href="../REC_COMUNES/template_IBAN.xsl" />

	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.municipios" select="'MUNICIPIO:'"/>
	<xsl:variable name="lang.textoInmuebles" select="'INMUEBLES A LOS QUE AFECTA LA SOLICITUD, SÓLO PARA IBI-URBANA e IBI-CONSTRUCCIONES RÚSTICAS.-'"/>
	<xsl:variable name="lang.notasInmuebles" select="'Indicar los 20 dígitos de la referencia catastral.'"/>
	<xsl:variable name="lang.referencia" select="'Referencia catastral'"/>

	<xsl:variable name="lang.textoIban" select="'DOMICILIACIÓN (con IBAN).-'"/>

	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>

	<xsl:variable name="lang.notas" select="'Notas Informativas.-'"/>
	<xsl:variable name="lang.notas1" select="'1.- El pago se ejecutará mediante el sistema de domiciliación bancaria, a mediados de julio y octubre.'"/>
	<xsl:variable name="lang.notas2" select="'2.- El impago o la devolución del 1º fraccionamiento, dejará sin virtualidad el 2º, siendo exigible el pago del total de la deuda durante el periodo de cobro en el que se exija el padrón del IBI. El impago del 2º plazo, determinará la exigibilidad de la deuda en vía ejecutiva. La devolución y/o impago de algunos de los plazos implicará que la solicitud quede sin efecto para próximos ejercicios.'"/>
	<xsl:variable name="lang.notas3" select="'3.- En los casos en que concurran varios cotitulares como sujetos pasivos del impuesto, la solicitud deberán realizarla conjuntamente todos y cada uno de los obligados tributarios. Quedarán exceptuados los casos de cotitularidad por razón del matrimonio, en cuyo supuesto bastará que la solicitud sea instada por uno cualquiera de los cónyuges.'"/>
	<xsl:variable name="lang.notas4" select="'4.- Las solicitudes serán resueltas por la Diputación de Ciudad Real, como ente gestor del impuesto por delegación del Ayuntamiento, entendiéndose estimadas sin necesidad de resolución expresa, por el mero hecho de que se produzca el cargo en cuenta del primer plazo del fraccionamiento en las fechas indicadas a tal fin. La desestimación o inadmisión por causas distintas a las indicadas en las notas informativas, serán comunicadas en el plazo máximo de 6 meses desde la presentación de la solicitud.'"/>
	<xsl:variable name="lang.notas5" select="'5.- No se admitirán a trámite solicitudes referidas a liquidaciones cuya cuota íntegra sea inferior a 100,00€.'"/>
	<xsl:variable name="lang.notas6" select="'6.- Los obligados tributarios que quieran acogerse a este sistema de pago, no podrán figurar como deudores a la hacienda local, en la base de datos del Servicio de Gestión Tributaria, Inspección y Recaudación de la Diputación. En el supuesto de que exisitieran deudas pendientes de pago, se comunicarán al solicitante para que en el plazo máximo de 20 días proceda a regularizar la situación, procediendo a la inadmisión de la solicitud en caso de que no se llevare a cabo la misma en el plazo indicado.'"/>
	<xsl:variable name="lang.notas7" select="'7.- Aquellos contribuyentes que opten por este sistema de pago, no será necesario que vuelvan a solicitarlo nuevamente en años sucesivos, entendiéndose prorrogada la solicitud para devengos posteriores, salvo que insten la anulación de la misma en el plazo habilitado para la presentación.'"/>

	<xsl:variable name="lang.notas8" select="'REPRESENTANTE.- En caso de que la solicitud se realice por el representante legal o voluntario del obligado, deberá acreditarse dicha representación.'"/>
	
	<xsl:variable name="lang.anexos" select="'Anexos'"/>

	<xsl:template match="/">
		<xsl:call-template name="DATOS_SOLICITUD_RELLENOS_PRESENTADOR" />
		
		<br/>
		<div class="submenu">
			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
		</div>
		<div class="cuadro">
			<div style="margin-bottom:10px;color:#006699;">
				<b>		
					<label class="gr">
						<xsl:attribute name="style">width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.municipios"/>
					</label>
				</b>
			</div>
			<div class="col">			
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_mun_1"/>
				</label>
			</div>
			<div style="margin-top:20px;margin-bottom:10px;color:#006699;">
				<b>		
					<label class="gr">
						<xsl:attribute name="style">width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.textoInmuebles"/>
					</label>
				</b>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:350px;margin-bottom:10px;</xsl:attribute>
					<xsl:value-of select="$lang.notasInmuebles"/>
				</label>
			
				<table style="font-size:10px; width:90%; table-layout: fixed;" cellspacing="0" border="1">
					<tr>
						<td style="width:170px;background-color:#dee1e8"><xsl:value-of select="$lang.referencia"/></td>
					</tr>
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
				</table>
			</div>	
			<br/>
			<div style="margin-top:10px;margin-bottom:10px;color:#006699;">
				<b>		
					<label class="gr">
						<xsl:attribute name="style">width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.textoIban"/>
					</label>
				</b>
			</div>
			<xsl:call-template name="CUENTA_CORRIENTE_IBAN_RELLENOS" />

			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.observaciones"/>:	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/observaciones"/>
				</label>
			</div>
		</div>
		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.notas"/></h1>
   		</div>
		<div class="cuadro" style="color: grey; text-align:justify">
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas1"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas2"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas3"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas4"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas5"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas6"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas7"/></i></div>
			
			<div style="margin-bottom:10px;margin-top:10px;"><i><xsl:value-of select="$lang.notas8"/></i></div>
		</div>
		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_AUTOFIRMA_COMUN_RELLENO" />
	</xsl:template>

	<xsl:template name="FIELDS">
	    <xsl:param name="row_id" />
		<xsl:variable name="param_ref1">ref1_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref2">ref2_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref3">ref3_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref4">ref4_<xsl:value-of select="$row_id"/></xsl:variable>

		<xsl:variable name="row_style">		
			<xsl:choose>
				<xsl:when test="$row_id&gt;'5'">
					display:none;
				</xsl:when>
			</xsl:choose>
		</xsl:variable>

		<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref1]='')">
			<tr id="row_{$row_id}" style="{$row_style}">	        
				<td style="align:center;">
					<label style="margin:0px 20px 0px 30px;">
						INMUEBLE <xsl:value-of select="$row_id"/>:
					</label>
					<label class="gr" style="position: relative; width:300px;">
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref1]"/> .
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref2]"/> .
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref3]"/> .
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ref4]"/>
						<br/>
					</label>
				</td>	       
			</tr>
		</xsl:if>
	</xsl:template>	
</xsl:stylesheet>
