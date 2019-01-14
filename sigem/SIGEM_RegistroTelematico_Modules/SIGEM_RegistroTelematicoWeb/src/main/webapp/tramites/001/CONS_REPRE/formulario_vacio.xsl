<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'CONSULTA DE ENTIDADES REPRESENTADAS'"/>
	
	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>

	<xsl:variable name="lang.listaRepresentados" select="'Lista de representados'"/>

	<xsl:variable name="lang.mensajeRegistro" select="'Si necesita la obtención de esta información sellada por la Corporación pulse aceptar en los siguientes pasos.'"/>

	<xsl:variable name="lang.mensajePopUps" select="'Si está viendo este mensaje debe &lt;b&gt;deshabilitar&lt;/b&gt; el bloqueador de elementos emergentes de su navegador para &lt;b&gt;permitir los PopUps en Diputación de Ciudad Real&lt;/b&gt;.'"/>
	<xsl:variable name="lang.mensajeAlert" select="'Debe deshabilitar el bloqueador de elementos emergentes de su navegador para permitir los PopUps en Diputación de Ciudad Real y poder comprobar sus representados antes de continuar.'"/>
		
	<xsl:template match="/"  xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(2);
			
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');

			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(7);
			
			especificos[0] = new Array('documentoIdentidad','documentoIdentidad');
			especificos[1] = new Array('nombreSolicitante','nombreSolicitante');
			especificos[2] = new Array('emailSolicitante','emailSolicitante');
			especificos[3] = new Array('asociaciones_xsd','asociaciones_xsd');
			especificos[4] = new Array('asociaciones_jasper','asociaciones_jasper');

			especificos[5] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[6] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');

						
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				if(document.getElementById('asociaciones_jasper').innerHTML == ''){
					alert('<xsl:value-of select="$lang.mensajeAlert"/>');
					return false;
				}

				return true;
			}		

			function getRepresentados(){
				var left = (screen.width - 500) / 2;
			        var top = (screen.height - 40) / 4;
				window.open('tramites/001/CONS_REPRE/dameRepresentantes.jsp?valor='+document.getElementById('documentoIdentidad').value+';001','','width=500px,height=40,left=' + left + ',top=' + top);
			}

		</script>

		<h1><xsl:value-of select="$lang.titulo"/></h1>   	
   		<div class="cuadro" >	
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
   			<h1><xsl:value-of select="$lang.listaRepresentados"/></h1>
   		</div>   		
   		<div class="cuadro" >	
			<div class="col">
				<img>
					<xsl:attribute name="src">img/close.png</xsl:attribute>
					<xsl:attribute name="id">aviso_img</xsl:attribute>
				</img>
				<label>					
					<xsl:attribute name="style">color:#006699; width:100%; line-height:15px; padding-left:20px;</xsl:attribute>
					<xsl:attribute name="name">asociaciones_lbl</xsl:attribute>
					<xsl:attribute name="id">asociaciones_lbl</xsl:attribute>
				</label>					
			</div>
			<br/>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; padding-left:20px;</xsl:attribute>
					<xsl:value-of select="$lang.mensajeRegistro"/>
				</label>
			</div> 			
		</div>
		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN" />
		<br/>
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

		<textarea>
			<xsl:attribute name="style">display:none;</xsl:attribute>
			<xsl:attribute name="name">asociaciones_xsd</xsl:attribute>
			<xsl:attribute name="id">asociaciones_xsd</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/asociaciones_xsd"/></xsl:attribute>
		</textarea>
   		<textarea>
			<xsl:attribute name="style">display:none;</xsl:attribute>
			<xsl:attribute name="name">asociaciones_jasper</xsl:attribute>
			<xsl:attribute name="id">asociaciones_jasper</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/asociaciones_jasper"/></xsl:attribute>
		</textarea>

		<script language="Javascript">
			document.getElementById('asociaciones_lbl').innerHTML = '<xsl:value-of select="$lang.mensajePopUps"/>';
			document.getElementById('asociaciones_xsd').innerHTML = '';
			document.getElementById('asociaciones_jasper').innerHTML = '';
			getRepresentados();
		</script>
	</xsl:template>
</xsl:stylesheet>
