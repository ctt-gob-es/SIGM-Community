var isLoadSeatAuthorizations = false;

// Carga la pestaña inicialmente
function loadSeatAuthorizations() {
	if(!isLoadSeatAuthorizations) {
		jsAjaxStatus.startAjax();
		$.ajaxSetup({cache : false});
		$.ajax({
			url : "usersManagement/seatAuthorization/loadAuthorizations",
			dataType : 'html',
			async : false,
			success : function(model) {
				// se carga los dos listatados de las autorizaciones de sede
				$("#tab4").html(model);
				setupSeatAuthorization();
				jsAjaxStatus.stopAjax();
			},
			error : function error(jqXHR, textStatus, errorThrown) {
				showError(genericMessage);
				jsAjaxStatus.stopAjax();
			}
		});
		isLoadSeatAuthorizations = true;
	}
}

// Actualiza los listados de autorizaciones enviadas y recibidas
function loadSeatAuthorizationsList(userId) {
	if(userId != '') {
		jsAjaxStatus.startAjax();
		$.ajaxSetup({cache : false});
		$.ajax({
			url : "usersManagement/seatAuthorization/loadList",
			dataType : 'html',
			data : 'userId=' + userId,
			async : false,
			success : function(model) {
				// se carga los dos listatados de las autorizaciones de sede
				$("#seatAuthorizationsList").html(model);
				jsAjaxStatus.stopAjax();
			},
			error : function error(jqXHR, textStatus, errorThrown) {
				showError(genericMessage);
				jsAjaxStatus.stopAjax();
			}
		});
	}
}

// Para configurar los componentes JQuery de la pestaña
function setupSeatAuthorization() {
	// Autocompletado para buscar usuarios de sede
	$('#searchSeatAuthorization').autocomplete({
		minLength: 3,
		source: 'usersManagement/seatAuthorization/autocomplete',
		select: function( event, ui ) {
			$('#searchSeatAuthorization').val(ui.item.label);
			$('#searchSeatAuthorizationId').val(ui.item.id);
			loadSeatAuthorizationsList(ui.item.id);
			return false;
		}
	});
	
	// Se configuran los calendarios
	$( ".datepicker" ).datepicker();

	// Patrón de máscara personalizado
	$.mask.definitions['h'] = "[0-2]";
	$.mask.definitions['t'] = "[0-5]";

	// Máscaras para las fechas para alta de autorización..
	$('#new_frequest').mask("99/99/9999");
	$('#new_hrequest').mask("h9:t9:t9",{placeholder:"0"});
	$('#new_frevocation').mask("99/99/9999");
	$('#new_hrevocation').mask("h9:t9:t9",{placeholder:"0"});
	
	// .. y para modificación
	$('#modify_frevocation').mask("99/99/9999");
	$('#modify_hrevocation').mask("h9:t9:t9",{placeholder:"0"});
	
	// Configuración de autocompletado para autorizaciones nuevas
	$('#new_authorized_fullName').autocomplete({
		 minLength: 3,
		 source: 'usersManagement/seatAuthorization/autocomplete',
		 select: function( event, ui ) {
			 $('#new_authorized_fullName').val(ui.item.label);
			 $('#new_authorized_pk').val(ui.item.id);
			 return false;
		 }
	});
	$('#new_receiver_fullName').autocomplete({
		 minLength: 3,
		 source: 'usersManagement/seatAuthorization/autocomplete',
		 select: function( event, ui ) {
			 $('#new_receiver_fullName').val(ui.item.label);
			 $('#new_receiver_pk').val(ui.item.id);
			 return false;
		 }
	});
	// Ventana modal para crear una autorización de administrador de sede
	$("#new_authorization_modal").dialog({
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
	            	insertAuthorization();
	            }
	        }           
	    ],   
	    create:function () {               
	    	customModalStyle();
	    },
        close:function () {
            $('#errorInsertAuthorization').html('');        	
        }
	});

	// Ventana modal para crear modificar una autorización de administrador de sede
	$("#modify_authorization_modal").dialog({
	    autoOpen: false,
	    resizable: false,
	    width: 350,
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
	            	updateAuthorization();
	            }
	        }           
	    ],   
	    create:function () {               
	    	customModalStyle();
	    },
	    close:function () {
	    	$('#errorModifyAuthorization').html('');        	
	    }
	});
}

//Prepara el alta de una autorización y muestra la modal correspondiente
function prepareNewSeatAuthorization() {
	$('#new_authorized_fullName').val('');
	$('#new_authorized_pk').val('');
	$('#new_authorizationType').val('');
	$('#new_receiver_fullName').val('');
	$('#new_receiver_pk').val('');
	$('#new_frequest').val('');
	$('#new_hrequest').val('00:00:00');
	$('#new_frevocation').val('');
	$('#new_hrevocation').val('00:00:00');
	$('#new_observations').val('');
	$('#new_authorization_modal').dialog('open');
}

//Guarda una nueva autorización
function insertAuthorization() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "usersManagement/seatAuthorization/insert",
		type: 'post',
		dataType: 'json',
		data: $("#newSeatAuthorizationForm").serialize(),
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				$('#new_authorization_modal').dialog('close');
				jsAjaxStatus.stopAjax();
				loadSeatAuthorizationsList($('#searchSeatAuthorizationId').val());
			} else {
				$('#errorInsertAuthorization').html(getErrors(data));
				jsAjaxStatus.stopAjax();
			}
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	jsAjaxStatus.stopAjax();
		}
	});
}

//Prepara la modificación de una autorización y muestra la modal correspondiente
function prepareModifySeatAuthorization(primaryKey) {
	$('#modify_authorization_pk').val(primaryKey);
	var fRevocation = $('#frevocation_' + primaryKey).html().split(' ');
	$('#modify_frevocation').val(fRevocation[0]);
	if(fRevocation[1] == null || fRevocation[1] == '') {
		$('#modify_hrevocation').val('00:00:00');
	} else {
		$('#modify_hrevocation').val(fRevocation[1]);
	}
	$('#modify_authorization_modal').dialog('open');
}

//Actualiza una autorización. En principio, solo se puede cambiar la fecha de revocación
function updateAuthorization() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "usersManagement/seatAuthorization/update",
		type: 'post',
		dataType: 'json',
		data: $("#modifySeatAuthorizationForm").serialize(),
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				$('#modify_authorization_modal').dialog('close');
				jsAjaxStatus.stopAjax();
				loadSeatAuthorizationsList($('#searchSeatAuthorizationId').val());
			} else {
				$('#errorModifyAuthorization').html(getErrors(data));
				jsAjaxStatus.stopAjax();
			}
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	jsAjaxStatus.stopAjax();
		}
	});
}
