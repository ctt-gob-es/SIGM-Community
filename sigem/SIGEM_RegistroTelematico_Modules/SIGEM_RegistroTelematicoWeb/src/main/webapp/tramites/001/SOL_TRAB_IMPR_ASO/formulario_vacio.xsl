<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'SOLICITUD DE REGISTRO TELEMÁTICO'"/>

	<xsl:variable name="lang.tituloSolicitud" select="'SOLICITUD DE TRABAJOS A LA IMPRENTA PROVINCIAL'"/>

	<xsl:variable name="lang.datosPresentador" select="'Datos de la persona que presenta la solicitud'"/>
	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>
	<xsl:variable name="lang.cargo" select="'Cargo'"/>

	<xsl:variable name="lang.datosRepresentante" select="'Datos de la persona de contacto'"/>
	<xsl:variable name="lang.nif_repre" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre_repre" select="'Apellidos y Nombre'"/>
	<xsl:variable name="lang.domicilio_repre" select="'Domicilio'"/>
	<xsl:variable name="lang.ciudad_repre" select="'Localidad'"/>
	<xsl:variable name="lang.c_postal_repre" select="'Código Postal'"/>
	<xsl:variable name="lang.region_repre" select="'Región / País'"/>
	<xsl:variable name="lang.movil_repre" select="'Teléfono'"/>
	<xsl:variable name="lang.d_email_repre" select="'Correo electrónico'"/>	

	<xsl:variable name="lang.datosInteresado" select="'Datos de la Asociación solicitante'"/>
	<xsl:variable name="lang.representante" select="'Asociación'"/>
	<xsl:variable name="lang.cif" select="'CIF'"/>
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>

	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	<xsl:variable name="lang.clase" select="'Clase y denominación del trabajo'"/>
	<xsl:variable name="lang.tipo_trabajo" select="'Tipo de trabajo solicitado'"/>
	<xsl:variable name="lang.nota" select="'Una solicitud por cada tipo de trabajo.'"/>
	<xsl:variable name="lang.cantidad" select="'Cantidad'"/>
	<xsl:variable name="lang.formato" select="'Formato'"/>
	<xsl:variable name="lang.deposito_legal" select="'Depósito legal'"/>
	<xsl:variable name="lang.jccm" select="'(asignado por la JCCM, cuando proceda)'"/>
	<xsl:variable name="lang.texto_enlace" select="'Si no dispone de él, pinche aquí'"/>

	
	<xsl:variable name="lang.anexar" select="'DOCUMENTACIÓN QUE SE ACOMPAÑA'"/>
	
	<xsl:variable name="lang.anexarInfo" select="'Para adjuntar los ficheros, pulse el botón examinar. Seleccione el fichero que desee anexar a la solicitud.'"/>

	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>


	<xsl:variable name="lang.infoLegal" select="'Los datos personales, identificativos y de contacto, aportados mediante esta comunicación se entienden facilitados voluntariamente, y serán incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del ámbito de las competencias de esta Administración Pública así como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telemáticos y enviarle invitaciones para eventos y felicitaciones en fechas señaladas. Entenderemos que presta su consentimiento tácito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podrá ejercer sus derechos de acceso, rectificación, cancelación y oposición ante el Responsable del Fichero, la Diputación Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - España, siempre acreditando conforme a Derecho su identidad en la comunicación. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigiéndose a la dirección citada ut supra o bien al correo electrónico lopd@dipucr.es o bien por telefono al numero gratuito 900 714 080.'"/>
		
	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(5);
			
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');
			validar[2] = new Array('cargo','<xsl:value-of select="$lang.cargo"/>');
			validar[3] = new Array('d_email_repre','<xsl:value-of select="$lang.d_email_repre"/>');
			validar[4] = new Array('emailSolicitante','<xsl:value-of select="$lang.email"/>');

			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(29);
			
			especificos[0] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[1] = new Array('nombreSolicitante','nombreSolicitante');
			especificos[2] = new Array('emailSolicitante','emailSolicitante');
			
			especificos[3] = new Array('asociacion','asociacion');
			especificos[4] = new Array('domicilioNotificacion','domicilioNotificacion');
			especificos[5] = new Array('localidad','localidad');
			especificos[6] = new Array('provincia','provincia');
			especificos[7] = new Array('codigoPostal','codigoPostal');
			especificos[8] = new Array('telefono','telefono');
			especificos[9] = new Array('cif','cif');

			especificos[10] = new Array('nif_repre','nif_repre');
			especificos[11] = new Array('nombre_repre','nombre_repre');
			especificos[12] = new Array('domicilio_repre','domicilio_repre');
			especificos[13] = new Array('ciudad_repre','ciudad_repre');
			especificos[14] = new Array('c_postal_repre','c_postal_repre');
			especificos[15] = new Array('region_repre','region_repre');
			especificos[16] = new Array('movil_repre','movil_repre');
			especificos[17] = new Array('d_email_repre','d_email_repre');

			especificos[18] = new Array('cargo','cargo');

			especificos[19] = new Array('tipo_trabajo','tipo_trabajo');
			especificos[20] = new Array('cantidad','cantidad');
			especificos[21] = new Array('formato','formato');
			especificos[22] = new Array('jccm','jccm');

			especificos[23] = new Array('nifPeticionario','nifPeticionario');
			especificos[24] = new Array('nombrePeticionario','nombrePeticionario');
			especificos[25] = new Array('nombreAytoAsocia','nombreAytoAsocia');
			especificos[26] = new Array('recurso','recurso');

			especificos[27] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[28] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');

			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {

				document.getElementById('nifPeticionario').value = document.getElementById('documentoIdentidad').value;
				document.getElementById('nombrePeticionario').value = document.getElementById('nombreSolicitante').value;
				document.getElementById('nombreAytoAsocia').value = document.getElementById('asociacion').value;

				document.getElementById('nombreSolicitante').value = document.getElementById('asociacion').value;
				document.getElementById('documentoIdentidad').value = document.getElementById('cif').value;

				document.getElementById('recurso').value = 'Pers.Fis.-Empr.';
			
				return true;			
			}
	
		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
   		
   		<div class="cuadro" style="">
			<div class="col">
				<label class="gr" style="position: relative; width:100%; text-align:center; background-color:ORANGE; vertical-align:middle;">
					<font style="color:WHITE; font-weight: bold; font-size:14px;"><xsl:value-of select="$lang.tituloSolicitud"/></font>
				</label>
			</div>
		</div>
		<div class="submenu">
			<h1><xsl:value-of select="$lang.datosPresentador"/></h1>
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
					<xsl:value-of select="$lang.cargo"/>:*
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
					<xsl:value-of select="$lang.d_email_repre"/>:*
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
					<xsl:value-of select="$lang.representante"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">asociacion</xsl:attribute>
					<xsl:attribute name="id">asociacion</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/asociacion"/></xsl:attribute>
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/cif"/></xsl:attribute>
				</input>
			</div>
			<div class="col" style="">
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
					<xsl:value-of select="$lang.email"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">emailSolicitante</xsl:attribute>
					<xsl:attribute name="id">emailSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Correo_Electronico"/></xsl:attribute>
				</input>
			</div>
		</div>

   		<div class="submenu" style="position: relative; ">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
   		<div class="cuadro" style="position: relative; ">
			<div class="col">	
				<label class="gr" align="center">
					<xsl:attribute name="style">position: relative; width:100%; font-size:14px; color:BROWN; font-weight:bold; </xsl:attribute>
					<xsl:value-of select="$lang.clase"/>
				</label>
				<br/><br/>
				<div class="col">
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:100%; font-size:11px;</xsl:attribute>
						<xsl:value-of select="$lang.tipo_trabajo"/>:
					</label>
<br/>
					<label class="gr">
						<xsl:attribute name="style">position: relative; width:100%; font-size:10px; font-weight:bold; font-style:italic;</xsl:attribute>
						<xsl:value-of select="$lang.nota"/>
					</label>
<br/><br/>
					<input type="text">
						<xsl:attribute name="style">position: relative; width:600px; </xsl:attribute>
						<xsl:attribute name="name">tipo_trabajo</xsl:attribute>
						<xsl:attribute name="id">tipo_trabajo</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/tipo_trabajo"/></xsl:attribute>	
					</input>
				</div>
				<table>
					<tr>					
						<td style="vertical-align:top;">
							<label class="gr">
								<xsl:attribute name="style">position: relative; width:100%; font-size:11px; </xsl:attribute>
								<xsl:value-of select="$lang.cantidad"/>:
							</label>
						</td>
						<td style="vertical-align:top;">
							<label class="gr">
								<xsl:attribute name="style">position: relative; width:100%; font-size:11px;</xsl:attribute>
								<xsl:value-of select="$lang.formato"/>:
							</label>
						</td>
						<td style="vertical-align:top;">
							<label class="gr">
								<xsl:attribute name="style">position: relative; width:100%; font-size:11px;</xsl:attribute>
								<xsl:value-of select="$lang.deposito_legal"/>:
							</label>
							<label class="gr">
								<xsl:attribute name="style">position: relative; width:100%; font-size:10px;</xsl:attribute>
								<xsl:value-of select="$lang.jccm"/>
							</label>
							<a>
								<xsl:attribute name="href">http://depositolegal.castillalamancha.es</xsl:attribute>
								<xsl:attribute name="name">vinculo</xsl:attribute>
								<label >
									<xsl:attribute name="style">position: relative; width:100%; font-size:10px; font-weight:bold; text-decoration:underline; cursor:hand</xsl:attribute>
									<xsl:value-of select="$lang.texto_enlace"/>
								</label>
							</a>
						</td>
					</tr>
					<tr>
						<td>
							<input type="text">
								<xsl:attribute name="style">position: relative; size:5; font-size:11px;</xsl:attribute>
								<xsl:attribute name="name">cantidad</xsl:attribute>
								<xsl:attribute name="id">cantidad</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/cantidad"/></xsl:attribute>
							</input>
						</td>
						<td>
							<input type="text">
								<xsl:attribute name="style">position: relative; size:10; font-size:11px;</xsl:attribute>
								<xsl:attribute name="name">formato</xsl:attribute>
								<xsl:attribute name="id">formato</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/formato"/></xsl:attribute>
							</input>
						</td>
						<td>
							<input type="text">
								<xsl:attribute name="style">position: relative; size:10; font-size:11px;</xsl:attribute>
								<xsl:attribute name="name">jccm</xsl:attribute>
								<xsl:attribute name="id">jccm</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/jccm"/></xsl:attribute>
							</input>
						</td>
					</tr>
				</table>
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
		<input type="hidden">
			<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
			<xsl:attribute name="name">emailSolicitante</xsl:attribute>
			<xsl:attribute name="id">emailSolicitante</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Correo_Electronico"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
			<xsl:attribute name="name">recurso</xsl:attribute>
			<xsl:attribute name="id">recurso</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/recurso"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
			<xsl:attribute name="name">nifPeticionario</xsl:attribute>
			<xsl:attribute name="id">nifPeticionario</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nifPeticionario"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
			<xsl:attribute name="name">nombrePeticionario</xsl:attribute>
			<xsl:attribute name="id">nombrePeticionario</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombrePeticionario"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
			<xsl:attribute name="name">nombreAytoAsocia</xsl:attribute>
			<xsl:attribute name="id">nombreAytoAsocia</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombreAytoAsocia"/></xsl:attribute>
		</input>
	</xsl:template>
</xsl:stylesheet>