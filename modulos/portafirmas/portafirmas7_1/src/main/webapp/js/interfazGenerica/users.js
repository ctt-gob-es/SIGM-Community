// Preparación para el borrado de usuario
function prepareDeleteExternalUser(primaryKey, identifier) {
	$('#questionMessageExternalUser').html('Va a borrar al usuario con ID "' + identifier + '", ¿está usted seguro?');
	$('#yesButtonExternalUser').attr('onclick','deleteUser(' + primaryKey + ');');
	$('#questionExternalUser').show();
}

// Borrado de usuario
function deleteUser(primaryKey)  {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "externalUser/user/delete",
		type: 'post',
		dataType: 'json',
		data: 'primaryKey=' + primaryKey,
		async: true,
		success:
			function(response) {
				$('#questionExternalUser').hide();
	        	window.location.reload();
	        	jsAjaxStatus.stopAjax();
			},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
			jsAjaxStatus.stopAjax();
		}
	});
}

// Preparación para insertar un usuario: Se resetean los campos
function prepareInsertExternalUser() {
	$('#idUsuario').val('');
	$('#dni').val('');
	$('#nombre').val('');
	$('#apellido1').val('');
	$('#apellido2').val('');
	$('#portafirmas').val('');
	$('#externalUser_dialog').dialog('open');
	$('#valid').prop("checked",true);
}

// Preparación para modificar usuario: Se carga el usuario correspondiente en la ventana modal 
function prepareModifyExternalUser(primaryKey) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "externalUser/user/load",
		type: 'get',
		dataType: 'html',
		data: 'primaryKey=' + primaryKey,
		async: true,
		success:
			function(model) {
			$('#externalUser_dialog').html(model);
			$('#externalUser_dialog').dialog('open');
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#externalUser_dialog').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

// Guarda y modifica al usuario que presenta la ventana modal
function saveExternalUser() {
	var serializado = $("#form_externalUsers_modal").serialize();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "externalUser/user/save",
		type: 'post',
		dataType: 'json',
		data: serializado,
		async: true,
		success:
			function(warning) {
			$('#warningExternalUserModal').hide();
			// Si no hay avisos, se refresca la página
			if(jQuery.isEmptyObject(warning)) {
	        	$('#externalUser_dialog').dialog("close");
	        	window.location.reload();
			} else {
				// Si se detectan errores, se muestran los avisos en la modal
				//$('#errorMsgUser').html(getErrors(warning));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#externalUser_dialog').dialog("close");
			jsAjaxStatus.stopAjax();
//			$('#warningExternalUserModal').hide();
		}
	});
}

//Al terminar de cargar la pagina, se configuran componentes
$(document).ready(function() {
	
	// Ventana modal para edición de usuario
	$("#externalUser_dialog").dialog({
	    autoOpen: false,
	    resizable: false,
	    width: 350,
	    modal: true,       
	    buttons: [
	        {
	        	text: "Cancelar",       
	            "class": 'secondary',
	            click: function() {                   
					$('#warningUserModal').hide();
	            	$(this).dialog("close");
	            }
	        },
	        {                   
	        	text: "Aceptar",                   
	            "class": 'primary',
	            click: function() {
	            	var dniCorrecto = validarDNI($("#dni").val());
	            	if (!dniCorrecto){
		            	if (confirm("El identificador introducido no tiene formato de DNI:\n ¿desea continuar?\n")){
		            		saveExternalUser();
						}
	            	} else {
	            		saveExternalUser();
	            	}
	            }
	        }           
	    ],   
	    //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
	    create:function () {               
	        $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
	        $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
	    }
	});

});// Fin cargar página


function validarDNI (value) {
	  var validChars = 'TRWAGMYFPDXBNJZSQVHLCKET';
	  var nifRexp = /^[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKET]$/i;
	  var nieRexp = /^[XYZ][0-9]{7}[TRWAGMYFPDXBNJZSQVHLCKET]$/i;
	  var str = value.toString();

	  if (!nifRexp.test(str) && !nieRexp.test(str)) return false;

	  var nie = str
	      .replace(/^[X]/, '0')
	      .replace(/^[Y]/, '1')
	      .replace(/^[Z]/, '2');

	  var letter = str.substr(-1);
	  var charIndex = parseInt(nie.substr(0, 8)) % 23;

	  if (validChars.charAt(charIndex) === letter) return true;

	  return false;
}