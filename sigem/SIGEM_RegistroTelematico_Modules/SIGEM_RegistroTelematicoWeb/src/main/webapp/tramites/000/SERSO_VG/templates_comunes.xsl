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
</xsl:stylesheet>
