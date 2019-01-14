<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:template name="VALIDACIONES">

			function numerico(valor){
				cad = valor.toString();
				for (var i=0; i &lt; cad.length; i++) {
					var caracter = cad.charAt(i);
					if (caracter &lt; "0" || caracter &gt; "9")
						if(caracter == "."){}
						else	return false;
				}
				return true;
			}	
	
			//Valida si el campo para el NIF/CIF/NIE es correcto
			function validarCampoNIFCIF(campoNif) {
				
				if (campoNif.value == '' || campoNif.value == undefined){
					return true;
				}

				var validacion = valida_nif_cif(campoNif);
				
				if (validacion == 0){
					alert('El CIF/NIF no cumple ninguno de los formatos permitidos. Ej: P1300000E');
					return false;
				}
				else if (validacion == -1){
					alert('El NIF introducido es incorrecto. Compruebe la letra.');
					return false;
				}
				else if (validacion == -2){
					alert('El CIF introducido es incorrecto. Compruebe la letra');
					return false;
				}
				else if (validacion == -3){
					alert('El NIE introducido es incorrecto. Compruebe la letra');
					return false;
				}
				else{
					return true;
				}
			}
			
			//Retorna: 1 = NIF ok, 2 = CIF ok, 3 = NIE ok, -1 = NIF error, -2 = CIF error, -3 = NIE error, 0 = ??? error
			function valida_nif_cif(campoNif){
				var a = escape(campoNif.value.toUpperCase());
				var temp=a.toUpperCase();
				campoNif.value = temp;
				var cadenadni="TRWAGMYFPDXBNJZSQVHLCKE";
 
				if (temp!=''){
					//si no tiene un formato valido devuelve error
					if(!/^[XYZ]{1}[0-9]{7}[A-Z]{1}$/.test(temp))
					if(!/^[KLM]{1}[0-9]{7}[A-Z]{1}$/.test(temp))
					if(!/^[0-9]{8}[A-Z]{1}$/.test(temp))
					if(!/^[A-Z]{1}[0-9]{7}[A-Z]{1}$/.test(temp))
					if(!/^[A-Z]{1}[0-9]{8}$/.test(temp))
					{
						return 0;
					}
 
					//Comprobación de NIFs estandar
					if (/^[0-9]{8}[A-Z]{1}$/.test(temp)){
						posicion = a.substring(8,0) % 23;
						letra = cadenadni.charAt(posicion);
						var letradni=temp.charAt(8);
						if (letra == letradni){
						 	return 1;
						}
						else{
							return -1;
						}
					}
					
					//Comprobación de CIFs y NIFs especiales
					//Menores, residentes en extranjero y extranjeros sin NIE
					//algoritmo para comprobacion de codigos tipo CIF (usado para estos NIE)
					suma = parseInt(a.charAt(2))+parseInt(a.charAt(4))+parseInt(a.charAt(6));
					for( i = 1; i &lt; 8; i += 2 )
					{
						temp1 = 2 * parseInt( a.charAt( i ) );
						temp1 += '';
						temp1 = temp1.substring(0,1);
						temp2 = 2 * parseInt( a.charAt( i ) );
						temp2 += '';
						temp2 = temp2.substring( 1,2 );
						if( temp2 == '' )
						{
							temp2 = '0';
						}
			 
						suma += ( parseInt( temp1 ) + parseInt( temp2 ) );
					}
					suma += '';
					n = 10 - parseInt(suma.substring(suma.length-1, suma.length));
					
					//comprobación
					if (/^[KLM]{1}/.test(temp)){
						if (a.charAt(8) == String.fromCharCode(64 + n)){
							return 1;
						}
						else{
							return -1;
						}
					}
					
					//comprobacion de CIFs
					if( /^[ABCDEFGHJNPQRSUVW]{1}/.test( temp ) )
					{
						temp = n + '';
						if( a.charAt( 8 ) == String.fromCharCode( 64 + n ) || a.charAt( 8 ) == parseInt( temp.substring( temp.length-1, temp.length ) ) )
						{
							return 2;
						}
						else
						{
							return -2;
						}
					}

					//Comprobación de NIEs
					//T
					if (/^[T]{1}/.test(temp)){
						if (a.charAt(8) == /^[T]{1}[A-Z0-9]{8}$/.test(temp)){
							return 3;
						}
						else{
							return -3;
						}
					}
					//XYZ
					if (/^[XYZ]{1}/.test(temp)){
 						temp = temp.replace('X','0')
						temp = temp.replace('Y','1')
 						temp = temp.replace('Z','2')
 						pos = temp.substring(0, 8)% 23;
						if (a.charAt(8) == cadenadni.substring(pos, pos + 1)){
 							return 3;
 						}
 						else{
 							return -3;
						}
					}
				}
				return 0;
			}

			//Recupera los datos del interesado		
			function getDatosInteresado(nif){
				window.open('tramites/001/TRAM_COMUNES/buscaInteresado.jsp?valor='+nif+';001','','width=3,height=3');
			}

			//Recupera los datos del representante
			function getDatosRepresentante(nif){
				window.open('tramites/001/TRAM_COMUNES/buscaRepresentante.jsp?valor='+nif+';001','','width=3,height=3');
			}

	</xsl:template>


	<xsl:template name="DATOS_SOLICITANTE">

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


	<xsl:template name="DATOS_INTERESADO_REPRESENTANTE">
	
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosInteresado"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<div style="margin-bottom:10px;color:#006699;">
				<b><xsl:value-of select="$lang.interesado"/>:</b>
			</div>
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
					<xsl:attribute name="onblur">if(!validarCampoNIFCIF(document.getElementById('nif'))){this.focus()}</xsl:attribute>
				</input>
				<xsl:if test="Datos_Registro/datos_especificos/Admin != ''">
					<img onclick="getDatosInteresado(document.getElementById('nif').value);" src="img/search-mg.gif"/>
				</xsl:if>
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
					<xsl:value-of select="$lang.ciudad"/>:
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
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/c_postal"/></xsl:attribute>
				</input>
			</div>	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.region"/>:
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
					<xsl:value-of select="$lang.d_email"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">d_email</xsl:attribute>
					<xsl:attribute name="id">d_email</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/d_email"/></xsl:attribute>
				</input>
			</div>
			<div style="margin-top:20px;margin-bottom:10px;color:#006699;">
				<b><xsl:value-of select="$lang.representante"/>:</b>
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
					<xsl:attribute name="onblur">if(!validarCampoNIFCIF(document.getElementById('repres_nif'))){this.focus()}</xsl:attribute>
				</input>
				<xsl:if test="Datos_Registro/datos_especificos/Admin != ''">
					<img onclick="getDatosRepresentante(document.getElementById('repres_nif').value);" src="img/search-mg.gif"/>
				</xsl:if>
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
	</xsl:template>


	<xsl:template name="ANEXAR_DOCUMENTOS">
		
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   		</div>
   		<div class="cuadro" style="">

			<label class="gr">
			   	<xsl:attribute name="style">width:650px;</xsl:attribute>
		   		<xsl:value-of select="$lang.anexarInfo1"/><br/>
		   		<xsl:value-of select="$lang.anexarInfo2"/><br/>
				<xsl:value-of select="$lang.anexarInfo3"/><br/>
			</label>

			<br/>

   			<div class="col">
	  		<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento1"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; </xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$doc.docodt"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$doc.docodt"/></xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name"><xsl:value-of select="$doc.docodt"/>_Tipo</xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$doc.docodt"/>_Tipo</xsl:attribute>
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
					<xsl:attribute name="name"><xsl:value-of select="$doc.pdf"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$doc.pdf"/></xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name"><xsl:value-of select="$doc.pdf"/>_Tipo</xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$doc.pdf"/>_Tipo</xsl:attribute>
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
					<xsl:attribute name="name"><xsl:value-of select="$doc.jpg"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$doc.jpg"/></xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name"><xsl:value-of select="$doc.jpg"/>_Tipo</xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$doc.jpg"/>_Tipo</xsl:attribute>
					<xsl:attribute name="value">jpg,jpeg</xsl:attribute>
				</input>
			</div>

			<div class="col">
	  		<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento4"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:400px; </xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$doc.zip"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$doc.zip"/></xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name"><xsl:value-of select="$doc.zip"/>_Tipo</xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$doc.zip"/>_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
			</div>
   		</div>
	</xsl:template>


	<xsl:template name="HIDDEN_ATTRIBUTES">
		<input type="hidden"><xsl:attribute name="id">emailSolicitante</xsl:attribute></input>
   		<input type="hidden">
			<xsl:attribute name="name">datosEspecificos</xsl:attribute>
			<xsl:attribute name="id">datosEspecificos</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name">nombresolihidden</xsl:attribute>
			<xsl:attribute name="id">nombresolihidden</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombresolihidden"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name">nifsolihidden</xsl:attribute>
			<xsl:attribute name="id">nifsolihidden</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nifsolihidden"/></xsl:attribute>
		</input>
		<input type="hidden">
			<xsl:attribute name="name">admin</xsl:attribute>
			<xsl:attribute name="id">admin</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Admin"/></xsl:attribute>
		</input>
	</xsl:template>


	<xsl:template name="OPTIONS_MUNICIPIOS" xmlns:java="http://xml.apache.org/xslt/java">
		<xsl:variable name="fileAyuntam" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosTablaValidacionConSustituto('REC_VLDTBL_MUNICIPIOS','001')"/>
		<xsl:variable name="b" select="document($fileAyuntam)"/>
		<xsl:for-each select="$b/listado/dato">
			<option>
				<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
				<xsl:value-of select="sustituto"/>
			</option>
		</xsl:for-each>
	</xsl:template>
	

	<xsl:template name="DATOS_SOLICITUD_RELLENOS">

		<div class="col">
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.datosSolicitante"/></b>	
			</label>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Documento_Identificacion/Numero"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.nombre"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Nombre"/>
			</label>
			<br/>
		</div>
		<br/>
		<div class="col">
			<b>
				<label class="gr" style="position: relative; width:400px;">
					<xsl:value-of select="$lang.datosInteresado"/>:	
				</label>
			</b>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nif"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.nombre"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombre"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.direccion"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/calle"/>,
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ciudad"/>, 
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/region"/>, 
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/c_postal"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.movil"/>:
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/movil"/>
			</label>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.d_email"/>:
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/d_email"/>
			</label>
		</div>
		<br/>
		<div class="col">
			<b>
				<label class="gr" style="position: relative; width:400px;">
					<xsl:value-of select="$lang.datosRepresentante"/>:	
				</label>
			</b>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_nif"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.nombre"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_nombre"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.direccion"/>:	
			</label>
			<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_nif != ''">
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rcalle"/>,
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rciudad"/>, 
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rregion"/>, 
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rc_postal"/>
				</label>
			</xsl:if>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.movil"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_movil"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.d_email"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_d_email"/>
			</label>
			<br/>
		</div>	
	</xsl:template>


	<xsl:template name="INFORMACION_REGISTRO">
		
		<div class="col" xml:space="preserve">
			<label class="gr" style="position: relative; width:170px;">
				<xsl:value-of select="$lang.id_nif"/>:	
			</label>
			<label class="gr" style="position: relative; width:460px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Documento_Identificacion/Numero"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:170px;">
				<xsl:value-of select="$lang.id_nombre"/>:	
			</label>
			<label class="gr" style="position: relative; width:460px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Nombre"/>
			</label>
			<br/>
		</div>
		<br/>
		<div class="col">
			<label class="gr" style="position: relative; width:170px;">
				<xsl:value-of select="$lang.numRegistro"/>:	
			</label>
			<label class="gr" style="position: relative; width:460px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Registro/Numero_Registro"/>
			</label>
			<input type="hidden" id="numeroRegistro" name="numeroRegistro">
				<xsl:attribute name="value"><xsl:value-of select="Solicitud_Registro/Datos_Registro/Numero_Registro"/></xsl:attribute>
			</input>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:170px;">
				<xsl:value-of select="$lang.fechaPresentacion"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:460px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Registro/Hora_Presentacion"/>, 
				<xsl:call-template name="transformaFecha">
				   <xsl:with-param name="node" select="Solicitud_Registro/Datos_Registro/Fecha_Presentacion"/>
				</xsl:call-template>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:170px;">
				<xsl:value-of select="$lang.fechaEfectiva"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:460px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Registro/Hora_Efectiva"/>, 
				<xsl:call-template name="transformaFecha">
				   <xsl:with-param name="node" select="Solicitud_Registro/Datos_Registro/Fecha_Efectiva"/>
				</xsl:call-template>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:170px;">
				<xsl:value-of select="$lang.asunto"/>:	
			</label>
			<label class="gr" style="position: relative; width:460px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Asunto/Descripcion"/>
			</label>
			<br/>
		</div><br/>
	</xsl:template>
	
	<!-- Template para formatear las fechas. Se llama automáticamente desde INFORMACION_REGISTRO --> 
	<xsl:template name="transformaFecha">
			<xsl:param name="node"/>
			<xsl:variable name="date" select="concat(substring(string($node),9,2),'-',substring(string($node),6,2),'-',substring(string($node),1,4))"/>
			<xsl:value-of select="$date"/>
	</xsl:template>
	
</xsl:stylesheet>