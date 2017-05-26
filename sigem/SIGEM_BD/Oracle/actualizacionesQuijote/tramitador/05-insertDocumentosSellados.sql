insert into spac_ct_tpdoc (id, nombre, cod_tpdoc, autor, fecha) values (SPAC_SQ_ID_CTTPDOC.nextval,'Documentos Sellados', 'doc-sell','SYSSUPERUSER',SYSDATE);
insert into spac_p_plantdoc (id, id_tpdoc, fecha, nombre, mimetype, cod_plant) select SPAC_SQ_ID_PPLANTILLAS.nextval, id, fecha, nombre, 'application/msword', 'doc-sell' from spac_ct_tpdoc where nombre='Documentos Sellados';
