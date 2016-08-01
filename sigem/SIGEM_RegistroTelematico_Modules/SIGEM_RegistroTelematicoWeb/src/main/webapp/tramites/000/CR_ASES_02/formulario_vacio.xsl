<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'Solicitud de Defensa Judicial en recurso contencioso-administrativo'"/>

	<xsl:variable name="lang.datosInteresado" select="'Datos del interesado'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.representante" select="'En representación del Ayuntamiento de'"/>
	<xsl:variable name="lang.cif" select="'con CIF'"/>
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>

	<xsl:variable name="lang.ayuntamiento" select="'Nombre del Ayuntamiento'"/>	
	<xsl:variable name="lang.recurrente" select="'Nombre del Recurrente'"/>	
	<xsl:variable name="lang.juzgado" select="'Nombre del Juzgado o Tribunal ante el que se sigue el recurso (p.ej: Juzgado de lo social nº1 de Madrid)'"/>
	<xsl:variable name="lang.procedimiento" select="'Nombre y Número del Procedimiento: (p.ej: Recurso Contencioso - Administrativo nº888/2011)'"/>
	<xsl:variable name="lang.impugnado" select="'Acuerdo impugnado: (p.ej. Decreto o Acuerdo de Junta de Gobierno o Pleno)'"/>
	<xsl:variable name="lang.fechaImpugnado" select="'Fecha del acuerdo impugnado'"/>
	
	<xsl:variable name="lang.asunto" select="'Asunto'"/>
	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>	
	

	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Debe anexar copia del expediente administrativo al que se refiere la presente solicitud y, el acuerdo municipal por el que se solicita la defensa Jurídica de la Unidad Técnica de la Diputación Provincial.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.-  Para adjuntar los ficheros, pulse el botón examinar. Seleccione el fichero que desee anexar a la solicitud. Recuerde que debe anexar copia del expediente administrativo al que se refiere la presente solicitud y el acuerdo adoptado por el órgano municipal competente solicitando a la Diputación Provincial que la UATA asuma la defensa del ayuntamiento . Si el expediente o el acuerdo están en soporte papel, debe escanearlo con carácter previo. '"/>
	<xsl:variable name="lang.anexarInfo3" select="'3.- Si el expediente es voluminoso, se recomienda comprimir toda la documentación en formato zip.'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>
	<xsl:variable name="lang.documentoTipo_DOC_ODT" select="'Archivo ODT/DOC'"/>
	<xsl:variable name="lang.documentoTipoPDF" select="'Archivo PDF'"/>
	<xsl:variable name="lang.documentoTipoJPEG" select="'Archivo JPEG'"/>
		
	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(15);

			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.nombre"/>');
			validar[2] = new Array('domicilioNotificacion','<xsl:value-of select="$lang.direccion"/>');
			validar[3] = new Array('localidad','<xsl:value-of select="$lang.localidad"/>');
			validar[4] = new Array('provincia','<xsl:value-of select="$lang.provincia"/>');
			validar[5] = new Array('codigoPostal','<xsl:value-of select="$lang.cp"/>');

			validar[6] = new Array('ayuntamiento', '<xsl:value-of select="$lang.ayuntamiento"/>');
			validar[7] = new Array('recurrente','<xsl:value-of select="$lang.recurrente"/>');
			validar[8] = new Array('juzgado','<xsl:value-of select="$lang.juzgado"/>');
			validar[9] = new Array('procedimiento','<xsl:value-of select="$lang.procedimiento"/>');
			validar[10] = new Array('impugnado','<xsl:value-of select="$lang.impugnado"/>');
			validar[11] = new Array('fechaImpugnado','<xsl:value-of select="$lang.fechaImpugnado"/>');
			

			
			validar[12] = new Array('expone','<xsl:value-of select="$lang.expone"/>');
			validar[13] = new Array('solicita','<xsl:value-of select="$lang.solicita"/>');
			validar[14] = new Array('asunto','<xsl:value-of select="$lang.asunto"/>');
			


			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(19);
			
			especificos[0] = new Array('domicilioNotificacion','domicilioNotificacion');
			especificos[1] = new Array('localidad','localidad');
			especificos[2] = new Array('provincia','provincia');
			especificos[3] = new Array('codigoPostal','codigoPostal');
			especificos[4] = new Array('telefono','telefono');
			
			especificos[5] = new Array('ayuntamiento','ayuntamiento');
			especificos[6] = new Array('recurrente','recurrente');
			especificos[7] = new Array('juzgado','juzgado');
			especificos[8] = new Array('procedimiento','procedimiento');
			especificos[9] = new Array('impugnado','impugnado');
			especificos[10] = new Array('fechaImpugnado','fechaImpugnado');
			
			especificos[11] = new Array('expone','expone');
			especificos[12] = new Array('solicita','solicita');
			especificos[13] = new Array('asunto','asunto');
			
			especificos[14] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[15] = new Array('nombreSolicitante','nombreSolicitante');
			especificos[16] = new Array('emailSolicitante','emailSolicitante');
			
			especificos[17] = new Array('asuntoOpcion','asuntoOpcion');
			especificos[18] = new Array('cif','cif');
			
			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				return true;
			}
			
			function abrirDocumento(){ 
				v=window.open("documentos/MODELO.odt"); 
			} 
			function getDatosAyto(valueAyto){
				window.open('tramites/001/ayuntamiento.jsp?valor='+valueAyto+';001','','width=3,height=3,left=10000,top=10000');
			}
		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosInteresado"/></h1>
   		</div>
   		<div class="cuadro" style="">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nif"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:attribute name="name">documentoIdentidad</xsl:attribute>
					<xsl:attribute name="id">documentoIdentidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Documento_Identificacion/Numero"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nombre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="id">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Nombre"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<xsl:variable name="fileAyuntam" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosTablaValidacionConSustituto('REC_VLDTBL_MUNICIPIOS', '000')"/>
			
			
			<!-- <xsl:variable name="fileAyuntam" select="java:es.dipucr.sigem.api.registroTelematico.XmlAyuntamiento.getAyuntamientos()"/> -->
			<xsl:variable name="b" select="document($fileAyuntam)"/>
		
			
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.representante"/>:*
				</label>
				<!-- Carga de los ayuntamientos de la provincia de Ciudad Real-->
				<select onchange="getDatosAyto(this.value)">
					<xsl:attribute name="style">border:none; width:400px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">ayuntamiento</xsl:attribute>
					<xsl:attribute name="id">ayuntamiento</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ayuntamiento"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>	
			</div>
			<!-- <br/><br/><br/>hola mundo<br/><br/>
   		<input id='hola'/>
    		<button onclick='abreVentana()'>Buscar</button>
    		<input id='adios' />
			        		
	    <br/><br/><br/>hola mundo<br/><br/>
	     -->
	    
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.cif"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">cif</xsl:attribute>
					<xsl:attribute name="id">cif</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/cif"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col" style="height: 35px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.direccion"/>:*	
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">domicilioNotificacion</xsl:attribute>
					<xsl:attribute name="id">domicilioNotificacion</xsl:attribute>
					<xsl:attribute name="rows">5</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/Domicilio_Notificacion"/>
				</textarea>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.localidad"/>:*	
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">localidad</xsl:attribute>
					<xsl:attribute name="id">localidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Localidad"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.provincia"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">provincia</xsl:attribute>
					<xsl:attribute name="id">provincia</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Provincia"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.cp"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">codigoPostal</xsl:attribute>
					<xsl:attribute name="id">codigoPostal</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Codigo_Postal"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.telefono"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">telefono</xsl:attribute>
					<xsl:attribute name="id">telefono</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Telefono"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.email"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">emailSolicitante</xsl:attribute>
					<xsl:attribute name="id">emailSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Correo_Electronico"/></xsl:attribute>
				</input>
			</div>
		</div>
		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
   		<div class="cuadro" style="">	
   			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.asunto"/>:*
				</label>
				<label type="text">
					<xsl:attribute name="style">position: relative; width:210px; </xsl:attribute>
					<xsl:attribute name="name">asunto</xsl:attribute>
					<xsl:attribute name="id">asunto</xsl:attribute>
					Solicitud de Defensa Judicial
				</label>
				
				<xsl:variable name="fileMateria" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosTablaValidacionConSustituto('DPCR_VLDTBL_MATERIA','000')"/>
				<xsl:variable name="xmlMateria" select="document($fileMateria)"/>

				<select>
					<xsl:attribute name="style">border:none; width:400px;color:#006699; </xsl:attribute>
					<xsl:attribute name="style">position: relative; width:270px </xsl:attribute>
					<xsl:attribute name="name">asuntoOpcion</xsl:attribute>
					<xsl:attribute name="id">asuntoOpcion</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/asuntoOpcion"/></xsl:attribute>
					<xsl:for-each select="$xmlMateria/listado/dato">
						<option>
							<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
			</div>	

			<br/>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.recurrente"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">recurrente</xsl:attribute>
					<xsl:attribute name="id">recurrente</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/recurrente"/></xsl:attribute>
				</input>
			</div>
	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.juzgado"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">juzgado</xsl:attribute>
					<xsl:attribute name="id">juzgado</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/juzgado"/></xsl:attribute>
				</input>
			</div>
	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.procedimiento"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">procedimiento</xsl:attribute>
					<xsl:attribute name="id">procedimiento</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/procedimiento"/></xsl:attribute>
				</input>
			</div>
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.impugnado"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">impugnado</xsl:attribute>
					<xsl:attribute name="id">impugnado</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/impugnado"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.fechaImpugnado"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">fechaImpugnado</xsl:attribute>
					<xsl:attribute name="id">fechaImpugnado</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/fechaImpugnado"/></xsl:attribute>
				</input>
			</div>
			<br/>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.expone"/>:*
				</label>
				<textarea type="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">expone</xsl:attribute>
					<xsl:attribute name="id">expone</xsl:attribute>
					<xsl:attribute name="rows">7</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/expone"/>Que ante la carencia de medios personales y materiales suficientes y la indefensión con que se encuentra el Ayuntamiento.
				</textarea>
			</div>
			<br/>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.solicita"/>:*	
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">solicita</xsl:attribute>
					<xsl:attribute name="id">solicita</xsl:attribute>
					<xsl:attribute name="rows">7</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/solicita"/>Que por la Unidad de Apoyo Técnico a Ayuntamientos se asuma la defensa en juicio del Ayuntamiento en los autos arriba referenciados.
				</textarea>
			</div>
		</div>
   		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.anexarInfo1"/>
		   			<a style="text-decoration:underline; color:red" href="javascript:abrirDocumento();">Modelo orientativo de acuerdo</a><br/>
		   			<br/>
		   			<xsl:value-of select="$lang.anexarInfo2"/>			
		   			<xsl:value-of select="$lang.anexarInfo3"/><br/>
			</label>
			<br/><br/><br/>
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_ASES_02_D1</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_02_D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_ASES_02_D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_02_D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
				
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo_DOC_ODT"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_ASES_01_D2</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_01_D2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_ASES_01_D2_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_01_D2_Tipo</xsl:attribute>
					<xsl:attribute name="value">odt,doc</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoPDF"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_ASES_01_D3</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_01_D3</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_ASES_01_D3_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_01_D3_Tipo</xsl:attribute>
					<xsl:attribute name="value">PDF</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoJPEG"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_ASES_01_D4</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_01_D4</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_ASES_01_D4_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_ASES_01_D4_Tipo</xsl:attribute>
					<xsl:attribute name="value">JPEG</xsl:attribute>
				</input>
			</div>
			<br/>
			<div style="color: grey; text-align:justify">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
					Los datos personales, identificativos y de contacto, aportados mediante esta comunicación se entienden facilitados voluntariamente, y serán incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del ámbito de las competencias de esta Administración Pública así como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telemáticos y enviarle invitaciones para eventos y felicitaciones en fechas señaladas. Entenderemos que presta su consentimiento tácito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podrá ejercer sus derechos de acceso, rectificación, cancelación y oposición ante el Responsable del Fichero, la Diputación Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - España, siempre acreditando conforme a Derecho su identidad en la comunicación. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigiéndose a la dirección citada ut supra o bien al correo electrónico lopd@dipucr.es o bien por telefono al numero gratuito 900 714 080.	
				</label>
			</div>
   		</div>
   		<br/>
   		<input type="hidden">
			<xsl:attribute name="name">datosEspecificos</xsl:attribute>
			<xsl:attribute name="id">datosEspecificos</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
	</xsl:template>
</xsl:stylesheet>
