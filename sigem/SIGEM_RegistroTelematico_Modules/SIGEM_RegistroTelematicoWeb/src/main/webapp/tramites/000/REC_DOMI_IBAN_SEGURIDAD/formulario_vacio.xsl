<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'Gestión Tributaria, Inspección y Recaudación. Domiciliaciones'"/>

	<xsl:variable name="lang.datosObligado" select="'Datos del contribuyente'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>

	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>
	<xsl:variable name="lang.representacion" select="'Rellene los siguientes datos si actúa como representante'"/>			
	<xsl:variable name="lang.nif_repr" select="'NIF/CIF de la persona o entidad a quien representa'"/>			
	<xsl:variable name="lang.nombre_repr" select="'Nombre del representado'"/>			
	<xsl:variable name="lang.repres_nota" select="'NOTA: se encuentra disponible un formulario para la representación voluntaria.'"/>			

	<xsl:variable name="lang.nif" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre" select="'Apellidos y Nombre o Denominación Social'"/>
	<xsl:variable name="lang.calle" select="'Domicilio'"/>
	<xsl:variable name="lang.numero" select="'Número'"/>
	<xsl:variable name="lang.escalera" select="'Escalera'"/>
	<xsl:variable name="lang.planta_puerta" select="'Planta/Puerta'"/>
	<xsl:variable name="lang.c_postal" select="'Código Postal'"/>
	<xsl:variable name="lang.ciudad" select="'Ciudad'"/>
	<xsl:variable name="lang.region" select="'Región / País'"/>
	<xsl:variable name="lang.movil" select="'Número de teléfono móvil'"/>
	<xsl:variable name="lang.d_email" select="'Dirección de correo electrónico'"/>	

	<xsl:variable name="lang.objeto_nota" select="'NOTAS:'"/>	
	<xsl:variable name="lang.objeto_nota1" select="'* Sólo cumplimentar en el caso de que sean distintos del contribuyente.'"/>	
	<xsl:variable name="lang.objeto_nota2" select="'** Nº de liquidación, situación del inmueble, matrícula del vehículo o definición de la actividad'"/>	

	<xsl:variable name="lang.municipio" select="'Municipio:'"/>
	<xsl:variable name="lang.apellidos_nombre" select="'Sujeto/s Pasivo/s *'"/>
	<xsl:variable name="lang.tributo" select="'Tributo'"/>
	<xsl:variable name="lang.tipo" select="'Tipo'"/>
	<xsl:variable name="lang.objeto" select="'Objeto Gravamen **'"/>

	<xsl:variable name="lang.entidad" select="'Entidad:'"/>
	<xsl:variable name="lang.sucursal" select="'Sucursal:'"/>
	<xsl:variable name="lang.dc" select="'D.C.:'"/>
	<xsl:variable name="lang.ncc" select="'NºC.C.:'"/>

	<xsl:variable name="lang.datosBancarios" select="'Datos Bancarios'"/>
	<xsl:variable name="lang.titular" select="'Titular de la cuenta:'"/>
	
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero (*.zip), pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.documento" select="'Conjunto de anexos de la solicitud'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>

	<xsl:variable name="lang.legislacion" select="'Arts. 38 y 25 Domiciliación en Entidades de depósito (Reglamento General de Recaudación).'"/>
	<xsl:variable name="lang.nota1" select="'1. Los deudores podrán domiciliar el pago de las deudas a que se refiere este capítulo en cuentas abiertas en Entidades de depósito con oficina en la demarcación.'"/>
	<xsl:variable name="lang.nota2" select="'2. Para ello, dirigirán comunicación al órgano recaudatorio correspondiente al menos dos meses antes del comienzo del periodo recaudatorio. En otro caso, surtirán efecto a partir del periodo siguiente.'"/>
	<xsl:variable name="lang.nota3" select="'3. Las domiciliaciones tendrán validez por tiempo indefinido en tanto no sean anuladas por el interesado, rechazas por la Entidad de depósito o la Administración disponga expresamente su invalidez por razones justificadas.'"/>

	<xsl:variable name="lang.info_legal" select="'Los datos personales, identificativos y de contacto, aportados mediante esta comunicación se entienden facilitados voluntariamente, y serán incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del ámbito de las competencias de esta Administración Pública así como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telemáticos y enviarle invitaciones para eventos y felicitaciones en fechas señaladas. Entenderemos que presta su consentimiento tácito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podrá ejercer sus derechos de acceso, rectificación, cancelación y oposición ante el Responsable del Fichero, la Diputación Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - España, siempre acreditando conforme a Derecho su identidad en la comunicación. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigiéndose a la dirección citada ut supra o bien al correo electrónico lopd@dipucr.es o bien por teléfono al número gratuito 900 714 080.'"/>

		
	<xsl:template match="/" xmlns:java="http://xml.apache.org/xslt/java">		
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(10);

			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');

			validar[2] = new Array('nif', '<xsl:value-of select="$lang.nif"/>');
			validar[3] = new Array('nombre','<xsl:value-of select="$lang.nombre"/>');
			validar[4] = new Array('calle','<xsl:value-of select="$lang.calle"/>');
			validar[5] = new Array('c_postal','<xsl:value-of select="$lang.c_postal"/>');

			validar[6] = new Array('municipio1','municipio1');
			validar[7] = new Array('tributo1','tributo1');
			validar[8] = new Array('tipo','tipo');
			validar[9] = new Array('objeto1','objeto1');

			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(65);	
			
			especificos[0] = new Array('nif','nif');
			especificos[1] = new Array('nombre','nombre');
			especificos[2] = new Array('calle','calle');
			especificos[3] = new Array('c_postal','c_postal');
			especificos[4] = new Array('movil','movil');
			especificos[5] = new Array('d_email','d_email');

			especificos[6] = new Array('repres_nif','repres_nif');

			especificos[7] = new Array('repres_nombre','repres_nombre');
			especificos[8] = new Array('repres_movil','repres_movil');
			especificos[9] = new Array('repres_d_email','repres_d_email');

			especificos[10] = new Array('municipio1','municipio1');
			especificos[11] = new Array('apellidos_nombre1','apellidos_nombre1');
			especificos[12] = new Array('tributo1','tributo1');
			especificos[13] = new Array('objeto1','objeto1');
			especificos[14] = new Array('municipio2','municipio2');
			especificos[15] = new Array('apellidos_nombre2','apellidos_nombre2');
			especificos[16] = new Array('tributo2','tributo2');
			especificos[17] = new Array('objeto2','objeto2');
			especificos[18] = new Array('municipio3','municipio3');
			especificos[19] = new Array('apellidos_nombre3','apellidos_nombre3');
			especificos[20] = new Array('tributo3','tributo3');
			especificos[21] = new Array('objeto3','objeto3');
			especificos[22] = new Array('municipio4','municipio4');
			especificos[23] = new Array('apellidos_nombre4','apellidos_nombre4');
			especificos[24] = new Array('tributo4','tributo4');
			especificos[25] = new Array('objeto4','objeto4');
			especificos[26] = new Array('municipio5','municipio5');
			especificos[27] = new Array('apellidos_nombre5','apellidos_nombre5');
			especificos[28] = new Array('tributo5','tributo5');
			especificos[29] = new Array('objeto5','objeto5');
			especificos[30] = new Array('municipio6','municipio6');
			especificos[31] = new Array('apellidos_nombre6','apellidos_nombre6');
			especificos[32] = new Array('tributo6','tributo6');
			especificos[33] = new Array('objeto6','objeto6');
			especificos[34] = new Array('municipio7','municipio7');
			especificos[35] = new Array('apellidos_nombre7','apellidos_nombre7');
			especificos[36] = new Array('tributo7','tributo7');
			especificos[37] = new Array('objeto7','objeto7');
			especificos[38] = new Array('municipio8','municipio8');
			especificos[39] = new Array('apellidos_nombre8','apellidos_nombre8');
			especificos[40] = new Array('tributo8','tributo8');
			especificos[41] = new Array('objeto8','objeto8');						
			especificos[42] = new Array('entidad','entidad');
			especificos[43] = new Array('sucursal','sucursal');
			especificos[44] = new Array('dc','dc');
			especificos[45] = new Array('ncc','ncc');
			especificos[46] = new Array('titular','titular');
			especificos[47] = new Array('nifTitular','nifTitular');
			especificos[48] = new Array('telefonoTitular','telefonoTitular');
			
			especificos[49] = new Array('nombresolihidden','nombresolihidden');
			especificos[50] = new Array('nifsolihidden','nifsolihidden');
			especificos[51] = new Array('ciudad','ciudad')
			especificos[52] = new Array('region','region');

			especificos[53] = new Array('rcalle','rcalle');
			especificos[54] = new Array('rc_postal','rc_postal');
			especificos[55] = new Array('rciudad','rciudad');
			especificos[56] = new Array('rregion','rregion');

			especificos[57] = new Array('tipo','tipo');
			especificos[58] = new Array('tipo2','tipo2');
			especificos[59] = new Array('tipo3','tipo3');
			especificos[60] = new Array('tipo4','tipo4');
			especificos[61] = new Array('tipo5','tipo5');
			especificos[62] = new Array('tipo6','tipo6');
			especificos[63] = new Array('tipo7','tipo7');
			especificos[64] = new Array('tipo8','tipo8');

			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				var municipio = '';
				var tributo = '';
				var objeto = '';
				var tipo = '';
				var sujeto = '';

				municipio = document.getElementById('municipio1').value;
				tributo = document.getElementById('tributo1');
				objeto = document.getElementById('objeto1');
				tipo = document.getElementById('tipo');
				sujeto = document.getElementById('apellidos_nombre1');

				if(!validarDatos(municipio, tributo, objeto, tipo)){return false;}
				rellenaSujeto(municipio, sujeto);

				municipio = document.getElementById('municipio2').value;
				tributo = document.getElementById('tributo2');
				objeto = document.getElementById('objeto2');
				tipo = document.getElementById('tipo2');
				sujeto = document.getElementById('apellidos_nombre2');

				if(!validarDatos(municipio, tributo, objeto, tipo)){return false;}
				rellenaSujeto(municipio, sujeto);
				
				municipio = document.getElementById('municipio3').value;
				tributo = document.getElementById('tributo3');
				objeto = document.getElementById('objeto3');
				tipo = document.getElementById('tipo3');
				sujeto = document.getElementById('apellidos_nombre3');

				if(!validarDatos(municipio, tributo, objeto, tipo)){return false;}
				rellenaSujeto(municipio, sujeto);

				municipio = document.getElementById('municipio4').value;
				tributo = document.getElementById('tributo4');
				objeto = document.getElementById('objeto4');
				tipo = document.getElementById('tipo4');
				sujeto = document.getElementById('apellidos_nombre4');

				if(!validarDatos(municipio, tributo, objeto, tipo)){return false;}
				rellenaSujeto(municipio, sujeto);
				
				municipio = document.getElementById('municipio5').value;
				tributo = document.getElementById('tributo5');
				objeto = document.getElementById('objeto5');
				tipo = document.getElementById('tipo5');
				sujeto = document.getElementById('apellidos_nombre5');

				if(!validarDatos(municipio, tributo, objeto, tipo)){return false;}
				rellenaSujeto(municipio, sujeto);

				municipio = document.getElementById('municipio6').value;
				tributo = document.getElementById('tributo6');
				objeto = document.getElementById('objeto6');
				tipo = document.getElementById('tipo6');
				sujeto = document.getElementById('apellidos_nombre6');

				if(!validarDatos(municipio, tributo, objeto, tipo)){return false;}
				rellenaSujeto(municipio, sujeto);

				municipio = document.getElementById('municipio7').value;
				tributo = document.getElementById('tributo7');
				objeto = document.getElementById('objeto7');
				tipo = document.getElementById('tipo7');
				sujeto = document.getElementById('apellidos_nombre7');

				if(!validarDatos(municipio, tributo, objeto, tipo)){return false;}
				rellenaSujeto(municipio, sujeto);

				municipio = document.getElementById('municipio8').value;
				tributo = document.getElementById('tributo8');
				objeto = document.getElementById('objeto8');
				tipo = document.getElementById('tipo8');
				sujeto = document.getElementById('apellidos_nombre8');

				if(!validarDatos(municipio, tributo, objeto, tipo)){return false;}
				rellenaSujeto(municipio, sujeto);

				if(!validarCuentaBancaria()){return false;}
				if(!validaCC()) return false;

				if(!validaNIFObligado()) return false;
				if(!validaNIFRepresentante()) return false;
				
				if(!validaNIFPresentador()) return false;
				else{
					document.getElementById('nombresolihidden').value = document.getElementById('nombreSolicitante').value;
					document.getElementById('nifsolihidden').value = document.getElementById('documentoIdentidad').value;
				}

				return true;
			}

			function validarCuentaBancaria(){
				var ccc1 = document.getElementById('entidad');
				var ccc2 = document.getElementById('sucursal');
				var ccc3 = document.getElementById('dc');
				var ccc4 = document.getElementById('ncc');

				if (ccc1.value != ""  || ccc2.value != "" ||
						ccc3.value != "" || ccc4.value != "") 
			
				if (ccc1.value == ""  || ccc2.value == "" ||
						ccc3.value == "" || ccc4.value == "") {
					alert("Por favor, introduzca correctamente los datos de su cuenta;"
							+ " no están completos");
					return false;
				} else {
					if (ccc1.value.length != 4 || ccc2.value.length != 4 ||
							ccc3.value.length != 2 || ccc4.value.length != 10) {
						alert("Por favor, introduzca correctamente los datos de su cuenta;"
							+ " no están completos");
						return false;
					} else {
						if (!numerico(ccc1.value) || !numerico(ccc2.value) ||
								!numerico(ccc3.value) || !numerico(ccc4.value)) {
							alert("Por favor, introduzca correctamente los datos de su "
								+ "cuenta; no son numéricos");
							return false;
						} else {
							if (!(obtenerDigito("00" + ccc1.value + ccc2.value) ==
									parseInt(ccc3.value.charAt(0))) || 
									!(obtenerDigito(ccc4.value) ==
									parseInt(ccc3.value.charAt(1)))) {
								alert("Los dígitos de control no se corresponden con los demás"
									+ " números de la cuenta");
								return false;
							} else {
								return true;
							}
						}
					}
				}
				return true;
			} 

			function validaCC(){
				var valorNifTit = document.getElementById('nifTitular');
				if(document.getElementById('entidad').value != null){
					if(document.getElementById('entidad').value!=''){
						if(!validaSiTitularCC()) return false;
						if(valorNifTit.value !=null){
							if(valorNifTit.value!=''){
								var validaNifTit = valida_nif_cif_nie(valorNifTit);
								if(validaNifTit != 1)
									if(validaNifTit != 2)
										if(validaNifTit != 3){
											alert('El NIF/CIF del titular de la cuenta es incorrecto');
											valorNifTit.focus();			
											return false;
										}
							}
						}
					}
					else{
						document.getElementById('titular').value='';
						document.getElementById('nifTitular').value='';
						document.getElementById('telefonoTitular').value='';
					}
				}
				else{
					document.getElementById('titular').value='';
					document.getElementById('nifTitular').value='';
					document.getElementById('telefonoTitular').value='';
				}
				return true;
			}

			function validaSiTitularCC(){
				var valorNifTit = document.getElementById('nifTitular');
				if(document.getElementById('entidad').value != null){
					if(document.getElementById('entidad').value!=''){
						if(valorNifTit.value ==null || valorNifTit.value==''){
							alert('Debe indicar el NIF/CIF del titular de la cuenta');
							return false();
						}
					}
				}
				return true;
			}


			function obtenerDigito(valor){
				valores = new Array(1, 2, 4, 8, 5, 10, 9, 7, 3, 6);
				control = 0;
				for (i=0; i &lt;=9; i++)
					control += parseInt(valor.charAt(i)) * valores[i];
					control = 11 - (control % 11);
					if (control == 11) 
						control = 0;
					else if (control == 10) 
						control = 1;
					return control;
			}

			function numerico(valor){
				cad = valor.toString();
				for (var i=0; i &lt; cad.length; i++) {
					var caracter = cad.charAt(i);
					if (caracter &lt; "0" || caracter &gt; "9")
						return false;
				}
				return true;
			}


			function getDatosObligado(nif){
				window.open('tramites/000/REC_DOMI/buscaObligado.jsp?valor='+nif+';000','','width=3,height=3');
			}
			function getDatosRepresentante(nif){
				window.open('tramites/000/REC_DOMI/buscaRepresentante.jsp?valor='+nif+';000','','width=3,height=3');
			}
			
			function validarDatos(municipio, tributo, objeto, tipo){
				if(municipio == null || municipio =='000'){}
				else{
					if(tributo.value == null || tributo.value == '' || tributo.value == '000'){
						alert('El campo tributo no puede ser vacío')
						tributo.focus();
						return false;
					}
					if(tipo.value == null || tipo.value == '' || tipo.value == '0'){
						alert('El campo tipo no puede ser vacío')
						tipo.focus();
						return false;
					}
					if(objeto.value == null || objeto.value == ''){
						alert('El campo objeto no puede ser vacío')
						objeto.focus();
						return false;
					}										
				}
				return true;
			}

			//Si el sujeto es vacío pone el obligado
			function rellenaSujeto(municipio, sujeto){
				if(municipio == null || municipio =='000'){}
				else{
					if(sujeto.value == null || sujeto.value == ''){
						sujeto.value = document.getElementById('nombre').value;
					}
				}
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

		</script>

	<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<xsl:call-template name="BOTON_LIMPIAR_FORMULARIO" />
   		<br/>
		<div class="submenu">
			<xsl:variable name="lang.datosPresentador" select="'Datos del presentador'"/>
   			<h1><xsl:value-of select="$lang.datosPresentador"/></h1>
   		</div>

		<xsl:call-template name="DATOS_SOLICITANTE_EDITABLE" />		<script>
			function validaNIFPresentador(){
				var valorNifPresen = document.getElementById('documentoIdentidad');
				if(valorNifPresen.value != null){
					if(valorNifPresen.value != ''){
						var validaNifPresen = valida_nif_cif_nie(valorNifPresen);
						if(validaNifPresen != 1)
							if(validaNifPresen != 2)
								if(validaNifPresen  != 3){
									alert('El NIF/CIF del presentador es incorrecto');
									valorNifPresen.focus();			
									return false;
								}
					}
				}
				return true;
			}	
		</script>

		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:640px; font-size:8; </xsl:attribute>
					<b><i><xsl:value-of select="$lang.legislacion"/></i></b>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:640px; font-size:10</xsl:attribute>
					<xsl:value-of select="$lang.nota1"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:640px; font-size:10</xsl:attribute>
					<xsl:value-of select="$lang.nota2"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:640px; font-size:10</xsl:attribute>
					<xsl:value-of select="$lang.nota3"/>
				</label>
			</div>
		</div>
   		<div class="submenu" style="">
   			<h1><xsl:value-of select="$lang.datosObligado"/></h1>
   		</div>
   		<div class="cuadro" style="">			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nif"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:100px; </xsl:attribute>
					<xsl:attribute name="name">nif</xsl:attribute>
					<xsl:attribute name="id">nif</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nif"/></xsl:attribute>
				</input>
				<img onclick="getDatosObligado(document.getElementById('nif').value);" src="img/search-mg.gif"/>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nombre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">nombre</xsl:attribute>
					<xsl:attribute name="id">nombre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.calle"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">calle</xsl:attribute>
					<xsl:attribute name="id">calle</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/calle"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.ciudad"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">ciudad</xsl:attribute>
					<xsl:attribute name="id">ciudad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ciudad"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.c_postal"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:60px; </xsl:attribute>
					<xsl:attribute name="name">c_postal</xsl:attribute>
					<xsl:attribute name="id">c_postal</xsl:attribute>
					<xsl:attribute name="maxlength">5</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/c_postal"/></xsl:attribute>
				</input>
			</div>	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.region"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">region</xsl:attribute>
					<xsl:attribute name="id">region</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/region"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.movil"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">movil</xsl:attribute>
					<xsl:attribute name="id">movil</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/movil"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.d_email"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">d_email</xsl:attribute>
					<xsl:attribute name="id">d_email</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/d_email"/></xsl:attribute>
				</input>
			</div>
			<div style="margin-top:20px;margin-bottom:10px;color:#006699;">
				<b>Representante o Presentador Autorizado:</b>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nif"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:100px; </xsl:attribute>
					<xsl:attribute name="name">repres_nif</xsl:attribute>
					<xsl:attribute name="id">repres_nif</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/repres_nif"/></xsl:attribute>
				</input>
				<img onclick="getDatosRepresentante(document.getElementById('repres_nif').value);" src="img/search-mg.gif"/>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nombre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">repres_nombre</xsl:attribute>
					<xsl:attribute name="id">repres_nombre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/repres_nombre"/></xsl:attribute>
				</input>
			</div>
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.calle"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">rcalle</xsl:attribute>
					<xsl:attribute name="id">rcalle</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/rcalle"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.ciudad"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">rciudad</xsl:attribute>
					<xsl:attribute name="id">rciudad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/rciudad"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.c_postal"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:60px; </xsl:attribute>
					<xsl:attribute name="name">rc_postal</xsl:attribute>
					<xsl:attribute name="id">rc_postal</xsl:attribute>
					<xsl:attribute name="maxlength">5</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/rc_postal"/></xsl:attribute>
				</input>
			</div>	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.region"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">rregion</xsl:attribute>
					<xsl:attribute name="id">rregion</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/rregion"/></xsl:attribute>
				</input>
			</div>


			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.movil"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">repres_movil</xsl:attribute>
					<xsl:attribute name="id">repres_movil</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/repres_movil"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.d_email"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">repres_d_email</xsl:attribute>
					<xsl:attribute name="id">repres_d_email</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/repres_d_email"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">margin-left:150px;width:490px;</xsl:attribute>
					<i><xsl:value-of select="$lang.repres_nota"/></i>
				</label>
			</div>
		</div>
		<script>
			function validaNIFObligado(){
				var valorNif = document.getElementById('nif');
				var validaNif = valida_nif_cif_nie(valorNif);
				if(validaNif != 1)
					if(validaNif != 2)
						if(validaNif != 3){
							alert('El NIF/CIF del obligado tributario es incorrecto');
							valorNif.focus();			
							return false;
						}
				return true;
			}

			function validaNIFRepresentante(){
				var valorNifRepre = document.getElementById('repres_nif');
				if(valorNifRepre.value != null){
					if(valorNifRepre.value != ''){
						var validaNifRepre = valida_nif_cif_nie(valorNifRepre);
						if(validaNifRepre != 1)
							if(validaNifRepre != 2)
								if(validaNifRepre!= 3){
									alert('El NIF/CIF del representante es incorrecto');
									valorNifRepre.focus();			
									return false;
								}
					}
				}
				return true;
			}	
		</script>

		<br/>
   		<div class="submenu" style="">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
		<xsl:variable name="fileAyuntam" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosTablaValidacionConSustituto('REC_VLDTBL_MUNICIPIOS','000')"/>
			<xsl:variable name="b" select="document($fileAyuntam)"/>

			<xsl:variable name="fileTributos" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosTablaValidacionConSustituto('REC_TRIBUTOS','000')"/>
			<xsl:variable name="c" select="document($fileTributos)"/>

			<xsl:variable name="fileAltaBaja" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosTablaValidacionConSustituto('REC_ALTA_BAJA','000')"/>
			<xsl:variable name="d" select="document($fileAltaBaja)"/>
		<div class="cuadro" style="">			

			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">margin-left:150px;width:490px;</xsl:attribute>
					<xsl:attribute name="align">left</xsl:attribute>
					<i><xsl:value-of select="$lang.objeto_nota"/></i>
				</label>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">margin-left:160px;width:490px;</xsl:attribute>
					<xsl:attribute name="align">left</xsl:attribute>
					<i><xsl:value-of select="$lang.objeto_nota1"/></i>
				</label>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">margin-left:160px;width:490px;</xsl:attribute>
					<xsl:attribute name="align">left</xsl:attribute>
					<i><xsl:value-of select="$lang.objeto_nota2"/></i>
				</label>
			</div>
			<div class="col">	
				<b>
					<label class="gr">
						<xsl:attribute name="style">width:100px;</xsl:attribute>
						<xsl:value-of select="$lang.municipio"/>
					</label>
				</b>
				<!-- Carga de los ayuntamientos de la provincia de Ciudad Real-->
				<select class="gr">
					<xsl:attribute name="style">border:none; width:250px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">municipio1</xsl:attribute>
					<xsl:attribute name="id">municipio1</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/municipio1"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>		
				<br/>	
				<label>
					<xsl:attribute name="style">width:270px;margin-bottom:1px;</xsl:attribute>
					<xsl:value-of select="$lang.apellidos_nombre"/>
				</label>
				<label>
					<xsl:attribute name="style">width:150px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tributo"/>
				</label>
				<label>
					<xsl:attribute name="style">width:100px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tipo"/>
				</label>
				<br/>
				<input type="text">
					<xsl:attribute name="style">width:270px;margin-left:1px;margin-bottom:1px;margin-top:5px;</xsl:attribute>
					<xsl:attribute name="name">apellidos_nombre1</xsl:attribute>
					<xsl:attribute name="id">apellidos_nombre1</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/apellidos_nombre1"/></xsl:attribute>
				</input>				
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tributo1</xsl:attribute>
					<xsl:attribute name="id">tributo1</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tributo1"/></xsl:attribute>
					<xsl:for-each select="$c/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tipo</xsl:attribute>
					<xsl:attribute name="id">tipo</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tipo"/></xsl:attribute>
					<xsl:for-each select="$d/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<br/>							
				<label>
					<xsl:attribute name="style">width:220px;</xsl:attribute>
					<xsl:value-of select="$lang.objeto"/>				
				</label>
				<br/>
				<textarea type="text">
					<xsl:attribute name="style">margin-bottom:1px;</xsl:attribute>
					<xsl:attribute name="name">objeto1</xsl:attribute>
					<xsl:attribute name="id">objeto1</xsl:attribute>
					<xsl:attribute name="cols">75</xsl:attribute>
					<xsl:attribute name="rows">2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/objeto1"/></xsl:attribute>
				</textarea>
			</div>
			<div class="col">		   					
				<b>
					<label class="gr">
						<xsl:attribute name="style">width:100px;</xsl:attribute>
						<xsl:value-of select="$lang.municipio"/>
					</label>
				</b>
				<!-- Carga de los ayuntamientos de la provincia de Ciudad Real-->
				<select class="gr">
					<xsl:attribute name="style">border:none; width:250px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">municipio2</xsl:attribute>
					<xsl:attribute name="id">municipio2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/municipio2"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>		
				<br/>	
				<label>
					<xsl:attribute name="style">width:270px;margin-bottom:1px;</xsl:attribute>
					<xsl:value-of select="$lang.apellidos_nombre"/>
				</label>
				<label>
					<xsl:attribute name="style">width:150px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tributo"/>
				</label>
				<label>
					<xsl:attribute name="style">width:100px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tipo"/>
				</label>
				<br/>
				<input type="text">
					<xsl:attribute name="style">width:270px;margin-left:1px;margin-bottom:1px;margin-top:5px;</xsl:attribute>
					<xsl:attribute name="name">apellidos_nombre2</xsl:attribute>
					<xsl:attribute name="id">apellidos_nombre2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/apellidos_nombre2"/></xsl:attribute>
				</input>				
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tributo2</xsl:attribute>
					<xsl:attribute name="id">tributo2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tributo2"/></xsl:attribute>
					<xsl:for-each select="$c/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tipo2</xsl:attribute>
					<xsl:attribute name="id">tipo2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tipo2"/></xsl:attribute>
					<xsl:for-each select="$d/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<br/>							
				<label>
					<xsl:attribute name="style">width:220px;</xsl:attribute>
					<xsl:value-of select="$lang.objeto"/>				
				</label>
				<br/>
				<textarea type="text">
					<xsl:attribute name="style">margin-bottom:1px;</xsl:attribute>
					<xsl:attribute name="name">objeto2</xsl:attribute>
					<xsl:attribute name="id">objeto2</xsl:attribute>
					<xsl:attribute name="cols">75</xsl:attribute>
					<xsl:attribute name="rows">2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/objeto2"/></xsl:attribute>
				</textarea>
			</div>
			<div class="col">		   					
				<b>
					<label class="gr">
						<xsl:attribute name="style">width:100px;</xsl:attribute>
						<xsl:value-of select="$lang.municipio"/>
					</label>
				</b>
				<!-- Carga de los ayuntamientos de la provincia de Ciudad Real-->
				<select class="gr">
					<xsl:attribute name="style">border:none; width:250px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">municipio3</xsl:attribute>
					<xsl:attribute name="id">municipio3</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/municipio3"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>		
				<br/>	
				<label>
					<xsl:attribute name="style">width:270px;margin-bottom:1px;</xsl:attribute>
					<xsl:value-of select="$lang.apellidos_nombre"/>
				</label>
				<label>
					<xsl:attribute name="style">width:150px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tributo"/>
				</label>
				<label>
					<xsl:attribute name="style">width:100px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tipo"/>
				</label>
				<br/>
				<input type="text">
					<xsl:attribute name="style">width:270px;margin-left:1px;margin-bottom:1px;margin-top:5px;</xsl:attribute>
					<xsl:attribute name="name">apellidos_nombre3</xsl:attribute>
					<xsl:attribute name="id">apellidos_nombre3</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/apellidos_nombre3"/></xsl:attribute>
				</input>				
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tributo3</xsl:attribute>
					<xsl:attribute name="id">tributo3</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tributo3"/></xsl:attribute>
					<xsl:for-each select="$c/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tipo3</xsl:attribute>
					<xsl:attribute name="id">tipo3</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tipo3"/></xsl:attribute>
					<xsl:for-each select="$d/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<br/>							
				<label>
					<xsl:attribute name="style">width:220px;</xsl:attribute>
					<xsl:value-of select="$lang.objeto"/>				
				</label>
				<br/>
				<textarea type="text">
					<xsl:attribute name="style">margin-bottom:1px;</xsl:attribute>
					<xsl:attribute name="name">objeto3</xsl:attribute>
					<xsl:attribute name="id">objeto3</xsl:attribute>
					<xsl:attribute name="cols">75</xsl:attribute>
					<xsl:attribute name="rows">2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/objeto3"/></xsl:attribute>
				</textarea>
			</div>
			<div class="col">		   					
				<b>
					<label class="gr">
						<xsl:attribute name="style">width:100px;</xsl:attribute>
						<xsl:value-of select="$lang.municipio"/>
					</label>
				</b>
				<!-- Carga de los ayuntamientos de la provincia de Ciudad Real-->
				<select class="gr">
					<xsl:attribute name="style">border:none; width:250px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">municipio4</xsl:attribute>
					<xsl:attribute name="id">municipio4</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/municipio4"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>		
				<br/>	
				<label>
					<xsl:attribute name="style">width:270px;margin-bottom:1px;</xsl:attribute>
					<xsl:value-of select="$lang.apellidos_nombre"/>
				</label>
				<label>
					<xsl:attribute name="style">width:150px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tributo"/>
				</label>
				<label>
					<xsl:attribute name="style">width:100px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tipo"/>
				</label>
				<br/>
				<input type="text">
					<xsl:attribute name="style">width:270px;margin-left:1px;margin-bottom:1px;margin-top:5px;</xsl:attribute>
					<xsl:attribute name="name">apellidos_nombre4</xsl:attribute>
					<xsl:attribute name="id">apellidos_nombre4</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/apellidos_nombre4"/></xsl:attribute>
				</input>				
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tributo4</xsl:attribute>
					<xsl:attribute name="id">tributo4</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tributo4"/></xsl:attribute>
					<xsl:for-each select="$c/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tipo4</xsl:attribute>
					<xsl:attribute name="id">tipo4</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tipo4"/></xsl:attribute>
					<xsl:for-each select="$d/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<br/>							
				<label>
					<xsl:attribute name="style">width:220px;</xsl:attribute>
					<xsl:value-of select="$lang.objeto"/>				
				</label>
				<br/>
				<textarea type="text">
					<xsl:attribute name="style">margin-bottom:1px;</xsl:attribute>
					<xsl:attribute name="name">objeto4</xsl:attribute>
					<xsl:attribute name="id">objeto4</xsl:attribute>
					<xsl:attribute name="cols">75</xsl:attribute>
					<xsl:attribute name="rows">2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/objeto4"/></xsl:attribute>
				</textarea>
			</div>
			<div class="col">		   					
				<b>
					<label class="gr">
						<xsl:attribute name="style">width:100px;</xsl:attribute>
						<xsl:value-of select="$lang.municipio"/>
					</label>
				</b>
				<!-- Carga de los ayuntamientos de la provincia de Ciudad Real-->
				<select class="gr">
					<xsl:attribute name="style">border:none; width:250px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">municipio5</xsl:attribute>
					<xsl:attribute name="id">municipio5</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/municipio5"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>		
				<br/>	
				<label>
					<xsl:attribute name="style">width:270px;margin-bottom:1px;</xsl:attribute>
					<xsl:value-of select="$lang.apellidos_nombre"/>
				</label>
				<label>
					<xsl:attribute name="style">width:150px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tributo"/>
				</label>
				<label>
					<xsl:attribute name="style">width:100px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tipo"/>
				</label>
				<br/>
				<input type="text">
					<xsl:attribute name="style">width:270px;margin-left:1px;margin-bottom:1px;margin-top:5px;</xsl:attribute>
					<xsl:attribute name="name">apellidos_nombre5</xsl:attribute>
					<xsl:attribute name="id">apellidos_nombre5</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/apellidos_nombre5"/></xsl:attribute>
				</input>				
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tributo5</xsl:attribute>
					<xsl:attribute name="id">tributo5</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tributo5"/></xsl:attribute>
					<xsl:for-each select="$c/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tipo5</xsl:attribute>
					<xsl:attribute name="id">tipo5</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tipo5"/></xsl:attribute>
					<xsl:for-each select="$d/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<br/>							
				<label>
					<xsl:attribute name="style">width:220px;</xsl:attribute>
					<xsl:value-of select="$lang.objeto"/>				
				</label>
				<br/>
				<textarea type="text">
					<xsl:attribute name="style">margin-bottom:1px;</xsl:attribute>
					<xsl:attribute name="name">objeto5</xsl:attribute>
					<xsl:attribute name="id">objeto5</xsl:attribute>
					<xsl:attribute name="cols">75</xsl:attribute>
					<xsl:attribute name="rows">2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/objeto5"/></xsl:attribute>
				</textarea>
			</div>
			<div class="col">		   					
				<b>
					<label class="gr">
						<xsl:attribute name="style">width:100px;</xsl:attribute>
						<xsl:value-of select="$lang.municipio"/>
					</label>
				</b>
				<!-- Carga de los ayuntamientos de la provincia de Ciudad Real-->
				<select class="gr">
					<xsl:attribute name="style">border:none; width:250px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">municipio6</xsl:attribute>
					<xsl:attribute name="id">municipio6</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/municipio6"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>		
				<br/>	
				<label>
					<xsl:attribute name="style">width:270px;margin-bottom:1px;</xsl:attribute>
					<xsl:value-of select="$lang.apellidos_nombre"/>
				</label>
				<label>
					<xsl:attribute name="style">width:150px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tributo"/>
				</label>
				<label>
					<xsl:attribute name="style">width:100px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tipo"/>
				</label>
				<br/>
				<input type="text">
					<xsl:attribute name="style">width:270px;margin-left:1px;margin-bottom:1px;margin-top:5px;</xsl:attribute>
					<xsl:attribute name="name">apellidos_nombre6</xsl:attribute>
					<xsl:attribute name="id">apellidos_nombre6</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/apellidos_nombre6"/></xsl:attribute>
				</input>				
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tributo6</xsl:attribute>
					<xsl:attribute name="id">tributo6</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tributo6"/></xsl:attribute>
					<xsl:for-each select="$c/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tipo6</xsl:attribute>
					<xsl:attribute name="id">tipo6</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tipo6"/></xsl:attribute>
					<xsl:for-each select="$d/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<br/>							
				<label>
					<xsl:attribute name="style">width:220px;</xsl:attribute>
					<xsl:value-of select="$lang.objeto"/>				
				</label>
				<br/>
				<textarea type="text">
					<xsl:attribute name="style">margin-bottom:1px;</xsl:attribute>
					<xsl:attribute name="name">objeto6</xsl:attribute>
					<xsl:attribute name="id">objeto6</xsl:attribute>
					<xsl:attribute name="cols">75</xsl:attribute>
					<xsl:attribute name="rows">2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/objeto6"/></xsl:attribute>
				</textarea>
			</div>
			<div class="col">		   					
				<b>
					<label class="gr">
						<xsl:attribute name="style">width:100px;</xsl:attribute>
						<xsl:value-of select="$lang.municipio"/>
					</label>
				</b>
				<!-- Carga de los ayuntamientos de la provincia de Ciudad Real-->
				<select class="gr">
					<xsl:attribute name="style">border:none; width:250px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">municipio7</xsl:attribute>
					<xsl:attribute name="id">municipio7</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/municipio7"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>		
				<br/>	
				<label>
					<xsl:attribute name="style">width:270px;margin-bottom:1px;</xsl:attribute>
					<xsl:value-of select="$lang.apellidos_nombre"/>
				</label>
				<label>
					<xsl:attribute name="style">width:150px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tributo"/>
				</label>
				<label>
					<xsl:attribute name="style">width:100px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tipo"/>
				</label>
				<br/>
				<input type="text">
					<xsl:attribute name="style">width:270px;margin-left:1px;margin-bottom:1px;margin-top:5px;</xsl:attribute>
					<xsl:attribute name="name">apellidos_nombre7</xsl:attribute>
					<xsl:attribute name="id">apellidos_nombre7</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/apellidos_nombre7"/></xsl:attribute>
				</input>				
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tributo7</xsl:attribute>
					<xsl:attribute name="id">tributo7</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tributo7"/></xsl:attribute>
					<xsl:for-each select="$c/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tipo7</xsl:attribute>
					<xsl:attribute name="id">tipo7</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tipo7"/></xsl:attribute>
					<xsl:for-each select="$d/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<br/>							
				<label>
					<xsl:attribute name="style">width:220px;</xsl:attribute>
					<xsl:value-of select="$lang.objeto"/>				
				</label>
				<br/>
				<textarea type="text">
					<xsl:attribute name="style">margin-bottom:1px;</xsl:attribute>
					<xsl:attribute name="name">objeto7</xsl:attribute>
					<xsl:attribute name="id">objeto7</xsl:attribute>
					<xsl:attribute name="cols">75</xsl:attribute>
					<xsl:attribute name="rows">2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/objeto7"/></xsl:attribute>
				</textarea>
			</div>
			<div class="col">		   					
				<b>
					<label class="gr">
						<xsl:attribute name="style">width:100px;</xsl:attribute>
						<xsl:value-of select="$lang.municipio"/>
					</label>
				</b>
				<!-- Carga de los ayuntamientos de la provincia de Ciudad Real-->
				<select class="gr">
					<xsl:attribute name="style">border:none; width:250px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">municipio8</xsl:attribute>
					<xsl:attribute name="id">municipio8</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/municipio8"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>		
				<br/>	
				<label>
					<xsl:attribute name="style">width:270px;margin-bottom:1px;</xsl:attribute>
					<xsl:value-of select="$lang.apellidos_nombre"/>
				</label>
				<label>
					<xsl:attribute name="style">width:150px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tributo"/>
				</label>
				<label>
					<xsl:attribute name="style">width:100px;margin-bottom:1px;margin-left:10px;</xsl:attribute>
					<xsl:value-of select="$lang.tipo"/>
				</label>
				<br/>
				<input type="text">
					<xsl:attribute name="style">width:270px;margin-left:1px;margin-bottom:1px;margin-top:5px;</xsl:attribute>
					<xsl:attribute name="name">apellidos_nombre8</xsl:attribute>
					<xsl:attribute name="id">apellidos_nombre8</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/apellidos_nombre8"/></xsl:attribute>
				</input>				
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tributo8</xsl:attribute>
					<xsl:attribute name="id">tributo8</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tributo8"/></xsl:attribute>
					<xsl:for-each select="$c/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<select class="gr">
					<xsl:attribute name="style">border:none; width:150px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">tipo8</xsl:attribute>
					<xsl:attribute name="id">tipo8</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tipo8"/></xsl:attribute>
					<xsl:for-each select="$d/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
				<br/>							
				<label>
					<xsl:attribute name="style">width:220px;</xsl:attribute>
					<xsl:value-of select="$lang.objeto"/>				
				</label>
				<br/>
				<textarea type="text">
					<xsl:attribute name="style">margin-bottom:1px;</xsl:attribute>
					<xsl:attribute name="name">objeto8</xsl:attribute>
					<xsl:attribute name="id">objeto8</xsl:attribute>
					<xsl:attribute name="cols">75</xsl:attribute>
					<xsl:attribute name="rows">2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/objeto8"/></xsl:attribute>
				</textarea>
			</div>
		</div>
		<br/>
   		<div class="submenu" style="">
   			<h1><xsl:value-of select="$lang.datosBancarios"/></h1>
   		</div>
		<div class="cuadro" style="">
			<div class="col">
				<!--Datos de la cuenta Bancaria-->			
				<label>
					<xsl:attribute name="style">width:55px;</xsl:attribute>
					<xsl:value-of select="$lang.entidad"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:40px;margin-left:7px</xsl:attribute>
					<xsl:attribute name="size">4</xsl:attribute>
					<xsl:attribute name="maxlength">4</xsl:attribute>
					<xsl:attribute name="name">entidad</xsl:attribute>
					<xsl:attribute name="id">entidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/entidad"/></xsl:attribute>
				</input>
				<br/>
				<label>
					<xsl:attribute name="style">width:55px;</xsl:attribute>
					<xsl:value-of select="$lang.sucursal"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:40px;margin-left:7px</xsl:attribute>
					<xsl:attribute name="size">4</xsl:attribute>
					<xsl:attribute name="maxlength">4</xsl:attribute>
					<xsl:attribute name="name">sucursal</xsl:attribute>
					<xsl:attribute name="id">sucursal</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/sucursal"/></xsl:attribute>
				</input>
				<br/>
				<label>
					<xsl:attribute name="style">width:55px;</xsl:attribute>
					<xsl:value-of select="$lang.dc"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:20px;margin-left:7px</xsl:attribute>
					<xsl:attribute name="size">2</xsl:attribute>
					<xsl:attribute name="maxlength">2</xsl:attribute>
					<xsl:attribute name="name">dc</xsl:attribute>
					<xsl:attribute name="id">dc</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/dc"/></xsl:attribute>
				</input>
				<br/>				
				<label>
					<xsl:attribute name="style">width:55px;</xsl:attribute>
					<xsl:value-of select="$lang.ncc"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:100px;margin-left:7px</xsl:attribute>
					<xsl:attribute name="size">10</xsl:attribute>
					<xsl:attribute name="maxlength">10</xsl:attribute>
					<xsl:attribute name="name">ncc</xsl:attribute>
					<xsl:attribute name="id">ncc</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ncc"/></xsl:attribute>
				</input>
			</div>
			<div class="col">	
				<!--Datos del titular de la cuenta-->
				
					<label>
						<xsl:attribute name="style">width:130;margin-right:3px;</xsl:attribute>
						<xsl:value-of select="$lang.titular"/>
					</label>
				
				<input type="text">
					<xsl:attribute name="style">width:500px;</xsl:attribute>
					<xsl:attribute name="size">255</xsl:attribute>
					<xsl:attribute name="maxlength">255</xsl:attribute>
					<xsl:attribute name="name">titular</xsl:attribute>
					<xsl:attribute name="id">titular</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/titular"/></xsl:attribute>
				</input>
				<br/>	
				
					<label>
						<xsl:attribute name="style">width:130;margin-right:3px;</xsl:attribute>
						<xsl:value-of select="$lang.nif"/>:
					</label>
				
				<input type="text">
					<xsl:attribute name="style">width:100px;</xsl:attribute>
					<xsl:attribute name="size">10</xsl:attribute>
					<xsl:attribute name="maxlength">10</xsl:attribute>
					<xsl:attribute name="name">nifTitular</xsl:attribute>
					<xsl:attribute name="id">nifTitular</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nifTitular"/></xsl:attribute>
				</input>
				<br/>
				
					<label>
						<xsl:attribute name="style">width:130;margin-right:3px;</xsl:attribute>
						<xsl:value-of select="$lang.telefono"/>:
					</label>
				
				<input type="text">
					<xsl:attribute name="style">width:100px;</xsl:attribute>
					<xsl:attribute name="size">14</xsl:attribute>
					<xsl:attribute name="maxlength">14</xsl:attribute>
					<xsl:attribute name="name">telefonoTitular</xsl:attribute>
					<xsl:attribute name="id">telefonoTitular</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/telefonoTitular"/></xsl:attribute>
				</input>
			</div>
		</div>
   		<br/>
		<div class="submenu" style="">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<label class="gr">
			   		<xsl:attribute name="style">width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.anexarInfo1"/><br/>
		   			<xsl:value-of select="$lang.anexarInfo2"/><br/>
			</label>
			<br/>
   			<div class="col">
	   			<label class="gr">
					<xsl:attribute name="style">width:250px;</xsl:attribute>
					<xsl:value-of select="$lang.documento"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">width:400px; </xsl:attribute>
					<xsl:attribute name="name">DOC_01_ZIP</xsl:attribute>
					<xsl:attribute name="id">DOC_01_ZIP</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">DOC_01_ZIP_Tipo</xsl:attribute>
					<xsl:attribute name="id">DOC_01_ZIP_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
			</div>
   		</div>
   		<div class="cuadro" style="color: grey; text-align:justify">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:650px</xsl:attribute>
					<xsl:value-of select="$lang.info_legal"/>
				</label>
			</div>
		</div>

		<input type="hidden"><xsl:attribute name="id">telefono</xsl:attribute></input>
		<input type="hidden"><xsl:attribute name="id">domicilioNotificacion</xsl:attribute></input>
		<input type="hidden"><xsl:attribute name="id">localidad</xsl:attribute></input>
		<input type="hidden"><xsl:attribute name="id">provincia</xsl:attribute></input>
		<input type="hidden"><xsl:attribute name="id">codigoPostal</xsl:attribute></input>
		<input type="hidden"><xsl:attribute name="id">emailSolicitante</xsl:attribute></input>
   		<input type="hidden">
			<xsl:attribute name="name">datosEspecificos</xsl:attribute>
			<xsl:attribute name="id">datosEspecificos</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>

		<input type="hidden">
			<xsl:attribute name="name">nombresolihidden</xsl:attribute>
			<xsl:attribute name="id">nombresolihidden</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombresolihidden"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name">nifsolihidden</xsl:attribute>
			<xsl:attribute name="id">nifsolihidden</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nifsolihidden"/></xsl:attribute>
		</input>		
	</xsl:template>
</xsl:stylesheet>
