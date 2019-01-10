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

</script>

<table cellspacing="0" cellpadding="0" align="center" width="90%">

	<tr>
		<td height="10px"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="10px"/></td>
	</tr>
	<tr>
		<td class="textbar">

			<table cellspacing="0" cellpadding="0" border="0" width="100%">

				<tr>
					<td class="textbar">
						<bean:message key="forms.tasks.attached.documents"/>:
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

			<table style=" background-color:#FFFFFF;" class="caja" cellspacing="0" cellpadding="0" width="100%">

				<tr>
					<td height="8px"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
				</tr>

				<logic:equal value="false" property="readonly" name="defaultForm">

					<tr>
						<td align="right" class="formsTitleB">

							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="formsTitle" >
										<bean:message key="forms.tasks.generate.document"/>:
									</td>
									<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
									<td>
										<ispac:linkframe 	id="GenerarDocumentoPlantilla"
												target="workframe"
												action="selectTemplateDocumentType.do"
												titleKey="forms.tasks.generate.document.from.template"
												showFrame="true"
												inputField=""
												styleClass="tdlink"
												width="500"
												height="300"
												needToConfirm="true">
										</ispac:linkframe>
									</td>
									<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
									<td>
										<ispac:linkframe 	id="GenerarDocumentoAnexar"
												target="workframe"
												action="selectDocumentType.do"
												titleKey="forms.tasks.generate.document.attach"
												showFrame="true"
												inputField=""
												styleClass="tdlink"
												width="500"
												height="300"
												needToConfirm="true">
										</ispac:linkframe>
									</td>
									<!-- INICIO [dipucr-Felipe #630] -->
									<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
									<td>
										<ispac:linkframe 	id="GenerarDocumentosZipAnexar"
												target="workframe"
												action="selectDocumentType.do?action=attachFilesFromZip.do"
												titleKey="forms.tasks.generate.document.attachZip"
												showFrame="true"
												inputField=""
												styleClass="tdlink"
												width="500"
												height="300"
												needToConfirm="true">
										</ispac:linkframe>
									</td>
									<!-- FIN [dipucr-Felipe #630] -->
									<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
									<td>
										<ispac:linkframe 	id="GenerarDocumentoEscanear"
												target="workframe"
												action="selectDocumentType.do?action=scanFiles.do"
												titleKey="forms.tasks.generate.document.scan"
												showFrame="true"
												inputField=""
												styleClass="tdlink"
												width="500"
												height="300"
												needToConfirm="true">
										</ispac:linkframe>
									</td>
									<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
									<!-- INICIO [dipucr-Felipe 3#152] -->
									<!-- Comento la opción desde repositorio y me traigo aquí la de "en bloque" -->
									<!--
									<td>
										<ispac:linkframe id="GenerarDocumentoRepositorio"
												 target="workframe"
												 action="selectDocumentType.do?action=uploadRepoFiles.do"
												 titleKey="forms.tasks.generate.document.from.repository"
												 showFrame="true"
												 inputField=""
												 styleClass="tdlink"
												 width="640"
												 height="480"
												 needToConfirm="true">
										</ispac:linkframe>
									</td>
									 -->
									<!-- [Dipucr-Manu Ticket #478] - INICIO - ALSIGM3 Nueva opción Repositorio Común -->
									<td>
										<ispac:linkframe id="GenerarDocumentoRepositorioComun"
												 target="workframe"
												 action="selectDocumentType.do?action=uploadRepoComunFiles.do"
												 titleKey="forms.tasks.generate.document.from.repositoryComun"
												 showFrame="true"
												 inputField=""
												 styleClass="tdlink"
												 width="640"
												 height="480"
												 needToConfirm="true">
										</ispac:linkframe>
									</td>
									<!-- [Dipucr-Manu Ticket #478] - FIN - ALSIGM3 Nueva opción Repositorio Común -->
									<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
									<td>
										<ispac:linkframe
												id="GenerarDocumentoPlantilla"
												target="workframe"
												action="selectTemplateDocumentType.do?selectParticipantes=1"
											    titleKey="forms.listdoc.generateDocsEnBloque"
												showFrame="true"
												inputField=""
												styleClass="tdlink"
												width="500"
												height="300"
												needToConfirm="true">
										</ispac:linkframe>
									</td>
									<!-- FIN [dipucr-Felipe 3#152] -->
									<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
								</tr>
								<tr>
									<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="5px"/></td>
								</tr>
							</table>

						</td>
					</tr>
					<tr>
						<td align="right" class="formsTitleB">
							<table border="0" cellspacing="0" cellpadding="0">

								<tr>
									<td class="formsTitleB">
										<a   href="javascript: deleteDocument();" class="tdlink">
											<bean:message key="forms.tasks.delete.document"/>
										</a>
									</td>
									<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
								</tr>
								<tr>
									<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="15px"/></td>
								</tr>
							</table>
						</td>
					</tr>

			   <!-- INICIO [dipucr-Felipe 3#152] -->
			   <!-- Paso esta opción al panel de arriba -->
			   <!-- 
    	 	   <tr>
		          <td align="right" class="formsTitleB">
						<table border="0" cellspacing="0" cellpadding="0">
							<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
							<tr>

							<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
									<td>
												<ispac:linkframe
												id="GenerarDocumentoPlantilla"
												target="workframe"
												action="selectTemplateDocumentType.do?selectParticipantes=1"
											    titleKey="forms.listdoc.generateDocsEnBloque"
												showFrame="true"
												inputField=""
												styleClass="tdlink"
												width="500"
												height="300"
												needToConfirm="true">
										</ispac:linkframe>
									</td>
									<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
							</tr>
							<tr>
									<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="5px"/></td>
							</tr>
						</table>
					</td>
		        </tr>
		        -->
		        <!-- FIN [dipucr-Felipe 3#152] -->

				<tr>
		          <td align="right" class="formsTitleB">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>'
									border="0" width="10px" /></td>
								<td>
									<ispac:linkframe
											id="FirmaMultiple"
											target="workframe"
											action="signDocuments.do"
											titleKey="forms.listdoc.firmarDocumentos"
											showFrame="true"
											inputField="multibox"
											styleClass="tdlink"
											width="500"
											height="300"
											needToConfirm="true">
									</ispac:linkframe>
								</td>
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px" /></td>
								<!-- INICIO [eCenpri-Tere #97] Firmar todos -->
								<td>
									<c:set var="_method" value="${appConstants.actions.SELECT_OPTION}" /> 
									<ispac:linkframe id="GenerarDocumentoFirmarTodos"
											 target="workframe"
											 action="signAllDocument.do?method=selectOption" 
											 inputField="property(SPAC_DT_DOCUMENTOS:ID)"
											 titleKey="forms.tasks.generate.document.from.firmarTodo" 
											 showFrame="true" 
											 styleClass="tdlink"
											 width="640"
											 height="480"
											 needToConfirm="true">
									</ispac:linkframe>
								</td>
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/></td>
								<!-- FIN [eCenpri-Tere #97] -->
							</tr>
							<tr>
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="5px"/></td>
							</tr>
						</table>
					</td>
		        </tr>

		        <tr>
		          <td align="right" class="formsTitleB">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="formsTitle"><bean:message
									key="forms.listdoc.registrarSalida" />:</td>
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>'
									border="0" width="10px" /></td>
								<td>
									<ispac:linkframe
											id="RegistroMultiple"
											target="workframe"
											action="insertBatchOutputRegistry.do"
											titleKey="forms.listdoc.registroMultiple"
											showFrame="true"
											inputField="multibox"
											styleClass="tdlink"
											width="500"
											height="300"
											needToConfirm="true">
									</ispac:linkframe>
								</td>
								<!-- [dipucr-Felipe #200] -->
								<!-- 
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>'
									border="0" width="10px" /></td>
								<td>
									<ispac:linkframe
										 	id="RegistroAgrupado"
											target="workframe"
											action="insertGroupedOutputRegistry.do"
											titleKey="forms.listdoc.registroAgrupado"
											showFrame="true"
											inputField="multibox"
											styleClass="tdlink"
											width="500"
											height="300"
											needToConfirm="true">
									</ispac:linkframe>
									</td>
								 -->
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>'
									border="0" width="10px" /></td>
									
									<!--[Manu Ticket #107] - INICIO - ALSIGM3 Registrar salida, comunicación con Comparece y Gestión de Representantes-->
								<td>
									<ispac:linkframe
											id="RegistroAllMultiple"
											target="workframe"
											action="insertAllBatchOutputRegistry.do"
											titleKey="forms.listdoc.registroAllMultiple"
											showFrame="true"
											inputField="multibox"
											styleClass="tdlink"
											width="500"
											height="300"
											needToConfirm="true">
									</ispac:linkframe>
								</td>
								<!-- [dipucr-Felipe #200] -->
								<!-- 
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>'
									border="0" width="10px" /></td>
								<td>
									<ispac:linkframe
										 	id="RegistroAllAgrupado"
											target="workframe"
											action="insertAllGroupedOutputRegistry.do"
											titleKey="forms.listdoc.registroAllAgrupado"
											showFrame="true"
											inputField="multibox"
											styleClass="tdlink"
											width="500"
											height="300"
											needToConfirm="true">
									</ispac:linkframe>
									</td>
								-->
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>'
									border="0" width="10px" /></td>
									<!--[Manu Ticket #107] - FIN - ALSIGM3 Registrar salida, comunicación con Comparece y Gestión de Representantes-->
							</tr>
							<tr>
									<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="5px"/></td>
							</tr>
						</table>
					</td>
		        </tr>

				<!-- INICIO [dipucr-Felipe 3#152]-->
				<!-- Comento esta opción pues no se usará -->
				<!--
				<tr>
		          <td align="right" class="formsTitleB">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="formsTitle"><bean:message
									key="forms.listdoc.notificaciones" />:</td>
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>'
									border="0" width="10px" /></td>
								<td>
									<ispac:linkframe
											id="NotificacionMultiple"
											target="workframe"
											action="notifyDocuments.do"
											titleKey="forms.listdoc.notificarDocumentos"
											showFrame="true"
											inputField="multibox"
											styleClass="tdlink"
											width="500"
											height="300"
											needToConfirm="true">
									</ispac:linkframe>
								</td>
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px" /></td>
							</tr>
							<tr>
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="5px"/></td>
							</tr>
						</table>
					</td>
		        </tr>
		        -->
		        <!-- FIN [dipucr-Felipe 3#152]-->

				</logic:equal>

		        <tr>
		          <td align="right" class="formsTitleB">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="formsTitle"><bean:message
									key="forms.listdoc.descargarDocumentos" />:</td>
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>'
									border="0" width="10px" /></td>
								<td><a href="javascript:downloadDocuments();" class="tdlink">
								<bean:message key="forms.listdoc.descargarDocumentos" /> </a></td>
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>'
									border="0" width="10px" /></td>
								<td><a href="javascript:convertDocuments2PDF();"
									class="tdlink"> <bean:message
									key="forms.listdoc.convertDocuments2PDF" /> </a></td>
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>'
									border="0" width="10px" /></td>
							</tr>
						</table>
					</td>
		        </tr>





				<tr>
					<td>
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="5px"/></td>
								<td style="border-bottom: 1px DOTTED #5C65A0;" width="100%"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="5px"/></td>
								<td><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="5px"/></td>
							</tr>
						</table>
					</td>
				</tr>

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
										<table cellpadding="0" cellspacing="0" border="0" width="100%">
											<tr>
												<td align="center" valign="middle">
													<img src='<ispac:rewrite href="img/pixel.gif"/>' width="3" height="1" border="0" />
												</td>
												<td align="center" valign="middle">
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

												</td>
											</tr>
										</table>
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
											<c:when test="${_data == 'PE'}"><font color="blue"><b>[Comparece] Pendiente de comparecencia</b></font></c:when>
											<c:when test="${_data == 'PR'}"><b>Envío postal</b></c:when>
											<c:when test="${_data == 'OK'}"><font color="green"><b>[Comparece] Notificada</b></font></c:when>
											<c:when test="${_data == 'CA'}"><font color="red"><b>[Comparece] Caducada</b></c:when>
											<c:when test="${_data == 'RE'}"><font color="red"><b>[Comparece] Rechazada</b></c:when>
											<c:when test="${_data == 'ER'}"><font color="red"><b>[Notifica] NIF Erróneo</b></font></c:when>
											<c:when test="${_data == 'CO'}"><font color="blue"><b>[Comparece] Pendiente de comparecencia</b></font></c:when>
											<c:when test="${_data == 'NT'}"><font color="blue"><b>[Notifica] Pendiente de comparecencia</b></font></c:when>
											<c:when test="${_data == 'NTAUSENT'}"><font color="red"><b>[Notifica] Ausente</b></font></c:when>
											<c:when test="${_data == 'NTDESCON'}"><font color="red"><b>[Notifica] Desconocido</b></font></c:when>
											<c:when test="${_data == 'NTDIERRO'}"><font color="red"><b>[Notifica] Dirección incorrecta</b></font></c:when>
											<c:when test="${_data == 'NTENVIMP'}"><font color="red"><b>[Notifica] Enviado al centro de impresión</b></font></c:when>
											<c:when test="${_data == 'NTENVDEH'}"><font color="blue"><b>[Notifica] Enviado a la DEH</b></font></c:when>
											<c:when test="${_data == 'NTILEIDA'}"><font color="green"><b>[Notifica] Leida</b></font></c:when>
											<c:when test="${_data == 'NTIERROR'}"><font color="red"><b>[Notifica] Error en el envío a Notifica</b></font></c:when>
											<c:when test="${_data == 'NTEXTRAV'}"><font color="red"><b>[Notifica] Extraviada</b></font></c:when>
											<c:when test="${_data == 'NTFALLEC'}"><font color="red"><b>[Notifica] Fallecido</b></font></c:when>
											<c:when test="${_data == 'NTIFCAOK'}"><font color="green"><b>[Notifica] Notificada</b></font></c:when>
											<c:when test="${_data == 'NTPENENV'}"><font color="red"><b>[Notifica] Caducada, envío postal</b></font></c:when>
											<c:when test="${_data == 'NTPENCOM'}"><font color="blue"><b>[Notifica] Pendiente de comparecencia</b></font></c:when>
											<c:when test="${_data == 'NTREHUSA'}"><font color="green"><b>[Notifica] Rehusada</b></font></c:when>
											<c:when test="${_data == 'NTPENPRO'}"><font color="blue"><b>[Notifica] Envío programado</b></font></c:when>
											<c:when test="${_data == 'NTSININF'}"><font color="red"><b>[Notifica] Sin información</b></font></c:when>
											<c:when test="${_data == 'NTPOSTAL'}"><font color="red"><b>[Notifica] Envío postal</b></font></c:when>
											<c:when test="${_data == 'NTCADUCA'}"><font color="red"><b>[Notifica] Caducada</b></font></c:when>
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
														<img class="tooltip" src='<ispac:rewrite href="img/viewDoc.gif"/>' title='Documento no editable Online'/>
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
													<img class="tooltip" src='<ispac:rewrite href="img/viewDoc.gif"/>' title='Documento no editable Online'/>
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
												<c:if test="${estadofirma != '00' && estadofirma != '02' && estadofirma != '03' && estadofirma != '04'}">
													<a class="tooltip" href="javascript:showFrame('workframe','showSignDetailCustom.do?parameters=workframe&document=<bean:write name="document" property="property(ID)" />',640,480,'',true);" title='<%=format.getTooltipTitle()%>' onclick='javascript:seleccionarDoc(document.defaultForm.multibox, <c:out value="${document.keyProperty}"/>); document.getElementsByName("property(SPAC_DT_DOCUMENTOS:ID)")[0].value=<c:out value="${document.keyProperty}"/>;'>
														<img class="tooltip" src='<ispac:rewrite href="img/detalleFirma.gif"/>' title='Detalles de firma'/>
													</a>
												</c:if>
												<c:if test="${estadofirma == '02' || estadofirma == '03'}">
													<a class="tooltip" href="javascript:showFrame('workframe','showSignDetailCustom.do?parameters=workframe&document=<bean:write name="document" property="property(ID)" />',640,480,'',true);" title='<%=format.getTooltipTitle()%>' onclick='javascript:seleccionarDoc(document.defaultForm.multibox, <c:out value="${document.keyProperty}"/>); document.getElementsByName("property(SPAC_DT_DOCUMENTOS:ID)")[0].value=<c:out value="${document.keyProperty}"/>;'>
														<img class="tooltip" src='<ispac:rewrite href="img/docFirmado.gif"/>' title='Documento firmado'/>
													</a>
												</c:if>
												<c:if test="${estadofirma == '00'}">
													<a class="tooltip" href="javascript:showFrame('workframe','<c:out value="${_link}"/>?field=multibox/>&parameters=workframe',640,480,'',true);" title='<%=format.getTooltipTitle()%>' onclick='javascript:seleccionarDoc(document.defaultForm.multibox, <c:out value="${document.keyProperty}"/>);'> 
														<img class="tooltip" src='<ispac:rewrite href="img/firmarDoc.gif"/>' title='Firmar Documento'/>
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
												<c:if test="${estadofirma != '00' && estadofirma != '02' && estadofirma != '03' && estadofirma != '04'}">
													<a class="tooltip" href="javascript:showFrame('workframe','showSignDetailCustom.do?parameters=workframe&document=<bean:write name="document" property="property(ID)" />',640,480,'',true);" title='<%=format.getTooltipTitle()%>' onclick='javascript:seleccionarDoc(document.defaultForm.multibox, <c:out value="${document.keyProperty}"/>); document.getElementsByName("property(SPAC_DT_DOCUMENTOS:ID)")[0].value=<c:out value="${document.keyProperty}"/>;'>
														<img class="tooltip" src='<ispac:rewrite href="img/detalleFirma.gif"/>' title='Detalles de firma'/>
													</a>
												</c:if>
												<c:if test="${estadofirma == '02' || estadofirma == '03'}">
													<a class="tooltip" href="javascript:showFrame('workframe','showSignDetailCustom.do?parameters=workframe&document=<bean:write name="document" property="property(ID)" />',640,480,'',true);" title='<%=format.getTooltipTitle()%>' onclick='javascript:seleccionarDoc(document.defaultForm.multibox, <c:out value="${document.keyProperty}"/>); document.getElementsByName("property(SPAC_DT_DOCUMENTOS:ID)")[0].value=<c:out value="${document.keyProperty}"/>;'>
														<img class="tooltip" src='<ispac:rewrite href="img/docFirmado.gif"/>' title='Documento firmado'/>
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

