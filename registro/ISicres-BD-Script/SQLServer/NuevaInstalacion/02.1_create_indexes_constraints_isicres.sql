
----------------------------------------
--       PRIMARY KEYS           	  --
----------------------------------------


ALTER TABLE SCR_ADDRESS ADD CONSTRAINT PK_SCR_ADDRESS0
	PRIMARY KEY (ID)
;

ALTER TABLE SCR_CA ADD CONSTRAINT PK_SCR_CA
	PRIMARY KEY (ID)
;

ALTER TABLE SCR_OFIC ADD CONSTRAINT PK_SCR_OFIC
	PRIMARY KEY (ID)
;

ALTER TABLE SCR_ORGS ADD CONSTRAINT PK_SCR_ORGS
	PRIMARY KEY (ID)
;

ALTER TABLE SCR_TYPEADDRESS ADD CONSTRAINT PK_SCR_TYPEADDRESS
	PRIMARY KEY (ID)
;

ALTER TABLE SCR_TYPEADM ADD CONSTRAINT PK_SCR_TYPEADM
	PRIMARY KEY (ID)
;

ALTER TABLE SCR_TYPEDOC ADD CONSTRAINT PK_SCR_TYPEDOC0
	PRIMARY KEY (ID)
;

ALTER TABLE SCR_TYPEOFIC ADD CONSTRAINT PK_SCR_TYPEOFIC
	PRIMARY KEY (ID)
;

ALTER TABLE scr_repositoryconf ADD CONSTRAINT pk_repositoryconf
	PRIMARY KEY (id)
;

ALTER TABLE scr_repobooktype ADD CONSTRAINT pk_repobooktype
	PRIMARY KEY (book_type)
;

ALTER TABLE scr_prov ADD CONSTRAINT pk_prov
	PRIMARY KEY (id)
;

ALTER TABLE scr_cities ADD CONSTRAINT pk_cities
	PRIMARY KEY (id)
;

ALTER TABLE scr_pagetype ADD CONSTRAINT pk_pagetype
	PRIMARY KEY (id)
;


----------------------------------------
--       END PRIMARY KEYS             --
----------------------------------------


--------------------------------
-- INDEX KEYS INVESSICRES     --
--------------------------------
CREATE UNIQUE INDEX ACTWS_IDX1
ON SCR_ACTWS (IDPUESTO)
;
CREATE INDEX SCR_ADDRESS1
ON SCR_ADDRESS (ID_PERSON)
;
CREATE UNIQUE INDEX SCR_ADDRTEL0
ON SCR_ADDRTEL (ID)
;
CREATE INDEX SCR_BOOKADMIN0
ON SCR_BOOKADMIN (IDBOOK)
;
CREATE INDEX SCR_BOOKADMIN1
ON SCR_BOOKADMIN (IDBOOK, IDUSER)
;
CREATE INDEX SCR_BOOKASOC0
ON SCR_BOOKASOC (ID_BOOK)
;
CREATE UNIQUE INDEX SCR_BOOKCONFIG0
ON SCR_BOOKCONFIG (BOOKID)
;
CREATE INDEX SCR_BOOKOFIC0
ON SCR_BOOKOFIC (ID)
;
CREATE INDEX SCR_BOOKOFIC1
ON SCR_BOOKOFIC (ID_BOOK)
;
CREATE INDEX SCR_BOOKOFIC2
ON SCR_BOOKOFIC (ID_OFIC)
;
CREATE UNIQUE INDEX SCRBOOKREPOSITORY0
ON SCR_BOOKREPOSITORY (BOOKID)
;
CREATE UNIQUE INDEX SCR_BOOKTYPECONFIG
ON SCR_BOOKTYPECONFIG (BOOKTYPE)
;
CREATE UNIQUE INDEX SCRCA1
ON SCR_CA (CODE)
;
CREATE INDEX SCR_CAADM0
ON SCR_CAADM (ID)
;
CREATE INDEX SCR_CAADM1
ON SCR_CAADM (IDUSER)
;
CREATE INDEX SCR_CAADM2
ON SCR_CAADM (IDCA)
;
CREATE INDEX SCRCAAUX0
ON SCR_CAAUX (ID)
;
CREATE INDEX SCRCAAUX1
ON SCR_CAAUX (ID_MATTER)
;
CREATE INDEX SCR_CADOCSIDX1
ON SCR_CADOCS (ID)
;
CREATE INDEX SCR_CADOCSIDX2
ON SCR_CADOCS (ID_MATTER)
;
CREATE INDEX SCR_CAOFICIDX1
ON SCR_CAOFIC (ID)
;
CREATE INDEX SCR_CAOFICIDX2
ON SCR_CAOFIC (ID_MATTER)
;
CREATE INDEX SCR_CITIES0
ON SCR_CITIES (ID)
;
CREATE INDEX SCR_CITIES1
ON SCR_CITIES (NAME)
;
CREATE INDEX SCR_CITIES2
ON SCR_CITIES (ID_PROV)
;
CREATE UNIQUE INDEX TABLACNTIDX
ON SCR_CONTADOR (TABLAID)
;
CREATE UNIQUE INDEX DELTAS_IDX1
ON SCR_DELTAS (IDCAMBIO)
;
CREATE UNIQUE INDEX SCR_DIROFIC0
ON SCR_DIROFIC (ID_OFIC)
;
CREATE UNIQUE INDEX SCR_DIRORGS0
ON SCR_DIRORGS (ID_ORGS)
;
CREATE INDEX SCR_DISTACCEPT0
ON SCR_DISTACCEPT (BOOKID)
;
CREATE INDEX SCR_DISTACCEPT1
ON SCR_DISTACCEPT (REGID)
;
CREATE INDEX SCR_DISTACCEPT2
ON SCR_DISTACCEPT (OFFICEID)
;
CREATE INDEX SCR_DISTLIST0
ON SCR_DISTLIST (ID)
;
CREATE INDEX SCR_DISTLIST1
ON SCR_DISTLIST (ID_ORGS)
;
CREATE INDEX SCRDISTREG0
ON SCR_DISTREG (ID)
;
CREATE INDEX SCRDISTREG1
ON SCR_DISTREG (ID_ARCH)
;
CREATE INDEX SCRDISTREG2
ON SCR_DISTREG (ID_FDR)
;
CREATE INDEX SCRDISTREG3
ON SCR_DISTREG (TYPE_DEST)
;
CREATE INDEX SCRDISTREG4
ON SCR_DISTREG (ID_DEST)
;
CREATE INDEX SCRDISTREG5
ON SCR_DISTREG (STATE)
;
CREATE INDEX SCRDISTREG6
ON SCR_DISTREG (STATE_DATE)
;
CREATE INDEX SCRDISTREG7
ON SCR_DISTREG (TYPE_ORIG)
;
CREATE INDEX SCRDISTREG8
ON SCR_DISTREG (ID_ORIG)
;
CREATE INDEX SCRDISTREGSTATE0
ON SCR_DISTREGSTATE (ID)
;
CREATE INDEX SCRDISTREGSTATE1
ON SCR_DISTREGSTATE (ID_DIST)
;
CREATE UNIQUE INDEX SCR_DOM0
ON SCR_DOM (ID)
;
CREATE UNIQUE INDEX SCR_ENTREGSND0
ON SCR_ENTREGSND (ID_ENTREG)
;
CREATE UNIQUE INDEX SCR_EXPCNTS0
ON SCR_EXPCNTS (ID)
;
CREATE UNIQUE INDEX SCR_EXPCNTS1
ON SCR_EXPCNTS (ID_TASK)
;
CREATE UNIQUE INDEX SCREXPFILTERS0
ON SCR_EXPFILTERS (ID)
;
CREATE UNIQUE INDEX SCREXPFILTERS1
ON SCR_EXPFILTERS (ID_TASK)
;
CREATE INDEX SCREXPTASKS1
ON SCR_EXPTASKS (ID)
;
CREATE INDEX SCREXPTASKS2
ON SCR_EXPTASKS (ID_USER)
;
CREATE UNIQUE INDEX SCR_FLDACCPERMS0
ON SCR_FLDACCPERMS (ARCHIVETYPE, IDFLD)
;
CREATE UNIQUE INDEX SCR_FLDPERMARCH0
ON SCR_FLDPERMARCH (IDARCHIVE, IDFLD)
;
CREATE UNIQUE INDEX SCR_FLDPERMUSER0
ON SCR_FLDPERMUSER (IDARCHIVE, IDFLD)
;
CREATE UNIQUE INDEX SCR_GENPERMS0
ON SCR_GENPERMS (ID)
;
CREATE UNIQUE INDEX SCRLASTCONNECTION0
ON SCR_LASTCONNECTION (IDUSER)
;
CREATE INDEX SCR_LASTREGISTER0
ON SCR_LASTREGISTER (BOOKID)
;
CREATE INDEX SCR_LASTREGISTER1
ON SCR_LASTREGISTER (USERID)
;
CREATE INDEX SCR_LOCKRELATIONS0
ON SCR_LOCKRELATIONS (TYPEBOOK)
;
CREATE INDEX SCR_LOCKRELATIONS1
ON SCR_LOCKRELATIONS (TYPEREL)
;
CREATE INDEX SCR_LOCKRELATIONS2
ON SCR_LOCKRELATIONS (IDOFIC)
;
CREATE INDEX SCR_MODIFREG0
ON SCR_MODIFREG (ID)
;
CREATE INDEX SCR_MODIFREG1
ON SCR_MODIFREG (MODIF_DATE)
;
CREATE INDEX SCR_MODIFREG2
ON SCR_MODIFREG (NUM_REG)
;
CREATE INDEX SCR_MODIFREG3
ON SCR_MODIFREG (ID_FLD)
;
CREATE UNIQUE INDEX SCRNACKRECVMSG0
ON SCR_NACKRECVMSG (ID)
;
CREATE UNIQUE INDEX SCRNACKRECVREG0
ON SCR_NACKRECVREG (ID)
;
CREATE UNIQUE INDEX SCRNACKSENDMSG0
ON SCR_NACKSENDMSG (ID)
;
CREATE UNIQUE INDEX SCRNACKSENDREG0
ON SCR_NACKSENDREG (ID)
;
CREATE INDEX SCR_OFIC1
ON SCR_OFIC (CODE)
;
CREATE INDEX SCR_OFIC2
ON SCR_OFIC (CREATION_DATE)
;
CREATE INDEX SCR_OFIC3
ON SCR_OFIC (DISABLE_DATE)
;
CREATE INDEX SCR_OFIC4
ON SCR_OFIC (ID_ORGS)
;
CREATE INDEX SCR_OFIC5
ON SCR_OFIC (DEPTID)
;
CREATE INDEX SCR_OFICADM0
ON SCR_OFICADM (ID)
;
CREATE INDEX SCR_OFICADM1
ON SCR_OFICADM (IDUSER)
;
CREATE INDEX SCR_OFICADM2
ON SCR_OFICADM (IDOFIC)
;
CREATE INDEX SCR_ORGS1
ON SCR_ORGS (CODE)
;
CREATE INDEX SCR_ORGS2
ON SCR_ORGS (ID_FATHER)
;
CREATE INDEX SCR_ORGS3
ON SCR_ORGS (ACRON)
;
CREATE INDEX SCR_ORGS4
ON SCR_ORGS (CREATION_DATE, DISABLE_DATE)
;
CREATE INDEX SCR_ORGS5
ON SCR_ORGS (ID_FATHER, TYPE)
;
CREATE INDEX SCR_PAGEINFO0
ON SCR_PAGEINFO (BOOKID)
;
CREATE INDEX SCR_PAGEINFO1
ON SCR_PAGEINFO (REGID)
;
CREATE INDEX SCR_PAGEINFO2
ON SCR_PAGEINFO (PAGEID)
;
CREATE INDEX SCRFDRREPOSITORY1
ON SCR_PAGEREPOSITORY (FDRID)
;
CREATE INDEX SCRFDRREPOSITORY2
ON SCR_PAGEREPOSITORY (PAGEID)
;
CREATE INDEX SCRPAGEREPOSITORY0
ON SCR_PAGEREPOSITORY (BOOKID)
;
CREATE INDEX SCRPFIS0
ON SCR_PFIS (ID)
;
CREATE INDEX SCRPFIS1
ON SCR_PFIS (NIF)
;
CREATE INDEX SCRPFIS2
ON SCR_PFIS (FIRST_NAME)
;
CREATE INDEX SCRPFIS3
ON SCR_PFIS (SECOND_NAME)
;
CREATE INDEX SCRPFIS4
ON SCR_PFIS (SURNAME)
;
CREATE INDEX SCR_PINFO0
ON SCR_PINFO (ID)
;
CREATE INDEX SCR_PINFO1
ON SCR_PINFO (OFFICEID)
;
CREATE INDEX SCRPJUR0
ON SCR_PJUR (ID)
;
CREATE INDEX SCRPJUR1
ON SCR_PJUR (CIF)
;
CREATE INDEX SCRPJUR2
ON SCR_PJUR (NAME)
;
CREATE INDEX SCRPRIVORGS0
ON SCR_PRIVORGS (ID)
;
CREATE INDEX SCRPRIVORGS1
ON SCR_PRIVORGS (IDORGS)
;
CREATE INDEX SCRPRIVORGS2
ON SCR_PRIVORGS (IDOFIC)
;
CREATE INDEX SCRPROCREG0
ON SCR_PROCREG (ID)
;
CREATE INDEX SCRPROCREG1
ON SCR_PROCREG (ID_DIST)
;
CREATE INDEX SCR_PROV0
ON SCR_PROV (ID)
;
CREATE INDEX SCR_PROV1
ON SCR_PROV (NAME)
;
CREATE UNIQUE INDEX SCRRECVMSG0
ON SCR_RECVMSG (ID)
;
CREATE UNIQUE INDEX SCRRECVMSG1
ON SCR_RECVMSG (FILE_NAME)
;
CREATE UNIQUE INDEX SCRRECVREG0
ON SCR_RECVREG (ID)
;
CREATE INDEX SCR_REGASOC0
ON SCR_REGASOC (ID)
;
CREATE INDEX SCR_REGASOC1
ON SCR_REGASOC (ID_ARCHPRIM)
;
CREATE INDEX SCR_REGASOC2
ON SCR_REGASOC (ID_FDRPRIM)
;
CREATE UNIQUE INDEX SCR_REGASOCEX0
ON SCR_REGASOCEX (ID_ASOC)
;
CREATE INDEX SCR_REGINT0
ON SCR_REGINT (ID)
;
CREATE INDEX SCR_REGINT1
ON SCR_REGINT (ID_ARCH)
;
CREATE INDEX SCR_REGINT2
ON SCR_REGINT (ID_FDR)
;
CREATE INDEX SCR_REGINT3
ON SCR_REGINT (NAME)
;
CREATE INDEX SCR_REGPDOCS0
ON SCR_REGPDOCS (ID)
;
CREATE INDEX SCR_REGPDOCS1
ON SCR_REGPDOCS (ID_ARCH)
;
CREATE INDEX SCR_REGPDOCS2
ON SCR_REGPDOCS (ID_FDR)
;
CREATE INDEX SCR_REGSTATE0
ON SCR_REGSTATE (ID)
;
CREATE INDEX SCR_REGSTATE1
ON SCR_REGSTATE (IDARCHREG)
;
CREATE INDEX SCR_RELATIONS0
ON SCR_RELATIONS (TYPEBOOK)
;
CREATE INDEX SCR_RELATIONS1
ON SCR_RELATIONS (TYPEREL)
;
CREATE INDEX SCR_RELATIONS2
ON SCR_RELATIONS (RELYEAR)
;
CREATE INDEX SCR_RELATIONS3
ON SCR_RELATIONS (RELMONTH)
;
CREATE INDEX SCR_RELATIONS4
ON SCR_RELATIONS (RELDAY)
;
CREATE INDEX SCR_RELATIONS5
ON SCR_RELATIONS (IDOFIC)
;
CREATE INDEX SCR_RELATIONS6
ON SCR_RELATIONS (IDUNIT)
;
CREATE INDEX SCRREPORTARC0
ON SCR_REPORTARCH (ID)
;
CREATE INDEX SCRREPORTARCH1
ON SCR_REPORTARCH (ID_REPORT)
;
CREATE INDEX SCRREPORTOFIC0
ON SCR_REPORTOFIC (ID)
;
CREATE INDEX SCRREPORTOFIC1
ON SCR_REPORTOFIC (ID_REPORT)
;
CREATE INDEX SCRREPORTPERF0
ON SCR_REPORTPERF (ID)
;
CREATE INDEX SCRREPORTPERF1
ON SCR_REPORTPERF (ID_REPORT)
;
CREATE INDEX SCRREPORT0
ON SCR_REPORTS (ID)
;
CREATE INDEX SCRREPORT1
ON SCR_REPORTS (TYPE_REPORT)
;
CREATE INDEX SCRREPORT2
ON SCR_REPORTS (TYPE_ARCH)
;
CREATE UNIQUE INDEX SCRREPOSITORY0
ON SCR_REPOSITORY (ID)
;
CREATE INDEX SCRSENDMSG0
ON SCR_SENDMSG (ID)
;
CREATE INDEX SCRSENDREG0
ON SCR_SENDREG (ID)
;
CREATE INDEX SCRSENDREG1
ON SCR_SENDREG (ID_MSG)
;
CREATE INDEX SCRSENDREG2
ON SCR_SENDREG (RCV_STATE)
;
CREATE INDEX SCRSHAREDFILES0
ON SCR_SHAREDFILES (FILEID)
;
CREATE INDEX SCRTRANSFMT0
ON SCR_TRANSLATEDFMT (ID)
;
CREATE INDEX SCRTRANSFMT1
ON SCR_TRANSLATEDFMT (LANGID)
;
CREATE INDEX SCRTRANSFMT2
ON SCR_TRANSLATEDFMT (SPA_TEXT)
;
CREATE UNIQUE INDEX SCR_TYPEADM1
ON SCR_TYPEADM (CODE)
;
CREATE UNIQUE INDEX SCRUSERCONFIG0
ON SCR_USERCONFIG (USERID)
;
CREATE INDEX SCR_USERFILTER0
ON SCR_USERFILTER (IDARCH)
;
CREATE INDEX SCR_USERFILTER1
ON SCR_USERFILTER (IDUSER)
;
CREATE INDEX SCR_USERFILTER2
ON SCR_USERFILTER (IDARCH, IDUSER, TYPE_OBJ)
;
CREATE UNIQUE INDEX SCR_USRIDENT0
ON SCR_USRIDENT (USERID)
;
CREATE UNIQUE INDEX SCR_USRLOC0
ON SCR_USRLOC (USERID)
;
CREATE INDEX SCR_USROFIC0
ON SCR_USROFIC (ID)
;
CREATE INDEX SCR_USROFIC1
ON SCR_USROFIC (IDUSER)
;
CREATE INDEX SCR_USROFIC2
ON SCR_USROFIC (IDOFIC)
;
CREATE UNIQUE INDEX SCR_USRPERMS0
ON SCR_USRPERMS (ID_USR)
;
CREATE INDEX SCR_VALDATE0
ON SCR_VALDATE (ID)
;
CREATE INDEX SCR_VALNUM0
ON SCR_VALNUM (ID)
;
CREATE INDEX SCR_VALSTR0
ON SCR_VALSTR (ID)
;
CREATE UNIQUE INDEX SCR_WS0
ON SCR_WS (ID)
;
CREATE UNIQUE INDEX SCR_WS1
ON SCR_WS (CODE)
;

-- TABLA scr_unitadm
CREATE INDEX scr_unitadm0 ON scr_unitadm (userid);
CREATE INDEX scr_unitadm1 ON scr_unitadm (objid);

-- TABLA scr_lockitems
CREATE INDEX scrlockitems0 ON scr_lockitems (objid);
CREATE INDEX scrlockitems1 ON scr_lockitems (userid);

-- TABLA scr_versionitems
CREATE INDEX scrversionitems0 ON scr_versionitems (code);
CREATE INDEX scrversionitems1 ON scr_versionitems (objid);

-- TABLA scr_seqcnt
CREATE UNIQUE INDEX scrseqcnt0 ON scr_seqcnt (id);
CREATE CLUSTERED INDEX scrseqcnt1 ON scr_seqcnt (userid);

CREATE INDEX scr_orgslang0 ON scr_orgslang (id);
CREATE UNIQUE INDEX scr_orgslang1 ON scr_orgslang (id,language);

CREATE INDEX scr_typeadmlang0 ON scr_typeadmlang (id);
CREATE UNIQUE INDEX scr_typeadmlang1 ON scr_typeadmlang (id,language);

CREATE INDEX scr_oficlang0 ON scr_oficlang (id);
CREATE UNIQUE INDEX scr_oficlang1 ON scr_oficlang (id,language);

CREATE INDEX scr_calang0 ON scr_calang (id);
CREATE UNIQUE INDEX scr_calang1 ON scr_calang (id,language);

CREATE INDEX scr_ttlang0 ON scr_ttlang (id);
CREATE UNIQUE INDEX scr_ttlang1 ON scr_ttlang (id,language);

CREATE INDEX scr_reportslang0 ON scr_reportslang (id);
CREATE UNIQUE INDEX scr_reportslang1 ON scr_reportslang (id,language);

CREATE INDEX scr_bookslang0 ON scr_bookslang (id);
CREATE UNIQUE INDEX scr_bookslang1 ON scr_bookslang (id,language);

CREATE UNIQUE INDEX scr_repoconf1 ON scr_repositoryconf (id);

CREATE UNIQUE INDEX scr_repbooktype1 ON scr_repobooktype (book_type);

CREATE UNIQUE INDEX scr_doclocator0 ON scr_doclocator (locator);

--------------------------------
-- END INDEX KEYS INVESSICRES --
--------------------------------

-----------------------------------
-- FOREIGN KEYS ARCHIVADORES     --
-----------------------------------
ALTER TABLE SCR_ADDRTEL ADD CONSTRAINT FK_SCR_ADDRTEL0
	FOREIGN KEY (TYPE) REFERENCES SCR_TYPEADDRESS (ID)
;

ALTER TABLE SCR_CAADM ADD CONSTRAINT FK_SCRCAADM0
	FOREIGN KEY (IDCA) REFERENCES SCR_CA (ID)
;

ALTER TABLE SCR_CAOFIC ADD CONSTRAINT FK_SCR_CAOFIC0
	FOREIGN KEY (ID_OFIC) REFERENCES SCR_OFIC (ID)
;

ALTER TABLE SCR_LOCKRELATIONS ADD CONSTRAINT FK_SCRLCKREL0
	FOREIGN KEY (IDOFIC) REFERENCES SCR_OFIC (ID)
;

ALTER TABLE SCR_OFIC ADD CONSTRAINT FK_SCR_OFIC0
	FOREIGN KEY (ID_ORGS) REFERENCES SCR_ORGS (ID)
;

ALTER TABLE SCR_OFIC ADD CONSTRAINT FK_SCR_OFIC1
	FOREIGN KEY (TYPE) REFERENCES SCR_TYPEOFIC (ID)
;

ALTER TABLE SCR_OFICADM ADD CONSTRAINT FK_SCROFICADM0
	FOREIGN KEY (IDOFIC) REFERENCES SCR_OFIC (ID)
;

ALTER TABLE SCR_ORGS ADD CONSTRAINT FK_SCR_ORGS0
	FOREIGN KEY (TYPE) REFERENCES SCR_TYPEADM (ID)
;

ALTER TABLE SCR_PFIS ADD CONSTRAINT FK_SCR_PFIS0
	FOREIGN KEY (TYPE_DOC) REFERENCES SCR_TYPEDOC (ID)
;

ALTER TABLE SCR_PJUR ADD CONSTRAINT FK_SCR_PJUR0
	FOREIGN KEY (TYPE_DOC) REFERENCES SCR_TYPEDOC (ID)
;

ALTER TABLE SCR_PRIVORGS ADD CONSTRAINT FK_SCR_PRIVORGS0
	FOREIGN KEY (IDORGS) REFERENCES SCR_ORGS (ID)
;

ALTER TABLE SCR_RECVMSG ADD CONSTRAINT FK_SCR_RECVMSG0
	FOREIGN KEY (SENDER) REFERENCES SCR_ORGS (ID)
;

ALTER TABLE SCR_RECVMSG ADD CONSTRAINT FK_SCR_RECVMSG1
	FOREIGN KEY (DESTINATION) REFERENCES SCR_ORGS (ID)
;

ALTER TABLE SCR_RELATIONS ADD CONSTRAINT FK_SCRRELS0
	FOREIGN KEY (IDOFIC) REFERENCES SCR_OFIC (ID)
;

ALTER TABLE SCR_RELATIONS ADD CONSTRAINT FK_SCRRELS1
	FOREIGN KEY (IDUNIT) REFERENCES SCR_ORGS (ID)
;

ALTER TABLE SCR_REPORTOFIC ADD CONSTRAINT FK_SCR_REPORTOFIC0
	FOREIGN KEY (ID_OFIC) REFERENCES SCR_OFIC (ID)
;

ALTER TABLE SCR_SENDMSG ADD CONSTRAINT FK_SCR_SENDMSG0
	FOREIGN KEY (SENDER) REFERENCES SCR_ORGS (ID)
;

ALTER TABLE SCR_SENDMSG ADD CONSTRAINT FK_SCR_SENDMSG1
	FOREIGN KEY (DESTINATION) REFERENCES SCR_ORGS (ID)
;

ALTER TABLE SCR_SENDREG ADD CONSTRAINT FK_SCR_SENDREG0
	FOREIGN KEY (ID_ENTREG_DEST) REFERENCES SCR_ORGS (ID)
;

ALTER TABLE SCR_USROFIC ADD CONSTRAINT FK_SCR_USROFIC0
	FOREIGN KEY (IDOFIC) REFERENCES SCR_OFIC (ID)
;

ALTER TABLE SCR_WS ADD CONSTRAINT FK_SCR_WS0
	FOREIGN KEY (IDOFIC) REFERENCES SCR_OFIC (ID)
;

ALTER TABLE scr_repobooktype ADD CONSTRAINT fk_repobooktype0
	FOREIGN KEY (id_rep_conf) REFERENCES scr_repositoryconf(id)
;

ALTER TABLE scr_pagerepository ADD CONSTRAINT fk_pagerepository0
	FOREIGN KEY (id_pagetype) REFERENCES scr_pagetype(id)
;

-----------------------------------
-- END FOREIGN KEYS ARCHIVADORES --
-----------------------------------
