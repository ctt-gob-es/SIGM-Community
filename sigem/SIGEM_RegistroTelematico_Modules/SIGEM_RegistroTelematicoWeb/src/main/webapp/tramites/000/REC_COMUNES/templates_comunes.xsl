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
	</xsl:template>
	
	<xsl:template name="VALIDACIONES">
			// Validación número de cuenta
			function obtenerDigito(valor){
				valores = new Array(1, 2, 4, 8, 5, 10, 9, 7, 3, 6);
				control = 0;
				for (i=0; i &lt;=9; i++)
					control += parseInt(valor.charAt(i)) * valores[i];
					control = 11 - (control % 11);
					if (control == 11) 
						control = 0;
					else if (control == 10) 
						control = 1;
					return control;
					}

			function numerico(valor){
				cad = valor.toString();
				for (var i=0; i &lt; cad.length; i++) {
					var caracter = cad.charAt(i);
					if (caracter &lt; "0" || caracter &gt; "9")
						return false;
				}
				return true;
			}
			
			function validarCuenta(f) {
				if (f.ccc1.value != ""  || f.ccc2.value != "" ||
						f.ccc3.value != "" || f.ccc4.value != "") 
			
				if (f.ccc1.value == ""  || f.ccc2.value == "" ||
						f.ccc3.value == "" || f.ccc4.value == "") {
					alert("Por favor, introduzca correctamente los datos de su cuenta;"
							+ " no están completos");
					return false;
				} else {
					if (f.ccc1.value.length != 4 || f.ccc2.value.length != 4 ||
							f.ccc3.value.length != 2 || f.ccc4.value.length != 10) {
						alert("Por favor, introduzca correctamente los datos de su cuenta;"
							+ " no están completos");
						return false;
					} else {
						if (!numerico(f.ccc1.value) || !numerico(f.ccc2.value) ||
								!numerico(f.ccc3.value) || !numerico(f.ccc4.value)) {
							alert("Por favor, introduzca correctamente los datos de su "
								+ "cuenta; no son numericos");
							return false;
						} else {
							if (!(obtenerDigito("00" + f.ccc1.value + f.ccc2.value) ==
									parseInt(f.ccc3.value.charAt(0))) || 
									!(obtenerDigito(f.ccc4.value) ==
									parseInt(f.ccc3.value.charAt(1)))) {
								alert("Los dígitos de control no se corresponden con los demás"
									+ " números de la cuenta");
								return false;
							} else {
								return true;
							}
						}
					}
				}
				
				else return true;
				
			}
	
			function validarLiquidacion(campo){
				var valor= escape(campo.value);
				if(valor.length!= 18 || !numerico(valor.substring(0,12)) || valor.substring(12,13) != '/' || !numerico(valor.substring(13, valor.length)))
					return false;				
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
		<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
		<xsl:variable name="lang.id_nombre" select="'Nombre'"/>

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

	<xsl:template name="DATOS_SOLICITANTE_EDITABLE">
		<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
		<xsl:variable name="lang.id_nombre" select="'Nombre'"/>

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
					<!--<xsl:attribute name="disabled"></xsl:attribute>-->
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
					<!--<xsl:attribute name="disabled"></xsl:attribute>-->
				</input>
			</div>
		</div>
		<script>
			function validaNIFPresentador(){
				var valorNifPresen = document.getElementById('documentoIdentidad');
				if(valorNifPresen.value != null){
					if(valorNifPresen.value != ''){
						var validaNifPresen = valida_nif_cif_nie(valorNifPresen);
						if(validaNifPresen != 1)
							if(validaNifPresen != 2)
								if(validaNifPresen  != 3){
									alert('El NIF/CIF del presentador es incorrecto');
									valorNifPresen.focus();			
									return false;
								}
					}
				}
				return true;
			}	
		</script>
	</xsl:template>
	
	<xsl:template name="DATOS_OBLIGADO">
		<xsl:variable name="lang.datosObligado" select="'Datos del contribuyente'"/>
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
					<xsl:attribute name="maxlength">5</xsl:attribute>
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
					<xsl:value-of select="$lang.calle"/>:*
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
					<xsl:value-of select="$lang.c_postal"/>:*
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
			function validaNIFObligado(){
				var valorNif = document.getElementById('nif');
				var validaNif = valida_nif_cif_nie(valorNif);
				if(validaNif != 1)
					if(validaNif != 2)
						if(validaNif != 3){
							alert('El NIF/CIF del obligado tributario es incorrecto');
							valorNif.focus();			
							return false;
						}
				return true;
			}

			function validaNIFRepresentante(){
				var valorNifRepre = document.getElementById('repres_nif');
				if(valorNifRepre.value != null){
					if(valorNifRepre.value != ''){
						var validaNifRepre = valida_nif_cif_nie(valorNifRepre);
						if(validaNifRepre != 1)
							if(validaNifRepre != 2)
								if(validaNifRepre!= 3){
									alert('El NIF/CIF del representante es incorrecto');
									valorNifRepre.focus();			
									return false;
								}
					}
				}
				return true;
			}	
		</script>

	</xsl:template>

	<xsl:template name="DATOS_OBLIGADO_SIN_LUPA">
		<xsl:variable name="lang.datosObligado" select="'Datos del contribuyente'"/>
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
					<xsl:attribute name="maxlength">5</xsl:attribute>
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
					<xsl:value-of select="$lang.calle"/>:*
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
					<xsl:value-of select="$lang.c_postal"/>:*
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
			function validaNIFObligado(){
				var valorNif = document.getElementById('nif');
				var validaNif = valida_nif_cif_nie(valorNif);
				if(validaNif != 1)
					if(validaNif != 2)
						if(validaNif != 3){
							alert('El NIF/CIF del obligado tributario es incorrecto');
							valorNif.focus();			
							return false;
						}
				return true;
			}

			function validaNIFRepresentante(){
				var valorNifRepre = document.getElementById('repres_nif');
				if(valorNifRepre.value != null){
					if(valorNifRepre.value != ''){
						var validaNifRepre = valida_nif_cif_nie(valorNifRepre);
						if(validaNifRepre != 1)
							if(validaNifRepre != 2)
								if(validaNifRepre!= 3){
									alert('El NIF/CIF del representante es incorrecto');
									valorNifRepre.focus();			
									return false;
								}
					}
				}
				return true;
			}	
		</script>

	</xsl:template>

	<xsl:template name="CUENTA_CORRIENTE">
		<xsl:variable name="lang.entidad" select="'Entidad:'"/>
		<xsl:variable name="lang.sucursal" select="'Sucursal:'"/>
		<xsl:variable name="lang.dc" select="'D.C.:'"/>
		<xsl:variable name="lang.ncc" select="'NºC.C.:'"/>

		<xsl:variable name="lang.datosBancarios" select="'Datos Bancarios'"/>
		<xsl:variable name="lang.titular" select="'Titular de la cuenta:'"/>
		<xsl:variable name="lang.telefono" select="'Teléfono'"/>
		<xsl:variable name="lang.cuenta" select="'Cuenta'"/>
		<xsl:variable name="lang.nif" select="'NIF/CIF'"/>

		<div class="col">
			<div style="display:inline;margin-right:20px;color:#006699;"><xsl:value-of select="$lang.cuenta"/></div>
			<input type="text">
			    <xsl:attribute name="style">width:40px;color:#006699;</xsl:attribute>
			    <xsl:attribute name="name">ccc1</xsl:attribute>
			    <xsl:attribute name="id">ccc1</xsl:attribute>
				<xsl:attribute name="size">4</xsl:attribute>
			       <xsl:attribute name="maxlength">4</xsl:attribute>
			    <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ccc1"/></xsl:attribute>
			</input>
			<input type="text">
			    <xsl:attribute name="style">width:40px;color:#006699;</xsl:attribute>
			    <xsl:attribute name="name">ccc2</xsl:attribute>
			    <xsl:attribute name="id">ccc2</xsl:attribute>
			    <xsl:attribute name="size">4</xsl:attribute>
			    <xsl:attribute name="maxlength">4</xsl:attribute>
			    <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ccc2"/></xsl:attribute>
			</input>
			<input type="text">
			    <xsl:attribute name="style">width:20px;color:#006699;</xsl:attribute>
			    <xsl:attribute name="name">ccc3</xsl:attribute>
			    <xsl:attribute name="id">ccc3</xsl:attribute>
	    		    <xsl:attribute name="size">2</xsl:attribute>
			    <xsl:attribute name="maxlength">2</xsl:attribute>
			    <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ccc3"/></xsl:attribute>
			</input>
			<input type="text">
			    <xsl:attribute name="style">width:85px;color:#006699;</xsl:attribute>
			    <xsl:attribute name="name">ccc4</xsl:attribute>
			    <xsl:attribute name="id">ccc4</xsl:attribute>
			    <xsl:attribute name="size">10</xsl:attribute>
			    <xsl:attribute name="maxlength">10</xsl:attribute>
			    <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ccc4"/></xsl:attribute>
			</input><br/>
		</div>
		<div class="col">	
			<!--Datos del titular de la cuenta-->				
			<label>
				<xsl:attribute name="style">width:130;margin-right:3px;</xsl:attribute>
				<xsl:value-of select="$lang.titular"/>
			</label>
			<input type="text">
				<xsl:attribute name="style">width:500px;</xsl:attribute>
				<xsl:attribute name="size">255</xsl:attribute>
				<xsl:attribute name="maxlength">255</xsl:attribute>
				<xsl:attribute name="name">titular</xsl:attribute>
				<xsl:attribute name="id">titular</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/titular"/></xsl:attribute>
			</input>
			<br/>	
			<label>
				<xsl:attribute name="style">width:130;margin-right:3px;</xsl:attribute>
				<xsl:value-of select="$lang.nif"/>:
			</label>
			<input type="text">
				<xsl:attribute name="style">width:100px;</xsl:attribute>
				<xsl:attribute name="size">10</xsl:attribute>
				<xsl:attribute name="maxlength">10</xsl:attribute>
				<xsl:attribute name="name">nifTitular</xsl:attribute>
				<xsl:attribute name="id">nifTitular</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/nifTitular"/></xsl:attribute>
			</input>
			<br/>
			<label>
				<xsl:attribute name="style">width:130;margin-right:3px;</xsl:attribute>
				<xsl:value-of select="$lang.telefono"/>:
			</label>
			<input type="text">
				<xsl:attribute name="style">width:100px;</xsl:attribute>
				<xsl:attribute name="size">14</xsl:attribute>
				<xsl:attribute name="maxlength">14</xsl:attribute>
				<xsl:attribute name="name">telefonoTitular</xsl:attribute>
				<xsl:attribute name="id">telefonoTitular</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/telefonoTitular"/></xsl:attribute>
			</input>
		</div>
		<script>
			function validaCC(){
				var valorNifTit = document.getElementById('nifTitular');
				if(document.getElementById('ccc1').value != null){
					if(document.getElementById('ccc1').value!=''){
						if(!validaSiTitularCC()) return false;
						if(valorNifTit.value !=null){
							if(valorNifTit.value!=''){
								var validaNifTit = valida_nif_cif_nie(valorNifTit);
								if(validaNifTit != 1)
									if(validaNifTit != 2)
										if(validaNifTit != 3){
											alert('El NIF/CIF del titular de la cuenta es incorrecto');
											valorNifTit.focus();			
											return false;
										}
							}
						}
					}
					else{
						document.getElementById('titular').value='';
						document.getElementById('nifTitular').value='';
						document.getElementById('telefonoTitular').value='';
					}
				}
				else{
					document.getElementById('titular').value='';
					document.getElementById('nifTitular').value='';
					document.getElementById('telefonoTitular').value='';
				}
				return true;
			}
			function validaSiTitularCC(){
				var valorNifTit = document.getElementById('nifTitular');
				if(document.getElementById('ccc1').value != null){
					if(document.getElementById('ccc1').value!=''){
						if(valorNifTit.value ==null || valorNifTit.value==''){
							alert('Debe indicar el NIF/CIF del titular de la cuenta');
							return false();
						}
					}
				}
				return true;
			}
		</script>

	</xsl:template>
	
	<xsl:template name="SOLICITA1">
		<xsl:variable name="lang.solicita1" select="'SOLICITA'"/>
		<xsl:variable name="lang.solicita2" select="', que previa las comprobaciones oportunas, procedan a conceder el beneficio fiscal solicitado para los años:'"/>
		<b><xsl:value-of select="$lang.solicita1"/></b><xsl:value-of select="$lang.solicita2"/>
			<input type="text">
				<xsl:attribute name="style">margin-left:10px;width:250px;color:#006699;</xsl:attribute>
				<xsl:attribute name="name">years</xsl:attribute>
				<xsl:attribute name="id">years</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/years"/></xsl:attribute>
			</input><br/>
	</xsl:template>

	<xsl:template name="SOLICITA3">
		<xsl:variable name="lang.solicita1" select="'SOLICITA'"/>
		<xsl:variable name="lang.solicita3" select="'Así mismo, '"/>
		<xsl:variable name="lang.solicita4" select="' que previa las comprobaciones oportunas, procedan a efectuar la devolución que proceda de los años '"/>
		<xsl:variable name="lang.ccc" select="'a la siguiente entidad y cuenta corriente :'"/>

		<xsl:variable name="lang.cuantia" select="'Cuantía:'"/>

		<xsl:value-of select="$lang.solicita3"/><b><xsl:value-of select="$lang.solicita1"/></b><xsl:value-of select="$lang.solicita4"/>
			<input type="text">
				<xsl:attribute name="style">margin-left:10px;width:250px;color:#006699;</xsl:attribute>
				<xsl:attribute name="name">years2</xsl:attribute>
				<xsl:attribute name="id">years2</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/years2"/></xsl:attribute>
			</input>
		<xsl:value-of select="$lang.ccc"/><br/>
			<br/>
			<div style="margin-left:10px;">
			    <div style="display:inline;margin-right:20px;color:#006699;"><xsl:value-of select="$lang.cuantia"/></div>
			    <input type="text">
				    <xsl:attribute name="style">width:75px;color:#006699;</xsl:attribute>
				    <xsl:attribute name="name">cuantia</xsl:attribute>
				    <xsl:attribute name="id">cuantia</xsl:attribute>
				    <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/cuantia"/></xsl:attribute>
			    </input><br/>
			    <br/>
			    <xsl:call-template name="CUENTA_CORRIENTE_IBAN" />
			</div>
	</xsl:template>

	<xsl:template name="OPTIONS_MUNICIPIOS" xmlns:java="http://xml.apache.org/xslt/java">
		<xsl:variable name="fileAyuntam" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.
		getDatosTablaValidacionConSustituto('REC_VLDTBL_MUNICIPIOS','000')"/>
		<xsl:variable name="b" select="document($fileAyuntam)"/>
		<xsl:for-each select="$b/listado/dato">
			<option>
				<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
				<xsl:value-of select="sustituto"/>
			</option>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="OPTIONS_MUNICIPIOS_FRAC_IBI" xmlns:java="http://xml.apache.org/xslt/java">
		<xsl:variable name="fileAyuntam" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosTablaValidacionConSustituto('REC_VLDTBL_MUNICIP_FRAC_IBI','000')"/>
		<xsl:variable name="b" select="document($fileAyuntam)"/>
		<xsl:for-each select="$b/listado/dato">
			<option>
				<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
				<xsl:value-of select="sustituto"/>
			</option>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="CUENTA_CORRIENTE_DATOS_RELLENOS">
		<xsl:variable name="lang.entidad" select="'Entidad:'"/>
		<xsl:variable name="lang.sucursal" select="'Sucursal:'"/>
		<xsl:variable name="lang.dc" select="'D.C.:'"/>
		<xsl:variable name="lang.ncc" select="'NºC.C.:'"/>
		<xsl:variable name="lang.nif" select="'NIF/CIF'"/>

		<xsl:variable name="lang.datosBancarios" select="'Datos Bancarios'"/>
		<xsl:variable name="lang.titular" select="'Titular de la cuenta: '"/>
		<xsl:variable name="lang.telefono" select="'Teléfono: '"/>
		<xsl:variable name="lang.nifTitular" select="'NIF/CIF Titular: '"/>

		<xsl:variable name="lang.cuenta" select="'Cuenta: '"/>

		<div class="col">
			<label class="gr" style="position: relative; width:200px;">
				<xsl:value-of select="$lang.cuenta"/>
				<br/>
			</label>

			<label class="gr" style="position: relative; width:40px;color:#006699;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ccc1"/>
				<br/>
			</label>
			<label class="gr" style="position: relative; width:40px;color:#006699;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ccc2"/>
				<br/>
			</label>
			<label class="gr" style="position: relative; width:20px;color:#006699">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ccc3"/>
				<br/>
			</label>
			<label class="gr" style="position: relative; width:85px;color:#006699;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ccc4"/>
				<br/>
			</label>
			<br/>				
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:130;">
				<xsl:value-of select="$lang.titular"/>
				<br/>
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/titular"/>
				<br/>
			</label>
			<br/>
			<label class="gr" style="position: relative; width:130;">
				<xsl:value-of select="$lang.nifTitular"/>	
				<br/>
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nifTitular"/>
				<br/>
			</label>
			<br/>
			<label class="gr" style="position: relative; width:130;">
				<xsl:value-of select="$lang.telefono"/>	
				<br/>
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/telefonoTitular"/>
				<br/>
			</label>
			<br/>
		</div>
		<script>
			function validaCC(){
				var valorNifTit = document.getElementById('nifTitular');
				if(document.getElementById('ccc1').value != null){
					if(document.getElementById('ccc1').value!=''){
						if(!validaSiTitularCC()) return false;
						if(valorNifTit.value !=null){
							if(valorNifTit.value!=''){
								var validaNifTit = valida_nif_cif_nie(valorNifTit);
								if(validaNifTit != 1)
									if(validaNifTit != 2)
										if(validaNifTit != 3){
											alert('El NIF/CIF del titular de la cuenta es incorrecto');
											valorNifTit.focus();			
											return false;
										}
							}
						}
					}
					else{
						document.getElementById('titular').value='';
						document.getElementById('nifTitular').value='';
						document.getElementById('telefonoTitular').value='';
					}
				}
				else{
					document.getElementById('titular').value='';
					document.getElementById('nifTitular').value='';
					document.getElementById('telefonoTitular').value='';
				}
				return true;
			}
			function validaSiTitularCC(){
				var valorNifTit = document.getElementById('nifTitular');
				if(document.getElementById('ccc1').value != null){
					if(document.getElementById('ccc1').value!=''){
						if(valorNifTit.value ==null || valorNifTit.value==''){
							alert('Debe indicar el NIF/CIF del titular de la cuenta');
							return false();
						}
					}
				}
				return true;
			}
		</script>
	</xsl:template>

	<xsl:template name="SOLICITA4">	
		<xsl:variable name="lang.solicita1" select="'SOLICITA'"/>
		<xsl:variable name="lang.solicita3" select="'Así mismo, '"/>
		<xsl:variable name="lang.solicita4" select="' que previa las comprobaciones oportunas, procedan a efectuar la devolución que proceda '"/>
		<xsl:variable name="lang.ccc" select="'a la siguiente entidad y cuenta corriente :'"/>
		<xsl:value-of select="$lang.solicita3"/><b><xsl:value-of select="$lang.solicita1"/></b><xsl:value-of select="$lang.solicita4"/>
		<xsl:value-of select="$lang.ccc"/><br/>
		<xsl:variable name="lang.cuantia" select="'Cuantía:'"/>
			<br/>
			<div style="margin-left:10px;">
			    <div style="display:inline;margin-right:20px;color:#006699;"><xsl:value-of select="$lang.cuantia"/></div>
			    <input type="text">
				    <xsl:attribute name="style">width:75px;color:#006699;</xsl:attribute>
				    <xsl:attribute name="name">cuantia</xsl:attribute>
				    <xsl:attribute name="id">cuantia</xsl:attribute>
				    <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/cuantia"/></xsl:attribute>
			    </input><br/>
			    <br/>
			    <xsl:call-template name="CUENTA_CORRIENTE_IBAN" />
			</div>
	</xsl:template>

	<xsl:template name="DATOS_SOLICITUD_RELLENOS">

		<xsl:variable name="lang.datosIdentificativos" select="'Datos identificativos'"/>		
		<xsl:variable name="lang.datosObligado" select="'Datos del contribuyente'"/>
		<xsl:variable name="lang.datosRepresentante" select="'Datos del representante o presentador autorizado'"/>

		<xsl:variable name="lang.pres_nif" select="'NIF Presentador'"/>
		<xsl:variable name="lang.pres_nombre" select="'Nombre Presentador'"/>
		<xsl:variable name="lang.repres_nif" select="'NIF/CIF Representante'"/>
		<xsl:variable name="lang.repres_nombre" select="'Nombre Representante'"/>
		<xsl:variable name="lang.repres_movil" select="'Número de teléfono móvil Representante'"/>
		<xsl:variable name="lang.repres_d_email" select="'Dirección de correo electrónico Representante'"/>
		<xsl:variable name="lang.nif" select="'NIF/CIF Contribuyente'"/>
		<xsl:variable name="lang.nombre" select="'Nombre Contribuyente'"/>
		<xsl:variable name="lang.direccion" select="'Dirección'"/>
		<xsl:variable name="lang.calle" select="'Domicilio'"/>
		<xsl:variable name="lang.numero" select="'Numero'"/>
		<xsl:variable name="lang.escalera" select="'Escalera'"/>
		<xsl:variable name="lang.planta_puerta" select="'Planta/Puerta'"/>
		<xsl:variable name="lang.c_postal" select="'Código Postal'"/>
		<xsl:variable name="lang.movil" select="'Número de teléfono móvil'"/>
		<xsl:variable name="lang.d_email" select="'Dirección de correo electrónico'"/>
	
		<div class="col">
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.datosIdentificativos"/></b>	
			</label>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.pres_nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Documento_Identificacion/Numero"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.pres_nombre"/>:	
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
					<xsl:value-of select="$lang.datosObligado"/>:	
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
				<!--<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numero"/>,
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/escalera"/>,
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/planta_puerta"/>,-->
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
				<xsl:value-of select="$lang.repres_nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_nif"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.repres_nombre"/>:	
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
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rcalle"/>,
				<!--<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rnumero"/>,
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rescalera"/>,
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rplanta_puerta"/>,-->
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rciudad"/>, 
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rregion"/>, 
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rc_postal"/>
			</label>
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
				if(auxNifSoli != "P1300000E"){
					document.getElementById('nombreSolicitante').value = "Oficina Gestión";
					document.getElementById('documentoIdentidad').value = "P1300000E";
				}
				else{
					document.getElementById('nombreSolicitante').value = auxNombreSoli;
					document.getElementById('documentoIdentidad').value = auxNifSoli;
				}
			}
		</script>
	</xsl:template>

	<xsl:template name="DATOS_SOLICITUD_RELLENOS_PRESENTADOR">

		<xsl:variable name="lang.datosIdentificativos" select="'Datos identificativos'"/>		
		<xsl:variable name="lang.datosPresentador" select="'Datos del presentador'"/>
		<xsl:variable name="lang.datosObligado" select="'Datos del contribuyente'"/>
		<xsl:variable name="lang.datosRepresentante" select="'Datos del representante o presentador autorizado'"/>

		<xsl:variable name="lang.grab_nif" select="'NIF Grabador'"/>
		<xsl:variable name="lang.grab_nombre" select="'Nombre Grabador'"/>
		<xsl:variable name="lang.pres_nif" select="'NIF Presentador'"/>
		<xsl:variable name="lang.pres_nombre" select="'Nombre Presentador'"/>
		<xsl:variable name="lang.repres_nif" select="'NIF/CIF Representante'"/>
		<xsl:variable name="lang.repres_nombre" select="'Nombre Representante'"/>
		<xsl:variable name="lang.repres_movil" select="'Número de teléfono móvil Representante'"/>
		<xsl:variable name="lang.repres_d_email" select="'Dirección de correo electrónico Representante'"/>
		<xsl:variable name="lang.nif" select="'NIF/CIF Contribuyente'"/>
		<xsl:variable name="lang.nombre" select="'Nombre Contribuyente'"/>
		<xsl:variable name="lang.direccion" select="'Dirección'"/>
		<xsl:variable name="lang.calle" select="'Domicilio'"/>
		<xsl:variable name="lang.numero" select="'Numero'"/>
		<xsl:variable name="lang.escalera" select="'Escalera'"/>
		<xsl:variable name="lang.planta_puerta" select="'Planta/Puerta'"/>
		<xsl:variable name="lang.c_postal" select="'Código Postal'"/>
		<xsl:variable name="lang.movil" select="'Número de teléfono móvil'"/>
		<xsl:variable name="lang.d_email" select="'Dirección de correo electrónico'"/>
	
		<div class="col">
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.datosIdentificativos"/></b>	
			</label>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.grab_nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Documento_Identificacion/Numero"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.grab_nombre"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Genericos/Remitente/Nombre"/>
			</label>
			<br/>
		</div>

		<br/>
		<div class="col">
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.datosPresentador"/></b>	
			</label>
		</div>

		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.pres_nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nifsolihidden"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.pres_nombre"/>:	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/nombresolihidden"/>
			</label>
			<br/>
		</div>
		<br/>
		<div class="col">
			<b>
				<label class="gr" style="position: relative; width:400px;">
					<xsl:value-of select="$lang.datosObligado"/>:	
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
				<!--<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/numero"/>,
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/escalera"/>,
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/planta_puerta"/>,-->
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
				<xsl:value-of select="$lang.repres_nif"/>:	
			</label>
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:500px;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/repres_nif"/>
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="$lang.repres_nombre"/>:	
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
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rcalle"/>,
				<!--<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rnumero"/>,
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rescalera"/>,
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rplanta_puerta"/>,-->
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rciudad"/>, 
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rregion"/>, 
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/rc_postal"/>
			</label>
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

	<xsl:template name="SOLICITA_BENEF_IBI">
		<xsl:variable name="lang.years" select="'Años'"/>
		<xsl:variable name="lang.cuantia" select="'Cuantía'"/>
		<div class="col">			
			<label class="gr" style="position: relative; width:300px;"><xsl:value-of select="$lang.years"/></label>
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/years"/>
				<br/>
			</label>
		    <br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:300px;"><xsl:value-of select="$lang.cuantia"/></label>
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/cuantia"/>
				<br/>
			</label>
			<label class="gr" style="position: relative; width:300px;"><xsl:value-of select="$lang.years"/></label>
			<label class="gr" style="position: relative; width:150px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/years2"/>
				<br/>
			</label>
		    <br/>
		</div>
		<xsl:call-template name="CUENTA_CORRIENTE_IBAN_RELLENOS" />
		
	</xsl:template>

</xsl:stylesheet>
