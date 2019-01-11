alter table scr_attachment add fileid integer;


CREATE TABLE scr_modifdoc(
	id integer NOT NULL,
	usr character varying(32) NOT NULL,
	modif_date timestamp without time zone NOT NULL,
	num_reg character varying(20) NOT NULL,
	id_arch integer NOT NULL,
	tipodoc integer NOT NULL,
	nombreDoc varchar(255) NOT NULL,
	nombreDocNuevo varchar(255),
	accion integer NOT NULL
);

CREATE UNIQUE INDEX scr_modifdoc0 ON scr_modifdoc (id);

CREATE INDEX scr_modifdoc1 ON scr_modifdoc (modif_date);

CREATE INDEX scr_modifdoc2 ON scr_modifdoc (num_reg);
