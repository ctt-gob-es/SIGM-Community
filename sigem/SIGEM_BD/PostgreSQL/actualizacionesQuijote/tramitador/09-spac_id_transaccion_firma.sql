
CREATE TABLE spac_id_transaccion_firma(
  id integer NOT NULL,
  id_documento integer,
  id_transaccion varchar2(32),
  hash varchar2(128),
  datos_firmante varchar2(256),
  fecha timestamp,
  CONSTRAINT pk_id_transaccion_firma PRIMARY KEY (id)
);

CREATE INDEX spac_id_trans_firma_ix_iddoc ON spac_id_transaccion_firma (id_documento);

CREATE SEQUENCE spac_sq_id_transaccionfirma
  INCREMENT by 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START with 1;

  
--  **UPNA** La tabla SPAC_ID_TRANSACCION_FIRMA en principio debe tener el id=10 para que funcione correctamente el circuito de firma. 

-- insert into spac_ct_entidades (id, tipo, nombre, campo_pk, descripcion, sec_pk, fecha)
-- values (NEXTVAL('SPAC_SQ_ID_CTENTIDADES'),'0','SPAC_ID_TRANSACCION_FIRMA','ID','Almacena el id de transacción de la firma de documentos','SPAC_SQ_ID_TRANSACCIONFIRMA', NOW());

insert into spac_ct_entidades (id, tipo, nombre, campo_pk, descripcion, sec_pk, fecha)
values (10,'0','SPAC_ID_TRANSACCION_FIRMA','ID','Almacena el id de transacción de la firma de documentos','SPAC_SQ_ID_TRANSACCIONFIRMA', NOW());
