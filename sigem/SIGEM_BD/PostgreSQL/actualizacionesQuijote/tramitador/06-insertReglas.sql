insert into spac_ct_reglas values (NEXTVAL('SPAC_SQ_ID_CTREGLAS'),'AvisoNuevoTramiteRule','Cuando un trámite pasa a otro servicio, se avisa a los miembros de este que se les ha mandado ese nuevo trámite desde el servicio propietario.','es.dipucr.sigem.api.rule.common.avisos.AvisoNuevoTramiteRule',1);
insert into spac_ct_reglas values (NEXTVAL('SPAC_SQ_ID_CTREGLAS'),'EliminarAvisosNuevoTramiteRule','Cuando se termina un trámite, se borran los avisos que se hayan podido generar avisando al nueuvo propietario del trámite.','es.dipucr.sigem.api.rule.common.avisos.EliminarAvisosNuevoTramiteRule',1);
insert into spac_ct_reglas values (NEXTVAL('SPAC_SQ_ID_CTREGLAS'),'AvisoFinTramiteRule','','es.dipucr.sigem.api.rule.common.avisos.AvisoFinTramiteRule',1);
insert into spac_ct_reglas values (NEXTVAL('SPAC_SQ_ID_CTREGLAS'),'EliminarAvisosFinTramiteRule','','es.dipucr.sigem.api.rule.common.avisos.EliminarAvisosFinTramiteRule',1);

insert into spac_ct_reglas values (NEXTVAL('SPAC_SQ_ID_CTREGLAS'),'AvisoFinFirmaRule','Avisa del fin del circuito de firma al creador del documento en cuestión. La notificación de hace mediante aviso electrónico en la pantalla inicial de Sigem.','es.dipucr.sigem.api.rule.common.avisos.AvisoFinFirmaRule',1);
insert into spac_ct_reglas values (NEXTVAL('SPAC_SQ_ID_CTREGLAS'),'EliminarAvisosFirmaRule','Regla para eliminar los avisos relativos a la firma de documentos de un expediente. Se ejecutará al final de los trámites de Creación del Decreto y Firmas y traslados','es.dipucr.sigem.api.rule.common.avisos.EliminarAvisosFirmaRule',1);
insert into spac_ct_reglas values (NEXTVAL('SPAC_SQ_ID_CTREGLAS'),'DipucrAvisoFirmanteRule','Regla para el aviso a un firmante de circuito de firmas en el momento que le llegue el turno','es.dipucr.sigem.api.rule.common.avisos.DipucrAvisoFirmanteRule',1);

insert into spac_ct_reglas values (NEXTVAL('SPAC_SQ_ID_CTREGLAS'),'TrasladarDocumentosRule','','es.dipucr.sigem.api.rule.common.TrasladarDocumentosRule',1);

insert into spac_ct_reglas values (NEXTVAL('SPAC_SQ_ID_CTREGLAS'),'DipucrAutogeneraDocumentoEspecificoInitTramite','','es.dipucr.sigem.api.rule.common.documento.DipucrAutogeneraDocumentoEspecificoInitTramite',1);
insert into spac_ct_reglas values (NEXTVAL('SPAC_SQ_ID_CTREGLAS'),'EliminarTodosAvisosExpedienteRule','','es.dipucr.sigem.api.rule.common.avisos.EliminarTodosAvisosExpedienteRule',1);
