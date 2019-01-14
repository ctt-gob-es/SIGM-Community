<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="templates_rellenos.xsl" />


<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'CAMPAÑA SOBRE LA ERRADICACIÓN DE LA VIOLENCIA DE GÉNERO E INTERCULTURALIDAD'"/>
	
	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>
	<xsl:variable name="lang.cargo" select="'Cargo'"/>

	<xsl:variable name="lang.convocatoria" select="'Convocatorias abiertas:'"/>
	<xsl:variable name="lang.seleccionar" select="'Seleccionar '"/>
	<xsl:variable name="lang.convocatoriaObligatoria" select="'Convocatorias'"/>

	<xsl:variable name="lang.ayto" select="'Ayuntamiento/EATIM'"/>

	<xsl:variable name="lang.datosRepresentante" select="'Persona de contacto responsable de la actividad en el ayuntamiento / EATIM'"/>
	<xsl:variable name="lang.datosRepresentante2" select="'  (Servicios Sociales o Centro Mujer)'"/>
	<xsl:variable name="lang.nif_repre" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre_repre" select="'Nombre y Apellidos'"/>
	<xsl:variable name="lang.cargo_repre" select="'Cargo'"/>
	<xsl:variable name="lang.domicilio_repre" select="'Domicilio'"/>
	<xsl:variable name="lang.ciudad_repre" select="'Localidad'"/>
	<xsl:variable name="lang.c_postal_repre" select="'Código Postal'"/>
	<xsl:variable name="lang.region_repre" select="'Región / País'"/>
	<xsl:variable name="lang.movil_repre" select="'Teléfono'"/>
	<xsl:variable name="lang.d_email_repre" select="'Correo electrónico'"/>	

	<xsl:variable name="lang.centros" select="'Centros Solicitados'"/>

	<xsl:variable name="lang.centroX" select="' Centro Escolar '"/>

	<xsl:variable name="lang.nombreCentro" select="'Nombre del Centro Escolar'"/>
	<xsl:variable name="lang.direccionCentro" select="'Dirección'"/>
	<xsl:variable name="lang.telefonoCentro" select="'Teléfono'"/>
	<xsl:variable name="lang.faxCentro" select="'Fax'"/>
	<xsl:variable name="lang.emailCentro" select="'Correo Electrónico'"/>
	<xsl:variable name="lang.contactoCentro" select="'Nombre del/a responsable/s o persona/s de contacto del colegio o I.E.S.'"/>
	<xsl:variable name="lang.cargoCentro" select="'Cargo'"/>

	<!-- Se añaden los campos del Bullying -->
	<xsl:variable name="lang.tutor" select="'Tutor/a'"/>
	<xsl:variable name="lang.director" select="'Director/a'"/>
	<xsl:variable name="lang.secretario" select="'Secretario/a'"/>
	<xsl:variable name="lang.jefeEstudios" select="'Jefe/a de Estudios'"/>


	<xsl:variable name="lang.tipoCentro" select="'El centro es'"/>
	<xsl:variable name="lang.publico" select="'Público'"/>
	<xsl:variable name="lang.concertado" select="'Concertado'"/>
	<xsl:variable name="lang.privado" select="'Privado'"/>

	<xsl:variable name="lang.actividadesSolicitadas" select="'Actividades Solicitadas del Centro'"/>

	<xsl:variable name="lang.talleresIgualdad" select="'TALLERES DE EDUCACIÓN EN LA IGUALDAD'"/>
	<xsl:variable name="lang.numGrupos" select="'Nº Total de Grupos de 1º de la E.S.O.'"/>
	<xsl:variable name="lang.numAlunmos" select="'Nº de Alumnos/as de cada grupo de 1º de la E.S.O.'"/>
	<xsl:variable name="lang.a" select="' - A: '"/>
	<xsl:variable name="lang.b" select="' - B: '"/>
	<xsl:variable name="lang.c" select="' - C: '"/>
	<xsl:variable name="lang.d" select="' - D: '"/>
	<xsl:variable name="lang.e" select="' - E: '"/>
	<xsl:variable name="lang.numTotalIgualdad" select="'Nº Total de Talleres solicitados en Igualdad'"/>

	<xsl:variable name="lang.talleresIntercultura" select="'TALLERES DE EDUCACIÓN EN INTERCULTURALIDAD'"/>
	<xsl:variable name="lang.numCursos4" select="'Nº de cursos de 4º  '"/>
	<xsl:variable name="lang.numCursos5" select="'Nº de cursos de 5º  '"/>
	<xsl:variable name="lang.numCursos6" select="'Nº de cursos de 6º  '"/>
	<xsl:variable name="lang.especificar" select="'especificar nº de alumnos/as por grupo'"/>
	<xsl:variable name="lang.numTotalIntercultura" select="'Nº Total de Talleres solicitados en Interculturalidad'"/>

	<!-- Se añaden los campos del Bullying -->
	<xsl:variable name="lang.talleresBullying" select="'TALLERES CONTRA BULLYING Y CIBERBULLYING'"/>
	<xsl:variable name="lang.numGruposBullying" select="'Nº Total de Grupos de 2º de la E.S.O.'"/>
	<xsl:variable name="lang.numAlunmosBullying" select="'Nº de Alumnos/as de cada grupo de 2º de la E.S.O.'"/>
	<xsl:variable name="lang.numTotalBullying" select="'Nº Total de Talleres solicitados Contra el Bullying y Ciberbullying'"/>

	<xsl:variable name="lang.añadir_centro" select="'[Añadir Centro]'"/>
	<xsl:variable name="lang.maximo" select="' (Máximo 8)'"/>

	<xsl:variable name="lang.datosDeclarativos" select="'Declaraciones Preceptivas que formula el solicitante'"/>
	
	<xsl:variable name="lang.declaro" select="'Asimismo, declaro bajo mi Responsabilidad (obligatorio marcar con una x cada cuadro para poder seguir el trámite)'"/>

	
	<xsl:template match="/">
		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:200px; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.id_nif"/>:
				</label>
				<label>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Documento_Identificacion/Numero"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:200px; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.id_nombre"/>:
				</label>
				<label>
					<xsl:value-of select="Datos_Registro/Remitente/Nombre"/>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Nombre"/>
				</label>
			</div>	

			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:200px; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.cargo"/>:
				</label>
				<label>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/cargo"/>
				</label>
			</div>	
		</div>

		<div class="submenu">
   			<h1><xsl:value-of select="$lang.convocatoria"/></h1>
   		</div>
		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
			   		<b><xsl:value-of select="$lang.seleccionar"/></b>				
				</label>
				<br/>
				<label>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/convocatoria"/><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_convocatoria"/>
				</label>
			</div>
		</div>

		<div class="submenu">
   			<h1><xsl:value-of select="$lang.ayto"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.ayto"/>:
				</label>
				<label>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_ayuntamiento"/>
				</label>			
			</div>
		</div>

		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosRepresentante"/>
			<font style="font-size:9px;"><xsl:value-of select="$lang.datosRepresentante2"/></font></h1>
   		</div>
   		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.nif_repre"/>:
				</label>
				<label>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nif_repre"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.nombre_repre"/>:
				</label>
				<label>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre_repre"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.cargo_repre"/>:
				</label>
				<label>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/cargo_repre"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.domicilio_repre"/>:
				</label>
				<label>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/domicilio_repre"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.ciudad_repre"/>:
				</label>
				<label>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ciudad_repre"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.c_postal_repre"/>:
				</label>
				<label>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/c_postal_repre"/>
				</label>
			</div>	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.region_repre"/>:
				</label>
				<label>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/region_repre"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.movil_repre"/>:
				</label>
				<label>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/movil_repre"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px; font-weight: bold;</xsl:attribute>
					<xsl:value-of select="$lang.d_email_repre"/>:
				</label>
				<label>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/d_email_repre"/>
				</label>
			</div>
   		</div>
   		   		
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.centros"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<table style="font-size:11px; margin:0 auto 0  auto; " cellspacing="0" cellpadding="10" border="1" width="93%">
				<xsl:call-template name="COLEGIO">
					<xsl:with-param name="row_id">1</xsl:with-param>
					<xsl:with-param name="numColegios"><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numColegios"/></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="COLEGIO">
					<xsl:with-param name="row_id">2</xsl:with-param>
					<xsl:with-param name="numColegios"><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numColegios"/></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="COLEGIO">
					<xsl:with-param name="row_id">3</xsl:with-param>
					<xsl:with-param name="numColegios"><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numColegios"/></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="COLEGIO">
					<xsl:with-param name="row_id">4</xsl:with-param>
					<xsl:with-param name="numColegios"><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numColegios"/></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="COLEGIO">
					<xsl:with-param name="row_id">5</xsl:with-param>
					<xsl:with-param name="numColegios"><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numColegios"/></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="COLEGIO">
					<xsl:with-param name="row_id">6</xsl:with-param>
					<xsl:with-param name="numColegios"><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numColegios"/></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="COLEGIO">
					<xsl:with-param name="row_id">7</xsl:with-param>
					<xsl:with-param name="numColegios"><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numColegios"/></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="COLEGIO">
					<xsl:with-param name="row_id">8</xsl:with-param>
					<xsl:with-param name="numColegios"><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numColegios"/></xsl:with-param>
				</xsl:call-template>
			</table>			
		</div>

		<div class="submenu">
			<h1><xsl:value-of select="$lang.datosDeclarativos"/></h1>
		</div>
   		<div class="cuadro" style="">
			<xsl:call-template name="DECLARACIONES"/>
		</div>
		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_AUTOFIRMA_COMUN_RELLENO" />
	</xsl:template>
</xsl:stylesheet>