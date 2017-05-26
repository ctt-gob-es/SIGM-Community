-- e-TRAMITACION --
INSERT INTO sgmrtcatalogo_documentos (id, descripcion, extension, conector_firma, conector_contenido) VALUES ('ZIP', 'Fichero ZIP', 'ZIP', '', '');
INSERT INTO sgmrtcatalogo_documentos (id, descripcion, extension, conector_firma, conector_contenido) VALUES ('XSIG', 'Documento XSIG', 'XSIG', '', '');

insert into sgmrtcatalogo_tramites values ('SUBSANACION_JUST_REPRESENT','SUBS_JUST_REPRE','SUBSANACIÓN JUSTIFICACIÓN Y MODIFICACIÓN CON REPRESENTANTES','SUBJUSM','0','1','999','');

insert into sgmrtcatalogo_docstramite values ('SUBSANACION_JUST_REPRESENT','ZIP','CR_SUBJUSREPRE_D1','0');
insert into sgmrtcatalogo_docstramite values ('SUBSANACION_JUST_REPRESENT','DOC','CR_SUBJUSREPRE_D2','0');
insert into sgmrtcatalogo_docstramite values ('SUBSANACION_JUST_REPRESENT','PDF','CR_SUBJUSREPRE_D3','0');
insert into sgmrtcatalogo_docstramite values ('SUBSANACION_JUST_REPRESENT','JPEG','CR_SUBJUSREPRE_D4','0');

insert into sgmafconector_autenticacion values( 'SUBSANACION_JUST_REPRESENT','AUTH_CERTIFICADO');

insert into sgmrtcatalogo_organos values ('SUBJUSM','SUBJUSM');