<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'SOLICITUD DE AUTORIZACIÓN DE USOS EXCEPCIONALES DE CARRETERAS PROVINCIALES'"/>
	
	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>

	<xsl:variable name="lang.datosRepresentante" select="'Datos de la persona física o del representante de la persona jurídica:'"/>
	<xsl:variable name="lang.datosInteresado" select="'Datos de la entidad'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	<xsl:variable name="lang.datosDeclarativos" select="'Declaraciones Preceptivas que formule el solicitante'"/>
	
	<xsl:variable name="lang.nif_repre" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre_repre" select="'Apellidos y Nombre o Denominación Social'"/>
	<xsl:variable name="lang.domicilio_repre" select="'Domicilio'"/>
	<xsl:variable name="lang.ciudad_repre" select="'Localidad'"/>
	<xsl:variable name="lang.c_postal_repre" select="'Código Postal'"/>
	<xsl:variable name="lang.region_repre" select="'Provincia'"/>
	<xsl:variable name="lang.movil_repre" select="'Teléfono'"/>
	<xsl:variable name="lang.d_email_repre" select="'Correo electrónico'"/>	

	<xsl:variable name="lang.representante" select="'Nombre o Razón Social'"/>
	<xsl:variable name="lang.cif" select="'CIF'"/>
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>

	<xsl:variable name="lang.asunto" select="'Asunto'"/>
	
	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.expone1" select="'Que es titular de la finca catastral'"/>
	<xsl:variable name="lang.expone2" select="'de la carretera provincial'"/>
	<xsl:variable name="lang.expone3" select="'término municipal'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>
	<xsl:variable name="lang.declaro" select="'Asimismo, declaro bajo mi Responsabilidad (obligatorio marcar con una x cada cuadro para poder seguir el trámite)'"/>

	<xsl:variable name="lang.anexar" select="'Ficheros que se Anexan'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Debe anexar:'"/>
	<xsl:variable name="lang.anexarInfo11" select="'- Si se actúa en representación de persona física o jurídica, docuemnto que acredite tal representación.'"/>
	<xsl:variable name="lang.anexarInfo12" select="'- Documento acreditativo de la titularidad o derecho sobre la parcela en que tendrá lugar la actuación.'"/>
	<xsl:variable name="lang.anexarInfo13" select="'- Representación gráfica de la zona y actuación prevista (plano, croquis). Proyecto de obras, en su caso.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.-  Para adjuntar los ficheros, pulse el botón examinar. Seleccione el fichero que desee anexar a la solicitud. Recuerde que debe anexar copia de la Memoria o proyecto al que se refiere la presente solicitud y de cualquier otro que considere conveniente. Si el documento está en soporte papel, debe escanearlo con carácter previo.'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>
	<xsl:variable name="lang.documentoTipo_DOC_ODT" select="'Archivo ODT/DOC'"/>
	<xsl:variable name="lang.documentoTipoPDF" select="'Archivo PDF'"/>
	<xsl:variable name="lang.documentoTipoJPEG" select="'Archivo JPEG'"/>
	
		
	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(10);
			
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');
			
			validar[2] = new Array('localidad','<xsl:value-of select="$lang.localidad"/>');
			validar[3] = new Array('provincia','<xsl:value-of select="$lang.provincia"/>');
			validar[4] = new Array('codigoPostal','<xsl:value-of select="$lang.cp"/>');

			validar[5] = new Array('asunto','<xsl:value-of select="$lang.asunto"/>');
			
			validar[6] = new Array('expone1','<xsl:value-of select="$lang.expone1"/>');
			validar[7] = new Array('expone2','<xsl:value-of select="$lang.expone2"/>');
			validar[8] = new Array('expone3','<xsl:value-of select="$lang.expone3"/>');

			validar[9] = new Array('solicita','<xsl:value-of select="$lang.solicita"/>');
			

			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(25);
			
			especificos[0] = new Array('ayuntamiento','ayuntamiento');
			especificos[1] = new Array('domicilioNotificacion','domicilioNotificacion');
			especificos[2] = new Array('localidad','localidad');
			especificos[3] = new Array('provincia','provincia');
			especificos[4] = new Array('codigoPostal','codigoPostal');
			especificos[5] = new Array('telefono','telefono');
			
			especificos[6] = new Array('asunto','asunto');
						
			especificos[7] = new Array('expone1','expone1');
			especificos[8] = new Array('expone2','expone2');
			especificos[9] = new Array('expone3','expone3');
			especificos[10] = new Array('solicita','solicita');
			
			especificos[11] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[12] = new Array('nombreSolicitante','nombreSolicitante');
			especificos[13] = new Array('emailSolicitante','emailSolicitante');

			especificos[14] = new Array('cif','cif');
			
			especificos[15] = new Array('nif_repre','nif_repre');
			especificos[16] = new Array('nombre_repre','nombre_repre');
			especificos[17] = new Array('domicilio_repre','domicilio_repre');
			especificos[18] = new Array('ciudad_repre','ciudad_repre');
			especificos[19] = new Array('c_postal_repre','c_postal_repre');
			especificos[20] = new Array('region_repre','region_repre');
			especificos[21] = new Array('movil_repre','movil_repre');
			especificos[22] = new Array('d_email_repre','d_email_repre');
			especificos[23] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[24] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');
			
			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				document.getElementById('nombre_repre').value = document.getElementById('nombreSolicitante').value;
				document.getElementById('nif_repre').value = document.getElementById('documentoIdentidad').value;
				//document.getElementById('domicilioNotificacion').value = document.getElementById('domicilio_repre').value;
//				document.getElementById('localidad').value = document.getElementById('ciudad_repre').value;
				return true;
			}
			function getDatosObligadoEntidad(nif){
				window.open('tramites/001/VYO_USOSEXCP/buscaObligadoEntidad.jsp?valor='+nif+';001','','width=3,height=3');
			}
			
			function abrirDocumento(){ 
				v=window.open("documentos/MODELO.odt"); 
			} 
		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
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
					<xsl:value-of select="$lang.nombre_repre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="id">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Nombre"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
				<input type="hidden">
					<xsl:attribute name="style">width:100px; </xsl:attribute>
					<xsl:attribute name="name">nif_repre</xsl:attribute>
					<xsl:attribute name="id">nif_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nif_repre"/></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">nombre_repre</xsl:attribute>
					<xsl:attribute name="id">nombre_repre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombre_repre"/></xsl:attribute>
				</input>
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
					<xsl:value-of select="$lang.ciudad_repre"/>:*
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
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.cif"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:100px; </xsl:attribute>
					<xsl:attribute name="name">cif</xsl:attribute>
					<xsl:attribute name="id">cif</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/cif"/></xsl:attribute>
				</input>
				<img onclick="getDatosObligadoEntidad(document.getElementById('cif').value);" src="img/search-mg.gif"/>
			</div>
			    
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.representante"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">ayuntamiento</xsl:attribute>
					<xsl:attribute name="id">ayuntamiento</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ayuntamiento"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.direccion"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px;</xsl:attribute>
					<xsl:attribute name="name">domicilioNotificacion</xsl:attribute>
					<xsl:attribute name="id">domicilioNotificacion</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/Domicilio_Notificacion"/>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.localidad"/>:	
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
					<xsl:value-of select="$lang.provincia"/>:
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
					<xsl:value-of select="$lang.cp"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:60px;</xsl:attribute>
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
					<xsl:attribute name="style">width:70px; </xsl:attribute>
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
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">asunto</xsl:attribute>
					<xsl:attribute name="id">asunto</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/asunto"/></xsl:attribute>
				</input>
			</div>				
			<br/>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; witdh:300px;</xsl:attribute>
					<xsl:value-of select="$lang.expone"/>:*
				</label>
				<br/><br/>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px;</xsl:attribute>
					<xsl:value-of select="$lang.expone1"/>*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:440px;</xsl:attribute>
					<xsl:attribute name="name">expone1</xsl:attribute>
					<xsl:attribute name="id">expone1</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/expone1"/></xsl:attribute>
				</input>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px;</xsl:attribute>
					<xsl:value-of select="$lang.expone2"/>*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:440px;</xsl:attribute>
					<xsl:attribute name="name">expone2</xsl:attribute>
					<xsl:attribute name="id">expone2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/expone2"/></xsl:attribute>
				</input>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px;</xsl:attribute>
					<xsl:value-of select="$lang.expone3"/>*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:440px;</xsl:attribute>
					<xsl:attribute name="name">expone3</xsl:attribute>
					<xsl:attribute name="id">expone3</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/expone3"/></xsl:attribute>
				</input>
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

			<br/>
			<xsl:call-template name="TEXTO_LEGAL_COMUN" />
			<br/>
				
			<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   			</div>
   			<div class="cuadro" style="">
				<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.anexarInfo1"/>
					<br/>
					<xsl:value-of select="$lang.anexarInfo11"/>
					<br/>
					<xsl:value-of select="$lang.anexarInfo12"/>
					<br/>
					<xsl:value-of select="$lang.anexarInfo13"/>
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
					<xsl:attribute name="name">VYO_USOSEXCP_D1</xsl:attribute>
					<xsl:attribute name="id">VYO_USOSEXCP_D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">VYO_USOSEXCP_D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">VYO_USOSEXCP_D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
				
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo_DOC_ODT"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">VYO_USOSEXCP_D2</xsl:attribute>
					<xsl:attribute name="id">VYO_USOSEXCP_D2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">VYO_USOSEXCP_D2_Tipo</xsl:attribute>
					<xsl:attribute name="id">VYO_USOSEXCP_D2_Tipo</xsl:attribute>
					<xsl:attribute name="value">odt,doc</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoPDF"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">VYO_USOSEXCP_D3</xsl:attribute>
					<xsl:attribute name="id">VYO_USOSEXCP_D3</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">VYO_USOSEXCP_D3_Tipo</xsl:attribute>
					<xsl:attribute name="id">VYO_USOSEXCP_D3_Tipo</xsl:attribute>
					<xsl:attribute name="value">PDF</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoJPEG"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">VYO_USOSEXCP_D4</xsl:attribute>
					<xsl:attribute name="id">VYO_USOSEXCP_D4</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">VYO_USOSEXCP_D4_Tipo</xsl:attribute>
					<xsl:attribute name="id">VYO_USOSEXCP_D4_Tipo</xsl:attribute>
					<xsl:attribute name="value">JPEG</xsl:attribute>
				</input>
			</div>
			<br/>
			<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN" />
	   		<br/>
			<xsl:call-template name="TEXTO_COMPARECE_COMUN" />

   		</div>
   		<br/>
   		<input type="hidden">
			<xsl:attribute name="name">datosEspecificos</xsl:attribute>
			<xsl:attribute name="id">datosEspecificos</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
	</xsl:template>
</xsl:stylesheet>
