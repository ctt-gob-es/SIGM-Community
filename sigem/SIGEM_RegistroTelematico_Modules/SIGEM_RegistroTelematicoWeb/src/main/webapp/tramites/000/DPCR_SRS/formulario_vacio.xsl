<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>
	<!--<xsl:variable name="lang.titulo" select="'Formulario Trámite Instancia General (00005)'"/>-->
	<xsl:variable name="lang.titulo" select="'Formulario de Solicitudes, Recursos, Alegaciones, Quejas y Sugerencias'"/>
	<xsl:variable name="lang.datosSolicitante" select="'Datos del Solicitante'"/>
	<xsl:variable name="lang.docIdentidad" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.domicilio" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>
	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.organoAsignado" select="'Órgano al que se dirige: Registro de la Excma. Diputación Provincial de Ciudad Real'"/>
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
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
		
	<xsl:template match="/">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			var validar = new Array(9);
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.docIdentidad"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.nombre"/>');
			validar[2] = new Array('telefono','<xsl:value-of select="$lang.telefono"/>');
			validar[3] = new Array('domicilioNotificacion','<xsl:value-of select="$lang.domicilio"/>');
			validar[4] = new Array('localidad','<xsl:value-of select="$lang.localidad"/>');
			validar[5] = new Array('provincia','<xsl:value-of select="$lang.provincia"/>');
			validar[6] = new Array('codigoPostal','<xsl:value-of select="$lang.cp"/>');
			validar[7] = new Array('expone','<xsl:value-of select="$lang.expone"/>');
			validar[8] = new Array('solicita','<xsl:value-of select="$lang.solicita"/>');
			
			var validar_NO = new Array(5);
			validar_NO[0] = new Array('DPCR_SRSD1','<xsl:value-of select="$lang.documento1"/>');
			validar_NO[1] = new Array('DPCR_SRSD2','<xsl:value-of select="$lang.documento2"/>');
			validar_NO[2] = new Array('DPCR_SRSD3','<xsl:value-of select="$lang.documento3"/>');
			validar_NO[3] = new Array('DPCR_SRSD4','<xsl:value-of select="$lang.documento4"/>');
			validar_NO[4] = new Array('DPCR_SRSD5','<xsl:value-of select="$lang.documento5"/>');
	
			//Array con los datos especificos del formulario -> -> ('id_campo','tag_xml')
			var especificos = new Array(9);
			especificos[0] = new Array('telefono','Telefono');
			especificos[1] = new Array('domicilioNotificacion','Domicilio_Notificacion');
			especificos[2] = new Array('localidad','Localidad');
			especificos[3] = new Array('provincia','Provincia');
			especificos[4] = new Array('codigoPostal','Codigo_Postal');
			especificos[5] = new Array('expone','Expone');
			especificos[6] = new Array('solicita','Solicita');
			especificos[7] = new Array('solicitarEnvio','Solicitar_Envio');
			especificos[8] = new Array('direccionElectronicaUnica','Direccion_Electronica_Unica');
						
			
			var validarNumero = new Array(1);
			validarNumero[0] = new Array('codigoPostal','<xsl:value-of select="$lang.cp"/>',5);
			
			function verificacionesEspecificas() {
				return true;
			}
		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitante"/></h1>
   		</div>
   		<div class="cuadro" style="">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.docIdentidad"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px;</xsl:attribute>
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
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="id">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Nombre"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col" style="height: 35px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.telefono"/>:*	
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">telefono</xsl:attribute>
					<xsl:attribute name="id">telefono</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
			</div>
			<div class="col" style="height: 35px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.domicilio"/>:*	
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">domicilioNotificacion</xsl:attribute>
					<xsl:attribute name="id">domicilioNotificacion</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.email"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">emailSolicitante</xsl:attribute>
					<xsl:attribute name="id">emailSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Correo_Electronico"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.localidad"/>:*	
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">localidad</xsl:attribute>
					<xsl:attribute name="id">localidad</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.provincia"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">provincia</xsl:attribute>
					<xsl:attribute name="id">provincia</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.cp"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">codigoPostal</xsl:attribute>
					<xsl:attribute name="id">codigoPostal</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
			</div>
		</div>
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
				<br/>
				<div style="color: grey; text-align:justify">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
					Los datos personales, identificativos y de contacto, aportados mediante esta comunicación se entienden facilitados voluntariamente, y serán incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del ámbito de las competencias de esta Administración Pública así como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telemáticos y enviarle invitaciones para eventos y felicitaciones en fechas señaladas. Entenderemos que presta su consentimiento tácito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podrá ejercer sus derechos de acceso, rectificación, cancelación y oposición ante el Responsable del Fichero, la Diputación Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - España, siempre acreditando conforme a Derecho su identidad en la comunicación. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigiéndose a la dirección citada ut supra o bien al correo electrónico lopd@dipucr.es o bien por telefono al numero gratuito 900 714 080.	
				</label>
			</div>

			
   		</div>
   		<br/>
		<div class="cuadro" style="">
			<xsl:value-of select="$lang.Comparece"/><br/>
			<xsl:value-of select="$lang.Comparece2"/><br/>
			<a href="http://comparece.dipucr.es:8080/CompareceNotificadorInterfaz/" target="_blank">
				<xsl:attribute name="style">font-weight:bold; color:red; </xsl:attribute>				
				<xsl:value-of select="$lang.Comparece1"/>
			</a>
			
			<br/>
			<xsl:value-of select="$lang.Comparece3"/><br/>
			<br/>
			

   		</div>
   		<input type="hidden">
			<xsl:attribute name="name">datosEspecificos</xsl:attribute>
			<xsl:attribute name="id">datosEspecificos</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
   		<br/>
   		


   			<input class="gr" type="hidden" id="solicitarEnvio" onclick="activarDEU();" style="border:0px; width:20px;" />
   			
   			<input class="gr" type="hidden" id="direccionElectronicaUnica" />




	</xsl:template>
</xsl:stylesheet>
