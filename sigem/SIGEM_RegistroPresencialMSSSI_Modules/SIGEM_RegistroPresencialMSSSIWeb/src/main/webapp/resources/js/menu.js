$(function() {
    
    // set effect from select menu value
    $( "#buttonhide" ).click(function() {
      runEffect();
      return false;
    });
    // set effect from select menu value
    $( "#buttonshow" ).click(function() {
      runEffectshow();
      return false;
    });
    
    
  });

	//run the currently selected effect
	function runEffect() {
	  // get effect type from
	  var selectedEffect = "slide";
	
	  // most effect types need no options passed by default
	  var options = {};
	 
	  // run the effect
	  
	  $( "#menudiv" ).hide( null, options, 0, null );
	  $( "#apartadoOculto").show( null, options, 0, null );
	  $( "#contenidodiv" ).removeClass( "contenidopequenio",0 ,"easeOutBounce" );
	  $( "#contenidodiv" ).addClass( "contenidogrande", 0, "easeOutBounce" );
	};
	
    function runEffectshow() {
	      // get effect type from
	      var selectedEffect = "slide";
	 
	      // most effect types need no options passed by default
	      var options = {};
	      
	      $( "#contenidodiv" ).addClass( "contenidopequenio", 0, "easeOutBounce" );
	      $( "#contenidodiv" ).removeClass( "contenidogrande",0 ,"easeOutBounce" );
	      // run the effect
	  	  $( "#apartadoOculto").hide( null, options, 0, null );
	      $( "#menudiv" ).show( null, options, 0, null );
	      
	    };