<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'Solicitud de Documentos del Padrón de Habitantes'"/>
	<xsl:variable name="lang.datosSolicitante" select="'Datos del Solicitante'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>
	<xsl:variable name="lang.solicitaTexto" select="'Que me sea remitido a la dirección de correo electrónico que más abajo indico el documento anteriormente señalado.'"/>
	<xsl:variable name="lang.compruebaMail" select="'Compruebe que la dirección de correo electrónico es la correcta.'"/>
	<xsl:variable name="lang.docIdentidad" select="'NIF / NIE'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.tipoCert" select="'Documento que se solicita'"/>
	<xsl:variable name="lang.tipoFamilia" select="'Individual / Familiar'"/>
	<xsl:variable name="lang.observacionesDoc" select="'Observaciones (Documento)'"/>
	<xsl:variable name="lang.efectoDoc" select="'Efecto (Documento)'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono móvil'"/>
	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.anexarInfo3" select="'(Máximo 7 mbs total)'"/>
	<xsl:variable name="lang.documento1" select="'Documento ZIP'"/>
	<xsl:variable name="lang.envio" select="'Envío de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'Solicito el envío de notificaciones por medios telemáticos'"/>
	<xsl:variable name="lang.deu" select="'D.E.U.'"/>

	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			var validar = new Array(4);
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.docIdentidad"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.nombre"/>');
			validar[2] = new Array('tipoCert','<xsl:value-of select="$lang.tipoCert"/>');
			validar[3] = new Array('tipoFamilia','<xsl:value-of select="$lang.tipoFamilia"/>');
			validar[4] = new Array('emailSolicitante','<xsl:value-of select="$lang.email"/>');

			var validar_NO = new Array(1);
			validar_NO[0] = new Array('BOP1D1','<xsl:value-of select="$lang.documento1"/>');

			//Array con los datos especificos del formulario -> -> ('id_campo','tag_xml')
			var especificos = new Array(8);
			especificos[0] = new Array('tipoCert','Tipo_Cert');
			especificos[1] = new Array('tipoFamilia','Tipo_Familia');
			especificos[2] = new Array('observacionesDoc','Observaciones_Doc');
			especificos[3] = new Array('efectoDoc','Efecto_Doc');
			especificos[4] = new Array('emailSolicitante','Email_Solicitante');
			especificos[5] = new Array('observaciones','Observaciones');
			especificos[6] = new Array('documentoIdentidad','NIF');
			especificos[7] = new Array('nombreSolicitante','Nombre');
			especificos[8] = new Array('codInstitucion','Cod_Institucion');

			especificos[9] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[10] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');

			var validarNumero = new Array(0);

			function verificacionesEspecificas() {
				//Sólo si estamos en administración, debemos comprobar el NIF/NIE
				var admin = '<xsl:value-of select="Datos_Registro/datos_especificos/Admin"/>';
				if (admin == 'true'){
					var campoNombre = document.getElementById('nombreSolicitante');
					if (campoNombre.value == ''){
						alert('Debe introducir un NIF/NIE válido');
						return false;
					}
				}
			}

			//Recupera el nombre de la persona del servicio web del Padrón
			//Sólo se usa si estamos en la Administración
			function getDatosPersona(nif){
				//if (validarCampoNIF()){
					window.open('tramites/000/CERT_PADRON/buscaPersonaPadron.jsp?valor='+nif+';','','width=3,height=3');
				//}
			}

			//Valida si el campo para el NIF / NIE es correcto
			function validarCampoNIF() {
				
				var campoNif = document.getElementById('documentoIdentidad');
				var validacion = valida_nif_nie(campoNif);
				
				if (validacion == 0){
					alert('El NIF/NIE no cumple ninguno de los formatos permitidos.');
					return false;
				}
				else if (validacion == -1){
					alert('El NIF introducido es incorrecto. Compruebe la letra.');
					return false;
				}
				else if (validacion == -2){
					alert('El NIE introducido es incorrecto. Compruebe la letra');
					return false;
				}
				else{
					return true;
				}
			}

			//Retorna: 1 = NIF ok, 2 = NIE ok, -1 = NIF error, -2 = NIE error, 0 = ??? error
			function valida_nif_nie(campoNif){
				var a = escape(campoNif.value.toUpperCase());
				var temp=a.toUpperCase();
				campoNif.value = temp;
				var cadenadni="TRWAGMYFPDXBNJZSQVHLCKE";

				if (temp!=''){
					//si no tiene un formato valido devuelve error
					if(!/^[XYZ]{1}[0-9]{7}[A-Z]{1}$/.test(temp))
					if(!/^[KLM]{1}[0-9]{7}[A-Z]{1}$/.test(temp))
					if(!/^[0-9]{8}[A-Z]{1}$/.test(temp)){
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

					//Comprobación de NIF especiales (se calculan como CIFs)
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

					//Comprobación de NIEs
					//T
					if (/^[T]{1}/.test(temp)){
						if (a.charAt(8) == /^[T]{1}[A-Z0-9]{8}$/.test(temp)){
							return 2;
						}
						else{
							return -2;
						}
					}
					//XYZ
					if (/^[XYZ]{1}/.test(temp)){
 						temp = temp.replace('X','0')
						temp = temp.replace('Y','1')
 						temp = temp.replace('Z','2')
 						pos = temp.substring(0, 8)% 23;
						if (a.charAt(8) == cadenadni.substring(pos, pos + 1)){
 							return 2;
 						}
 						else{
 							return -2;
						}
					}
				}
				return 0;
			}


		</script>

		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitante"/></h1>
   		</div>
   		<div class="cuadro" style="">

			<xsl:if test="Datos_Registro/datos_especificos/Admin">
				<div class="col">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.docIdentidad"/>:*
					</label>
					<input type="text">
						<xsl:attribute name="style">position: relative; width:350px;</xsl:attribute>
						<xsl:attribute name="name">documentoIdentidad</xsl:attribute>
						<xsl:attribute name="id">documentoIdentidad</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/NIF"/></xsl:attribute>
					</input>
					<img onclick="getDatosPersona(document.getElementById('documentoIdentidad').value);" src="img/search-mg.gif"/>
				</div>
				<div class="col" style="height: 35px;">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.nombre"/>:*
					</label>
					<input type="text">
						<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
						<xsl:attribute name="name">nombreSolicitante</xsl:attribute>
						<xsl:attribute name="id">nombreSolicitante</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Nombre"/></xsl:attribute>
						<xsl:attribute name="disabled"></xsl:attribute>
					</input>
				</div>
			</xsl:if>
			<xsl:if test="not(Datos_Registro/datos_especificos/Admin)">
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
				<div class="col" style="height: 35px;">
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
			</xsl:if>
		</div>

		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
		<div class="cuadro" style="">

			<!-- Cargamos los tipos de certificados mediante servicio web -->
			<!-- CAMBIAR ID_ENTIDAD DEPENDIENDO DEL AYUNTAMIENTO -->
			<xsl:variable name="fileTiposCert" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosVldtblConSustitutoByOrden('VLDTBL_CERT_PADRON','000')"/>
			<xsl:variable name="b" select="document($fileTiposCert)"/>

			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.tipoCert"/>:*	
				</label>
				<select>
					<xsl:attribute name="style">position:relative;width:450px;color:#006699;font-size:13px;</xsl:attribute>
					<xsl:attribute name="name">tipoCert</xsl:attribute>
					<xsl:attribute name="id">tipoCert</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Tipo_Cert"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
			</div>

			<!-- Cargamos los tipos de unidades familiares mediante servicio web -->
			<!-- CAMBIAR ID_ENTIDAD DEPENDIENDO DEL AYUNTAMIENTO -->
			<xsl:variable name="fileTiposFamiliar" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosVldtblConSustitutoByOrden('VLDTBL_CERT_PADRON_FAMILIAR','000')"/>
			<xsl:variable name="b" select="document($fileTiposFamiliar)"/>

			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.tipoFamilia"/>:*	
				</label>
				<select>
					<xsl:attribute name="style">position:relative;width:350px;color:#006699;font-size:13px;</xsl:attribute>
					<xsl:attribute name="name">tipoFamilia</xsl:attribute>
					<xsl:attribute name="id">tipoFamilia</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Tipo_Familia"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="style">position: relative; width:350px;</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>
			</div>

			<xsl:if test="Datos_Registro/datos_especificos/Admin">
				<div class="col">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.observacionesDoc"/>:
					</label>
					<textarea class="gr">
						<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
						<xsl:attribute name="name">observacionesDoc</xsl:attribute>
						<xsl:attribute name="id">observacionesDoc</xsl:attribute>
						<xsl:attribute name="rows">3</xsl:attribute>
						<xsl:value-of select="Datos_Registro/datos_especificos/Observaciones_Doc"/>
					</textarea>
				</div>
				<div class="col" style="height: 35px;">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.efectoDoc"/>:
					</label>
					<input type="text">
						<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
						<xsl:attribute name="name">efectoDoc</xsl:attribute>
						<xsl:attribute name="id">efectoDoc</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Efecto_Doc"/></xsl:attribute>
					</input>
				</div>
			</xsl:if>
			</div>

			<div class="submenu">
				<h1><xsl:value-of select="$lang.solicita"/></h1>
			</div>

			<div class="cuadro" style="">
			<xsl:value-of select="$lang.solicitaTexto"/><br/><br/>
			<div class="col" style="border:1">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.email"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">emailSolicitante</xsl:attribute>
					<xsl:attribute name="id">emailSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Correo_Electronico"/></xsl:attribute>
				</input>
				<div class="col">
					<label class="gr"></label>
					<label style="color:red; margin-left:40px"><xsl:value-of select="$lang.compruebaMail"/></label>
				</div>
			</div>
			<div class="col" style="height: 66px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.observaciones"/>:
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">observaciones</xsl:attribute>
					<xsl:attribute name="id">observaciones</xsl:attribute>
					<xsl:attribute name="rows">3</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/Observaciones"/>
				</textarea>
			</div>
		</div>
		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN" />
   		<br/>
		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN" />
   		<br/>
		<xsl:call-template name="TEXTO_COMPARECE_COMUN" />

			<input type="hidden">
				<xsl:attribute name="name">datosEspecificos</xsl:attribute>
				<xsl:attribute name="id">datosEspecificos</xsl:attribute>
				<xsl:attribute name="value"></xsl:attribute>
			</input>

			<!-- CAMBIAR DEPENDIENDO DEL AYUNTAMIENTO -->
			<input type="hidden">
				<xsl:attribute name="name">codInstitucion</xsl:attribute>
				<xsl:attribute name="id">codInstitucion</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Cod_Institucion"/></xsl:attribute>
				<xsl:attribute name="value"></xsl:attribute>
			</input>

		<xsl:if test="not(Datos_Registro/datos_especificos/Admin)">
			<input type="hidden">
				<xsl:attribute name="name">observacionesDoc</xsl:attribute>
				<xsl:attribute name="id">observacionesDoc</xsl:attribute>
				<xsl:attribute name="value"></xsl:attribute>
			</input>
			<input type="hidden">
				<xsl:attribute name="name">efectoDoc</xsl:attribute>
				<xsl:attribute name="id">efectoDoc</xsl:attribute>
				<xsl:attribute name="value"></xsl:attribute>
			</input>
		</xsl:if>

   		<br/>

   			<input class="gr" type="hidden" id="solicitarEnvio" onclick="activarDEU();" style="border:0px; width:20px;" />
   			<input class="gr" type="hidden" id="direccionElectronicaUnica" />

	</xsl:template>
</xsl:stylesheet>