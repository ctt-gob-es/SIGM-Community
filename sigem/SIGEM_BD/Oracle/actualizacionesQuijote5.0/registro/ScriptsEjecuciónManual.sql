-- Para cada Libro de entrada
-- Hay que ejecutar las sentencias para cada libro de entrada a parte del que viene por defecto (id_libro = 1)
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD503 INTEGER;
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD504 INTEGER;
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD505 INTEGER;
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD506 INTEGER;
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD1002 INTEGER;
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD1003 INTEGER;
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD1004 VARCHAR(255);

ALTER TABLE A1SF ADD COLUMN FLD503 INTEGER;
ALTER TABLE A1SF ADD COLUMN FLD504 INTEGER;
ALTER TABLE A1SF ADD COLUMN FLD505 INTEGER;
ALTER TABLE A1SF ADD COLUMN FLD506 INTEGER;
ALTER TABLE A1SF ADD COLUMN FLD1002 INTEGER;
ALTER TABLE A1SF ADD COLUMN FLD1003 INTEGER;
ALTER TABLE A1SF ADD COLUMN FLD1004 VARCHAR(255);


-- Para cada Libro de salida
-- Hay que ejecutar las sentencias para cada libro de salida a parte del que viene por defecto (id_libro = 2)
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLd16 INTEGER;
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD17 VARCHAR(1000);
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD19 VARCHAR(80);
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD503 INTEGER;
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD504 INTEGER;
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD505 INTEGER;
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD506 INTEGER;
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD1002 INTEGER;
-- ALTER TABLE A[[id_libro]]SF ADD COLUMN FLD1004 VARCHAR(255);

ALTER TABLE A2SF ADD COLUMN FLd16 INTEGER;
ALTER TABLE A2SF ADD COLUMN FLD17 VARCHAR(1000);
ALTER TABLE A2SF ADD COLUMN FLD19 VARCHAR(80);
ALTER TABLE A2SF ADD COLUMN FLD503 INTEGER;
ALTER TABLE A2SF ADD COLUMN FLD504 INTEGER;
ALTER TABLE A2SF ADD COLUMN FLD505 INTEGER;
ALTER TABLE A2SF ADD COLUMN FLD506 INTEGER;
ALTER TABLE A2SF ADD COLUMN FLD1002 INTEGER;
ALTER TABLE A2SF ADD COLUMN FLD1004 VARCHAR(255);


/* Formatted on 29/06/2016 11:17:03 (QP5 v5.139.911.3011) */
-- Hay que añadir para cada libro de entrada a parte del que viene por defecto (id_libro = 1)
--      UNION ALL
--      SELECT [[id_libro]] AS IDBOOK,
--          FDRID, FLD1, FLD2, FLD7, FLD8, FLD9, FLD16, FLD17
--        FROM A[[id_libro]]SF INNER JOIN SCR_DISTREG ON FDRID = ID_FDR AND ID_ARCH = [[id_libro]]

-- Para cada libro de salida a parte del que viene por defecto (id_libro = 2)
--      UNION ALL
--      SELECT [[id_libro]] AS IDBOOK,
--          FDRID, FLD1, FLD2, FLD7, FLD8, FLD9, FLD12 AS FLD16, FLD13 AS FLD17
--        FROM A[[id_libro]]SF INNER JOIN SCR_DISTREG ON FDRID = ID_FDR AND ID_ARCH = [[id_libro]]

CREATE OR REPLACE FORCE VIEW SCR_REGDIST
(
   IDBOOK,
   FDRID, FLD1, FLD2, FLD7, FLD8, FLD9, FLD16, FLD17
)
AS
   SELECT 1 AS IDBOOK,
          FDRID, FLD1, FLD2, FLD7, FLD8, FLD9, FLD16, FLD17
     FROM A1SF INNER JOIN SCR_DISTREG ON FDRID = ID_FDR AND ID_ARCH = 1
   UNION ALL
   SELECT 2 AS IDBOOK,
          FDRID, FLD1, FLD2, FLD7, FLD8, FLD9, FLD12 AS FLD16, FLD13 AS FLD17
     FROM A2SF INNER JOIN SCR_DISTREG ON FDRID = ID_FDR AND ID_ARCH = 2;




-- Hay que insertar únicamente los que no se encuentren ya en SC_TYPEADM
-- es obligatorio que PROPIOS tenga el código 0
-- PROPIOS
INSERT INTO scr_typeadm (id, code, description) VALUES (select CONTADOR From scr_contador where tablaid='SCR_TYPEADM', '0', 'PROPIOS');
UPDATE scr_contador SET contador = contador + 1 where tablaid='SCR_TYPEADM';
-- ADMINISTRACIÓN ESTATAL
INSERT INTO scr_typeadm (id, code, description) VALUES (select CONTADOR From scr_contador where tablaid='SCR_TYPEADM', 'E', 'PODER EJECUTIVO');
UPDATE scr_contador SET contador = contador + 1 where tablaid='SCR_TYPEADM';
-- ADMINISTRACIÓN AUTONÓMICA
INSERT INTO scr_typeadm (id, code, description) VALUES (select CONTADOR From scr_contador where tablaid='SCR_TYPEADM', 'A', 'COMUNIDADES AUTÓNOMAS');
UPDATE scr_contador SET contador = contador + 1 where tablaid='SCR_TYPEADM';
-- ADMINISTRACIÓN LOCAL
INSERT INTO scr_typeadm (id, code, description) VALUES (select CONTADOR From scr_contador where tablaid='SCR_TYPEADM', 'L', 'ADMINISTRACIÓN LOCAL');
UPDATE scr_contador SET contador = contador + 1 where tablaid='SCR_TYPEADM';
-- UNIVERSIDADES
INSERT INTO scr_typeadm (id, code, description) VALUES (select CONTADOR From scr_contador where tablaid='SCR_TYPEADM', 'U', 'UNIVERSIDADES');
UPDATE scr_contador SET contador = contador + 1 where tablaid='SCR_TYPEADM';
-- OTROS
INSERT INTO scr_typeadm (id, code, description) VALUES (select CONTADOR From scr_contador where tablaid='SCR_TYPEADM', 'I', 'OTROS');
UPDATE scr_contador SET contador = contador + 1 where tablaid='SCR_TYPEADM';
