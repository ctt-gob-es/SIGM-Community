CREATE TABLE dpcr_acuse_email
(
  id integer NOT NULL,
  numexp  varchar2(30) NOT NULL,
  nombre_notif varchar2(128),
  fecha_envio timestamp,
  nombre_doc varchar2(128),
  enviado numeric(1,0),
  email varchar2(100),
  descripcion_error varchar2(255),
  descripcion_documento varchar2(255),
  CONSTRAINT pk_346066818 PRIMARY KEY (id)
);


CREATE INDEX ind_346066818 ON dpcr_acuse_email (numexp);


insert into spac_ct_entidades (id,tipo,nombre,campo_pk,campo_numexp,schema_expr,descripcion,sec_pk,fecha)
VALUES( NEXTVAL('SPAC_SQ_ID_CTENTIDADES'),'1','DPCR_ACUSE_EMAIL','ID','NUMEXP','NUMEXP','','SPAC_SQ_346066818', NOW());


CREATE SEQUENCE SPAC_SQ_346066818 
  INCREMENT by 1 
  MINVALUE 1 
  MAXVALUE 9223372036854775807 
  START with 1; 

insert into spac_ct_informes (id, nombre, descripcion, xml, fecha, tipo, visibilidad) values
(NEXTVAL('spac_sq_id_ctinformes'), 'Acuse Email','Informe de los correos electrónicos enviados.','',current_timestamp,1,1);

DECLARE
datoXml clob;

datoDefinicion clob;
datoFrm_jsp clob;

BEGIN
datoXml := '<?xml version="1.0" encoding="UTF-8"  ?>
<!-- Created with iReport - A designer for JasperReports -->
<!DOCTYPE jasperReport PUBLIC "//JasperReports//DTD Report Design//EN" "http://jasperreports.sourceforge.net/dtds/jasperreport.dtd">
<jasperReport
		 name="AcusesNotificadosEmail"
		 columnCount="1"
		 printOrder="Vertical"
		 orientation="Portrait"
		 pageWidth="595"
		 pageHeight="842"
		 columnWidth="535"
		 columnSpacing="0"
		 leftMargin="30"
		 rightMargin="30"
		 topMargin="20"
		 bottomMargin="20"
		 whenNoDataType="NoPages"
		 isTitleNewPage="false"
		 isSummaryNewPage="false">
	<property name="ireport.scriptlethandling" value="0" />
	<property name="ireport.encoding" value="UTF-8" />
	<import value="java.util.*" />
	<import value="net.sf.jasperreports.engine.*" />
	<import value="net.sf.jasperreports.engine.data.*" />

	<parameter name="NUM_EXP" isForPrompting="true" class="java.lang.String">
		<defaultValueExpression ><![CDATA["DPCR2010/6606"]]></defaultValueExpression>
	</parameter>
	<parameter name="IMAGES_REPOSITORY_PATH" isForPrompting="false" class="java.lang.String">
		<defaultValueExpression ><![CDATA["K:\\Documentos\\ImÃ¡genes"]]></defaultValueExpression>
	</parameter>
	<queryString><![CDATA[select numexp, nombre_notif, fecha_envio, nombre_doc, enviado, email, 
descripcion_error, descripcion_documento 
from dpcr_acuse_email 
where numexp = $P{NUM_EXP}
order by nombre_doc]]></queryString>

	<field name="numexp" class="java.lang.String"/>
	<field name="nombre_notif" class="java.lang.String"/>
	<field name="fecha_envio" class="java.sql.Timestamp"/>
	<field name="nombre_doc" class="java.lang.String"/>
	<field name="enviado" class="java.math.BigDecimal"/>
	<field name="email" class="java.lang.String"/>
	<field name="descripcion_error" class="java.lang.String"/>
	<field name="descripcion_documento" class="java.lang.String"/>


		<group  name="Documento" isStartNewPage="true" isReprintHeaderOnEachPage="true" >
			<groupExpression><![CDATA[$F{nombre_doc}]]></groupExpression>
			<groupHeader>
			<band height="18"  isSplitAllowed="true" >
				<textField isStretchWithOverflow="false" isBlankWhenNull="false" evaluationTime="Now" hyperlinkType="None"  hyperlinkTarget="Self" >
					<reportElement
						x="0"
						y="0"
						width="535"
						height="18"
						key="textField"/>
					<box topBorder="1Point" topBorderColor="#000000" leftBorder="1Point" leftBorderColor="#000000" rightBorder="1Point" rightBorderColor="#000000" bottomBorder="1Point" bottomBorderColor="#000000"/>
					<textElement verticalAlignment="Middle">
						<font fontName="Arial" pdfFontName="Helvetica-Bold" size="12" isBold="true"/>
					</textElement>
				<textFieldExpression   class="java.lang.String"><![CDATA["  Documento: "+$F{nombre_doc}]]></textFieldExpression>
				</textField>
			</band>
			</groupHeader>
			<groupFooter>
			<band height="9"  isSplitAllowed="true" >
			</band>
			</groupFooter>
		</group>
		<background>
			<band height="0"  isSplitAllowed="true" >
			</band>
		</background>
		<title>
			<band height="6"  isSplitAllowed="true" >
			</band>
		</title>
		<pageHeader>
			<band height="127"  isSplitAllowed="true" >
				<textField isStretchWithOverflow="false" isBlankWhenNull="false" evaluationTime="Now" hyperlinkType="None"  hyperlinkTarget="Self" >
					<reportElement
						x="249"
						y="99"
						width="249"
						height="18"
						key="textField"/>
					<box topBorder="None" topBorderColor="#000000" leftBorder="None" leftBorderColor="#000000" rightBorder="None" rightBorderColor="#000000" bottomBorder="None" bottomBorderColor="#000000"/>
					<textElement verticalAlignment="Middle">
						<font fontName="Arial" pdfFontName="Helvetica-Bold" size="12" isBold="true"/>
					</textElement>
				<textFieldExpression   class="java.lang.String"><![CDATA["NÂº Expediente: "+$P{NUM_EXP}]]></textFieldExpression>
				</textField>
				<image  evaluationTime="Now" hyperlinkType="None"  hyperlinkTarget="Self" >
					<reportElement
						x="4"
						y="6"
						width="161"
						height="104"
						key="image-1"/>
					<box topBorder="None" topBorderColor="#000000" leftBorder="None" leftBorderColor="#000000" rightBorder="None" rightBorderColor="#000000" bottomBorder="None" bottomBorderColor="#000000"/>
					<graphicElement stretchType="NoStretch"/>
					<imageExpression class="java.lang.String"><![CDATA[$P{IMAGES_REPOSITORY_PATH}+"/logoCabecera.gif"]]></imageExpression>
				</image>
				<textField isStretchWithOverflow="false" isBlankWhenNull="false" evaluationTime="Now" hyperlinkType="None"  hyperlinkTarget="Self" >
					<reportElement
						mode="Opaque"
						x="168"
						y="6"
						width="367"
						height="84"
						forecolor="#000000"
						backcolor="#CCCCCC"
						key="textField-1"/>
					<box topBorder="None" topBorderColor="#000000" leftBorder="None" leftBorderColor="#000000" rightBorder="None" rightBorderColor="#000000" bottomBorder="None" bottomBorderColor="#000000"/>
					<textElement textAlignment="Center" verticalAlignment="Middle">
						<font fontName="Arial" pdfFontName="Helvetica-Bold" size="14" isBold="true"/>
					</textElement>
				<textFieldExpression   class="java.lang.String"><![CDATA["Acuses de recibos de notificaciones electrÃ³nicas"]]></textFieldExpression>
				</textField>
			</band>
		</pageHeader>
		<columnHeader>
			<band height="8"  isSplitAllowed="true" >
			</band>
		</columnHeader>
		<detail>
			<band height="58"  isSplitAllowed="true" >
				<textField isStretchWithOverflow="false" isBlankWhenNull="false" evaluationTime="Now" hyperlinkType="None"  hyperlinkTarget="Self" >
					<reportElement
						x="0"
						y="22"
						width="319"
						height="18"
						key="textField"/>
					<box topBorder="1Point" topBorderColor="#000000" leftBorder="1Point" leftBorderColor="#000000" rightBorder="1Point" rightBorderColor="#000000" bottomBorder="1Point" bottomBorderColor="#000000"/>
					<textElement verticalAlignment="Middle">
						<font fontName="Arial"/>
					</textElement>
				<textFieldExpression   class="java.lang.String"><![CDATA[" Notificado: "+$F{nombre_notif}]]></textFieldExpression>
				</textField>
				<textField isStretchWithOverflow="false" isBlankWhenNull="false" evaluationTime="Now" hyperlinkType="None"  hyperlinkTarget="Self" >
					<reportElement
						x="4"
						y="40"
						width="107"
						height="18"
						key="textField"/>
					<box topBorder="None" topBorderColor="#000000" leftBorder="None" leftBorderColor="#000000" rightBorder="None" rightBorderColor="#000000" bottomBorder="None" bottomBorderColor="#000000"/>
					<textElement verticalAlignment="Middle">
						<font fontName="Arial"/>
					</textElement>
				<textFieldExpression   class="java.sql.Timestamp"><![CDATA[$F{fecha_envio}]]></textFieldExpression>
				</textField>
				<textField isStretchWithOverflow="false" pattern="" isBlankWhenNull="false" evaluationTime="Now" hyperlinkType="None"  hyperlinkTarget="Self" >
					<reportElement
						x="111"
						y="40"
						width="106"
						height="18"
						key="textField"/>
					<box topBorder="1Point" topBorderColor="#000000" leftBorder="1Point" leftBorderColor="#000000" rightBorder="1Point" rightBorderColor="#000000" bottomBorder="1Point" bottomBorderColor="#000000"/>
					<textElement verticalAlignment="Middle">
						<font fontName="Arial"/>
					</textElement>
				<textFieldExpression   class="java.lang.String"><![CDATA[($F{enviado}.intValue()==0)?" Error en el envÃ­o":" EnvÃ­o correcto"]]></textFieldExpression>
				</textField>
				<textField isStretchWithOverflow="false" isBlankWhenNull="true" evaluationTime="Now" hyperlinkType="None"  hyperlinkTarget="Self" >
					<reportElement
						x="319"
						y="22"
						width="216"
						height="18"
						key="textField"/>
					<box topBorder="1Point" topBorderColor="#000000" leftBorder="1Point" leftBorderColor="#000000" rightBorder="1Point" rightBorderColor="#000000" bottomBorder="1Point" bottomBorderColor="#000000"/>
					<textElement verticalAlignment="Middle">
						<font fontName="Arial"/>
					</textElement>
				<textFieldExpression   class="java.lang.String"><![CDATA[$F{email}==null || $F{email}.equals("")?"":" "+$F{email}]]></textFieldExpression>
				</textField>
				<textField isStretchWithOverflow="false" isBlankWhenNull="true" evaluationTime="Now" hyperlinkType="None"  hyperlinkTarget="Self" >
					<reportElement
						x="217"
						y="40"
						width="318"
						height="18"
						key="textField"/>
					<box topBorder="1Point" topBorderColor="#000000" leftBorder="1Point" leftBorderColor="#000000" rightBorder="1Point" rightBorderColor="#000000" bottomBorder="1Point" bottomBorderColor="#000000"/>
					<textElement verticalAlignment="Middle">
						<font fontName="Arial"/>
					</textElement>
				<textFieldExpression   class="java.lang.String"><![CDATA[$F{descripcion_error}==null || $F{descripcion_error}.equals("")?"":" "+$F{descripcion_error}]]></textFieldExpression>
				</textField>
				<rectangle>
					<reportElement
						x="0"
						y="0"
						width="535"
						height="4"
						backcolor="#66FFFF"
						key="rectangle-1"/>
					<graphicElement stretchType="NoStretch"/>
				</rectangle>
				<textField isStretchWithOverflow="false" isBlankWhenNull="false" evaluationTime="Now" hyperlinkType="None"  hyperlinkTarget="Self" >
					<reportElement
						x="0"
						y="4"
						width="535"
						height="18"
						key="textField-4"/>
					<box topBorder="1Point" topBorderColor="#000000" leftBorder="1Point" leftBorderColor="#000000" rightBorder="1Point" rightBorderColor="#000000" bottomBorder="1Point" bottomBorderColor="#000000"/>
					<textElement verticalAlignment="Middle">
						<font fontName="Arial"/>
					</textElement>
				<textFieldExpression   class="java.lang.String"><![CDATA[" DescripciÃ³n: "+(($F{descripcion_documento}==null)?"":$F{descripcion_documento})]]></textFieldExpression>
				</textField>
				<rectangle>
					<reportElement
						mode="Transparent"
						x="0"
						y="40"
						width="111"
						height="18"
						key="rectangle-2"/>
					<graphicElement stretchType="NoStretch"/>
				</rectangle>
			</band>
		</detail>
		<columnFooter>
			<band height="10"  isSplitAllowed="true" >
			</band>
		</columnFooter>
		<pageFooter>
			<band height="31"  isSplitAllowed="true" >
				<image  evaluationTime="Now" hyperlinkType="None"  hyperlinkTarget="Self" >
					<reportElement
						x="0"
						y="0"
						width="535"
						height="31"
						key="image-2"/>
					<box topBorder="None" topBorderColor="#000000" leftBorder="None" leftBorderColor="#000000" rightBorder="None" rightBorderColor="#000000" bottomBorder="None" bottomBorderColor="#000000"/>
					<graphicElement stretchType="NoStretch"/>
					<imageExpression class="java.lang.String"><![CDATA[$P{IMAGES_REPOSITORY_PATH}+"/logoPie.jpg"]]></imageExpression>
				</image>
				<textField isStretchWithOverflow="false" isBlankWhenNull="false" evaluationTime="Now" hyperlinkType="None"  hyperlinkTarget="Self" >
					<reportElement
						x="482"
						y="5"
						width="28"
						height="15"
						forecolor="#FFFFFF"
						key="textField-2"/>
					<box topBorder="None" topBorderColor="#000000" leftBorder="None" leftBorderColor="#000000" rightBorder="None" rightBorderColor="#000000" bottomBorder="None" bottomBorderColor="#000000"/>
					<textElement textAlignment="Center" verticalAlignment="Middle">
						<font fontName="Calibri" pdfFontName="Helvetica-Bold" size="11" isBold="true"/>
					</textElement>
				<textFieldExpression   class="java.lang.Integer"><![CDATA[$V{PAGE_NUMBER}]]></textFieldExpression>
				</textField>
			</band>
		</pageFooter>
		<lastPageFooter>
			<band height="31"  isSplitAllowed="true" >
				<image  evaluationTime="Now" hyperlinkType="None"  hyperlinkTarget="Self" >
					<reportElement
						x="0"
						y="0"
						width="535"
						height="31"
						key="image-3"/>
					<box topBorder="None" topBorderColor="#000000" leftBorder="None" leftBorderColor="#000000" rightBorder="None" rightBorderColor="#000000" bottomBorder="None" bottomBorderColor="#000000"/>
					<graphicElement stretchType="NoStretch"/>
					<imageExpression class="java.lang.String"><![CDATA[$P{IMAGES_REPOSITORY_PATH}+"/logoPie.jpg"]]></imageExpression>
				</image>
				<textField isStretchWithOverflow="false" isBlankWhenNull="false" evaluationTime="Now" hyperlinkType="None"  hyperlinkTarget="Self" >
					<reportElement
						x="482"
						y="6"
						width="28"
						height="15"
						forecolor="#FFFFFF"
						key="textField-3"/>
					<box topBorder="None" topBorderColor="#000000" leftBorder="None" leftBorderColor="#000000" rightBorder="None" rightBorderColor="#000000" bottomBorder="None" bottomBorderColor="#000000"/>
					<textElement textAlignment="Center" verticalAlignment="Middle">
						<font fontName="Calibri" pdfFontName="Helvetica-Bold" size="11" isBold="true"/>
					</textElement>
				<textFieldExpression   class="java.lang.Integer"><![CDATA[$V{PAGE_NUMBER}]]></textFieldExpression>
				</textField>
			</band>
		</lastPageFooter>
		<summary>
			<band height="9"  isSplitAllowed="true" >
			</band>
		</summary>
</jasperReport>';

datoDefinicion := '<entity version=''1.00''><editable>S</editable><dropable>S</dropable><status>0</status><fields><field id=''1''><physicalName>id</physicalName><descripcion><![CDATA[Campos Clave de la entidad (Uso interno del sistema)]]></descripcion><type>3</type><nullable>N</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''2''><physicalName>numexp</physicalName><descripcion><![CDATA[Campo que relaciona la entidad con un número de expediente (Uso interno del sistema)]]></descripcion><type>0</type><size>30</size><nullable>N</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''3''><physicalName>nombre_notif</physicalName><type>0</type><size>128</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''4''><physicalName>fecha_envio</physicalName><type>6</type><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''5''><physicalName>nombre_doc</physicalName><type>0</type><size>128</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''6''><physicalName>enviado</physicalName><type>2</type><size>1</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''7''><physicalName>email</physicalName><type>11</type><size>100</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''8''><physicalName>descripcion_error</physicalName><type>0</type><size>255</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field><field id=''9''><physicalName>descripcion_documento</physicalName><descripcion><![CDATA[Descripción del documento que se envía]]></descripcion><type>0</type><size>255</size><nullable>S</nullable><documentarySearch>N</documentarySearch><multivalue>N</multivalue></field></fields><indexes><index id=''9''><name><![CDATA[IND_346066818]]></name><fields><field idField=''2''/></fields></index></indexes></entity>';
datoFrm_jsp := '<div id="dataBlock_DPCR_ACUSE_EMAIL" style="position: relative; height: 185px; width: 600px"><div id="label_DPCR_ACUSE_EMAIL:NOMBRE_NOTIF" style="position: absolute; top: 10px; left: 10px; width: 110px;" class="formsTitleB"><bean:write name="defaultForm" property="entityApp.label(DPCR_ACUSE_EMAIL:NOMBRE_NOTIF)" />:</div><div id="data_DPCR_ACUSE_EMAIL:NOMBRE_NOTIF" style="position: absolute; top: 10px; left: 130px; width:100% ;" ><ispac:htmlText property="property(DPCR_ACUSE_EMAIL:NOMBRE_NOTIF)" readonly="false" propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" size="80" maxlength="128" ></ispac:htmlText></div><div id="label_DPCR_ACUSE_EMAIL:FECHA_ENVIO" style="position: absolute; top: 35px; left: 10px; width: 110px;" class="formsTitleB"><bean:write name="defaultForm" property="entityApp.label(DPCR_ACUSE_EMAIL:FECHA_ENVIO)" />:</div><div id="data_DPCR_ACUSE_EMAIL:FECHA_ENVIO" style="position: absolute; top: 35px; left: 130px; width:100% ;" ><nobr><ispac:htmlTextCalendar property="property(DPCR_ACUSE_EMAIL:FECHA_ENVIO)" readonly="false" propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" size="14" maxlength="10" image=''<%= buttoncalendar %>'' format="dd/mm/yyyy" enablePast="true" ></ispac:htmlTextCalendar></nobr></div><div id="label_DPCR_ACUSE_EMAIL:NOMBRE_DOC" style="position: absolute; top: 60px; left: 10px; width: 110px;" class="formsTitleB"><bean:write name="defaultForm" property="entityApp.label(DPCR_ACUSE_EMAIL:NOMBRE_DOC)" />:</div><div id="data_DPCR_ACUSE_EMAIL:NOMBRE_DOC" style="position: absolute; top: 60px; left: 130px; width:100% ;" ><ispac:htmlText property="property(DPCR_ACUSE_EMAIL:NOMBRE_DOC)" readonly="false" propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" size="80" maxlength="128" ></ispac:htmlText></div><div id="label_DPCR_ACUSE_EMAIL:ENVIADO" style="position: absolute; top: 85px; left: 10px; width: 110px;" class="formsTitleB"><bean:write name="defaultForm" property="entityApp.label(DPCR_ACUSE_EMAIL:ENVIADO)" />:</div><div id="data_DPCR_ACUSE_EMAIL:ENVIADO" style="position: absolute; top: 85px; left: 130px; width:100% ;" ><ispac:htmlText property="property(DPCR_ACUSE_EMAIL:ENVIADO)" readonly="false" propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" size="80" maxlength="1" ></ispac:htmlText></div><div id="label_DPCR_ACUSE_EMAIL:EMAIL" style="position: absolute; top: 110px; left: 10px; width: 110px;" class="formsTitleB"><bean:write name="defaultForm" property="entityApp.label(DPCR_ACUSE_EMAIL:EMAIL)" />:</div><div id="data_DPCR_ACUSE_EMAIL:EMAIL" style="position: absolute; top: 110px; left: 130px; width:100% ;" ><ispac:htmlText property="property(DPCR_ACUSE_EMAIL:EMAIL)" readonly="false" propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" size="80" maxlength="100" ></ispac:htmlText></div><div id="label_DPCR_ACUSE_EMAIL:DESCRIPCION_ERROR" style="position: absolute; top: 135px; left: 10px; width: 110px;" class="formsTitleB"><bean:write name="defaultForm" property="entityApp.label(DPCR_ACUSE_EMAIL:DESCRIPCION_ERROR)" />:</div><div id="data_DPCR_ACUSE_EMAIL:DESCRIPCION_ERROR" style="position: absolute; top: 135px; left: 130px; width:100% ;" ><ispac:htmlText property="property(DPCR_ACUSE_EMAIL:DESCRIPCION_ERROR)" readonly="false" propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" size="80" maxlength="255" ></ispac:htmlText></div></div>';


update spac_ct_informes set xml = datoXml  where nombre = 'Acuse Email';

update spac_ct_entidades set definicion = datoDefinicion, frm_jsp = datoFrm_jsp  where nombre = 'DPCR_ACUSE_EMAIL';

END;
