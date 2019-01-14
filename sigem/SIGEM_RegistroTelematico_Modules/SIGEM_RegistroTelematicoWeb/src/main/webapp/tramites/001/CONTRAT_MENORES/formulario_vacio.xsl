<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'OFERTAS ECONÓMICAS CONTRATOS MENORES'"/>
	
	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>

	<xsl:variable name="lang.datosRepresentante" select="'Datos del presentador'"/>
	<xsl:variable name="lang.datosInteresado" select="'Datos del Licitador'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	<xsl:variable name="lang.datosDeclarativos" select="'Declaraciones Preceptivas que formula el solicitante'"/>
	
	<xsl:variable name="lang.nif_repre" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre_repre" select="'Apellidos y Nombre o Denominación Social'"/>
	<xsl:variable name="lang.domicilio_repre" select="'Domicilio'"/>
	<xsl:variable name="lang.ciudad_repre" select="'Localidad'"/>
	<xsl:variable name="lang.provincia_repre" select="'Provincia'"/>
	<xsl:variable name="lang.c_postal_repre" select="'Código Postal'"/>
	<xsl:variable name="lang.region_repre" select="'Región / País'"/>
	<xsl:variable name="lang.movil_repre" select="'Teléfono'"/>
	<xsl:variable name="lang.d_email_repre" select="'Correo electrónico'"/>	


	<xsl:variable name="lang.ayuntamiento" select="'Nombre del Ayuntamiento'"/>	
	<xsl:variable name="lang.nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.representante" select="'Nombre de la Empresa'"/>
	<xsl:variable name="lang.cif" select="'CIF'"/>
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>	
	
	<xsl:variable name="lang.cpv" select="'CPV:* (seleccionar)'"/>
	<xsl:variable name="lang.convocatoria" select="'Expedientes de Contratación:* (seleccionar)'"/>
	
	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>
	
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.anexarInfo3" select="'(Máximo 7 MB total)'"/>
	<xsl:variable name="lang.documento1" select="'Documento DOC'"/>
	<xsl:variable name="lang.documento2" select="'Documento JPG'"/>
	<xsl:variable name="lang.documento3" select="'Documento PDF'"/>
	<xsl:variable name="lang.documento4" select="'Documento ZIP'"/>
	<xsl:variable name="lang.documento5" select="'Documento XSIG'"/>
	
	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(6);
			
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');
			
			validar[2] = new Array('domicilioNotificacion','<xsl:value-of select="$lang.direccion"/>');
			validar[3] = new Array('localidad','<xsl:value-of select="$lang.localidad"/>');
			validar[4] = new Array('provincia','<xsl:value-of select="$lang.provincia"/>');
			validar[5] = new Array('codigoPostal','<xsl:value-of select="$lang.cp"/>');
			
			//validar[6] = new Array('nif_repre','<xsl:value-of select="$lang.nif_repre"/>');
			//validar[7] = new Array('nombre_repre','<xsl:value-of select="lang.nombre_repre"/>');


			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(16);
			
			especificos[0] = new Array('ayuntamiento','ayuntamiento');
			especificos[1] = new Array('cif','cif');
			especificos[2] = new Array('domicilioNotificacion','domicilioNotificacion');
			especificos[3] = new Array('localidad','localidad');
			especificos[4] = new Array('provincia','provincia');
			especificos[5] = new Array('codigoPostal','codigoPostal');
			especificos[6] = new Array('telefono','telefono');			
			especificos[7] = new Array('convocatoria','convocatoria');			
			especificos[8] = new Array('expone','expone');
			especificos[9] = new Array('solicita','solicita');	
			especificos[10] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[11] = new Array('nombreSolicitante','nombreSolicitante');
			especificos[12] = new Array('emailSolicitante','emailSolicitante');
			especificos[13] = new Array('cpv','cpv');

			especificos[14] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[15] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');

			
			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				document.getElementById('nombreSolicitante').value = document.getElementById('nombreSolicitante').value;
				document.getElementById('documentoIdentidad').value = document.getElementById('documentoIdentidad').value;
				//document.getElementById('domicilioNotificacion').value = document.getElementById('domicilio_repre').value;
				//document.getElementById('localidad').value = document.getElementById('ciudad_repre').value;

				if(document.getElementById('convocatoria').value=='-------'){
					alert('Debe seleccionar una convocatoria');
					document.getElementById('convocatoria').focus();
					return false;
				}

				return true;
			}	
			
			function getDatos(cpv){
				//alert(cpv.options[cpv.selectedIndex].text);
				//alert(cpv.options[cpv.selectedIndex].value);
				window.open('tramites/001/CONTRAT_MENORES/expedientesCPV.jsp?valor='+cpv.options[cpv.selectedIndex].text+';001','','width=3,height=3,left=10000,top=10000');
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
   			<h1><xsl:value-of select="$lang.datosInteresado"/></h1>
   		</div>

   		<div class="cuadro" style="">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.cif"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:100px; </xsl:attribute>
					<xsl:attribute name="name">cif</xsl:attribute>
					<xsl:attribute name="id">cif</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/cif"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.representante"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:490px; </xsl:attribute>
					<xsl:attribute name="name">ayuntamiento</xsl:attribute>
					<xsl:attribute name="id">ayuntamiento</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ayuntamiento"/></xsl:attribute>
				</input>
			</div>
			<div class="col" style="height: 35px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.direccion"/>:*	
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">domicilioNotificacion</xsl:attribute>
					<xsl:attribute name="id">domicilioNotificacion</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Domicilio_Notificacion"/></xsl:attribute>
				</input>
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
					<xsl:attribute name="value"></xsl:attribute>
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
					<xsl:attribute name="style">width:70px; </xsl:attribute>
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
			<xsl:variable name="filecpv" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosConsulta('CPV_CONT_MENOR','select value,value from CONTRATACION_CMENOR_CPV_S order by value','001')"/>
			<xsl:variable name="cpvVariable" select="document($filecpv)"/>
	   		<div class="col">
					<label class="gr">
						<xsl:attribute name="style">width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.cpv"/>:
					</label>
					<select onchange="getDatos(cpv)">
						<xsl:attribute name="style">color:#006699;</xsl:attribute>
						<xsl:attribute name="name">cpv</xsl:attribute>
						<xsl:attribute name="id">cpv</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/cpv"/></xsl:attribute>
						<xsl:for-each select="$cpvVariable/listado/dato">
							<option>
								<xsl:attribute name="style">position: relative; width:490px;</xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
								<xsl:value-of select="sustituto"/>
							</option>
						</xsl:for-each>
					</select>					
					<br/>	
			</div>	
	    
			<div class="col">
		   		<b><xsl:value-of select="$lang.convocatoria"/></b>				
				<tb/>
				<select class="gr">
					<xsl:attribute name="style">border:none; width:650px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">convocatoria</xsl:attribute>
					<xsl:attribute name="id">convocatoria</xsl:attribute>
					<xsl:attribute name="onblur"> document.getElementById('asunto').value = this.options[this.selectedIndex].text; document.getElementById('numExpediente').value = this.value;</xsl:attribute>
						<option>
							<xsl:attribute name="value">-------</xsl:attribute>----------------------------------------------------
						</option>
				</select>
			</div>	
		</div>  		

   		<div class="cuadro" style="">	
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
						<xsl:value-of select="Datos_Registro/datos_especificos/solicita"/>
					</textarea>
				</div>
				<script>
				document.getElementById('expone').value = 'Que enterado de la tramitación por la Diputación Provincial de Ciudad Real del expediente de contratación arriba indicada, en este acto manifiesto la voluntad del licitador que se señala de participar en ese procedimiento. '+document.getElementById('expone').value ;
				document.getElementById('solicita').value = 'Que se tenga por presentada la oferta económica que se anexa. '+document.getElementById('solicita').value ;
				</script>
			</div>
		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN" />
   		<br/>		
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>	
   		</div>
   		<br/>
   		<div class="cuadro" style="">
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
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">DPCR_SRSD1</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">DPCR_SRSD1_Tipo</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD1_Tipo</xsl:attribute>
					<xsl:attribute name="value">doc, odt</xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento2"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">DPCR_SRSD2</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">DPCR_SRSD2_Tipo</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD2_Tipo</xsl:attribute>
					<xsl:attribute name="value">jpg,jpeg,gif,tif,tiff,bmp</xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento3"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">DPCR_SRSD3</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD3</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">DPCR_SRSD3_Tipo</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD3_Tipo</xsl:attribute>
					<xsl:attribute name="value">pdf</xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento4"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">DPCR_SRSD4</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD4</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">DPCR_SRSD4_Tipo</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD4_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
			</div>
			<div class="col">
	  			<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento5"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">DPCR_SRSD5</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD5</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">DPCR_SRSD5_Tipo</xsl:attribute>
					<xsl:attribute name="id">DPCR_SRSD5_Tipo</xsl:attribute>
					<xsl:attribute name="value">xsig</xsl:attribute>
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
	</xsl:template>
</xsl:stylesheet>
