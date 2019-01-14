<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:template name="DATOS_SOLICITANTE">
		<xsl:variable name="lang.titulo" select="'Solicitud de ayudas al Plan de Emergencia'"/>
		<xsl:variable name="lang.id_nif" select="'NIF del Trabajador/a Social'"/>
		<xsl:variable name="lang.id_nombre" select="'Nombre del Trabajador/a Social'"/>

		<div class="submenu">
   			<h1><xsl:value-of select="$lang.titulo"/></h1>
   		</div>
   		<div class="cuadro" style="">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.id_nif"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Documento_Identificacion/Numero"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.id_nombre"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Nombre"/>
				</label>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="DATOS_BENEFICIARIO">
		<xsl:variable name="lang.datosBeneficiario" select="'Datos del beneficiario'"/>
		<xsl:variable name="lang.nif" select="'NIF'"/>
		<xsl:variable name="lang.nombre" select="'Apellidos y Nombre'"/>
		<xsl:variable name="lang.ciudad" select="'Localidad'"/>		
	
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosBeneficiario"/></h1>
   		</div>
   		<div class="cuadro" style="">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.nif"/>:* </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nif"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.nombre"/>:* </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre"/>
				</label>
			</div>			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.ciudad"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_ciudad "/>
				</label>
			</div>					
		</div>
	</xsl:template>

	<xsl:template name="DATOS_FAMILIA">

		<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

		<xsl:variable name="lang.convocatoria" select="'Convocatorias abiertas'"/>
		<xsl:variable name="lang.convocatoriaObligatoria" select="'Convocatorias'"/>

		<xsl:variable name="lang.tipoAyuda" select="'Tipo de Ayuda'"/>	

		<xsl:variable name="lang.nfamiliar" select="'Número de miembros de la unidad familiar'"/>
		<xsl:variable name="lang.menores3anios" select="'Menores de 3 años'"/>		

		<xsl:variable name="lang.total" select="'Total mensual'"/>

		<xsl:variable name="lang.tipoFamilia" select="'Tipo de familia'"/>
		<xsl:variable name="lang.colectivo" select="'Colectivo'"/>
		<xsl:variable name="lang.vivienda" select="'Vivienda'"/>
		<xsl:variable name="lang.diagnostico" select="'Diagnóstico inicial/Valoración Social'"/>
		<xsl:variable name="lang.diagnostico2" select="'Informe Social Completo'"/>
	
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>

		<div class="cuadro" style="">
			<div class="col">
					<label class="gr" style="position: relative; width:200px;font-weight: bold;">
						<xsl:value-of select="$lang.convocatoria"/>:	
					</label>
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:550px;</xsl:attribute>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_convocatoria"/>
					</label>
					<br/>
			</div>

			<div class="col">
					<label class="gr" style="position: relative; width:400px;">
						<b><xsl:value-of select="$lang.tipoAyuda"/>:	</b><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tipoAyuda"/>
					</label>
					<br/>
			</div>

   		
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.nfamiliar"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nfamiliar"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.menores3anios"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/menores3anios"/>
				</label>
			</div>	
			<xsl:call-template name="TABLA_FAMILIA" />
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.total"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/total"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.tipoFamilia"/>: </b>
				</label>
				<br/>
				<br/>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/totalFamilia1='No')">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						- USUARIOS/AS HABITUALES DE SERVICIOS SOCIALES
					</label>
					<br/>
				</xsl:if>				
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/totalFamilia2='No')">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						- FAMILIA NORMALIZADA EN SITUACIÓN DE NECESIDAD DERIVADA DE DESEMPLEO RECIENTE (2 AÑOS).
					</label>
					<br/>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/totalFamilia3='No')">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						- FAMILIA NORMALIZADA QUE NECESITA UN APOYO ECONÓMICO PUNTUAL
					</label>
					<br/>
				</xsl:if>				
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.colectivo"/>: </b>
				</label>
				<br/>
				<br/>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/colectivo1='No')">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						- INMIGRANTES
					</label>
					<br/>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/colectivo2='No')">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						- ETNIA GITANA
					</label>
					<br/>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/colectivo3='No')">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						- FAMILIA MONOPARENTAL CON HIJOS A CARGO
					</label>
					<br/>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/colectivo4='')">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						- OTRO(Especificar):
						<br/>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/colectivo4"/>
					</label>
					<br/>
				</xsl:if>				
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.vivienda"/>: </b>
				</label>
				<br/><br/>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/vivienda1='No')">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						- PROPIA PAGADA
					</label>
					<br/>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/vivienda2='No')">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						- PROPIA CON HIPOTECA DE <xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/viviendaEuros2"/> € MES
					</label>
					<br/>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/vivienda3='No')">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						- ALQUILADA <xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/viviendaEuros3"/> € MES
					</label>
					<br/>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/vivienda4='')">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						- OTRA(Especificar):
						<br/>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/vivienda4"/>
					</label>
					<br/>
				</xsl:if>
			</div>
			<div class="col">
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='EXCEPCIONAL')">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:600px;</xsl:attribute>
						<b><xsl:value-of select="$lang.diagnostico"/>: </b>
					</label>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='ALIMENTACION')">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:600px;</xsl:attribute>
						<b><xsl:value-of select="$lang.diagnostico2"/>: </b>
					</label>
				</xsl:if>
				<label class="gr" style="position: relative; width:600px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/diagnostico"/>				
				</label>
				<br/>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="TABLA_FAMILIA">
		<xsl:variable name="lang.composicion_familiar" select="'Composición familiar'"/>
		<xsl:variable name="lang.parentesco" select="'PARENTESCO'"/>
		<xsl:variable name="lang.edad" select="'EDAD'"/>
		<xsl:variable name="lang.profesion" select="'PROFESIÓN'"/>
		<xsl:variable name="lang.situacionLab" select="'SITUACIÓN LABORAL'"/>
		<xsl:variable name="lang.ingresos" select="'INGRESOS/MES'"/>
		
		<div class="col">
			<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<b><xsl:value-of select="$lang.composicion_familiar"/>: </b>
			</label>
			<br/><br/>
			<table style="font-size:12px; margin:5 auto 20 auto;" cellspacing="0" border="1">
				<tr>
					<td style="width:20%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.parentesco"/></td>
					<td style="width:10%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.edad"/></td>
					<td style="width:30%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.profesion"/></td>
					<td style="width:20%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.situacionLab"/></td>
					<td style="width:20%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.ingresos"/></td>
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
	</xsl:template>

	<xsl:template name="FIELDS">
		<xsl:param name="row_id" />
			<xsl:variable name="param_parentesco">par_<xsl:value-of select="$row_id"/></xsl:variable>
			<xsl:variable name="param_edad">edad_<xsl:value-of select="$row_id"/></xsl:variable>
			<xsl:variable name="param_profesion">prof_<xsl:value-of select="$row_id"/></xsl:variable>
			<xsl:variable name="param_situLab">sit_<xsl:value-of select="$row_id"/></xsl:variable>
			<xsl:variable name="param_ingresos">ing_<xsl:value-of select="$row_id"/></xsl:variable>
			<xsl:variable name="row_style">
		<xsl:choose>
	       <xsl:when test="$row_id&gt;'5'">
   		        display:none;
       	</xsl:when>
		</xsl:choose>
		</xsl:variable>
		
			<tr id="row_{$row_id}" style="{$row_style}">
				<td>
		       		<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_parentesco]"/>
				</td>
				<td>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_edad]"/>
				</td>
				<td>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_profesion]"/>
				</td>
				<td>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_situLab]"/>
				</td>
				<td>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ingresos]"/>
				</td>
			</tr>	

	</xsl:template>

	<xsl:template name="DATOS_PROPUESTA_1">
		<xsl:variable name="lang.propuesta" select="'PROPUESTA DE AYUDA ECONÓMICA'"/>
		<xsl:variable name="lang.semestre1" select="'SEMESTRE DEL 1 DE JUNIO DE 2012 A 30 DE NOVIEMBRE DE 2012'"/>
		<xsl:variable name="lang.propuesta1" select="'Propuesta'"/>		
		<xsl:variable name="lang.propuesta2" select="'Segunda propuesta'"/>		
		<xsl:variable name="lang.propuesta3" select="'Tercera propuesta'"/>		

		<xsl:variable name="lang.semestre2" select="'SEMESTRE DEL 1 DE DICIEMBRE DE 2012 A 31 DE MAYO DE 2013'"/>

		<xsl:variable name="lang.fecha" select="' - Fecha: '"/>
		<xsl:variable name="lang.meses" select="' - Mes a cubrir: '"/>		
		<xsl:variable name="lang.meses2" select="' - Concepto: '"/>
		<xsl:variable name="lang.importe" select="' - Importe solicitado: '"/>
	
   		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><script>
							var d = new Date();
							var n = d.getMonth();
							if ( n == 0 || n==1 || n==2){
								document.write('PRIMER TRIMESTRE');
							}
							else if ( n == 3 || n== 4 || n== 5){
								document.write('SEGUNDO TRIMESTRE');
							}
							else if ( n == 6 || n==7 || n==8){
								document.write('TERCER TRIMESTRE');
							}
							else{
								document.write('CUARTO TRIMESTRE');
							}
					</script></b>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.propuesta"/></b>
				</label>
			</div>
			<div class="col">
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='EXCEPCIONAL')">
					<label class="gr" style="position: relative; width:600px;">
						<b><xsl:value-of select="$lang.propuesta1"/></b>
					</label>
				</xsl:if>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.fecha"/>	</b>				
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta1_fecha"/>
				</label>
				<br/>

				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='EXCEPCIONAL')">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:600px;</xsl:attribute>
						<b><xsl:value-of select="$lang.meses"/>: </b><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta1_meses"/>
					</label>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='ALIMENTACION')">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:600px;</xsl:attribute>
						<b><xsl:value-of select="$lang.meses2"/>: </b><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta1_meses"/>
					</label>
				</xsl:if>
				<br/>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.importe"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta1_importe"/> €
				</label>
			</div>

		<!--<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='EXCEPCIONAL')">
			<div class="col" id="divProp1">
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.propuesta2"/></b>
				</label>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.fecha"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta2_fecha"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.meses"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta2_meses"/>
				</label>
					<br/>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.importe"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta2_importe"/> €
				</label>
			</div>
		</xsl:if>

			<div class="col">
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.propuesta3"/></b>
				</label>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.fecha"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta3_fecha"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.meses"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta3_meses"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.importe"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta3_importe"/>
				</label>
			</div>-->
		</div>
	</xsl:template>

	<xsl:template name="DATOS_FAMILIA2">
		<xsl:variable name="lang.nfamiliar" select="'Número de miembros de la unidad familiar'"/>
		<xsl:variable name="lang.menores3anios" select="'Menores de 3 años'"/>		

		<xsl:variable name="lang.total" select="'Total miembros de la familia'"/>

		<xsl:variable name="lang.tipoFamilia" select="'Tipo de familia'"/>
		<xsl:variable name="lang.colectivo" select="'Colectivo'"/>
		<xsl:variable name="lang.vivienda" select="'Vivienda'"/>
		<xsl:variable name="lang.diagnostico" select="'Diagnóstico inicial/Valoración Social'"/>

		<xsl:variable name="lang.propuesta" select="'PROPUESTA DE AYUDA ECONÓMICA'"/>
		<xsl:variable name="lang.propuesta1" select="'Propuesta'"/>		
		<xsl:variable name="lang.propuesta2" select="'Segunda propuesta'"/>		
		<xsl:variable name="lang.propuesta3" select="'Tercera propuesta'"/>		

		<xsl:variable name="lang.semestre2" select="'SEMESTRE DEL 1 DE DICIEMBRE DE 2012 A 31 DE MAYO DE 2013'"/>

		<xsl:variable name="lang.fecha" select="' - Fecha: '"/>
		<xsl:variable name="lang.meses" select="' - Mes a cubrir: '"/>
		<xsl:variable name="lang.importe" select="' - Importe solicitado: '"/>

   		<div class="cuadro" style="">
			<div class="col">
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.semestre2"/></b>
				</label>
			</div>

			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.nfamiliar"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nfamiliar2"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.menores3anios"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/menores3anios2"/>
				</label>
			</div>	
			<xsl:call-template name="TABLA_FAMILIA2" />
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.total"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/total2"/>
				</label>
			</div>			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.diagnostico"/>: </b>
				</label>
				<label class="gr" style="position: relative; width:600px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/diagnostico2"/>				
				</label>
				<br/>
			</div>
		</div>
		<div class="cuadro" style="">
			<div class="col">
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.propuesta"/></b>
				</label>
			</div>
			<div class="col">
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.propuesta1"/></b>
				</label>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.fecha"/>	</b>				
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta1_fecha2"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.meses"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta1_meses2"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.importe"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta1_importe2"/>
				</label>
			</div>

			<div class="col">
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.propuesta2"/></b>
				</label>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.fecha"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta2_fecha2"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.meses"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta2_meses2"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.importe"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta2_importe2"/>
				</label>
			</div>

			<div class="col">
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.propuesta3"/></b>
				</label>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.fecha"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta3_fecha2"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.meses"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta3_meses2"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:600px;">
					<b><xsl:value-of select="$lang.importe"/></b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/propuesta3_importe2"/>
				</label>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="TABLA_FAMILIA2">
		<xsl:variable name="lang.composicion_familiar" select="'Composición familiar'"/>
		<xsl:variable name="lang.parentesco" select="'PARENTESCO'"/>
		<xsl:variable name="lang.edad" select="'EDAD'"/>
		<xsl:variable name="lang.profesion" select="'PROFESIÓN'"/>
		<xsl:variable name="lang.situacionLab" select="'SITUACIÓN LABORAL'"/>
		<xsl:variable name="lang.ingresos" select="'INGRESOS/MES'"/>
		
		<div class="col">
			<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<b><xsl:value-of select="$lang.composicion_familiar"/>: </b>
			</label>
			<br/><br/>
			<table style="font-size:12px; margin:5 auto 20 auto;" cellspacing="0" border="1">
				<tr>
					<td style="width:20%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.parentesco"/></td>
					<td style="width:10%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.edad"/></td>
					<td style="width:30%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.profesion"/></td>
					<td style="width:20%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.situacionLab"/></td>
					<td style="width:20%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.ingresos"/></td>
				</tr>

				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">1</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">2</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">3</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">4</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">5</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">6</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">7</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">8</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">9</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">10</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">11</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">12</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">13</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">14</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">15</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">16</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">17</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">18</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">19</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id2">20</xsl:with-param></xsl:call-template>
			</table>
		</div>
	</xsl:template>

	<xsl:template name="FIELDS2">
		<xsl:param name="row_id2" />
			<xsl:variable name="param_parentesco">par2_<xsl:value-of select="$row_id2"/></xsl:variable>
			<xsl:variable name="param_edad">edad2_<xsl:value-of select="$row_id2"/></xsl:variable>
			<xsl:variable name="param_profesion">prof2_<xsl:value-of select="$row_id2"/></xsl:variable>
			<xsl:variable name="param_situLab">sit2_<xsl:value-of select="$row_id2"/></xsl:variable>
			<xsl:variable name="param_ingresos">ing2_<xsl:value-of select="$row_id2"/></xsl:variable>
			<xsl:variable name="row_style">
		<xsl:choose>
	       <xsl:when test="$row_id2&gt;'5'">
   		        display:none;
       	</xsl:when>
		</xsl:choose>
		</xsl:variable>
		
			<tr id="row_{$row_id2}" style="{$row_style}">
				<td>
		       		<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_parentesco]"/>
				</td>
				<td>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_edad]"/>
				</td>
				<td>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_profesion]"/>
				</td>
				<td>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_situLab]"/>
				</td>
				<td>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_ingresos]"/>
				</td>
			</tr>	

	</xsl:template>

</xsl:stylesheet>
