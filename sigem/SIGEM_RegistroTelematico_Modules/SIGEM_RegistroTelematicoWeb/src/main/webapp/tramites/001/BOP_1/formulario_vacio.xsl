<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'Formulario General de Solicitud de inserción de anuncio en el BOP'"/>
	
	<xsl:variable name="lang.datosSolicitante" select="'Datos del Firmante'"/>
	<xsl:variable name="lang.docIdentidad" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.cargo" select="'Cargo'"/>
	
	<xsl:variable name="lang.datosEntidad" select="'Datos de la Entidad solicitante'"/>
	<xsl:variable name="lang.nifcif" select="'NIF/CIF'"/>
	<xsl:variable name="lang.entidad" select="'Entidad'"/>
	<xsl:variable name="lang.domicilio" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono de contacto'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>
	
	<xsl:variable name="lang.datosSolicitud" select="'Datos del Anuncio'"/>
	<xsl:variable name="lang.sumario" select="'Sumario'"/>
	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>
	<xsl:variable name="lang.urgencia" select="'Urgencia'"/>
	<xsl:variable name="lang.avisoUrgencia" select="'Si se especifica el anuncio como Urgente, el coste del mismo se duplica.'"/>
	<xsl:variable name="lang.urgente" select="'Urgente'"/>
	<xsl:variable name="lang.normal" select="'Normal'"/>
	
	<xsl:variable name="lang.avisoCoste" select="'Tarifa del anuncio: 0,062 euros + IVA por cada carácter o pulsación. Importe mínimo de publicación: 34,12 euros + IVA. PAGO ADELANTADO.'"/>
	<xsl:variable name="lang.avisoFecha" select="'En el caso de tener una fecha deseada para la publicación, se indicará en el campo de observaciones.'"/>
	<xsl:variable name="lang.avisoDoc" select="'Es obligatorio adjuntar un documento con el texto deseado para el anuncio, en formato DOC (Microsoft Word) o ODT (OpenOffice Text).'"/> <!--[ecenpri-Felipe Ticket#63] Añadir extensión ODT -->
	<xsl:variable name="lang.avisoParticular" select="'Si usted es un particular, inserte sus datos personales.'"/>
	<xsl:variable name="lang.avisoEmail" select="'El anuncio será remitido automáticamente a la dirección de correo electrónico indicada en el momento en que se publique el B.O.P. que lo recoja.'"/>
	
	<xsl:variable name="lang.organoAsignado" select="'Órgano al que se dirige: Registro de la Excma. Diputación Provincial de Ciudad Real (B.O.P.)'"/>
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.anexarInfo3" select="'(Máximo 7 mbs total)'"/>
	<xsl:variable name="lang.documento1" select="'Documento ODT/DOC'"/> <!--[ecenpri-Felipe Ticket#63] Añadir extensión ODT -->
	<xsl:variable name="lang.documento2" select="'Documento ZIP'"/>
	<xsl:variable name="lang.envio" select="'Envío de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'Solicito el envío de notificaciones por medios telemáticos'"/>
	<xsl:variable name="lang.deu" select="'D.E.U.'"/>

	<xsl:variable name="lang.Comparece" select="'QUEREMOS SER MÁS ÁGILES Y CONTESTARLE CON RAPIDEZ'"/>
	<xsl:variable name="lang.Comparece1" select="'Portal de Notificaciones Telemáticas de la Diputación de Ciudad Real COMPARECE'"/>
	<xsl:variable name="lang.Comparece2" select="'Si lo desea, puede ayudarnos dándose de alta en el'"/>
	<xsl:variable name="lang.Comparece3" select="'Rápido, gratuito, sin papel.'"/>
	
	<xsl:variable name="lang.info_legal" select="'Los datos personales, identificativos y de contacto, aportados mediante esta comunicación se entienden facilitados voluntariamente, y serán incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del ámbito de las competencias de esta Administración Pública así como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telemáticos y enviarle invitaciones para eventos y felicitaciones en fechas señaladas. Entenderemos que presta su consentimiento tácito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podrá ejercer sus derechos de acceso, rectificación, cancelación y oposición ante el Responsable del Fichero, la Diputación Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - España, siempre acreditando conforme a Derecho su identidad en la comunicación. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigiéndose a la dirección citada ut supra o bien al correo electrónico lopd@dipucr.es o bien por telefono al numero gratuito 900 714 080.'"/>
	
	<xsl:template match="/">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			var validar = new Array(10);
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.docIdentidad"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.nombre"/>');
			validar[2] = new Array('nifcif','<xsl:value-of select="$lang.nifcif"/>');
			validar[3] = new Array('entidad','<xsl:value-of select="$lang.entidad"/>');
			validar[4] = new Array('domicilioNotificacion','<xsl:value-of select="$lang.domicilio"/>');
			validar[5] = new Array('localidad','<xsl:value-of select="$lang.localidad"/>');
			validar[6] = new Array('provincia','<xsl:value-of select="$lang.provincia"/>');
			validar[7] = new Array('codigoPostal','<xsl:value-of select="$lang.cp"/>');
			validar[8] = new Array('sumario','<xsl:value-of select="$lang.sumario"/>');
			validar[9] = new Array('urgencia','<xsl:value-of select="$lang.urgencia"/>');

			var validar_NO = new Array(2);
			validar_NO[0] = new Array('BOP1D1','<xsl:value-of select="$lang.documento1"/>');
			validar_NO[1] = new Array('BOP1D2','<xsl:value-of select="$lang.documento2"/>');
			
			//Array con los datos especificos del formulario -> -> ('id_campo','tag_xml')
			var especificos = new Array(16);
			especificos[0] = new Array('cargo','Cargo');
			especificos[1] = new Array('nifcif','NIF_CIF');
			especificos[2] = new Array('entidad','Entidad');
			especificos[3] = new Array('domicilioNotificacion','Domicilio_Notificacion');
			especificos[4] = new Array('localidad','Localidad');
			especificos[5] = new Array('provincia','Provincia');
			especificos[6] = new Array('codigoPostal','Codigo_Postal');
			especificos[7] = new Array('telefono','Telefono');			
			especificos[8] = new Array('emailSolicitante','Email_Solicitante');
			especificos[9] = new Array('sumario','Sumario');
			especificos[10] = new Array('observaciones','Observaciones');			
			especificos[11] = new Array('urgencia','Urgencia');
			especificos[12] = new Array('solicitarEnvio','Solicitar_Envio');
			especificos[13] = new Array('direccionElectronicaUnica','Direccion_Electronica_Unica');

			especificos[14] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[15] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');

			
			var validarNumero = new Array(1);
			validarNumero[0] = new Array('codigoPostal','<xsl:value-of select="$lang.cp"/>',5);
			
			function verificacionesEspecificas() {
				return true;
			}
			
			function getDatosTercero(nif){
				window.open('tramites/001/BOP_1/buscaTercero.jsp?valor='+nif+';001','','width=3,height=3');
			}
			
			//Valida si el campo para el NIF/CIF/NIE es correcto
			function validarCampoNIFCIF() {
				
				var campoNif = document.getElementById('nifcif');
				var validacion = valida_nif_cif(campoNif);
				
				if (validacion == 0){
					alert('El CIF/NIF no cumple ninguno de los formatos permitidos. Ej: P1300000E');
					return false;
				}
				else if (validacion == -1){
					alert('El NIF introducido es incorrecto. Compruebe la letra.');
					return false;
				}
				else if (validacion == -2){
					alert('El CIF introducido es incorrecto. Compruebe la letra');
					return false;
				}
				else if (validacion == -3){
					alert('El NIE introducido es incorrecto. Compruebe la letra');
					return false;
				}
				else{
					return true;
				}
			}
			
			//Retorna: 1 = NIF ok, 2 = CIF ok, 3 = NIE ok, -1 = NIF error, -2 = CIF error, -3 = NIE error, 0 = ??? error
			function valida_nif_cif(campoNif){
				var a = escape(campoNif.value.toUpperCase());
				var temp=a.toUpperCase();
				campoNif.value = temp;
				var cadenadni="TRWAGMYFPDXBNJZSQVHLCKE";
 
				if (temp!=''){
					//si no tiene un formato valido devuelve error
					if(!/^[XYZ]{1}[0-9]{7}[A-Z]{1}$/.test(temp))
					if(!/^[KLM]{1}[0-9]{7}[A-Z]{1}$/.test(temp))
					if(!/^[0-9]{8}[A-Z]{1}$/.test(temp))
					if(!/^[A-Z]{1}[0-9]{7}[A-Z]{1}$/.test(temp))
					if(!/^[A-Z]{1}[0-9]{8}$/.test(temp))
					{
						return 0;
					}
 
					//Comprobación de NIFs estandar
					if (/^[0-9]{8}[A-Z]{1}$/.test(temp)){
						posicion = a.substring(8,0) % 23;
						letra = cadenadni.charAt(posicion);
						var letradni=temp.charAt(8);
						if (letra == letradni){
						 	return 1;
						}
						else{
							return -1;
						}
					}
					
					//Comprobación de CIFs y NIFs especiales
					//Menores, residentes en extranjero y extranjeros sin NIE
					//algoritmo para comprobacion de codigos tipo CIF (usado para estos NIE)
					suma = parseInt(a.charAt(2))+parseInt(a.charAt(4))+parseInt(a.charAt(6));
					for( i = 1; i &lt; 8; i += 2 )
					{
						temp1 = 2 * parseInt( a.charAt( i ) );
						temp1 += '';
						temp1 = temp1.substring(0,1);
						temp2 = 2 * parseInt( a.charAt( i ) );
						temp2 += '';
						temp2 = temp2.substring( 1,2 );
						if( temp2 == '' )
						{
							temp2 = '0';
						}
			 
						suma += ( parseInt( temp1 ) + parseInt( temp2 ) );
					}
					suma += '';
					n = 10 - parseInt(suma.substring(suma.length-1, suma.length));
					
					//comprobación
					if (/^[KLM]{1}/.test(temp)){
						if (a.charAt(8) == String.fromCharCode(64 + n)){
							return 1;
						}
						else{
							return -1;
						}
					}
					
					//comprobacion de CIFs
					if( /^[ABCDEFGHJNPQRSUVW]{1}/.test( temp ) )
					{
						temp = n + '';
						if( a.charAt( 8 ) == String.fromCharCode( 64 + n ) || a.charAt( 8 ) == parseInt( temp.substring( temp.length-1, temp.length ) ) )
						{
							return 2;
						}
						else
						{
							return -2;
						}
					}

					//Comprobación de NIEs
					//T
					if (/^[T]{1}/.test(temp)){
						if (a.charAt(8) == /^[T]{1}[A-Z0-9]{8}$/.test(temp)){
							return 3;
						}
						else{
							return -3;
						}
					}
					//XYZ
					if (/^[XYZ]{1}/.test(temp)){
 						temp = temp.replace('X','0')
						temp = temp.replace('Y','1')
 						temp = temp.replace('Z','2')
 						pos = temp.substring(0, 8)% 23;
						if (a.charAt(8) == cadenadni.substring(pos, pos + 1)){
 							return 3;
 						}
 						else{
 							return -3;
						}
					}
				}
				return 0;
			}

			/*[ecenpri-Felipe Ticket#45] 25.06.2010
			* Para los campos de tipo textarea se incluyen estas funciones ya que
			* no existe la propiedad maxlength*/
			function imposeMaxLength(objeto, len)
			{	
				if (objeto.value.length &gt; len)
				{
					objeto.value = objeto.value.substr(0, len);
				}
			}

			/*[ecenpri-Felipe Ticket#45] 25.06.2010*/
			function controlMaxLength(objeto, nombre, len)
			{
				if (objeto.value.length &gt; len)
				{
					alert('El campo ' + nombre + ' no puede superar los ' + len + ' caracteres');
					objeto.value = objeto.value.substr(0, len);
					objeto.focus();
				}
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
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.cargo"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">cargo</xsl:attribute>
					<xsl:attribute name="id">cargo</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Cargo"/></xsl:attribute>
				</input>
			</div>
		</div>
		
		<br/>	
		<div class="submenu">
   		<h1><xsl:value-of select="$lang.datosEntidad"/></h1>
   	</div>
   	<div class="cuadro" style="">
   		<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nifcif"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:100px; </xsl:attribute>
					<xsl:attribute name="name">nifcif</xsl:attribute>
					<xsl:attribute name="id">nifcif</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/NIF_CIF"/></xsl:attribute>
					<xsl:attribute name="onblur">if(!validarCampoNIFCIF()){this.focus()}</xsl:attribute>
				</input>
				<img onclick="getDatosTercero(document.getElementById('nifcif').value);" src="img/search-mg.gif"/>
				<div class="col">
					<label class="gr"></label>
					<label style="color:grey; margin-left:40px"><xsl:value-of select="$lang.avisoParticular"/></label>
				</div>
			</div>
   		<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.entidad"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">entidad</xsl:attribute>
					<xsl:attribute name="id">entidad</xsl:attribute>
					<xsl:attribute name="onblur">return controlMaxLength(this, 'entidad', 150);</xsl:attribute><!--[ecenpri-Felipe Ticket#45] 25.06.2010-->
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Entidad"/></xsl:attribute>
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Domicilio_Notificacion"/></xsl:attribute>
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Localidad"/></xsl:attribute>
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Provincia"/></xsl:attribute>
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Codigo_Postal"/></xsl:attribute>
				</input>
			</div>
			<div class="col" style="height: 35px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.telefono"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
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
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">emailSolicitante</xsl:attribute>
					<xsl:attribute name="id">emailSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Correo_Electronico"/></xsl:attribute>
				</input>
				<div class="col">
					<label class="gr"></label>
					<label style="color:red; margin-left:40px"><xsl:value-of select="$lang.avisoEmail"/></label>
				</div>
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
					<xsl:value-of select="$lang.sumario"/>:*
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">sumario</xsl:attribute>
					<xsl:attribute name="id">sumario</xsl:attribute>
					<xsl:attribute name="rows">3</xsl:attribute>
					<xsl:attribute name="onblur">return controlMaxLength(this, 'sumario', 220);</xsl:attribute><!--[ecenpri-Felipe Ticket#45] 25.06.2010-->
					<xsl:value-of select="Datos_Registro/datos_especificos/Sumario"/>
				</textarea>
			</div>
			<div class="col" style="height: 50px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.observaciones"/>:
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">observaciones</xsl:attribute>
					<xsl:attribute name="id">observaciones</xsl:attribute>
					<xsl:attribute name="rows">5</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/Observaciones"/>
				</textarea>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:640px;</xsl:attribute>
					<xsl:value-of select="$lang.avisoFecha"/>
				</label>
			</div>
			<div class="col">
			        <input>
			        	<xsl:value-of select="Datos_Registro/datos_especificos/Urgencia"/>
			        	<xsl:attribute name="name">urgencia</xsl:attribute>
				        <xsl:attribute name="id">urgencia</xsl:attribute>
				        <xsl:attribute name="type">hidden</xsl:attribute> 
					<xsl:attribute name="value"><xsl:value-of select="$lang.normal"/></xsl:attribute> 
			        </input>
		      </div>
		      <div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:640px;</xsl:attribute>
					<xsl:value-of select="$lang.avisoCoste"/>
				</label>
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
					<xsl:value-of select="$lang.documento1"/>:*
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">BOP1D1</xsl:attribute>
					<xsl:attribute name="id">BOP1D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">BOP1D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">BOP1D1_Tipo</xsl:attribute>
					<!--[ecenpri-Felipe Ticket#63] Añadir extensión ODT -->
					<xsl:attribute name="value">odt,doc</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:640px;</xsl:attribute>
					<xsl:value-of select="$lang.avisoDoc"/>
				</label>
			</div>			
			<div class="col">
		  		<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento2"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">BOP1D2</xsl:attribute>
					<xsl:attribute name="id">BOP1D2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">BOP1D2_Tipo</xsl:attribute>
					<xsl:attribute name="id">BOP1D2_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
			</div>
   		</div>
		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN" />
   		<br/>
		<xsl:call-template name="TEXTO_COMPARECE_COMUN" />
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
