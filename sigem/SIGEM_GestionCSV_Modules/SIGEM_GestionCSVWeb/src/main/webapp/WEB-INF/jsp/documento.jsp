<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


	<form:form action="action/documento/search" commandName="documento" cssClass="block">

		<div class="onecol">
				<p class="enlaces">
					<a href='<spring:url value="action/documento/form"/>' title='<spring:message code="label.form.button.home"/>' class="inicio">
						<span>
							<spring:message code="label.form.button.home"/>
						</span>
					</a>
				</p>
		</div>

		<div class="twocolrightb cabecera">
			<div class="column first">
				<h4><spring:message code="label.form.csv.title" /></h4>
			</div>
			<div class="column last textright">
				<p class="botones">
					<input type="submit" id="search" name="search"
						class="buscar" value='<spring:message code="label.form.button.search" />'/>
				</p>
			</div>
		</div>

		<form:errors path="*" cssClass="form-errors" htmlEscape="on" />

		<c:if test="${!empty mensajeError}">
		<div id="errorMessage">
			<p>${mensajeError}</p>
		</div>
		</c:if>

		<div class="formulario">

			<c:if test="${not empty listaEntidades}">
			<p>
				<label for="idEntidad">
					<spring:message code="label.form.csv.entidad" />
				</label>
				<select name="idEntidad" id="idEntidad">
					<c:forEach items="${listaEntidades}" var="entidad" >
						<option label='<c:out value="${entidad.nombre}" />' value='<c:out value="${entidad.identificador}" />'>
					</c:forEach>
				</select>
			</p>
			</c:if>
			<c:if test="${!empty idEntidad}">
				<input type="hidden" name="idEntidad" value="${idEntidad}"/>
			</c:if>

			<p>
				<label for="csv">
					<spring:message code="label.form.csv.csv" />
				</label>
				<input type="text" id="csv" name="csv" class="small" value="${csv}" />
			</p>

			<!-- [eCenpri-Manu Ticket #280] - INICIO ALSIGM3 Incluir texto aclaratorio Consulta de documentos con CSV -->
			<div>
				<p style='text-align:justify;display:block;padding:0;margin: 0px 0px 0px 0px;color:#105CB6;font-family:helvetica;'>
					<label for="captcha_answer">&nbsp;</label>&nbsp;&nbsp;NOTA: El código debe introducirse sin espacios.</br>
					<label for="captcha_answer">&nbsp;</label>&nbsp;&nbsp;l = ele minúsucla</br>
					<label for="captcha_answer">&nbsp;</label>&nbsp;&nbsp;I = i latina mayúscula</br>
					<label for="captcha_answer">&nbsp;</label>&nbsp;&nbsp;0 = cero</br>
					<label for="captcha_answer">&nbsp;</label>&nbsp;&nbsp;O = o mayúscula</br>
				</p>
			</div>			

			<c:if test="${configProperties['fwktd-csv-webapp.useCaptcha']}">
			<p>	
				<label for="captcha_answer">&nbsp;</label>		
				<img id="captcha" src='<spring:url value="action/captcha" />' />
				<br/>
				<label for="captcha">
					Introduzca el texto de la imagen:
				</label>
				<input type="text" id="captcha_answer" name="captcha_answer" class="small" />
			</p>
			</c:if>
			<!-- [eCenpri-Manu Ticket #280] - FIN ALSIGM3 Incluir texto aclaratorio Consulta de documentos con CSV -->
		</div>
	</form:form>

	<c:if test="${!empty infoDocumento}">
	<div class="infoDoc">
			<div class="twocolrightb cabecera">
			<div class="column first">
				<h4><spring:message code="label.document.title" /></h4>
			</div>
			<div class="column last textright">
				<p class="botones">
					<c:if test="${infoDocumento.disponible}">
						<c:if test="${!infoDocumento.firmaConJustificante}">
							<a href='<spring:url value="action/documento/download" />' title='<spring:message code="label.form.button.download"/>' class="descargar">
								<span>
									<spring:message code="label.form.button.download"/>
								</span>
							</a>
						</c:if>
										
						<c:if test="${infoDocumento.firmaConJustificante}">
							<a href='<spring:url value="action/documento/download" />' title='<spring:message code="label.form.button.downloadReceipt"/>' class="descargar">
								<span>
									<spring:message code="label.form.button.downloadReceipt"/>
								</span>
							</a>
							<!-- [dipucr-Felipe #1246] -->
							<br/>
							<a href='<spring:url value="action/documento/downloadOriginal" />' title='<spring:message code="label.form.button.downloadOriginal"/>' class="descargar">
								<span>
									<spring:message code="label.form.button.downloadOriginal"/>
								</span>
							</a>
						</c:if>
					</c:if>
				</p>
			</div>
		</div>

		<c:if test="${!infoDocumento.disponible}">
			<div id="errorMessage">
				<p><spring:message code="error.csv.download.notAvailable" /></p>
			</div>
		</c:if>

		<dl class="form">

			<dd>${infoDocumento.nombre}</dd>

			<c:if test="${not empty infoDocumento.descripcion}">
			<dt>
				<spring:message code="label.document.description" />
			</dt>
			<dd>${infoDocumento.descripcion}</dd>
			</c:if>

			<dt>
				<spring:message code="label.document.date" />
			</dt>
			<dd><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${infoDocumento.fechaCreacion}" /></dd>

			<c:if test="${!empty infoDocumento.fechaCaducidad}">
			<dt>
				<spring:message code="label.document.expiration_date" />
			</dt>
			<dd><fmt:formatDate pattern="yyyy-MM-dd" value="${infoDocumento.fechaCaducidad}" /></dd>
			</c:if>
			
			<!-- [Manu Ticket #625] CVE Consulta de documentos - Añadir campos para registros de salida -->
			
			<c:if test="${!empty infoDocumento.numeroRegistro}">
			<dt>
				<spring:message code="label.document.numero_registro" />
			</dt>
			<dd>${infoDocumento.numeroRegistro}</dd>
			
			<dt>
				<spring:message code="label.document.registro_date" />
			</dt>
			<dd><fmt:formatDate pattern="yyyy-MM-dd" value="${infoDocumento.fechaRegistro}" /></dd>
			
			<dt>
				<spring:message code="label.document.origen_registro" />
			</dt>
			<dd>${infoDocumento.origenRegistro}</dd>
			
			<dt>
				<spring:message code="label.document.destino_registro" />
			</dt>
			<dd>${infoDocumento.destinoRegistro}</dd>
			</c:if>
			
			<!-- [Manu Ticket #625] CVE Consulta de documentos - Añadir campos para registros de salida -->
		</dl>
	</div>
	</c:if>

	<div class="infoDoc">
		<!-- [Manu #65] + ALSIGM3 Añadir texto jurídico en formulario de búsqueda de documentos -->
			<p style='text-align:justify;display:block;padding:0;margin: 0px 0px 0px 0px;color:#105CB6;'>
				<spring:message code="label.textoLegal" />
			</p>
		<!-- [Manu #65] + ALSIGM3 Añadir texto jurídico en formulario de búsqueda de documentos -->
	</div>

<script type="text/javascript">
//<!--
	$(document).ready(function() {
		$("#csv").focus();
	});
//-->
</script>
