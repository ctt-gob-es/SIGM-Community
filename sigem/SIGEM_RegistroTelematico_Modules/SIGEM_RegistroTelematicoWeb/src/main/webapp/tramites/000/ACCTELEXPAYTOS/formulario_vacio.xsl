<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'SOLICITUD DE ACCESO TELEMÁTICO A EXPEDIENTE'"/>
	
	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>
	<xsl:variable name="lang.anio" select="'Año de inicio del expediente:*'"/>

	<xsl:variable name="lang.datosRepresentante" select="'Datos del responsable del proyecto/actividad'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.expediente" select="'Expdientes del solicitante:* (seleccionar)'"/>
	<xsl:variable name="lang.expedienteObligatoria" select="'Expediente'"/>

	<xsl:variable name="lang.nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.cargo" select="'Cargo'"/>

	<xsl:variable name="lang.ayuntamiento" select="'Nombre del Ayuntamiento'"/>	
	<xsl:variable name="lang.nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.representante" select="'Ayuntamiento'"/>
	<xsl:variable name="lang.cif" select="'CIF'"/>
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>

	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>
	
	<xsl:variable name="lang.anexar" select="'DOCUMENTACIÓN QUE SE ACOMPAÑA'"/>
	
	<xsl:variable name="lang.anexarInfo" select="'Para adjuntar los ficheros, pulse el botón examinar. Seleccione el fichero que desee anexar a la solicitud.'"/>

	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>
	<xsl:variable name="lang.documentoTipoXSIG" select="'Archivo XSIG'"/>
	<xsl:variable name="lang.documentoTipo_DOC_ODT" select="'Archivo ODT/DOC'"/>
	<xsl:variable name="lang.documentoTipoPDF" select="'Archivo PDF'"/>
	<xsl:variable name="lang.documentoTipoJPEG" select="'Archivo JPEG'"/>

	<xsl:variable name="lang.infoLegal" select="'Los datos personales, identificativos y de contacto, aportados mediante esta comunicación se entienden facilitados voluntariamente, y serán incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del ámbito de las competencias de esta Administración Pública así como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telemáticos y enviarle invitaciones para eventos y felicitaciones en fechas señaladas. Entenderemos que presta su consentimiento tácito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podrá ejercer sus derechos de acceso, rectificación, cancelación y oposición ante el Responsable del Fichero, la Diputación Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - España, siempre acreditando conforme a Derecho su identidad en la comunicación. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigiéndose a la dirección citada ut supra o bien al correo electrónico lopd@dipucr.es o bien por telefono al numero gratuito 900 714 080.'"/>
		
	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(4);
			
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');
			validar[2] = new Array('cargo','<xsl:value-of select="$lang.cargo"/>');
			validar[3] = new Array('anio','Año en el que se inició el expediente');
			validar[4] = new Array('expediente','<xsl:value-of select="$lang.expedienteObligatoria"/>');


			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(18);
			
			especificos[0] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[1] = new Array('nombreSolicitante','nombreSolicitante');
			especificos[2] = new Array('emailSolicitante','emailSolicitante');
			especificos[3] = new Array('anio','anio');
			especificos[4] = new Array('asunto','asunto');
			especificos[5] = new Array('numExpediente','numExpediente');
			especificos[6] = new Array('expone','expone');
			especificos[7] = new Array('solicita','solicita');	
			especificos[8] = new Array('ayuntamiento','ayuntamiento');
			especificos[9] = new Array('domicilioNotificacion','domicilioNotificacion');
			especificos[10] = new Array('localidad','localidad');
			especificos[11] = new Array('provincia','provincia');
			especificos[12] = new Array('codigoPostal','codigoPostal');
			especificos[13] = new Array('telefono','telefono');
			especificos[14] = new Array('cif','cif');
			especificos[15] = new Array('ayuntamiento','ayuntamiento');
			especificos[16] = new Array('recurso','recurso');
			especificos[17] = new Array('cargo','cargo');

			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				document.getElementById('nombreSolicitante').value = document.getElementById('ayuntamiento').options[document.getElementById('ayuntamiento').selectedIndex].text;
				document.getElementById('documentoIdentidad').value = document.getElementById('cif').value;

				document.getElementById('recurso').value= 'Aytos.Adm.Publ.';

				if(document.getElementById('anio').value=='----'){
					alert('Debe seleccionar un año para el expediente');
					document.getElementById('anio').focus();
					return false;
				}

				if(document.getElementById('expediente').value=='-------'){
					alert('Debe seleccionar un expediente');
					document.getElementById('expediente').focus();
					return false;
				}

				return true;
			}
			function getDatos(anio){
				window.open('tramites/000/ACCTELEXPAYTOS/dameExpedientes.jsp?valor='+document.getElementById('cif').value+';'+anio.value+';000','','width=3,height=3,left=10000,top=10000');
			}
			function getDatosAyto(valueAyto){
				document.getElementById('anio').value = '----';
				document.getElementById('expediente').value = '-------';
				window.open('tramites/000/ayuntamiento.jsp?valor='+valueAyto+';000','','width=3,height=3,left=10000,top=10000');
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

   		<div class="submenu" style="position: relative; ">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
   		<div class="cuadro" style="position: relative; ">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.anio"/>:
				</label>
				<select>
					<xsl:attribute name="style">color:#006699;</xsl:attribute>
					<xsl:attribute name="name">anio</xsl:attribute>
					<xsl:attribute name="id">anio</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/anio"/></xsl:attribute>
					<xsl:attribute name="onchange">getDatos(anio)</xsl:attribute>
					<options>
						<option value='----'>----</option>
					</options>
				</select>
				<script>
					var anio = 2009;
					var f = new Date();
					var anioAc = f.getFullYear();
					for (var i=1; (anio+i)&lt;=anioAc; i++){
						document.getElementById('anio').options[i] = new Option (anio+i,anio+i);
					}
				</script>
				<br/>	
			</div>	
			<br/>			
	   		<b><xsl:value-of select="$lang.expediente"/></b>
			<tb/>
			<select class="gr">
				<xsl:attribute name="style">border:none; width:650px;color:#006699;</xsl:attribute>
				<xsl:attribute name="name">expediente</xsl:attribute>
				<xsl:attribute name="id">expediente</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/expediente"/></xsl:attribute>
				<xsl:attribute name="onblur"> document.getElementById('asunto').value = this.options[this.selectedIndex].text; document.getElementById('numExpediente').value = this.value;</xsl:attribute>
				<option><xsl:attribute name="value">-------</xsl:attribute>----------------------------------------------------</option>
			</select>
			<div class="col" style="display:none;">
				<input type="text">
					<xsl:attribute name="style">position: relative; width:200px;</xsl:attribute>
					<xsl:attribute name="name">numExpediente</xsl:attribute>
					<xsl:attribute name="id">numExpediente</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/numExpediente"/></xsl:attribute>
				</input>
			</div>
   			<div class="col" style="display:none;">
				<input type="text">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">asunto</xsl:attribute>
					<xsl:attribute name="id">asunto</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/asunto"/></xsl:attribute>
				</input>
			</div>
		</div>

   		<div class="cuadro" style="position: relative; ">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.expone"/>:
				</label>
				<textarea type="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">expone</xsl:attribute>
					<xsl:attribute name="id">expone</xsl:attribute>
					<xsl:attribute name="rows">7</xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/expone"/>
				</textarea>
			</div>
			<br/>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.solicita"/>:	
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">solicita</xsl:attribute>
					<xsl:attribute name="id">solicita</xsl:attribute>
					<xsl:attribute name="rows">7</xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/solicita"/>
				</textarea>
			</div>

			<script>
				document.getElementById('expone').value = 'Que el ayuntamiento interesado tiene interés en conocer el estado de tramitación del expediente que señala y los documentos que lo integran.';
				document.getElementById('solicita').value = 'Que se me facilite la documentación solicitada.';
			</script>
			
			</div>		
   			<br/>
			<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   			</div>
   			<div class="cuadro" style="text-align:justify">
				<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.anexarInfo"/><br/>
				</label>
			<br/><br/>
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_CONVSUB_D1</xsl:attribute>
					<xsl:attribute name="id">CR_CONVSUB_D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_CONVSUB_D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_CONVSUB_D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
				
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo_DOC_ODT"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_CONVSUB_D2</xsl:attribute>
					<xsl:attribute name="id">CR_CONVSUB_D2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_CONVSUB_D2_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_CONVSUB_D2_Tipo</xsl:attribute>
					<xsl:attribute name="value">odt,doc</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoPDF"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_CONVSUB_D3</xsl:attribute>
					<xsl:attribute name="id">CR_CONVSUB_D3</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_CONVSUB_D3_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_CONVSUB_D3_Tipo</xsl:attribute>
					<xsl:attribute name="value">PDF</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoJPEG"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_CONVSUB_D4</xsl:attribute>
					<xsl:attribute name="id">CR_CONVSUB_D4</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_CONVSUB_D4_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_CONVSUB_D4_Tipo</xsl:attribute>
					<xsl:attribute name="value">JPG</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoXSIG"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_CONVSUB_D5</xsl:attribute>
					<xsl:attribute name="id">CR_CONVSUB_D5</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_CONVSUB_D5_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_CONVSUB_D5_Tipo</xsl:attribute>
					<xsl:attribute name="value">XSIG</xsl:attribute>
				</input>
			</div>
			<br/>
			<div style="color: grey; text-align:justify">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
					<xsl:value-of select="$lang.infoLegal"/><br/>
				</label>
			</div>
   		</div>
   		<br/>
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
	</xsl:template>
</xsl:stylesheet>
