CREATE TABLE A11CLFH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 BYTE)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A11CLFHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 BYTE)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A11DOCH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 BYTE)                    NULL,
  AUTHOR      VARCHAR2(64 BYTE)                     NULL,
  KEYWORDS    VARCHAR2(254 BYTE)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A11DOCHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 BYTE)                    NULL,
  AUTHOR      VARCHAR2(64 BYTE)                     NULL,
  KEYWORDS    VARCHAR2(254 BYTE)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A11FDRH
(
  ID          NUMBER(10)                            NULL,
  VERSTAT     NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A11PAGEH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 BYTE)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A11PAGEHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 BYTE)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A11SF
(
  FDRID      NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL,
  FLD1       VARCHAR2(20 CHAR)                      NULL,
  FLD2       DATE                                   NULL,
  FLD3       VARCHAR2(32 CHAR)                      NULL,
  FLD4       DATE                                   NULL,
  FLD5       NUMBER(10)                             NULL,
  FLD6       NUMBER(10)                             NULL,
  FLD7       NUMBER(10)                             NULL,
  FLD8       NUMBER(10)                             NULL,
  FLD9       VARCHAR2(80 CHAR)                      NULL,
  FLD10      VARCHAR2(20 CHAR)                      NULL,
  FLD11      NUMBER(10)                             NULL,
  FLD12      DATE                                   NULL,
  FLD13      NUMBER(10)                             NULL,
  FLD14      VARCHAR2(31 CHAR)                      NULL,
  FLD15      VARCHAR2(30 CHAR)                      NULL,
  FLD16      NUMBER(10)                             NULL,
  FLD17      NVARCHAR2(1000)                        NULL,
  FLD19      NVARCHAR2(80)                          NULL,
  FLD20      DATE                                   NULL,
  FLD503     NUMBER(10)                             NULL,
  FLD504     NUMBER(10)                             NULL,
  FLD505     NUMBER(10)                             NULL,
  FLD506     NUMBER(10)                             NULL,
  FLD1002    NUMBER(1)                              NULL
);


CREATE TABLE A11XF
(
  ID         NUMBER(10)                             NULL,
  FDRID      NUMBER(10)                             NULL,
  FLDID      NUMBER(10)                             NULL,
  TEXT       CLOB                                   NULL,
  TIMESTAMP  DATE                                   NULL
);


CREATE TABLE A11XNID
(
  TYPE      NUMBER(10)                              NULL,
  PARENTID  NUMBER(10)                              NULL,
  ID        NUMBER(10)                              NULL
);


CREATE TABLE A13CLFH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 BYTE)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A13CLFHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 BYTE)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A13DOCH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 BYTE)                    NULL,
  AUTHOR      VARCHAR2(64 BYTE)                     NULL,
  KEYWORDS    VARCHAR2(254 BYTE)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A13DOCHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 BYTE)                    NULL,
  AUTHOR      VARCHAR2(64 BYTE)                     NULL,
  KEYWORDS    VARCHAR2(254 BYTE)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A13FDRH
(
  ID          NUMBER(10)                            NULL,
  VERSTAT     NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A13PAGEH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 BYTE)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A13PAGEHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 BYTE)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A13SF
(
  FDRID      NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL,
  FLD1       VARCHAR2(20 BYTE)                      NULL,
  FLD2       DATE                                   NULL,
  FLD3       VARCHAR2(32 BYTE)                      NULL,
  FLD4       DATE                                   NULL,
  FLD5       NUMBER(10)                             NULL,
  FLD6       NUMBER(10)                             NULL,
  FLD7       NUMBER(10)                             NULL,
  FLD8       NUMBER(10)                             NULL,
  FLD9       VARCHAR2(80 BYTE)                      NULL,
  FLD10      VARCHAR2(20 BYTE)                      NULL,
  FLD11      NUMBER(10)                             NULL,
  FLD12      DATE                                   NULL,
  FLD13      NUMBER(10)                             NULL,
  FLD14      VARCHAR2(31 BYTE)                      NULL,
  FLD15      VARCHAR2(30 CHAR)                      NULL,
  FLD16      NUMBER(10)                             NULL,
  FLD17      NVARCHAR2(1000)                        NULL,
  FLD19      NVARCHAR2(80)                          NULL,
  FLD20      DATE                                   NULL,
  FLD503     NUMBER(10)                             NULL,
  FLD504     NUMBER(10)                             NULL,
  FLD505     NUMBER(10)                             NULL,
  FLD506     NUMBER(10)                             NULL,
  FLD1002    NUMBER(1)                              NULL
);


CREATE TABLE A13XF
(
  ID         NUMBER(10)                             NULL,
  FDRID      NUMBER(10)                             NULL,
  FLDID      NUMBER(10)                             NULL,
  TEXT       CLOB                                   NULL,
  TIMESTAMP  DATE                                   NULL
);


CREATE TABLE A13XNID
(
  TYPE      NUMBER(10)                              NULL,
  PARENTID  NUMBER(10)                              NULL,
  ID        NUMBER(10)                              NULL
);


CREATE TABLE A14CLFH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 BYTE)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A14CLFHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 BYTE)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A14DOCH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 BYTE)                    NULL,
  AUTHOR      VARCHAR2(64 BYTE)                     NULL,
  KEYWORDS    VARCHAR2(254 BYTE)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A14DOCHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 BYTE)                    NULL,
  AUTHOR      VARCHAR2(64 BYTE)                     NULL,
  KEYWORDS    VARCHAR2(254 BYTE)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A14FDRH
(
  ID          NUMBER(10)                            NULL,
  VERSTAT     NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A14PAGEH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 BYTE)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A14PAGEHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 BYTE)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A14SF
(
  FDRID      NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL,
  FLD1       VARCHAR2(20 BYTE)                      NULL,
  FLD2       DATE                                   NULL,
  FLD3       VARCHAR2(32 BYTE)                      NULL,
  FLD4       DATE                                   NULL,
  FLD5       NUMBER(10)                             NULL,
  FLD6       NUMBER(10)                             NULL,
  FLD7       NUMBER(10)                             NULL,
  FLD8       NUMBER(10)                             NULL,
  FLD9       VARCHAR2(80 BYTE)                      NULL,
  FLD10      VARCHAR2(31 BYTE)                      NULL,
  FLD11      VARCHAR2(30 BYTE)                      NULL,
  FLD12      NUMBER(10)                             NULL,
  FLD13      NVARCHAR2(1000)                        NULL,
  FLD15      DATE                                   NULL,
  FLD503     NUMBER(10)                             NULL,
  FLD504     NUMBER(10)                             NULL,
  FLD505     NUMBER(10)                             NULL,
  FLD506     NUMBER(10)                             NULL,
  FLD1002    NUMBER(1)                              NULL
);


CREATE TABLE A14XF
(
  ID         NUMBER(10)                             NULL,
  FDRID      NUMBER(10)                             NULL,
  FLDID      NUMBER(10)                             NULL,
  TEXT       CLOB                                   NULL,
  TIMESTAMP  DATE                                   NULL
);


CREATE TABLE A14XNID
(
  TYPE      NUMBER(10)                              NULL,
  PARENTID  NUMBER(10)                              NULL,
  ID        NUMBER(10)                              NULL
);


CREATE TABLE A1CLFH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A1CLFHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A1DOCH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 CHAR)                    NULL,
  AUTHOR      VARCHAR2(64 CHAR)                     NULL,
  KEYWORDS    VARCHAR2(254 CHAR)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A1DOCHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 CHAR)                    NULL,
  AUTHOR      VARCHAR2(64 CHAR)                     NULL,
  KEYWORDS    VARCHAR2(254 CHAR)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A1FDRH
(
  ID          NUMBER(10)                            NULL,
  VERSTAT     NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A1PAGEH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 CHAR)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A1PAGEHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 CHAR)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A1SF
(
  FDRID      NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL,
  FLD1       VARCHAR2(20 CHAR)                      NULL,
  FLD2       DATE                                   NULL,
  FLD3       VARCHAR2(32 CHAR)                      NULL,
  FLD4       DATE                                   NULL,
  FLD5       NUMBER(10)                             NULL,
  FLD6       NUMBER(10)                             NULL,
  FLD7       NUMBER(10)                             NULL,
  FLD8       NUMBER(10)                             NULL,
  FLD9       VARCHAR2(80 CHAR)                      NULL,
  FLD10      VARCHAR2(20 CHAR)                      NULL,
  FLD11      NUMBER(10)                             NULL,
  FLD12      DATE                                   NULL,
  FLD13      NUMBER(10)                             NULL,
  FLD14      VARCHAR2(31 CHAR)                      NULL,
  FLD15      VARCHAR2(30 CHAR)                      NULL,
  FLD16      NUMBER(10)                             NULL,
  FLD17      NVARCHAR2(1000)                        NULL,
  FLD19      NVARCHAR2(80)                          NULL,
  FLD20      DATE                                   NULL,
  FLD503     NUMBER(10)                             NULL,
  FLD504     NUMBER(10)                             NULL,
  FLD505     NUMBER(10)                             NULL,
  FLD506     NUMBER(10)                             NULL,
  FLD1002    NUMBER(1)                              NULL
);


CREATE TABLE A1XF
(
  ID         NUMBER(10)                             NULL,
  FDRID      NUMBER(10)                             NULL,
  FLDID      NUMBER(10)                             NULL,
  TEXT       CLOB                                   NULL,
  TIMESTAMP  DATE                                   NULL
);


CREATE TABLE A1XNID
(
  TYPE      NUMBER(10)                              NULL,
  PARENTID  NUMBER(10)                              NULL,
  ID        NUMBER(10)                              NULL
);


CREATE TABLE A2CLFH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A2CLFHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A2DOCH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 CHAR)                    NULL,
  AUTHOR      VARCHAR2(64 CHAR)                     NULL,
  KEYWORDS    VARCHAR2(254 CHAR)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A2DOCHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 CHAR)                    NULL,
  AUTHOR      VARCHAR2(64 CHAR)                     NULL,
  KEYWORDS    VARCHAR2(254 CHAR)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A2FDRH
(
  ID          NUMBER(10)                            NULL,
  VERSTAT     NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A2PAGEH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 CHAR)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A2PAGEHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 CHAR)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A2SF
(
  FDRID      NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL,
  FLD1       VARCHAR2(20 CHAR)                      NULL,
  FLD2       DATE                                   NULL,
  FLD3       VARCHAR2(32 CHAR)                      NULL,
  FLD4       DATE                                   NULL,
  FLD5       NUMBER(10)                             NULL,
  FLD6       NUMBER(10)                             NULL,
  FLD7       NUMBER(10)                             NULL,
  FLD8       NUMBER(10)                             NULL,
  FLD9       VARCHAR2(80 CHAR)                      NULL,
  FLD10      VARCHAR2(31 CHAR)                      NULL,
  FLD11      VARCHAR2(30 CHAR)                      NULL,
  FLD12      NUMBER(10)                             NULL,
  FLD13      NVARCHAR2(1000)                        NULL,
  FLD15      DATE                                   NULL,
  FLD503     NUMBER(10)                             NULL,
  FLD504     NUMBER(10)                             NULL,
  FLD505     NUMBER(10)                             NULL,
  FLD506     NUMBER(10)                             NULL,
  FLD1002    NUMBER(1)                              NULL
);


CREATE TABLE A2XF
(
  ID         NUMBER(10)                             NULL,
  FDRID      NUMBER(10)                             NULL,
  FLDID      NUMBER(10)                             NULL,
  TEXT       CLOB                                   NULL,
  TIMESTAMP  DATE                                   NULL
);


CREATE TABLE A2XNID
(
  TYPE      NUMBER(10)                              NULL,
  PARENTID  NUMBER(10)                              NULL,
  ID        NUMBER(10)                              NULL
);


CREATE TABLE A3CLFH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A3CLFHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A3DOCH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 CHAR)                    NULL,
  AUTHOR      VARCHAR2(64 CHAR)                     NULL,
  KEYWORDS    VARCHAR2(254 CHAR)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A3DOCHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 CHAR)                    NULL,
  AUTHOR      VARCHAR2(64 CHAR)                     NULL,
  KEYWORDS    VARCHAR2(254 CHAR)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A3FDRH
(
  ID          NUMBER(10)                            NULL,
  VERSTAT     NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A3PAGEH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 CHAR)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A3PAGEHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 CHAR)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A3SF
(
  FDRID      NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL,
  FLD1       NUMBER(10)                             NULL,
  FLD2       VARCHAR2(250 CHAR)                     NULL,
  FLD3       NUMBER(10)                             NULL,
  FLD4       NUMBER(10)                             NULL,
  FLD5       VARCHAR2(250 CHAR)                     NULL,
  FLD6       NUMBER(10)                             NULL,
  FLD7       VARCHAR2(250 CHAR)                     NULL,
  FLD8       VARCHAR2(250 CHAR)                     NULL,
  FLD9       VARCHAR2(250 CHAR)                     NULL,
  FLD10      NUMBER(10)                             NULL,
  FLD11      VARCHAR2(250 CHAR)                     NULL,
  FLD12      NUMBER(10)                             NULL,
  FLD13      VARCHAR2(1000 CHAR)                    NULL,
  FLD14      VARCHAR2(250 CHAR)                     NULL,
  FLD15      NUMBER(10)                             NULL
);


CREATE TABLE A3XF
(
  ID         NUMBER(10)                             NULL,
  FDRID      NUMBER(10)                             NULL,
  FLDID      NUMBER(10)                             NULL,
  TEXT       CLOB                                   NULL,
  TIMESTAMP  DATE                                   NULL
);


CREATE TABLE A3XNID
(
  TYPE      NUMBER(10)                              NULL,
  PARENTID  NUMBER(10)                              NULL,
  ID        NUMBER(10)                              NULL
);


CREATE TABLE A4CLFH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A4CLFHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A4DOCH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 CHAR)                    NULL,
  AUTHOR      VARCHAR2(64 CHAR)                     NULL,
  KEYWORDS    VARCHAR2(254 CHAR)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A4DOCHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 CHAR)                    NULL,
  AUTHOR      VARCHAR2(64 CHAR)                     NULL,
  KEYWORDS    VARCHAR2(254 CHAR)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A4FDRH
(
  ID          NUMBER(10)                            NULL,
  VERSTAT     NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A4PAGEH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 CHAR)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A4PAGEHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 CHAR)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A4SF
(
  FDRID      NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL,
  FLD1       VARCHAR2(250 CHAR)                     NULL,
  FLD2       VARCHAR2(250 CHAR)                     NULL,
  FLD3       VARCHAR2(250 CHAR)                     NULL,
  FLD4       VARCHAR2(250 CHAR)                     NULL,
  FLD5       VARCHAR2(250 CHAR)                     NULL,
  FLD7       VARCHAR2(250 CHAR)                     NULL,
  FLD8       NUMBER(10)                             NULL,
  FLD9       DATE                                   NULL
);


CREATE TABLE A4XF
(
  ID         NUMBER(10)                             NULL,
  FDRID      NUMBER(10)                             NULL,
  FLDID      NUMBER(10)                             NULL,
  TEXT       CLOB                                   NULL,
  TIMESTAMP  DATE                                   NULL
);


CREATE TABLE A4XNID
(
  TYPE      NUMBER(10)                              NULL,
  PARENTID  NUMBER(10)                              NULL,
  ID        NUMBER(10)                              NULL
);


CREATE TABLE A5CLFH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 BYTE)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A5CLFHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 BYTE)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A5DOCH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 BYTE)                    NULL,
  AUTHOR      VARCHAR2(64 BYTE)                     NULL,
  KEYWORDS    VARCHAR2(254 BYTE)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A5DOCHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 BYTE)                    NULL,
  AUTHOR      VARCHAR2(64 BYTE)                     NULL,
  KEYWORDS    VARCHAR2(254 BYTE)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A5FDRH
(
  ID          NUMBER(10)                            NULL,
  VERSTAT     NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A5PAGEH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 BYTE)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A5PAGEHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 BYTE)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A5SF
(
  FDRID      NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL,
  FLD1       VARCHAR2(20 BYTE)                      NULL,
  FLD2       DATE                                   NULL,
  FLD3       VARCHAR2(32 BYTE)                      NULL,
  FLD4       DATE                                   NULL,
  FLD5       NUMBER(10)                             NULL,
  FLD6       NUMBER(10)                             NULL,
  FLD7       NUMBER(10)                             NULL,
  FLD8       NUMBER(10)                             NULL,
  FLD9       VARCHAR2(80 BYTE)                      NULL,
  FLD10      VARCHAR2(20 BYTE)                      NULL,
  FLD11      NUMBER(10)                             NULL,
  FLD12      DATE                                   NULL,
  FLD13      NUMBER(10)                             NULL,
  FLD14      VARCHAR2(31 BYTE)                      NULL,
  FLD15      VARCHAR2(30 CHAR)                      NULL,
  FLD16      NUMBER(10)                             NULL,
  FLD17      NVARCHAR2(1000)                        NULL,
  FLD19      NVARCHAR2(80)                          NULL,
  FLD20      DATE                                   NULL,
  FLD503     NUMBER(10)                             NULL,
  FLD504     NUMBER(10)                             NULL,
  FLD505     NUMBER(10)                             NULL,
  FLD506     NUMBER(10)                             NULL,
  FLD1002    NUMBER(1)                              NULL
);


CREATE TABLE A5XF
(
  ID         NUMBER(10)                             NULL,
  FDRID      NUMBER(10)                             NULL,
  FLDID      NUMBER(10)                             NULL,
  TEXT       CLOB                                   NULL,
  TIMESTAMP  DATE                                   NULL
);


CREATE TABLE A5XNID
(
  TYPE      NUMBER(10)                              NULL,
  PARENTID  NUMBER(10)                              NULL,
  ID        NUMBER(10)                              NULL
);


CREATE TABLE A6CLFH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 BYTE)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A6CLFHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 BYTE)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A6DOCH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 BYTE)                    NULL,
  AUTHOR      VARCHAR2(64 BYTE)                     NULL,
  KEYWORDS    VARCHAR2(254 BYTE)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A6DOCHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 BYTE)                    NULL,
  AUTHOR      VARCHAR2(64 BYTE)                     NULL,
  KEYWORDS    VARCHAR2(254 BYTE)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A6FDRH
(
  ID          NUMBER(10)                            NULL,
  VERSTAT     NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A6PAGEH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 BYTE)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A6PAGEHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 BYTE)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A6SF
(
  FDRID      NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL,
  FLD1       VARCHAR2(20 BYTE)                      NULL,
  FLD2       DATE                                   NULL,
  FLD3       VARCHAR2(32 BYTE)                      NULL,
  FLD4       DATE                                   NULL,
  FLD5       NUMBER(10)                             NULL,
  FLD6       NUMBER(10)                             NULL,
  FLD7       NUMBER(10)                             NULL,
  FLD8       NUMBER(10)                             NULL,
  FLD9       VARCHAR2(80 BYTE)                      NULL,
  FLD10      VARCHAR2(20 BYTE)                      NULL,
  FLD11      NUMBER(10)                             NULL,
  FLD12      DATE                                   NULL,
  FLD13      NUMBER(10)                             NULL,
  FLD14      VARCHAR2(31 BYTE)                      NULL,
  FLD15      VARCHAR2(30 CHAR)                      NULL,
  FLD16      NUMBER(10)                             NULL,
  FLD17      NVARCHAR2(1000)                        NULL,
  FLD19      NVARCHAR2(80)                          NULL,
  FLD20      DATE                                   NULL,
  FLD503     NUMBER(10)                             NULL,
  FLD504     NUMBER(10)                             NULL,
  FLD505     NUMBER(10)                             NULL,
  FLD506     NUMBER(10)                             NULL,
  FLD21      VARCHAR2(20 BYTE)                      NULL,
  FLD22      VARCHAR2(20 BYTE)                      NULL,
  FLD1002    NUMBER(1)                              NULL
);


CREATE TABLE A6XF
(
  ID         NUMBER(10)                             NULL,
  FDRID      NUMBER(10)                             NULL,
  FLDID      NUMBER(10)                             NULL,
  TEXT       CLOB                                   NULL,
  TIMESTAMP  DATE                                   NULL
);


CREATE TABLE A6XNID
(
  TYPE      NUMBER(10)                              NULL,
  PARENTID  NUMBER(10)                              NULL,
  ID        NUMBER(10)                              NULL
);


CREATE TABLE A7CLFH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 BYTE)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A7CLFHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 BYTE)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A7DOCH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 BYTE)                    NULL,
  AUTHOR      VARCHAR2(64 BYTE)                     NULL,
  KEYWORDS    VARCHAR2(254 BYTE)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A7DOCHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 BYTE)                    NULL,
  AUTHOR      VARCHAR2(64 BYTE)                     NULL,
  KEYWORDS    VARCHAR2(254 BYTE)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A7FDRH
(
  ID          NUMBER(10)                            NULL,
  VERSTAT     NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A7PAGEH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 BYTE)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A7PAGEHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 BYTE)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A7SF
(
  FDRID      NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL,
  FLD1       VARCHAR2(20 BYTE)                      NULL,
  FLD2       DATE                                   NULL,
  FLD3       VARCHAR2(32 BYTE)                      NULL,
  FLD4       DATE                                   NULL,
  FLD5       NUMBER(10)                             NULL,
  FLD6       NUMBER(10)                             NULL,
  FLD7       NUMBER(10)                             NULL,
  FLD8       NUMBER(10)                             NULL,
  FLD9       VARCHAR2(80 BYTE)                      NULL,
  FLD10      VARCHAR2(31 BYTE)                      NULL,
  FLD11      VARCHAR2(30 BYTE)                      NULL,
  FLD12      NUMBER(10)                             NULL,
  FLD13      NVARCHAR2(1000)                        NULL,
  FLD15      DATE                                   NULL,
  FLD503     NUMBER(10)                             NULL,
  FLD504     NUMBER(10)                             NULL,
  FLD505     NUMBER(10)                             NULL,
  FLD506     NUMBER(10)                             NULL,
  FLD21      VARCHAR2(20 BYTE)                      NULL,
  FLD22      VARCHAR2(20 BYTE)                      NULL,
  FLD1002    NUMBER(1)                              NULL
);


CREATE TABLE A7XF
(
  ID         NUMBER(10)                             NULL,
  FDRID      NUMBER(10)                             NULL,
  FLDID      NUMBER(10)                             NULL,
  TEXT       CLOB                                   NULL,
  TIMESTAMP  DATE                                   NULL
);


CREATE TABLE A7XNID
(
  TYPE      NUMBER(10)                              NULL,
  PARENTID  NUMBER(10)                              NULL,
  ID        NUMBER(10)                              NULL
);


CREATE TABLE A8CLFH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 BYTE)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A8CLFHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 BYTE)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  PARENTID    NUMBER(10)                            NULL,
  INFO        NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE A8DOCH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 BYTE)                    NULL,
  AUTHOR      VARCHAR2(64 BYTE)                     NULL,
  KEYWORDS    VARCHAR2(254 BYTE)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A8DOCHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 BYTE)                    NULL,
  AUTHOR      VARCHAR2(64 BYTE)                     NULL,
  KEYWORDS    VARCHAR2(254 BYTE)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE A8FDRH
(
  ID          NUMBER(10)                            NULL,
  VERSTAT     NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A8PAGEH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 BYTE)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A8PAGEHX
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  NAME        VARCHAR2(80 CHAR)                     NULL,
  SORTORDER   NUMBER(10)                            NULL,
  DOCID       NUMBER(10)                            NULL,
  FILEID      NUMBER(10)                            NULL,
  VOLID       NUMBER(10)                            NULL,
  LOC         VARCHAR2(254 BYTE)                    NULL,
  ANNID       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 BYTE)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL
);


CREATE TABLE A8SF
(
  FDRID      NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL,
  FLD1       VARCHAR2(20 BYTE)                      NULL,
  FLD2       DATE                                   NULL,
  FLD3       VARCHAR2(32 BYTE)                      NULL,
  FLD4       DATE                                   NULL,
  FLD5       NUMBER(10)                             NULL,
  FLD6       NUMBER(10)                             NULL,
  FLD7       NUMBER(10)                             NULL,
  FLD8       NUMBER(10)                             NULL,
  FLD9       VARCHAR2(80 BYTE)                      NULL,
  FLD10      VARCHAR2(31 BYTE)                      NULL,
  FLD11      VARCHAR2(30 BYTE)                      NULL,
  FLD12      NUMBER(10)                             NULL,
  FLD13      NVARCHAR2(1000)                        NULL,
  FLD15      DATE                                   NULL,
  FLD503     NUMBER(10)                             NULL,
  FLD504     NUMBER(10)                             NULL,
  FLD505     NUMBER(10)                             NULL,
  FLD506     NUMBER(10)                             NULL,
  FLD21      VARCHAR2(20 BYTE)                      NULL,
  FLD22      VARCHAR2(20 BYTE)                      NULL,
  FLD1002    NUMBER(1)                              NULL
);


CREATE TABLE A8XF
(
  ID         NUMBER(10)                             NULL,
  FDRID      NUMBER(10)                             NULL,
  FLDID      NUMBER(10)                             NULL,
  TEXT       CLOB                                   NULL,
  TIMESTAMP  DATE                                   NULL
);


CREATE TABLE A8XNID
(
  TYPE      NUMBER(10)                              NULL,
  PARENTID  NUMBER(10)                              NULL,
  ID        NUMBER(10)                              NULL
);


CREATE TABLE IDOCAPPEVENT
(
  APPID    NUMBER(10)                               NULL,
  EVENTID  NUMBER(10)                               NULL,
  MACROID  NUMBER(10)                               NULL
);


CREATE TABLE IDOCARCHDET
(
  ARCHID   NUMBER(10)                               NULL,
  DETTYPE  NUMBER(10)                               NULL,
  DETVAL   CLOB                                     NULL
);


CREATE TABLE IDOCARCHHDR
(
  ID          NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  TBLPREFIX   VARCHAR2(16 CHAR)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  FLAGS       NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE IDOCAUTONUMCTLG
(
  ID        NUMBER(10)                              NULL,
  NAME      VARCHAR2(64 CHAR)                       NULL,
  TBLNAME   VARCHAR2(32 CHAR)                       NULL,
  INFO      VARCHAR2(254 CHAR)                      NULL,
  REMARKS   VARCHAR2(254 CHAR)                      NULL,
  CRTRID    NUMBER(10)                              NULL,
  CRTNDATE  DATE                                    NULL,
  UPDRID    NUMBER(10)                              NULL,
  UPDDATE   DATE                                    NULL
);


CREATE TABLE IDOCBTBLCTLG
(
  ID        NUMBER(10)                              NULL,
  NAME      VARCHAR2(32 CHAR)                       NULL,
  DEF       CLOB                                    NULL,
  FLAGS     NUMBER(10)                              NULL,
  REMARKS   VARCHAR2(254 CHAR)                      NULL,
  CRTRID    NUMBER(10)                              NULL,
  CRTNDATE  DATE                                    NULL,
  UPDRID    NUMBER(10)                              NULL,
  UPDDATE   DATE                                    NULL
);


CREATE TABLE IDOCCOMPUSG
(
  COMPID    NUMBER(10)                              NULL,
  COMPTYPE  NUMBER(10)                              NULL,
  USRID     NUMBER(10)                              NULL,
  USRTYPE   NUMBER(10)                              NULL,
  ARCHID    NUMBER(10)                              NULL
);


CREATE TABLE IDOCDATNODE
(
  ID        NUMBER(10)                              NULL,
  TYPE      NUMBER(10)                              NULL,
  PARENTID  NUMBER(10)                              NULL
);


CREATE TABLE IDOCDBCTLG
(
  ID          NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  DBNAME      VARCHAR2(32 CHAR)                     NULL,
  DBUSER      VARCHAR2(32 CHAR)                     NULL,
  DBPASSWORD  VARCHAR2(34 CHAR)                     NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL
);


CREATE TABLE IDOCDBINFO
(
  ID              NUMBER(10)                        NULL,
  NAME            VARCHAR2(32 CHAR)                 NULL,
  GRALDBNAME      VARCHAR2(32 CHAR)                 NULL,
  GRALDBUSER      VARCHAR2(32 CHAR)                 NULL,
  GRALDBPASSWORD  VARCHAR2(34 CHAR)                 NULL,
  MISC            CLOB                              NULL
);


CREATE TABLE IDOCDIRHDR
(
  ID          NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  TYPE        NUMBER(10)                            NULL,
  FLAGS       NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE IDOCFDRLINK
(
  ID          NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  CNTRDBID    NUMBER(10)                            NULL,
  CNTRARCHID  NUMBER(10)                            NULL,
  CNTRFDRID   NUMBER(10)                            NULL,
  CNTRCLFID   NUMBER(10)                            NULL,
  SRVRDBID    NUMBER(10)                            NULL,
  SRVRARCHID  NUMBER(10)                            NULL,
  SRVRFDRID   NUMBER(10)                            NULL,
  FLAGS       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL
);


CREATE TABLE IDOCFDRLINKX
(
  ID          NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  CNTRDBID    NUMBER(10)                            NULL,
  CNTRARCHID  NUMBER(10)                            NULL,
  CNTRFDRID   NUMBER(10)                            NULL,
  CNTRCLFID   NUMBER(10)                            NULL,
  SRVRDBID    NUMBER(10)                            NULL,
  SRVRARCHID  NUMBER(10)                            NULL,
  SRVRFDRID   NUMBER(10)                            NULL,
  FLAGS       NUMBER(10)                            NULL,
  STAT        NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL
);


CREATE TABLE IDOCFDRSTAT
(
  FDRID      NUMBER(10)                             NULL,
  ARCHID     NUMBER(10)                             NULL,
  STAT       NUMBER(10)                             NULL,
  USERID     NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL,
  FLAGS      NUMBER(10)                             NULL
);


CREATE TABLE IDOCFIAUXTBLCTLG
(
  ID         NUMBER(10)                             NULL,
  NAME       VARCHAR2(16 CHAR)                      NULL,
  USERID     NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL
);


CREATE TABLE IDOCFMT
(
  ID          NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  ARCHID      NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  SUBTYPE     NUMBER(10)                            NULL,
  DATA        CLOB                                  NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE IDOCGLBDOCH
(
  ID          NUMBER(10)                            NULL,
  FDRID       NUMBER(10)                            NULL,
  ARCHID      NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  CLFID       NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL,
  TITLE       VARCHAR2(128 CHAR)                    NULL,
  AUTHOR      VARCHAR2(64 CHAR)                     NULL,
  KEYWORDS    VARCHAR2(254 CHAR)                    NULL,
  STAT        NUMBER(10)                            NULL,
  REFCOUNT    NUMBER(10)                            NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL,
  ACCRID      NUMBER(10)                            NULL,
  ACCDATE     DATE                                  NULL,
  ACCCOUNT    NUMBER(10)                            NULL,
  TIMESTAMP   DATE                                  NULL
);


CREATE TABLE IDOCMACRO
(
  ID        NUMBER(10)                              NULL,
  NAME      VARCHAR2(32 CHAR)                       NULL,
  TYPE      NUMBER(10)                              NULL,
  LANGUAGE  NUMBER(10)                              NULL,
  TEXT      CLOB                                    NULL,
  REMARKS   VARCHAR2(254 CHAR)                      NULL,
  CRTRID    NUMBER(10)                              NULL,
  CRTNDATE  DATE                                    NULL,
  UPDRID    NUMBER(10)                              NULL,
  UPDDATE   DATE                                    NULL
);


CREATE TABLE IDOCNEXTID
(
  TYPE  NUMBER(10)                                  NULL,
  ID    NUMBER(10)                                  NULL
);


CREATE TABLE IDOCPAGEANNS
(
  ID        NUMBER(10)                              NULL,
  DATA      CLOB                                    NULL,
  CRTRID    NUMBER(10)                              NULL,
  CRTNDATE  DATE                                    NULL,
  UPDRID    NUMBER(10)                              NULL,
  UPDDATE   DATE                                    NULL
);


CREATE TABLE IDOCPICT
(
  ID        NUMBER(10)                              NULL,
  NAME      VARCHAR2(32 CHAR)                       NULL,
  TYPE      NUMBER(10)                              NULL,
  DATA      BLOB                                    NULL,
  REMARKS   VARCHAR2(254 CHAR)                      NULL,
  CRTRID    NUMBER(10)                              NULL,
  CRTNDATE  DATE                                    NULL,
  UPDRID    NUMBER(10)                              NULL,
  UPDDATE   DATE                                    NULL
);


CREATE TABLE IDOCPREFFMT
(
  ARCHID   NUMBER(10)                               NULL,
  FMTTYPE  NUMBER(10)                               NULL,
  USERID   NUMBER(10)                               NULL,
  FMTID    NUMBER(10)                               NULL
);


CREATE TABLE IDOCPREFWFMT
(
  ARCHID   NUMBER(10)                               NULL,
  FMTTYPE  NUMBER(10)                               NULL,
  USERID   NUMBER(10)                               NULL,
  FMTID    NUMBER(10)                               NULL
);


CREATE TABLE IDOCRPT
(
  ID          NUMBER(10)                            NULL,
  NAME        VARCHAR2(32 CHAR)                     NULL,
  ARCHID      NUMBER(10)                            NULL,
  EDITTYPE    NUMBER(10)                            NULL,
  DATA        BLOB                                  NULL,
  REMARKS     VARCHAR2(254 CHAR)                    NULL,
  ACCESSTYPE  NUMBER(10)                            NULL,
  ACSID       NUMBER(10)                            NULL,
  CRTRID      NUMBER(10)                            NULL,
  CRTNDATE    DATE                                  NULL,
  UPDRID      NUMBER(10)                            NULL,
  UPDDATE     DATE                                  NULL
);


CREATE TABLE IDOCRPTAUXTBL
(
  FDRID  NUMBER(10)                                 NULL
);


CREATE TABLE IDOCSRVSTATEDET
(
  ID         NUMBER(10)                             NULL,
  SESSIONID  NUMBER(10)                             NULL,
  PARENTID   NUMBER(10)                             NULL,
  INFO       CLOB                                   NULL
);


CREATE TABLE IDOCSRVSTATEHDR
(
  ID         NUMBER(10)                             NULL,
  PRODID     NUMBER(10)                             NULL,
  APPID      NUMBER(10)                             NULL,
  USERID     NUMBER(10)                             NULL,
  ALIASID    NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL,
  RANDOM     NUMBER(10)                             NULL,
  INFO       CLOB                                   NULL
);


CREATE TABLE IDOCSSNEXTID
(
  ID  NUMBER(10)                                    NULL
);


CREATE TABLE IDOCVTBLCTLG
(
  ID        NUMBER(10)                              NULL,
  NAME      VARCHAR2(64 CHAR)                       NULL,
  BTBLID    NUMBER(10)                              NULL,
  TYPE      NUMBER(10)                              NULL,
  INFO      CLOB                                    NULL,
  FLAGS     NUMBER(10)                              NULL,
  REMARKS   VARCHAR2(254 CHAR)                      NULL,
  CRTRID    NUMBER(10)                              NULL,
  CRTNDATE  DATE                                    NULL,
  UPDRID    NUMBER(10)                              NULL,
  UPDDATE   DATE                                    NULL
);


CREATE TABLE IDOCWMACRO
(
  ID        NUMBER(10)                              NULL,
  NAME      VARCHAR2(32 CHAR)                       NULL,
  TEXT      CLOB                                    NULL,
  REMARKS   VARCHAR2(254 CHAR)                      NULL,
  CRTRID    NUMBER(10)                              NULL,
  CRTNDATE  DATE                                    NULL,
  UPDRID    NUMBER(10)                              NULL,
  UPDDATE   DATE                                    NULL
);


CREATE TABLE IDOCXNEXTID
(
  TYPE      NUMBER(10)                              NULL,
  PARENTID  NUMBER(10)                              NULL,
  ID        NUMBER(10)                              NULL
);


CREATE TABLE ITDSMSNEXTID
(
  CID  NUMBER(10)                                   NULL
);


CREATE TABLE ITDSMSSESS
(
  CID      NUMBER(10)                               NULL,
  CAPPID   VARCHAR2(64 CHAR)                        NULL,
  CUSERID  NUMBER(10)                               NULL,
  CCRTTS   DATE                                     NULL
);


CREATE TABLE IUSERCURRCNT
(
  ID         NUMBER(10)                             NULL,
  USERID     NUMBER(10)                             NULL,
  PRODID     NUMBER(10)                             NULL,
  APPID      NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL
);


CREATE TABLE IUSERDATA
(
  ID              NUMBER                            NULL,
  CARGO           VARCHAR2(256 BYTE)                NULL,
  TFNO_MOVIL      VARCHAR2(16 BYTE)                 NULL,
  ID_CERTIFICADO  VARCHAR2(256 BYTE)                NULL,
  EMAIL           VARCHAR2(256 BYTE)                NULL,
  NOMBRE          VARCHAR2(256 BYTE)                NULL,
  APELLIDOS       VARCHAR2(256 BYTE)                NULL
);


CREATE TABLE IUSERDEPTHDR
(
  ID        NUMBER(10)                              NULL,
  NAME      VARCHAR2(128 CHAR)                      NULL,
  PARENTID  NUMBER(10)                              NULL,
  MGRID     NUMBER(10)                              NULL,
  TYPE      NUMBER(10)                              NULL,
  REMARKS   VARCHAR2(254 CHAR)                      NULL,
  CRTRID    NUMBER(10)                              NULL,
  CRTNDATE  DATE                                    NULL,
  UPDRID    NUMBER(10)                              NULL,
  UPDDATE   DATE                                    NULL,
  IDORG     NUMBER(10)                              NULL
);


CREATE TABLE IUSERGENPERMS
(
  DSTTYPE  NUMBER(10)                               NULL,
  DSTID    NUMBER(10)                               NULL,
  PRODID   NUMBER(10)                               NULL,
  PERMS    NUMBER(10)                               NULL
);


CREATE TABLE IUSERGROUPHDR
(
  ID        NUMBER(10)                              NULL,
  NAME      VARCHAR2(32 CHAR)                       NULL,
  MGRID     NUMBER(10)                              NULL,
  TYPE      NUMBER(10)                              NULL,
  REMARKS   VARCHAR2(254 CHAR)                      NULL,
  CRTRID    NUMBER(10)                              NULL,
  CRTNDATE  DATE                                    NULL,
  UPDRID    NUMBER(10)                              NULL,
  UPDDATE   DATE                                    NULL
);


CREATE TABLE IUSERGROUPUSER
(
  GROUPID  NUMBER(10)                               NULL,
  USERID   NUMBER(10)                               NULL
);


CREATE TABLE IUSERLDAPGRPHDR
(
  ID            NUMBER(10)                          NULL,
  LDAPGUID      VARCHAR2(36 CHAR)                   NULL,
  LDAPFULLNAME  VARCHAR2(254 CHAR)                  NULL,
  TYPE          NUMBER(10)                          NULL
);


CREATE TABLE IUSERLDAPUSERHDR
(
  ID            NUMBER(10)                          NULL,
  LDAPGUID      VARCHAR2(36 CHAR)                   NULL,
  LDAPFULLNAME  VARCHAR2(254 CHAR)                  NULL
);


CREATE TABLE IUSERLICENCES
(
  INFO  CLOB                                        NULL
);


CREATE TABLE IUSERNEXTID
(
  TYPE  NUMBER(10)                                  NULL,
  ID    NUMBER(10)                                  NULL
);


CREATE TABLE IUSEROBJHDR
(
  ID         NUMBER(10)                             NULL,
  PRODID     NUMBER(10)                             NULL,
  TYPE       NUMBER(10)                             NULL,
  EXTID1     NUMBER(10)                             NULL,
  EXTID2     NUMBER(10)                             NULL,
  EXTID3     NUMBER(10)                             NULL,
  OWNERTYPE  NUMBER(10)                             NULL,
  OWNERID    NUMBER(10)                             NULL,
  CRTRID     NUMBER(10)                             NULL,
  CRTNDATE   DATE                                   NULL,
  UPDRID     NUMBER(10)                             NULL,
  UPDDATE    DATE                                   NULL
);


CREATE TABLE IUSEROBJPERM
(
  DSTTYPE  NUMBER(10)                               NULL,
  DSTID    NUMBER(10)                               NULL,
  OBJID    NUMBER(10)                               NULL,
  APERM    NUMBER(10)                               NULL
);


CREATE TABLE IUSERREMUSER
(
  ID       NUMBER(10)                               NULL,
  NAME     VARCHAR2(32 CHAR)                        NULL,
  REMARKS  VARCHAR2(254 CHAR)                       NULL,
  REMDATE  DATE                                     NULL
);


CREATE TABLE IUSERUSERHDR
(
  ID            NUMBER(10)                          NULL,
  NAME          VARCHAR2(32 CHAR)                   NULL,
  PASSWORD      VARCHAR2(68 CHAR)                   NULL,
  DEPTID        NUMBER(10)                          NULL,
  FLAGS         NUMBER(10)                          NULL,
  STAT          NUMBER(10)                          NULL,
  NUMBADCNTS    NUMBER(10)                          NULL,
  REMARKS       VARCHAR2(254 CHAR)                  NULL,
  CRTRID        NUMBER(10)                          NULL,
  CRTNDATE      DATE                            DEFAULT sysdate                   NULL,
  UPDRID        NUMBER(10)                          NULL,
  UPDDATE       DATE                                NULL,
  PWDLASTUPDTS  NUMBER                              NULL,
  PWDMBC        VARCHAR2(1 CHAR)                    NULL,
  PWDVPCHECK    VARCHAR2(1 CHAR)                    NULL
);


CREATE TABLE IUSERUSERHDR_WS
(
  NAME           VARCHAR2(32 CHAR)                  NULL,
  SIGN_REQUIRED  NUMBER(1)                          NULL,
  ID             VARCHAR2(32 CHAR)                  NULL
);


CREATE TABLE IUSERUSERMAP
(
  SRCDBID    NUMBER(10)                             NULL,
  SRCUSERID  NUMBER(10)                             NULL,
  DSTDBID    NUMBER(10)                             NULL,
  DSTUSERID  NUMBER(10)                             NULL
);


CREATE TABLE IUSERUSERPWDS
(
  USERID    NUMBER(10)                              NULL,
  PASSWORD  VARCHAR2(68 CHAR)                       NULL,
  UPDRID    NUMBER(10)                              NULL,
  UPDDATE   DATE                                    NULL
);


CREATE TABLE IUSERUSERSYS
(
  MAXBADCNTS    NUMBER(10)                          NULL,
  PWDVP         NUMBER(20,4)                        NULL,
  PWDMBC        VARCHAR2(1 CHAR)                    NULL,
  PWDMINLEN     NUMBER(5)                           NULL,
  PWDEXPIREDAP  NUMBER                              NULL,
  NUMPWDLOCK    NUMBER(5)                           NULL,
  FLAGS         NUMBER(10)                          NULL,
  UPDRID        NUMBER(10)                          NULL,
  UPDDATE       DATE                                NULL
);


CREATE TABLE IUSERUSERTYPE
(
  USERID  NUMBER(10)                                NULL,
  PRODID  NUMBER(10)                                NULL,
  TYPE    NUMBER(10)                                NULL
);


CREATE TABLE IVOLACTDIR
(
  VOLID     NUMBER(10)                              NULL,
  ACTDIR    NUMBER(10)                              NULL,
  NUMFILES  NUMBER(10)                              NULL
);


CREATE TABLE IVOLARCHLIST
(
  ARCHID  NUMBER(10)                                NULL,
  LISTID  NUMBER(10)                                NULL
);


CREATE TABLE IVOLFILEFTS
(
  ID         NUMBER(10)                             NULL,
  EXTID1     NUMBER(10)                             NULL,
  EXTID2     NUMBER(10)                             NULL,
  EXTID3     NUMBER(10)                             NULL,
  EXTID4     NUMBER(10)                             NULL,
  PATH       VARCHAR2(254 CHAR)                     NULL,
  TIMESTAMP  DATE                                   NULL
);


CREATE TABLE IVOLFILEHDR
(
  ID         NUMBER(10)                             NULL,
  VOLID      NUMBER(10)                             NULL,
  LOC        VARCHAR2(254 CHAR)                     NULL,
  EXTID1     NUMBER(10)                             NULL,
  EXTID2     NUMBER(10)                             NULL,
  EXTID3     NUMBER(10)                             NULL,
  FLAGS      NUMBER(10)                             NULL,
  STAT       NUMBER(10)                             NULL,
  TIMESTAMP  DATE                                   NULL,
  FILESIZE   NUMBER                                 NULL
);


CREATE TABLE IVOLLISTHDR
(
  ID        NUMBER(10)                              NULL,
  NAME      VARCHAR2(32 CHAR)                       NULL,
  FLAGS     NUMBER(10)                              NULL,
  STAT      NUMBER(10)                              NULL,
  REMARKS   VARCHAR2(254 CHAR)                      NULL,
  CRTRID    NUMBER(10)                              NULL,
  CRTNDATE  DATE                                    NULL,
  UPDRID    NUMBER(10)                              NULL,
  UPDDATE   DATE                                    NULL
);


CREATE TABLE IVOLLISTVOL
(
  LISTID     NUMBER(10)                             NULL,
  VOLID      NUMBER(10)                             NULL,
  SORTORDER  NUMBER(10)                             NULL
);


CREATE TABLE IVOLNEXTID
(
  TYPE  NUMBER(10)                                  NULL,
  ID    NUMBER(10)                                  NULL
);


CREATE TABLE IVOLREPHDR
(
  ID        NUMBER(10)                              NULL,
  NAME      VARCHAR2(32 CHAR)                       NULL,
  TYPE      NUMBER(10)                              NULL,
  INFO      CLOB                                    NULL,
  STAT      NUMBER(10)                              NULL,
  REMARKS   VARCHAR2(254 CHAR)                      NULL,
  CRTRID    NUMBER(10)                              NULL,
  CRTNDATE  DATE                                    NULL,
  UPDRID    NUMBER(10)                              NULL,
  UPDDATE   DATE                                    NULL
);


CREATE TABLE IVOLVOLHDR
(
  ID        NUMBER(10)                              NULL,
  NAME      VARCHAR2(32 CHAR)                       NULL,
  REPID     NUMBER(10)                              NULL,
  INFO      CLOB                                    NULL,
  ITEMP     NUMBER(10)                              NULL,
  ACTSIZE   VARCHAR2(32 CHAR)                       NULL,
  NUMFILES  NUMBER(10)                              NULL,
  STAT      NUMBER(10)                              NULL,
  REMARKS   VARCHAR2(254 CHAR)                      NULL,
  CRTRID    NUMBER(10)                              NULL,
  CRTNDATE  DATE                                    NULL,
  UPDRID    NUMBER(10)                              NULL,
  UPDDATE   DATE                                    NULL
);


CREATE TABLE IVOLVOLTBL
(
  LOCID    VARCHAR2(32 CHAR)                        NULL,
  EXTID1   NUMBER(10)                               NULL,
  EXTID2   NUMBER(10)                               NULL,
  EXTID3   NUMBER(10)                               NULL,
  EXTID4   NUMBER(10)                               NULL,
  FILEEXT  VARCHAR2(10 CHAR)                        NULL,
  FILEVAL  BLOB                                     NULL
);


CREATE TABLE QRTZ_BLOB_TRIGGERS
(
  TRIGGER_NAME   VARCHAR2(200 BYTE)                 NULL,
  TRIGGER_GROUP  VARCHAR2(200 BYTE)                 NULL,
  BLOB_DATA      BLOB                               NULL
);


CREATE TABLE QRTZ_CALENDARS
(
  CALENDAR_NAME  VARCHAR2(200 BYTE)                 NULL,
  CALENDAR       BLOB                               NULL
);


CREATE TABLE QRTZ_CRON_TRIGGERS
(
  TRIGGER_NAME     VARCHAR2(200 BYTE)               NULL,
  TRIGGER_GROUP    VARCHAR2(200 BYTE)               NULL,
  CRON_EXPRESSION  VARCHAR2(120 BYTE)               NULL,
  TIME_ZONE_ID     VARCHAR2(80 BYTE)                NULL
);


CREATE TABLE QRTZ_FIRED_TRIGGERS
(
  ENTRY_ID           VARCHAR2(95 BYTE)              NULL,
  TRIGGER_NAME       VARCHAR2(200 BYTE)             NULL,
  TRIGGER_GROUP      VARCHAR2(200 BYTE)             NULL,
  IS_VOLATILE        VARCHAR2(1 BYTE)               NULL,
  INSTANCE_NAME      VARCHAR2(200 BYTE)             NULL,
  FIRED_TIME         NUMBER(13)                     NULL,
  PRIORITY           NUMBER(13)                     NULL,
  STATE              VARCHAR2(16 BYTE)              NULL,
  JOB_NAME           VARCHAR2(200 BYTE)             NULL,
  JOB_GROUP          VARCHAR2(200 BYTE)             NULL,
  IS_STATEFUL        VARCHAR2(1 BYTE)               NULL,
  REQUESTS_RECOVERY  VARCHAR2(1 BYTE)               NULL
);


CREATE TABLE QRTZ_JOB_DETAILS
(
  JOB_NAME           VARCHAR2(200 BYTE)             NULL,
  JOB_GROUP          VARCHAR2(200 BYTE)             NULL,
  DESCRIPTION        VARCHAR2(250 BYTE)             NULL,
  JOB_CLASS_NAME     VARCHAR2(250 BYTE)             NULL,
  IS_DURABLE         VARCHAR2(1 BYTE)               NULL,
  IS_VOLATILE        VARCHAR2(1 BYTE)               NULL,
  IS_STATEFUL        VARCHAR2(1 BYTE)               NULL,
  REQUESTS_RECOVERY  VARCHAR2(1 BYTE)               NULL,
  JOB_DATA           BLOB                           NULL
);


CREATE TABLE QRTZ_JOB_LISTENERS
(
  JOB_NAME      VARCHAR2(200 BYTE)                  NULL,
  JOB_GROUP     VARCHAR2(200 BYTE)                  NULL,
  JOB_LISTENER  VARCHAR2(200 BYTE)                  NULL
);


CREATE TABLE QRTZ_LOCKS
(
  LOCK_NAME  VARCHAR2(40 BYTE)                      NULL
);


CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
  TRIGGER_GROUP  VARCHAR2(200 BYTE)                 NULL
);


CREATE TABLE QRTZ_SCHEDULER_STATE
(
  INSTANCE_NAME      VARCHAR2(200 BYTE)             NULL,
  LAST_CHECKIN_TIME  NUMBER(13)                     NULL,
  CHECKIN_INTERVAL   NUMBER(13)                     NULL
);


CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
  TRIGGER_NAME     VARCHAR2(200 BYTE)               NULL,
  TRIGGER_GROUP    VARCHAR2(200 BYTE)               NULL,
  REPEAT_COUNT     NUMBER(7)                        NULL,
  REPEAT_INTERVAL  NUMBER(12)                       NULL,
  TIMES_TRIGGERED  NUMBER(10)                       NULL
);


CREATE TABLE QRTZ_TRIGGERS
(
  TRIGGER_NAME    VARCHAR2(200 BYTE)                NULL,
  TRIGGER_GROUP   VARCHAR2(200 BYTE)                NULL,
  JOB_NAME        VARCHAR2(200 BYTE)                NULL,
  JOB_GROUP       VARCHAR2(200 BYTE)                NULL,
  IS_VOLATILE     VARCHAR2(1 BYTE)                  NULL,
  DESCRIPTION     VARCHAR2(250 BYTE)                NULL,
  NEXT_FIRE_TIME  NUMBER(13)                        NULL,
  PREV_FIRE_TIME  NUMBER(13)                        NULL,
  PRIORITY        NUMBER(13)                        NULL,
  TRIGGER_STATE   VARCHAR2(16 BYTE)                 NULL,
  TRIGGER_TYPE    VARCHAR2(8 BYTE)                  NULL,
  START_TIME      NUMBER(13)                        NULL,
  END_TIME        NUMBER(13)                        NULL,
  CALENDAR_NAME   VARCHAR2(200 BYTE)                NULL,
  MISFIRE_INSTR   NUMBER(2)                         NULL,
  JOB_DATA        BLOB                              NULL
);


CREATE TABLE QRTZ_TRIGGER_LISTENERS
(
  TRIGGER_NAME      VARCHAR2(200 BYTE)              NULL,
  TRIGGER_GROUP     VARCHAR2(200 BYTE)              NULL,
  TRIGGER_LISTENER  VARCHAR2(200 BYTE)              NULL
);


CREATE TABLE SCR_ACTWS
(
  IDPUESTO  NUMBER(10)                              NULL,
  IDCAMBIO  NUMBER(10)                              NULL
);


CREATE TABLE SCR_ADDRESS
(
  ID         NUMBER(10)                             NULL,
  ID_PERSON  NUMBER(10)                             NULL,
  TYPE       NUMBER(10)                             NULL
);


CREATE TABLE SCR_ADDRTEL
(
  ID          NUMBER(10)                            NULL,
  ADDRESS     VARCHAR2(160 CHAR)                    NULL,
  TYPE        NUMBER(10)                            NULL,
  PREFERENCE  NUMBER(10)                            NULL
);


CREATE TABLE SCR_ATTACHMENT
(
  ID             NUMBER(10)                         NULL,
  BOOKID         NUMBER(10)                         NULL,
  FOLDERID       NUMBER(10)                         NULL,
  PAGEID         NUMBER(10)                         NULL,
  COMMENTS       VARCHAR2(50 CHAR)                  NULL,
  MIME_TYPE      VARCHAR2(20 CHAR)                  NULL,
  NAME           VARCHAR2(80 CHAR)                  NULL,
  CODE_NAME      VARCHAR2(50 CHAR)                  NULL,
  VALIDITY_TYPE  NUMBER(10)                         NULL,
  DOCUMENT_TYPE  NUMBER(10)                         NULL
);


CREATE TABLE SCR_ATTACHMENT_DOCUMENT_TYPE
(
  ID    NUMBER(10)                                  NULL,
  NAME  VARCHAR2(250 CHAR)                          NULL,
  CODE  VARCHAR2(2 CHAR)                            NULL
);


CREATE TABLE SCR_ATTACHMENT_SIGN_INFO
(
  ID                    NUMBER(10)                  NULL,
  ID_ATTACHMENT         NUMBER(10)                  NULL,
  ID_ATTACHMENT_SIGNED  NUMBER(10)                  NULL,
  CERT                  BLOB                        NULL,
  SIGNATURE             BLOB                        NULL,
  SIGNATURE_ALG         VARCHAR2(2 CHAR)            NULL,
  SIGNATURE_FORMAT      VARCHAR2(2 CHAR)            NULL,
  TIME_SIGNATURE        BLOB                        NULL,
  OCSP_VALIDATION       BLOB                        NULL,
  HASH_ALG              VARCHAR2(2 CHAR)            NULL,
  HASH                  BLOB                        NULL
);


CREATE TABLE SCR_ATTACHMENT_VALIDITY_TYPE
(
  ID    NUMBER(10)                                  NULL,
  NAME  VARCHAR2(250 CHAR)                          NULL,
  CODE  VARCHAR2(2 CHAR)                            NULL
);


CREATE TABLE SCR_BOOKADMIN
(
  IDBOOK  NUMBER(10)                                NULL,
  IDUSER  NUMBER(10)                                NULL
);


CREATE TABLE SCR_BOOKASOC
(
  ID_BOOK     NUMBER(10)                            NULL,
  ID_BOOKSEC  NUMBER(10)                            NULL,
  TYPE        NUMBER(10)                            NULL
);


CREATE TABLE SCR_BOOKCONFIG
(
  BOOKID   NUMBER(10)                               NULL,
  OPTIONS  CLOB                                     NULL
);


CREATE TABLE SCR_BOOKOFIC
(
  ID          NUMBER(10)                            NULL,
  ID_BOOK     NUMBER(10)                            NULL,
  ID_OFIC     NUMBER(10)                            NULL,
  NUMERATION  NUMBER(10)                            NULL
);


CREATE TABLE SCR_BOOKREPOSITORY
(
  BOOKID         NUMBER(10)                         NULL,
  CONFIGURATION  CLOB                               NULL
);


CREATE TABLE SCR_BOOKSLANG
(
  ID        NUMBER(10)                              NULL,
  LANGUAGE  NUMBER(10)                              NULL,
  NAME      VARCHAR2(250 CHAR)                      NULL
);


CREATE TABLE SCR_BOOKTYPECONFIG
(
  BOOKTYPE  NUMBER(10)                              NULL,
  OPTIONS   CLOB                                    NULL
);


CREATE TABLE SCR_CA
(
  ID             NUMBER(10)                         NULL,
  CODE           VARCHAR2(16 CHAR)                  NULL,
  MATTER         VARCHAR2(250 CHAR)                 NULL,
  FOR_EREG       NUMBER(10)                         NULL,
  FOR_SREG       NUMBER(10)                         NULL,
  ALL_OFICS      NUMBER(10)                         NULL,
  ID_ARCH        NUMBER(10)                         NULL,
  CREATION_DATE  DATE                               NULL,
  DISABLE_DATE   DATE                               NULL,
  ENABLED        NUMBER(10)                         NULL,
  ID_ORG         NUMBER(10)                         NULL
);


CREATE TABLE SCR_CAADM
(
  ID      NUMBER(10)                                NULL,
  IDUSER  NUMBER(10)                                NULL,
  IDCA    NUMBER(10)                                NULL
);


CREATE TABLE SCR_CAAUX
(
  ID         NUMBER(10)                             NULL,
  ID_MATTER  NUMBER(10)                             NULL,
  DATOS_AUX  CLOB                                   NULL
);


CREATE TABLE SCR_CADOCS
(
  ID           NUMBER(10)                           NULL,
  ID_MATTER    NUMBER(10)                           NULL,
  DESCRIPTION  VARCHAR2(32 CHAR)                    NULL,
  MANDATORY    NUMBER(10)                           NULL
);


CREATE TABLE SCR_CALANG
(
  ID        NUMBER(10)                              NULL,
  LANGUAGE  NUMBER(10)                              NULL,
  NAME      VARCHAR2(250 CHAR)                      NULL
);


CREATE TABLE SCR_CAOFIC
(
  ID         NUMBER(10)                             NULL,
  ID_MATTER  NUMBER(10)                             NULL,
  ID_OFIC    NUMBER(10)                             NULL
);


CREATE TABLE SCR_CCAA
(
  ID       VARCHAR2(2 CHAR)                         NULL,
  NAME     VARCHAR2(50 CHAR)                        NULL,
  TMSTAMP  DATE                                     NULL
);


CREATE TABLE SCR_CITIES
(
  ID       NUMBER(10)                               NULL,
  TMSTAMP  DATE                                     NULL,
  CODE     VARCHAR2(16 CHAR)                        NULL,
  NAME     VARCHAR2(40 CHAR)                        NULL,
  ID_PROV  NUMBER(10)                               NULL
);


CREATE TABLE SCR_CITIESEXREG
(
  ID       NUMBER(10)                               NULL,
  CODE     VARCHAR2(5 CHAR)                         NULL,
  NAME     VARCHAR2(80 CHAR)                        NULL,
  ID_CITY  NUMBER(10)                               NULL
);


CREATE TABLE SCR_CNTCENTRAL
(
  AN       NUMBER(10)                               NULL,
  NUM_REG  NUMBER(10)                               NULL,
  ID_ARCH  NUMBER(10)                               NULL
);


CREATE TABLE SCR_CNTLOCAL
(
  AN       NUMBER(10)                               NULL,
  PUESTO   NUMBER(10)                               NULL,
  NUM_REG  NUMBER(10)                               NULL,
  ID_ARCH  NUMBER(10)                               NULL
);


CREATE TABLE SCR_CNTOFICINA
(
  AN       NUMBER(10)                               NULL,
  OFICINA  NUMBER(10)                               NULL,
  NUM_REG  NUMBER(10)                               NULL,
  ID_ARCH  NUMBER(10)                               NULL
);


CREATE TABLE SCR_CNTORDINAL
(
  CONTADOR  NUMBER(10)                              NULL,
  HORA      VARCHAR2(4 CHAR)                        NULL
);


CREATE TABLE SCR_CNTREFCIERRE
(
  TIPO      VARCHAR2(3 CHAR)                        NULL,
  CONTADOR  NUMBER(10)                              NULL
);


CREATE TABLE SCR_CONFIGURATION
(
  ID       NUMBER(10)                               NULL,
  VERSION  VARCHAR2(10 CHAR)                        NULL,
  OPTIONS  CLOB                                     NULL
);


CREATE TABLE SCR_CONTADOR
(
  TABLAID   VARCHAR2(30 CHAR)                       NULL,
  CONTADOR  NUMBER(10)                              NULL
);


CREATE TABLE SCR_COUNTRY
(
  ID       NUMBER(10)                               NULL,
  TMSTAMP  DATE                                     NULL,
  CODE     VARCHAR2(16 CHAR)                        NULL,
  NAME     VARCHAR2(50 CHAR)                        NULL
);


CREATE TABLE SCR_DELTAS
(
  IDCAMBIO    NUMBER(10)                            NULL,
  TABLA       VARCHAR2(20 CHAR)                     NULL,
  IDREGISTRO  NUMBER(10)                            NULL,
  TIPOCAMBIO  NUMBER(10)                            NULL
);


CREATE TABLE SCR_DIROFIC
(
  ID_OFIC    NUMBER(10)                             NULL,
  ADDRESS    VARCHAR2(255 CHAR)                     NULL,
  CITY       VARCHAR2(100 CHAR)                     NULL,
  ZIP        VARCHAR2(10 CHAR)                      NULL,
  COUNTRY    VARCHAR2(100 CHAR)                     NULL,
  TELEPHONE  VARCHAR2(160 CHAR)                     NULL,
  FAX        VARCHAR2(160 CHAR)                     NULL,
  EMAIL      VARCHAR2(255 CHAR)                     NULL,
  SIGNER     VARCHAR2(160 CHAR)                     NULL
);


CREATE TABLE SCR_DIRORGS
(
  ID_ORGS    NUMBER(10)                             NULL,
  ADDRESS    VARCHAR2(255 CHAR)                     NULL,
  CITY       VARCHAR2(100 CHAR)                     NULL,
  ZIP        VARCHAR2(10 CHAR)                      NULL,
  COUNTRY    VARCHAR2(100 CHAR)                     NULL,
  TELEPHONE  VARCHAR2(160 CHAR)                     NULL,
  FAX        VARCHAR2(160 CHAR)                     NULL,
  EMAIL      VARCHAR2(255 CHAR)                     NULL
);


CREATE TABLE SCR_DISTACCEPT
(
  BOOKID    NUMBER(10)                              NULL,
  REGID     NUMBER(10)                              NULL,
  OFFICEID  NUMBER(10)                              NULL,
  DISTDATE  DATE                                    NULL,
  ACCDATE   DATE                                    NULL,
  ACCUSER   VARCHAR2(80 CHAR)                       NULL,
  STATE     NUMBER(10)                              NULL,
  DATA      CLOB                                    NULL
);


CREATE TABLE SCR_DISTLIST
(
  ID         NUMBER(10)                             NULL,
  ID_ORGS    NUMBER(10)                             NULL,
  TYPE_DEST  NUMBER(10)                             NULL,
  ID_DEST    NUMBER(10)                             NULL
);


CREATE TABLE SCR_DISTREG
(
  ID          NUMBER(10)                            NULL,
  ID_ARCH     NUMBER(10)                            NULL,
  ID_FDR      NUMBER(10)                            NULL,
  DIST_DATE   DATE                                  NULL,
  TYPE_ORIG   NUMBER(10)                            NULL,
  ID_ORIG     NUMBER(10)                            NULL,
  TYPE_DEST   NUMBER(10)                            NULL,
  ID_DEST     NUMBER(10)                            NULL,
  STATE       NUMBER(10)                            NULL,
  STATE_DATE  DATE                                  NULL,
  MESSAGE     VARCHAR2(250 CHAR)                    NULL
);


CREATE TABLE SCR_DISTREGSTATE
(
  ID          NUMBER(10)                            NULL,
  ID_DIST     NUMBER(10)                            NULL,
  STATE       NUMBER(10)                            NULL,
  STATE_DATE  DATE                                  NULL,
  USERNAME    VARCHAR2(32 CHAR)                     NULL
);


CREATE TABLE SCR_DOCLOCATOR
(
  BOOKID    NUMBER(10)                              NULL,
  FOLDERID  NUMBER(10)                              NULL,
  PAGEID    NUMBER(10)                              NULL,
  LOCATOR   VARCHAR2(128 CHAR)                      NULL
);


CREATE TABLE SCR_DOM
(
  ID          NUMBER(10)                            NULL,
  ADDRESS     VARCHAR2(160 CHAR)                    NULL,
  CITY        VARCHAR2(40 CHAR)                     NULL,
  ZIP         VARCHAR2(5 CHAR)                      NULL,
  COUNTRY     VARCHAR2(30 CHAR)                     NULL,
  PREFERENCE  NUMBER(10)                            NULL,
  PAIS        VARCHAR2(50 CHAR)                     NULL
);


CREATE TABLE SCR_ENTITYREG
(
  ID       NUMBER(10)                               NULL,
  CODE     VARCHAR2(21 CHAR)                        NULL,
  NAME     VARCHAR2(80 CHAR)                        NULL,
  ID_OFIC  NUMBER(10)                               NULL
);


CREATE TABLE SCR_ENTREGSND
(
  ID_ENTREG            NUMBER(10)                   NULL,
  TRANSPORT_TYPE       NUMBER(10)                   NULL,
  ENCODING_MSG         NUMBER(10)                   NULL,
  CERTIFICATE_ISSUER   VARCHAR2(255 CHAR)           NULL,
  CERTIFICATE_NSERIAL  VARCHAR2(255 CHAR)           NULL
);


CREATE TABLE SCR_EXPCNTS
(
  ID        NUMBER(10)                              NULL,
  ID_TASK   NUMBER(10)                              NULL,
  USERNAME  VARCHAR2(32 CHAR)                       NULL,
  PASSWORD  VARCHAR2(34 CHAR)                       NULL
);


CREATE TABLE SCR_EXPFILTERS
(
  ID       NUMBER(10)                               NULL,
  ID_TASK  NUMBER(10)                               NULL,
  FILTER   CLOB                                     NULL
);


CREATE TABLE SCR_EXPTASKS
(
  ID             NUMBER(10)                         NULL,
  ID_USER        NUMBER(10)                         NULL,
  DESCRIPTION    VARCHAR2(50 CHAR)                  NULL,
  ID_SOURCEARCH  NUMBER(10)                         NULL,
  ID_TARGETARCH  NUMBER(10)                         NULL,
  ID_TARGETDB    NUMBER(10)                         NULL,
  ID_FILTER      NUMBER(10)                         NULL,
  ID_CONNECTION  NUMBER(10)                         NULL,
  STATUS         NUMBER(10)                         NULL,
  DELETE_SOURCE  NUMBER(10)                         NULL,
  EXPLASTDATE    DATE                               NULL
);


CREATE TABLE SCR_EXREG
(
  ID               NUMBER(10)                       NULL,
  ID_ARCH          NUMBER(10)                       NULL,
  ID_FDR           NUMBER(10)                       NULL,
  ID_OFIC          NUMBER(10)                       NULL,
  TYPE_ORIG        NUMBER(10)                       NULL,
  EXCHANGE_DATE    DATE                             NULL,
  STATE            NUMBER(10)                       NULL,
  STATE_DATE       DATE                             NULL,
  ID_EXCHANGE_SIR  VARCHAR2(33 CHAR)                NULL,
  ID_EXCHANGE      NUMBER(10)                       NULL,
  USERNAME         VARCHAR2(32 CHAR)                NULL,
  CODE_TRAMUNIT    VARCHAR2(21 CHAR)                NULL,
  NAME_TRAMUNIT    VARCHAR2(80 CHAR)                NULL,
  CODE_ENTITY      VARCHAR2(21 CHAR)                NULL,
  NAME_ENTITY      VARCHAR2(80 CHAR)                NULL,
  COMMENTS         VARCHAR2(4000 CHAR)              NULL
);


CREATE TABLE SCR_EXREGSTATE
(
  ID          NUMBER(10)                            NULL,
  ID_EXREG    NUMBER(10)                            NULL,
  STATE       NUMBER(10)                            NULL,
  STATE_DATE  DATE                                  NULL,
  USERNAME    VARCHAR2(32 CHAR)                     NULL,
  COMMENTS    VARCHAR2(4000 CHAR)                   NULL
);


CREATE TABLE SCR_EXREG_IN
(
  ID               NUMBER(10)                       NULL,
  ID_ARCH          NUMBER(10)                       NULL,
  ID_FDR           NUMBER(10)                       NULL,
  ID_OFIC          NUMBER(10)                       NULL,
  EXCHANGE_DATE    DATE                             NULL,
  STATE            NUMBER(10)                       NULL,
  STATE_DATE       DATE                             NULL,
  ID_EXCHANGE_SIR  VARCHAR2(33 CHAR)                NULL,
  ID_EXCHANGE      NUMBER(10)                       NULL,
  USERNAME         VARCHAR2(32 CHAR)                NULL,
  CODE_TRAMUNIT    VARCHAR2(21 CHAR)                NULL,
  NAME_TRAMUNIT    VARCHAR2(80 CHAR)                NULL,
  CODE_ENTITY      VARCHAR2(21 CHAR)                NULL,
  NAME_ENTITY      VARCHAR2(80 CHAR)                NULL,
  NUM_REG_ORIG     VARCHAR2(20 CHAR)                NULL,
  CONTACTO_ORIG    VARCHAR2(160 CHAR)               NULL,
  COMMENTS         VARCHAR2(4000 CHAR)              NULL
);


CREATE TABLE SCR_FLDACCPERMS
(
  ARCHIVETYPE  NUMBER(10)                           NULL,
  IDFLD        NUMBER(10)                           NULL,
  USERPERMS    NUMBER(10)                           NULL,
  ADMINPERMS   NUMBER(10)                           NULL,
  SUSERPERMS   NUMBER(10)                           NULL
);


CREATE TABLE SCR_FLDPERMARCH
(
  IDARCHIVE   NUMBER(10)                            NULL,
  IDFLD       NUMBER(10)                            NULL,
  USERPERMS   NUMBER(10)                            NULL,
  ADMINPERMS  NUMBER(10)                            NULL,
  SUSERPERMS  NUMBER(10)                            NULL
);


CREATE TABLE SCR_FLDPERMUSER
(
  IDARCHIVE  NUMBER(10)                             NULL,
  IDFLD      NUMBER(10)                             NULL,
  IDUSER     NUMBER(10)                             NULL,
  PERMS      NUMBER(10)                             NULL
);


CREATE TABLE SCR_GENPERMS
(
  ID     NUMBER(10)                                 NULL,
  PERMS  NUMBER(10)                                 NULL
);


CREATE TABLE SCR_LASTCONNECTION
(
  IDUSER        NUMBER(10)                          NULL,
  CONNECT_DATE  DATE                                NULL
);


CREATE TABLE SCR_LASTREGISTER
(
  BOOKID  NUMBER(10)                                NULL,
  FDRID   NUMBER(10)                                NULL,
  USERID  NUMBER(10)                                NULL
);


CREATE TABLE SCR_LOCKITEMS
(
  OBJTYPE   NUMBER(10)                              NULL,
  OBJID     NUMBER(10)                              NULL,
  USERID    NUMBER(10)                              NULL,
  LOCKDATE  DATE                                    NULL
);


CREATE TABLE SCR_LOCKRELATIONS
(
  TYPEBOOK  NUMBER(10)                              NULL,
  TYPEREL   NUMBER(10)                              NULL,
  IDOFIC    NUMBER(10)                              NULL
);


CREATE TABLE SCR_MODIFREG
(
  ID          NUMBER(10)                            NULL,
  USR         VARCHAR2(32 CHAR)                     NULL,
  MODIF_DATE  DATE                                  NULL,
  ID_FLD      NUMBER(10)                            NULL,
  NUM_REG     VARCHAR2(20 CHAR)                     NULL,
  ID_ARCH     NUMBER(10)                            NULL,
  ID_OFIC     NUMBER(10)                            NULL,
  MODIF_TYPE  NUMBER(10)                            NULL
);


CREATE TABLE SCR_NACKRECVMSG
(
  ID          NUMBER(10)                            NULL,
  ID_MSG      NUMBER(10)                            NULL,
  ID_SENDMSG  NUMBER(10)                            NULL,
  REASON      VARCHAR2(254 CHAR)                    NULL
);


CREATE TABLE SCR_NACKRECVREG
(
  ID          NUMBER(10)                            NULL,
  ID_REG      NUMBER(10)                            NULL,
  ID_SENDMSG  NUMBER(10)                            NULL,
  REASON      VARCHAR2(254 CHAR)                    NULL
);


CREATE TABLE SCR_NACKSENDMSG
(
  ID         NUMBER(10)                             NULL,
  ID_MSG     NUMBER(10)                             NULL,
  ID_RCVMSG  NUMBER(10)                             NULL,
  REASON     VARCHAR2(254 CHAR)                     NULL
);


CREATE TABLE SCR_NACKSENDREG
(
  ID         NUMBER(10)                             NULL,
  ID_REG     NUMBER(10)                             NULL,
  ID_RCVMSG  NUMBER(10)                             NULL,
  REASON     VARCHAR2(254 CHAR)                     NULL
);


CREATE TABLE SCR_OFIC
(
  ID             NUMBER(10)                         NULL,
  CODE           VARCHAR2(10 CHAR)                  NULL,
  ACRON          VARCHAR2(12 CHAR)                  NULL,
  NAME           VARCHAR2(60 CHAR)                  NULL,
  CREATION_DATE  DATE                               NULL,
  DISABLE_DATE   DATE                               NULL,
  ID_ORGS        NUMBER(10)                         NULL,
  STAMP          VARCHAR2(240 CHAR)                 NULL,
  DEPTID         NUMBER(10)                         NULL,
  TYPE           NUMBER(10)                         NULL
);


CREATE TABLE SCR_OFICADM
(
  ID      NUMBER(10)                                NULL,
  IDUSER  NUMBER(10)                                NULL,
  IDOFIC  NUMBER(10)                                NULL
);


CREATE TABLE SCR_OFICLANG
(
  ID        NUMBER(10)                              NULL,
  LANGUAGE  NUMBER(10)                              NULL,
  NAME      VARCHAR2(32 CHAR)                       NULL
);


CREATE TABLE SCR_ORGS
(
  ID                  NUMBER(10)                    NULL,
  CODE                VARCHAR2(16 CHAR)             NULL,
  ID_FATHER           NUMBER(10)                    NULL,
  ACRON               VARCHAR2(12 CHAR)             NULL,
  NAME                VARCHAR2(250 CHAR)            NULL,
  CREATION_DATE       DATE                          NULL,
  DISABLE_DATE        DATE                          NULL,
  TYPE                NUMBER(10)                    NULL,
  ENABLED             NUMBER(10)                    NULL,
  CIF                 VARCHAR2(15 CHAR)             NULL,
  HIERARCHICAL_LEVEL  NUMBER(6)                     NULL,
  ADMIN_LEVEL         VARCHAR2(2 CHAR)              NULL,
  ENTITY_TYPE         VARCHAR2(2 CHAR)              NULL,
  UO_TYPE             VARCHAR2(3 CHAR)              NULL,
  ID_ROOT             NUMBER(10)                    NULL,
  ID_CCAA             VARCHAR2(2 CHAR)              NULL,
  ID_PROV             NUMBER(10)                    NULL
);


CREATE TABLE SCR_ORGSLANG
(
  ID        NUMBER(10)                              NULL,
  LANGUAGE  NUMBER(10)                              NULL,
  NAME      VARCHAR2(250 CHAR)                      NULL
);


CREATE TABLE SCR_PAGEINFO
(
  BOOKID       NUMBER(10)                           NULL,
  REGID        NUMBER(10)                           NULL,
  PAGEID       NUMBER(10)                           NULL,
  HASHVERSION  NUMBER(10)                           NULL,
  HASH         CLOB                                 NULL
);


CREATE TABLE SCR_PAGEREPOSITORY
(
  BOOKID       NUMBER(10)                           NULL,
  FDRID        NUMBER(10)                           NULL,
  PAGEID       NUMBER(10)                           NULL,
  DOCUID       VARCHAR2(50 CHAR)                    NULL,
  ID_PAGETYPE  NUMBER                           DEFAULT 0                         NULL,
  DELETEDATE   DATE                                 NULL,
  FLAG         NUMBER(3)                            NULL
);


CREATE TABLE SCR_PAGETYPE
(
  ID    NUMBER                                      NULL,
  NAME  VARCHAR2(100 CHAR)                          NULL
);


CREATE TABLE SCR_PFIS
(
  ID           NUMBER(10)                           NULL,
  TYPE_DOC     NUMBER(10)                           NULL,
  NIF          VARCHAR2(25 CHAR)                    NULL,
  FIRST_NAME   VARCHAR2(30 CHAR)                    NULL,
  SECOND_NAME  VARCHAR2(30 CHAR)                    NULL,
  SURNAME      VARCHAR2(100 CHAR)                   NULL
);


CREATE TABLE SCR_PINFO
(
  ID        NUMBER(10)                              NULL,
  OPTYPE    NUMBER(10)                              NULL,
  OFFICEID  NUMBER(10)                              NULL,
  USERNAME  VARCHAR2(32 CHAR)                       NULL,
  OPDATE    DATE                                    NULL
);


CREATE TABLE SCR_PJUR
(
  ID        NUMBER(10)                              NULL,
  TYPE_DOC  NUMBER(10)                              NULL,
  CIF       VARCHAR2(17 CHAR)                       NULL,
  NAME      VARCHAR2(250 CHAR)                      NULL
);


CREATE TABLE SCR_PRIVORGS
(
  ID      NUMBER(10)                                NULL,
  IDORGS  NUMBER(10)                                NULL,
  IDOFIC  NUMBER(10)                                NULL
);


CREATE TABLE SCR_PROCREG
(
  ID       NUMBER(10)                               NULL,
  ID_DIST  NUMBER(10)                               NULL,
  ID_PROC  NUMBER(10)                               NULL,
  TYPE     NUMBER(10)                               NULL
);


CREATE TABLE SCR_PROV
(
  ID       NUMBER(10)                               NULL,
  TMSTAMP  DATE                                     NULL,
  CODE     VARCHAR2(16 CHAR)                        NULL,
  NAME     VARCHAR2(30 CHAR)                        NULL,
  IDCCAA   VARCHAR2(2 CHAR)                         NULL
);


CREATE TABLE SCR_PROVEXREG
(
  ID       NUMBER(10)                               NULL,
  CODE     VARCHAR2(2 CHAR)                         NULL,
  NAME     VARCHAR2(80 CHAR)                        NULL,
  ID_PROV  NUMBER(10)                               NULL
);


CREATE TABLE SCR_RECVMSG
(
  ID           NUMBER(10)                           NULL,
  UID_MSG      VARCHAR2(40 CHAR)                    NULL,
  TYPE_MSG     NUMBER(10)                           NULL,
  FILE_NAME    VARCHAR2(40 CHAR)                    NULL,
  SENDER       NUMBER(10)                           NULL,
  DESTINATION  NUMBER(10)                           NULL,
  RCV_TYPE     NUMBER(10)                           NULL,
  RCV_DATE     DATE                                 NULL,
  RCV_STATE    NUMBER(10)                           NULL,
  ID_ACKMSG    NUMBER(10)                           NULL
);


CREATE TABLE SCR_RECVREG
(
  ID            NUMBER(10)                          NULL,
  ID_MSG        NUMBER(10)                          NULL,
  ID_ARCH       NUMBER(10)                          NULL,
  ID_FDR        NUMBER(10)                          NULL,
  REGORIG_CODE  VARCHAR2(16 CHAR)                   NULL,
  REGORIG_NUM   VARCHAR2(20 CHAR)                   NULL,
  REGORIG_DATE  DATE                                NULL,
  REGORIG_TYPE  NUMBER(10)                          NULL,
  ATTACH_PAGES  NUMBER(10)                          NULL,
  ATTACH_DOC    NUMBER(10)                          NULL,
  ID_ACKMSG     NUMBER(10)                          NULL,
  RCV_STATE     NUMBER(10)                          NULL,
  RCV_NUMREG    VARCHAR2(20 CHAR)                   NULL,
  RCV_DATEREG   DATE                                NULL
);


CREATE TABLE SCR_REGASOC
(
  ID           NUMBER(10)                           NULL,
  ID_ARCHPRIM  NUMBER(10)                           NULL,
  ID_FDRPRIM   NUMBER(10)                           NULL,
  ID_ARCHSEC   NUMBER(10)                           NULL,
  ID_FDRSEC    NUMBER(10)                           NULL
);


CREATE TABLE SCR_REGASOCEX
(
  ID_ASOC  NUMBER(10)                               NULL,
  TYPE     NUMBER(10)                               NULL
);


CREATE TABLE SCR_REGINT
(
  ID          NUMBER(10)                            NULL,
  ID_ARCH     NUMBER(10)                            NULL,
  ID_FDR      NUMBER(10)                            NULL,
  NAME        VARCHAR2(250 CHAR)                    NULL,
  ID_PERSON   NUMBER(10)                            NULL,
  ID_ADDRESS  NUMBER(10)                            NULL,
  ORD         NUMBER(10)                            NULL
);


CREATE TABLE SCR_REGPDOCS
(
  ID         NUMBER(10)                             NULL,
  ID_ARCH    NUMBER(10)                             NULL,
  ID_FDR     NUMBER(10)                             NULL,
  NAME_DOC   VARCHAR2(250 CHAR)                     NULL,
  PRESENTED  NUMBER(10)                             NULL
);


CREATE TABLE SCR_REGSTATE
(
  ID               NUMBER(10)                       NULL,
  IDARCHREG        NUMBER(10)                       NULL,
  STATE            NUMBER(10)                       NULL,
  CLOSEDATE        DATE                             NULL,
  CLOSEUSER        VARCHAR2(32 CHAR)                NULL,
  NUMERATION_TYPE  NUMBER(10)                       NULL,
  IMAGE_AUTH       NUMBER(10)                       NULL
);


CREATE TABLE SCR_RELATIONS
(
  TYPEBOOK  NUMBER(10)                              NULL,
  TYPEREL   NUMBER(10)                              NULL,
  RELYEAR   NUMBER(10)                              NULL,
  RELMONTH  NUMBER(10)                              NULL,
  RELDAY    NUMBER(10)                              NULL,
  IDOFIC    NUMBER(10)                              NULL,
  RELDATE   DATE                                    NULL,
  IDUNIT    NUMBER(10)                              NULL,
  NREL      NUMBER(10)                              NULL
);


CREATE TABLE SCR_REPOBOOKTYPE
(
  BOOK_TYPE    NUMBER(10)                           NULL,
  ID_REP_CONF  NUMBER(10)                           NULL
);


CREATE TABLE SCR_REPORTARCH
(
  ID         NUMBER(10)                             NULL,
  ID_REPORT  NUMBER(10)                             NULL,
  ID_ARCH    NUMBER(10)                             NULL
);


CREATE TABLE SCR_REPORTOFIC
(
  ID         NUMBER(10)                             NULL,
  ID_REPORT  NUMBER(10)                             NULL,
  ID_OFIC    NUMBER(10)                             NULL
);


CREATE TABLE SCR_REPORTPERF
(
  ID         NUMBER(10)                             NULL,
  ID_REPORT  NUMBER(10)                             NULL,
  ID_PERF    NUMBER(10)                             NULL
);


CREATE TABLE SCR_REPORTS
(
  ID           NUMBER(10)                           NULL,
  REPORT       VARCHAR2(250 CHAR)                   NULL,
  TYPE_REPORT  NUMBER(10)                           NULL,
  TYPE_ARCH    NUMBER(10)                           NULL,
  ALL_ARCH     NUMBER(10)                           NULL,
  ALL_OFICS    NUMBER(10)                           NULL,
  ALL_PERFS    NUMBER(10)                           NULL,
  DESCRIPTION  VARCHAR2(250 CHAR)                   NULL,
  DATA         BLOB                                 NULL
);


CREATE TABLE SCR_REPORTSLANG
(
  ID        NUMBER(10)                              NULL,
  LANGUAGE  NUMBER(10)                              NULL,
  NAME      VARCHAR2(250 CHAR)                      NULL
);


CREATE TABLE SCR_REPOSITORY
(
  ID             NUMBER(10)                         NULL,
  TYPE           NUMBER(10)                         NULL,
  CONFIGURATION  CLOB                               NULL
);


CREATE TABLE SCR_REPOSITORYCONF
(
  ID    NUMBER(10)                                  NULL,
  DATA  CLOB                                        NULL
);


CREATE TABLE SCR_REPRE
(
  ID                NUMBER(10)                      NULL,
  ID_REPRESENTANTE  NUMBER(10)                      NULL,
  ID_REPRESENTADO   NUMBER(10)                      NULL,
  ID_ADDRESS        NUMBER(10)                      NULL,
  NAME              VARCHAR2(100 CHAR)              NULL
);


CREATE TABLE SCR_SENDMSG
(
  ID           NUMBER(10)                           NULL,
  UID_MSG      VARCHAR2(40 CHAR)                    NULL,
  TYPE_MSG     NUMBER(10)                           NULL,
  FILE_NAME    VARCHAR2(40 CHAR)                    NULL,
  SENDER       NUMBER(10)                           NULL,
  DESTINATION  NUMBER(10)                           NULL,
  SEND_TYPE    NUMBER(10)                           NULL,
  GEN_DATE     DATE                                 NULL,
  SEND_DATE    DATE                                 NULL,
  RCV_DATE     DATE                                 NULL,
  SEND_STATE   NUMBER(10)                           NULL,
  ID_ACKMSG    NUMBER(10)                           NULL,
  SEND_NUM     NUMBER(10)                           NULL
);


CREATE TABLE SCR_SENDREG
(
  ID              NUMBER(10)                        NULL,
  ID_MSG          NUMBER(10)                        NULL,
  ID_ARCH         NUMBER(10)                        NULL,
  ID_FDR          NUMBER(10)                        NULL,
  REGORIG_CODE    VARCHAR2(16 CHAR)                 NULL,
  REGORIG_NUM     VARCHAR2(20 CHAR)                 NULL,
  REGORIG_DATE    DATE                              NULL,
  REGORIG_TYPE    NUMBER(10)                        NULL,
  ATTACH_PAGES    NUMBER(10)                        NULL,
  ATTACH_DOC      NUMBER(10)                        NULL,
  ID_ACKMSG       NUMBER(10)                        NULL,
  RCV_STATE       NUMBER(10)                        NULL,
  ID_ENTREG_DEST  NUMBER(10)                        NULL,
  RCV_NUMREG      VARCHAR2(20 CHAR)                 NULL,
  RCV_DATEREG     DATE                              NULL
);


CREATE TABLE SCR_SHAREDFILES
(
  FILEID       NUMBER(10)                           NULL,
  OWNERBOOKID  NUMBER(10)                           NULL,
  OWNERREGID   NUMBER(10)                           NULL,
  BOOKID       NUMBER(10)                           NULL,
  REGID        NUMBER(10)                           NULL
);


CREATE TABLE SCR_TMZOFIC
(
  ID   NUMBER(10)                                   NULL,
  TMZ  NUMBER(10)                                   NULL
);


CREATE TABLE SCR_TRAMUNIT
(
  ID             NUMBER(10)                         NULL,
  CODE_TRAMUNIT  VARCHAR2(21 CHAR)                  NULL,
  NAME_TRAMUNIT  VARCHAR2(250 CHAR)                 NULL,
  CODE_ENTITY    VARCHAR2(21 CHAR)                  NULL,
  NAME_ENTITY    VARCHAR2(250 CHAR)                 NULL,
  ID_ORGS        NUMBER(10)                         NULL
);


CREATE TABLE SCR_TRANSLATEDFMT
(
  ID          NUMBER(10)                            NULL,
  LANGID      NUMBER(10)                            NULL,
  SPA_TEXT    VARCHAR2(254 CHAR)                    NULL,
  TRANS_TEXT  VARCHAR2(254 CHAR)                    NULL
);


CREATE TABLE SCR_TT
(
  ID         NUMBER(10)                             NULL,
  TRANSPORT  VARCHAR2(31 CHAR)                      NULL
);


CREATE TABLE SCR_TTLANG
(
  ID        NUMBER(10)                              NULL,
  LANGUAGE  NUMBER(10)                              NULL,
  NAME      VARCHAR2(31 CHAR)                       NULL
);


CREATE TABLE SCR_TYPEADDRESS
(
  ID           NUMBER(10)                           NULL,
  DESCRIPTION  VARCHAR2(50 CHAR)                    NULL,
  CODE         VARCHAR2(2 CHAR)                     NULL
);


CREATE TABLE SCR_TYPEADM
(
  ID           NUMBER(10)                           NULL,
  CODE         VARCHAR2(1 CHAR)                     NULL,
  DESCRIPTION  VARCHAR2(50 CHAR)                    NULL
);


CREATE TABLE SCR_TYPEADMLANG
(
  ID        NUMBER(10)                              NULL,
  LANGUAGE  NUMBER(10)                              NULL,
  NAME      VARCHAR2(50 CHAR)                       NULL
);


CREATE TABLE SCR_TYPEDOC
(
  ID           NUMBER(10)                           NULL,
  DESCRIPTION  VARCHAR2(50 CHAR)                    NULL,
  TYPE_PERSON  NUMBER(10)                           NULL,
  CODE         VARCHAR2(1 CHAR)                     NULL
);


CREATE TABLE SCR_TYPEOFIC
(
  ID           NUMBER(10)                           NULL,
  DESCRIPTION  VARCHAR2(50 CHAR)                    NULL
);


CREATE TABLE SCR_UNITADM
(
  USERID   NUMBER(10)                               NULL,
  OBJID    NUMBER(10)                               NULL,
  OBJTYPE  NUMBER(10)                               NULL
);


CREATE TABLE SCR_USERCONFIG
(
  USERID      NUMBER(10)                            NULL,
  DATA        CLOB                                  NULL,
  IDOFICPREF  NUMBER(10)                            NULL
);


CREATE TABLE SCR_USERFILTER
(
  IDARCH     NUMBER(10)                             NULL,
  IDUSER     NUMBER(10)                             NULL,
  FILTERDEF  CLOB                                   NULL,
  TYPE_OBJ   NUMBER(10)                             NULL
);


CREATE TABLE SCR_USRIDENT
(
  USERID       NUMBER(10)                           NULL,
  TMSTAMP      DATE                                 NULL,
  FIRST_NAME   VARCHAR2(25 CHAR)                    NULL,
  SECOND_NAME  VARCHAR2(25 CHAR)                    NULL,
  SURNAME      VARCHAR2(20 CHAR)                    NULL
);


CREATE TABLE SCR_USRLOC
(
  USERID     NUMBER(10)                             NULL,
  TMSTAMP    DATE                                   NULL,
  ADDRESS    VARCHAR2(255 CHAR)                     NULL,
  CITY       VARCHAR2(100 CHAR)                     NULL,
  ZIP        VARCHAR2(10 CHAR)                      NULL,
  COUNTRY    VARCHAR2(100 CHAR)                     NULL,
  TELEPHONE  VARCHAR2(160 CHAR)                     NULL,
  FAX        VARCHAR2(160 CHAR)                     NULL,
  EMAIL      VARCHAR2(255 CHAR)                     NULL
);


CREATE TABLE SCR_USROFIC
(
  ID      NUMBER(10)                                NULL,
  IDUSER  NUMBER(10)                                NULL,
  IDOFIC  NUMBER(10)                                NULL
);


CREATE TABLE SCR_USRPERMS
(
  ID_USR   NUMBER(10)                               NULL,
  TMSTAMP  DATE                                     NULL,
  PERMS    NUMBER(10)                               NULL
);


CREATE TABLE SCR_VALDATE
(
  ID        NUMBER(10)                              NULL,
  VALUE     DATE                                    NULL,
  OLDVALUE  DATE                                    NULL
);


CREATE TABLE SCR_VALNUM
(
  ID        NUMBER(10)                              NULL,
  VALUE     NUMBER(10)                              NULL,
  OLDVALUE  NUMBER(10)                              NULL
);


CREATE TABLE SCR_VALSTR
(
  ID        NUMBER(10)                              NULL,
  VALUE     VARCHAR2(4000 CHAR)                     NULL,
  OLDVALUE  VARCHAR2(4000 CHAR)                     NULL
);


CREATE TABLE SCR_VERSIONITEMS
(
  OBJTYPE      NUMBER(10)                           NULL,
  CODE         VARCHAR2(32 CHAR)                    NULL,
  OBJID        NUMBER(10)                           NULL,
  VERSION      NUMBER(10)                           NULL,
  USERID       NUMBER(10)                           NULL,
  VERSIONDATE  DATE                                 NULL
);


CREATE TABLE SCR_WS
(
  ID           NUMBER(10)                           NULL,
  NOMBRE       VARCHAR2(50 CHAR)                    NULL,
  MACADDR      VARCHAR2(32 CHAR)                    NULL,
  IPADDR       VARCHAR2(20 CHAR)                    NULL,
  IDOFIC       NUMBER(10)                           NULL,
  CODE         VARCHAR2(4 CHAR)                     NULL,
  DESCRIPCION  VARCHAR2(250 CHAR)                   NULL
);


CREATE TABLE XT_FIELD
(
  FLDID        NUMBER(10)                           NULL,
  NAME         VARCHAR2(32 CHAR)                    NULL,
  DESCRIPCION  VARCHAR2(256 CHAR)                   NULL,
  SECCION      VARCHAR2(32 CHAR)                    NULL
);

CREATE TABLE VERSION
(
  VERSION      VARCHAR2(20),
  VERSIONDATE  DATE
);
