<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../TRAM_COMUNES/templates_comunes.xsl" />

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'Formulario de solicitud de Comunicaciones Previa de Obra'"/>

	<xsl:include href="../TRAM_COMUNES/lang_comunes_es.xsl" />

	<xsl:variable name="lang.expone1" select="'EXPONE'"/>
	<xsl:variable name="lang.expone2" select="'Que teniendo interés en la ejecución de las obras que se indican y conocimiento de la obligación legal de comunicarlo previamente a la Administración a los efectos legales'"/>
	<xsl:variable name="lang.solicita1" select="'SOLICITA'"/>
	<xsl:variable name="lang.solicita2" select="'Que teniendo por presentado el presente escrito, con la documentación que al mismo se acompaña, lo admita y previos los trámites pertinentes, se tenga por comunicada la intención de la realización de las referidas obras'"/>
	<xsl:variable name="lang.finalidad" select="'Finalidad'"/>
	<xsl:variable name="lang.ejemplo" select="'Por ejemplo'"/>
	<xsl:variable name="lang.solicita_defecto" select="'Sustitución del alicatado del aseo en C) .............., nº ...., con un presupuesto de ejecución de .............. euros.'"/>
	<xsl:variable name="lang.anexar_presupuesto" select="'Debe anexar como adjunto el presupuesto final del contratista'"/>

	<xsl:variable name="doc.docodt" select="'COMPREVIA_D1'"/>
	<xsl:variable name="doc.pdf" select="'COMPREVIA_D2'"/>
	<xsl:variable name="doc.jpg" select="'COMPREVIA_D3'"/>
	<xsl:variable name="doc.zip" select="'COMPREVIA_D4'"/>

		
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

			validar[6] = new Array('finalidad','<xsl:value-of select="$lang.finalidad"/>');


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

			especificos[18] = new Array('finalidad','Finalidad');

			especificos[19] = new Array('admin','Admin');
			
			especificos[20] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[21] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');
			

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

			<b><xsl:value-of select="$lang.solicita1"/></b> <br/>
			<p style="text-align:justify; margin-right:10px"><xsl:value-of select="$lang.solicita2"/>:</p>

	   		<br/>

			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:120px;</xsl:attribute>
					(*) <xsl:value-of select="$lang.finalidad"/>:*
				</label>
				<textarea type="gr">
					<xsl:attribute name="style">position: relative; width:520px; font:normal 110%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">finalidad</xsl:attribute>
					<xsl:attribute name="id">finalidad</xsl:attribute>
					<xsl:attribute name="rows">6</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/Finalidad"/>
				</textarea>
			</div>

			<br/>

			<p style="text-align:justify; color:grey; margin-right:10px"><i>
				(*) <xsl:value-of select="$lang.ejemplo"/>: 
				<xsl:value-of select="$lang.solicita_defecto"/>
			</i></p>
			
			<p style="text-align:justify; color:grey; margin-right:10px">
				<b><i>(**) <xsl:value-of select="$lang.anexar_presupuesto"/></i></b>
			</p>
	   		
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
