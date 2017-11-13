<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>

<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="java.io.IOException"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="es.dipucr.sigem.api.rule.common.Propiedades"%>
<%@page import="java.util.Properties"%>

<script type="text/javascript" src="js/scsp.js"></script>

<div id="msgAlert"></div>

<input id="modoEntrada" type="hidden" name="modoEntrada" value="formulario"></input>
<input id="versionEsquemaV2" type="hidden" name="versionEsquemaV2" value="<%= request.getAttribute("isV2")%>"></input>
<input id="tipoDocumentacion_str" type="hidden" name="tipoDocumentacion_str" value=""></input>

<%
	String codigoCertificado = (String)request.getAttribute("codigoCertificado");
	String coreDescripcion = (String)request.getAttribute("coreDescripcion");
	String descripcion = (String)request.getAttribute("descripcion");
	String nombreFuncionario = (String)request.getAttribute("nombreFuncionario");
	String cifFuncionario = (String)request.getAttribute("cifFuncionario");
	String cifSolicitante = (String)request.getAttribute("cifSolicitante");
	String unidadTramitadora = (String)request.getAttribute("unidadTramitadora");
	String codigoProcedimiento = (String)request.getAttribute("codigoProcedimiento");
	String nombreProcedimiento = (String)request.getAttribute("nombreProcedimiento");
	String sFinalidad = (String)request.getAttribute("finalidad");
	String nombrePartic = (String)request.getAttribute("nombrePartic");
	String ndocPartic = (String)request.getAttribute("ndocPartic");
	String numexp = (String)request.getAttribute("numexp");
	String taskId = (String)request.getAttribute("taskId");
	String entidad = (String)request.getAttribute("entidad");
	
%>	

<div id="divDatos" style="height: 100%;width:100%;">
	<div class="divGenericos" style="margin-top: 0px;">
		<input maxlength="50" id="nombreSolicitante" name="nombreSolicitante" value="Universidad Pública de Navarra" type="hidden" />
		<h4 class="tableDatos">Datos genéricos del solicitante</h4>
		
		<table class="tablaListado">	
			<tr>
				<td class="formsTitle">NIF solicitante:</td>
				<td >Q3150012G</td>
				
			</tr>
			<tr>
				<td class="formsTitle">Nombre solicitante:</td>
				<td><input maxlength="60" type="text" id="nombreSolicitanteOriginal" readonly="true" value="Universidad Pública de Navarra"/></td>
			</tr>
			<tr>
				<td class="formsTitle">Nombre Funcionario:</td>
				<td><input type="text" id="nombreFuncionario" name="nombreFuncionario" readonly="true" value='<%=nombreFuncionario%>'/></td>
			</tr>
			<tr>
				<td class="formsTitle">NIF funcionario:</td>
				<td><input type="text" id="cifFuncionario" name="cifFuncionario" readonly="true" value='<%=cifFuncionario%>'/></td>
			</tr>
			<tr>
				<td class="formsTitle">Consentimiento:</td>
				<td>Si</td>
			</tr>
			<tr>
				<td class="formsTitle">Finalidad:</td>
				<td class="borde">
					<textarea name="finalidad" id="finalidad" class="inputReadOnly" readonly="true" rows="3" cols="40"><%=sFinalidad%></textarea>
				</td>
			</tr>
			
			<tr>
				<td class="formsTitle">Código procedimiento:</td>
				<td ><input type="text" id="codigoProcedimiento" name="codigoProcedimiento" readonly="true" value='<%=codigoProcedimiento%>'/></td>
			</tr>
			<tr>
				<td class="formsTitle">Nombre procedimiento:</td>
				<td ><input type="text" id="nombreProcedimiento" name="nombreProcedimiento" readonly="true" value='<%=nombreProcedimiento%>'/></td>
			</tr>
			
			
			<tr>
				<td class="formsTitle">Unidad tramitadora:</td>
				<td ><input type="text" id="unidadTramitadora" readonly="true" value='<%=unidadTramitadora%>'/></td>
			</tr>
			<tr>
				<td class="formsTitle">Número de expediente:</td>
				<td ><input type="text" id="idExpediente" readonly="true" value='<%=numexp%>'/></td>
			</tr>
		</table>
		<%
		String certificado = request.getParameter("codigoProcedimiento");	
		Propiedades prop = new Propiedades("tipoDocumentacion.properties");
		 
		 String grupoTipoDocumentacion = prop.getProperty(certificado);
		 if(grupoTipoDocumentacion == null
				 || "".equalsIgnoreCase(grupoTipoDocumentacion)){
			 grupoTipoDocumentacion = prop.getProperty("common");
		 }
		 
		 int i = 0;
		 StringTokenizer strToken = new StringTokenizer(grupoTipoDocumentacion ,"#");
		 String optionLabel = "";
		 for(;strToken.hasMoreTokens();){
			 if(i==0){
				 optionLabel+="<option selected='selected'>"+strToken.nextToken()+"</option>";
			 }else{
				 String option=strToken.nextToken();
				 optionLabel+="<option value="+option+">"+option+"</option>";
			 }
			 i++;
		 }
	 
		%>
		
		<h4 class="tableDatos">Datos del titular</h4>
		<table class="tableDatos">
		<tr>
			<td class="label" style="width:150px;">Tipo de documentación:</td>
			<td style="width:50px;"><select class="textPequenio" id="tipoDocumentacion" name="tipoDocumentacion">
				<%=optionLabel%>
			</select></td>
		</tr>
			<tr>
				<td class="formsTitle">Documentacion:</td>
				<td >
					<input type="text" id="documentacion" readonly="true" value='<%=ndocPartic%>'/>
				</td>
			</tr>
			<tr id="trNombre">
				<td class="formsTitle">Nombre:</td>
				<td >
					<input type="text" id="nombreCompletoTitular" readonly="true" value='<%=nombrePartic%>'/>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="divEspecificos" class="divEspecificos">
		<h4>Datos específicos</h4>
		
		<div class="divContenedorDatosEspecificos">
			<div id="datosEspecificosForm">
				<span id="tituloFormulario">
					Rellene los datos del titular que se muestran a continuación para realizar la consulta del certificado. 
				</span>
				<jsp:include page="datosEspecificosALSGM.jsp" />
			</div>
			
			<div id="datosEspecificosXML" style="display:none">
				<p>Esta opción le permite establecer los datos específicos a través de un fichero XML, el cual tendrá la estructura del esquema de datos específicos correspondiente al certificado a consultar. </p>
				<textarea name="datosEspecificos">${strDatosEspecificos}</textarea>
			</div>
			</br>
			<div id="divEnviarPeticion" align="center">
				<input type="button" value="Enviar petición" id="enviarPeticion" class="form_button_white"
					onclick="javascript:procesarPeticionSincrona()" />
				<input type="button" value="Cancelar" id="btnCancel" class="form_button_white"
					onclick="javascript:volverInicio()" />
			</div>
		</div>
		
	</div>
	
</div>
<br/>



<script>
//Si estamos en IE retocamos el ancho del datosEspecificos ya que coge mal el valor del width
var navegador = navigator.appName;
//Preseleccionamos el formulario
enable('opt1') ;
if (navegador == "Microsoft Internet Explorer") {
	document.getElementById('divEspecificos').style.top="-0.5%";
}else{
	document.getElementById('divEspecificos').style.top="2%";
}
function volverInicio() {
	//Pruebas entity=467
	//window.location.href= "<%=request.getContextPath()%>/showTask.do?taskId=<%=taskId%>&entity=467&numexp=<%=numexp%>";
	//Produccion
	window.location.href= "<%=request.getContextPath()%>/showTask.do?taskId=<%=taskId%>&entity=<%=entidad%>&numexp=<%=numexp%>";
}
function procesarPeticionSincrona() {
	ok = true;
	var mensajeError="Complete los campos obligatorios para continuar.";
	//recorremos buscando datos marcados como required por cada servicio para los datos especificos.
	for (var i=0;  i < document.formulario.elements.length; i++){
		var identi=document.formulario.elements[i].id;
		if(identi =='')continue;
		if( document.formulario.elements[i].disabled==true)continue;
		if( document.formulario.elements[i].getAttribute('required')==null)continue;
		if(document.getElementById("modoEntrada").value  =='xml'){
			//Si estamos en modo de envio de XML obviamos los campos del formulario de datos especificos.
			if(document.formulario.elements[i].getAttribute('datoespecifico')!=null)continue;
		}
		if( (document.formulario.elements[i].type=='radio'||document.formulario.elements[i].type=='checkbox') ){
			validateCheck(identi);
		}else{
		    validate(identi, "");
		}
	}
	var esquemaV2 = document.getElementById("versionEsquemaV2").value;	
	if( ok && esquemaV2 == "true"){
		/**
		 * Esta parte controla el size ad-hoc que debbe tener la unidad tramitadora
		 * */
		warningSizeSolicitante("nombreSolicitante",
			"En los mensajes con esquema V2, la unidad tramitadora se concatenara con el nombre del solicitante." +
			"Dicha concatenacion ha sobrepasado el tamaño máximo: "+ tamanioMaxNombreSolicitante+" caracteres . El texto será cortado.");
		var mensajeFinalidad =validateFinalidadCombianda();
		mensajeError= mensajeFinalidad;
	}
	if (ok) {
		showTransparentDiv();
		document.formulario.submit();
		document.getElementById("msgAlert").innerHTML = "";
	} else {
		document.getElementById("msgAlert").innerHTML = "<div class=\"alert\">"+mensajeError+"</div>";
	}
}
</script>