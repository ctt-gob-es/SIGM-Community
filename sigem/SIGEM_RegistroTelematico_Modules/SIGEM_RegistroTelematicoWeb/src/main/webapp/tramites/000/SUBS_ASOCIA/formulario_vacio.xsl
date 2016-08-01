<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'SUBSANACIÓN O MODIFICACIÓN DE EXPEDIENTES PARA ASOCIACIONES Y OTRAS ENTIDADES'"/>
	
	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>
	<xsl:variable name="lang.cargo" select="'Cargo*'"/>

	<xsl:variable name="lang.datosRepresentante" select="'Datos del responsable del proyecto o actividad'"/>
	<xsl:variable name="lang.datosInteresado" select="'Datos de la Asociación u Otra Entidad'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.nif_repre" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre_repre" select="'Nombre y Apellidos'"/>
	<xsl:variable name="lang.domicilio_repre" select="'Domicilio'"/>
	<xsl:variable name="lang.ciudad_repre" select="'Localidad'"/>
	<xsl:variable name="lang.c_postal_repre" select="'Código Postal'"/>
	<xsl:variable name="lang.region_repre" select="'Provincia'"/>
	<xsl:variable name="lang.movil_repre" select="'Teléfono'"/>
	<xsl:variable name="lang.d_email_repre" select="'Correo electrónico'"/>
	
	<xsl:variable name="lang.anio" select="'Año de inicio del expediente:*'"/>

	<xsl:variable name="lang.asunto" select="'Asunto'"/>
	<xsl:variable name="lang.numexp" select="'Número de Registro de Entrada en Diputación de la solicitud'"/>
	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>

	<xsl:variable name="lang.ayuntamiento" select="'Nombre de la asociación'"/>	
	<xsl:variable name="lang.nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.representante" select="'Nombre o Denominación Social'"/>
	<xsl:variable name="lang.cif" select="'CIF'"/>
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>	

	<xsl:variable name="lang.declaro" select="'Asimismo, declaro bajo mi Responsabilidad (obligatorio marcar con una x cada cuadro para poder seguir el trámite)'"/>

	<xsl:variable name="lang.anexar" select="'Puede anexar los documentos que considere pertinentes en cualquiera de los formatos abajo indicados'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.anexarInfo3" select="'(Máximo 7 MB total)'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>
	<xsl:variable name="lang.documentoTipo_DOC_ODT" select="'Archivo ODT/DOC'"/>
	<xsl:variable name="lang.documentoTipoPDF" select="'Archivo PDF'"/>
	<xsl:variable name="lang.documentoTipoJPEG" select="'Archivo JPG'"/>
	
	<xsl:variable name="lang.Comparece" select="'QUEREMOS SER MÁS ÁGILES Y CONTESTARLE CON RAPIDEZ'"/>
	<xsl:variable name="lang.Comparece1" select="'Portal de Notificaciones Telemáticas de la Diputación de Ciudad Real COMPARECE'"/>
	<xsl:variable name="lang.Comparece2" select="'Si lo desea, puede ayudarnos dándose de alta en el'"/>
	<xsl:variable name="lang.Comparece3" select="'Rápido, gratuito, sin papel.'"/>
	
		
	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(13);
			
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');
			validar[2] = new Array('cargo','Cargo');

			validar[3] = new Array('nif_repre','<xsl:value-of select="$lang.nif_repre"/>');
			validar[4] = new Array('nombre_repre','<xsl:value-of select="lang.nombre_repre"/>');
			
			validar[5] = new Array('cif','CIF');
			validar[6] = new Array('ayuntamiento','Ayuntamiento');
			
			validar[7] = new Array('domicilioNotificacion','<xsl:value-of select="$lang.direccion"/>');
			validar[8] = new Array('localidad','<xsl:value-of select="$lang.localidad"/>');
			validar[9] = new Array('provincia','<xsl:value-of select="$lang.provincia"/>');
			validar[10] = new Array('codigoPostal','<xsl:value-of select="$lang.cp"/>');

			validar[11] = new Array('expone','<xsl:value-of select="$lang.expone"/>');
			validar[12] = new Array('solicita','<xsl:value-of select="$lang.solicita"/>');


			//Array con los datos especificos del formulario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(24);
				
			especificos[0] = new Array('asunto','asunto');
			especificos[1] = new Array('numExpediente','numExpediente');
			
			especificos[2] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[3] = new Array('nombreSolicitante','nombreSolicitante');
			especificos[4] = new Array('emailSolicitante','emailSolicitante');
			
			especificos[5] = new Array('cif','cif');
			especificos[6] = new Array('ayuntamiento','ayuntamiento');
			especificos[7] = new Array('domicilioNotificacion','domicilioNotificacion');
			especificos[8] = new Array('localidad','localidad');
			especificos[9] = new Array('provincia','provincia');
			especificos[10] = new Array('codigoPostal','codigoPostal');
			especificos[11] = new Array('telefono','telefono');

			especificos[12] = new Array('expone','expone');
			especificos[13] = new Array('solicita','solicita');
			
			especificos[14] = new Array('nif_repre','nif_repre');
			especificos[15] = new Array('nombre_repre','nombre_repre');
			especificos[16] = new Array('domicilio_repre','domicilio_repre');
			especificos[17] = new Array('ciudad_repre','ciudad_repre');
			especificos[18] = new Array('c_postal_repre','c_postal_repre');
			especificos[19] = new Array('region_repre','region_repre');
			especificos[20] = new Array('movil_repre','movil_repre');
			especificos[21] = new Array('d_email_repre','d_email_repre');

			especificos[22] = new Array('cargo','cargo');
			especificos[23] = new Array('Descripcion_ayuntamiento','Descripcion_ayuntamiento');			

			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				document.getElementById('nombreSolicitante').value = document.getElementById('nombreSolicitante').value;
				document.getElementById('documentoIdentidad').value = document.getElementById('documentoIdentidad').value;
				document.getElementById('localidad').value = document.getElementById('ciudad_repre').value;

				document.getElementById('Descripcion_ayuntamiento').value = document.getElementById('ayuntamiento').value;

				if(!validaNIFCIF(document.getElementById('nif_repre'))) return false;
				if(!validaNIFCIF(document.getElementById('cif'))) return false;

				return true;
			}
			
			function abrirDocumento(){ 
				v=window.open("documentos/MODELO.odt"); 
			}

			function validaNIFCIF(campo){				
				var validaNif = valida_nif_cif_nie(campo);
				if(validaNif != 1)
					if(validaNif != 2)
						if(validaNif != 3){
							alert('El NIF/CIF es incorrecto');
							campo.focus();			
							return false;
						}
				return true;
			}

			function validaCIF(cif){
				var par = 0;
				var non = 0;
				var letras = "ABCDEFGHJKLMNPRQSUVW";
				var caracterControlLetra = "KPQS";
				var caracterControlNum = "ABEH";
				var i;
				var parcial;
				var control;
				var controlLetra = "JABCDEFGHIJ";
				var letraIni = cif.charAt(0);
				 
				if (cif.length!=9){
				    alert("El Cif debe tener 9 dígitos",3);
					    return -2;
				}
				else{
				    if (letras.indexOf(letraIni.toUpperCase())==-1)
				    {
					alert("La letra del CIF introducido no es correcta",3);
					    return -2;
				    }
				    var i = 2;
				    while (i!=8){
					par = par + parseInt(cif.charAt(i));
					i = i+2;
				    }
			 
				    i = 1;
				    while ( i!= 9){
					var nn = 2 * parseInt(cif.charAt(i));
					if (nn > 9) nn = 1 + (nn-10);
					non = non + nn;
					i = i+2;
				    }
			 
				    parcial = par + non;			 
				    control = (10 - ( parcial % 10));
			 			 
				    if (caracterControlLetra.indexOf(letraIni.toUpperCase()) != -1){
					// El caracter de control deberá ser una letra
			 
					if (controlLetra.charAt(control) != cif.charAt(8).toUpperCase()){
					    alert("El Cif no es válido",3);
					    return -2;
					}
				    }
				    if (caracterControlNum.indexOf(letraIni.toUpperCase()) != -1)
				    {
					// El caracter de control deberá ser un número
			 
					if (control == 10) control = 0;			 
					if (control != cif.charAt(8)){
					    alert("El Cif no es válido",3);
					    return -2;
					}
				    }
				    if (caracterControlLetra.indexOf(letraIni.toUpperCase()) == -1)
					if(caracterControlNum.indexOf(letraIni.toUpperCase()) == -1){
					// En este caso el carácter de control puede ser una letra o un número			 
					if (control == 10){
					    control = 0;
					}
					if (controlLetra.charAt(control) != cif.charAt(8).toUpperCase())
						if(control != cif.charAt(8)){
						    alert("El Cif no es válido",3);
						    return -2;
						}
				    }	
				}
				return 2;
			}


			//Retorna: 1 = NIF ok, 2 = CIF ok, 3 = NIE ok, -1 = NIF error, -2 = CIF error, -3 = NIE error, 0 = ??? error
			function valida_nif_cif_nie(campoNif){
				var a = escape(campoNif.value.toUpperCase());
				var temp=a.toUpperCase();
				campoNif.value = temp;
				var cadenadni="TRWAGMYFPDXBNJZSQVHLCKE";
 
				if (temp!=''){
				//si no tiene un formato valido devuelve error
					if ((!/^[A-Z]{1}[0-9]{7}[A-Z0-9]{1}$/.test(temp)))
						if( !/^[T]{1}[A-Z0-9]{8}$/.test(temp))
							if(!/^[0-9]{8}[A-Z]{1}$/.test(temp)){
								return 0;
							}
 
					//comprobacion de NIFs estandar
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
 
					//algoritmo para comprobacion de codigos tipo CIF
					suma = parseInt(a.charAt(2))+parseInt(a.charAt(4))+parseInt(a.charAt(6));
					var i = 1;
					var fin = false;
					while (!fin){
						temp1 = 2 * parseInt(a.charAt(i));
						temp1 += '';
						temp1 = temp1.substring(0,1);
						temp2 = 2 * parseInt(a.charAt(i));
						temp2 += '';
						temp2 = temp2.substring(1,2);
						if (temp2 == ''){
							temp2 = '0';
						}
						suma += (parseInt(temp1) + parseInt(temp2));
						i = i+2;
						if(i!=8 || i!=9 || i==100){
							fin = true;
						}
					}
					suma += '';
					n = 10 - parseInt(suma.substring(suma.length-1, suma.length));
					//comprobacion de NIFs especiales (se calculan como CIFs)
					if (/^[KLM]{1}/.test(temp)){
						if (a.charAt(8) == String.fromCharCode(64 + n)){
							return 1;
						}
						else{
							return -1;
						}
					}
	 
					//comprobacion de CIFs
					if (/^[ABCDEFGHJNPQRSUVW]{1}/.test(temp)){
						return validaCIF(a);
					}
 
					//comprobacion de NIEs
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

		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
   		
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
					<xsl:value-of select="$lang.cargo"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">cargo</xsl:attribute>
					<xsl:attribute name="id">cargo</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/cargo"/></xsl:attribute>
				</input>
			</div>	
		</div>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosRepresentante"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nif_repre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:100px; </xsl:attribute>
					<xsl:attribute name="name">nif_repre</xsl:attribute>
					<xsl:attribute name="id">nif_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nif_repre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nombre_repre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">nombre_repre</xsl:attribute>
					<xsl:attribute name="id">nombre_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombre_repre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.domicilio_repre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">domicilio_repre</xsl:attribute>
					<xsl:attribute name="id">domicilio_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/domicilio_repre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.ciudad_repre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">ciudad_repre</xsl:attribute>
					<xsl:attribute name="id">ciudad_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ciudad_repre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.c_postal_repre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:60px; </xsl:attribute>
					<xsl:attribute name="name">c_postal_repre</xsl:attribute>
					<xsl:attribute name="id">c_postal_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/c_postal_repre"/></xsl:attribute>
				</input>
			</div>	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.region_repre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">region_repre</xsl:attribute>
					<xsl:attribute name="id">region_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/region_repre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.movil_repre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">movil_repre</xsl:attribute>
					<xsl:attribute name="id">movil_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/movil_repre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.d_email_repre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">d_email_repre</xsl:attribute>
					<xsl:attribute name="id">d_email_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/d_email_repre"/></xsl:attribute>
				</input>
			</div>
   		</div>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosInteresado"/></h1>
   		</div>
   		<div class="cuadro" style="">				
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
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.representante"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">ayuntamiento</xsl:attribute>
					<xsl:attribute name="id">ayuntamiento</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ayuntamiento"/></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">Descripcion_ayuntamiento</xsl:attribute>
					<xsl:attribute name="id">Descripcion_ayuntamiento</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Descripcion_ayuntamiento"/></xsl:attribute>
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Domicilio_Notificacion"/></xsl:attribute>
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
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
		<!-- MQE Cargamos los expedientes sobre los que puede subsanar-->	
   		<div class="cuadro" style="">
			<div class="col" style="display:none;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.numexp"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:200px; </xsl:attribute>
					<xsl:attribute name="name">numExpediente</xsl:attribute>
					<xsl:attribute name="id">numExpediente</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/numExpediente"/></xsl:attribute>
				</input>
				<img onclick="getAsuntoNumexp(document.getElementById('numExpediente').value);" src="img/search-mg.gif"/>
			</div>
			<br/>
   			<div class="col" style="display:none;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.asunto"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">asunto</xsl:attribute>
					<xsl:attribute name="id">asunto</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/asunto"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			
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
					<xsl:value-of select="Datos_Registro/datos_especificos/expone"/>
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
					<xsl:value-of select="Datos_Registro/datos_especificos/solicita"/>
				</textarea>
			</div>	
		</div>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<label class="gr">
		   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
				<xsl:value-of select="$lang.anexarInfo1"/>
		  		<br/>
		   		<xsl:value-of select="$lang.anexarInfo2"/><br/>
		   		<xsl:value-of select="$lang.anexarInfo3"/><br/>
			</label>
			<br/>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_SUBJUS_D1</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_SUBJUS_D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip,rar</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo_DOC_ODT"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_SUBJUS_D2</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_SUBJUS_D2_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D2_Tipo</xsl:attribute>
					<xsl:attribute name="value">odt,doc</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoPDF"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_SUBJUS_D3</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D3</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_SUBJUS_D3_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D3_Tipo</xsl:attribute>
					<xsl:attribute name="value">PDF</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoJPEG"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_SUBJUS_D4</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D4</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_SUBJUS_D4_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D4_Tipo</xsl:attribute>
					<xsl:attribute name="value">JPG</xsl:attribute>
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
		<br/>
		<input type="hidden">
			<xsl:attribute name="name">datosEspecificos</xsl:attribute>
			<xsl:attribute name="id">datosEspecificos</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name">cif</xsl:attribute>
			<xsl:attribute name="id">cif</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name">emailSolicitante</xsl:attribute>
			<xsl:attribute name="id">emailSolicitante</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
	</xsl:template>
</xsl:stylesheet>
