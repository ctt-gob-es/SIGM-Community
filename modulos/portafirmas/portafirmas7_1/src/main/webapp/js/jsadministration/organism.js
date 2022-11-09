var isLoadOrganisms = false;

//Para cargar los organismos una única vez
function loadOrganisms() {	
	if(!isLoadOrganisms) {
		reloadOrganisms();
	}
}

//Para recargar los organismos
function reloadOrganisms() {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "organismsManagement/loadListOrganism",
		dataType: 'html',
		async: false,
		success: function(model) {
			$("#tab11").html(model);
			configOrganismModal("organismsManagement", reloadOrganisms);
        	$('#organism_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		},
		error: function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
        	$('#organism_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
};

//Para insertar o modificar una sede
function saveOrganism(baseUrl, funcionRecargaDeSedes) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "organismsManagement/saveOrganism",
		type: 'post',
		dataType: 'json',
		data: "primaryKey=" + $('#organismPk').val() +
			  "&organismCode=" + $('#organismCode').val() + 
			  "&organismName=" + $('#organismName').val() +
			  "&organismParent=" + $('#organismParent').val(),
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				funcionRecargaDeSedes();
			} else {
				$('#organismErrorMessage').html(getErrors(data));
			}
			jsAjaxStatus.stopAjax();
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
        	$('#organism_modal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

//Se crea el diálogo de la ventana modal de organismos
function configOrganismModal(baseUrl, funcionDeRecarga) {
	$("#organism_modal").dialog({
	    autoOpen: false,
	    resizable: false,
	    width: 500,
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
	  	        	text: "Guardar",                   
	  	            "class": 'primary',
	  	            click: function() {
	  	            	saveOrganism(baseUrl, funcionDeRecarga);
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
	      	$('#organismErrorMessage').html('');
	    }
	});
	isLoadOrganisms = true;
}

//Prepara el borrado de un organismo
function prepareDeleteOrganism(primaryKey, funcionSi) {
	$('#questionMessageOrganism').html('Va a borrar un organismo, ¿está usted seguro?');
	if(funcionSi==null || funcionSi == undefined || funcionSi == "null" || funcionSi == "undefined"){
		$('#yesButtonOrganism').attr('onclick',"deleteOrganism('" + primaryKey + "');");
	}
	else{
		$('#yesButtonOrganism').attr('onclick',funcionSi+"('" + primaryKey + "');");
	}
	
	$('#questionOrganism').show();
}

//Para borrar una sede siendo administrador de organismo
function deleteOrganism(primaryKey) {
	$('#questionOrganism').hide();
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: "organismsManagement/deleteOrganism",
		dataType: 'json',
		data: "primaryKey=" + primaryKey,
		async: true,
		success:
			function(data) {
			if(jQuery.isEmptyObject(data)) {
				reloadOrganisms();
			} else {
				showError(getErrors(data));
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

//Prepara el alta de un organismo
function prepareNewOrganism() {
	$('#organismPk').val('');
	$('#organismCode').val('');
	$('#organismName').val('');
	$('#organism_modal').dialog('open');
}

//Prepara la modificación de un organismo
function prepareModifyOrganism(primaryKey) {
	$('#organismPk').val(primaryKey);
	$('#organismCode').val($('#organismCode_' + primaryKey).html());
	$('#organismName').val($('#organismName_' + primaryKey).html());
	$('#organism_modal').dialog('open');
}
