$(function() {
    $( "#accordion" ).accordion({
      collapsible: false
    });
  });

function showResults(xhr, status, args) {
	 if(args.isErrors || args.validationFailed) {  
		 $( '#accordion' ).accordion({active: 0});
	 }  else $( '#accordion' ).accordion({active: 1});
    	
  
}