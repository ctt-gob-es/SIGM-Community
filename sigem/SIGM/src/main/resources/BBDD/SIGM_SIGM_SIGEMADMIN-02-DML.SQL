
SET DEFINE OFF;
Insert into SGM_ADM_ACCIONES
   (ID, NOMBRE, CLASE_CONFIG, CLASE_EXEC, INFO_ADICIONAL)
 Values
   (1, 'Comparar/Importar reglas', 'ieci.tecdoc.sgm.admsistema.action.accionesmultientidad.CompararImportarReglasAccionConfiguracion', 'ieci.tecdoc.sgm.admsistema.proceso.accionmultientidad.CompararImportarReglasAccionEjecucion', NULL);
Insert into SGM_ADM_ACCIONES
   (ID, NOMBRE, CLASE_CONFIG, CLASE_EXEC, INFO_ADICIONAL)
 Values
   (2, 'Importar Procedimiento', 'ieci.tecdoc.sgm.admsistema.action.accionesmultientidad.ImportarProcedimientoAccionConfiguracion', 'ieci.tecdoc.sgm.admsistema.proceso.accionmultientidad.ImportarProcedimientoAccionEjecucion', NULL);
COMMIT;

SET DEFINE OFF;
Insert into SGM_ADM_ENTIDADES
   (ID, NOMBRECORTO, NOMBRELARGO, CODIGO_INE)
 Values
   ('000', 'msssi', 'msssi', NULL);
COMMIT;

SET DEFINE OFF;
Insert into SGM_ADM_PERFILES
   (ID_ENTIDAD, ID_USUARIO, ID_APLICACION)
 Values
   ('000', 'administrador', '1');
Insert into SGM_ADM_PERFILES
   (ID_ENTIDAD, ID_USUARIO, ID_APLICACION)
 Values
   ('000', 'administrador', '2');
Insert into SGM_ADM_PERFILES
   (ID_ENTIDAD, ID_USUARIO, ID_APLICACION)
 Values
   ('000', 'administrador', '3');
Insert into SGM_ADM_PERFILES
   (ID_ENTIDAD, ID_USUARIO, ID_APLICACION)
 Values
   ('000', 'administrador', '4');
Insert into SGM_ADM_PERFILES
   (ID_ENTIDAD, ID_USUARIO, ID_APLICACION)
 Values
   ('000', 'administrador', '5');
Insert into SGM_ADM_PERFILES
   (ID_ENTIDAD, ID_USUARIO, ID_APLICACION)
 Values
   ('000', 'administrador', '6');
Insert into SGM_ADM_PERFILES
   (ID_ENTIDAD, ID_USUARIO, ID_APLICACION)
 Values
   ('000', 'administrador', '7');
Insert into SGM_ADM_PERFILES
   (ID_ENTIDAD, ID_USUARIO, ID_APLICACION)
 Values
   ('000', 'administrador', '8');
COMMIT;

SET DEFINE OFF;
Insert into SGM_ADM_USUARIOS
   (USUARIO, PASSWORD, NOMBRE, APELLIDOS, FECHA_ALTA)
 Values
   ('administrador', '/MIavXzE5N9Up2lyXZE/SQ==', 'Usuario', 'Administrador', TO_DATE('05/06/2008 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));
COMMIT;
