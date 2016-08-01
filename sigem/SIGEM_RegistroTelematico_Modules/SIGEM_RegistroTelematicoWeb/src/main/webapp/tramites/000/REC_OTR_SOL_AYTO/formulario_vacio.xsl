<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />
<xsl:include href="../REC_COMUNES/template_IBAN.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'Formulario de Comunicaciones al Servicio de Recaudación - Ayuntamientos'"/>

	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Comunicación'"/>

	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>

	<xsl:variable name="lang.datosInteresado" select="'Datos de la Entidad Local solicitante'"/>
	<xsl:variable name="lang.representante" select="'Ayuntamiento'"/>
	<xsl:variable name="lang.cif" select="'CIF'"/>
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>

	<xsl:variable name="lang.datosRepresentante" select="'Representante'"/>
	<xsl:variable name="lang.nif" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre" select="'Apellidos y Nombre o Denominación Social'"/>
	<xsl:variable name="lang.calle" select="'Domicilio'"/>
	<xsl:variable name="lang.c_postal" select="'Código Postal'"/>
	<xsl:variable name="lang.ciudad" select="'Ciudad'"/>
	<xsl:variable name="lang.region" select="'Provincia'"/>
	<xsl:variable name="lang.movil" select="'Número de teléfono móvil'"/>

	<xsl:variable name="lang.representacion" select="'Rellene los siguientes datos si actúa como representante'"/>			
	<xsl:variable name="lang.nif_repr" select="'NIF/CIF de la persona o entidad a quien representa'"/>			
	<xsl:variable name="lang.nombre_repr" select="'Nombre del representado'"/>			

	<xsl:variable name="lang.solicita" select="'Solicita'"/>
	<xsl:variable name="lang.expone" select="'Expone'"/>

	<xsl:variable name="lang.organoAsignado" select="'Órgano al que se dirige: Servicio de Gestión Tributaria, Inspección y Recaudación.'"/>

	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero (*.zip), pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.documento" select="'Conjunto de anexos de la solicitud'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>

		
	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(8);

			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');

			validar[2] = new Array('cif', '<xsl:value-of select="$lang.cif"/>');
			validar[3] = new Array('ayuntamiento','<xsl:value-of select="$lang.nombre"/>');
			validar[4] = new Array('domicilioNotificacion','<xsl:value-of select="$lang.calle"/>');
			validar[5] = new Array('codigoPostal','<xsl:value-of select="$lang.c_postal"/>');

			validar[6] = new Array('expone','Expone');
			validar[7] = new Array('solicita','Solicita');
			

			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(21);
			
			especificos[0] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[1] = new Array('nombreSolicitante','nombreSolicitante');
			especificos[2] = new Array('emailSolicitante','emailSolicitante');
			
			especificos[3] = new Array('ayuntamiento','ayuntamiento');
			especificos[4] = new Array('domicilioNotificacion','domicilioNotificacion');
			especificos[5] = new Array('localidad','localidad');
			especificos[6] = new Array('provincia','provincia');
			especificos[7] = new Array('codigoPostal','codigoPostal');
			especificos[8] = new Array('Telefono','Telefono');
			especificos[9] = new Array('cif','cif');
			especificos[10] = new Array('emailSolicitante','emailSolicitante');

			especificos[11] = new Array('repres_nif','repres_nif');
			especificos[12] = new Array('repres_nombre','repres_nombre');
			especificos[13] = new Array('repres_movil','repres_movil');
			especificos[14] = new Array('repres_d_email','repres_d_email');
			especificos[15] = new Array('expone','expone');
			especificos[16] = new Array('solicita','solicita');
			
			especificos[17] = new Array('rciudad','rciudad');
			especificos[18] = new Array('rregion','rregion');
			especificos[19] = new Array('rc_postal','rc_postal');
			especificos[20] = new Array('rcalle','rcalle');



			<xsl:call-template name="VALIDACIONES" />

			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
			
				var f = document.forms[0];

				if(!validaNIFRepresentante()) return false;
				document.getElementById('nombresolihidden').value = document.getElementById('nombreSolicitante').value;
				document.getElementById('nifsolihidden').value = document.getElementById('documentoIdentidad').value;
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

			function getDatosAyto(valueAyto){
				window.open('tramites/000/ayuntamiento.jsp?valor='+valueAyto+';000','','width=3,height=3,left=10000,top=10000');
			}

		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
		<div class="submenu">
			<xsl:variable name="lang.datosPresentador" select="'Datos del presentador'"/>
   			<h1><xsl:value-of select="$lang.datosPresentador"/></h1>
   		</div>

		<xsl:call-template name="DATOS_SOLICITANTE" />
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosInteresado"/></h1>
   		</div>
		<div class="cuadro" style="">
			<xsl:variable name="fileAyuntam" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosTablaValidacionConSustituto('REC_VLDTBL_MUNICIPIOS','000')"/>
			<xsl:variable name="b" select="document($fileAyuntam)"/>					
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.representante"/>:*
				</label>
				<!-- Carga de los ayuntamientos de la provincia de Ciudad Real-->
				<select onchange="getDatosAyto(this.value)">
					<xsl:attribute name="style">width:400px;color:#006699;</xsl:attribute>
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
					<xsl:attribute name="name">Telefono</xsl:attribute>
					<xsl:attribute name="id">Telefono</xsl:attribute>
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/emailSolicitante"/></xsl:attribute>
				</input>
			</div>
		</div>
		<div class="submenu">
			<div class="submenu">
   				<h1><xsl:value-of select="$lang.datosRepresentante"/></h1>
   			</div>
   		</div>
   		<div class="cuadro" style="">
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
					<xsl:value-of select="$lang.email"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">repres_d_email</xsl:attribute>
					<xsl:attribute name="id">repres_d_email</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/repres_d_email"/></xsl:attribute>
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
					<xsl:value-of select="$lang.expone"/>:*
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">expone</xsl:attribute>
					<xsl:attribute name="id">expone</xsl:attribute>
					<xsl:attribute name="rows">5</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/expone"/>
				</textarea>
			</div>
		   	<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.solicita"/>:*
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">solicita</xsl:attribute>
					<xsl:attribute name="id">solicita</xsl:attribute>
					<xsl:attribute name="rows">5</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/solicita"/>
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
					<xsl:attribute name="name">REC_OTR_SOL_D1</xsl:attribute>
					<xsl:attribute name="id">REC_OTR_SOL_D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">REC_OTR_SOL_D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">REC_OTR_SOL_D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
			</div>
   		</div>
   		<br/>
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
