<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<script language="JavaScript" type="text/javascript">

	function downloadDocuments() {
		var data = checkboxElement(document.forms["defaultForm"].multibox);
		if (data != "") {
      		var oldTarget = document.forms["defaultForm"].target;
      		var oldAction = document.forms["defaultForm"].action;

			document.forms["defaultForm"].action = "downloadDocuments.do";
      		document.forms["defaultForm"].target = "workframe";
			document.forms["defaultForm"].submit();

      		document.forms["defaultForm"].target = oldTarget;
      		document.forms["defaultForm"].action = oldAction;
		} else {
			jAlert('<bean:message key="forms.listdoc.descargarDocumentos.empty"/>', '<bean:message key="common.alert"/>', '<bean:message key="common.message.ok"/>' , '<bean:message key="common.message.cancel"/>');
		}
	}

	function convertDocuments2PDF() {
		var data = checkboxElement(document.forms["defaultForm"].multibox);
		if (data != "") {
            var oldTarget = document.forms["defaultForm"].target;
            var oldAction = document.forms["defaultForm"].action;

			document.forms["defaultForm"].action = "convertDocuments2PDF.do";
            document.forms["defaultForm"].target = "workframe";
			showLayer("waitInProgress");
			document.forms["defaultForm"].submit();

            document.forms["defaultForm"].target = oldTarget;
            document.forms["defaultForm"].action = oldAction;
		} else {
			jAlert('<bean:message key="forms.listdoc.convertDocuments2PDF.empty"/>', '<bean:message key="common.alert"/>' , '<bean:message key="common.message.ok"/>' , '<bean:message key="common.message.cancel"/>');
		}
	}

	function desplegar(dropdownName) {
		document.getElementById(dropdownName).classList.toggle("show");

		for (i=1; i<=6; i++){//Cerramos los demás
			if (dropdownName != "menuDesplegable"+i){
		  		var myDropdown = document.getElementById("menuDesplegable"+i);
		    	if (myDropdown.classList.contains('show')) {
		       		myDropdown.classList.remove('show');
		    	}
			}
	  	}
	}

	// Close the dropdown if the user clicks outside of it
	window.onclick = function(e) {
		if (!e.target.matches('.dropbtn')) {
			for (i=1; i<=6; i++){
		  		var myDropdown = document.getElementById("menuDesplegable"+i);
		    	if (myDropdown.classList.contains('show')) {
		       		myDropdown.classList.remove('show');
		    	}
		  	}
	  	}
	}

</script>

<!-- INICIO [dipucr-Felipe #1246] Compatibilidad Firma 3 fases -->
<%@page import = "ieci.tdw.ispac.ispaclib.session.OrganizationUser"%>
<%@page import = "ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo"%>
<%@page import = "ieci.tdw.ispac.ispaclib.sign.SignConnectorFactory" %>
<% 
	OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
	String entityId = info.getOrganizationId();
	boolean bFirma3Fases = SignConnectorFactory.isDefaultImplClass(entityId);
	boolean bFirmaPortafirmas = SignConnectorFactory.isPortafirmasImplClass(entityId);
%>
<!-- FIN [dipucr-Felipe #1246] Compatibilidad Firma 3 fases -->

<!-- [dipucr-Felipe #1677] -->
<%@ page import = "es.dipucr.sigem.api.rule.procedures.Constants"%>

<table cellspacing="0" cellpadding="0" align="center" width="90%">

	<tr>
		<td height="10px"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="10px"/></td>
	</tr>
	<tr>
		<td class="textbar">

			<table cellspacing="0" cellpadding="0" border="0" width="100%">

				<tr>
					<td class="textbar">
						<br/>
						<bean:message key="forms.tasks.attached.documents"/>:
						<br/>
					</td>
				</tr>

			</table>

		</td>
	</tr>
	<tr>
		<td width="100%" valign="bottom" height="5px" style="font-size:4px;">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td valign="top">
		
			<!-- [dipucr-Felipe #1606] Barra de menú desplegable -->
			<div class="navbar">
				
				<!-- IMPORTANTE: Dado que van alineados a la derecha, ordenamos los menús inversamente de dcha a izda -->
				<div class="dropdown">
					<a class="dropbtn" onclick="desplegar('menuDesplegable6')">
						<bean:message key="menu.task.dipucr.download"/>
						<img src='<ispac:rewrite href="img/ul-v-blue.gif"/>' border="0" style="margin-bottom:2px"/>
					
						<div id="menuDesplegable6" class="dropdown-content">
							
							<a href="javascript:downloadDocuments();">
								<bean:message key="menu.task.dipucr.download.zip" />
							</a>
						
							<a href="javascript:convertDocuments2PDF();">
								<bean:message key="menu.task.dipucr.download.pdf" />
							</a>
						</div>
					</a>
				</div>
				
				<logic:equal value="false" property="readonly" name="defaultForm">
					
					<div class="dropdown">
						<a class="dropbtn" onclick="desplegar('menuDesplegable5')">
							<bean:message key="menu.task.dipucr.trasladar"/>
							<img src='<ispac:rewrite href="img/ul-v-blue.gif"/>' border="0" style="margin-bottom:2px"/>
						
							<div id="menuDesplegable5" class="dropdown-content">
									
								<ispac:linkframe
										id="TrasladarDocumento"
										target="workframe"
										action="sendTrasladoManual.do?method=showForm"
									    titleKey="menu.task.dipucr.trasladar.selected"
										showFrame="true"
										inputField="multibox"
										width="500"
										height="300"
										needToConfirm="true">
								</ispac:linkframe>
								
								<ispac:linkframe
										id="TrasladarDocumentoReferencia"
										target="workframe"
										action="sendTrasladoManual.do?method=showForm&referencia=true"
									    titleKey="menu.task.dipucr.trasladar.ref.selected"
										showFrame="true"
										inputField="multibox"
										width="500"
										height="300"
										needToConfirm="true">
								</ispac:linkframe>
							</div>
						</a>
					</div>
					
					<div class="dropdown">
						<a class="dropbtn" onclick="desplegar('menuDesplegable4')">
							<bean:message key="menu.task.dipucr.outputRegistry"/>
							<img src='<ispac:rewrite href="img/ul-v-blue.gif"/>' border="0" style="margin-bottom:2px"/>
						
							<div id="menuDesplegable4" class="dropdown-content">
								
								<ispac:linkframe
										id="RegistroAllMultiple"
										target="workframe"
										action="insertAllBatchOutputRegistry.do"
										titleKey="menu.task.dipucr.outputRegistry.all"
										showFrame="true"
 										inputField="multibox"
										width="500"
										height="300"
										needToConfirm="true">
								</ispac:linkframe>
								
								<ispac:linkframe
										id="RegistroMultiple"
										target="workframe"
										action="insertBatchOutputRegistry.do"
										titleKey="menu.task.dipucr.outputRegistry.selected"
										showFrame="true"
										inputField="multibox"
										width="500"
										height="300"
										needToConfirm="true">
								</ispac:linkframe>
							</div>
						</a>
					</div>
					
					<div class="dropdown">
						<a class="dropbtn" onclick="desplegar('menuDesplegable3')">
							<bean:message key="menu.task.dipucr.signs"/>
							<img src='<ispac:rewrite href="img/ul-v-blue.gif"/>' border="0" style="margin-bottom:2px"/>
							
							<div id="menuDesplegable3" class="dropdown-content">
								
								<ispac:linkframe id="GenerarDocumentoFirmarTodos"
										 target="workframe"
										 action="signAllDocument.do"
										 inputField="multibox"
										 titleKey="menu.task.dipucr.signs.all" 
										 showFrame="true"
										 width="640"
										 height="480"
										 needToConfirm="true">
								</ispac:linkframe>
								
								<ispac:linkframe
										id="FirmaMultiple"
										target="workframe"
										action="signDocuments.do"
										titleKey="menu.task.dipucr.signs.selected"
										showFrame="true"
										inputField="multibox"
										width="500"
										height="300"
										needToConfirm="true">
								</ispac:linkframe>
								
								<ispac:linkframe id="AnularTodosCircuitosFirma"
										 target="workframe"
										 action="anularAllSignCircuits.do" 
										 inputField="multibox"
										 titleKey="menu.task.dipucr.signs.anularCircuitoFirma" 
										 showFrame="true"
										 width="640"
										 height="480"
										 needToConfirm="true">
								</ispac:linkframe>
								
								<!-- [dipucr-Felipe #1706] -->
								<hr/>
								<ispac:linkframe
										id="FirmaExterna"
										target="workframe"
										action="signDocumentByExternals.do?method=showForm"
										titleKey="menu.task.dipucr.signs.external"
										showFrame="true"
										inputField="multibox"
										width="500"
										height="300"
										needToConfirm="true">
								</ispac:linkframe>
							</div>
						</a>
					</div>
					
					<div class="dropdown">
						<a class="dropbtn" onclick="desplegar('menuDesplegable2')">
							<bean:message key="menu.task.dipucr.delete.document"/>
							<img src='<ispac:rewrite href="img/ul-v-blue.gif"/>' border="0" style="margin-bottom:2px"/>
								
							<div id="menuDesplegable2" class="dropdown-content">
								
								<a href="javascript: deleteAllDocument();">
									<bean:message key="menu.task.dipucr.delete.document.all"/>
								</a>
								
								<a href="javascript: deleteDocument();">
									<bean:message key="menu.task.dipucr.delete.document.selected"/>
								</a>
							</div>
						</a>
					</div>
				
					<div class="dropdown">
						<a class="dropbtn" onclick="desplegar('menuDesplegable1')">
							<bean:message key="menu.task.dipucr.create.document"/>
							<img src='<ispac:rewrite href="img/ul-v-blue.gif"/>' border="0" style="margin-bottom:2px"/>
							
						<div id="menuDesplegable1" class="dropdown-content">
						
							<ispac:linkframe id="GenerarDocumentoPlantilla"
									target="workframe"
									action="selectTemplateDocumentType.do"
									titleKey="menu.task.dipucr.create.document.template"
									showFrame="true"
									inputField=""
									width="500"
									height="300"
									needToConfirm="true">
							</ispac:linkframe>
							
							<ispac:linkframe 	id="GenerarDocumentoAnexar"
									target="workframe"
									action="selectDocumentType.do"
									titleKey="menu.task.dipucr.create.document.attach"
									showFrame="true"
									inputField=""
									width="500"
									height="300"
									needToConfirm="true">
							</ispac:linkframe>
							
							<!-- INICIO [dipucr-Felipe #630] -->
							<ispac:linkframe 	id="GenerarDocumentosZipAnexar"
									target="workframe"
									action="selectDocumentType.do?action=attachFilesFromZip.do"
									titleKey="menu.task.dipucr.create.document.attachZip"
									showFrame="true"
									inputField=""
									width="500"
									height="300"
									needToConfirm="true">
							</ispac:linkframe>
							<!-- FIN [dipucr-Felipe #630] -->
							
							<ispac:linkframe 	id="GenerarDocumentoEscanear"
									target="workframe"
									action="selectDocumentType.do?action=scanFiles.do"
									titleKey="menu.task.dipucr.create.document.scan"
									showFrame="true"
									inputField=""
									width="500"
									height="300"
									needToConfirm="true">
							</ispac:linkframe>
							
							<ispac:linkframe id="GenerarDocumentoRepositorioComun"
 									 target="workframe" 
 									 action="selectDocumentType.do?action=uploadRepoComunFiles.do" 
 									 titleKey="forms.tasks.generate.document.from.repositoryComun" 
 									 showFrame="true" 
 									 inputField="" 
 									 width="640" 
 									 height="480" 
 									 needToConfirm="true"> 
 							</ispac:linkframe> 
							
<%-- 							<ispac:linkframe --%>
<%-- 									id="GenerarDocumentoPlantilla" --%>
<%-- 									target="workframe" --%>
<%-- 									action="selectTemplateDocumentType.do?selectParticipantes=1" --%>
<%-- 								    titleKey="menu.task.dipucr.create.document.bloque" --%>
<%-- 									showFrame="true" --%>
<%-- 									inputField="" --%>
<%-- 									width="500" --%>
<%-- 									height="300" --%>
<%-- 									needToConfirm="true"> --%>
<%-- 							</ispac:linkframe> --%>
							
							<ispac:linkframe
									id="AvanzadoAnexosBloque"
									target="workframe"
									action="selectDocumentType.do?action=attachAvanzadoAnexosBloque.do"
								    titleKey="menu.task.dipucr.create.document.avanzado"
									showFrame="true"
									inputField=""
									width="500"
									height="300"
									needToConfirm="true">
							</ispac:linkframe>
							
						</div>
					</div>
					
				</logic:equal>
			</div>

			<table style=" background-color:#FFFFFF;" class="caja" cellspacing="0" cellpadding="0" width="100%">

		        <tr>
					<td>

						<!-- displayTag con formateador -->
						<bean:define name="defaultForm" property="formatter" id="formatter" type="ieci.tdw.ispac.ispaclib.bean.BeanFormatter"/>

						<jsp:useBean id="checkboxDecorator" scope="page" class="ieci.tdw.ispac.ispacweb.decorators.CheckboxTableDecorator" />
						<jsp:setProperty name="checkboxDecorator" property="fieldName" value="multibox" />

						<display:table name="sessionScope.defaultForm.items"
									   id="document"
									   form="defaultForm"
									   excludedParams="*"
									   decorator="checkboxDecorator"
									   export='<%=formatter.getExport()%>'
							   		   class='<%=formatter.getStyleClass()%>'
									   sort='<%=formatter.getSort()%>'
									   pagesize='<%=formatter.getPageSize()%>'
									   defaultorder='<%=formatter.getDefaultOrder()%>'
									   defaultsort='<%=formatter.getDefaultSort()%>'
									   requestURI="/showTask.do">
							<!-- [Manu Ticket #109] - INICIO - ALSIGM3 Cambiar apariencia listado de documentos -->
							<logic:iterate name="defaultForm" property="formatter" id="format" type="ieci.tdw.ispac.ispaclib.bean.BeanPropertyFmt">

								<logic:equal name="format" property="fieldType" value="CHECKBOX">

									<jsp:setProperty name="checkboxDecorator" property="id" value='<%=format.getPropertyName()%>' />
							
									<display:column title='<%="<input type=\'checkbox\' onclick=\'javascript:_checkAll(document.defaultForm.multibox, this);\' name=\'allbox\'/>"%>'
												media='<%=format.getMedia()%>'
												headerClass='<%=format.getHeaderClass()%>'
												sortable='<%=format.getSortable()%>'
												sortProperty='<%=format.getPropertyName()%>'
												decorator='<%=format.getDecorator()%>'
												class='<%=format.getColumnClass()%>'
												comparator='<%=format.getComparator()%>'>
										<!-- INICIO [dipucr-Felipe #202] -->
										<!-- No mostrar el check para documentos bloqueados o registrados de salida -->
										<%-- Estado de Bloqueo del documento --%>
										<c:set var="_lockState">
											<bean:write name="document" property="property(BLOQUEO)" />
										</c:set>
										<%-- Se incluye check de seleccion para el documento, para su borrado, si esta desbloqueado--%>
										<c:if test="${(empty _lockState) || (_lockState != appConstants.documentLockStates.TOTAL_LOCK)}">
											<!-- Sólo mostramos el check si no tiene registro de salida -->
											<logic:empty name="document" property="property(NREG)" >
												<html:multibox property="multibox">
													<%=format.formatProperty(document)%>
												</html:multibox>
											</logic:empty>
										</c:if>
										<!-- FIN [dipucr-Felipe #202] -->
									</display:column>
							    </logic:equal>

								<logic:equal name="format" property="fieldType" value="LISTNOMBRE">

								  	<display:column titleKey='<%=format.getTitleKey()%>'
								  					media='<%=format.getMedia()%>'
								  					headerClass='<%=format.getHeaderClass()%>'
								  					sortable='<%=format.getSortable()%>'
								  					sortProperty='<%=format.getPropertyName()%>'
								  					decorator='<%=format.getDecorator()%>'
								  					class='<%=format.getColumnClass()%>'
								  					comparator='<%=format.getComparator()%>'>

										<c:set var="extension" value="unknown"/>
										<logic:notEmpty name="document" property="property(EXTENSION_RDE)">
											<c:set var="extension">
												<bean:write name="document" property="property(EXTENSION_RDE)" />
											</c:set>
										</logic:notEmpty>
										<logic:empty name="document" property="property(EXTENSION_RDE)">
											<logic:notEmpty name="document" property="property(EXTENSION)">
												<c:set var="extension">
													<bean:write name="document" property="property(EXTENSION)" />
												</c:set>
											</logic:notEmpty>
										</logic:empty>
										<bean:define id="extension" name="extension" type="java.lang.String"/>
										
										<ispac:documenticon imageSrc="img/docs/" extension='<%=extension%>' styleClass="imgTextBottom"/>
										
										<%=format.formatProperty(document)%>
										<% if (format.formatProperty(document).equals(Constants.TRAMITES_PERSONALIZADOS.TIPODOC.NOMBRE)){ %>
											<logic:notEmpty name="defaultForm" property="property(SPAC_DT_TRAMITES:NOMBRE_AUX)">
								  				(<bean:write name="defaultForm" property="property(SPAC_DT_TRAMITES:NOMBRE_AUX)"/>)
								  			</logic:notEmpty>
								  		<% } %>

								  	</display:column>

								 </logic:equal>

								<logic:equal name="format" property="fieldType" value="LIST">

									<display:column titleKey='<%=format.getTitleKey()%>'
													media='<%=format.getMedia()%>'
													headerClass='<%=format.getHeaderClass()%>'
													sortable='<%=format.getSortable()%>'
													sortProperty='<%=format.getPropertyName()%>'
													decorator='<%=format.getDecorator()%>'
													class='<%=format.getColumnClass()%>'
													comparator='<%=format.getComparator()%>'>

										<%=format.formatProperty(document)%>
										<logic:equal name="format" property="property" value="DESCRIPCION">
											<% if (format.formatProperty(document).equals(Constants.TRAMITES_PERSONALIZADOS.PLANTILLA.NOMBRE)){ %>
												<logic:notEmpty name="defaultForm" property="property(SPAC_DT_TRAMITES:NOMBRE_AUX)">
													(<bean:write name="defaultForm" property="property(SPAC_DT_TRAMITES:NOMBRE_AUX)"/>)
												</logic:notEmpty>
									  		<% } %>
										</logic:equal>

									</display:column>

								</logic:equal>								
								
								<logic:equal name="format" property="fieldType" value="FIRMA">

									<display:column titleKey='<%=format.getTitleKey()%>'
													media='<%=format.getMedia()%>'
													headerClass='<%=format.getHeaderClass()%>'
													sortable='<%=format.getSortable()%>'
													sortProperty='<%=format.getPropertyName()%>'
													decorator='<%=format.getDecorator()%>'
													class='<%=format.getColumnClass()%>'
													comparator='<%=format.getComparator()%>'>
										
										<c:set var="tipoRegistro" value="unknown"/>
										<logic:notEmpty name="document" property="property(TP_REG)">
											<c:set var="tipoRegistro">
												<bean:write name="document" property="property(TP_REG)" />
											</c:set>
										</logic:notEmpty>
										
										<c:set var="_data" value="unknown"/>
										<logic:notEmpty name="document" property="property(ESTADOFIRMA)">
											<c:set var="_data">
												<bean:write name="document" property="property(ESTADOFIRMA)" />
											</c:set>
										</logic:notEmpty>
										<c:choose>
											<c:when test="${_data == '00'}">Sin firma</c:when>
											<c:when test="${_data == '01'}">Pendiente Firma</c:when>
											<c:when test="${_data == '02'}"><font color="green"><b>Firmado</b></font></c:when>
											<c:when test="${_data == '03'}">Firmado con reparos</c:when>
											<c:when test="${_data == '04'}">Rechazado</c:when>
											<c:when test="${_data == '05'}">Pendiente circuito de firma</c:when>
											<c:when test="${_data == '06'}"><font color="green"><b>Sellado</b></font></c:when>
											<c:when test="${_data == '07'}"><font color="var(--rojodipucr)">Pendiente eventos portafirmas</font></c:when>
											<c:otherwise>
											    -
											</c:otherwise>
										</c:choose>					
										
									</display:column>

								</logic:equal>										
								
								
								<logic:equal name="format" property="fieldType" value="NOTIFICACION">

									<display:column titleKey='<%=format.getTitleKey()%>'
													media='<%=format.getMedia()%>'
													headerClass='<%=format.getHeaderClass()%>'
													sortable='<%=format.getSortable()%>'
													sortProperty='<%=format.getPropertyName()%>'
													decorator='<%=format.getDecorator()%>'
													class='<%=format.getColumnClass()%>'
													comparator='<%=format.getComparator()%>'>
										
										<c:set var="_data" value="unknown"/>
										<logic:notEmpty name="document" property="property(ESTADONOTIFICACION)">
											<c:set var="_data">
												<bean:write name="document" property="property(ESTADONOTIFICACION)" />
											</c:set>
										</logic:notEmpty>
										<c:choose>
											<c:when test="${_data == 'PE'}"><font color="blue"><b>[Notifica] Pendiente de comparecencia</b></font></c:when>
											<c:when test="${_data == 'PR'}"><b>Envío postal</b></c:when>
											<c:when test="${_data == 'OK'}"><font color="green"><b>[Notifica] Notificada</b></font></c:when>
											<c:when test="${_data == 'CA'}"><font color="var(--rojodipucr)"><b>[Notifica] Caducada</b></c:when>
											<c:when test="${_data == 'RE'}"><font color="var(--rojodipucr)"><b>[Notifica] Rechazada</b></c:when>
											<c:when test="${_data == 'ER'}"><font color="var(--rojodipucr)"><b>[Notifica] Error en el envío a Notifica</b></font></c:when>
											<c:when test="${_data == 'CO'}"><font color="blue"><b>[Notifica] Pendiente de comparecencia</b></font></c:when>
											<c:when test="${_data == 'NT'}"><font color="blue"><b>[Notifica] Pendiente de comparecencia</b></font></c:when>
											<c:when test="${_data == 'NTPENSEDE'}"><font color="blue"><b>[Notifica] Pendiente en DEHU</b></font></c:when>
											<c:when test="${_data == 'NTAUSENT'}"><font color="var(--rojodipucr)"><b>[Notifica] Ausente</b></font></c:when>
											<c:when test="${_data == 'NTDESCON'}"><font color="var(--rojodipucr)"><b>[Notifica] Desconocido</b></font></c:when>
											<c:when test="${_data == 'NTDIERRO'}"><font color="var(--rojodipucr)"><b>[Notifica] Dirección incorrecta</b></font></c:when>
											<c:when test="${_data == 'NTENVIMP'}"><font color="var(--rojodipucr)"><b>[Notifica] Enviado al centro de impresión</b></font></c:when>
											<c:when test="${_data == 'NTENVDEH'}"><font color="blue"><b>[Notifica] Enviado a la DEH</b></font></c:when>
											<c:when test="${_data == 'NTILEIDA'}"><font color="#013220"><b>[Notifica] Leida</b></font></c:when>
											<c:when test="${_data == 'NTIERROR'}"><font color="var(--rojodipucr)"><b>[Notifica] Error en el envío a Notifica</b></font></c:when>
											<c:when test="${_data == 'NTEXTRAV'}"><font color="var(--rojodipucr)"><b>[Notifica] Extraviada</b></font></c:when>
											<c:when test="${_data == 'NTFALLEC'}"><font color="var(--rojodipucr)"><b>[Notifica] Fallecido</b></font></c:when>
											<c:when test="${_data == 'NTIFCAOK'}"><font color="green"><b>[Notifica] Notificada</b></font></c:when>
											<c:when test="${_data == 'NTPENENV'}"><font color="var(--rojodipucr)"><b>[Notifica] Caducada, envío postal</b></font></c:when>
											<c:when test="${_data == 'NTPENCOM'}"><font color="blue"><b>[Notifica] Pendiente de comparecencia</b></font></c:when>
											<c:when test="${_data == 'NTREHUSA'}"><font color="#013220"><b>[Notifica] Rehusada</b></font></c:when>
											<c:when test="${_data == 'NTEXPIRADA'}"><font color="#013220"><b>[Notifica] Expirada</b></font></c:when>
											<c:when test="${_data == 'NTPENPRO'}"><font color="blue"><b>[Notifica] Envío programado</b></font></c:when>
											<c:when test="${_data == 'NTSININF'}"><font color="var(--rojodipucr)"><b>[Notifica] Sin información</b></font></c:when>
											<c:when test="${_data == 'NTPOSTAL'}"><font color="var(--rojodipucr)"><b>[Notifica] Envío postal</b></font></c:when>
											<c:when test="${_data == 'NTCADUCA'}"><font color="var(--rojodipucr)"><b>[Notifica] Caducada</b></font></c:when>
											<c:otherwise>
											    -
											</c:otherwise>
										</c:choose>	

									</display:column>

								</logic:equal>
								
												
								<logic:equal name="format" property="fieldType" value="DESTINOTIPO">

									<display:column titleKey='<%=format.getTitleKey()%>'
													media='<%=format.getMedia()%>'
													headerClass='<%=format.getHeaderClass()%>'
													sortable='<%=format.getSortable()%>'
													sortProperty='<%=format.getPropertyName()%>'
													decorator='<%=format.getDecorator()%>'
													class='<%=format.getColumnClass()%>'
													comparator='<%=format.getComparator()%>'>
										
										<c:set var="tipoDestino" value="unknown"/>
										<logic:notEmpty name="document" property="property(DESTINO_TIPO)">
											<c:set var="tipoDestino">
												<bean:write name="document" property="property(DESTINO_TIPO)" />
											</c:set>
										</logic:notEmpty>
										<c:choose>
											<c:when test="${tipoDestino == 'F'}"><font color="blue"><b>Física</b></></c:when>
											<c:when test="${tipoDestino == 'J'}"><font color="green"><b>Jurídica</b></c:when>
											<c:otherwise>
											    -
											</c:otherwise>
										</c:choose>	

									</display:column>

								</logic:equal>

								<!-- INICIO [dipucr-Felipe #1805] Visualización de originales de documentos -->
								<logic:equal name="format" property="fieldType" value="SHOWTRASLADAR">
									<display:column	titleKey='<%=format.getTitleKey()%>'
													media='<%=format.getMedia()%>'
													headerClass='<%=format.getHeaderClass()%>'
													sortable='<%=format.getSortable()%>'
													sortProperty='<%=format.getPropertyName()%>'
													decorator='<%=format.getDecorator()%>'
													class='<%=format.getColumnClass()%>'
													comparator='<%=format.getComparator()%>'>

											<bean:define id="_link" value='<%=format.getUrl()%>'></bean:define>
											<c:url value="${_link}" var="link">
												<c:param name="document" value="${document.keyProperty}"/>
											</c:url>
											
											<c:set var="estadofirma" value="unknown"/>
											<logic:notEmpty name="document" property="property(ESTADOFIRMA)">
												<c:set var="estadofirma">
													<bean:write name="document" property="property(ESTADOFIRMA)" />
												</c:set>
											</logic:notEmpty>
											<bean:define id="estadofirma" name="estadofirma" type="java.lang.String"/>										
											
											<c:if test="${estadofirma == '02'}">
												<bean:define id="_link" value='sendTrasladoManual.do'></bean:define>
												<c:url value="${_link}" var="link">
													<c:param name="method" value="showForm"/>
													<c:param name="document" value="${document.keyProperty}"/>
												</c:url>
	
												<a href='javascript:showFrame("workframe", "<c:out value="${link}"/>");' class='<%=format.getStyleClass()%> tooltip' title='<%=format.getTooltipTitle()%>' >
													<img src='<ispac:rewrite href="img/envelope.gif"/>'/>								
												</a>
											</c:if>
									</display:column>
								</logic:equal>
								<!-- FIN [dipucr-Felipe #970] -->
								
								<!-- INICIO [dipucr-Felipe #970] Visualización de originales de documentos -->
								<logic:equal name="format" property="fieldType" value="SHOWORIGINALDOC">
									<display:column	titleKey='<%=format.getTitleKey()%>'
													media='<%=format.getMedia()%>'
													headerClass='<%=format.getHeaderClass()%>'
													sortable='<%=format.getSortable()%>'
													sortProperty='<%=format.getPropertyName()%>'
													decorator='<%=format.getDecorator()%>'
													class='<%=format.getColumnClass()%>'
													comparator='<%=format.getComparator()%>'>

											<c:set var="extension" value="unknown"/>
											<logic:notEmpty name="document" property="property(EXTENSION)">
												<c:set var="extension">
													<bean:write name="document" property="property(EXTENSION)" />
												</c:set>
											</logic:notEmpty>
											<bean:define id="extension" name="extension" type="java.lang.String"/>
											<bean:define id="_link" value='<%=format.getUrl()%>'></bean:define>
											<c:url value="${_link}" var="link">
												<c:param name="document" value="${document.keyProperty}"/>
											</c:url>
											
											<c:set var="estadofirma" value="unknown"/>
											<logic:notEmpty name="document" property="property(ESTADOFIRMA)">
												<c:set var="estadofirma">
													<bean:write name="document" property="property(ESTADOFIRMA)" />
												</c:set>
											</logic:notEmpty>
											<bean:define id="estadofirma" name="estadofirma" type="java.lang.String"/>										
											
											<c:if test="${(estadofirma == '02' || estadofirma == '03') && (extension == 'doc' || extension == 'ppt' || extension == 'docx' || extension == 'xslx' || extension == 'pptx' || extension == 'odt' || extension == 'ods' || extension == 'DOC' || extension == 'PPT' || extension == 'DOCX' || extension == 'XSLX' || extension == 'PPTX' || extension == 'ODT' || extension == 'ODS')}">
												<bean:define id="_link" value='showDocument.do'></bean:define>
												<c:url value="${_link}" var="link">
													<c:param name="document" value="${document.keyProperty}"/>
													<c:param name="original" value="true"/>
												</c:url>
	
												<a href='<c:out value="${link}"/>' class='<%=format.getStyleClass()%> tooltip' title='<%=format.getTooltipTitle()%>'>
													<img src='<ispac:rewrite href="img/doc_odt.gif"/>'/>								
												</a>
											</c:if>
									</display:column>
								</logic:equal>
								<!-- FIN [dipucr-Felipe #970] -->
																
								<logic:equal name="format" property="fieldType" value="SHOWPROPERTIES">

								  	<display:column titleKey='<%=format.getTitleKey()%>'
								  					media='<%=format.getMedia()%>'
								  					headerClass='<%=format.getHeaderClass()%>'
								  					sortable='<%=format.getSortable()%>'
								  					sortProperty='<%=format.getPropertyName()%>'
								  					decorator='<%=format.getDecorator()%>'
								  					class='<%=format.getColumnClass()%>'
								  					comparator='<%=format.getComparator()%>'>

										<bean:define id="_link" value='<%=format.getUrl()%>'></bean:define>
										<c:url value="${_link}" var="link">
											<c:if test="${!empty param.taskId}">
												<c:param name="taskId" value='${param.taskId}'/>
											</c:if>
											<c:param name="key" value="${document.keyProperty}"/>
											<c:param name="entity" value="2"/>
											<c:if test="${!empty param.readonly}">
												<c:param name="readonly" value='${param.readonly}'/>
											</c:if>
										</c:url>

										<a href='<c:out value="${link}"/>' class='<%=format.getStyleClass()%> tooltip' title='<%=format.getTooltipTitle()%>'>
											<img src='<ispac:rewrite href="img/info_doc.gif"/>'/>								
										</a>
								  	</display:column>
								</logic:equal>
								
								<!-- INICIO [dipucr-Felipe #1246] -->
								<logic:equal name="format" property="fieldType" value="SHOWDOCUMENTPORTAFIRMAS">

									<% if(bFirmaPortafirmas){ %>
										<display:column	titleKey='<%=format.getTitleKey()%>'
													media='<%=format.getMedia()%>'
													headerClass='<%=format.getHeaderClass()%>'
													sortable='<%=format.getSortable()%>'
													sortProperty='<%=format.getPropertyName()%>'
													decorator='<%=format.getDecorator()%>'
													class='<%=format.getColumnClass()%>'
													comparator='<%=format.getComparator()%>'>
										
											<logic:notEmpty name="document" property='<%= format.getPropertyName() %>'>
											
												<logic:notEmpty name="document" property="property(INFOPAG_RDE_ORIGINAL)">
	
													<bean:define id="_link" value='showDocument.do'></bean:define>
													<c:url value="${_link}" var="link">
														<c:param name="document" value="${document.keyProperty}"/>
														<c:param name="portafirmas" value="true"/>
													</c:url>
			
													<a target="_blank" href='<c:out value="${link}"/>' class='tooltip' title='Ver documento con firmas originales'>
														<img src='<ispac:rewrite href="img/buscar_go.gif"/>'/>
											  		</a>
												</logic:notEmpty>
		
											</logic:notEmpty>

										</display:column>
									<% } else{ %>
										<display:column	titleKey='<%=format.getTitleKey()%>'
													media='<%=format.getMedia()%>'
													headerClass='<%=format.getHeaderClass()%>'
													sortable='<%=format.getSortable()%>'
													sortProperty='<%=format.getPropertyName()%>'
													decorator='<%=format.getDecorator()%>'
													class='<%=format.getColumnClass()%>'
													comparator='<%=format.getComparator()%>'>
										
											<c:set var="num_acto" value="unknown"/>
											<logic:notEmpty name="document" property="property(NUM_ACTO)">
												<c:set var="num_acto">
													<bean:write name="document" property="property(NUM_ACTO)" />
												</c:set>
											</logic:notEmpty>
											<bean:define id="num_acto" name="num_acto" type="java.lang.String"/>
	
											<c:if test="${num_acto == 'PORTAFIRMAS'}">
											
												<logic:notEmpty name="document" property='<%= format.getPropertyName() %>'>
		
													<bean:define id="_link" value='showDocumentPortafirmas.do'></bean:define>
													<c:url value="${_link}" var="link">
														<c:param name="document" value="${document.keyProperty}"/>
													</c:url>
			
													<a target="_blank" href='<c:out value="${link}"/>' class='tooltip' title='Visualizar firmas'>
														<img src='<ispac:rewrite href="img/portafirmas.png"/>'/>
											  		</a>
		
												</logic:notEmpty>
											</c:if>
	
										</display:column>
									<% } %>
									
									
								</logic:equal>
								<!-- FIN [dipucr-Felipe #1246] -->
								
								<logic:equal name="format" property="fieldType" value="EDITDOCUMENT">
									<logic:equal value="false" property="readonly" name="defaultForm">
										<display:column	titleKey='<%=format.getTitleKey()%>'
														media='<%=format.getMedia()%>'
														headerClass='<%=format.getHeaderClass()%>'
														sortable='<%=format.getSortable()%>'
														sortProperty='<%=format.getPropertyName()%>'
														decorator='<%=format.getDecorator()%>'
														class='<%=format.getColumnClass()%>'
														comparator='<%=format.getComparator()%>'>
	
												<c:set var="extension" value="unknown"/>
												<logic:notEmpty name="document" property="property(EXTENSION_RDE)">
													<c:set var="extension">
														<bean:write name="document" property="property(EXTENSION_RDE)" />
													</c:set>
												</logic:notEmpty>
												<logic:empty name="document" property="property(EXTENSION_RDE)">
													<logic:notEmpty name="document" property="property(EXTENSION)">
														<c:set var="extension">
															<bean:write name="document" property="property(EXTENSION)" />
														</c:set>
													</logic:notEmpty>
												</logic:empty>
												<bean:define id="extension" name="extension" type="java.lang.String"/>
												<bean:define id="_link" value='<%=format.getUrl()%>'></bean:define>
												<c:url value="${_link}" var="link">
													<c:param name="document" value="${document.keyProperty}"/>
													<c:if test="${!empty param.readonly}">
														<c:param name="readonly" value='${param.readonly}'/>
													</c:if>
												</c:url>
												
												<c:set var="tipoRegistro" value="unknown"/>
												<logic:notEmpty name="document" property="property(TP_REG)">
													<c:set var="tipoRegistro">
														<bean:write name="document" property="property(TP_REG)" />
													</c:set>
												</logic:notEmpty>
												
												<c:set var="estadofirma" value="unknown"/>
												<logic:notEmpty name="document" property="property(ESTADOFIRMA)">
													<c:set var="estadofirma">
														<bean:write name="document" property="property(ESTADOFIRMA)" />
													</c:set>
												</logic:notEmpty>
												<bean:define id="estadofirma" name="estadofirma" type="java.lang.String"/>										
												
												<c:if test="${ tipoRegistro != 'ENTRADA' && (extension == 'doc' || extension == 'ppt' || extension == 'docx' || extension == 'xslx' || extension == 'pptx' || extension == 'odt' || extension == 'ods' || extension == 'DOC' || extension == 'PPT' || extension == 'DOCX' || extension == 'XSLX' || extension == 'PPTX' || extension == 'ODT' || extension == 'ODS') && estadofirma == '00'}">
													<a class="tooltip" href='<c:out value="${link}"/>' class='<%=format.getStyleClass()%>' title='<%=format.getTooltipTitle()%>' target='workframe_document'> 
														<img src='<ispac:rewrite href="img/editDoc.gif"/>'/>
													</a>
												</c:if>
												<c:if test="${tipoRegistro == 'ENTRADA' || not (extension == 'doc' || extension == 'ppt' || extension == 'docx' || extension == 'xslx' || extension == 'pptx' || extension == 'odt' || extension == 'ods' || extension == 'DOC' || extension == 'PPT' || extension == 'DOCX' || extension == 'XSLX' || extension == 'PPTX' || extension == 'ODT' || extension == 'ODS') || estadofirma != '00'}">
													<bean:define id="_link" value='showDocument.do'></bean:define>
													<c:url value="${_link}" var="link">
														<c:param name="document" value="${document.keyProperty}"/>
													</c:url>
		
													<a target="_blank" href='<c:out value="${link}"/>' class='tooltip' title='Ver documento'>
														<!-- [dipucr-Felipe #1246] -->
														<logic:empty name="document" property="property(INFOPAG_RDE_ORIGINAL)">
															<img class="tooltip" src='<ispac:rewrite href="img/viewDoc.gif"/>' title='Documento no editable Online'/>
														</logic:empty>
														<logic:notEmpty name="document" property="property(INFOPAG_RDE_ORIGINAL)">
															<img class="tooltip" src='<ispac:rewrite href="img/viewDoc.gif"/>' title='Ver Justificante de firma'/>
														</logic:notEmpty>
											  		</a>
												</c:if>
										</display:column>
									</logic:equal>
									<logic:equal value="true" property="readonly" name="defaultForm">
										<display:column	titleKey='<%=format.getTitleKey()%>'
														media='<%=format.getMedia()%>'
														headerClass='<%=format.getHeaderClass()%>'
														sortable='<%=format.getSortable()%>'
														sortProperty='<%=format.getPropertyName()%>'
														decorator='<%=format.getDecorator()%>'
														class='<%=format.getColumnClass()%>'
														comparator='<%=format.getComparator()%>'>
	
												<bean:define id="_link" value='<%=format.getUrl()%>'></bean:define>
												<c:url value="${_link}" var="link">
													<c:param name="document" value="${document.keyProperty}"/>
													<c:if test="${!empty param.readonly}">
														<c:param name="readonly" value='${param.readonly}'/>
													</c:if>
												</c:url>
												
												<bean:define id="_link" value='showDocument.do'></bean:define>
												<c:url value="${_link}" var="link">
													<c:param name="document" value="${document.keyProperty}"/>
												</c:url>
	
												<a target="_blank" href='<c:out value="${link}"/>' class='tooltip' title='Ver documento'>
													<!-- [dipucr-Felipe #1246] -->
													<logic:empty name="document" property="property(INFOPAG_RDE_ORIGINAL)">
														<img class="tooltip" src='<ispac:rewrite href="img/viewDoc.gif"/>' title='Documento no editable Online'/>
													</logic:empty>
													<logic:notEmpty name="document" property="property(INFOPAG_RDE_ORIGINAL)">
														<img class="tooltip" src='<ispac:rewrite href="img/viewDoc.gif"/>' title='Ver Justificante de firma'/>
													</logic:notEmpty>
										  		</a>
										</display:column>
									</logic:equal>
								</logic:equal>
								<!-- 
								<logic:equal name="format" property="fieldType" value="SHOWDOCUMENT">

									<display:column	titleKey='<%=format.getTitleKey()%>'
													media='<%=format.getMedia()%>'
													headerClass='<%=format.getHeaderClass()%>'
													sortable='<%=format.getSortable()%>'
													sortProperty='<%=format.getPropertyName()%>'
													decorator='<%=format.getDecorator()%>'
													class='<%=format.getColumnClass()%>'
													comparator='<%=format.getComparator()%>'>

										<logic:notEmpty name="document" property='<%= format.getPropertyName() %>'>

											<bean:define id="_link" value='showDocument.do'></bean:define>
											<c:url value="${_link}" var="link">
												<c:param name="document" value="${document.keyProperty}"/>
											</c:url>
	
											<a target="_blank" href='<c:out value="${link}"/>' class='tooltip' title='Ver documento'>
												<img src='<ispac:rewrite href="img/viewDoc.gif"/>'/>
									  		</a>

										</logic:notEmpty>

									</display:column>

								</logic:equal>
								-->
								
								<!-- INICIO [dipucr-Felipe #1246] -->
								<!-- ---------------------------- -->
<%-- 								<logic:equal name="format" property="fieldType" value="SHOWDOCUMENTPORTAFIRMAS"> --%>

<%-- 									<display:column	titleKey='<%=format.getTitleKey()%>' --%>
<%-- 													media='<%=format.getMedia()%>' --%>
<%-- 													headerClass='<%=format.getHeaderClass()%>' --%>
<%-- 													sortable='<%=format.getSortable()%>' --%>
<%-- 													sortProperty='<%=format.getPropertyName()%>' --%>
<%-- 													decorator='<%=format.getDecorator()%>' --%>
<%-- 													class='<%=format.getColumnClass()%>' --%>
<%-- 													comparator='<%=format.getComparator()%>'> --%>
										
<%-- 										<c:set var="num_acto" value="unknown"/> --%>
<%-- 										<logic:notEmpty name="document" property="property(NUM_ACTO)"> --%>
<%-- 											<c:set var="num_acto"> --%>
<%-- 												<bean:write name="document" property="property(NUM_ACTO)" /> --%>
<%-- 											</c:set> --%>
<%-- 										</logic:notEmpty> --%>
<%-- 										<bean:define id="num_acto" name="num_acto" type="java.lang.String"/> --%>

<%-- 										<c:if test="${num_acto == 'PORTAFIRMAS'}"> --%>
										
<%-- 											<logic:notEmpty name="document" property='<%= format.getPropertyName() %>'> --%>
	
<%-- 												<bean:define id="_link" value='showDocumentPortafirmas.do'></bean:define> --%>
<%-- 												<c:url value="${_link}" var="link"> --%>
<%-- 													<c:param name="document" value="${document.keyProperty}"/> --%>
<%-- 												</c:url> --%>
		
<%-- 												<a target="_blank" href='<c:out value="${link}"/>' class='tooltip' title='Visualizar firmas'> --%>
<%-- 													<img src='<ispac:rewrite href="img/portafirmas.png"/>'/> --%>
<!-- 										  		</a> -->
	
<%-- 											</logic:notEmpty> --%>
<%-- 										</c:if> --%>

<%-- 									</display:column> --%>
<%-- 								</logic:equal> --%>
								<!-- ------------------------- -->
								<!-- FIN [dipucr-Felipe #1246] -->
								
								
								<logic:equal name="format" property="fieldType" value="SIGNDOCUMENT">
									<logic:equal value="false" property="readonly" name="defaultForm">
										<display:column	titleKey='<%=format.getTitleKey()%>'
														media='<%=format.getMedia()%>'
														headerClass='<%=format.getHeaderClass()%>'
														sortable='<%=format.getSortable()%>'
														sortProperty='<%=format.getPropertyName()%>'
														decorator='<%=format.getDecorator()%>'
														class='<%=format.getColumnClass()%>'
														comparator='<%=format.getComparator()%>'>
	
												<c:set var="extension" value="unknown"/>
												<logic:notEmpty name="document" property="property(EXTENSION_RDE)">
													<c:set var="extension">
														<bean:write name="document" property="property(EXTENSION_RDE)" />
													</c:set>
												</logic:notEmpty>
												<logic:empty name="document" property="property(EXTENSION_RDE)">
													<logic:notEmpty name="document" property="property(EXTENSION)">
														<c:set var="extension">
															<bean:write name="document" property="property(EXTENSION)" />
														</c:set>
													</logic:notEmpty>
												</logic:empty>
												<bean:define id="extension" name="extension" type="java.lang.String"/>
												<bean:define id="_link" value='<%=format.getUrl()%>'></bean:define>
												<c:url value="${_link}" var="link">
													<c:param name="document" value="${document.keyProperty}"/>
													<c:if test="${!empty param.readonly}">
														<c:param name="readonly" value='${param.readonly}'/>
													</c:if>
												</c:url>
												
												<c:set var="estadofirma" value="unknown"/>
												<logic:notEmpty name="document" property="property(ESTADOFIRMA)">
													<c:set var="estadofirma">
														<bean:write name="document" property="property(ESTADOFIRMA)" />
													</c:set>
												</logic:notEmpty>
												<bean:define id="estadofirma" name="estadofirma" type="java.lang.String"/>										
												
												<c:if test="${estadofirma == '00'}">
													<c:url value="${_link}" var="link">
														<c:param name="method" value="selectOption"/>
														<c:param name="parameters" value="workframe"/>
														<c:param name="field" value="${document.keyProperty}"/>
													</c:url>
																					
												</c:if>
												<c:if test="${estadofirma == '04'}">
													<img src='<ispac:rewrite href="img/rechazoFirma.gif"/>' title='Firma Rechazada'/>
												</c:if>
												<c:if test="${estadofirma == '01' || estadofirma == '05'}">
													<a class="tooltip" href="javascript:showFrame('workframe','showSignDetail.do?parameters=workframe&document=<bean:write name="document" property="property(ID)" />',640,480,'',true);" title='<%=format.getTooltipTitle()%>' onclick='javascript:seleccionarDoc(document.defaultForm.multibox, <c:out value="${document.keyProperty}"/>); document.getElementsByName("property(SPAC_DT_DOCUMENTOS:ID)")[0].value=<c:out value="${document.keyProperty}"/>;'>
														<img class="tooltip" src='<ispac:rewrite href="img/detalleFirma.gif"/>' title='Detalles de firma'/>
													</a>
												</c:if>
												<c:if test="${estadofirma == '02' || estadofirma == '03'}">
													<a class="tooltip" href="javascript:showFrame('workframe','showSignDetail.do?parameters=workframe&document=<bean:write name="document" property="property(ID)" />',640,480,'',true);" title='<%=format.getTooltipTitle()%>' onclick='javascript:seleccionarDoc(document.defaultForm.multibox, <c:out value="${document.keyProperty}"/>); document.getElementsByName("property(SPAC_DT_DOCUMENTOS:ID)")[0].value=<c:out value="${document.keyProperty}"/>;'>
														<img class="tooltip" src='<ispac:rewrite href="img/docFirmado.gif"/>' title='Documento firmado'/>
													</a>
												</c:if>
												<c:if test="${estadofirma == '00'}">
													<% if(bFirma3Fases){ %>
														<a class="tooltip" href="javascript:showFrame('workframe','signDocuments3Fases.do?field=multibox/>&parameters=workframe',640,480,'',true);" title='<%=format.getTooltipTitle()%>' onclick='javascript:seleccionarDoc(document.defaultForm.multibox, <c:out value="${document.keyProperty}"/>);'> 
															<img class="tooltip" src='<ispac:rewrite href="img/firmarDoc.gif"/>' title='Firmar Documento'/>
														</a>
													<% } else { %>
														<a class="tooltip" href="javascript:showFrame('workframe','<c:out value="${_link}"/>?field=multibox/>&parameters=workframe',640,480,'',true);" title='<%=format.getTooltipTitle()%>' onclick='javascript:seleccionarDoc(document.defaultForm.multibox, <c:out value="${document.keyProperty}"/>);'> 
															<img class="tooltip" src='<ispac:rewrite href="img/firmarDoc.gif"/>' title='Firmar Documento'/>
														</a>
													<% } %>
												</c:if>
												<c:if test="${estadofirma == '07'}">
													<a class="tooltip" href="javascript:showFrame('workframe','showSignDetail.do?parameters=workframe&document=<bean:write name="document" property="property(ID)" />',640,480,'',true);" title='<%=format.getTooltipTitle()%>' onclick='javascript:seleccionarDoc(document.defaultForm.multibox, <c:out value="${document.keyProperty}"/>); document.getElementsByName("property(SPAC_DT_DOCUMENTOS:ID)")[0].value=<c:out value="${document.keyProperty}"/>;'>
														<img src='<ispac:rewrite href="img/document3.gif"/>' title='Pendiente eventos portafirmas'/>
													</a>
												</c:if>
												<script>											
													function seleccionarDoc(field, valor){
														document.defaultForm.allbox.checked=false;
														for (i = 0; i < field.length; i++){
															if(field[i].value == valor){
																field[i].checked = true;
															}
															else{
																field[i].checked = false;
															}
														}
														field.checked = true;
													}
												</script>
										</display:column>
									</logic:equal>
									<logic:equal value="true" property="readonly" name="defaultForm">
										<display:column	titleKey='<%=format.getTitleKey()%>'
														media='<%=format.getMedia()%>'
														headerClass='<%=format.getHeaderClass()%>'
														sortable='<%=format.getSortable()%>'
														sortProperty='<%=format.getPropertyName()%>'
														decorator='<%=format.getDecorator()%>'
														class='<%=format.getColumnClass()%>'
														comparator='<%=format.getComparator()%>'>
	
												<c:set var="extension" value="unknown"/>
												<logic:notEmpty name="document" property="property(EXTENSION_RDE)">
													<c:set var="extension">
														<bean:write name="document" property="property(EXTENSION_RDE)" />
													</c:set>
												</logic:notEmpty>
												<logic:empty name="document" property="property(EXTENSION_RDE)">
													<logic:notEmpty name="document" property="property(EXTENSION)">
														<c:set var="extension">
															<bean:write name="document" property="property(EXTENSION)" />
														</c:set>
													</logic:notEmpty>
												</logic:empty>
												<bean:define id="extension" name="extension" type="java.lang.String"/>
												<bean:define id="_link" value='<%=format.getUrl()%>'></bean:define>
												<c:url value="${_link}" var="link">
													<c:param name="document" value="${document.keyProperty}"/>
													<c:if test="${!empty param.readonly}">
														<c:param name="readonly" value='${param.readonly}'/>
													</c:if>
												</c:url>
												
												<c:set var="estadofirma" value="unknown"/>
												<logic:notEmpty name="document" property="property(ESTADOFIRMA)">
													<c:set var="estadofirma">
														<bean:write name="document" property="property(ESTADOFIRMA)" />
													</c:set>
												</logic:notEmpty>
												<bean:define id="estadofirma" name="estadofirma" type="java.lang.String"/>										
												
												<c:if test="${estadofirma == '00'}">
													<c:url value="${_link}" var="link">
														<c:param name="method" value="selectOption"/>
														<c:param name="parameters" value="workframe"/>
														<c:param name="field" value="${document.keyProperty}"/>
													</c:url>
																					
												</c:if>
												<c:if test="${estadofirma == '04'}">
													<img src='<ispac:rewrite href="img/rechazoFirma.gif"/>' title='Firma Rechazada'/>
												</c:if>
												<c:if test="${estadofirma == '01' || estadofirma == '05'}">
													<a class="tooltip" href="javascript:showFrame('workframe','showSignDetail.do?parameters=workframe&document=<bean:write name="document" property="property(ID)" />',640,480,'',true);" title='<%=format.getTooltipTitle()%>' onclick='javascript:seleccionarDoc(document.defaultForm.multibox, <c:out value="${document.keyProperty}"/>); document.getElementsByName("property(SPAC_DT_DOCUMENTOS:ID)")[0].value=<c:out value="${document.keyProperty}"/>;'>
														<img class="tooltip" src='<ispac:rewrite href="img/detalleFirma.gif"/>' title='Detalles de firma'/>
													</a>
												</c:if>
												<c:if test="${estadofirma == '02' || estadofirma == '03'}">
													<a class="tooltip" href="javascript:showFrame('workframe','showSignDetail.do?parameters=workframe&document=<bean:write name="document" property="property(ID)" />',640,480,'',true);" title='<%=format.getTooltipTitle()%>' onclick='javascript:seleccionarDoc(document.defaultForm.multibox, <c:out value="${document.keyProperty}"/>); document.getElementsByName("property(SPAC_DT_DOCUMENTOS:ID)")[0].value=<c:out value="${document.keyProperty}"/>;'>
														<img class="tooltip" src='<ispac:rewrite href="img/docFirmado.gif"/>' title='Documento firmado'/>
													</a>
												</c:if>
												<c:if test="${estadofirma == '07'}">
													<a class="tooltip" href="javascript:showFrame('workframe','showSignDetail.do?parameters=workframe&document=<bean:write name="document" property="property(ID)" />',640,480,'',true);" title='<%=format.getTooltipTitle()%>' onclick='javascript:seleccionarDoc(document.defaultForm.multibox, <c:out value="${document.keyProperty}"/>); document.getElementsByName("property(SPAC_DT_DOCUMENTOS:ID)")[0].value=<c:out value="${document.keyProperty}"/>;'>
														<img src='<ispac:rewrite href="img/document3.gif"/>' title='Pendiente eventos portafirmas'/>
													</a>
												</c:if>
												<script>											
													function seleccionarDoc(field, valor){
														document.defaultForm.allbox.checked=false;
														for (i = 0; i < field.length; i++){
															if(field[i].value == valor){
																field[i].checked = true;
															}
															else{
																field[i].checked = false;
															}
														}
														field.checked = true;
													}
												</script>
										</display:column>
									</logic:equal>
								</logic:equal>

							</logic:iterate>
<!-- [Manu Ticket #109] - FIN - ALSIGM3 Cambiar apariencia listado de documentos -->

						</display:table>

					</td>
				</tr>
				<tr>
					<td height="10px"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="10px"/></td>
				</tr>

			</table>

		</td>
	</tr>
	<tr>
		<td height="4px"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="4px"/></td>
	</tr>

</table>

<logic:equal value="false" property="readonly" name="defaultForm">

	<script language="JavaScript" type="text/javascript">

		if (document.defaultForm['multibox'] == undefined) {
			if (document.defaultForm['allbox'] != undefined) {
				document.defaultForm['allbox'].disabled = true;
			}
		}
	</script>

</logic:equal>

<iframe src='' id='workframe_document' name='workframe_document' style='visibility:visible;height:0px;margin:0px;padding:0px;border:none;' allowtransparency='true'></iframe>

