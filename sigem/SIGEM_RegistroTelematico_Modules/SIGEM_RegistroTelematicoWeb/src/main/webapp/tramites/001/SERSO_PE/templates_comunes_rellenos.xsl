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

		<xsl:variable name="lang.menores316anios" select="'Menores de 3 a 16 años inclusives'"/>	
		<xsl:variable name="lang.menores3anios" select="'Menores de 3 años'"/>	
		<xsl:variable name="lang.numMenores1" select="'Número de menores de 3 años: 1'"/>
		<xsl:variable name="lang.numMenores2" select="'Número de menores de 3 años: 2 o más'"/>

		<xsl:variable name="lang.pensionAlimenticia" select="'Pensión alimenticia'"/>
		<xsl:variable name="lang.total" select="'Total'"/>

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

			<div class="col" style="visibility:'hidden';display: none">
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
			<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
					<div class="col">
						<label class="gr">
							<xsl:attribute name="style">width:600px;</xsl:attribute>
							<b><xsl:value-of select="$lang.menores3anios"/>: </b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/menores3anios"/>
						</label>

						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/menores3anios='No')">
							<xsl:if test="(Solicitud_Registro/Datos_Firmados/Datos_Especificos/numMenores='1')">
								<label class="gr">
									<xsl:attribute name="style">width:600px;</xsl:attribute>
									<b><xsl:value-of select="$lang.numMenores1"/> </b>
								</label>
							</xsl:if>
							<xsl:if test="(Solicitud_Registro/Datos_Firmados/Datos_Especificos/numMenores='2')">
								<label class="gr">
									<xsl:attribute name="style">width:600px;</xsl:attribute>
									<b><xsl:value-of select="$lang.numMenores2"/> </b>
								</label>
							</xsl:if>
						</xsl:if>	
					</div>	
				</xsl:if>
			</xsl:if>

			<xsl:if test="(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
				<div class="col">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						<b><xsl:value-of select="$lang.menores316anios"/>: </b>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numMenores"/>
					</label>
				</div>	
			</xsl:if>
			<xsl:if test="(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
				<div class="col">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						<b><xsl:value-of select="$lang.menores316anios"/>: </b>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numMenores"/>
					</label>
				</div>	
			</xsl:if>

			<xsl:call-template name="TABLA_FAMILIA" />
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.pensionAlimenticia"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ing_20"/>
				</label>
			</div>
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
					<xsl:if test="(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">	
						<label class="gr">
							<xsl:attribute name="style">width:600px;</xsl:attribute>
							- BENEFICIARIOS DEL PLAN DE AYUDAS DE EMERGENCIA SOCIAL DE LA DIPUTACIÓN
						</label>
						<br/>
					</xsl:if>
					<xsl:if test="(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">	
						<label class="gr">
							<xsl:attribute name="style">width:600px;</xsl:attribute>
							- BENEFICIARIOS DEL PLAN DE AYUDAS DE EMERGENCIA SOCIAL DE LA DIPUTACIÓN
						</label>
						<br/>
					</xsl:if>
					<xsl:if test="(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='ALIMENTACION')">	
						<label class="gr">
							<xsl:attribute name="style">width:600px;</xsl:attribute>
							- SOLICITANTES HABITUAL DE AYUDAS ECONÓMICAS. USUARIOS/AS HABITUALES DE SERVICIOS SOCIALES
						</label>
						<br/>
					</xsl:if>
					<xsl:if test="(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='EXCEPCIONAL')">	
						<label class="gr">
							<xsl:attribute name="style">width:600px;</xsl:attribute>
							- SOLICITANTES HABITUAL DE AYUDAS ECONÓMICAS. USUARIOS/AS HABITUALES DE SERVICIOS SOCIALES
						</label>
						<br/>
					</xsl:if>

				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/totalFamilia11='No')">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						- FAMILIA INCLUIDA EN EL PROGRAMA DE INTERVENCIÓN FAMILIAR
					</label>
					<br/>
				</xsl:if>				
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/totalFamilia2='No')">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						- FAMILIA NORMALIZADA EN SITUACIÓN DE NECESIDAD DERIVADA DE DESEMPLEO PROLONGADO.
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
						- FAMILIA MONOPARENTAL CON HIJOS MENORES A CARGO
					</label>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:100%; padding-left:25px;</xsl:attribute>
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
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/vivienda31='No')">
					<label class="gr">
						<xsl:attribute name="style">width:600px;</xsl:attribute>
						- DESAHUCIADO O EN PROCESO
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
			
			<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='ALIMENTACION')">
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='EXCEPCIONAL')">
					<div id="tabla_libros_comedor">
						<xsl:call-template name="TABLA_LIBROS_COMEDOR" />
						<br/>
					</div>
				</xsl:if>
			</xsl:if>

			<div class="col">
				<xsl:if test="(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='EXCEPCIONAL')">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:600px;</xsl:attribute>
						<b><xsl:value-of select="$lang.diagnostico2"/>: </b>
					</label>
				</xsl:if>

					<label class="gr">
						<xsl:attribute name="style">position: relative; width:600px;</xsl:attribute>
						<b><xsl:value-of select="$lang.diagnostico"/>: </b>
					</label>

				<label class="gr" style="position: relative; width:600px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/diagnostico"/>				
				</label>
				<br/>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="TABLA_LIBROS_COMEDOR">
		<xsl:variable name="lang.texto1" select="'Relación de menores para los que se solicita la ayuda / beca'"/>

		<xsl:variable name="lang.tabla1" select="'Nombre'"/>
		<xsl:variable name="lang.tabla2" select="'Edad/Curso'"/>
		<xsl:variable name="lang.tabla3" select="'Material Escolar'"/>
		<xsl:variable name="lang.tabla4" select="'Coste Libros'"/>
		<xsl:variable name="lang.tabla5" select="'Empresa de Comedor'"/>
		<xsl:variable name="lang.tabla6" select="'Importe Solicitado'"/>
		<xsl:variable name="lang.tabla7" select="'Colegio'"/>

		<xsl:variable name="lang.total" select="'Total: '"/>
			
		<xsl:variable name="lang.hapagado" select="'Modalidad de pago a la que se acogen:'"/>
		<xsl:variable name="lang.si" select="'    - Pago a la familia'"/>
		<!--<xsl:variable name="lang.siNota" select="' (los libros los han adquirido y pagado antes de la fecha de publicación de la convocatoria).'"/>-->
		<xsl:variable name="lang.siNota" select="''"/>
		<xsl:variable name="lang.no" select="'    - Pago a la librería'"/>
		<!--<xsl:variable name="lang.noNota" select="' (aún no se ha pagado a los proveedores).'"/>-->
		<xsl:variable name="lang.noNota" select="''"/>

		<!--<xsl:variable name="lang.hapagado" select="'¿La familia ha pagado los libros?'"/>
		<xsl:variable name="lang.si" select="'SÍ'"/>
		<xsl:variable name="lang.no" select="'NO'"/>-->

		<xsl:variable name="lang.costeMensual" select="'Coste día / menor (inferior a 4,77 €)'"/>
		<xsl:variable name="lang.mesInicio" select="'Mes de inicio y finalización del comedor'"/>
		<xsl:variable name="lang.mesDesde" select="'   - desde '"/>
		<xsl:variable name="lang.mesHasta" select="' hasta '"/>
		<xsl:variable name="lang.costeAnualMenor" select="'Coste anual por menor'"/>
		<xsl:variable name="lang.importeTotalComedor" select="'IMPORTE TOTAL SOLICITADO'"/>

		<xsl:variable name="lang.proveedor" select="'Nombre del proveedor'"/>
		<xsl:variable name="lang.noFactura" select="'Nº de Factura'"/>
		<xsl:variable name="lang.fechaFactura" select="'Fecha'"/>

		<div class="col">
			<label class="gr">
				<xsl:attribute name="style">width:100%;</xsl:attribute>
				<b><xsl:value-of select="$lang.texto1"/>:</b>
			</label>
			<table style="font-size:12px; margin:5 auto 20 auto;" cellspacing="0" border="1">
				<tr>
					<td style="width:5%;text-align:center" id="idcolumna">
					</td>
					<td style="width:20%;background-color:#dee1e8;text-align:center" id="nombrecolumna">
						<label class="gr">
							<xsl:attribute name="style">width:100%;text-align:center</xsl:attribute>
							<xsl:value-of select="$lang.tabla1"/>
						</label>
					</td>
					<td style="width:5%;background-color:#dee1e8;text-align:center" id="edadcolumna">
						<label class="gr">
							<xsl:attribute name="style">width:100%;text-align:center</xsl:attribute>
							<xsl:value-of select="$lang.tabla2"/>
						</label>
					</td>
					<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
						<td style="width:20%;background-color:#dee1e8;text-align:center" id="cursocolumna">
							<label class="gr">
								<xsl:attribute name="style">width:100%;text-align:center</xsl:attribute>
								<xsl:value-of select="$lang.tabla3"/>
							</label>
						</td>
					</xsl:if>
					<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
						<td style="width:20%;background-color:#dee1e8;text-align:center" id="cursocolumna">
							<label class="gr">
								<xsl:attribute name="style">width:100%;text-align:center</xsl:attribute>
								<xsl:value-of select="$lang.tabla7"/>
							</label>
						</td>
					</xsl:if>
					<td style="width:20%;background-color:#dee1e8;text-align:center" id="costecolumna">
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<label class="gr">
								<xsl:attribute name="id">costelibros</xsl:attribute>
								<xsl:attribute name="style">width:100%;text-align:center</xsl:attribute>
								<xsl:value-of select="$lang.tabla4"/>
							</label>
						</xsl:if>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
							<label class="gr">
								<xsl:attribute name="id">empresacomedor</xsl:attribute>
								<xsl:attribute name="style">width:100%;text-align:center</xsl:attribute>
								<xsl:value-of select="$lang.tabla5"/>
							</label>
						</xsl:if>
					</td>
					<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
						<td style="width:20%;background-color:#dee1e8;text-align:center" id="importelibrossolcolumna">
							<label class="gr">
								<xsl:attribute name="id">importelibrossol</xsl:attribute>
								<xsl:attribute name="style">width:100%;text-align:center</xsl:attribute>
								<xsl:value-of select="$lang.tabla6"/>
							</label>
						</td>
					</xsl:if>
				</tr>
				<tr>
					<td style="width:5%;background-color:#dee1e8;text-align:center">
						<label class="gr">
							<xsl:attribute name="id">id</xsl:attribute>
							<xsl:attribute name="style">width:100%</xsl:attribute>
							1
						</label>
					</td>
					<td>
						<label class="gr" >
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre1"/>				
						</label>
					</td>
					<td>
						<label class="gr" >
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/edad1"/>				
						</label>
					</td>
					<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/curso1"/>				
							</label>
						</td>
					</xsl:if>
					<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/colegio1"/>				
							</label>
						</td>
					</xsl:if>
					<td>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/costelibros1"/>				
							</label>
						</xsl:if>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/empresacomedor1"/>				
							</label>
						</xsl:if>
					</td>
					<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
						<td>
							<label class="gr">
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/importelibrossol1"/>
							</label>
						</td>
					</xsl:if>
				</tr>
				<tr>
					<td style="width:5%;background-color:#dee1e8;text-align:center">
						<label class="gr">
							<xsl:attribute name="id">id</xsl:attribute>
							<xsl:attribute name="style">width:100%</xsl:attribute>
							2
						</label>
					</td>
					<td>
						<label class="gr" >
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre2"/>				
						</label>
					</td>
					<td>
						<label class="gr" >
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/edad2"/>				
						</label>
					</td>
					<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/curso2"/>				
							</label>
						</td>
					</xsl:if>
					<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/colegio2"/>				
							</label>
						</td>
					</xsl:if>
					<td>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/costelibros2"/>	
							</label>
						</xsl:if>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/empresacomedor2"/>
							</label>
						</xsl:if>
					</td>
					<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
						<td>
							<label class="gr">
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/importelibrossol2"/>
							</label>
						</td>
					</xsl:if>
				</tr>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
					<tr>
						<td style="width:5%;background-color:#dee1e8;text-align:center">
							<label class="gr">
								<xsl:attribute name="id">id</xsl:attribute>
								<xsl:attribute name="style">width:100%</xsl:attribute>
								3
							</label>
						</td>
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre3"/>				
							</label>
						</td>
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/edad3"/>				
							</label>
						</td>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<td>
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/curso3"/>	
								</label>
							</td>
						</xsl:if>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
							<td>
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/colegio3"/>				
								</label>
							</td>
						</xsl:if>
						<td>
							<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/costelibros3"/>
								</label>
							</xsl:if>
							<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/empresacomedor3"/>
								</label>
							</xsl:if>
						</td>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<td>
								<label class="gr">
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/importelibrossol3"/>
								</label>
							</td>
						</xsl:if>
					</tr>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
					<tr>
						<td style="width:5%;background-color:#dee1e8;text-align:center">
							<label class="gr">
								<xsl:attribute name="id">id</xsl:attribute>
								<xsl:attribute name="style">width:100%</xsl:attribute>
								4
							</label>
						</td>	
						<td>	
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre4"/>
							</label>
						</td>
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/edad4"/>				
							</label>
						</td>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<td>
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/curso4"/>
								</label>
							</td>
						</xsl:if>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
							<td>
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/colegio4"/>				
								</label>
							</td>
						</xsl:if>
						<td>
							<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/costelibros4"/>
								</label>
							</xsl:if>
							<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/empresacomedor4"/>
								</label>
							</xsl:if>
						</td>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<td>
								<label class="gr">
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/importelibrossol4"/>
								</label>
							</td>
						</xsl:if>
					</tr>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
					<tr>
						<td style="width:5%;background-color:#dee1e8;text-align:center">
							<label class="gr">
								<xsl:attribute name="id">id</xsl:attribute>
								<xsl:attribute name="style">width:100%</xsl:attribute>
								5
							</label>
						</td>
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre5"/>
							</label>
						</td>
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/edad5"/>				
							</label>
						</td>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<td>
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/curso5"/>
								</label>
							</td>
						</xsl:if>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
							<td>
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/colegio5"/>				
								</label>
							</td>
						</xsl:if>
						<td>
							<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/costelibros5"/>
								</label>
							</xsl:if>
							<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/empresacomedor5"/>
								</label>
							</xsl:if>
						</td>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<td>
								<label class="gr">
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/importelibrossol5"/>
								</label>
							</td>
						</xsl:if>
					</tr>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
					<tr>
						<td style="width:5%;background-color:#dee1e8;text-align:center">
							<label class="gr">
								<xsl:attribute name="id">id</xsl:attribute>
								<xsl:attribute name="style">width:100%</xsl:attribute>
								6
							</label>
						</td>
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre6"/>
							</label>
						</td>
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/edad6"/>				
							</label>
						</td>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<td>
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/curso6"/>
								</label>
							</td>
						</xsl:if>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
							<td>
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/colegio6"/>				
								</label>
							</td>
						</xsl:if>
						<td>
							<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/costelibros6"/>
								</label>
							</xsl:if>
							<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/empresacomedor6"/>
								</label>
							</xsl:if>
						</td>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<td>
								<label class="gr">
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/importelibrossol6"/>
								</label>
							</td>
						</xsl:if>
					</tr>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
					<tr>
						<td style="width:5%;background-color:#dee1e8;text-align:center">
							<label class="gr">
								<xsl:attribute name="id">id</xsl:attribute>
								<xsl:attribute name="style">width:100%</xsl:attribute>
								7
							</label>
						</td>
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre7"/>
							</label>
						</td>
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/edad7"/>				
							</label>
						</td>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<td>
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/curso7"/>
								</label>
							</td>
						</xsl:if>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
							<td>
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/colegio7"/>				
								</label>
							</td>
						</xsl:if>
						<td>
							<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/costelibros7"/>
								</label>
							</xsl:if>
							<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/empresacomedor7"/>
								</label>
							</xsl:if>
						</td>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<td>
								<label class="gr">
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/importelibrossol7"/>
								</label>
							</td>
						</xsl:if>
					</tr>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
					<tr>
						<td style="width:5%;background-color:#dee1e8;text-align:center">
							<label class="gr">
								<xsl:attribute name="id">id</xsl:attribute>
								<xsl:attribute name="style">width:100%</xsl:attribute>
								8
							</label>
						</td>
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre8"/>
							</label>
						</td>
						<td>
							<label class="gr" >
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/edad8"/>				
							</label>
						</td>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<td>
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/curso8"/>
								</label>
							</td>
						</xsl:if>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
							<td>
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/colegio8"/>				
								</label>
							</td>
						</xsl:if>
						<td>
							<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
									<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/costelibros8"/>
								</label>
							</xsl:if>
							<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/empresacomedor8"/>
								</label>
							</xsl:if>
						</td>
						<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
							<td>
								<label class="gr" >
									<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/importelibrossol8"/>
								</label>
							</td>
						</xsl:if>
					</tr>
				</xsl:if>
				<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
					<tr id="filatotal">
						<td colspan='5' align='right'>
							<label class="gr">
								<xsl:attribute name="style">width:100%;text-align:right</xsl:attribute>
								<xsl:value-of select="$lang.total"/>
							</label>
						</td>
						<td valign="middle">
							<label class="gr">
								<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/totallibros"/>
							</label>
						</td>
					</tr>
				</xsl:if>
			</table>
		</div>
		<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
			<div class="col" id="datosLibros">
				<label class="gr">
					<xsl:attribute name="style">width:100%;</xsl:attribute>
					<b><xsl:value-of select="$lang.hapagado"/> </b>
					<br/>
					<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/pagado='L')">
						<b><xsl:value-of select="$lang.si"/></b><xsl:value-of select="$lang.siNota"/>
					</xsl:if>
					<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/pagado='F')">
						<b><xsl:value-of select="$lang.no"/></b><xsl:value-of select="$lang.noNota"/>
					</xsl:if>
				</label>
			</div>
			<div class="col" style="display:none" >
				<label class="gr">
					<xsl:attribute name="style">width:25%;</xsl:attribute>
					<b><xsl:value-of select="$lang.proveedor"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombreProveedor"/>
				</label>
			</div>
			<div class="col" style="display:none" >
				<label class="gr">
					<xsl:attribute name="style">width:25%;</xsl:attribute>
					<b><xsl:value-of select="$lang.noFactura"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/noFactura"/>
				</label>
			</div>
			<div class="col" style="display:none" >
				<label class="gr">
					<xsl:attribute name="style">width:25%;</xsl:attribute>
					<b><xsl:value-of select="$lang.fechaFactura"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/fechaFactura"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
			<div class="col" id="datosComedor">			
				<label class="gr">
					<xsl:attribute name="style">width:100%;</xsl:attribute>
					<b><xsl:value-of select="$lang.costeMensual"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/costeMensual"/> €
				</label>
			</div>
			<div class="col" >
				<label class="gr">
					<xsl:attribute name="style">width:100%;</xsl:attribute>
					<b><xsl:value-of select="$lang.mesInicio"/>: </b>
				</label>
				<br/>
				<label class ="gr">
					<xsl:attribute name="style">width:100%;left:10px;</xsl:attribute>
					<b><xsl:value-of select="$lang.mesDesde"/> </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/mesInicio"/>
					<b> <xsl:value-of select="$lang.mesHasta"/> </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/mesesSolComedor"/>
				</label>
			</div>
			<div class="col" >
				<label class="gr">
					<xsl:attribute name="style">width:100%;</xsl:attribute>
					<b><xsl:value-of select="$lang.costeAnualMenor"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/costeAnualMenor"/> €
				</label>
				<br/>
			</div>
			<div class="col" >
				<label class="gr">
					<xsl:attribute name="style">width:100%;</xsl:attribute>
					<b><xsl:value-of select="$lang.importeTotalComedor"/>: </b>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/importeTotalComedor"/> €
				</label>
				<br/>
			</div>
		</xsl:if>
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
				<!--<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">20</xsl:with-param></xsl:call-template>-->
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
