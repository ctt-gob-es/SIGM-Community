happdig = null;
hdivdig = null;
nameDigital = null;

function initDIG() {
	happdig = document.getElementById("jcofreapplet");
	hdivdig = document.getElementById("contentjcofreapplet");
}

function beginDIG() {
	cleanDIG();
	PF('dialogAppletDig').show();
	happdig.init();
	happdig.setPageSize("A4");
	happdig.setMode("RGB_8");
	happdig.setResolution("200");
	listScannersDIG();
}

function endDIG() {
	cleanDIG();
	PF('dialogAppletDig').hide();
}
function cleanDIG() {
	hdivdig.innerHTML = "";
}

function aboutDIG() {
	happdig.about();
}

function cleanDIG() {
	hdivdig.innerHTML = "";
}

function listScannersDIG() {
	var theArray = JSON.parse(happdig.listScanners());
	if (theArray.length > 1){
		printlnDIG("Por favor, seleccione un scanner de la lista");
		printlnDIG("");
		for (var i = 0; i < theArray.length; i++){
			printlnDIG("<div style=\"text-align:center\"><a href='#' onclick=\"selectScannerDIG("+i+",'"+theArray[i]+"');return false;\">"+theArray[i]+"</a></div>");
	    }
	}else {
		if (theArray.length == 1){
			printlnDIG("El escanner disponible para la digitalizaci\u00F3n es: <b>"+theArray[0]+"</b>");
			printlnDIG("");
			printlnDIG("Nombre del fichero: <input type=\"text\"  name=\"nameDocDig\" id=\"nameDocDig\" maxlength=\"76\" value=\"\"/>");
			printlnDIG("");
			printlnDIG("<div style=\"text-align:center\"><input type=\"button\" onclick=\"scanDIG();\" name=\"scan\" id=\"scan\" value=\"Digitalizar\"/></b>");
			printlnDIG("");
		}
	}
}

function scanDIG() {
	if (document.getElementById('nameDocDig').value != null 
			&& document.getElementById('nameDocDig').value != ""){
		nameDigital = document.getElementById('nameDocDig').value;
		cleanDIG();
		printlnDIG("El escanner est\u00E1 procediendo a digitalizar el documento");
		happdig.scan();
		createPDFDocumentB64DIG();
	} else {
		cleanDIG();
		printlnDIG("Se ha producido un error al digitalizar el documento. No se ha introducido el nombre del documento");
		printlnDIG("Por favor vuelva a intentarlo");
		nameDigital = null;
	}
}

function selectScannerDIG(index, name) {
	happdig.selectScanner(index);
	cleanDIG();
	printlnDIG("El escanner disponible para la digitalizaci\u00F3n es: <b>"+name+"</b>");
	printlnDIG("");
	printlnDIG("Nombre del fichero: <input type=\"text\"  name=\"nameDocDig\" maxlength=\"76\" id=\"nameDocDig\" value=\"\"/>");
	printlnDIG("");
	printlnDIG("<div style=\"text-align:center\"><input type=\"button\" onclick=\"scanDIG();\" name=\"scan\" id=\"scan\" value=\"Digitalizar\"/></b>");
	
}

function createPDFDocumentB64DIG() {
	document.getElementById('documentDig').value = happdig.getPDFDocumentB64();
	if (document.getElementById('documentDig').value != null 
			&& document.getElementById('documentDig').value != ""){	
		endDIG();
		//abrimos el applet de firma
		beginWithDig(nameDigital);
		nameDigital = null;
	}else {
		cleanDIG();
		printlnDIG("Se ha producido un error al digitalizar el documento.");
		printlnDIG("Por favor vuelva a intentarlo");
		nameDigital = null;
	}
	return false;
}

function printlnDIG(txt){
	 div=document.createElement("DIV");
	 if (txt=="")
	  txt="&nbsp;";
	 div.innerHTML=txt;
	 hdivdig.appendChild(div);
	}