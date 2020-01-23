
CREATE TABLE spac_p_tram_datosespecificos(
  id integer NOT NULL,
  id_tram_pcd integer,
  plantilla_defecto clob,
  otros_datos clob,
  CONSTRAINT pk_p_tram_datosespecificos PRIMARY KEY (id)
);


CREATE SEQUENCE SPAC_SQ_ID_PTRAM_DATOSESPEC
  INCREMENT by 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START with 1;

insert into spac_ct_entidades values (NEXTVAL('SPAC_SQ_ID_CTENTIDADES'),0,'SPAC_P_TRAM_DATOSESPECIFICOS','ID','','','','','SPAC_SQ_ID_PTRAM_DATOSESPEC','','', NOW());



CREATE TABLE spac_p_ctosfirmatramite
(
  id integer NOT NULL,
  id_circuito integer,
  id_tram_pcd integer,
  CONSTRAINT pk_p_ctosfirmatramite PRIMARY KEY (id)
);

CREATE SEQUENCE spac_sq_id_pctosfirmatramite
  INCREMENT BY 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START WITH 1;