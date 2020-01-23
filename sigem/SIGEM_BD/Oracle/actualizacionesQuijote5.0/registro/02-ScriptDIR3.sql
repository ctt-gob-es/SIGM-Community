-- BD de registro

UPDATE SCR_TYPEADM SET DESCRIPTION = 'ADMINISTRACIÓN LOCAL' WHERE CODE = 'L';

-- Incrementamos el contador antes del primer insert por si no se aumentó en la última inserción
UPDATE SCR_CONTADOR SET CONTADOR = CONTADOR + 1 WHERE TABLAID = 'SCR_TYPEADM';
INSERT INTO SCR_TYPEADM (ID, CODE, DESCRIPTION) VALUES ((SELECT CONTADOR FROM SCR_CONTADOR WHERE TABLAID = 'SCR_TYPEADM'), 'U', 'UNIVERSIDADES');
UPDATE SCR_CONTADOR SET CONTADOR = CONTADOR + 1 WHERE TABLAID = 'SCR_TYPEADM';
INSERT INTO SCR_TYPEADM (ID, CODE, DESCRIPTION) VALUES ((SELECT CONTADOR FROM SCR_CONTADOR WHERE TABLAID = 'SCR_TYPEADM'), 'I', 'OTRAS INSTITUCIONES');
UPDATE SCR_CONTADOR SET CONTADOR = CONTADOR + 1 WHERE TABLAID = 'SCR_TYPEADM';
INSERT INTO SCR_TYPEADM (ID, CODE, DESCRIPTION) VALUES ((SELECT CONTADOR FROM SCR_CONTADOR WHERE TABLAID = 'SCR_TYPEADM'), 'J', 'ADMINISTRACIÓN DE JUSTICIA');
UPDATE SCR_CONTADOR SET CONTADOR = CONTADOR + 1 WHERE TABLAID = 'SCR_TYPEADM';
