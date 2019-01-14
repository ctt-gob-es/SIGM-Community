<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="../templates_comunes.xsl" />

<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:include href="../REC_COMUNES/templates_comunes.xsl" />
	<xsl:include href="../REC_COMUNES/template_IBAN.xsl" />

	<xsl:variable name="lang.titulo" select="'Gestión Tributaria, Inspección y Recaudación. Domiciliaciones'"/>

	<xsl:variable name="lang.datosIdentificativos" select="'Datos identificativos'"/>
	<xsl:variable name="lang.datosObligado" select="'Datos del contribuyente'"/>
	<xsl:variable name="lang.datosRepresentante" select="'Datos del representante o presentador autorizado'"/>
	<xsl:variable name="lang.datosSolicitud" select="'Datos de la Solicitud'"/>
	<xsl:variable name="lang.datosBancarios" select="'Datos Bancarios'"/>

	<xsl:variable name="lang.pres_nif" select="'NIF Presentador'"/>
	<xsl:variable name="lang.pres_nombre" select="'Nombre Presentador'"/>

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

	<xsl:variable name="lang.nif" select="'NIF/CIF'"/>
	<xsl:variable name="lang.nombre" select="'Apellidos y Nombre o Denominación Social'"/>
	<xsl:variable name="lang.calle" select="'Domicilio'"/>
	<xsl:variable name="lang.numero" select="'Número'"/>
	<xsl:variable name="lang.escalera" select="'Escalera'"/>
	<xsl:variable name="lang.planta_puerta" select="'Planta/Puerta'"/>
	<xsl:variable name="lang.c_postal" select="'Código Postal'"/>
	<xsl:variable name="lang.ciudad" select="'Ciudad'"/>
	<xsl:variable name="lang.region" select="'Región / País'"/>
	<xsl:variable name="lang.movil" select="'Número de teléfono móvil'"/>
	<xsl:variable name="lang.d_email" select="'Dirección de correo electrónico'"/>	

	<xsl:variable name="lang.municipio" select="'Municipio:'"/>
	<xsl:variable name="lang.apellidos_nombre" select="'Sujeto/s Pasivo/s *:'"/>
	<xsl:variable name="lang.tributo" select="'Tributo:'"/>
	<xsl:variable name="lang.tipo" select="'Tipo:'"/>
	<xsl:variable name="lang.objeto" select="'Objeto Gravamen **:'"/>

	<xsl:variable name="lang.entidad" select="'Entidad:'"/>
	<xsl:variable name="lang.sucursal" select="'Sucursal:'"/>
	<xsl:variable name="lang.dc" select="'D.C.:'"/>
	<xsl:variable name="lang.ncc" select="'NºC.C.:'"/>

	<xsl:variable name="lang.titular" select="'Titular de la cuenta:'"/>
	<xsl:variable name="lang.anexos" select="'Anexos'"/>
	
	<xsl:template match="/">
		<xsl:call-template name="DATOS_SOLICITUD_RELLENOS_PRESENTADOR" />
		<br/>	
		<div class="col">
			<label class="gr" style="position: relative; width:350px;">
				<b><xsl:value-of select="$lang.datosSolicitud"/></b>	
			</label>
			<br/>
		</div>
		<div class="col">
			<label class="gr" style="position: relative; width:130px;">
				<xsl:value-of select="$lang.municipio"/>	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_municipio1"/>
			</label>
			<br/>
			<label class="gr" style="position: relative; width:130px;">
				<xsl:value-of select="$lang.apellidos_nombre"/>	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/apellidos_nombre1"/>
			</label>
			<br/>
			<label class="gr" style="position: relative; width:130px;">
				<xsl:value-of select="$lang.tributo"/>	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tributo1"/>
			</label>
			<br/>
			<label class="gr" style="position: relative; width:130px;">
				<xsl:value-of select="$lang.tipo"/>	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tipo"/>
			</label>
			<br/>
			<label class="gr" style="position: relative; width:130px;">
				<xsl:value-of select="$lang.objeto"/>	
			</label>
			<label class="gr" style="position: relative; width:500px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/objeto1"/>
			</label>
			<br/>
		</div>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/municipio2!='000'">
			<div class="col">
				<label class="gr" style="position: relative; width:150px;">
					<xsl:value-of select="$lang.municipio"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_municipio2"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.apellidos_nombre"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/apellidos_nombre2"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.tributo"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tributo2"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.tributo"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tipo2"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.objeto"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/objeto2"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/municipio3!='000'">
			<div class="col">
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.municipio"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_municipio3"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.apellidos_nombre"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/apellidos_nombre3"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.tributo"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tributo3"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.tributo"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tipo3"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.objeto"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/objeto3"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/municipio4!='000'">
			<div class="col">
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.municipio"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_municipio4"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.apellidos_nombre"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/apellidos_nombre4"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.tributo"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tributo4"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.tributo"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tipo4"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.objeto"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/objeto4"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/municipio5!='000'">
			<div class="col">
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.municipio"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_municipio5"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.apellidos_nombre"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/apellidos_nombre5"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.tributo"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tributo5"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.tributo"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tipo5"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.objeto"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/objeto5"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/municipio6!='000'">
			<div class="col">
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.municipio"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_municipio6"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.apellidos_nombre"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/apellidos_nombre6"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.tributo"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tributo6"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.tributo"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tipo6"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.objeto"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/objeto6"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/municipio7!='000'">
			<div class="col">
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.municipio"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_municipio7"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.apellidos_nombre"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/apellidos_nombre7"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.tributo"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tributo7"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.tributo"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tipo7"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.objeto"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/objeto7"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<xsl:if test="Solicitud_Registro/Datos_Firmados/Datos_Especificos/municipio8!='000'">
			<div class="col">
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.municipio"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_municipio8"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.apellidos_nombre"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/apellidos_nombre8"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.tributo"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tributo8"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.tributo"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/Descripcion_tipo8"/>
				</label>
				<br/>
				<label class="gr" style="position: relative; width:130px;">
					<xsl:value-of select="$lang.objeto"/>	
				</label>
				<label class="gr" style="position: relative; width:500px;">
					<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/objeto8"/>
				</label>
				<br/>
			</div>
		</xsl:if>
		<br/>
		<div class="col">
			<b>
				<label class="gr" style="position: relative; width:400px;">
					<xsl:value-of select="$lang.datosBancarios"/>:	
				</label>
			</b>
		</div>
		<div class="col">
			<xsl:call-template name="CUENTA_CORRIENTE_IBAN_RELLENOS" />
		</div>
		<br/>
		<xsl:call-template name="TEXTO_LEGAL_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_DATOS_PERSONALES_COMUN_RELLENO" />

		<xsl:call-template name="TEXTO_AUTOFIRMA_COMUN_RELLENO" />
	</xsl:template>
</xsl:stylesheet>