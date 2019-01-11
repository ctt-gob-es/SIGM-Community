create table scr_document_metadatos(
  id integer NOT NULL,
  bookid integer NOT NULL,
  folderid integer NOT NULL,
  pageid integer NOT NULL,
  fileid integer NOT NULL,

  nombreMetadato varchar(255) NOT NULL,
  valorMetadato varchar(255)
);
