<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
	<xsl:variable name="lang.calle" select="'Calle'"/>
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
	//<xsl:variable name="lang.ejecutiva1" select="'a) Para '"/>
	//<xsl:variable name="lang.ejecutiva2" select="'liquidaciones en ejecutiva'"/>
	//<xsl:variable name="lang.ejecutiva3" select="' (marcar la opción):'"/>
	//<xsl:variable name="lang.opcion-b1" select="'1.- Para aplazamientos (máximo 12 meses desde la solicitud) indicar la fecha límite de pago:'"/>
	//<xsl:variable name="lang.opcion-b2" select="'2.- Para fraccionamientos (máximo 18 meses desde la solicitud) indicar una de las opciones de pago:'"/>
	//<xsl:variable name="lang.opcion-b21" select="'Pago mensual (primeros 5 días del mes)'"/>
	//<xsl:variable name="lang.opcion-b22" select="'Pago trimestral (primeros 5 días del trimestre natural)'"/>
	//<xsl:variable name="lang.opcion-b23" select="'Pago semestral (primeros 5 días del semestre natural)'"/>
	<xsl:variable name="lang.notas" select="'NOTAS INFORMATIVAS:'"/>
	<xsl:variable name="lang.notas1" select="'1.- El plazo máximo de resolución de su solicitud es de 6 meses. Dentro de dicho plazo recibirá el acuerdo correspondiente o las liquidaciones  aplazadas o fraccionadas. Los efectos del silencio serán NEGATIVOS.'"/>
	<xsl:variable name="lang.notas2" select="'2.- En el caso de que la/s deuda/s superen los 18.000,00 €, deberán aportar garantía consistente en aval bancario, certificado de seguro de caución. Se podrá proponer la aportación de otras garantías, cuya suficiencia se apreciará discrecionalmente por esta Administración.'"/>
	<xsl:variable name="lang.notas3" select="'3.- Las cantidades aplazadas o fraccionadas devengará el interés de demora aplicable durante el plazo que dure el aplazamiento o fraccionamiento.'"/>
	<xsl:variable name="lang.observaciones" select="'Observaciones'"/>
	 	
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero (*.zip), pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.documento" select="'Conjunto de anexos de la solicitud'"/>
	<xsl:variable name="lang.documentoTipo" select="'Archivo ZIP'"/>

		
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

			validar[6] = new Array('municipio_liq','municipio (liquidaciones)');
			validar[7] = new Array('n_liq_1','liquidaciones');
			

			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			//----------------------------------------------
			var especificos = new Array(55);
			
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

			especificos[13] = new Array('municipio_liq','municipio_liq');
			especificos[14] = new Array('n_liq_1','n_liq_1');
			especificos[15] = new Array('n_liq_2','n_liq_2');
			especificos[16] = new Array('n_liq_3','n_liq_3');
			especificos[17] = new Array('n_liq_4','n_liq_4');
			especificos[18] = new Array('n_liq_5','n_liq_5');
			especificos[19] = new Array('n_liq_6','n_liq_6');
			especificos[20] = new Array('n_liq_7','n_liq_7');
			especificos[21] = new Array('n_liq_8','n_liq_8');
			especificos[22] = new Array('n_liq_9','n_liq_9');
			especificos[23] = new Array('n_liq_10','n_liq_10');
			especificos[24] = new Array('n_liq_11','n_liq_11');
			especificos[25] = new Array('n_liq_12','n_liq_12');
			especificos[26] = new Array('n_liq_13','n_liq_13');
			especificos[27] = new Array('n_liq_14','n_liq_14');
			especificos[28] = new Array('n_liq_15','n_liq_15');
			especificos[29] = new Array('n_liq_16','n_liq_16');
			especificos[30] = new Array('n_liq_17','n_liq_17');
			especificos[31] = new Array('n_liq_18','n_liq_18');
			especificos[32] = new Array('n_liq_19','n_liq_19');
			especificos[33] = new Array('n_liq_20','n_liq_20');
			especificos[34] = new Array('n_liq_21','n_liq_21');
			especificos[35] = new Array('n_liq_22','n_liq_22');
			especificos[36] = new Array('n_liq_23','n_liq_23');
			especificos[37] = new Array('n_liq_24','n_liq_24');
			
			especificos[38] = new Array('aplazamiento','aplazamiento');
			especificos[39] = new Array('fraccionamiento','fraccionamiento');
			especificos[40] = new Array('opcion-a11','opcion-a11');
			especificos[41] = new Array('opcion-a21','opcion-a21');
			especificos[42] = new Array('opcion-a22','opcion-a22');
			especificos[43] = new Array('observaciones','observaciones');
			especificos[44] = new Array('ciudad','ciudad')
			especificos[45] = new Array('region','region');
			especificos[46] = new Array('nombresolihidden','nombresolihidden');
			especificos[47] = new Array('nifsolihidden','nifsolihidden');
			//especificos[43] = new Array('opcion-b11','opcion-b11');
			//especificos[44] = new Array('opcion-b21','opcion-b21');
			//especificos[45] = new Array('opcion-b22','opcion-b22');
			//especificos[46] = new Array('opcion-b23','opcion-b23');
			especificos[48] = new Array('rcalle','rcalle');
			especificos[49] = new Array('rnumero','rnumero');
			especificos[50] = new Array('rescalera','rescalera');
			especificos[51] = new Array('rplanta_puerta','rplanta_puerta');
			especificos[52] = new Array('rc_postal','rc_postal');
			especificos[53] = new Array('rciudad','rciudad')
			especificos[54] = new Array('rregion','rregion');
			
			
			//Array de validaciones
			//----------------------------------------------
			var validarNumero;
			function verificacionesEspecificas() {
				document.getElementById('nombresolihidden').value = document.getElementById('nombreSolicitante').value;
				document.getElementById('nifsolihidden').value = document.getElementById('documentoIdentidad').value;
				return true;
			}
		</script>
		
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
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
					<xsl:value-of select="$lang.numero"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:35px; </xsl:attribute>
					<xsl:attribute name="name">numero</xsl:attribute>
					<xsl:attribute name="id">numero</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/numero"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.escalera"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:75px; </xsl:attribute>
					<xsl:attribute name="name">escalera</xsl:attribute>
					<xsl:attribute name="id">escalera</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/escalera"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.planta_puerta"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:45px; </xsl:attribute>
					<xsl:attribute name="name">planta_puerta</xsl:attribute>
					<xsl:attribute name="id">planta_puerta</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/planta_puerta"/></xsl:attribute>
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
					<xsl:value-of select="$lang.numero"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:35px; </xsl:attribute>
					<xsl:attribute name="name">rnumero</xsl:attribute>
					<xsl:attribute name="id">rnumero</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/rnumero"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.escalera"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:75px; </xsl:attribute>
					<xsl:attribute name="name">rescalera</xsl:attribute>
					<xsl:attribute name="id">rescalera</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/rescalera"/></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.planta_puerta"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">width:45px; </xsl:attribute>
					<xsl:attribute name="name">rplanta_puerta</xsl:attribute>
					<xsl:attribute name="id">rplanta_puerta</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/rplanta_puerta"/></xsl:attribute>
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
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">margin-left:150px;width:490px;</xsl:attribute>
					<i><xsl:value-of select="$lang.repres_nota"/></i>
				</label>
			</div>
		</div>
		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosSolicitud"/></h1>
   		</div>
   		<div class="cuadro" style="">	
	   		<b><xsl:value-of select="$lang.expone1"/></b><xsl:value-of select="$lang.expone2"/><br/><xsl:value-of select="$lang.municipio_liq"/>
				<select class="gr">
					<xsl:attribute name="style">margin:5px 20px; border:none; width:400px;color:#006699;</xsl:attribute>
					<xsl:attribute name="name">municipio_liq</xsl:attribute>
					<xsl:attribute name="id">municipio_liq</xsl:attribute>
					<option selected="selected" value=""></option>
					<option value="128889">ABENOJAR</option>
					<option value="128924">AGUDO</option>
					<option value="128911">ALAMILLO</option>
					<option value="128956">ALBALADEJO</option>
					<option value="128887">ALCAZAR DE SAN JUAN</option>
					<option value="128957">ALCOBA DE LOS MONTES</option>
					<option value="128923">ALCOLEA DE CVA</option>
					<option value="128927">ALCUBILLAS</option>
					<option value="125219">ALDEA DEL REY</option>
					<option value="128930">ALHAMBRA</option>
					<option value="125233">ALMADEN</option>
					<option value="125253">ALMADENEJOS</option>
					<option value="128925">ALMAGRO</option>
					<option value="128912">ALMEDINA</option>
					<option value="128946">ALMODOVAR DEL CAMPO</option>
					<option value="128934">ALMURADIEL</option>
					<option value="129066">ANCHURAS</option>
					<option value="128890">ARENALES DE SAN GREGORIO</option>
					<option value="128936">ARENAS DE SAN JUAN</option>
					<option value="125383">ARGAMASILLA DE ALBA</option>
					<option value="128938">ARGAMASILLA DE CVA</option>
					<option value="128940">ARROBA DE LOS MONTES</option>
					<option value="128942">BALLESTEROS DE CVA</option>
					<option value="125495">BOLAÑOS DE CVA</option>
					<option value="128948">BRAZATORTAS</option>
					<option value="128935">CABEZARADOS</option>
					<option value="128937">CABEZARRUBIAS DEL PUERTO</option>
					<option value="128939">CALZADA DE CVA</option>
					<option value="128941">CAMPO DE CRIPTANA</option>
					<option value="128949">CAÑADA DE CVA</option>
					<option value="128943">CARACUEL DE CVA</option>
					<option value="128945">CARRION DE CVA</option>
					<option value="125702">CARRIZOSA</option>
					<option value="128947">CASTELLAR DE SANTIAGO</option>
					<option value="128950">CHILLON</option>
					<option value="140839">CINCO CASAS</option>
					<option value="128879">CIUDAD REAL</option>
					<option value="128951">CORRAL DE CVA</option>
					<option value="128953">COZAR</option>
					<option value="125855">DAIMIEL</option>
					<option value="999">DIPUCTACIÓN DE CIUDAD REAL</option>
					<option value="129130">EL HOYO DE MESTANZA</option>
					<option value="128929">EL ROBLEDO</option>
					<option value="129138">EL TORNO</option>
					<option value="125889">FERNAN CABALLERO</option>
					<option value="128954">FONTANAREJO</option>
					<option value="128959">FUENCALIENTE</option>
					<option value="125901">FUENLLANA</option>
					<option value="128913">FUENTE EL FRESNO</option>
					<option value="128961">GRANATULA DE CVA</option>
					<option value="128963">GUADALMEZ</option>
					<option value="125949">HERENCIA</option>
					<option value="128896">HINOJOSAS DE CVA</option>
					<option value="126024">HORCAJO DE LOS MONTES</option>
					<option value="128966">LAS LABORES</option>
					<option value="126836">LA SOLANA</option>
					<option value="129067">LLANOS DEL CAUDILLO</option>
					<option value="128952">LOS CORTIJOS</option>
					<option value="130183">LOS POZUELOS DE CALATRAVA</option>
					<option value="128962">LOS POZUELOS DE CVA</option>
					<option value="128968">LUCIANA</option>
					<option value="128910">MALAGON</option>
					<option value="128926">MANZANARES</option>
					<option value="128928">MEMBRILLA</option>
					<option value="128970">MESTANZA</option>
					<option value="128971">MIGUELTURRA</option>
					<option value="128944">MONTIEL</option>
					<option value="128972">MORAL DE CVA</option>
					<option value="128897">NAVALPINO</option>
					<option value="128899">NAVAS DE ESTENA</option>
					<option value="128900">PEDRO MUÑOZ</option>
					<option value="129050">PICON</option>
					<option value="126403">PIEDRABUENA</option>
					<option value="128958">POBLETE</option>
					<option value="128895">PORZUNA</option>
					<option value="128960">POZUELO DE CVA</option>
					<option value="128984">PUEBLA DE DON RODRIGO</option>
					<option value="128965">PUEBLA DEL PRINCIPE</option>
					<option value="128967">PUERTO LAPICE</option>
					<option value="126579">PUERTOLLANO</option>
					<option value="128969">RETUERTA BULLAQUE</option>
					<option value="128969">RETUERTA DEL BULLAQUE</option>
					<option value="129068">RUIDERA</option>
					<option value="128973">SACERUELA</option>
					<option value="128974">SAN CARLOS DEL VALLE</option>
					<option value="128975">SAN LORENZO DE CVA</option>
					<option value="128976">SANTA CRUZ DE LOS CAÑAMOS</option>
					<option value="128977">SANTA CRUZ DE MUDELA</option>
					<option value="128978">SOCUELLAMOS</option>
					<option value="128979">SOLANA DEL PINO</option>
					<option value="128980">TERRINCHES</option>
					<option value="128932">TOMELLOSO</option>
					<option value="128933">TORRALBA DE CVA</option>
					<option value="128981">TORRE DE JUAN ABAD</option>
					<option value="128881">TORRENUEVA</option>
					<option value="128982">VALDEMANCO DEL ESTERAS</option>
					<option value="128891">VALDEPEÑAS</option>
					<option value="128983">VALENZUELA DE CALATRAVA</option>
					<option value="130190">VILLAHERMOSA</option>
					<option value="127655">VILLAMANRIQUE</option>
					<option value="128905">VILLAMAYOR DE CVA</option>
					<option value="128906">VILLANUEVA DE LA FUENTE</option>
					<option value="128909">VILLANUEVA DE LOS INFANTES</option>
					<option value="128907">VILLANUEVA DE SAN CARLOS</option>
					<option value="128908">VILLAR DEL POZO</option>
					<option value="128892">VILLARRUBIA DE LOS OJOS</option>
					<option value="127730">VILLARTA DE SAN JUAN</option>
					<option value="127737">VISO DEL MARQUES</option>
				</select>
			<br/>
			<div style="margin-left:235px;margin-top:10px;color:#006699;">
				<i><xsl:value-of select="$lang.liquidaciones"/></i>
			</div>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;margin-top:5px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_1</xsl:attribute>
				<xsl:attribute name="id">n_liq_1</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_1"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_2</xsl:attribute>
				<xsl:attribute name="id">n_liq_2</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_2"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_3</xsl:attribute>
				<xsl:attribute name="id">n_liq_3</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_3"/></xsl:attribute>
			</input>
			<br/>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_4</xsl:attribute>
				<xsl:attribute name="id">n_liq_4</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_4"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_5</xsl:attribute>
				<xsl:attribute name="id">n_liq_5</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_5"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_6</xsl:attribute>
				<xsl:attribute name="id">n_liq_6</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_6"/></xsl:attribute>
			</input>
			<br/>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_7</xsl:attribute>
				<xsl:attribute name="id">n_liq_7</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_7"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_8</xsl:attribute>
				<xsl:attribute name="id">n_liq_8</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_8"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_9</xsl:attribute>
				<xsl:attribute name="id">n_liq_9</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_9"/></xsl:attribute>
			</input>
			<br/>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_10</xsl:attribute>
				<xsl:attribute name="id">n_liq_10</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_10"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_11</xsl:attribute>
				<xsl:attribute name="id">n_liq_11</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_11"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_12</xsl:attribute>
				<xsl:attribute name="id">n_liq_12</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_12"/></xsl:attribute>
			</input>
			<br/>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_13</xsl:attribute>
				<xsl:attribute name="id">n_liq_13</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_13"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_14</xsl:attribute>
				<xsl:attribute name="id">n_liq_14</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_14"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_15</xsl:attribute>
				<xsl:attribute name="id">n_liq_15</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_15"/></xsl:attribute>
			</input>
			<br/>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_16</xsl:attribute>
				<xsl:attribute name="id">n_liq_16</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_16"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_17</xsl:attribute>
				<xsl:attribute name="id">n_liq_17</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_17"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_18</xsl:attribute>
				<xsl:attribute name="id">n_liq_18</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_18"/></xsl:attribute>
			</input>
			<br/>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_19</xsl:attribute>
				<xsl:attribute name="id">n_liq_19</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_19"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_20</xsl:attribute>
				<xsl:attribute name="id">n_liq_20</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_20"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_21</xsl:attribute>
				<xsl:attribute name="id">n_liq_21</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_21"/></xsl:attribute>
			</input>
			<br/>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-left:100px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_22</xsl:attribute>
				<xsl:attribute name="id">n_liq_22</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_22"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_23</xsl:attribute>
				<xsl:attribute name="id">n_liq_23</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/n_liq_23"/></xsl:attribute>
			</input>
			<input type="text">
				<xsl:attribute name="style">width:120px;margin-bottom:1px;</xsl:attribute>
				<xsl:attribute name="name">n_liq_24</xsl:attribute>
				<xsl:attribute name="id">n_liq_24</xsl:attribute>
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
