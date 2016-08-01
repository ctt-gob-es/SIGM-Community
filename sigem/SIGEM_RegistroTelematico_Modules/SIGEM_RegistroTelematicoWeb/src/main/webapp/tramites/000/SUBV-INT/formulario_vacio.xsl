<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'Formulario de Subvención'"/>

	<xsl:variable name="lang.datosEnt" select="'Datos de la Entidad Solicitante'"/>
	<xsl:variable name="lang.datosRep" select="'Datos del Representante'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.nif" select="'CIF'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.direccion" select="'Dirección'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.fax" select="'Fax'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>
	<xsl:variable name="lang.cuenta" select="'Cuenta bancaria'"/> 

	<xsl:variable name="lang.proy_convocatoria" select="'Convocatoria'"/>			
	<xsl:variable name="lang.proy_titulo" select="'Título del proyecto'"/>			
	<xsl:variable name="lang.proy_resumen" select="'Exposición de motivos'"/>			
	<xsl:variable name="lang.proy_importe" select="'Presupuesto estimado'"/>			
	<xsl:variable name="lang.proy_cuantia" select="'Cuantía solicitada'"/>			
	<xsl:variable name="lang.proy_observaciones" select="'Observaciones'"/>			

	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo" select="'Para adjuntar un fichero (*.zip, *.xsig, *.odt o *.pdf), pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Memoria descriptiva del proyecto.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Certificado de aprobación de la memoria.'"/>
	<xsl:variable name="lang.anexarInfo3" select="'3.- Declaración de estar al corriente.'"/>
	<xsl:variable name="lang.anexarInfo4" select="'4.- Seleccione el fichero ZIP que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.documento1" select="'Documento ODT'"/>
	<xsl:variable name="lang.documento2" select="'Documento XSIG'"/>
	<xsl:variable name="lang.documento3" select="'Documento PDF'"/>
	<xsl:variable name="lang.documento4" select="'Documento ZIP'"/>
	
	<xsl:variable name="lang.conv249" select="'Convocatoria Plan de Mancomunidades 2010.'"/>
	<xsl:variable name="lang.convTitulo" select="'Convocatoria Plan de Mancomunidades 2010.'"/>

		
	<xsl:template match="/">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(19);

			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.nombre"/>');
			validar[2] = new Array('domicilioNotificacion','<xsl:value-of select="$lang.direccion"/>');
			validar[3] = new Array('localidad','<xsl:value-of select="$lang.localidad"/>');
			validar[4] = new Array('provincia','<xsl:value-of select="$lang.provincia"/>');
			validar[5] = new Array('codigoPostal','<xsl:value-of select="$lang.cp"/>');
			validar[6] = new Array('ent_nif', '<xsl:value-of select="$lang.nif"/>');
			validar[7] = new Array('ent_nombre','<xsl:value-of select="$lang.nombre"/>');
			validar[8] = new Array('ent_direccion','<xsl:value-of select="$lang.direccion"/>');
			validar[9] = new Array('ent_localidad','<xsl:value-of select="$lang.localidad"/>');
			validar[10] = new Array('ent_provincia','<xsl:value-of select="$lang.provincia"/>');
			validar[11] = new Array('ent_codigoPostal','<xsl:value-of select="$lang.cp"/>');
			validar[12] = new Array('emailSolicitante','<xsl:value-of select="$lang.email"/>');
			validar[13] = new Array('proy_convocatoria','<xsl:value-of select="$lang.proy_convocatoria"/>');
			validar[14] = new Array('proy_titulo','<xsl:value-of select="$lang.proy_titulo"/>');
			validar[15] = new Array('proy_resumen','<xsl:value-of select="$lang.proy_resumen"/>');
			validar[16] = new Array('proy_importe','<xsl:value-of select="$lang.proy_importe"/>');
			validar[17] = new Array('proy_cuantia','<xsl:value-of select="$lang.proy_cuantia"/>');
			validar[18] = new Array('ent_telefono','<xsl:value-of select="$lang.telefono"/>');

			

			//Array con los datos especificos del formulario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(22);
			
			especificos[0] = new Array('domicilioNotificacion','Domicilio_Notificacion');
			especificos[1] = new Array('localidad','Localidad');
			especificos[2] = new Array('provincia','Provincia');
			especificos[3] = new Array('codigoPostal','Codigo_Postal');
			especificos[4] = new Array('telefono','Telefono');
			especificos[5] = new Array('fax','Fax');
			
			especificos[6] = new Array('ent_nif','ent_nif');
			especificos[7] = new Array('ent_nombre','ent_nombre');
			especificos[8] = new Array('ent_direccion','ent_direccion');
			especificos[9] = new Array('ent_localidad','ent_localidad');
			especificos[10] = new Array('ent_provincia','ent_provincia');
			especificos[11] = new Array('ent_codigoPostal','ent_codigoPostal');
			especificos[12] = new Array('ent_telefono','ent_telefono');
			especificos[13] = new Array('ent_fax','ent_fax');
			especificos[14] = new Array('ent_email','ent_email');
			especificos[15] = new Array('proy_observaciones','proy_observaciones');
			
			especificos[16] = new Array('proy_convocatoria','proy_convocatoria');
			especificos[17] = new Array('proy_tituloconv','proy_tituloconv');
			especificos[18] = new Array('proy_titulo','proy_titulo');
			especificos[19] = new Array('proy_resumen','proy_resumen');
			especificos[20] = new Array('proy_importe','proy_importe');
			especificos[21] = new Array('proy_cuantia','proy_cuantia');
		
			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				return true;
			}
		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosRep"/></h1>
   		</div>
   		<div class="cuadro" style="">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nif"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
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
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="id">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Nombre"/></xsl:attribute>
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
					<xsl:value-of select="$lang.fax"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">fax</xsl:attribute>
					<xsl:attribute name="id">fax</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Fax"/></xsl:attribute>
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
		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosEnt"/></h1>
   		</div>
   		<div class="cuadro" style="">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nif"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
					<xsl:attribute name="name">ent_nif</xsl:attribute>
					<xsl:attribute name="id">ent_nif</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ent_nif"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nombre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">ent_nombre</xsl:attribute>
					<xsl:attribute name="id">ent_nombre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ent_nombre"/></xsl:attribute>
				</input>
			</div>
			<div class="col" style="height: 35px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.direccion"/>:*	
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">ent_direccion</xsl:attribute>
					<xsl:attribute name="id">ent_direccion</xsl:attribute>
					<xsl:attribute name="rows">5</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/ent_direccion"/>
				</textarea>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.localidad"/>:*	
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">ent_localidad</xsl:attribute>
					<xsl:attribute name="id">ent_localidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ent_localidad"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.provincia"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">ent_provincia</xsl:attribute>
					<xsl:attribute name="id">ent_provincia</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ent_provincia"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.cp"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">ent_codigoPostal</xsl:attribute>
					<xsl:attribute name="id">ent_codigoPostal</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ent_codigoPostal"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.telefono"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">ent_telefono</xsl:attribute>
					<xsl:attribute name="id">ent_telefono</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ent_telefono"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.fax"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">ent_fax</xsl:attribute>
					<xsl:attribute name="id">ent_fax</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ent_fax"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.email"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">ent_email</xsl:attribute>
					<xsl:attribute name="id">ent_email</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ent_email"/></xsl:attribute>
				</input>
			</div>
			<!-- <div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.cuenta"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">ent_cuenta</xsl:attribute>
					<xsl:attribute name="id">ent_cuenta</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ent_cuenta"/></xsl:attribute>
				</input>
			</div> -->
		</div>
		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
   		<div class="cuadro" style="">
   			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.proy_convocatoria"/>:*
				</label>
				<xsl:variable name="clas" select="Datos_Registro/datos_especificos/proy_convocatoria"/>
				<select class="gr">
					<xsl:attribute name="style">position: relative; width:650px; </xsl:attribute>
					<xsl:attribute name="name">proy_convocatoria</xsl:attribute>
					<xsl:attribute name="id">proy_convocatoria</xsl:attribute>
					<!--<option value=""></option>-->
					<xsl:choose>
				       	<xsl:when test="$clas='DPCR2010/12940'">
				           		<option value="DPCR2010/12940" selected="selected"><xsl:value-of select="$lang.conv249"/></option>
				       	</xsl:when>
				      		<xsl:otherwise>
				           		<option value="DPCR2010/12940"><xsl:value-of select="$lang.conv249"/></option> 
				       	</xsl:otherwise>
				    </xsl:choose>
				</select>		

				<xsl:variable name="clas" select="Datos_Registro/datos_especificos/proy_convocatoria"/>
				<xsl:variable name="clas" select="Datos_Registro/datos_especificos/proy_convocatoria"/>
			</div>
			
				<input type="hidden">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">proy_tituloconv</xsl:attribute>
					<xsl:attribute name="id">proy_tituloconv</xsl:attribute>
<!--					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/proy_tituloconv"/></xsl:attribute>-->
					<xsl:attribute name="value"><xsl:value-of select="$lang.convTitulo"/></xsl:attribute>
				</input>


			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.proy_titulo"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">proy_titulo</xsl:attribute>
					<xsl:attribute name="id">proy_titulo</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/proy_titulo"/></xsl:attribute>
				</input>
			</div>
 			<div class="col" style="height: 66px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.proy_resumen"/>:*
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">proy_resumen</xsl:attribute>
					<xsl:attribute name="id">proy_resumen</xsl:attribute>
					<xsl:attribute name="rows">5</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/proy_resumen"/>
				</textarea>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.proy_importe"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">proy_importe</xsl:attribute>
					<xsl:attribute name="id">proy_importe</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/proy_importe"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.proy_cuantia"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">proy_cuantia</xsl:attribute>
					<xsl:attribute name="id">proy_cuantia</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/proy_cuantia"/></xsl:attribute>
				</input>
			</div>
			<div class="col" style="height: 66px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.proy_observaciones"/>:
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">proy_observaciones</xsl:attribute>
					<xsl:attribute name="id">proy_observaciones</xsl:attribute>
					<xsl:attribute name="rows">5</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/proy_observaciones"/>
				</textarea>
			</div>
   		</div>
   		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   		</div>
		<div class="cuadro" style="">
			<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
					<xsl:value-of select="$lang.anexarInfo"/><br/>		   	
			</label>
		</div>
		<div class="cuadro" style="">
			<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.anexarInfo1"/><br/>
			</label>
   			<div class="col">
	   			<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento1"/>:*
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">MEMORIA DESCRIPTIVA</xsl:attribute>
					<xsl:attribute name="id">MEMORIA DESCRIPTIVA</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">MEMORIA DESCRIPTIVA_Tipo</xsl:attribute>
					<xsl:attribute name="id">MEMORIA DESCRIPTIVA_Tipo</xsl:attribute>
					<xsl:attribute name="value">odt</xsl:attribute>
				</input>
			</div>
   		</div>
		<div class="cuadro" style="">
			<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.anexarInfo2"/><br/>
			</label>
   			<div class="col">
	   			<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento2"/>:*
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">CERTIFICADO APROBACION MEMORIA</xsl:attribute>
					<xsl:attribute name="id">CERTIFICADO APROBACION MEMORIA</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CERTIFICADO APROBACION MEMORIA_Tipo</xsl:attribute>
					<xsl:attribute name="id">CERTIFICADO APROBACION MEMORIA_Tipo</xsl:attribute>
					<xsl:attribute name="value">xsig</xsl:attribute>
				</input>
			</div>
   		</div>
		<div class="cuadro" style="">
			<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>		   			
		   			<xsl:value-of select="$lang.anexarInfo3"/><br/>
			</label>
   			<div class="col">
	   			<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>					
					<xsl:value-of select="$lang.documento3"/>:*
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">DECLARACION ESTAR AL CORRIENTE</xsl:attribute>
					<xsl:attribute name="id">DECLARACION ESTAR AL CORRIENTE</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">DECLARACION ESTAR AL CORRIENTE_Tipo</xsl:attribute>
					<xsl:attribute name="id">DECLARACION ESTAR AL CORRIENTE_Tipo</xsl:attribute>
					<xsl:attribute name="value">pdf</xsl:attribute>
				</input>
							
			</div>
   		</div>	
		<div class="cuadro" style="">
			<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>		   			
		   			<xsl:value-of select="$lang.anexarInfo4"/><br/>
			</label>
   			<div class="col">
	   			<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>					
					<xsl:value-of select="$lang.documento4"/>:*
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">SUBVD4</xsl:attribute>
					<xsl:attribute name="id">SUBVD4</xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">SUBVD4_Tipo</xsl:attribute>
					<xsl:attribute name="id">SUBVD4_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
							
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