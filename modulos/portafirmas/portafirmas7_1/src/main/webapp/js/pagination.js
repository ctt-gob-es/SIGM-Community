function goFirst(id) {
	var first = $("#paginatorCurrentPage_" + id +  " option:first").val();
	$("#paginatorCurrentPage_" + id).val(first);
}

function goPrevious(id, methodJs) {
	var previous = $("#paginatorCurrentPage_" + id + " > option:selected").prev('option').val();
	$("#paginatorCurrentPage_" + id).val(previous);
}

function goNext(id, methodJs) {
	var next = $("#paginatorCurrentPage_" + id + " > option:selected").next('option').val();
	$("#paginatorCurrentPage_" + id).val(next);
}

function goLast(id, methodJs) {
	var last = $("#paginatorCurrentPage_" + id + " option:last").val();
	$("#paginatorCurrentPage_" + id).val(last);
}