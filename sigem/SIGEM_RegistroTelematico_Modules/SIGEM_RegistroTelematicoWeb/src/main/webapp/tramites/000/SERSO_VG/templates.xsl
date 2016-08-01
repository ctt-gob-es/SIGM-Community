<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="templates_comunes.xsl" />

	<xsl:template name="ADD_ROW">
		var last_row = new Number(1);
		var max_num_rows = new Number(8);
		document.getElementById('numColegios').value = last_row;

		function addRow(){
			if (last_row &lt; max_num_rows){
				last_row = last_row + 1;
				var row = document.getElementById("row_"+last_row.toString());
				row.style.display = "";
				if (last_row == max_num_rows){
					var link = document.getElementById("addRow_link");
					link.style.display = "none";
				}
				document.getElementById('numColegios').value=last_row;
			}
		}
	</xsl:template>

	<xsl:template name="VALIDACIONES_ESPECIFICAS">
		function validar_eCorreos(){
			if(!validarEmail(document.getElementById('d_email_repre'))){
				document.getElementById('d_email_repre').focus();
				alert('Dirección de correo electrónico incorrecta');
				return false;
			}

			for(var i = 1; i&lt;=8 ; i++){

				if(!validarEmail(document.getElementById('emailCentro'+i))){
					document.getElementById('emailCentro'+i).focus();
					alert('Dirección de correo electrónico incorrecta');
					return false;
				}
			}

			return true;
		}

		function validar_numeros(){

			for(var i = 1; i&lt;=8 ; i++){
				if(!numerico(document.getElementById('numGrupos'+i).value)){
					document.getElementById('numGrupos'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('a'+i).value)){
					document.getElementById('a'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}
				
				if(!numerico(document.getElementById('b'+i).value)){
					document.getElementById('b'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('c'+i).value)){
					document.getElementById('c'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('d'+i).value)){
					document.getElementById('d'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('e'+i).value)){
					document.getElementById('e'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('numTotalIgualdad'+i).value)){
					document.getElementById('numTotalIgualdad'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('numCursos4'+i).value)){
					document.getElementById('numCursos4'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('a4'+i).value)){
					document.getElementById('a4'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('b4'+i).value)){
					document.getElementById('b4'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('c4'+i).value)){
					document.getElementById('c4'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('d4'+i).value)){
					document.getElementById('d4'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('e4'+i).value)){
					document.getElementById('e4'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('numCursos5'+i).value)){
					document.getElementById('numCursos5'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('a5'+i).value)){
					document.getElementById('a5'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('b5'+i).value)){
					document.getElementById('b5'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('c5'+i).value)){
					document.getElementById('c5'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('d5'+i).value)){
					document.getElementById('d5'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('e5'+i).value)){
					document.getElementById('e5'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('numCursos6'+i).value)){
					document.getElementById('numCursos6'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('a6'+i).value)){
					document.getElementById('a6'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('b6'+i).value)){
					document.getElementById('b6'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('c6'+i).value)){
					document.getElementById('c6'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('d6'+i).value)){
					document.getElementById('d6'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}

				if(!numerico(document.getElementById('e6'+i).value)){
					document.getElementById('e6'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}
				
				if(!numerico(document.getElementById('numTotalIntercultura'+i).value)){
					document.getElementById('numTotalIntercultura'+i).focus();
					alert('Valor incorrecto. Debe introducir un valor numérico');
					return false;
				}
			}

			return true;
		}
	</xsl:template>

	<xsl:template name="COLEGIO">
		<xsl:param name="row_id" />

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
			       <xsl:when test="$row_id&gt;'1'">
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
						<xsl:attribute name="style">width:90%;</xsl:attribute>
						<xsl:value-of select="$lang.nombreCentro"/>:*
					</label>
					<br/>
					<input type="text">
						<xsl:attribute name="style">width:550px; </xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of select="$param_nombreCentro"/></xsl:attribute>
						<xsl:attribute name="id"><xsl:value-of select="$param_nombreCentro"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_nombreCentro]"/></xsl:attribute>
					</input>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:120px;</xsl:attribute>
						<xsl:value-of select="$lang.direccionCentro"/>:*
					</label>
					<input type="text">
						<xsl:attribute name="style">width:502px; </xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of select="$param_direccionCentro"/></xsl:attribute>
						<xsl:attribute name="id"><xsl:value-of select="$param_direccionCentro"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_direccionCentro]"/></xsl:attribute>
					</input>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:120px;</xsl:attribute>
						<xsl:value-of select="$lang.telefonoCentro"/>:*
					</label>
					<input type="text">
						<xsl:attribute name="style">width:300px; </xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of select="$param_telefonoCentro"/></xsl:attribute>
						<xsl:attribute name="id"><xsl:value-of select="$param_telefonoCentro"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_telefonoCentro]"/></xsl:attribute>
					</input>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:120px;</xsl:attribute>
						<xsl:value-of select="$lang.faxCentro"/>:*
					</label>
					<input type="text">
						<xsl:attribute name="style">width:300px; </xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of select="$param_faxCentro"/></xsl:attribute>
						<xsl:attribute name="id"><xsl:value-of select="$param_faxCentro"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_faxCentro]"/></xsl:attribute>
					</input>
				</div>	
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:120px;</xsl:attribute>
						<xsl:value-of select="$lang.emailCentro"/>:*
					</label>
					<input type="text">
						<xsl:attribute name="style">width:300px; </xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of select="$param_emailCentro"/></xsl:attribute>
						<xsl:attribute name="id"><xsl:value-of select="$param_emailCentro"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_emailCentro]"/></xsl:attribute>
					</input>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:90%;</xsl:attribute>
						<xsl:value-of select="$lang.contactoCentro"/>:*
					</label>
					<br/>
					<input type="text">
						<xsl:attribute name="style">width:550px; </xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of select="$param_contactoCentro"/></xsl:attribute>
						<xsl:attribute name="id"><xsl:value-of select="$param_contactoCentro"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_contactoCentro]"/></xsl:attribute>
					</input>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:120px;</xsl:attribute>
						<xsl:value-of select="$lang.cargoCentro"/>:*
					</label>
					<input type="text">
						<xsl:attribute name="style">width:300px; </xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of select="$param_cargoCentro"/></xsl:attribute>
						<xsl:attribute name="id"><xsl:value-of select="$param_cargoCentro"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_cargoCentro]"/></xsl:attribute>
					</input>
				</div>
				<div class="col" style="padding-left:10px;">
					<label class="gr">
						<xsl:attribute name="style">width:100%x;</xsl:attribute>
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
							<xsl:attribute name="onclick">
								if(this.checked){
									document.getElementById('<xsl:value-of select="$param_concertado"/>').checked = false;
									document.getElementById('<xsl:value-of select="$param_privado"/>').checked = false;
								}
							</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_publico]"/></xsl:attribute>
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
							<xsl:attribute name="onclick">
								if(this.checked){
									document.getElementById('<xsl:value-of select="$param_publico"/>').checked = false;
									document.getElementById('<xsl:value-of select="$param_privado"/>').checked = false;
								}
							</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_concertado]"/></xsl:attribute>
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
							<xsl:attribute name="onclick">
								if(this.checked){
									document.getElementById('<xsl:value-of select="$param_publico"/>').checked = false;
									document.getElementById('<xsl:value-of select="$param_concertado"/>').checked = false;
								}
							</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_privado]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.privado"/>					
					</label>
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
						<xsl:attribute name="style">width:240px;padding-left:20px;</xsl:attribute>
						<xsl:value-of select="$lang.numGrupos"/>:
					</label>
					<input type="text">
						<xsl:attribute name="style">width:50px; </xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of select="$param_numGrupos"/></xsl:attribute>
						<xsl:attribute name="id"><xsl:value-of select="$param_numGrupos"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_numGrupos]"/></xsl:attribute>
					</input>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:100%;padding-left:20px;</xsl:attribute>
						<xsl:value-of select="$lang.numAlunmos"/>:
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:300px;padding-left:50px;</xsl:attribute>
						<xsl:value-of select="$lang.a"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_a"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_a"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_a]"/></xsl:attribute>
						</input>

						<xsl:value-of select="$lang.b"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_b"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_b"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_b]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.c"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_c"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_c"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_c]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.d"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_d"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_d"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_d]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.e"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_e"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_e"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_e]"/></xsl:attribute>
						</input>
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:280px;padding-left:20px;</xsl:attribute>
						<xsl:value-of select="$lang.numTotalIgualdad"/>:
					</label>
					<input type="text">
						<xsl:attribute name="style">width:50px; </xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of select="$param_numTotalIgualdad"/></xsl:attribute>
						<xsl:attribute name="id"><xsl:value-of select="$param_numTotalIgualdad"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_numTotalIgualdad]"/></xsl:attribute>
					</input>
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
						<xsl:value-of select="$lang.numCursos4"/>
					
						<input type="text">
							<xsl:attribute name="style">width:50px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_numCursos4"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_numCursos4"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_numCursos4]"/></xsl:attribute>
						</input>
					</label>
					<label class="gr">
						<xsl:attribute name="style">width:270px;padding-left:20px;</xsl:attribute>
						<xsl:value-of select="$lang.especificar"/>:
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:300px;padding-left:50px;</xsl:attribute>
						<xsl:value-of select="$lang.a"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_a4"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_a4"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_a4]"/></xsl:attribute>
						</input>

						<xsl:value-of select="$lang.b"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_b4"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_b4"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_b4]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.c"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_c4"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_c4"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_c4]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.d"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_d4"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_d4"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_d4]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.e"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_e4"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_e4"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_e4]"/></xsl:attribute>
						</input>
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:190px;padding-left:20px;</xsl:attribute>
						<xsl:value-of select="$lang.numCursos5"/>
					
						<input type="text">
							<xsl:attribute name="style">width:50px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_numCursos5"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_numCursos5"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_numCursos5]"/></xsl:attribute>
						</input>
					</label>
					<label class="gr">
						<xsl:attribute name="style">width:250px;padding-left:0px;</xsl:attribute>
						<xsl:value-of select="$lang.especificar"/>:
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:300px;padding-left:50px;</xsl:attribute>
						<xsl:value-of select="$lang.a"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_a5"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_a5"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_a5]"/></xsl:attribute>
						</input>

						<xsl:value-of select="$lang.b"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_b5"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_b5"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_b5]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.c"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_c5"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_c5"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_c5]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.d"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_d5"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_d5"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_d5]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.e"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_e5"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_e5"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_e5]"/></xsl:attribute>
						</input>
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:190px;padding-left:20px;</xsl:attribute>
						<xsl:value-of select="$lang.numCursos6"/>
					
						<input type="text">
							<xsl:attribute name="style">width:50px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_numCursos6"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_numCursos6"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_numCursos6]"/></xsl:attribute>
						</input>
					</label>
					<label class="gr">
						<xsl:attribute name="style">width:250px;padding-left:0px;</xsl:attribute>
						<xsl:value-of select="$lang.especificar"/>:
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:300px;padding-left:50px;</xsl:attribute>
						<xsl:value-of select="$lang.a"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_a6"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_a6"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_a6]"/></xsl:attribute>
						</input>

						<xsl:value-of select="$lang.b"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_b6"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_b6"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_b6]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.c"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_c6"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_c6"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_c6]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.d"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_d6"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_d6"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_d6]"/></xsl:attribute>
						</input>
						<xsl:value-of select="$lang.e"/>
						<input type="text">
							<xsl:attribute name="style">width:20px; </xsl:attribute>
							<xsl:attribute name="name"><xsl:value-of select="$param_e6"/></xsl:attribute>
							<xsl:attribute name="id"><xsl:value-of select="$param_e6"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_e6]"/></xsl:attribute>
						</input>
					</label>
					<br/>
					<br/>
					<label class="gr">
						<xsl:attribute name="style">width:330px;padding-left:20px;</xsl:attribute>
						<xsl:value-of select="$lang.numTotalIntercultura"/>:
					</label>
					<input type="text">
						<xsl:attribute name="style">width:50px; </xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of select="$param_numTotalIntercultura"/></xsl:attribute>
						<xsl:attribute name="id"><xsl:value-of select="$param_numTotalIntercultura"/></xsl:attribute>
						<xsl:attribute name="value"><xsl:value-of select="Datos_Registro/datos_especificos/*[name()=$param_numTotalIntercultura]"/></xsl:attribute>
					</input>
					<br/>
					<br/>
				</div>
			</td>
		</tr>
	</xsl:template>

	<xsl:template name="DECLARACIONES">

		<div class="cuadro" style="text-align:justify">	
			<input type="checkbox" id="declaro1" checked="Yes" style="border:none;color:#006699;position: relative; width:20px;" DISABLED="Yes"/>
			<xsl:value-of select="$lang.declaro1"/>
			<input type="hidden">
				<xsl:attribute name="name">texto_declaro1</xsl:attribute>
				<xsl:attribute name="id">texto_declaro1</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="$lang.declaro1"/></xsl:attribute>
			</input>
			<br/>
			<input type="checkbox" id="declaro2" checked="Yes" style="border:none;color:#006699;position: relative; width:20px;" DISABLED="Yes"/>
			<xsl:value-of select="$lang.declaro2"/>
			<input type="hidden">
				<xsl:attribute name="name">texto_declaro2</xsl:attribute>
				<xsl:attribute name="id">texto_declaro2</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="$lang.declaro2"/></xsl:attribute>
			</input>
			<br/>
			<input type="checkbox" id="declaro3" checked="Yes" style="border:none;color:#006699;position: relative; width:20px;" DISABLED="Yes"/>
			<xsl:value-of select="$lang.declaro3"/>
			<input type="hidden">
				<xsl:attribute name="name">texto_declaro3</xsl:attribute>
				<xsl:attribute name="id">texto_declaro3</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="$lang.declaro3"/></xsl:attribute>
			</input>
			<br/>
		</div>
	</xsl:template>
</xsl:stylesheet>
