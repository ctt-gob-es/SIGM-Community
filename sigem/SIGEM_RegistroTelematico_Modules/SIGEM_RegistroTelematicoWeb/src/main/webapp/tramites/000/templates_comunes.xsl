<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- <xsl:include href="../templates_comunes.xsl" /> -->

<!-- <xsl:call-template name="DATOS_PRESENTADOR" /> -->
<!-- especificos[xx] = new Array('documentoIdentidad','documentoIdentidad'); -->
<!-- especificos[xx] = new Array('nombreSolicitante','nombreSolicitante'); -->
<!-- <xsl:call-template name="DATOS_PRESENTADOR_RELLENO" /> -->

<!-- <xsl:call-template name="DATOS_SOLICITANTE_REPRESENTANTE" /> -->
<!-- especificos[xx] = new Array('nif','nif'); -->
<!-- especificos[xx] = new Array('nombre','nombre'); -->
<!-- especificos[xx] = new Array('calle','calle'); -->
<!-- especificos[xx] = new Array('ciudad','ciudad'); -->
<!-- especificos[xx] = new Array('c_postal','c_postal'); -->
<!-- especificos[xx] = new Array('region','region'); -->
<!-- especificos[xx] = new Array('movil','movil'); -->
<!-- especificos[xx] = new Array('d_email','d_email'); -->
<!-- especificos[xx] = new Array('repres_nif','repres_nif'); -->
<!-- especificos[xx] = new Array('repres_nombre','repres_nombre'); -->
<!-- especificos[xx] = new Array('rcalle','rcalle'); -->
<!-- especificos[xx] = new Array('rciudad','rciudad); -->
<!-- especificos[xx] = new Array('rc_postal','rc_postal'); -->
<!-- especificos[xx] = new Array('rregion','rregion'); -->
<!-- especificos[xx] = new Array('repres_movil','repres_movil'); -->
<!-- especificos[xx] = new Array('repres_d_email','repres_d_email'); -->
<!-- <xsl:call-template name="DATOS_SOLICITANTE_REPRESENTANTE_RELLENO" /> -->

<!-- <xsl:call-template name="TEXTO_LEGAL_COMUN" /> -->
<!-- especificos[xx] = new Array('texto_legal_comun','texto_legal_comun'); -->
<!-- especificos[xx] = new Array('texto_legal_comun_ck','texto_legal_comun_ck'); -->
<!-- <xsl:call-template name="TEXTO_LEGAL_COMUN_RELLENO" /> -->

<!-- <xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN" /> -->
<!-- especificos[xx] = new Array('texto_datos_personales_comun','texto_datos_personales_comun'); -->
<!-- <xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUNRELLENO" /> -->

<!-- <xsl:call-template name="TEXTO_COMPARECE_COMUN" /> -->

	<xsl:template name="DATOS_PRESENTADOR">
		<xsl:variable name="lang.datosPresentador" select="'Datos del presentador'"/>
		<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
		<xsl:variable name="lang.id_nombre" select="'Nombre'"/>

		<div class="submenu">
			<xsl:variable name="lang.datosPresentador" select="'Datos del presentador'"/>
   			<h1><xsl:value-of select="$lang.datosPresentador"/></h1>
   		</div>

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
	</xsl:template>

	<xsl:template name="DATOS_PRESENTADOR_RELLENO">
		<xsl:variable name="lang.datosPresentador" select="'Datos del presentador'"/>
		<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
		<xsl:variable name="lang.id_nombre" select="'Nombre'"/>

		<div class="submenu">
			<xsl:variable name="lang.datosPresentador" select="'Datos del presentador'"/>
   			<h1><xsl:value-of select="$lang.datosPresentador"/></h1>
   		</div>

   		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.id_nif"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Documento_Identificacion/Numero"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.id_nombre"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Nombre"/>
				</label>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="DATOS_SOLICITANTE_REPRESENTANTE">
		<xsl:variable name="lang.datosObligado" select="'Datos del solicitante'"/>
		<xsl:variable name="lang.nif" select="'NIF/CIF'"/>
		<xsl:variable name="lang.nombre" select="'Apellidos y Nombre o Denominación Social'"/>
		<xsl:variable name="lang.calle" select="'Domicilio'"/>
		<xsl:variable name="lang.c_postal" select="'Código Postal'"/>
		<xsl:variable name="lang.ciudad" select="'Ciudad'"/>
		<xsl:variable name="lang.region" select="'Provincia'"/>
		<xsl:variable name="lang.movil" select="'Número de teléfono móvil'"/>
		<xsl:variable name="lang.d_email" select="'Dirección de correo electrónico'"/>

		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosObligado"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nif"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:100px; </xsl:attribute>
					<xsl:attribute name="name">nif</xsl:attribute>
					<xsl:attribute name="id">nif</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nif"/></xsl:attribute>
				</input>
				<img onclick="getDatosObligado(document.getElementById('nif').value);" src="img/search-mg.gif"/>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nombre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">nombre</xsl:attribute>
					<xsl:attribute name="id">nombre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.calle"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">calle</xsl:attribute>
					<xsl:attribute name="id">calle</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/calle"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.ciudad"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">ciudad</xsl:attribute>
					<xsl:attribute name="id">ciudad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ciudad"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.c_postal"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:60px; </xsl:attribute>
					<xsl:attribute name="name">c_postal</xsl:attribute>
					<xsl:attribute name="id">c_postal</xsl:attribute>
					<xsl:attribute name="maxlength">5</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/c_postal"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.region"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">region</xsl:attribute>
					<xsl:attribute name="id">region</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/region"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.movil"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">movil</xsl:attribute>
					<xsl:attribute name="id">movil</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/movil"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.d_email"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">d_email</xsl:attribute>
					<xsl:attribute name="id">d_email</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/d_email"/></xsl:attribute>
				</input>
			</div>
			<div style="margin-top:20px;margin-bottom:10px;color:#006699;">
				<b>Representante:</b>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nif"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:100px; </xsl:attribute>
					<xsl:attribute name="name">repres_nif</xsl:attribute>
					<xsl:attribute name="id">repres_nif</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/repres_nif"/></xsl:attribute>
				</input>
				<img onclick="getDatosRepresentante(document.getElementById('repres_nif').value);" src="img/search-mg.gif"/>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nombre"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">repres_nombre</xsl:attribute>
					<xsl:attribute name="id">repres_nombre</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/repres_nombre"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.calle"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:490px; </xsl:attribute>
					<xsl:attribute name="name">rcalle</xsl:attribute>
					<xsl:attribute name="id">rcalle</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/rcalle"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.ciudad"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">rciudad</xsl:attribute>
					<xsl:attribute name="id">rciudad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/rciudad"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.c_postal"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:60px; </xsl:attribute>
					<xsl:attribute name="name">rc_postal</xsl:attribute>
					<xsl:attribute name="id">rc_postal</xsl:attribute>
					<xsl:attribute name="maxlength">5</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/rc_postal"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.region"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">rregion</xsl:attribute>
					<xsl:attribute name="id">rregion</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/rregion"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.movil"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">repres_movil</xsl:attribute>
					<xsl:attribute name="id">repres_movil</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/repres_movil"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.d_email"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">repres_d_email</xsl:attribute>
					<xsl:attribute name="id">repres_d_email</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/repres_d_email"/></xsl:attribute>
				</input>
			</div>
		</div>

		<script>
			function getDatosObligado(nif){
				window.open('tramites/000/buscaObligado.jsp?valor=' + nif + ';000', '', 'width=3,height=3');
			}
			function getDatosRepresentante(nif){
				window.open('tramites/000/buscaRepresentante.jsp?valor=' + nif + ';000', '', 'width=3,height=3');
			}
			function validaNIFObligado(){
				var valorNif = document.getElementById('nif');
				var validaNif = valida_nif_cif_nie(valorNif);
				if(validaNif != 1){
					if(validaNif != 2){
						if(validaNif != 3){
							alert('El NIF/CIF del obligado tributario es incorrecto');
							valorNif.focus();
							return false;
						}
					}
				}
				return true;
			}
			function validaNIFRepresentante(){
				var valorNifRepre = document.getElementById('repres_nif');
				if(valorNifRepre.value != null){
					if(valorNifRepre.value != ''){
						var validaNifRepre = valida_nif_cif_nie(valorNifRepre);
						if(validaNifRepre != 1){
							if(validaNifRepre != 2){
								if(validaNifRepre!= 3){
									alert('El NIF/CIF del representante es incorrecto');
									valorNifRepre.focus();
									return false;
								}
							}
						}
					}
				}
				return true;
			}
		</script>
	</xsl:template>

	<xsl:template name="DATOS_SOLICITANTE_REPRESENTANTE_RELLENO">
		<xsl:variable name="lang.datosObligado" select="'Datos del solicitante'"/>
		<xsl:variable name="lang.nif" select="'NIF/CIF'"/>
		<xsl:variable name="lang.nombre" select="'Apellidos y Nombre o Denominación Social'"/>
		<xsl:variable name="lang.calle" select="'Domicilio'"/>
		<xsl:variable name="lang.c_postal" select="'Código Postal'"/>
		<xsl:variable name="lang.ciudad" select="'Ciudad'"/>
		<xsl:variable name="lang.region" select="'Provincia'"/>
		<xsl:variable name="lang.movil" select="'Número de teléfono móvil'"/>
		<xsl:variable name="lang.d_email" select="'Dirección de correo electrónico'"/>

		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosObligado"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.nif"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nif"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.nombre"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.calle"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/calle"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.ciudad"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ciudad"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.c_postal"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/c_postal"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.region"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/region"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.movil"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/movil"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.d_email"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/d_email"/>
				</label>
			</div>
			<div style="margin-top:20px;margin-bottom:10px;color:#006699;">
				<b>Representante:</b>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.nif"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_nif"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.nombre"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_nombre"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.calle"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rcalle"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.ciudad"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rciudad"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.c_postal"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rc_postal"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.region"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rregion"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.movil"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_movil"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:100%; font-weight:bold;</xsl:attribute>
					<xsl:value-of select="$lang.d_email"/>:
				</label>
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_d_email"/>
				</label>
			</div>
		</div>
	</xsl:template>


	<xsl:template name="TEXTO_LEGAL_COMUN">
		<xsl:variable name="lang.autorizacion" select="'Autorización de consulta de datos de carácter personal'"/>
		<xsl:variable name="lang.texto_legal" select="'De conformidad con lo dispuesto en el art. 28.2 de la Ley 39/2015, 1 de Octubre, los interesados no estarán obligados a aportar documentos que hayan sido elaborados por cualquier Administración. En el mismo precepto se indica que se presumirá que la consulta u obtención es autorizada por los interesados salvo que conste en el procedimiento su oposición expresa o la ley especial aplicable requiera consentimiento expreso. En consecuencia, salvo que expresamente se señale otra cosa en el apartado de expone o solicita de este formulario, esta entidad recabará los documentos que sean precisos para la tramitación del expediente a través de sus redes corporativas o mediante consulta a las plataformas de intermediación de datos u otros sistemas electrónicos habilitados al efecto.'"/>

   		<div class="submenu">
			<h1><xsl:value-of select="$lang.autorizacion"/></h1>
		</div>
		<div class="cuadro">
			<xsl:attribute name="style">text-align:justify;</xsl:attribute>
		   	<label class="gr">
				<input type="checkbox">
					<xsl:attribute name="style">width:auto;</xsl:attribute>
					<xsl:attribute name="name">texto_legal_comun_ck</xsl:attribute>
					<xsl:attribute name="id">texto_legal_comun_ck</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/texto_legal_comun_ck"/></xsl:attribute>
				</input>
				<xsl:attribute name="style">width:100%; text-align:justify;</xsl:attribute>
				<xsl:value-of select="$lang.texto_legal"/>
			</label>
		</div>
		<input type="hidden">
			<xsl:attribute name="name">texto_legal_comun</xsl:attribute>
			<xsl:attribute name="id">texto_legal_comun</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="$lang.texto_legal"/></xsl:attribute>
		</input>
	</xsl:template>

	<xsl:template name="TEXTO_LEGAL_COMUN_RELLENO">
		<xsl:variable name="lang.autorizacion" select="'Autorización de consulta de datos de carácter personal'"/>

		<div class="submenu">
			<h1><xsl:value-of select="$lang.autorizacion"/></h1>
		</div>
		<div class="cuadro">
			<xsl:attribute name="style">text-align:justify;</xsl:attribute>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:100%; padding-left:5px; text-align: justify;</xsl:attribute>
					<input type="checkbox">
						<xsl:attribute name="style">width:auto;</xsl:attribute>
						<xsl:attribute name="name">texto_legal_comun_ck</xsl:attribute>
						<xsl:attribute name="id">texto_legal_comun_ck</xsl:attribute>
						<xsl:attribute name="disabled"/>
						<xsl:attribute name="value"><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/texto_legal_comun_ck"/></xsl:attribute>
					</input>
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/texto_legal_comun"/>
				</label>
				<br/>
			</div>
		</div>

		<script>
			var campo_ck = document.getElementById('texto_legal_comun_ck');
			if(campo_ck.value.toUpperCase() != 'NO'){
				campo_ck.checked = true;
			} else {
				campo_ck.checked = false;
			}
		</script>
	</xsl:template>

	<xsl:template name="TEXTO_DATOS_PERSONALES_COMUN">
		<xsl:variable name="lang.texto_datos_personales_comun" select="'Los datos personales, identificativos y de contacto, aportados mediante esta comunicación se entienden facilitados voluntariamente, y serán incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del ámbito de las competencias de esta Administración Pública así como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telemáticos y enviarle invitaciones para eventos y felicitaciones en fechas señaladas. Entenderemos que presta su consentimiento tácito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podrá ejercer sus derechos de acceso, rectificación, cancelación y oposición ante el Responsable del Fichero, la Diputación Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - España, siempre acreditando conforme a Derecho su identidad en la comunicación. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigiéndose a la dirección citada ut supra o bien al correo electrónico lopd@dipucr.es o bien por telefono al numero gratuito 900 714 080.'"/>

		<div class="cuadro">
			<xsl:attribute name="style">text-align:justify;</xsl:attribute>
		   	<label class="gr">
				<xsl:attribute name="style">width:100%; text-align:justify; color:grey;</xsl:attribute>
				<xsl:value-of select="$lang.texto_datos_personales_comun"/>
			</label>
		</div>
		<input type="hidden">
			<xsl:attribute name="name">texto_datos_personales_comun</xsl:attribute>
			<xsl:attribute name="id">texto_datos_personales_comun</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="$lang.texto_datos_personales_comun"/></xsl:attribute>
		</input>
	</xsl:template>

	<xsl:template name="TEXTO_DATOS_PERSONALES_COMUN_RELLENO">
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/texto_datos_personales_comun">
			<div class="col">
				<label class="gr" style="position: relative; width:100%; text-align:justify; color:grey;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/texto_datos_personales_comun"/>
				</label>
				<br/>
			</div>
		</xsl:if>
	</xsl:template>

	<xsl:template name="TEXTO_COMPARECE_COMUN">
		<xsl:variable name="lang.Comparece00" select="'Información sobre Notificaciones Electrónicas'"/>
		<xsl:variable name="lang.Comparece10" select="'QUEREMOS SER MÁS ÁGILES Y CONTESTARLE CON RAPIDEZ'"/>
		<xsl:variable name="lang.Comparece20" select="'Puede acceder a la notificación electrónica de las resoluciones que sean de su interés a través de:'"/>
		<xsl:variable name="lang.Comparece30" select="'Portal de Notificaciones Telemáticas de la Diputación de Ciudad Real COMPARECE'"/>
		<xsl:variable name="lang.Comparece40" select="'Notifica/Carpeta Ciudadana'"/>
		<xsl:variable name="lang.Comparece50" select="'Dirección Electrónica Habilitada (DEH)'"/>
		<xsl:variable name="lang.Comparece60" select="'RÁPIDO, GRATUITO, SIN PAPEL.'"/>

		<div class="submenu">
			<h1><xsl:value-of select="$lang.Comparece00"/></h1>
		</div>
  		<div class="cuadro" style="">
			<div style="width:100%; text-align:center; padding-bottom:5px;">
				<label class="gr">
					<xsl:attribute name="style">font-weight:bold; font-size:1.0em; width:500px; text-align:center; </xsl:attribute>
					<xsl:value-of select="$lang.Comparece10"/>
				</label>
			</div>
			<div style="padding-bottom:5px;">
				<label class="gr">
					<xsl:attribute name="style">width:100%; text-align:justify</xsl:attribute>
					<xsl:value-of select="$lang.Comparece20"/>
				</label>
			</div>
			<!-- <li>
				<xsl:attribute name="style">padding-left:10px;</xsl:attribute>
				<a href="http://comparece.dipucr.es:8080/CompareceNotificadorInterfaz/" target="_blank">
					<xsl:value-of select="$lang.Comparece30"/>
				</a>
			</li> -->
			<li>
				<xsl:attribute name="style">padding-left:10px;</xsl:attribute>
				<a href="https://sede.administracion.gob.es/carpeta/clave.htm" target="_blank">
					<xsl:value-of select="$lang.Comparece40"/>
				</a>
			</li>
			<!-- <li>
				<xsl:attribute name="style">padding-left:10px;</xsl:attribute>
				<a href="https://notificaciones.060.es/PC_init.action" target="_blank">
					<xsl:value-of select="$lang.Comparece50"/>
				</a>
			</li> -->
			<div style="width:100%; text-align:center; padding-top:5px;">
				<label class="gr">
					<xsl:attribute name="style">font-weight:bold; width:100%; </xsl:attribute>
					<xsl:value-of select="$lang.Comparece60"/><br/>
				</label>
			</div>
   		</div>
	</xsl:template>

	<xsl:template name="TEXTO_AUTOFIRMA_COMUN_RELLENO">
		<xsl:variable name="lang.Autofirma00" select="'ATENCIÓN'"/>
		<xsl:variable name="lang.Autofirma10" select="'A PARTIR DEL 18 de ENERO de 2016 se introducen modificaciones técnicas que simplifican y facilitan el proceso de presentación y recepción de documentos electrónicos.'"/>
		<xsl:variable name="lang.Autofirma20" select="'Descargue e instale en su ordenador el software AUTOFIRMA, la versión actual es la 1.6.'"/>
		<xsl:variable name="lang.Autofirma30" select="'Si ya tenía instalado AUTOFIRMA y tiene problemas al firmar descargue e instale de nuevo AUTOFIRMA.'"/>
		<xsl:variable name="lang.Autofirma40" select="'Autofirma requiere version 11 de Internet Explorer y superior, también funciona en otros navegadores como Google Chrome.'"/>
		<xsl:variable name="lang.Autofirma50" select="'Haga click en el siguiente enlace y siga las instrucciones:'"/>
		<xsl:variable name="lang.Autofirma60" select="'DESCARGAR SOFTWARE AUTOFIRMA'"/>
		<xsl:variable name="lang.Autofirma70" select="'En caso de duda puede consultar este video tutorial'"/>

		<div class="col" style="">
			<div style="width:100%; padding-bottom:5px;">
				<label class="gr">
					<xsl:attribute name="style">font-weight:bold; width:100%; color:red; text-align:center; font-size:1.5em;</xsl:attribute>
					<xsl:value-of select="$lang.Autofirma00"/><br/>
				</label>
			</div>
			<div style="width:100%; padding-bottom:5px;">
				<label class="gr">
					<xsl:attribute name="style">width:100%; text-align:justify;</xsl:attribute>
					<xsl:value-of select="$lang.Autofirma10"/><br/>
				</label>
			</div>
			<div style="width:100%; padding-bottom:5px;">
				<label class="gr">
					<xsl:attribute name="style">width:100%; text-align:justify;</xsl:attribute>
					<xsl:value-of select="$lang.Autofirma20"/><br/>
				</label>
			</div>
			<div style="width:100%; padding-bottom:5px;">
				<label class="gr">
					<xsl:attribute name="style">width:100%; text-align:justify;</xsl:attribute>
					<xsl:value-of select="$lang.Autofirma30"/><br/>
				</label>
			</div>
			<div style="width:100%; padding-bottom:5px;">
				<label class="gr">
					<xsl:attribute name="style">width:100%; text-align:justify;</xsl:attribute>
					<xsl:value-of select="$lang.Autofirma40"/><br/>
				</label>
			</div>
			<div style="width:100%; padding-bottom:5px;">
				<label class="gr">
					<xsl:attribute name="style">width:100%; text-align:justify;</xsl:attribute>
					<xsl:value-of select="$lang.Autofirma50"/><br/>
				</label>
			</div>
			<div style="width:100%; padding-bottom:5px;">
				<li>
					<xsl:attribute name="style">padding-left:10px;</xsl:attribute>
					<a href="https://cloud.dipucr.es/owncloud/index.php/s/tIkHBHanU5i7XGF/download" target="_blank">
						<xsl:attribute name="style">font-weight:bold; color:blue; padding-top:10px;</xsl:attribute>
						<xsl:value-of select="$lang.Autofirma60"/>
					</a>
				</li>
			</div>
			<div style="width:100%; padding-bottom:5px;">
				<li>
					<xsl:attribute name="style">padding-left:10px;</xsl:attribute>
					<a href="https://www.youtube.com/watch?v=i0Uebz-HyiU" target="_blank">
						<xsl:attribute name="style">font-weight:bold; color:blue; padding-top:5px;</xsl:attribute>
						<xsl:value-of select="$lang.Autofirma70"/>
					</a>
				</li>
			</div>
		</div>
	</xsl:template>
</xsl:stylesheet>
