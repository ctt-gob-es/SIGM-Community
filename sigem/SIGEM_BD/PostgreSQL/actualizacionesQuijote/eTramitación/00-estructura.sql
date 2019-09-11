alter table sgmrdedocumentos add idfileregistropres varchar(250);

alter table csv_documentos add numero_registro varchar(250);
alter table csv_documentos add fecha_registro timestamp;
alter table csv_documentos add origen_registro varchar(250);
alter table csv_documentos add destino_registro varchar(250);

alter table sgmrdedocumentos alter column contenido drop not null;