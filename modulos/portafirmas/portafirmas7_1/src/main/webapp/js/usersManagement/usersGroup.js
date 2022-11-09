
//Carga los usuarios de un grupo
function prepareUsersGroup(id) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache : false});
	$.ajax({
		url : "usersManagement/userGroup/load",
		type : 'post',
		dataType : 'html',
		data : "id=" + id,
		async : false,
		success : 
			function(model) {
				setupUserGroup(model);
				$('#groupUsersModal').dialog('open');
			},
		error : function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
//			jsAjaxStatus.stopAjax();
		}
	});
	jsAjaxStatus.stopAjax();
}

//Guarda el usuario de grupo correspondiente
function saveUserGroup(userId) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache : false});
	$.ajax({
		url : "usersManagement/userGroup/save",
		type : 'post',
		dataType : 'html',
		data : "userId=" + userId + "&groupId=" + $('#groupPk').val(),
		async : false,
		success : 
			function(model) {
				setupUserGroup(model);
			},
		error : function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
//			jsAjaxStatus.stopAjax();
		}
	});
	jsAjaxStatus.stopAjax();
}

//Borra el usuario de grupo correspondiente
function deleteUserGroup(userGroupId) {
	jsAjaxStatus.startAjax();
	$.ajaxSetup({cache : false});
	$.ajax({
		url : "usersManagement/userGroup/delete",
		type : 'post',
		dataType : 'html',
		data : "userGroupId=" + userGroupId,
		async : false,
		success : 
			function(model) {
				setupUserGroup(model);
			},
		error : function error(jqXHR, textStatus, errorThrown) {
			showError(genericMessage);
//			jsAjaxStatus.stopAjax();
		}
	});
	jsAjaxStatus.stopAjax();
}

/*
 * Para configurar los componentes JQuery de la ventana modal.
 * Al cargar o recargar la ventana modal hay que volver a configurar
 */
function setupUserGroup(model) {
	$('#groupUsersModal').html(model);
	// Autocompletado para usuarios de grupo
	$('#newUserGroupName').autocomplete({
		minLength: 3,
		source: 'usersManagement/userGroup/autocomplete?groupId=' + $('#groupPk').val(),
		select: function( event, ui ) {
			$('#newUserGroupName').val(ui.item.label);
			saveUserGroup(ui.item.id);
			return false;
		}
	});
	$('#newUserGroupName').focus();
	jsAjaxStatus.stopAjax();
}