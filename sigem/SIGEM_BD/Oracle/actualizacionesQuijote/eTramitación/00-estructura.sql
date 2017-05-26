alter table sgmrdedocumentos add idfileregistropres varchar2(250);

alter table csv_documentos add numero_registro varchar2(250);
alter table csv_documentos add fecha_registro timestamp;
alter table csv_documentos add origen_registro varchar2(250);
alter table csv_documentos add destino_registro varchar2(250);

alter table sgmrdedocumentos modify contenido null;