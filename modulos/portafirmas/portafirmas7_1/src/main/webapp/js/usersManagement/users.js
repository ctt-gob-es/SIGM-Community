// Preparación para el borrado de usuario
function prepareDeleteUser(primaryKey, identifier) {
	$('#questionMessageUser').html('Va a borrar al usuario con ID "' + identifier + '", ¿está usted seguro?');
	$('#yesButtonUser').attr('onclick','deleteUser(' + primaryKey + ');');
	$('#questionUser').show();
}

// Borrado de usuario
function deleteUser(primaryKey)  {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "usersManagement/user/delete",
		type: 'post',
		dataType: 'json',
		data: 'primaryKey=' + primaryKey,
		async: true,
		success:
			function(response) {
			// Si no hay avisos ni errores, es que se ha borrado y refrescamos la página
			if(jQuery.isEmptyObject(response.warnings) && jQuery.isEmptyObject(response.errors)) {
				goUserPage(1);
			} else  if (!jQuery.isEmptyObject(response.errors)) {
				$('#questionUser').hide();
				showError(getErrors(response.errors));
			} else  if (!jQuery.isEmptyObject(response.warnings)) {
				// Hay avisos. No se borró el usuario y se pregunta si se quiere deshabilitar
				prepareRevokeUser(primaryKey, getErrors(response.warnings));
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

// Preparación para deshabilitar al usuario
function prepareRevokeUser(primaryKey, warning) {
	$('#questionMessageUser').html(warning + '¿desea deshabilitar el usuario?');
	$('#yesButtonUser').attr('onclick','revokeUser(' + primaryKey + ');');
	$('#questionUser').show();
}

// Deshabilita a un usuario
function revokeUser(primaryKey) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "usersManagement/user/revoke",
		type: 'post',
		dataType: 'json',
		data: 'primaryKey=' + primaryKey,
		async: true,
		success:
			function(errors) {
			// Si no hay errores, se refresca la página
			if(jQuery.isEmptyObject(errors)) {
				goUserPage(1);
			} else {
				// Hay errores, se oculta la venta de confimación y se muestra el error
				$('#questionUser').hide();
				showError(getErrors(errors));
				jsAjaxStatus.stopAjax();
			}
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
			jsAjaxStatus.stopAjax();
		}
	});
}

// Preparación para insertar un usuario: Se resetean los campos
function prepareInsertUser() {
	inicializarFragmentoUsuarios();
	$('#nif').val('');
	$('#primaryKeyUser').val('');
	$('#name').val('');
	$('#lastName1').val('');
	$('#lastName2').val('');
	$('#password').val('');
	$('#ldapId').val('');
	$('#email').val('');
	$('#email').show();
	$('#labelEmail').show();
	var isGestorLdap =$('#isGestorLdap').val(); 
	if (isGestorLdap === "true"){
		$('#ldapId').show();
		$('#labelldapId').show();
	} else {
		$('#ldapId').hide();
		$('#labelldapId').hide();
	}
	$('#province').val('');
	$('#valid').prop("checked",true);
	$('#publico').removeProp("checked");
	$("input[name='profiles']").removeProp("checked");
	$("input[id='adminSeats']").removeProp("checked");
	$('#adminOrg').removeProp("checked");
	toogleAdminSeats(adminSeatProfile);
	toogleAdminSeats(adminOrganismProfile);
	$("#idInputOrganismo").val('');
	$('#errorMsgUser').html('');
	$('#users_modal').dialog('open');
	$('#scrollAdminSeat').scrollTop(0);
	$('#scrollAdminOrg').scrollTop(0);
	$('#nif').attr('onblur','aMaysTrim(this);loadUser(this.value);');
	$('#nif').attr('onkeyup','aMaysTrim(this)');
	$('#tipoUsuario').val('USUARIO');
	var isAdministrador =$('#isAdministrador').val();  
	if (isAdministrador === "true"){
		$('#tipoUsuario').show();
		$('#labeltipoUsuario').show();
	} else {
		$('#tipoUsuario').hide();
		$('#labeltipoUsuario').hide();
	}

}

// Preparación para modificar usuario: Se carga el usuario correspondiente en la ventana modal 
function prepareModifyUser(primaryKey, provinceName) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "usersManagement/user/load",
		type: 'post',
		dataType: 'html',
		data: 'primaryKey=' + primaryKey,
		async: true,
		success:
			function(model) {
			$('#users_modal').html(model);
			toogleAdminSeats(adminSeatProfile);
			toogleAdminSeats(adminOrganismProfile);
			$('#users_modal').dialog('open');
			$('#scrollAdminSeat').scrollTop(0);
			$('#scrollAdminOrg').scrollTop(0);
			inicializarFragmentoUsuarios();
			var isGestorLdap =$('#isGestorLdap').val(); 
			if (isGestorLdap === "true"){
				$('#ldapId').show();
				$('#labelldapId').show();
			} else {
				$('#ldapId').hide();
				$('#labelldapId').hide();
			}
			$('#email').val('');
			$('#email').hide();
			$('#labelEmail').hide();
			jsAjaxStatus.stopAjax();
			if(provinceName !== undefined) {
				$('#warningMessageUserModal').html('Este usuario ya está dado de alta en la sede "' + provinceName + '"');
				$('#warningUserModal').show();
			}
			$('#nif').attr('onblur','aMaysTrim(this);');
			$('#nif').attr('onkeyup','aMaysTrim(this)');
			var isAdministrador =$('#isAdministrador').val();  
			if (isAdministrador === "true"){
				$('#tipoUsuario').show();
				$('#labeltipoUsuario').show();
			} else {
				$('#tipoUsuario').hide();
				$('#labeltipoUsuario').hide();
			}

		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#users_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function loadUser(dni) {
	$.ajaxSetup({cache: false});
	$.ajax({url: "usersManagement/user/load",
		type: 'get',
		data: 'dni=' + dni,
		async: true,
		success:
			function(model) {
				if(model.primaryKey !== undefined) {
					prepareModifyUser(model.primaryKey, model.provinceName);
				}
			},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			}
	});
}

// Guarda y modifica al usuario que presenta la ventana modal
function saveUser() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "usersManagement/user/save",
		type: 'post',
		dataType: 'json',
		data: $("#form_users_modal").serialize(),
		async: true,
		success:
			function(warning) {
			$('#warningUserModal').hide();
			// Si no hay avisos, se refresca la página
			if(jQuery.isEmptyObject(warning)) {
	        	$('#users_modal').dialog("close");
				goUserPage(1);
			} else {
				// Si se detectan errores, se muestran los avisos en la modal
				$('#errorMsgUser').html(getErrors(warning));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#users_modal').dialog("close");
			jsAjaxStatus.stopAjax();
			$('#warningUserModal').hide();
		}
	});
}

// Configuración de la ordenación
function orderUser(campo){
	// Configuración del sentido de la ordenación
	if($('#orderUserField').val() == campo){
		if($('#orderUser').val() == 'asc'){
			$('#orderUser').val('desc');
		}else{
			$('#orderUser').val('asc');
		}
	}else{
		$('#orderUser').val('asc');
	}
	// Configuracion de campo por el que se ordena
	$('#orderUserField').val(campo);
	
	goUserPage(1);
}

//Recarga la página pero actualizando la página que se quiere mostrar en la paginación
function goUserPage(page){
	$('#currentUserPage').val(page);
	jQuery('#usersForm').submit();
}

function showPassword() {
	$('#passwordVisible').val($('#password').val());
	$('#password').hide();
	$('#passwordVisible').show();
}

function hidePassword() {
	$('#passwordVisible').val('');
	$('#password').show();
	$('#passwordVisible').hide();
}

function toogleAdminSeats(profile) {
	if(profile == adminSeatProfile) {
		if($('#profile_' + adminSeatProfile).is(':checked')) {
			$('#adminSeatDiv').show();
		} else {
			$('#adminSeatDiv').hide();
		}
		$('#scrollAdminSeat').scrollTop(0);
	}
	if(profile == adminOrganismProfile) {	
		if($('#profile_' + adminOrganismProfile).is(':checked')) {
			$('#adminOrgDiv').show();
		} else {
			$('#adminOrgDiv').hide();
			$('#adminOrg').removeAttr('checked')
		}
		$('#scrollAdminOrg').scrollTop(0);
	}
}

//Constante para indicar el perfil de administración de provincias
const adminSeatProfile = 'ADMINPROV';
const adminOrganismProfile = 'ADMIN_ORG';

//Al terminar de cargar la pagina, se configuran componentes
$(document).ready(function() {
	
	// Ventana modal para edición de usuario
	$("#users_modal").dialog({
	    autoOpen: false,
	    resizable: false,
	    width: 650,
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
	            	var dniCorrecto = validarDNI($("#nif").val());
	            	if (!dniCorrecto){
		            	if (confirm("El identificador introducido no tiene formato de DNI:\n ¿desea continuar?\n")){
		            		saveUser();
						}
	            	} else {
	            		saveUser();
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


//Preparación para la simulación del usuario
function prepareSimulateUser(primaryKey, identifier) {
	$('#questionMessageUser').html('Va a simular al usuario con ID "' + identifier + '", ¿está usted seguro?');
	$('#yesButtonUser').attr('onclick','simulateUser(' + primaryKey + ');');
	$('#questionUser').show();

}

//Simula a un usuario
function simulateUser(primaryKey) {	
	jsAjaxStatus.startAjax();
	$('#simulateUserPk').val(primaryKey);
	$('#simulateUserForm').submit();
}

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

function aMaysTrim(e) {
	var textoAux =  e.value; 
	textoAux = textoAux.toUpperCase();
  e.value = textoAux.trim();
}