function selectCurrentRow_paginator(table,index){
	table.unselectAllRows();
	table.selectRow(index-(table.paginator.cfg.page*table.paginator.cfg.rows) ,false);
}

function selectCurrentRow(table,index){
	table.unselectAllRows();
	table.selectRow(index ,false);
}

function dblclickCurrentRow (table,index){
	table.unselectAllRows();
	table.selectRow(index ,false);
	if ($('#form\\:editButton') != null){
		$('#form\\:editButton').click();
	} else {
		if ($('#form\\:verButton') != null){
			$('#form\\:verButton').click();
		}
	}
	
}