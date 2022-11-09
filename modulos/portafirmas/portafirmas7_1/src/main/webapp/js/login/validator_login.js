function validatorLogin(userPk) {
	jsAjaxStatus.startAjax();
	$('#userPk').val(userPk);
	$('#userLoginForm').submit();
	jsAjaxStatus.stopAjax();
}

function gestorLogin(gestorPk) {
	jsAjaxStatus.startAjax();
	$('#gestorPk').val(gestorPk);
	$('#gestorLoginForm').submit();
	jsAjaxStatus.stopAjax();
}

function groupLogin(groupPk) {
	jsAjaxStatus.startAjax();
	$('#groupPk').val(groupPk);
	$('#groupLoginForm').submit();
	jsAjaxStatus.stopAjax();
}