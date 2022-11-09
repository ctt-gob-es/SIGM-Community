var organosAdded = 0;
var adicionalesAdded = 0;
$(document).ready(function() {
	$("#documentTabs").tabs();
	$("#toggleRequestInfo").click(function() {
		$(this).parent().children("#requestInfo").slideToggle("slow");
		$("#toggleRequestInfoButton").toggleClass("is-open");
	});
	
	var position = $('#indexPosition').val();
	var lastPosition = $('#lastPosition').val();
	if (position == 1) {
		$('#previousRequest').attr("style", "display: none");
	} else {
		$('#previousRequest').attr("style", "display: ");
	}
	if (position == lastPosition) {
		$('#nextRequest').attr("style", "display: none");
	} else {
		$('#nextRequest').attr("style", "display: ");
	}

	//Se crea el diálogo para confirmaciones
	$("#confirmRegenerate").dialog({
		autoOpen : false,
		resizable : false,
		modal : true,
		create : customModalStyle,
		close : function() {
		}
	});
	
	loadModalMetadata();
	
	loadModalEmail();

});

function loadModalMetadata() {

	//Se crea el diálogo de la ventana modal de Administración de Etiquetas
	$("#metadataEni")
			.dialog(
					{
						autoOpen : false,
						resizable : false,
						//height:450,
						width : 550,
						modal : true,
						buttons : [
								{
									text : "Cancelar",
									"class" : 'secondary cancel_reject',
									click : function() {
										$(this).dialog("close");
									}
								},
								{
									text : "Aceptar",
									"class" : 'primary start_reject',
									click : function() {
										generateEni();
									}
								} ],
						//Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
						create : function() {
							$(this).closest(".ui-dialog").find(
									".btn_dlg_primary").removeClass().addClass(
									"simbutton primary");
							$(this).closest(".ui-dialog").find(
									".btn_dlg_secondary").removeClass()
									.addClass("simbutton secondary");
						},
						open : function() {
							//Cargar combos, listados
						},
						close : function() {

						}
					});

	// Se configuran los calendarios
	$(".datepicker").datepicker();

	$.mask.definitions['h'] = "[0-2]";
	$.mask.definitions['t'] = "[0-5]";

	$('#fcaptura').mask("99/99/9999");
}


function loadModalEmail() {
	//Se crea el diálogo para seleccionar los tipos de documentos a enviar y los destinatarios
	$("#sendEmailDialog").dialog({
	    autoOpen: false,
	    resizable: false,
	    height: 550,
	    width: 680,
	    modal: true,       
	    buttons: [
	        {
	        	text: "Cancelar",
	            "class": 'secondary cancel_reject',
	            "id" : 'botonCancelar',
	            click: function() {
	            	$(this).dialog("close");
	            }
	        },
	        {
	        	text : "Enviar",
				"class" : 'primary start_reject',
				"id" : 'botonEnviar',
				click : function() {
					sendEmailTo();
				}
	        }
	    ],   
	    //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
	    create:function () {               
	        $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	        $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
	    },
	    open: function(){
	    	
	    },
	    close:function () {
	       
	    }
	});
}


$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

function findPropertyNameByRegex(o, r) {
	var todasLasPropiedades = [];
	  for (var key in o) {
	    if (key.match(r)) {
	    	todasLasPropiedades.push(key); 
	    }
	  }
	  return todasLasPropiedades;
};

function generateEni () {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	
	$('#errorMsgEni').html('');
	$('errorMsgEni').hide();
	desHabilitarOrganos(false);
	
	$.ajax({
		url: $("#metadataEniForm")[0].action,
		data: $("#metadataEniForm").serialize(),
		type: "post",
    	dataType: 'json',
    	async: false,
    	success: function(data) {
    		desHabilitarOrganos(true);
    		
    		if (data.message == null) {
	    		var dataBytes = convertToBase64ToByteArray(data.content);
	    		var blob1 = new Blob([ dataBytes ], {
					type : data.mime
				});
				var fileName = data.name;
				saveAs(blob1, fileName);
    		} else {
    			$('#errorMsgEni').html(data.message);
          		$('errorMsgEni').show();
    		}
    		
          	jsAjaxStatus.stopAjax();
    	},
      	error: function error(jqXHR, textStatus, errorThrown) {
      		desHabilitarOrganos(true);
      		
      		$('#errorMsgEni').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
      		$('errorMsgEni').show();
      		jsAjaxStatus.stopAjax();
  		}
	});
}

// Confirmación para el borrado de un e-mail
function prepareRegenerateReport(regenerateChash) {
	$(window).scrollTop(0);
	$('#confirmRegenerateMessage').html('Si el informe se generó dañado. Puede volver a generarlo, ¿desea continuar?');
	$('#confirmRegenerate').dialog("option", "buttons", [getNoButton(), getRegenerateButton(regenerateChash) ]);
	$('#confirmRegenerate').dialog('open');
}

function prepareAnullation(requestTag, anulation){
	$(window).scrollTop(0);
	if(anulation === true){
		$('#confirmRegenerateMessage').html('¿Está usted seguro de que desea anular las firmas de la petición seleccionada?');
		$('#confirmRegenerate').dialog("option", "buttons", [getNoButton(), getAnulledSign(requestTag) ]);
	}else{
		$('#confirmRegenerateMessage').html('¿Está usted seguro de que desea recuperar las firmas de la petición anulada?');
		$('#confirmRegenerate').dialog("option", "buttons", [getNoButton(), getRecoverSign(requestTag) ]);
	}
	$('#confirmRegenerate').dialog('open');
}

function getAnulledSign(requestTag){
	return {
		text : "Sí",
		"class" : 'primary',
		click : 
			function() {
				signAnullation(requestTag);
			}
	};
}

function getRecoverSign(requestTag){
	return {
		text : "Sí",
		"class" : 'primary',
		click : 
			function() {
				signRecovering(requestTag);
			}
	};
}

function signAnullation(requestTag) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({
		url: "inbox/doAnullation",
		data: 'requestTagHash=' + requestTag + '&anulled=' + true,
    	dataType: 'json',
    	async: true,
    	success: function(model) {
    		if (model.status == "success") {
    			showInfo("Las firmas de la petición seleccionada se han anulado correctamente.");
    		} else {
    			showError(genericMessage);
    		}
    		$('#confirmRegenerate').dialog('close');
    		enviarFormulario('inboxForm', 'loadInbox',
					[{'parameterId':'requestBarSelected', 'parameterValue':  $('#requestBarSelected').val()},
				     {'parameterId':'currentPage', 'parameterValue': $('#currentPage').val()},
				     {'parameterId':'searchFilter', 'parameterValue': $('#searchFilter').val()},
				     {'parameterId':'appFilter', 'parameterValue': $('#appFilter').val()}]);
    		jsAjaxStatus.stopAjax();
    	},
	  	error: function error(jqXHR, textStatus, errorThrown) {
	  		showError(genericMessage);
	  		jsAjaxStatus.stopAjax();
	  	}
	});	
}

function signRecovering(requestTag) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({
		url: "inbox/doAnullation",
		data: 'requestTagHash=' + requestTag + '&anulled=' + false,
		dataType: 'json',
		async: true,
		success: function(model) {
    		if (model.status == "success") {
    			showInfo("Las firmas de la petición anulada seleccionada se han recuperado correctamente.");
    		} else {
    			showError(genericMessage);
    		}
    		$('#confirmRegenerate').dialog('close');
    		enviarFormulario('inboxForm', 'loadInbox',
					[{'parameterId':'requestBarSelected', 'parameterValue':  $('#requestBarSelected').val()},
				     {'parameterId':'currentPage', 'parameterValue': $('#currentPage').val()},
				     {'parameterId':'searchFilter', 'parameterValue': $('#searchFilter').val()},
				     {'parameterId':'appFilter', 'parameterValue': $('#appFilter').val()}]);
    		jsAjaxStatus.stopAjax();
	    	},
	  	error: function error(jqXHR, textStatus, errorThrown) {
	  		showError(genericMessage);
	  		jsAjaxStatus.stopAjax();
		}
	});	
	}


//Obtiene el objeto que configura la acción de eliminar e-mail
function getRegenerateButton(regenerateChash) {
	return {
		text : "Sí",
		"class" : 'primary',
		click : function() {
			$(this).dialog("close");
			$('#regenerateChash').val(regenerateChash);
			jQuery('#regenerateForm').submit();
		}
	};
}

// Metadatos ENI
function addOrganoDoc(valor) {
	if (valor.trim() != "") {
		if (organosAdded == '') {
			organosAdded = 0;
		}

		var key_input = document.createElement("input");
		var remove_button = document.createElement("button");
		var div = document.createElement("div");

		key_input.setAttribute("type", "text");
		key_input.setAttribute("name", "metadatosEni.organo[" + organosAdded
				+ "]");
		key_input.setAttribute("id", "metadatosEni.organo" + organosAdded);
		key_input.setAttribute("class", "sticked-input-right");
		key_input.setAttribute("disabled", "disabled");
		key_input.setAttribute("value", valor);

		var a = $('<a />');
		a.attr("id", "removeOrganoButton_" + organosAdded);
		a.attr("href", "javascript:void(0)");
		a.attr("onclick", "removeOrganoDoc(" + organosAdded + ")");
		a.html('<span class="mf-icon mf-icon-delete-16">Eliminar</span>');

		div.setAttribute("id", "divOrgano_" + organosAdded);

		$("#listOrganos").append(div);

		$("#divOrgano_" + organosAdded).append(key_input);
		$("#divOrgano_" + organosAdded).append(a);

		$('#organos').val('');
		organosAdded++;
	}else{
		$('#errorMsgEni').html("Debe introducir el valor del órgano que quiere añadir.");
  		$('errorMsgEni').show();	
	}
}

function removeOrganoDoc(position) {
	//document.getElementById("metadatosEni.organo" + position).remove();
	$("#metadatosEni.organo" + position).remove();
	$("#removeOrganoButton_" + position).remove();
	$("#divOrgano_" + position).remove();
	while (organosAdded - 1 > position) {
		var aux = position + 1;
		document.getElementById("metadatosEni.organo" + aux).setAttribute(
				"name", "metadatosEni.organo[" + position + "]");
		document.getElementById("metadatosEni.organo" + aux).setAttribute("id",
				"metadatosEni.organo" + position);
		$("#removeOrganoButton_" + aux).attr("onclick",
				"removeOrganoDoc(" + position + ")");
		$("#removeOrganoButton_" + aux).attr("id",
				"removeOrganoButton_" + position);
		$("#divOrgano_" + aux).attr("id", "divOrgano_" + position);
		position++;
	}
	organosAdded--;
}

function desHabilitarOrganos(enabled) {
	var cont = 0;
	var objeto = document.getElementById("metadatosEni.organo" + cont);
	while (objeto != null) {
		objeto.disabled = enabled;
		cont++;
		objeto = document.getElementById("metadatosEni.organo" + cont);
	}
}

/**
 * Mótodo que rechaza la petición seleccionada
 */
function prepareGenerateSignEni(chash) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({
		url: "inbox/loadMetadatasEni",
    	dataType: 'json',
    	data: "idDocumento=" + chash,
    	async: false,
    	success: function(data) {
    		loadDataEni('#actionSign', chash, data);
          	jsAjaxStatus.stopAjax();
    	},
      	error: function error(jqXHR, textStatus, errorThrown) {
      		$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
      		$('#error').show();
      		jsAjaxStatus.stopAjax();
  		}
	});
}

function loadDataEni (action, chash, datosEni) {
	$('#versionNTI').val(datosEni.metadatosEni.versionNTI);
	$('#fcaptura').val(datosEni.fcaptura);
	$('#origen').val(datosEni.metadatosEni.origenCiudadanoAdministracion);
	$('#identificador').val(datosEni.metadatosEni.identificador);
	$('#estadoElaboracion').val(datosEni.metadatosEni.estadoElaboracion);
	$('#tipoDocumental').val(datosEni.metadatosEni.tipoDocumental);
	
	//carga de organos
	$("[id^='metadatosEni.organo']").remove();
	$("[id^='removeOrganoButton_']").remove();
	$("[id^='divOrgano_']").remove();
	organosAdded = 0;

	$.each(datosEni.metadatosEni.organo, function (i, item) {
		addOrganoDoc(item);
    });
	
	
	
	$('#errorMsgEni').html("");

	$('#idDocumentEni').val(chash);
	
	$("#metadataEniForm")[0].action = $(action).val();
	
	
	//carga de metadatos adicionales
	$("[id^='metadatoAdicionalNombre']").remove();
	$("[id^='metadatoAdicionalValor']").remove();
	$("[id^='removeAdicionalButton_']").remove();
	$("[id^='divAdicional_']").remove();
	adicionalesAdded = 0;
	
	$.each(datosEni.metadatosAdicionales, function (i, item) {
		if (datosEni.metadatosObligatorios.indexOf(item.nombre) >= 0) {
			addAdicional(item.nombre, item.valor);
		}
    });

	$('#metadataEni').dialog('open');
}

/**
 * Método que rechaza la petición seleccionada
 */
function prepareGenerateReportEni(chash) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({
		url: "inbox/loadMetadatasEni",
    	dataType: 'json',
    	data: "idDocumento=" + chash,
    	async: false,
    	success: function(data) {
    		loadDataEni('#actionReport', chash, data);
          	jsAjaxStatus.stopAjax();
    	},
      	error: function error(jqXHR, textStatus, errorThrown) {
      		$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
      		$('#error').show();
      		jsAjaxStatus.stopAjax();
  		}
	});
}

function addAdicional(nombre, valor) {
	if (nombre.trim() != "") {
		if (adicionalesAdded == '') {
			adicionalesAdded = 0;
		}

		var key_input = document.createElement("input");
		var key_input_nombre = document.createElement("input");
		var remove_button = document.createElement("button");
		var div = document.createElement("div");

		key_input.setAttribute("type", "text");
		key_input.setAttribute("name", "metadatosAdicionales[" + adicionalesAdded
				+ "].valor");
		key_input.setAttribute("id", "metadatoAdicionalValor" + adicionalesAdded);
		key_input.setAttribute("class", "sticked-input-right");
		if (valor != null) {
			key_input.setAttribute("value", valor);
		}
		
		
		key_input_nombre.setAttribute("type", "text");
		key_input_nombre.setAttribute("name", "metadatosAdicionales[" + adicionalesAdded
				+ "].nombre");
		key_input_nombre.setAttribute("id", "metadatoAdicionalNombre" + adicionalesAdded);
		key_input_nombre.setAttribute("class", "sticked-input-right");
		key_input_nombre.setAttribute("value", nombre);

		var a = $('<a />');
		a.attr("id", "removeAdicionalButton_" + adicionalesAdded);
		a.attr("href", "javascript:void(0)");
		a.attr("onclick", "removeAdicional(" + adicionalesAdded + ")");
		a.html('<span class="mf-icon mf-icon-delete-16">Eliminar</span>');

		div.setAttribute("id", "divAdicional_" + adicionalesAdded);

		$("#listAdicionales").append(div);

		$("#divAdicional_" + adicionalesAdded).append(key_input_nombre);
		$("#divAdicional_" + adicionalesAdded).append(key_input);
		$("#divAdicional_" + adicionalesAdded).append(a);

		$('#adicionalNombre').val('');
		$('#adicionalValor').val('');
		adicionalesAdded++;
	}
}

function removeAdicional(position) {
	$("#metadatoAdicionalNombre" + position).remove();
	$("#metadatoAdicionalValor" + position).remove();
	$("#removeAdicionalButton_" + position).remove();
	$("#divAdicional_" + position).remove();
	while (adicionalesAdded - 1 > position) {
		var aux = position + 1;
		document.getElementById("metadatoAdicionalNombre" + aux).setAttribute(
				"name", "metadatosAdicionales[" + position + "].nombre");
		document.getElementById("metadatoAdicionalNombre" + aux).setAttribute("id",
				"metadatoAdicionalNombre" + position);
		
		document.getElementById("metadatoAdicionalValor" + aux).setAttribute(
				"name", "metadatosAdicionales[" + position + "].valor");
		document.getElementById("metadatoAdicionalValor" + aux).setAttribute("id",
				"metadatoAdicionalValor" + position);
		
		$("#removeAdicionalButton_" + aux).attr("onclick",
				"removeAdicional(" + position + ")");
		$("#removeAdicionalButton_" + aux).attr("id",
				"removeAdicionalButton_" + position);
		$("#divAdicional_" + aux).attr("id", "divAdicional_" + position);
		position++;
	}
	adicionalesAdded--;
}

function convertToBase64ToByteArray(data) {
	var byteCharacters = atob(data);
	var byteNumbers = new Array(byteCharacters.length);
	for (var i = 0; i < byteCharacters.length; i++) {
		byteNumbers[i] = byteCharacters.charCodeAt(i);
	}
	return new Uint8Array(byteNumbers);
}


function descargarInforme(urlDescarga, nombreDocumento){
	var req = new XMLHttpRequest();
	req.open("GET",urlDescarga, true);
	req.responseType = "blob";
	nombreDocumento = nombreDocumento.slice(0, -4);
	req.onreadystatechange = function () {
	    if (req.readyState === 4 && req.status === 200) {
	        var filename = "report_" + nombreDocumento + ".pdf";
	        if (typeof window.chrome !== 'undefined') {
	            // Chrome version
	            var link = document.createElement('a');
	            link.href = window.URL.createObjectURL(req.response);
	            link.download = filename;
	            link.click();
	        } else if (typeof window.navigator.msSaveBlob !== 'undefined') {
	            // IE version
	            var blob = new Blob([req.response], { type: 'application/pdf' });
	            window.navigator.msSaveOrOpenBlob(blob, filename);
	        } else {
	            // Firefox version
	        	 var e = document.createElement('a');
		         e.style = "display: none";  
		         var url = window.URL.createObjectURL(blob);
		         e.href = url;
		         e.download = filename;
		         document.body.appendChild(e);
		         e.click();
		         setTimeout(function(){
		        	 document.body.removeChild(e);
		             window.URL.revokeObjectURL(url);  
		         }, 0);
	        }
	    }else if (req.readyState === 4 && req.status === 500){
	    	alert("Error descargando informe");
		}
	};
	req.send();
}

function sendEmail(requestId){
	$('#requestId').val(requestId);
	$(window).scrollTop(0);
	$('#target_0').val('');
	$('#chkboxFirma').attr('checked', false);
	$('#chkboxInforme').attr('checked', false);
	$('#chkboxNormalizado').attr('checked', false);
	$('#ajaxBusy').hide();
	$('#errorMsgSendEmail').html('');
	$('errorMsgSendEmail').hide();
	$('#sendEmailDialog').dialog('open');
}

function deleteTargetLine(rowIndex) {
	
	var row = document.getElementById(rowIndex);
	var table = document.getElementById("targetlines");
	
	for(var i=0; i<table.rows.length; ++i){
		if(table.rows[i].id == rowIndex ){
			table.deleteRow(i);
			break;
		}
	}

}


function addTargetLine() {

	var i = sessionStorage.getItem('lineasParaEnvio');
	
	if(i==null || i == undefined || i=='null' || i == 'undefined'){
		i=0;
	}
	
	i = Number(i)+1;
	
	sessionStorage.setItem('lineasParaEnvio', i);
	
	var table = document.getElementById("targetlines");

	// Create an empty <tr> element and add it to the 1st position of the table:
	var row = table.insertRow();

	// Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
	var cell1 = row.insertCell(0);
	var cell2 = row.insertCell(1);
	var cell3 = row.insertCell(2);

	// Add some text to the new cells:
	cell1.innerHTML = "";
	cell2.innerHTML = '<input id="target_'+i+'" name="target_'+i+'" class="target_input" type="email" value="" role="textbox" />';
	cell3.innerHTML = '<a href="javascript:void(0);" onclick="deleteTargetLine(\'' + "linea_"+i + '\');"><span class="mf-icon mf-icon-delete-16" th:text="#{selectUsers}"></span></a>';
	
	row.id = "linea_"+i;
	

	// Si el usuario borra el contenido, el destinatario se elimina
	$('#target_' + i).focusout(function() {
		var currentVal = $('#target_' + i).val();
		if (i > 0) {
    		if (currentVal == null || currentVal == '') {
    			deleteTargetLine(i);
    		}
		} else {
			if (currentVal == null || currentVal == '') {
				primeraLineaDeFirmaBorrada = true;
				var targets = $("#targets").val().split(",");
				var newTargets = targets.slice(1, targets.length);
				$("#targets").val("");
				$("#targets").val(newTargets);
    		}
		}
	});
}

function sendEmailTo() {
	$('#errorMsgSendEmail').html('');
	$('errorMsgSendEmail').hide();
	
	var formulario = $("#sendEmailForm").serializeObject();
	var propiedadesTarget = findPropertyNameByRegex(formulario, 'target*');
	formulario.targets = [];

	for(var i =0; i< propiedadesTarget.length; ++i){
		formulario.targets.push(formulario[propiedadesTarget[i]]);
		delete formulario[propiedadesTarget[i]];
	}

	$.ajax({
		url: "inbox/sendEmail",
		data: JSON.stringify(formulario),
		contentType:"application/json",
		type: "POST",
    	dataType: 'json',
    	cache: false,
    	beforeSend: function() {
    		$('#ajaxBusy').show();
    		$('#botonCancelar').attr("disabled", true);
    		$('#botonEnviar').attr("disabled", true);
    	},
    	success: function(data) {
    		if (data.message.length !== 0) {
    			$('#errorMsgSendEmail').html(data.message);
          		$('errorMsgSendEmail').show();
          		$('#ajaxBusy').hide();
    	  		$('#botonCancelar').attr("disabled", false);
    	  		$('#botonEnviar').attr("disabled", false);
    		}else{
	    		$('#sendEmailDialog').dialog('close');
	    		$('#ajaxBusy').hide();
	    		$('#botonCancelar').attr("disabled", false);
	    		$('#botonEnviar').attr("disabled", false);
	    		alert('Correo electrónico enviado correctamente.');
    		}
    	},
	  	error: function error(jqXHR, textStatus, errorThrown) {
	  		$('#errorMsgSendEmail').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
	  		$('errorMsgSendEmail').show();
	  		$('#ajaxBusy').hide();
	  		$('#botonCancelar').attr("disabled", false);
	  		$('#botonEnviar').attr("disabled", false);
		}
	});	
}

var indexViewDocument = 0;

function visualizarDocumento(i) {
	indexViewDocument = i;
	actualizarLeyendaNavDoc();
	$( "tr[id='visualizarDocumento" + indexViewDocument + "']" ).click();
}

function visualizarSiguienteDocumento() {
	if(indexViewDocument + 1 < $("tr[id^='visualizarDocumento']").length) {
		indexViewDocument++;
		actualizarLeyendaNavDoc();
		$( "tr[id='visualizarDocumento" + indexViewDocument + "']" ).click();
	} 
}

function visualizarAnteriorDocumento() {
	if(indexViewDocument + 1 > 1) {
		indexViewDocument--;
		actualizarLeyendaNavDoc();
		$( "tr[id='visualizarDocumento" + indexViewDocument + "']" ).click();
	}
}

function actualizarLeyendaNavDoc() {
	var indice = indexViewDocument + 1;
	var total = $("tr[id^='visualizarDocumento']").length;
	var texto = indice + '/' + total;
	$("#infoNavegadorDoc").html(texto);
	$("#previousDocument").show();
	$("#nextDocument").show();
	if(indice == 1) {
		$("#previousDocument").hide();
	}
	if(indice == total) {
		$("#nextDocument").hide();
	}
	$("tr[id^='visualizarDocumento']").removeClass('ui-selected');
	$("#visualizarDocumento" + indexViewDocument).addClass('ui-selected');
}

function navegadorEI() {
	if (navigator.userAgent.indexOf('Firefox') !=-1 || navigator.userAgent.indexOf('Chrome') !=-1) {
		return false;
	} else {
		return true;
	}
}


function verDocumentoRequest(idDocumento, i) {
	indexViewDocument = i;
	actualizarLeyendaNavDoc();
	///
	$("#viewDocument").hide();
	$("#viewDocument").empty();
	$("#viewDocument").show();
	
	//TODO temp js
	var _pdf = null,
    _currentPage;
	var disableButtons = function(){
		jsAjaxStatus.startAjax();
	    $('.js-pdf-next').attr('disabled','disabled');
	    $('.js-pdf-prev').attr('disabled','disabled');
	};
	var enableButtons = function(){
		jsAjaxStatus.stopAjax();
	    $('.js-pdf-next').removeAttr('disabled');
	    $('.js-pdf-prev').removeAttr('disabled');
	    
	};
	var isAgoodPage = function(numPage){
	    return ((numPage > 0) && (numPage <= _pdf.numPages));
	};
	var nextPage = function(){
	    if(isAgoodPage((_currentPage + 1))){
	        _currentPage +=1;
	        _pdf.getPage(_currentPage).then(onGetPage);
	        disableButtons();
	    }
	};
	var prevPage = function(){
	    if(isAgoodPage(_currentPage - 1)){
	        _currentPage -=1;
	        _pdf.getPage(_currentPage).then(onGetPage);
	        disableButtons();
	    }
	};
	
	var onGetPage = function(page){
	    var anchoCanvas = $('#tab1').width();
	    var altoCanvas = 1024;
	    var anchoPagPDF = page.pageInfo.view[2];
	    var altoPagPDF = page.pageInfo.view[3];
	    var factorAnchos = anchoCanvas / anchoPagPDF;
	    var factorAltos = altoCanvas / altoPagPDF;
	    var scale = Math.min (anchoCanvas / anchoPagPDF, altoCanvas / altoPagPDF);
	    
	    
	    var viewport = page.getViewport(scale);
	    // Prepare canvas using PDF page dimensions.
	    var canvas = document.getElementById('pdfContainer');
	    var context = canvas.getContext('2d');
	    canvas.height = altoCanvas;
	    canvas.width = anchoCanvas;
	    // Render PDF page into canvas context.
	    var renderContext = {
	        canvasContext: context,
	        viewport: viewport
	    };
	    page.render(renderContext);
	    enableButtons();
	};
	
	///
	jsAjaxStatus.startAjax();
	var esExpl = navegadorEI();
	var esExplParam = '&esIExpl=' + esExpl;
	var documentParam = '&idDocumento=' + idDocumento;
	isLoadComments = false;
	var esExpl = navegadorEI();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'inbox/verDocumentoRequest',
        	dataType: 'html',
        	data: documentParam + esExplParam,
        	async: false,
        	success:
        		function(model) {
	        		
/*        			var esExpl = navegadorEI();
	        		if (esExpl){
	        			$("#iframePrevDocument").hide();
	        			$("#canvasPrevDocument").show();
	        		} else {
	        			$("#iframePrevDocument").show();
	        			$("#canvasPrevDocument").hide();
	        		}*/
	        		
        			$("#viewDocument").html(model);
        			
        			jsAjaxStatus.stopAjax();
	          		var _base64Response = $('.js-pdf-data').data('pdfbase64');
	          		if (typeof _base64Response != "undefined"){
	          			regExp = new RegExp('\n', 'g');
	          			_base64Response = _base64Response.replace(regExp,'');
	          			_base64 = atob(_base64Response);
	          		
	          			PDFJS.getDocument({data: _base64}).then(function getPdfHelloWorld(pdf) {
		               
			                _pdf = pdf;
			                _currentPage = 1;
			                
			                pdf.getPage(1).then(onGetPage);
			                $('.js-pdf-next').on('click',nextPage);
			                $('.js-pdf-prev').on('click',prevPage);
			                jsAjaxStatus.stopAjax();
	          			});
	          		}
		                
        	},
          	error:
          		function error(jqXHR, textStatus, errorThrown) {
	          		$('#viewDocumentError').html('No se pudo visualizar el documento. Si el problema persiste, contacte con su administrador');
	          		$('#viewDocumentError').show();
	          		jsAjaxStatus.stopAjax();
          		}
	});

}


//Anexo

var indexViewAnnex = 0;

function visualizarAnexo(i) {
	indexViewAnnex = i;
	actualizarLeyendaNavAnex();
	$( "tr[id='visualizarAnexo" + indexViewAnnex + "']" ).click();
}

function visualizarSiguienteAnexo() {
	if(indexViewAnnex + 1 < $("tr[id^='visualizarAnexo']").length) {
		indexViewAnnex++;
		actualizarLeyendaNavAnex();
		$( "tr[id='visualizarAnexo" + indexViewAnnex + "']" ).click();
	} 
}

function visualizarAnteriorAnexo() {
	if(indexViewAnnex + 1 > 1) {
		indexViewAnnex--;
		actualizarLeyendaNavAnex();
		$( "tr[id='visualizarAnexo" + indexViewAnnex + "']" ).click();
	}
}

function actualizarLeyendaNavAnex() {
	var indice = indexViewAnnex + 1;
	var total = $("tr[id^='visualizarAnexo']").length;
	var texto = indice + '/' + total;
	$("#infoNavegadorAnex").html(texto);
	$("#previousAnex").show();
	$("#nextAnex").show();
	if(indice == 1) {
		$("#previousAnex").hide();
	}
	if(indice == total) {
		$("#nextAnex").hide();
	}
	$("tr[id^='visualizarAnexo']").removeClass('ui-selected');
	$("#visualizarAnexo" + indexViewAnnex).addClass('ui-selected');
}

function verAnexoRequest(idAnexo, i) {
	indexViewAnnex = i;
	actualizarLeyendaNavAnex();
	///
	$("#viewAnex").hide();
	$("#viewAnex").empty();
	$("#viewAnex").show();
	
	//TODO temp js
	var _pdf = null,
    _currentPage;
	var disableButtons = function(){
		jsAjaxStatus.startAjax();
	    $('.js-pdf-next').attr('disabled','disabled');
	    $('.js-pdf-prev').attr('disabled','disabled');
	};
	var enableButtons = function(){
		jsAjaxStatus.stopAjax();
	    $('.js-pdf-next').removeAttr('disabled');
	    $('.js-pdf-prev').removeAttr('disabled');
	    
	};
	var isAgoodPage = function(numPage){
	    return ((numPage > 0) && (numPage <= _pdf.numPages));
	};
	var nextPage = function(){
	    if(isAgoodPage((_currentPage + 1))){
	        _currentPage +=1;
	        _pdf.getPage(_currentPage).then(onGetPage);
	        disableButtons();
	    }
	};
	var prevPage = function(){
	    if(isAgoodPage(_currentPage - 1)){
	        _currentPage -=1;
	        _pdf.getPage(_currentPage).then(onGetPage);
	        disableButtons();
	    }
	};
	
	var onGetPage = function(page){
	    var anchoCanvas = $('#tab2').width();
	    var altoCanvas = 1024;
	    var anchoPagPDF = page.pageInfo.view[2];
	    var altoPagPDF = page.pageInfo.view[3];
	    var factorAnchos = anchoCanvas / anchoPagPDF;
	    var factorAltos = altoCanvas / altoPagPDF;
	    var scale = Math.min (anchoCanvas / anchoPagPDF, altoCanvas / altoPagPDF);
	    
	    
	    var viewport = page.getViewport(scale);
	    // Prepare canvas using PDF page dimensions.
	    var canvas = document.getElementById('pdfContainer');
	    var context = canvas.getContext('2d');
	    canvas.height = altoCanvas;
	    canvas.width = anchoCanvas;
	    // Render PDF page into canvas context.
	    var renderContext = {
	        canvasContext: context,
	        viewport: viewport
	    };
	    page.render(renderContext);
	    enableButtons();
	};
	
	///
	jsAjaxStatus.startAjax();
	var esExpl = navegadorEI();
	var esExplParam = '&esIExpl=' + esExpl;
	var documentParam = '&idAnexo=' + idAnexo;
	isLoadComments = false;
	var esExpl = navegadorEI();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'inbox/verAnexoRequest',
        	dataType: 'html',
        	data: documentParam + esExplParam,
        	async: false,
        	success:
        		function(model) {
	        		
/*        			var esExpl = navegadorEI();
	        		if (esExpl){
	        			$("#iframePrevDocument").hide();
	        			$("#canvasPrevDocument").show();
	        		} else {
	        			$("#iframePrevDocument").show();
	        			$("#canvasPrevDocument").hide();
	        		}*/
	        		
        			$("#viewAnex").html(model);
        			
        			jsAjaxStatus.stopAjax();
	          		var _base64Response = $('.js-pdf-data').data('pdfbase64');
	          		if (typeof _base64Response != "undefined"){
	          			regExp = new RegExp('\n', 'g');
	          			_base64Response = _base64Response.replace(regExp,'');
	          			_base64 = atob(_base64Response);
	          		
	          			PDFJS.getDocument({data: _base64}).then(function getPdfHelloWorld(pdf) {
		               
			                _pdf = pdf;
			                _currentPage = 1;
			                
			                pdf.getPage(1).then(onGetPage);
			                $('.js-pdf-next').on('click',nextPage);
			                $('.js-pdf-prev').on('click',prevPage);
			                jsAjaxStatus.stopAjax();
	          			});
	          		}
		                
        	},
          	error:
          		function error(jqXHR, textStatus, errorThrown) {
	          		$('#viewAnnexError').html('No se pudo visualizar el anexo. Si el problema persiste, contacte con su administrador');
	          		$('#viewDocumeviewAnnexErrorntError').show();
	          		jsAjaxStatus.stopAjax();
          		}
	});

}

