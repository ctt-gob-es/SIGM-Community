<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />
<xsl:include href="../REC_COMUNES/template_IBAN.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'Formulario de Solicitud de Fraccionamiento de Pago en el Padrón de IBI'"/>

	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>
	<xsl:variable name="lang.nif" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre" select="'Apellidos y Nombre o Denominación Social'"/>
	<xsl:variable name="lang.calle" select="'Calle'"/>
	<xsl:variable name="lang.c_postal" select="'Código Postal'"/>
	<xsl:variable name="lang.iban" select="'IBAN'"/>

	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.municipios" select="'MUNICIPIO:'"/>
	<xsl:variable name="lang.textoInmuebles" select="'INMUEBLES A LOS QUE AFECTA LA SOLICITUD, SÓLO PARA IBI-URBANA e IBI-CONSTRUCCIONES RÚSTICAS.-'"/>
	<xsl:variable name="lang.notasInmuebles" select="'Indicar los 20 dígitos de la referencia catastral.'"/>
	<xsl:variable name="lang.referencia" select="'Referencia catastral'"/>

	<xsl:variable name="lang.textoIban" select="'DOMICILIACIÓN (con IBAN).-'"/>

	<xsl:variable name="lang.notas" select="'Notas Informativas.-'"/>
	<xsl:variable name="lang.notas1" select="'1.- El pago se ejecutará mediante el sistema de domiciliación bancaria, a mediados de julio y octubre.'"/>
	<xsl:variable name="lang.notas2" select="'2.- El impago o la devolución del 1º fraccionamiento, dejará sin virtualidad el 2º, siendo exigible el pago del total de la deuda durante el periodo de cobro en el que se exija el padrón del IBI. El impago del 2º plazo, determinará la exigibilidad de la deuda en vía ejecutiva. La devolución y/o impago de algunos de los plazos implicará que la solicitud quede sin efecto para próximos ejercicios.'"/>
	<xsl:variable name="lang.notas3" select="'3.- En los casos en que concurran varios cotitulares como sujetos pasivos del impuesto, la solicitud deberán realizarla conjuntamente todos y cada uno de los obligados tributarios. Quedarán exceptuados los casos de cotitularidad por razón del matrimonio, en cuyo supuesto bastará que la solicitud sea instada por uno cualquiera de los cónyuges.'"/>
	<xsl:variable name="lang.notas4" select="'4.- Las solicitudes serán resueltas por la Diputación de Ciudad Real, como ente gestor del impuesto por delegación del Ayuntamiento, entendiéndose estimadas sin necesidad de resolución expresa, por el mero hecho de que se produzca el cargo en cuenta del primer plazo del fraccionamiento en las fechas indicadas a tal fin. La desestimación o inadmisión por causas distintas a las indicadas en las notas informativas, serán comunicadas en el plazo máximo de 6 meses desde la presentación de la solicitud.'"/>
	<xsl:variable name="lang.notas5" select="'5.- No se admitirán a trámite solicitudes referidas a liquidaciones cuya cuota íntegra sea inferior a 100,00€.'"/>
	<xsl:variable name="lang.notas6" select="'6.- Los obligados tributarios que quieran acogerse a este sistema de pago, no podrán figurar como deudores a la hacienda local, en la base de datos del Serivico de Gestión Tributaria, Inspección y Recaudación de la Diputación. En el supuesto de que exisitieran deudas pendientes de pago, se comunicarán al solicitante para que en el plazo máximo de 20 días proceda a regularizar la situación, procediendo a la inadmismión de la solicitud en caso de que no se llevare a cabo la misma en el plazo indicado.'"/>
	<xsl:variable name="lang.notas7" select="'7.- Aquellos contribuyentes que opten por este sistema de pago, no será necesario que vuelvan a solicitarlo nuevamente en años sucesivos, entendiéndose prorrogada la solicitud para devengos posteriores, salvo que insten la anulación de la misma en el plazo habilitado para la presentación.'"/>

	<xsl:variable name="lang.notas8" select="'REPRESENTANTE.- En caso de que la solicitud se realice por el representante legal o voluntario del obligado, deberá acreditarse dicha representación.'"/>
	
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero (*.zip), pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.documento" select="'Conjunto de anexos de la solicitud'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>


	<xsl:variable name="lang.info_legal" select="'Los datos personales, identificativos y de contacto, aportados mediante esta comunicación se entienden facilitados voluntariamente, y serán incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del ámbito de las competencias de esta Administración Pública así como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telemáticos y enviarle invitaciones para eventos y felicitaciones en fechas señaladas. Entenderemos que presta su consentimiento tácito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podrá ejercer sus derechos de acceso, rectificación, cancelación y oposición ante el Responsable del Fichero, la Diputación Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - España, siempre acreditando conforme a Derecho su identidad en la comunicación. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigiéndose a la dirección citada ut supra o bien al correo electrónico lopd@dipucr.es o bien por teléfono al número gratuito 900 714 080.'"/>

		
	<xsl:template match="/" xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(11);

			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');
			validar[2] = new Array('nif', '<xsl:value-of select="$lang.nif"/>');
			validar[3] = new Array('nombre','<xsl:value-of select="$lang.nombre"/>');
			validar[4] = new Array('calle','<xsl:value-of select="$lang.calle"/>');
			validar[5] = new Array('c_postal','<xsl:value-of select="$lang.c_postal"/>');
			validar[6] = new Array('mun_1','<xsl:value-of select="$lang.municipios"/>');
			validar[7] = new Array('ref1_1','<xsl:value-of select="$lang.referencia"/>');
			validar[8] = new Array('ref2_1','<xsl:value-of select="$lang.referencia"/>');
			validar[9] = new Array('ref3_1','<xsl:value-of select="$lang.referencia"/>');
			validar[10] = new Array('ref4_1','<xsl:value-of select="$lang.referencia"/>');
			validar[11] = new Array('iban','<xsl:value-of select="$lang.iban"/>');

			
			//Array con los datos especificos del formulario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(107);
			
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
			especificos[14] = new Array('ref1_1','ref1_1');
			especificos[15] = new Array('ref2_1','ref2_1');
			especificos[16] = new Array('ref3_1','ref3_1');
			especificos[17] = new Array('ref4_1','ref4_1');
			especificos[18] = new Array('ref1_2','ref1_2');
			especificos[19] = new Array('ref2_2','ref2_2');

			especificos[20] = new Array('ref3_2','ref3_2');
			especificos[21] = new Array('ref4_2','ref4_2');
			especificos[22] = new Array('ref1_3','ref1_3');
			especificos[23] = new Array('ref2_3','ref2_3');
			especificos[24] = new Array('ref3_3','ref3_3');
			especificos[25] = new Array('ref4_3','ref4_3');
			especificos[26] = new Array('ref1_4','ref1_4');
			especificos[27] = new Array('ref2_4','ref2_4');
			especificos[28] = new Array('ref3_4','ref3_4');
			especificos[29] = new Array('ref4_4','ref4_4');
			especificos[30] = new Array('ref1_5','ref1_5');
			especificos[31] = new Array('ref2_5','ref2_5');
			especificos[32] = new Array('ref3_5','ref3_5');
			especificos[33] = new Array('ref4_5','ref4_5');
			especificos[34] = new Array('ref1_6','ref1_6');
			especificos[35] = new Array('ref2_6','ref2_6');
			especificos[36] = new Array('ref3_6','ref3_6');
			especificos[37] = new Array('ref4_6','ref4_6');
			especificos[38] = new Array('ref1_7','ref1_7');
			especificos[39] = new Array('ref2_7','ref2_7');

			especificos[40] = new Array('ref3_7','ref3_7');
			especificos[41] = new Array('ref4_7','ref4_7');
			especificos[42] = new Array('ref1_8','ref1_8');
			especificos[43] = new Array('ref2_8','ref2_8');
			especificos[44] = new Array('ref3_8','ref3_8');
			especificos[45] = new Array('ref4_8','ref4_8');
			especificos[46] = new Array('ref1_9','ref1_9');
			especificos[47] = new Array('ref2_9','ref2_9');
			especificos[48] = new Array('ref3_9','ref3_9');
			especificos[49] = new Array('ref4_9','ref4_9');
			especificos[50] = new Array('ref1_10','ref1_10');
			especificos[51] = new Array('ref2_10','ref2_10');
			especificos[52] = new Array('ref3_10','ref3_10');
			especificos[53] = new Array('ref4_10','ref4_10');
			especificos[54] = new Array('ref1_11','ref1_11');
			especificos[55] = new Array('ref2_11','ref2_11');
			especificos[56] = new Array('ref3_11','ref3_11');
			especificos[57] = new Array('ref4_11','ref4_11');
			especificos[58] = new Array('ref1_12','ref1_12');
			especificos[59] = new Array('ref2_12','ref2_12');

			especificos[60] = new Array('ref3_12','ref3_12');
			especificos[61] = new Array('ref4_12','ref4_12');
			especificos[62] = new Array('ref1_13','ref1_13');
			especificos[63] = new Array('ref2_13','ref2_13');
			especificos[64] = new Array('ref3_13','ref3_13');
			especificos[65] = new Array('ref4_13','ref4_13');
			especificos[66] = new Array('ref1_14','ref1_14');
			especificos[67] = new Array('ref2_14','ref2_14');
			especificos[68] = new Array('ref3_14','ref3_14');
			especificos[69] = new Array('ref4_14','ref4_14');
			especificos[70] = new Array('ref1_15','ref1_15');
			especificos[71] = new Array('ref2_15','ref2_15');
			especificos[72] = new Array('ref3_15','ref3_15');
			especificos[73] = new Array('ref4_15','ref4_15');
			especificos[74] = new Array('ref1_16','ref1_16');
			especificos[75] = new Array('ref2_16','ref2_16');
			especificos[76] = new Array('ref3_16','ref3_16');
			especificos[77] = new Array('ref4_16','ref4_16');
			especificos[78] = new Array('ref1_17','ref1_17');
			especificos[79] = new Array('ref2_17','ref2_17');

			especificos[80] = new Array('ref3_17','ref3_17');
			especificos[81] = new Array('ref4_17','ref4_17');
			especificos[82] = new Array('ref1_18','ref1_18');
			especificos[83] = new Array('ref2_18','ref2_18');
			especificos[84] = new Array('ref3_18','ref3_18');
			especificos[85] = new Array('ref4_18','ref4_18');
			especificos[86] = new Array('ref1_19','ref1_19');
			especificos[87] = new Array('ref2_19','ref2_19');
			especificos[88] = new Array('ref3_19','ref3_19');
			especificos[89] = new Array('ref4_19','ref4_19');
			especificos[90] = new Array('ref1_20','ref1_20');
			especificos[91] = new Array('ref2_20','ref2_20');
			especificos[92] = new Array('ref3_20','ref3_20');
			especificos[93] = new Array('ref4_20','ref4_20');			
			especificos[94] = new Array('ccc1','ccc1');
			especificos[95] = new Array('ccc2','ccc2');
			especificos[96] = new Array('ccc3','ccc3');
			especificos[97] = new Array('ccc4','ccc4');
			especificos[98] = new Array('rciudad','rciudad');
			especificos[99] = new Array('rregion','rregion');

			especificos[100] = new Array('rc_postal','rc_postal');
			especificos[101] = new Array('nombresolihidden','nombresolihidden');
			especificos[102] = new Array('nifsolihidden','nifsolihidden');
			especificos[103] = new Array('titular','titular');
			especificos[104] = new Array('nifTitular','nifTitular');
			especificos[105] = new Array('telefonoTitular','telefonoTitular');
			especificos[106] = new Array('iban','iban');
			
			<xsl:call-template name="ADD_ROW2" />
			
			<xsl:call-template name="VALIDACIONES" />
			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			
			function verificacionesEspecificas() {	
				var f = document.forms[0];

				if(!validaNIFObligado()) return false;
				if(!validaNIFRepresentante()) return false;
				document.getElementById('nombresolihidden').value = document.getElementById('nombreSolicitante').value;
				document.getElementById('nifsolihidden').value = document.getElementById('documentoIdentidad').value;
				
				if(document.getElementById('mun_1').value=='000'){
					alert('Municipio obligatorio');
					document.getElementById('mun_1').focus();
					return false;
				}

				var i = 1;
				while(i!=21){
					var ref1 = document.getElementById('ref1_'+i);
					if(ref1.value != null){
						if(ref1.value!=''){
							var ref2 = document.getElementById('ref2_'+i);
							var ref3 = document.getElementById('ref3_'+i);
							var ref4 = document.getElementById('ref4_'+i);
							if((ref1.value == null || ref1.value=='') || (ref2.value == null || ref2.value=='') || (ref3.value == null || ref3.value=='')||(ref4.value == null || ref4.value=='')){
								alert('Es obligatorio rellenar todos los campos de la referencia catastral');
								ref1.focus();
								return false;								
							}
						}
					}
					i++;
				}
				
				if(!validaCC()) return false;

				if(!validarCuenta(f)) return false;

				if(document.getElementById('iban').value == ''){
					alert('Debe rellenar el campo IBAN');
					document.getElementById('iban').focus();
					return false;
				}

				if(document.getElementById('iban').value.length != 4){
					alert('El campo IBAN debe contener 4 caracteres');
					document.getElementById('iban').focus();
					return false;
				}
				document.getElementById('iban').value = document.getElementById('iban').value.toUpperCase();
				var iban = String(document.getElementById('iban').value) + String(document.getElementById('ccc1').value) + String(document.getElementById('ccc2').value) + String(document.getElementById('ccc3').value) + String(document.getElementById('ccc4').value);

				if(!validarIBAN(iban)){
					alert('El campo IBAN es incorrecto.');
					document.getElementById('iban').focus();
					return false;
				}
				return true;
			}			
			
			function getDatosObligado(nif){
				window.open('tramites/000/REC_FRAC_IBI/buscaObligado.jsp?valor='+nif+';000','','width=3,height=3');
			}
			function getDatosRepresentante(nif){
				window.open('tramites/000/REC_FRAC_IBI/buscaRepresentante.jsp?valor='+nif+';000','','width=3,height=3');
			}			
		</script>
		
		<h1><xsl:value-of select="$lang.titulo"/></h1>
		<xsl:call-template name="BOTON_LIMPIAR_FORMULARIO" />
   		<br/>
		<div class="submenu">
			<xsl:variable name="lang.datosPresentador" select="'Datos del presentador'"/>
   			<h1><xsl:value-of select="$lang.datosPresentador"/></h1>
   		</div>

		<!--  <xsl:call-template name="DATOS_SOLICITANTE" />-->
		<xsl:call-template name="DATOS_SOLICITANTE_EDITABLE" />
		<br/>
		<xsl:call-template name="DATOS_OBLIGADO" />
		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>				

   		<div class="cuadro" style="">
			<div style="margin-bottom:10px;color:#006699;">
				<b>		
					<label class="gr">
						<xsl:attribute name="style">width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.municipios"/>
					</label>
				</b>
			</div>
			<div class="col">
				<select class="gr">
					<xsl:attribute name="style">border:none; width:350px;color:#006699;margin-bottom:10px;</xsl:attribute>
					<xsl:attribute name="name">mun_1</xsl:attribute>
					<xsl:attribute name="id">mun_1</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/mun_1"/></xsl:attribute>
					<xsl:call-template name="OPTIONS_MUNICIPIOS_FRAC_IBI" />
				</select>
			</div>
			<div style="margin-top:20px;margin-bottom:10px;color:#006699;">
				<b>		
					<label class="gr">
						<xsl:attribute name="style">width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.textoInmuebles"/>
					</label>
				</b>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:350px;margin-bottom:10px;</xsl:attribute>
					<xsl:value-of select="$lang.notasInmuebles"/>
				</label>
			
				<table style="font-size:10px; width:90%; table-layout: fixed;" cellspacing="0" border="1">
					<tr>
						<td style="width:170px;background-color:#dee1e8"><xsl:value-of select="$lang.referencia"/></td>
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
		
				<div style="margin:5px 0 10px 10px;">
              			<a id="addRow_link" style="cursor:pointer" onclick="addRow()">[Añadir fila]</a><br/>
				</div>
			</div>
			<div style="margin-top:10px;margin-bottom:10px;color:#006699;">
				<b>		
					<label class="gr">
						<xsl:attribute name="style">width:150px;</xsl:attribute>
						<xsl:value-of select="$lang.textoIban"/>
					</label>
				</b>
			</div>
			<xsl:call-template name="CUENTA_CORRIENTE_IBAN" />
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
   			<label class="gr">
				<xsl:attribute name="style">width:250px;</xsl:attribute>
				<xsl:value-of select="$lang.documento"/>:
			</label>
			<input type="file">
				<xsl:attribute name="style">width:400px; </xsl:attribute>
				<xsl:attribute name="name">REC_FRAC_IBI_D1</xsl:attribute>
				<xsl:attribute name="id">REC_FRAC_IBI_D1</xsl:attribute>
				<xsl:attribute name="value"></xsl:attribute>
			</input>
			<input type="hidden">
				<xsl:attribute name="name">REC_FRAC_IBI_D1_Tipo</xsl:attribute>
				<xsl:attribute name="id">REC_FRAC_IBI_D1_Tipo</xsl:attribute>
				<xsl:attribute name="value">zip</xsl:attribute>
			</input>
   		</div>
		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.notas"/></h1>
   		</div>
		<div class="cuadro" style="color: grey; text-align:justify">
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas1"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas2"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas3"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas4"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas5"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas6"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas7"/></i></div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:650px; margin-bottom:10px;</xsl:attribute>
					<xsl:value-of select="$lang.info_legal"/>
				</label>
			</div>
			<div style="margin-bottom:10px;margin-top:10px;"><i><xsl:value-of select="$lang.notas8"/></i></div>
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
		<xsl:variable name="param_ref1">ref1_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref2">ref2_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref3">ref3_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_ref4">ref4_<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="row_style">		
			<xsl:choose>
				<xsl:when test="$row_id&gt;'5'">
					display:none;
				</xsl:when>
			</xsl:choose>
		</xsl:variable>

		<tr id="row_{$row_id}" style="{$row_style}">	        
			<td style="align:center;">
				<label style="margin:0px 20px 0px 30px;">
					INMUEBLE <xsl:value-of select="$row_id"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">border:'';margin:0;width:90px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param_ref1"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param_ref1"/></xsl:attribute>
					<xsl:attribute name="size">14</xsl:attribute>
					<xsl:attribute name="maxlength">14</xsl:attribute>
					<xsl:attribute name="onkeyup">if(this.value.length==14){document.getElementById('<xsl:value-of select="$param_ref2"/>').focus();}</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_ref1]"/></xsl:attribute>
				</input>·
				<input type="text">
					<xsl:attribute name="style">border:'';margin:0;width:32px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param_ref2"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param_ref2"/></xsl:attribute>
					<xsl:attribute name="size">4</xsl:attribute>
					<xsl:attribute name="maxlength">4</xsl:attribute>
					<xsl:attribute name="onkeyup">if(this.value.length==4){document.getElementById('<xsl:value-of select="$param_ref3"/>').focus();}</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_ref2]"/></xsl:attribute>
				</input>·
				<input type="text">
					<xsl:attribute name="style">border:'';margin:0;width:12px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param_ref3"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param_ref3"/></xsl:attribute>
					<xsl:attribute name="size">1</xsl:attribute>
					<xsl:attribute name="maxlength">1</xsl:attribute>
					<xsl:attribute name="onkeyup">if(this.value.length==1){document.getElementById('<xsl:value-of select="$param_ref4"/>').focus();}</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_ref3]"/></xsl:attribute>
				</input>·
				<input type="text">
					<xsl:attribute name="style">border:'';margin:0;width:12px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param_ref4"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param_ref4"/></xsl:attribute>
					<xsl:attribute name="size">1</xsl:attribute>
					<xsl:attribute name="maxlength">1</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_ref4]"/></xsl:attribute>
				</input>
			</td>	       
		</tr>
		<script>
			document.getElementById('motivo').value = '1';
		</script>
	</xsl:template>

	<xsl:template name="ADD_ROW2">
		var last_row = new Number(5);
		var max_num_rows = new Number(15);
		
		function addRow(){
			if (last_row &lt; max_num_rows){
				last_row = last_row + 1;
				var row = document.getElementById("row_"+last_row.toString());
				row.style.display = "";
				if (last_row == max_num_rows){
					var link = document.getElementById("addRow_link");
					link.style.display = "none";
				}
			}
		}
	</xsl:template>
</xsl:stylesheet>
