

//Para cargar los comentarios una única vez
function loadComments() {

	if (!isLoadComments) {
		reloadComments('get');
		isLoadComments = true;
	}

}

// Para recargar los comentarios
function reloadComments(type) {
	// hay cambios en los servidores y queremos que se reflejen
	// la próxima vez que se visite la pestaña de configuraciones
	jsAjaxStatus.startAjax();
	$.ajaxSetup({
		cache : false
	});
	$
			.ajax({
				url : "inbox/comment",
				type : type,
				dataType : 'html',
				data : "currentRequest=" + $('#currentRequestHash').val(),
				async : false,
				success : function(model) {
					$('#commentModal').dialog("close");
					$("#tab3").html(model);
					jsAjaxStatus.stopAjax();
				},
				error : function error(jqXHR, textStatus, errorThrown) {
					$('#error')
							.html(
									'Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador');
					$('#error').show();
					$('#commentModal').dialog("close");
					jsAjaxStatus.stopAjax();
				}
			});

	$("#commentModal").dialog({
		autoOpen : false,
		resizable : false,
		width : 600,
		modal : true,
		buttons : [ {
			text : "Cancelar",
			"class" : 'secondary',
			click : function() {
				$(this).dialog("close");
			},
		}, {
			text : "Guardar",
			"class" : 'primary',
			click : function() {
				saveComment();
			}
		} ],
		create : customModalStyle,
		close : function() {
			$('#commentErrorMessage').html('');
		}
	});

};

function setupComment(comment) {

	$("#usersCommentList").html("");

	$('#commentText').val(comment.tcomment);
	$('#commentPrimaryKey').val(comment.primaryKey);

	var currentUser = $('#currentUser').val();
	var currentGroup = $('#currentGroup').val();

	var ul = document.createElement('ul');
	ul.setAttribute('id', 'userList');

	var numRow = 0;

	$.each(comment.signers, function(i, item) {

		var key_checkbox = document.createElement("input");
		var key_span = document.createElement("span");

		var li = document.createElement('li');

		key_checkbox.setAttribute("type", "checkbox");
		key_checkbox.setAttribute("name", "usercomment[" + numRow + "]");
		key_checkbox.setAttribute("id", "usercomment");
		key_checkbox.setAttribute("value", item.nif);
		key_checkbox.setAttribute("onclick", "cambiarUsuarioComentario(this)");

		if (item.nif == currentUser || item.group == currentGroup) {
			key_checkbox.setAttribute("checked", true);
			key_checkbox.setAttribute("disabled", "disabled");
		} else {
			if (comment.users.length > 0
					&& jQuery.inArray(item.nif, comment.users) == 0) {
				key_checkbox.setAttribute("checked", true);
			}
		}
		key_span.innerHTML = item.name;

		li.appendChild(key_checkbox);
		li.appendChild(key_span);

		ul.appendChild(li);

		numRow++;

	});

	$("#usersCommentList").append(ul);

	$('#commentModal').dialog('open');

}

function prepareModifyComment(primaryKey) {

	jsAjaxStatus.startAjax();
	$.ajaxSetup({
		cache : false
	});
	$.ajax({
		url : "inbox/comment/loadComment",
		dataType : 'html',
		data : 'currentRequest=' + $('#currentRequestHash').val()
				+ "&primaryKey=" + primaryKey,
		async : false,
		success : function(data) {
			var comment = JSON.parse(data);

			setupComment(comment);

			// $('#commentModal').dialog('open');
			jsAjaxStatus.stopAjax();
		},
		error : function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
			$('#commentModal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});

}


// Prepara la creación de comentarios
function prepareNewComment() {
	var primaryKey = null;
	jsAjaxStatus.startAjax();
	$.ajaxSetup({
		cache : false
	});
	$.ajax({
		url : "inbox/comment/loadComment",
		dataType : 'html',
		data : 'currentRequest=' + $('#currentRequestHash').val()
				+ '&primaryKey=' + primaryKey,
		async : false,
		success : function(data) {
			var comment = JSON.parse(data);

			setupComment(comment);

			// $('#commentModal').dialog('open');
			jsAjaxStatus.stopAjax();
		},
		error : function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
			$('#commentModal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

// Prepara el borrado de un servidor
function prepareDeleteComment(primaryKey) {
	$('#questionMessageComment').html(
			'Va a borrar un comentario, ¿está usted seguro?');
	$('#yesButtonComment').attr('onclick',
			"deleteComment('" + primaryKey + "');");
	$('#questionComment').show();
}

// Para borrar una sede
function deleteComment(primaryKey) {
	$('#questionComment').hide();

	jsAjaxStatus.startAjax();
	$.ajaxSetup({
		cache : false
	});
	$.ajax({
		url : "inbox/comment/deleteComment",
		type : 'post',
		dataType : 'json',
		data : "currentRequest=" + $('#currentRequestHash').val()
				+ "&primaryKey=" + primaryKey,
		async : true,
		success : function(data) {
			if (jQuery.isEmptyObject(data)) {
				reloadComments('post');
			} else {
				showError(getErrors(data));
			}
			jsAjaxStatus.stopAjax();
		},
		error : function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
			jsAjaxStatus.stopAjax();
		}
	});

}

// Para insertar o modificar un comentario
function saveComment() {

	var selectedUsers = $('input[id="usercomment"]:checked');

	var userComments = getCheckIds(selectedUsers);

	jsAjaxStatus.startAjax();
	$.ajaxSetup({
		cache : false
	});
	$.ajax({
		url : "inbox/comment/saveComment",
		type : 'post',
		dataType : 'json',
		data : "currentRequest=" + $('#currentRequestHash').val()
				+ "&primaryKey=" + $('#commentPrimaryKey').val()
				+ "&commentText=" + $('#commentText').val() + "&userComments="
				+ userComments,
		async : true,
		success : function(data) {
			if (jQuery.isEmptyObject(data)) {
				reloadComments('post');

				$('#commentModal').dialog("close");
			} else {
				$('#serverErrorMessage').html(getErrors(data));
			}
			jsAjaxStatus.stopAjax();
		},
		error : function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
			$('#commentModal').dialog("close");
			jsAjaxStatus.stopAjax();
		}
	});
}

function cambiarUsuarioComentario(obj) {

	if ($('#commentPrimaryKey').val() != null
			&& $('#commentPrimaryKey').val() != '') {
		var url = "";
		if (obj.checked) {
			url = "inbox/comment/insertUsersComment";
		} else {
			url = "inbox/comment/deleteUsersComment";
		}

		jsAjaxStatus.startAjax();
		$.ajaxSetup({
			cache : false
		});
		$
				.ajax({
					url : url,
					type : 'post',
					dataType : 'json',
					data : "&primaryKey=" + $('#commentPrimaryKey').val()
							+ "&userComment=" + $(obj).val(),
					async : false,
					success : function(model) {
						if (model.status != "success") {
							$('#serverErrorMessage').html(
									"Se ha produciendo un error");
						}
						jsAjaxStatus.stopAjax();
					},
					error : function error(jqXHR, textStatus, errorThrown) {
						$('#error').html(genericMessage);
						$('#error').show();
						result = "error";
						jsAjaxStatus.stopAjax();
					}
				});
	} else {
		jsAjaxStatus.stopAjax();
	}
}