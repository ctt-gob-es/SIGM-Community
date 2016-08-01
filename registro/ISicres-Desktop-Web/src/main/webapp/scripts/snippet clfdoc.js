//Escanea una imagen y mete el fichero dentro del menu de documentos
function escanear() {
	IsClickElem = true; //Indicar que estamos trabajando sobre un elemento del menu, cuando salimos se deberia de establecer a false
                       //pero no hay forma de controlar cuando se sale, y si lo establecemos a "false" al final de la funcion entonces
                       //se pierde el tipo de elemento sobre el que hemos hecho "click" y daria un error al añadir los ficheros escaneados.

	
	var URL = top.g_URL + "/escanear.jsp?sessionPId=" + top.g_SessionPId+"&idLibro="+top.g_ArchiveId.toString()+"&idRegistro="+top.g_FolderId.toString();
	window.open(URL, "FolderFormCompulsaData","location=no",true);

					   
	realizarPoolFicherosEscaneados();
		
	HideMenu();
}


function realizarPoolFicherosEscaneados() {
	var ficherosYaMostrados = new Array();
	
	clearInterval(intervalConsultaEscaneados);
	intervalConsultaEscaneados = window.setInterval(function() {				 
			var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if (xhttp.readyState == 4 && xhttp.status == 200) {
					var ficherosTempSubidos = JSON.parse(xhttp.responseText);
					if (ficherosTempSubidos.length > ficherosYaMostrados.length) {
						addFicherosEscaneados(calcularFicherosNuevos(ficherosTempSubidos, ficherosYaMostrados));
						ficherosYaMostrados = ficherosTempSubidos;						
					}
					else if (ficherosTempSubidos.length < ficherosYaMostrados.length) {
						//Significa que había listados subidos, pero ya no, con lo que se guardaron los cambios
						clearInterval(intervalConsultaEscaneados);
					}
				}
			};
			xhttp.open("GET", "FileUploadScanInfo?SessionPId=" +top.g_SessionPId + "&FolderId=" + top.g_FolderId, true);
			xhttp.send();
		}, 10000);
}

function calcularFicherosNuevos(ficherosConsultados, ficherosRegistrados) {
	var ficherosNuevos = new Array();
	for(var i=0; i<ficherosConsultados.length; i++) {						
		if (ficherosRegistrados.indexOf(ficherosConsultados[i]) == -1) {
			ficherosNuevos.push(ficherosConsultados[i]);
		}
	}	
	return ficherosNuevos;
}
