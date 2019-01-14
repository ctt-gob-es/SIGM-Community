<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template name="CUENTA_CORRIENTE_IBAN">
		<xsl:variable name="lang.blanco" select="' '"/>
		<xsl:variable name="lang.iban" select="'IBAN'"/>
		<xsl:variable name="lang.entidad" select="'Entidad'"/>
		<xsl:variable name="lang.sucursal" select="'Sucursal'"/>
		<xsl:variable name="lang.dc" select="'D.C.'"/>
		<xsl:variable name="lang.ncc" select="'N.C.C.'"/>
		<xsl:variable name="lang.separador" select="'- '"/>

		<xsl:variable name="lang.datosBancarios" select="'Datos Bancarios'"/>
		<xsl:variable name="lang.titular" select="'Titular de la cuenta:'"/>
		<xsl:variable name="lang.telefono" select="'Teléfono'"/>
		<xsl:variable name="lang.cuenta" select="'Cuenta'"/>
		<xsl:variable name="lang.nif" select="'NIF/CIF'"/>

		<div class="col">
			<table>
				<tr>
					<td><div style="display:inline;color:#006699;font-size:10;"><xsl:value-of select="$lang.blanco"/></div></td>
					<td><div style="display:inline;color:#006699;font-size:10;text-align:center;"><xsl:value-of select="$lang.iban"/></div></td>
					<td><div style="display:inline;color:#006699;font-size:10;text-align:center;"><xsl:value-of select="$lang.separador"/></div></td>
					<td><div style="display:inline;color:#006699;font-size:10;text-align:center;"><xsl:value-of select="$lang.entidad"/></div></td>
					<td><div style="display:inline;color:#006699;font-size:10;text-align:center;"><xsl:value-of select="$lang.separador"/></div></td>
					<td><div style="display:inline;color:#006699;font-size:10;text-align:center;"><xsl:value-of select="$lang.sucursal"/></div></td>
					<td><div style="display:inline;color:#006699;font-size:10;text-align:center;"><xsl:value-of select="$lang.separador"/></div></td>
					<td><div style="display:inline;color:#006699;font-size:10;text-align:center;"><xsl:value-of select="$lang.dc"/></div></td>
					<td><div style="display:inline;color:#006699;font-size:10;text-align:center;"><xsl:value-of select="$lang.separador"/></div></td>
					<td><div style="display:inline;color:#006699;font-size:10;text-align:center;"><xsl:value-of select="$lang.ncc"/></div></td>
				</tr>
				<tr>
					<td>
						<div style="display:inline;color:#006699;font-size:11;">
							<xsl:value-of select="$lang.cuenta"/>:
						</div>
					</td>
					<td>
						<input type="text">
							<xsl:attribute name="style">width:40px;color:#006699;font-size:11;</xsl:attribute>
							<xsl:attribute name="name">iban</xsl:attribute>
							<xsl:attribute name="id">iban</xsl:attribute>
							<xsl:attribute name="size">4</xsl:attribute>
						    <xsl:attribute name="maxlength">4</xsl:attribute>
						    <xsl:attribute name="onblur">this.value=this.value.toUpperCase()</xsl:attribute>
						    <xsl:attribute name="onkeyup">if(this.value.length==4){document.getElementById('ccc1').focus();}</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/iban"/></xsl:attribute>
						</input>
					</td>
					<td><div style="display:inline;color:#006699;font-size:10;"><xsl:value-of select="$lang.separador"/></div></td>
					<td>
						<input type="text">
							<xsl:attribute name="style">width:40px;color:#006699;font-size:11</xsl:attribute>
							<xsl:attribute name="name">ccc1</xsl:attribute>
							<xsl:attribute name="id">ccc1</xsl:attribute>
							<xsl:attribute name="size">4</xsl:attribute>
							<xsl:attribute name="maxlength">4</xsl:attribute>
							<xsl:attribute name="onkeyup">if(this.value.length==4){document.getElementById('ccc2').focus();}</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ccc1"/></xsl:attribute>
						</input>
					</td>
					<td><div style="display:inline;color:#006699;font-size:10;"><xsl:value-of select="$lang.separador"/></div></td>
					<td>
						<input type="text">
							<xsl:attribute name="style">width:40px;color:#006699;font-size:11;</xsl:attribute>
							<xsl:attribute name="name">ccc2</xsl:attribute>
							<xsl:attribute name="id">ccc2</xsl:attribute>
							<xsl:attribute name="size">4</xsl:attribute>
							<xsl:attribute name="maxlength">4</xsl:attribute>
							<xsl:attribute name="onkeyup">if(this.value.length==4){document.getElementById('ccc3').focus();}</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ccc2"/></xsl:attribute>
						</input>
					</td>
					<td><div style="display:inline;color:#006699;font-size:10;text-align:center;"><xsl:value-of select="$lang.separador"/></div></td>
					<td>
						<input type="text">
							<xsl:attribute name="style">width:20px;color:#006699;font-size:11;</xsl:attribute>
							<xsl:attribute name="name">ccc3</xsl:attribute>
							<xsl:attribute name="id">ccc3</xsl:attribute>
					    	<xsl:attribute name="size">2</xsl:attribute>
							<xsl:attribute name="maxlength">2</xsl:attribute>
							<xsl:attribute name="onkeyup">if(this.value.length==2){document.getElementById('ccc4').focus();}</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ccc3"/></xsl:attribute>
						</input>
					</td>
					<td><div style="display:inline;color:#006699;font-size:10;text-align:center;"><xsl:value-of select="$lang.separador"/></div></td>
					<td>
						<input type="text">
							<xsl:attribute name="style">width:85px;color:#006699;font-size:11;</xsl:attribute>
							<xsl:attribute name="name">ccc4</xsl:attribute>
							<xsl:attribute name="id">ccc4</xsl:attribute>
							<xsl:attribute name="size">10</xsl:attribute>
							<xsl:attribute name="maxlength">10</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/ccc4"/></xsl:attribute>
						</input>
					</td>
				</tr>
			</table>
		</div>
		<div class="col">	
			<!--Datos del titular de la cuenta-->				
			<label>
				<xsl:attribute name="style">width:130;margin-right:3px;</xsl:attribute>
				<xsl:value-of select="$lang.titular"/>
			</label>
			<input type="text">
				<xsl:attribute name="style">width:500px;color:#006699;</xsl:attribute>
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
				<xsl:attribute name="style">width:100px;color:#006699;</xsl:attribute>
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
				<xsl:attribute name="style">width:100px;color:#006699;</xsl:attribute>
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
     
			function trim(myString){
				return myString.replace(/^\s+/g,'').replace(/\s+$/g,'');
			}

			function getnumIBAN(letra){
				ls_letras = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';           
				return ls_letras.search(letra) + 10;
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

			function validarIBAN(IBAN) {
				if(IBAN != ''){
					//Limpiamos el numero de IBAN
					IBAN = IBAN.toUpperCase();  //Todo a Mayus
					IBAN = trim(IBAN); //Quitamos blancos de principio y final.
					IBAN = IBAN.replace(/\s/g, "");  //Quitamos blancos del medio.

					var letra1,letra2,num1,num2, numIban;
					var isbanaux;
					var numeroSustitucion;

					if (IBAN.length != 24){
						alert('El campo IBAN debe contener 24 caracteres');
						return false;
					}
					// Cambiamos las letras por numeros.
					letra1 = IBAN.substring(0, 1);
					letra2 = IBAN.substring(1, 2);
					numIban = IBAN.substring(2, 4);

					if(!numerico(numIban)){
						alert('El IBAN debe contener dos letras y dos números');
						return false;
					}

					num1 = getnumIBAN(letra1);
					num2 = getnumIBAN(letra2);

					isbanaux = IBAN.substring(4, IBAN.length) + String(num1) + String(num2) + String(numIban);                

					//Calculamos el resto
					var divisor = 97;
					primeraParte = isbanaux.substring(0,9);
					primerResto = primeraParte % divisor;

					segundaParte = String(primerResto) + isbanaux.substring(9,16);
					segundoResto = segundaParte % divisor;

					terceraParte= String(segundoResto) + isbanaux.substring(16,23);
					tercerResto = terceraParte % divisor;

					cuartaParte = String(tercerResto) + isbanaux.substring(23, isbanaux.length);
					resto = cuartaParte % divisor;

					if (resto == 1){
						return true;
					}
					else{
						return false;
					}
				}
				return true;
			}			
		</script>

	</xsl:template>

	<xsl:template name="CUENTA_CORRIENTE_IBAN_RELLENOS">
		<xsl:variable name="lang.iban" select="'IBAN:'"/>
		<xsl:variable name="lang.entidad" select="'Entidad:'"/>
		<xsl:variable name="lang.sucursal" select="'Sucursal:'"/>
		<xsl:variable name="lang.dc" select="'D.C.:'"/>
		<xsl:variable name="lang.ncc" select="'NºC.C.:'"/>
		<xsl:variable name="lang.separador" select="' - '"/>

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
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/iban"/>
			</label>
			<label class="gr" style="position: relative; width:10px;">
				<xsl:value-of select="$lang.separador"/>
			</label>
			<label class="gr" style="position: relative; width:40px;color:#006699;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ccc1"/>
			</label>
			<label class="gr" style="position: relative; width:10px;">
				<xsl:value-of select="$lang.separador"/>
			</label>
			<label class="gr" style="position: relative; width:40px;color:#006699;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ccc2"/>
			</label>
			<label class="gr" style="position: relative; width:10px;">
				<xsl:value-of select="$lang.separador"/>
			</label>
			<label class="gr" style="position: relative; width:20px;color:#006699">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ccc3"/>
				<br/>
			</label>
			<label class="gr" style="position: relative; width:10px;">
				<xsl:value-of select="$lang.separador"/>
			</label>
			<label class="gr" style="position: relative; width:85px;color:#006699;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/ccc4"/>
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
	</xsl:template>

</xsl:stylesheet>
