<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'SOLICITUD DE ALTA, BAJA O MODIFICACIÓN DE DATOS DE UNA ENTIDAD (AYUNTAMIENTO, ASOCIACIÓN, EMPRESA U OTRA ENTIDAD) EN LA PLATAFORMA PROVINCIAL DE NOTIFICACIONES TELEMÁTICAS COMPARECE'"/>
	
  <xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>
	<xsl:variable name="lang.cargo" select="'Cargo'"/>
	
	<xsl:variable name="lang.datosSolicitante" select="'DATOS DEL SOLICITANTE'"/>
	<xsl:variable name="lang.datosInteresado" select="'DATOS DE LA ENTIDAD SOLICITANTE'"/>
	<xsl:variable name="lang.datosSolicitud" select="'DATOS DEL REPRESENTANTE EN COMPARECE'"/>
	<xsl:variable name="lang.datosDeclarativos" select="'Manifiesta'"/>

	<xsl:variable name="lang.tipo_entidad" select="'Tipo de entidad'"/>
	<xsl:variable name="lang.nombre_entidad" select="'Nombre de la Entidad'"/>
	<xsl:variable name="lang.cif" select="'CIF'"/>
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>
	
	<xsl:variable name="lang.tercero_tipo_operacion" select="'Tipo de operación'"/>
	<xsl:variable name="lang.tercero_nombre" select="'Nombre'"/>
	<xsl:variable name="lang.tercero_dni" select="'DNI'"/>
	<xsl:variable name="lang.tercero_email" select="'Email'"/>
	<xsl:variable name="lang.tercero_movil" select="'Teléfono móvil'"/>
	<xsl:variable name="lang.tercero_cargo" select="'Cargo o puesto'"/>
	
	<xsl:variable name="lang.info" select="'En el siguiente cuadro se indica la persona a la que se quiere dar de ALTA, BAJA o MODIFICACIÓN de sus datos en la plataforma COMPARECE como representante de la entidad (*).'"/>
	<xsl:variable name="lang.info1" select="'2.-Para ALTA nueva en CUALQUIER ENTIDAD debe anexar escaneada la(s) escritura(s) de constitución de la empresa y de nombramiento del representante que firma la solicitud. '"/> 
	<xsl:variable name="lang.info2" select="'(*)ES ACONSEJABLE DAR DE ALTA MÁS DE UNA PERSONA COMO REPRESENTANTE (Para evitar problemas de recepción de notificaciones en casos de enfermedad, vacaciones, etc). Para realizar más de una operación de ALTA, BAJA o MODIFICACIÓN que afecte a distintas personas se deben realizar tantas solicitudes como operaciones se plantean.'"/> 
	<xsl:variable name="lang.declaro1" select="'- Que al objeto de agilizar los trámites administrativos y reducir los tiempos en la gestión de los asuntos públicos, solicito a la Diputación Provincial de Ciudad Real que se de ALTA, BAJA o MODIFICACIÓN de los datos de la persona que se indica en el siguiente cuadro (según la opción que se escoja en el campo TIPO DE OPERACIÓN) como representante de la entidad arriba indicada, en la Plataforma Provincial de Notificaciones Electrónicas para que, de forma preferente, a través del sistema de comparecencia electrónica COMPARECE de esa institución provincial, cuyo funcionamiento manifiesto conocer, le sean notificados todos los acuerdos y resoluciones  adoptados por esa Diputación y cualquier otra comunicación provincial de interés para esta entidad.'"/>
	<xsl:variable name="lang.declaro2" select="'- Que entiendo que la notificación que se practique será efectiva a todos los efectos legales a partir del momento en el que se acceda a ella por cualquiera de las personas indicadas, en el marco de lo dispuesto en el art. 28.5 de la ley 11/2007, de Acceso Electrónico de los Ciudadanos a los Servicios Públicos, en el art. 40 del RD 1671/2009 de 6 noviembre, de desarrollo parcial de la Ley 11/2007, y en el manual de instrucciones de la aplicación Comparece, instalado en la sede electrónica provincial https://sede.dipucr.es/documentacion.'"/>
	<xsl:variable name="lang.declaro3" select="'- Que, mientras no se indique lo contrario por esta entidad arriba indicada, los avisos, notificaciones y escritos de la Diputación Provincial de Ciudad Real, se deberán remitir electrónicamente a través de la Plataforma de Notificaciones Telemáticas a las personas que se den de ALTA a través de este formulario.'"/>	
	<xsl:variable name="lang.anexar" select="'Ficheros que se Anexan'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- En caso de ALTA INICIAL DE CUALQUIER ENTIDAD, se debe anexar escaneada la(s) escritura(s) de constitución de la empresa, o los estatutos de la entidad, y el nombramiento del representante de la misma, que es el que debe firmar la presente solicitud electrónica.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.-  Para adjuntar los ficheros, pulse el botón examinar, después seleccione el fichero o ficheros que desee anexar a la solicitud.'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>
	<xsl:variable name="lang.documentoTipo_DOC_ODT" select="'Archivo ODT/DOC'"/>
	<xsl:variable name="lang.documentoTipoPDF" select="'Archivo PDF'"/>
	<xsl:variable name="lang.documentoTipoJPEG" select="'Archivo JPG'"/>
	
		
	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(16);
			
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');
			validar[2] = new Array('cargo','<xsl:value-of select="$lang.cargo"/>');
			
			validar[3] = new Array('nombre_entidad','<xsl:value-of select="$lang.nombre_entidad"/>');
			validar[4] = new Array('cif','<xsl:value-of select="$lang.cif"/>');
			validar[5] = new Array('domicilioNotificacion','<xsl:value-of select="$lang.direccion"/>');
			validar[6] = new Array('localidad','<xsl:value-of select="$lang.localidad"/>');
			validar[7] = new Array('provincia','<xsl:value-of select="$lang.provincia"/>');
			validar[8] = new Array('codigoPostal','<xsl:value-of select="$lang.cp"/>');
			validar[9] = new Array('telefono','<xsl:value-of select="$lang.telefono"/>');
			validar[10] = new Array('emailSolicitante','<xsl:value-of select="$lang.email"/>');

			validar[11] = new Array('tercero_nombre','<xsl:value-of select="$lang.tercero_nombre"/>');
			validar[12] = new Array('tercero_dni','<xsl:value-of select="$lang.tercero_dni"/>');
			validar[13] = new Array('tercero_movil','<xsl:value-of select="$lang.tercero_movil"/>');
			validar[14] = new Array('tercero_email','<xsl:value-of select="$lang.tercero_email"/>');
			validar[15] = new Array('tercero_cargo','<xsl:value-of select="$lang.tercero_cargo"/>');
			
			
			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(21);
			
			especificos[0] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[1] = new Array('nombreSolicitante','nombreSolicitante');
			especificos[2] = new Array('cargo','cargo');
			
			especificos[3] = new Array('nombre_entidad','nombre_entidad');
			especificos[4] = new Array('cif','cif');
			especificos[5] = new Array('domicilioNotificacion','domicilioNotificacion');
			especificos[6] = new Array('localidad','localidad');
			especificos[7] = new Array('provincia','provincia');
			especificos[8] = new Array('codigoPostal','codigoPostal');
			especificos[9] = new Array('telefono','telefono');
			especificos[10] = new Array('emailSolicitante','emailSolicitante');

			especificos[11] = new Array('tercero_nombre','tercero_nombre');
			especificos[12] = new Array('tercero_dni','tercero_dni');
			especificos[13] = new Array('tercero_movil','tercero_movil');
			especificos[14] = new Array('tercero_email','tercero_email');
			especificos[15] = new Array('tercero_cargo','tercero_cargo');

			especificos[16] = new Array('tipo_entidad','tipo_entidad');
			especificos[17] = new Array('tercero_tipo_operacion','tercero_tipo_operacion');
			especificos[18] = new Array('declaro1_texto','declaro1_texto');
			especificos[19] = new Array('declaro2_texto','declaro2_texto');
			especificos[20] = new Array('declaro3_texto','declaro3_texto');


			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero = new Array(3);
			validarNumero[0] = new Array('tercero_movil','<xsl:value-of select="$lang.tercero_movil"/>',9);
			validarNumero[1] = new Array('codigoPostal','<xsl:value-of select="$lang.cp"/>',5);
			validarNumero[2] = new Array('telefono','<xsl:value-of select="$lang.telefono"/>',9);

			function verificacionesEspecificas() {
				return true;
			}
			
			
			//Valida si el campo para el NIF/CIF/NIE es correcto
			function validarCampoNIFCIF(campoNif) {
				
				//var campoNif = document.getElementById('cif');
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

			
			
			function imposeMaxLength(objeto, len)
			{	
				if (objeto.value.length &gt; len)
				{
					objeto.value = objeto.value.substr(0, len);
				}
			}

			
			function controlMaxLength(objeto, nombre, len)
			{
				if (objeto.value.length &gt; len)
				{
					alert('El campo ' + nombre + ' no puede superar los ' + len + ' caracteres');
					objeto.value = objeto.value.substr(0, len);
					objeto.focus();
				}
			}


			function validarEmail(inputText)
    			{
    				var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    				if(inputText.value.match(mailformat))
    				{
   
    					return true;
    				}
    				else
    				{
    					alert("El correo que has tecleado no es válido");
    
   					return false;
    				}
    			}


		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
   		
   		<!-- AJM Datos generales dni, nombre, cargo  -->
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitante"/></h1>
   		</div>
   		<div class="cuadro" style="">
   		<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.id_nif"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px;</xsl:attribute>
					<xsl:attribute name="name">documentoIdentidad</xsl:attribute>
					<xsl:attribute name="id">documentoIdentidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Documento_Identificacion/Numero"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.id_nombre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="id">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Nombre"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.cargo"/>*:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">cargo</xsl:attribute>
					<xsl:attribute name="id">cargo</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/cargo"/></xsl:attribute>
				</input>						
			</div>						
			<br/>				
   		</div>
   		
   		<!-- AJM Datos de la entidad solicitante, tipo, cif, domicilio, localidad, provincia, codigo postal, tel, email  -->
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosInteresado"/></h1>
   		</div>
   		<div class="cuadro" style="">	
   		
   		<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.tipo_entidad"/>:*
				</label>
				<select>
					<xsl:attribute name="style">color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tipo_entidad</xsl:attribute>
					<xsl:attribute name="id">tipo_entidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tipo_entidad"/></xsl:attribute>				
					<options>
						<option value='EMPRESA'>EMPRESA</option>
						<option value='AYUNTAMIENTO'>AYUNTAMIENTO</option>
						<option value='ASOCIACIÓN'>ASOCIACIÓN</option>
						<option value='OTRAS'>OTRAS</option>						
					</options>
				</select>				
				<br/>	
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nombre_entidad"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">nombre_entidad</xsl:attribute>
					<xsl:attribute name="id">nombre_entidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombre_entidad"/></xsl:attribute>	
				</input>
			</div>			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.cif"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">cif</xsl:attribute>
					<xsl:attribute name="id">cif</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/cif"/></xsl:attribute>
					<xsl:attribute name="onblur">if(!validarCampoNIFCIF(document.getElementById('cif'))){this.focus()}</xsl:attribute>
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/domicilioNotificacion"/></xsl:attribute>
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/localidad"/></xsl:attribute>
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/provincia"/></xsl:attribute>
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/codigoPostal"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.telefono"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">telefono</xsl:attribute>
					<xsl:attribute name="id">telefono</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/telefono"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.email"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">emailSolicitante</xsl:attribute>
					<xsl:attribute name="id">emailSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/emailSolicitante"/></xsl:attribute>
					<xsl:attribute name="onblur">if(!validarEmail(document.getElementById('emailSolicitante'))){this.focus()}</xsl:attribute>
				</input>
			</div>
		</div>		
		<br/>
		<div class="submenu">
   				<h1><xsl:value-of select="$lang.datosDeclarativos"/></h1>
   			</div>
   			<div class="cuadro" style="">						
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
					<xsl:value-of select="$lang.declaro1"/> 
				</label>	
				<br/>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
					<xsl:value-of select="$lang.declaro2"/> 
				</label>
				<br/>
				<br/>	
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
					<xsl:value-of select="$lang.declaro3"/> 
				</label>
			</div>
		
   			<br/>
		<!-- AJM Datos de la solicitud  -->
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
		<!-- AJM Terceros con los que hay que operar  -->
		<div class="cuadro" style="">
			<label class="gr">
					<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
					<xsl:value-of select="$lang.info"/>	
				</label>	
			<!-- AJM Primer tercero  -->
			<div class="cuadro" style="">				
				<div class="col">
					<label class="gr">
						<xsl:attribute name="style">width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.tercero_tipo_operacion"/>:*
					</label>
					<select>
						<xsl:attribute name="style">color:#006699;</xsl:attribute>
						<xsl:attribute name="name">tercero_tipo_operacion</xsl:attribute>
						<xsl:attribute name="id">tercero_tipo_operacion</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tercero_tipo_operacion"/></xsl:attribute>					
						<options>
							<option value='ALTA'>ALTA</option>
							<option value='BAJA'>BAJA</option>
							<option value='MODIFICACIÓN'>MODIFICACIÓN</option>
						</options>
					</select>									
				</div>	
				<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.tercero_nombre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:460px; </xsl:attribute>
					<xsl:attribute name="name">tercero_nombre</xsl:attribute>
					<xsl:attribute name="id">tercero_nombre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tercero_nombre"/></xsl:attribute>	
				</input>
				</div>			
				<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.tercero_dni"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:460px; </xsl:attribute>
					<xsl:attribute name="name">tercero_dni</xsl:attribute>
					<xsl:attribute name="id">tercero_dni</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tercero_dni"/></xsl:attribute>
					<xsl:attribute name="onblur">if(!validarCampoNIFCIF(document.getElementById('tercero_dni'))){this.focus()}</xsl:attribute>
				</input>
				</div>				
				<div class="col">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.tercero_movil"/>:*
					</label>
					<input type="text">
						<xsl:attribute name="style">position: relative; width:460px; </xsl:attribute>
						<xsl:attribute name="name">tercero_movil</xsl:attribute>
						<xsl:attribute name="id">tercero_movil</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tercero_movil"/></xsl:attribute>
					</input>
				</div>
				<div class="col">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.tercero_email"/>:*
					</label>
					<input type="text">
						<xsl:attribute name="style">position: relative; width:460px; </xsl:attribute>
						<xsl:attribute name="name">tercero_email</xsl:attribute>
						<xsl:attribute name="id">tercero_email</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tercero_email"/></xsl:attribute>
						<xsl:attribute name="onblur">if(!validarEmail(document.getElementById('tercero_email'))){this.focus()}</xsl:attribute>
					</input>
				</div>	
				<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.tercero_cargo"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:460px; </xsl:attribute>
					<xsl:attribute name="name">tercero_cargo</xsl:attribute>
					<xsl:attribute name="id">tercero_cargo</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tercero_cargo"/></xsl:attribute>
				</input>
				</div>		
			</div> 	
			<label class="gr">
					<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
					<xsl:value-of select="$lang.info2"/>		
			</label>				
			
		</div> 
		
			
			
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   			</div>
   	<div class="cuadro" style="">
				<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.anexarInfo1"/>
		   			<!-- <a style="text-decoration:underline; color:red" href="javascript:abrirDocumento();">Modelo orientativo de acuerdo</a><br/>  -->
		   			<br/>
		   			<xsl:value-of select="$lang.anexarInfo2"/><br/>
				</label>
			<br/><br/>
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">SOLALTBAJMODCOMP_D1</xsl:attribute>
					<xsl:attribute name="id">SOLALTBAJMODCOMP_D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">SOLALTBAJMODCOMP_D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">SOLALTBAJMODCOMP_D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
				
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoPDF"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">SOLALTBAJMODCOMP_D2</xsl:attribute>
					<xsl:attribute name="id">SOLALTBAJMODCOMP_D2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">SOLALTBAJMODCOMP_D2_Tipo</xsl:attribute>
					<xsl:attribute name="id">SOLALTBAJMODCOMP_D2_Tipo</xsl:attribute>
					<xsl:attribute name="value">PDF</xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">declaro1_texto</xsl:attribute>
					<xsl:attribute name="id">declaro1_texto</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="$lang.declaro1"/></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">declaro2_texto</xsl:attribute>
					<xsl:attribute name="id">declaro2_texto</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="$lang.declaro2"/></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">declaro3_texto</xsl:attribute>
					<xsl:attribute name="id">declaro3_texto</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="$lang.declaro3"/></xsl:attribute>
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