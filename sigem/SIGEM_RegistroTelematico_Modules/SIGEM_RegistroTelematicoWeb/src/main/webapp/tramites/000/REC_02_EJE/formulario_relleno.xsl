<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.datosIdentificativos" select="'Datos identificativos'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.pres_nif" select="'NIF Presentador'"/>
	<xsl:variable name="lang.pres_nombre" select="'Nombre Presentador'"/>
	<xsl:variable name="lang.repres_nif" select="'NIF/CIF Representante'"/>
	<xsl:variable name="lang.repres_nombre" select="'Nombre Representante'"/>
	<xsl:variable name="lang.repres_movil" select="'Número de teléfono móvil Representante'"/>
	<xsl:variable name="lang.repres_d_email" select="'Dirección de correo electrónico Representante'"/>
	<xsl:variable name="lang.nif" select="'NIF/CIF Contribuyente'"/>
	<xsl:variable name="lang.nombre" select="'Nombre Contribuyente'"/>
	<xsl:variable name="lang.direccion" select="'Dirección'"/>
	<xsl:variable name="lang.calle" select="'Domicilio'"/>
	<xsl:variable name="lang.numero" select="'Numero'"/>
	<xsl:variable name="lang.escalera" select="'Escalera'"/>
	<xsl:variable name="lang.planta_puerta" select="'Planta/Puerta'"/>
	<xsl:variable name="lang.c_postal" select="'Código Postal'"/>
	<xsl:variable name="lang.ciudad" select="'Ciudad'"/>
	<xsl:variable name="lang.region" select="'Región / País'"/>
	<xsl:variable name="lang.movil" select="'Número de teléfono móvil'"/>
	<xsl:variable name="lang.d_email" select="'Dirección de correo electrónico'"/>
	
	<xsl:variable name="lang.municipio" select="'Municipio'"/>
	<xsl:variable name="lang.n_liq_1" select="'Nº liquidación 1'"/>
	<xsl:variable name="lang.n_liq_2" select="'Nº liquidación 2'"/>
	<xsl:variable name="lang.n_liq_3" select="'Nº liquidación 3'"/>
	<xsl:variable name="lang.n_liq_4" select="'Nº liquidación 4'"/>
	<xsl:variable name="lang.n_liq_5" select="'Nº liquidación 5'"/>
	<xsl:variable name="lang.n_liq_6" select="'Nº liquidación 6'"/>
	<xsl:variable name="lang.n_liq_7" select="'Nº liquidación 7'"/>
	<xsl:variable name="lang.n_liq_8" select="'Nº liquidación 8'"/>
	<xsl:variable name="lang.n_liq_9" select="'Nº liquidación 9'"/>
	<xsl:variable name="lang.n_liq_10" select="'Nº liquidación 10'"/>
	<xsl:variable name="lang.n_liq_11" select="'Nº liquidación 11'"/>
	<xsl:variable name="lang.n_liq_12" select="'Nº liquidación 12'"/>
	<xsl:variable name="lang.n_liq_13" select="'Nº liquidación 13'"/>
	<xsl:variable name="lang.n_liq_14" select="'Nº liquidación 14'"/>
	<xsl:variable name="lang.n_liq_15" select="'Nº liquidación 15'"/>
	<xsl:variable name="lang.n_liq_16" select="'Nº liquidación 16'"/>
	<xsl:variable name="lang.n_liq_17" select="'Nº liquidación 17'"/>
	<xsl:variable name="lang.n_liq_18" select="'Nº liquidación 18'"/>
	<xsl:variable name="lang.n_liq_19" select="'Nº liquidación 19'"/>
	<xsl:variable name="lang.n_liq_20" select="'Nº liquidación 20'"/>
	<xsl:variable name="lang.n_liq_21" select="'Nº liquidación 21'"/>
	<xsl:variable name="lang.n_liq_22" select="'Nº liquidación 22'"/>
	<xsl:variable name="lang.n_liq_23" select="'Nº liquidación 23'"/>
	<xsl:variable name="lang.n_liq_24" select="'Nº liquidación 24'"/>
	<xsl:variable name="lang.solicitud" select="'Solicitud'"/>
	<xsl:variable name="lang.aplazamiento" select="'Aplazamiento de pago'"/>
	<xsl:variable name="lang.fraccionamiento" select="'Fraccionamiento de pago'"/>
	<xsl:variable name="lang.opcion-a11" select="'Vía voluntaria - Aplazamiento en el siguiente periodo de pago voluntario'"/>
	<xsl:variable name="lang.opcion-a21" select="'Vía voluntaria - Fraccionamiento en los dos siguientes periodos de pago voluntario'"/>
	<xsl:variable name="lang.opcion-a22" select="'Vía voluntaria - Fracciona en los tres siguientes periodos de pago voluntario'"/>
	<xsl:variable name="lang.opcion-b11" select="'Vía ejecutiva - Aplazamiento. Fecha límite de pago'"/>
	<xsl:variable name="lang.opcion-b21" select="'Vía ejecutiva - Fraccionamiento. Pago mensual'"/>
	<xsl:variable name="lang.opcion-b22" select="'Vía ejecutiva - Fraccionamiento. Pago trimestral'"/>
	<xsl:variable name="lang.opcion-b23" select="'Vía ejecutiva - Fraccionamiento. Pago semenstral'"/>
	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>
	<xsl:variable name="lang.anexos" select="'Anexos'"/>
	
	<xsl:template match="/">
		<div class="col">
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.datosIdentificativos"/></b>	
			</label>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.pres_nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Documento_Identificacion/Numero"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.pres_nombre"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Nombre"/>
			</label>
			<br/>
		</div>
		<br/>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.repres_nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_nif"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.repres_nombre"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_nombre"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
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
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.repres_movil"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_movil"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.repres_d_email"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_d_email"/>
			</label>
			<br/>
		</div>
		<br/>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nif"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.nombre"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.direccion"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/calle"/>,
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ciudad"/>, 
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/region"/>, 
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/c_postal"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.movil"/>:
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/movil"/>
			</label>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.d_email"/>:
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/d_email"/>
			</label>
		</div>
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
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/municipio_liq"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_1"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_1"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_2"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_2"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_3"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_3"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_4"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_4"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_5"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_5"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_6"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_6"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_7"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_7"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_8"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_8"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_9"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_9"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_10"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_10"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_11"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_11"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_12"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_12"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_13"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_13"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_14"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_14"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_15"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_15"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_16"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_16"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_17"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_17"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_18"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_18"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_19"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_19"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_20"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_20"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_21"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_21"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_22"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_22"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_23"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_23"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.n_liq_24"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/n_liq_24"/>
			</label>
			<br/>
		</div>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/aplazamiento='Si'">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.solicitud"/>:
				</label>			
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="$lang.aplazamiento"/>
				</label>			
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/fraccionamiento='Si'">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.solicitud"/>:
				</label>			
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="$lang.fraccionamiento"/>
				</label>			
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/opcion-a11='Si'">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;"></label>			
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="$lang.opcion-a11"/>
				</label>			
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/opcion-a21='Si'">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;"></label>			
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="$lang.opcion-a21"/>
				</label>			
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/opcion-a22='Si'">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;"></label>			
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="$lang.opcion-a22"/>
				</label>			
			</div>
		</xsl:if>
		<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/opcion-b11='')">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;"></label>			
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="$lang.opcion-b11"/>: <xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/opcion-b11"/>
				</label>			
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/opcion-b21='Si'">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;"></label>			
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="$lang.opcion-b21"/>
				</label>			
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/opcion-b22='Si'">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;"></label>			
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="$lang.opcion-b22"/>
				</label>			
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/opcion-b23='Si'">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;"></label>			
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="$lang.opcion-b23"/>
				</label>			
			</div>
		</xsl:if>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.observaciones"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/observaciones"/>
			</label>
			<br/>
		</div>
		<br/>
		<br/>
	</xsl:template>
</xsl:stylesheet>