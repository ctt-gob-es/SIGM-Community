// Para indicar si la pestaña está cargada
var isLoadRestrictions = false;

// Carga la lista de restricciones
function loadRestrictions() {
	if(!isLoadRestrictions) {
		reloadRestrictions('get', null);
		isLoadRestrictions = true;
	}
}

//Recarga la lista de restricciones
function reloadRestrictions(type, data) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({
		url: 'userManagement/restriction',
		dataType: 'html',
		data: data,
		type: type,
		async: false,
		success: function(model) {
        	$('#restriction_modal').dialog("close");
			$("#tab5").html(model);
			setupRestriction();
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html(genericMessage);
			$('#error').show();
        	$('#restriction_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function setupRestriction() {
	
	// Autocompletado para buscar usuarios de sede
	$('#searchTextRestriction').autocomplete({
		minLength: 3,
		source: 'userManagement/restriction/autocompleteUserRestriction',
		select: function( event, ui ) {
			$('#searchTextRestriction').val(ui.item.label);
			$('#searchRestrictionId').val(ui.item.id);
			loadRestrictionsList(ui.item.id);
			return false;
		}
	});
	
	//.. y autocompletado para restringir usuarios
	$('#new_restriction').autocomplete({
		minLength: 3,
		source: 'userManagement/restriction/autocompleteUserRestriction',
		select: function( event, ui ) {
			$('#new_restriction').val(ui.item.label);
			$('#new_restriction_pk').val(ui.item.id);
			return false;
		}
	});
	//.. y autocompletado para los usuarios validos
	$('#new_valid_user').autocomplete({
		minLength: 3,
		source: 'userManagement/restriction/autocompleteUserRestriction',
		select: function( event, ui ) {
			$('#new_valid_user').val(ui.item.label);
			$('#new_valid_user_pk').val(ui.item.id);
			return false;
		}
	});
	
	//Se crea el diálogo de la ventana modal
	$("#restriction_modal").dialog({
	    autoOpen: false,
	    resizable: false,
	    width: 400,
	    modal: true,       
	    buttons: [
	        {
	        	text: "Cancelar",       
	            "class": 'secondary',
	            click: function() {                   
	            	$(this).dialog("close");
	            }
	        },
	        {                   
	        	text: "Aceptar",                   
	            "class": 'primary',
	            click: function() {
	            	saveRestriction();
	            }
	        }           
	    ],   
	    //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
	    create:function () {               
	        $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	        $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
	    },
	    close:function () {
	    }
	});	
}

function prepareNewRestriction() {
	if ($('#searchTextRestriction').val() != null && $('#searchRestrictionId').val() != null &&
		$('#searchTextRestriction').val() != "" && $('#searchRestrictionId').val() != "") {
		$('#new_restriction').val($('#searchTextRestriction').val());
		$('#new_restriction_pk').val($('#searchRestrictionId').val());
		$('#new_restriction').prop("disabled", true);
		$('#new_restriction_pk').prop("disabled", true);
	} else {
		$('#new_restriction').val('');	
		$('#new_restriction_pk').val('');
		$('#new_restriction').prop("disabled", false);
		$('#new_restriction_pk').prop("disabled", false);
	}
	
	$('#new_valid_user').val('');
	$('#new_valid_user_pk').val('');
	$('#errorMsgRestriction').html('');
	$("#restriction_modal").dialog("open");
}

//FIXME function prepareModifyRestriction(primaryKey) {
//	jsAjaxStatus.startAjax();
//	$.ajaxSetup({cache: false});
//	$.ajax({url: "userManagement/restriction/load",
//		type: 'post',
//		dataType: 'html',
//		data: 'primaryKey=' + primaryKey,
//		async: true,
//		success:
//			function(model) {
//			$('#restriction_modal').html(model);
//			$('#restriction_modal').dialog('open');
//			jsAjaxStatus.stopAjax();
//		},
//		error:
//			function error(jqXHR, textStatus, errorThrown) {
//			showError(genericMessage);
//        	$('#restriction_modal').dialog("close");
//			jsAjaxStatus.stopAjax();
//		}
//	});
//}

function saveRestriction() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "userManagement/restriction/save",
		type: 'post',
		dataType: 'json',
		data: "userRestrict=" + $('#new_restriction_pk').val() +
		  	  "&userValid=" + $('#new_valid_user_pk').val(),
		async: true,
		success:
			function(warning) {
			// Si no hay avisos, se refresca la página
			if(jQuery.isEmptyObject(warning)) {
				//goRestrictionPage(1);
				$('#searchTextRestriction').val($('#new_restriction').val());
				$('#searchRestrictionId').val($('#new_restriction_pk').val());
				$('#restriction_modal').dialog("close");
				loadRestrictionsList($('#searchRestrictionId').val());
				
				
			} else {
				// Si se detectan errores, se muestran los avisos en la modal
				$('#errorMsgRestriction').html(getErrors(warning));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#restriction_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function prepareDeleteUserValid(primaryKey) {
	$('#questionMessageRestriction').html('Va a borrar el usuario habilitado para el envío, ¿está usted seguro?');
	$('#yesButtonRestriction').attr('onclick','deleteUserValid(' + primaryKey + ');');
	$('#questionRestriction').show();
}

// Borrado de usuario valido
function deleteUserValid(primaryKey)  {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "userManagement/restriction/deleteUserValid",
		type: 'post',
		dataType: 'json',
		data: 'primaryKey=' + primaryKey + 
			  '&userRestrict=' + $('#userId').val(),
		async: true,
		success:
			function(response) {
			// Si no hay avisos, es que se ha borrado y refrescamos la página
			if(jQuery.isEmptyObject(response.errors)) {
				//goRestrictionPage(1);
				loadRestrictionsList($('#searchRestrictionId').val());
			} else if (!jQuery.isEmptyObject(response.errors)) {
				// Hay errores, se oculta la venta de confimación y se muestra el error
				$('#questionRestriction').hide();
				showError(getErrors(response.errors));
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

// Configuración de la ordenación
function orderRestriction(campo){
	// Configuración del sentido de la ordenación
	if($('#orderRestrictionField').val() == campo){
		if($('#orderRestriction').val() == 'asc'){
			$('#orderRestriction').val('desc');
		}else{
			$('#orderRestriction').val('asc');
		}
	}else{
		$('#orderRestriction').val('asc');
	}
	// Configuracion de campo por el que se ordena
	$('#orderRestrictionField').val(campo);
	
	goRestrictionPage(1);
}

//Refresca el listado de cargos
function goRestrictionPage(page){
	$('#currentRestrictionPage').val(page);
	reloadRestrictions('post', jQuery('#restrictionForm').serialize());
}

//Actualiza el listado de restricciones
function loadRestrictionsList(userId) {
	if(userId != '') {
		jsAjaxStatus.startAjax();
		$.ajaxSetup({cache : false});
		$.ajax({
			url : "userManagement/restriction/loadList",
			dataType : 'html',
			data : 'userId=' + userId,
			async : false,
			success : function(model) {
				// se carga los dos listatados de las autorizaciones de sede
				$("#restrictionList").html(model);
				jsAjaxStatus.stopAjax();
			},
			error : function error(jqXHR, textStatus, errorThrown) {
				showError(genericMessage);
				jsAjaxStatus.stopAjax();
			}
		});
	}
}
