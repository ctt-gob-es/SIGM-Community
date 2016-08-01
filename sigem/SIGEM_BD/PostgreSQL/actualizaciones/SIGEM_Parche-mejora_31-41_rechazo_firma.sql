ALTER TABLE spac_dt_documentos ADD COLUMN motivo_rechazo VARCHAR(255);

UPDATE spac_ct_entidades SET definicion = '<entity version=''1.00''>
<editable>S</editable>
<dropable>N</dropable>
<status>0</status>
<fields>
	<field id=''1''>
		<physicalName>id</physicalName>
		<type>3</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''2''>
		<physicalName>numexp</physicalName>
		<type>0</type>
		<size>30</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''3''>
		<physicalName>fdoc</physicalName>
		<type>6</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''4''>
		<physicalName>nombre</physicalName>
		<type>0</type>
		<size>100</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''5''>
		<physicalName>autor</physicalName>
		<type>0</type>
		<size>250</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''6''>
		<physicalName>id_fase</physicalName>
		<type>3</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''7''>
		<physicalName>id_tramite</physicalName>
		<type>3</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''8''>
		<physicalName>id_tpdoc</physicalName>
		<type>3</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''9''>
		<physicalName>tp_reg</physicalName>
		<type>0</type>
		<size>16</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''10''>
		<physicalName>nreg</physicalName>
		<type>0</type>
		<size>16</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''11''>
		<physicalName>freg</physicalName>
		<type>6</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''12''>
		<physicalName>infopag</physicalName>
		<type>0</type>
		<size>100</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''13''>
		<physicalName>id_fase_pcd</physicalName>
		<type>3</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''14''>
		<physicalName>id_tramite_pcd</physicalName>
		<type>3</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''15''>
		<physicalName>estado</physicalName>
		<type>0</type>
		<size>16</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''16''>
		<physicalName>origen</physicalName>
		<type>0</type>
		<size>128</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''17''>
		<physicalName>descripcion</physicalName>
		<type>0</type>
		<size>250</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''18''>
		<physicalName>origen_id</physicalName>
		<type>0</type>
		<size>20</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''19''>
		<physicalName>destino</physicalName>
		<type>0</type>
		<size>250</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''20''>
		<physicalName>autor_info</physicalName>
		<type>0</type>
		<size>250</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''21''>
		<physicalName>estadofirma</physicalName>
		<type>0</type>
		<size>2</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''22''>
		<physicalName>id_notificacion</physicalName>
		<type>0</type>
		<size>64</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''23''>
		<physicalName>estadonotificacion</physicalName>
		<type>0</type>
		<size>2</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''24''>
		<physicalName>destino_id</physicalName>
		<type>0</type>
		<size>20</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''25''>
		<physicalName>fnotificacion</physicalName>
		<type>6</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''26''>
		<physicalName>faprobacion</physicalName>
		<type>6</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''27''>
		<physicalName>origen_tipo</physicalName>
		<type>0</type>
		<size>1</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''28''>
		<physicalName>destino_tipo</physicalName>
		<type>0</type>
		<size>1</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''29''>
		<physicalName>id_plantilla</physicalName>
		<type>3</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''30''>
		<physicalName>bloqueo</physicalName>
		<type>0</type>
		<size>2</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''31''>
		<physicalName>repositorio</physicalName>
		<type>0</type>
		<size>8</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''32''>
		<physicalName>extension</physicalName>
		<type>0</type>
		<size>64</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''33''>
		<physicalName>ffirma</physicalName>
		<type>6</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''34''>
		<physicalName>infopag_rde</physicalName>
		<type>0</type>
		<size>256</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''35''>
		<physicalName>id_reg_entidad</physicalName>
		<type>3</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''36''>
		<physicalName>id_entidad</physicalName>
		<type>3</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
		</field>
	<field id=''37''>
		<physicalName>extension_rde</physicalName>
		<descripcion><![CDATA[Extensión del documento firmado]]></descripcion>
		<type>0</type>
		<size>64</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''38''>
		<physicalName>cod_cotejo</physicalName>
		<descripcion><![CDATA[Código de cotejo]]></descripcion>
		<type>0</type>
		<size>50</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''39''>
		<physicalName>id_proceso_firma</physicalName>
		<descripcion><![CDATA[Datos identificativos del proceso de firma externo en el que está inmerso el documento]]></descripcion>
		<type>1</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''40''>
		<physicalName>id_circuito</physicalName>
		<descripcion><![CDATA[Identificador del circuito utilizado para enviar al portafirmas]]></descripcion>
		<type>3</type>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''41''>
		<physicalName>hash</physicalName>
		<descripcion><![CDATA[Hash de la función resumen aplicada al contenido del documento]]></descripcion>
		<type>0</type>
		<size>512</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''42''>
		<physicalName>funcion_hash</physicalName>
		<descripcion><![CDATA[Función resumen aplicada para el cálculo del hash del contenido del documento]]></descripcion>
		<type>0</type>
		<size>128</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	<field id=''43''>
		<physicalName>motivo_rechazo</physicalName>
		<descripcion>
			<![CDATA[Motivo de rechazo de la firma del documento]]>
		</descripcion>
		<type>0</type>
		<size>255</size>
		<nullable>S</nullable>
		<documentarySearch>N</documentarySearch>
		<multivalue>N</multivalue>
	</field>
	</fields>
	<validations>
		<validation id=''1''>
			<fieldId>23</fieldId>
			<table>SPAC_TBL_006</table>
			<tableType>3</tableType>
			<class/>
			<mandatory>N</mandatory>
		</validation>
		<validation id=''2''>
			<fieldId>21</fieldId>
			<table>SPAC_TBL_008</table>
			<tableType>3</tableType>
			<class/>
			<mandatory>N</mandatory>
		</validation>
	</validations>
</entity>'
WHERE id = 2;