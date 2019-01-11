--DROP TABLE ORVE_HISTO_REGISTRO;

CREATE TABLE ORVE_HISTO_REGISTRO(
	id integer NOT NULL,
	identificador_orve int,
	nreg_sigem varchar(20),
	fecha_registro timestamp without time zone,
	  
	CONSTRAINT pk_id_orve_histo_registro PRIMARY KEY (id)
);

--DROP SEQUENCE SPAC_SQ_ID_ORVEHISTOREGISTRO;
CREATE SEQUENCE SPAC_SQ_ID_ORVEHISTOREGISTRO
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;


CREATE OR REPLACE FUNCTION spac_nextval(
    IN sequence_name character varying,
    OUT sequence_id numeric)
  RETURNS numeric AS
$BODY$
DECLARE stmt varchar(512);

BEGIN

stmt:='SELECT nextval(''' || sequence_name || ''')' ; 

EXECUTE  stmt INTO sequence_id;

END$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
--DROP TABLE  orve_fecha_ultima_actualizacion;
create table orve_fecha_ultima_actualizacion(
	id integer NOT NULL,
	fecha_ultima_actualizacion timestamp without time zone,
	correo_enviado varchar(2),

	CONSTRAINT pk_id_orve_fecha_ultima_actualizacion PRIMARY KEY (id)
);

--DROP SEQUENCE SPAC_SQ_ID_ORVEFECHAULTIMAACTUALIZACION;
CREATE SEQUENCE SPAC_SQ_ID_ORVEFECHAULTIMAACTUALIZACION
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




--
-- Creación del usuario 'ORVE' para el proceso de consolidación
--

INSERT INTO iuseruserhdr (id, name, password, deptid, flags, stat, numbadcnts, remarks, crtrid, crtndate, updrid, upddate, pwdlastupdts, pwdmbc, pwdvpcheck) 
	VALUES ((select id From iusernextid where type=1), 'ORVE', '2k9E1U4=', 5, 0, 0, 0, 'Usuario de ORVE', 0, current_timestamp, null, null, 346255, 'N', 'N');


INSERT INTO iusergenperms (dsttype, dstid, prodid, perms) VALUES (1, (select id From iusernextid where type=1), 3, 0);
INSERT INTO iusergenperms (dsttype, dstid, prodid, perms) VALUES (1, (select id From iusernextid where type=1), 4, 0);
INSERT INTO iusergenperms (dsttype, dstid, prodid, perms) VALUES (1, (select id From iusernextid where type=1), 5, 0);

INSERT INTO iuserusertype (userid, prodid, type) VALUES ((select id From iusernextid where type=1), 1, 0);
INSERT INTO iuserusertype (userid, prodid, type) VALUES ((select id From iusernextid where type=1), 2, 0);
INSERT INTO iuserusertype (userid, prodid, type) VALUES ((select id From iusernextid where type=1), 3, 1);
INSERT INTO iuserusertype (userid, prodid, type) VALUES ((select id From iusernextid where type=1), 4, 1);
INSERT INTO iuserusertype (userid, prodid, type) VALUES ((select id From iusernextid where type=1), 5, 3);
INSERT INTO iuserusertype (userid, prodid, type) VALUES ((select id From iusernextid where type=1), 6, 0);
INSERT INTO iuserusertype (userid, prodid, type) VALUES ((select id From iusernextid where type=1), 7, 0);
INSERT INTO iuserusertype (userid, prodid, type) VALUES ((select id From iusernextid where type=1), 8, 0);

INSERT INTO scr_userconfig (userid, data, idoficpref) VALUES ((select id From iusernextid where type=1), '<Configuration></Configuration>', 3);
INSERT INTO scr_usrident (userid, tmstamp, first_name, second_name, surname) 
	VALUES ((select id From iusernextid where type=1), current_timestamp, '-', '-', 'ORVE');
INSERT INTO scr_usrloc (userid, tmstamp, address, city, zip, country, telephone, fax, email) 
	VALUES ((select id From iusernextid where type=1), current_timestamp, '-', '-', '', '', '', '', '');

UPDATE iusernextid SET id= id+1 WHERE type=1;


INSERT INTO iuserdepthdr (id, name, parentid, mgrid, type, remarks, crtrid, crtndate, updrid, upddate) VALUES ((select id From iusernextid where type=2), 'OFICINA DE ORVE', 0, 3, 1, NULL, 3, '2018-09-12 14:44:22', 3, '2018-09-12 14:44:22');

UPDATE SCR_CONTADOR SET CONTADOR = CONTADOR+1 where tablaid='SCR_OFIC';
INSERT INTO scr_ofic (id, code, acron, name, creation_date, disable_date, id_orgs, stamp, deptid, type) VALUES ((select CONTADOR from scr_contador where tablaid='SCR_OFIC'), '998', 'ORVE', 'SIR/ORVE', '2018-09-12 14:44:22', NULL, 4890, '', (select id From iusernextid where type=2), 2);

UPDATE iusernextid SET id = id+1  WHERE type=2;


UPDATE SCR_CONTADOR SET CONTADOR = CONTADOR+1 where tablaid='SCR_CA';
insert into scr_ca (id, code, matter, for_ereg, for_sreg, all_ofics, id_arch, creation_date, disable_date, enabled, id_org) values ((select CONTADOR from scr_contador where tablaid='SCR_CA'),'ORVE','ORVE',1,1,1,0,'2018-09-12 14:44:22',null,1,0);


UPDATE SCR_CONTADOR SET CONTADOR = CONTADOR+1 where tablaid='SCR_TT';
insert into scr_tt (id, transport) values ((select CONTADOR from scr_contador where tablaid='SCR_TT'),'ORVE');
