CREATE TABLE spac_s_sesion_mensaje(
  id integer NOT NULL,
  numexp varchar2(30) NOT NULL,
  id_sesion varchar2(200),
  id_mensaje varchar2(200),
  usuario varchar2(200),
  CONSTRAINT pk_2081913613 PRIMARY KEY (id)
);

CREATE INDEX ind_2081913613 ON spac_s_sesion_mensaje (numexp);



insert into spac_ct_entidades (id,tipo,nombre,campo_pk,campo_numexp,schema_expr,descripcion,sec_pk,fecha)
VALUES( NEXTVAL('SPAC_SQ_ID_CTENTIDADES'),'1','SPAC_S_SESION_MENSAJE','ID','NUMEXP','NUMEXP','','SPAC_SQ_2081913613',SYSDATE);


CREATE SEQUENCE SPAC_SQ_2081913613 
  INCREMENT by 1 
  MINVALUE 1 
  MAXVALUE 9223372036854775807 
  START with 1; 

DECLARE

datoDefinicion clob;
datoFrm_jsp clob;

begin

datoDefinicion := '<entity version=''1.00''><editable>S</editable><dropable>S</dropable><status>0</status><fields><field id=''1''><physicalName>id</physicalName><descripcion><![CDATA[Campos Clave de la entidad (Uso interno del sistema)]]></descripcion><type>3</type><nullable>N</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''2''><physicalName>numexp</physicalName><descripcion><![CDATA[Campo que relaciona la entidad con un número de expediente (Uso interno del sistema)]]></descripcion><type>0</type><size>30</size><nullable>N</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''3''><physicalName>ID_SESION</physicalName><type>0</type><size>200</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''4''><physicalName>ID_MENSAJE</physicalName><type>0</type><size>200</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''5''><physicalName>USUARIO</physicalName><type>0</type><size>200</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field></fields><indexes><index id=''5''><name><![CDATA[IND_2081913613]]></name><fields><field idField=''2''/></fields></index></indexes></entity>';
datoFrm_jsp := '<div id="dataBlock_SPAC_S_SESION_MENSAJE" style="position: relative; height: 85px; width: 600px"><div id="label_SPAC_S_SESION_MENSAJE:ID_SESION" style="position: absolute; top: 10px; left: 10px; width: 110px;" class="formsTitleB"><bean:write name="defaultForm" property="entityApp.label(SPAC_S_SESION_MENSAJE:ID_SESION)" />:</div><div id="data_SPAC_S_SESION_MENSAJE:ID_SESION" style="position: absolute; top: 10px; left: 130px; width:100% ;" ><ispac:htmlText property="property(SPAC_S_SESION_MENSAJE:ID_SESION)" readonly="false" propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" size="80" maxlength="200" ></ispac:htmlText></div><div id="label_SPAC_S_SESION_MENSAJE:ID_MENSAJE" style="position: absolute; top: 35px; left: 10px; width: 110px;" class="formsTitleB"><bean:write name="defaultForm" property="entityApp.label(SPAC_S_SESION_MENSAJE:ID_MENSAJE)" />:</div><div id="data_SPAC_S_SESION_MENSAJE:ID_MENSAJE" style="position: absolute; top: 35px; left: 130px; width:100% ;" ><ispac:htmlText property="property(SPAC_S_SESION_MENSAJE:ID_MENSAJE)" readonly="false" propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" size="80" maxlength="200" ></ispac:htmlText></div></div>';

update spac_ct_entidades set definicion = datoDefinicion, frm_jsp = datoFrm_jsp  where nombre = 'SPAC_S_SESION_MENSAJE';

end;