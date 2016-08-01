<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />
<xsl:include href="../REC_COMUNES/template_IBAN.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'Formulario de solicitud de devolución de ingresos indebidos'"/>

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
	<xsl:variable name="lang.expone2" select="', que habiendo efectuado el pago de las liquidaciones que se indican correspondientes al municipio de:'"/>
	<xsl:variable name="lang.expone3" select="', por (seleccionar la causa):'"/>
	<xsl:variable name="lang.motivo1" select="'1 - Ingreso de la misma liquidación en dos o más ocasiones'"/>
	<xsl:variable name="lang.motivo2" select="'2 - Pago por importe superior al consignado en la liquidación'"/>
	<xsl:variable name="lang.motivo3" select="'3 - Embargo y pago en ventanilla'"/>
	<xsl:variable name="lang.motivo4" select="'4 - Por importe superior al resultar la base liquidable del IBI menor, según resolución de la Dirección General del Catastro'"/>
	<xsl:variable name="lang.motivo5" select="'5 - Pago de una liquidación posteriormente anulada y/o aprobada por importe inferior al ingresado'"/>
	<xsl:variable name="lang.motivo6" select="'6 - Pago de una liquidación declarada prescrita'"/>
	<xsl:variable name="lang.motivo7" select="'7 - Devolución de recargo ejecutivo o de apremio por falta de notificación'"/>
	<xsl:variable name="lang.notas_motivos1" select="'Opción 1: deberá aportarse copia de la liquidación ingresada en 1º lugar y original de las restantes.'"/>
	<xsl:variable name="lang.notas_motivos2" select="'Opción 3: deberá aportarse copia de la liquidación ingresada en ventanilla cuando se solicite la devolución del embargo u original en otro caso.'"/>
	<xsl:variable name="lang.notas_motivos3" select="'Opción 4: deberán aportar copia resolución en la que basan la petición.'"/>
	<xsl:variable name="lang.notas_motivos4" select="'Opción 5: deberá aportar copia de la resolución de anulación, o si ésta procediese de esta Administración indicar el nº de expediente:'"/>
	<xsl:variable name="lang.notas_motivos8" select="'Opción 8: indicar motivo: '"/>
	<xsl:variable name="lang.liquidaciones" select="'LIQUIDACIONES OBJETO DE DEVOLUCIÓN*'"/>
	<xsl:variable name="lang.municipio" select="'Nombre del municipio'"/>
	<xsl:variable name="lang.liquidacion" select="'Nº de liquidación'"/>
	<xsl:variable name="lang.solicita1" select="'SOLICITA'"/>
	<xsl:variable name="lang.solicita2" select="' que previa las comprobaciones oportunas, procedan a efectuar la devolución que proceda a la'"/>
	<xsl:variable name="lang.ccc" select="'siguiente entidad y cuenta corriente :'"/>
	<xsl:variable name="lang.cuantia" select="'Cuantía de la devolución que se solicita:*'"/>
	<xsl:variable name="lang.cuenta" select="'Código de Cuenta Corriente:*'"/>
	<xsl:variable name="lang.notas" select="'NOTAS INFORMATIVAS:'"/>
	<xsl:variable name="lang.notas1" select="'1.- El plazo máximo de resolución de su solicitud es de 6 meses, siendo los efectos del silencio administrativo NEGATIVOS.'"/>
	<xsl:variable name="lang.notas2" select="'2.- Efectuada la devolución en la cuenta indicada, se entenderá estimada la solicitud sin necesidad de comunicación expresa en este sentido.'"/>
	<xsl:variable name="lang.notas3" select="'3.- En el caso de que la solicitud venga referida a liquidaciones a nombre de otro obligado tributario, deberá acreditarse la representación legal o voluntaria ( ver modelo en la página) del mismo.'"/>
	<xsl:variable name="lang.notas4" select="'4.- Deberá aportarse original de las liquidaciones.'"/>
	
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero (*.zip), pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.documento" select="'Conjunto de anexos de la solicitud'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>

	<xsl:variable name="lang.info_legal" select="'Los datos personales, identificativos y de contacto, aportados mediante esta comunicación se entienden facilitados voluntariamente, y serán incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del ámbito de las competencias de esta Administración Pública así como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telemáticos y enviarle invitaciones para eventos y felicitaciones en fechas señaladas. Entenderemos que presta su consentimiento tácito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podrá ejercer sus derechos de acceso, rectificación, cancelación y oposición ante el Responsable del Fichero, la Diputación Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - España, siempre acreditando conforme a Derecho su identidad en la comunicación. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigiéndose a la dirección citada ut supra o bien al correo electrónico lopd@dipucr.es o bien por teléfono al número gratuito 900 714 080.'"/>

	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>
		
	<xsl:template match="/" xmlns:java="http://xml.apache.org/xslt/java">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			//----------------------------
			var validar = new Array(12);

			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.id_nif"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.id_nombre"/>');

			validar[2] = new Array('nif', '<xsl:value-of select="$lang.nif"/>');
			validar[3] = new Array('nombre','<xsl:value-of select="$lang.nombre"/>');
			validar[4] = new Array('calle','<xsl:value-of select="$lang.calle"/>');
			validar[5] = new Array('c_postal','<xsl:value-of select="$lang.c_postal"/>');

			validar[6] = new Array('motivo','Motivo');
			validar[7] = new Array('iban','IBAN');
			validar[8] = new Array('ccc1','Código de entidad');
			validar[9] = new Array('ccc2','Código de sucursal');
			validar[10] = new Array('ccc3','D.C.');
			validar[11] = new Array('ccc4','Cuenta corriente');
			

			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(52);
			
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
			
			especificos[34] = new Array('motivo','motivo');
			especificos[35] = new Array('expediente','expediente');
			especificos[36] = new Array('cuantia','cuantia');
			especificos[37] = new Array('ccc1','ccc1');
			especificos[38] = new Array('ccc2','ccc2');
			especificos[39] = new Array('ccc3','ccc3');
			especificos[40] = new Array('ccc4','ccc4');

			especificos[41] = new Array('rciudad','rciudad');
			especificos[42] = new Array('rregion','rregion');
			especificos[43] = new Array('rc_postal','rc_postal');

			especificos[44] = new Array('nombresolihidden','nombresolihidden');
			especificos[45] = new Array('nifsolihidden','nifsolihidden');

			especificos[46] = new Array('titular','titular');
			especificos[47] = new Array('nifTitular','nifTitular');
			especificos[48] = new Array('telefonoTitular','telefonoTitular');

			especificos[49] = new Array('otro_motivo','otro_motivo');
			especificos[50] = new Array('observaciones','observaciones');
			especificos[51] = new Array('iban','iban');
			
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
				window.open('tramites/000/REC_01/buscaObligado.jsp?valor='+nif+';000','','width=3,height=3');
			}
			function getDatosRepresentante(nif){
				window.open('tramites/000/REC_01/buscaRepresentante.jsp?valor='+nif+';000','','width=3,height=3');
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
			<br/><xsl:value-of select="$lang.expone3"/><br/>

			<xsl:variable name="fileTributos" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosTablaValidacionConSustituto('REC_VLDTBL_MOT_DEVOLUCION','000')"/>
			<xsl:variable name="c" select="document($fileTributos)"/>

			
				<select class="gr">
					<xsl:attribute name="style">border:none; width:550px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">motivo</xsl:attribute>
					<xsl:attribute name="id">motivo</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/motivo"/></xsl:attribute>
					<xsl:for-each select="$c/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>

			<br/>
			<div style="margin-left:10px;margin-top:10px;">
				<i><xsl:value-of select="$lang.notas_motivos1"/></i><br/>
				<i><xsl:value-of select="$lang.notas_motivos2"/></i><br/>
				<i><xsl:value-of select="$lang.notas_motivos3"/></i><br/>
				<i><xsl:value-of select="$lang.notas_motivos4"/></i>
				<input type="text">
					<xsl:attribute name="style">margin-left:10px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">expediente</xsl:attribute>
					<xsl:attribute name="id">expediente</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/expediente"/></xsl:attribute>
				</input><br/>			
				<i><xsl:value-of select="$lang.notas_motivos8"/></i><br/>
				<textarea>
					<xsl:attribute name="style">margin-left:75px;margin-top:5px;width:400px;font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">otro_motivo</xsl:attribute>
					<xsl:attribute name="id">otro_motivo</xsl:attribute>
					<xsl:attribute name="rows">3</xsl:attribute>
					<xsl:attribute name="onkeypress">if(this.value.length&gt;255){ alert('Tamaño máximo de campo alcanzado'); this.value = this.value.substr(0,255);}</xsl:attribute>
					<xsl:attribute name="onkeyup">if(this.value.length&gt;255){ alert('Tamaño máximo de campo alcanzado'); this.value = this.value.substr(0,255);}</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/otro_motivo"/>
				</textarea>
			</div>

			<div style="margin-top:30px; width:100%; text-align:center;color:#006699;"><xsl:value-of select="$lang.liquidaciones"/></div>
			<table style="font-size:12px; margin:5 auto 20 auto;" cellspacing="0" border="1">
				<tr>
					<td style="width:25%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.liquidacion"/></td>
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
	
			<xsl:call-template name="SOLICITA4" />
	   		<br/>
			
			<div style="margin-bottom:10px;margin-top:30px;"><i><xsl:value-of select="$lang.notas"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas1"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas2"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas3"/></i></div>
			<div style="margin-bottom:10px;"><i><xsl:value-of select="$lang.notas4"/></i></div>
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
		<div class="cuadro" style="color: grey; text-align:justify">
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style"> width:650px</xsl:attribute>
					<xsl:value-of select="$lang.info_legal"/>
				</label>
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
					<xsl:attribute name="maxlength">18</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_liq]"/></xsl:attribute>
				</input>
			</td>
		</tr>
				
	</xsl:template>

</xsl:stylesheet>

