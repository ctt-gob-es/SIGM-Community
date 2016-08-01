<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />
<xsl:include href="../REC_COMUNES/template_IBAN.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:variable name="lang.titulo" select="'Formulario de solicitud de aplazamiento/fraccionamiento de pago de liquidaciones'"/>

	<xsl:variable name="lang.datosObligado" select="'Datos del contribuyente'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>

	<xsl:variable name="lang.id_nif" select="'Documento de identidad'"/>
	<xsl:variable name="lang.id_nombre" select="'Nombre'"/>

	<xsl:variable name="lang.direccion" select="'Domicilio a efectos de notificación'"/>
	<xsl:variable name="lang.localidad" select="'Localidad'"/>
	<xsl:variable name="lang.cp" select="'Código postal'"/>
	<xsl:variable name="lang.provincia" select="'Provincia'"/>
	<xsl:variable name="lang.telefono" select="'Teléfono'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>
	<xsl:variable name="lang.representacion" select="'Rellene los siguientes datos si actúa como representante'"/>			
	<xsl:variable name="lang.nif_repr" select="'NIF/CIF de la persona o entidad a quien representa'"/>			
	<xsl:variable name="lang.nombre_repr" select="'Nombre del representado'"/>			
	<xsl:variable name="lang.repres_nota" select="'NOTA: se encuentra disponible un formulario para la representación voluntaria.'"/>			

	<xsl:variable name="lang.nif" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre" select="'Apellidos y Nombre o Denominación Social'"/>
	<xsl:variable name="lang.calle" select="'Domicilio'"/>
	<xsl:variable name="lang.numero" select="'Número'"/>
	<xsl:variable name="lang.escalera" select="'Escalera'"/>
	<xsl:variable name="lang.planta_puerta" select="'Planta/Puerta'"/>
	<xsl:variable name="lang.ciudad" select="'Ciudad'"/>
	<xsl:variable name="lang.region" select="'Región / País'"/>
	<xsl:variable name="lang.c_postal" select="'Código Postal'"/>
	<xsl:variable name="lang.movil" select="'Número de teléfono móvil'"/>
	<xsl:variable name="lang.d_email" select="'Dirección de correo electrónico'"/>
	
	<xsl:variable name="lang.expone1" select="'EXPONE'"/>
	<xsl:variable name="lang.expone2" select="', que habiendo recibido notificación de la/s liquidación/es siguientes, correspondientes'"/>
	<xsl:variable name="lang.municipio_liq" select="'al municipio de:'"/>
	<xsl:variable name="lang.liquidaciones" select="'Nº de liquidaciones'"/>
	<xsl:variable name="lang.solicita1" select="'y dado que mi situación económico-financiera me impide, de forma transitoria, efectuar el pago en el plazo establecido, '"/>
	<xsl:variable name="lang.solicita2" select="'SOLICITA'"/>
	<xsl:variable name="lang.solicita3" select="': (marcar una de las dos opciones)'"/>
	<xsl:variable name="lang.aplazamiento" select="'El aplazamiento de pago de la citada/s liquidación/es'"/>
	<xsl:variable name="lang.fraccionamiento" select="'El fraccionamiento de pago de la citada/s liquidación/es'"/>
	<xsl:variable name="lang.plazos" select="'En los siguientes plazos:'"/>
	<xsl:variable name="lang.voluntaria1" select="'a) Para '"/>
	<xsl:variable name="lang.voluntaria2" select="'liquidaciones en voluntaria'"/>
	<xsl:variable name="lang.voluntaria3" select="' (marcar la opción):'"/>
	<xsl:variable name="lang.opcion-a1" select="'1.- Aplazamiento:'"/>
	<xsl:variable name="lang.opcion-a11" select="'En el siguiente periodo de pago voluntario'"/>
	<xsl:variable name="lang.opcion-a2" select="'2.- Fraccionamiento:'"/>
	<xsl:variable name="lang.opcion-a21" select="'En los dos siguientes periodos de pago voluntario'"/>
	<xsl:variable name="lang.opcion-a22" select="'En los tres siguientes periodos de pago voluntario'"/>

	<xsl:variable name="lang.notas" select="'NOTAS INFORMATIVAS:'"/>
	<xsl:variable name="lang.notas1" select="'1.- El plazo máximo de resolución de su solicitud es de 6 meses. Dentro de dicho plazo recibirá el acuerdo correspondiente o las liquidaciones  aplazadas o fraccionadas. Los efectos del silencio serán NEGATIVOS.'"/>
	<xsl:variable name="lang.notas2" select="'2.- En el caso de que la/s deuda/s superen los 30.000,00 €, deberán aportar garantía consistente en aval bancario, certificado de seguro de caución. Se podrá proponer la aportación de otras garantías, cuya suficiencia se apreciará discrecionalmente por esta Administración.'"/>
	<xsl:variable name="lang.notas3" select="'3.- Las cantidades aplazadas o fraccionadas devengará el interés de demora aplicable durante el plazo que dure el aplazamiento o fraccionamiento.'"/>
	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>
	 	
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero (*.zip), pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.documento" select="'Conjunto de anexos de la solicitud'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>

		
	<xsl:template match="/" xmlns:java="http://xml.apache.org/xslt/java">
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

			validar[6] = new Array('municipio_liq','municipio (liquidaciones)');
			validar[7] = new Array('n_liq_1','liquidaciones');
			

			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(49);
			
			especificos[0] = new Array('nif','nif');
			especificos[1] = new Array('nombre','nombre');
			especificos[2] = new Array('calle','calle');
			especificos[3] = new Array('c_postal','c_postal');
			especificos[4] = new Array('movil','movil');
			especificos[5] = new Array('d_email','d_email');
			especificos[6] = new Array('repres_nif','repres_nif');
			especificos[7] = new Array('repres_nombre','repres_nombre');
			especificos[8] = new Array('repres_movil','repres_movil');
			especificos[9] = new Array('repres_d_email','repres_d_email');

			especificos[10] = new Array('municipio_liq','municipio_liq');
			especificos[11] = new Array('n_liq_1','n_liq_1');
			especificos[12] = new Array('n_liq_2','n_liq_2');
			especificos[13] = new Array('n_liq_3','n_liq_3');
			especificos[14] = new Array('n_liq_4','n_liq_4');
			especificos[15] = new Array('n_liq_5','n_liq_5');
			especificos[16] = new Array('n_liq_6','n_liq_6');
			especificos[17] = new Array('n_liq_7','n_liq_7');
			especificos[18] = new Array('n_liq_8','n_liq_8');
			especificos[19] = new Array('n_liq_9','n_liq_9');
			especificos[20] = new Array('n_liq_10','n_liq_10');
			especificos[21] = new Array('n_liq_11','n_liq_11');
			especificos[22] = new Array('n_liq_12','n_liq_12');
			especificos[23] = new Array('n_liq_13','n_liq_13');
			especificos[24] = new Array('n_liq_14','n_liq_14');
			especificos[25] = new Array('n_liq_15','n_liq_15');
			especificos[26] = new Array('n_liq_16','n_liq_16');
			especificos[27] = new Array('n_liq_17','n_liq_17');
			especificos[28] = new Array('n_liq_18','n_liq_18');
			especificos[29] = new Array('n_liq_19','n_liq_19');
			especificos[30] = new Array('n_liq_20','n_liq_20');
			especificos[31] = new Array('n_liq_21','n_liq_21');
			especificos[32] = new Array('n_liq_22','n_liq_22');
			especificos[33] = new Array('n_liq_23','n_liq_23');
			especificos[34] = new Array('n_liq_24','n_liq_24');
		
			especificos[35] = new Array('aplazamiento','aplazamiento');
			especificos[36] = new Array('fraccionamiento','fraccionamiento');
			especificos[37] = new Array('opcion-a11','opcion-a11');
			especificos[38] = new Array('opcion-a21','opcion-a21');
			especificos[39] = new Array('opcion-a22','opcion-a22');
			especificos[40] = new Array('observaciones','observaciones');
			especificos[41] = new Array('ciudad','ciudad')
			especificos[42] = new Array('region','region');
			especificos[43] = new Array('nombresolihidden','nombresolihidden');
			especificos[44] = new Array('nifsolihidden','nifsolihidden');
			especificos[45] = new Array('rcalle','rcalle');
			especificos[46] = new Array('rc_postal','rc_postal');
			especificos[47] = new Array('rciudad','rciudad')
			especificos[48] = new Array('rregion','rregion');
			
			<xsl:call-template name="VALIDACIONES" />
			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				if(!validaNIFObligado()) return false;
				if(!validaNIFRepresentante()) return false;
				if(!validaNIFPresentador()) return false;
				else{
					document.getElementById('nombresolihidden').value = document.getElementById('nombreSolicitante').value;
					document.getElementById('nifsolihidden').value = document.getElementById('documentoIdentidad').value;
				}
				
				return validaLiq();
			}

			function validaLiq(){
				var i = 1;
				while(i!=24){
					if(document.getElementById('n_liq_'+i)!='undefined')
						if(document.getElementById('n_liq_'+i).value!=''){
						if(!validarLiquidacion(document.getElementById('n_liq_'+i))){
							alert('Nº de liquidación no válido: '+document.getElementById('n_liq_'+i).value);						
							document.getElementById('n_liq_'+i).focus();
							return false;
						}
					}
					i = i+1;
				}
				return true;
			}


			function getDatosObligado(nif){
				window.open('tramites/000/REC_02_VOL/buscaObligado.jsp?valor='+nif+';000','','width=3,height=3');
			}
			function getDatosRepresentante(nif){
				window.open('tramites/000/REC_02_VOL/buscaRepresentante.jsp?valor='+nif+';000','','width=3,height=3');
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
		<xsl:variable name="fileAyuntam" select="java:es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos.getDatosTablaValidacionConSustituto('REC_VLDTBL_MUNICIPIOS','000')"/>
			<xsl:variable name="b" select="document($fileAyuntam)"/>

   		<div class="cuadro" style="">	
	   		<b><xsl:value-of select="$lang.expone1"/></b><xsl:value-of select="$lang.expone2"/><br/><xsl:value-of select="$lang.municipio_liq"/>
				<select class="gr">
					<xsl:attribute name="style">margin:5px 20px; border:none; width:400px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">municipio_liq</xsl:attribute>
					<xsl:attribute name="id">municipio_liq</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/municipio1"/></xsl:attribute>
					<xsl:for-each select="$b/listado/dato">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="valor"/></xsl:attribute>
							<xsl:value-of select="sustituto"/>
						</option>
					</xsl:for-each>
				</select>	
			<br/>
			<div style="margin-left:235px;margin-top:10px;color:#006699;">
				<i><xsl:value-of select="$lang.liquidaciones"/></i>
			</div>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;margin-top:5px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_1</xsl:attribute>
				<xsl:attribute name="id">n_liq_1</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_1"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_2</xsl:attribute>
				<xsl:attribute name="id">n_liq_2</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_2"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_3</xsl:attribute>
				<xsl:attribute name="id">n_liq_3</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_3"/></xsl:attribute>
			</input>
			<br/>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_4</xsl:attribute>
				<xsl:attribute name="id">n_liq_4</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_4"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_5</xsl:attribute>
				<xsl:attribute name="id">n_liq_5</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_5"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_6</xsl:attribute>
				<xsl:attribute name="id">n_liq_6</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_6"/></xsl:attribute>
			</input>
			<br/>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_7</xsl:attribute>
				<xsl:attribute name="id">n_liq_7</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_7"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_8</xsl:attribute>
				<xsl:attribute name="id">n_liq_8</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_8"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_9</xsl:attribute>
				<xsl:attribute name="id">n_liq_9</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_9"/></xsl:attribute>
			</input>
			<br/>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_10</xsl:attribute>
				<xsl:attribute name="id">n_liq_10</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_10"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_11</xsl:attribute>
				<xsl:attribute name="id">n_liq_11</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_11"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_12</xsl:attribute>
				<xsl:attribute name="id">n_liq_12</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_12"/></xsl:attribute>
			</input>
			<br/>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_13</xsl:attribute>
				<xsl:attribute name="id">n_liq_13</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_13"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_14</xsl:attribute>
				<xsl:attribute name="id">n_liq_14</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_14"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_15</xsl:attribute>
				<xsl:attribute name="id">n_liq_15</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_15"/></xsl:attribute>
			</input>
			<br/>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_16</xsl:attribute>
				<xsl:attribute name="id">n_liq_16</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_16"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_17</xsl:attribute>
				<xsl:attribute name="id">n_liq_17</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_17"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_18</xsl:attribute>
				<xsl:attribute name="id">n_liq_18</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_18"/></xsl:attribute>
			</input>
			<br/>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_19</xsl:attribute>
				<xsl:attribute name="id">n_liq_19</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_19"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_20</xsl:attribute>
				<xsl:attribute name="id">n_liq_20</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_20"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_21</xsl:attribute>
				<xsl:attribute name="id">n_liq_21</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_21"/></xsl:attribute>
			</input>
			<br/>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_22</xsl:attribute>
				<xsl:attribute name="id">n_liq_22</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_22"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_23</xsl:attribute>
				<xsl:attribute name="id">n_liq_23</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_23"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_24</xsl:attribute>
				<xsl:attribute name="id">n_liq_24</xsl:attribute>
				<xsl:attribute name="maxlength">18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_24"/></xsl:attribute>
			</input>
			<br/>
			<br/>

			<xsl:value-of select="$lang.solicita1"/><b><xsl:value-of select="$lang.solicita2"/></b><xsl:value-of select="$lang.solicita3"/><br/>
			<br/>
			<div style="margin-bottom:5px;color:#006699;">
				<label for="aplazamiento" style="margin-left:20px;"><xsl:value-of select="$lang.aplazamiento"/></label>
				<input type="checkbox" id="aplazamiento" style="border:0px; width:20px;vertical-align:middle;" onclick="checkAplaz()" />
			</div>
			<div style="margin-bottom:20px;color:#006699;">
				<label for="fraccionamiento" style="margin-left:20px;"><xsl:value-of select="$lang.fraccionamiento"/></label>
				<input type="checkbox" id="fraccionamiento" style="border:0px; width:20px;vertical-align:middle;" onclick="checkFracc()" />
			</div>

			<div style="margin-bottom:20px;">
			<xsl:value-of select="$lang.plazos"/><br/>
			</div>

			<xsl:value-of select="$lang.voluntaria1"/><b><u><xsl:value-of select="$lang.voluntaria2"/></u></b><xsl:value-of select="$lang.voluntaria3"/><br/>
			<br/>

			<div style="margin-bottom:10px;margin-left:20px;"><xsl:value-of select="$lang.opcion-a1"/></div>
			<div style="margin-bottom:5px;color:#006699;">
				<label for="opcion-a11" style="margin-left:75px;"><xsl:value-of select="$lang.opcion-a11"/></label>
				<input type="checkbox" id="opcion-a11" disabled="disabled" style="border:0px; width:20px;vertical-align:middle;" onclick="a11Clicked()"/>
			</div>

			<div style="margin-bottom:10px;margin-left:20px;"><xsl:value-of select="$lang.opcion-a2"/></div>
			<div style="margin-bottom:5px;color:#006699;">
				<label for="opcion-a21" style="margin-left:75px;"><xsl:value-of select="$lang.opcion-a21"/></label>
				<input type="checkbox" id="opcion-a21" disabled="disabled" style="border:0px; width:20px;vertical-align:middle;" onclick="unCheckOthersFracc('opcion-a21')"/>
			</div>
			<div style="margin-bottom:5px;color:#006699;">
				<label for="opcion-a22" style="margin-left:75px;"><xsl:value-of select="$lang.opcion-a22"/></label>
				<input type="checkbox" id="opcion-a22" disabled="disabled" style="border:0px; width:20px;vertical-align:middle;" onclick="unCheckOthersFracc('opcion-a22')"/>
			</div>
			<!--<div style="margin-bottom:20px;margin-top:10px;">
				<xsl:value-of select="$lang.ejecutiva1"/><b><u><xsl:value-of select="$lang.ejecutiva2"/></u></b><xsl:value-of select="$lang.ejecutiva3"/><br/>
			</div>

	   		<div style="margin-left:20px;margin-bottom:10px;">
				<xsl:value-of select="$lang.opcion-b1"/>
			</div>
			<input type="text" disabled="disabled">
				<xsl:attribute name="style">width:250px;margin-left:75px;</xsl:attribute>
				<xsl:attribute name="name">opcion-b11</xsl:attribute>
				<xsl:attribute name="id">opcion-b11</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/opcion-b11"/></xsl:attribute>
				<xsl:attribute name="onkeypress">b11Changed()</xsl:attribute>
			</input>
			
			<div style="margin-bottom:10px;margin-left:20px;margin-top:10px;"><xsl:value-of select="$lang.opcion-b2"/></div>
			<div style="margin-bottom:5px;color:#006699;">
				<label for="opcion-b21" style="margin-left:75px;"><xsl:value-of select="$lang.opcion-b21"/></label>
				<input type="checkbox" id="opcion-b21" disabled="disabled" style="border:0px; width:20px;vertical-align:middle;" onclick="unCheckOthersFracc('opcion-b21')"/>
			</div>
			<div style="margin-bottom:5px;color:#006699;">
				<label for="opcion-b22" style="margin-left:75px;"><xsl:value-of select="$lang.opcion-b22"/></label>
				<input type="checkbox" id="opcion-b22" disabled="disabled" style="border:0px; width:20px;vertical-align:middle;" onclick="unCheckOthersFracc('opcion-b22')"/>
			</div>
			<div style="margin-bottom:5px;color:#006699;">
				<label for="opcion-b23" style="margin-left:75px;"><xsl:value-of select="$lang.opcion-b23"/></label>
				<input type="checkbox" id="opcion-b23" disabled="disabled" style="border:0px; width:20px;vertical-align:middle;" onclick="unCheckOthersFracc('opcion-b23')"/>
			</div>-->
			
			<label class="gr">
				<xsl:attribute name="style">width:100px;</xsl:attribute>
				<xsl:value-of select="$lang.observaciones"/>:
			</label>
			<input type="text">
				<xsl:attribute name="style">width:500px; </xsl:attribute>
				<xsl:attribute name="name">observaciones</xsl:attribute>
				<xsl:attribute name="id">observaciones</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/observaciones"/></xsl:attribute>
			</input>
			<div class="col">
				<br/>
			</div>

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
					<xsl:attribute name="name">REC-02-D1</xsl:attribute>
					<xsl:attribute name="id">REC-02-D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">REC-02-D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">REC-02-D1_Tipo</xsl:attribute>
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

		<script language='JavaScript' type='text/javascript'>
			function unCheck(field, causer) {
				if (causer.checked) {
					field.checked = false;
				}
			}
			
			function disableCheck(field) {
				field.checked = false;
				field.disabled = true;
			}
			
			function enableCheck(field) {
				field.disabled = false;
			}
			
			function disableOpcionesFracc() {
				disableCheck(document.getElementById("opcion-a21"));
				disableCheck(document.getElementById("opcion-a22"));
				//disableCheck(document.getElementById("opcion-b21"));
				//disableCheck(document.getElementById("opcion-b22"));
				//disableCheck(document.getElementById("opcion-b23"));
			}
			
			function enableOpcionesFracc() {
				enableCheck(document.getElementById("opcion-a21"));
				enableCheck(document.getElementById("opcion-a22"));
				//enableCheck(document.getElementById("opcion-b21"));
				//enableCheck(document.getElementById("opcion-b22"));
				//enableCheck(document.getElementById("opcion-b23"));
			}
			
			function disableOpcionesAplaz() {
				disableCheck(document.getElementById("opcion-a11"));
				//document.getElementById("opcion-b11").disabled = true;
				//document.getElementById("opcion-b11").value = "";
			}
			
			function enableOpcionesAplaz() {
				enableCheck(document.getElementById("opcion-a11"));
				//document.getElementById("opcion-b11").disabled = false;
			}
			
			function checkFracc(){
				var causer = document.getElementById("fraccionamiento");
				unCheck(document.getElementById("aplazamiento"), causer);
				if (causer.checked) {
					enableOpcionesFracc();
					disableOpcionesAplaz();
				} else {
					disableOpcionesFracc();
					disableOpcionesAplaz();
				}
			}
			
			function checkAplaz(){
				var causer = document.getElementById("aplazamiento");
				unCheck(document.getElementById("fraccionamiento"), causer);
				if (causer.checked) {
					enableOpcionesAplaz();
					disableOpcionesFracc();
				} else {
					disableOpcionesFracc();
					disableOpcionesAplaz();
				}
			}
			
			function unCheckOthersFracc(field){
			    if (field != "opcion-a21") {
    			    var chk = document.getElementById("opcion-a21");
			        chk.checked = false;
			    }
			    if (field != "opcion-a22") {
    			    var chk = document.getElementById("opcion-a22");
			        chk.checked = false;
			    }
			    if (field != "opcion-b21") {
    			   // var chk = document.getElementById("opcion-b21");
			       // chk.checked = false;
			    }
			    if (field != "opcion-b22") {
    			   // var chk = document.getElementById("opcion-b22");
			      //  chk.checked = false;
			    }
			    if (field != "opcion-b23") {
    			   // var chk = document.getElementById("opcion-b23");
			       // chk.checked = false;
			    }
			}

			function a11Clicked() {
			   // document.getElementById("opcion-b11").value = "";
			}	
			
			function b11Changed() {
   			    var chk = document.getElementById("opcion-a11");
		        chk.checked = false;
			}

		</script>
	</xsl:template>
</xsl:stylesheet>
