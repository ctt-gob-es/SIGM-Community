<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />

<xsl:include href="../templates_comunes.xsl" /><xsl:output encoding="ISO-8859-1" method="html"/>

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
	<xsl:variable name="lang.expone2" select="', que habiendo efectuado el pago de las liquidaciones que se indican por el año completo, y habiendo cesado en el ejercicio de la actividad, tal como se acredita con la copia adjunta de la baja presentada en A.E.A.T. (adjuntar copia baja).'"/>
	<xsl:variable name="lang.liquidaciones" select="'LIQUIDACIONES OBJETO DE DEVOLUCIÓN*'"/>
	<xsl:variable name="lang.municipio" select="'Nombre del municipio'"/>
	<xsl:variable name="lang.liquidacion" select="'Nº de liquidación'"/>
	<xsl:variable name="lang.solicita1" select="'SOLICITA'"/>
	<xsl:variable name="lang.solicita2" select="', que previa las comprobaciones oportunas, procedan a efectuar la devolución que por los trimestres no ejercidos a la siguiente entidad y cuenta corriente :'"/>
	<xsl:variable name="lang.cuantia" select="'Cuantía de la devolución que se solicita:*'"/>
	<xsl:variable name="lang.cuenta" select="'Código de Cuenta Corriente:*'"/>
	<xsl:variable name="lang.ccc" select="''"/>
	<xsl:variable name="lang.notas" select="'NOTAS INFORMATIVAS:'"/>
	<xsl:variable name="lang.notas1" select="'1.- El plazo máximo de resolución de su solicitud es de 6 meses, siendo los efectos del silencio administrativo NEGATIVOS. Asimismo, si transcurriere dicho plazo sin que se hubiere efectuado la devolución, se devengarán los intereses de demora correspondientes desde el día siguiente a la finalización de los 6 meses hasta la fecha en que se apruebe la devolución , caso de que ésta proceda.'"/>
	<xsl:variable name="lang.notas2" select="'2.- Efectuada la devolución en la cuenta indicada, se entenderá estimada la solicitud sin necesidad de comunicación expresa en este sentido.'"/>
	<xsl:variable name="lang.notas3" select="'3.- En el caso de que la solicitud venga referida a liquidaciones a nombre de otro obligado tributario, deberá acreditarse la representación legal o voluntaria ( ver modelo en la página) del mismo.'"/>
	
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero (*.zip), pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.documento" select="'Conjunto de anexos de la solicitud'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>

		
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

			validar[6] = new Array('cuantia','Cuantía solicitada');
			validar[7] = new Array('ccc1','Código de entidad');
			validar[8] = new Array('ccc2','Código de sucursal');
			validar[9] = new Array('ccc3','D.C.');
			validar[10] = new Array('ccc4','Cuenta corriente');
			

			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(59);
			
			especificos[0] = new Array('nif','nif');
			especificos[1] = new Array('nombre','nombre');
			especificos[2] = new Array('calle','calle');
			especificos[3] = new Array('numero','numero');
			especificos[4] = new Array('escalera','escalera');
			especificos[5] = new Array('planta_puerta','planta_puerta');
			especificos[6] = new Array('c_postal','c_postal');
			especificos[7] = new Array('movil','movil');
			especificos[8] = new Array('d_email','d_email');
			especificos[9] = new Array('repres_nif','repres_nif');
			especificos[10] = new Array('repres_nombre','repres_nombre');
			especificos[11] = new Array('repres_movil','repres_movil');
			especificos[12] = new Array('repres_d_email','repres_d_email');

			especificos[13] = new Array('mun_1','mun_1');
			especificos[14] = new Array('mun_2','mun_2');
			especificos[15] = new Array('mun_3','mun_3');
			especificos[16] = new Array('mun_4','mun_4');
			especificos[17] = new Array('mun_5','mun_5');
			especificos[18] = new Array('mun_6','mun_6');
			especificos[19] = new Array('mun_7','mun_7');
			especificos[20] = new Array('mun_8','mun_8');
			especificos[21] = new Array('mun_9','mun_9');
			especificos[22] = new Array('mun_10','mun_10');
			especificos[23] = new Array('mun_11','mun_11');
			especificos[24] = new Array('mun_12','mun_12');
			especificos[25] = new Array('mun_13','mun_13');
			especificos[26] = new Array('mun_14','mun_14');
			especificos[27] = new Array('mun_15','mun_15');
			especificos[28] = new Array('mun_16','mun_16');
			especificos[29] = new Array('mun_17','mun_17');
			especificos[30] = new Array('mun_18','mun_18');
			especificos[31] = new Array('mun_19','mun_19');
			especificos[32] = new Array('mun_20','mun_20');
			especificos[33] = new Array('liq_1','liq_1');
			especificos[34] = new Array('liq_2','liq_2');
			especificos[35] = new Array('liq_3','liq_3');
			especificos[36] = new Array('liq_4','liq_4');
			especificos[37] = new Array('liq_5','liq_5');
			especificos[38] = new Array('liq_6','liq_6');
			especificos[39] = new Array('liq_7','liq_7');
			especificos[40] = new Array('liq_8','liq_8');
			especificos[41] = new Array('liq_9','liq_9');
			especificos[42] = new Array('liq_10','liq_10');
			especificos[43] = new Array('liq_11','liq_11');
			especificos[44] = new Array('liq_12','liq_12');
			especificos[45] = new Array('liq_13','liq_13');
			especificos[46] = new Array('liq_14','liq_14');
			especificos[47] = new Array('liq_15','liq_15');
			especificos[48] = new Array('liq_16','liq_16');
			especificos[49] = new Array('liq_17','liq_17');
			especificos[50] = new Array('liq_18','liq_18');
			especificos[51] = new Array('liq_19','liq_19');
			especificos[52] = new Array('liq_20','liq_20');
			
			especificos[53] = new Array('cuantia','cuantia');
			especificos[54] = new Array('ccc1','ccc1');
			especificos[55] = new Array('ccc2','ccc2');
			especificos[56] = new Array('ccc3','ccc3');
			especificos[57] = new Array('ccc4','ccc4');
			especificos[58] = new Array('motivo','motivo');
			
			<xsl:call-template name="ADD_ROW" />
			
			<xsl:call-template name="VALIDACIONES" />
			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				var f = document.forms[0];
				return validarCuenta(f);
			}
		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
		<xsl:call-template name="DATOS_SOLICITANTE" />
		<xsl:call-template name="DATOS_OBLIGADO" />
		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
   		<div class="cuadro" style="">	
	   		<b><xsl:value-of select="$lang.expone1"/></b><xsl:value-of select="$lang.expone2"/><br/>
			<br/>

			<div style="margin-top:30px; width:100%; text-align:center;color:#006699;"><xsl:value-of select="$lang.liquidaciones"/></div>
			<table style="font-size:12px; margin:5 auto 20 auto;" cellspacing="0" border="1">
				<tr>
					<td style="width:25%;background-color:#dee1e8;text-align:center"><xsl:value-of select="$lang.municipio"/></td>
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
			
			<b><xsl:value-of select="$lang.solicita1"/></b><xsl:value-of select="$lang.solicita2"/><br/>
			<br/>
			<div style="margin-left:20px;">
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
	</xsl:template>
	
	<xsl:template name="FIELDS">
	    <xsl:param name="row_id" />
	    <xsl:variable name="param_mun">mun_<xsl:value-of select="$row_id"/></xsl:variable>
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
		        <select class="gr">
			        <xsl:attribute name="style">border:none;color:#006699;width:100%;</xsl:attribute>
			        <xsl:attribute name="name"><xsl:value-of select="$param_mun"/></xsl:attribute>
			        <xsl:attribute name="id"><xsl:value-of select="$param_mun"/></xsl:attribute>
			        <xsl:call-template name="OPTIONS_MUNICIPIOS" />
		        </select>
			</td>
			<td>
				<input type="text">
					<xsl:attribute name="style">width:100%;border:none;color:#006699;</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="$param_liq"/></xsl:attribute>
					<xsl:attribute name="id"><xsl:value-of select="$param_liq"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_liq]"/></xsl:attribute>
				</input>
			</td>
		</tr>
				
	</xsl:template>

</xsl:stylesheet>
