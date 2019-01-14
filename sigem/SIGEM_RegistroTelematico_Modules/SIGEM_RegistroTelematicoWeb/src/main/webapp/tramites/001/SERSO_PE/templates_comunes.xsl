<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="ADD_ROW">
		var last_row = new Number(5);
		var max_num_rows = new Number(19);
		function addRow() {
	            if (last_row &lt; max_num_rows)	{	
			      last_row = last_row + 1;
              	var row = document.getElementById("row_"+last_row.toString());
	                    row.style.display = "";
       	             if (last_row == max_num_rows)
              	      {
                     	   var link = document.getElementById("addRow_link");
	                        link.style.display = "none";
       	             }
	                }
		}

		var last_row2 = new Number(5);
		var max_num_rows2 = new Number(19);
		function addRow2() {
	            if (last_row2 &lt; max_num_rows2)	{	
			      last_row2 = last_row2 + 1;
	                    var row = document.getElementById("row2_"+last_row2.toString());
              	      row.style.display = "";
       	             if (last_row2 == max_num_rows2)
	                    {
                     	   var link = document.getElementById("addRow2_link");                        
              	          link.style.display = "none";
		          }
	             	}
		}
	</xsl:template>
	
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

			//Retorna: 1 = NIF ok, 2 = CIF ok, 3 = NIE ok, -1 = NIF error, -2 = CIF error, -3 = NIE error, 0 = ??? error
			function valida_nif_cif_nie(campoNif){
				var a = escape(campoNif.value.toUpperCase());
				var temp=a.toUpperCase();
				campoNif.value = temp;
				var cadenadni="TRWAGMYFPDXBNJZSQVHLCKE";
 
				if (temp!=''){
				//si no tiene un formato valido devuelve error
					if ((!/^[A-Z]{1}[0-9]{7}[A-Z0-9]{1}$/.test(temp)))
						if( !/^[T]{1}[A-Z0-9]{8}$/.test(temp))
							if(!/^[0-9]{8}[A-Z]{1}$/.test(temp)){
								return 0;
							}
 
					//comprobacion de NIFs estandar
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
 
					//algoritmo para comprobacion de codigos tipo CIF
					suma = parseInt(a.charAt(2))+parseInt(a.charAt(4))+parseInt(a.charAt(6));
					var i = 1;
					var fin = false;
					while (!fin){
						temp1 = 2 * parseInt(a.charAt(i));
						temp1 += '';
						temp1 = temp1.substring(0,1);
						temp2 = 2 * parseInt(a.charAt(i));
						temp2 += '';
						temp2 = temp2.substring(1,2);
						if (temp2 == ''){
							temp2 = '0';
						}
						suma += (parseInt(temp1) + parseInt(temp2));
						i = i+2;
						if(i!=8 || i!=9 || i==100){
							fin = true;
						}
					}
					suma += '';
					n = 10 - parseInt(suma.substring(suma.length-1, suma.length));
					//comprobacion de NIFs especiales (se calculan como CIFs)
					if (/^[KLM]{1}/.test(temp)){
						if (a.charAt(8) == String.fromCharCode(64 + n)){
							return 1;
						}
						else{
							return -1;
						}
					}
	 
					//comprobacion de CIFs
					if (/^[ABCDEFGHJNPQRSUVW]{1}/.test(temp)){
						return validaCIF(a);
					}
 
					//comprobacion de NIEs
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

		function validaCIF(cif){
			var par = 0;
			var non = 0;
			var letras = "ABCDEFGHJKLMNPRQSUVW";
			var caracterControlLetra = "KPQS";
			var caracterControlNum = "ABEH";
			var i;
			var parcial;
			var control;
			var controlLetra = "JABCDEFGHIJ";
			var letraIni = cif.charAt(0);
			 
			if (cif.length!=9){
			    alert("El Cif debe tener 9 dígitos",3);
				    return -2;
			}
			else{
			    if (letras.indexOf(letraIni.toUpperCase())==-1)
			    {
				alert("La letra del CIF introducido no es correcta",3);
				    return -2;
			    }
			    var i = 2;
			    while (i!=8){
				par = par + parseInt(cif.charAt(i));
				i = i+2;
			    }
			 
			    i = 1;
			    while ( i!= 9){
				var nn = 2 * parseInt(cif.charAt(i));
				if (nn > 9) nn = 1 + (nn-10);
				non = non + nn;
				i = i+2;
			    }
			 
			    parcial = par + non;			 
			    control = (10 - ( parcial % 10));
			 			 
			    if (caracterControlLetra.indexOf(letraIni.toUpperCase()) != -1){
				// El caracter de control deberá ser una letra
			 
				if (controlLetra.charAt(control) != cif.charAt(8).toUpperCase()){
				    alert("El Cif no es válido",3);
				    return -2;
				}
			    }
			    if (caracterControlNum.indexOf(letraIni.toUpperCase()) != -1)
			    {
				// El caracter de control deberá ser un número
			 
				if (control == 10) control = 0;			 
				if (control != cif.charAt(8)){
				    alert("El Cif no es válido",3);
				    return -2;
				}
			    }
			    if (caracterControlLetra.indexOf(letraIni.toUpperCase()) == -1)
				if(caracterControlNum.indexOf(letraIni.toUpperCase()) == -1){
				// En este caso el carácter de control puede ser una letra o un número			 
					if (control == 10){
					    control = 0;
					}
					if (controlLetra.charAt(control) != cif.charAt(8).toUpperCase())
						if(control != cif.charAt(8)){
						    alert("El Cif no es válido",3);
						    return -2;
						}
			    }
			}
			return 2;
		}
	</xsl:template>

	<xsl:template name="DATOS_SOLICITANTE">
		<xsl:variable name="lang.id_nif" select="'NIF del Trabajador/a Social'"/>
		<xsl:variable name="lang.id_nombre" select="'Nombre del Trabajador/a Social'"/>

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

		
	<xsl:template name="DATOS_BENEFICIARIO">
		<xsl:variable name="lang.datosBeneficiario" select="'Datos del beneficiario'"/>
		<xsl:variable name="lang.nif" select="'NIF/CIF'"/>
		<xsl:variable name="lang.nombre" select="'Apellidos y Nombre'"/>
		<xsl:variable name="lang.ciudad" select="'Ciudad'"/>		
	
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosBeneficiario"/></h1>
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
					<xsl:value-of select="$lang.ciudad"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">width:300px; </xsl:attribute>
					<xsl:attribute name="name">ciudad</xsl:attribute>
					<xsl:attribute name="id">ciudad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ciudad"/></xsl:attribute>
				</input>
			</div>					
		</div>
	</xsl:template>

	<xsl:template match="/" xmlns:java="http://xml.apache.org/xslt/java" name="DATOS_FAMILIA">

		<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

		<xsl:variable name="lang.convocatoria" select="'Convocatorias abiertas:* (seleccionar)'"/>		
		
		<xsl:variable name="lang.tipoAyuda" select="'Tipo de Ayuda'"/>	

		<xsl:variable name="lang.nfamiliar" select="'Número de miembros de la unidad familiar *'"/>
		<xsl:variable name="lang.menores3anios" select="'Menores de 3 años inclusive *'"/>		
		<xsl:variable name="lang.menores3anios2" select="'Menores de 3 a 16 años inclusive *'"/>

		<xsl:variable name="lang.pensionAlimenticia" select="'Pensión alimenticia'"/>
		<xsl:variable name="lang.total" select="'Total'"/>

		<xsl:variable name="lang.tipoFamilia" select="'Tipo de familia'"/>
		<xsl:variable name="lang.colectivo" select="'Colectivo'"/>
		<xsl:variable name="lang.vivienda" select="'Vivienda'"/>
		<xsl:variable name="lang.diagnostico" select="'Diagnóstico inicial/Valoración Social *'"/>
		<xsl:variable name="lang.diagnostico2" select="'Informe Social Completo *'"/>

		<xsl:variable name="lang.meses" select="'- Mes a cubrir:'"/>
		<xsl:variable name="lang.meses2" select="'- Concepto:'"/>

		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
		<div class="cuadro" style="">	
	   		<b><xsl:value-of select="$lang.convocatoria"/></b>				
			<xsl:variable name="convocatoria" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosConsulta('Convocatoria', 'select numexp, asunto from spac_expedientes where codprocedimiento=#PCD-221# and fcierre is null and estadoadm=#PR#','001')"/>
			<xsl:variable name="convocatoria" select="document($convocatoria)"/>
			<br/>
			<select class="gr">
				<xsl:attribute name="style">border:none; width:600px;color:#006699;</xsl:attribute>
				<xsl:attribute name="name">convocatoria</xsl:attribute>
				<xsl:attribute name="id">convocatoria</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/convocatoria"/></xsl:attribute>
				<xsl:attribute name="onchange">
					var texto = this.options[this.selectedIndex].text;
					if(texto.toUpperCase().indexOf('ALIMENTACIÓN')>0 || texto.toUpperCase().indexOf('ALIMENTACION')>0){
						document.getElementById('tipoAyuda').value='ALIMENTACION';
						document.getElementById('numMenores').style.display='none';
						document.getElementById('numMenores').style.visibility='hidden';
						document.getElementById('tablaMenores').style.display='';
						document.getElementById('menores3anios').innerText='<xsl:value-of select="$lang.menores3anios"/>:';
						document.getElementById('etiquetaDiagnostico').innerText='<xsl:value-of select="$lang.diagnostico"/>:';
						document.getElementById('tabla_libros_comedor').style.display='none';
						document.getElementById('datosPropuesta').style.display='';
						document.getElementById('propuesta1_importe').value='';
						document.getElementById('textoSolicitante').innerText='SOLICITANTE HABITUAL DE AYUDAS ECONÓMICAS. USUARIOS/AS HABITUALES DE SERVICIOS SOCIALES';
						cambiaColumnas('ALIMENTACION');
					}else if(texto.toUpperCase().indexOf('COMEDOR')>0){
						document.getElementById('tipoAyuda').value='COMEDOR';
						document.getElementById('numMenores').style.display='';
						document.getElementById('numMenores').style.visibility='';
						document.getElementById('tablaMenores').style.display='none';
						document.getElementById('menores3anios').innerText='<xsl:value-of select="$lang.menores3anios2"/>:';
						document.getElementById('etiquetaDiagnostico').innerText='<xsl:value-of select="$lang.diagnostico"/>:';
						document.getElementById('tabla_libros_comedor').style.display='';
						document.getElementById('datosPropuesta').style.display='none';
						document.getElementById('propuesta1_importe').value='0';
						document.getElementById('textoSolicitante').innerText='BENEFICIARIOS DEL PLAN DE AYUDAS DE EMERGENCIA SOCIAL DE LA DIPUTACIÓN';
						cambiaColumnas('COMEDOR');
					}else if(texto.toUpperCase().indexOf('LIBROS')>0){
						document.getElementById('tipoAyuda').value='LIBROS';
						document.getElementById('numMenores').style.display='';
						document.getElementById('numMenores').style.visibility='';
						document.getElementById('menores3anios').innerText='<xsl:value-of select="$lang.menores3anios2"/>:';
						document.getElementById('tablaMenores').style.display='none';
						document.getElementById('etiquetaDiagnostico').innerText='<xsl:value-of select="$lang.diagnostico"/>:';
						document.getElementById('tabla_libros_comedor').style.display='';
						document.getElementById('datosPropuesta').style.display='none';
						document.getElementById('propuesta1_importe').value='0';
						document.getElementById('textoSolicitante').innerText='BENEFICIARIOS DEL PLAN DE AYUDAS DE EMERGENCIA SOCIAL DE LA DIPUTACIÓN';
						cambiaColumnas('LIBROS');
					}else {
						document.getElementById('tipoAyuda').value='EXCEPCIONAL';
						document.getElementById('numMenores').style.display='none';
						document.getElementById('numMenores').style.visibility='hidden';
						document.getElementById('tablaMenores').style.display='';
						document.getElementById('menores3anios').innerText='<xsl:value-of select="$lang.menores3anios"/>:';
						document.getElementById('etiquetaDiagnostico').innerText='<xsl:value-of select="$lang.diagnostico2"/>:';
						document.getElementById('tabla_libros_comedor').style.display='none';
						document.getElementById('datosPropuesta').style.display='';
						document.getElementById('propuesta1_importe').value='';
						document.getElementById('textoSolicitante').innerText='SOLICITANTE HABITUAL DE AYUDAS ECONÓMICAS. USUARIOS/AS HABITUALES DE SERVICIOS SOCIALES';
						cambiaColumnas('EXCEPCIONAL');
					}

				</xsl:attribute>
				<script>
					function cambiaColumnas(tipoAyuda){
						if(tipoAyuda == 'LIBROS'){
							document.getElementById("empresacomedor").style.display='none';
							document.getElementById("empresacomedor1").style.display='none';
							document.getElementById("empresacomedor2").style.display='none';
							document.getElementById("empresacomedor3").style.display='none';
							document.getElementById("empresacomedor4").style.display='none';
							document.getElementById("empresacomedor5").style.display='none';
							document.getElementById("empresacomedor6").style.display='none';
							document.getElementById("empresacomedor7").style.display='none';
							document.getElementById("empresacomedor8").style.display='none';

							document.getElementById("costelibros").style.display='';
							document.getElementById("costelibros1").style.display='';
							document.getElementById("costelibros2").style.display='';
							document.getElementById("costelibros3").style.display='';
							document.getElementById("costelibros4").style.display='';
							document.getElementById("costelibros5").style.display='';
							document.getElementById("costelibros6").style.display='';
							document.getElementById("costelibros7").style.display='';
							document.getElementById("costelibros8").style.display='';

							document.getElementById("curso").style.display='';
							document.getElementById("curso1").style.display='';
							document.getElementById("curso2").style.display='';
							document.getElementById("curso3").style.display='';
							document.getElementById("curso4").style.display='';
							document.getElementById("curso5").style.display='';
							document.getElementById("curso6").style.display='';
							document.getElementById("curso7").style.display='';
							document.getElementById("curso8").style.display='';

							document.getElementById("colegio").style.display='none';
							document.getElementById("colegio1").style.display='none';
							document.getElementById("colegio2").style.display='none';
							document.getElementById("colegio3").style.display='none';
							document.getElementById("colegio4").style.display='none';
							document.getElementById("colegio5").style.display='none';
							document.getElementById("colegio6").style.display='none';
							document.getElementById("colegio7").style.display='none';
							document.getElementById("colegio8").style.display='none';

							document.getElementById("importelibrossol").style.display='';
							document.getElementById("importelibrossol1").style.display='';
							document.getElementById("importelibrossol2").style.display='';
							document.getElementById("importelibrossol3").style.display='';
							document.getElementById("importelibrossol4").style.display='';
							document.getElementById("importelibrossol5").style.display='';
							document.getElementById("importelibrossol6").style.display='';
							document.getElementById("importelibrossol7").style.display='';
							document.getElementById("importelibrossol8").style.display='';

							document.getElementById("ninio3").style.display='';
							document.getElementById("ninio4").style.display='';
							document.getElementById("ninio5").style.display='';
							document.getElementById("ninio6").style.display='';
							document.getElementById("ninio7").style.display='';
							document.getElementById("ninio8").style.display='';

							document.getElementById("idcolumna").style.width='5%';
							document.getElementById("nombrecolumna").style.width='65%';
							document.getElementById("edadcolumna").style.width='5%';
							document.getElementById("cursocolumna").style.width='5%';
							document.getElementById("costecolumna").style.width='10%';
							document.getElementById("importelibrossolcolumna").style.width='10%';
							document.getElementById("filatotal").style.display='';
							document.getElementById("datosLibros").style.display='';
							document.getElementById('datosLibros2').style.display='none';
							document.getElementById("datosComedor").style.display='none';
						}
						else{
							document.getElementById("empresacomedor").style.display='';
							document.getElementById("empresacomedor1").style.display='';
							document.getElementById("empresacomedor2").style.display='';
							document.getElementById("empresacomedor3").style.display='';
							document.getElementById("empresacomedor4").style.display='';
							document.getElementById("empresacomedor5").style.display='';
							document.getElementById("empresacomedor6").style.display='';
							document.getElementById("empresacomedor7").style.display='';
							document.getElementById("empresacomedor8").style.display='';

							document.getElementById("costelibros").style.display='none';
							document.getElementById("costelibros1").style.display='none';
							document.getElementById("costelibros2").style.display='none';
							document.getElementById("costelibros3").style.display='none';
							document.getElementById("costelibros4").style.display='none';
							document.getElementById("costelibros5").style.display='none';
							document.getElementById("costelibros6").style.display='none';
							document.getElementById("costelibros7").style.display='none';
							document.getElementById("costelibros8").style.display='none';

							document.getElementById("curso").style.display='none';
							document.getElementById("curso1").style.display='none';
							document.getElementById("curso2").style.display='none';
							document.getElementById("curso3").style.display='none';
							document.getElementById("curso4").style.display='none';
							document.getElementById("curso5").style.display='none';
							document.getElementById("curso6").style.display='none';
							document.getElementById("curso7").style.display='none';
							document.getElementById("curso8").style.display='none';

							document.getElementById("colegio").style.display='';
							document.getElementById("colegio1").style.display='';
							document.getElementById("colegio2").style.display='';
							document.getElementById("colegio3").style.display='';
							document.getElementById("colegio4").style.display='';
							document.getElementById("colegio5").style.display='';
							document.getElementById("colegio6").style.display='';
							document.getElementById("colegio7").style.display='';
							document.getElementById("colegio8").style.display='';

							document.getElementById("importelibrossol").style.display='none';
							document.getElementById("importelibrossol1").style.display='none';
							document.getElementById("importelibrossol2").style.display='none';
							document.getElementById("importelibrossol3").style.display='none';
							document.getElementById("importelibrossol4").style.display='none';
							document.getElementById("importelibrossol5").style.display='none';
							document.getElementById("importelibrossol6").style.display='none';
							document.getElementById("importelibrossol7").style.display='none';
							document.getElementById("importelibrossol8").style.display='none';

							document.getElementById("ninio3").style.display='none';
							document.getElementById("ninio4").style.display='none';
							document.getElementById("ninio5").style.display='none';
							document.getElementById("ninio6").style.display='none';
							document.getElementById("ninio7").style.display='none';
							document.getElementById("ninio8").style.display='none';

							document.getElementById("idcolumna").style.width='5%';
							document.getElementById("nombrecolumna").style.width='35%';
							document.getElementById("edadcolumna").style.width='10%';
							document.getElementById("cursocolumna").style.width='25%';
							document.getElementById("costecolumna").style.width='25%';
							document.getElementById("importelibrossolcolumna").style.width='0%';
							document.getElementById("filatotal").style.display='none';
							document.getElementById('datosLibros2').style.display='none';
							document.getElementById("datosLibros").style.display='none';
							document.getElementById("datosComedor").style.display='';
						}
					}
				</script>


				<option>
					<xsl:attribute name="value">-------</xsl:attribute>----------------------------------------------------</option>
				<xsl:for-each select="$convocatoria/listado/dato">
					<option>
						<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
						<xsl:value-of select="valor"/> - <xsl:value-of select="sustituto"/>
					</option>
				</xsl:for-each>
			</select>
		</div> 

		<div class="cuadro" style="visibility:'hidden';display: none">	
	   		<b><xsl:value-of select="$lang.tipoAyuda"/>: </b>				
			<select class="gr">
				<xsl:attribute name="style">border:none; width:300px;color:#006699;</xsl:attribute>
				<xsl:attribute name="name">tipoAyuda</xsl:attribute>
				<xsl:attribute name="id">tipoAyuda</xsl:attribute>
				<xsl:attribute name="onblur">if(this.value== 'EXCEPCIONAL'){document.getElementById('primeraProp').style.display='none';document.getElementById('divProp1').style.display='none';document.getElementById('divProp2').style.display='none';document.getElementById('etiquetaDiagnostico').innerText='<xsl:value-of select="$lang.diagnostico2"/>:';document.getElementById('etiquetaMeses').innerText='<xsl:value-of select="$lang.meses2"/>';}else{document.getElementById('primeraProp').style.display='';document.getElementById('divProp1').style.display='';document.getElementById('divProp2').style.display='';document.getElementById('etiquetaDiagnostico').innerText='<xsl:value-of select="$lang.diagnostico"/>:';document.getElementById('etiquetaMeses').innerText='<xsl:value-of select="$lang.meses"/>';}</xsl:attribute>				<option>
					<xsl:attribute name="value">ALIMENTACION</xsl:attribute>Ayuda a la alimentación e higiene
				</option>
				<option>
					<xsl:attribute name="value">EXCEPCIONAL</xsl:attribute>Ayudas excepcionales
				</option>
				<option>
					<xsl:attribute name="value">LIBROS</xsl:attribute>Ayuda Libros
				</option>
				<option>
					<xsl:attribute name="value">COMEDOR</xsl:attribute>Becas Comedor
				</option>
			</select>
			<script>
				function cambiaEtiquetaDiag(){
					if(document.getElementById('tipoAyuda').value == 'EXCEPCIONAL'){
						document.getElementById('etiquetaDiagnostico').innerText='<xsl:value-of select="$lang.diagnostico2"/>:';
					else{
						document.getElementById('etiquetaDiagnostico').innerText='<xsl:value-of select="$lang.diagnostico"/>:';
					}
				}
			</script>
		</div>

   		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nfamiliar"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">nfamiliar</xsl:attribute>
					<xsl:attribute name="id">nfamiliar</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nfamiliar"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
			
				<label class="gr">
					<xsl:attribute name="style">width:220px;</xsl:attribute>
					<xsl:attribute name="id">menores3anios</xsl:attribute>
					<xsl:value-of select="$lang.menores3anios"/>:
				</label>
				<input>
					<xsl:attribute name="name">numMenores</xsl:attribute>
					<xsl:attribute name="id">numMenores</xsl:attribute>
					<xsl:attribute name="style">visibility:'hidden';display:'none'; width:70px;</xsl:attribute>
					<xsl:attribute name="size">4</xsl:attribute>
					<xsl:attribute name="maxlength">4</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/numMenores"/></xsl:attribute>
				</input>
				<br/>
				<br/>
				<table id="tablaMenores">
					<tr>
						<td class="gr">
							<label>
								<xsl:attribute name="style">font-size:12px; </xsl:attribute>
								Sí
							</label>
							<input type="radio">
								<xsl:attribute name="style">width:20px; </xsl:attribute>
								<xsl:attribute name="name">menores3aniosrd</xsl:attribute>
								<xsl:attribute name="id">menores3aniosrd</xsl:attribute>
								<xsl:attribute name="onclick">
									document.getElementById("menores3anios").value='SI';
									document.getElementById("celdita").style.display='';
								</xsl:attribute>
							</input>
						</td>
						<td align='left' style="display:none" id="celdita">													
							<label>
								<xsl:attribute name="style">font-size:12px; </xsl:attribute>
								1
							</label>
							<input type="radio">
								<xsl:attribute name="style">width:20px; </xsl:attribute>
								<xsl:attribute name="name">numMenoresRd</xsl:attribute>
								<xsl:attribute name="id">numMenoresRd</xsl:attribute>
								<xsl:attribute name="onclick">
									document.getElementById("numMenores").value='1';
								</xsl:attribute>
							</input>
							<br/>
							<label>
								<xsl:attribute name="style">font-size:12px; </xsl:attribute>
								2 o más
							</label>
							<input type="radio">
								<xsl:attribute name="style">width:20px; </xsl:attribute>
								<xsl:attribute name="name">numMenoresRd</xsl:attribute>
								<xsl:attribute name="id">numMenoresRd</xsl:attribute>
								<xsl:attribute name="onclick">
									document.getElementById("numMenores").value='2';
								</xsl:attribute>
							</input>
						</td>
					</tr>
					<tr>
						<td class="gr" colspan="2">
							<label>
								<xsl:attribute name="style">font-size:12px; </xsl:attribute>
								No
							</label>
							<input type="radio">
								<xsl:attribute name="style">width:20px; </xsl:attribute>
								<xsl:attribute name="name">menores3aniosrd</xsl:attribute>
								<xsl:attribute name="id">menores3aniosrd</xsl:attribute>
								<xsl:attribute name="onclick">
									document.getElementById("numMenores").value='0';
									document.getElementById("numMenoresRd").checked=false;
									document.getElementById("menores3anios").value='NO';
									document.getElementById("celdita").style.display='none';									
								</xsl:attribute>
							</input>
						</td>
					</tr>
				</table>
				<input type="hidden">
					<xsl:attribute name="name">menores3anios</xsl:attribute>
					<xsl:attribute name="id">menores3anios</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/menores3anios"/></xsl:attribute>
				</input>				
			</div>	
			<xsl:call-template name="TABLA_FAMILIA" />
			<div class="col" align='right'>
				<label class="gr">
					<xsl:attribute name="style">width:520px;</xsl:attribute>
					<xsl:value-of select="$lang.pensionAlimenticia"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:120px; </xsl:attribute>
					<xsl:attribute name="name">ing_20</xsl:attribute>
					<xsl:attribute name="id">ing_20</xsl:attribute>
					<xsl:attribute name="onchange">document.getElementById('total').value = parseFloat(this.value) + parseFloat(document.getElementById('total').value);</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ing_20"/></xsl:attribute>
				</input>€
			</div>

			<input type="hidden">
				<xsl:attribute name="name">par_20</xsl:attribute>
				<xsl:attribute name="id">par_20</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/par_20"/></xsl:attribute>
			</input>
			<input type="hidden">
				<xsl:attribute name="name">edad_20</xsl:attribute>
				<xsl:attribute name="id">edad_20</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/edad_20"/></xsl:attribute>
			</input>
			<input type="hidden">
				<xsl:attribute name="name">prof_20</xsl:attribute>
				<xsl:attribute name="id">prof_20</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/prof_20"/></xsl:attribute>
			</input>
			<input type="hidden">
				<xsl:attribute name="name">sit_20</xsl:attribute>
				<xsl:attribute name="id">sit_20</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/sit_20"/></xsl:attribute>
			</input>

			<div class="col" align='right'>
				<label class="gr">
					<xsl:attribute name="style">width:520px;</xsl:attribute>
					<xsl:value-of select="$lang.total"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:120px; </xsl:attribute>
					<xsl:attribute name="name">total</xsl:attribute>
					<xsl:attribute name="id">total</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/total"/></xsl:attribute>
				</input>€
<script>
if(document.getElementById('total').value=='')
	document.getElementById('total').value=0;
</script>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.tipoFamilia"/>:
				</label>
				<br/>
				<br/>
				<label for="totalFamilia1">      - 				
					<input type="checkbox">
						<xsl:attribute name="style">width:17px; padding:0px;</xsl:attribute>
						<xsl:attribute name="name">totalFamilia1</xsl:attribute>
						<xsl:attribute name="id">totalFamilia1</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/totalFamilia1"/></xsl:attribute>
					</input>
				</label>
				<label for="totalFamilia1" id="textoSolicitante" style="display:inline;color:#000000;">SOLICITANTE HABITUAL DE AYUDAS ECONÓMICAS. USUARIOS/AS HABITUALES DE SERVICIOS SOCIALES</label>
				<br/><br/>
				<label for="totalFamilia11">      - 
					<input type="checkbox">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">totalFamilia11</xsl:attribute>
						<xsl:attribute name="id">totalFamilia11</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/totalFamilia11"/></xsl:attribute>
					</input>
				</label>
				<label for="totalFamilia11" id="textoSolicitante2" style="display:inline;color:#000000;">FAMILIA INCLUIDA EN EL PROGRAMA DE INTERVENCIÓN FAMILIAR</label>
				<br/><br/>
				<label for="totalFamilia2">      - 				
					<input type="checkbox">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">totalFamilia2</xsl:attribute>
						<xsl:attribute name="id">totalFamilia2</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/totalFamilia2"/></xsl:attribute>
					</input>
				</label>
				<label for="totalFamilia2" id="textoSolicitante3" style="display:inline;color:#000000;">FAMILIA NORMALIZADA EN SITUACIÓN DE NECESIDAD DERIVADA DE DESEMPLEO PROLONGADO</label>
				<br/><br/>
				<label for="totalFamilia3">      - 				
					<input type="checkbox">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">totalFamilia3</xsl:attribute>
						<xsl:attribute name="id">totalFamilia3</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/totalFamilia3"/></xsl:attribute>
					</input>
				</label>
				<label for="totalFamilia3" id="textoSolicitante4" style="display:inline;color:#000000;">FAMILIA NORMALIZADA QUE NECESITA UN APOYO ECONÓMICO PUNTUAL</label>
				<br/>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.colectivo"/>:
				</label>
				<br/><br/>
				<label>      - 
					<input type="checkbox">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">colectivo1</xsl:attribute>
						<xsl:attribute name="id">colectivo1</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colectivo1"/></xsl:attribute>
					</input>
				</label>
				<label for="colectivo1" id="textocolectivo1" style="display:inline;color:#000000;">INMIGRANTES</label>
				<br/><br/>
				<label>      - 
					<input type="checkbox">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">colectivo2</xsl:attribute>
						<xsl:attribute name="id">colectivo2</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colectivo2"/></xsl:attribute>
					</input>
				</label>
				<label for="colectivo2" id="textocolectivo2" style="display:inline;color:#000000;">ETNIA GITANA</label>
				<br/><br/>
				<label>      - 
					<input type="checkbox">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">colectivo3</xsl:attribute>
						<xsl:attribute name="id">colectivo3</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colectivo3"/></xsl:attribute>
					</input>
				</label>
				<label for="colectivo3" id="textocolectivo3" style="display:inline;color:#000000;">FAMILIA MONOPARENTAL CON HIJOS MENORES A CARGO</label>
				<br/>
<br/>
				<label>
					<xsl:attribute name="style">width:100%; padding-left:25px;</xsl:attribute>
					<input type="radio">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">rdFamilia</xsl:attribute>
						<xsl:attribute name="id">rdFamilia</xsl:attribute>
						<xsl:attribute name="onclick">document.getElementById('colectivo4').value='Hijos/as no reconocidos/as'</xsl:attribute>
					</input>
					Hijos/as no reconocidos/as
				</label>
				<br/>
				<label>
					<xsl:attribute name="style">width:100%; padding-left:25px;</xsl:attribute>
					<input type="radio">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">rdFamilia</xsl:attribute>
						<xsl:attribute name="id">rdFamilia</xsl:attribute>
						<xsl:attribute name="onclick">document.getElementById('colectivo4').value='Con pensión alimenticia'</xsl:attribute>
					</input>
					Con pensión alimenticia
				</label>
				<br/>
				<label>
					<xsl:attribute name="style">width:100%; padding-left:25px;</xsl:attribute>
					<input type="radio">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">rdFamilia</xsl:attribute>
						<xsl:attribute name="id">rdFamilia</xsl:attribute>
						<xsl:attribute name="onclick">document.getElementById('colectivo4').value='Sin pensión alimentica y denunciado/a'</xsl:attribute>
					</input>
					Sin pensión alimentica y denunciado/a
				</label>
				<br/>
				<label>
					<xsl:attribute name="style">width:100%; padding-left:25px;</xsl:attribute>			
					<input type="radio">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">rdFamilia</xsl:attribute>
						<xsl:attribute name="id">rdFamilia</xsl:attribute>
						<xsl:attribute name="onclick">document.getElementById('colectivo4').value='Sin pensión alimenticia y no denunciado/a'</xsl:attribute>
					</input>
					Sin pensión alimenticia y no denunciado/a
				</label>
				<br/>
				<label>
					<xsl:attribute name="style">width:100%; padding-left:25px;</xsl:attribute>
					<input type="radio">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">rdFamilia</xsl:attribute>
						<xsl:attribute name="id">rdFamilia</xsl:attribute>
						<xsl:attribute name="onclick">document.getElementById('colectivo4').value='Otras situaciones, explicar en diagnóstico social'</xsl:attribute>
					</input>
					Otras situaciones, explicar en diagnóstico social
				</label>
			

				<input type="hidden">
					<xsl:attribute name="style">width:100%; </xsl:attribute>
					<xsl:attribute name="maxlength">145</xsl:attribute>
					<xsl:attribute name="size">150</xsl:attribute>
					<xsl:attribute name="name">colectivo4</xsl:attribute>
					<xsl:attribute name="id">colectivo4</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colectivo4"/></xsl:attribute>
				</input> 
				<br/>
<!--
				<br/>
				<label>      - </label>
				OTRO(Especificar):
				<br/>				
				<input type="text">
					<xsl:attribute name="style">width:100%; </xsl:attribute>
					<xsl:attribute name="maxlength">145</xsl:attribute>
					<xsl:attribute name="size">150</xsl:attribute>
					<xsl:attribute name="name">colectivo4</xsl:attribute>
					<xsl:attribute name="id">colectivo4</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colectivo4"/></xsl:attribute>
				</input>
-->
				<br/>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.vivienda"/>:
				</label>
				<br/><br/>
				<label>      - 
					<input type="checkbox">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">vivienda1</xsl:attribute>
						<xsl:attribute name="id">vivienda1</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/vivienda1"/></xsl:attribute>
					</input>
				</label>
				<label for="vivienda1" id="textovivienda1" style="display:inline;color:#000000;">PROPIA PAGADA</label>
				<br/><br/>
				<label>      - 
					<input type="checkbox">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">vivienda2</xsl:attribute>
						<xsl:attribute name="id">vivienda2</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/vivienda2"/></xsl:attribute>
					</input>
				</label>
				<label for="vivienda2" id="textovivienda2" style="display:inline;color:#000000;">PROPIA CON HIPOTECA DE   <input type="text">
					<xsl:attribute name="style">width:50px; </xsl:attribute>
					<xsl:attribute name="maxlength">20</xsl:attribute>
					<xsl:attribute name="size">20</xsl:attribute>
					<xsl:attribute name="name">viviendaEuros2</xsl:attribute>
					<xsl:attribute name="id">viviendaEuros2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/viviendaEuros2"/></xsl:attribute>
				</input> € MES</label>
				<br/><br/>
				<label>      - 
					<input type="checkbox">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">vivienda3</xsl:attribute>
						<xsl:attribute name="id">vivienda3</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/vivienda3"/></xsl:attribute>
					</input>
				</label>
				<label for="vivienda3" id="textovivienda3" style="display:inline;color:#000000;">ALQUILADA <input type="text">
					<xsl:attribute name="style">width:50px; </xsl:attribute>
					<xsl:attribute name="maxlength">20</xsl:attribute>
					<xsl:attribute name="size">20</xsl:attribute>
					<xsl:attribute name="name">viviendaEuros3</xsl:attribute>
					<xsl:attribute name="id">viviendaEuros3</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/viviendaEuros3"/></xsl:attribute>
				</input> € MES</label>
				<br/><br/>
				<label>      - 
					<input type="checkbox">
						<xsl:attribute name="style">width:17px; </xsl:attribute>
						<xsl:attribute name="name">vivienda31</xsl:attribute>
						<xsl:attribute name="id">vivienda31</xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/vivienda31"/></xsl:attribute>
					</input>
				</label>
				<label for="vivienda31" id="textovivienda31" style="display:inline;color:#000000;">DESAHUCIADO O EN PROCESO</label>
				<br/><br/>
				<label>      - </label>
				OTRA(Especificar):
				<br/>
				<input type="text">
					<xsl:attribute name="style">width:100%; </xsl:attribute>
					<xsl:attribute name="maxlength">145</xsl:attribute>
					<xsl:attribute name="size">150</xsl:attribute>
					<xsl:attribute name="name">vivienda4</xsl:attribute>
					<xsl:attribute name="id">vivienda4</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/vivienda4"/></xsl:attribute>
				</input> 
				<br/>
			</div>

			<div style="display:'none';" id="tabla_libros_comedor">
				<xsl:call-template name="TABLA_LIBROS_COMEDOR" />
				<br/>
			</div>

			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:300px;</xsl:attribute>
					<xsl:attribute name="id">etiquetaDiagnostico</xsl:attribute>
					<xsl:value-of select="$lang.diagnostico"/>:
				</label>								
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:620px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">diagnostico</xsl:attribute>
					<xsl:attribute name="id">diagnostico</xsl:attribute>
					<xsl:attribute name="rows">10</xsl:attribute>
					<xsl:attribute name="onkeypress">if(this.value.length&gt;25500){ alert('Tamaño máximo de campo alcanzado'); this.value = this.value.substr(0,25500);}</xsl:attribute>
					<xsl:attribute name="onkeyup">if(this.value.length&gt;25500){ alert('Tamaño máximo de campo alcanzado'); this.value = this.value.substr(0,25500);}</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/diagnostico"/>
				</textarea>
				<br/>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="TABLA_LIBROS_COMEDOR">
		<xsl:variable name="lang.texto1" select="'Relación de menores para los que se solicita la ayuda / beca'"/>

		<xsl:variable name="lang.tabla1" select="'Nombre y Apellidos'"/>
		<xsl:variable name="lang.tabla2" select="'Edad/Curso'"/>
		<xsl:variable name="lang.tabla3" select="'Material Escolar'"/>
		<xsl:variable name="lang.tabla4" select="'Coste Libros'"/>
		<xsl:variable name="lang.tabla5" select="'Empresa de Comedor'"/>
		<xsl:variable name="lang.tabla6" select="'Importe Solicitado'"/>
		<xsl:variable name="lang.tabla7" select="'Colegio'"/>

		<xsl:variable name="lang.total" select="'Total: '"/>
			
		<xsl:variable name="lang.hapagado" select="'Modalidad de pago a la que se acogen (sólo una):'"/>
		<xsl:variable name="lang.si" select="'Pago a la familia'"/>
		<!--<xsl:variable name="lang.siNota" select="' (los libros los han adquirido y pagado antes de la fecha de publicación de la convocatoria).'"/>-->
		<xsl:variable name="lang.siNota" select="''"/>
		<xsl:variable name="lang.no" select="'Pago a la librería'"/>
		<!--<xsl:variable name="lang.noNota" select="' (aún no se ha pagado a los proveedores).'"/>-->
		<xsl:variable name="lang.noNota" select="''"/>

		<!--<xsl:variable name="lang.hapagado" select="'¿La familia ha pagado los libros?'"/>
		<xsl:variable name="lang.si" select="'SÍ'"/>
		<xsl:variable name="lang.no" select="'NO'"/>-->

		<xsl:variable name="lang.costeMensual" select="'Coste día / menor (inferior a 4.77 €)'"/>
		<xsl:variable name="lang.mesInicio" select="'Mes de inicio y finalización del comedor'"/>
		<xsl:variable name="lang.mesDesde" select="'Desde '"/>
		<xsl:variable name="lang.mesHasta" select="'hasta '"/>
		<xsl:variable name="lang.costeAnualMenor" select="'Coste anual por menor'"/>
		<xsl:variable name="lang.importeTotalComedor" select="'IMPORTE TOTAL SOLICITADO'"/>

		<xsl:variable name="lang.proveedor" select="'Nombre del proveedor'"/>
		<xsl:variable name="lang.noFactura" select="'Nº de Factura'"/>
		<xsl:variable name="lang.fechaFactura" select="'Fecha'"/>

		<div class="col">
			<label class="gr">
					<xsl:attribute name="style">width:100%;</xsl:attribute>
					<xsl:value-of select="$lang.texto1"/>:
			</label>
			<table style="font-size:12px; margin:5 auto 20 auto;" cellspacing="0" border="1">
				<tr>
					<td style="width:5%;text-align:center" id="idcolumna">
					</td>
					<td style="width:35%;background-color:#dee1e8;text-align:center" id="nombrecolumna">
						<label class="gr">
							<xsl:attribute name="style">width:100%;text-align:center</xsl:attribute>
							<xsl:value-of select="$lang.tabla1"/>
						</label>
					</td>
					<td style="width:15%;background-color:#dee1e8;text-align:center" id="edadcolumna">
						<label class="gr">
							<xsl:attribute name="style">width:100%;text-align:center</xsl:attribute>
							<xsl:value-of select="$lang.tabla2"/>
						</label>
					</td>
					<td style="width:15%;background-color:#dee1e8;text-align:center" id="cursocolumna">
						<label class="gr">
							<xsl:attribute name="id">curso</xsl:attribute>
							<xsl:attribute name="style">width:100%;text-align:center</xsl:attribute>
							<xsl:value-of select="$lang.tabla3"/>
						</label>
						<label class="gr">
							<xsl:attribute name="id">colegio</xsl:attribute>
							<xsl:attribute name="style">width:100%;text-align:center</xsl:attribute>
							<xsl:value-of select="$lang.tabla7"/>
						</label>
					</td>
					<td style="width:30%;background-color:#dee1e8;text-align:center" id="costecolumna">
						<label class="gr">
							<xsl:attribute name="id">costelibros</xsl:attribute>
							<xsl:attribute name="style">width:100%;text-align:center</xsl:attribute>
							<xsl:value-of select="$lang.tabla4"/>
						</label>						
						<label class="gr">
							<xsl:attribute name="id">empresacomedor</xsl:attribute>
							<xsl:attribute name="style">width:100%;text-align:center</xsl:attribute>
							<xsl:value-of select="$lang.tabla5"/>
						</label>
					</td>
					<td style="width:15%;background-color:#dee1e8;text-align:center" id="importelibrossolcolumna">
						<label class="gr">
							<xsl:attribute name="id">importelibrossol</xsl:attribute>
							<xsl:attribute name="style">width:100%;text-align:center</xsl:attribute>
							<xsl:value-of select="$lang.tabla6"/>
						</label>
					</td>
				</tr>
				<tr id="ninio1">
					<td style="width:5%;background-color:#dee1e8;text-align:center">
						<label class="gr">
							<xsl:attribute name="id">id</xsl:attribute>
							<xsl:attribute name="style">width:100%</xsl:attribute>
							1
						</label>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">nombre1</xsl:attribute>
							<xsl:attribute name="id">nombre1</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombre1"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">edad1</xsl:attribute>
							<xsl:attribute name="id">edad1</xsl:attribute>
							<xsl:attribute name="size">10</xsl:attribute>
							<xsl:attribute name="maxlength">10</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/edad1"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">curso1</xsl:attribute>
							<xsl:attribute name="id">curso1</xsl:attribute>
							<xsl:attribute name="size">6</xsl:attribute>
							<xsl:attribute name="maxlength">6</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero1(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/curso1"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">colegio1</xsl:attribute>
							<xsl:attribute name="id">colegio1</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colegio1"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">costelibros1</xsl:attribute>
							<xsl:attribute name="id">costelibros1</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/costelibros1"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">empresacomedor1</xsl:attribute>
							<xsl:attribute name="id">empresacomedor1</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/empresacomedor1"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">importelibrossol1</xsl:attribute>
							<xsl:attribute name="id">importelibrossol1</xsl:attribute>
							<xsl:attribute name="onblur">compruebaMaterial(document.getElementById('curso1'), document.getElementById('costelibros1'), this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/importelibrossol1"/></xsl:attribute>
						</input>
					</td>
				</tr>
				<tr id="ninio2">
					<td style="width:5%;background-color:#dee1e8;text-align:center">
						<label class="gr">
							<xsl:attribute name="id">id2</xsl:attribute>
							<xsl:attribute name="style">width:100%</xsl:attribute>
							2
						</label>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">nombre2</xsl:attribute>
							<xsl:attribute name="id">nombre2</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombre2"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">edad2</xsl:attribute>
							<xsl:attribute name="id">edad2</xsl:attribute>
							<xsl:attribute name="size">10</xsl:attribute>
							<xsl:attribute name="maxlength">10</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/edad2"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">curso2</xsl:attribute>
							<xsl:attribute name="id">curso2</xsl:attribute>
							<xsl:attribute name="size">6</xsl:attribute>
							<xsl:attribute name="maxlength">6</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero1(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/curso2"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">colegio2</xsl:attribute>
							<xsl:attribute name="id">colegio2</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colegio2"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">costelibros2</xsl:attribute>
							<xsl:attribute name="id">costelibros2</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/costelibros2"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">empresacomedor2</xsl:attribute>
							<xsl:attribute name="id">empresacomedor2</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/empresacomedor2"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">importelibrossol2</xsl:attribute>
							<xsl:attribute name="id">importelibrossol2</xsl:attribute>
							<xsl:attribute name="onblur">compruebaMaterial(document.getElementById('curso2'), document.getElementById('costelibros2'), this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/importelibrossol2"/></xsl:attribute>
						</input>
					</td>
				</tr>
				<tr id="ninio3">
					<td style="width:5%;background-color:#dee1e8;text-align:center">
						<label class="gr">
							<xsl:attribute name="id">id3</xsl:attribute>
							<xsl:attribute name="style">width:100%</xsl:attribute>
							3
						</label>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">nombre3</xsl:attribute>
							<xsl:attribute name="id">nombre3</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombre3"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">edad3</xsl:attribute>
							<xsl:attribute name="id">edad3</xsl:attribute>
							<xsl:attribute name="size">10</xsl:attribute>
							<xsl:attribute name="maxlength">10</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/edad3"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">curso3</xsl:attribute>
							<xsl:attribute name="id">curso3</xsl:attribute>
							<xsl:attribute name="size">6</xsl:attribute>
							<xsl:attribute name="maxlength">6</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero1(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/curso3"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">colegio3</xsl:attribute>
							<xsl:attribute name="id">colegio3</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colegio3"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">costelibros3</xsl:attribute>
							<xsl:attribute name="id">costelibros3</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/costelibros3"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">empresacomedor3</xsl:attribute>
							<xsl:attribute name="id">empresacomedor3</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/empresacomedor3"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">importelibrossol3</xsl:attribute>
							<xsl:attribute name="id">importelibrossol3</xsl:attribute>
							<xsl:attribute name="onblur">compruebaMaterial(document.getElementById('curso3'), document.getElementById('costelibros3'), this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/importelibrossol3"/></xsl:attribute>
						</input>
					</td>
				</tr>
				<tr id="ninio4">
					<td style="width:5%;background-color:#dee1e8;text-align:center">
						<label class="gr">
							<xsl:attribute name="id">id4</xsl:attribute>
							<xsl:attribute name="style">width:100%</xsl:attribute>
							4
						</label>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">nombre4</xsl:attribute>
							<xsl:attribute name="id">nombre4</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombre4"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">edad4</xsl:attribute>
							<xsl:attribute name="id">edad4</xsl:attribute>
							<xsl:attribute name="size">10</xsl:attribute>
							<xsl:attribute name="maxlength">10</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/edad4"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">curso4</xsl:attribute>
							<xsl:attribute name="id">curso4</xsl:attribute>
							<xsl:attribute name="size">6</xsl:attribute>
							<xsl:attribute name="maxlength">6</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero1(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/curso4"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">colegio4</xsl:attribute>
							<xsl:attribute name="id">colegio4</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colegio4"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">costelibros4</xsl:attribute>
							<xsl:attribute name="id">costelibros4</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/costelibros4"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">empresacomedor4</xsl:attribute>
							<xsl:attribute name="id">empresacomedor4</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/empresacomedor4"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">importelibrossol4</xsl:attribute>
							<xsl:attribute name="id">importelibrossol4</xsl:attribute>
							<xsl:attribute name="onblur">compruebaMaterial(document.getElementById('curso4'), document.getElementById('costelibros4'), this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/importelibrossol4"/></xsl:attribute>
						</input>
					</td>
					
				</tr>
				<tr id="ninio5">
					<td style="width:5%;background-color:#dee1e8;text-align:center">
						<label class="gr">
							<xsl:attribute name="id">id5</xsl:attribute>
							<xsl:attribute name="style">width:100%</xsl:attribute>
							5
						</label>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">nombre5</xsl:attribute>
							<xsl:attribute name="id">nombre5</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombre5"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">edad5</xsl:attribute>
							<xsl:attribute name="id">edad5</xsl:attribute>
							<xsl:attribute name="size">10</xsl:attribute>
							<xsl:attribute name="maxlength">10</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/edad5"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">curso5</xsl:attribute>
							<xsl:attribute name="id">curso5</xsl:attribute>
							<xsl:attribute name="size">6</xsl:attribute>
							<xsl:attribute name="maxlength">6</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero1(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/curso5"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">colegio5</xsl:attribute>
							<xsl:attribute name="id">colegio5</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colegio5"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">costelibros5</xsl:attribute>
							<xsl:attribute name="id">costelibros5</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/costelibros5"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">empresacomedor5</xsl:attribute>
							<xsl:attribute name="id">empresacomedor5</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/empresacomedor5"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">importelibrossol5</xsl:attribute>
							<xsl:attribute name="id">importelibrossol5</xsl:attribute>
							<xsl:attribute name="onblur">compruebaMaterial(document.getElementById('curso5'), document.getElementById('costelibros5'), this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/importelibrossol5"/></xsl:attribute>
						</input>
					</td>
				</tr>
				<tr id="ninio6">
					<td style="width:5%;background-color:#dee1e8;text-align:center">
						<label class="gr">
							<xsl:attribute name="id">id6</xsl:attribute>
							<xsl:attribute name="style">width:100%</xsl:attribute>
							6
						</label>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">nombre6</xsl:attribute>
							<xsl:attribute name="id">nombre6</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombre6"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">edad6</xsl:attribute>
							<xsl:attribute name="id">edad6</xsl:attribute>
							<xsl:attribute name="size">10</xsl:attribute>
							<xsl:attribute name="maxlength">10</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/edad6"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">curso6</xsl:attribute>
							<xsl:attribute name="id">curso6</xsl:attribute>
							<xsl:attribute name="size">6</xsl:attribute>
							<xsl:attribute name="maxlength">6</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero1(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/curso6"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">colegio6</xsl:attribute>
							<xsl:attribute name="id">colegio6</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colegio6"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">costelibros6</xsl:attribute>
							<xsl:attribute name="id">costelibros6</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/costelibros6"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">empresacomedor6</xsl:attribute>
							<xsl:attribute name="id">empresacomedor6</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/empresacomedor6"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">importelibrossol6</xsl:attribute>
							<xsl:attribute name="id">importelibrossol6</xsl:attribute>
							<xsl:attribute name="onblur">compruebaMaterial(document.getElementById('curso6'), document.getElementById('costelibros6'), this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/importelibrossol6"/></xsl:attribute>
						</input>
					</td>
				</tr>
				<tr id="ninio7">
					<td style="width:5%;background-color:#dee1e8;text-align:center">
						<label class="gr">
							<xsl:attribute name="id">id7</xsl:attribute>
							<xsl:attribute name="style">width:100%</xsl:attribute>
							7
						</label>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">nombre7</xsl:attribute>
							<xsl:attribute name="id">nombre7</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombre7"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">edad7</xsl:attribute>
							<xsl:attribute name="id">edad7</xsl:attribute>
							<xsl:attribute name="size">10</xsl:attribute>
							<xsl:attribute name="maxlength">10</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/edad7"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">curso7</xsl:attribute>
							<xsl:attribute name="id">curso7</xsl:attribute>
							<xsl:attribute name="size">6</xsl:attribute>
							<xsl:attribute name="maxlength">6</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero1(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/curso7"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">colegio7</xsl:attribute>
							<xsl:attribute name="id">colegio7</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colegio7"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">costelibros7</xsl:attribute>
							<xsl:attribute name="id">costelibros7</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/costelibros7"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">empresacomedor7</xsl:attribute>
							<xsl:attribute name="id">empresacomedor7</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/empresacomedor7"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">importelibrossol7</xsl:attribute>
							<xsl:attribute name="id">importelibrossol7</xsl:attribute>
							<xsl:attribute name="onblur">compruebaMaterial(document.getElementById('curso7'), document.getElementById('costelibros7'), this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/importelibrossol7"/></xsl:attribute>
						</input>
					</td>
				</tr>
				<tr id="ninio8">
					<td style="width:5%;background-color:#dee1e8;text-align:center">
						<label class="gr">
							<xsl:attribute name="id">id8</xsl:attribute>
							<xsl:attribute name="style">width:100%</xsl:attribute>
							8
						</label>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">nombre8</xsl:attribute>
							<xsl:attribute name="id">nombre8</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombre8"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">edad8</xsl:attribute>
							<xsl:attribute name="id">edad8</xsl:attribute>
							<xsl:attribute name="size">10</xsl:attribute>
							<xsl:attribute name="maxlength">10</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/edad8"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">colegio8</xsl:attribute>
							<xsl:attribute name="id">colegio8</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colegio8"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">curso8</xsl:attribute>
							<xsl:attribute name="id">curso8</xsl:attribute>
							<xsl:attribute name="size">6</xsl:attribute>
							<xsl:attribute name="maxlength">6</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero1(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/curso8"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">costelibros8</xsl:attribute>
							<xsl:attribute name="id">costelibros8</xsl:attribute>
							<xsl:attribute name="onblur">compruebaNumero(this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/costelibros8"/></xsl:attribute>
						</input>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">empresacomedor8</xsl:attribute>
							<xsl:attribute name="id">empresacomedor8</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/empresacomedor8"/></xsl:attribute>
						</input>
					</td>
					<td>
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">importelibrossol8</xsl:attribute>
							<xsl:attribute name="id">importelibrossol8</xsl:attribute>
							<xsl:attribute name="onblur">compruebaMaterial(document.getElementById('curso8'), document.getElementById('costelibros8'), this);</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/importelibrossol8"/></xsl:attribute>
						</input>
					</td>
				</tr>
				<tr id="filatotal">
					<td colspan='5' align='right'>
						<label class="gr">
							<xsl:attribute name="style">width:100%;text-align:right</xsl:attribute>
							<xsl:value-of select="$lang.total"/>
						</label>
					</td>
					<td valign="middle">
						<input type="">
							<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
							<xsl:attribute name="name">totallibros</xsl:attribute>
							<xsl:attribute name="id">totallibros</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/totallibros"/></xsl:attribute>
						</input>
					</td>
				</tr>
			</table>
			<script>if(document.getElementById('totallibros').value == '' || parseFloat(document.getElementById('totallibros').value) == 'NaN') document.getElementById('totallibros').value = 0;</script>
		</div>
		<div class="col" id="datosLibros">
			<label class="gr">
					<xsl:attribute name="style">width:100%;</xsl:attribute>
					<xsl:value-of select="$lang.hapagado"/>:
			</label>
			<br/>
			<label>
				<input type = "radio">
					<xsl:attribute name="style">border:none;width:20px; </xsl:attribute>
					<xsl:attribute name="name">rdPagado</xsl:attribute>
					<xsl:attribute name="id">rdPagado</xsl:attribute>
					<xsl:attribute name="onclick">document.getElementById('pagado').value='F';</xsl:attribute>
				</input>	
			
				<b><xsl:value-of select="$lang.si"/></b><xsl:value-of select="$lang.siNota"/>							
			</label>		
			<br/>
			<label>
				<input type = "radio">
					<xsl:attribute name="style">border:none;width:20px; </xsl:attribute>
					<xsl:attribute name="name">rdPagado</xsl:attribute>
					<xsl:attribute name="id">rdPagado</xsl:attribute>
					<xsl:attribute name="onclick">document.getElementById('pagado').value='L';</xsl:attribute>
				</input>
				<b><xsl:value-of select="$lang.no"/></b><xsl:value-of select="$lang.noNota"/>
			</label>
			<input type = "hidden">
				<xsl:attribute name="name">pagado</xsl:attribute>
				<xsl:attribute name="id">pagado</xsl:attribute>
			</input>
		</div>
		<div class="col" id="datosLibros2" style="display:none">
			<label class="gr">
					<xsl:attribute name="style">width:25%;</xsl:attribute>
					<xsl:value-of select="$lang.proveedor"/>:
			</label>
			<input type="">
				<xsl:attribute name="style">width:80%; </xsl:attribute>
				<xsl:attribute name="name">nombreProveedor</xsl:attribute>
				<xsl:attribute name="id">nombreProveedor</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nombreProveedor"/></xsl:attribute>
			</input>
			<br/>
			<label class="gr">
					<xsl:attribute name="style">width:25%;</xsl:attribute>
					<xsl:value-of select="$lang.noFactura"/>:
			</label>
			<input type="">
				<xsl:attribute name="style">width:50%; </xsl:attribute>
				<xsl:attribute name="name">noFactura</xsl:attribute>
				<xsl:attribute name="id">noFactura</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/noFactura"/></xsl:attribute>
			</input>
			<br/>
			<label class="gr">
					<xsl:attribute name="style">width:25%;</xsl:attribute>
					<xsl:value-of select="$lang.fechaFactura"/>:
			</label>
			<input type="">
				<xsl:attribute name="style">width:20%; </xsl:attribute>
				<xsl:attribute name="name">fechaFactura</xsl:attribute>
				<xsl:attribute name="id">fechaFactura</xsl:attribute>
				<xsl:attribute name="size">12</xsl:attribute>
				<xsl:attribute name="maxlength">12</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/fechaFactura"/></xsl:attribute>
			</input>
			<br/>
		</div>
		<div class="col" id="datosComedor">			
			<label class="gr">
				<xsl:attribute name="style">width:35%;</xsl:attribute>
				<xsl:value-of select="$lang.costeMensual"/>:
			</label>
			<input type="">
				<xsl:attribute name="style">width:20%;</xsl:attribute>
				<xsl:attribute name="name">costeMensual</xsl:attribute>
				<xsl:attribute name="size">6</xsl:attribute>
				<xsl:attribute name="maxlength">6</xsl:attribute>
				<xsl:attribute name="id">costeMensual</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/costeMensual"/></xsl:attribute>
			</input> €
			<br/>
			<label class="gr">
				<xsl:attribute name="style">width:35%;</xsl:attribute>
				<xsl:value-of select="$lang.mesInicio"/>:
			</label>
			<br/>
			<label for='mesInicio' class="gr">
				<xsl:attribute name="style">width:30%;</xsl:attribute>
				<xsl:value-of select="$lang.mesDesde"/>	
		
				<input type="">
					<xsl:attribute name="style">width:70%;</xsl:attribute>
					<xsl:attribute name="name">mesInicio</xsl:attribute>
					<xsl:attribute name="id">mesInicio</xsl:attribute>
					<xsl:attribute name="size">15</xsl:attribute>
					<xsl:attribute name="maxlength">15</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/mesInicio"/></xsl:attribute>
				</input>
			</label>
			<label for='mesesSolComedor' class="gr">
				<xsl:attribute name="style">width:30%;</xsl:attribute>
				<xsl:value-of select="$lang.mesHasta"/>
			
				<input type="">
					<xsl:attribute name="style">width:70%;</xsl:attribute>
					<xsl:attribute name="name">mesesSolComedor</xsl:attribute>
					<xsl:attribute name="id">mesesSolComedor</xsl:attribute>
					<xsl:attribute name="size">15</xsl:attribute>
					<xsl:attribute name="maxlength">15</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/mesesSolComedor"/></xsl:attribute>
				</input>
			</label>
			<br/>
			<label class="gr">
				<xsl:attribute name="style">width:30%;</xsl:attribute>
				<xsl:value-of select="$lang.costeAnualMenor"/>:
			</label>
			<input type="">
				<xsl:attribute name="style">width:20%;</xsl:attribute>
				<xsl:attribute name="name">costeAnualMenor</xsl:attribute>
				<xsl:attribute name="id">costeAnualMenor</xsl:attribute>
				<xsl:attribute name="size">6</xsl:attribute>
				<xsl:attribute name="maxlength">6</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/costeAnualMenor"/></xsl:attribute>
			</input> €
			<br/>
			<label class="gr">
				<xsl:attribute name="style">width:30%;</xsl:attribute>
				<xsl:value-of select="$lang.importeTotalComedor"/>:
			</label>
			<input type="">
				<xsl:attribute name="style">width:20%;</xsl:attribute>
				<xsl:attribute name="name">importeTotalComedor</xsl:attribute>
				<xsl:attribute name="id">importeTotalComedor</xsl:attribute>
				<xsl:attribute name="size">6</xsl:attribute>
				<xsl:attribute name="maxlength">6</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/importeTotalComedor"/></xsl:attribute>
			</input> €
			<br/>
		</div>

		<script>
			function compruebaNumero1(coste){
				if(coste.value != ''){
					if(!numerico(coste.value)){
						alert('El importe debe ser numérico, sin separador de miles y para indicar los decimales debe usarse el punto.');
						coste.focus();
						return false;
					}
					else{
						var iCoste = parseFloat(coste.value);
						if(25 &lt; iCoste){
							coste.value='25';
						}
					}
				}
				else{
					coste.value = '0';
				}
			}

			function compruebaNumero(coste){
				if(coste.value != ''){
					if(!numerico(coste.value)){
						alert('El importe debe ser numérico, sin separador de miles y para indicar los decimales debe usarse el punto.');
						coste.focus();
						return false;
					}
				}
				else{
					coste.value = '0';
				}
			}

			function compruebaMaterial(costeMaterial, costeLibros, importeSolicitado){
				if(costeMaterial.value != ''){
					if(!numerico(costeMaterial.value)){
						alert('El importe de material escolar debe ser numérico, sin separador de miles y para indicar los decimales debe usarse el punto.');
						costeMaterial.focus();
						return false;
					}
				}
				else{
					alert('Debe indicar el importe del material escolar');
					costeMaterial.focus();
					return false;
				}

				if(costeLibros.value != ''){
					if(!numerico(costeLibros.value)){
						alert('El coste de los libros debe ser numérico, sin separador de miles y para indicar los decimales debe usarse el punto.');
						costeLibros.focus();
						return false;
					}
				}
				else{
					alert('Debe indicar el coste de los libros');
					costeLibros.focus();
					return false;
				}

				if(importeSolicitado.value != ''){
					if(!numerico(importeSolicitado.value)){
						alert('El coste de los libros debe ser numérico, sin separador de miles y para indicar los decimales debe usarse el punto.');
						importeSolicitado.focus();
						return false;
					}
				}
				else{
					alert('Debe indicar el importe solicitado');
					importeSolicitado.focus();
					return false;
				}

				var iMaterialEscolar = parseFloat(costeMaterial.value);
				var iCosteLibros = parseFloat(costeLibros.value);
				var iImporteSolicitado = parseFloat(importeSolicitado.value);

				if((iCosteLibros + iMaterialEscolar) &lt; iImporteSolicitado){
					importeSolicitado.value = (iCosteLibros + iMaterialEscolar);
				}

				if(importeSolicitado.value &gt; 125){
					alert('El importe máximo por niño es de 125 €');
					importeSolicitado.value = 125;
				}

				document.getElementById('totallibros').value = '0'; 
			
				for(var i = 1; i&lt; 9; i++){
					var importe = document.getElementById('importelibrossol' + i ).value;
					if(importe != '') document.getElementById('totallibros').value = parseFloat(document.getElementById('totallibros').value) + parseFloat(importe);
				}

				if(parseFloat(document.getElementById('totallibros').value) &gt; 500){
					alert('El máximo por familia son 500 €');
					document.getElementById('totallibros').value = '500'
				}
			}
		</script>
	
	</xsl:template>

	<xsl:template name="TABLA_FAMILIA">
		<xsl:variable name="lang.composicion_familiar" select="'Composición familiar'"/>
		<xsl:variable name="lang.parentesco" select="'PARENTESCO'"/>
		<xsl:variable name="lang.edad" select="'EDAD'"/>
		<xsl:variable name="lang.profesion" select="'PROFESIÓN'"/>
		<xsl:variable name="lang.situacionLab" select="'SITUACIÓN LABORAL'"/>
		<xsl:variable name="lang.ingresos" select="'INGRESOS/MES'"/>
		
		<div class="col">
			<label class="gr">
				<xsl:attribute name="style">width:150px;</xsl:attribute>
				<xsl:value-of select="$lang.composicion_familiar"/>:
			</label>
		</div>
		<div class="">
			<table style="font-size:12px; margin:5 auto 20 auto;" cellspacing="0" border="1">
				<tr>
					<td style="width:20%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.parentesco"/></td>
					<td style="width:10%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.edad"/></td>
					<td style="width:30%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.profesion"/></td>
					<td style="width:20%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.situacionLab"/></td>
					<td style="width:20%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.ingresos"/></td>
				</tr>

				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">1</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">2</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">3</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">4</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">5</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">6</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">7</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">8</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">9</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">10</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">11</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">12</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">13</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">14</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">15</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">16</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">17</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">18</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">19</xsl:with-param></xsl:call-template>
				<!--<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">20</xsl:with-param></xsl:call-template>-->
			</table>
			<div style="margin:5px 0 20px 10px;">
	              	<a id="addRow_link" style="cursor:pointer" onclick="addRow()">[Añadir fila]</a><br/>
       		</div>
		</div>
	</xsl:template>

	<xsl:template name="FIELDS">
		<xsl:param name="row_id" />
			<xsl:variable name="param_parentesco">par_<xsl:value-of select="$row_id"/></xsl:variable>
			<xsl:variable name="param_edad">edad_<xsl:value-of select="$row_id"/></xsl:variable>
			<xsl:variable name="param_profesion">prof_<xsl:value-of select="$row_id"/></xsl:variable>
			<xsl:variable name="param_situLab">sit_<xsl:value-of select="$row_id"/></xsl:variable>
			<xsl:variable name="param_ingresos">ing_<xsl:value-of select="$row_id"/></xsl:variable>
			<xsl:variable name="row_style">
		<xsl:choose>
	       <xsl:when test="$row_id&gt;'5'">
   		        display:none;
       	</xsl:when>
		</xsl:choose>
		</xsl:variable>
		
		<tr id="row_{$row_id}" style="{$row_style}">
			<td>
		       	<input type="text">
					<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param_parentesco"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param_parentesco"/></xsl:attribute>
					<xsl:attribute name="maxlength">17</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_parentesco]"/></xsl:attribute>
				</input>
			</td>
			<td>
				<input type="text">
					<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param_edad"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param_edad"/></xsl:attribute>
					<xsl:attribute name="maxlength">4</xsl:attribute>
					<xsl:attribute name="size">4</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_edad]"/></xsl:attribute>
				</input>
			</td>
			<td>
				<input type="text">
					<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param_profesion"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param_profesion"/></xsl:attribute>
					<xsl:attribute name="maxlength">17</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_profesion]"/></xsl:attribute>
				</input>
			</td>
			<td>
				<input type="text">
					<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param_situLab"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param_situLab"/></xsl:attribute>
					<xsl:attribute name="maxlength">17</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_situLab]"/></xsl:attribute>
				</input>
			</td>
			<td>
				<input type="text">
					<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param_ingresos"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param_ingresos"/></xsl:attribute>
					<xsl:attribute name="maxlength">17</xsl:attribute>
					<xsl:attribute name="onchange">document.getElementById('total').value = parseFloat(this.value) + parseFloat(document.getElementById('total').value);</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_ingresos]"/></xsl:attribute>
				</input>
			</td>
		</tr>	
	</xsl:template>


	<xsl:template name="DATOS_PROPUESTA_1">
		<xsl:variable name="lang.propuesta" select="'PROPUESTA DE AYUDA ECONÓMICA'"/>
		<xsl:variable name="lang.semestre1" select="'SEMESTRE DEL 1 DE JUNIO DE 2012 A 30 DE NOVIEMBRE DE 2012'"/>
		<xsl:variable name="lang.propuesta1" select="'Propuesta'"/>		
		<xsl:variable name="lang.propuesta2" select="'Segunda propuesta'"/>		
		<xsl:variable name="lang.propuesta3" select="'Tercera propuesta'"/>		

		<xsl:variable name="lang.semestre2" select="'SEMESTRE DEL 1 DE DICIEMBRE DE 2012 A 31 DE MAYO DE 2013'"/>

		<xsl:variable name="lang.fecha" select="' - Fecha: *'"/>
		<xsl:variable name="lang.meses" select="' - Mes a cubrir: *'"/>
		<xsl:variable name="lang.meses2" select="' - Concepto:'"/>
		<xsl:variable name="lang.importe" select="' - Importe solicitado: *'"/>
	
   		<div class="cuadro" style="" id="datosPropuesta">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><script>
							var d = new Date();
							var n = d.getMonth();
							if ( n == 0 || n==1 || n==2){
								document.write('PRIMER TRIMESTRE');
							}
							else if ( n == 3 || n== 4 || n== 5){
								document.write('SEGUNDO TRIMESTRE');
							}
							else if ( n == 6 || n==7 || n==8){
								document.write('TERCER TRIMESTRE');
							}
							else{
								document.write('CUARTO TRIMESTRE');
							}
					</script></b>
				</label>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.propuesta"/></b>
				</label>
				<br/>
				
			</div>
			<div class="col" id="primeraProp">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<xsl:value-of select="$lang.propuesta1"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.fecha"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">propuesta1_fecha</xsl:attribute>
					<xsl:attribute name="id">propuesta1_fecha</xsl:attribute>
					<xsl:attribute name="maxlength">15</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta1_fecha"/></xsl:attribute>
				</input>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:attribute name="id">etiquetaMeses</xsl:attribute>
					<xsl:value-of select="$lang.meses"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:400px; </xsl:attribute>
					<xsl:attribute name="name">propuesta1_meses</xsl:attribute>
					<xsl:attribute name="id">propuesta1_meses</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta1_meses"/></xsl:attribute>
				</input>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.importe"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">propuesta1_importe</xsl:attribute>
					<xsl:attribute name="id">propuesta1_importe</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta1_importe"/></xsl:attribute>
				</input>€
			</div>

				<label class="gr">
					<xsl:attribute name="style">width:150px; visibility:hidden;</xsl:attribute>
					<xsl:value-of select="$lang.fecha"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; visibility:hidden; </xsl:attribute>
					<xsl:attribute name="name">propuesta2_fecha</xsl:attribute>
					<xsl:attribute name="id">propuesta2_fecha</xsl:attribute>
					<xsl:attribute name="maxlength">15</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta2_fecha"/></xsl:attribute>
				</input>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">width:150px; visibility:hidden;</xsl:attribute>
					<xsl:value-of select="$lang.meses"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:400px; visibility:hidden; </xsl:attribute>
					<xsl:attribute name="name">propuesta2_meses</xsl:attribute>
					<xsl:attribute name="id">propuesta2_meses</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta2_meses"/></xsl:attribute>
				</input>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">width:150px; visibility:hidden;</xsl:attribute>
					<xsl:value-of select="$lang.importe"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; visibility:hidden; </xsl:attribute>
					<xsl:attribute name="name">propuesta2_importe</xsl:attribute>
					<xsl:attribute name="id">propuesta2_importe</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta2_importe"/></xsl:attribute>
				</input>


				<input type="hidden">
					<xsl:attribute name="style">width:70px; visibility:hidden; </xsl:attribute>
					<xsl:attribute name="name">propuesta3_fecha</xsl:attribute>
					<xsl:attribute name="id">propuesta3_fecha</xsl:attribute>
					<xsl:attribute name="maxlength">15</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta3_fecha"/></xsl:attribute>
				</input>

				<input type="hidden">
					<xsl:attribute name="style">width:400px; visibility:hidden; </xsl:attribute>
					<xsl:attribute name="name">propuesta3_meses</xsl:attribute>
					<xsl:attribute name="id">propuesta3_meses</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta3_meses"/></xsl:attribute>
				</input>

				<input type="hidden">
					<xsl:attribute name="style">width:70px; visibility:hidden; </xsl:attribute>
					<xsl:attribute name="name">propuesta3_importe</xsl:attribute>
					<xsl:attribute name="id">propuesta3_importe</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta3_importe"/></xsl:attribute>
				</input>	

		</div>		
	</xsl:template>

	<xsl:template name="DATOS_FAMILIA2">

		<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
		<xsl:variable name="lang.nfamiliar" select="'Número de miembros de la unidad familiar'"/>
		<xsl:variable name="lang.menores3anios" select="'Menores de 3 años inclusive'"/>		

		<xsl:variable name="lang.total" select="'Total'"/>

		<xsl:variable name="lang.tipoFamilia" select="'Tipo de familia'"/>
		<xsl:variable name="lang.colectivo" select="'Colectivo'"/>
		<xsl:variable name="lang.vivienda" select="'Vivienda'"/>
		<xsl:variable name="lang.diagnostico" select="'Diagnóstico inicial/Valoración Social *'"/>

		<xsl:variable name="lang.semestre2" select="'SEMESTRE DEL 1 DE DICIEMBRE DE 2012 A 31 DE MAYO DE 2013'"/>
		<xsl:variable name="lang.semestre2Info" select="'(Rellenar sólo en caso de variación respecto al primer trimestre hasta el apartado DIAGNÓSTICO/ VALORACIÓN SOCIAL)'"/>

		<xsl:variable name="lang.propuesta" select="'PROPUESTA DE AYUDA ECONÓMICA'"/>

		<xsl:variable name="lang.propuesta1" select="'Propuesta'"/>		
		<xsl:variable name="lang.propuesta2" select="'Segunda propuesta'"/>		
		<xsl:variable name="lang.propuesta3" select="'Tercera propuesta'"/>		

		<xsl:variable name="lang.fecha" select="' - Fecha:'"/>
		<xsl:variable name="lang.meses" select="' - Mes a cubrir:'"/>
		<xsl:variable name="lang.importe" select="' - Importe solicitado:'"/>

   		<div class="cuadro" style="">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.semestre2"/></b>
				</label>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">width:600px;font-size:9px</xsl:attribute>
					<i><xsl:value-of select="$lang.semestre2Info"/></i>
				</label>
			</div>
   	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nfamiliar"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">nfamiliar2</xsl:attribute>
					<xsl:attribute name="id">nfamiliar2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nfamiliar2"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.menores3anios"/>:
				</label>
				Sí
				<input type="radio">
					<xsl:attribute name="style">width:20px; </xsl:attribute>
					<xsl:attribute name="name">menores3aniosrd2</xsl:attribute>
					<xsl:attribute name="id">menores3aniosrd2</xsl:attribute>
					<xsl:attribute name="onclick">document.getElementById("menores3anios2").value='SI'</xsl:attribute>
				</input>
				No
				<input type="radio">
					<xsl:attribute name="style">width:20px; </xsl:attribute>
					<xsl:attribute name="name">menores3aniosrd2</xsl:attribute>
					<xsl:attribute name="id">menores3aniosrd2</xsl:attribute>
					<xsl:attribute name="onclick">document.getElementById("menores3anios2").value='NO'</xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">menores3anios2</xsl:attribute>
					<xsl:attribute name="id">menores3anios2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/menores3anios2"/></xsl:attribute>
				</input>
			</div>	
			<xsl:call-template name="TABLA_FAMILIA2" />
			<div class="col" align='right'>
				<label class="gr">
					<xsl:attribute name="style">width:520px;</xsl:attribute>
					<xsl:value-of select="$lang.total"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:120px; </xsl:attribute>
					<xsl:attribute name="name">total2</xsl:attribute>
					<xsl:attribute name="id">total2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/total2"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.diagnostico"/>:
				</label>
				<br/>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:620px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">diagnostico2</xsl:attribute>
					<xsl:attribute name="id">diagnostico2</xsl:attribute>
					<xsl:attribute name="rows">10</xsl:attribute>
					<xsl:attribute name="onkeypress">if(this.value.length&gt;25500){ alert('Tamaño máximo de campo alcanzado'); this.value = this.value.substr(0,25500);}</xsl:attribute>
					<xsl:attribute name="onkeyup">if(this.value.length&gt;25500){ alert('Tamaño máximo de campo alcanzado'); this.value = this.value.substr(0,25500);}</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/diagnostico2"/>
				</textarea>
				<br/>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<b><xsl:value-of select="$lang.propuesta"/></b>
				</label>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<xsl:value-of select="$lang.propuesta1"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.fecha"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">propuesta1_fecha2</xsl:attribute>
					<xsl:attribute name="id">propuesta1_fecha2</xsl:attribute>
					<xsl:attribute name="maxlength">15</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta1_fecha2"/></xsl:attribute>
				</input>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.meses"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:400px; </xsl:attribute>
					<xsl:attribute name="name">propuesta1_meses2</xsl:attribute>
					<xsl:attribute name="id">propuesta1_meses2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta1_meses2"/></xsl:attribute>
				</input>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.importe"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">propuesta1_importe2</xsl:attribute>
					<xsl:attribute name="id">propuesta1_importe2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta1_importe2"/></xsl:attribute>
				</input>
			</div>

			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<xsl:value-of select="$lang.propuesta2"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.fecha"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">propuesta2_fecha2</xsl:attribute>
					<xsl:attribute name="id">propuesta2_fecha2</xsl:attribute>
					<xsl:attribute name="maxlength">15</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta2_fecha2"/></xsl:attribute>
				</input>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.meses"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:400px; </xsl:attribute>
					<xsl:attribute name="name">propuesta2_meses2</xsl:attribute>
					<xsl:attribute name="id">propuesta2_meses2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta2_meses2"/></xsl:attribute>
				</input>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.importe"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">propuesta2_importe2</xsl:attribute>
					<xsl:attribute name="id">propuesta2_importe2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta2_importe2"/></xsl:attribute>
				</input>
			</div>


			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:600px;</xsl:attribute>
					<xsl:value-of select="$lang.propuesta3"/>
				</label>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.fecha"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">propuesta3_fecha2</xsl:attribute>
					<xsl:attribute name="id">propuesta3_fecha2</xsl:attribute>
					<xsl:attribute name="maxlength">15</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta3_fecha2"/></xsl:attribute>
				</input>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.meses"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:400px; </xsl:attribute>
					<xsl:attribute name="name">propuesta3_meses2</xsl:attribute>
					<xsl:attribute name="id">propuesta3_meses2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta3_meses2"/></xsl:attribute>
				</input>
				<br/>
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.importe"/>
				</label>
				<input type="text">
					<xsl:attribute name="style">width:70px; </xsl:attribute>
					<xsl:attribute name="name">propuesta3_importe2</xsl:attribute>
					<xsl:attribute name="id">propuesta3_importe2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/propuesta3_importe2"/></xsl:attribute>
				</input>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="TABLA_FAMILIA2">
		<xsl:variable name="lang.composicion_familiar" select="'Composición familiar'"/>
		<xsl:variable name="lang.parentesco" select="'PARENTESCO'"/>
		<xsl:variable name="lang.edad" select="'EDAD'"/>
		<xsl:variable name="lang.profesion" select="'PROFESIÓN'"/>
		<xsl:variable name="lang.situacionLab" select="'SITUACIÓN LABORAL'"/>
		<xsl:variable name="lang.ingresos" select="'INGRESOS/MES'"/>
		
		<div class="col">
			<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.composicion_familiar"/>:
			</label>
		</div>
		<div class="">
			<table style="font-size:12px; margin:5 auto 20 auto;" cellspacing="0" border="1">
				<tr>
					<td style="width:20%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.parentesco"/></td>
					<td style="width:10%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.edad"/></td>
					<td style="width:30%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.profesion"/></td>
					<td style="width:20%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.situacionLab"/></td>
					<td style="width:20%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.ingresos"/></td>
				</tr>

				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">1</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">2</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">3</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">4</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">5</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">6</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">7</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">8</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">9</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">10</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">11</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">12</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">13</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">14</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">15</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">16</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">17</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">18</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">19</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row2_id">20</xsl:with-param></xsl:call-template>
			</table>
			<div style="margin:5px 0 20px 10px;">
	              	<a id="addRow2_link" style="cursor:pointer" onclick="addRow2()">[Añadir fila]</a><br/>
       		</div>
		</div>
	</xsl:template>

	<xsl:template name="FIELDS2">
		<xsl:param name="row2_id" />
			<xsl:variable name="param2_parentesco">par2_<xsl:value-of select="$row2_id"/></xsl:variable>
			<xsl:variable name="param2_edad">edad2_<xsl:value-of select="$row2_id"/></xsl:variable>
			<xsl:variable name="param2_profesion">prof2_<xsl:value-of select="$row2_id"/></xsl:variable>
			<xsl:variable name="param2_situLab">sit2_<xsl:value-of select="$row2_id"/></xsl:variable>
			<xsl:variable name="param2_ingresos">ing2_<xsl:value-of select="$row2_id"/></xsl:variable>
			<xsl:variable name="row_style">
		<xsl:choose>
	       <xsl:when test="$row2_id&gt;'5'">
   		        display:none;
       	</xsl:when>
		</xsl:choose>
		</xsl:variable>
		
		<tr id="row2_{$row2_id}" style="{$row_style}">
			<td>
		       	<input type="text">
					<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param2_parentesco"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param2_parentesco"/></xsl:attribute>
					<xsl:attribute name="maxlength">17</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param2_parentesco]"/></xsl:attribute>
				</input>
			</td>
			<td>
				<input type="text">
					<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param2_edad"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param2_edad"/></xsl:attribute>
					<xsl:attribute name="maxlength">4</xsl:attribute>
					<xsl:attribute name="size">4</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param2_edad]"/></xsl:attribute>
				</input>
			</td>
			<td>
				<input type="text">
					<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param2_profesion"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param2_profesion"/></xsl:attribute>
					<xsl:attribute name="maxlength">17</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param2_profesion]"/></xsl:attribute>
				</input>
			</td>
			<td>
				<input type="text">
					<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param2_situLab"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param2_situLab"/></xsl:attribute>
					<xsl:attribute name="maxlength">17</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param2_situLab]"/></xsl:attribute>
				</input>
			</td>
			<td>
				<input type="text">
					<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param2_ingresos"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param2_ingresos"/></xsl:attribute>
					<xsl:attribute name="maxlength">17</xsl:attribute>					
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param2_ingresos]"/></xsl:attribute>
				</input>
			</td>
		</tr>	
	</xsl:template>

	<xsl:template name="BOTON_LIMPIAR_FORMULARIO">
		<input type='button' onclick='limpiar()' value='Limpiar Formulario'/>
		<script>
			function limpiar(){
				var auxNombreSoli = document.getElementById('nombreSolicitante').value;
				var auxNifSoli = document.getElementById('documentoIdentidad').value;
				var frm_elements = document.forms[0].elements;
				var i = 0;
				while (i != frm_elements.length){
					field_type = frm_elements[i].type.toLowerCase();    
					switch (field_type){    
						case "text":    
						case "password":    
						case "textarea":    
						case "hidden":        
							frm_elements[i].value = "";        
							break;    
						case "radio":    
						case "checkbox":        
							if (frm_elements[i].checked){
	       		     				frm_elements[i].checked = false;        
							}        
							break;    
						case "select-one":    
						case "select-multi":        
							frm_elements[i].selectedIndex = 0;        
							break;    
						default:        
							break;
					}
					i++;
				}
				document.getElementById('nombreSolicitante').value = auxNombreSoli;
				document.getElementById('documentoIdentidad').value = auxNifSoli;

				document.getElementById('par_1').value='SOLICITANTE';
				document.getElementById('par_2').value='CÓNYUGE';

				document.getElementById('total').value=0;
			}
		</script>
	</xsl:template>


</xsl:stylesheet>
