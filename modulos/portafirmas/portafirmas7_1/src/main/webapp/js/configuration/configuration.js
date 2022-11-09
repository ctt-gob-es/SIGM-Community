/*******************************************

   Este script está complementado por:
   
     · personalData.js
     · authorizations.js
     · validators.js

 *******************************************/

function toogleApps() {
	if($('#listApps').is(':checked')) {
		$('#scrollAppsTitle').show();
		$('#scrollApps').show();
	} else {
		$('#scrollAppsTitle').hide();
		$('#scrollApps').hide();
	}
	$('#scrollApps').scrollTop(0);
}

//Para insertar un validador
function insertarValorfiltroValidadorMovilActivo() {
	var hasFilter = 'N';
	if($('#filtroValidadorMovilActivoCheck').prop('checked')) {
		hasFilter = 'S';
	}
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "configuration/insertarValorfiltroValidadorMovilActivo",
		dataType: 'json',
		data: "hasFilter=" + hasFilter,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				;
			} else {
				$('#divErrorMessage').html(getErrors(data));
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


// Al terminar de cargar la pagina
$(document).ready(function() {

	// Configuración de las máscaras
	$.mask.definitions['h'] = "[0-2]";
	$.mask.definitions['t'] = "[0-5]";
	$('#frequest').mask("99/99/9999");
	$('#hrequest').mask("h9:t9:t9",{placeholder:"0"});
	$('#frevocation').mask("99/99/9999");
	$('#hrevocation').mask("h9:t9:t9",{placeholder:"0"});
	
	$('#new_frequest').mask("99/99/9999");
	$('#new_hrequest').mask("h9:t9:t9",{placeholder:"0"});
	$('#new_frevocation').mask("99/99/9999");
	$('#new_hrevocation').mask("h9:t9:t9",{placeholder:"0"});
	
	// Configuración de autocompletado para autorizaciones..
	$('#new_authorized_fullName').autocomplete({
		 minLength: 3,
		 source: 'configuration/authorization/autocompleteAuthorizations',
		 select: function( event, ui ) {
			 $('#new_authorized_fullName').val(ui.item.label);
			 $('#new_authorized_pk').val(ui.item.id);
			 return false;
		 }
	});
	//.. y autocompletado para validadores
	$('#new_validator_fullName').autocomplete({
		minLength: 3,
		source: 'configuration/validators/autocompleteValidators',
		select: function( event, ui ) {
			$('#new_validator_fullName').val(ui.item.label);
			$('#new_validator_pk').val(ui.item.id);
			return false;
		}
	});
	
	//.. y autocompletado para gestores
	$('#new_gestor_fullName').autocomplete({
		minLength: 3,
		source: 'configuration/gestores/autocompleteGestores',
		select: function( event, ui ) {
			$('#new_gestor_fullName').val(ui.item.label);
			$('#new_gestor_pk').val(ui.item.id);
			return false;
		}
	});
	
	//Se crea el diálogo de la ventana modal para modificar autorizaciones
	$('#modify_authorization_modal').dialog({
	    autoOpen: false,
	    resizable: false,
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
	            	 modifyAuthorization();
	            }
	        }           
	    ],   
	    //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
	    create:function () {               
	        $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	        $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
	    },
        close:function () {
            $('#divErrorMessageModify').html('');        	
        }
	});
	
	//Se crea el diálogo de la ventana modal para nuevas autorizaciones
	$('#new_authorization_modal').dialog({
	    autoOpen: false,
	    resizable: false,
	    //height:450,
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
	            	newAuthorization();
	            }
	        }           
	    ],   
	    //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
	    create:function () {               
	        $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	        $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
	    },
        close:function () {
            $('#divErrorMessage').html('');        	
        }
	});
	
	//Se crea el diálogo de la ventana modal de validadores
	$("#validators_modal").dialog({
	    autoOpen: false,
	    resizable: false,
	    modal: true,       
	    width: 400,
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
	    			jsAjaxStatus.startAjax();
	    			
	    			var values = $('input:checkbox[name="apps"]:checked')
	    	        .map(function() {return $(this).val();
	    	        }).get();
	    			
           			insertValidator($('#new_validator_pk').val(), values);
	            }
	        }           
	    ],   
	    //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
	    create:function () {               
	        $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	        $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
	    },
	    close:function () {
	        $('#new_validator_fullName').val('');        	
	        $('#new_validator_pk').val('');        	
            $('#validatorErrorMessage').html('');        	
	    }
	});
	
	//Se crea el diálogo de la ventana modal de gestores
	$("#gestores_modal").dialog({
	    autoOpen: false,
	    resizable: false,
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
	    			jsAjaxStatus.startAjax();
           			insertGestor($('#new_gestor_pk').val());
	            }
	        }           
	    ],   
	    //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
	    create:function () {               
	        $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	        $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
	    },
	    close:function () {
	        $('#new_gestor_fullName').val('');        	
	        $('#new_gestor_pk').val('');        	
            $('#gestorErrorMessage').html('');        	
	    }
	});
	
	//Se crea el diálogo para confirmaciones
	$("#confirmPersonalData").dialog({
	    autoOpen: false,
	    resizable: false,
	    modal: true,       
	    create: customModalStyle,
	    close:function () {
	        $('#new_validator_fullName').val('');        	
	        $('#new_validator_pk').val('');        	
            $('#validatorErrorMessage').html('');
            $('#new_gestor_fullName').val('');        	
	        $('#new_gestor_pk').val('');        	
            $('#gestorErrorMessage').html('');   
	    }
	});
	
	//Se crea el diálogo para mensajes de ok
	$("#okPersonalData").dialog({
	    autoOpen: false,
	    resizable: false,
	    modal: true,       
	    create: customModalStyle,
	    close:function () {
	        $('#new_validator_fullName').val('');        	
	        $('#new_validator_pk').val('');        	
            $('#validatorErrorMessage').html('');
            $('#new_gestor_fullName').val('');        	
	        $('#new_gestor_pk').val('');        	
            $('#gestorErrorMessage').html('');   
	    }
	});
	
	toogleApps();
	
});// Fin cargar página