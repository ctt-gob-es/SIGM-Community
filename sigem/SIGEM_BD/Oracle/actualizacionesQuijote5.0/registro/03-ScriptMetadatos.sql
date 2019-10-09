create table scr_document_metadatos(
  id integer NOT NULL,
  bookid integer NOT NULL,
  folderid integer NOT NULL,
  pageid integer NOT NULL,
  fileid integer NOT NULL,

  nombreMetadato varchar2(255 CHAR) NOT NULL,
  valorMetadato varchar2(255 CHAR)
);

create index scr_document_metadatos1 on scr_document_metadatos (bookid, folderid, pageid, fileid, nombremetadato);