<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'SOLICITUD DE ACCESO TELEMÁTICO A EXPEDIENTE'"/>
	
	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>
	<xsl:variable name="lang.cargo" select="'Cargo'"/>

	<xsl:variable name="lang.datosRepresentante" select="'Datos del responsable del proyecto/actividad'"/>
	<xsl:variable name="lang.datosInteresado" select="'Datos del solicitante o del representado'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.expediente" select="'Expdientes del solicitante:* (seleccionar)'"/>
	<xsl:variable name="lang.expedienteObligatoria" select="'Expediente'"/>

	<xsl:variable name="lang.nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>

	<xsl:variable name="lang.anio" select="'Año de inicio del expediente*'"/>

	<xsl:variable name="lang.nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.representante" select="'En representación de'"/>
	<xsl:variable name="lang.cif" select="'CIF'"/>
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>

	<xsl:variable name="lang.selecSol" select="'Seleccione el expediente que desea subsanar, justificar o modificar.'"/>
	<xsl:variable name="lang.asunto" select="'Asunto'"/>
	<xsl:variable name="lang.numexp" select="'Número de Registro de Entrada en Diputación de la solicitud'"/>

	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>
	
	<xsl:variable name="lang.anexar" select="'DOCUMENTACIÓN QUE SE ACOMPAÑA'"/>
	
	<xsl:variable name="lang.anexarInfo" select="'Para adjuntar los ficheros, pulse el botón examinar. Seleccione el fichero que desee anexar a la solicitud.'"/>

	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>
	<xsl:variable name="lang.documentoTipoXSIG" select="'Archivo XSIG'"/>
	<xsl:variable name="lang.documentoTipo_DOC_ODT" select="'Archivo ODT/DOC'"/>
	<xsl:variable name="lang.documentoTipoPDF" select="'Archivo PDF'"/>
	<xsl:variable name="lang.documentoTipoJPEG" select="'Archivo JPEG'"/>
		
	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(4);
			
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');
			validar[2] = new Array('expone','<xsl:value-of select="$lang.expone"/>');
			validar[3] = new Array('solicita','<xsl:value-of select="$lang.solicita"/>');

			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(13);
			
			especificos[0] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[1] = new Array('nombreSolicitante','nombreSolicitante');
			especificos[2] = new Array('emailSolicitante','emailSolicitante');
			especificos[3] = new Array('expone','expone');
			especificos[4] = new Array('solicita','solicita');
			especificos[5] = new Array('recurso','recurso');
			especificos[6] = new Array('asunto','asunto');
			especificos[7] = new Array('numExpediente','numExpediente');
			especificos[8] = new Array('asociacion','asociacion');
			especificos[9] = new Array('anio','anio');
			especificos[10] = new Array('cargo','cargo');
			
			especificos[11] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[12] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');
						
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {

				if(document.getElementById('cargo').value == ''){
					alert('Debe inidicar el cargo de la persona peticionaria');
					document.getElementById('cargo').focus();
					return false;
				}

				if(document.getElementById('asociacion').value=='-------'){
					alert('Debe seleccionar un representado');
					document.getElementById('asociacion').focus();
					return false;
				}

				if(document.getElementById('anio').value=='----'){
					alert('Debe seleccionar un año para el expediente');
					document.getElementById('anio').focus();
					return false;
				}

				if(document.getElementById('expedientes').value=='-------'){
					alert('Debe seleccionar un expediente');
					document.getElementById('expedientes').focus();
					return false;
				}

				document.getElementById('nombreSolicitante').value = document.getElementById('asociacion').options[document.getElementById('asociacion').selectedIndex].text;
				document.getElementById('documentoIdentidad').value = document.getElementById('cif').value;

				document.getElementById('recurso').value= 'Pers.Fis.-Empr.';

				return true;
			}
			
			function getDatosObligado(nif){
				window.open('tramites/001/ACCTELEXPEMP/buscaObligado.jsp?valor='+nif+';001','','width=3,height=3');
			}
			
			function abrirDocumento(){ 
				v=window.open("documentos/MODELO.odt"); 
			} 
			function getDatosRepre(cifRepre){
				document.getElementById('anio').value='----';
				document.getElementById('expedientes').value='-------';
				document.getElementById('cif').value = cifRepre;
			}
			
			function getAsuntoNumexp(numExpediente){
				window.open('tramites/001/ACCTELEXPEMP/buscaAsuntoNumExp.jsp?valor='+numExpediente+';001','','width=700,height=300,resizable=1');
			}
			function getDatosObligadoEntidad(nif){
				window.open('tramites/001/ACCTELEXPEMP/buscaObligadoEntidad.jsp?valor='+nif+';001','','width=3,height=3');
			}
			function getDatos(anio){
				var dni = document.getElementById('cif').value;
				if(dni=="-------" || dni==""){<!-- Compruebo que el cif no sea vacio -->
					document.getElementById('anio').value='----';
					alert('Debe seleccionar un representado.');
					document.getElementById('asociacion').focus();
					return false;
				}
				window.open('tramites/001/ACCTELEXPEMP/dameExpedientes.jsp?valor='+dni+';'+anio.value+';001','','width=3,height=3,left=10000,top=10000');
			}
			
			function getRepresentados(){
				window.open('tramites/001/ACCTELEXPEMP/dameRepresentantes.jsp?valor='+document.getElementById('documentoIdentidad').value+';001','','width=3,height=3,left=10000,top=10000');
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

   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosInteresado"/></h1>
   		</div>
   		
   		<div class="cuadro" style="">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.representante"/>:*
				</label>
				<select onchange="getDatosRepre(this.value)">
					<xsl:attribute name="style">width:400px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">asociacion</xsl:attribute>
					<xsl:attribute name="id">asociacion</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/asociacion"/></xsl:attribute>
					<option>
						<xsl:attribute name="value">-------</xsl:attribute>----------------------------------------------------
					</option>
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
			
		</div>
		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
		<div class="cuadro" style="">
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

	<!-- MQE Cargamos los expedientes sobre los que puede subsanar-->	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%;</xsl:attribute>
					<xsl:value-of select="$lang.selecSol"/>:
				</label>
			</div>
	
			<select class="gr">
				<xsl:attribute name="style">border:none; width:650px;color:#006699;</xsl:attribute>
				<xsl:attribute name="name">expedientes</xsl:attribute>
				<xsl:attribute name="id">expedientes</xsl:attribute>
				<xsl:attribute name="onblur"> document.getElementById('asunto').value = this.options[this.selectedIndex].text; document.getElementById('numExpediente').value = this.value;</xsl:attribute>
					<option>
						<xsl:attribute name="value">-------</xsl:attribute>----------------------------------------------------
					</option>
			</select>
		</div>  
	<!-- MQE fin modificaciones -->


   		<div class="cuadro" style="">
			<div class="col" style="display:none;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.numexp"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:200px;</xsl:attribute>
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
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">asunto</xsl:attribute>
					<xsl:attribute name="id">asunto</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/asunto"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>

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
				document.getElementById('expone').value = 'Que la entidad a la que represento tiene interés en conocer el estado de tramitación del expediente que señala y los documentos que lo integran.';
				document.getElementById('solicita').value = 'Que se me facilite la documentación solicitada.';
			</script>
			
			</div>		
   			<br/>
			<xsl:call-template name="TEXTO_LEGAL_COMUN" />
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
   		</div>
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
		<script language="Javascript">
			getRepresentados();
		</script>
	</xsl:template>
</xsl:stylesheet>
