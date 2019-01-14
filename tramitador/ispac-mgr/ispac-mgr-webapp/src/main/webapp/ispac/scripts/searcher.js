// En real tiene que buscar solo dentro de #blockfasesTree

jQ1_12_2('#searcher').quicksearch('#blockfasesTree .menu11bold', {
    'delay': 100,
    'bind': 'keyup keydown',
    'show': function() {
			jQ1_12_2(this).removeClass('oculto');
			jQ1_12_2(this).next('.fases').removeClass('oculto');
			
			// Al li anterior al padre (el lu) le quitamos el hide también ,
			// lo hacemos también en dos niveles
			jQ1_12_2(this).parent().prev('li').removeClass('oculto');
			jQ1_12_2(this).parent().parent().prev('li').removeClass('oculto');
			
			// Puede que el li superior no esté directamente en la posición anterior al padre,
			// en ese caso: (Haciéndolo así no se mostrarían las fases de los padres,
			// da igual porque en realidad no se está buscando al padre):
			jQ1_12_2(this).parent().prevUntil('li').prev().removeClass('oculto');
			
			// No he visto ningún caso de que el abuelo tenga fases colgando,
			// si lo hubiera probar con:
			// jQ1_12_2(this).parent().parent().prevUntil('li').prev().removeClass('oculto');
    },
    'onAfter': function() {
        if (jQ1_12_2('#searcher').val() === '') {
			return;
        }
		// Descomentar par que haga scroll
        // if (jQ1_12_2('.show:first').length > 0) {
        	// jQ1_12_2('html,body').scrollTop($('.show:first').offset().top);
        // }
    },
    'hide': function() {
        jQ1_12_2(this).addClass('oculto');		
		jQ1_12_2(this).next('.fases').addClass('oculto');
    },
    'prepareQuery': function(val) {
        return new RegExp(val, "i");
    },
    'testQuery': function(query, txt, _row) {
        return query.test(txt);
    }
});

