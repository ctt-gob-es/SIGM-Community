
---------    SPAC_DT_DOCUMENTOS_H ---------

  CREATE TABLE "SPAC_DT_DOCUMENTOS_H" 
   (	"ID" NUMBER NOT NULL ENABLE, 
	"NUMEXP" varchar(30 CHAR), 
	"FDOC" DATE, 
	"NOMBRE" varchar(100 CHAR), 
	"AUTOR" varchar(250 CHAR), 
	"ID_FASE" NUMBER, 
	"ID_TRAMITE" NUMBER, 
	"ID_TPDOC" NUMBER, 
	"TP_REG" varchar(16 CHAR), 
	"NREG" varchar(64 CHAR), 
	"FREG" DATE, 
	"INFOPAG" varchar(100 CHAR), 
	"ID_FASE_PCD" NUMBER, 
	"ID_TRAMITE_PCD" NUMBER, 
	"ESTADO" varchar(16 CHAR), 
	"ORIGEN" varchar(250 CHAR), 
	"DESCRIPCION" varchar(250 CHAR), 
	"ORIGEN_ID" varchar(20 CHAR), 
	"DESTINO" varchar(250 CHAR), 
	"AUTOR_INFO" varchar(250 CHAR), 
	"ESTADOFIRMA" varchar(2 CHAR), 
	"ID_NOTIFICACION" varchar(64 CHAR), 
	"ESTADONOTIFICACION" varchar(2 CHAR), 
	"DESTINO_ID" varchar(20 CHAR), 
	"FNOTIFICACION" DATE, 
	"FAPROBACION" DATE, 
	"ORIGEN_TIPO" varchar(1 CHAR), 
	"DESTINO_TIPO" varchar(1 CHAR), 
	"ID_PLANTILLA" NUMBER, 
	"BLOQUEO" CHAR(2 CHAR), 
	"REPOSITORIO" varchar(8 CHAR), 
	"EXTENSION" varchar(64 CHAR), 
	"FFIRMA" DATE, 
	"INFOPAG_RDE" varchar(256 CHAR), 
	"EXTENSION_RDE" varchar(64 CHAR), 
	"ID_REG_ENTIDAD" NUMBER, 
	"ID_ENTIDAD" NUMBER, 
	"COD_COTEJO" varchar(50 CHAR), 
	"ID_PROCESO_FIRMA" CLOB, 
	"ID_CIRCUITO" NUMBER, 
	"HASH" varchar(512 CHAR), 
	"FUNCION_HASH" varchar(128 CHAR), 
	"NDOC" NUMBER(*,0), 
	"NUM_ACTO" varchar(16 CHAR), 
	"COD_VERIFICACION" varchar(50 CHAR), 
	"MOTIVO_REPARO" varchar(255 CHAR), 
	"MOTIVO_RECHAZO" varchar(255 CHAR), 
	 CONSTRAINT "PK_DT_DOCUMENTOS_H" PRIMARY KEY ("ID"));
CREATE INDEX spac_dt_documentos_h_ix_idfase ON spac_dt_documentos_h (id_fase);
CREATE INDEX spac_dt_doc_h_ix_idtramite ON spac_dt_documentos_h (id_tramite);
CREATE INDEX spac_dt_doc_h_ix_numexp ON spac_dt_documentos_h (numexp);
CREATE SEQUENCE spac_sq_id_dtdocumentos_h  increment by  1 MINVALUE 1 MAXVALUE 9223372036854775807 start with  1;



-------------   SPAC_DT_INTERVINIENTES_H  ----------------

CREATE TABLE "SPAC_DT_INTERVINIENTES_H" 
   (	"ID" NUMBER NOT NULL ENABLE, 
	"ID_EXT" varchar(32 CHAR), 
	"NUMEXP" varchar(30 CHAR), 
	"ROL" varchar(4 CHAR), 
	"TIPO" NUMBER(3,0), 
	"TIPO_PERSONA" varchar(1 CHAR), 
	"NDOC" varchar(12 CHAR), 
	"NOMBRE" varchar(250 CHAR), 
	"IDDIRECCIONPOSTAL" varchar(32 CHAR), 
	"DIRNOT" varchar(250 CHAR), 
	"EMAIL" varchar(250 CHAR), 
	"C_POSTAL" varchar(10 CHAR), 
	"LOCALIDAD" varchar(150 CHAR), 
	"CAUT" varchar(150 CHAR), 
	"TFNO_FIJO" varchar(32 CHAR), 
	"TFNO_MOVIL" varchar(32 CHAR), 
	"TIPO_DIRECCION" CHAR(1 CHAR), 
	"DIRECCIONTELEMATICA" varchar(60 CHAR), 
	"DECRETO_NOTIFICADO" varchar(1 CHAR), 
	"DECRETO_TRASLADADO" varchar(1 CHAR), 
	"ACUSE_GENERADO" varchar(1 CHAR), 
	"RECURSO" varchar(16 CHAR), 
	"OBSERVACIONES" varchar(1000 CHAR), 
	"FECHA_ACUSE" TIMESTAMP (6), 
	"MOTIVO_ACUSE" varchar(1000 CHAR), 
	"RECURSO_TEXTO" varchar(1024 CHAR), 
	"ASISTE" varchar(2 CHAR), 
	"TIPO_PODER" varchar(25 CHAR), 
	"FECHA_INI" TIMESTAMP (6), 
	"FECHA_FIN" TIMESTAMP (6), 
	"SOLICITAR_OFERTA" varchar(2 CHAR), 
	"SOLICITADA_OFERTA" varchar(2 CHAR), 
	"CCC" varchar(1000 CHAR), 
	 CONSTRAINT "PK_SPAC_DT_INT_H" PRIMARY KEY ("ID"));
CREATE INDEX spac_dt_int_h_ix_numexp ON spac_dt_intervinientes_h (numexp);
CREATE SEQUENCE SPAC_SQ_ID_DTINTERVINIENTES_H  increment by  1 MINVALUE 1 MAXVALUE 9223372036854775807 start with  1;


-------------   SPAC_DT_TRAMITES_H   -------------------------

CREATE TABLE "SPAC_DT_TRAMITES_H" 
   (	"ID" NUMBER NOT NULL ENABLE, 
	"NUMEXP" varchar(30 CHAR), 
	"NOMBRE" varchar(250 CHAR), 
	"ESTADO" NUMBER(3,0), 
	"ID_TRAM_PCD" NUMBER, 
	"ID_FASE_PCD" NUMBER, 
	"ID_FASE_EXP" NUMBER, 
	"ID_TRAM_EXP" NUMBER, 
	"ID_TRAM_CTL" NUMBER, 
	"NUM_ACTO" varchar(16 CHAR), 
	"COD_ACTO" varchar(16 CHAR), 
	"ESTADO_INFO" varchar(100 CHAR), 
	"FECHA_INICIO" DATE, 
	"FECHA_CIERRE" DATE, 
	"FECHA_LIMITE" DATE, 
	"FECHA_FIN" DATE, 
	"FECHA_INICIO_PLAZO" DATE, 
	"PLAZO" NUMBER, 
	"UPLAZO" varchar(1 CHAR), 
	"OBSERVACIONES" varchar(254 CHAR), 
	"DESCRIPCION" varchar(100 CHAR), 
	"UND_RESP" varchar(250 CHAR), 
	"FASE_PCD" varchar(250 CHAR), 
	"ID_SUBPROCESO" NUMBER, 
	"ID_RESP_CLOSED" varchar(250 CHAR), 
	 CONSTRAINT "SPAC_PK_DTTRAMITES_H" PRIMARY KEY ("ID"));
CREATE INDEX spac_dt_tra_h_ix_idtramexp ON spac_dt_tramites_h (id_tram_exp);
CREATE INDEX spac_dt_tra_h_ix_numexp ON spac_dt_tramites_h (numexp);
CREATE SEQUENCE spac_sq_id_dttramites_h  increment by  1 MINVALUE 1 MAXVALUE 9223372036854775807 start with  1;


------    SPAC_EXPEDIENTES_H   ------------

CREATE TABLE "SPAC_EXPEDIENTES_H" 
   (	"ID" NUMBER NOT NULL ENABLE, 
	"ID_PCD" NUMBER, 
	"NUMEXP" varchar(30 CHAR), 
	"REFERENCIA_INTERNA" varchar(30 CHAR), 
	"NREG" varchar(64 CHAR), 
	"FREG" DATE, 
	"ESTADOINFO" varchar(128 CHAR), 
	"FESTADO" DATE, 
	"NIFCIFTITULAR" varchar(16 CHAR), 
	"IDTITULAR" varchar(32 CHAR), 
	"IDDIRECCIONPOSTAL" varchar(32 CHAR), 
	"DOMICILIO" varchar(128 CHAR), 
	"CIUDAD" varchar(64 CHAR), 
	"REGIONPAIS" varchar(64 CHAR), 
	"CPOSTAL" varchar(5 CHAR), 
	"IDENTIDADTITULAR" varchar(128 CHAR), 
	"TIPOPERSONA" varchar(1 CHAR), 
	"ROLTITULAR" varchar(4 CHAR), 
	"ASUNTO" varchar(512 CHAR), 
	"FINICIOPLAZO" DATE, 
	"POBLACION" varchar(64 CHAR), 
	"MUNICIPIO" varchar(64 CHAR), 
	"LOCALIZACION" varchar(256 CHAR), 
	"EXPRELACIONADOS" varchar(256 CHAR), 
	"CODPROCEDIMIENTO" varchar(16 CHAR), 
	"NOMBREPROCEDIMIENTO" varchar(128 CHAR), 
	"PLAZO" NUMBER, 
	"UPLAZO" varchar(1 CHAR), 
	"FORMATERMINACION" varchar(1 CHAR), 
	"UTRAMITADORA" varchar(256 CHAR), 
	"FUNCIONACTIVIDAD" varchar(80 CHAR), 
	"MATERIAS" varchar(2 CHAR), 
	"SERVPRESACTUACIONES" varchar(128 CHAR), 
	"TIPODEDOCUMENTAL" varchar(16 CHAR), 
	"PALABRASCLAVE" varchar(64 CHAR), 
	"FCIERRE" DATE, 
	"ESTADOADM" varchar(128 CHAR), 
	"HAYRECURSO" varchar(2 CHAR), 
	"EFECTOSDELSILENCIO" varchar(1 CHAR), 
	"FAPERTURA" DATE, 
	"OBSERVACIONES" varchar(256 CHAR), 
	"IDUNIDADTRAMITADORA" varchar(250 CHAR), 
	"IDPROCESO" NUMBER, 
	"TIPODIRECCIONINTERESADO" varchar(16 CHAR), 
	"NVERSION" varchar(16 CHAR), 
	"IDSECCIONINICIADORA" varchar(64 CHAR), 
	"SECCIONINICIADORA" varchar(250 CHAR), 
	"TFNOFIJO" varchar(32 CHAR), 
	"TFNOMOVIL" varchar(32 CHAR), 
	"DIRECCIONTELEMATICA" varchar(60 CHAR), 
	"VERSION" varchar(16 CHAR), 
	"FECHA_APROBACION" TIMESTAMP (6), 
	"TIPOEXP" varchar(16 CHAR));
CREATE INDEX spac_exp_h_ix_id ON spac_expedientes_h (id);
CREATE INDEX spac_exp_h_ix_numexp ON spac_expedientes_h (numexp);
CREATE SEQUENCE SPAC_SQ_ID_EXPEDIENTES_H  increment by  1 MINVALUE 0 MAXVALUE 9223372036854775807 start with  1;


--------     SPAC_HITOS_H   --------------

CREATE TABLE "SPAC_HITOS_H" 
   (	"ID" NUMBER NOT NULL ENABLE, 
	"ID_EXP" NUMBER, 
	"ID_FASE" NUMBER, 
	"ID_TRAMITE" NUMBER, 
	"HITO" NUMBER, 
	"FECHA" DATE, 
	"FECHA_LIMITE" DATE, 
	"INFO" CLOB, 
	"AUTOR" varchar(250 CHAR), 
	"DESCRIPCION" varchar(250 CHAR), 
	"ID_INFO" NUMBER, 
	 CONSTRAINT "PK_HITOS_H" PRIMARY KEY ("ID"));
CREATE INDEX spac_hitos_h_ix_idsbusqueda ON spac_hitos_h (id_exp, id_fase, id_tramite, hito);
CREATE SEQUENCE spac_sq_id_hitos_h  increment by  1 MINVALUE 0 MAXVALUE 9223372036854775807 start with 1;


----- Insertamos en SPAC_CT_ENTIDADES las nuevas entidades   ------

insert into spac_ct_entidades values (NEXTVAL('SPAC_SQ_ID_CTENTIDADES'), 1, 'SPAC_EXPEDIENTES_H', 'ID', 'NUMEXP','NUMEXP', 1,'Histórico de Expedientes','SPAC_SQ_ID_EXPEDIENTES_H', null, null, NOW());
insert into spac_ct_entidades values (NEXTVAL('SPAC_SQ_ID_CTENTIDADES'), 1, 'SPAC_DT_INTERVINIENTES_H', 'ID', 'NUMEXP','NOMBRE', 3,'Histórico de Participantes','SPAC_SQ_ID_DTINTERVINIENTES_H', null,null, NOW());
insert into spac_ct_entidades values (NEXTVAL('SPAC_SQ_ID_CTENTIDADES'), 1, 'SPAC_DT_TRAMITES_H', 'ID', 'NUMEXP','NOMBRE', 4,'Histórico de Trámites','SPAC_SQ_ID_DTTRAMITES_H', null, null, NOW());
insert into spac_ct_entidades values (NEXTVAL('SPAC_SQ_ID_CTENTIDADES'), 1, 'SPAC_DT_DOCUMENTOS_H', 'ID', 'NUMEXP','NOMBRE', 5,'Histórico de Documentos','SPAC_SQ_ID_DTDOCUMENTOS_H', null,null, NOW());

update spac_ct_entidades set definicion = (select definicion from spac_ct_entidades where nombre = 'SPAC_EXPEDIENTES') where nombre = 'SPAC_EXPEDIENTES_H';
update spac_ct_entidades set definicion = (select definicion from spac_ct_entidades where nombre = 'SPAC_DT_TRAMITES') where nombre = 'SPAC_DT_TRAMITES_H';
update spac_ct_entidades set definicion = (select definicion from spac_ct_entidades where nombre = 'SPAC_DT_INTERVINIENTES') where nombre = 'SPAC_DT_INTERVINIENTES_H';
update spac_ct_entidades set definicion = (select definicion from spac_ct_entidades where nombre = 'SPAC_DT_DOCUMENTOS') where nombre = 'SPAC_DT_DOCUMENTOS_H';

insert into spac_ct_entidades_resources select NEXTVAL('SPAC_SQ_ID_ENTIDADESRESOURCES'), id, 'es', nombre, 'Histórico de Expedientes' from spac_ct_entidades where nombre = 'SPAC_EXPEDIENTES_H';
insert into spac_ct_entidades_resources select NEXTVAL('SPAC_SQ_ID_ENTIDADESRESOURCES'), id, 'es', nombre, 'Histórico de Trámites' from spac_ct_entidades where nombre = 'SPAC_DT_TRAMITES_H';
insert into spac_ct_entidades_resources select NEXTVAL('SPAC_SQ_ID_ENTIDADESRESOURCES'), id, 'es', nombre, 'Histórico de Intervinientes' from spac_ct_entidades where nombre = 'SPAC_DT_INTERVINIENTES_H';
insert into spac_ct_entidades_resources select NEXTVAL('SPAC_SQ_ID_ENTIDADESRESOURCES'), id, 'es', nombre, 'Histórico de Documentos' from spac_ct_entidades where nombre = 'SPAC_DT_DOCUMENTOS_H';


----   Realizamos las inserciones    ----

insert into spac_expedientes_h select * from spac_expedientes where numexp in (select numexp from spac_procesos where estado='2'); -- 6s
delete from spac_expedientes where numexp in (select numexp from spac_procesos where estado='2');  -- 2s
insert into spac_dt_tramites_h select * from spac_dt_tramites where numexp in (select numexp from spac_expedientes_h); -- 11s
delete from spac_dt_tramites where numexp in (select numexp from spac_expedientes_h);  -- 4s
insert into spac_dt_intervinientes_h select * from spac_dt_intervinientes where numexp in (select numexp from spac_expedientes_h); -- 8 s
delete from spac_dt_intervinientes where numexp in (select numexp from spac_expedientes_h);  -- 1s

insert into spac_dt_documentos_h (ID,NUMEXP,FDOC,NOMBRE,AUTOR,ID_FASE,ID_TRAMITE,ID_TPDOC,TP_REG,NREG,FREG,INFOPAG,
ID_FASE_PCD,ID_TRAMITE_PCD,ESTADO,ORIGEN,DESCRIPCION,ORIGEN_ID,DESTINO,AUTOR_INFO,ESTADOFIRMA,ID_NOTIFICACION,
ESTADONOTIFICACION,DESTINO_ID,FNOTIFICACION,FAPROBACION,ORIGEN_TIPO,DESTINO_TIPO,ID_PLANTILLA,BLOQUEO,REPOSITORIO,
EXTENSION,FFIRMA,INFOPAG_RDE,
EXTENSION_RDE,ID_REG_ENTIDAD,
ID_ENTIDAD,COD_COTEJO,
ID_PROCESO_FIRMA,ID_CIRCUITO,HASH,
FUNCION_HASH,NDOC,NUM_ACTO,COD_VERIFICACION,MOTIVO_REPARO,MOTIVO_RECHAZO )
select ID,NUMEXP,FDOC,NOMBRE,AUTOR,ID_FASE,ID_TRAMITE,ID_TPDOC,TP_REG,NREG,FREG,INFOPAG,
ID_FASE_PCD,ID_TRAMITE_PCD,ESTADO,ORIGEN,DESCRIPCION,ORIGEN_ID,DESTINO,AUTOR_INFO,ESTADOFIRMA,ID_NOTIFICACION,
ESTADONOTIFICACION,DESTINO_ID,FNOTIFICACION,FAPROBACION,ORIGEN_TIPO,DESTINO_TIPO,ID_PLANTILLA,BLOQUEO,REPOSITORIO,
EXTENSION,FFIRMA,INFOPAG_RDE,
EXTENSION_RDE,ID_REG_ENTIDAD,
ID_ENTIDAD,COD_COTEJO,
ID_PROCESO_FIRMA,
ID_CIRCUITO,HASH,
FUNCION_HASH,NDOC,NUM_ACTO,COD_VERIFICACION,MOTIVO_REPARO,MOTIVO_RECHAZO 
from spac_dt_documentos where numexp in (select numexp from spac_expedientes_h);

delete from spac_dt_documentos where numexp in (select numexp from spac_expedientes_h);  -- 22s
insert into spac_hitos_h select NEXTVAL('spac_sq_id_hitos_h'), id_exp, id_fase, id_tramite, hito, fecha, fecha_limite, info, autor, descripcion, id_info from spac_hitos where id_exp in (select idproceso from spac_expedientes_h); -- 63s
delete from spac_hitos where id_exp in (select idproceso from spac_expedientes_h); -- 56s


DECLARE

dato1 clob;
dato2 clob;
dato3 clob;
dato4 clob;

begin

dato1 := '<entity version=''1.00''><editable>S</editable><dropable>N</dropable><status>0</status><fields><field id=''1''><physicalName>id_pcd</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''2''><physicalName>numexp</physicalName><type>0</type><size>30</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''3''><physicalName>referencia_interna</physicalName><type>0</type><size>30</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''4''><physicalName>nreg</physicalName><type>0</type><size>16</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''5''><physicalName>freg</physicalName><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''6''><physicalName>estadoinfo</physicalName><type>0</type><size>128</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''7''><physicalName>festado</physicalName><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''8''><physicalName>nifciftitular</physicalName><type>0</type><size>16</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''9''><physicalName>idtitular</physicalName><type>0</type><size>32</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''10''><physicalName>domicilio</physicalName><type>0</type><size>128</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''11''><physicalName>ciudad</physicalName><type>0</type><size>64</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''12''><physicalName>regionpais</physicalName><type>0</type><size>64</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''13''><physicalName>cpostal</physicalName><type>0</type><size>5</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''14''><physicalName>identidadtitular</physicalName><type>0</type><size>128</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''15''><physicalName>tipopersona</physicalName><type>0</type><size>1</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''16''><physicalName>roltitular</physicalName><type>0</type><size>4</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''17''><physicalName>asunto</physicalName><type>0</type><size>512</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''18''><physicalName>finicioplazo</physicalName><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''19''><physicalName>poblacion</physicalName><type>0</type><size>64</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''20''><physicalName>municipio</physicalName><type>0</type><size>64</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''21''><physicalName>localizacion</physicalName><type>0</type><size>256</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''22''><physicalName>exprelacionados</physicalName><type>0</type><size>256</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''23''><physicalName>codprocedimiento</physicalName><type>0</type><size>16</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''24''><physicalName>nombreprocedimiento</physicalName><type>0</type><size>128</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''25''><physicalName>plazo</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''26''><physicalName>uplazo</physicalName><type>0</type><size>1</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''27''><physicalName>formaterminacion</physicalName><type>0</type><size>1</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''28''><physicalName>utramitadora</physicalName><type>0</type><size>256</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''29''><physicalName>funcionactividad</physicalName><type>0</type><size>80</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''30''><physicalName>materias</physicalName><type>0</type><size>2</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''31''><physicalName>servpresactuaciones</physicalName><type>0</type><size>128</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''32''><physicalName>tipodedocumental</physicalName><type>0</type><size>16</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''33''><physicalName>palabrasclave</physicalName><type>0</type><size>64</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''34''><physicalName>fcierre</physicalName><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''35''><physicalName>estadoadm</physicalName><type>0</type><size>128</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''36''><physicalName>hayrecurso</physicalName><type>0</type><size>2</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''37''><physicalName>efectosdelsilencio</physicalName><type>0</type><size>1</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''38''><physicalName>fapertura</physicalName><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''39''><physicalName>observaciones</physicalName><type>0</type><size>256</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''40''><physicalName>idunidadtramitadora</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''41''><physicalName>idproceso</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''42''><physicalName>tipodireccioninteresado</physicalName><type>0</type><size>16</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''43''><physicalName>nversion</physicalName><type>0</type><size>16</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''44''><physicalName>idseccioniniciadora</physicalName><type>0</type><size>64</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''45''><physicalName>seccioniniciadora</physicalName><type>0</type><size>250</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''46''><physicalName>tfnofijo</physicalName><type>0</type><size>32</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''47''><physicalName>tfnomovil</physicalName><type>0</type><size>32</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''48''><physicalName>direcciontelematica</physicalName><type>0</type><size>60</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''49''><physicalName>id</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''1''><physicalName>iddireccionpostal</physicalName><type>0</type><size>32</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''51''><physicalName>version</physicalName><type>0</type><size>16</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''52''><physicalName>fecha_aprobacion</physicalName><descripcion><![CDATA[fecha aprobacion de la propuesta]]></descripcion><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''53''><physicalName>tipoexp</physicalName><descripcion><![CDATA[Tipo Expediente]]></descripcion><type>0</type><size>16</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field></fields><validations><validation id=''1''><fieldId>27</fieldId><table>SPAC_TBL_003</table><tableType>3</tableType><class/><mandatory>N</mandatory><hierarchicalId/><hierarchicalName/></validation><validation id=''2''><fieldId>35</fieldId><table>SPAC_TBL_004</table><tableType>3</tableType><class/><mandatory>N</mandatory><hierarchicalId/><hierarchicalName/></validation><validation id=''3''><fieldId>42</fieldId><table>SPAC_TBL_005</table><tableType>3</tableType><class/><mandatory>N</mandatory><hierarchicalId/><hierarchicalName/></validation><validation id=''4''><fieldId>16</fieldId><table>SPAC_TBL_002</table><tableType>3</tableType><class/><mandatory>N</mandatory><hierarchicalId/><hierarchicalName/></validation><validation id=''5''><fieldId>17</fieldId><table/><tableType/><class/><mandatory>S</mandatory><hierarchicalId/><hierarchicalName/></validation></validations></entity>';
dato2 := '<entity version=''1.00''><editable>S</editable><dropable>N</dropable><status>0</status><fields><field id=''1''><physicalName>id</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''2''><physicalName>numexp</physicalName><type>0</type><size>30</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''3''><physicalName>fdoc</physicalName><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''4''><physicalName>nombre</physicalName><type>0</type><size>100</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''5''><physicalName>autor</physicalName><type>0</type><size>250</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''6''><physicalName>id_fase</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''7''><physicalName>id_tramite</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''8''><physicalName>id_tpdoc</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''9''><physicalName>tp_reg</physicalName><type>0</type><size>16</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''10''><physicalName>nreg</physicalName><type>0</type><size>16</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''11''><physicalName>freg</physicalName><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''12''><physicalName>infopag</physicalName><type>0</type><size>100</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''13''><physicalName>id_fase_pcd</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''14''><physicalName>id_tramite_pcd</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''15''><physicalName>estado</physicalName><type>0</type><size>16</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''16''><physicalName>origen</physicalName><type>0</type><size>128</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''17''><physicalName>descripcion</physicalName><type>0</type><size>250</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''18''><physicalName>origen_id</physicalName><type>0</type><size>20</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''19''><physicalName>destino</physicalName><type>0</type><size>250</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''20''><physicalName>autor_info</physicalName><type>0</type><size>250</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''21''><physicalName>estadofirma</physicalName><type>0</type><size>2</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''22''><physicalName>id_notificacion</physicalName><type>0</type><size>64</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''23''><physicalName>estadonotificacion</physicalName><type>0</type><size>2</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''24''><physicalName>destino_id</physicalName><type>0</type><size>20</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''25''><physicalName>fnotificacion</physicalName><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''26''><physicalName>faprobacion</physicalName><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''27''><physicalName>origen_tipo</physicalName><type>0</type><size>1</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''28''><physicalName>destino_tipo</physicalName><type>0</type><size>1</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''29''><physicalName>id_plantilla</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''30''><physicalName>bloqueo</physicalName><type>0</type><size>2</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''31''><physicalName>repositorio</physicalName><type>0</type><size>8</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''32''><physicalName>extension</physicalName><type>0</type><size>64</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''33''><physicalName>ffirma</physicalName><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''34''><physicalName>infopag_rde</physicalName><type>0</type><size>256</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''35''><physicalName>id_reg_entidad</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''36''><physicalName>id_entidad</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''37''><physicalName>extension_rde</physicalName><descripcion><![CDATA[Extensión del documento firmado]]></descripcion><type>0</type><size>64</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''38''><physicalName>cod_cotejo</physicalName><descripcion><![CDATA[Código de cotejo]]></descripcion><type>0</type><size>50</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''39''><physicalName>id_proceso_firma</physicalName><descripcion><![CDATA[Datos identificativos del proceso de firma externo en el que está inmerso el documento]]></descripcion><type>1</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''40''><physicalName>id_circuito</physicalName><descripcion><![CDATA[Identificador del circuito utilizado para enviar al portafirmas]]></descripcion><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''41''><physicalName>hash</physicalName><descripcion><![CDATA[Hash de la función resumen aplicada al contenido del documento]]></descripcion><type>0</type><size>512</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''42''><physicalName>ndoc</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''43''><physicalName>num_acto</physicalName><type>0</type><size>16</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''44''><physicalName>funcion_hash</physicalName><descripcion><![CDATA[Función resumen aplicada para el cálculo del hash del contenido del documento]]></descripcion><type>0</type><size>128</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''45''><physicalName>cod_verificacion</physicalName><descripcion><![CDATA[Código verificación]]></descripcion><type>0</type><size>50</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''46''><physicalName>motivo_rechazo</physicalName><descripcion><![CDATA[Motivo de rechazo de la firma]]></descripcion><type>0</type><size>255</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''47''><physicalName>motivo_reparo</physicalName><descripcion><![CDATA[Motivo de reparo de la firma]]></descripcion><type>0</type><size>255</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field></fields><validations><validation id=''1''><fieldId>23</fieldId><table>SPAC_TBL_006</table><tableType>3</tableType><class/><mandatory>N</mandatory></validation><validation id=''2''><fieldId>21</fieldId><table>SPAC_TBL_008</table><tableType>3</tableType><class/><mandatory>N</mandatory></validation></validations></entity>';
dato3 := '<entity version=''1.00''><editable>S</editable><dropable>N</dropable><status>0</status><fields><field id=''1''><physicalName>id_ext</physicalName><type>0</type><size>32</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''2''><physicalName>rol</physicalName><type>0</type><size>4</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''3''><physicalName>tipo</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''4''><physicalName>tipo_persona</physicalName><type>0</type><size>1</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''5''><physicalName>ndoc</physicalName><type>0</type><size>12</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''6''><physicalName>nombre</physicalName><type>0</type><size>250</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''7''><physicalName>dirnot</physicalName><type>0</type><size>250</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''8''><physicalName>email</physicalName><type>0</type><size>250</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''9''><physicalName>c_postal</physicalName><type>0</type><size>10</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''10''><physicalName>localidad</physicalName><type>0</type><size>150</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''11''><physicalName>caut</physicalName><type>0</type><size>150</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''12''><physicalName>tfno_fijo</physicalName><type>0</type><size>32</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''13''><physicalName>tfno_movil</physicalName><type>0</type><size>32</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''14''><physicalName>tipo_direccion</physicalName><type>0</type><size>1</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''15''><physicalName>direcciontelematica</physicalName><type>0</type><size>60</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''16''><physicalName>id</physicalName><type>3</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''17''><physicalName>numexp</physicalName><type>0</type><size>30</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''18''><physicalName>iddireccionpostal</physicalName><type>0</type><size>32</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''19''><physicalName>DECRETO_NOTIFICADO</physicalName><descripcion><![CDATA[Decreto notificado]]></descripcion><type>0</type><size>1</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''20''><physicalName>DECRETO_TRASLADADO</physicalName><descripcion><![CDATA[Decreto Trasladado]]></descripcion><type>0</type><size>1</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''21''><physicalName>ACUSE_GENERADO</physicalName><descripcion><![CDATA[Acuse de recibo generado]]></descripcion><type>0</type><size>1</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''22''><physicalName>recurso</physicalName><descripcion><![CDATA[Recurso]]></descripcion><type>0</type><size>16</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''23''><physicalName>observaciones</physicalName><descripcion><![CDATA[Observaciones]]></descripcion><type>1</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''24''><physicalName>fecha_acuse</physicalName><descripcion><![CDATA[Fecha de acuse]]></descripcion><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''25''><physicalName>motivo_acuse</physicalName><descripcion><![CDATA[Motivo Acuse]]></descripcion><type>1</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''26''><physicalName>recurso_texto</physicalName><descripcion><![CDATA[Recurso texto]]></descripcion><type>0</type><size>1024</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''27''><physicalName>asiste</physicalName><type>0</type><size>2</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''28''><physicalName>tipo_poder</physicalName><descripcion><![CDATA[Tipo de Poder]]></descripcion><type>0</type><size>25</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''29''><physicalName>fecha_ini</physicalName><descripcion><![CDATA[Fecha de inicio de validez del Poder]]></descripcion><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''30''><physicalName>fecha_fin</physicalName><descripcion><![CDATA[Fecha de vencimiento del Poder]]></descripcion><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''31''><physicalName>solicitar_oferta</physicalName><descripcion><![CDATA[Solicitar oferta]]></descripcion><type>0</type><size>2</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''32''><physicalName>solicitada_oferta</physicalName><descripcion><![CDATA[Solicitada oferta]]></descripcion><type>0</type><size>2</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''26''><physicalName>CCC</physicalName><descripcion><![CDATA[Cuenta bancaria]]></descripcion><type>1</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field></fields><validations><validation id=''1''><fieldId>6</fieldId><table/><tableType/><class/><mandatory>S</mandatory><hierarchicalId/><hierarchicalName/></validation><validation id=''2''><fieldId>14</fieldId><table>SPAC_TBL_005</table><tableType>3</tableType><class/><mandatory>N</mandatory><hierarchicalId/><hierarchicalName/></validation><validation id=''3''><fieldId>2</fieldId><table>SPAC_TBL_002</table><tableType>3</tableType><class/><mandatory>N</mandatory><hierarchicalId/><hierarchicalName/></validation><validation id=''4''><fieldId>19</fieldId><table>SPAC_TBL_009</table><tableType>2</tableType><class/><mandatory>N</mandatory><hierarchicalId/><hierarchicalName/></validation><validation id=''6''><fieldId>22</fieldId><table>DPCR_RECURSOS</table><tableType>3</tableType><class/><mandatory>N</mandatory><hierarchicalId/><hierarchicalName/></validation><validation id=''7''><fieldId>28</fieldId><table>VLDTBL_TIPO_PODER</table><tableType>2</tableType><class/><mandatory>S</mandatory><hierarchicalId/><hierarchicalName/></validation></validations></entity>';
dato4 := '<entity version=''1.00''><editable>S</editable><dropable>N</dropable><status>0</status><fields><field id=''1''>      <physicalName>nombre</physicalName><type>0</type><size>250</size></field><field id=''2''><physicalName>estado</physicalName><type>2</type>              <size>1</size>  </field>        <field id=''3''>                <physicalName>id_tram_pcd</physicalName>                <type>3</type>  </field><field id=''4''>                <physicalName>id_fase_pcd</physicalName>                <type>3</type>  </field>        <field id=''5''>                <physicalName>id_fase_exp</physicalName>                <type>3</type>  </field>        <field id=''6''>                <physicalName>id_tram_exp</physicalName>                <type>3</type>  </field>        <field id=''7''>                <physicalName>id_tram_ctl</physicalName><type>3</type></field>  <field id=''8''>                <physicalName>num_acto</physicalName><type>0</type>     <size>16</size> </field><field id=''9''>                <physicalName>cod_acto</physicalName>           <type>0</type>  <size>16</size> </field><field id=''10''>       <physicalName>estado_info</physicalName><type>0</type><size>100</size></field><field id=''11''>             <physicalName>fecha_inicio</physicalName>               <type>13</type> </field>        <field id=''12''>               <physicalName>fecha_cierre</physicalName>               <type>6</type>  </field>        <field id=''13''>               <physicalName>fecha_limite</physicalName>               <type>6</type>  </field>        <field id=''14''>               <physicalName>fecha_fin</physicalName>          <type>6</type>  </field>        <field id=''15''>               <physicalName>fecha_inicio_plazo</physicalName>         <type>6</type>  </field>        <field id=''16''>               <physicalName>plazo</physicalName>              <type>3</type>  </field>        <field id=''17''>               <physicalName>uplazo</physicalName>             <type>0</type>          <size>1</size>  </field>        <field id=''18''>               <physicalName>observaciones</physicalName>              <type>0</type>          <size>254</size>        </field>        <field id=''19''>               <physicalName>descripcion</physicalName>                <type>0</type>          <size>100</size>        </field>        <field id=''20''>               <physicalName>und_resp</physicalName>           <type>0</type>          <size>250</size></field><field id=''21''>     <physicalName>fase_pcd</physicalName><type>0</type>             <size>250</size>        </field>        <field id=''22''>               <physicalName>id_subproceso</physicalName>              <type>3</type>  </field>        <field id=''23''><physicalName>id_resp_closed</physicalName><type>0</type><size>250</size></field><field id=''24''>            <physicalName>id</physicalName>         <type>3</type>  </field>        <field id=''25''>              <physicalName>numexp</physicalName>             <type>0</type>          <size>30</size> </field></fields><validations>  <validation id=''1''>           <fieldId>17</fieldId>           <table>SPAC_TBL_001</table>             <tableType>3</tableType>                <class/>                <mandatory>N</mandatory>        </validation></validations></entity>';

update spac_ct_entidades set definicion = dato1 where nombre = 'SPAC_EXPEDIENTES_H';
update spac_ct_entidades set definicion = dato2 where nombre = 'SPAC_DT_DOCUMENTOS_H';
update spac_ct_entidades set definicion = dato3 where nombre = 'SPAC_DT_INTERVINIENTES_H';
update spac_ct_entidades set definicion = dato4 where nombre = 'SPAC_DT_TRAMITES_H';

end;
