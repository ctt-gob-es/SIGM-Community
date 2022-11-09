function appendShowAjaxContent(url,data,title){
	$('#AjaxShow').dialog('option', 'title', title);
	var jqxhr_a = $.post(url, data, title).complete(function(){
		$('#AjaxShow').html(jqxhr_a.responseText);
		$('#AjaxShow').dialog('open');
	});

	return jqxhr_a;
}

$(document).ready(function() {
	// Detect js
	$('html').removeClass('no-js');
	
	$('.toggle').click(function() {
		$(this).next().toggle('fast');
		$(this).children("span.ui-icon-triangle-1-e").switchClass("ui-icon-triangle-1-e", "ui-icon-triangle-1-s", 100 );
		$(this).children("span.ui-icon-triangle-1-s").switchClass("ui-icon-triangle-1-s", "ui-icon-triangle-1-e", 100 );
		return false;
	}).next().hide();
});