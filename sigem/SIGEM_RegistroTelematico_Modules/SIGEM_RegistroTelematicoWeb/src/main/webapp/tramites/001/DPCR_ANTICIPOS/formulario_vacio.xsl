<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'Formulario General de Solicitud de Anticipos Reintegrables'"/>
	<xsl:variable name="lang.datosSolicitante" select="'Datos del Solicitante'"/>
	<xsl:variable name="lang.docIdentidad" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>
	<xsl:variable name="lang.departamento" select="'Departamento'"/>
	<xsl:variable name="lang.tipoContrato" select="'Tipo de Contrato'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	<xsl:variable name="lang.importeTotal" select="'Importe Total'"/>
	<xsl:variable name="lang.numMeses" select="'Meses'"/>
	<xsl:variable name="lang.importeMes" select="'Importe Mes'"/>
	<xsl:variable name="lang.importeUltimoMes" select="'Último Mes'"/>
	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>
	<xsl:variable name="lang.organoAsignado" select="'Órgano al que se dirige: Registro de la Excma. Diputación Provincial de Ciudad Real (Personal)'"/>
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.docAnexar" select="'Documentación obligatoria: '"/>
	<xsl:variable name="lang.docAnexar2" select="'Puede presentar la documentación escaneada o presentarla en persona a su Jefe de Departamento.'"/>
	<xsl:variable name="lang.docNoObligatoria" select="'Este trámite electrónico no tiene documentación obligatoria a anexar.'"/>
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
	<xsl:variable name="lang.simboloEuro" select="'€'"/>
	<xsl:variable name="lang.compromiso_titulo" select="'Compromiso de Reintegro'"/>
	<xsl:variable name="lang.compromiso_texto" select="'De conformidad con lo dispuesto en las Bases de Ejecución del Presupuesto de la Diputación Provincial, asumo expresamente el compromiso de devolución mediante domiciliación bancaria de las mensualidades que correspondan incluso cuando, por cualquier causa, se produzca la suspensión del pago de haberes por parte de la Diputación.'"/>

	<xsl:template match="/">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			var validar = new Array(8);
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.docIdentidad"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.nombre"/>');
			validar[2] = new Array('departamento','<xsl:value-of select="$lang.departamento"/>');
			validar[3] = new Array('tipoContrato','<xsl:value-of select="$lang.tipoContrato"/>');
			validar[4] = new Array('importeTotal','<xsl:value-of select="$lang.importeTotal"/>');
			validar[5] = new Array('numMeses','<xsl:value-of select="$lang.numMeses"/>');
			validar[6] = new Array('importeMes','<xsl:value-of select="$lang.importeMes"/>');
			validar[7] = new Array('importeUltimoMes','<xsl:value-of select="$lang.importeUltimoMes"/>');
			var validar = undefined;

			var validar_NO = new Array(4);
			validar_NO[0] = new Array('ANTICIPOS_D1','<xsl:value-of select="$lang.documento1"/>');
			validar_NO[1] = new Array('ANTICIPOS_D2','<xsl:value-of select="$lang.documento2"/>');
			validar_NO[2] = new Array('ANTICIPOS_D3','<xsl:value-of select="$lang.documento3"/>');
			validar_NO[3] = new Array('ANTICIPOS_D4','<xsl:value-of select="$lang.documento4"/>');

			//Array con los datos especificos del formulario -> -> ('id_campo','tag_xml')
			var especificos = new Array(11);
			especificos[0] = new Array('departamento','Departamento');
			especificos[1] = new Array('tipoContrato','Tipo_Contrato');
			especificos[2] = new Array('importeTotal','Importe_Total');
			especificos[3] = new Array('numMeses','Num_Meses');
			especificos[4] = new Array('importeMes','Importe_Mes');
			especificos[5] = new Array('importeUltimoMes','Importe_Ultimo_Mes');
			especificos[6] = new Array('observaciones','Observaciones');
			especificos[7] = new Array('documentoIdentidad','NIF');
			especificos[8] = new Array('nombreSolicitante','Nombre');
			
			especificos[9] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[10] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');

			var validarNumero = undefined;

			function verificacionesEspecificas() {
				var NIFcertificado = '<xsl:value-of select="Datos_Registro/Remitente/Documento_Identificacion/Numero"/>';
				var NIFsolicitud = '<xsl:value-of select="Datos_Registro/datos_especificos/NIF"/>';
				var esAdministracion = '<xsl:value-of select="Datos_Registro/datos_especificos/Es_Administracion"/>';
				if (esAdministracion != 'true'){
					if (NIFcertificado != NIFsolicitud){
						alert('El NIF del solicitante "' + NIFsolicitud + '" es distinto al NIF del certificado con el que está realizando el registro.' +
							'\nPor favor, asegurese de que el certificado con el que está accediendo le pertenece.')
						return false;
					}
					else{
						return true;
					}
				}
				else{
					return true;
				}
			}

		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/NIF"/></xsl:attribute>
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Nombre"/></xsl:attribute>
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
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>-->
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
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.tipoContrato"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">tipoContrato</xsl:attribute>
					<xsl:attribute name="id">tipoContrato</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Tipo_Contrato"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
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
					<xsl:value-of select="$lang.importeTotal"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:60px; text-align:right; padding-right:3px;</xsl:attribute>
					<xsl:attribute name="name">importeTotal</xsl:attribute>
					<xsl:attribute name="id">importeTotal</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Importe_Total"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
				<span style="color: #006699;"><xsl:value-of select="$lang.simboloEuro"/></span>
			</div>
 			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.numMeses"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:60px; text-align:right; padding-right:3px;</xsl:attribute>
					<xsl:attribute name="name">numMeses</xsl:attribute>
					<xsl:attribute name="id">numMeses</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Num_Meses"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.importeMes"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:60px; text-align:right; padding-right:3px;</xsl:attribute>
					<xsl:attribute name="name">importeMes</xsl:attribute>
					<xsl:attribute name="id">importeMes</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Importe_Mes"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
				<span style="color: #006699;"><xsl:value-of select="$lang.simboloEuro"/></span>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.importeUltimoMes"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:60px; text-align:right; padding-right:3px;</xsl:attribute>
					<xsl:attribute name="name">importeUltimoMes</xsl:attribute>
					<xsl:attribute name="id">importeUltimoMes</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Importe_Ultimo_Mes"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
				<span style="color: #006699;"><xsl:value-of select="$lang.simboloEuro"/></span>
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
    
    <div class="submenu">
 			<h1><xsl:value-of select="$lang.compromiso_titulo"/></h1>
 		</div>
  	<div class="cuadro" style="text-align:justify">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:650px</xsl:attribute>
					<xsl:value-of select="$lang.compromiso_texto"/>
				</label>
			</div>
		</div>

   		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.organoAsignado"/></h1>
   		</div>

   		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN" />
   		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<xsl:if test="Datos_Registro/datos_especificos/Docs != ''">
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
   			</xsl:if>
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
					<xsl:attribute name="name">ANTICIPOS_D1</xsl:attribute>
					<xsl:attribute name="id">ANTICIPOS_D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">ANTICIPOS_D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">ANTICIPOS_D1_Tipo</xsl:attribute>
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
					<xsl:attribute name="name">ANTICIPOS_D2</xsl:attribute>
					<xsl:attribute name="id">ANTICIPOS_D2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">ANTICIPOS_D2_Tipo</xsl:attribute>
					<xsl:attribute name="id">ANTICIPOS_D2_Tipo</xsl:attribute>
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
					<xsl:attribute name="name">ANTICIPOS_D3</xsl:attribute>
					<xsl:attribute name="id">ANTICIPOS_D3</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">ANTICIPOS_D3_Tipo</xsl:attribute>
					<xsl:attribute name="id">ANTICIPOS_D3_Tipo</xsl:attribute>
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
					<xsl:attribute name="name">ANTICIPOS_D4</xsl:attribute>
					<xsl:attribute name="id">ANTICIPOS_D4</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">ANTICIPOS_D4_Tipo</xsl:attribute>
					<xsl:attribute name="id">ANTICIPOS_D4_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
			</div>
		</div>
		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN" />
   		<br/>
		<xsl:call-template name="TEXTO_COMPARECE_COMUN" />
			
   		<br/>
   		<input type="hidden">
				<xsl:attribute name="name">datosEspecificos</xsl:attribute>
				<xsl:attribute name="id">datosEspecificos</xsl:attribute>
				<xsl:attribute name="value"></xsl:attribute>
			</input>
   		<input class="gr" type="hidden" id="solicitarEnvio" onclick="activarDEU();" style="border:0px; width:20px;" />
   		<input class="gr" type="hidden" id="direccionElectronicaUnica" />
   		<input class="gr" type="hidden" id="emailSolicitante" />
   		<br/>

	</xsl:template>
</xsl:stylesheet>
