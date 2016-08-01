var displayNombreCompleto = false;
var ok = false;
var tamanioMaxNombreSolicitante=50;
var tamanioMaxFinalidad=250; 
function actualizarCampo(id,idNew){
	document.getElementById(id).value = document.getElementById(idNew).value;
}
function changeNombreTitular() {
	//Debemos tener en cuenta si eran required la anterior opcion, para establecerlo en la siguiente:
	if(displayNombreCompleto){
		if(document.getElementById("nombreCompletoTitular").getAttribute('required')!=null){
			document.getElementById("nombreCompletoTitular").removeAttribute('required');
			document.getElementById("nombreTitular").setAttribute('required','1');
			document.getElementById("apellido1").setAttribute('required','1');
			document.getElementById("apellido2").setAttribute('required','1');
		}
	}else{
		if(document.getElementById("apellido1").getAttribute('required')!=null){
			document.getElementById("nombreCompletoTitular").setAttribute('required','1');
			document.getElementById("nombreTitular").removeAttribute('required');
			document.getElementById("apellido1").removeAttribute('required');
			document.getElementById("apellido2").removeAttribute('required');
		}
	}
	
	displayNombreCompleto = !displayNombreCompleto;
	document.getElementById("nombreCompletoTitular").value = "";
	document.getElementById("nombreTitular").value = "";
	document.getElementById("apellido1").value = "";
	document.getElementById("apellido2").value = "";
	//document.getElementById("nombreCompletoTitularMsg").innerHTML = "";
	//document.getElementById("nombreTitularMsg").innerHTML = "";
	
	document.getElementById("trNombreCompleto").style.display = (displayNombreCompleto ? "" : "none");
	document.getElementById("trNombre").style.display = (!displayNombreCompleto ? "" : "none");
	document.getElementById("trApellido1").style.display = (!displayNombreCompleto ? "" : "none");
	document.getElementById("trApellido2").style.display = (!displayNombreCompleto ? "" : "none");
	
	
	
}

function enableDatosEspecificosForm() {
	document.getElementById("datosEspecificosXML").style.display = "none";
	document.getElementById("datosEspecificosForm").style.display = "";
	document.getElementById("datosEspecificosExcel").style.display = "none";
	document.getElementById("modoEntrada").value = "formulario";
	enable("opt1");
	disable("opt2");
	disable("opt3");
	habilitarControlesExcel(false);
}

function enableDatosEspecificosXML() {
	document.getElementById("datosEspecificosXML").style.display = "";
	document.getElementById("datosEspecificosForm").style.display = "none";
	document.getElementById("datosEspecificosExcel").style.display = "none";
	document.getElementById("modoEntrada").value = "xml";
	disable("opt1");
	enable("opt2");
	disable("opt3");
	
	habilitarControlesExcel(false);
}

function enableDatosEspecificosExcel() {
	document.getElementById("datosEspecificosXML").style.display = "none";
	document.getElementById("datosEspecificosForm").style.display = "none";
	document.getElementById("datosEspecificosExcel").style.display = "";
	document.getElementById("modoEntrada").value = "excel";
	disable("opt1");
	disable("opt2")
	enable("opt3");
	
	habilitarControlesExcel(true);
}
function showPDF(){
	actualizarPdf();
	document.getElementById("divRespuesta").style.display = "none";
	document.getElementById("divDatorsEspecificosXML").style.display = "none";
	document.getElementById("divRawXML").style.display = "none";
	document.getElementById("divDescargasPDF").style.display ="";
}

function showExcel(){
	actualizarExcel();
	document.getElementById("divRespuesta").style.display = "none";
	document.getElementById("divDatorsEspecificosXML").style.display = "none";
	document.getElementById("divRawXML").style.display = "none";
	document.getElementById("divDescargasExcel").style.display = "";
	if(document.getElementById("divDescargasPDF")!=null){
		document.getElementById("divDescargasPDF").style.display ="none";
	}
	document.getElementById("opt2EXCEL").style.display = "none";	
}

function showRespuestaGeneral() {
	
	document.getElementById("divRespuesta").style.display = "";
	document.getElementById("divDatorsEspecificosXML").style.display = "";
	if(document.getElementById("divRawXML").style.display == ""){
		revertirEstilo("opt2");	
	}
	document.getElementById("divRawXML").style.display = "none";
	
	if(document.getElementById("divDescargasExcel")!=null){
		if(document.getElementById("divDescargasExcel").style.display == ""){
			revertirEstilo("divXls");
		}
		document.getElementById("divDescargasExcel").style.display = "none";
	}else{
		if(isXls != ""){
			revertirEstilo("divXls");
		}
	}
	if(document.getElementById("divDescargasPDF")!=null){
		if(document.getElementById("divDescargasPDF").style.display == ""){
			revertirEstilo("divPdf");
		}
		document.getElementById("divDescargasPDF").style.display ="none";
	}else{
		if(isPdf != ""){
			revertirEstilo("divPdf");
		}
	}
	cambiarEstilo("opt1");
	//enable("opt1");
	//disable("opt2");
}

function showRespuestaXML() {
	if(document.getElementById("divRespuesta").style.display == ""){
		revertirEstilo("opt1");	
	}
	document.getElementById("divRespuesta").style.display = "none";
	document.getElementById("divDatorsEspecificosXML").style.display = "none";
	if(document.getElementById("divDescargasPDF")!=null){
		if(document.getElementById("divDescargasPDF").style.display == ""){
			revertirEstilo("divPdf");
		}
		document.getElementById("divDescargasPDF").style.display ="none";
	}else{
		if(isPdf != ""){
			revertirEstilo("divPdf");
		}
	}
	if(document.getElementById("divDescargasExcel")!=null){	
		if(document.getElementById("divDescargasExcel").style.display == ""){
			revertirEstilo("divXls");
		}
		document.getElementById("divDescargasExcel").style.display = "none";
	}else{
		if(isXls != ""){
			revertirEstilo("divXls");
		}
	}
	document.getElementById("divRawXML").style.display = "";
	
	cambiarEstilo("opt2");
	//disable("opt1");
	//enable("opt2");
}

function habilitarControlesExcel(_v) {
	document.getElementById("documentacion").disabled = _v;
	document.getElementById("idExpediente").disabled = _v;
	document.getElementById("nombreCompletoTitular").disabled = _v;
	document.getElementById("nombreTitular").disabled = _v;
	document.getElementById("apellido1").disabled = _v;
	document.getElementById("apellido2").disabled = _v;
	if(_v) {
		document.getElementById("documentacion").value = "";
		document.getElementById("idExpediente").value = "";
		document.getElementById("nombreCompletoTitular").value = "";
		document.getElementById("nombreTitular").value = "";
		document.getElementById("apellido1").value = "";
		document.getElementById("apellido2").value = "";
		//Quitamos el marco rojo		
		document.getElementById("documentacion").style.borderColor="";
		document.getElementById("idExpediente").style.borderColor="";
		document.getElementById("nombreCompletoTitular").style.borderColor="";
		document.getElementById("nombreTitular").style.borderColor="";
		document.getElementById("apellido1").style.borderColor="";
		document.getElementById("apellido2").style.borderColor="";
	}
}

function procesarPeticion() {
	ok = true;
	var mensajeError="Complete los campos obligatorios para continuar.";
	
	/****Control de cumplimentacion de campos obligatorios***/
	//Rellenado con ceros por la izquierda del DNI
	fillDNI();
	
	//Control de los campos genericos
	//validate("cifSolicitante", "cifSolicitanteMsg");
	//validate("nombreSolicitante", "nombreSolicitanteMsg");
	validate("finalidad", "finalidadMsg");
	//La documentacion solo se valida cuando no se utiliza el formato excel de subida
	var mode = document.getElementById("modoEntrada").value;
	if( "excel" != mode) {
		//Los required son solo para formularios y XML en el caso del excel, solo recuperamos del formulario los datos del solicitante
			validate("documentacion", "documentacionMsg");			
				
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

//Esta comprobacion se debe a un parche de la gente del ministerio, que impone que en la V2, se concatene con
//el formato codigoProcedimiento+"#::#"+idExpediente+"#::#"+finalidad para el campo Finalidad, ya que no existen
//los nodos de codigoProcedimietno ni idExpediente en la V2.
function validateFinalidadCombianda(){
	 var codProc = document.getElementById("codigoProcedimiento").value;
	 var idExpediente =  document.getElementById("idExpediente").value;
	 var finalidad = document.getElementById("finalidad").value;
 	 var textoFinal = codProc+"#::#"+idExpediente+"#::#"+finalidad;
 	 if(textoFinal.length > tamanioMaxFinalidad){
 		ok=false;
 		return "Para los servicios con esquema V2, el campo finalidad enviado seguir&aacute; el formato codigoProcedimiento#::#idExpediente#::#finalidad." +
	 		"El campo finalidad sobrepasa el tama&ntilde;o m&aacute;ximo permitido :"+tamanioMaxFinalidad;
 		 
 	 }
 	 return "";
}
function showTransparentDiv() {
	/*document.getElementById("divTransparente").style.display = "";*/
}
function validateCheck(_name) {
	if (document.getElementById(_name).checked == false) {
		document.getElementById(_name).parentNode.style.border="1px solid red";
		ok = false;
		
	} else {
		document.getElementById(_name).parentNode.style.border="";
		
	}
}
function validate(_name, _msg) {
	if (document.getElementById(_name).value == "") {
		//si posee el atributo marcarPadre, se establece el borde sobre el padre del elemento.
		if(document.getElementById(_name).getAttribute('marcarpadre')==null){ 
			document.getElementById(_name).style.borderColor="red";
		}else{
			document.getElementById(_name).parentNode.style.border="1px solid red";
		}
		ok = false;
		
	} else {
		if(document.getElementById(_name).getAttribute('marcarpadre')==null){ 
			document.getElementById(_name).style.borderColor="";
		}else{
			document.getElementById(_name).parentNode.style.border="";
		}
	}
}
function warningSizeSolicitante(_name, _msg){
	concatenaUnidadNombreSolicitante();
	if (document.getElementById(_name).value.length >tamanioMaxNombreSolicitante) {
		alert(_msg);
		document.getElementById(_name).value=	document.getElementById(_name).value.substring(0,tamanioMaxNombreSolicitante);
	}
}
function enable(_name) {
	//document.getElementById(_name).setAttribute("class", "menuOptionEnabled"); //esto no funciona en ie
	try{
		document.getElementById(_name).style.backgroundColor = "#3399CC";
		document.getElementById(_name).style.borderColor = "#339900";
		document.getElementById(_name).style.color = "white";
	}catch (e) {
		// 
	}
}

function disable(_name) {
	//document.getElementById(_name).setAttribute("class", "menuOptionDisabled"); //esto no funciona en ie
	try{
	document.getElementById(_name).style.backgroundColor = "#666666";
	document.getElementById(_name).style.borderColor = "#333333";
	document.getElementById(_name).style.color = "CCCCCC";
	}catch (e) {
		// TODO: handle exception
	}
}

/*Funciones para el polling*/
//Funcion  que realiza la llamada a la busqueda de peticiones para polling segun filtro
function enviar(){
	alert("enviando...");
	if(validar()){
		document.getElementsByTagName("form")[0].submit();
	}
}
//Funcion que valida la entrada de los filtros para el polling
function validar(){
	alert("validando..." + document.getElementById("from").value);
	var RegExPattern = "^\d{1,2}\/\d{1,2}\/\d{2,4}$";
   
    if ((document.getElementById("from").value.trim()!='')) {
    	
		if((document.getElementById("from").value.match(RegExPattern))){
			alert('Fecha desde erronea. El formato debe ser dd/MM/yyyy'); 
	    	return false;
		} else {
			alert("se la traga ...");
		}
	}
    if ((document.getElementById("to").value.trim()!='')) {
		if((document.getElementById("to").value.match(RegExPattern))){
			alert('Fecha hasta erronea. El formato debe ser dd/MM/yyyy'); 
	    	return false;
		}
	}		
	return true;
  
} 
function selectPais(espania){
	var pais= document.getElementById('vPais').options[document.getElementById('vPais').selectedIndex].value;
	document.getElementById('paisNacimiento').value=pais;
	if(pais != espania){//NO ES ESPAï¿½OL
		document.getElementById('vComunidad').style.display = "none";
		if(document.getElementById('vProvincia') !=null){
			document.getElementById('vProvincia').style.display = "none";
		}
		document.getElementById('extranjero').value="true";
	}else{
		if(document.getElementById('vProvincia') !=null){
			document.getElementById('vProvincia').style.display = "";
		}
		document.getElementById('vComunidad').style.display = "";
		document.getElementById('extranjero').value="false";
	}	 
}
function refreshNombreProcedimiento(){
	var procedimiento= document.getElementById('codigoProcedimiento').options[document.getElementById('codigoProcedimiento').selectedIndex].text;
	var cod= document.getElementById('codigoProcedimiento').options[document.getElementById('codigoProcedimiento').selectedIndex].value;
	 document.getElementById('nombreProcedimiento').value=procedimiento;
	 document.getElementById('codigoProc').value=cod;
	 
}

function habilitaConsultaBienesInmueble(tipo){
	
	if(tipo == 'catastral'){
		document.getElementById('pcatastral1').disabled=false;
		                         
		document.getElementById('pcatastral2').disabled=false;
		document.getElementById('car').disabled=false;
		document.getElementById('cc1').disabled=false;
		document.getElementById('cc2').disabled=false;
		
		document.getElementById('codPoligono').value="";
		document.getElementById('codParcela').value="";
		document.getElementById('codPoligono').disabled=true;
		document.getElementById('codParcela').disabled=true;
		
		
		if(document.getElementById('vComunidad')!=null){
			document.getElementById('vComunidad').disabled=true;
			document.getElementById('vComunidad').style.borderColor="";
			document.getElementById('vComunidad').selectedIndex=0;
		}
		if(document.getElementById('vProvincia')!=null){
			document.getElementById('vProvincia').disabled=true;
			document.getElementById('vProvincia').style.borderColor="";
			document.getElementById('vProvincia').selectedIndex=0;
		} 
		if(document.getElementById('vMunicipio')!=null){
			document.getElementById('vMunicipio').disabled=true;
			document.getElementById('vMunicipio').style.borderColor="";
			document.getElementById('vMunicipio').selectedIndex=0;
		} 
		//Establecemos obligaroriedades de parcelar catastral
		document.getElementById('pcatastral1').setAttribute('required',"1");
		document.getElementById('pcatastral2').setAttribute('required',"1");
		document.getElementById('pcatastral1').setAttribute('datoespecifico',"1");
		document.getElementById('pcatastral2').setAttribute('datoespecifico',"1");
		
		document.getElementById('codPoligono').removeAttribute('required');
		document.getElementById('codPoligono').style.borderColor="";
	}else{
		document.getElementById('pcatastral1').value="";
		document.getElementById('pcatastral2').value="";
		document.getElementById('car').value="";
		document.getElementById('cc1').value="";
		document.getElementById('cc2').value="";
		document.getElementById('pcatastral1').disabled=true;
		document.getElementById('pcatastral2').disabled=true;
		document.getElementById('car').disabled=true;
		document.getElementById('cc1').disabled=true;
		document.getElementById('cc2').disabled=true;
		
		document.getElementById('codPoligono').disabled=false;
		document.getElementById('codParcela').disabled=false;
		
		//Establecemos obligaroriedades de parcelar rustica
		document.getElementById('codPoligono').setAttribute('required',"1");
		document.getElementById('codPoligono').setAttribute('datoespecifico',"1");
		
		
		document.getElementById('pcatastral1').removeAttribute('required');
		document.getElementById('pcatastral2').removeAttribute('required');
		document.getElementById('pcatastral1').style.borderColor="";
		document.getElementById('pcatastral2').style.borderColor="";
		if(document.getElementById('vComunidad')!=null){
			document.getElementById('vComunidad').disabled=false;
			document.getElementById('vComunidad').style.borderColor="";
			document.getElementById('vComunidad').setAttribute('datoespecifico',"1");
		}
		if(document.getElementById('vProvincia')!=null){
			document.getElementById('vProvincia').disabled=false;
			document.getElementById('vProvincia').style.borderColor="";
			document.getElementById('vProvincia').setAttribute('datoespecifico',"1");
		} 
		if(document.getElementById('vMunicipio')!=null){
			document.getElementById('vMunicipio').disabled=false;
			document.getElementById('vMunicipio').style.borderColor="";
			document.getElementById('vMunicipio').setAttribute('datoespecifico',"1");
		} 
	}
}
function habilitaConsultaGraficaInmueble(tipo){
	 
	if(tipo == 'catastral'){
		document.getElementById('pcatastral1').disabled=false;
		document.getElementById('pcatastral2').disabled=false;
		document.getElementById('car').disabled=false;
		document.getElementById('cc1').disabled=false;
		document.getElementById('cc2').disabled=false;
		
		document.getElementById('codPoligono').value="";
		document.getElementById('codParcela').value="";
		document.getElementById('codPoligono').disabled=true;
		document.getElementById('codParcela').disabled=true;
		//Establecemos obligaroriedades de parcelar catastral
		document.getElementById('pcatastral1').setAttribute('required',"1");
		document.getElementById('pcatastral2').setAttribute('required',"1");
		document.getElementById('pcatastral1').setAttribute('datoespecifico',"1");
		document.getElementById('pcatastral2').setAttribute('datoespecifico',"1");

		 
		document.getElementById('car').setAttribute('required',"1");
		
		document.getElementById('codPoligono').removeAttribute('required');
		document.getElementById('codParcela').removeAttribute('required');
		document.getElementById('codPoligono').style.borderColor="";
		document.getElementById('codParcela').style.borderColor="";
	}else{
		document.getElementById('pcatastral1').value="";
		document.getElementById('pcatastral2').value="";
		document.getElementById('car').value="";
		document.getElementById('cc1').value="";
		document.getElementById('cc2').value="";
		document.getElementById('pcatastral1').disabled=true;
		document.getElementById('pcatastral2').disabled=true;
		document.getElementById('car').disabled=true;
		document.getElementById('cc1').disabled=true;
		document.getElementById('cc2').disabled=true;
		
		document.getElementById('codPoligono').disabled=false;
		document.getElementById('codParcela').disabled=false;
		
		//Establecemos obligaroriedades de parcelar rustica
		document.getElementById('codPoligono').setAttribute('required',"1");
		document.getElementById('codParcela').setAttribute('required',"1");
		document.getElementById('codPoligono').setAttribute('datoespecifico',"1");
		document.getElementById('codParcela').setAttribute('datoespecifico',"1");
		
		document.getElementById('pcatastral1').removeAttribute('required');
		document.getElementById('pcatastral2').removeAttribute('required');
		document.getElementById('car').removeAttribute('required');
		document.getElementById('pcatastral1').style.borderColor="";
		document.getElementById('pcatastral2').style.borderColor="";
		document.getElementById('car').style.borderColor="";
		
	}
	
}
function goBack(){
	window.history.go(-1);
}
function concatenaUnidadNombreSolicitante(){
		var texto = document.getElementById('unidadTramitadora').value;
		var solicitante =document.getElementById('nombreSolicitanteOriginal').value ;	
		var solconcatenado = solicitante;
		
		if(texto != ''){
			
			solconcatenado = solicitante+"-"+texto;
		}
		document.getElementById('nombreSolicitante').value = solconcatenado;
		
}
function truncarValue(campo,size){
	var texto = document.getElementById(campo).value;
	if(texto.length >size){
		document.getElementById(campo).value = texto.substring(0,size);
	}
}
function modalWin(url) {
 
		window.open(url,'SCSP','height=250,width=950');
	 
} 

//Funcion utilizada por cambio de domicilio, para establecer los valores de  los campos del titular y hacerlos no editables.
function actualizaTitular(tipodoc,documentacion,nombre,ap1,ap2){
	var options = document.getElementById('tipoDocumentacion').options;
	for(var i=0;i< document.getElementById('tipoDocumentacion').length;i++){
		if(options[i].value == tipodoc){
			document.getElementById('tipoDocumentacion').selectedIndex =i;
		}
	} 
	document.getElementById('documentacion').value=documentacion;
	
	document.getElementById('apellido2').value=ap2;
	
	document.getElementById('apellido1').value=ap1;
	document.getElementById('nombreTitular').value=nombre;
	
	document.getElementById('documentacion').readOnly=true;
	document.getElementById('apellido2').readOnly=true;	
	document.getElementById('apellido1').readOnly=true;
	document.getElementById('nombreTitular').readOnly=true;
	document.getElementById('tipoDocumentacion').disabled=true;
	document.getElementById('tipoDocumentacion_str').value=tipodoc;
	//En Cambio de domicilio, no permitimos modificar el tipo de nombre +apps hacia nombre completo.
	document.getElementById('enlaceNombreCompleto').href="#nombreTitular";
	//document.getElementById('nombreCompletoTitular').value=nombre+" "+ap1+" "+ap2;
	
}
function setRequiredNames(){
	document.getElementById('nombreCompleto_required').innerHTML=" *";
	document.getElementById('nombreTitular_required').innerHTML=" *";
	document.getElementById('apellido1_required').innerHTML=" *";
	document.getElementById('apellido2_required').innerHTML=" *";
	document.getElementById('apellido2').setAttribute('required',"1");
	document.getElementById('apellido1').setAttribute('required',"1");
	document.getElementById('nombreTitular').setAttribute('required',"1");
	 
}
function setDisable(id,booleano){
	document.getElementById(id).disabled =booleano;
}
function verificaAlgunaEntidadSelected(){
	var someselected=false;
	for (var i=0;  i < document.formulario.elements.length; i++){
		var elemento = document.formulario.elements[i];
		var nombre=elemento.name;
		if(nombre.indexOf('entidad_')!=-1){
			if(elemento.checked)someselected=true;
		}
	}
	if(someselected==true)document.getElementById('selectedEntidades').value="ok";
	else document.getElementById('selectedEntidades').value="";
}
function validarDocumentacion(doc){
	if(isNaN(doc.substr(0,1))){
		//es NIE
		return validarNIE(doc);
	}else{
		//es DNI
		return validaDNI(doc);
	}
	
	
}
function validarNIE(nie){
	nie =nie.toUpperCase();
	var cadenadni="TRWAGMYFPDXBNJZSQVHLCKE";
	var temp=nie.toUpperCase();
	if (!(/^[XxTtYyZz]{1}[0-9]{7}[a-zA-Z]{1}$/.test(temp)) && temp!=""){
		alert ("1.El Documento NIE no cumple con el formato esperado");
		return false;
	}
	if (/^[T]{1}/.test(temp)){
		if (nie[8] == /^[T]{1}[A-Z0-9]{8}$/.test(temp))return true;
		else{
			alert ("2.El Documento NIE no cumple con el formato esperado");
			return false;
		}
	}
	//XYZ
	if (/^[XYZ]{1}/.test(temp))
	{
		temp = str_replace('X', '0', temp);
		temp = str_replace('Y', '1', temp);
		temp = str_replace('Z', '2', temp);
		
		pos = temp.substring(0, 8) % 23;
		 
		if (nie[8] == cadenadni.substring(pos, pos + 1))
		{
			return true;
		}
		else
		{
			alert ("3.El Documento NIE no cumple con el formato esperado");
			return false;
		}
	}
	alert('El NIE introducido no es correcto');
	return false;

}
function str_replace(busca, repla, orig)
{
	str 	= new String(orig);

	rExp	= "/"+busca+"/g";
	rExp	= eval(rExp);
	newS	= String(repla);

	str = new String(str.replace(rExp, newS));

	return str;
}

function validaDNI(dni) {
	    
	  numero = dni.substr(0,dni.length-1);
	  let = dni.substr(dni.length-1,1);
	  numero = numero % 23;
	  letra='TRWAGMYFPDXBNJZSQVHLCKET';
	  letra=letra.substring(numero,numero+1);
	  if (letra!=let){
	    alert('El DNI introducido no es correcto');
	    return false;
	  }
	  return true;
	  
}
function fillDNI(){
	var element = document.getElementById("tipoDocumentacion");
	var val = element.options[element.selectedIndex].value;
	if(val == "NIF"  || val == "DNI"){
		var nif = document.getElementById("documentacion").value;
		if(nif == "")return;
		var iter = 9 - nif.length;
		for(var i = 0;i<iter;i++)nif = '0'+nif;
		document.getElementById("documentacion").value = nif;
	}
	 
}
function selectAllTransmisiones(){
	var checks = document.getElementsByName("checkJustificantePDF");
	for (var i=0;  i < checks.length; i++){
		checks[i].checked=1;
	}
	document.getElementById("opt2PDF").style.display = "";	
}
function changeStatusCheckPDF(){
	var checks = document.getElementsByName("checkJustificantePDF");
	var checkeado=0;
	for (var i=0;  i < checks.length; i++){
		if(checks[i].checked==1)checkeado=1;
	}
	if(checkeado== 0){
		document.getElementById("opt2PDF").style.display = "none";	
	}else{
		document.getElementById("opt2PDF").style.display = "";	
	}
}
function enviarPDF(){
	var checks = document.getElementsByName("checkJustificantePDF");
	var idstransmisiones="";
	for (var i=0;  i < checks.length; i++){
		if(checks[i].checked==1){
			if(i==0)idstransmisiones=checks[i].value;
			else idstransmisiones+="$$$"+checks[i].value;
		}
	}
	window.location.href="pdf?idtransmisiones="+idstransmisiones;
}
function enviarPDFPosteriori(){
	var checks = document.getElementsByName("checkJustificantePDF");
	var idstransmisiones="";
	for (var i=0;  i < checks.length; i++){
		if(checks[i].checked==1){
			if(i==0)idstransmisiones=checks[i].value;
			else idstransmisiones+="$$$"+checks[i].value;
		}
	}
	if(idstransmisiones =="")alert("Debe seleccionar alguna transmision");
	else{
		document.getElementById('idstransmision').value= idstransmisiones;
		document.getElementById('op').value="pdf";
		document.getElementById('formulario').submit();
	}
	
}
function enviarXMLResponse(){
	var checks = document.getElementsByName("checkXMLResponse");
	var idspeticiones="";
	for (var i=0;  i < checks.length; i++){
		if(checks[i].checked==1){
			if(i==0)idspeticiones=checks[i].value;
			else idspeticiones+="$$$"+checks[i].value;
		}
	}
	if(idspeticiones =="")alert("Debe seleccionar alguna transmision");
	else{
		document.getElementById('idstransmision').value= idspeticiones;
		document.getElementById('op').value="xmlResponse";
		document.getElementById('formulario').submit();
	}
	
}
function enviarXMLTransmision(){
	var checks = document.getElementsByName("checkXMLTransmision");
	var idstransmisiones="";
	for (var i=0;  i < checks.length; i++){
		if(checks[i].checked==1){
			if(i==0)idstransmisiones=checks[i].value;
			else idstransmisiones+="$$$"+checks[i].value;
		}
	}
	if(idstransmisiones =="")alert("Debe seleccionar alguna transmision");
	else{
		document.getElementById('idstransmision').value= idstransmisiones;
		document.getElementById('op').value="xmlTransmision";
		document.getElementById('formulario').submit();
	}
	
}

function selectAllCasillas(){
	var checks = document.getElementsByName("checkCasillaExcel");
	for (var i=0;  i < checks.length; i++){
		checks[i].checked=1;
	}
	document.getElementById("opt2EXCEL").style.display = "";	
}

function desSelectAllCasillas(){
	var checks = document.getElementsByName("checkCasillaExcel");
	for (var i=0;  i < checks.length; i++){
		checks[i].checked=0;
	}
	document.getElementById("opt2EXCEL").style.display = "none";	
}

function changeStatusCheckExcel(){
	var checks = document.getElementsByName("checkCasillaExcel");
	var checkeado=0;
	for (var i=0;  i < checks.length; i++){
		if(checks[i].checked==1)checkeado=1;
	}
	if(checkeado== 0){
		document.getElementById("opt2EXCEL").style.display = "none";	
	}else{
		document.getElementById("opt2EXCEL").style.display = "";	
	}
}

function enviarEXCEL(){
	var checks = document.getElementsByName("checkCasillaExcel");
	var hidden = document.getElementsByName("anio");
	var anioSeleccionado=hidden[0].value;
	var casillas="";
	for (var i=0;  i < checks.length; i++){
		if(checks[i].checked==1){
			if(i==0)casillas=checks[i].value;
			else casillas+="$$$"+checks[i].value;
		}
	}
	window.location.href="excel?action=dr&anio="+anioSeleccionado+"&casillas="+casillas;
}
function showTransmision(idtrans){
	var trans = document.getElementsByName("transmisionAsincrona");
	if(document.getElementById(idtrans).style.display == "none"){
		for (var i=0;  i < trans.length; i++){
			trans[i].style.display="none";
		}
		document.getElementById(idtrans).style.display="";
	}else{
		document.getElementById(idtrans).style.display = "none";
	}
}
function marcarAll(idCheck, nombreChecks){
	var checks = document.getElementsByName(nombreChecks);
	for (var i=0;  i < checks.length; i++){
		 checks[i].checked = document.getElementById(idCheck).checked;
	} 	
}

var theObj="";
function toolTip(text,me) {
       theObj=me;
       theObj.onmousemove=updatePos;
       document.getElementById('toolTipBox').innerHTML=text;
       document.getElementById('toolTipBox').style.display="block";
       window.onscroll=updatePos;
}
function updatePos() {
       var ev=arguments[0]?arguments[0]:event;
       var x=ev.clientX;
       var y=ev.clientY;
       diffX=24;
       diffY=0;
       document.getElementById('toolTipBox').style.top  = y-2+diffY+document.body.scrollTop+ "px";
       document.getElementById('toolTipBox').style.left = x-2+diffX+document.body.scrollLeft+"px";
       theObj.onmouseout=hideMe;
}
function hideMe() {
       document.getElementById('toolTipBox').style.display="none";
}

function actualizarExcel(){
	
	if(document.getElementById("divRespuesta").style.display == ""){
		revertirEstilo("opt1");	
	}
	
	if(document.getElementById("divRawXML").style.display == ""){
		revertirEstilo("opt2");	
	}
	
	if(document.getElementById("divDescargasPDF")!=null){
		if(document.getElementById("divDescargasPDF").style.display == ""){
			revertirEstilo("divPdf");
		}
		document.getElementById("divDescargasPDF").style.display ="none";
	}else{
		if(isPdf != ""){
			revertirEstilo("divPdf");
		}
	}
	cambiarEstilo("divXls");
	isXls="true";
}

function actualizarPdf(){
	
	if(document.getElementById("divRespuesta").style.display == ""){
		revertirEstilo("opt1");	
	}
	
	if(document.getElementById("divRawXML").style.display == ""){
		revertirEstilo("opt2");	
	}
	
	if(document.getElementById("divDescargasExcel")!=null){	
		if(document.getElementById("divDescargasExcel").style.display == ""){
			revertirEstilo("divXls");
		}
		document.getElementById("divDescargasExcel").style.display = "none";
	}else{
		if(isXls != ""){
			revertirEstilo("divXls");
		}
	}
	cambiarEstilo("divPdf");
	isPdf="true";
}

function revertirEstilo(id){
	var elemento = document.getElementById(id);
    if(elemento!= null){
    	elemento.style.fontSize = "";
        elemento.style.border= "";
        elemento.style.backgroundColor= "";
        elemento.style.borderColor="";
        elemento.style.color="";
        elemento.style.padding= "";        
        if(id=="opt1" || id=="opt2"|| id=="divXls" ||id=="divPdf"){
        	elemento.style.padding= "";
        }else{
        	elemento.style.margin= "";
        }        
        elemento.style.fontWeight="";
        elemento.className="menuOptionEnabled";
    }
}
function cambiarEstilo(id) {  
	
    var elemento = document.getElementById(id);
    if(elemento!= null){
    	elemento.style.fontSize = "10pt";
        elemento.style.border= "1px solid";
        elemento.style.backgroundColor= "#EFF3F9";
        elemento.style.borderColor="#555555";
        elemento.style.color="#555555";
        elemento.style.padding= "5px 5px 5px 5px";        
        if(id=="opt1" || id=="opt2" || id =="divXls"  ||id=="divPdf"){
        	elemento.style.padding= "3px";
        }else{
        	elemento.style.margin= "0px 5px 0px 0px";
        }        
        elemento.style.fontWeight="bold";
    }	
}  