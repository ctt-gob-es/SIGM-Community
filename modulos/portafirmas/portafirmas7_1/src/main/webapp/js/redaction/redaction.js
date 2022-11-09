var primeraLineaDeFirmaBorrada = false;
var XMLHttpRequestObject=false;
var listaAccionesFirmante;

try {
	// Creacion del objeto AJAX para navegadores no IE
	XMLHttpRequestObject=new ActiveXObject("Msxml2.XMLHTTP");
} catch(e) {
	try	{
		// Creacion del objet AJAX para IE
		XMLHttpRequestObject=new ActiveXObject("Microsoft.XMLHTTP");
	} catch(E) {
		if (!XMLHttpRequestObject && typeof XMLHttpRequest!='undefined') XMLHttpRequestObject=new XMLHttpRequest();
	}
}

function mostrarDocumentos(id, fileName) {

	var documentTypes = cargarTiposDocumento();
	var documentTypesHTML = "";
	$.each(documentTypes, function (i, item) {
		if (item.name == 'GENERICO') {
			documentTypesHTML += '<option value="' + item.name +'" selected="selected">' + item.name + '</option>';
		} else {
			documentTypesHTML += '<option value="' + item.name +'">' + item.name + '</option>';
		}
    });

	var idDoc = 'document_' + id;
		$('#uploadedDocs').addClass('attachedDocs');
		$('#uploadedDocs').append('<li id="' + idDoc + '"><a class="fileName"><span>' + fileName + '</span></a>'+
									'<select type="text" id="signline_type_0" name="signline_type_0" value="" maxlength="15"' +
								   	 'onchange="actualizarTipoDocumento(\'docId=' + id + '&type=document&docType=\' + this.value);">' + 
								   	 	documentTypesHTML +
								   	'</select>'+
								  '<a href="javascript:void(0)" onclick="borrarDocumento(\'' + idDoc + '\',\'type=document&id=' + id + '\');" class="va_top right" >' + 
								  '<span class="mf-icon mf-icon-delete-16" ></span></li>');
	// Si estaba activo el mensaje de error se quita
	var errorMsg = $("#uploadedDocsErrorMessage");
	if (errorMsg != null) {
		errorMsg.remove();
	}
}

function errorDocumentos(id, fileName) {
//No hay que hacer nada, ya aparecen los errores
}


function mostrarAnexos(id, fileName) {
	var documentTypes = cargarTiposDocumento();
	var documentTypesHTML = "";
	$.each(documentTypes, function (i, item) {
		if (item.name == 'GENERICO') {
			documentTypesHTML += '<option value="' + item.name +'" selected="selected">' + item.name + '</option>';
		} else {
			documentTypesHTML += '<option value="' + item.name +'">' + item.name + '</option>';
		}
    });

	var idDoc = 'annex_' + id;
	$('#uploadedAnexos').addClass('attachedDocs');
	$('#uploadedAnexos').append('<li id="' + idDoc + '"><a class="fileName"><span>' + fileName + '</span></a>'+
								'<select type="text" id="signline_type_0" name="signline_type_0" value="" maxlength="15"' +
							   	 'onchange="actualizarTipoDocumento(\'docId=' + id + '&type=annex&docType=\' + this.value);">' + 
							   	 	documentTypesHTML +
							   	'</select>'+
							  '<a href="javascript:void(0)" onclick="borrarDocumento(\'' + idDoc + '\',\'type=annex&id=' + id + '\');" class="va_top right" >' + 
							  '<span class="mf-icon mf-icon-delete-16" ></span></li>');

}

function errorAnexos(id, fileName) {
	var messageError = "Se ha producido un error al subir al servidor el anexo " + fileName;
			if ($('#error').is(":visible")){
				var aux = $('#error').html();
				aux += "<br/>";
				aux += messageError;
				$('#error').html(aux);
			} else {
				$('#error').html(messageError);
			}
			$('#error').show();
			$('#error').delay(10000).hide(600);
}

/**
 * Método que carga la lista de tipos de documento
 * @returns Lista de tipos de documento
 */
function cargarTiposDocumento() {
	var result = "";
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'redaction/getDocumentTypes',
     	dataType: 'json',
     	data: "",
     	async: false,
     	success:
     		function(model) {
     			result = model;
       		},
       	error:
       		function error(jqXHR, textStatus, errorThrown) {
	       		$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
	      		$('#error').show();
       		}
	});
	jsAjaxStatus.stopAjax();
	return result;
}

/**
 * Método que carga la lista de tipos de documento
 * @returns Lista de tipos de documento
 */
function cargarListaAccionesFirmante() {
	var result = "";
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'redaction/getListaAccionesFirmante',
     	dataType: 'json',
     	data: "",
     	async: false,
     	success:
     		function(model) {
     			result = model;
       		},
       	error:
       		function error(jqXHR, textStatus, errorThrown) {
	       		$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
	      		$('#error').show();
       		}
	});
	jsAjaxStatus.stopAjax();
	return result;
}

function borrarDocumento(idDoc, queryString) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'redaction/deleteDocument',
     	dataType: 'html',
     	data: queryString,
     	async: false,
     	success:
     		function(model) {
     			$("#" + idDoc).remove();

     			var docs =  $('#uploadedDocs').html();
     			if (docs == null || docs == "") {
     				$('#uploadedDocs').removeClass("attachedDocs");
     			}

     			var docs =  $('#uploadedAnexos').html();
     			if (docs == null || docs == "") {
     				$('#uploadedAnexos').removeClass("attachedDocs");
     			}
       		},
       	error:
       		function error(jqXHR, textStatus, errorThrown) {
	       		$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
	      		$('#error').show();
       		}
	});
	jsAjaxStatus.stopAjax();
	return false;
}

function actualizarTipoDocumento(queryString) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'redaction/updateDocumentType',
     	dataType: 'html',
     	data: queryString,
     	async: false,
     	success:
     		function(model) {
     			
       		},
       	error:
       		function error(jqXHR, textStatus, errorThrown) {
	       		$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
	      		$('#error').show();
       		}
	});
	jsAjaxStatus.stopAjax();
	return false;
}

function cargarComboTiposUsurio() {
	$('#userTypeFilter').html("");
	$('#userTypeFilter').append($('<option>', {
        value: "filterAll",
        text : "Todos",
        selected: "selected"
    }));
	$('#userTypeFilter').append($('<option>', {
	    value: "filterPeople",
	    text : "Personas",
	}));
	$('#userTypeFilter').append($('<option>', {
	    value: "filterJobs",
	    text : "Cargos"
	}));
	$('#userTypeFilter').append($('<option>', {
	    value: "filterMostUsed",
	    text : "Favoritos"
	}));
}

function cargarComboSedesUsuarios(model) {
	$('#userSeatFilter').html("");
	$('#userSeatFilter').append($('<option>', {
        value: "",
        text : "Todas",
        selected: "selected"
    }));
	$.each(model.seatList, function (i, item) {
        $('#userSeatFilter').append($('<option>', {
            value: item.code,
            text : item.name
        }));
    });
}

function cargarPortafirmas(model) {
	$('#portafirmasFilter').html("");
	$('#portafirmasFilter').append($('<option>', {
        value: "",
        text : "Todos",
        selected: "selected"
    }));
	$.each(model.portafirmasList, function (i, portafirmas) {
        $('#portafirmasFilter').append($('<option>', {
            value: portafirmas.idPortafirmas,
            text : portafirmas.nombre
        }));
    });
}

function cargarListaUsuarios(model) {
	// Se reinicia la lista de usuarios
	$('.pickList_sourceList li').remove();
	$('.pickList_targetList li').remove();
	$("#userPickList").pickList("destroy");

	var pickListHTML = "";
	if(model!=undefined && model!=null && model.userSelectionList!=null && model.userSelectionList!=undefined){
		$.each(model.userSelectionList, function (i, item) {
			pickListHTML += '<option value="' + item.pk + '"';
			if (item.selected == true) {
				pickListHTML += ' selected="selected"';
			}
			pickListHTML += '>' + item.fullNameWithProvince + '</option>';
	    });
	}
	
	$("#userPickList").html(pickListHTML);

	if(model!=undefined && model!=null && model.userSelectionList!=null && model.userSelectionList!=undefined){
		$("#userPickList").pickList({
	        sourceListLabel:    "Resultados de la búsqueda",
	        targetListLabel:    "Firmantes seleccionados",
	        addAllLabel:        "Añadir todos",
	        addLabel:           "Añadir",
	        removeAllLabel:     "Borrar todos",
	        removeLabel:        "Borrar",
	        sortAttribute:      "value",
		});
	}

}

function loadUserPickList(queryString) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'redaction/selectUsers',
     	dataType: 'json',
     	data: queryString,
     	async: false,
     	success:
     		function(model) {
     			// Se limpia el filtro
     			$('#userNameFilter').val("");

     			// Se carga el combo con los tipos de usuario
     			cargarComboTiposUsurio();

     			// Se carga el combo con las sedes
     			cargarComboSedesUsuarios(model);
     			
     			cargarPortafirmas(model);

     			// Se carga la lista de usuarios
     			cargarListaUsuarios(model);
       		},
       	error:
       		function error(jqXHR, textStatus, errorThrown) {
	       		$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
	      		$('#error').show();
       		}
	});

	jsAjaxStatus.stopAjax();
	return false;
}

function loadSignLineConfig() {
	
	var signlines = "";
	var signLinesConfig = $(".signline");
	var signer_0 = $("#signer_0").val();

	signLinesConfig.each(function(i, item) {
		var value = $(item).val();

		if (signer_0 == '') {
			if (i > 0) {
				if (i == 1) {
					signlines = value;
				} else {
					signlines = signlines + "," + value;
				}
			}
		} else {
			if (i == 0) {
				signlines = value;
			} else {
				signlines = signlines + "," + value;
			}
		}
	});

	return signlines;
}

function loadSignLineAcciones() {
	var acciones = "";
	var signLinesacciones = $(".actionSigner");
	var signer_0 = $("#signer_0").val();

	signLinesacciones.each(function(i, item) {
		var value = $(item).val();

		if (signer_0 == '') {
			if (i > 0) {
				if (i == 1) {
					acciones = value;
				} else {
					acciones = acciones + "," + value;
				}
			}
		} else {
			if (i == 0) {
				acciones = value;
			} else {
				acciones = acciones + "," + value;
			}
		}
	});

	return acciones;
}
function deleteSignLine(index) {
	$("#signline_type_" + index).parent().remove();
	var signers = $("#signers").val().split(",");
	var newSigners = "";
	for (var i = 0; i < signers.length; i++) {
		if (i != index) {
			if (i == 0) {
				newSigners = signers[i];
			} else {
				newSigners = newSigners + "," + signers[i];
			}
		}
	}
	$("#signers").val("");
	$("#signers").val(newSigners);
}

function prepareUserPickListFilter() {
	jsAjaxStatus.startAjax();
	// Recupera los valores seleccionados
	var selected = "";
	var signers = $('#signlines').find('.signer_input');

	signers.each(function (i, item) {
		var value =  $(item).attr('codigo');
		if (i == 0) {
			selected = value;
		} else {
			selected = selected + "," + value;
		}
	});

	// Recupera los valores de los filtros
	var userNameFilter = $('#userNameFilter').val();
	var userTypeFilter = $('#userTypeFilter').val();
	var userSeatFilter = $('#userSeatFilter').val();
	var portafirmasFilter = $('#portafirmasFilter').val();

	// Recarga la lista de usuarios
	loadUserPickList("signers=" + encodeURIComponent(window.btoa(selected)) +
					 "&signLinesConfig=" + loadSignLineConfig() +
					 "&signLinesAccion=" + loadSignLineAcciones() +
					 "&userNameFilter=" + userNameFilter +
					 "&userTypeFilter=" + userTypeFilter +
					 "&userSeatFilter=" + userSeatFilter + 
					 "&portafirmasFilter=" + portafirmasFilter);
}

function prepareUserPickList() {
	jsAjaxStatus.startAjax();
	var	signersVal = $('#signers').encodeValue();
	loadUserPickList("signers=" + encodeURIComponent(window.btoa(signersVal)) + "&signLinesConfig=" + loadSignLineConfig() + "&signLinesAccion=" + loadSignLineAcciones() +
					 "&userNameFilter=&userTypeFilter=&userSeatFilter=&portafirmasFilter="+"&firstInvocation=true");
	$('#signers_modal').dialog('open');
}

function addSignLine() {
	var signLines = $(".signline");
	var i = signLines.length;
	if (i == 0) {
		 i = 1;
	}
	var auxhtml = "";
	auxhtml = auxhtml + '<li>' +
	   		'<select class="left signline" type="text" id="signline_type_' + i +'" name="signline_type_' + i + '" value="" maxlength="15">' +
	   			'<option selected = "selected">Firma</option>' +
	   			'<option>Visto bueno</option>' +
			'</select>' +
	   		'<div class="overflowh" style="position: relative;">' +
				'<input id="signer_' + i + '" class="ui-autocomplete-input signer_input" style="width: 58%;" type="text" th:field="*{signer}" value="" name="signer_' + i + '" autocomplete="off" role="textbox" aria-autocomplete="list" aria-haspopup="true" codigo="">' +
				'<a href="javascript:void(0);" onclick="deleteSignLine(' + i + ');" style="padding: 5px 0; position: absolute; right: 0px;"><span class="mf-icon mf-icon-delete-16" th:text="#{selectUsers}"></span></a>' +
				'<select id="actionSigner_'+ i +'" name="actionSigner_'+ i +'" class="actionSigner" >';
						$.each(listaAccionesFirmante, function (j, item) {
							if (item.id==1){
								auxhtml = auxhtml +'<option value="'+item.id+'" selected="selected">'+item.name+'</option>';
							} else {
								auxhtml = auxhtml +'<option value="'+item.id+'" >'+item.name+'</option>';
							}
					    });
	auxhtml = auxhtml + '</select>' +
			'</div>' + 
	   '</li>';
	$('#signlines').append(auxhtml);
	
	$('#signer_' + i).autocomplete({
		source: "redaction/autocompleteSigners",		
		// Función que se ejecuta al seleccionar un elemento
		select: function( event, ui ) {
			// Se fuerza a que el componente capture el valor
			// seleccionado por el usuario (teclado o ratón)
			$(this).val(ui.item.label);
			$(this).attr('codigo',ui.item.id);
			
			actualizarCodigosDestinarios();
			return false;
		}
	});

	// Si el usuario borra el contenido la línea de firma se elimina
	$('#signer_' + i).focusout(function() {
		var currentVal = $('#signer_' + i).val();
		if (i > 0) {
    		if (currentVal == null || currentVal == '') {
    			deleteSignLine(i);
    		}
		} else {
			if (currentVal == null || currentVal == '') {
				primeraLineaDeFirmaBorrada = true;
				var signers = $("#signers").val().split(",");
				var newSigners = signers.slice(1, signers.length);
				$("#signers").val("");
				$("#signers").val(newSigners);
    		}
		}
	});
}

function appendSignLines() {
	var signers = getSigners();	
	var signlinesType = $("#signlinesType").val().split(",");
	var signlinesAccion = $("#signlinesAccion").val().split(",");

	for (var i = 0; i < signers.length; i++){
		if (i == 0) {
			var data = signers[i];
			$('#signer_0').val(signers[i].label);
			$('#signer_0').attr('codigo',signers[i].id);
			$('#signline_type_0').val(signlinesType[i]);
			$('#actionSigner_0').val(signlinesAccion[i]);
		} else {
			var auxhtml = "";
			auxhtml = auxhtml +'<li>';
			if (signers.length == signlinesType.length) {
				auxhtml = auxhtml +'<select class="left signline" type="text" id="signline_type_' + i +'" name="signline_type_' + i + '" value="' + signlinesType[i] + '" maxlength="15">' ;
			} else {
				auxhtml = auxhtml +'<select class="left signline" type="text" id="signline_type_' + i +'" name="signline_type_' + i + '" value="" maxlength="15">' ;
			}
			auxhtml = auxhtml +'<option selected = "selected">Firma</option>' +
				   	'<option>Visto bueno</option>' +
				'</select>' +
				'<div class="overflowh" style="position: relative;">' +
					'<input id="signer_' + i + '" class="ui-autocomplete-input signer_input" style="width: 58%;" type="text" value="' + signers[i].label + '" name="signer_' + i + '" autocomplete="off" role="textbox" aria-autocomplete="list" aria-haspopup="true" codigo="' + signers[i].id + '">' +
					'<a href="javascript:void(0);" onclick="deleteSignLine(' + i + ');" style="padding: 5px 0; position: absolute; right: 0px;"><span class="mf-icon mf-icon-delete-16" th:text="#{selectUsers}"></span></a>'; 
			if (signers.length == signlinesAccion.length) {
				auxhtml = auxhtml +'<select id="actionSigner_' + i + '" name="actionSigner_' + i + '" class="actionSigner" value="' + signlinesAccion[i] + '" >' ;
			} else {
				auxhtml = auxhtml +'<select id="actionSigner_' + i + '" name="actionSigner_' + i + '" class="actionSigner" >' ;
			}
			$.each(listaAccionesFirmante, function (j, item) {
				if (item.id==1){
					auxhtml = auxhtml +'<option value="'+item.id+'" selected="selected">'+item.name+'</option>';
				} else {
					auxhtml = auxhtml +'<option value="'+item.id+'">'+item.name+'</option>';				}
		    });
			auxhtml = auxhtml +'</select></div></li>';
			$('#signlines').append(auxhtml);
			
			if (signers.length == signlinesType.length) {
				$('#signline_type_' + i).val(signlinesType[i]);
			}
			if (signers.length == signlinesAccion.length) {
				$('#actionSigner_' + i).val(signlinesAccion[i]);
			}
		}
	};
}

function buildQuery(obj) {
    var Result= '';
    if(typeof(obj)== 'object') {
        jQuery.each(obj, function(key, value) {
            Result+= (Result) ? '&' : '';
            if(typeof(value)== 'object' && value.length) {
                for(var i=0; i<value.length; i++) {
                    Result+= [key+'[]', encodeURIComponent(value[i])].join('=');
                }
            } else {
                Result+= [key, encodeURIComponent(value)].join('=');
            }
        });
    }
    return Result;
}

function actualizarCodigosDestinarios() {
	var selected = "";
	var signers = $('#signlines').find('.signer_input');
	signers.each(function (i, item) {
		var value =  $(item).attr('codigo');
		if (i == 0) {
			selected = value;
		} else {
			selected = selected + "," + value;
		}
	});

	// Se actualizan los firmantes seleccionados
	$('#signers').val('');
	$('#signers').val(selected);
}

/**
 * Método que carga los nombres de los firmantes
 * @returns Lista de tipos de documento
 */
function getSigners() {
	var result = "";
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache: false});
	$.ajax({url: 'redaction/getSigners',
		dataType: 'json',
		data: "signers=" + $("#signers").val(),
		async: false,
		success:
			function(model) {
			if (model.status == "error") {
				var messageError = model.log;
				$('#error').html(messageError);
				$('#error').show();
				jsAjaxStatus.stopAjax();
			} else {				
				if (model != null) {
     				result = model;
     			}
			}
		},
		error:
			function error(jqXHR, textStatus, errorThrown) {
			$('#error').html('Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
			$('#error').show();
			jsAjaxStatus.stopAjax();
		}
	});
	jsAjaxStatus.stopAjax();
	return result;
}

function mostrarOcultarImporte() {
	if($('#checkAddImporteFacturaContrato').is(':checked')) {
		$('#importeEuros').show();
		$('#textoImporteEuros').show();
	} else {
		$('#importeEuros').hide();
		$('#textoImporteEuros').hide();
		$('#importeEuros').val('');
	}
}

function soloNumeros(event) {
	var key = event.keyCode;
   	return (key >= 48 && key <= 57);
	
}



$(document).ready(function() {

	$("#uploadedDocsError").attr("style", "display: none");
	
	listaAccionesFirmante = cargarListaAccionesFirmante();
	
	appendSignLines();

	// Se crea el editor WYSIWYG
	$('.test-editor').jqte({"center": false, "color": false,
							"fsize": false, "format": false,
							"indent": false, "link": false,
							"left": false, "ol": false,
							"outdent": false, "p": false,
							"remove": false, "right": false,
							"rule": false, "sub": false,
							"strike": false, "sup": false,
							"title": false, "ul": false,
							"unlink": false, "source": false});

	// Se crean los botones para subir documentos y anexos si estamos en la página de redacción
	var href = $(location).attr('href');
	if (href.indexOf('redaction') != -1) {
		createFileUploadValidado('fileUploadButton', 'redaction/uploadFile',
						 '<span></span>', mostrarDocumentos, errorDocumentos);
	
		createFileUpload('annexUploadButton', 'redaction/uploadAnnex',
				 		 '<span></span>', mostrarAnexos, errorAnexos);
		
		
	}
	
	// Se crea el diálogo de la ventana modal de usuarios
	$("#signers_modal").dialog({
        autoOpen: false,
        resizable: false,
        //height:450,
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
                	// Recupero la configuración de las líneas de firma actuales option:selected
               		var signLinesConfig = $('.signline option:selected');
               		var signlines = "";
               		var signer_0 = $("#signer_0").val();
               		signLinesConfig.each(function(i, item) {
               			var value = $(item).val();
               			if (signer_0 == '') {
               				if (i > 0) {
               					if (i == 1) {
               						signlines = value;
               					} else {
               						signlines = signlines + "," + value;
               					}
               				} else {
               					signlines = value;
               				}
               			} else {
               				if (i == 0) {
               					signlines = value;
               				} else {
               					signlines = signlines + "," + value;
               				}
               			}
               		});
               		
               	// Recupero las acciones de las líneas de firma actuales option:selected
               		var signLinesAcciones = $('.actionSigner option:selected');
               		var signlinesAcc = "";
               		signLinesAcciones.each(function(i, item) {
               			var value = $(item).val();
               			if (signer_0 == '') {
               				if (i > 0) {
               					if (i == 1) {
               						signlinesAcc = value;
               					} else {
               						signlinesAcc = signlinesAcc + "," + value;
               					}
               				} else {
               					signlinesAcc = value;
               				}
               			} else {
               				if (i == 0) {
               					signlinesAcc = value;
               				} else {
               					signlinesAcc = signlinesAcc + "," + value;
               				}
               			}
               		});
               		
                	
                	var signLinesConfigSplit = signlines.split(",");
                	var signLinesAccionesSplit = signlinesAcc.split(",");
               		var nSignLines = $(signLinesConfig).length;
                	if (primeraLineaDeFirmaBorrada) {
                		signLinesConfig = signLinesConfig.slice(1, nSignLines);
                		signlinesAcc = signlinesAcc.slice(1, nSignLines);
                		nSignLines--;
                		primeraLineaDeFirmaBorrada = false;
                	}

                	// Se reinician las líneas de firma actuales
                	var selected = '';
                	if(nSignLines!=1){
                		$('#signlines').html('');
                	}

                	var users = $(".pickList_targetList li");

                	if (users.length == 0 && nSignLines!=1) {
                		var auxhtml = "";
                		auxhtml = auxhtml + '<li>' +
											   		'<select class="left signline" type="text" id="signline_type_0" name="signline_type_0" value="" maxlength="15">' +
														'<option selected = "selected">Firma</option>' +
														'<option>Visto bueno</option>' +
													'</select>' +
											   		'<div>' +
											   			'<a href="javascript:void(0)" onclick="$(\'#signers_modal\').dialog(\'open\');">' +
											   				'<span class="mf-icon-24 mf-icon-user-16" th:text="#{selectUsers}"></span>' +
														'</a>' +
														'<input id="signer_0" class="ui-autocomplete-input signer_input" style="width: 58%;" type="text" value="" name="signer_0" autocomplete="off" role="textbox" aria-autocomplete="list" aria-haspopup="true" codigo="">';
                		auxhtml = auxhtml +             '<select id="actionSigner_' + i + '" name="actionSigner_' + i + '" class="actionSigner" >' ;
											        			$.each(listaAccionesFirmante, function (j, item) {
											        				if (item.id==1){
											        					auxhtml = auxhtml +'<option value="'+item.id+'" selected="selected">'+item.name+'</option>';
											        				} else {
											        					auxhtml = auxhtml +'<option value="'+item.id+'" >'+item.name+'</option>';
											        				}
											        		    });
	        			auxhtml = auxhtml +             '</select>';
				
	        			auxhtml = auxhtml +			'</div>' +
											   '</li>';
        			$('#signlines').append(auxhtml);
                	} else {
                		users.each(function (i, item) {
                			var code = $(item).attr("data-value");
                			var value = $(item).html();

                			// Se obtiene la configuración de firma
                			var signLineChosen = "";
                			if (nSignLines > i) {
                				if (signLinesConfigSplit[i] == "Firma") {
                					signLineChosen = '<option selected = "selected">Firma</option><option>Visto bueno</option>';
                				} else {
                					signLineChosen = '<option>Firma</option><option selected = "selected">Visto bueno</option>';
                				}
                			} else {
                				signLineChosen = '<option selected = "selected">Firma</option><option>Visto bueno</option>';
                			}
                			
                			if (i == 0) {
	                			selected = code;
	                			$('#signlines').html('');
	                			var auxhtml = "";
	                			auxhtml = auxhtml +'<li>' +
													   		'<select class="left signline" type="text" id="signline_type_' + i +'" name="signline_type_' + i + '" value="" maxlength="15">' +
													   			signLineChosen +
															'</select>' +
													   		'<div class="overflowh">' +
																'<input id="signer_' + i + '" class="ui-autocomplete-input signer_input" style="width: 58%;" type="text" value="" name="signer_' + i + '" autocomplete="off" role="textbox" aria-autocomplete="list" aria-haspopup="true" codigo="">';
                				auxhtml = auxhtml +				'<select id="actionSigner_' + i + '" name="actionSigner_' + i + '" class="actionSigner" >' ;
											                			$.each(listaAccionesFirmante, function (j, item) {
													        				if (signLinesAccionesSplit[i] == item.id){
													        					auxhtml = auxhtml +'<option value="'+item.id+'" selected="selected">'+item.name+'</option>';
													        			        
													        				} else {
													        					auxhtml = auxhtml +'<option value="'+item.id+'" >'+item.name+'</option>';
													        				}
													        		    });
	                			auxhtml = auxhtml +              '</select>';
	                			auxhtml = auxhtml +		    '</div>' +
													   '</li>';
	                			$('#signlines').append(auxhtml);
	                		} else {
	                			selected = selected + "," + code;
	                			var auxhtml = "";
	                			auxhtml = auxhtml +'<li>' +
													   		'<select class="left signline" type="text" id="signline_type_' + i +'" name="signline_type_' + i + '" value="" maxlength="15">' +
													   			signLineChosen +
															'</select>' +
													   		'<div class="overflowh" style="position: relative;">' +
																'<input id="signer_' + i + '" class="ui-autocomplete-input signer_input" style="width: 58%;" type="text" th:field="*{signer}" value="" name="signer_' + i + '" autocomplete="off" role="textbox" aria-autocomplete="list" aria-haspopup="true" codigo="">' +
																'<a href="javascript:void(0);" onclick="deleteSignLine(' + i + ');" style="padding: 5px 0; position: absolute; right: 0px;"><span class="mf-icon mf-icon-delete-16" th:text="#{selectUsers}"></span></a>';
	                			auxhtml = auxhtml +				'<select id="actionSigner_' + i + '" name="actionSigner_' + i + '" class="actionSigner" >' ;
											                			$.each(listaAccionesFirmante, function (j, item) {
													        				if (signLinesAccionesSplit[i] == item.id){
													        					auxhtml = auxhtml +'<option value="'+item.id+'" selected="selected">'+item.name+'</option>';
													        				} else {
													        					auxhtml = auxhtml +'<option value="'+item.id+'">'+item.name+'</option>';
													        				}
													        		    });
	                			auxhtml = auxhtml +             '</select>';
                				auxhtml = auxhtml +		'</div>' + 
													   '</li>';
								$('#signlines').append(auxhtml);
	                		}
	
	                		$('#signer_' + i).val(value);
	                		$('#signer_' + i).attr('codigo', code);
	
	                		// Se añade la lista de usuarios para autocompletar
	                    	$('#signer_' + i).autocomplete({
		                    		source: "redaction/autocompleteSigners",		                    		
		                    		// Función que se ejecuta al seleccionar un elemento
		                    		select: function( event, ui ) {
		                    			// Se fuerza a que el componente capture el valor
		                    			// seleccionado por el usuario (teclado o ratón)
		                    			$(this).val(ui.item.label);
		                    			$(this).attr('codigo',ui.item.id);
		                    			
		                    			actualizarCodigosDestinarios();
		                    			return false;
		                    		}
	                    	});

	                    	// Si el usuario borra el contenido la línea de firma se elimina
	                    	$('#signer_' + i).focusout(function() {
	                    		var currentVal = $('#signer_' + i).val();
	                    		if (i > 0) {
		                    		if (currentVal == null || currentVal == '') {
		                    			deleteSignLine(i);
		                    		}
	                    		} else {
	                    			if (currentVal == null || currentVal == '') {
	                    				primeraLineaDeFirmaBorrada = true;
	                    				var signers = $("#signers").val().split(",");
	                    				var newSigners = signers.slice(1, signers.length);
	                    				$("#signers").val("");
	                    				$("#signers").val(newSigners);
		                    		}
	                    		}
	                    	});
	                    });
                	}

                	// Se actualizan los firmantes
                	$('#signers').val('');
                	$('#signers').val(selected);

                	// Se borra el mensaje de error de los firmantes si existe
                	var errorMsg = $('#signersErrorMessage');
                	if (errorMsg != null) {
                		errorMsg.remove();
                	}

                	$(this).dialog("close");
                }
            }           
        ],   
        //Al crearse el DOM modificamos la apariencia de los botones con nuestros propios estilos
        create:function () {               
            $(this).closest(".ui-dialog").find(".btn_dlg_primary").removeClass().addClass("simbutton primary");
            $(this).closest(".ui-dialog").find(".btn_dlg_secondary").removeClass().addClass("simbutton secondary");
        },
        open: function(){
        	//Cargar combos, listados
        },
        close:function () {
           //Limpiar campos del formulario del dialog, etc
        }
    });

	// Se permite autocompletar el destinatario inicial
	$('#signer_0').autocomplete({
		source: "redaction/autocompleteSigners",
		// Función que se ejecuta al seleccionar un elemento
		select: function( event, ui ) {
			// Se fuerza a que el componente capture el valor
			// seleccionado por el usuario (teclado o ratón)
			$(this).val(ui.item.label);
			$(this).attr('codigo',ui.item.id);
			
			actualizarCodigosDestinarios();
			return false;
		}
	});

	// Si el usuario borra el contenido la primera línea de firma se elimina
	$('#signer_0').focusout(function() {
		var currentVal = $(this).val();
		if (currentVal == null || currentVal == '') {
			primeraLineaDeFirmaBorrada = true;
			var signers = $("#signers").val().split(",");
			var newSigners = signers.slice(1, signers.length);
			$("#signers").val("");
			$("#signers").val(newSigners);
			$('#signer_0').attr('codigo', "");
		}
	});

	// Botón para mostrar más opciones 
	$('#more-options-btn').toggle( function(e) {
		 $(this).parent().children('.more-options-panel').slideDown("slow");
		 $(this).children("span").addClass('mf-icon-minus-16');
		 e.preventDefault();	     
		}, function () {
			$(this).parent().children('.more-options-panel').slideUp("slow");
			$(this).children("span").removeClass('mf-icon-minus-16');
	});


	// Se muestran los documentos adjuntos
	var hasErrors = $('#thereAreErrors').val();
	var uploadedDocs = $('#documentString').val();
	if (hasErrors == "true" && uploadedDocs != "") {
		var docs = uploadedDocs.split(",");
		for (var i=0; i<docs.length; i++) {
			var doc = docs[i];
			mostrarDocumentos(i,doc);
		};
	}

	// Se muestran los documentos anexos
	var uploadedAnnexes = $('#annexString').val();
	if (hasErrors == "true" && uploadedDocs != "") {
		var annexes = uploadedAnnexes.split(",");
		for (var i=0; i<annexes.length; i++) {
			var annex = annexes[i];
			mostrarAnexos(i,annex);
		};
	}

	// Asociar el icono de espera al evento submit del formulario
	$('#redactionForm').bind('submit', function(event) {
		jsAjaxStatus.startAjax();
    });

	// Se captura el evento de teclado
	$(window).keydown(function(event) {
		// Si se pulsa "enter" y no está enfocados los elementos..
	    if(event.keyCode == 13 
	    		&& !$('#more-sign-line').is(":focus")
	    		&& !$('.jqte_editor').is(":focus")
	    		&& !$('#more-options-btn').is(":focus")) {
	    	//.. se desactiva la acción del evento.
			event.preventDefault();
			return false;
	    }
	});
});