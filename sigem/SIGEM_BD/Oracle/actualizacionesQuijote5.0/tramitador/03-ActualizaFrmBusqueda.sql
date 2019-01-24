update spac_ct_frmbusqueda set frm_bsq = replace(frm_bsq, 
'AND TIPO = 1',
'AND TIPO = 1
	AND (
		(
			USER_UID_PARAM in (select uid_usr from SPAC_SS_FUNCIONES where uid_usr = USER_UID_PARAM and funcion in (4, 5))
		)
		or (
			id in
			(select sp.id_obj from spac_permisos sp where sp.tp_obj = 1 and
				(sp.id_resp = USER_UID_PARAM or
				sp.id_resp = USER_DEPT_UID_PARAM or
				sp.id_resp in (USER_GROUPS_UID_PARAM)
				)
			)
		)
 	)'
) where descripcion = 'BÚSQUEDA';