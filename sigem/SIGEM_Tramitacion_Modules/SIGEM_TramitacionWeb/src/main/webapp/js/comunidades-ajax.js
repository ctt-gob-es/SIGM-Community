var xmlhttp1;
var xmlhttp2;
var xmlhttpPais;
var contentType = 'application/x-www-form-urlencoded';
var comunidadesCargadas =false;
function createHttpRequest1(url) {
	xmlhttp = false;
	if (window.XMLHttpRequest) {
		try {
			xmlhttp1 = new XMLHttpRequest();
		} catch (e) {
			xmlhttp1 = false;
		}
	} else if (window.ActiveXObject) {
		try {
			xmlhttp1 = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				xmlhttp1 = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {
				xmlhttp1 = false;
			}
		}
	}
	xmlhttp1.open("POST", url, true);
	xmlhttp1.setRequestHeader('Content-Type', contentType);
}
function createHttpRequest2(url) {
	xmlhttp2 = false;
	if (window.XMLHttpRequest) {
		try {
			xmlhttp2 = new XMLHttpRequest();
		} catch (e) {
			xmlhttp2 = false;
		}
	} else if (window.ActiveXObject) {
		try {
			xmlhttp2 = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				xmlhttp2 = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {
				xmlhttp2 = false;
			}
		}
	}
	xmlhttp2.open("POST", url, true);
	xmlhttp2.setRequestHeader('Content-Type', contentType);
}
function createHttpRequestPais(url) {
	xmlhttp = false;
	if (window.XMLHttpRequest) {
		try {
			xmlhttpPais = new XMLHttpRequest();
		} catch (e) {
			xmlhttpPais = false;
		}
	} else if (window.ActiveXObject) {
		try {
			xmlhttpPais = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				xmlhttpPais = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {
				xmlhttpPais = false;
			}
		}
	}
	xmlhttpPais.open("POST", url, true);
	xmlhttpPais.setRequestHeader('Content-Type', contentType);
}
function renderComunidad(idDivContenedor, idControl, idParentControl) {
	var parent = document.getElementById(idParentControl);
	var contenedor = document.getElementById(idDivContenedor);
	if(parent == null) {
		contenedor.innerHTML = "no se encuentra el padre";
	} else {
		if(parent.value == "ESP" || parent.value == "ESPA�A" || parent.value == "ES") {
			createControl(idDivContenedor);
		} else {
			var control = "<input id=\"" + idControl + "\" name=\"" + idControl + "\" />";
			//alert(control);
			contenedor.innerHTML = "<input id=\"" + idControl + "\" name=\"" + idControl + "\" />";
		}
		
	}
}

function createControl(idDivContenedor) {
	var contenedor = document.getElementById(idDivContenedor);
	var url = "controlMunicipiosServer.jsp";
	var params = "mode=listComunidad";
	sendRequest(idDivContenedor, url, params, renderComunidades);
}

function sendRequest(idDiv, url, params, callback) {
	try {
		document.getElementById(idDiv).innerHTML = "cargando..."; //"<img src='./images/Loading8.gif'></img>";
		createHttpRequest(url);
		xmlhttp.onreadystatechange = callback;
		xmlhttp.send(params);
	} catch (e) {
		alert(e.message);
		document.getElementById(idDiv).innerHTML = "";
	}
}

function displayList(idDivContenedor, respuesta) {
	var contenedor = document.getElementById(idDivContenedor);
	document.getElementById(idDivContenedor).innerHTML = "<select><option>a</option><option>vvv</option></select>";
}

function renderComunidades(){
	var xmlDocument;
	var respuesta;
	if (xmlhttp.readyState == 4) {
		alert(xmlhttp.responseText);
		if (xmlhttp.status == 200) {
			respuesta = xmlhttp.responseText;
			displayList("comunidad01", respuesta);
		} else {
			document.getElementById("comunidad01").innerHTML = "Error al cargar el componente";
		}
	}
}

//------------------------------ old --------------------------------
function loadPaises() {
	var url = "/SIGEM_TramitacionWeb/jsp/controlMunicipiosServer.jsp";
	var params = "mode=pais";
	sendRequestPaises("divControlPaises", url, params);

}

function loadComunidad() {
	document.getElementById("divControlProvincias").innerHTML = "";
	document.getElementById("divControlMunicipios").innerHTML = "";
	var url = "/SIGEM_TramitacionWeb/jsp/controlMunicipiosServer.jsp";
	var params = "mode=comunidad";
	sendRequestComunidades("divControlComunidades", url, params);
	

}
function loadComunidadCatastro() {
	document.getElementById("divControlProvincias").innerHTML = "";
	document.getElementById("divControlMunicipios").innerHTML = "";
	var url = "/SIGEM_TramitacionWeb/jsp/controlMunicipiosServer.jsp";
	var params = "mode=comunidad&catastro=true";
	sendRequestComunidades("divControlComunidades", url, params);
	
}
function loadProvincia() {
	document.getElementById("divControlMunicipios").innerHTML = "";
	var codComunidad = document.getElementById("vComunidad").value;	
	var url = "/SIGEM_TramitacionWeb/jsp/controlMunicipiosServer.jsp";
	var params = "mode=provincia&codComunidad=" + codComunidad;
	sendRequestProvincias("divControlProvincias", url, params);
	
}
function loadProvinciaCatastro() {
	document.getElementById("divControlMunicipios").innerHTML = "";
	var codComunidad = document.getElementById("vComunidad").value;	
	var url = "/SIGEM_TramitacionWeb/jsp/controlMunicipiosServer.jsp";
	var params = "mode=provincia&codComunidad=" + codComunidad+"&catastro=true";
	sendRequestProvincias("divControlProvincias", url, params);
	
}

function loadMunicipio() {
	var codProvincia = document.getElementById("vProvincia").value;	
	var url = "/SIGEM_TramitacionWeb/jsp/controlMunicipiosServer.jsp";
	var params = "mode=municipio&codProvincia=" + codProvincia;
	sendRequestMunicipios("divControlMunicipios", url, params);
	
	
}

function loadComunidadNac() {
	document.getElementById("divControlProvinciasNacimiento").innerHTML = "";
	document.getElementById("divControlMunicipiosNacimiento").innerHTML = "";
	var url = "/SIGEM_TramitacionWeb/jsp/controlMunicipiosNacimientoServer.jsp";
	var params = "mode=comunidad";
	sendRequestComunidadesNac("divControlComunidadesNacimiento", url, params);
	
	
}

function loadCasillasAnio(){
	//document.getElementById("divCasillas").innerHTML = "";
	var select = document.getElementsByName("anio");
	var anioSeleccionado=select[0].options[select[0].selectedIndex].text;
	var url="controlCasillasAnioServer.jsp";
	var params="anio="+anioSeleccionado;
	sendRequestCasillaAnio("divCasillas", url, params);
}

function loadProvinciaNac() {
	document.getElementById("divControlMunicipiosNacimiento").innerHTML = "";
	var codComunidad = document.getElementById("vComunidadNacimiento").value;	
	var url = "/SIGEM_TramitacionWeb/jsp/controlMunicipiosNacimientoServer.jsp";
	var params = "mode=provincia&codComunidad=" + codComunidad;
	sendRequestProvinciasNac("divControlProvinciasNacimiento", url, params);
	
}

function loadMunicipioNac() {
	var codProvincia = document.getElementById("vProvinciaNacimiento").value;	
	var url = "/SIGEM_TramitacionWeb/jsp/controlMunicipiosNacimientoServer.jsp";
	var params = "mode=municipio&codProvincia=" + codProvincia;
	sendRequestMunicipiosNac("divControlMunicipiosNacimiento", url, params);
	var required = document.getElementById('divControlMunicipiosNacimiento').getAttribute('required');
	
}
function ocultaDiv(idDiv){
	document.getElementById(idDiv).style.display = "none";
}
//------------------ comunidades ----------------------
function sendRequestPaises(idDiv, url, params) {
	try {
		document.getElementById(idDiv).innerHTML = "cargando..."; //"<img src='./images/Loading8.gif'></img>";
		createHttpRequestPais(url);
		xmlhttpPais.onreadystatechange = handleResponsePaises;
		xmlhttpPais.send(params);
	} catch (e) {
		alert(e.message);
		document.getElementById(idDiv).innerHTML = "";
	}
}

function sendRequestComunidades(idDiv, url, params) {
	try {
		document.getElementById(idDiv).innerHTML = "cargando..."; //"<img src='./images/Loading8.gif'></img>";
		createHttpRequest1(url);
		xmlhttp1.onreadystatechange = handleResponseComunidades;
		xmlhttp1.send(params);
	} catch (e) {
		alert(e.message);
		document.getElementById(idDiv).innerHTML = "";
	}
}
function sendRequestComunidadesNac(idDiv, url, params) {
	try {
		document.getElementById(idDiv).innerHTML = "cargando..."; //"<img src='./images/Loading8.gif'></img>";
		createHttpRequest2(url);
		xmlhttp2.onreadystatechange = handleResponseComunidadesNac;
		xmlhttp2.send(params);
	} catch (e) {
		alert(e.message);
		document.getElementById(idDiv).innerHTML = "";
	}
}
function handleResponsePaises() {
	var xmlDocument;
	var respuesta;
	if (xmlhttpPais.readyState == 4) {
		//alert(xmlhttp.responseText);
		if (xmlhttpPais.status == 200) {
			respuesta = xmlhttpPais.responseText;
			document.getElementById("divControlPaises").innerHTML = respuesta;
			if(document.getElementById("divControlPaises").getAttribute("required")!=null
					&& document.getElementById("vPais")!=null){
				document.getElementById("vPais").setAttribute("required","1");
			}
		} else {
			document.getElementById("divControlPaises").innerHTML = "Error al cargar el componente";
		}
	}
}
function handleResponseComunidades() {
	var xmlDocument;
	var respuesta;
	if (xmlhttp1.readyState == 4) {
		//alert(xmlhttp.responseText);
		if (xmlhttp1.status == 200) {
			respuesta = xmlhttp1.responseText;
			document.getElementById("divControlComunidades").innerHTML = respuesta;
			if(document.getElementById("divControlComunidades").getAttribute("required")!=null
					&& document.getElementById("vComunidad")!=null){
				document.getElementById("vComunidad").setAttribute("required","1");
			}
			comunidadesCargadas = true;
		} else {
			document.getElementById("divControlComunidades").innerHTML = "Error al cargar el componente";
		}
	}
}
function handleResponseComunidadesNac() {
	var xmlDocument;
	var respuesta;
	if (xmlhttp2.readyState == 4) {
		//alert(xmlhttp.responseText);
		if (xmlhttp2.status == 200) {
			respuesta = xmlhttp2.responseText;
			document.getElementById("divControlComunidadesNacimiento").innerHTML = respuesta;
			if(document.getElementById("divControlComunidadesNacimiento").getAttribute("required")!=null
					&& document.getElementById("vComunidadNacimiento")!=null){
				document.getElementById("vComunidadNacimiento").setAttribute("required","1");
			}
		} else {
			document.getElementById("divControlComunidadesNacimiento").innerHTML = "Error al cargar el componente";
		}
	}
}
//------------------ provincias ----------------------

function sendRequestProvincias(idDiv, url, params) {
	try {
		document.getElementById(idDiv).innerHTML = "cargando..."; //"<img src='./images/Loading8.gif'></img>";
		createHttpRequest1(url);
		xmlhttp1.onreadystatechange = handleResponseProvincias;
		xmlhttp1.send(params);
	} catch (e) {
		alert(e.message);
		document.getElementById(idDiv).innerHTML = "Error al crear el componente";
	}
}
function sendRequestProvinciasNac(idDiv, url, params) {
	try {
		document.getElementById(idDiv).innerHTML = "cargando..."; //"<img src='./images/Loading8.gif'></img>";
		createHttpRequest2(url);
		xmlhttp2.onreadystatechange = handleResponseProvinciasNac;
		xmlhttp2.send(params);
	} catch (e) {
		alert(e.message);
		document.getElementById(idDiv).innerHTML = "Error al crear el componente";
	}
}

function sendRequestCasillaAnio(idDiv, url, params) {
	try {
		document.getElementById(idDiv).innerHTML = "cargando..."; //"<img src='./images/Loading8.gif'></img>";
		createHttpRequest2(url);
		xmlhttp2.onreadystatechange = handleResponseCasillasAnio;
		xmlhttp2.send(params);
	} catch (e) {
		alert(e.message);
		document.getElementById(idDiv).innerHTML = "Error al crear el componente";
	}
}
function handleResponseCasillasAnio() {
	var xmlDocument;
	var respuesta;
	if (xmlhttp2.readyState == 4) {
		//alert(xmlhttp.responseText);
		if (xmlhttp2.status == 200) {
			respuesta = xmlhttp2.responseText;
			document.getElementById("divCasillas").innerHTML = respuesta;
		} else {
			document.getElementById("divCasillas").innerHTML = "Error al cargar el componente";
		}
	}
}
function handleResponseProvincias() {
	var xmlDocument;
	var respuesta;
	if (xmlhttp1.readyState == 4) {
		//alert(xmlhttp.responseText);
		if (xmlhttp1.status == 200) {
			respuesta = xmlhttp1.responseText;
			document.getElementById("divControlProvincias").innerHTML = respuesta;
			if(document.getElementById("divControlProvincias").getAttribute("required")!=null
					&& document.getElementById("vProvincia")!=null){
				document.getElementById("vProvincia").setAttribute("required","1");
			}
		} else {
			document.getElementById("divControlProvincias").innerHTML = "Error al cargar el componente";
		}
	}
}
function handleResponseProvinciasNac() {
	var xmlDocument;
	var respuesta;
	if (xmlhttp2.readyState == 4) {
		//alert(xmlhttp.responseText);
		if (xmlhttp2.status == 200) {
			respuesta = xmlhttp2.responseText;
			document.getElementById("divControlProvinciasNacimiento").innerHTML = respuesta;
			if(document.getElementById("divControlProvinciasNacimiento").getAttribute("required")!=null
					&& document.getElementById("vProvinciaNacimiento")!=null){
				document.getElementById("vProvinciaNacimiento").setAttribute("required","1");
			}
		} else {
			document.getElementById("divControlProvinciasNacimiento").innerHTML = "Error al cargar el componente";
		}
	}
}
//------------------ municipios ----------------------

function sendRequestMunicipios(idDiv, url, params) {
	try {
		document.getElementById(idDiv).innerHTML = "cargando..."; //"<img src='./images/Loading8.gif'></img>";
		createHttpRequest1(url);
		xmlhttp1.onreadystatechange = handleResponseMunicipios;
		xmlhttp1.send(params);
	} catch (e) {
		alert(e.message);
		document.getElementById(idDiv).innerHTML = "Error al crear el componente";
	}
}
function sendRequestMunicipiosNac(idDiv, url, params) {
	try {
		document.getElementById(idDiv).innerHTML = "cargando..."; //"<img src='./images/Loading8.gif'></img>";
		createHttpRequest2(url);
		xmlhttp2.onreadystatechange = handleResponseMunicipiosNac;
		xmlhttp2.send(params);
	} catch (e) {
		alert(e.message);
		document.getElementById(idDiv).innerHTML = "Error al crear el componente";
	}
}
function handleResponseMunicipios() {
	var xmlDocument;
	var respuesta;
	if (xmlhttp1.readyState == 4) {
		if (xmlhttp1.status == 200) {
			respuesta = xmlhttp1.responseText;
			document.getElementById("divControlMunicipios").innerHTML = respuesta;
			if(document.getElementById("divControlMunicipios").getAttribute("required")!=null
					&& document.getElementById("vMunicipio")!=null){
				document.getElementById("vMunicipio").setAttribute("required","1");
			}
		} else {
			document.getElementById("divControlMunicipios").innerHTML = "Error al cargar el componente";
		}
	}
}
function handleResponseMunicipiosNac() {
	var xmlDocument;
	var respuesta;
	if (xmlhttp2.readyState == 4) {
		if (xmlhttp2.status == 200) {
			respuesta = xmlhttp2.responseText;
			document.getElementById("divControlMunicipiosNacimiento").innerHTML = respuesta;
			if(document.getElementById("divControlMunicipiosNacimiento").getAttribute("required")!=null
					&& document.getElementById("vMunicipioNacimiento")!=null){
				document.getElementById("vMunicipioNacimiento").setAttribute("required","1");
			}
		} else {
			document.getElementById("divControlMunicipiosNacimiento").innerHTML = "Error al cargar el componente";
		}
	}
}