<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>
	<xsl:variable name="lang.titulo" select="'Formulario de Presentación de Propuestas y Mociones para la Diputacion Provincial de Ciudad Real'"/>
	<xsl:variable name="lang.datosInteresado" select="'Datos del Interesado'"/>
	<xsl:variable name="lang.docIdentidad" select="'Documento de identidad'"/>
	<xsl:variable name="lang.nombre" select="'Nombre'"/>
	<xsl:variable name="lang.email" select="'Correo electrónico'"/>
	<xsl:variable name="lang.datosPropuesta" select="'Datos de la Propuesta'"/>
	<xsl:variable name="lang.extracto" select="'Extracto'"/>
	<xsl:variable name="lang.origen" select="'Origen'"/>
	<xsl:variable name="lang.destino" select="'Órgano competente'"/>
	<xsl:variable name="lang.prioridad" select="'Prioridad'"/>
	<xsl:variable name="lang.motivo" select="'Motivo de la prioridad'"/>
	<xsl:variable name="lang.pleno" select="'Pleno'"/>
	<xsl:variable name="lang.jgov" select="'Junta de Gobierno'"/>
	<xsl:variable name="lang.mesa" select="'Mesa de Contratación'"/>
	<xsl:variable name="lang.comi" select="'Comisión Informativa'"/>
	<xsl:variable name="lang.muyalta" select="'Muy alta'"/>
	<xsl:variable name="lang.alta" select="'Alta'"/>
	<xsl:variable name="lang.media" select="'Media'"/>
	<xsl:variable name="lang.baja" select="'Baja'"/>
	<xsl:variable name="lang.anexar" select="'Anexar ficheros'"/>
	<xsl:variable name="lang.anexarInfo1" select="'1.- Para adjuntar un fichero (*.zip), pulse el botón examinar.'"/>
	<xsl:variable name="lang.anexarInfo2" select="'2.- Seleccione el fichero que desea anexar a la solicitud.'"/>
	<xsl:variable name="lang.documento1" select="'Contenido'"/>
	<xsl:variable name="lang.documento2" select="'Anexos'"/>
		
	<xsl:template match="/">
		<script language="Javascript">
			//array de campos obligatorios -> ('id_campo','nombre_campo')
			var validar = new Array(6);
			validar[0] = new Array('documentoIdentidad', '<xsl:value-of select="$lang.docIdentidad"/>');
			validar[1] = new Array('nombreSolicitante','<xsl:value-of select="$lang.nombre"/>');
			validar[2] = new Array('extracto','<xsl:value-of select="$lang.extracto"/>');
			validar[3] = new Array('SECR1D1','<xsl:value-of select="$lang.documento1"/>');
			validar[4] = new Array('origen','<xsl:value-of select="$lang.origen"/>');
			validar[5] = new Array('destino','<xsl:value-of select="$lang.destino"/>');
			
			//Array con los datos especificos del formilario -> -> ('id_campo','tag_xml')
			var especificos = new Array(5);
			especificos[0] = new Array('extracto','Extracto');
			especificos[1] = new Array('origen','Origen');
			especificos[2] = new Array('destino','Destino');
			especificos[3] = new Array('prioridad','Prioridad');
			especificos[4] = new Array('motivo','Motivo');

			var validarNumero;
			
			function verificacionesEspecificas() {
				return true;
			}
		</script>
		<h1><xsl:value-of select="$lang.titulo"/></h1>
   		<br/>
   		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosInteresado"/></h1>
   		</div>
   		<div class="cuadro" style="">	
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.docIdentidad"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px;</xsl:attribute>
					<xsl:attribute name="name">documentoIdentidad</xsl:attribute>
					<xsl:attribute name="id">documentoIdentidad</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Documento_Identificacion/Numero"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.nombre"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="id">nombreSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Nombre"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.email"/>:
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">emailSolicitante</xsl:attribute>
					<xsl:attribute name="id">emailSolicitante</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/Remitente/Correo_Electronico"/></xsl:attribute>
					<xsl:attribute name="disabled"></xsl:attribute>
				</input>
			</div>
			<input type="hidden"><xsl:attribute name="id">telefono</xsl:attribute></input>
			<input type="hidden"><xsl:attribute name="id">domicilioNotificacion</xsl:attribute></input>
			<input type="hidden"><xsl:attribute name="id">localidad</xsl:attribute></input>
			<input type="hidden"><xsl:attribute name="id">provincia</xsl:attribute></input>
			<input type="hidden"><xsl:attribute name="id">codigoPostal</xsl:attribute></input>
		</div>
		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.datosPropuesta"/></h1>
   		</div>
   		<div class="cuadro" style="">
 			<div class="col" style="height: 66px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.extracto"/>:*
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">extracto</xsl:attribute>
					<xsl:attribute name="id">extracto</xsl:attribute>
					<xsl:attribute name="rows">5</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/Extracto"/>
				</textarea>
			</div>
   			<div class="col">
	   			<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento1"/> (*.doc):*
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">SECR1D1</xsl:attribute>
					<xsl:attribute name="id">SECR1D1</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">SECR1D1_Tipo</xsl:attribute>
					<xsl:attribute name="id">SECR1D1_Tipo</xsl:attribute>
					<xsl:attribute name="value">doc</xsl:attribute>
				</input>
			</div>
			<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.origen"/>:*
				</label>
				<input type="text">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">origen</xsl:attribute>
					<xsl:attribute name="id">origen</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/Origen"/></xsl:attribute>
				</input>
			</div>
		   	<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.destino"/>:*
				</label>
				<xsl:variable name="clas" select="Datos_Registro/datos_especificos/Destino"/> 
				<select class="gr">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">destino</xsl:attribute>
					<xsl:attribute name="id">destino</xsl:attribute>
					<option value=""></option>
					<xsl:choose>
				       <xsl:when test="$clas='PLEN'">
				           <option value="PLEN" selected="selected"><xsl:value-of select="$lang.pleno"/></option>
				       </xsl:when>
				       <xsl:otherwise>
				           <option value="PLEN"><xsl:value-of select="$lang.pleno"/></option> 
				       </xsl:otherwise>
				    </xsl:choose>
					<xsl:choose>
				       <xsl:when test="$clas='JGOV'">
				           <option value="JGOV" selected="selected"><xsl:value-of select="$lang.jgov"/></option>
				       </xsl:when>
				       <xsl:otherwise>
				           <option value="JGOV"><xsl:value-of select="$lang.jgov"/></option> 
				       </xsl:otherwise>
				    </xsl:choose>
					<xsl:choose>
				       <xsl:when test="$clas='MESA'">
				           <option value="MESA" selected="selected"><xsl:value-of select="$lang.mesa"/></option>
				       </xsl:when>
				       <xsl:otherwise>
				           <option value="MESA"><xsl:value-of select="$lang.mesa"/></option> 
				       </xsl:otherwise>
				    </xsl:choose>
					<xsl:choose>
				       <xsl:when test="$clas='COMI'">
				           <option value="COMI" selected="selected"><xsl:value-of select="$lang.comi"/></option>
				       </xsl:when>
				       <xsl:otherwise>
				           <option value="COMI"><xsl:value-of select="$lang.comi"/></option> 
				       </xsl:otherwise>
				    </xsl:choose>
				</select>
			</div>
		   	<div class="col">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.prioridad"/>:
				</label>
				<xsl:variable name="clas" select="Datos_Registro/datos_especificos/Prioridad"/> 
				<select class="gr">
					<xsl:attribute name="style">position: relative; width:350px; </xsl:attribute>
					<xsl:attribute name="name">prioridad</xsl:attribute>
					<xsl:attribute name="id">prioridad</xsl:attribute>
					<option value=""></option>
					<xsl:choose>
				       <xsl:when test="$clas='1'">
				           <option value="1" selected="selected"><xsl:value-of select="$lang.muyalta"/></option>
				       </xsl:when>
				       <xsl:otherwise>
				           <option value="1"><xsl:value-of select="$lang.muyalta"/></option> 
				       </xsl:otherwise>
				    </xsl:choose>
					<xsl:choose>
				       <xsl:when test="$clas='2'">
				           <option value="2" selected="selected"><xsl:value-of select="$lang.alta"/></option>
				       </xsl:when>
				       <xsl:otherwise>
				           <option value="2"><xsl:value-of select="$lang.alta"/></option> 
				       </xsl:otherwise>
				    </xsl:choose>
					<xsl:choose>
				       <xsl:when test="$clas='3'">
				           <option value="3" selected="selected"><xsl:value-of select="$lang.media"/></option>
				       </xsl:when>
				       <xsl:otherwise>
				           <option value="3"><xsl:value-of select="$lang.media"/></option> 
				       </xsl:otherwise>
				    </xsl:choose>
					<xsl:choose>
				       <xsl:when test="$clas='4'">
				           <option value="4" selected="selected"><xsl:value-of select="$lang.baja"/></option>
				       </xsl:when>
				       <xsl:otherwise>
				           <option value="4"><xsl:value-of select="$lang.baja"/></option> 
				       </xsl:otherwise>
				    </xsl:choose>
				</select>
			</div>
		   	<div class="col" style="height: 66px;">
				<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.motivo"/>
				</label>
				<textarea class="gr">
					<xsl:attribute name="style">position: relative; width:490px; font:normal 100%/normal 'Arial', Tahoma, Helvetica, sans-serif;</xsl:attribute>
					<xsl:attribute name="name">motivo</xsl:attribute>
					<xsl:attribute name="id">motivo</xsl:attribute>
					<xsl:attribute name="rows">5</xsl:attribute>
					<xsl:value-of select="Datos_Registro/datos_especificos/Motivo"/>
				</textarea>
			</div>
   		</div>
   		<br/>
		<div class="submenu">
   			<h1><xsl:value-of select="$lang.anexar"/></h1>
   		</div>
   		<div class="cuadro" style="">
			<label class="gr">
			   		<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
		   			<xsl:value-of select="$lang.anexarInfo1"/><br/>
		   			<xsl:value-of select="$lang.anexarInfo2"/><br/>
			</label>
   			<div class="col">
	   			<label class="gr">
					<xsl:attribute name="style">position: relative; width:150px;</xsl:attribute>
					<xsl:value-of select="$lang.documento2"/>:
				</label>
				<input type="file">
					<xsl:attribute name="style">position: relative; width:500px; </xsl:attribute>
					<xsl:attribute name="name">SECR1D2</xsl:attribute>
					<xsl:attribute name="id">SECR1D2</xsl:attribute>
					<xsl:attribute name="value"></xsl:attribute>
				</input>
				<input type="hidden">
					<xsl:attribute name="name">SECR1D2_Tipo</xsl:attribute>
					<xsl:attribute name="id">SECR1D2_Tipo</xsl:attribute>
					<xsl:attribute name="value">zip</xsl:attribute>
				</input>
			</div>
   		</div>
   		<br/>
		<input type="hidden">
			<xsl:attribute name="name">datosEspecificos</xsl:attribute>
			<xsl:attribute name="id">datosEspecificos</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</input>
   		<br/>
	</xsl:template>
</xsl:stylesheet>
