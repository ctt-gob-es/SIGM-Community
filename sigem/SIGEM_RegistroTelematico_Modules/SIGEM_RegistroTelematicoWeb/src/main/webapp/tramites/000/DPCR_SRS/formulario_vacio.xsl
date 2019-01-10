<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'Formulario de Solicitudes, Recursos, Alegaciones, Quejas y Sugerencias'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	<xsl:variable name="lang.docIdentidad" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.domicilio" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.telefono" select="'Número de teléfono móvil'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>
	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.organoAsignado" select="'Órgano al que se dirige: Registro General de la Entidad Local.'"/>
	<xsl:variable name="lang.organoAlternativo" select="'Órgano alternativo'"/>
	
	<xsl:variable name="lang.servRelacion" select="'Servicio de Relaciones con el Ciudadano'"/>
	<xsl:variable name="lang.servTramLicencias" select="'Servicio de Tramitación de Licencias'"/>
	<xsl:variable name="lang.servSecretaria" select="'Servicio de Secretaría'"/>
	<xsl:variable name="lang.servCenpri" select="'CENPRI'"/>
	<xsl:variable name="lang.registroTelematico" select="'REGISTRO TELEMATICO'"/>
	
	<xsl:variable name="lang.servSecretariaGeneral" select="'Servicio de Secretaría General'"/>
	<xsl:variable name="lang.servTramitacionLicencias" select="'Servicio de Tramitación de Licencias'"/>

	<xsl:variable name="lang.Comparece" select="'QUEREMOS SER MÁS ÁGILES Y CONTESTARLE CON RAPIDEZ'"/>
	<xsl:variable name="lang.Comparece1" select="'Portal de Notificaciones Telemáticas de la Diputación de Ciudad Real COMPARECE'"/>
	<xsl:variable name="lang.Comparece2" select="'Si lo desea, puede ayudarnos dándose de alta en el'"/>
	<xsl:variable name="lang.Comparece3" select="'Rápido, gratuito, sin papel.'"/>
	
	<xsl:variable name="lang.servAreaIgualdadServicios" select="'Área de Igualdad y Servicios Sanitarios'"/>
	
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.anexarInfo3" select="'(Máximo 7 MB total)'"/>
	<xsl:variable name="lang.documento1" select="'Documento DOC'"/>
	<xsl:variable name="lang.documento2" select="'Documento JPG'"/>
	<xsl:variable name="lang.documento3" select="'Documento PDF'"/>
	<xsl:variable name="lang.documento4" select="'Documento ZIP'"/>
	<xsl:variable name="lang.documento5" select="'Documento XSIG'"/>
	<xsl:variable name="lang.envio" select="'Envío de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'Solicito el envío de notificaciones por medios telemáticos'"/>
	<xsl:variable name="lang.deu" select="'D.E.U.'"/>
		
	<xsl:template match="/">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			var validar = new Array(9);
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.docIdentidad"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.nombre"/>');
			validar[2] = new Array('calle','<xsl:value-of select="$lang.domicilio"/>');
			validar[3] = new Array('ciudad','<xsl:value-of select="$lang.localidad"/>');
			validar[4] = new Array('c_postal','<xsl:value-of select="$lang.cp"/>');
			validar[5] = new Array('region','<xsl:value-of select="$lang.provincia"/>');
			validar[6] = new Array('movil','<xsl:value-of select="$lang.telefono"/>');
			validar[7] = new Array('expone','<xsl:value-of select="$lang.expone"/>');
			validar[8] = new Array('solicita','<xsl:value-of select="$lang.solicita"/>');
			
			var validar_NO = new Array(5);
			validar_NO[0] = new Array('DPCR_SRSD1','<xsl:value-of select="$lang.documento1"/>');
			validar_NO[1] = new Array('DPCR_SRSD2','<xsl:value-of select="$lang.documento2"/>');
			validar_NO[2] = new Array('DPCR_SRSD3','<xsl:value-of select="$lang.documento3"/>');
			validar_NO[3] = new Array('DPCR_SRSD4','<xsl:value-of select="$lang.documento4"/>');
			validar_NO[4] = new Array('DPCR_SRSD5','<xsl:value-of select="$lang.documento5"/>');
	
			//Array con los datos especificos del formulario -> -> ('id_campo','tag_xml')
			var especificos = new Array(25);

			especificos[0] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[1] = new Array('nombreSolicitante','nombreSolicitante');

			especificos[2] = new Array('nif','nif');
			especificos[3] = new Array('nombre','nombre');
			especificos[4] = new Array('calle','calle');
			especificos[5] = new Array('ciudad','ciudad');
			especificos[6] = new Array('c_postal','c_postal');
			especificos[7] = new Array('region','region');
			especificos[8] = new Array('movil','movil');
			especificos[9] = new Array('d_email','d_email');
			especificos[10] = new Array('repres_nif','repres_nif');
			especificos[11] = new Array('repres_nombre','repres_nombre');
			especificos[12] = new Array('rcalle','rcalle');
			especificos[13] = new Array('rciudad','rciudad');
			especificos[14] = new Array('rc_postal','rc_postal');
			especificos[15] = new Array('rregion','rregion');
			especificos[16] = new Array('repres_movil','repres_movil');
			especificos[17] = new Array('repres_d_email','repres_d_email');
			
			especificos[18] = new Array('expone','expone');
			especificos[19] = new Array('solicita','solicita');
			especificos[20] = new Array('solicitarEnvio','Solicitar_Envio');
			especificos[21] = new Array('direccionElectronicaUnica','Direccion_Electronica_Unica');
				
			especificos[22] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[23] = new Array('texto_legal_comun_ck','texto_legal_comun_ck');
			especificos[24] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');		
			
			var validarNumero = new Array(1);
			validarNumero[0] = new Array('c_postal','<xsl:value-of select="$lang.cp"/>',5);
			
			function verificacionesEspecificas() {
				return true;
			}
		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>		
		<xsl:call-template name="DATOS_PRESENTADOR" />
		<xsl:call-template name="DATOS_SOLICITANTE_REPRESENTANTE" />
		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
   		<div class="cuadro" style="">
 			<div class="col" style="height: 66px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.expone"/>:*
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">expone</xsl:attribute>
					<xsl:attribute name="id">expone</xsl:attribute>
					<xsl:attribute name="rows">5</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</textarea>
			</div>
		   	<div class="col" style="height: 66px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.solicita"/>:*
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">solicita</xsl:attribute>
					<xsl:attribute name="id">solicita</xsl:attribute>
					<xsl:attribute name="rows">5</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</textarea>
			</div>
   		</div>
   		<br/>
		<div class="submenu">
			<h1><xsl:value-of select="$lang.organoAsignado"/></h1>
		</div>
   		
   		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN" />
   		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
				
   		</div>
   		<div class="cuadro" style="">
			<label class="gr">
		   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
				
	   			<xsl:value-of select="$lang.anexarInfo1"/><br/>
	   			<xsl:value-of select="$lang.anexarInfo2"/><br/>
	   			<xsl:value-of select="$lang.anexarInfo3"/><br/>
			</label>
	   		<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento1"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">DPCR_SRSD1</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">DPCR_SRSD1_Tipo</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD1_Tipo</xsl:attribute>
					<xsl:attribute name="value">doc, odt</xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento2"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">DPCR_SRSD2</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">DPCR_SRSD2_Tipo</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD2_Tipo</xsl:attribute>
					<xsl:attribute name="value">jpg,jpeg,gif,tif,tiff,bmp</xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento3"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">DPCR_SRSD3</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD3</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">DPCR_SRSD3_Tipo</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD3_Tipo</xsl:attribute>
					<xsl:attribute name="value">pdf</xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento4"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">DPCR_SRSD4</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD4</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">DPCR_SRSD4_Tipo</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD4_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento5"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">DPCR_SRSD5</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD5</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">DPCR_SRSD5_Tipo</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD5_Tipo</xsl:attribute>
					<xsl:attribute name="value">xsig</xsl:attribute>
				</input>
			</div>
   		</div>
		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN" />
   		<br/>
		<xsl:call-template name="TEXTO_COMPARECE_COMUN" />
   		<br/>		
   		<input type="hidden">
			<xsl:attribute name="name">datosEspecificos</xsl:attribute>
			<xsl:attribute name="id">datosEspecificos</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
   		<br/>
  		<input class="gr" type="hidden" id="solicitarEnvio" onclick="activarDEU();" style="border:0px; width:20px;" />
   			
		<input class="gr" type="hidden" id="direccionElectronicaUnica" />
		<input type="hidden"><xsl:attribute name="id">emailSolicitante</xsl:attribute></input>
	</xsl:template>
</xsl:stylesheet>
