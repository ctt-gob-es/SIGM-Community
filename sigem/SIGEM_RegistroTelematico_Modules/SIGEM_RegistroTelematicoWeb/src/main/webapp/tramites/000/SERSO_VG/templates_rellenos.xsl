<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="COLEGIO">
		<xsl:param name="row_id" />
		<xsl:param name="numColegios" />

		<xsl:variable name="param_nombreCentro">nombreCentro<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_direccionCentro">direccionCentro<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_telefonoCentro">telefonoCentro<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_faxCentro">faxCentro<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_emailCentro">emailCentro<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_contactoCentro">contactoCentro<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_cargoCentro">cargoCentro<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_publico">publico<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_concertado">concertado<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_privado">privado<xsl:value-of select="$row_id"/></xsl:variable>

		<xsl:variable name="param_numGrupos">numGrupos<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_a">a<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_b">b<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_c">c<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_d">d<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_e">e<xsl:value-of select="$row_id"/></xsl:variable>
		
		<xsl:variable name="param_numTotalIgualdad">numTotalIgualdad<xsl:value-of select="$row_id"/></xsl:variable>

		<xsl:variable name="param_numCursos4">numCursos4<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_a4">a4<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_b4">b4<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_c4">c4<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_d4">d4<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_e4">e4<xsl:value-of select="$row_id"/></xsl:variable>

		<xsl:variable name="param_numCursos5">numCursos5<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_a5">a5<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_b5">b5<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_c5">c5<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_d5">d5<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_e5">e5<xsl:value-of select="$row_id"/></xsl:variable>

		<xsl:variable name="param_numCursos6">numCursos6<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_a6">a6<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_b6">b6<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_c6">c6<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_d6">d6<xsl:value-of select="$row_id"/></xsl:variable>
		<xsl:variable name="param_e6">e6<xsl:value-of select="$row_id"/></xsl:variable>

		<xsl:variable name="param_numTotalIntercultura">numTotalIntercultura<xsl:value-of select="$row_id"/></xsl:variable>


		<xsl:variable name="row_style">
			<xsl:choose>
			       <xsl:when test="$row_id&gt;$numColegios">
					display:none;
		       		</xsl:when>
			</xsl:choose>
		</xsl:variable>
		
		<tr id="row_{$row_id}" style="{$row_style}">
			<td>
				<div class="submenu" style="width:100%">
   					<h1><xsl:value-of select="$lang.centroX"/> <xsl:value-of select="$row_id"/></h1>
		   		</div>
   				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:90%; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.nombreCentro"/>:*
					</label>
					<br/>
					<label>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_nombreCentro]"/>
					</label>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:120px; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.direccionCentro"/>:*
					</label>
					<label>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_direccionCentro]"/>					</label>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:120px; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.telefonoCentro"/>:*
					</label>
					<label>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_telefonoCentro]"/>
					</label>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:120px; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.faxCentro"/>:*
					</label>
					<label>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_faxCentro]"/>
					</label>
				</div>	
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:120px; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.emailCentro"/>:*
					</label>
					<label>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_emailCentro]"/>
					</label>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:90%; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.contactoCentro"/>:*
					</label>
					<br/>
					<label>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_contactoCentro]"/>
					</label>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:120px; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.cargoCentro"/>:*
					</label>
					<label>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_cargoCentro]"/>
					</label>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:100%x; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.tipoCentro"/>:			
					</label>
					<br/>	
					<label>
						<xsl:attribute name="style">padding-left:20px;</xsl:attribute>
						<input>
							<xsl:attribute name="style">width:20px;</xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_publico"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_publico"/></xsl:attribute>
							<xsl:attribute name="type">checkbox</xsl:attribute>
							<xsl:attribute name="disabled">true</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_publico]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.publico"/>								
					</label>
					<label>
						<xsl:attribute name="style">padding-left:50px;</xsl:attribute>
						<input>
							<xsl:attribute name="style">width:20px;padding-left:50px;</xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_concertado"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_concertado"/></xsl:attribute>
							<xsl:attribute name="type">checkbox</xsl:attribute>
							<xsl:attribute name="disabled">true</xsl:attribute>							
							<xsl:attribute name="value"><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_concertado]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.concertado"/>					
					</label>
					<label>
						<xsl:attribute name="style">padding-left:50px;</xsl:attribute>
						<input>
							<xsl:attribute name="style">width:20px;padding-left:50px;</xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_privado"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_privado"/></xsl:attribute>
							<xsl:attribute name="type">checkbox</xsl:attribute>
							<xsl:attribute name="disabled">true</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_privado]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.privado"/>					
					</label>
					<script>
						if(document.getElementById('<xsl:value-of select="$param_publico"/>').value=='Si')
							document.getElementById('<xsl:value-of select="$param_publico"/>').checked = true;
						if(document.getElementById('<xsl:value-of select="$param_concertado"/>').value=='Si')
							document.getElementById('<xsl:value-of select="$param_concertado"/>').checked = true;
						if(document.getElementById('<xsl:value-of select="$param_privado"/>').value=='Si')
							document.getElementById('<xsl:value-of select="$param_privado"/>').checked = true;
					</script>
				</div>

				<div class="col" style="background-color:#F8E0F7; width:95%;">
					<h1><xsl:value-of select="$lang.actividadesSolicitadas"/></h1>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:100%;</xsl:attribute>
						<b>A) <u><xsl:value-of select="$lang.talleresIgualdad"/></u>:</b>
					</label>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:260px;padding-left:20px; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.numGrupos"/>:
					</label>
					<label>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_numGrupos]"/>
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:100%;padding-left:20px; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.numAlunmos"/>:
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:350px;padding-left:50px;</xsl:attribute>
						<b><xsl:value-of select="$lang.a"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_a]"/>
						<b><xsl:value-of select="$lang.b"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_b]"/>
						<b><xsl:value-of select="$lang.c"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_c]"/>
						<b><xsl:value-of select="$lang.d"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_d]"/>
						<b><xsl:value-of select="$lang.e"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_e]"/>
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:300px;padding-left:20px; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.numTotalIgualdad"/>:
					</label>
					<label>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_numTotalIgualdad]"/>
					</label>
				</div>

				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:100%;</xsl:attribute>
						<b>B) <u><xsl:value-of select="$lang.talleresIntercultura"/></u>:</b>
					</label>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:190px;padding-left:20px;</xsl:attribute>
						<b><xsl:value-of select="$lang.numCursos4"/></b>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_numCursos4]"/>
					</label>
					<label class="gr">
						<xsl:attribute name="style">width:270px;padding-left:0px; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.especificar"/>:
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:350px;padding-left:50px;</xsl:attribute>
						<b><xsl:value-of select="$lang.a"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_a4]"/>
						<b><xsl:value-of select="$lang.b"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_b4]"/>
						<b><xsl:value-of select="$lang.c"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_c4]"/>
						<b><xsl:value-of select="$lang.d"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_d4]"/>
						<b><xsl:value-of select="$lang.e"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_e4]"/>
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:190px;padding-left:20px;</xsl:attribute>
						<b><xsl:value-of select="$lang.numCursos5"/></b>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_numCursos5]"/>
					</label>
					<label class="gr">
						<xsl:attribute name="style">width:270px;padding-left:0px; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.especificar"/>:
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:350px;padding-left:50px;</xsl:attribute>
						<b><xsl:value-of select="$lang.a"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_a5]"/>
						<b><xsl:value-of select="$lang.b"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_b5]"/>
						<b><xsl:value-of select="$lang.c"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_c5]"/>
						<b><xsl:value-of select="$lang.d"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_d5]"/>
						<b><xsl:value-of select="$lang.e"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_e5]"/>
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:190px;padding-left:20px;</xsl:attribute>
						<b><xsl:value-of select="$lang.numCursos6"/></b>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_numCursos6]"/>
					</label>
					<label class="gr">
						<xsl:attribute name="style">width:270px;padding-left:0px; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.especificar"/>:
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:350px;padding-left:50px;</xsl:attribute>
						<b><xsl:value-of select="$lang.a"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_a6]"/>
						<b><xsl:value-of select="$lang.b"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_b6]"/>
						<b><xsl:value-of select="$lang.c"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_c6]"/>
						<b><xsl:value-of select="$lang.d"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_d6]"/>
						<b><xsl:value-of select="$lang.e"/></b>
							<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_e6]"/>
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:350px;padding-left:20px; font-weight: bold;</xsl:attribute>
						<xsl:value-of select="$lang.numTotalIntercultura"/>:
					</label>
					<label>
						<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/*[name()=$param_numTotalIntercultura]"/>
					</label>
					<br/>
					<br/>
				</div>
			</td>
		</tr>
	</xsl:template>

	<xsl:template name="DECLARACIONES">
		<div class="cuadro" style="text-align:justify">	
			<input type="checkbox" id="declaro1" checked="Yes" style="border:none;color:#006699; width:20px; vertical-align:top;" DISABLED="Yes">
			<label class="gr">
				<xsl:attribute name="style">width:90%;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/texto_declaro1"/>
			</label></input>
			<br/><br/>
			<input type="checkbox" id="declaro2" checked="Yes" style="border:none;color:#006699; width:20px; vertical-align:top;" DISABLED="Yes"/>
			<label class="gr">
				<xsl:attribute name="style">width:90%;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/texto_declaro2"/>
			</label>
			<br/><br/>
			<input type="checkbox" id="declaro3" checked="Yes" style="border:none;color:#006699;position: ; width:20px; vertical-align:top;" DISABLED="Yes"/>
			<label class="gr">
				<xsl:attribute name="style">width:90%;</xsl:attribute>
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/texto_declaro3"/>
			</label>
			<br/>
		</div>
	</xsl:template>

</xsl:stylesheet>
