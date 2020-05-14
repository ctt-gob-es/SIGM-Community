
-- Molinero: adaptado para Oracle

alter table scr_attachment add fileid integer;


CREATE TABLE scr_modifdoc(
	id number(10) NOT NULL,
	usr varchar2(32 CHAR) NOT NULL,
	modif_date timestamp NOT NULL,
	num_reg varchar2(20 CHAR) NOT NULL,
	id_arch number(10) NOT NULL,
	tipodoc number(10) NOT NULL,
	nombreDoc varchar2(255 CHAR) NOT NULL,
	nombreDocNuevo varchar2(255 CHAR),
	accion number(10) NOT NULL
);

CREATE UNIQUE INDEX scr_modifdoc0 ON scr_modifdoc (id);

CREATE INDEX scr_modifdoc1 ON scr_modifdoc (modif_date);

CREATE INDEX scr_modifdoc2 ON scr_modifdoc (num_reg);
