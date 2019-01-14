<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../TRAM_COMUNES/templates_comunes.xsl" />

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'Formulario de solicitud de Actividades Comerciales'"/>

	<xsl:include href="../TRAM_COMUNES/lang_comunes_es.xsl" />

	<xsl:variable name="lang.expone1" select="'EXPONE'"/>
	<xsl:variable name="lang.expone2" select="'Que de conformidad con lo dispuesto en la Ley 12/2012, de 26 de diciembre, de medidas urgentes de liberalización del Comercio y de determinados servicios, y en la Ley 17/2009, de 23 de noviembre, sobre el libre acceso a las actividades de servicios y su ejercicio (BOE del día 24-11-2009), pone en conocimiento de ese Ayuntamiento que se va a iniciar el ejercicio de la actividad que se indica.'"/>
	<xsl:variable name="lang.actividad" select="'Descripción de la Actividad'"/>
	<xsl:variable name="lang.emplazamiento" select="'Emplazamiento de la actividad'"/>
	<xsl:variable name="lang.obras" select="'Descripcion de las obras (en su caso)'"/>
	<xsl:variable name="lang.importe" select="'Importe aprox. del presupuesto de obras (si procede)'"/>
	<xsl:variable name="lang.m2via" select="'Ocupación aprox. de la vía pública en m² (si procede)'"/>
	<xsl:variable name="lang.otrasActuaciones" select="'Otras actuaciones (en su caso)'"/>
	<xsl:variable name="doc.docodt" select="'ACTCOM_D1'"/>
	<xsl:variable name="doc.pdf" select="'ACTCOM_D2'"/>
	<xsl:variable name="doc.jpg" select="'ACTCOM_D3'"/>
	<xsl:variable name="doc.zip" select="'ACTCOM_D4'"/>

		
	<xsl:template match="/" xmlns:java="http://xml.apache.org/xslt/java">

		<script language="Javascript">

			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(7);

			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');

			validar[2] = new Array('nif', '<xsl:value-of select="$lang.nif"/>');
			validar[3] = new Array('nombre','<xsl:value-of select="$lang.nombre"/>');
			validar[4] = new Array('calle','<xsl:value-of select="$lang.calle"/>');
			validar[5] = new Array('c_postal','<xsl:value-of select="$lang.c_postal"/>');

			validar[6] = new Array('actividad','<xsl:value-of select="$lang.actividad"/>');


			//Array con los datos especificos del formulario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(22);
			
			especificos[0] = new Array('nif','nif');
			especificos[1] = new Array('nombre','nombre');
			especificos[2] = new Array('calle','calle');
			especificos[3] = new Array('ciudad','ciudad');
			especificos[4] = new Array('region','region');
			especificos[5] = new Array('rcalle','rcalle');
			especificos[6] = new Array('c_postal','c_postal');
			especificos[7] = new Array('movil','movil');
			especificos[8] = new Array('d_email','d_email');
			especificos[9] = new Array('repres_nif','repres_nif');
			especificos[10] = new Array('repres_nombre','repres_nombre');
			especificos[11] = new Array('repres_movil','repres_movil');
			especificos[12] = new Array('repres_d_email','repres_d_email');
			especificos[13] = new Array('rciudad','rciudad');
			especificos[14] = new Array('rregion','rregion');
			especificos[15] = new Array('rc_postal','rc_postal');

			especificos[16] = new Array('nombresolihidden','nombresolihidden');
			especificos[17] = new Array('nifsolihidden','nifsolihidden');

			especificos[18] = new Array('actividad','Actividad');
			especificos[19] = new Array('emplazamiento','Emplazamiento');
			especificos[20] = new Array('obras','Obras');
			especificos[21] = new Array('importe','Importe');
			especificos[22] = new Array('m2via','M2Via');
			especificos[23] = new Array('otrasActuaciones','OtrasActuaciones');

			especificos[24] = new Array('admin','Admin');
			
			especificos[25] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[26] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');
			

			<xsl:call-template name="VALIDACIONES" />
			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			
			function verificacionesEspecificas() {

				document.getElementById('nombresolihidden').value = document.getElementById('nombreSolicitante').value;
				document.getElementById('nifsolihidden').value = document.getElementById('documentoIdentidad').value;

				var admin = '<xsl:value-of select="Datos_Registro/datos_especificos/Admin"/>';
				if (admin == 'true'){
					alert('Recuerde que usted, funcionario actuante, está obrando en representación del ' + 
						'ciudadano o entidad solicitante y va a presentar la solicitud en su nombre.\n\n' + 
						'No olvide escanear y adjuntar la documentación que acredita esta representación.\n\n' +
						'Si ha olvidado adjuntar este documento, pulse el botón \"Corregir\" en el siguiente formulario y adjuntelo.');
				}
				
				if(document.getElementById('importe').value != ''){
					if(!numerico(document.getElementById('importe').value)){
						alert('El campo Importe de las obras debe ser numérico');
						document.getElementById('importe').focus();
						return false;
					}
				}
			}
			
		</script>

		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
		<xsl:call-template name="DATOS_SOLICITANTE" />
		<xsl:call-template name="DATOS_INTERESADO_REPRESENTANTE" />
		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>				

   		<div class="cuadro" style="">	

	   		<b><xsl:value-of select="$lang.expone1"/></b> <br/>
			<p style="text-align:justify; margin-right:10px"><xsl:value-of select="$lang.expone2"/></p>
			
			<br/>

			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:120px;</xsl:attribute>
					<xsl:value-of select="$lang.actividad"/>:*
				</label>
				<textarea type="gr">
					<xsl:attribute name="style">position: relative; width:520px; font:normal 110%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">actividad</xsl:attribute>
					<xsl:attribute name="id">actividad</xsl:attribute>
					<xsl:attribute name="rows">4</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/Actividad"/>
				</textarea>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:120px;</xsl:attribute>
					<xsl:value-of select="$lang.emplazamiento"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:520px; </xsl:attribute>
					<xsl:attribute name="name">emplazamiento</xsl:attribute>
					<xsl:attribute name="id">emplazamiento</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Emplazamiento"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:120px;</xsl:attribute>
					<xsl:value-of select="$lang.obras"/>:
				</label>
				<textarea type="gr">
					<xsl:attribute name="style">position: relative; width:520px; font:normal 110%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">obras</xsl:attribute>
					<xsl:attribute name="id">obras</xsl:attribute>
					<xsl:attribute name="rows">4</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/Obras"/>
				</textarea>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:120px;</xsl:attribute>
					<xsl:value-of select="$lang.importe"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:200px; </xsl:attribute>
					<xsl:attribute name="name">importe</xsl:attribute>
					<xsl:attribute name="id">importe</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Importe"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:120px;</xsl:attribute>
					<xsl:value-of select="$lang.m2via"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:200px; </xsl:attribute>
					<xsl:attribute name="name">m2via</xsl:attribute>
					<xsl:attribute name="id">m2via</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/M2Via"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:120px;</xsl:attribute>
					<xsl:value-of select="$lang.otrasActuaciones"/>:
				</label>
				<textarea type="gr">
					<xsl:attribute name="style">position: relative; width:520px; font:normal 110%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">otrasActuaciones</xsl:attribute>
					<xsl:attribute name="id">otrasActuaciones</xsl:attribute>
					<xsl:attribute name="rows">6</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/OtrasActuaciones"/>
				</textarea>
			</div>

			<br/>

			En virtud de lo expresado, <b>DECLARA BAJO SU RESPONSABILIDAD</b>:<br/>
			<p style="text-align:justify; margin-right:10px">
1. Que la actividad y las obras que van a ser desarrolladas no tienen impacto en el patrimonio histórico-artístico o en el uso privativo y ocupación de los bienes de dominio público.<br/>
2. Que las obras a desarrollar no requieren de la redacción de un proyecto de obras de edificación, de conformidad con la Ley 38/1999, de 5 de noviembre, de Ordenación de la Edificación.<br/>
3. Que la actividad se encuentra incluida en el Anexo de la Ley 12/2012, de 26 de diciembre, de medidas urgentes de liberalización del Comercio y de determinados servicios y que su superficie útil de exposición y venta al público no supera los 750 metros cuadrados.<br/>
4. Que se encuentra en posesión de los siguientes documentos:<br/>
<ul>
 <li>Proyecto técnico de obras e instalaciones cuando sea exigible conforme a la normativa correspondiente, firmado por técnico competente de acuerdo con la legislación vigente.</li>
 <li>Justificante de pago del tributo o tributos correspondientes.</li>
</ul>
5. Que la actividad y las obras ejecutadas cumplen con todos los requisitos que resultan exigibles de acuerdo con lo previsto en la legislación vigente, y en particular, entre otras, en las siguientes disposiciones:<br/>
<ul>
<li>Ley 12/2012, de 26 de diciembre, de medidas urgentes de liberalización del Comercio y de determinados servicios.</li>
<li>Ley urbanística autonómica</li>
<li>Otras normas sectoriales aplicables</li>
<li>Ordenanza municipal de licencias</li>
<li>Otras ordenanzas municipales</li>
</ul>
6. Que se compromete a mantener el cumplimiento de la normativa mencionada durante el desarrollo de la actividad y/o ejecución de la obra así como a adaptarse a las modificaciones legales que durante el desarrollo de la actividad y/o ejecución de la obra pudieran producirse.<br/>
7. Que se compromete a conservar la documentación que acredita el cumplimiento de los requisitos exigidos durante el desarrollo de la actividad, así como a su presentación a requerimiento del personal habilitado para su comprobación. <br/>
8. Que en el momento de la apertura del local se cumple con la normativa de prevención contra incendios y se tiene contratado el mantenimiento de las instalaciones de protección contra incendios.<br/>
9. Que se encuentra en posesión de la correspondiente póliza de responsabilidad civil vigente u otro seguro equivalente y al corriente de pago cuando lo exija la normativa sectorial aplicable. 
			</p>

	   		<br/>
	   		
		</div>
   		<br/>
		
   		<xsl:call-template name="TEXTO_LEGAL_COMUN" />
	 	<br/>
   		<xsl:call-template name="ANEXAR_DOCUMENTOS" />
		<br/>
		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN" />
   		<br/>
		<xsl:call-template name="TEXTO_COMPARECE_COMUN" />
   		<br/>
		<xsl:call-template name="HIDDEN_ATTRIBUTES" />

   		<br/>
	
	</xsl:template>
	

</xsl:stylesheet>
