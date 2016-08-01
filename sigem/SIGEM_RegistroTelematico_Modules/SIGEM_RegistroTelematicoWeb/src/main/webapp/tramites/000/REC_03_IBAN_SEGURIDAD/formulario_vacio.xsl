<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'Formulario de solicitud de compensación en el IBI'"/>

	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>

	<xsl:variable name="lang.nif" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre" select="'Apellidos y Nombre o Denominación Social'"/>
	<xsl:variable name="lang.calle" select="'Calle'"/>
	<xsl:variable name="lang.c_postal" select="'Código Postal'"/>
	
	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>
	<xsl:variable name="lang.representacion" select="'Rellene los siguientes datos si actúa como representante'"/>			
	<xsl:variable name="lang.nif_repr" select="'NIF/CIF de la persona o entidad a quien representa'"/>			
	<xsl:variable name="lang.nombre_repr" select="'Nombre del representado'"/>			

	<xsl:variable name="lang.expone1" select="'EXPONE'"/>
	<xsl:variable name="lang.expone2" select="', que ha recibido la liquidación/es que a continuación se relaciona/n correspondientes al municipio de:'"/>
	<xsl:variable name="lang.expone3" select="'Que se efectuó el pago de las liquidaciones que se indican, correspondientes a la misma finca y ejercicios, por lo que solicita la anulación de la liquidación/es arriba identificadas, y se proceda a compensar las cantidades ya pagadas por la causa que se expresa (seleccionar una de las causas)'"/>
	<xsl:variable name="lang.motivo1" select="'1 - Segregación de las fincas'"/>
	<xsl:variable name="lang.motivo2" select="'2 - Agrupación de las fincas'"/>
	<xsl:variable name="lang.motivo3" select="'3 - Cambio de titular'"/>
	<xsl:variable name="lang.motivo4" select="'4 - Nueva construcción'"/>
	<xsl:variable name="lang.motivo5" select="'5 - Otras distintas de las enunciadas'"/>
	<xsl:variable name="lang.notas_motivos1" select="'Opción 1: Se deberá aportar copia de la/s liquidación/es ingresada/s en el cuadro superior y original de las restantes.'"/>
	<xsl:variable name="lang.notas_motivos2" select="'Opción 5: Indicar cual:'"/>
	<xsl:variable name="lang.liquidaciones" select="'LIQUIDACIONES OBJETO DE COMPENSACIÓN*'"/>
	<xsl:variable name="lang.municipio" select="'Nombre del municipio'"/>
	<xsl:variable name="lang.liquidacion" select="'Nº de liquidación'"/>
	<xsl:variable name="lang.pagada" select="'Pagada / Pendiente'"/>
	<xsl:variable name="lang.solicita1" select="'SOLICITA'"/>
	<xsl:variable name="lang.solicita2" select="', que previa las comprobaciones oportunas, procedan a efectuar la compensación procedente, y en su caso, a emitir nuevas liquidaciones compensadas.'"/>
	<xsl:variable name="lang.solicita3" select="'Así mismo, '"/>
     	<xsl:variable name="lang.solicita4" select="' que previa las comprobaciones oportunas, procedan a efectuar la devolución que proceda a la '"/>
	<xsl:variable name="lang.ccc" select="'siguiente entidad y cuenta corriente :'"/>
    	<xsl:variable name="lang.cuantia" select="'Cuantía de la devolución que se solicita:'"/>
	<xsl:variable name="lang.cuenta" select="'Código de Cuenta Corriente:'"/>
	<xsl:variable name="lang.notas" select="'NOTAS INFORMATIVAS:'"/>
	<xsl:variable name="lang.notas1" select="'1.- El plazo máximo de resolución de su solicitud es de 6 meses, siendo los efectos del silencio administrativo NEGATIVOS.'"/>
	<xsl:variable name="lang.notas2" select="'2.- Se deberá aportar original de la/s liquidación/es pagadas a fin de que se practique la compensación solicitada con la/s nueva liquidación/es'"/>
	<xsl:variable name="lang.notas3" select="'3.- En el caso de que la solicitud venga referida a liquidaciones a nombre de otro obligado tributario, deberá acreditarse la representación legal o voluntaria (ver modelo en la página) del mismo.'"/>
	
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero (*.zip), pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.documento" select="'Conjunto de anexos de la solicitud'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>

	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>

		
	<xsl:template match="/">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(8);

			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');

			validar[2] = new Array('nif', '<xsl:value-of select="$lang.nif"/>');
			validar[3] = new Array('nombre','<xsl:value-of select="$lang.nombre"/>');
			validar[4] = new Array('calle','<xsl:value-of select="$lang.calle"/>');
			validar[5] = new Array('c_postal','<xsl:value-of select="$lang.c_postal"/>');

			validar[6] = new Array('motivo','Motivo');
			validar[7] = new Array('mun_1','mun_1');
			

			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(89);
			
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

			especificos[13] = new Array('mun_1','mun_1');
			especificos[14] = new Array('liq_1','liq_1');
			especificos[15] = new Array('liq_2','liq_2');
			especificos[16] = new Array('liq_3','liq_3');
			especificos[17] = new Array('liq_4','liq_4');
			especificos[18] = new Array('liq_5','liq_5');
			especificos[19] = new Array('liq_6','liq_6');
			especificos[20] = new Array('liq_7','liq_7');
			especificos[21] = new Array('liq_8','liq_8');
			especificos[22] = new Array('liq_9','liq_9');
			especificos[23] = new Array('liq_10','liq_10');
			especificos[24] = new Array('liq_11','liq_11');
			especificos[25] = new Array('liq_12','liq_12');
			especificos[26] = new Array('liq_13','liq_13');
			especificos[27] = new Array('liq_14','liq_14');
			especificos[28] = new Array('liq_15','liq_15');

			especificos[29] = new Array('liq1_1','liq1_1');
			especificos[30] = new Array('liq1_2','liq1_2');
			especificos[31] = new Array('liq1_3','liq1_3');
			especificos[32] = new Array('liq1_4','liq1_4');
			especificos[33] = new Array('liq1_5','liq1_5');

			especificos[34] = new Array('pag_1','pag_1');
			especificos[35] = new Array('pag_2','pag_2');
			especificos[36] = new Array('pag_3','pag_3');
			especificos[37] = new Array('pag_4','pag_4');
			especificos[38] = new Array('pag_5','pag_5');
			especificos[39] = new Array('pag_6','pag_6');
			especificos[40] = new Array('pag_7','pag_7');
			especificos[41] = new Array('pag_8','pag_8');
			especificos[42] = new Array('pag_9','pag_9');
			especificos[43] = new Array('pag_10','pag_10');
			especificos[44] = new Array('pag_11','pag_11');
			especificos[45] = new Array('pag_12','pag_12');
			especificos[46] = new Array('pag_13','pag_13');
			especificos[47] = new Array('pag_14','pag_14');
			especificos[48] = new Array('pag_15','pag_15');

			especificos[49] = new Array('pag1_1','pag1_1');
			especificos[50] = new Array('pag1_2','pag1_2');
			especificos[51] = new Array('pag1_3','pag1_3');
			especificos[52] = new Array('pag1_4','pag1_4');
			especificos[53] = new Array('pag1_5','pag1_5');
			
			especificos[54] = new Array('motivo','motivo');
			especificos[55] = new Array('otro_motivo','otro_motivo');
			especificos[56] = new Array('ccc1','ccc1');
			especificos[57] = new Array('ccc2','ccc2');
			especificos[58] = new Array('ccc3','ccc3');
			especificos[59] = new Array('ccc4','ccc4');	

			especificos[60] = new Array('rciudad','rciudad');
			especificos[61] = new Array('rregion','rregion');
			especificos[62] = new Array('rc_postal','rc_postal');

			especificos[63] = new Array('nombresolihidden','nombresolihidden');
			especificos[64] = new Array('nifsolihidden','nifsolihidden');

			especificos[65] = new Array('titular','titular');
			especificos[66] = new Array('nifTitular','nifTitular');
			especificos[67] = new Array('telefonoTitular','telefonoTitular');
			especificos[68] = new Array('observaciones','observaciones');

			especificos[69] = new Array('liq1_6','liq1_6');
			especificos[70] = new Array('pag1_6','pag1_6');
			especificos[71] = new Array('liq1_7','liq1_7');
			especificos[72] = new Array('pag1_7','pag1_7');
			especificos[73] = new Array('liq1_8','liq1_8');
			especificos[74] = new Array('pag1_8','pag1_8');
			especificos[75] = new Array('liq1_9','liq1_9');
			especificos[76] = new Array('pag1_9','pag1_9');
			especificos[77] = new Array('liq1_10','liq1_10');
			especificos[78] = new Array('pag1_10','pag1_10');
			especificos[79] = new Array('liq1_11','liq1_11');
			especificos[80] = new Array('pag1_11','pag1_11');
			especificos[81] = new Array('liq1_12','liq1_12');
			especificos[82] = new Array('pag1_12','pag1_12');
			especificos[83] = new Array('liq1_13','liq1_13');
			especificos[84] = new Array('pag1_13','pag1_13');
			especificos[85] = new Array('liq1_14','liq1_14');
			especificos[86] = new Array('pag1_14','pag1_14');
			especificos[87] = new Array('liq1_15','liq1_15');
			especificos[88] = new Array('pag1_15','pag1_15');


			
			<xsl:call-template name="ADD_ROW" />
			max_num_rows = new Number(15);
			<xsl:call-template name="VALIDACIONES" />
			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			var validarNumero;
			function verificacionesEspecificas() {
				
				var f = document.forms[0];

				if(!validaNIFObligado()) return false;
				if(!validaNIFRepresentante()) return false;
				if(!validaNIFPresentador()) return false;
				else{
					document.getElementById('nombresolihidden').value = document.getElementById('nombreSolicitante').value;
					document.getElementById('nifsolihidden').value = document.getElementById('documentoIdentidad').value;
				}

				if(document.getElementById('mun_1').value=='000'){
					alert('Municipio obligatorio');
					document.getElementById('mun_1').focus();
					return false;
				}

				var liq = validaLiq();
				if(!liq) return false;
				
				if(!validaCC()) return false;

				return validarCuenta(f);
			}

			function validaLiq(){
				var i = 1;
				while(i!=16){
					if(document.getElementById('liq_'+i)!='undefined')
						if(document.getElementById('liq_'+i).value!=''){
						if(!validarLiquidacion(document.getElementById('liq_'+i))){
							alert('Nº de liquidación no válido: '+document.getElementById('liq_'+i).value);						
							document.getElementById('liq_'+i).focus();
							return false;
						}
					}

					if(document.getElementById('liq1_'+i)!='undefined')
						if(document.getElementById('liq1_'+i).value!=''){
						if(!validarLiquidacion(document.getElementById('liq1_'+i))){
							alert('Nº de liquidación no válido: '+document.getElementById('liq1_'+i).value);						
							document.getElementById('liq1_'+i).focus();
							return false;
						}
					}

					i = i+1;
				}
				
				return true;
			}

			function getDatosObligado(nif){
				window.open('tramites/000/REC_03/buscaObligado.jsp?valor='+nif+';000','','width=3,height=3');
			}
			function getDatosRepresentante(nif){
				window.open('tramites/000/REC_03/buscaRepresentante.jsp?valor='+nif+';000','','width=3,height=3');
			}

			var last_row2 = new Number(5);
			var max_num_rows2 = new Number(15);
			
			function addRow2() {
	            		if (last_row2 &lt; max_num_rows2)	{	
			        	last_row2 = last_row2 + 1;
                    			var row2 = document.getElementById("row1_"+last_row2.toString());
                    			row2.style.display = "";
                    			if (last_row2 == max_num_rows2)
                    			{
						var link2 = document.getElementById("addRow_link2");                        
                        			link2.style.display = "none";
					}
				}
			}
		</script>
		
		<h1><xsl:value-of select="$lang.titulo"/>	</h1>
		<xsl:call-template name="BOTON_LIMPIAR_FORMULARIO" />
   		<br/>
		<div class="submenu">
			<xsl:variable name="lang.datosPresentador" select="'Datos del presentador'"/>
   			<h1><xsl:value-of select="$lang.datosPresentador"/></h1>
   		</div>

		<xsl:call-template name="DATOS_SOLICITANTE_EDITABLE" />
		<xsl:call-template name="DATOS_OBLIGADO" />
   		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
   		<div class="cuadro" style="">	
	   		<b><xsl:value-of select="$lang.expone1"/></b><xsl:value-of select="$lang.expone2"/><br/>
				<select class="gr">
					<xsl:attribute name="style">border:none; width:250px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">mun_1</xsl:attribute>
					<xsl:attribute name="id">mun_1</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/mun_1"/></xsl:attribute>
					<xsl:call-template name="OPTIONS_MUNICIPIOS" />
				</select>	
			
			<table style="font-size:10px; margin:5 auto 20 auto; width:90%" cellspacing="0" border="1">
				<tr>
					<td style="width:20%;background-color:#dee1e8"><xsl:value-of select="$lang.liquidacion"/></td>
					<td style="width:20%;background-color:#dee1e8"><xsl:value-of select="$lang.pagada"/></td>
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

			</table>
           		<div style="margin:5px 0 20px 10px;">
                		<a id="addRow_link" style="cursor:pointer" onclick="addRow()">[Añadir fila]</a><br/>
            		</div>
		
			<xsl:value-of select="$lang.expone3"/>
			<select class="gr">
				<xsl:attribute name="style">margin-top:10px; width:400px;color:#006699;</xsl:attribute>
				<xsl:attribute name="name">motivo</xsl:attribute>
				<xsl:attribute name="id">motivo</xsl:attribute>
				<option selected="selected" value=""></option>
				<option value="MOTIVO_1"><xsl:value-of select="$lang.motivo1"/></option>
				<option value="MOTIVO_2"><xsl:value-of select="$lang.motivo2"/></option>
				<option value="MOTIVO_3"><xsl:value-of select="$lang.motivo3"/></option>
				<option value="MOTIVO_4"><xsl:value-of select="$lang.motivo4"/></option>
				<option value="MOTIVO_5"><xsl:value-of select="$lang.motivo5"/></option>
			</select>
			<br/>
			<div style="margin-left:10px;margin-top:10px;">
				<i><xsl:value-of select="$lang.notas_motivos1"/></i><br/>
				<i><xsl:value-of select="$lang.notas_motivos2"/></i>
			</div>
			<textarea>
				<xsl:attribute name="style">margin-left:75px;margin-top:5px;width:400px;font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;color:#006699;</xsl:attribute>
				<xsl:attribute name="name">otro_motivo</xsl:attribute>
				<xsl:attribute name="id">otro_motivo</xsl:attribute>
				<xsl:attribute name="rows">3</xsl:attribute>
				<xsl:value-of select="Datos_Registro/datos_especificos/otro_motivo"/>
			</textarea>

			<div style="margin-top:30px; width:100%; text-align:center;color:#006699;"><xsl:value-of select="$lang.liquidaciones"/></div>
			<table style="font-size:10px; margin:5 auto 20 auto; width:90%" cellspacing="0" border="1">
				<tr>
					<td style="width:20%;background-color:#dee1e8"><xsl:value-of select="$lang.liquidacion"/></td>
					<td style="width:20%;background-color:#dee1e8"><xsl:value-of select="$lang.pagada"/></td>
				</tr>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">1</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">2</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">3</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">4</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">5</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">6</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">7</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">8</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">9</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">10</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">11</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">12</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">13</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">14</xsl:with-param></xsl:call-template>
				<xsl:call-template name="FIELDS2"><xsl:with-param name="row_id1">15</xsl:with-param></xsl:call-template>

			</table>
           		<div style="margin:5px 0 20px 10px;">
                		<a id="addRow_link2" style="cursor:pointer" onclick="addRow2()">[Añadir fila]</a><br/>
            		</div>
			<hr/>
			<div style="display:inline;margin-right:20px;color:#006699;"><xsl:value-of select="$lang.observaciones"/>:</div>
			<br/>
		   	<textarea>
				<xsl:attribute name="style">margin-left:75px;margin-top:5px;width:450px;font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;color:#006699;</xsl:attribute>
				<xsl:attribute name="name">observaciones</xsl:attribute>
				<xsl:attribute name="id">observaciones</xsl:attribute>
				<xsl:attribute name="rows">3</xsl:attribute>
				<xsl:attribute name="onkeypress">if(this.value.length&gt;255){ alert('Tamaño máximo de campo alcanzado'); this.value = this.value.substr(0,255);}</xsl:attribute>
				<xsl:attribute name="onkeyup">if(this.value.length&gt;255){ alert('Tamaño máximo de campo alcanzado'); this.value = this.value.substr(0,255);}</xsl:attribute>				
				<xsl:value-of select="Datos_Registro/datos_especificos/observaciones"/>
			</textarea>
			<hr/>
			<br/>
			<b><xsl:value-of select="$lang.solicita1"/></b><xsl:value-of select="$lang.solicita2"/><br/>
	   		<br/>
			<xsl:value-of select="$lang.solicita3"/><b><xsl:value-of select="$lang.solicita1"/></b>, <xsl:value-of select="$lang.solicita4"/><xsl:value-of select="$lang.ccc"/><br/>
	   		<br/>
			
			<xsl:call-template name="CUENTA_CORRIENTE" />
	   		<br/>
			
			<div style="margin-bottom:10px;margin-top:30px;"><i><xsl:value-of select="$lang.notas"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas1"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas2"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas3"/></i></div>
		</div>
   		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<label class="gr">
			   		<xsl:attribute name="style">width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.anexarInfo1"/><br/>
		   			<xsl:value-of select="$lang.anexarInfo2"/><br/>
			</label>
			<br/>
   			<div class="col">
	   			<label class="gr">
					<xsl:attribute name="style">width:250px;</xsl:attribute>
					<xsl:value-of select="$lang.documento"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">width:400px; </xsl:attribute>
					<xsl:attribute name="name">REC-03-D1</xsl:attribute>
					<xsl:attribute name="id">REC-03-D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">REC-03-D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">REC-03-D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
			</div>
   		</div>
   		<br/>
		<input type="hidden"><xsl:attribute name="id">telefono</xsl:attribute></input>
		<input type="hidden"><xsl:attribute name="id">domicilioNotificacion</xsl:attribute></input>
		<input type="hidden"><xsl:attribute name="id">localidad</xsl:attribute></input>
		<input type="hidden"><xsl:attribute name="id">provincia</xsl:attribute></input>
		<input type="hidden"><xsl:attribute name="id">codigoPostal</xsl:attribute></input>
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
	</xsl:template>

	<xsl:template name="FIELDS">
		<xsl:param name="row_id" />
		<xsl:variable name="param_liq">liq_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_pag">pag_<xsl:value-of select="$row_id"/></xsl:variable>
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
					<xsl:attribute name="name"><xsl:value-of select="$param_liq"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param_liq"/></xsl:attribute>
					<xsl:attribute name="maxlength">17</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_liq]"/></xsl:attribute>
				</input>
			</td>
			<td>
				<select class="gr">
			        <xsl:attribute name="style">border:none;color:#006699;width:100%;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_pag"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_pag"/></xsl:attribute>
					<option selected="selected" value=""></option>
					<option value="PAGADA">PAGADA</option>
					<option value="PENDIENTE">PENDIENTE</option>
				</select>
			</td>
		</tr>
	</xsl:template>

	<xsl:template name="FIELDS2">
		<xsl:param name="row_id1" />
		<xsl:variable name="param_liq">liq1_<xsl:value-of select="$row_id1"/></xsl:variable>
		<xsl:variable name="param_pag">pag1_<xsl:value-of select="$row_id1"/></xsl:variable>
		<xsl:variable name="row_style">
	    <xsl:choose>
            <xsl:when test="$row_id1&gt;'5'">
   		        display:none;
            </xsl:when>
        </xsl:choose>
	    </xsl:variable>
		<tr id="row1_{$row_id1}" style="{$row_style}">
			<td>
				<input type="text">
					<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param_liq"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param_liq"/></xsl:attribute>
					<xsl:attribute name="maxlength">17</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_liq]"/></xsl:attribute>
				</input>
			</td>
			<td>
				<select class="gr">
			        <xsl:attribute name="style">border:none;color:#006699;width:100%;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_pag"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_pag"/></xsl:attribute>
					<option selected="selected" value=""></option>
					<option value="PAGADA">PAGADA</option>
					<option value="PENDIENTE">PENDIENTE</option>
				</select>
			</td>
		</tr>
	</xsl:template>

</xsl:stylesheet>
