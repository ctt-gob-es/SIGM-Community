<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="ISO-8859-1" method="html"/>

	<xsl:include href="templates_comunes_rellenos.xsl" />	
	<xsl:template match="/">
		<xsl:call-template name="DATOS_SOLICITANTE" />
		<xsl:call-template name="DATOS_BENEFICIARIO" />
		<xsl:call-template name="DATOS_FAMILIA" />
		<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='LIBROS')">
			<xsl:if test="not(Solicitud_Registro/Datos_Firmados/Datos_Especificos/tipoAyuda='COMEDOR')">
				<xsl:call-template name="DATOS_PROPUESTA_1" />
			</xsl:if>
		</xsl:if>
		<!--<xsl:call-template name="DATOS_FAMILIA2" />-->
		<br/>
		<div style="color: grey; text-align:justify">
			<!--label class="gr">
				<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
				Los datos personales, identificativos y de contacto, aportados mediante esta comunicación se entienden facilitados voluntariamente, y serán incorporados a un fichero cuya finalidad es la de mantener con Vd. relaciones dentro del ámbito de las competencias de esta Administración Pública así como informarle de nuestros servicios presentes y futuros ya sea por correo ordinario o por medios telemáticos y enviarle invitaciones para eventos y felicitaciones en fechas señaladas. Entenderemos que presta su consentimiento tácito para este tratamiento de datos si en el plazo de un mes no expresa su voluntad en contra. Podrá ejercer sus derechos de acceso, rectificación, cancelación y oposición ante el Responsable del Fichero, la Diputación Provincial de Ciudad Real en C/ Toledo, 17, 13071 Ciudad Real - España, siempre acreditando conforme a Derecho su identidad en la comunicación. En cumplimiento de la L.O. 34/2002 le informamos de que puede revocar en cualquier momento el consentimiento que nos otorga dirigiéndose a la dirección citada ut supra o bien al correo electrónico lopd@dipucr.es o bien por telefono al numero gratuito 900 714 080.	
			</label-->

			<label class="gr">
				<xsl:attribute name="style">position: relative; width:650px;</xsl:attribute>
				<br/>
				AVISO LEGAL:
				<br/><br/>
				En cumplimiento de la Ley Orgánica 15/1999, de 13 de diciembre, de Protección de Datos de Carácter Personal, le informamos que sus datos serán incorporados a un fichero con datos de carácter personal cuya finalidad es la gestión de las solicitudes realizadas por ciudadanos/as a la Diputación Provincial de Ciudad Real. Le informamos así mismo que los datos podrán ser comunicados a otras Administraciones Públicas en el ámbito de competencias semejantes o materias comunes, en cumplimiento de la legislación aplicable. El/a interesado/a declara estar informado/a de las condiciones detalladas en la presente cláusula, se compromete a mantener actualizados sus datos y, en cualquier caso, podrá ejercitar gratuitamente los derechos de acceso, rectificación, cancelación y oposición (siempre de acuerdo con los supuestos contemplados por la legislación vigente) dirigiéndose a la Diputación Provincial de Ciudad Real en C/ Toledo, 17. 13071-Ciudad Real- España, siempre acreditando conforme a Derecho su identidad en la comunicación. 
			</label>
		</div>


		<div class="col">
			<label class="gr">
				<xsl:attribute name="style">position: relative; width:600px;</xsl:attribute>
				<b>Documentación que se aporta: </b>
			</label>
			<label class="gr" style="position: relative; width:600px;">
				<xsl:value-of select="Solicitud_Registro/Datos_Firmados/Datos_Especificos/documentos"/>				
			</label>
		</div>
	</xsl:template>
</xsl:stylesheet>
