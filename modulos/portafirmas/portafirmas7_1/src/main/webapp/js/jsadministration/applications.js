// Indica si ya se cargaron la aplicaciones
var isLoadApplications = false;

//Prepara la creacion de una aplicacion
function prepareNewApplication() {
	clearErrors();
	
	$('#primaryKey').val('');
	$('#appCode').val('');
	$('#appName').val('');
	$('#pfConfiguration').val('0');
	$('#appParent').val('');
	$('#wsUser').val('');
	$('#wsLocation').val('');
	$('#wsPassword').val('');
	$('#wsActiva').removeProp("checked");
	$('#wsNotifIntermedios').removeProp("checked");
	
	prepareTabs();
	clearMetadata();
	
	$('#modalApps').dialog("open");
}

//Preparación para modificar usuario: Se carga el usuario correspondiente en la ventana modal 
function prepareModifyApplication(primaryKey) {
	clearErrors();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/applications/recoverApp",
		dataType: 'html',
		data: 'primaryKey=' + primaryKey,
		async: true,
		success:
			function(model) {			
			$('#modalApps').html(model);
			$('#primaryKey').val(primaryKey);
			
			prepareTabs();

			$('#modalApps').dialog('open');
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#modalApps').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}


function guardarApp(){
	habilitarOrganos(false);
	
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({
		url: "administration/applications/guardarApp",
		type: 'post',
		dataType: 'json',
		data: $("#modalAppForm").serialize(),
		async: true,
		success:
			function(errors) {
			if(jQuery.isEmptyObject(errors)) {
	        	$('#modalApps').dialog("close");
				reloadApplications();
			} else {
				$('#applicatonErrorMessage').show();
				$('#applicatonErrorMessage').html(getErrors(errors));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#modalApps').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

// Prepara el beloadAporrado de una aplicacion
function prepareDeleteApplication(primaryKey) {
	$('#questionMessageApp').html('Va a borrar una aplicacion, ¿está usted seguro?');
	$('#yesButtonApp').attr('onclick',"deleteApplication('" + primaryKey + "');");
	$('#questionApp').show();
}

// Para borrar una aplicacion
function deleteApplication(primaryKey) {
	$('#questionApp').hide();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/applications/deleteApplication",
		dataType: 'json',
		data: "primaryKey=" + primaryKey,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadApplications();
			} else {
				$('#error').html(getErrors(data));
				$('#error').show();
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
			jsAjaxStatus.stopAjax();
		}
	});
}

// Para cargar las aplicaciones una única vez
function loadApplications() {
	if(!isLoadApplications) {
		reloadApplications();
		isLoadApplications = true;
	}
}

// Para recargar las aplicaciones
function reloadApplications() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "administration/applications/loadApplications",
		dataType: 'html',
		async: false,
		success: function(model) {
        	$('#modalApps').dialog("close");
			$("#tab2").html(model);
			setupApplications();
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html(genericMessage);
			$('#error').show();
        	$('#modalApps').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
};

function setupApplications() {
	$("#modalApps").dialog({
	    autoOpen: false,
	    resizable: false,
	    width: 700,
	    modal: true,       
	    buttons: [
	        {
	        	text: "Cancelar",       
	            "class": 'secondary',
	            click: function() {                   
	            	$(this).dialog("close");
	            },
	        },
	        {                   
	        	text: "Aceptar",                   
	            "class": 'primary',
	            click: function() {
	            	guardarApp();
	            }
	        }           
	    ],   
	    create: customModalStyle
	});
}

function prepareTabs() {
	$("#applicationTabs").tabs({ selected: 0 });
}

function clearMetadata() {
	$('#origen').val("");
	$('#identificador').val("");
	$('#estadoElaboracion').val("");
	$('#tipoDocumental').val("");
	$('#versionNTI').val("");

	if (organosAdded != '') {
		$("[id^='documentEni.metadatosEni.organo']").remove();
		$("[id^='removeOrganoButton_']").remove();
		$("[id^='divOrgano_']").remove();
		organosAdded = 0;
	}
	
	if (organosAdded != '') {
		$("[id^='metadatoAdicionalValor']").remove();
		$("[id^='metadatoAdicionalNombre']").remove();
		$("[id^='removeAdicionalButton_']").remove();
		$("[id^='divAdicional_']").remove();
		adicionalesAdded = 0;
	}
	
}

function setupMetadata(datosEni) {

	if (datosEni.metadatosEni.origenCiudadanoAdministracion) {
		$('#origen').val("1");
	} else {
		$('#origen').val("0");
	}
	
	$('#identificador').val(datosEni.metadatosEni.identificador);
	if (datosEni.metadatosEni.estadoElaboracion != null) {
		$('#estadoElaboracion').val(datosEni.metadatosEni.estadoElaboracion.valorEstadoElaboracion);
	}
	$('#tipoDocumental').val(datosEni.metadatosEni.tipoDocumental);
	$('#versionNTI').val(datosEni.metadatosEni.versionNTI);
	//habilitarOrganos(false);
	
	// Metadatos Adicionales
	if (adicionalesAdded != '') {
		$("[id^='documentEni.metadatoAdicionalValor']").remove();
		$("[id^='documentEni.metadatoAdicionalNombre']").remove();
		$("[id^='removeAdicionalButton_']").remove();
		$("[id^='divAdicional_']").remove();
		
	}
	
	adicionalesAdded = 0; 
	$.each(datosEni.metadatosAdicionales, function (i, item) {
		var checked = false;
		if (datosEni.metadatosObligatorios.indexOf(item.nombre) >= 0) {
			checked= true;
		}
		addAdicional(item.nombre, item.valor, checked);
		
		
    });
	
	
	// Organos
	if (organosAdded != '') {
		$("[id^='documentEni.metadatosEni.organo']").remove();
		$("[id^='removeOrganoButton_']").remove();
		$("[id^='divOrgano_']").remove();
	}
	
	organosAdded = 0;
	$.each(datosEni.metadatosEni.organo, function (i, item) {
		//console.log(datosEni.metadatosObligatorios);
		addOrganoDoc(item);
    });

}



//Metadatos ENI
function addOrganoDoc(organo) {
	if (organo.trim() != "") {
		if (organosAdded == '') {
			organosAdded = 0;
		}

		var key_input = document.createElement("input");
		var div = document.createElement("div");

		key_input.setAttribute("type", "text");
		key_input.setAttribute("name", "documentEni.metadatosEni.organo[" + organosAdded
				+ "]");
		key_input.setAttribute("id", "documentEni.metadatosEni.organo" + organosAdded);
		key_input.setAttribute("class", "sticked-input-right");
		key_input.setAttribute("disabled", "disabled");
		key_input.setAttribute("value", organo);

		var a = $('<a />');
		a.attr("id", "removeOrganoButton_" + organosAdded);
		a.attr("href", "javascript:void(0)");
		a.attr("onclick", "removeOrganoDoc(" + organosAdded + ")");
		a.html('<span class="mf-icon mf-icon-delete-16">Eliminar</span>');

		div.setAttribute("id", "divOrgano_" + organosAdded);

		$("#listOrganos").append(div);

		$("#divOrgano_" + organosAdded).append(key_input);
		$("#divOrgano_" + organosAdded).append(a);

		$("#organos").val('');
		organosAdded++;
	}
}

function removeOrganoDoc(position) {
	//document.getElementById("metadatosEni.organo" + position).remove();
	$("#documentEni.metadatosEni.organo" + position).remove();
	$("#removeOrganoButton_" + position).remove();
	$("#divOrgano_" + position).remove();
	while (organosAdded - 1 > position) {
		var aux = position + 1;
		document.getElementById("documentEni.metadatosEni.organo" + aux).setAttribute(
				"name", "documentEni.metadatosEni.organo[" + position + "]");
		document.getElementById("documentEni.metadatosEni.organo" + aux).setAttribute("id",
				"documentEni.metadatosEni.organo" + position);
		$("#removeOrganoButton_" + aux).attr("onclick",
				"removeOrganoDoc(" + position + ")");
		$("#removeOrganoButton_" + aux).attr("id",
				"removeOrganoButton_" + position);
		$("#divOrgano_" + aux).attr("id", "divOrgano_" + position);
		position++;
	}
	organosAdded--;
}

function habilitarOrganos(disabled) {
	var cont = 0;
	var objeto = document.getElementById("documentEni.metadatosEni.organo" + cont);
	while (objeto != null) {
		objeto.disabled = disabled;
		cont++;
		objeto = document.getElementById("documentEni.metadatosEni.organo" + cont);
	}
}

function addAdicional(nombre, valor, checked) {
	if (nombre.trim() != "") {
		if (adicionalesAdded == '') {
			adicionalesAdded = 0;
		}

		var key_input = document.createElement("input");
		var key_input_nombre = document.createElement("input");
		var key_checkbox_mandatory = document.createElement("input");
		var div = document.createElement("div");

		key_input.setAttribute("type", "text");
		key_input.setAttribute("name", "documentEni.metadatosAdicionales[" + adicionalesAdded + "].valor");
		key_input.setAttribute("id", "documentEni.metadatoAdicionalValor" + adicionalesAdded);
		key_input.setAttribute("class", "sticked-input-right");
		if (valor != null) {
			key_input.setAttribute("value", valor);
		}
		
		
		key_input_nombre.setAttribute("type", "text");
		key_input_nombre.setAttribute("name", "documentEni.metadatosAdicionales[" + adicionalesAdded + "].nombre");
		key_input_nombre.setAttribute("id", "documentEni.metadatoAdicionalNombre" + adicionalesAdded);
		key_input_nombre.setAttribute("class", "sticked-input-right");
		key_input_nombre.setAttribute("value", nombre);
		
		
		key_checkbox_mandatory.setAttribute("type", "checkbox");
		key_checkbox_mandatory.setAttribute("name", "documentEni.metadatosObligatorios[" + adicionalesAdded + "]");
		key_checkbox_mandatory.setAttribute("id", "documentEni.metadatosObligatorios" + adicionalesAdded);
		key_checkbox_mandatory.setAttribute("class", "sticked-input-right");
		key_checkbox_mandatory.setAttribute("value", nombre);
		if (checked) {
			key_checkbox_mandatory.setAttribute("checked", true);
		} 
		
				

		var a = $('<a />');
		a.attr("id", "removeAdicionalButton_" + adicionalesAdded);
		a.attr("href", "javascript:void(0)");
		a.attr("onclick", "removeAdicional(" + adicionalesAdded + ")");
		a.html('<span class="mf-icon mf-icon-delete-16">Eliminar</span>');

		div.setAttribute("id", "divAdicional_" + adicionalesAdded);

		$("#listAdicionales").append(div);

		$("#divAdicional_" + adicionalesAdded).append(key_input_nombre);
		$("#divAdicional_" + adicionalesAdded).append(key_input);
		$("#divAdicional_" + adicionalesAdded).append(key_checkbox_mandatory);
		$("#divAdicional_" + adicionalesAdded).append(a);

		$('#adicionalNombre').val('');
		$('#adicionalValor').val('');
		adicionalesAdded++;
	}
}

function removeAdicional(position) {
	//var checked = document.getElementById("documentEni.metadatosObligatorios" + position).checked;
	$("#documentEni.metadatoAdicionalValor" + position).remove();
	$("#documentEni.metadatoAdicionalNombre" + position).remove();
	$("#documentEni.metadatosObligatorios" + position).remove();
	$("#removeAdicionalButton_" + position).remove();
	$("#divAdicional_" + position).remove();
	while (adicionalesAdded - 1 > position) {
		var aux = position + 1;
	
		document.getElementById("documentEni.metadatoAdicionalValor" + aux).setAttribute(
				"name", "documentEni.metadatosAdicionales[" + position + "].valor");
		document.getElementById("documentEni.metadatoAdicionalValor" + aux).setAttribute("id",
				"documentEni.metadatoAdicionalValor" + position);
		
		document.getElementById("documentEni.metadatoAdicionalNombre" + aux).setAttribute(
				"name", "documentEni.metadatosAdicionales[" + position + "].nombre");
		document.getElementById("documentEni.metadatoAdicionalNombre" + aux).setAttribute("id",
				"documentEni.metadatoAdicionalNombre" + position);
		
		//document.getElementById("documentEni.metadatosObligatorios" + aux).checked = checked;
		
		document.getElementById("documentEni.metadatosObligatorios" + aux).setAttribute(
				"name", "documentEni.metadatosObligatorios[" + position + "]");
		document.getElementById("documentEni.metadatosObligatorios" + aux).setAttribute("id",
				"documentEni.metadatosObligatorios" + position);
		
		
		$("#removeAdicionalButton_" + aux).attr("onclick",
				"removeAdicional(" + position + ")");
		$("#removeAdicionalButton_" + aux).attr("id",
				"removeAdicionalButton_" + position);
		$("#divAdicional_" + aux).attr("id", "divAdicional_" + position);
		
		position++;
		
//		if (position <= adicionalesAdded- 1) {
//			checked = document.getElementById("documentEni.metadatosObligatorios" + position).checked;
//		}
	}
	adicionalesAdded--;
}


function prepareModifyMetadata(primaryKey) {
	if (primaryKey != null && primaryKey != '') {
		clearErrors();
		jsAjaxStatus.startAjax();
		$.ajaxSetup({cache: false});
		$.ajax({url: "administration/applications/loadMetadatasEni",
			dataType: 'html',
			data: 'primaryKey=' + primaryKey,
			async: true,
			success:
				function(data) {			
				var docEni = JSON.parse(data);
				setupMetadata(docEni);
				
				jsAjaxStatus.stopAjax();
			},
			error:
				function error(jqXHR, textStatus, errorThrown) {
				showError(genericMessage);
				jsAjaxStatus.stopAjax();
			}
		});
	} else {
		clearMetadata();
	}
}

function clearErrors() {
	$('#applicatonErrorMessage').hide();
	$('#applicatonErrorMessage').html('');
}