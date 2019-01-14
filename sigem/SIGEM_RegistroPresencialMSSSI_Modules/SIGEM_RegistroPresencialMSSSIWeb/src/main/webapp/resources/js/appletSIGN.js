happ=null;
hmaindiv=null;
isdigital=false;
isOtherSign =false;
nameDoc=null;
form=null;
sf={};



// NAVIGATOR INFORMATION
var browser = navigator.userAgent.toLowerCase();
var opera = (browser.indexOf("opera") >= 0);
var Chrome = !opera && (browser.indexOf("chrome") >= 0);
var safari = !Chrome && !opera && (browser.indexOf("safari") >= 0);
var ie = !opera && !safari && !Chrome
		&& ((browser.indexOf("msie") >= 0) || (browser.indexOf("boie") >= 0));
var ie11 = !opera
		&& !safari
		&& !Chrome
		&& ((browser.indexOf("trident") >= 0) || (browser.indexOf("rv:11") >= 0));
var firefox = !opera && !safari && !Chrome && !ie && !ie11
		&& (browser.indexOf("firefox") >= 0);
var linux = navigator.appVersion.indexOf("Linux") != -1
		|| navigator.appVersion.indexOf("X11") != -1;

function getBrowserSignSource() {
	var source = "";
	if (opera) {
		source = "InternetExplorer";
	} else if (Chrome) {
		source = "InternetExplorer";
	} else if (safari) {
		source = "MacOSX";
	} else if (ie || ie11) {
		source = "InternetExplorer";
	} else if (firefox) {
		source = "InternetExplorer";
	}
	return source;
}


// -----------------------------------------------------------------------------
// --                                                                         --
// --                                                                    INIT --
// --                                                                         --
// -----------------------------------------------------------------------------

function initAppletSign(paramform){
 happ=document.getElementById("jsapplet");

 form=paramform;
}

function initAppletOtherSign(paramform){
	if (happ != null){
		happ=document.getElementById("jsapplet");
	}
	 form=paramform;
}

// -----------------------------------------------------------------------------
// --                                                                         --
// --                                                                SECTIONS --
// --                                                                         --
// -----------------------------------------------------------------------------

function begin(){
 hdiv=document.getElementById("contentapplet");
 clean();
 PF('dialogAppletSign').show();
 isdigital=false;
 $('#accordion\\:'+form+'\\:contentDocument').val('');
 $('#accordion\\:'+form+'\\:signDocument').val('');
 $('#accordion\\:'+form+'\\:observationDocument').val('');
 $('#accordion\\:'+form+'\\:nameDocument').val('');
 $('#accordion\\:'+form+'\\:certificateDocument').val('');

 doSignSource(getBrowserSignSource());
}

function beginOtherSign(){
	 hdiv=document.getElementById("contentappletOtherSign");
	 clean();
	 PF('dialogOtherSign').show();
	 isdigital=false;
	 isOtherSign = true;
	 $('#accordion\\:'+form+'\\:contentDocumentOtherSign').val('');
	 $('#accordion\\:'+form+'\\:signDocument').val('');
	 $('#accordion\\:'+form+'\\:observationDocument').val('');
	 $('#accordion\\:'+form+'\\:nameDocument').val('');
	 $('#accordion\\:'+form+'\\:certificateDocument').val('');
	 showSignPanelOtherSign();
	
	}

function beginWithDig(name){
	 clean();
	 PF('dialogAppletSign').show();
	 isdigital=true;
	 nameDoc = name +".PDF";
	 $('#accordion\\:'+form+'\\:contentDocument').val('');
	 $('#accordion\\:'+form+'\\:signDocument').val('');
	 $('#accordion\\:'+form+'\\:observationDocument').val('');
	 $('#accordion\\:'+form+'\\:nameDocument').val('');
	 $('#accordion\\:'+form+'\\:certificateDocument').val('');
	 doSignSource(getBrowserSignSource());
	}


function end(){
	clean();
	isdigital=false;
	PF('dialogAppletSign').hide();
	//happ.releaseTask(sf.tsk);
	happ.gc();
}

function endOtherSign(){
	clean();
	isdigital=false;
	PF('dialogOtherSign').hide();
	happ.gc();
}

function doSignSource(src){
 clean();
 happ.setSignSource(src);
 if (!happ.isInitiedSignEngine())
  happ.initSignEngine();
 if (src=="DNIe"){
  println("Insert DNI on the reader, and wait until led stops.");
  println("Write DNIe password:");
  println("<input id='passwd' type='password' />");
  gLink("Ok","doDNIe()");
 }else{
  //happ.setFirefoxStoePath("/home/ubuntu/.mozilla/firefox/4s8rs39p.default/");
  //happ.setFirefoxInstallPath("/usr/lib/firefox/");
  println("Seleccione el Certificado:");
  println("<br/>");
  gLinksPro(eval("("+happ.listCerts()+")"),"alias","display","#V#","doCertificate('#K#')");
 }
}

function doSignSourceOtherSign(src){
	 clean();
	 println("El fichero que ha subido no est\u00E1 firmado.");
	 happ.setSignSource(src);
	 if (!happ.isInitiedSignEngine())
	  happ.initSignEngine();
	 if (src=="DNIe"){
	  println("Insert DNI on the reader, and wait until led stops.");
	  println("Write DNIe password:");
	  println("<input id='passwd' type='password' />");
	  gLink("Ok","doDNIe()");
	 }else{
	  //happ.setFirefoxStoePath("/home/ubuntu/.mozilla/firefox/4s8rs39p.default/");
	  //happ.setFirefoxInstallPath("/usr/lib/firefox/");
	  println("Seleccione el Certificado con el que se firmar\u00E1:");
	  println("<br/>");
	  gLinksPro(eval("("+happ.listCerts()+")"),"alias","display","#V#","doCertificateOtherSign('#K#')");
	 }
	}

function doDNIe(){
 happ.setPassword(document.getElementById("passwd").value);
 sf.tsk=happ.newTask();
 showSignPanel();
}

function doCertificate(alias){
 happ.setCertAlias(alias);
 sf.tsk=happ.newTask();
 showSignPanel();
}

function doCertificateOtherSign(alias){
	 happ.setCertAlias(alias);
	 showSignPanelOtherSign2();
	}

function showSignPanelOtherSign(){
	 clean();
	 sf.tsk=happ.newTask();
	 happ.setPreview(false);
	 isLoad = true;

	println("Seleccionen el fichero que desea adjuntar.");
	isLoad = happ.loadFileChoose(sf.tsk);

	 if (!isLoad || !happ.isAllowedSize(sf.tsk)){
		 clean();
		 println("No se ha seleccionado ning\u00FAn fichero o el fichero seleccionado ha superado los 10 MB.");
		 
	 }else {
		 if (happ.getFileName().length > 80){
			 clean();
			 println("El nombre del fichero a adjuntar no puede tener mas de 80 caracteres.");
			 println("Por favor reduzca el tama\u00f1o del nombre del documento y vuelva a seleccionarlo.");
		 } else {
			 if (duplicate (happ.getFileName().split('.')[0])){
				 clean();
				 println("Ya existe un documento en el registro con ese nombre.");
				 println("Por favor cambie el nombre del documento y vuelva a seleccionarlo.");
			 }
			 else {
				 var extension =  happ.getFileName().split('.').pop();
				 
				 if (extension.toUpperCase() == 'PDF' || isdigital){
					 happ.asPDF(sf.tsk);
					 happ.setAttachment('ATTACHED');
				 }else {
					 happ.asCAdES(sf.tsk);
					 happ.setAttachment('DETACHED');
				 }
			 
				 numFileContent = happ.getNumberOfSplitedBase64(sf.tsk);
				 var content = '[';
				 
				 for ( numFile = 1; numFile <= numFileContent ;numFile ++){
					 content += '{  "block" : "'+happ.returnBase64(sf.tsk, numFile) +'"}';
					 if (numFile < numFileContent){
						 content += ','; 
					 }
				 }
				 content += ']';
			 
				 $('#accordion\\:'+form+'\\:contentDocument').val(content);
				 clean();
				 if (happ.isSigned(sf.tsk)){
					 showSignPanelOtherSign3('N');
				 }else {
					 doSignSourceOtherSign(getBrowserSignSource());
				 }
			 }
		 }
	 }
}


function showSignPanelOtherSign2(){
	 clean();
	 happ.setPreview(true);
	 isLoad = true;
	 if (happ.sign(sf.tsk)){
		 
		 if (happ.getLastError()!= null && happ.getLastError()!= ""){
			 println("<span>Se ha producido un error en la firma del documento.</span>");
			 println("Si el error persiste reporte una incidencia.");
			 println("Detalle del error:");
			 println(happ.getLastError());
		 }else {
			 showSignPanelOtherSign3('S');
		 }
	 } else {
		 if (happ.getLastError()!= null && happ.getLastError()!= ""){
			 println("<span>Se ha producido un error en la firma del documento.</span>");
			 println("Si el error persiste reporte una incidencia.");
			 println("Detalle del error:");
			 println(happ.getLastError());
		 }else {
			 println("<span>Se ha cancelado el proceso de adjuntaci\u00F3n y firma del documento.</span>");
		 }
	 }

}

function showSignPanelOtherSign3(owner){
	if (owner  == 'S'){
		println("<div class='signFileOk'>El fichero se ha firmado correctamente.</div>");
	}else {
		println("<div class='signFileOk'>El fichero ya est\u00E1 firmado.</div>");
	}
	 println("Nombre del Fichero:&nbsp;&nbsp;" +happ.getFileName());
	 println("");
	 println("Validez del documento:"+"<select id='validezDoc' style='margin-left: 50px;margin-bottom:10px'>" +
	 		"<option value='01'>Copia</option>" +
	 		"<option value='02'>Copia compulsada</option>" +
	 		"<option value='03'>Copia original</option>" +
	 		"<option value='04'>Original</option>" +
	 		"</select>");
	 println("<div style='display: inline;vertical-align: top;'>Observaciones del documento:</div>" +
	 		"&nbsp;&nbsp;"+"<textarea id='observacionesDoc' style='display:inline;height: 60px;width:200px' maxlength='50'></textarea>");
	 

	 numFileResult = happ.getNumberOfSplitedBase64(sf.tsk);
	 var signContent = '[';
	 
	 for ( numFile = 1; numFile <= numFileResult ;numFile ++){
		 signContent += '{  "block" : "'+happ.returnBase64(sf.tsk, numFile) +'"}';
		 if (numFile < numFileResult){
			 signContent += ','; 
		 }
	 }
	 signContent += ']';
	 
	 $('#accordion\\:'+form+'\\:certificateDocument').val('');
	 var extension =  happ.getFileName().split('.').pop();
	 if (extension.toUpperCase() != 'PDF'){
		 $('#accordion\\:'+form+'\\:signDocument').val(signContent);
	 }else {
		 $('#accordion\\:'+form+'\\:contentDocument').val(signContent);
	 }
	 $('#accordion\\:'+form+'\\:nameDocument').val(happ.getFileName());
	 
	 document.getElementById('contentDocument').style="display:inline;width:90%;font-size: 0.8em;";
	 document.getElementById('accordion:'+form+':buttonAddDocumentOtherSign').setAttribute('style','display:inline;font-size: 75%');

}
function showSignPanel(){
	 clean();
	 happ.setPreview(true);
	 isLoad = true;

	 if (isdigital){
		 happ.setFileName(nameDoc);
		 isLoad = happ.loadBase64(sf.tsk, document.getElementById('documentDig').value);
	 }else {
		 println("Seleccionen el fichero que desea adjuntar.");
		 isLoad = happ.loadFileChoose(sf.tsk);
	 }

	 if (!isLoad || !happ.isAllowedSize(sf.tsk)){
		 clean();
		 println("No se ha seleccionado ning\u00FAn fichero o el fichero seleccionado ha superado los 10 MB.");
		 
	 }else {
		 if (happ.getFileName().length > 80){
			 clean();
			 println("El nombre del fichero a adjuntar no puede tener mas de 80 caracteres.");
			 println("Por favor reduzca el tama\u00f1o del nombre del documento y vuelva a seleccionarlo.");
		 } else {
			 if (duplicate (happ.getFileName().split('.')[0])){
				 clean();
				 println("Ya existe un documento en el registro con ese nombre.");
				 println("Por favor cambie el nombre del documento y vuelva a seleccionarlo.");
			 }
			 else {
				 var extension =  happ.getFileName().split('.').pop();
				 if (extension.toUpperCase() == 'PDF' || isdigital){
					 happ.asPDF(sf.tsk);
					 happ.setAttachment('ATTACHED');
				 }else {
					 happ.asCAdES(sf.tsk);
					 happ.setAttachment('DETACHED');
				 }
			 
				 numFileContent = happ.getNumberOfSplitedBase64(sf.tsk);
				 var content = '[';
				 
				 for ( numFile = 1; numFile <= numFileContent ;numFile ++){
					 content += '{  "block" : "'+happ.returnBase64(sf.tsk, numFile) +'"}';
					 if (numFile < numFileContent){
						 content += ','; 
					 }
				 }
				 content += ']';
			 
				 $('#accordion\\:'+form+'\\:contentDocument').val(content);
				 clean();
				 if (happ.sign(sf.tsk)){
					 
					 if (happ.getLastError()!= null && happ.getLastError()!= ""){
						 println("<span>Se ha producido un error en la firma del documento.</span>");
						 println("Si el error persiste reporte una incidencia.");
						 println("Detalle del error:");
						 println(happ.getLastError());
					 }else {
						 println("<div class='signFileOk'>El fichero se ha firmado correctamente.</div>");
						 println("Nombre del Fichero:&nbsp;&nbsp;" +happ.getFileName());
						 println("");
						 println("Validez del documento:"+"<select id='validezDoc' style='margin-left: 50px;margin-bottom:10px'>" +
						 		"<option value='01'>Copia</option>" +
						 		"<option value='02'>Copia compulsada</option>" +
						 		"<option value='03'>Copia original</option>" +
						 		"<option value='04'>Original</option>" +
						 		"</select>");
						 println("<div style='display: inline;vertical-align: top;'>Observaciones del documento:</div>" +
						 		"&nbsp;&nbsp;"+"<textarea id='observacionesDoc' style='display:inline;height: 60px;width:200px' maxlength='50'></textarea>");
						 
		
						 numFileResult = happ.getNumberOfSplitedBase64(sf.tsk);
						 var signContent = '[';
						 
						 for ( numFile = 1; numFile <= numFileResult ;numFile ++){
							 signContent += '{  "block" : "'+happ.returnBase64(sf.tsk, numFile) +'"}';
							 if (numFile < numFileResult){
								 signContent += ','; 
							 }
						 }
						 signContent += ']';
						 
						 $('#accordion\\:'+form+'\\:certificateDocument').val(happ.getCertificateB64());
				
						 if (extension.toUpperCase() != 'PDF'){
							 $('#accordion\\:'+form+'\\:signDocument').val(signContent);
						 }else {
							 $('#accordion\\:'+form+'\\:contentDocument').val(signContent);
						 }
						 $('#accordion\\:'+form+'\\:nameDocument').val(happ.getFileName());
						 
						 document.getElementById('contentDocument').style="display:inline;width:90%;font-size: 0.8em;";
						 document.getElementById('accordion:'+form+':buttonAddDocument').setAttribute('style','display:inline;font-size: 75%');
			
					 }
				 } else {
					 if (happ.getLastError()!= null && happ.getLastError()!= ""){
						 println("<span>Se ha producido un error en la firma del documento.</span>");
						 println("Si el error persiste reporte una incidencia.");
						 println("Detalle del error:");
						 println(happ.getLastError());
					 }else {
						 println("<span>Se ha cancelado el proceso de adjuntaci\u00F3n y firma del documento.</span>");
					 }
				 }
			 }
		 }
	 }
}


function addDocument (){
	$('#accordion\\:'+form+'\\:observationDocument').val($('#observacionesDoc').val());
	PF('validateDocument').selectValue($('#validezDoc').val());
}
function tskSign(){

}

function tskSHA1(){
}

function closeDigAppletSIGN(){
	
}

// -----------------------------------------------------------------------------
// --                                                                         --
// --                                                                 TOOLKIT --
// --                                                                         --
// -----------------------------------------------------------------------------

function clean(){
 hdiv.innerHTML="";
 document.getElementById('contentDocument').style="display:none;width:90%;font-size: 0.8em;";
 document.getElementById('accordion:'+form+':buttonAddDocument').style="display:none;font-size: 75%;";
 document.getElementById('accordion:'+form+':buttonAddDocumentOtherSign').style="display:none;font-size: 75%;";
}


function println(txt){
 div=document.createElement("DIV");
 if (txt=="")
  txt="&nbsp;";
 div.innerHTML=txt;
 hdiv.appendChild(div);
}

function gLink(txt,action){
 println("<a href='#' onclick=\""+action+";return false;\">"+txt+"</a>");
}

function gLinks(data,text,action){ // use #V# as value, #I# as index.
 for(i in data){
  v=data[i];
  txti=text.replace("#I#",i).replace("#V#",v);
  txtv=action.replace("#I#",i).replace("#V#",v);
  gLink(txti,txtv);
 }
}

function gLinksPro(data,key,value,text,action){ // use #V# as value, #I# as index, #K# as key.
 for(i in data){
  o=data[i];
  k=o[key];
  v=o[value];
  k=fixGenitive(k);
  txti=text.replace("#I#",i).replace("#K#",k).replace("#V#",v);
  txtv=action.replace("#I#",i).replace("#K#",k).replace("#V#",v);
  gLink(txti,txtv);
 }
}

function fixGenitive(s){
 i=s.length;
 while (i>0){
  //if (s[i]=="'")
  if (s.substring(i,i+1)=="'") // Thanks IE
   s=s.substring(0,i)+"\\"+s.substring(i);
  i--;   
 }
 return s;
}

function duplicate (nameDocument){
	var exist = false;
	var arrayDocs =JSON.parse(document.getElementById("accordion:"+form+":listNameDocument").value);
	var arrayLength = arrayDocs.length;
	for (var i = 0; i < arrayLength; i++) {
		if (arrayDocs[i].NameDoc == nameDocument){
			exist = true;
		}
	}
	return exist;
}