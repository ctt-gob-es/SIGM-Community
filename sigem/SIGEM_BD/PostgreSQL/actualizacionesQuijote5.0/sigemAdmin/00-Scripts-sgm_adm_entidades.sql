alter table sgm_adm_entidades add column password_entidad varchar(50);
alter table sgm_adm_entidades add column cif varchar(12);
alter table sgm_adm_entidades add column dir3 varchar(12);
alter table sgm_adm_entidades add column sia varchar(12);
alter table sgm_adm_entidades add column deh varchar(12);

-- Actualizar los campos CIF, DIR3, SIA y DEH con los valores propios del organismo de cada entidad.
-- Por ejemplo, el campo DIR3 se utiliza para la creación del metadato identificador de documento.
update sgm_adm_entidades
set cif = 'xxx', dir3 = 'xxx';
