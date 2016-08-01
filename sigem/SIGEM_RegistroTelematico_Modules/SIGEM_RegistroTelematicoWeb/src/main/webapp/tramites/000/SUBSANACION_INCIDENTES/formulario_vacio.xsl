<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'SUBSANACIÓN O TRAMITACIÓN DE INCIDENTES EN EXPEDIENTE INICIADO'"/>
	
	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>

	<xsl:variable name="lang.datosRepresentante" select="'Datos de la persona física o del representante de la persona jurídica'"/>
	<xsl:variable name="lang.datosInteresado" select="'Datos de la entidad'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.selecSol" select="'Seleccione el expediente que desea subsanar o modificar.'"/>
	
	<xsl:variable name="lang.anio" select="'Año de inicio del expediente:*'"/>

	<xsl:variable name="lang.asunto" select="'Asunto'"/>
	<xsl:variable name="lang.numexp" select="'Número de Registro de Entrada en Diputación de la solicitud'"/>
	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>	

	<xsl:variable name="lang.declaro" select="'Asimismo, declaro bajo mi Responsabilidad (obligatorio marcar con una x cada cuadro para poder seguir el trámite)'"/>

	<xsl:variable name="lang.anexar" select="'Puede anexar los documentos que considere pertinentes en cualquiera de los formatos abajo indicados'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.anexarInfo3" select="'(Máximo 7 MB total)'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>
	<xsl:variable name="lang.documentoTipo_DOC_ODT" select="'Archivo ODT/DOC'"/>
	<xsl:variable name="lang.documentoTipoPDF" select="'Archivo PDF'"/>
	<xsl:variable name="lang.documentoTipoJPEG" select="'Archivo JPG'"/>
	
	<xsl:variable name="lang.Comparece" select="'QUEREMOS SER MÁS ÁGILES Y CONTESTARLE CON RAPIDEZ'"/>
	<xsl:variable name="lang.Comparece1" select="'Portal de Notificaciones Telemáticas de la Diputación de Ciudad Real COMPARECE'"/>
	<xsl:variable name="lang.Comparece2" select="'Si lo desea, puede ayudarnos dándose de alta en el'"/>
	<xsl:variable name="lang.Comparece3" select="'Rápido, gratuito, sin papel.'"/>
	
		
	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(4);
			
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');

			validar[2] = new Array('expone','<xsl:value-of select="$lang.expone"/>');
			validar[3] = new Array('solicita','<xsl:value-of select="$lang.solicita"/>');


			//Array con los datos especificos del formulario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(9);
				
			especificos[0] = new Array('asunto','asunto');
			especificos[1] = new Array('numExpediente','numExpediente');
			
			especificos[2] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[3] = new Array('nombreSolicitante','nombreSolicitante');
			especificos[4] = new Array('emailSolicitante','emailSolicitante');
			
			especificos[5] = new Array('cif','cif');
			
			especificos[6] = new Array('asunto','asunto');
			
			especificos[7] = new Array('expone','expone');
			especificos[8] = new Array('solicita','solicita');
			

			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				document.getElementById('cif').value = document.getElementById('documentoIdentidad').value;

				if(document.getElementById('anio').value=='----'){
					alert('Debe seleccionar un año de inicio del expediente');
					document.getElementById('anio').focus();
					return false;
				}

				if(document.getElementById('expedientes').value=='-------'){
					alert('Debe seleccionar un expediente');
					document.getElementById('expedientes').focus();
					return false;
				}

				return true;
			}
			
			function abrirDocumento(){ 
				v=window.open("documentos/MODELO.odt"); 
			} 
			function getDatos(anio){
				window.open('tramites/000/SUBSANACION_INCIDENTES/dameExpedientes.jsp?valor='+document.getElementById('documentoIdentidad').value+';'+anio.value+';000','','width=3,height=3,left=10000,top=10000');
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
		</div>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
		<!-- MQE Cargamos los expedientes sobre los que puede subsanar-->	
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
					<xsl:attribute name="style">position: relative; width:200px; </xsl:attribute>
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
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">asunto</xsl:attribute>
					<xsl:attribute name="id">asunto</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/asunto"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.expone"/>:*
				</label>
				<textarea type="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">expone</xsl:attribute>
					<xsl:attribute name="id">expone</xsl:attribute>
					<xsl:attribute name="rows">7</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/expone"/>
				</textarea>
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
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<label class="gr">
		   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
				<xsl:value-of select="$lang.anexarInfo1"/>
		  		<br/>
		   		<xsl:value-of select="$lang.anexarInfo2"/><br/>
		   		<xsl:value-of select="$lang.anexarInfo3"/><br/>
			</label>
			<br/>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_SUBJUS_D1</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_SUBJUS_D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip,rar</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipo_DOC_ODT"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_SUBJUS_D2</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_SUBJUS_D2_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D2_Tipo</xsl:attribute>
					<xsl:attribute name="value">odt,doc</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoPDF"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_SUBJUS_D3</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D3</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_SUBJUS_D3_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D3_Tipo</xsl:attribute>
					<xsl:attribute name="value">PDF</xsl:attribute>
				</input>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:200px; text-indent:40px</xsl:attribute>
					<b><xsl:value-of select="$lang.documentoTipoJPEG"/>:<font color="#950000"></font></b>
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; size:400px;</xsl:attribute>
					<xsl:attribute name="name">CR_SUBJUS_D4</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D4</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">CR_SUBJUS_D4_Tipo</xsl:attribute>
					<xsl:attribute name="id">CR_SUBJUS_D4_Tipo</xsl:attribute>
					<xsl:attribute name="value">JPG</xsl:attribute>
				</input>
			</div>
			<br/>
			<div style="color: grey; text-align:justify">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
					Los datos personales, identificativos y de contacto, aportados mediante esta comunicación se entienden facilitados voluntariamente, y serán incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del ámbito de las competencias de esta Administración Pública así como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telemáticos y enviarle invitaciones para eventos y felicitaciones en fechas señaladas. Entenderemos que presta su consentimiento tácito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podrá ejercer sus derechos de acceso, rectificación, cancelación y oposición ante el Responsable del Fichero, la Diputación Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - España, siempre acreditando conforme a Derecho su identidad en la comunicación. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigiéndose a la dirección citada ut supra o bien al correo electrónico lopd@dipucr.es o bien por telefono al numero gratuito 900 714 080.	
				</label>
			</div>
	   	</div>   		
   		<br/>
		<div class="cuadro" style="">
			<xsl:value-of select="$lang.Comparece"/><br/>
			<xsl:value-of select="$lang.Comparece2"/><br/>
			<a href="http://comparece.dipucr.es:8080/CompareceNotificadorInterfaz/" target="_blank">
				<xsl:attribute name="style">font-weight:bold; color:red; </xsl:attribute>				
				<xsl:value-of select="$lang.Comparece1"/>
			</a>			
			<br/>
			<xsl:value-of select="$lang.Comparece3"/><br/>
			<br/>
		</div>
		<br/>
		<input type="hidden">
			<xsl:attribute name="name">datosEspecificos</xsl:attribute>
			<xsl:attribute name="id">datosEspecificos</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name">cif</xsl:attribute>
			<xsl:attribute name="id">cif</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name">emailSolicitante</xsl:attribute>
			<xsl:attribute name="id">emailSolicitante</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
	</xsl:template>
</xsl:stylesheet>
