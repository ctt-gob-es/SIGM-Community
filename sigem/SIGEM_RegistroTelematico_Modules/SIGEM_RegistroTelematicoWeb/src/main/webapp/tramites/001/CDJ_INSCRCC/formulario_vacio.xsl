<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="templates_comunes.xsl" />

<xsl:include href="../templates_comunes.xsl" /><xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'SOLICITUD DE INSCRIPCION EN LA GUÍA CULTURAL'"/>
	
	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>
	<xsl:variable name="lang.cargo" select="'Cargo*'"/>

	<xsl:variable name="lang.datosRepresentante" select="'Datos del responsable del proyecto/actividad'"/>
	<xsl:variable name="lang.datosInteresado" select="'Datos de la Entidad Local solicitante'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	<xsl:variable name="lang.datosDeclarativos" select="'Declaraciones Preceptivas que formula el solicitante'"/>
	
	<xsl:variable name="lang.nif_repre" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre_repre" select="'Apellidos y Nombre o Denominación Social'"/>
	<xsl:variable name="lang.domicilio_repre" select="'Domicilio'"/>
	<xsl:variable name="lang.ciudad_repre" select="'Localidad'"/>
	<xsl:variable name="lang.c_postal_repre" select="'Código Postal'"/>
	<xsl:variable name="lang.region_repre" select="'Región / País'"/>
	<xsl:variable name="lang.movil_repre" select="'Teléfono'"/>
	<xsl:variable name="lang.d_email_repre" select="'Correo electrónico'"/>	

	<xsl:variable name="lang.convocatoria" select="'Convocatorias abiertas:* (seleccionar)'"/>
	<xsl:variable name="lang.convocatoriaObligatoria" select="'Convocatorias'"/>

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

	<xsl:variable name="lang.finalidad" select="'Finalidad (Obra, Suministro o Actividad)'"/>	
	<xsl:variable name="lang.presupuesto" select="'Presupuesto Total Estimado (xxxx.xx)'"/>
	<xsl:variable name="lang.subvencion" select="'Subvención que se solicita (xxxx.xx)'"/>
	<xsl:variable name="lang.otrasSubvenciones" select="'Otras subvenciones solicitadas y/o concedidas para la actividad, servicio y/o inversión'"/>
	<xsl:variable name="lang.fecha" select="'Fecha prevista de realización actividad / suministro / terminación de las obras'"/>
	
	
	<xsl:variable name="lang.expone" select="'Expone'"/>
	<xsl:variable name="lang.solicita" select="'Solicita'"/>
	<xsl:variable name="lang.declaro" select="'Asimismo, declaro bajo mi Responsabilidad (obligatorio marcar con una x cada cuadro para poder seguir el trámite)'"/>
	
	<xsl:variable name="lang.declaro1" select="'Que al día de la fecha, esta entidad se encuentra al corriente en el cumplimiento de sus obligaciones tributarias y frente a la Seguidad Social, así como de sus obligaciones fiscales con la Excma. Diputación Provincial de Ciudad Real, no es deudora por resolución de procedencia de reintegro de subvenciones, y autorizo a la Diputación Provincial para la obtención de los certificados de la Agencia Estatal de la Administración Tributaria y de la Tesorería Territorial de la Seguridad de estar al corriente en el cumplimiento de dichas obligaciones.'"/>
	<xsl:variable name="lang.declaro2" select="'Que no me encuentro/esta entidad no se encuentra incursa en ninguna de las circunstancias de prohibición para la obtención de la condición de beneficiario de ayuda o subvención, previstas en el artículo 13 de la Ley General de Subvenciones, de 17 de noviembre de 2003.'"/>
	<xsl:variable name="lang.declaro3" select="'Que la persona o entidad solicitante autoriza expresamente a la Diputación de Ciudad Real para consultar las expresadas circunstancias ante las entidades señaladas.'"/>	

	<xsl:variable name="lang.anexar" select="'DOCUMENTACIÓN QUE SE ACOMPAÑA'"/>
	<!--<xsl:variable name="lang.anexarInfo1" select="'1.- Memoria descriptiva y valorada económicamente del proyecto, programa, servicio o actividad a subvencionar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Certificación de aprobación de la Memoria por el órgano competente de la entidad local.'"/>
	<xsl:variable name="lang.anexarInfo3" select="'3.- Para adjuntar los ficheros, pulse el botón examinar. Seleccione el fichero que desee anexar a la solicitud. Recuerde que debe anexar copia de la Memoria o proyecto al que se refiere la presente solicitud y de cualquier otro que considere conveniente. Si el documento está en soporte papel, debe escanearlo con carácter previo. Los originales quedan a disposición de la Diputación Provincial durante toda la tramitación del expediente.'"/>-->

	<xsl:variable name="lang.anexarInfo1" select="'1.- Debe anexarse la documentación establecida en la convocatoria.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Para adjuntar los ficheros, pulse el botón examinar. Seleccione el fichero que desee anexar a la solicitud. Recuerde que debe anexar copia de la Memoria o proyecto al que se refiere la presente solicitud y de cualquier otro que considere conveniente. Si el documento está en soporte papel, debe escanearlo con carácter previo. Los originales quedan a disposición de la Diputación Provincial durante toda la tramitación del expediente.'"/>

	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>
	<xsl:variable name="lang.documentoTipoXSIG" select="'Archivo XSIG'"/>
	<xsl:variable name="lang.documentoTipo_DOC_ODT" select="'Archivo ODT/DOC'"/>
	<xsl:variable name="lang.documentoTipoPDF" select="'Archivo PDF'"/>
	<xsl:variable name="lang.documentoTipoJPEG" select="'Archivo JPEG'"/>
	
		
	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(14);
			
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');
			
			validar[2] = new Array('domicilioNotificacion','<xsl:value-of select="$lang.direccion"/>');
			validar[3] = new Array('localidad','<xsl:value-of select="$lang.localidad"/>');
			validar[4] = new Array('provincia','<xsl:value-of select="$lang.provincia"/>');
			validar[5] = new Array('codigoPostal','<xsl:value-of select="$lang.cp"/>');

			validar[6] = new Array('convocatoria','<xsl:value-of select="$lang.convocatoriaObligatoria"/>');
			validar[7] = new Array('finalidad', '<xsl:value-of select="$lang.finalidad"/>');
			validar[8] = new Array('presupuesto','<xsl:value-of select="$lang.presupuesto"/>');
			validar[9] = new Array('subvencion','<xsl:value-of select="$lang.subvencion"/>');
			validar[10] = new Array('fecha','<xsl:value-of select="$lang.fecha"/>');
			
			validar[11] = new Array('nif_repre','<xsl:value-of select="$lang.nif_repre"/>');
			validar[12] = new Array('nombre_repre','<xsl:value-of select="lang.nombre_repre"/>');
			validar[13] = new Array('cargo','Cargo');


			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(30);
			
			especificos[0] = new Array('ayuntamiento','ayuntamiento');
			especificos[1] = new Array('domicilioNotificacion','domicilioNotificacion');
			especificos[2] = new Array('localidad','localidad');
			especificos[3] = new Array('provincia','provincia');
			especificos[4] = new Array('codigoPostal','codigoPostal');
			especificos[5] = new Array('telefono','telefono');
			
			especificos[6] = new Array('convocatoria','convocatoria');

			especificos[7] = new Array('finalidad','finalidad');
			especificos[8] = new Array('presupuesto','presupuesto');
			especificos[9] = new Array('subvencion','subvencion');
			especificos[10] = new Array('otrasSubvenciones','otrasSubvenciones');
			especificos[11] = new Array('fecha','fecha');
						
			especificos[12] = new Array('expone','expone');
			especificos[13] = new Array('solicita','solicita');
			
			
			especificos[14] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[15] = new Array('nombreSolicitante','nombreSolicitante');
			especificos[16] = new Array('emailSolicitante','emailSolicitante');
			
			especificos[17] = new Array('cif','cif');
			
			especificos[18] = new Array('declaro1','declaro1');
			especificos[19] = new Array('declaro2','declaro2');
			especificos[20] = new Array('declaro3','declaro3');
			
			especificos[21] = new Array('nif_repre','nif_repre');
			especificos[22] = new Array('nombre_repre','nombre_repre');
			especificos[23] = new Array('domicilio_repre','domicilio_repre');
			especificos[24] = new Array('ciudad_repre','ciudad_repre');
			especificos[25] = new Array('c_postal_repre','c_postal_repre');
			especificos[26] = new Array('region_repre','region_repre');
			especificos[27] = new Array('movil_repre','movil_repre');
			especificos[28] = new Array('d_email_repre','d_email_repre');

			especificos[29] = new Array('cargo','cargo');
			
			<xsl:call-template name="VALIDACIONES" />
			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				document.getElementById('nombreSolicitante').value = document.getElementById('nombreSolicitante').value;
				document.getElementById('documentoIdentidad').value = document.getElementById('documentoIdentidad').value;
//				document.getElementById('domicilioNotificacion').value = document.getElementById('domicilio_repre').value;
				document.getElementById('localidad').value = document.getElementById('ciudad_repre').value;

				if(document.getElementById('convocatoria').value=='-------'){
					alert('Debe seleccionar una convocatoria');
					document.getElementById('convocatoria').focus();
					return false;
				}

				if(document.getElementById('presupuesto').value != ''){
					if(!numerico(document.getElementById('presupuesto').value)){
						alert('El campo Presupuesto Total Estimado debe ser numérico');
						document.getElementById('presupuesto').focus();
						return false;
					}
				}

				if(document.getElementById('subvencion').value != ''){
					if(!numerico(document.getElementById('subvencion').value)){
						alert('El campo Subvención que se solicita debe ser numérico');
						document.getElementById('subvencion').focus();
						return false;
					}
				}

				return true;
			}
			function getDatosObligado(nif){
				window.open('tramites/001/CONVSUB_01/buscaObligado.jsp?valor='+nif+';001','','width=3,height=3');
			}
			
			function abrirDocumento(){ 
				v=window.open("documentos/MODELO.odt"); 
			} 
			function getDatosAyto(valueAyto){
				document.getElementById('convocatoria').value = '-------';
				window.open('tramites/001/ayuntamiento.jsp?valor='+valueAyto+';001','','width=3,height=3,left=10000,top=10000');
			}	
			function compruebaSolicitud(valueConvocatoria){
				var selectAyto = document.getElementById('ayuntamiento');
				var valueAyto = selectAyto.options[selectAyto.selectedIndex].text;
				if(valueAyto == '' || valueAyto == '-------------------------'){
					alert('Debe introducir una localidad válida');
					document.getElementById('convocatoria').value = '-------';
					selectAyto .focus();
				}
				else{
					window.open('tramites/001/CONVSUB_01/compruebaSol.jsp?valor='+document.getElementById('cif').value+';001;'+valueConvocatoria,'','width=3,height=3,left=10000,top=10000');
				}
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
					<xsl:value-of select="$lang.cargo"/>:
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
				<img onclick="getDatosObligado(document.getElementById('nif_repre').value);" src="img/search-mg.gif"/>
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
			<xsl:variable name="fileAyuntam" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosTablaValidacionConSustituto('REC_VLDTBL_MUNICIPIOS','001')"/>
	
			<!-- <xsl:variable name="fileAyuntam" select="java:es.dipucr.sigem.api.registroTelematico.XmlAyuntamiento.getAyuntamientos()"/> -->
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/cif"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col" >
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

		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
   		<div class="cuadro" style="">	
	   		<b><xsl:value-of select="$lang.convocatoria"/></b>				
			<xsl:variable name="convocatoria" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosConsulta('Convocatoria', 'select numexp, asunto from spac_expedientes where codprocedimiento IN ( select valor from DPCR_CONV_EXP_CONV) and fcierre is null and estadoadm=#PR#','001')"/>
			<xsl:variable name="convocatoria" select="document($convocatoria)"/>
			<tb/>
			<select class="gr">
				<xsl:attribute name="style">border:none; width:650px;color:#006699;</xsl:attribute>
				<xsl:attribute name="name">convocatoria</xsl:attribute>
				<xsl:attribute name="id">convocatoria</xsl:attribute>
				<xsl:attribute name="onchange">compruebaSolicitud(this.value);</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/convocatoria"/></xsl:attribute>
				<option>
					<xsl:attribute name="value">-------</xsl:attribute>----------------------------------------------------</option>
				<xsl:for-each select="$convocatoria/listado/dato">
					<option>
						<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
						<xsl:value-of select="sustituto"/>
					</option>
				</xsl:for-each>				
			</select>
		</div>  		

   		<div class="cuadro" style="">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%;</xsl:attribute>
					<xsl:value-of select="$lang.finalidad"/>:*
				</label>
				<br/>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:90%; </xsl:attribute>
					<xsl:attribute name="name">finalidad</xsl:attribute>
					<xsl:attribute name="id">finalidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/finalidad"/></xsl:attribute>
				</input>
			</div>
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%;</xsl:attribute>
					<xsl:value-of select="$lang.presupuesto"/>:*
				</label>
				<br/>
				<input type="text">
					<xsl:attribute name="style">width:150px; </xsl:attribute>
					<xsl:attribute name="name">presupuesto</xsl:attribute>
					<xsl:attribute name="id">presupuesto</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/presupuesto"/></xsl:attribute>
				</input> €

			</div>
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%;</xsl:attribute>
					<xsl:value-of select="$lang.subvencion"/>:*
				</label>
				<br/>
				<input type="text">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:attribute name="name">subvencion</xsl:attribute>
					<xsl:attribute name="id">subvencion</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/subvencion"/></xsl:attribute>
				</input> €
			</div>	

			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%;</xsl:attribute>
					<xsl:value-of select="$lang.otrasSubvenciones"/>:
				</label>
				<br/>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:90%; </xsl:attribute>
					<xsl:attribute name="name">otrasSubvenciones</xsl:attribute>
					<xsl:attribute name="id">otrasSubvenciones</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/otrasSubvenciones"/></xsl:attribute>
				</input>
			</div>	
			
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%;</xsl:attribute>
					<xsl:value-of select="$lang.fecha"/>:*
				</label>
				<br/>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:150px; </xsl:attribute>
					<xsl:attribute name="name">fecha</xsl:attribute>
					<xsl:attribute name="id">fecha</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/fecha"/></xsl:attribute>
				</input>
			</div>
			<br/>
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
document.getElementById('expone').value = 'Que el ayuntamiento mencionado tiene interés en participar en la convocatoria. '+document.getElementById('expone').value ;
document.getElementById('solicita').value = 'Que se conceda la más alta subvención posible. '+document.getElementById('solicita').value ;
</script>
			
			</div>
			<div class="submenu">
   				<h1><xsl:value-of select="$lang.datosDeclarativos"/></h1>
   			</div>
   			<div class="cuadro" style="">	
				<input type="checkbox" id="declaro1" checked="Yes" style="border:none;color:#006699;position: relative; width:20px;" DISABLED="Yes"/>
				<xsl:value-of select="$lang.declaro1"/>
				
				<br/>
				
				<input type="checkbox" id="declaro2" checked="Yes" style="border:none;color:#006699;position: relative; width:20px;" DISABLED="Yes"/>
				<xsl:value-of select="$lang.declaro2"/>
				<br/>
				
				<input type="checkbox" id="declaro3" checked="Yes" style="border:none;color:#006699;position: relative; width:20px;" DISABLED="Yes"/>
				<xsl:value-of select="$lang.declaro3"/>
				<br/>
			</div>
		
   			<br/>
			<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   			</div>
   			<div class="cuadro" style="">
				<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.anexarInfo1"/>
		   			<!-- <a style="text-decoration:underline; color:red" href="javascript:abrirDocumento();">Modelo orientativo de acuerdo</a><br/>  -->
		   			<br/>
		   			<xsl:value-of select="$lang.anexarInfo2"/><br/>
					<!--<xsl:value-of select="$lang.anexarInfo3"/><br/>-->
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
					Los datos personales, identificativos y de contacto, aportados mediante esta comunicación se entienden facilitados voluntariamente, y serán incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del ámbito de las competencias de esta Administración Pública así como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telemáticos y enviarle invitaciones para eventos y felicitaciones en fechas señaladas. Entenderemos que presta su consentimiento tácito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podrá ejercer sus derechos de acceso, rectificación, cancelación y oposición ante el Responsable del Fichero, la Diputación Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - España, siempre acreditando conforme a Derecho su identidad en la comunicación. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigiéndose a la dirección citada ut supra o bien al correo electrónico lopd@dipucr.es o bien por telefono al número gratuito 900 714 080.	
				</label>
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
