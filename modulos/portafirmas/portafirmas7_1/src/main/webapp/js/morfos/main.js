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

	var no_editable = new Array();

	$('.no_editable').click(function(){
		alert('campo no editable');
		return false;
	});

	$('.no_editable').keydown(function(){
		alert('El valor de este campo no se puede modificar');
		return false;
	});

	$('input.no_editable').focus(function(){
		no_editable[$(this).attr('id')] = $(this).val();
	});

	$('input.no_editable').change(function(){
		$(this).val(no_editable[$(this).attr('id')]);
	});

	$('a.ui-state-default').mouseover(function(){
		$(this).removeClass('ui-state-default');
		$(this).addClass('ui-state-hover');
	});
	$('a.ui-state-default').mouseout(function(){
		$(this).removeClass('ui-state-hover');
		$(this).addClass('ui-state-default');
	});

	$('table.data tbody tr:odd').addClass('odd');
	$('table.data tbody tr:even').addClass('even');

	// Accordion
	$(".accPanel").accordion({
		header: "h4",
		autoHeight: false
	});

	// Toggle menus
	$('.panel .toggle h4').click(function() {
		$(this).next().toggle('fast');
		$(this).children("span.ui-icon-triangle-1-e").switchClass("ui-icon-triangle-1-e", "ui-icon-triangle-1-s", 100 );
		$(this).children("span.ui-icon-triangle-1-s").switchClass("ui-icon-triangle-1-s", "ui-icon-triangle-1-e", 100 );
		return false;
	}).next().hide();

	// Datepicker
	$( ".datepicker" ).datepicker();

	// Tabs
	$( ".tabs" ).tabs();

	$(document).bind('ajaxSend', function() {
		$('#loading-ajax').show();
	}).bind('ajaxComplete', function() {
		$('#loading-ajax').hide();
	});

    $('div#AjaxShow').dialog({
        autoOpen: false,
        modal: true,
        width: 800,
        maxHeight: 600,
        position: ['center', 100],
        resizable: false,
        zindex: 10000
    });

    $(document).on('click','a.appendAjaxContent',function(){
        appendShowAjaxContent($(this).attr('href'),{},$(this).attr('title'));
        if($(this).attr('id')=='userLn'){
            $('div#AjaxShow').parent().addClass('userLnModal');
            $("div#AjaxShow").siblings('div.ui-dialog-titlebar').remove();
            $('div#AjaxShow').dialog({
                position: 'top',
                width: 960,
                open: function(event, ui) { $(".ui-dialog-titlebar-close").hide();}
            });

        }
        return false;
    });

    $(document).on("click",'.ui-widget-overlay', function() {
        $("div#AjaxShow").dialog("close");
    });

        $(document).on('click','input[type=submit].appendAjaxContent',function(){
                var data = $(this).parents('form:first').serialize();
                data += (data!='')?'&':'';
                if($(this).attr('name')){
                  data += 'modal=1&'+$(this).attr('name')+'=1';
                }else{
                  data += 'modal=1';
                }
		appendShowAjaxContent($(this).parents('form:first').attr('action'),data,$('#ui-dialog-title-AjaxShow').html());
		return false;
	});

        $(document).on('click','a.clearForm',function(){
		$(this).parents('form').each(function(){
			$(this).find('input[type=text],input[type=search]').each(function(){
				$(this).val('');
			});
		});
		return false;
	});

	// Sliding filter
	$(document).on('click','.slidingFilter > a',function() {
		$('form.filterForm').slideToggle('slow');
	});

	// Button w/multiple sec. actions
	$(document).on('click','.multisimbutton .multiDropdown',function() {
		$(this).focus();
		$(this).toggleClass( 'selected', 0);
		$(this).parent().next('ul').slideToggle('fast');
		$(this).parent().next('ul').position({
			of: $(this).parent(),
			my: 'right top',
			at: 'right bottom',
			offset: '0 1'
		});

		if($(this).children('span').hasClass('mf-icon')){
			$(this).children('span.mf-icon-down-16').switchClass('mf-icon-down-16', 'mf-icon-up-16', 10);
			$(this).children('span.mf-icon-up-16').switchClass('mf-icon-up-16', 'mf-icon-down-16', 10);
		};

		return false;
	});
    $(document).on('mousedown','.multisimbutton .dropdownList a',function() {
        divclick = true;
        return false;
    });
    var divclick = false;
	$(document).on('blur','.multisimbutton .multiDropdown',function() {
        if ( divclick ) return( divclick = false );
		if($(this).hasClass( 'selected')) {
			$(this).toggleClass( 'selected', 0);
			$(this).parent().parent().children('ul').slideToggle('fast');
			$(this).children('span.mf-icon-up-16').switchClass('mf-icon-up-16', 'mf-icon-down-16', 10);
		};
	});

	//	Dropdown menus

	$(document).on('click','.dropdownMenu .dropdown',function() {
		$(this).focus();
		$(this).toggleClass( 'selected', 0);
		$(this).parent().next('ul').slideToggle('fast');
		$(this).parent().next('ul').position({
			of: $(this).parent(),
			my: 'right top',
			at: 'right bottom',
			offset: '0 1'
		});

		if($(this).children('span').hasClass('mf-icon')){
			$(this).children('span.mf-icon-down-16').switchClass('mf-icon-down-16', 'mf-icon-up-16', 10);
			$(this).children('span.mf-icon-up-16').switchClass('mf-icon-up-16', 'mf-icon-down-16', 10);
		};
		return false;
	});
	$(document).on('mousedown','.dropdownMenu .dropdownList a',function() {
            divclick = true;
            return false;
        });
	$(document).on('blur','.dropdownMenu .dropdown',function() {
            if ( divclick ) return( divclick = false );
		if($(this).hasClass('selected')) {
			$(this).toggleClass( 'selected', 0);
			$(this).parent().next('ul').slideToggle('fast');
			$(this).children('span.mf-icon-up-16').switchClass('mf-icon-up-16', 'mf-icon-down-16', 10);
		};
	});
});