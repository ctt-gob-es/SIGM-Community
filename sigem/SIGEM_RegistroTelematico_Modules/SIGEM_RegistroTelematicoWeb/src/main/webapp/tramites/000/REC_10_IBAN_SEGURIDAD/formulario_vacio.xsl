<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'Formulario de solicitud de devolución derivada de la normativa del tributo IAE'"/>

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
	<xsl:variable name="lang.expone2" select="', que habiendo efectuado el pago de las liquidaciones, correspondientes al municipio de '"/>
	<xsl:variable name="lang.expone3" select="', que se indican por el año completo, y habiendo cesado en el ejercicio de la actividad, tal como se acredita con la copia adjunta de la baja presentada en A.E.A.T. (adjuntar copia baja).'"/>
	<xsl:variable name="lang.liquidaciones" select="'LIQUIDACIONES OBJETO DE DEVOLUCIÓN*'"/>
	<xsl:variable name="lang.municipio" select="'Nombre del municipio'"/>
	<xsl:variable name="lang.liquidacion" select="'Nº de liquidación'"/>
	<xsl:variable name="lang.epigrafe" select="'Epígrafe'"/>
	<xsl:variable name="lang.referencia" select="'Referencia de la AEAT'"/>	<xsl:variable name="lang.solicita1" select="'SOLICITA'"/>
	<xsl:variable name="lang.solicita2" select="', que previa las comprobaciones oportunas, procedan a efectuar la devolución que por los trimestres no ejercidos a la siguiente entidad y cuenta corriente :'"/>
	<xsl:variable name="lang.cuantia" select="'Cuantía de la devolución que se solicita:'"/>
	<xsl:variable name="lang.cuenta" select="'Código de Cuenta Corriente:*'"/>
	<xsl:variable name="lang.ccc" select="''"/>
	<xsl:variable name="lang.notas" select="'NOTAS INFORMATIVAS:'"/>
	<xsl:variable name="lang.notas1" select="'1.- El plazo máximo de resolución de su solicitud es de 6 meses, siendo los efectos del silencio administrativo NEGATIVOS. Asimismo, si transcurriere dicho plazo sin que se hubiere efectuado la devolución, se devengarán los intereses de demora correspondientes desde el día siguiente a la finalización de los 6 meses hasta la fecha en que se apruebe la devolución, caso de que ésta proceda.'"/>
	<xsl:variable name="lang.notas2" select="'2.- Efectuada la devolución en la cuenta indicada, se entenderá estimada la solicitud sin necesidad de comunicación expresa en este sentido.'"/>
	<xsl:variable name="lang.notas3" select="'3.- En el caso de que la solicitud venga referida a liquidaciones a nombre de otro obligado tributario, deberá acreditarse la representación legal o voluntaria ( ver modelo en la página) del mismo.'"/>
	
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
			var validar = new Array(10);

			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');

			validar[2] = new Array('nif', '<xsl:value-of select="$lang.nif"/>');
			validar[3] = new Array('nombre','<xsl:value-of select="$lang.nombre"/>');
			validar[4] = new Array('calle','<xsl:value-of select="$lang.calle"/>');
			validar[5] = new Array('c_postal','<xsl:value-of select="$lang.c_postal"/>');

			//validar[6] = new Array('cuantia','Cuantía solicitada');
			validar[6] = new Array('ccc1','Código de entidad');
			validar[7] = new Array('ccc2','Código de sucursal');
			validar[8] = new Array('ccc3','D.C.');
			validar[9] = new Array('ccc4','Cuenta corriente');
			

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
			especificos[29] = new Array('liq_16','liq_16');
			especificos[30] = new Array('liq_17','liq_17');
			especificos[31] = new Array('liq_18','liq_18');
			especificos[32] = new Array('liq_19','liq_19');
			especificos[33] = new Array('liq_20','liq_20');
			
			especificos[34] = new Array('cuantia','cuantia');
			especificos[35] = new Array('ccc1','ccc1');
			especificos[36] = new Array('ccc2','ccc2');
			especificos[37] = new Array('ccc3','ccc3');
			especificos[38] = new Array('ccc4','ccc4');
			especificos[39] = new Array('motivo','motivo');

			especificos[40] = new Array('rciudad','rciudad');
			especificos[41] = new Array('rregion','rregion');
			especificos[42] = new Array('rc_postal','rc_postal');

			especificos[43] = new Array('nombresolihidden','nombresolihidden');
			especificos[44] = new Array('nifsolihidden','nifsolihidden');

			especificos[45] = new Array('titular','titular');
			especificos[46] = new Array('nifTitular','nifTitular');
			especificos[47] = new Array('telefonoTitular','telefonoTitular');
			especificos[48] = new Array('observaciones','observaciones');

			especificos[49] = new Array('ent_1','ent_1');
			especificos[50] = new Array('ent_2','ent_2');
			especificos[51] = new Array('ent_3','ent_3');
			especificos[52] = new Array('ent_4','ent_4');
			especificos[53] = new Array('ent_5','ent_5');
			especificos[54] = new Array('ent_6','ent_6');
			especificos[55] = new Array('ent_7','ent_7');
			especificos[56] = new Array('ent_8','ent_8');
			especificos[57] = new Array('ent_9','ent_9');
			especificos[58] = new Array('ent_10','ent_10');
			especificos[59] = new Array('ent_11','ent_11');
			especificos[60] = new Array('ent_12','ent_12');
			especificos[61] = new Array('ent_13','ent_13');
			especificos[62] = new Array('ent_14','ent_14');
			especificos[63] = new Array('ent_15','ent_15');
			especificos[64] = new Array('ent_16','ent_16');
			especificos[65] = new Array('ent_17','ent_17');
			especificos[66] = new Array('ent_18','ent_18');
			especificos[67] = new Array('ent_19','ent_19');
			especificos[68] = new Array('ent_20','ent_20');
			especificos[69] = new Array('imp_1','imp_1');
			especificos[70] = new Array('imp_2','imp_2');
			especificos[71] = new Array('imp_3','imp_3');
			especificos[72] = new Array('imp_4','imp_4');
			especificos[73] = new Array('imp_5','imp_5');
			especificos[74] = new Array('imp_6','imp_6');
			especificos[75] = new Array('imp_7','imp_7');
			especificos[76] = new Array('imp_8','imp_8');
			especificos[77] = new Array('imp_9','imp_9');
			especificos[78] = new Array('imp_10','imp_10');
			especificos[79] = new Array('imp_11','imp_11');
			especificos[80] = new Array('imp_12','imp_12');
			especificos[81] = new Array('imp_13','imp_13');
			especificos[82] = new Array('imp_14','imp_14');
			especificos[83] = new Array('imp_15','imp_15');
			especificos[84] = new Array('imp_16','imp_16');
			especificos[85] = new Array('imp_17','imp_17');
			especificos[86] = new Array('imp_18','imp_18');
			especificos[87] = new Array('imp_19','imp_19');
			especificos[88] = new Array('imp_20','imp_20');


			
			<xsl:call-template name="ADD_ROW" />
			
			<xsl:call-template name="VALIDACIONES" />
			
			//Array de validaciones
			//----------------------------------------------
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
				while(i!=20){
					if(document.getElementById('liq_'+i)!='undefined')
						if(document.getElementById('liq_'+i).value!=''){
						if(!validarLiquidacion(document.getElementById('liq_'+i))){
							alert('Nº de liquidación no válido: '+document.getElementById('liq_'+i).value);						
							document.getElementById('liq_'+i).focus();
							return false;
						}
					}
					i = i+1;
				}
				return true;
			}


			function getDatosObligado(nif){
				window.open('tramites/000/REC_10/buscaObligado.jsp?valor='+nif+';000','','width=3,height=3');
			}
			function getDatosRepresentante(nif){
				window.open('tramites/000/REC_10/buscaRepresentante.jsp?valor='+nif+';000','','width=3,height=3');
			}
		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
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
			<xsl:value-of select="$lang.expone3"/><br/>
			<br/>

			<div style="margin-top:30px; width:100%; text-align:center;color:#006699;"><xsl:value-of select="$lang.liquidaciones"/></div>
			<table style="font-size:10px; margin:5 auto 20 auto; width:90%" cellspacing="0" border="1">
				<tr>
					<td style="width:25%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.liquidacion"/></td>
					<td style="width:20%;background-color:#dee1e8"><xsl:value-of select="$lang.epigrafe"/></td>
					<td style="width:30%;background-color:#dee1e8"><xsl:value-of select="$lang.referencia"/></td>
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

			<b><xsl:value-of select="$lang.solicita1"/></b><xsl:value-of select="$lang.solicita2"/><br/>
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
				<xsl:call-template name="CUENTA_CORRIENTE" />
			</div>
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
					<xsl:attribute name="name">REC-01-D1</xsl:attribute>
					<xsl:attribute name="id">REC-01-D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">REC-01-D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">REC-01-D1_Tipo</xsl:attribute>
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
			<xsl:attribute name="name">motivo</xsl:attribute>
			<xsl:attribute name="id">motivo</xsl:attribute>
			<xsl:attribute name="value">MOTIVO_1</xsl:attribute>
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
		<xsl:variable name="param_epi">ent_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref">imp_<xsl:value-of select="$row_id"/></xsl:variable>
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
				<input type="text">
					<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param_epi"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param_epi"/></xsl:attribute>
					<xsl:attribute name="maxlength">6</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_epi]"/></xsl:attribute>
				</input>
			</td>
			<td>
				<input type="text">
					<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param_ref"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param_ref"/></xsl:attribute>
					<xsl:attribute name="maxlength">13</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_ref]"/></xsl:attribute>
				</input>
			</td>
		</tr>
				
	</xsl:template>

</xsl:stylesheet>
