var isLoadGroups = false;

// Carga los grupos y configura las ventana modales
function loadGroups() {
	if(!isLoadGroups) {
		jsAjaxStatus.startAjax();
		$.ajaxSetup({cache : false});
		$.ajax({
			url : "usersManagement/group/loadGroups",
			dataType : 'html',
			async : false,
			success : function(model) {
				// se carga la pestaña de grupos
				$("#tab3").html(model);
				// se configuran los componentes
				setupGroup();
//				jsAjaxStatus.stopAjax();
			},
			error : function error(jqXHR, textStatus, errorThrown) {
				showError(genericMessage);
//				jsAjaxStatus.stopAjax();
			}
		});
		jsAjaxStatus.stopAjax();
		isLoadGroups = true;
	}
}

// Recarga los grupos únicamente
function reloadGroups() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache : false});
	$.ajax({
		url : "usersManagement/group/reloadGroups",
		dataType : 'html',
		async : false,
		success : function(model) {
			// se recarga la lista de grupos
			
			$("#groupList").html(model);
//			jsAjaxStatus.stopAjax();
		},
		error : function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
//			jsAjaxStatus.stopAjax();
		}
	});
	jsAjaxStatus.stopAjax();
}

// Para configurar los componentes JQuery de la pestaña
function setupGroup() {
	// Ventana modal para crear y modificar un grupo
	$("#group_modal").dialog({
	    autoOpen: false,
	    resizable: false,
	    width: 300,
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
	            	saveGroup();
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
	// Ventana modal para gestionar los usuarios de un grupo
	$("#groupUsersModal").dialog({
	    autoOpen: false,
	    resizable: false,
	    width: 400,
	    modal: true,       
	    buttons: [
	        {
	        	text: "Cerrar",       
	            "class": 'secondary',
	            click: function() {                
	            	$(this).dialog("close");
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

//Prepara la modificación de un grupo
function prepareModifyGroup(id) {
	$('#errorMsgGroup').html('');
	$('#groupId').val(id);
	$('#groupCode').val($('#groupIdCode_' + id).val());
	$('#groupDescription').val($('#groupIdDescription_' + id).val());
	$('#groupSeat').val($('#groupIdSede_' + id).val());
	$('#group_modal').dialog('open');
}

//Prepara el alta de un Grupo
function prepareNewGroup() {
	$('#errorMsgGroup').html('');
	$('#groupId').val('');
	$('#groupCode').val('');
	$('#groupDescription').val('');
	$('#groupSeat').val('');
	$('#group_modal').dialog('open');
}

//Guarda un grupo
function saveGroup(ignoreWarnings) {
	if(ignoreWarnings==null || ignoreWarnings == undefined){
		ignoreWarnings = false;
	}

	var dataModal = {};
    $("#groupForm_modal").serializeArray().map(function(x){dataModal[x.name] = x.value;});

	var dataEnviar = {"group":dataModal,"ignoreWarnings":ignoreWarnings};
	var dataEnviarString = JSON.stringify(dataEnviar);
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "usersManagement/group/insertGroup",
		type: 'post',
		contentType: 'application/json',
		data: dataEnviarString,
        dataType: 'json',
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)
					|| (jQuery.isEmptyObject(data.errors)
							&& (ignoreWarnings || jQuery.isEmptyObject(data.warnings)) )) {
				reloadGroups();
				$('#group_modal').dialog('close');
			} else {
				$('#errorMsgGroup').html(getErrorsFromErrorsAndWarnings(data));
				$('#errorMsgGroup').show();
				if(jQuery.isEmptyObject(data.errors)){
					if (confirm("Se presentan las siguientes advertencias en su proceso:\n"+getWarningsFromErrorsAndWarnings(data)+"\ndesea continuar?\n")){
						saveGroup(true);
					}
				}
//				jsAjaxStatus.stopAjax();
			}
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	jsAjaxStatus.stopAjax();
		}
	});
}

//Dialogo para borrar grupo
function prepareDeleteGroup(id) {
	$('#questionMessageGroup').html('Va a borrar el grupo ' + $('#groupIdCode_' + id).val() +', ¿está usted seguro?');
	$('#yesButtonGroup').attr('onclick',"deleteGroup('" + id + "');");
	$('#divErrorMessage').html('');
	$('#questionGroup').show();
}

//Borrar un grupo
function deleteGroup(id) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache : false});
	$.ajax({
		url : "usersManagement/group/deleteGroup",
		dataType : 'json',
		data : "id=" + id,
		async : true,
		success : function(data) {
			if (jQuery.isEmptyObject(data)) {
				$('#questionGroup').hide();
				reloadGroups();
				$('#group_modal').dialog('close');
			} else {
				$('#questionGroup').hide();
				showWarning(getErrors(data));
//				jsAjaxStatus.stopAjax();
			}
			jsAjaxStatus.stopAjax();
		},
		error : function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
			$('#questionGroup').hide();
			jsAjaxStatus.stopAjax();
		}
	});
}