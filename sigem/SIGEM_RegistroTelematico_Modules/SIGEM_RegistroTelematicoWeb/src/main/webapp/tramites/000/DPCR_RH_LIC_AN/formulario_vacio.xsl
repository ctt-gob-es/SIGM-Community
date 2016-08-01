<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'Formulario de ANULACIÓN de '"/>
	<xsl:variable name="lang.datosSolicitante" select="'Datos del Solicitante'"/>
	<xsl:variable name="lang.docIdentidad" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>
	<xsl:variable name="lang.codigoDepartamento" select="'Código Departamento'"/>
	<xsl:variable name="lang.departamento" select="'Departamento'"/>
	<xsl:variable name="lang.contratado" select="'Contratado'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	<xsl:variable name="lang.idSolicitud" select="'Id Licencia'"/>
	<xsl:variable name="lang.codigoPeticion" select="'Código Licencia'"/>
	<xsl:variable name="lang.tipoPeticion" select="'Tipo Licencia'"/>
	<xsl:variable name="lang.fechaInicio" select="'Fecha Inicio'"/>
	<xsl:variable name="lang.fechaFin" select="'Fecha Fin'"/>
	<xsl:variable name="lang.numDiasSolicitados" select="'Número días'"/>
	<xsl:variable name="lang.diasSolicitados" select="'Fechas a anular'"/>
	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>
	<xsl:variable name="lang.organoAsignado" select="'Órgano al que se dirige: Registro de la Excma. Diputación Provincial de Ciudad Real (Personal)'"/>
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.docAnexar" select="'Documentación obligatoria: '"/>
	<xsl:variable name="lang.docAnexar2" select="'Puede presentar la documentación escaneada o presentarla en persona a su Jefe de Departamento.'"/>
	<xsl:variable name="lang.docNoObligatoria" select="'El tipo de licencia seleccionada no tiene documentación obligatoria a anexar.'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.anexarInfo3" select="'(Máximo 7 mbs total)'"/>
	<xsl:variable name="lang.documento1" select="'Documento ODT/DOC'"/>
	<xsl:variable name="lang.documento2" select="'Documento PDF'"/>
	<xsl:variable name="lang.documento3" select="'Documento JPEG'"/>
	<xsl:variable name="lang.documento4" select="'Documento ZIP'"/>
	<xsl:variable name="lang.envio" select="'Envío de notificaciones'"/>
	<xsl:variable name="lang.solicitoEnvio" select="'Solicito el envío de notificaciones por medios telemáticos'"/>
	<xsl:variable name="lang.deu" select="'D.E.U.'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.info" select="'Información de la licencia'"/>

	<xsl:template match="/">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			var validar = new Array(12);
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.docIdentidad"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.nombre"/>');
			validar[2] = new Array('codigoDepartamento','<xsl:value-of select="$lang.codigoDepartamento"/>');
			validar[3] = new Array('departamento','<xsl:value-of select="$lang.departamento"/>');
			validar[4] = new Array('contratado','<xsl:value-of select="$lang.contratado"/>');
			validar[5] = new Array('codigoPeticion','<xsl:value-of select="$lang.codigoPeticion"/>');
			validar[6] = new Array('tipoPeticion','<xsl:value-of select="$lang.tipoPeticion"/>');
			validar[7] = new Array('fechaInicio','<xsl:value-of select="$lang.fechaInicio"/>');
			validar[8] = new Array('fechaFin','<xsl:value-of select="$lang.fechaFin"/>');
			validar[9] = new Array('numDiasSolicitados','<xsl:value-of select="$lang.numDiasSolicitados"/>');
			validar[10] = new Array('diasSolicitados','<xsl:value-of select="$lang.diasSolicitados"/>');
			var validar = undefined;

			var validar_NO = new Array(4);
			validar_NO[0] = new Array('RHLIC_D1','<xsl:value-of select="$lang.documento1"/>');
			validar_NO[1] = new Array('RHLIC_D2','<xsl:value-of select="$lang.documento2"/>');
			validar_NO[1] = new Array('RHLIC_D3','<xsl:value-of select="$lang.documento3"/>');
			validar_NO[1] = new Array('RHLIC_D4','<xsl:value-of select="$lang.documento4"/>');

			//Array con los datos especificos del formulario -> -> ('id_campo','tag_xml')
			var especificos = new Array(10);
			especificos[0] = new Array('codigoDepartamento','Cod_Departamento');
			especificos[1] = new Array('departamento','Departamento');
			//especificos[2] = new Array('contratado','Contratado');
			especificos[2] = new Array('codigoPeticion','Codigo_Peticion');
			especificos[3] = new Array('tipoPeticion','Tipo_Peticion');
			especificos[4] = new Array('fechaInicio','Fecha_Inicio');
			especificos[5] = new Array('fechaFin','Fecha_Fin');
			especificos[6] = new Array('numDiasSolicitados','Num_Dias_Solicitados');
			especificos[7] = new Array('diasSolicitados','Dias_Solicitados');
			especificos[8] = new Array('observaciones','Observaciones');
			especificos[9] = new Array('idSolicitud','Id_Solicitud');
			//especificos[10] = new Array('info','Info');
			//especificos[10] = new Array('docs','Docs');
			
			var validarNumero = undefined;

			function verificacionesEspecificas() {
				var NIFcertificado = '<xsl:value-of select="Datos_Registro/Remitente/Documento_Identificacion/Numero"/>';
				var NIFsolicitud = '<xsl:value-of select="Datos_Registro/datos_especificos/NIF"/>';
				if (NIFcertificado != NIFsolicitud){
					alert('El NIF del solicitante "' + NIFsolicitud + '" es distinto al NIF del certificado con el que está realizando el registro.' +
						'\nPor favor, asegurese de que el certificado con el que está accediendo le pertenece.')
					return false;
				}
				else{
					return true;
				}
			}

		</script>
		<h1><xsl:value-of select="$lang.titulo"/><xsl:value-of select="Datos_Registro/datos_especificos/Tipo_Peticion"/></h1>
			<!--<label>
				<xsl:attribute name="name">info</xsl:attribute>
				<xsl:attribute name="id">info</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Info"/></xsl:attribute>
				<xsl:value-of select="Datos_Registro/datos_especificos/Info"/><br/>
			</label>-->
   		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitante"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.docIdentidad"/>:
				</label>
				<input id="inputdni" type="text">
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
					<xsl:value-of select="$lang.nombre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="id">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Nombre"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<!--<div class="col">
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
			</div>-->
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.codigoDepartamento"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">codigoDepartamento</xsl:attribute>
					<xsl:attribute name="id">codigoDepartamento</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Cod_Departamento"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.departamento"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">departamento</xsl:attribute>
					<xsl:attribute name="id">departamento</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Departamento"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<!--<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.contratado"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:50px; </xsl:attribute>
					<xsl:attribute name="name">contratado</xsl:attribute>
					<xsl:attribute name="id">contratado</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Contratado"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>-->
		</div>
		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
   		<div class="cuadro" style="">
   		<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.idSolicitud"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">idSolicitud</xsl:attribute>
					<xsl:attribute name="id">idSolicitud</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Id_Solicitud"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.codigoPeticion"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:50px; </xsl:attribute>
					<xsl:attribute name="name">codigoPeticion</xsl:attribute>
					<xsl:attribute name="id">codigoPeticion</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Codigo_Peticion"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
 			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.tipoPeticion"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">tipoPeticion</xsl:attribute>
					<xsl:attribute name="id">tipoPeticion</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Tipo_Peticion"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.fechaInicio"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">fechaInicio</xsl:attribute>
					<xsl:attribute name="id">fechaInicio</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Fecha_Inicio"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.fechaFin"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">fechaFin</xsl:attribute>
					<xsl:attribute name="id">fechaFin</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Fecha_Fin"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.numDiasSolicitados"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:50px; </xsl:attribute>
					<xsl:attribute name="name">numDiasSolicitados</xsl:attribute>
					<xsl:attribute name="id">numDiasSolicitados</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Num_Dias_Solicitados"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
		  <div class="col" style="height: 66px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.diasSolicitados"/>:
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:350px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">diasSolicitados</xsl:attribute>
					<xsl:attribute name="id">diasSolicitados</xsl:attribute>
					<xsl:attribute name="rows">3</xsl:attribute>
					<xsl:attribute name="disabled">disabled</xsl:attribute>
					<!--<xsl:attribute name="onfocus">alert('No puede modificar las fechas solicitadas');this.blur();</xsl:attribute>-->
					<xsl:value-of select="Datos_Registro/datos_especificos/Dias_Solicitados"/>
				</textarea>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.observaciones"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">observaciones</xsl:attribute>
					<xsl:attribute name="id">observaciones</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Observaciones"/></xsl:attribute>
					<xsl:attribute name="maxlength">24</xsl:attribute>
				</input>
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
			<!--<xsl:if test="Datos_Registro/datos_especificos/Docs != ''">
				<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px; color:red; font-weight:bold;</xsl:attribute>
			   		<xsl:attribute name="name">docs</xsl:attribute>
						<xsl:attribute name="id">docs</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Docs"/></xsl:attribute>
		   			<xsl:value-of select="$lang.docAnexar"/>
		   			<xsl:value-of select="Datos_Registro/datos_especificos/Docs"/><br/>
				</label>
				<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.docAnexar2"/><br/><br/>
				</label>
   			</xsl:if>
   			<xsl:if test="not(Datos_Registro/datos_especificos/Docs != '')">
				<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px; font-weight:bold;</xsl:attribute>
			   		<xsl:attribute name="name">docs</xsl:attribute>
						<xsl:attribute name="id">docs</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Docs"/></xsl:attribute>
		   			<xsl:value-of select="$lang.docNoObligatoria"/><br/><br/>
				</label>
   			</xsl:if>-->
			<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.anexarInfo1"/><br/>
		   			<xsl:value-of select="$lang.anexarInfo2"/><br/>
						<xsl:value-of select="$lang.anexarInfo3"/><br/>
			</label>
			<div class="col">
	  		<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento1"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; </xsl:attribute>
					<xsl:attribute name="name">RHLIC_D1</xsl:attribute>
					<xsl:attribute name="id">RHLIC_D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">RHLIC_D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">RHLIC_D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">odt,doc</xsl:attribute>
				</input>
			</div>

			<div class="col">
	  		<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento2"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; </xsl:attribute>
					<xsl:attribute name="name">RHLIC_D2</xsl:attribute>
					<xsl:attribute name="id">RHLIC_D2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">RHLIC_D2_Tipo</xsl:attribute>
					<xsl:attribute name="id">RHLIC_D2_Tipo</xsl:attribute>
					<xsl:attribute name="value">pdf</xsl:attribute>
				</input>
			</div>

			<div class="col">
	  		<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento3"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; </xsl:attribute>
					<xsl:attribute name="name">RHLIC_D3</xsl:attribute>
					<xsl:attribute name="id">RHLIC_D3</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">RHLIC_D3_Tipo</xsl:attribute>
					<xsl:attribute name="id">RHLIC_D3_Tipo</xsl:attribute>
					<xsl:attribute name="value">jpeg</xsl:attribute>
				</input>
			</div>

			<div class="col">
	  		<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento4"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; </xsl:attribute>
					<xsl:attribute name="name">RHLIC_D4</xsl:attribute>
					<xsl:attribute name="id">RHLIC_D4</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">RHLIC_D4_Tipo</xsl:attribute>
					<xsl:attribute name="id">RHLIC_D4_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
			</div>

   		</div>
   		<br/>
   		<input type="hidden"><xsl:attribute name="id">emailSolicitante</xsl:attribute></input>
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
