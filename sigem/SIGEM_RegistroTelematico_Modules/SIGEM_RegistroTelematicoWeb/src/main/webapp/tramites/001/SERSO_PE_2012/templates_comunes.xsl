<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="ADD_ROW">
		var last_row = new Number(5);
		var max_num_rows = new Number(20);
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
		var max_num_rows2 = new Number(20);
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

		<xsl:variable name="lang.total" select="'Total'"/>

		<xsl:variable name="lang.tipoFamilia" select="'Tipo de familia'"/>
		<xsl:variable name="lang.colectivo" select="'Colectivo'"/>
		<xsl:variable name="lang.vivienda" select="'Vivienda'"/>
		<xsl:variable name="lang.diagnostico" select="'Diagnóstico inicial/Valoración Social'"/>
		<xsl:variable name="lang.diagnostico2" select="'Informe Social Completo'"/>

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

		<div class="cuadro" style="">	
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
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.menores3anios"/>:
				</label>
				Sí
				<input type="radio">
					<xsl:attribute name="style">width:20px; </xsl:attribute>
					<xsl:attribute name="name">menores3aniosrd</xsl:attribute>
					<xsl:attribute name="id">menores3aniosrd</xsl:attribute>
					<xsl:attribute name="onclick">document.getElementById("menores3anios").value='SI'</xsl:attribute>
				</input>
				No
				<input type="radio">
					<xsl:attribute name="style">width:20px; </xsl:attribute>
					<xsl:attribute name="name">menores3aniosrd</xsl:attribute>
					<xsl:attribute name="id">menores3aniosrd</xsl:attribute>
					<xsl:attribute name="onclick">document.getElementById("menores3anios").value='NO'</xsl:attribute>
				</input>
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
				<label>      - </label>
				<input type="checkbox">
					<xsl:attribute name="style">width:17px; </xsl:attribute>
					<xsl:attribute name="name">totalFamilia1</xsl:attribute>
					<xsl:attribute name="id">totalFamilia1</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/totalFamilia1"/></xsl:attribute>
				</input>USUARIOS/AS HABITUALES DE SERVICIOS SOCIALES
				<br/>
				<label>      - </label>
				<input type="checkbox">
					<xsl:attribute name="style">width:17px; </xsl:attribute>
					<xsl:attribute name="name">totalFamilia2</xsl:attribute>
					<xsl:attribute name="id">totalFamilia2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/totalFamilia2"/></xsl:attribute>
				</input>FAMILIA NORMALIZADA EN SITUACIÓN DE NECESIDAD DERIVADA DE DESEMPLEO RECIENTE (2 AÑOS)
				<br/>
				<label>      - </label>
				<input type="checkbox">
					<xsl:attribute name="style">width:17px; </xsl:attribute>
					<xsl:attribute name="name">totalFamilia3</xsl:attribute>
					<xsl:attribute name="id">totalFamilia3</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/totalFamilia3"/></xsl:attribute>
				</input>FAMILIA NORMALIZADA QUE NECESITA UN APOYO ECONÓMICO PUNTUAL
				<br/>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.colectivo"/>:
				</label>
				<br/>
				<br/>
				<label>      - </label>
				<input type="checkbox">
					<xsl:attribute name="style">width:17px; </xsl:attribute>
					<xsl:attribute name="name">colectivo1</xsl:attribute>
					<xsl:attribute name="id">colectivo1</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colectivo1"/></xsl:attribute>
				</input>INMIGRANTES
				<br/>
				<label>      - </label>
				<input type="checkbox">
					<xsl:attribute name="style">width:17px; </xsl:attribute>
					<xsl:attribute name="name">colectivo2</xsl:attribute>
					<xsl:attribute name="id">colectivo2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colectivo2"/></xsl:attribute>
				</input>ETNIA GITANA
				<br/>
				<label>      - </label>
				<input type="checkbox">
					<xsl:attribute name="style">width:17px; </xsl:attribute>
					<xsl:attribute name="name">colectivo3</xsl:attribute>
					<xsl:attribute name="id">colectivo3</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/colectivo3"/></xsl:attribute>
				</input>FAMILIA MONOPARENTAL CON HIJOS A CARGO
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
				<br/>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.vivienda"/>:
				</label>
				<br/><br/>
				<label>      - </label>
				<input type="checkbox">
					<xsl:attribute name="style">width:17px; </xsl:attribute>
					<xsl:attribute name="name">vivienda1</xsl:attribute>
					<xsl:attribute name="id">vivienda1</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/vivienda1"/></xsl:attribute>
				</input>PROPIA PAGADA
				<br/>
				<label>      - </label>
				<input type="checkbox">
					<xsl:attribute name="style">width:17px; </xsl:attribute>
					<xsl:attribute name="name">vivienda2</xsl:attribute>
					<xsl:attribute name="id">vivienda2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/vivienda2"/></xsl:attribute>
				</input>PROPIA CON HIPOTECA DE   <input type="text">
					<xsl:attribute name="style">width:50px; </xsl:attribute>
					<xsl:attribute name="maxlength">20</xsl:attribute>
					<xsl:attribute name="size">20</xsl:attribute>
					<xsl:attribute name="name">viviendaEuros2</xsl:attribute>
					<xsl:attribute name="id">viviendaEuros2</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/viviendaEuros2"/></xsl:attribute>
				</input> € MES
				<br/>
				<label>      - </label>
				<input type="checkbox">
					<xsl:attribute name="style">width:17px; </xsl:attribute>
					<xsl:attribute name="name">vivienda3</xsl:attribute>
					<xsl:attribute name="id">vivienda3</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/vivienda3"/></xsl:attribute>
				</input>ALQUILADA <input type="text">
					<xsl:attribute name="style">width:50px; </xsl:attribute>
					<xsl:attribute name="maxlength">20</xsl:attribute>
					<xsl:attribute name="size">20</xsl:attribute>
					<xsl:attribute name="name">viviendaEuros3</xsl:attribute>
					<xsl:attribute name="id">viviendaEuros3</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/viviendaEuros3"/></xsl:attribute>
				</input> € MES
				<br/>
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
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
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
				<xsl:call-template name="FIELDS"><xsl:with-param name="row_id">20</xsl:with-param></xsl:call-template>
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

		<xsl:variable name="lang.fecha" select="' - Fecha:'"/>
		<xsl:variable name="lang.meses" select="' - Mes a cubrir:'"/>
		<xsl:variable name="lang.meses2" select="' - Concepto:'"/>
		<xsl:variable name="lang.importe" select="' - Importe solicitado:'"/>
	
   		<div class="cuadro" style="">
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
		<xsl:variable name="lang.diagnostico" select="'Diagnóstico inicial/Valoración Social'"/>

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
		<button onclick='limpiar()'>Limpiar Formulario</button>
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
