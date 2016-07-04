/*
* Copyright 2014 Ministerio de Industria, Energía y Turismo 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la <<Licencia>>); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/
var CON_CAMBIOS = "1";

function cambiosSinGuardar(){
	if(document.forms[0].cambios && document.forms[0].cambios.value == CON_CAMBIOS) return true;
	else return false;
}

function actualizaEstado(){
	if(cambiosSinGuardar()) window.status ="Cambios Pendientes de Grabar";
}

function cambio(){
	if(document.forms[0].cambios) document.forms[0].cambios.value = CON_CAMBIOS;
}

function llamadaActionComprobarCambios(action,comprobarCambios,msgCambios) {
	if(comprobarCambios && cambiosSinGuardar()){
		if(window.confirm(msgCambios)){
			llamadaAction(action);
		}
	}
	else llamadaAction(action);
	
}


function addControlCambios(){
	if(!cambiosSinGuardar()){
		var formulario = document.forms[0];
		                            	                                  
		if(formulario){
			  var campos = formulario.getElementsByTagName("input");
			  if(campos){
				  for(var i=0; i<campos.length; i++) {
					  campo = campos[i];
					  if(campo){
						 if(campo.type == "text" || campo.type== "checkbox" || campo.type=="radio"){
							 addEvento(campo, "change", function(){cambio();});
						 }
					  }
				  }
			  }

			  var textAreas = formulario.getElementsByTagName("textarea");
			  if(textAreas){
				  for(var j=0; j<textAreas.length; j++) {
					  textArea = textAreas[j];
					  if(textArea){
						  addEvento(textArea, "change", function(){cambio();});
					  }
				  }
			  }
			  
			  var selects = formulario.getElementsByTagName("select");
			  if(selects){
				  for(var k=0; k<selects.length; k++) {
					  select = selects[k];
					  if(select){
						  addEvento(select, "change", function(){cambio();});
					  }
				  }
			  }
		}
	}
}

function addEvento(elemento, evento, funcion) {
	if (elemento.addEventListener) {
		elemento.addEventListener(evento, funcion, false);
    } else {
    	elemento.attachEvent("on"+evento, funcion);
    }
}