alter table sgm_adm_entidades add password_entidad varchar2(50 CHAR);
alter table sgm_adm_entidades add cif varchar2(12 CHAR);
alter table sgm_adm_entidades add dir3 varchar2(12 CHAR);
alter table sgm_adm_entidades add sia varchar2(12 CHAR);
alter table sgm_adm_entidades add deh varchar2(12 CHAR);

-- Actualizar los campos CIF, DIR3, SIA y DEH con los valores propios del organismo de cada entidad.
-- Por ejemplo, el campo DIR3 se utiliza para la creación del metadato identificador de documento.
update sgm_adm_entidades
set cif = 'xxx', dir3 = 'xxx';
