<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />
<xsl:include href="../REC_COMUNES/template_IBAN.xsl" />


<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'Formulario de solicitud de beneficio fiscal en el Impuesto sobre Bienes Inmuebles (IBI)'"/>

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
	<xsl:variable name="lang.expone2" select="', que considerando que tiene derecho al disfrute del beneficio fiscal en el IBI Familia numerosa (IBI-URBANA) correspondientes al municipio de: '"/>
	<xsl:variable name="lang.notas_motivos1" select="'Se deberá aportar copia del libro de familia numerosa vigente y certificados de empadronamiento y de residencia.'"/>
	<xsl:variable name="lang.liquidaciones" select="'LIQUIDACIÓN E INMUEBLE OBJETO DE LA SOLICITUD*'"/>
	<xsl:variable name="lang.municipio" select="'Nombre del municipio'"/>
	<xsl:variable name="lang.liquidacion" select="'Nº de liquidación'"/>
	<xsl:variable name="lang.referencia" select="'Referencia catastral'"/>
	<xsl:variable name="lang.situacion" select="'Situación del inmueble'"/>
	<xsl:variable name="lang.solicita1" select="'SOLICITA'"/>
	<xsl:variable name="lang.solicita2" select="', que previa las comprobaciones oportunas, procedan a conceder el beneficio fiscal solicitado para los años:'"/>
    	<xsl:variable name="lang.cuantia" select="'Cuantía de la devolución que se solicita:'"/>
	<xsl:variable name="lang.cuenta" select="'Código de Cuenta Corriente:'"/>
	<xsl:variable name="lang.notas" select="'INFORMACIÓN PARA EL CONTRIBUYENTE:'"/>
	<xsl:variable name="lang.notas1" select="'1.- El plazo máximo de resolución de su solicitud es de 6 meses, siendo los efectos del silencio administrativo NEGATIVOS.'"/>
	<xsl:variable name="lang.notas2" select="'2.- Es necesario consignar obligatoriamente los campos de nº de liquidación y referencia catastral con sus 20 dígitos.'"/>
	<xsl:variable name="lang.notas3" select="'3.- En el caso de que la solicitud venga referida a liquidaciones a nombre de otro obligado tributario, deberá acreditarse la representación legal o voluntaria (ver modelo en la página) del mismo.'"/>
	
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero (*.zip), pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.documento" select="'Conjunto de anexos de la solicitud'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>

	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>
	
	<xsl:template match="/" xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(14);

			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');

			validar[2] = new Array('nif', '<xsl:value-of select="$lang.nif"/>');
			validar[3] = new Array('nombre','<xsl:value-of select="$lang.nombre"/>');
			validar[4] = new Array('calle','<xsl:value-of select="$lang.calle"/>');
			validar[5] = new Array('c_postal','<xsl:value-of select="$lang.c_postal"/>');

			validar[6] = new Array('motivo','Motivo');
			
			validar[7] = new Array('mun_1','<xsl:value-of select="$lang.municipio"/>');
			validar[8] = new Array('liq_1','<xsl:value-of select="$lang.liquidacion"/>');
			validar[9] = new Array('ref1_1','<xsl:value-of select="$lang.referencia"/>');
			validar[10] = new Array('ref1_1','<xsl:value-of select="$lang.referencia"/>');
			validar[11] = new Array('ref1_1','<xsl:value-of select="$lang.referencia"/>');
			validar[12] = new Array('ref1_1','<xsl:value-of select="$lang.referencia"/>');
			validar[13] = new Array('sit_1','<xsl:value-of select="$lang.situacion"/>');

			//Array con los datos especificos del formulario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(155);
			
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
			especificos[15] = new Array('ref1_1','ref1_1');
			especificos[16] = new Array('ref2_1','ref2_1');
			especificos[17] = new Array('ref3_1','ref3_1');
			especificos[18] = new Array('ref4_1','ref4_1');
			especificos[19] = new Array('sit_1','sit_1');
			especificos[20] = new Array('liq_2','liq_2');
			especificos[21] = new Array('ref1_2','ref1_2');
			especificos[22] = new Array('ref2_2','ref2_2');
			especificos[23] = new Array('ref3_2','ref3_2');
			especificos[24] = new Array('ref4_2','ref4_2');
			especificos[25] = new Array('sit_2','sit_2');
			especificos[26] = new Array('liq_3','liq_3');
			especificos[27] = new Array('ref1_3','ref1_3');
			especificos[28] = new Array('ref2_3','ref2_3');
			especificos[29] = new Array('ref3_3','ref3_3');
			especificos[30] = new Array('ref4_3','ref4_3');
			especificos[31] = new Array('sit_3','sit_3');
			especificos[32] = new Array('liq_4','liq_4');
			especificos[33] = new Array('ref1_4','ref1_4');
			especificos[34] = new Array('ref2_4','ref2_4');
			especificos[35] = new Array('ref3_4','ref3_4');
			especificos[36] = new Array('ref4_4','ref4_4');
			especificos[37] = new Array('sit_4','sit_4');
			especificos[38] = new Array('liq_5','liq_5');
			especificos[39] = new Array('ref1_5','ref1_5');
			especificos[40] = new Array('ref2_5','ref2_5');
			especificos[41] = new Array('ref3_5','ref3_5');
			especificos[42] = new Array('ref4_5','ref4_5');
			especificos[43] = new Array('sit_5','sit_5');
			especificos[44] = new Array('liq_6','liq_6');
			especificos[45] = new Array('ref1_6','ref1_6');
			especificos[46] = new Array('ref2_6','ref2_6');
			especificos[47] = new Array('ref3_6','ref3_6');
			especificos[48] = new Array('ref4_6','ref4_6');
			especificos[49] = new Array('sit_6','sit_6');
			especificos[50] = new Array('liq_7','liq_7');
			especificos[51] = new Array('ref1_7','ref1_7');
			especificos[52] = new Array('ref2_7','ref2_7');
			especificos[53] = new Array('ref3_7','ref3_7');
			especificos[54] = new Array('ref4_7','ref4_7');
			especificos[55] = new Array('sit_7','sit_7');
			especificos[56] = new Array('liq_8','liq_8');
			especificos[57] = new Array('ref1_8','ref1_8');
			especificos[58] = new Array('ref2_8','ref2_8');
			especificos[59] = new Array('ref3_8','ref3_8');
			especificos[60] = new Array('ref4_8','ref4_8');
			especificos[61] = new Array('sit_8','sit_8');
			especificos[62] = new Array('liq_9','liq_9');
			especificos[63] = new Array('ref1_9','ref1_9');
			especificos[64] = new Array('ref2_9','ref2_9');
			especificos[65] = new Array('ref3_9','ref3_9');
			especificos[66] = new Array('ref4_9','ref4_9');
			especificos[67] = new Array('sit_9','sit_9');
			especificos[68] = new Array('liq_10','liq_10');
			especificos[69] = new Array('ref1_10','ref1_10');
			especificos[70] = new Array('ref2_10','ref2_10');
			especificos[71] = new Array('ref3_10','ref3_10');
			especificos[72] = new Array('ref4_10','ref4_10');
			especificos[73] = new Array('sit_10','sit_10');
			especificos[74] = new Array('liq_11','liq_11');
			especificos[75] = new Array('ref1_11','ref1_11');
			especificos[76] = new Array('ref2_11','ref2_11');
			especificos[77] = new Array('ref3_11','ref3_11');
			especificos[78] = new Array('ref4_11','ref4_11');
			especificos[79] = new Array('sit_11','sit_11');
			especificos[80] = new Array('liq_12','liq_12');
			especificos[81] = new Array('ref1_12','ref1_12');
			especificos[82] = new Array('ref2_12','ref2_12');
			especificos[83] = new Array('ref3_12','ref3_12');
			especificos[84] = new Array('ref4_12','ref4_12');
			especificos[85] = new Array('sit_12','sit_12');
			especificos[86] = new Array('liq_13','liq_13');
			especificos[87] = new Array('ref1_13','ref1_13');
			especificos[88] = new Array('ref2_13','ref2_13');
			especificos[89] = new Array('ref3_13','ref3_13');
			especificos[90] = new Array('ref4_13','ref4_13');
			especificos[91]= new Array('sit_13','sit_13');
			especificos[92] = new Array('liq_14','liq_14');
			especificos[93] = new Array('ref1_14','ref1_14');
			especificos[94] = new Array('ref2_14','ref2_14');
			especificos[95] = new Array('ref3_14','ref3_14');
			especificos[96] = new Array('ref4_14','ref4_14');
			especificos[97] = new Array('sit_14','sit_14');
			especificos[98] = new Array('liq_15','liq_15');
			especificos[99] = new Array('ref1_15','ref1_15');
			especificos[100] = new Array('ref2_15','ref2_15');
			especificos[101] = new Array('ref3_15','ref3_15');
			especificos[102] = new Array('ref4_15','ref4_15');
			especificos[103] = new Array('sit_15','sit_15');
			especificos[104] = new Array('liq_16','liq_16');
			especificos[105] = new Array('ref1_16','ref1_16');
			especificos[106] = new Array('ref2_16','ref2_16');
			especificos[107] = new Array('ref3_16','ref3_16');
			especificos[108] = new Array('ref4_16','ref4_16');
			especificos[109] = new Array('sit_16','sit_16');
			especificos[110] = new Array('liq_17','liq_17');
			especificos[111] = new Array('ref1_17','ref1_17');
			especificos[112] = new Array('ref2_17','ref2_17');
			especificos[113] = new Array('ref3_17','ref3_17');
			especificos[114] = new Array('ref4_17','ref4_17');
			especificos[115] = new Array('sit_17','sit_17');
			especificos[116] = new Array('liq_18','liq_18');
			especificos[117] = new Array('ref1_18','ref1_18');
			especificos[118] = new Array('ref2_18','ref2_18');
			especificos[119] = new Array('ref3_18','ref3_18');
			especificos[120] = new Array('ref4_18','ref4_18');
			especificos[121] = new Array('sit_18','sit_18');
			especificos[122] = new Array('liq_19','liq_19');
			especificos[123] = new Array('ref1_19','ref1_19');
			especificos[124] = new Array('ref2_19','ref2_19');
			especificos[125] = new Array('ref3_19','ref3_19');
			especificos[126] = new Array('ref4_19','ref4_19');
			especificos[127] = new Array('sit_19','sit_19');
			especificos[128] = new Array('liq_20','liq_20');
			especificos[129] = new Array('ref1_20','ref1_20');
			especificos[130] = new Array('ref2_20','ref2_20');
			especificos[131] = new Array('ref3_20','ref3_20');
			especificos[132] = new Array('ref4_20','ref4_20');
			especificos[133] = new Array('sit_20','sit_20');
			
			especificos[134] = new Array('motivo','motivo');
			especificos[135] = new Array('otro_motivo','otro_motivo');
			especificos[136] = new Array('years','years');
			especificos[137] = new Array('cuantia','cuantia');
			especificos[138] = new Array('ccc1','ccc1');
			especificos[139] = new Array('ccc2','ccc2');
			especificos[140] = new Array('ccc3','ccc3');
			especificos[141] = new Array('ccc4','ccc4');

			especificos[142] = new Array('rciudad','rciudad');
			especificos[143] = new Array('rregion','rregion');
			especificos[144] = new Array('rc_postal','rc_postal');

			especificos[145] = new Array('nombresolihidden','nombresolihidden');
			especificos[146] = new Array('nifsolihidden','nifsolihidden');

			especificos[147] = new Array('titular','titular');
			especificos[148] = new Array('nifTitular','nifTitular');
			especificos[149] = new Array('telefonoTitular','telefonoTitular');
			especificos[150] = new Array('years2','years2');

			especificos[151] = new Array('observaciones','observaciones');
			especificos[152] = new Array('iban','iban');

			especificos[153] = new Array('texto_legal_comun','texto_legal_comun');
			especificos[154] = new Array('texto_datos_personales_comun','texto_datos_personales_comun');
			
			<xsl:call-template name="ADD_ROW2" />
			
			<xsl:call-template name="VALIDACIONES" />
			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			
			function verificacionesEspecificas() {

				document.getElementById('motivo').value = '4';

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

				//var liq = validaLiq();
				//if(!liq) return false;
				
				if(!validaCC()) return false;

				if(!validarCuenta(f)) return false;

				document.getElementById('iban').value = document.getElementById('iban').value.toUpperCase();
				var iban = String(document.getElementById('iban').value) + String(document.getElementById('ccc1').value) + String(document.getElementById('ccc2').value) + String(document.getElementById('ccc3').value) + String(document.getElementById('ccc4').value);

				if(!validarIBAN(iban)){
					alert('El campo IBAN es incorrecto.');
					document.getElementById('iban').focus();
					return false;				
				}

				if(!validaSiTitularCC()){return false;}
				if(!validaCC()) return false;

				return true;
			}

			function validaLiq(){
				var i = 1;
				while(i!=21){
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
				window.open('tramites/001/REC_05_IV_INTERNO/buscaObligado.jsp?valor='+nif+';001','','width=3,height=3');
			}
			function getDatosRepresentante(nif){
				window.open('tramites/001/REC_05_IV_INTERNO/buscaRepresentante.jsp?valor='+nif+';001','','width=3,height=3');
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

		<xsl:variable name="fileMotBene" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosTablaValidacionConSustituto('REC_VLDTBL_MOT_BENEFICIO_IBI','001')"/>
		<xsl:variable name="motBene" select="document($fileMotBene)"/>

   		<div class="cuadro" style="">	

	   		<b><xsl:value-of select="$lang.expone1"/></b><xsl:value-of select="$lang.expone2"/>
				<select class="gr">
					<xsl:attribute name="style">border:none; width:250px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">mun_1</xsl:attribute>
					<xsl:attribute name="id">mun_1</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/mun_1"/></xsl:attribute>
					<xsl:call-template name="OPTIONS_MUNICIPIOS" />
				</select>

			<select class="gr">
				<xsl:attribute name="style">margin-top:10px; width:400px;color:#006699;display:none;</xsl:attribute>
				<xsl:attribute name="name">motivo</xsl:attribute>
				<xsl:attribute name="id">motivo</xsl:attribute>
				<xsl:for-each select="$motBene/listado/dato">
				<option>
					<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
					<xsl:value-of select="valor"/> - <xsl:value-of select="sustituto"/>
				</option>
			</xsl:for-each>
			</select>
			<br/>
			<div style="margin-left:10px;margin-top:10px;">
				<b><i><xsl:value-of select="$lang.notas_motivos1"/></i></b>
			</div>
			<textarea>
				<xsl:attribute name="style">margin-left:75px;margin-top:5px;width:400px;font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;color:#006699;display:none;</xsl:attribute>
				<xsl:attribute name="name">otro_motivo</xsl:attribute>
				<xsl:attribute name="id">otro_motivo</xsl:attribute>
				<xsl:attribute name="rows">3</xsl:attribute>
				<xsl:value-of select="Datos_Registro/datos_especificos/otro_motivo"/>
			</textarea>

			<div style="margin-top:30px; width:100%; text-align:center;color:#006699;"><xsl:value-of select="$lang.liquidaciones"/></div>
			<table style="font-size:10px; width:90%; table-layout: fixed;" cellspacing="0" border="1">
			    <tr>
					<td style="width:155px;background-color:#dee1e8"><xsl:value-of select="$lang.liquidacion"/></td>
					<td style="width:170px;background-color:#dee1e8"><xsl:value-of select="$lang.referencia"/></td>
					<td style="width:150px;background-color:#dee1e8"><xsl:value-of select="$lang.situacion"/></td>
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


			<xsl:call-template name="SOLICITA1" />
	   		<br/>
			
			<xsl:call-template name="SOLICITA3" />
	   		
		</div>
   		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN" />
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
					<xsl:attribute name="name">REC-05-D1</xsl:attribute>
					<xsl:attribute name="id">REC-05-D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">REC-05-D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">REC-05-D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
			</div>
   		</div>


		<div class="cuadro" style="color: grey; text-align:justify">
		<br/>
			
			<div style="margin-bottom:10px;margin-top:30px;"><i><xsl:value-of select="$lang.notas"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas1"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas2"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas3"/></i></div>
		</div>
		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN" />
   		<br/>
		<xsl:call-template name="TEXTO_COMPARECE_COMUN" />
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
	    <xsl:variable name="param_ref1">ref1_<xsl:value-of select="$row_id"/></xsl:variable>
	    <xsl:variable name="param_ref2">ref2_<xsl:value-of select="$row_id"/></xsl:variable>
	    <xsl:variable name="param_ref3">ref3_<xsl:value-of select="$row_id"/></xsl:variable>
	    <xsl:variable name="param_ref4">ref4_<xsl:value-of select="$row_id"/></xsl:variable>
	    <xsl:variable name="param_sit">sit_<xsl:value-of select="$row_id"/></xsl:variable>
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
			        <xsl:attribute name="style">border:none;color:#006699;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_liq"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_liq"/></xsl:attribute>
 				 <xsl:attribute name="maxlength">30</xsl:attribute>
				 <xsl:attribute name="onkeyup">if(this.value.length==30){document.getElementById('<xsl:value-of select="$param_ref1"/>').focus();}</xsl:attribute>
			        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_liq]"/></xsl:attribute>
		        </input>
	        </td>
	        <td>
		        <input type="text">
			        <xsl:attribute name="style">border:none;margin:0;width:90px;color:#006699;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_ref1"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_ref1"/></xsl:attribute>
			        <xsl:attribute name="size">14</xsl:attribute>
			        <xsl:attribute name="maxlength">14</xsl:attribute>
				 <xsl:attribute name="onkeyup">if(this.value.length==14){document.getElementById('<xsl:value-of select="$param_ref2"/>').focus();}</xsl:attribute>
			        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_ref1]"/></xsl:attribute>
		        </input>·
		        <input type="text">
			        <xsl:attribute name="style">border:none;margin:0;width:32px;color:#006699;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_ref2"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_ref2"/></xsl:attribute>
			        <xsl:attribute name="size">4</xsl:attribute>
			        <xsl:attribute name="maxlength">4</xsl:attribute>
				 <xsl:attribute name="onkeyup">if(this.value.length==4){document.getElementById('<xsl:value-of select="$param_ref3"/>').focus();}</xsl:attribute>
			        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_ref2]"/></xsl:attribute>
		        </input>·
		        <input type="text">
			        <xsl:attribute name="style">border:none;margin:0;width:12px;color:#006699;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_ref3"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_ref3"/></xsl:attribute>
			        <xsl:attribute name="size">1</xsl:attribute>
			        <xsl:attribute name="maxlength">1</xsl:attribute>
				 <xsl:attribute name="onkeyup">if(this.value.length==1){document.getElementById('<xsl:value-of select="$param_ref4"/>').focus();}</xsl:attribute>
			        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_ref3]"/></xsl:attribute>
		        </input>·
		        <input type="text">
			        <xsl:attribute name="style">border:none;margin:0;width:12px;color:#006699;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_ref4"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_ref4"/></xsl:attribute>
			        <xsl:attribute name="size">1</xsl:attribute>
			        <xsl:attribute name="maxlength">1</xsl:attribute>
				 <xsl:attribute name="onkeyup">if(this.value.length==1){document.getElementById('<xsl:value-of select="$param_sit"/>').focus();}</xsl:attribute>
			        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_ref4]"/></xsl:attribute>
		        </input>
	        </td>
	        <td>
		        <input type="text">
			        <xsl:attribute name="style">border:none;color:#006699;width:150px;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_sit"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_sit"/></xsl:attribute>
			        <xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_sit]"/></xsl:attribute>
		        </input>
	        </td>
	    </tr>
<script>
document.getElementById('motivo').value = '4';
</script>
	</xsl:template>

	<xsl:template name="ADD_ROW2">
			var last_row = new Number(5);
			var max_num_rows = new Number(15);
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

</xsl:stylesheet>
