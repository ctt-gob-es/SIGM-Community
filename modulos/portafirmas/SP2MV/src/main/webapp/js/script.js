/******************menu principal activados************/
function menuActivado(ider,menu,clase1,clase2){
// definimos la variable con la que trabajaremos nuestro contenedor del menu
idmenu = document.getElementById(menu);

// Aqui comprobamos con document.all para IE y else para gecko
// enseguida extraemos todas las TAGS div ( en este caso ) que esten dentro de nuestro contenedor
if (document.all) {
ded = idmenu.all.tags("div");
} else {
ded = idmenu.getElementsByTagName('div');
}

// contamos el numero de tags div dentro del contenedor y las tratamos una por una
for (i=0;i<=ded.length;i++) {

// si el tag que estemos tratando en ese momento es igual al que envio la peticion se activa la clase especial
if(ded[i] == ider){ ded[i].className=clase1; }

// en el else se trataran TODOs los elementos del menu, menos el que envio la peticion, se debolvera a la clase base
else{
// lo regresamos a la clase 2 ( de inicio )
ded[i].className=clase2;
}
}
}
// libre de " ver " cualquier error de pagina... �� molesto !
function detenerError(){return true} window.onerror=detenerError;

function showUrl(){		
	 document.forms[0].nodeUrl.value = document.forms[0].connector[document.forms[0].connector.selectedIndex].value; 
}
function showEidasUrl(){
	var i;
	var idx=0;
	for(i=0;i<document.forms.length;i++){
		if(document.forms[i].eidasconnector!=null){
			idx=i;
			break;
		}
	}
	document.forms[idx].nodeUrl.value = document.forms[idx].eidasconnector[document.forms[idx].eidasconnector.selectedIndex].value; 
}


function showUrl2(){	
	 document.forms[2].nodeUrl2.value = document.forms[2].connector2[document.forms[2].connector2.selectedIndex].value; 
}

function ajaxResignSubmit(formId, postUrl, onSuccessHandler, onErrorHandler){
    //setSAMRequestMethod();
    var formData = $("#"+formId).serialize();
    $.ajax({
        type: "POST",
        url: postUrl,
        cache: false,
        data: formData,
        success: onSuccessHandler,
        error: onErrorHandler
    });

    return;
}
function ajaxChangeHttpMethod(formId, postUrl, onSuccessHandler, onErrorHandler){
    setSAMRequestMethod();
	ajaxResignSubmit(formId, postUrl, onSuccessHandler, onErrorHandler);
    return;
}

function receiveSignedRequest(result){
    document.forms[1].samlRequestXML.value=result;
	callEncodeSAMLRequest();
}

function errorAjaxRequest(result){
	alert("Error performing resign ");
}
function setSAMRequestMethod(){
    if(document.forms[0].getmethod.checked)
        document.forms[0].method="GET";
    else
        document.forms[0].method="POST";
    document.forms[1].samlRequestBinding.value=document.forms[0].method;
    return true;
}
function signAndEncodeSAMLRequest(){
	ajaxResignSubmit('samlRequestXML','reSign.action', receiveSignedRequest,errorAjaxRequest);
}

function enableEncoding(enable){
//	if(document.getElementById("encodeButton")!=null)
//		document.getElementById("encodeButton").disabled= !enable;
}
function callEncodeSAMLRequest(){
	encodeSAMLRequest();
	enableEncoding(false);
}

function samlRequestXMLChanged(){
	enableEncoding(true);
}


