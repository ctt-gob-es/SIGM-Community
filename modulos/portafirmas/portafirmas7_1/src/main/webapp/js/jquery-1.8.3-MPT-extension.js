(function( window, undefined ) {
    var rCRLF = /\r?\n/g,
    rinput = /^(?:color|date|datetime|datetime-local|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i,
    rselectTextarea = /^(?:select|textarea)/i;

    jQuery.fn.extend({
        serializeAndEncode : function() {
            return jQuery.param( this.serializeAndEncodeArray());
        },
        serializeAndEncodeArray : function() {
            return this.map(function(){
                return this.elements ? jQuery.makeArray( this.elements ) : this;
            })
            .filter(function(){
                return this.name && !this.disabled &&
                ( this.checked || rselectTextarea.test(this.nodeName) || rinput.test(this.type) );
            })
            .map(function( i, elem ){
                var val = jQuery( this ).val();

                return val == null ?
                    null :
                    jQuery.isArray( val ) ?
                        jQuery.map( val, function( val, i ){
                            return { name: elem.name, value: encodeURIComponent(val.replace( rCRLF, "\r\n" )) };
                        }) :
                        { name: elem.name, value: encodeURIComponent(val.replace( rCRLF, "\r\n" )) };
            }).get();
        },
        encodeValue: function(){
        	var val = jQuery( this ).val();
        	return encodeURIComponent(val.replace( rCRLF, "\r\n" ));
        }        
    });
})( window );
